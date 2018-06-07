package com.chicsol.marrymax.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.search.SearchResultsActivity;
import com.chicsol.marrymax.fragments.list.MemberListActivity;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mSavList;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;

public class MySavedListAdapter extends ArrayAdapter<mSavList> {
    Fragment fragment;
    Context context;
    int layoutResourceId;
    List<mSavList> data = null;
    public OnUpdateListener onUpdateListener;

    public MySavedListAdapter(Context context, int layoutResourceId, List<mSavList> data, Fragment fragment, OnUpdateListener onUpdateListener) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;

        this.fragment = fragment;
        this.onUpdateListener = onUpdateListener;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new Holder();
            holder.txtName = (TextView) row.findViewById(R.id.TextViewMySavedListName);
            holder.TextViewMySavedListCount = (TextView) row.findViewById(R.id.TextViewMySavedListCount);
            //  holder.faTxtBlock = (faTextView) row.findViewById(R.id.faTextViewListSavSearchBlocked);
            //  holder.faTxtClick = (faTextView) row.findViewById(R.id.faTextViewListSavSearcheClick);
            holder.ivMenu = (AppCompatImageView) row.findViewById(R.id.ImageViewMySavedListOptions);


            row.setTag(holder);

        } else {
            holder = (Holder) row.getTag();
        }
        if (data.size() > 0) {
            final mSavList citem = data.get(position);

            holder.txtName.setVisibility(View.VISIBLE);
            holder.txtName.setText(citem.getAlias());
            holder.TextViewMySavedListCount.setText("Members: " + citem.getPrefvalue1());
            holder.txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(citem.getPrefvalue1()) > 0) {
                        Intent intent = new Intent(context, MemberListActivity.class);
                        intent.putExtra("userpath", citem.getPath());
                        intent.putExtra("id", citem.getId());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "List is Empty.", Toast.LENGTH_SHORT).show();
                    }

                }
            });


            final ImageView vie1w = holder.ivMenu;
            holder.ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, vie1w);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.menu_my_saved_list, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {


                            switch (item.getItemId()) {
                                case R.id.menu_mySavedListView:
                                    if (Integer.parseInt(citem.getPrefvalue1()) > 0) {
                                        Intent intent = new Intent(context, MemberListActivity.class);


                                        intent.putExtra("id", citem.getId());
                                        intent.putExtra("count", citem.getPrefvalue1());
                                        context.startActivity(intent);
                                    } else {
                                        Toast.makeText(context, "List is Empty.", Toast.LENGTH_SHORT).show();
                                    }


                                    break;

                                case R.id.menu_mySavedListDelete:
                                    mSavList item1 = data.get(position);

                                    JSONObject params = new JSONObject();
                                    try {
                                        params.put("id", item1.getId());
                                        params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    deleteList(params);


                                    break;


                            }


                            return true;
                        }

                    });
                    popup.show(); //showing popup menu
                }
            });
        }


        return row;
    }


    public void updateDataList(List<mSavList> cModelList) {

        data.clear();
        data.addAll(cModelList);
        notifyDataSetChanged();
    }


    private void deleteList(JSONObject params) {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        Log.e("params" + "  " + Urls.deleteList, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.deleteList, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update interest", response + "");
                        try {
                            int responseid = response.getInt("id");
                            if (responseid == 1) {
                                Toast.makeText(context, "List Deleted", Toast.LENGTH_SHORT).show();
                                onUpdateListener.onUpdate("");
                               // getData();
                            }
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                Toast.makeText(context, "Error !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };

// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq);

    }




    static class Holder {
        AppCompatImageView ivMenu;
        TextView txtName;
        TextView TextViewMySavedListCount;
        faTextView faTxtBlock;
        faTextView faTxtClick;
    }



    public interface OnUpdateListener {

        void onUpdate(String msg);
    }


}