package com.chicsol.marrymax.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.mAdvSearchListing;

import java.util.List;

public class AdvSearchAdapter extends ArrayAdapter<mAdvSearchListing> {

    Context context;
    int layoutResourceId;
    List<mAdvSearchListing> data = null;

    public AdvSearchAdapter(Context context, int layoutResourceId, List<mAdvSearchListing> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public void reset() {
        ///  Toast.makeText(context, "Reset Done", Toast.LENGTH_SHORT).show();
        //     Log.e("Reset","resetttttttttttt");

        //    Log.e("Reset","resetttttttttttt");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new WeatherHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.ImageViewAdvSearchIcon);

            holder.viewCircleHeight = (View) row.findViewById(R.id.ViewCircleHeight);
            // holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);


            row.setTag(holder);
        } else {
            holder = (WeatherHolder) row.getTag();
        }

        mAdvSearchListing item = data.get(position);
        // holder.txtTitle.setText(item_slider.name);
        holder.imgIcon.setImageResource(item.icon);

        if (item.isDataSelection()) {
            holder.viewCircleHeight.setVisibility(View.VISIBLE);
        }else {
            holder.viewCircleHeight.setVisibility(View.GONE);
        }

        return row;
    }

    static class WeatherHolder {
        ImageView imgIcon;
        TextView txtTitle;
        View viewCircleHeight;

    }
}