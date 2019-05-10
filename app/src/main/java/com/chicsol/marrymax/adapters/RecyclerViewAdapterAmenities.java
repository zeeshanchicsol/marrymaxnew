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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.chicsol.marrymax.R;

import java.util.HashMap;
import java.util.List;

public class RecyclerViewAdapterAmenities extends RecyclerView.Adapter<RecyclerViewAdapterAmenities.ViewHolder> {


    private List<String> items;
    //  private OnItemClickListener onItemClickListener;
    String articleid;
    String rcType;
    private Context context;
    private HashMap<String, String> cMap;
    private HashMap<String, String> pMap;

    //public RecyclerViewAdapterAmenities(List<String> items, Context context, String rctype, HashMap<String, String> cMap, HashMap<String, String> pMap) {
    public RecyclerViewAdapterAmenities(List<String> items, Context context) {
        this.items = items;
        this.context = context;
        //       this.rcType = rctype;
/*        this.cMap = cMap;
        this.pMap = pMap;*/
        //Log.e("rcType", "" + rcType);


    }

   /* public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_amenitie, parent, false);
        //    v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String obj = items.get(position);
        //  obj);
        holder.amName.setText(obj);
     /*   if (rcType.equals("R")) {
            if (pMap.get(obj) != null) {
                holder.amName.setText(pMap.get(obj));
            }

        } else {
            if (cMap.get(obj) != null) {
                holder.amName.setText(cMap.get(obj));
            }
        }*/


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        /* public ImageView image;
        public TextView articleno;
       */ public TextView amName;

        /* public TextView articleid;
         public ImageButton btFavourite;
 */
        public ViewHolder(View itemView) {
            super(itemView);
            // image = (ImageView) itemView.findViewById(R.id.ImageViewSubCategory);
            //   articleno= (TextView) itemView.findViewById(R.id.articleno);
            amName = (TextView) itemView.findViewById(R.id.TextViewAmenityName);
            //  articleid = (TextView) itemView.findViewById(R.id.id);
            //  btFavourite = (AppCompatImageButton) itemView.findViewById(R.id.ImageButtonFavourite);
        }
    }

    //listener ko class m handle krny k lie ye interface h . dkho iska object oper hga sort of class
  /*  public interface OnItemClickListener {

        void onItemClick(View view, String modelArticle);

        void onItemSavedUnSaved(String updated);

    }*/


    public void addAll(List<String> items2) {
        items.clear();
        items.addAll(items2);
        notifyDataSetChanged();

    }

}
