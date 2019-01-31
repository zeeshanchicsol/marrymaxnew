/*
 * Copyright (C) 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chicsol.marrymax.adapters;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.dialogs.dialogAddNotes;
import com.chicsol.marrymax.dialogs.dialogRemoveFromSearch;
import com.chicsol.marrymax.dialogs.dialogRequest;
import com.chicsol.marrymax.dialogs.dialogRequestPhone;
import com.chicsol.marrymax.dialogs.dialogShowInterest;
import com.chicsol.marrymax.interfaces.MatchesRefreshCallBackInterface;
import com.chicsol.marrymax.interfaces.PhoneRequestCallBackInterface;
import com.chicsol.marrymax.interfaces.RemoveMember;
import com.chicsol.marrymax.interfaces.RequestCallbackInterface;
import com.chicsol.marrymax.interfaces.UpdateMatchesCountCallback;
import com.chicsol.marrymax.interfaces.UpdateMemberFromDialogFragment;
import com.chicsol.marrymax.interfaces.WithdrawRequestCallBackInterface;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.functions;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapterMyMatches extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements dialogShowInterest.onCompleteListener, dialogRequestPhone.onCompleteListener, UpdateMemberFromDialogFragment, RemoveMember, RequestCallbackInterface, PhoneRequestCallBackInterface, WithdrawRequestCallBackInterface {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public ImageLoader imageLoader;
    // public OnItemClickListener onItemClickListener;
    public OnLoadMoreListener onLoadMoreListener;
    Context context;
    FragmentManager frgMngr;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private DisplayImageOptions options, optionsNormalImage;
    private LayoutInflater inflater;
    private List<Members> items;
    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private Fragment fragment;
    private int height = 0;
    MarryMax marryMax;
    private int selectedPosition;
    private UpdateMatchesCountCallback updateMatchesCountCallback;
    private MatchesRefreshCallBackInterface matchesRefreshCallBackInterface;
    private RecyclerView recyclerView;

    public Members getMemResultsObj() {
        return memResultsObj;
    }

    public void setMemResultsObj(Members memResultsObj) {
        this.memResultsObj = memResultsObj;
    }

    private Members memResultsObj;

    String TAG = null;

    public RecyclerViewAdapterMyMatches(final Context context, FragmentManager frg, OnLoadMoreListener onLoadMoreListener, Fragment fragment, UpdateMatchesCountCallback updateMatchesCountCallback, MatchesRefreshCallBackInterface matchesRefreshCallBackInterface, String TAG) {
        //this.items = items;
        Activity activity = (Activity) context;
        this.TAG = TAG;
        this.matchesRefreshCallBackInterface = matchesRefreshCallBackInterface;
        this.updateMatchesCountCallback = updateMatchesCountCallback;

        marryMax = new MarryMax(activity);
        marryMax.setWithdrawRequestCallBackInterface(RecyclerViewAdapterMyMatches.this);
        marryMax.setPhoneViewRequestInterface(RecyclerViewAdapterMyMatches.this);
        marryMax.setWithdrawRequestCallBackInterface(RecyclerViewAdapterMyMatches.this);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int heightScreen = metrics.heightPixels;
        height = (int) (heightScreen * 0.13);
        this.fragment = fragment;
        this.context = context;
        this.frgMngr = frg;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                //   .showImageOnLoading(resize(R.drawable.loading_sm))
                // .showImageOnLoading(resize(R.drawable.loading))
                // .showImageForEmptyUri(resize(R.drawable.oops_sm))
                // .showImageForEmptyUri(resize(R.drawable.no_image))
                //.showImageOnFail(resize(R.drawable.oops_sm))
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)

                .postProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bmp) {

                        Bitmap bmp_sticker;
                        //    Display display =context.getApplicationContext().getWindowManager().getDefaultDisplay();
                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

                        //  display.getMetrics(metrics);

                        int widthScreen = metrics.widthPixels;
                        int heightScreen = metrics.heightPixels;
                        if (widthScreen > heightScreen) {
                            int h = (int) (heightScreen * 0.046);//it set the height of image 10% of your screen

                            bmp_sticker = resizeImage(bmp, h);
                        } else {
                            int h = (int) (heightScreen * 0.13);//it set the height of image 10% of your screen


                            bmp_sticker = resizeImageToHeight(bmp, h);

                        }

                        return bmp_sticker;
                    }
                }).build();
        optionsNormalImage = new DisplayImageOptions.Builder()
                //   .showImageOnLoading(resize(R.drawable.loading_sm))
                // .showImageOnLoading(resize(R.drawable.loading))
                // .showImageForEmptyUri(resize(R.drawable.oops_sm))
                // .showImageForEmptyUri(resize(R.drawable.no_image))
                //.showImageOnFail(resize(R.drawable.oops_sm))
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)

                .postProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bmp) {

                        Bitmap bmp_sticker;
                        //  Display display =getWindowManager().getDefaultDisplay();
                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();


                        // display.getMetrics(metrics);

                        int widthScreen = metrics.widthPixels;
                        int heightScreen = metrics.heightPixels;
                        if (widthScreen > heightScreen) {
                            int h = (int) (heightScreen * 0.046);//it set the height of image 10% of your screen
                            //     iv.getLayoutParams().width = (int) (widthScreen * 0.10);
                            Log.e("wid " + widthScreen + "  " + heightScreen, "");
                            bmp_sticker = resizeImage(bmp, h);
                        } else {
                            int h = (int) (heightScreen * 0.027);//it set the height of image 10% of your screen
                            //   iv.getLayoutParams().width = (int) (widthScreen * 0.15);
                            bmp_sticker = resizeImage(bmp, h);
                            Log.e("wid " + widthScreen + "  " + heightScreen, "");
                        }

                        return bmp_sticker;
                    }
                }).build();
        this.onLoadMoreListener = onLoadMoreListener;
        items = new ArrayList<>();
    }


    public void setRecyclerView(RecyclerView mView) {
        recyclerView = mView;
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

                //      Log.e("zxzxzx "+isMoreLoading,"totalItemCount: "+totalItemCount+"  visibleItemCount : "+visibleItemCount+"");
                if (!isMoreLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isMoreLoading = true;
                }
            }
        });
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;


    }


    private Bitmap resizeImageToHeight(final Bitmap image, int maxHeight) {
        Bitmap resizedImage = null;
        if (image != null) {
            //  int maxHeight = 80; //actual image height coming from internet
            int maxWidth = 300; //actual image width coming from internet

            int imageHeight = image.getHeight();
            if (imageHeight > maxHeight)
                imageHeight = maxHeight;
            int imageWidth = (imageHeight * image.getWidth()) / image.getHeight();

            /*if (imageWidth > maxWidth) {
                imageWidth = maxWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }*/
            resizedImage = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);
        }
        return resizedImage;
    }


    private Bitmap resizeImage(final Bitmap image, int maxHeight) {
        Bitmap resizedImage = null;
        if (image != null) {
            //  int maxHeight = 80; //actual image height coming from internet
            int maxWidth = 300; //actual image width coming from internet

            int imageHeight = image.getHeight();
            if (imageHeight > maxHeight)
                imageHeight = maxHeight;
            int imageWidth = (imageHeight * image.getWidth()) / image.getHeight();
            if (imageWidth > maxWidth) {
                imageWidth = maxWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }
            resizedImage = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);
        }
        return resizedImage;
    }

 /*   public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_member_dash_main_mymatches, parent, false);
        //   v.setOnClickListener(this);
*//*
        int w = parent.getMeasuredWidth() / 2;
        // Log.e("wwww",w+"");
        v.setMinimumWidth(w - 20);*//*
        return new ViewHolder(v);*/

        if (viewType == VIEW_ITEM) {
            return new MMViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_member_dash_main_mymatches, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false));
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {
        final Members member = items.get(position);


        if (holder1 instanceof MMViewHolder) {
            final MMViewHolder holder = ((MMViewHolder) holder1);

            final View view = holder.tvBtMenu;
            final PopupMenu popup = new PopupMenu(context, view);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.menu_mymatches, popup.getMenu());


            holder.alias.setText(member.getAlias());
            holder.age.setText("( " + member.get_age() + " Years)");
            holder.tvAboutMe.setText(Html.fromHtml("<b>About: </b>" + member.get_about_member()));

            holder.pref1.setText(member.get_pref1() + ": ");
            holder.pref2.setText(member.get_pref2() + ": ");
            holder.pref3.setText(member.get_pref3() + ": ");
            holder.pref4.setText(member.get_pref4() + ": ");

            holder.prefValue1.setText(member.get_prefvalue1());
            holder.prefValue2.setText(member.get_prefvalue2());
            holder.prefValue3.setText(member.get_prefvalue3());
            holder.prefValue4.setText(member.get_prefvalue4());

            if (member.get_photo_count() > 0) {
                holder.imageCount.setText(member.get_photo_count() + "");

            } else {
                holder.ll_image_count.setVisibility(View.GONE);
            }
//========Phone View========================
            if (member.get_phone_view() == 2 || member.get_phone_privilege_id() > 0) {
                //see mobile  green
                holder.ivViewPhone.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_num_verified_icon_60));
            } else if (member.get_phone_view() == 1 && member.get_phone_privilege_id() == 0) {
                //orange
                holder.ivViewPhone.setImageDrawable(context.getResources().getDrawable(R.drawable.num_not_verified_icon_60));
            } else if (member.get_phone_view() != 2 && member.get_phone_view() != 1 && member.get_phone_privilege_id() == 0) {
                //grey
                holder.ivViewPhone.setImageDrawable(context.getResources().getDrawable(R.drawable.no_number_icon_60));
            }
            /*else if (member.get_phone_view() == 0 && member.get_profile_privilege_id() == 0) {
                //grey
                holder.ivViewPhone.setImageDrawable(context.getResources().getDrawable(R.drawable.no_number_icon_60));
            }*/


      /*    hidden_status==1 && profile_privilege_id==0  Request Profile
            profile_request_id>0 withdraw : (above option)
//      */


            //    Log.e("get_profil:  " + member.getAlias() + "   = " + member.get_hidden_status() + "  " + member.get_profile_privilege_id(), "" + member.get_profile_request_id());

            holder.tvBtMenu.setVisibility(View.VISIBLE);

            if (member.get_hidden_status() == 1 && member.get_profile_privilege_id() == 0) {


                if (member.get_profile_request_id() > 0) {

                    popup.getMenu().getItem(0).setTitle("Withdraw Request");

                } else {
                    popup.getMenu().getItem(0).setTitle("Request Profile");
                }
            } else if (member.get_hidden_status() == 0 && member.get_photo_privilege_id() == 0) {

                if (member.get_photo_count() == 0 && member.get_blur() == 0) {
                    if (member.get_photo_upload_request_id() == 0) {
                        //request  photo view
                        popup.getMenu().getItem(0).setTitle("Request Photo");
                    } else if (member.get_photo_upload_request_id() > 0) {
                        popup.getMenu().getItem(0).setTitle("Withdraw Request");
                    }


                } else if ((member.get_photo_count() > 0 || member.get_blur() == 1) && member.get_hide_photo() > 0) {
                    if (member.get_photo_request_id() == 0) {
                        //request  photo view
                        popup.getMenu().getItem(0).setTitle("Request Photo View");

                    } else if (member.get_photo_request_id() > 0) {
                        popup.getMenu().getItem(0).setTitle("Withdraw Request");
                    }


                } else {

                    holder.tvBtMenu.setVisibility(View.GONE);
                }
            } else {

                holder.tvBtMenu.setVisibility(View.GONE);
            }


