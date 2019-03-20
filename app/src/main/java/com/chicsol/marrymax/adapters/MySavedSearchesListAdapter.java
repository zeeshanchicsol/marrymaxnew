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
import com.chicsol.marrymax.dialogs.dialogUpdateSavedSearch;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.cModel;
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

public class MySavedSearchesListAdapter extends ArrayAdapter<cModel> {
    Fragment fragment;
    Context context;
    int layoutResourceId;
    List<cModel> data = null;
    public OnUpdateListener onUpdateListener;

    public MySavedSearchesListAdapter(Context context, int layoutResourceId, List<cModel> data, Fragment fragment, OnUpdateListener onUpdateListener) {
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
            holder.txtName = (TextView) row.findViewById(R.id.TextViewListSavSearchName);
            holder.txtDate = (TextView) row.findViewById(R.id.TextViewListSavSearchDate);
            holder.faTxtBlock = (faTextView) row.findViewById(R.id.faTextViewListSavSearchBlocked);
            holder.faTxtClick = (faTextView) row.findViewById(R.id.faTextViewListSavSearcheClick);
            holder.ivMenu = (AppCompatImageView) row.findViewById(R.id.ImageViewListSavSearchMenu);


            row.setTag(holder);

        } else {
            holder = (Holder) row.getTag();
        }
        if (data.size() > 0) {
            final cModel citem = data.get(position);
            holder.txtName.setText(citem.getName());
            holder.txtDate.setText(citem.getDate());
            holder.txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int searchKey = Integer.parseInt(citem.getId());


                    getSelectedSearchObject(searchKey);
                }
            });


            final ImageView vie1w = holder.ivMenu;
            holder.ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, vie1w);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.menu_my_saved_searches, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {


                            switch (item.getItemId()) {
                                case R.id.menu_mySaveSearches_edit:


                                    dialogUpdateSavedSearch newFragment = dialogUpdateSavedSearch.newInstance(citem);
                                    newFragment.setTargetFragment(fragment, 0);
                                    newFragment.show(fragment.getFragmentManager(), "dialog");


                                    break;

                                case R.id.menu_mySaveSearches_delete:
                                    cModel item1 = data.get(position);

                                    JSONObject params = new JSONObject();
                                    try {
                                        params.put("id", item1.getId());
                                        params.put("name", item1.getName());
                                        params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    deleteSearch(params);


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


    public void updateDataList(List<cModel> cModelList) {

        data.clear();
        data.addAll(cModelList);
        notifyDataSetChanged();
    }

 /*   private void updateSearch(JSONObject params) {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Log.e("params" + "  " + Urls.deleteSaveSearch, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.deleteSaveSearch, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update interest", response + "");
                        try {
                            int responseid = response.getInt("id");
                            if (responseid == 1) {
                                Toast.makeText(context, "Search Updated Successfully", Toast.LENGTH_SHORT).show();
                                onUpdateListener.onUpdate("");
                             //   getData();
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
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }){
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

    }*/

    private void deleteSearch(JSONObject params) {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        Log.e("params" + "  " + Urls.deleteSaveSearch, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.deleteSaveSearch, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update interest", response + "");
                        try {
                            int responseid = response.getInt("id");
                            if (responseid == 1) {
                                Toast.makeText(context, "Search Deleted", Toast.LENGTH_SHORT).show();
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
        TextView txtDate;
        faTextView faTxtBlock;
        faTextView faTxtClick;
    }

    private void getSelectedSearchObject(int searchid) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        Log.e("getRawData started=====", "=========================" + Urls.getSrhRawData + searchid);
        JsonArrayRequest req = new JsonArrayRequest(Urls.getSrhRawData + searchid,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response SRH", response.toString());
                        Log.e("getRawData finished===", "==========================");

                        try {


                            JSONObject jsonCountryStaeObj = response.getJSONArray(0).getJSONObject(0);
                            Log.e("Response 222", jsonCountryStaeObj.toString());

                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<Members>() {
                            }.getType();
                            defaultSelectionsObj=new Members();
                            defaultSelectionsObj = gsonc.fromJson(jsonCountryStaeObj.toString(), listType);

                           /* MarryMax marryMax = new MarryMax((Activity) context);
                            marryMax.setHeighAgeChecks();*/
                          /*  Gson gson=new Gson();
                            Log.e("body ids rawww", gson.toJson(defaultSelectionsObj) + "  --");
*/

                         //   Log.e("body ids rawww", defaultSelectionsObj.getChoice_height_from_id() + "  --");
                       //     Log.e("body ids rawww", defaultSelectionsObj.getChoice_height_to_id() + "  --");
                            /*Log.e("body ids rawww", defaultSelectionsObj.getCountry_id() + "  --");
                            Log.e("body ids rawww", defaultSelectionsObj.getChoice_country_ids() + "  --");
                            Log.e("body ids rawww", defaultSelectionsObj.getChoice_country_names() + "  --");*/
                            Constants.searchFromSavedListings = true;
                            Intent intent = new Intent(getContext(), SearchResultsActivity.class);
                            context.startActivity(intent);
                            //  intent.putExtra("MESSAGE", searchKey);

                            //    setResult(2, intent);
                            // finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                        }
                        pDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(req);
    }


    public interface OnUpdateListener {

        void onUpdate(String msg);
    }


}