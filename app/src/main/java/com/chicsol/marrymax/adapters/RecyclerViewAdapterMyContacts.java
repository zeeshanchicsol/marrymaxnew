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


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.chicsol.marrymax.activities.UserProfileActivityWithSlider;
import com.chicsol.marrymax.dialogs.dialogFeedback;
import com.chicsol.marrymax.dialogs.dialogFeedbackDetail;
import com.chicsol.marrymax.dialogs.dialogMatchAidFeedback;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mContacts;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapterMyContacts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public ImageLoader imageLoader;
    // public OnItemClickListener onItemClickListener;
    public OnUpdateListener onUpdateListener;
    Context context;
    FragmentManager frgMngr;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private DisplayImageOptions options, optionsNormalImage;
    private LayoutInflater inflater;
    private List<mContacts> items;
    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private Fragment fragment;
    private int height = 0;
    private boolean delete;
    String type = "";

    public RecyclerViewAdapterMyContacts(final Context context, FragmentManager frg, Fragment fragment, OnUpdateListener onUpdateListener, boolean delete, String type) {
        //this.items = items;
        this.type = type;

        this.delete = delete;
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

                            bmp_sticker = resizeImage(bmp, h);

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
        this.onUpdateListener = onUpdateListener;
        items = new ArrayList<>();
    }


    public void setRecyclerView(RecyclerView mView) {
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            }
        });
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == VIEW_ITEM) {
            return new MMViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_my_contacts, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false));
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, int position) {
        final mContacts obj = items.get(position);


        if (holder1 instanceof MMViewHolder) {
            MMViewHolder holder = ((MMViewHolder) holder1);
            holder.tvAlias.setText(obj.getAlias() + " ");
            holder.tvAge.setText("( " + obj.getMin_age() + " Years )");

            holder.tvPhone.setText(obj.getPhone_mobile());
            holder.tvPreferedCallTime.setText(obj.getOther_info());

            if (type.equals("st")) {
                holder.llPhoneNumber.setVisibility(View.GONE);
                holder.llCallTime.setVisibility(View.GONE);
            } else {
                holder.llPhoneNumber.setVisibility(View.VISIBLE);
                holder.llCallTime.setVisibility(View.VISIBLE);
            }


            if (type.equals("sv") || type.equals("st")) {

                holder.llFeedback.setVisibility(View.GONE);
                holder.llViewFeedback.setVisibility(View.GONE);

                if (obj.getMatch_id() != 0) {
                    if (obj.getFeedback_id() == 0) {
                        holder.llFeedback.setVisibility(View.VISIBLE);
                    } else {
                        holder.llViewFeedback.setVisibility(View.VISIBLE);
                    }

                }
            } else {
                holder.llFeedback.setVisibility(View.GONE);
                holder.llViewFeedback.setVisibility(View.GONE);
            }
            holder.tvDate.setText(obj.getStart_date());


            //   imageLoader.displayImage(Urls.baseUrl + "/images/flags/" + member.getDefault_image() + ".gif", holder.ivCountryFlag, optionsNormalImage);

            Log.e("bj.getDefault_image()", "" + obj.getDefault_image());
            holder.image.setMinimumHeight(height);
            imageLoader.displayImage(Urls.baseUrl + "/" + obj.getDefault_image(),
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

            holder.tvAlias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (obj.getUserpath() != null && obj.getUserpath() != "") {

                        showUserProfile(obj);

                    } else {
                        Toast.makeText(context, "Error ! ", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            if (delete) {

                holder.faRemove.setVisibility(View.VISIBLE);
            } else {
                holder.faRemove.setVisibility(View.GONE);

            }

            holder.faRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", obj.getPhone_request_id());
                        jsonObject.put("path", SharedPreferenceManager.getUserObject(context).get_path());

                        deleteMyContact(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });


            holder.llFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dialogFeedback newFragment = dialogFeedback.newInstance(obj.getUserpath(), obj.getMatch_id() + "");
                    newFragment.setTargetFragment(fragment, 0);
                    newFragment.show(frgMngr, "dialog");
                   /* JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", obj.getPhone_request_id());
                        jsonObject.put("path", SharedPreferenceManager.getUserObject(context).get_path());

                        deleteMyContact(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/

                }
            });

            holder.llViewFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", obj.getMatch_id());
                        jsonObject.put("my_id", obj.getFeedback_id());
                        jsonObject.put("userpath", obj.getUserpath());
                        jsonObject.put("path", SharedPreferenceManager.getUserObject(context).get_path());

                        dialogFeedbackDetail newFragment = dialogFeedbackDetail.newInstance(jsonObject.toString());
                        newFragment.setTargetFragment(fragment, 0);
                        newFragment.show(frgMngr, "dialog");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (obj.getUserpath() != null && obj.getUserpath() != "") {

                        showUserProfile(obj);


                    } else {
                        Toast.makeText(context, "Error ! ", Toast.LENGTH_SHORT).show();

                    }

                }
            });


            holder.itemView.setTag(obj);

        }
    }

    private void showUserProfile(mContacts obj) {
        Intent intent = new Intent(context, UserProfileActivityWithSlider.class);

        intent.putExtra("selectedposition", "-1");
        List<Members> memberDataList = new ArrayList<Members>();
        Members members = new Members();
        members.setUserpath(obj.getUserpath());
        memberDataList.add(members);
        SharedPreferenceManager.setMemberDataList(context, new Gson().toJson(memberDataList).toString());
        context.startActivity(intent);

    }


    @Override
    public int getItemViewType(int position) {
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private void deleteMyContact(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        Log.e("params", params.toString());
        Log.e("profile path", Urls.deleteMyContact);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.deleteMyContact, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  delete ", response + "");

                        try {
                            int responseid = response.getInt("id");

                            if (responseid == 1) {

                                onUpdateListener.onUpdate("Contact deleted successfully");


                            } else {
                                onUpdateListener.onUpdate("Error occurred, Try again.");

                            }

                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }


                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                onUpdateListener.onUpdate("Error occurred, Try again.");

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
        if (isProgress) {

            items.add(null);
//            notifyItemInserted(items.size() - 1);

        } else {
            items.remove(items.size() - 1);
            notifyItemRemoved(items.size());
        }
    }

    public void setMoreLoading(boolean isMoreLoading) {
        this.isMoreLoading = isMoreLoading;
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<mContacts> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();
        Log.e("item size in adapter", items.size() + "");
    }

    public void addItemMore(List<mContacts> lst) {
        items.addAll(lst);
        notifyItemRangeChanged(0, items.size());
    }
/*

    @Override
    public void onComplete(String s) {
        Log.e("clicked ", "Clicked");
        Log.e("--------------------", "---------------Recycler------------------------------------");
    }
*/


    /*public interface OnItemClickListener {

        void onItemClick(View view, Members members);

    }*/


    public interface OnUpdateListener {

        void onUpdate(String msg);
    }

    protected static class MMViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public TextView tvAlias, tvAge, tvPhone, tvPreferedCallTime, tvCountry, tvDate;
        public faTextView faRemove, faFeedback;
        LinearLayout llCallTime, llPhoneNumber, llFeedback, llViewFeedback;

        public MMViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.ImageViewListContactImage);
            tvAlias = (TextView) itemView.findViewById(R.id.TextViewListUsername);
            tvAge = (TextView) itemView.findViewById(R.id.TextViewListAge);
            tvPhone = (TextView) itemView.findViewById(R.id.TextViewListPhoneNumber);
            tvPreferedCallTime = (TextView) itemView.findViewById(R.id.TextViewMyContactListCallTime);

            tvDate = (TextView) itemView.findViewById(R.id.TextViewListDate);
            faRemove = (faTextView) itemView.findViewById(R.id.faTextViewMyContactItemRemove);
            faFeedback = (faTextView) itemView.findViewById(R.id.faTextViewMyContactItemFeedback);

            llFeedback = (LinearLayout) itemView.findViewById(R.id.LinearLayoutMyContactItemFeedback);


            llViewFeedback = (LinearLayout) itemView.findViewById(R.id.LinearLayoutMyContactItemViewFeedback);

            llCallTime = (LinearLayout) itemView.findViewById(R.id.LinearLayoutAccountSettingMyContactCallTime);
            llPhoneNumber = (LinearLayout) itemView.findViewById(R.id.LinearLayoutAccountSettingMyContactPhoneNumber);

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