//========Image View========================

            imageLoader.displayImage(Urls.baseUrl + "/images/flags/" + member.get_country_flag() + ".gif", holder.ivCountryFlag, optionsNormalImage);

            holder.image.setMinimumHeight(height);

            imageLoader.displayImage(Urls.baseUrl + "/" + member.get_default_image(),
                    holder.image, options,
                    new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            //  holder.progressBar.setVisibility(View.VISIBLE);
                            // holder.RLprogress1.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            // holder.RLprogress1.setVisibility(View.GONE);
                            //   holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            // holder.progressBar.setVisibility(View.GONE);
                            // holder.RLprogress1.setVisibility(View.GONE);
                            //   holder.progressBar.setVisibility(View.GONE);
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri,
                                                     View view, int current, int total) {
                            // holder.RLprogress1.setProgress(Math.round(100.0f
                            // * current / total));
                        }
                    });

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    //1 view profile
                    //2 interaction
                    Activity activity = (Activity) context;
                    if (ConnectCheck.isConnected(activity.findViewById(android.R.id.content))) {
                        marryMax.statusBaseChecks(member, context, 1, frgMngr, fragment, v, gson.toJson(items), "" + position, memResultsObj, TAG);
                    }

                }
            });


            holder.tvBtMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {


                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item2) {

                            selectedPosition = position;
                            boolean checkStatus = marryMax.statusBaseChecks(member, context, 2, frgMngr, fragment, view, null, null, memResultsObj, TAG);
                            Activity activity = (Activity) context;
                            if (ConnectCheck.isConnected(activity.findViewById(android.R.id.content))) {
                                if (checkStatus) {


                                    switch (item2.getItemId()) {


                                        case R.id.requests: {

                                            String type = null, title = null, btTitile = null, desc = null;


                                            //======================================================
                                            if (member.get_hidden_status() == 1 && member.get_profile_privilege_id() == 0) {


                                                if (member.get_profile_request_id() > 0) {

                                                    JSONObject params = new JSONObject();
                                                    try {

                                                        params.put("userpath", member.getUserpath());
                                                        params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                                                        params.put("interested_id", member.get_profile_request_id());
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    desc = "Are you sure you are not interested in viewing profile Of  <b> <font color=#216917>" + member.getAlias() + "</font></b>?";
                                                    marryMax.withdrawInterest(params, "Withdraw Profile Request", desc, null, frgMngr, "3");


                                                } else {
                                                    //   popup.getMenu().getItem(0).setTitle("Request Profile");

                                                    type = "3";
                                                    title = "Profile View Request";
                                                    btTitile = "Request Profile";
                                                    desc = "Request <b> <font color=#216917>" + member.getAlias() + "</font></b>" + " to view complete profile details.";
                                                    request(member, title, desc, btTitile, type);


                                                }
                                            }


//======================================================
                                            if (member.get_hidden_status() == 0 && member.get_photo_privilege_id() == 0) {

                                                if (member.get_photo_count() == 0 && member.get_blur() == 0) {
                                                    if (member.get_photo_upload_request_id() == 0) {
                                                        //request  photo view
                                                        // popup.getMenu().getItem(0).setTitle("Request Photo");

                                                        type = "1";
                                                        title = "Photo Request";
                                                        btTitile = "Request Photo";
                                                        desc = "Request <b> <font color=#216917>" + member.getAlias() + "</font></b>" + " to upload pictures.";

                                                        request(member, title, desc, btTitile, type);


                                                    } else if (member.get_photo_upload_request_id() > 0) {
                                                        // popup.getMenu().getItem(0).setTitle("Withdraw Request");

                                                        JSONObject params = new JSONObject();
                                                        try {

                                                            params.put("userpath", member.getUserpath());
                                                            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                                                            params.put("interested_id", member.get_photo_upload_request_id());
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        desc = "Are you sure you are not interested in viewing photos of  <b> <font color=#216917>" + member.getAlias() + "</font></b>?";

                                                        marryMax.withdrawInterest(params, "Withdraw Photo Request", desc, null, frgMngr, "1");


                                                    }


                                                } else if ((member.get_photo_count() > 0 || member.get_blur() == 1) && member.get_hide_photo() > 0) {
                                                    if (member.get_photo_request_id() == 0) {
                                                        //request  photo view
                                                        // popup.getMenu().getItem(0).setTitle("Request Photo View");

                                                        type = "2";
                                                        title = "Photo View Request";
                                                        btTitile = "Request Photo";
                                                        desc = "Request <b> <font color=#216917>" + member.getAlias() + "</font></b>" + " to view pictures.";
                                                        request(member, title, desc, btTitile, type);


                                                    } else if (member.get_photo_request_id() > 0) {
                                                        // popup.getMenu().getItem(0).setTitle("Withdraw Request");
                                                        JSONObject params = new JSONObject();
                                                        try {

                                                            params.put("userpath", member.getUserpath());
                                                            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                                                            params.put("interested_id", member.get_photo_request_id());
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        desc = "Are you sure you are not interested in viewing photos of  <b> <font color=#216917>" + member.getAlias() + "</font></b>?";

                                                        marryMax.withdrawInterest(params, "Withdraw Photo View Request", desc, null, frgMngr, "2");

                                                    }
                                                }
                                            }


                                        }
                                        break;


                                        default:
                                            break;

                                    }
                                }
                            }
                            return true;
                        }

                    });
                    popup.show(); //showing popup menu
                }
            });

            holder.ivViewPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Activity activity = (Activity) context;

                    if (ConnectCheck.isConnected(activity.findViewById(android.R.id.content))) {
                        selectedPosition = position;
                        marryMax.statusBaseChecks(member, context, 4, frgMngr, fragment, v, null, null, memResultsObj, TAG);
                    }

                }
            });
            holder.alias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1 view profile
                    //2 interaction


                    Activity activity = (Activity) context;
                    if (ConnectCheck.isConnected(activity.findViewById(android.R.id.content))) {
                        Gson gson = new Gson();
                        //Log.e("selected position", position + "");
                        marryMax.statusBaseChecks(member, context, 1, frgMngr, fragment, v, gson.toJson(items), position + "", memResultsObj, TAG);
                    }

