package com.chicsol.marrymax.fragments.AccountSetting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.mAsEmailNotifications;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 11/3/2016.
 */

public class EmailNotificationsFragment extends Fragment implements SwitchCompat.OnCheckedChangeListener {

    private LinearLayout llSwitchMain;
    private ViewGenerator viewGenerator;
    private SwitchCompat SCtDisableEmailNotifications;
    private ProgressBar pDialog;
    private Context context;

    private String Tag = "DashMembersFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_as_email_notifications, container, false);
        initilize(rootView);
        return rootView;
    }

    private void initilize(View view) {
        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarProjectMain);
        SCtDisableEmailNotifications = (SwitchCompat) view.findViewById(R.id.SwitchCompatDisableEmailNotifications);
        llSwitchMain = (LinearLayout) view.findViewById(R.id.LinearLayoutAccountSettingEmailNotificationsMain);
        viewGenerator = new ViewGenerator(getContext());
        SCtDisableEmailNotifications.setOnCheckedChangeListener(this);
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            getNotificationRequest();
        }

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.context = activity;
    }


    private void getNotificationRequest() {

        pDialog.setVisibility(View.VISIBLE);
        Log.e("getNotRequest p", "" + Urls.getNotificationList + SharedPreferenceManager.getUserObject(context).get_path());

        JsonArrayRequest req = new JsonArrayRequest(Urls.getNotificationList + SharedPreferenceManager.getUserObject(context).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                        try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mAsEmailNotifications>>() {
                            }.getType();

                            List<mAsEmailNotifications> dataList = (List<mAsEmailNotifications>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                            if (dataList.size() > 0) {

                                Gson gson;
                                GsonBuilder gsonBuilders = new GsonBuilder();
                                gson = gsonBuilders.create();
                                Type listTyp = new TypeToken<mAsEmailNotifications>() {
                                }.getType();

                                mAsEmailNotifications mem = (mAsEmailNotifications) gson.fromJson(response.getJSONArray(1).getJSONObject(0).toString(), listTyp);
                                Log.e("aa ====-----", mem.getId());
                                SCtDisableEmailNotifications.setId(Integer.parseInt(mem.getId()));
                                if (mem.getId().equals("0")) {

                                    SCtDisableEmailNotifications.setChecked(false);
                                    SCtDisableEmailNotifications.setText("Enable email notifications.");
                                    llSwitchMain.removeAllViews();
                                    viewGenerator.generateDynamicSwitchCompats(dataList, llSwitchMain, EmailNotificationsFragment.this, true);

                                } else {
                                    SCtDisableEmailNotifications.setChecked(true);
                                    SCtDisableEmailNotifications.setText("Completely disable all email notifications.");

                                    llSwitchMain.removeAllViews();
                                    viewGenerator.generateDynamicSwitchCompats(dataList, llSwitchMain, EmailNotificationsFragment.this, false);
                                }

                            }


                            //SCtDisableEmailNotifications

                            //  Log.e("size", "" + dataList.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(req,Tag);
    }


    private void putChangeNotificationRequest(JSONObject params) {

        pDialog.setVisibility(View.VISIBLE);
        Log.e("params", params.toString());
        Log.e("profile path", Urls.changeNotification);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.changeNotification, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  change notif ", response + "");

                        try {
                            int responseid = response.getInt("id");

                            if (responseid == 1) {

                                //  getNotificationRequest();
                            }


                        } catch (JSONException e) {
                            pDialog.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                pDialog.setVisibility(View.GONE);
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq,Tag);
    }


    private void putChangeNotificationAll(JSONObject params) {

        pDialog.setVisibility(View.GONE);
        Log.e("params", params.toString());
        Log.e("profile path", Urls.changeNotificationall);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.changeNotificationall, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  change notif ", response + "");

                        try {
                            int responseid = response.getInt("id");

                            if (responseid == 1) {

                                getNotificationRequest();
                            }


                        } catch (JSONException e) {
                            pDialog.setVisibility(View.GONE);
                            e.printStackTrace();
                        }


                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                pDialog.setVisibility(View.GONE);
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq,Tag);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        String my_id = null;
        if (isChecked) {
            my_id = "0";
        } else {
            my_id = "1";
        }

        if (buttonView.getTag().equals("disableallnoti")) {

            String id = null;
            Log.e("buttonView.getId() " + isChecked, buttonView.getId() + "");
            if (buttonView.isChecked()) {
                id = "1";
            } else {
                id = "0";
            }


            // Log.e("CompoundButton  ", isChecked + "   ===========  " + buttonView.getText());
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                jsonObject.put("id", id);

                putChangeNotificationAll(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                jsonObject.put("id", buttonView.getId());
                jsonObject.put("isedit", buttonView.getTag());
                jsonObject.put("my_id", my_id);
                putChangeNotificationRequest(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }



    @Override
    public void onStop() {
        super.onStop();
        MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);

    }


}
