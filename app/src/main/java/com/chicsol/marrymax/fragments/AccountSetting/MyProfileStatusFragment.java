package com.chicsol.marrymax.fragments.AccountSetting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Dashboards;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.Map;

public class MyProfileStatusFragment extends Fragment {


    String Tag = "MyProfileStatusFragment";
    private ProgressBar pDialog;
    LinearLayout llCompleteProfile, llVeriyEmail, llVerifyPhone, llAdminReview;
    TextView tvTitle, tvDesc;
    private Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MyProfileStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_myprofilestatus, container, false);

        initilize(rootView);
        setListeners();
        return rootView;
    }


    private void initilize(View view) {

        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarMyProfileStatusFragment);
        pDialog.setVisibility(View.GONE);


        llCompleteProfile = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusCompleteProfile);
        llVerifyPhone = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusVerifyPhone);
        llVeriyEmail = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusVerifyEmail);
        llAdminReview = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusAdminApproval);
        String compUptoSSeventyText = "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, your profile is <b> <font color=#9a0606>Not Live </font></b> ";
        getProfileCompletion();
    }

    private void setListeners() {
        llCompleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarryMax marryMax=new MarryMax(getActivity());
                marryMax.getProfileProgress(getContext(), SharedPreferenceManager.getUserObject(getContext()), getActivity());
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    private void getProfileCompletion() {
        pDialog.setVisibility(View.VISIBLE);

        // pDialog.show();
        Log.e("URL", Urls.getProfileCompletion + SharedPreferenceManager.getUserObject(context).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getProfileCompletion + SharedPreferenceManager.getUserObject(context).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());


                        try {
                            //     swipeRefreshLayout.setRefreshing(false);

                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);
                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<Dashboards>() {
                            }.getType();

                            Dashboards dashboards = (Dashboards) gsonc.fromJson(jsonCountryStaeObj.getJSONObject(0).toString(), listType);


                            Log.e("Email Complete Status", "" + dashboards.getEmail_complete_status());

                            //   tvProfileCompleteion.setText(dashboards.getProfile_complete_status() + "% Complete");


                            if (dashboards.getProfile_complete_status().equals("100")) {

                                llCompleteProfile.setVisibility(View.GONE);
                            } else {

                                llCompleteProfile.setVisibility(View.VISIBLE);

                            }


                            if (dashboards.getEmail_complete_status().equals("0")) {

                                llVeriyEmail.setVisibility(View.VISIBLE);

                            } else {
                                llVeriyEmail.setVisibility(View.GONE);

                            }


                            if (dashboards.getPhone_complete_status().equals("0")) {

                                llVerifyPhone.setVisibility(View.VISIBLE);
                            } else {
                                llVerifyPhone.setVisibility(View.GONE);

                            }
                            if (dashboards.getAdmin_approved_status().equals("0")) {
                                llAdminReview.setVisibility(View.VISIBLE);
                           /*     Log.e("mem status", member.get_member_status() + "");
                                if (member.get_member_status() == 7) {
                                    ivReviewPending.setImageResource(R.drawable.ver_step4);
                                    ivReviewPending.setBackgroundResource(R.drawable.border_dash_main_profilecombox_red);
                                    getAdminNotes();


                                } else {
                                    ivReviewPending.setImageResource(R.drawable.ver_step4);
                                    ivReviewPending.setBackgroundResource(R.drawable.border_dash_main_profilecombox);

                                }
*/

                            } else {
                                llAdminReview.setVisibility(View.GONE);
                             /*   ivReviewPending.setImageResource(R.drawable.ver_step4_active);
                                ivReviewPending.setBackgroundResource(R.drawable.border_dash_main_profilecombox_green);
                                ivReviewPending.setOnClickListener(null);

                                Members memberObj = SharedPreferenceManager.getUserObject(context);

                                if (memberObj.get_member_status() <= 1) {
                                    memberObj.set_member_status(2);
                                    SharedPreferenceManager.setUserObject(context, memberObj);
                                }*/


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //pDialog.hide();
                            pDialog.setVisibility(View.INVISIBLE);

                            ///  swipeRefreshLayout.setRefreshing(false);

                        }
                        pDialog.setVisibility(View.INVISIBLE);


                        //   pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                //  pDialog.hide();
                pDialog.setVisibility(View.INVISIBLE);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(req, Tag);
    }


}