/*

                    Intent intent = new Intent(context, UserProfileActivityWithSlider.class);

                    intent.putExtra("memberdatalist", gson.toJson(items));
                    intent.putExtra("selectedposition", position);
                    context.startActivity(intent);*/


                }
            });


//========================SHOW INTEREST==============================================
            if (member.get_request_response_id() == 0) {
                holder.tvShowInterestText.setText("Show Interest");
                holder.faInterestIcon.setText(context.getResources().getString(R.string.fa_icon_show_interest));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.bt_ShowInterest.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorMMButtonShowInterest));
                }

            } else if (member.get_request_response_id() == 1 || member.get_request_response_id() == 2) {
                holder.faInterestIcon.setText(context.getResources().getString(R.string.fa_icon_pause));

                holder.tvShowInterestText.setText("Awaiting Response");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.bt_ShowInterest.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorGrey));
                } else {
                }


            } else if (member.get_request_response_id() == 3) {
                holder.tvShowInterestText.setText("Interest Accepted");
                holder.faInterestIcon.setText(context.getResources().getString(R.string.fa_icon_accepted));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.bt_ShowInterest.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorMMButtonShowInterest));
                }

            }

            holder.bt_ShowInterest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("Loggg===" + member.get_request_response_id(), "==" + member.get_request_response_id());


                    boolean checkStatus = marryMax.statusBaseChecks(member, context, 2, frgMngr, fragment, view, null, null, null, null);
                    Activity activity = (Activity) context;
                    if (ConnectCheck.isConnected(activity.findViewById(android.R.id.content))) {
                        if (checkStatus) {

                            if (member.get_request_response_id() == 0) {

                                JSONObject params = new JSONObject();
                                try {
                                    params.put("userpath", member.getUserpath());
                                    params.put("path", SharedPreferenceManager.getUserObject(context).get_path());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                Members sessionObj = SharedPreferenceManager.getUserObject(context);
                                if (functions.checkProfileCompleteStatus(sessionObj)) {
                                    if (member.get_interested_id() == 0) {
                                        selectedPosition = position;
                                        showInterest(params, false, member);

                                    }

                                } else {

                                    Toast.makeText(context, "Please Complete Your Profile", Toast.LENGTH_SHORT).show();
                                    //comp profile  when click on this
                                }

                            }
                        }
                    }
                }
            });

            if (member.get_issubscribed() == 1 && member.get_isedit() == 2 && member.get_member_notes_id() != 0) {
                holder.faNotes.setVisibility(View.VISIBLE);
            } else {
                holder.faNotes.setVisibility(View.GONE);
            }

            holder.faNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean bcheck4 = marryMax.statusBaseChecks(member, context, 8, frgMngr, fragment, v, null, null, null, null);
                    if (bcheck4) {
                        dialogAddNotes newFragment = dialogAddNotes.newInstance(member.getUserpath());
                        newFragment.setTargetFragment(fragment, 0);
                        newFragment.show(frgMngr, "dialog");
                    }
                }
            });


            holder.faRemoveFromSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    boolean checkStatus = marryMax.statusBaseChecks(member, context, 3, frgMngr, fragment, v, null, null, null, null);
                    Activity activity = (Activity) context;
                    if (ConnectCheck.isConnected(activity.findViewById(android.R.id.content))) {
                        if (checkStatus) {
                            selectedPosition = position;
                            JSONObject params = new JSONObject();
                            try {
                                // Log.e("user path", member.getUserpath() + "==");
                                params.put("id", member.get_removed_member());
                                params.put("userpath", member.getUserpath());
                                params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            dialogRemoveFromSearch dRemoveFromSearch = dialogRemoveFromSearch.newInstance(member, params);
                            dRemoveFromSearch.setListener(RecyclerViewAdapterMyMatches.this);
                            dRemoveFromSearch.setTargetFragment(fragment, 0);
                            dRemoveFromSearch.show(frgMngr, "Remove From Search");
                        }
                    }

                }
            });
          /*  holder.faAddToFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject params = new JSONObject();
                    try {
                        params.put("id", "0");
                        params.put("userpath", item.getUserpath());
                        params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    addToFavourites(params);
                }
            });*/


