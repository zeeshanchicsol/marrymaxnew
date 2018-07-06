package com.chicsol.marrymax.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.chicsol.marrymax.widgets.faTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class BlockListAdapter extends ArrayAdapter<WebArdList> {

    Context context;
    int layoutResourceId;
    List<WebArdList> data = null;
    private onCompleteListener mCompleteListener;

    public BlockListAdapter(Context context, int layoutResourceId, List<WebArdList> data, onCompleteListener mmCompleteListener) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        try {
            this.mCompleteListener = mmCompleteListener;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new Holder();
            holder.txtTitle = (TextView) row.findViewById(R.id.TextViewBlockListAlias);
            holder.txtDate = (TextView) row.findViewById(R.id.TextViewBlockListDate);
            holder.faUnRemove = (faTextView) row.findViewById(R.id.faTextViewBlockListUnblock);
            holder.txtReason = (TextView) row.findViewById(R.id.TextViewBlockListUnBlockReason);

            row.setTag(holder);

        } else {
            holder = (Holder) row.getTag();
        }

        WebArdList item = data.get(position);
        holder.txtTitle.setText(item.getAlias());
        holder.txtDate.setText(item.getStart_date());

        holder.txtReason.setText(item.getDeactivate_reason());



        holder.faUnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Toast.makeText(context, "" + data.get(position).getAlias(), Toast.LENGTH_SHORT).show();

                WebArdList item = data.get(position);

                JSONObject params = new JSONObject();
                try {
                    params.put("about_type_id", "1");
                    params.put("userpath", item.getUserpath());
                    params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                unBlock(params);

            }
        });


        return row;
    }

    public void clear() {
        if (data != null) {
            data.clear();
            notifyDataSetChanged();

        }
    }

    private void unBlock(JSONObject params) {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        Log.e("params" + "  " + Urls.blockReason, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.blockReason, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update interest", response + "");
                        try {
                            int responseid = response.getInt("id");
                            if (responseid == 0) {
                                Toast.makeText(context, "User Un Blocked", Toast.LENGTH_SHORT).show();

                                //  getData();
                            }
                            mCompleteListener.onComplete(1);
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


    public static interface onCompleteListener {
        public abstract void onComplete(int s);
    }

    static class Holder {
        TextView txtTitle;
        TextView txtDate;
        TextView txtReason;
        faTextView faUnRemove;
    }

}