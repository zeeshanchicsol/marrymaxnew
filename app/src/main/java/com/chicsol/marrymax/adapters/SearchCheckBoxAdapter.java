package com.chicsol.marrymax.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.WebArd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 16/11/17.
 */

public class SearchCheckBoxAdapter extends RecyclerView.Adapter<SearchCheckBoxAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<WebArd> contactList;

    private List<WebArd> contactListFiltered;
    private ContactsAdapterListener listener;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //   public TextView name, phone;
        //       public ImageView thumbnail;
        public CheckBox cbMain;

        public MyViewHolder(View view) {
            super(view);
            cbMain = view.findViewById(R.id.checkboxItemAdvSearchMainCB);



      /*      view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });*/
        }
    }


    public SearchCheckBoxAdapter(Context context, List<WebArd> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
        this.itemStateArray = new SparseBooleanArray(contactList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_search_checkboxes, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WebArd obj = contactListFiltered.get(position);
        holder.cbMain.setText(obj.getName());

        holder.cbMain.setId(Integer.parseInt(obj.getId()));

        holder.cbMain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //  Log.e("CompoundButton", isChecked + "  ====  " + buttonView.getId());
                obj.setSelected(isChecked);


                listener.onContactSelected(contactList);


            }
        });

        if (obj.isSelected()) {
            holder.cbMain.setChecked(true);
        } else {
            holder.cbMain.setChecked(false);
        }

        //   holder.phone.setText(contact.getPhone());

     /*   Glide.with(context)
                .load(contact.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);*/
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<WebArd> filteredList = new ArrayList<>();
                    for (WebArd row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        // if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
                        if (row.getName().toLowerCase().startsWith(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<WebArd>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(List<WebArd> ardList);
    }
}