/*        holder.ll_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* Intent in = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(in);*//*
            }
        });*/

            holder.itemView.setTag(member);

        }
    }

    private void request(Members member, String title, String desc, String btTitle, String type) {


        JSONObject params = new JSONObject();
        try {
            params.put("alias", SharedPreferenceManager.getUserObject(context).getAlias());
            params.put("type", type);
            params.put("userpath", member.getUserpath());
            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialogRequest newFragment = dialogRequest.newInstance(params.toString(), title, desc, btTitle, true);

        newFragment.setListener(RecyclerViewAdapterMyMatches.this);
        newFragment.setTargetFragment(fragment, 0);
        newFragment.show(frgMngr, "dialog");


    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }





/*    @Override
    public void onClick(final View v) {
        onItemClickListener.onItemClick(v, (Members) v.getTag());
    }*/

    private void showInterest(JSONObject params, final boolean replyCheck, final Members member) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);
        Log.e("showInterest params", params.toString());
        Log.e("profile path", Urls.interestProvisions);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.interestProvisions, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  interest ", response + "");

                        try {
                            JSONObject responseObject = response.getJSONArray("data").getJSONArray(0).getJSONObject(0);


                            Gson gson;
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            Type type = new TypeToken<Members>() {
                            }.getType();
                            Members member2 = (Members) gson.fromJson(responseObject.toString(), type);
                            Log.e("interested id", "" + member.getAlias() + "====================");

                            dialogShowInterest newFragment = dialogShowInterest.newInstance(member, member.getUserpath(), replyCheck, member2);
                            newFragment.setListener(RecyclerViewAdapterMyMatches.this);

                            newFragment.setTargetFragment(fragment, 0);
                            newFragment.show(frgMngr, "dialog");


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }


                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    public void setProgressMore(final boolean isProgress) {
   /*     if (isProgress) {

            items.add(new Members());

         notifyItemInserted(items.size() );

        } else {
         //   items.remove(items.size() - 1);
            notifyItemRemoved(items.size());
        }*/


        if (isProgress) {

            items.add(null);
            recyclerView.post(new Runnable() {
                public void run() {
                    if (items.size() > 0) {
                        notifyItemInserted(items.size() - 1);
                    }
                }
            });


        } else {
            items.remove(items.size() - 1);

            recyclerView.post(new Runnable() {
                public void run() {
                    if (items.size() > 0) {
                        notifyItemRemoved(items.size());
                    }
                }
            });

        }
    }

    public void setMoreLoading(boolean isMoreLoading) {
        this.isMoreLoading = isMoreLoading;
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Members> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();
        //   Log.e("item size in adapter", items.size() + "");
    }

    public void addItemMore(List<Members> lst) {
        items.addAll(lst);
        notifyItemRangeChanged(0, items.size());
    }

    @Override
    public void onComplete(String s) {
        Toast.makeText(context, "Recycler ", Toast.LENGTH_SHORT).show();
        Log.e("clicked ", "Clicked");
        Log.e("--------------------", "---------------Recycler------------------------------------");
    }


    /*public interface OnItemClickListener {

        void onItemClick(View view, Members members);

    }*/


    @Override
    public void updateInterest(boolean update) {

        if (update) {
            items.get(selectedPosition).set_request_response_id(1);
            notifyDataSetChanged();
        }

    }

    @Override
    public void onRemove(boolean remove) {
        if (remove) {
            items.remove(selectedPosition);
            notifyDataSetChanged();
            updateMatchesCountCallback.onUpdateMatchCount(true);
        }
        if (items.size() == 0) {
            matchesRefreshCallBackInterface.onRefreshMatch();
        }
    }


    @Override
    public void onPhoneViewRequestComplete(String requestid) {

        items.get(selectedPosition).set_phone_request_id(Long.parseLong(requestid));
        notifyDataSetChanged();
    }

    @Override
    public void onWithdrawRequestComplete(String requestid) {
        //    Toast.makeText(context, "" + requestid, Toast.LENGTH_SHORT).show();
        int id = Integer.parseInt(requestid);
        switch (id) {
            case 1:

                items.get(selectedPosition).set_photo_upload_request_id(0);

                break;
            case 2:
                items.get(selectedPosition).set_photo_request_id(0);
                break;
            case 3:
                items.get(selectedPosition).set_profile_request_id(0);
                break;
            case 4:
                items.get(selectedPosition).set_phone_request_id(0);
                break;
            case 5:
                items.get(selectedPosition).set_interested_id(0);
                break;

        }
        notifyDataSetChanged();

    }

    @Override
    public void onRequestCallback(String type, String responseid) {

        if (type.equals("1")) {
            items.get(selectedPosition).set_photo_upload_request_id(Long.parseLong(responseid));

        } else if (type.equals("2")) {

            items.get(selectedPosition).set_photo_request_id(Long.parseLong(responseid));

        } else if (type.equals("3")) {
            items.get(selectedPosition).set_profile_request_id(Long.parseLong(responseid));


        }
        notifyDataSetChanged();

    }

    public interface OnLoadMoreListener {

        void onLoadMore();
    }

    protected static class MMViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public mTextView alias, imageCount, age, pref1, pref2, pref3, pref4, prefValue1, prefValue2, prefValue3, prefValue4, tvShowInterestText;
        public faTextView tvBtMenu, faRemoveFromSearch, faInterestIcon, faNotes;  //faAddToFav
        public ImageView ivCountryFlag, ivViewPhone;
        public TextView tvAboutMe;
        public LinearLayoutCompat bt_ShowInterest, ll_user_profile, ll_image_count;
        //    public mTextView country;

        public MMViewHolder(View itemView) {
            super(itemView);

            faInterestIcon = (faTextView) itemView.findViewById(R.id.faTextViewMyMatchesInterestIcon);
            faNotes = (faTextView) itemView.findViewById(R.id.faTextViewMMNotes);


            image = (ImageView) itemView.findViewById(R.id.ImageViewMyMatchesProfile);
            alias = (mTextView) itemView.findViewById(R.id.TextVewMMAlias);
            age = (mTextView) itemView.findViewById(R.id.TextViewMMAge);
            pref1 = (mTextView) itemView.findViewById(R.id.TextViewMMPref1);
            pref2 = (mTextView) itemView.findViewById(R.id.TextViewMMPref2);
            pref3 = (mTextView) itemView.findViewById(R.id.TextViewMMPref3);
            pref4 = (mTextView) itemView.findViewById(R.id.TextViewMMPref4);

            prefValue1 = (mTextView) itemView.findViewById(R.id.TextViewMMPrefValue1);
            prefValue2 = (mTextView) itemView.findViewById(R.id.TextViewMMPrefValue2);
            prefValue3 = (mTextView) itemView.findViewById(R.id.TextViewMMPrefValue3);
            prefValue4 = (mTextView) itemView.findViewById(R.id.TextViewMMPrefValue4);
            tvShowInterestText = (mTextView) itemView.findViewById(R.id.TextViewMMShowInterestText);

            tvAboutMe = (TextView) itemView.findViewById(R.id.TextViewMMAboutMe);


            tvBtMenu = (faTextView) itemView.findViewById(R.id.TextViewBtsDropDownMyMatche);
            faRemoveFromSearch = (faTextView) itemView.findViewById(R.id.faTextViewMMRemove);
            //    faAddToFav = (faTextView) itemView.findViewById(R.id.faTextViewMMAddToFavourites);


            bt_ShowInterest = (LinearLayoutCompat) itemView.findViewById(R.id.bt_show_interest);
            /*     bt_WithdrawInterest = (LinearLayout) itemView.findViewById(R.id.bt_withdraw_interest);*/
            imageCount = (mTextView) itemView.findViewById(R.id.TextViewMMNumberOfImages);

            ll_image_count = (LinearLayoutCompat) itemView.findViewById(R.id.LinearLayoutMMImagesCount);

            ivCountryFlag = (ImageView) itemView.findViewById(R.id.ImageViewMMCountryFlag);
            ivViewPhone = (ImageView) itemView.findViewById(R.id.ImageViewMMViewPhone);
            //   ll_user_profile = (LinearLayout) itemView.findViewById(R.id.LinearLayoutMMUserProfile);
            //   country = (mTextView) itemView.findViewById(R.id.TextViewMemberCountryDashMain);
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;

        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.pBar);
        }
    }
}
