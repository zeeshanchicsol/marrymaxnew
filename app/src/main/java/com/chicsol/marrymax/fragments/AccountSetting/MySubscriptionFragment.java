package com.chicsol.marrymax.fragments.AccountSetting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterMyAccountSubscription;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mSubQuotaInfo;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 11/3/2016.
 */

public class MySubscriptionFragment extends Fragment {
    private ProgressBar pDialog;
    private AppCompatButton bt_subscribe, bt_subscribe2, bt_viewprofile, bt_viewprofile2;
    // RecyclerViewAccountSMySubscription
    private RecyclerView recyclerView;
    private List<Members> dataList;
    private RecyclerViewAdapterMyAccountSubscription recyclerAdapter;

    private LinearLayout llProfileLive, llMySubscription, llDefault, llpProfileInComplete, llSubscriptionQouta;
    AppCompatButton btSubscribe, bt_NotLiveCompletetProfile;

    TextView tvTitleNotLive, tvNotLiveTitleDetail;
    Context context;

    TextView tProfileLive, tvPlanMessagesQouta, tvSubsPlanContactsQouta;
    AppCompatButton btProfileLive;

    private String Tag = "DashMembersFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        this.context = activity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_as_my_subscription, container, false);
        initilize(rootView);

        setListeners();


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        loadData();

    }


    private void loadData() {
        long member_status = SharedPreferenceManager.getUserObject(context).getMember_status();


        // Put SubscriptionData
        //  GetSubscriptions   path
        if (member_status < 3 || member_status >= 7) {
            //  GetSubscriptions   path
            //getSubscriptionRequest(SharedPreferenceManager.getUserObject(getContext()).getPath());
            // call api and display sub data
            if (member_status < 3) {
                //complete your profile
                llpProfileInComplete.setVisibility(View.VISIBLE);
                llDefault.setVisibility(View.GONE);
                llProfileLive.setVisibility(View.GONE);

                if (member_status == 0) {
                    tvTitleNotLive.setText("You Profile is Not Live");
                    tvNotLiveTitleDetail.setText("Please complete your profile and verify email and phone.");
                    bt_NotLiveCompletetProfile.setVisibility(View.VISIBLE);
                } else {
                    tvTitleNotLive.setText("You Profile is  Live");
                    tvNotLiveTitleDetail.setText("Please complete your profile and verify email and phone.");
                    bt_NotLiveCompletetProfile.setVisibility(View.VISIBLE);

                }
            } else if (member_status > 4) {

                // you need to contact marrymax support for subscription
                //hide complete profile button
                llpProfileInComplete.setVisibility(View.VISIBLE);
                llDefault.setVisibility(View.GONE);
                llProfileLive.setVisibility(View.GONE);

                tvTitleNotLive.setText("You Profile is Not Live");
                tvNotLiveTitleDetail.setText("You need to contact marrymax support to purchase subscription.");
                bt_NotLiveCompletetProfile.setVisibility(View.GONE);
                //You need to contact marrymax support to purchase subscription

            }
        } else {


            //call subscription api to show data
            Members mem = new Members();
            mem.setPath(SharedPreferenceManager.getUserObject(context).getPath());
            mem.setMember_status(SharedPreferenceManager.getUserObject(context).getMember_status());

            Gson gson = new Gson();
            String memString = gson.toJson(mem);

            try {

                if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {


                    putRequestGetSubscription(new JSONObject(memString));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // }

        }

    }

    private void initilize(View view) {

        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarProjectMain);
        dataList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewAccountSMySubscription);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        recyclerAdapter = new RecyclerViewAdapterMyAccountSubscription(dataList, getContext());

        recyclerView.setAdapter(recyclerAdapter);
        llProfileLive = (LinearLayout) view.findViewById(R.id.LinearLayoutASProfileIsLive);

        llDefault = (LinearLayout) view.findViewById(R.id.LinearLayoutASDefaultSubscription);

        llpProfileInComplete = (LinearLayout) view.findViewById(R.id.LinearLayoutASProfileNotLive);

        llSubscriptionQouta = (LinearLayout) view.findViewById(R.id.LinearLayoutSubscriptionQouta);


        llMySubscription = (LinearLayout) view.findViewById(R.id.LinearlayoutASMySubscription);


        //not live incomplete
        tvTitleNotLive = (TextView) view.findViewById(R.id.TextViewASTitleNotLive);
        tvNotLiveTitleDetail = (TextView) view.findViewById(R.id.TextViewASTitleNotLiveDetail);
        bt_NotLiveCompletetProfile = (AppCompatButton) view.findViewById(R.id.AppCompatButtonASTitleNotLiveCompleteProfile);


        //  bt_subscribe = (AppCompatButton) view.findViewById(R.id.ButtonAccountSettingMySubscriptionMain);
        bt_subscribe2 = (AppCompatButton) view.findViewById(R.id.ButtonAccountSettingMySubscriptionMain2);


//profile live layout
        btProfileLive = (AppCompatButton) view.findViewById(R.id.ButtonAccountSettingMySubscriptionProfileLiveButton);
        tProfileLive = (TextView) view.findViewById(R.id.TextViewAccountSettingMySubscriptionPorfileLIve);

        tvPlanMessagesQouta = (TextView) view.findViewById(R.id.TextViewSubsPlanMessagesQouta);
        tvSubsPlanContactsQouta = (TextView) view.findViewById(R.id.TextViewSubsPlanContactsQouta);


        //  ButtonAccountSettingMySubscriptionMain

/*        Members mem = new Members();
        mem.setPath(SharedPreferenceManager.getUserObject(getContext()).getPath());
        mem.setMember_status(SharedPreferenceManager.getUserObject(getContext()).getMember_status());

        Gson gson = new Gson();
        String memString = gson.toJson(mem);*/


        //  getSubscriptionRequest(SharedPreferenceManager.getUserObject(getContext()).getPath());


    }


    private void setListeners() {
        bt_NotLiveCompletetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //      new MarryMax(getActivity()).getProfileProgress(getContext(), SharedPreferenceManager.getUserObject(getContext()), getActivity());
                Intent in = new Intent(getActivity(), MainDirectiveActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("type", 22);
                getActivity().startActivity(in);


            }
        });
     /*   bt_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MarryMax marryMax = new MarryMax(getActivity());
                marryMax.subscribe();
            }
        });*/
        bt_subscribe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MarryMax marryMax = new MarryMax(getActivity());
                marryMax.subscribe();
            }
        });
    }

    private void putRequestGetSubscription(JSONObject params) {

        pDialog.setVisibility(View.VISIBLE);
        Log.e("GetSubscription params", params.toString());
        Log.e("GetSubscription ", Urls.subscriptionInfo);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.subscriptionInfo, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Res  subscription ", response.toString() + "");


                        try {
                            JSONArray jsonCountryStaeObj = response.getJSONArray("data").getJSONArray(0);
                            JSONObject jsonCountryStaeObj2 = response.getJSONArray("data").getJSONArray(1).getJSONObject(0);

                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();

                            Type listType = new TypeToken<List<Members>>() {
                            }.getType();
                            List<Members> dataList = (List<Members>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);


                            Type listType2 = new TypeToken<mSubQuotaInfo>() {
                            }.getType();

                            mSubQuotaInfo SubQuotaInfoObj = (mSubQuotaInfo) gsonc.fromJson(jsonCountryStaeObj2.toString(), listType2);


                            if (dataList.size() > 0) {

                                Members smem = dataList.get(0);
                                long member_status = SharedPreferenceManager.getUserObject(context).getMember_status();
                                if (member_status == 4) {

                                    llProfileLive.setVisibility(View.GONE);
                                    llDefault.setVisibility(View.GONE);
                                    llMySubscription.setVisibility(View.VISIBLE);
                                    llSubscriptionQouta.setVisibility(View.VISIBLE);
                                    recyclerAdapter.addAll(dataList);


                                    String msgText = "Your have used <font color='#9a0606'>" + "<b>" + SubQuotaInfoObj.getMessages() + "</b></font> Messages out of  <font color='#277410'>" + "<b>" + SubQuotaInfoObj.getMessages_count() + "</b></font>";
                                    tvPlanMessagesQouta.setText(Html.fromHtml(msgText));

                                    String cotactsText = "Your have used <font color='#9a0606'>" + "<b>" + SubQuotaInfoObj.getContacts() + "</b></font> Contacts out of  <font color='#277410'>" + "<b>" + SubQuotaInfoObj.getContacts_count() + "</b></font>";
                                    tvSubsPlanContactsQouta.setText(Html.fromHtml(cotactsText));


                                } else if (member_status == 3) {

                                    if (smem.getMy_id() == 0) {
                                        //show packages
                                        llProfileLive.setVisibility(View.GONE);

                                        llMySubscription.setVisibility(View.GONE);
                                        llSubscriptionQouta.setVisibility(View.GONE);
                                        llDefault.setVisibility(View.VISIBLE);


                                    } else if (smem.getMy_id() == 1) {

                                        llMySubscription.setVisibility(View.GONE);
                                        llSubscriptionQouta.setVisibility(View.GONE);

                                        llProfileLive.setVisibility(View.VISIBLE);


                                        tvTitleNotLive.setText("Your Profile is Live");
                                        tProfileLive.setText("Your Payment Pending for Approval, which will be done within 24 hours");
                                        btProfileLive.setVisibility(View.GONE);

                                        //   Toast.makeText(getContext(), "Your Payment Pending for Approval", Toast.LENGTH_SHORT).show();
                                        //Your Payment Pending for Approval
                                    }

                                }
                            }


                            //    Log.e("data list size", "" + dataList.size()+" =========================");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }








                  /*      if my_id == 1 {
                            display payment is waiting for approval
                        }
                        if (myid == 0) {
                            call api and display sub data
                        }
                        */





                     /*   try {
                            JSONObject responseObject = response.getJSONArray("data").getJSONArray(0).getJSONObject(0);



                          Gson gson;
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            Type type = new TypeToken<Members>() {
                            }.getType();
                            Members member2 = (Members) gson.fromJson(responseObject.toString(), type);
                            //  Log.e("interested id", "" + member.get_alias() + "====================");

                            dialogShowInterest newFragment = dialogShowInterest.newInstance(member, member.getUserpath(), replyCheck, member2);
                            newFragment.show(frgMngr, "dialog");



                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }*/


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
        //   MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);

    }


}
