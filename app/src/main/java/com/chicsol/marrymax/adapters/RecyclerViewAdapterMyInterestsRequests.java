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
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
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
import com.chicsol.marrymax.dialogs.dialogDeclineInterestInbox;
import com.chicsol.marrymax.dialogs.dialogReplyOnAcceptInterestInbox;
import com.chicsol.marrymax.dialogs.dialogRequestPhone;
import com.chicsol.marrymax.dialogs.dialogShowInterest;
import com.chicsol.marrymax.interfaces.RequestCallbackInterface;
import com.chicsol.marrymax.interfaces.WithdrawRequestCallBackInterface;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mCommunication;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapterMyInterestsRequests extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements dialogShowInterest.onCompleteListener, dialogRequestPhone.onCompleteListener, WithdrawRequestCallBackInterface ,RequestCallbackInterface {
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
    private List<mCommunication> items;
    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private Fragment fragment;
    private boolean interestCheck;
    public OnUpdateListener onUpdateListener;
    private boolean withdrawCheck;
    private MarryMax marryMax;
    private int selectedPosition = 0;

    public RecyclerViewAdapterMyInterestsRequests(final Context context, FragmentManager frg, OnLoadMoreListener onLoadMoreListener, Fragment fragment, boolean interestCheck, OnUpdateListener onUpdateListener, boolean withdrawcheck) {
        //this.items = items;
        marryMax = new MarryMax((Activity) context);
        marryMax.setWithdrawRequestCallBackInterface(RecyclerViewAdapterMyInterestsRequests.this);
        marryMax.setRequestCallBackInterface(RecyclerViewAdapterMyInterestsRequests.this);


        this.withdrawCheck = withdrawcheck;
        this.onUpdateListener = onUpdateListener;
        this.interestCheck = interestCheck;
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
                            //     iv.getLayoutParams().width = (int) (widthScreen * 0.10);
                            // Log.e("wid " + widthScreen + "  " + heightScreen, "");
                            bmp_sticker = resizeImage(bmp, h);
                        } else {
                            int h = (int) (heightScreen * 0.10);//it set the height of image 10% of your screen
                            //   iv.getLayoutParams().width = (int) (widthScreen * 0.15);
                            bmp_sticker = resizeImage(bmp, h);
                            // Log.e("wid " + widthScreen + "  " + heightScreen, "");
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
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
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
            return new MMViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_inbox_my_interests, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false));
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {
        final mCommunication obj = items.get(position);


        if (holder1 instanceof MMViewHolder) {
            MMViewHolder holder = ((MMViewHolder) holder1);
            holder.tvAlias.setText(obj.getAlias());
            holder.tvAge.setText(obj.getAge() + " Years, ");
            holder.tvEthnic.setText(obj.getReligious_sect_type());

            holder.tvEdu.setText(obj.getEducation_types());
            holder.tvMessage.setText(obj.getMessage());
            holder.tvCountry.setText(obj.getCountry_name() + " ");
            holder.tvOccupation.setText(obj.getOccupation_types() + " | ");
            holder.tvDate.setText(obj.getMessage_date());


            if (withdrawCheck) {
                holder.ll_No.setVisibility(View.GONE);
                holder.ll_Yes.setVisibility(View.GONE);
                holder.btWithdraw.setVisibility(View.VISIBLE);

            } else {
                holder.ll_No.setVisibility(View.VISIBLE);
                holder.ll_Yes.setVisibility(View.VISIBLE);
                holder.btWithdraw.setVisibility(View.GONE);

            }


            holder.tvAlias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (obj.getUserpath() != null && obj.getUserpath() != "") {
                       /* Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra("userpath", obj.getUserpath());
                        context.startActivity(intent);*/

                        showUserProfile(obj);


                    } else {
                        Toast.makeText(context, "Error ! ", Toast.LENGTH_SHORT).show();

                    }
                }
            });


            //   imageLoader.displayImage(Urls.baseUrl + "/images/flags/" + member.getDefault_image() + ".gif", holder.ivCountryFlag, optionsNormalImage);
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

            holder.btWithdraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = position;
                    JSONObject params = new JSONObject();
                    try {
                        params.put("userpath", obj.getUserpath());
                        params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                        params.put("interested_id", obj.getRequest_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String r;
                    if (interestCheck) {
                        r = "interest";
                    } else {
                        r = "request";
                    }
                    String desc = "You are not anymore interested in   <font color=#216917>" + obj.getAlias() + "</font>Are you sure you want to withdraw " + r;


                    marryMax.withdrawInterest(params, "Withdraw Request", desc, fragment, frgMngr, "5");
                }
            });
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (obj.getUserpath() != null && obj.getUserpath() != "") {
                       /* Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra("userpath", obj.getUserpath());
                        context.startActivity(intent);*/

                        showUserProfile(obj);


                    } else {
                        Toast.makeText(context, "Error ! ", Toast.LENGTH_SHORT).show();

                    }

                }
            });

            holder.ll_Yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    if (interestCheck) {

                        JSONObject params = new JSONObject();
                        try {
                            params.put("userpath", obj.getUserpath());
                            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        replyOnAcceptInterest(params, true, obj);
                    } else {


                        JSONObject params = new JSONObject();
                        try {

                            params.put("request_response_id", "3");
                            params.put("request_id", obj.getRequest_id());
                            params.put("type", obj.getRequest_type_id());
                            params.put("userpath", obj.getUserpath());
                            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                            params.put("alias", SharedPreferenceManager.getUserObject(context).getAlias());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("params", Urls.responseInterest + "   " + params);
                        responseOnInterest(params);

                    }
                }
            });
            holder.ll_No.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (interestCheck) {
                        dialogDeclineInterestInbox newFragment = dialogDeclineInterestInbox.newInstance(obj, obj.getUserpath());
                        newFragment.setTargetFragment(fragment, 0);
                        newFragment.show(frgMngr, "dialog");
                    } else {

                        JSONObject params = new JSONObject();
                        try {

                            params.put("request_response_id", "4");
                            params.put("request_id", obj.getRequest_id());
                            params.put("type", obj.getRequest_type_id());
                            params.put("userpath", obj.getUserpath());
                            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("params", Urls.responseInterest + "   " + params);
                        responseOnInterest(params);

                    }


                }
            });


            holder.itemView.setTag(obj);

        }
    }

    private void showUserProfile(mCommunication obj) {
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


    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {

            items.add(null);
            notifyItemInserted(items.size() - 1);

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

    public void addAll(List<mCommunication> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();
        Log.e("item size in adapter", items.size() + "");
    }

    public void addItemMore(List<mCommunication> lst) {
        items.addAll(lst);
        notifyItemRangeChanged(0, items.size());
    }

    @Override
    public void onComplete(String s) {
        Log.e("clicked ", "Clicked");
        Log.e("--------------------", "---------------Recycler------------------------------------");
    }

    @Override
    public void onWithdrawRequestComplete(String requestid) {
        //   Toast.makeText(context, "" + requestid, Toast.LENGTH_SHORT).show();
        int id = Integer.parseInt(requestid);
        switch (id) {
   /*         case 1:

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
                break;*/
            case 5:
                //  items.get(selectedPosition).set_interested_id(0);

                items.remove(selectedPosition);


                notifyDataSetChanged();
                if (items.size() == 0) {
                    onUpdateListener.onUpdate("");
                }
                break;

        }


    }

    @Override
    public void onRequestCallback(String type, String responseid) {
        onUpdateListener.onUpdate("");
    }






   /* private void withdrawInterest(JSONObject params, String title, String desc) {


        dialogWithdrawInterest newFragment = dialogWithdrawInterest.newInstance(params, title, desc);
        newFragment.setTargetFragment(fragment, 3);
        newFragment.show(frgMngr, "dialog");

    }*/

    public interface OnLoadMoreListener {

        void onLoadMore();
    }

    protected static class MMViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public TextView tvAlias, tvAge, tvEthnic, tvMessage, tvCountry, tvOccupation, tvEdu, tvDate;

        public LinearLayout ll_Yes, ll_No;
        public AppCompatButton btWithdraw;

        public MMViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.ImageViewInterestRequestUserProfileImage);
            tvAlias = (TextView) itemView.findViewById(R.id.TextViewMyInterestRequestsUsername);
            tvAge = (TextView) itemView.findViewById(R.id.TextViewInterestRequestAge);
            tvEthnic = (TextView) itemView.findViewById(R.id.TextViewInterestRequestEthnicBackground);
            tvMessage = (TextView) itemView.findViewById(R.id.TextViewInterestRequestMessage);
            tvCountry = (TextView) itemView.findViewById(R.id.TextViewInterestRequestCountry);
            tvOccupation = (TextView) itemView.findViewById(R.id.TextViewInterestRequestOccupation);
            tvEdu = (TextView) itemView.findViewById(R.id.TextViewInterestRequestEducation);
            tvDate = (TextView) itemView.findViewById(R.id.TextViewInterestRequestDate);
            ll_Yes = (LinearLayout) itemView.findViewById(R.id.LinearLayoutInterestRequestYes);
            ll_No = (LinearLayout) itemView.findViewById(R.id.LinearLayoutInterestRequestNo);
            btWithdraw = (AppCompatButton) itemView.findViewById(R.id.ButtonInterestRequestWithdraw);
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;

        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.pBar);
        }
    }


    private void replyOnAcceptInterest(JSONObject params, final boolean replyCheck, final mCommunication obj) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        Log.e("params", params.toString());
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
                            //     Log.e("interested id", "" + member.getAlias() + "====================");

                            dialogReplyOnAcceptInterestInbox newFragment = dialogReplyOnAcceptInterestInbox.newInstance(obj, obj.getUserpath(), replyCheck, member2);
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
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }


    private void responseOnInterest(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.responseInterest, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("re  responseOnInterest", response + "");

                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {

                                onUpdateListener.onUpdate("");
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


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };

// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);

    }

    public interface OnUpdateListener {

        void onUpdate(String msg);
    }

}
