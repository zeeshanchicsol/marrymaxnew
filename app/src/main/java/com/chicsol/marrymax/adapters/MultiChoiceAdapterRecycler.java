package com.chicsol.marrymax.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.WebArd;

import java.util.List;

/**
 * Created by Android on 11/14/2016.
 */

public class MultiChoiceAdapterRecycler extends RecyclerView.Adapter<MultiChoiceAdapterRecycler.ViewHolder> {
    // Initialise custom font, for example:
    String SelectedID = null;

    //List<WebArd> eduC;
    private static LayoutInflater inflater = null;
    List<WebArd> items;
    Context context;

    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

    public MultiChoiceAdapterRecycler(Context context, List<WebArd> items) {

        this.items = items;
    /*    if (checkedList.size() == 0) {
            this.checkedList = new ArrayList<>();

        } else {
            this.checkedList = checkedList;
        }*/
        this.context = context;
    }


    public void updateDataList(List<WebArd> newlist) {
        items.clear();
        items.addAll(newlist);
        this.notifyDataSetChanged();
    }


    /*    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WebArd webArd = items.get(position);
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

            return vi;
        }*/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_choice, parent, false);
        // v.setOnClickListener(this);
/*
        int w = parent.getMeasuredWidth() / 2;
        // Log.e("wwww",w+"");
        v.setMinimumWidth(w - 20);*/
        return new ViewHolder(v);


        //  return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final WebArd webArd = items.get(position);
        // holder.position=position;

        holder.tvTitle.setText(webArd.getName());
        holder.cbMain.setTag(webArd);

            holder.cbMain.setChecked(webArd.isSelected());

   /*     if (checkedList.size() > 0) {
            holder.cbMain.setChecked(false);
            for (int i = 0; i < checkedList.size(); i++) {
                if (Integer.parseInt(webArd.getId()) == Integer.parseInt(checkedList.get(i))) {
                    //Log.e();

                    //   view.setChecked(true);
                    // view.setSelected(true);
                    //view.setSelected(true);
                    //   view.toggle();
                    //  view.setBackgroundColor(getContext().getResources().getColor(R.color.colorTextRed));
                    //  Log.e(webArd.getId() + "======" + webArd.getName() + "  checked", "checked    " + checkedList.get(i));
                    holder.cbMain.setChecked(true);

                }
            }

        }
*/

        holder.cbMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                WebArd obj = (WebArd) cb.getTag();
                obj.setSelected(cb.isChecked());
                items.get(position).setSelected(cb.isChecked());
            }
        });
       /* holder.cbMain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                WebArd obj = (WebArd) compoundButton.getTag();

                obj.setSelected(b);
                items.get(position).setSelected(b);


            }
        });*/

        holder.itemView.setTag(webArd);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


   public List<WebArd> getSelection() {

        return items;

    }

  /*  @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            checkedList.add(compoundButton.getTag().toString());
            //   Log.e("added", "added");

         *//*   for (int i = 0; i < checkedList.size(); i++) {
                Log.e("array after", checkedList.get(i).toString());
            }*//*

        } else {

            checkedList.remove(compoundButton.getTag());
            //Log.e("removed", "removed");
            for (int i = 0; i < checkedList.size(); i++) {
                Log.e("array after", checkedList.get(i).toString());
            }
            Log.e("======", "=========="+compoundButton.getTag()+"==");
        }

    }
*/

   /* @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Toast.makeText(context, "=" + b, Toast.LENGTH_SHORT).show();




}*/


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

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        //    public CheckedTextView checkedTextView;
        public TextView tvTitle;
        public CheckBox cbMain;


        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.TextViewMultiChoice);
            cbMain = (CheckBox) itemView.findViewById(R.id.CheckBoxMultiChoice);
            // checkedTextView = (CheckedTextView) itemView.findViewById(R.id.CheckedTextMulti);

        }
    }
}