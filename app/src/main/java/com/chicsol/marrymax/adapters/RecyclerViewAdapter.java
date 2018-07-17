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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.widgets.mTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ImageLoader imageLoader;
    Context context;
    private DisplayImageOptions options;
    private LayoutInflater inflater;

    private List<Members> items;
    private OnItemClickListener onItemClickListener;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public OnLoadMoreListener onLoadMoreListener;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;

    private GridLayoutManager mLinearLayoutManager;

    public Members getMemResultsObj() {
        return memResultsObj;
    }

    public void setMemResultsObj(Members memResultsObj) {
        this.memResultsObj = memResultsObj;
    }

    private Members memResultsObj;

    public RecyclerViewAdapter(final Context context, OnLoadMoreListener onLoadMoreListener) {
        items = new ArrayList<>();
        this.context = context;
        this.onLoadMoreListener = onLoadMoreListener;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        inflater = LayoutInflater.from(context);

        options = new DisplayImageOptions.Builder()
                //   .showImageOnLoading(resize(R.drawable.loading_sm))
                // .showImageOnLoading(resize(R.drawable.loading))
                // .showImageForEmptyUri(resize(R.drawable.oops_sm))
                // .showImageForEmptyUri(resize(R.drawable.no_image))
                //.showImageOnFail(resize(R.drawable.oops_sm))
                .cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)

                ./*postProcessor(new BitmapProcessor() {
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
                            int h = (int) (heightScreen * 0.080);//it set the height of image 10% of your screen
                            //   iv.getLayoutParams().width = (int) (widthScreen * 0.15);
                            bmp_sticker = resizeImage(bmp, h);
                            // Log.e("wid " + widthScreen + "  " + heightScreen, "");
                        }

                        return bmp_sticker;
                    }
                }).*/build();


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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //   View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_member_dash_main_new, parent, false);


        if (viewType == VIEW_ITEM) {
            return new ViewHolderM(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_member_dash_main_new, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false));
        }


        //  return new ViewHolder(v);


        //  return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {
        final Members item = items.get(position);
        // holder.position=position;
        if (holder1 instanceof ViewHolderM) {

            final ViewHolderM holder = ((ViewHolderM) holder1);
            holder.alias.setText(item.getAlias());
            holder.age.setText("( " + item.get_age() + " )");
            holder.eduMaritalStatus.setText(item.get_education_types() + " | " + item.get_marital_status_types());
            holder.country.setText(item.get_country_name());

            holder.aboutme.setText(item.get_about_member());


            imageLoader.displayImage(Urls.baseUrl + "/" + item.get_default_image(),
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
            holder.llCardMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //    Toast.makeText(context, "clicked"+position, Toast.LENGTH_SHORT).show();
                    Log.e("getMoreLoading rec", getMoreLoading() + "");

                    if (!isMoreLoading) {
                        onItemClickListener.onItemClick(v, item, position, items,memResultsObj);
                    } else {

                        Toast.makeText(context, "Matches are loading try again ", Toast.LENGTH_SHORT).show();
                    }


                }
            });


            holder.itemView.setTag(item);

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

   /* @Override
    public void onClick(final View v) {

        onItemClickListener.onItemClick(v, (Members) v.getTag(),v.getTag().pos );
    }*/

    public interface OnItemClickListener {

        void onItemClick(View view, Members members, int position, List<Members> items,Members memResultsObj);

    }

    protected static class ViewHolderM extends RecyclerView.ViewHolder {
        public ImageView image;
        public mTextView alias, aboutme;
        public mTextView age;
        public mTextView eduMaritalStatus;
        public mTextView country;
        public int position;
        public LinearLayout llCardMain;


        public ViewHolderM(View itemView) {
            super(itemView);
            llCardMain = (LinearLayout) itemView.findViewById(R.id.LinearLayoutRecyclerMemSinceLastWeek);
            image = (ImageView) itemView.findViewById(R.id.ImageViewMemberImageDashMain);
            alias = (mTextView) itemView.findViewById(R.id.TextViewMemberAliasDashMain);
            age = (mTextView) itemView.findViewById(R.id.TextViewMemberAgeDashMain);
            eduMaritalStatus = (mTextView) itemView.findViewById(R.id.TextViewMemberEducationMartialStatusDashMain);
            country = (mTextView) itemView.findViewById(R.id.TextViewMemberCountryDashMain);
            aboutme = (mTextView) itemView.findViewById(R.id.TextViewMemberAboutMe);

        }
    }

    public interface OnLoadMoreListener {

        void onLoadMore();
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
                    isMoreLoading = true;
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }

                }
            }
        });
    }

    public void setLinearLayoutManager(GridLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;

        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.pBar);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
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

    public boolean getMoreLoading() {
        return this.isMoreLoading;
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Members> lst) {
       items.clear();
        items.addAll(lst);
        notifyDataSetChanged();
      //  Log.e("item size in adapter", items.size() + "");
    }

    public void addItemMore(List<Members> lst) {
        items.addAll(lst);
        notifyItemRangeChanged(0, items.size());
    }
}
