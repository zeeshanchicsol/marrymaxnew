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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.chicsol.marrymax.activities.UserProfileActivityWithSlider;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mMemListDetail;
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

public class RecyclerViewAdapterMemberList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    private List<mMemListDetail> items;
    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private Fragment fragment;
    private int height = 0;
    String userpath, id;
    Activity activity;

    public RecyclerViewAdapterMemberList(final Context context, FragmentManager frg, Fragment fragment, OnUpdateListener onUpdateListener, String userpath, String id, Activity activity) {
        //this.items = items;
        this.activity = activity;
        this.userpath = userpath;
        this.id = id;
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
            return new MMViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_list, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false));
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, int position) {
        final mMemListDetail obj = items.get(position);


        if (holder1 instanceof MMViewHolder) {
            MMViewHolder holder = ((MMViewHolder) holder1);
            holder.tvAlias.setText(obj.getAlias());
            holder.tvAge.setText("( " + obj.getMin_age() + " Years )");
            holder.tvCountry.setText(obj.getCountry_name() + " | " + obj.getEducation_types());

            //  holder.tvEdu.setText(obj.getEducation_types());
            // holder.tvPreferedCallTime.setText(obj.getOther_info());
            //      holder.tvCountry.setText(obj.getCountry_name() + " | ");
            //   holder.tvOccupation.setText(obj.get() + " | ");
             holder.tvMaritalStatus.setText(""+obj.getMarital_status_types());

          //  Log.e("obj.getDefault_image()", "" + obj.getDefault_image());
            imageLoader.displayImage(Urls.baseUrl + "/images/flags/" + obj.getCountry_flag() + ".gif", holder.ivCountryFlag, optionsNormalImage);

        //    Log.e("bj.getDefault_image()", "" + obj.getDefault_image());
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

            holder.faRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", id);
                        jsonObject.put("userpath", obj.getUserpath());


                        jsonObject.put("path", SharedPreferenceManager.getUserObject(context).get_path());

                        delete(jsonObject);
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

    private void showUserProfile(mMemListDetail obj) {
        Intent intent = new Intent(context, UserProfileActivityWithSlider.class);

        intent.putExtra("selectedposition", "-1");
        List<Members> memberDataList = new ArrayList<Members>();
        Members members = new Members();
        members.setUserpath(obj.getUserpath());
        memberDataList.add(members);
        SharedPreferenceManager.setMemberDataList(context, new Gson().toJson(memberDataList).toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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


    private void delete(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        Log.e("params delete", params.toString());
        Log.e("profile path", Urls.removeMemList);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.removeMemList, params,
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
                            pDialog.hide();
                            e.printStackTrace();
                        }


                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                onUpdateListener.onUpdate("Error occurred, Try again.");

                VolleyLog.e("res err", "Error: " + error);
                pDialog.hide();
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

    public void addAll(List<mMemListDetail> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();
        Log.e("item size in adapter", items.size() + "");
    }

    public void addItemMore(List<mMemListDetail> lst) {
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
        public ImageView image, ivCountryFlag;

        public TextView tvAlias, tvAge, tvPreferedCallTime, tvCountry, tvMaritalStatus;
        public faTextView faRemove;

        public MMViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.ImageViewListContactImage);
            ivCountryFlag = (ImageView) itemView.findViewById(R.id.ImageViewCountryFlag);

            tvAlias = (TextView) itemView.findViewById(R.id.TextViewListUsername);
            tvAge = (TextView) itemView.findViewById(R.id.TextViewListAge);
            tvCountry = (TextView) itemView.findViewById(R.id.TextViewMemListCountry);
            tvPreferedCallTime = (TextView) itemView.findViewById(R.id.TextViewMyContactListCallTime);

            tvMaritalStatus= (TextView) itemView.findViewById(R.id.TextViewListMaritalStatus);
            faRemove = (faTextView) itemView.findViewById(R.id.faTextViewMyContactItemRemove);

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
