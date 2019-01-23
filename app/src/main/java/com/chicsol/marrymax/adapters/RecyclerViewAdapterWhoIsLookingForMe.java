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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebCSC;
import com.chicsol.marrymax.modal.WebCSCWithList;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;

public class RecyclerViewAdapterWhoIsLookingForMe extends RecyclerView.Adapter<RecyclerViewAdapterWhoIsLookingForMe.ViewHolder> {

    Context context;
    Gson gson;

    ViewGenerator viewGenerator;
    private List<WebCSCWithList> items;
//    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapterWhoIsLookingForMe(List<WebCSCWithList> items, final Context context) {
        this.items = items;
        this.context = context;
        gson = new Gson();
        viewGenerator = new ViewGenerator(context);

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

        final String idkey = items.get(position).getId();
        final String name = items.get(position).getName();
        List<WebCSC> dataList = items.get(position).getList();
        holder.title.setText(name);


        MySpinnerCSCAdapter spAdapter = new MySpinnerCSCAdapter(context, android.R.layout.simple_spinner_dropdown_item, dataList);

        holder.spWhoIs.setAdapter(spAdapter);
        holder.spWhoIs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                WebCSC obj = (WebCSC) holder.spWhoIs.getSelectedItem();

                if (!obj.getId().equals("-1")) {
                    //  Toast.makeText(context, "" + obj.getId() + "   =====   " + name, Toast.LENGTH_SHORT).show();

                    String obString = gson.toJson(defaultSelectionsObj);
                    try {
                        JSONObject jsonObject = new JSONObject(obString);

                        if(idkey.equals("gender")){

                            if(obj.getId().equals("0")){
                                jsonObject.put(idkey, "M");
                            }else {
                                jsonObject.put(idkey, "F");
                            }

                        }else {
                            jsonObject.put(idkey, obj.getId());
                        }

                       // Log.e("gender  " + idkey, obj.getId());

                        defaultSelectionsObj = gson.fromJson(jsonObject.toString(), Members.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //   Toast.makeText(context, "" + selectedItem.getName(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (idkey.equals("religion_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_religion_id(), dataList);
        } else if (idkey.equals("gender")) {
            if (defaultSelectionsObj.get_gender().equals("M")) {
                holder.spWhoIs.setSelection(1);
            } else {
                holder.spWhoIs.setSelection(2);
            }


        } else if (idkey.equals("min_age")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_min_age(), dataList);
        } else if (idkey.equals("country_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_country_id(), dataList);
        } else if (idkey.equals("visa_status_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_visa_status_id(), dataList);
        } else if (idkey.equals("body_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_body_id(), dataList);
        } else if (idkey.equals("complexion_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_complexion_id(), dataList);
        } else if (idkey.equals("height_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_height_id(), dataList);
        } else if (idkey.equals("education_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_education_id(), dataList);
        } else if (idkey.equals("ethnic_background_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_ethnic_background_id(), dataList);
        } else if (idkey.equals("religious_sect_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_religious_sect_id(), dataList);
        } else if (idkey.equals("marital_status_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_marital_status_id(), dataList);
        } else if (idkey.equals("children_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_children_id(), dataList);
        } else if (idkey.equals("living_arrangement_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_living_arrangement_id(), dataList);
        } else if (idkey.equals("hijab_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_hijab_id(), dataList);
        } else if (idkey.equals("raised_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_raised_id(), dataList);
        } else if (idkey.equals("smoking_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_smoking_id(), dataList);
        } else if (idkey.equals("drink_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.get_drink_id(), dataList);
        }
        else if (idkey.equals("physic_id")) {
            viewGenerator.selectSpinnerItemByIdWebCSC(holder.spWhoIs, defaultSelectionsObj.getPhysic_id(), dataList);
        }

        //



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

    public void addAll(List<WebCSCWithList> lst) {
        items.clear();
        items.addAll(lst);
        notifyDataSetChanged();

    }

}
