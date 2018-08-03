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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebCSC;

import java.util.List;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;

public class RecyclerViewAdapterWhoIsLookingForMe extends RecyclerView.Adapter<RecyclerViewAdapterWhoIsLookingForMe.ViewHolder> {

    Context context;


    private List<List<WebCSC>> items;
//    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapterWhoIsLookingForMe(List<List<WebCSC>> items, final Context context) {
        this.items = items;
        this.context = context;
    }

/*    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_whoislookingforme, parent, false);
        return new ViewHolder(v);
        //  return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        List<WebCSC> dataList = items.get(position);

        MySpinnerCSCAdapter spAdapter = new MySpinnerCSCAdapter(context, android.R.layout.simple_spinner_dropdown_item, dataList);

        holder.spWhoIs.setAdapter(spAdapter);
        holder.spWhoIs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                WebCSC selectedItem = (WebCSC) holder.spWhoIs.getSelectedItem();
             //   Toast.makeText(context, "" + selectedItem.getName(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



     /*   Members obj = items.get(position);
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
        holder.itemView.setTag(obj);*/

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
        //  public ImageView icStatus;
        public TextView title;
        public Spinner spWhoIs;

        public ViewHolder(View itemView) {
            super(itemView);
            spWhoIs = (Spinner) itemView.findViewById(R.id.SpinnerWhoIsLooking);
            title = (TextView) itemView.findViewById(R.id.TextViewWhoIsTitle);


        }
    }

    public void addAll(List<List<WebCSC>> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();

    }

}
