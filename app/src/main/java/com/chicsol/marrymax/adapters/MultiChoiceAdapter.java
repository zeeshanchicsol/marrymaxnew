package com.chicsol.marrymax.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.WebArd;

import java.util.List;

/**
 * Created by Android on 11/14/2016.
 */

public class MultiChoiceAdapter extends BaseAdapter {
    // Initialise custom font, for example:
    String SelectedID = null;

    List<String> checkedList;
    private static LayoutInflater inflater = null;
    List<WebArd> itemslist;
    Context context;
    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

    public MultiChoiceAdapter(Context context, int resource, List<WebArd> items, List<String> checkedList) {

        this.itemslist = items;
        this.checkedList = checkedList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void updateDataList(List<WebArd> newlist) {
        itemslist.clear();
        itemslist.addAll(newlist);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemslist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WebArd webArd = itemslist.get(position);
        View vi = convertView;

        if (convertView == null)
            vi = inflater.inflate(R.layout.item_multi_choice, null);


        // CheckedTextView view = (CheckedTextView) super.getView(position, convertView, parent);

        CheckedTextView checkedTextView = (CheckedTextView) vi.findViewById(R.id.CheckedTextMulti);


        checkedTextView.setText(webArd.getName());
        checkedTextView.setTag(webArd.getId());



        if (checkedList.size() > 0) {
            for (int i = 0; i < checkedList.size(); i++) {
                if (Integer.parseInt(webArd.getId()) == Integer.parseInt(checkedList.get(i))) {
                    //Log.e();

                    //   view.setChecked(true);
                    // view.setSelected(true);
                    //view.setSelected(true);
                    //   view.toggle();
                    //  view.setBackgroundColor(getContext().getResources().getColor(R.color.colorTextRed));
                  Log.e(webArd.getId()+"======"+webArd.getName()+"  checked", "checked    "+checkedList.get(i));
                    ((ListView) parent).setItemChecked(i, true);

                }
            }

        }
        vi.setTag(webArd);
        return vi;
    }


    // Affects opened state of the spinner
  /*  @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);

        view.setText(items.get(position).getName());
  *//*      if (position == 0) {
            view.setTextColor(getContext().getResources().getColor(R.color.colorTextRed));
        }*//*
        return view;
    }*/
}