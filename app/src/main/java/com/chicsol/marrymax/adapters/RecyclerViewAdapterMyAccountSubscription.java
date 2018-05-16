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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Members;

import java.util.List;

public class RecyclerViewAdapterMyAccountSubscription extends RecyclerView.Adapter<RecyclerViewAdapterMyAccountSubscription.ViewHolder> {

    Context context;


    private List<Members> items;
//    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapterMyAccountSubscription(List<Members> items, final Context context) {
        this.items = items;
        this.context = context;
    }

/*    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_mysubscription, parent, false);
        return new ViewHolder(v);
        //  return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Members obj = items.get(position);
        holder.title.setText(obj.get_name());
        holder.startDate.setText(obj.get_start_date());
        holder.endDate.setText(obj.get_end_date());

        if (obj.get_member_status() == 1) {
            holder.statusText.setText("Active");
            holder.icStatus.setImageResource(R.drawable.ic_check_circle_black_24dp);

        } else {
            holder.statusText.setText("Expired");
            holder.icStatus.setImageResource(R.drawable.ic_highlight_off_black_18dp);

        }
        holder.itemView.setTag(obj);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

/*    @Override
    public void onClick(final View v) {
        onItemClickListener.onItemClick(v, (Members) v.getTag());
    }*/

 /*   public interface OnItemClickListener {

        void onItemClick(View view, Members members);

    }*/

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icStatus;
        public TextView title, startDate, endDate, statusText;

        public ViewHolder(View itemView) {
            super(itemView);
            icStatus = (ImageView) itemView.findViewById(R.id.ImageViewAccountSettingMySubsActive);
            title = (TextView) itemView.findViewById(R.id.TextViewASMySubPackageTitle);
            startDate = (TextView) itemView.findViewById(R.id.TextViewASMySubStartDate);
            endDate = (TextView) itemView.findViewById(R.id.TextViewASMySubEndDate);
            statusText = (TextView) itemView.findViewById(R.id.TextViewASMySubStatusText);


        }
    }

    public void addAll(List<Members> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();

    }

}
