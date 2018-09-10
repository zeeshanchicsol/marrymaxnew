package com.chicsol.marrymax.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.WebCSC;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxAdvSearchCSCRAdapter extends RecyclerView.Adapter<CheckBoxAdvSearchCSCRAdapter.ViewHolder> implements View.OnClickListener {


    public OnCheckedChangeListener onItemClickListener;
    public ArrayList<WebCSC> webCSCs;
    private int scCheck = 0;

    //scCheck value for state is 2 and for cities is 2
    public CheckBoxAdvSearchCSCRAdapter(List<WebCSC> webCSCs, int scCheck) {
        this.webCSCs = new ArrayList<>(webCSCs);
        this.scCheck = scCheck;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adv_search_item_list, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    public void selectItem(String items) {
        if (items != null && items != "") {
            String[] visa_status_check_ids = items.split(",");
            if (visa_status_check_ids.length > 0) {
                int childcount = webCSCs.size();
                for (int i = 0; i < childcount; i++) {

                    WebCSC sv = webCSCs.get(i);

                    for (int j = 0; j < visa_status_check_ids.length; j++) {

                        if (Integer.parseInt(sv.getId()) == Integer.parseInt(visa_status_check_ids[j])) {

                            sv.setSelected(true);

                        }


                    }


                }

            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bindData(webCSCs.get(position));

        //in some cases, it will prevent unwanted situations
        holder.checkbox.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.checkbox.setChecked(webCSCs.get(position).isSelected());

      /*  if(webCSCs.get(position).isSelected()){
            onItemClickListener.onCheckedChange(getCheckedItems(), scCheck,webCSCs.get(holder.getAdapterPosition()));
        }
*/
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                webCSCs.get(holder.getAdapterPosition()).setSelected(isChecked);
                Log.e("city and id", webCSCs.get(holder.getAdapterPosition()).getName() + "  " + webCSCs.get(holder.getAdapterPosition()).getId());
                Log.e("CheckedItems", "" + getCheckedItems());
                onItemClickListener.onCheckedChange(getCheckedItems(), scCheck,webCSCs.get(holder.getAdapterPosition()));

            }
        });

        holder.itemView.setTag(webCSCs.get(position));

    }

    @Override
    public int getItemCount() {
        return webCSCs.size();
    }

    public void setOnItemClickListener(OnCheckedChangeListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {

    }

    public void updateDataList(List<WebCSC> datas) {
        webCSCs.clear();
        webCSCs.addAll(datas);
        notifyDataSetChanged();
    }

    public void clear() {
        webCSCs.clear();
        notifyDataSetChanged();
    }

    private String getCheckedItems() {
        StringBuilder stringBuilder = new StringBuilder();
        for (WebCSC webCSC : webCSCs) {
            if (webCSC.isSelected()) {
                if (stringBuilder.length() > 0)
                    stringBuilder.append(",");
                stringBuilder.append(webCSC.getId());
            }
        }
        return stringBuilder.toString();
    }


    public interface OnCheckedChangeListener {
        void onCheckedChange(String selectedCheckBoxes, int scChec, WebCSC ObjCsc);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView ONEs;
        private TextView textONEs;
        private CheckBox checkbox;

        public ViewHolder(View v) {
            super(v);
            //   ONEs = (TextView) v.findViewById(R.id.TextViewItemAdvSearchId);
            textONEs = (TextView) v.findViewById(R.id.TextViewItemAdvSearchName);
            checkbox = (CheckBox) v.findViewById(R.id.checkboxItemAdvSearch);
        }

        public void bindData(WebCSC webCSC) {
            // ONEs.setText(webCSC.getId());
            textONEs.setText(webCSC.getName());
        }
    }

}