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
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.mCommunication;
import com.chicsol.marrymax.modal.mIceBreak;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ViewGenerator;
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

public class RecyclerViewAdapterQuestionsList extends RecyclerView.Adapter<RecyclerViewAdapterQuestionsList.MMViewHolder> implements View.OnClickListener {

    public ImageLoader imageLoader;
    Context context;
    FragmentManager frgMngr;
    private OnItemClickListener onItemClickListener;
    private DisplayImageOptions options, optionsNormalImage;
    private LayoutInflater inflater;
    private List<mIceBreak> items;

    private LinearLayoutManager mLinearLayoutManager;
    private Fragment fragment;
    MarryMax marryMax;

    ViewGenerator viewGenerator;

    public void setQuestionChoiceList(List<List<mIceBreak>> questionChoiceList) {
        QuestionChoiceList = questionChoiceList;
    }

    List<List<mIceBreak>> QuestionChoiceList;

    public RecyclerViewAdapterQuestionsList(final Context context, Activity activity) {
        //this.items = items;


        marryMax = new MarryMax(activity);
        this.fragment = fragment;
        this.context = context;
        viewGenerator = new ViewGenerator(this.context);
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
                            int h = (int) (heightScreen * 0.15);//it set the height of image 10% of your screen
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_list_question_answers, parent, false);
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
        final mIceBreak obj = items.get(position);

        holder.tvQuestionTitle.setText(obj.getQuestion());
        List<mIceBreak> mChoiceList = QuestionChoiceList.get(position);

        viewGenerator.generateQuestionChoices(mChoiceList, holder.rgMain);

        //   Log.e("messagee", obj.getMessage() + "==" + obj.self);
        //holder.tvText1.setText(Html.fromHtml(obj.getMessage()));
       /* if (obj.self == 0) {

            holder.tvText.setText(Html.fromHtml(obj.getMessage()));

            holder.tvDate.setText(marryMax.convertUTCTimeToLocal(obj.getMessage_date()));
            // holder.tvCountry.setText(member.getCountry_name());


            holder.ll_to.setVisibility(View.VISIBLE);
            holder.ll_self.setVisibility(View.GONE);

        } else {
            holder.tvText1.setText(Html.fromHtml(obj.getMessage()));

            holder.tvDate1.setText(marryMax.convertUTCTimeToLocal(obj.getMessage_date()));
            // holder.tvCountry.setText(member.getCountry_name());


            holder.ll_self.setVisibility(View.VISIBLE);
            holder.ll_to.setVisibility(View.GONE);
        }*/

        holder.itemView.setTag(obj);


    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<mIceBreak> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();

        Log.e("item size in adapter", items.size() + "");
    }

    public void addItemMore(List<mIceBreak
            > lst) {
        items.addAll(lst);
        notifyItemRangeChanged(0, items.size());

    }

    @Override
    public void onClick(View v) {
       // onItemClickListener.onItemClick(v, (mCommunication) v.getTag());
    }


    protected static class MMViewHolder extends RecyclerView.ViewHolder {

        public TextView tvQuestionTitle, tvAnswer;


        public RadioGroup rgMain;


        public MMViewHolder(View itemView) {
            super(itemView);

            tvQuestionTitle = (TextView) itemView.findViewById(R.id.TextViewQuestion);
            tvAnswer = (TextView) itemView.findViewById(R.id.TextViewQuestionAnswer);
            rgMain = (RadioGroup) itemView.findViewById(R.id.RadioGroupQuestionAnswers);

          /*  ll_self = (LinearLayout) itemView.findViewById(R.id.LinearlayoutChatListSelf);
            ll_to = (LinearLayout) itemView.findViewById(R.id.LinearlayoutChatListTo);

            ivUserImage = (RoundedImageView) itemView.findViewById(R.id.RoundedImageViewChatListUserImage);
            tvDate = (TextView) itemView.findViewById(R.id.TextViewChatListDate);
            tvText = (TextView) itemView.findViewById(R.id.TextViewChatListText);

            ivUserImage1 = (RoundedImageView) itemView.findViewById(R.id.RoundedImageViewChatListUserImage1);
            tvDate1 = (TextView) itemView.findViewById(R.id.TextViewChatListDate1);
            tvText1 = (TextView) itemView.findViewById(R.id.TextViewChatListText1);*/

           /* tvDesc = (TextView) itemView.findViewById(R.id.TextViewInboxListAge);
            tvEducation = (TextView) itemView.findViewById(R.id.TextViewInboxListEducation);
            tvCountry = (TextView) itemView.findViewById(R.id.TextViewInboxListCountry);*/

        }
    }


    public interface OnItemClickListener {

        void onItemClick(View view, mCommunication communication);

    }


}
