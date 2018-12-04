package com.chicsol.marrymax.fragments.AccountSetting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.MatchAidActivity;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.dialogs.dialogProfileCompletion;
import com.chicsol.marrymax.dialogs.dialogVerifyphone;
import com.chicsol.marrymax.modal.Dashboards;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.other.MarryMax;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyProfileSettingFragment extends Fragment implements dialogVerifyphone.onCompleteListener {


    String Tag = "MyProfileSettingFragment";
    private ProgressDialog pDialog;
    LinearLayout llCompleteProfile, llVeriyEmail, llVerifyPhone, llAdminReview, llPhoneVerified;
    RelativeLayout rlEmailVerified;
    TextView tvDesc, tvPhoneNumber;
    private Context context;
    TextView tvTitleLiveNotLive, tvAdminReviewTitle, tvAdminReviewTitleMain;
    private boolean addNumber = false;
    private AppCompatButton btAddNumber, btVerifyNumber, btUpdateNumber, btMatchAid;
    //   btUpdateEmailz  btResendVerification

    LinearLayout llASPhone, llASEmail;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //=============EMAIL=============
    private AppCompatButton btAlreadyHaveCode, btResendVerificationEmail, btUpdateEmail, btEmailCancel;
    private EditText etAsEmail;
    boolean emailEnabled = false;
    private Members member = null;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String country_id = "";
    // ===========================
    TextView tvSubscriberOnly;

    public MyProfileSettingFragment() {
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

    @Override
    public void onResume() {
        super.onResume();
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
    }

    private void initilize(View view) {


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshProfileStatus);
   /*     pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarMyProfileStatusFragment);
        pDialog.setVisibility(View.GONE);*/
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");


    /*    tvSubscriberOnly = (TextView) view.findViewById(R.id.TextViewMatchAidSubscribersOnly);
        if (SharedPreferenceManager.getUserObject(context).get_member_status() <= 3) {
            tvSubscriberOnly.setVisibility(View.VISIBLE);

        } else {
            tvSubscriberOnly.setVisibility(View.GONE);
        }
*/

        btAddNumber = (AppCompatButton) view.findViewById(R.id.ButtonMyProfileStatusAddNumber);
        btVerifyNumber = (AppCompatButton) view.findViewById(R.id.ButtonMyProfileStatusVerifyNumber);
        btUpdateNumber = (AppCompatButton) view.findViewById(R.id.ButtonMyProfileStatusUpdateNumber);

        btMatchAid = (AppCompatButton) view.findViewById(R.id.ButtonMatchAid);


        //  btResendVerification = (AppCompatButton) view.findViewById(R.id.ButtonMyProfileStatusResend);
        //  btUpdateEmailz = (AppCompatButton) view.findViewById(R.id.ButtonMyProfileStatusUpdate);


        llCompleteProfile = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusCompleteProfile);
        llVerifyPhone = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusVerifyPhone);
        llVeriyEmail = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusVerifyEmail);
        llAdminReview = (LinearLayout) view.findViewById(R.id.LinearLayoutMyProfileStatusAdminApproval);

        llPhoneVerified = (LinearLayout) view.findViewById(R.id.LinearLayoutVerifyMobile);
        rlEmailVerified = (RelativeLayout) view.findViewById(R.id.RelativeLayoutVerifyEmail);


        tvDesc = (TextView) view.findViewById(R.id.TextViewMyProfileStatusDesc);

        tvPhoneNumber = (TextView) view.findViewById(R.id.TextViewMyProfileStatusMobileNumber);


        // llASEmail = (LinearLayout) view.findViewById(R.id.LinearlayoutMyProfileStatusEmail);
        llASPhone = (LinearLayout) view.findViewById(R.id.LinearlayoutMyProfileStatusPhone);


        String compUptoSSeventyText = "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, your profile is <b> <font color=#9a0606>Not Live </font></b> ";

        tvTitleLiveNotLive = (TextView) view.findViewById(R.id.TextViewMyProfileStatusTitle);
        tvAdminReviewTitle = (TextView) view.findViewById(R.id.TextViewAdminReviewTitle);
        tvAdminReviewTitleMain = (TextView) view.findViewById(R.id.TextViewAdminReviewTitleMain);

        if (SharedPreferenceManager.getUserObject(context).get_member_status() == 7) {
            tvAdminReviewTitle.setText(" (Please review admin notes and update your profile as suggested.)");
            tvAdminReviewTitleMain.setText("Review completed");


        }


     /*   if (SharedPreferenceManager.getUserObject(getContext()).get_member_status() < 3 || SharedPreferenceManager.getUserObject(getContext()).get_member_status() >= 7) {
            checkEmailStatus(getContext());
            // llASEmail.setVisibility(View.VISIBLE);
        } else {
            rlEmailVerified.setVisibility(View.VISIBLE);
            llASEmail.setVisibility(View.GONE);
        }*/


        //============EMAIL

        btResendVerificationEmail = (AppCompatButton) view.findViewById(R.id.ButtonAccountSettingResendVerificationEmail);
        btUpdateEmail = (AppCompatButton) view.findViewById(R.id.ButtonAccountSettingUpdateEmail);
        btEmailCancel = (AppCompatButton) view.findViewById(R.id.ButtonAccountSettingUpdateEmailCancel);
        etAsEmail = (EditText) view.findViewById(R.id.EditTextAScontactEmail);
        llASEmail = (LinearLayout) view.findViewById(R.id.LinearlayoutAccountSettingEmail);

        etAsEmail.setText(SharedPreferenceManager.getUserObject(context).get_email());
        etAsEmail.setEnabled(false);


 /*  if (SharedPreferenceManager.getUserObject(getContext()).get_member_status() < 3 || SharedPreferenceManager.getUserObject(getContext()).get_member_status() >= 7) {
            checkEmailStatus(getContext());
            llASEmail.setVisibility(View.VISIBLE);
        } else {

            llASEmail.setVisibility(View.GONE);
        }
*/

        loadData();


    }


    private void loadData() {


        if (SharedPreferenceManager.getUserObject(context).get_member_status() < 3 || SharedPreferenceManager.getUserObject(context).get_member_status() >= 7) {
            tvDesc.setVisibility(View.VISIBLE);
        } else {
            tvDesc.setVisibility(View.GONE);
        }


        //============EMAIL
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            getRequest(SharedPreferenceManager.getUserObject(context).get_path());


            getPhoneNumber();


        }

    }

    private void setListeners() {

        btMatchAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SharedPreferenceManager.getUserObject(context).get_member_status() < 3) {

                    Toast.makeText(context, "Please complete and verify your profile details.", Toast.LENGTH_LONG).show();
                } else if (SharedPreferenceManager.getUserObject(context).get_member_status() == 7 || SharedPreferenceManager.getUserObject(context).get_member_status() == 8) {

                    Toast.makeText(context, "Please review notes as MarryMax team advised and update your profile or contact us for further assistance", Toast.LENGTH_LONG).show();
                } else {

                    //display screen
                    Intent intent = new Intent(getActivity(), MatchAidActivity.class);
                    startActivity(intent);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        btAddNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 23);
                startActivity(in);

            }
        });


        btVerifyNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferenceManager.getUserObject(context).get_member_status() == 0) {
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, please complete your profile first then we can send you verification code on your Mobile number.", "Complete Profile", 8);
                    dialogP.show(getFragmentManager(), "d");

                } else {
                  /*  dialogVerifyphone newFragment = dialogVerifyphone.newInstance(member.get_phone_mobile(), country_id, true);
                    newFragment.setTargetFragment(MyProfileSettingFragment.this, 3);
                    newFragment.show(getFragmentManager(), "dialog");*/

                    getValidCode(SharedPreferenceManager.getUserObject(context).get_path());


                }

            }
        });
        btUpdateNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, MainDirectiveActivity.class);
                in.putExtra("type", 23);
                startActivity(in);

            }
        });


   /*     btUpdateEmailz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 23);
                startActivity(in);
            }
        });*/

   /*     btResendVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 23);
                startActivity(in);
            }
        });
*/

        llCompleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarryMax marryMax = new MarryMax(getActivity());
                marryMax.getProfileProgress(context, SharedPreferenceManager.getUserObject(context), getActivity());
            }
        });


