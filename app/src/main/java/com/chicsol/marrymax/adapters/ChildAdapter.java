package com.chicsol.marrymax.adapters;

/**
 * Created by macintoshhd on 12/26/17.
 */

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.mChild;
import com.chicsol.marrymax.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<mChild> childData;
    private ArrayList<mChild> childDataBk;
    private String title;
    int pos;


    public ChildAdapter(ArrayList<mChild> childData, String title, int pos) {
        // this.childData = childData;

        this.childData = new ArrayList<>();
        mChild child = new mChild();
        child.setName("head a");
        this.childData.add(child);
        this.title = title;
        this.pos = pos;

        childDataBk = new ArrayList<>(childData);
        childDataBk.add(0, child);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nested_recycler_item_child, parent, false);

        ChildAdapter.ViewHolder cavh = new ChildAdapter.ViewHolder(itemLayoutView);
        return cavh;
    }


    final Handler handler = new Handler();
    Runnable collapseList = new Runnable() {
        @Override
        public void run() {
            if (getItemCount() > 1) {
                childData.remove(1);
                notifyDataSetChanged();
                handler.postDelayed(collapseList, 0);
            }
        }
    };

    Runnable expandList = new Runnable() {
        @Override
        public void run() {
            int currSize = childData.size();
            if (currSize == childDataBk.size()) return;

            if (currSize == 0) {
                childData.add(childDataBk.get(currSize));

            } else {
                childData.add(childDataBk.get(currSize));
            }
            notifyDataSetChanged();

            handler.postDelayed(expandList, 0);
        }
    };


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder vh = (ViewHolder) holder;

        if (position == 0 && getItemCount() == 1) {
            //    vh.rlHead.setVisibility(View.VISIBLE);
            //   vh.tvExpandCollapseToggle.setImageResource(R.drawable.ic_expand_more_white_24dp);
            //  vh.tvExpandCollapseToggle.setVisibility(View.VISIBLE);
        } else if (position == childDataBk.size() - 1) {
            //   vh.tvExpandCollapseToggle.setImageResource(R.drawable.ic_expand_less_white_24dp);
            //  vh.tvExpandCollapseToggle.setVisibility(View.VISIBLE);
        } else {
            //  vh.tvExpandCollapseToggle.setVisibility(View.GONE);
            //  vh.rlHead.setVisibility(View.GONE);
        }

        mChild c = childData.get(position);
        if (position == 0) {

            vh.rlHead.setVisibility(View.VISIBLE);
            vh.llContent.setVisibility(View.GONE);
        } else {
            vh.rlHead.setVisibility(View.GONE);
            vh.llContent.setVisibility(View.VISIBLE);


            holder.itemView.setTag(c);


        }


        vh.tvChild.setText(c.getName());
        //  vh.tvChild.check

        vh.tvTitle.setText(title);


        vh.rlHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItemCount() > 1) {
                    // vh.tvChild.setVisibility(View.VISIBLE);
                    handler.post(collapseList);
                } else {
                    //  vh.tvChild.setVisibility(View.GONE);
                    handler.post(expandList);
                }
            }
        });


        vh.cb.setChecked(Constants.selectedQuestions.get(pos));

        vh.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (Constants.selectedQuestions.size() > 5) {
                    Toast.makeText(buttonView.getContext(), "You have selected 5 question(s).You can not select more than 5 questions", Toast.LENGTH_SHORT).show();
                    buttonView.setChecked(!isChecked);
                } else {

                    Constants.selectedQuestions.append(pos, isChecked);
               /*     for (int i = 0; i < Constants.selectedQuestions.size(); i++) {
                        int key = Constants.selectedQuestions.keyAt(i);
                        Log.e("" + key, Constants.selectedQuestions.get(key) + "");
                    }*/
                }

            }
        });


        vh.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mChild cOb = (mChild) holder.itemView.getTag();


            }
        });


    }

    @Override
    public int getItemCount() {
        return childData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_child)
        TextView tvChild;
       /* @BindView(R.listid.iv_expand_collapse_toggle)
        ImageView tvExpandCollapseToggle;*/

        @BindView(R.id.RelativeLayoutHeadSubItem)
        LinearLayout rlHead;

        @BindView(R.id.LinearLayoutContent)
        LinearLayout llContent;

        @BindView(R.id.TextViewQuestionTitle)
        TextView tvTitle;


        @BindView(R.id.CheckboxQuestionTitle)
        CheckBox cb;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}