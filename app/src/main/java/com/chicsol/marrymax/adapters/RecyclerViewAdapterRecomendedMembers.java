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
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import com.chicsol.marrymax.activities.UserProfileActivityWithSlider;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mRecommended;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
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

public class RecyclerViewAdapterRecomendedMembers extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    private List<mRecommended> items;
    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private Fragment fragment;
    private int height = 0;

    public RecyclerViewAdapterRecomendedMembers(final Context context, FragmentManager frg, Fragment fragment, OnUpdateListener onUpdateListener) {

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
                            //Log.e("wid " + widthScreen + "  " + heightScreen, "");
                            bmp_sticker = resizeImage(bmp, h);
                        } else {
                            int h = (int) (heightScreen * 0.027);//it set the height of image 10% of your screen
                            //   iv.getLayoutParams().width = (int) (widthScreen * 0.15);
                            bmp_sticker = resizeImage(bmp, h);
                            //Log.e("wid " + widthScreen + "  " + heightScreen, "");
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
            return new MMViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_recommended_members, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false));
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, int position) {
        final mRecommended obj = items.get(position);


        if (holder1 instanceof MMViewHolder) {
            MMViewHolder holder = ((MMViewHolder) holder1);
            final PopupMenu popup = new PopupMenu(context, holder.fabAction);

            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.menu_recommended_matches, popup.getMenu());
            // popup.

            holder.fabAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (obj.getResponse_id() == 0) {
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {

                                    case R.id.likeit: {
                                        JSONObject params = new JSONObject();
                                        try {
                                            params.put("response_id", 1);
                                            params.put("path", SharedPreferenceManager.getUserObject(context).getPath());
                                            params.put("id", obj.id);
                                            recommendResponse(params);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                    break;
                                    case R.id.dontlike: {

                                        JSONObject params = new JSONObject();
                                        try {
                                            params.put("response_id", 2);
                                            params.put("path", SharedPreferenceManager.getUserObject(context).getPath());
                                            params.put("id", obj.getId());
                                            recommendResponse(params);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                                    case R.id.notmychoice: {
                                        JSONObject params = new JSONObject();
                                        try {
                                            params.put("response_id", 3);
                                            params.put("path", SharedPreferenceManager.getUserObject(context).getPath());
                                            params.put("id", obj.id);
                                            recommendResponse(params);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                                    case R.id.ignoreit: {
                                        JSONObject params = new JSONObject();
                                        try {
                                            params.put("response_id", 4);
                                            params.put("path", SharedPreferenceManager.getUserObject(context).getPath());
                                            params.put("id", obj.id);
                                            recommendResponse(params);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;

                                }


                                return false;
                            }
                        });

                        popup.show();

                    }

                }
            });

            if (obj.getResponse_id() != 0) {

                holder.fabAction.setEnabled(false);
                if (obj.getResponse_id() == 1) {

                    holder.fabAction.setImageResource(R.drawable.ic_thumb_up_black_24dp);


                    holder.fabAction.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorDefaultGreen)));

                } else if (obj.getResponse_id() == 2) {
                    holder.fabAction.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                    holder.fabAction.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorTextRed)));

                } else if (obj.getResponse_id() == 3) {
                    holder.fabAction.setImageResource(R.drawable.ic_person_outline_black_24dp);

                    holder.fabAction.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorASBlue)));


                } else if (obj.getResponse_id() == 4) {
                    holder.fabAction.setImageResource(R.drawable.ic_delete_forever_black_24dp);


                    holder.fabAction.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorGrey)));


                }

            } else {
                holder.fabAction.setEnabled(true);
                holder.fabAction.setImageResource(R.drawable.ic_more_vert_black_24dp_file);


                holder.fabAction.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorWhite)));

            }

            holder.tvAlias.setText(obj.getAlias());
            holder.tvAge.setText("( " + obj.getAge() + " Years )");
            holder.tvEducation.setText(obj.getEducation_types());

            holder.tvMaritalStatus.setText(obj.getMarital_status_types());
            holder.tvEthnicity.setText(obj.getEthnic_background_types());
            holder.tvReligious.setText(obj.getReligious_sec_type());
            holder.tvDate.setText(obj.getDate());
            holder.tvRecommendedBy.setText(obj.getAdmin_name());
            holder.tvRecommendationNotes.setText(obj.getNotes());

            // imageLoader.displayImage(Urls.baseUrl + "/images/flags/" + member.getDefault_image() + ".gif", holder.ivCountryFlag, optionsNormalImage);

            //Log.e("bj.getDefault_image()", "" + obj.getDefault_image());
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
                  /*      Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra("userpath", obj.getUserpath());

                        context.startActivity(intent);*/

                        showUserProfile(obj);

                    } else {
                        Toast.makeText(context, "Error ! ", Toast.LENGTH_SHORT).show();

                    }
                }
            });

     /*       holder.tvEthnicity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", obj.getPhone_request_id());
                        jsonObject.put("path", SharedPreferenceManager.getUserObject(context).getPath());

                        deleteMyContact(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
*/
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (obj.getUserpath() != null && obj.getUserpath() != "") {
                      /*  Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra("userpath", obj.getUserpath());

                        context.startActivity(intent);*/
                        showUserProfile(obj);

                    } else {
                        Toast.makeText(context, "Error ! ", Toast.LENGTH_SHORT).show();

                    }

                }
            });


            holder.itemView.setTag(obj);

        }
    }

    private void showUserProfile(mRecommended obj) {
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

    public void addAll(List<mRecommended> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();
        //Log.e("item size in adapter", items.size() + "");
    }

    public void addItemMore(List<mRecommended> lst) {
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

        public TextView tvAlias, tvAge, tvEducation, tvMaritalStatus, tvCountry, tvDate;
        public TextView tvEthnicity, tvReligious, tvRecommendedBy, tvRecommendationNotes;
        FloatingActionButton fabAction;

        public MMViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.ImageViewRecommendedMatchImage);
            tvAlias = (TextView) itemView.findViewById(R.id.TextViewRecommendedMatchAlias);
            tvAge = (TextView) itemView.findViewById(R.id.TextViewRecommendedMatchAge);
            tvEducation = (TextView) itemView.findViewById(R.id.TextViewRecommendedMatchEducation);
            tvMaritalStatus = (TextView) itemView.findViewById(R.id.TextViewRecommendedMatchMaritalStatus);
            tvRecommendedBy = (TextView) itemView.findViewById(R.id.TextViewRecommendedMatchRecomdBy);
            tvRecommendationNotes = (TextView) itemView.findViewById(R.id.TextViewRecommendationNotes);


            tvDate = (TextView) itemView.findViewById(R.id.TextViewRecommendedMatchDate);
            tvEthnicity = (TextView) itemView.findViewById(R.id.TextViewRecommendedMatchMaritalEthnicity);
            tvReligious = (TextView) itemView.findViewById(R.id.TextViewRecommendedMatchMaritalReligious);
            fabAction = (FloatingActionButton) itemView.findViewById(R.id.FloatingActionButtonRecommendedMatchesMenu);
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;

        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.pBar);
        }
    }


    private void recommendResponse(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        //Log.e("params", params.toString());
        //Log.e("profile path", Urls.recommendResponse);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.recommendResponse, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Res  interest ", response + "");


                        try {
                            int responseid = response.getInt("id");
                            if (responseid == 1) {

                                Toast.makeText(context, "Response send", Toast.LENGTH_SHORT).show();
                                onUpdateListener.onUpdate("");
                            } else {

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


}
