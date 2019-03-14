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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.modal.cModel;
import com.chicsol.marrymax.modal.mCommunication;
import com.chicsol.marrymax.modal.mIceBreak;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.utils.ViewGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecyclerViewAdapterFeedbackAnswerDetails extends RecyclerView.Adapter<RecyclerViewAdapterFeedbackAnswerDetails.MMViewHolder> implements View.OnClickListener {


    Context context;
    FragmentManager frgMngr;
    private OnItemClickListener onItemClickListener;


    private List<cModel> items;

    private LinearLayoutManager mLinearLayoutManager;
    private Fragment fragment;
    MarryMax marryMax;


    HashMap<String, String> mCheckedAnswersList;
    ViewGenerator viewGenerator;

    /* public void setQuestionChoiceList(List<WebArd> questionChoiceList) {
          QuestionChoiceList = questionChoiceList;
      }
  */
    List<List<mIceBreak>> QuestionChoiceList;

    public RecyclerViewAdapterFeedbackAnswerDetails(final Context context) {
        //this.items = items;
        mCheckedAnswersList = new HashMap<>();
        Activity activity = (Activity) context;
        marryMax = new MarryMax(activity);
        this.fragment = fragment;
        this.context = context;
        viewGenerator = new ViewGenerator(this.context);
        //   this.frgMngr = frg;


        items = new ArrayList<>();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




 /*   public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }*/

    @Override
    public MMViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_list_feedback, parent, false);
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
        final cModel obj = items.get(position);
        holder.tvQuestionTitle.setText(obj.getName());
       /* int q = position + 1;


        if (!obj.getAnswer().equals("")) {
            holder.tvAnswer.setVisibility(View.VISIBLE);
            holder.tvAnswer.setText("Ans : " + obj.getAnswer());

        } else {
            holder.tvAnswer.setVisibility(View.GONE);
        }

        */
        holder.rgMain.setVisibility(View.GONE);
        holder.tvAnswer.setVisibility(View.VISIBLE);
        holder.tvAnswer.setText(obj.getCode());

     /*   holder.rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                int idx = group.indexOfChild(radioButton);

                RadioButton r = (RadioButton) group.getChildAt(idx);

                //     Toast.makeText(context, obj.getId() + "  "+r.getTag(), Toast.LENGTH_SHORT).show();

                mCheckedAnswersList.put(obj.getId(), r.getTag() + "");
            }
        });*/

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

    public void addAll(List<cModel> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();

      //  Log.e("item size in adapter", items.size() + "");
    }

    public void addItemMore(List<cModel> lst) {
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

    public HashMap<String, String> getmCheckedAnswersList() {
        return mCheckedAnswersList;
    }
}
