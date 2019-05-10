package com.chicsol.marrymax.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.mMemList;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyAddToListAdapter extends ArrayAdapter<mMemList> {
    Fragment fragment;
    Context context;
    String userpath;
    int layoutResourceId;
    List<mMemList> data = null;
    public OnUpdateListener onUpdateListener;

    List<mMemList> dataList;
    public MyAddToListAdapter(Context context, int layoutResourceId, List<mMemList> data, Fragment fragment, OnUpdateListener onUpdateListener, String userpath) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;

        this.fragment = fragment;
        this.onUpdateListener = onUpdateListener;

        this.userpath = userpath;

        dataList=new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new Holder();
            holder.cbName = (CheckBox) row.findViewById(R.id.CheckBoxAddToListName);
            holder.tvMemCount = (TextView) row.findViewById(R.id.TextViewMySavedListCount);
            holder.llUpdateListName = (LinearLayout) row.findViewById(R.id.LineaLayoutAddtoListUpdateListName);

            holder.ivMenu = (AppCompatImageView) row.findViewById(R.id.ImageViewMySavedListOptions);
            holder.btUpdateList = (Button) row.findViewById(R.id.ButtonAddtoListUpdate);
            holder.etUpdateListName = (EditText) row.findViewById(R.id.EditTextAddtoListUpdateList);


            row.setTag(holder);

        } else {
            holder = (Holder) row.getTag();
        }
        if (data.size() > 0) {
            final mMemList citem = data.get(position);
            holder.cbName.setText(citem.getName());
            holder.etUpdateListName.setText(citem.getName());


            if (Integer.parseInt(citem.getMy_id()) > 0) {
                holder.cbName.setOnCheckedChangeListener(null);
                holder.cbName.setChecked(true);

            } else {
                holder.cbName.setOnCheckedChangeListener(null);
                holder.cbName.setChecked(false);
            }

            holder.tvMemCount.setText("Members: " + citem.getTotal_member_count());


            holder.cbName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {

                        AddMemList(citem.getMy_id(), citem.getId());
                    } else {
                        AddMemList(citem.getMy_id(), citem.getId());
                    }
                }
            });

            final Holder finalHolder = holder;
            holder.btUpdateList.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    String listname = finalHolder.etUpdateListName.getText().toString();
                    if (!TextUtils.isEmpty(listname.trim())) {
                        addEditList(listname, citem.getId());
                        finalHolder.etUpdateListName.setText("");
                        finalHolder.llUpdateListName.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(finalHolder.etUpdateListName.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);


                    } else {
                        Toast.makeText(context, "Please enter text first !", Toast.LENGTH_SHORT).show();
                    }


                }
            });


            final ImageView vie1w = holder.ivMenu;
            final Holder finalHolder1 = holder;
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

                                    finalHolder1.llUpdateListName.setVisibility(View.VISIBLE);
                                    break;

                                case R.id.menu_mySaveSearches_delete:
                                    DeleteList(citem.getId());


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


    public void updateDataList(List<mMemList> cModelList) {

        data.clear();
        data.addAll(cModelList);
        notifyDataSetChanged();
    }


    static class Holder {
        AppCompatImageView ivMenu;
        CheckBox cbName;
        TextView tvMemCount;
        LinearLayout llUpdateListName;
        Button btUpdateList;
        EditText etUpdateListName;
    }




    public interface OnUpdateListener {

        void onUpdate(String msg);
    }


    private void DeleteList(String id) {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        JSONObject params = new JSONObject();
        try {


            params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());

            params.put("id", id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("Params", Urls.deleteList + "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.deleteList, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re   myList", response + "");
                        getData();
                       /* try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray("data").getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mMemList>>() {
                            }.getType();

                            dataList = (List<mMemList>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);

                            Log.e("MyCountryStateDataList", "" + dataList.size());
                            if (dataList.size() > 0) {
                               *//* LinearLayoutMMMatchesNotFound.setVisibility(View.GONE);
                                llmainLayout.setVisibility(View.VISIBLE);
                                tvHeadingRemoveFromSerch.setVisibility(View.VISIBLE);*//*
                                mListAdapter.updateDataList(dataList);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

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

    private void AddMemList(String my_id, String id) {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        JSONObject params = new JSONObject();
        try {


            params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
            params.put("my_id", my_id);
            params.put("id", id);
            params.put("userpath", userpath);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("AddMemList", Urls.addMemList + "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.addMemList, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re   myList", response + "");
                        getData();

                           int responseid = 0;
                        try {
                            responseid = response.getInt("id");
                           // Log.e("id is ",""+responseid);
                            if (responseid == 1) {

                                Toast.makeText(getContext(), "Member added to lists successfully", Toast.LENGTH_SHORT).show();
                                onUpdateListener.onUpdate("");
                            }
                            else {
                                Toast.makeText(getContext(), "Member removed from lists successfully", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
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

    private void getData() {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");

        pDialog.show();


        JSONObject params = new JSONObject();
        try {


            params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
            params.put("userpath", userpath);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("Params", Urls.myList + "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.myList, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  update appearance", response + "");
                        try {

                            JSONArray jsonCountryStaeObj = response.getJSONArray("data").getJSONArray(0);

                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mMemList>>() {
                            }.getType();
                             dataList = (List<mMemList>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);

                            if (dataList.size() > 0) {
                                updateDataList(dataList);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            dataList.clear();
                            updateDataList(dataList);

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

    private void addEditList(String notes, String id) {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        JSONObject params = new JSONObject();
        try {


            params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
            params.put("notes", notes);
            params.put("id", id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("Params", Urls.addEditList + "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.addEditList, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re   myList", response + "");
                        getData();
                        // et.setText("");
                        /*    try {
                            int responseid = response.getInt("id");




                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }
*/
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

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


}