//===============Email===================
        btEmailCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAsEmail.setText(SharedPreferenceManager.getUserObject(context).get_email());
                etAsEmail.setEnabled(false);
                etAsEmail.setError(null);
                emailEnabled = false;
                btResendVerificationEmail.setVisibility(View.VISIBLE);
                btEmailCancel.setVisibility(View.GONE);
                btUpdateEmail.setText("Update");
            }
        });
        btUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailEnabled) {

                    if (!isEmailValid(etAsEmail.getText().toString())) {
                        etAsEmail.setError(getString(R.string.error_invalid_email));

                    } else {


                        updateEmail();
                    }
                } else {
                    //edit enabling
                    etAsEmail.setEnabled(true);
                    emailEnabled = true;
                    btUpdateEmail.setText("Save");

                    btResendVerificationEmail.setVisibility(View.GONE);

                    btEmailCancel.setVisibility(View.VISIBLE);

                }


            }
        });
        btResendVerificationEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Verify Your Email", "Here is your email address that needs to be verified.<br /> <b>  <font color=#216917>" + SharedPreferenceManager.getUserObject(context).get_email() + "</font></b><br /> " +
                        "Please verify your email by using the link, we had emailed you.<br /> <font color=#9a0606> (In case you didn't receive any email,  please check your spam/junk folder or click \"Resend Verification Email\" )</font> ", "Resend Verification Email", 22);
                dialogP.show(getFragmentManager(), "d");
            }
        });


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void getValidCode(final String path) {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        Log.e("path", "" + Urls.getValidCode + path);
        StringRequest req = new StringRequest(Urls.getValidCode + path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response--", response.toString() + "==");
                        if (response != null) {

                            if (Long.parseLong(response) == 0) {
                                dialogVerifyphone newFragment = dialogVerifyphone.newInstance(member.get_phone_mobile(), country_id, false);
                                newFragment.setTargetFragment(MyProfileSettingFragment.this, 3);
                                newFragment.show(getFragmentManager(), "dialog");

                            } else {

                                dialogVerifyphone newFragment = dialogVerifyphone.newInstance(member.get_phone_mobile(), country_id, true);
                                newFragment.setTargetFragment(MyProfileSettingFragment.this, 3);
                                newFragment.show(getFragmentManager(), "dialog");
                            }
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener()

        {
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


        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(req);

    }

    private void getProfileCompletion() {
        //   pDialog.setVisibility(View.VISIBLE);
        if (!((Activity) context).isFinishing()) {

            pDialog.show();

            //show dialog
        }


        Log.e("URL", Urls.getProfileCompletion + SharedPreferenceManager.getUserObject(context).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getProfileCompletion + SharedPreferenceManager.getUserObject(context).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());

                        swipeRefreshLayout.setRefreshing(false);

                        try {

                            pDialog.dismiss();


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


                            if (Integer.parseInt(dashboards.getProfile_complete_status()) < 70 || SharedPreferenceManager.getUserObject(context).get_member_status() >= 7) {

                                String compUptoSSeventyText = "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, your profile is <b> <font color=#9a0606>Not Live </font></b> ";
                                tvTitleLiveNotLive.setText(Html.fromHtml(compUptoSSeventyText));

                            } else {

                                String compUptoSSeventyText = "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, your profile is <b> <font color=#9a0606> Live </font></b> ";
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


                            if (SharedPreferenceManager.getUserObject(context).get_member_status() < 3 || SharedPreferenceManager.getUserObject(context).get_member_status() >= 7) {

                                llASEmail.setVisibility(View.VISIBLE);
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


                            if (!addNumber) {
                                btAddNumber.setVisibility(View.GONE);
                                btUpdateNumber.setVisibility(View.VISIBLE);
                                btVerifyNumber.setVisibility(View.VISIBLE);

                            } else {
                                btUpdateNumber.setVisibility(View.GONE);
                                btVerifyNumber.setVisibility(View.GONE);

                                btAddNumber.setVisibility(View.VISIBLE);
                            }


                            if (dashboards.getPhone_complete_status().equals("1")) {
                                llVerifyPhone.setVisibility(View.GONE);
                                llPhoneVerified.setVisibility(View.VISIBLE);
                                llASPhone.setVisibility(View.GONE);

                                //verfied


                            } else {
// if
                                llASPhone.setVisibility(View.VISIBLE);
                                llVerifyPhone.setVisibility(View.VISIBLE);

                            }

                     /*   if (dashboards.getPhone_complete_status().equals("1")) {
                                //hide update email

                                llASPhone.setVisibility(View.GONE);
                                llPhoneVerified.setVisibility(View.VISIBLE);

                            } else {
                                //show
                                llASPhone.setVisibility(View.GONE);
                                llPhoneVerified.setVisibility(View.VISIBLE);

                            }*/


                            if (dashboards.getAdmin_approved_status().equals("0")) {
                                llAdminReview.setVisibility(View.VISIBLE);

                            } else {
                                llAdminReview.setVisibility(View.GONE);


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            // pDialog.setVisibility(View.INVISIBLE);
                            swipeRefreshLayout.setRefreshing(false);


                        }
                        //  pDialog.setVisibility(View.INVISIBLE);
                        pDialog.dismiss();


                      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            if (getActivity().isDestroyed()) { // or call isFinishing() if min sdk version < 17
                                pDialog.dismiss();
                            }
                        }*/


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                //   pDialog.setVisibility(View.INVISIBLE);
                pDialog.dismiss();


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

                        swipeRefreshLayout.setRefreshing(false);

                        // result=result.replaceAll("^\"|\"$", "");
                        response = response.replaceAll("^\"|\"$", "");

                        if (!response.toString().equals("0")) {
                            tvPhoneNumber.setText(response);
                            addNumber = false;
                        } else {
                            addNumber = true;
                        }
                        getProfileCompletion();
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

/*
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
                              *//*  etAsEmail.setKeyListener(null);
                                etAsEmail.setEnabled(false);*//*
                                llASEmail.setVisibility(View.GONE);
                                rlEmailVerified.setVisibility(View.VISIBLE);

                            } else {
                                //show
                                rlEmailVerified.setVisibility(View.GONE);
                                llASEmail.setVisibility(View.VISIBLE);

                            }


                            if (dashboards.getPhone_complete_status().equals("1")) {
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

                                *//*else if (members.get_member_status() == 2) {
                                    if (dashboards.getEmail_complete_status().equals("1")  && dashboards.getProfile_complete_status().equals("100")) {
                                        members.set_member_status(3);
                                        SharedPreferenceManager.setUserObject(context,members);
                                    }

                                }*//*
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
    }*/


    //Email
/*    public void checkEmailStatus(final Context context) {

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
                                etAsEmail.setKeyListener(null);
                                etAsEmail.setEnabled(false);
                               // llASEmail.setVisibility(View.GONE);

                            } else {
                                //show
                              //  llASEmail.setVisibility(View.VISIBLE);

                            }

                                else if (members.get_member_status() == 2) {
                                    if (dashboards.getEmail_complete_status().equals("1")  && dashboards.getProfile_complete_status().equals("100")) {
                                        members.set_member_status(3);
                                        SharedPreferenceManager.setUserObject(context,members);
                                    }

                                }
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
    }*/

    private void updateEmail() {

        //   pDialog.setVisibility(View.VISIBLE);

        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject params = new JSONObject();
        try {


            params.put("name", "" + etAsEmail.getText().toString());
            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("updateEmail", Urls.updateEmail + " == " + params);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.updateEmail, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");


                        Gson gson;
                        GsonBuilder gsonBuilder = new GsonBuilder();

                        gson = gsonBuilder.create();
                        Type type = new TypeToken<WebArd>() {
                        }.getType();
                        WebArd webArd = (WebArd) gson.fromJson(response.toString(), type);
                        if (webArd.getId().equals("0")) {
                            Toast.makeText(context, "Email Not updated", Toast.LENGTH_SHORT).show();

                        } else if (webArd.getId().equals("1")) {

                            Toast.makeText(context, "Email Updated", Toast.LENGTH_SHORT).show();

                            etAsEmail.setEnabled(false);
                            emailEnabled = false;
                            btResendVerificationEmail.setVisibility(View.VISIBLE);
                            btEmailCancel.setVisibility(View.GONE);
                            btUpdateEmail.setText("Update Email");


                            Members member = SharedPreferenceManager.getUserObject(context);
                            member.set_email(etAsEmail.getText().toString());
                            SharedPreferenceManager.setUserObject(context, member);


                        } else if (webArd.getId().equals("2")) {
                            Toast.makeText(context, "Not a valid Email", Toast.LENGTH_SHORT).show();

                        } else if (webArd.getId().equals("3")) {
                            Toast.makeText(context, "Email  Exists", Toast.LENGTH_SHORT).show();

                        }

                        pDialog.dismiss();
                        //  pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                //  pDialog.setVisibility(View.GONE);
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq, Tag);

    }

    private boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }

    private void getRequest(final String path) {
        //   pDialog.setVisibility(View.VISIBLE);

        pDialog.dismiss();

        //     Log.e("path", "" + Urls.getProfileContact + path);
        JsonArrayRequest req = new JsonArrayRequest(Urls.getProfileContact + path,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   Log.d("getProfileContact", response.toString());
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONArray jsonData = response.getJSONArray(0);

                            if (jsonData.length() != 0) {

                                Gson gsonc;
                                GsonBuilder gsonBuilderc = new GsonBuilder();
                                gsonc = gsonBuilderc.create();
                                Type listType = new TypeToken<Members>() {
                                }.getType();
                                member = new Members();
                                member = (Members) gsonc.fromJson(jsonData.getJSONObject(0).toString(), listType);
                                country_id = member.get_country_id() + "";
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //     pDialog.setVisibility(View.GONE);

                        pDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                //   pDialog.setVisibility(View.GONE);
                pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(req, Tag);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (pDialog != null) {
            pDialog.dismiss();
        }

    }

    @Override
    public void onComplete(String s) {
        loadData();
    }
}
