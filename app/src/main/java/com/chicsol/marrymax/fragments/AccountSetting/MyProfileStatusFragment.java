package com.chicsol.marrymax.fragments.AccountSetting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
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
    LinearLayout llCompleteProfile, llVeriyEmail, llVerifyPhone, llAdminReview, llPhoneVerified;
    RelativeLayout rlEmailVerified;
    TextView tvDesc, tvPhoneNumber;
    private Context context;
    TextView tvEmail, tvTitleLiveNotLive;

    private AppCompatButton btAddNumber, btResendVerification, btUpdateEmail;


    LinearLayout llASEmail, llASPhone;
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


        btAddNumber = (AppCompatButton) view.findViewById(R.id.ButtonMyProfileStatusAddNumber);
        btResendVerification = (AppCompatButton) view.findViewById(R.id.ButtonMyProfileStatusResend);
        btUpdateEmail = (AppCompatButton) view.findViewById(R.id.ButtonMyProfileStatusUpdate);


        llCompleteProfile = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusCompleteProfile);
        llVerifyPhone = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusVerifyPhone);
        llVeriyEmail = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusVerifyEmail);
        llAdminReview = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusAdminApproval);

        llPhoneVerified = (LinearLayout) view.findViewById(R.id.LinearLayoutVerifyMobile);
        rlEmailVerified = (RelativeLayout) view.findViewById(R.id.RelativeLayoutVerifyEmail);

        tvEmail = (TextView) view.findViewById(R.id.TextViewMyProfileStatusEmail);

        tvDesc = (TextView) view.findViewById(R.id.TextViewMyProfileStatusDesc);

        tvPhoneNumber = (TextView) view.findViewById(R.id.TextViewMyProfileStatusMobileNumber);


        llASEmail = (LinearLayout) view.findViewById(R.id.LinearlayoutMyProfileStatusEmail);
        llASPhone = (LinearLayout) view.findViewById(R.id.LinearlayoutMyProfileStatusPhone);


        String compUptoSSeventyText = "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, your profile is <b> <font color=#9a0606>Not Live </font></b> ";

        tvTitleLiveNotLive = (TextView) view.findViewById(R.id.TextViewMyProfileStatusTitle);


        tvEmail.setText(SharedPreferenceManager.getUserObject(getContext()).get_email());


     /*   if (SharedPreferenceManager.getUserObject(getContext()).get_member_status() < 3 || SharedPreferenceManager.getUserObject(getContext()).get_member_status() >= 7) {
            checkEmailStatus(getContext());
            // llASEmail.setVisibility(View.VISIBLE);
        } else {
            rlEmailVerified.setVisibility(View.VISIBLE);
            llASEmail.setVisibility(View.GONE);
        }*/

        if (SharedPreferenceManager.getUserObject(getContext()).get_member_status() < 3 || SharedPreferenceManager.getUserObject(getContext()).get_member_status() >= 7) {
            tvDesc.setVisibility(View.VISIBLE);
        } else {
            tvDesc.setVisibility(View.GONE);
        }


        getPhoneNumber();


        getProfileCompletion();

    }

    private void setListeners() {
        btAddNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 23);
                startActivity(in);

            }
        });
        btUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 23);
                startActivity(in);
            }
        });

        btResendVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 23);
                startActivity(in);
            }
        });



        llCompleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarryMax marryMax = new MarryMax(getActivity());
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


                            if (Integer.parseInt(dashboards.getProfile_complete_status()) >= 70) {

                                String compUptoSSeventyText = "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, your profile is <b> <font color=#9a0606> Live </font></b> ";
                                tvTitleLiveNotLive.setText(Html.fromHtml(compUptoSSeventyText));

                            } else {
                                String compUptoSSeventyText = "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, your profile is <b> <font color=#9a0606>Not Live </font></b> ";
                                tvTitleLiveNotLive.setText(Html.fromHtml(compUptoSSeventyText));
                            }


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


                            if (SharedPreferenceManager.getUserObject(getContext()).get_member_status() < 3 || SharedPreferenceManager.getUserObject(getContext()).get_member_status() >= 7) {

                                // llASEmail.setVisibility(View.VISIBLE);
                                if (dashboards.getEmail_complete_status().equals("1")) {
                                    //hide update email
                              /*  etAsEmail.setKeyListener(null);
                                etAsEmail.setEnabled(false);*/
                                    llASEmail.setVisibility(View.GONE);
                                    rlEmailVerified.setVisibility(View.VISIBLE);

                                } else {
                                    //show
                                    rlEmailVerified.setVisibility(View.GONE);
                                    llASEmail.setVisibility(View.VISIBLE);

                                }


                            } else {
                                rlEmailVerified.setVisibility(View.VISIBLE);
                                llASEmail.setVisibility(View.GONE);
                            }


                            // if (dashboards.getPhone_complete_status().equals("0")) {}


                            if (dashboards.getPhone_complete_status().equals("1")) {
                                llVerifyPhone.setVisibility(View.GONE);
                                llPhoneVerified.setVisibility(View.VISIBLE);
                                llASPhone.setVisibility(View.GONE);


                            } else {

                                llASPhone.setVisibility(View.VISIBLE);
                                llVerifyPhone.setVisibility(View.VISIBLE);

                            }

                         /*   if (dashboards.getPhone_complete_status().equals("1")) {
                                //hide update email
                              *//*  etAsEmail.setKeyListener(null);
                                etAsEmail.setEnabled(false);*//*
                                llASPhone.setVisibility(View.GONE);
                                llPhoneVerified.setVisibility(View.VISIBLE);

                            } else {
                                //show
                                llASPhone.setVisibility(View.GONE);
                                llPhoneVerified.setVisibility(View.VISIBLE);

                            }
                            */


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
                            //pDialog.dismiss();
                            pDialog.setVisibility(View.INVISIBLE);

                            ///  swipeRefreshLayout.setRefreshing(false);

                        }
                        pDialog.setVisibility(View.INVISIBLE);


                        //   pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                //  pDialog.dismiss();
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


    private void getPhoneNumber() {


        Log.e(" Notification url", Urls.getPhn + SharedPreferenceManager.getUserObject(context).get_path());
        StringRequest req = new StringRequest(Urls.getPhn + SharedPreferenceManager.getUserObject(context).get_path(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getPhoneNumber ", "=======================  " + response.equalsIgnoreCase(response));
                        Log.e("getPhoneNumber ", "=======================  " + response.replaceAll("^\"|\"$", ""));

                        // result=result.replaceAll("^\"|\"$", "");
                        response = response.replaceAll("^\"|\"$", "");

                        if (!response.toString().equals("0")) {
                            tvPhoneNumber.setText(response);

                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(req);
    }


    public void checkEmailStatus(final Context context) {

        // pDialog.show();
        Log.e("status URL", Urls.getProfileCompletion + SharedPreferenceManager.getUserObject(context).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getProfileCompletion + SharedPreferenceManager.getUserObject(context).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());


                        try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);
                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<Dashboards>() {
                            }.getType();

                            Dashboards dashboards = (Dashboards) gsonc.fromJson(jsonCountryStaeObj.getJSONObject(0).toString(), listType);


                            boolean pCOmpleteStatus = true;
                            Members members = SharedPreferenceManager.getUserObject(context);
                            //   if (dashboards.getAdmin_approved_status().equals("1") && members.get_member_status() < 3) {

                            if (dashboards.getEmail_complete_status().equals("1")) {
                                //hide update email
                              /*  etAsEmail.setKeyListener(null);
                                etAsEmail.setEnabled(false);*/
                                llASEmail.setVisibility(View.GONE);
                                rlEmailVerified.setVisibility(View.VISIBLE);

                            } else {
                                //show
                                rlEmailVerified.setVisibility(View.GONE);
                                llASEmail.setVisibility(View.VISIBLE);

                            }


                            if (dashboards.getPhone_complete_status().equals("1")) {
                                //hide update email
                              /*  etAsEmail.setKeyListener(null);
                                etAsEmail.setEnabled(false);*/
                                llASPhone.setVisibility(View.GONE);
                                llPhoneVerified.setVisibility(View.VISIBLE);

                            } else {
                                //show
                                llASPhone.setVisibility(View.GONE);
                                llPhoneVerified.setVisibility(View.VISIBLE);

                            }

                                /*else if (members.get_member_status() == 2) {
                                    if (dashboards.getEmail_complete_status().equals("1")  && dashboards.getProfile_complete_status().equals("100")) {
                                        members.set_member_status(3);
                                        SharedPreferenceManager.setUserObject(context,members);
                                    }

                                }*/
                            //  }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //pDialog.dismiss();

                        }

                        //   pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                //  pDialog.dismiss();

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
