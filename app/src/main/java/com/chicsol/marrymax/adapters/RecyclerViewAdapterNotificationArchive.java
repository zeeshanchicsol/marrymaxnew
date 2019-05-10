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
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.mProperties;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.widgets.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterNotificationArchive extends RecyclerView.Adapter<RecyclerViewAdapterNotificationArchive.MMViewHolder> implements View.OnClickListener {

    public ImageLoader imageLoader;

    Context context;
    FragmentManager frgMngr;
    private OnItemClickListener onItemClickListener;
    private DisplayImageOptions options, optionsNormalImage;
    private LayoutInflater inflater;
    private List<mProperties> items;

    private LinearLayoutManager mLinearLayoutManager;
    private Fragment fragment;
    MarryMax marryMax;

    public RecyclerViewAdapterNotificationArchive(final Context context) {
        //this.items = items;
        marryMax = new MarryMax(null);
        this.fragment = fragment;
        this.context = context;
        //   this.frgMngr = frg;
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

                        //  Log.e("bitmap width height", bmp.getWidth() + "" + bmp.getHeight());

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

                            int h = (int) (heightScreen * 0.050);//it set the height of image 10% of your screen

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
                // .cacheOnDisk(true)
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
                            int h = (int) (heightScreen * 0.020);//it set the height of image 10% of your screen
                            //   iv.getLayoutParams().width = (int) (widthScreen * 0.15);
                            bmp_sticker = resizeImage(bmp, h);
                            //Log.e("wid " + widthScreen + "  " + heightScreen, "");
                        }

                        return bmp_sticker;
                    }
                }).build();

        items = new ArrayList<>();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
    public MMViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_list_notification_archive, parent, false);
        v.setOnClickListener(this);

/*        int w = parent.getMeasuredWidth() / 2;
        // Log.e("wwww",w+"");
        v.setMinimumWidth(w - 20);*/
        return new MMViewHolder(v);
/*
        if (viewType == VIEW_ITEM) {
            return new MMViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_list_my_inbox, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false));
        }*/

    }


    @Override
    public void onBindViewHolder(MMViewHolder holder, int position) {
        final mProperties member = items.get(position);
        //  Log.e("date is",""+member.getJoined_date());

        holder.tvDate.setText(" (" + marryMax.convertUTCTimeToLocalWithAgo(member.getJoined_date()) + ")");
        holder.tvDesc.setText(member.getName());
        holder.tvUsername.setText(member.getAlias());


//========Phone View========================

        //   imageLoader.displayImage(Urls.baseUrl + "/images/flags/" + member.getDefault_image() + ".gif", holder.ivCountryFlag, optionsNormalImage);
        imageLoader.displayImage(Urls.baseUrl + "/" + member.getDefault_image(),
                holder.ivUserImage, options,
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


        holder.itemView.setTag(member);


    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<mProperties> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();
        //Log.e("item size in adapter", items.size() + "");
    }

    public void addItemMore(List<mProperties> lst) {
        items.addAll(lst);
        notifyItemRangeChanged(0, items.size());
    }

    @Override
    public void onClick(View v) {
        onItemClickListener.onItemClick(v, (mProperties) v.getTag());
    }


    protected static class MMViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDate, tvUsername, tvDesc;
        public RoundedImageView ivUserImage;

        public LinearLayoutCompat bt_ShowInterest, ll_user_profile, ll_image_count;
        //    public mTextView country;

        public MMViewHolder(View itemView) {
            super(itemView);

            ivUserImage = (RoundedImageView) itemView.findViewById(R.id.RoundedImageViewNotiArchiveUserImage);
            tvDate = (TextView) itemView.findViewById(R.id.TextViewNotiArchiveUserDate);
            tvUsername = (TextView) itemView.findViewById(R.id.TextViewNotiArchiveUserAlias);
            tvDesc = (TextView) itemView.findViewById(R.id.TextViewNotiArchiveUserName);

        }
    }


    public interface OnItemClickListener {

        void onItemClick(View view, mProperties communication);

    }
}
