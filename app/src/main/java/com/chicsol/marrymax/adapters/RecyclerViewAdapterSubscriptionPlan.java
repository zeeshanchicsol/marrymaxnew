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


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.subscription.OrderProcessActivity;
import com.chicsol.marrymax.modal.Subscription;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterSubscriptionPlan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    private List<Subscription> items;
    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private Fragment fragment;
    private int height = 0;

    public RecyclerViewAdapterSubscriptionPlan(final Context context, FragmentManager frg, Fragment fragment, OnUpdateListener onUpdateListener) {

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
            return new MMViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_subscription_plan, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false));
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, int position) {
        final Subscription obj = items.get(position);


        if (holder1 instanceof MMViewHolder) {
            MMViewHolder holder = ((MMViewHolder) holder1);
            if (position == 1) {
                holder.tvRecomm.setVisibility(View.VISIBLE);
            } else {
                holder.tvRecomm.setVisibility(View.GONE);
            }

            holder.tvPlanName.setText("" + obj.getItem_name());
            holder.tvPlanDuration.setText("  (" + obj.getDuration() + " Months)");
            holder.tvPlanTitle.setText(obj.getName());

            holder.btPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (SharedPreferenceManager.getUserObject(context).getMember_status() == 3) {


                        String currency = obj.getItem_currency().charAt(0) + "";
                        Intent intent = new Intent(context, OrderProcessActivity.class);
                        intent.putExtra("item_id", obj.getItem_id() + "");
                        intent.putExtra("other_item_id", obj.getOther_item_id() + "");
                        intent.putExtra("currency", currency.toLowerCase());

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                    //  Log.e("currency", obj.getOther_item_id() + " " + currency);
                }
            });


        /*    if (obj.getItem_name().equals("Silver")) {

                holder.tvPlanDuration.setTextColor(context.getResources().getColor(R.color.colorSubscriptionBlue));
                holder.tvPlanTitle.setTextColor(context.getResources().getColor(R.color.colorSubscriptionBlue));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.btPurchase.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorSubscriptionBlue));
                }
                holder.tvPlanShortDescription.setText("10 Matches / Rishtay ");
                //     holder.tvPlanShortDescriptionMatchesCount.setText("50 Matches Communication ");


            } else */

            holder.tvPhoneNumbersCount.setText(Math.round(obj.getOrder_cost())+" Verified Phone Numbers ");
            holder.tvMatchesCount.setText(Math.round(obj.getTotal_cost())+" Matches Communication ");


            if (obj.getItem_name().equals("Gold")) {

                holder.tvPlanDuration.setTextColor(context.getResources().getColor(R.color.colorSubscriptionGolden));
                holder.tvPlanTitle.setTextColor(context.getResources().getColor(R.color.colorSubscriptionGolden));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.btPurchase.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorSubscriptionGolden));
                }



                //   holder.tvPlanShortDescriptionMatchesCount.setText("100 Matches Communication ");

            } else if (obj.getItem_name().equals("Platinum")
            ) {
                holder.tvPlanDuration.setTextColor(context.getResources().getColor(R.color.colorSubscriptionPlatinum));
                holder.tvPlanTitle.setTextColor(context.getResources().getColor(R.color.colorSubscriptionPlatinum));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.btPurchase.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorSubscriptionPlatinum));
                }


                //   holder.tvPlanShortDescriptionMatchesCount.setText("150 Matches Communication ");

            }
            holder.itemView.setTag(obj);

        }
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

    public void addAll(List<Subscription> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();
        Log.e("item size in adapter", items.size() + "");
    }

    public void addItemMore(List<Subscription> lst) {
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

        public TextView tvPlanName, tvRecomm, tvPlanDuration, tvPlanTitle, tvPhoneNumbersCount,tvMatchesCount;
        //   tvPlanShortDescriptionMatchesCount

        AppCompatButton btPurchase;

        public MMViewHolder(View itemView) {
            super(itemView);
            // image = (ImageView) itemView.findViewById(R.id.ImageViewRecommendedMatchImage);

            tvPlanName = (TextView) itemView.findViewById(R.id.TextViewSubscriptionPlanName);
            tvPlanDuration = (TextView) itemView.findViewById(R.id.TextViewSubscriptionPlanDuration);
            tvPlanTitle = (TextView) itemView.findViewById(R.id.TextViewSubscriptionPlanTitleDetail);
            tvPhoneNumbersCount = (TextView) itemView.findViewById(R.id.TextViewSubscriptionPlanPhoneNumbers);
            tvMatchesCount = (TextView) itemView.findViewById(R.id.TextViewSubscriptionPlanMatchesCount);

            // tvPlanShortDescriptionMatchesCount = (TextView) itemView.findViewById(R.id.TextViewSubscriptionPlanShortDescriptionMatchesCount);

            btPurchase = (AppCompatButton) itemView.findViewById(R.id.ButtonSubscriptionPurchase);
            tvRecomm = (TextView) itemView.findViewById(R.id.TextViewRecommendedP);

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
