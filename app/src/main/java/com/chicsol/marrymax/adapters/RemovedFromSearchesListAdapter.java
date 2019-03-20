package com.chicsol.marrymax.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.chicsol.marrymax.modal.WebArdList;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemovedFromSearchesListAdapter extends ArrayAdapter<WebArdList> {
    public OnUpdateListener onUpdateListener;
    Context context;
    int layoutResourceId;
    List<WebArdList> data = new ArrayList<>();

    public RemovedFromSearchesListAdapter(Context context, int layoutResourceId, List<WebArdList> data, OnUpdateListener onUpdateListener) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
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
            holder.txtTitle = (TextView) row.findViewById(R.id.TextViewRemoveSearchesName);
            holder.txtDate = (TextView) row.findViewById(R.id.TextViewRemoveSearchesDate);

            holder.ivUnRemove = (AppCompatImageView) row.findViewById(R.id.ImageViewRemoveSearchesUnremove);

            row.setTag(holder);

        } else {
            holder = (Holder) row.getTag();
        }

        WebArdList item = data.get(position);
        holder.txtTitle.setText(item.getAlias());
        holder.txtDate.setText(item.getStart_date());


        holder.ivUnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WebArdList item = data.get(position);

                JSONObject params = new JSONObject();
                try {
                    //  params.put("id", "1");
                    params.put("userpath", item.getUserpath());
                    params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                unRemove(params);
            }
        });


        return row;
    }

    private void unRemove(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        Log.e("params", params.toString() + " " + Urls.removedList);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.removedList, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  ", response + "");

                        try {
                            int responseid = response.getInt("id");
                            if (responseid == 1) {
                                Toast.makeText(getContext(), "User has been unremoved successfully ", Toast.LENGTH_SHORT).show();
                                //getData();
                                onUpdateListener.onUpdate("");
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




/*   private void getData() {


        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        Log.e("url", Urls.getRemovedList + SharedPreferenceManager.getUserObject(getContext()).getPath());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getRemovedList + SharedPreferenceManager.getUserObject(getContext()).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString() + "  ==   ");
                        Gson gsonc;
                        GsonBuilder gsonBuilderc = new GsonBuilder();
                        gsonc = gsonBuilderc.create();
                        Type listType = new TypeToken<List<WebArdList>>() {
                        }.getType();
                        List<WebArdList> dataList;

                        try {
                            Log.e("Response",response.getJSONArray(0).toString());
                            data.clear();
                            data.addAll( (List<WebArdList>) gsonc.fromJson(response.getJSONArray(0).toString(), listType));
                            notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(req);


    }*/


/*    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<WebArdList> lst) {
        data.clear();
        data.addAll(lst);
        notifyDataSetChanged();

    }*/


    static class Holder {
        TextView txtTitle;
        TextView txtDate;

        AppCompatImageView ivUnRemove;
    }

    public interface OnUpdateListener {

        void onUpdate(String msg);
    }
}