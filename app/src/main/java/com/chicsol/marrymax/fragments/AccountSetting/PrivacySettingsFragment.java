package com.chicsol.marrymax.fragments.AccountSetting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Dashboards;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Android on 11/3/2016.
 */

public class PrivacySettingsFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private ProgressBar pDialog;
    /*private Button bt_subscribe, bt_viewprofile, bt_viewprofile2;
    private SwitchCompat s1, s2, s3, s4, s5, s6, s7, s8, s9, s10;*/
    private RadioGroup rgProfile, rgPicture, rgPhone, rgMessage, rgInterest;
    private View view2;
    private TextView tvSubOpt1, tvSubOpt3, tvSubOpt4;
    private Context context;
    // tvSubOpt2

    private String Tag = "DashMembersFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_as_privacy_settings, container, false);
        initilize(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void initilize(View view) {
        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarProjectMain);
        view2 = view;

        rgProfile = (RadioGroup) view.findViewById(R.id.RadioGroupAccountSettingPrivacyProfile);
        rgPicture = (RadioGroup) view.findViewById(R.id.RadioGroupAccountSettingPrivacyPicture);

        rgPhone = (RadioGroup) view.findViewById(R.id.RadioGroupAccountSettingPrivacyPhone);
        rgMessage = (RadioGroup) view.findViewById(R.id.RadioGroupAccountSettingPrivacyMessage);
        rgInterest = (RadioGroup) view.findViewById(R.id.RadioGroupAccountSettingPrivacyInterest);
        tvSubOpt1 = (TextView) view.findViewById(R.id.TextViewAccountSettingPrivacySubOption1);
        //  tvSubOpt2 = (TextView) view.findViewById(R.id.TextViewAccountSettingPrivacySubOption2);
        tvSubOpt3 = (TextView) view.findViewById(R.id.TextViewAccountSettingPrivacySubOption3);
        tvSubOpt4 = (TextView) view.findViewById(R.id.TextViewAccountSettingPrivacyPhoneSubOption1);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            // FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            getRequest();
        }
    }

 /*   @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            if (isVisibleToUser) {
                getRequest();
            }
        }


    }
*/

    private void getRequest() {


        pDialog.setVisibility(View.VISIBLE);
        Log.e("api path", "" + Urls.getProfilePrivacy + SharedPreferenceManager.getUserObject(context).get_path());

        JsonArrayRequest req = new JsonArrayRequest(Urls.getProfilePrivacy + SharedPreferenceManager.getUserObject(context).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());
                        try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<Dashboards>() {
                            }.getType();

                            Dashboards dashObj = gsonc.fromJson(jsonCountryStaeObj.getJSONObject(0).toString(), listType);

                            setValues(dashObj);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.setVisibility(View.GONE);
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
        MySingleton.getInstance(context).addToRequestQueue(req, Tag);
    }

    private void setValues(Dashboards dash) {


        rgProfile.check(rgProfile.getChildAt(Integer.parseInt(dash.getPrivacy_profile())).getId());
        rgInterest.check(rgInterest.getChildAt(Integer.parseInt(dash.getPrivacy_interest())).getId());

        rgMessage.check(rgMessage.getChildAt(Integer.parseInt(dash.getPrivacy_message())).getId());

        if (dash.getPrivacy_phone().equals("0")) {
            rgPhone.check(rgPhone.getChildAt(1).getId());
        }else {
            rgPhone.check(rgPhone.getChildAt(0).getId());

        }




        rgPicture.check(rgPicture.getChildAt(Integer.parseInt(dash.getPrivacy_picture())).getId());


        rgProfile.setOnCheckedChangeListener(PrivacySettingsFragment.this);
        rgPicture.setOnCheckedChangeListener(PrivacySettingsFragment.this);
        rgPhone.setOnCheckedChangeListener(PrivacySettingsFragment.this);
        rgMessage.setOnCheckedChangeListener(PrivacySettingsFragment.this);
        rgInterest.setOnCheckedChangeListener(PrivacySettingsFragment.this);


        if (SharedPreferenceManager.getUserObject(context).get_member_status() < 4) {
            tvSubOpt1.setVisibility(View.VISIBLE);
            //  tvSubOpt2.setVisibility(View.VISIBLE);
            tvSubOpt3.setVisibility(View.VISIBLE);
            tvSubOpt4.setVisibility(View.VISIBLE);

            disableSubscribersRadios(rgProfile);
            //   disableSubscribersRadios(rgMessage);
            disableSubscribersRadios(rgInterest);
            disableSubscribersRadios(rgPhone);


        } else {
            tvSubOpt1.setVisibility(View.GONE);
            //     tvSubOpt2.setVisibility(View.GONE);
            tvSubOpt3.setVisibility(View.GONE);
            tvSubOpt4.setVisibility(View.GONE);

            enableeSubscribersRadios(rgProfile);
            //    enableeSubscribersRadios(rgMessage);
            enableeSubscribersRadios(rgInterest);
            enableeSubscribersRadios(rgPhone);
        }


    }

    private void disableSubscribersRadios(RadioGroup radioGroup) {
        radioGroup.getChildAt(radioGroup.getChildCount() - 2).setEnabled(false);
    }

    private void enableeSubscribersRadios(RadioGroup radioGroup) {
        radioGroup.getChildAt(radioGroup.getChildCount() - 2).setEnabled(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {


        RadioButton radioButton = (RadioButton) view2.findViewById(checkedId);
        Log.e("radioo  " + radioButton.getTag(), group.getCheckedRadioButtonId() + "");


        String tag = radioButton.getTag().toString();
        if (tag != null) {
            String[] in = tag.split(",");
            String id = in[0];
            String value = in[1];

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", id);
                jsonObject.put("value", value);
                jsonObject.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                editPrivacyRequest(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //   Log.e("radioo   " + id, "" + value);
            // Log.e("radioo   " + id, "" + value);

        }


    }


    private void editPrivacyRequest(JSONObject params) {

        pDialog.setVisibility(View.VISIBLE);
        Log.e("params", params.toString());
        Log.e("profile path", Urls.editPrivacy);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.editPrivacy, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  interest ", response + "");

                        try {
                            int responseid = response.getInt("id");
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, Tag);
    }


    @Override
    public void onStop() {
        super.onStop();
        pDialog.setVisibility(View.GONE);
        MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);

    }


}
