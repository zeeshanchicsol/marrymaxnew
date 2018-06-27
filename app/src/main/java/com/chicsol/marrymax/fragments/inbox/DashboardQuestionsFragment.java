package com.chicsol.marrymax.fragments.inbox;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterInBoxList;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mComCount;
import com.chicsol.marrymax.modal.mCommunication;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 11/3/2016.
 */

public class DashboardQuestionsFragment extends Fragment implements RecyclerViewAdapterInBoxList.OnItemClickListener {

    RecyclerView recyclerView;
    private RecyclerViewAdapterInBoxList recyclerAdapter;
    private List<mCommunication> items;
    private ProgressBar pDialog;
    private CardView llEmptyState;
    private Context context;
    private int new_questions_count = 0;
    private TextView TextViewEmptyMessage;
    ViewGenerator viewGenerator;
    private LinearLayout llEmptySubItems;
    private String Tag = "DashboardMessagesFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_mymessages, container, false);

        initilize(rootView);
        setListenders();


        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


    private void initilize(View view) {

        items = new ArrayList<>();
        llEmptyState = (CardView) view.findViewById(R.id.LinearLayoutMyMessagesEmptyState);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewInboxListMain);
        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarProjectMain);

        viewGenerator = new ViewGenerator(context);


        TextViewEmptyMessage = (TextView) view.findViewById(R.id.TextViewEmptyMessage);
        llEmptySubItems = (LinearLayout) view.findViewById(R.id.LinearLayoutEmptySubItems);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterInBoxList(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(DashboardQuestionsFragment.this);


    }


    @Override
    public void onResume() {
        super.onResume();
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            getCommunicationCount();
            getRequest();
        }
    }

    private void setListenders() {

      /*  ll_messagedetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), DashboardMessagesDetailActivity.class);
                startActivity(in);
            }
        });*/


    }

    private void getRequest() {

      /*  final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        pDialog.setVisibility(View.VISIBLE);
        Log.e("api inbox list", "" + Urls.getQuestionInbox + SharedPreferenceManager.getUserObject(getContext()).get_path());

        JsonArrayRequest req = new JsonArrayRequest(Urls.getQuestionInbox + SharedPreferenceManager.getUserObject(getContext()).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("msg Response", response.toString());
                        try {
                            llEmptySubItems.removeAllViews();
                            StringBuilder htmlDescriptionText = new StringBuilder();

                            Members member = SharedPreferenceManager.getUserObject(context);
                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);
                            //   JSONArray jsonObjDMsgDetails = response.getJSONArray(1);

                          /*  Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<mCommunication>() {
                            }.getType();

                            mCommunication mComObj = (mCommunication) gsonc.fromJson(jsonObjDMsgDetails.getJSONObject(0).toString(), listType);

                            mComObj.getRead_quota();
                            mComObj.getWrite_quota();*/


                            if (member.get_member_status() < 3 || member.get_member_status() > 4) {

                                llEmptyState.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                if (new_questions_count == 0) {

                                    htmlDescriptionText.append(" There are  " + new_questions_count + "  questions. \n");
                                    htmlDescriptionText.append(" Find your matches and start communicating. \n");
                                    TextViewEmptyMessage.setText(htmlDescriptionText.toString());

                                }
                                if (new_questions_count > 0) {
                                    //     new_questions_count>0
                             /*
                                       Complete Your Profile*/
                                    htmlDescriptionText.append(" There are " + new_questions_count + "  unanswered questions. \n");
                                    htmlDescriptionText.append(" Please complete & verify your profile to see your messages. \n");
                                    TextViewEmptyMessage.setText(htmlDescriptionText.toString());

                                }

                            } else {


                                if (jsonCountryStaeObj.length() == 0) {


                                    if (member.get_member_status() == 3) {


                              /*      You have 0 unread messages
                                    Free members have a complimentary limited messaging quota to read and write message.
                                            Subscribe now to enjoy following benefits.
                                            Priority Profile Listing.
                                    Maximum interaction & quick connect with other members.
                                            More Privacy options.
                                    Personalized service from MarryMax when need.
                                                   Subscribe*/

                                        htmlDescriptionText.append("  You have " + new_questions_count + " unanswered questions. \n");
                                        //    htmlDescriptionText.append("  Free members have a complimentary limited \n messaging quota to read and write message.\n" +
                                        ///        "\n Subscribe now to enjoy following benefits. ");


                                        viewGenerator.generateTextViewWithIcon(llEmptySubItems, "Priority Profile Listing.");
                                        viewGenerator.generateTextViewWithIcon(llEmptySubItems, "Maximum interaction & quick connect with other members.");
                                        viewGenerator.generateTextViewWithIcon(llEmptySubItems, "More Privacy options.");
                                        viewGenerator.generateTextViewWithIcon(llEmptySubItems, "Personalized service from MarryMax when need.");

                                        TextViewEmptyMessage.setText(htmlDescriptionText.toString());

                                    } else if (member.get_member_status() == 4) {
                                    /*Find Matches here and take initiative in sending a personalized message to know more.
                                    Start a conversation, with simple greetings or by asking a question or talk about a specific interest in a simple and short message.

                                    Search*/
                                    }


                                    llEmptyState.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);

                                } else {


                                    if (member.get_member_status() == 3) {

/*
                                    You have 0 unread messages OR
                                    You have 1 unread messages
                                    You can send & read only one message per week.We suggest you to
                                    subscribe as being a paid member you will enjoyPriority Profile
                                    ListingView Verified PhoneMore Privacy
                                    OptionsPersonalized Assistance

                                    Subscribe*/
                                    } else if (member.get_member_status() == 4) {
                                 /*   You have 0unread messages   Or
                                    You have 1unread messages*/

                                    }


                                    llEmptyState.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    Gson gson;
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    gson = gsonBuilder.create();
                                    Type listTypea = new TypeToken<List<mCommunication>>() {
                                    }.getType();

                                    List<mCommunication> dlist = (List<mCommunication>) gson.fromJson(jsonCountryStaeObj.toString(), listTypea);

                                    if (dlist.size() > 0) {
                                        recyclerAdapter.addAll(dlist);
                                    }

                                }

                            }
                        } catch (
                                JSONException e)

                        {
                            e.printStackTrace();
                        }

                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.setVisibility(View.GONE);
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(

                getContext()).

                addToRequestQueue(req, Tag);

    }


    @Override
    public void onItemClick(View view, mCommunication communication) {
        // Toast.makeText(getContext(), "" + communication.getAlias(), Toast.LENGTH_SHORT).show();
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {



            Log.e(" objCom.request_type_id", "" +  communication.getRequest_type_id());

      Intent in = new Intent(getActivity(), DashboardQuestionsDetailActivity.class);
            Gson gson = new Gson();
            String memString = gson.toJson(communication);
            //in.putExtra("obj", memString);
            SharedPreferenceManager.setQuestionObject(getContext(), memString);
            in.putExtra("objtype", 1);
            startActivity(in);
        }
    }

    private void getCommunicationCount() {
       /* final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        Log.e("url", Urls.getCommunicationCount + SharedPreferenceManager.getUserObject(context).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getCommunicationCount + SharedPreferenceManager.getUserObject(context).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("getSavedList ", response.toString() + "  ==   ");
                        Gson gsonc;
                        GsonBuilder gsonBuilderc = new GsonBuilder();
                        gsonc = gsonBuilderc.create();
                        Type listType = new TypeToken<mComCount>() {
                        }.getType();
                        try {


                            mComCount comCount = (mComCount) gsonc.fromJson(response.getJSONArray(0).getJSONObject(0).toString(), listType);

                            Log.e("ressssss", comCount.getNew_interests_count() + "");
                            new_questions_count = (int) comCount.getNew_questions_count();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        //   pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());

                //     pDialog.dismiss();
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
    public void onStop() {
        super.onStop();
        MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);

    }

}
