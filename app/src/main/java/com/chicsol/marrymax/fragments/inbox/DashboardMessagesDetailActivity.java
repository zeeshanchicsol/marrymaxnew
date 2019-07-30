package com.chicsol.marrymax.fragments.inbox;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.chicsol.marrymax.activities.UserProfileActivityWithSlider;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterChatList;
import com.chicsol.marrymax.dialogs.dialogFeedback;
import com.chicsol.marrymax.dialogs.dialogFeedbackDetail;
import com.chicsol.marrymax.dialogs.dialogRequestPhone;
import com.chicsol.marrymax.interfaces.PhoneRequestCallBackInterface;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mCommunication;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;
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

public class DashboardMessagesDetailActivity extends AppCompatActivity implements RecyclerViewAdapterChatList.OnItemClickListener, dialogFeedback.onCompleteListener, dialogFeedbackDetail.onCompleteListener, PhoneRequestCallBackInterface, dialogRequestPhone.onCompleteListener {
    private TextView tvAge, tvAlias, tvEthnic, tvReligious, tvMarital,tvEducationTypes, tvCountry;
    RecyclerView recyclerView;
    private RecyclerViewAdapterChatList recyclerAdapter;
    private List<mCommunication> items;
    private FrameLayout fl_send_message;
    LinearLayout ll_DeleteChat, llFeedback, llMessageDetail, llReadQuota;
    EditText etSendMessage;
    mCommunication objCom;
    private ProgressBar pDialog;
    private TextView tvReadQuotaHeading, tvReadQuotaSubHeading, tvFeedback;
    private faTextView faFeedback;
    AppCompatButton btSubscribe;
    MarryMax marryMax;
    private LinearLayout llEmptySubItems;
    long match_id = 0;
    long feedback_id = 0;
    long read_quota = 0;


    private String Tag = "DashboardMessagesDetailActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard_mymessages_detail2);


        etSendMessage = (EditText) findViewById(R.id.EditTextChatListsendMessageDesc);

        String obh = SharedPreferenceManager.getMessageObject(getApplicationContext());
        int objtype = 0;
        //getIntent().getIntExtra("objtype", 0);

        Gson gson;
        GsonBuilder gsonBuildert = new GsonBuilder();
        gson = gsonBuildert.create();
        Type membert = new TypeToken<mCommunication>() {
        }.getType();
        objCom = (mCommunication) gson.fromJson(obh, membert);

        //Log.e("Loggg", "" + obh);
        //Log.e(objCom.getGender() + "=====Loggg" + objtype, "" + objCom.getCountry_name());
        etSendMessage.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        initialize(objCom, objtype);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void initialize(mCommunication obj, int objtype) {

        marryMax = new MarryMax(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Message History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pDialog = (ProgressBar) findViewById(R.id.ProgressbarProjectMain);
        pDialog.setVisibility(View.GONE);
        ll_DeleteChat = (LinearLayout) findViewById(R.id.LinearLayoutMessageDetailDeleteChat);

        llFeedback = (LinearLayout) findViewById(R.id.LinearLayoutMessageDetailFeedback);


        llMessageDetail = (LinearLayout) findViewById(R.id.LinearLayoutMessageDetailData);
        llReadQuota = (LinearLayout) findViewById(R.id.LinearLayoutMMessagesReadQuota);

        btSubscribe = (AppCompatButton) findViewById(R.id.ButtonMessageDetailSubscribe);


        fl_send_message = (FrameLayout) findViewById(R.id.FrameLayoutChatListSendMessage);
        tvAge = (TextView) findViewById(R.id.TextViewMessageDetailAge);
        tvAlias = (TextView) findViewById(R.id.TextViewMessageDetailAlias);
        tvEthnic = (TextView) findViewById(R.id.TextViewMessageDetailEthnicbg);
        tvReligious = (TextView) findViewById(R.id.TextViewMessageDetailReligious);
        tvMarital = (TextView) findViewById(R.id.TextViewMessageDetailMaritalStatus);

        tvEducationTypes = (TextView) findViewById(R.id.TextViewMessageDetailEducationTypes);

        tvCountry = (TextView) findViewById(R.id.TextViewMessageDetailLivingCountry);
        tvReadQuotaHeading = (TextView) findViewById(R.id.TextViewReadQuotaHeading);
        tvReadQuotaSubHeading = (TextView) findViewById(R.id.TextViewReadQuotaSubHeading);

        tvFeedback = (TextView) findViewById(R.id.TextViewUserProfileFeedback);
        faFeedback = (faTextView) findViewById(R.id.faUserProfileFeedback);

        llEmptySubItems = (LinearLayout) findViewById(R.id.LinearLayoutEmptySubItems);

        //Log.e(obj.getEthnic_background_type() + "=====Loggg" + objtype, "" + objCom.getCountry_name());

        tvAge.setText("( " + obj.getAge() + " Years)");
        tvAlias.setText(obj.getAlias());

   /*     if (objtype == 1) {
            llMessageDetail.setVisibility(View.VISIBLE);
        } else {
            llMessageDetail.setVisibility(View.GONE);
        }
*/
        tvEthnic.setText(objCom.getEthnic_background_type() + ", ");
        tvEducationTypes.setText(objCom.getEducation_types() + ", ");
        tvReligious.setText(objCom.getReligious_sect_type());
        tvMarital.setText(objCom.getGender() + " , ");

      /*  if (objtype == 1) {
            tvMarital.setVisibility(View.VISIBLE);
            tvMarital.setText(objCom.getGender() + " , ");
        } else {
            tvMarital.setVisibility(View.GONE);
        }*/


        tvCountry.setText(objCom.getCountry_name() + " | ");
        items = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewMessageListChatMain);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // mLayoutManager.setStackFromEnd(true);
        // mLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterChatList(getApplicationContext(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(this);


        JSONObject params = new JSONObject();
        try {


            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
            params.put("userpath", obj.getUserpath());
            getChatRequest(params);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setListeners();
    }

    private void setListeners() {
        tvAlias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (objCom.getUserpath() != null && objCom.getUserpath() != "") {

                    showUserProfile(objCom.getUserpath());


                } else {
                    Toast.makeText(getApplicationContext(), "Error ! ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        btSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarryMax marryMax = new MarryMax(DashboardMessagesDetailActivity.this);
                marryMax.subscribe();
            }
        });
        etSendMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() == 3 && read_quota == 1) {
                    Toast.makeText(DashboardMessagesDetailActivity.this, "Dear " + SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias() + ", Please! Subscribe to send message and view verified phone numbers.", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fl_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(etSendMessage.getText().toString().trim())) {
                    JSONObject params = new JSONObject();
                    try {

                        if (etSendMessage.getText().toString().trim().length() < 2) {
                            Toast.makeText(DashboardMessagesDetailActivity.this, "Min 2 & max 2000 characters", Toast.LENGTH_SHORT).show();

                        } else {


                            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                            params.put("userpath", objCom.getUserpath());
                            params.put("alias", SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias());
                            params.put("default_image", SharedPreferenceManager.getUserObject(getApplicationContext()).getDefault_image());
                            params.put("message", etSendMessage.getText().toString());

                            if (SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() == 3 && read_quota == 1) {
                                Toast.makeText(DashboardMessagesDetailActivity.this, "Dear " + SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias() + ", Please! Subscribe to send message and view verified phone numbers.", Toast.LENGTH_LONG).show();

                            } else {
                                putSendMessage(params);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        llFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (feedback_id == 0) {


                    JSONObject params = new JSONObject();
                    try {
                        params.put("userpath", objCom.getUserpath());
                        params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

                        dialogFeedback newFragment = dialogFeedback.newInstance(objCom.getUserpath(), match_id + "", params.toString());
                        //newFragment.setTargetFragment(fragment, 0);
                        newFragment.show(getSupportFragmentManager(), "dialog");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    // ViewFeedback
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", match_id);
                        jsonObject.put("my_id", feedback_id);
                        jsonObject.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                        jsonObject.put("userpath", objCom.getUserpath());
                        dialogFeedbackDetail newFragment = dialogFeedbackDetail.newInstance(jsonObject.toString());
                        //  newFragment.setTargetFragment(getSupportFragmentManager(), 0);
                        newFragment.show(getSupportFragmentManager(), "dialog");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        ll_DeleteChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardMessagesDetailActivity.this);

                builder.setTitle("Delete Chat");
                builder.setMessage("Are you sure, delete complete message history between you and " + objCom.getAlias() + " ?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        try {


                            JSONObject params1 = new JSONObject();
                            params1.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                            params1.put("userpath", objCom.getUserpath());
                            deleteRequest(params1);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private void putSendMessage(JSONObject params) {

        final ProgressDialog pDialog1 = new ProgressDialog(DashboardMessagesDetailActivity.this);
        pDialog1.setMessage("Loading...");
        pDialog1.setCancelable(false);
        pDialog1.show();


        //Log.e("sendMessage Params", Urls.sendMessage + "    " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.sendMessage, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  message sent", response + "");
                        try {

                            JSONArray jsdata = response.getJSONArray("data");


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mCommunication>>() {
                            }.getType();

                            List<mCommunication> dlist = (List<mCommunication>) gsonc.fromJson(jsdata.getJSONArray(0).toString(), listType);

                            //  Log.e("list size", "" + dlist.size());
                            if (dlist.size() > 0) {
                                mCommunication mCommunication = dlist.get(0);


                                if (mCommunication.write_quota == 0) {
                                    if (SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() == 4) {

                                        Toast.makeText(DashboardMessagesDetailActivity.this, "Dear " + SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias() + ", you have send too many messages. In order to avoid spam please wait for sometime to send more messages.", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(DashboardMessagesDetailActivity.this, "Dear " + SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias() + ", Please! Subscribe to send message and view verified phone numbers.", Toast.LENGTH_LONG).show();

                                    }

                                } else if (mCommunication.write_quota == 1) {
                                    etSendMessage.setText("");
                                    JSONObject params1 = new JSONObject();


                                    params1.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                                    params1.put("userpath", objCom.getUserpath());
                                    getChatRequest(params1);
                                }
/*
                                0            Don't do anything
                                1            Phone is public,give option to show phone number
                                2            Phone is protected, give option to send request to view phone number
*/

                                //  mCommunication.setId(1);
                                if (mCommunication.getId() == 1) {
                                    //     Phone is public,give option to show phone number
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DashboardMessagesDetailActivity.this);
                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:

                                                    JSONObject params = new JSONObject();
                                                    try {
                                                        params.put("userpath", objCom.getUserpath());
                                                        params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    marryMax.getMobileInfo(params, objCom.getAlias(), getApplicationContext(), null, getSupportFragmentManager());
                                                    break;

                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    dialog.dismiss();
                                                    break;
                                            }
                                        }
                                    };


                                    builder.setMessage("Do you want to see contact details of " + objCom.getAlias() + " ?").setPositiveButton("Yes", dialogClickListener)
                                            .setNegativeButton("No", dialogClickListener).show();


                                } else if (mCommunication.getId() == 2) {
                                    //     Phone is protected, give option to send request to view phone number
                                    if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
                                        // selectedPosition = position;
                                        //   marryMax.statusBaseChecks(null, getApplicationContext(), 4, getSupportFragmentManager(), null, null, null, null, null, null);


                                        JSONObject params = new JSONObject();
                                        try {
                                            params.put("alias", SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias());
                                            params.put("type", "5");
                                            params.put("userpath", objCom.getUserpath());
                                            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        String desc = "Request <b> <font color=#216917>" + objCom.getAlias() + "</font></b>" + " for  Contact Details";

                                        dialogRequestPhone newFragment = dialogRequestPhone.newInstance(params.toString(), "Request Contact Details", desc);
                                        newFragment.setListener(DashboardMessagesDetailActivity.this);
                                    /*    if (fragment != null) {
                                            newFragment.setTargetFragment(fragment, 0);
                                        }*/
                                        newFragment.show(getSupportFragmentManager(), "dialog");


                                    }
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog1.hide();
                        }

                        pDialog1.hide();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();
                pDialog1.hide();

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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, Tag);

    }

    private void deleteRequest(JSONObject params) {
        pDialog.setVisibility(View.VISIBLE);


        //Log.e("Params", Urls.deleteMessages + "    " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.deleteMessages, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  update appearance", response + "");
                        try {

                            int responseid = response.getInt("id");
                            if (responseid == 1) {
                                Toast.makeText(DashboardMessagesDetailActivity.this, "Chat Deleted", Toast.LENGTH_SHORT).show();
                                recyclerAdapter.clear();
                                finish();

                             /*   JSONObject params1 = new JSONObject();
                                params1.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                                params1.put("userpath", objCom.getUserpath());
                                getChatRequest(params1);*/


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
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.setVisibility(View.GONE);
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, Tag);

    }

    private void getChatRequest(JSONObject params) {
        ll_DeleteChat.setVisibility(View.GONE);
        final ProgressDialog pDialog = new ProgressDialog(DashboardMessagesDetailActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        //Log.e("Params", Urls.messageDetail + "    " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.messageDetail, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  getChatRequest", response + "");
                        try {


                            JSONArray jsonObj = response.getJSONArray("data");


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mCommunication>>() {
                            }.getType();

                            List<mCommunication> dlist = (List<mCommunication>) gsonc.fromJson(jsonObj.getJSONArray(0).toString(), listType);

                            List<mCommunication> dlist2 = (List<mCommunication>) gsonc.fromJson(jsonObj.getJSONArray(1).toString(), listType);


                            if (dlist2.size() > 0) {
                                mCommunication mCom = dlist2.get(0);

                                match_id = mCom.getId();
                                feedback_id = mCom.getRequest_type_id();
                                read_quota = mCom.getRead_quota();

                                //  Log.e("mCom", mCom.getRead_quota() + " == " );

                                if (match_id != 0) {
                                    llFeedback.setVisibility(View.VISIBLE);
                                    if (feedback_id == 0) {
                                        //  holder.llFeedback.setVisibility(View.VISIBLE);
                                        tvFeedback.setText("Give Feedback");
                                        tvFeedback.setTextColor(getResources().getColor(R.color.colorTextRed));
                                        faFeedback.setTextColor(getResources().getColor(R.color.colorTextRed));

                                    } else {
                                        // holder.llViewFeedback.setVisibility(View.VISIBLE);
                                        tvFeedback.setText("View Feedback");
                                        tvFeedback.setTextColor(getResources().getColor(R.color.colorGrey));
                                        faFeedback.setTextColor(getResources().getColor(R.color.colorGrey));

                                    }
                                } else {
                                    llFeedback.setVisibility(View.GONE);

                                }
                            }


                            if (SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() == 3) {
                                if (dlist2.size() > 0) {
                                    mCommunication mCom = dlist2.get(0);


                                    if (mCom.read_quota == 0 && mCom.count > 0) {
                                        llReadQuota.setVisibility(View.VISIBLE);
                                        String headertxt = "<b>" + mCom.getCount() + "</b> unread messages from <font color='#9a0606'>" + "<b>" + objCom.getAlias().toUpperCase() + "</b></font>";
                                        tvReadQuotaHeading.setText(Html.fromHtml(headertxt));
                                        String subheadertxt = "Dear <font color='#9a0606'><b>" + SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias() + "</b></font> , Please subscribe to send personalized message and connect with the potential matches immediately.Subscribe now to enjoy following benefits. \n \u25CF As a free member you can find & view your matches. \n \u25CF To read & write direct messages, please upgrade your account to a subscriber.";
                                        tvReadQuotaSubHeading.setText(Html.fromHtml(subheadertxt));
                                        if (SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() >= 4) {
                                            btSubscribe.setVisibility(View.GONE);
                                        }

                                    }
                                }
                            }/* else if (SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() == 4) {


                            } */ else {
                                llReadQuota.setVisibility(View.GONE);

                            }


                            if (dlist.size() > 0) {
                                ll_DeleteChat.setVisibility(View.VISIBLE);


                                recyclerAdapter.addAll(dlist);
                                scrollToBottom();
                            } else {
                                ll_DeleteChat.setVisibility(View.GONE);

                            }
                        } catch (Exception e) {

                            //Log.e("Exception here", "Exception");
                            e.printStackTrace();
                            pDialog.dismiss();
                        }/* catch (TransactionTooLargeException e)
                        {
                            Log.e("Exception here","Exception");
                            e.printStackTrace();
                            pDialog.dismiss();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, Tag);

    }

    @Override
    public void onItemClick(View view, mCommunication communication) {

    }

    public void onSubscribeClick(View view) {
        MarryMax marryMax = new MarryMax(DashboardMessagesDetailActivity.this);
        marryMax.subscribe();

    }

    //method to scroll the recyclerview to bottom
    private void scrollToBottom() {
        recyclerAdapter.notifyDataSetChanged();
        if (recyclerAdapter.getItemCount() > 1)
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, recyclerAdapter.getItemCount() - 1);
    }
  /*  private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }*/


    private void showUserProfile(String path) {
        Intent intent = new Intent(getApplicationContext(), UserProfileActivityWithSlider.class);

        intent.putExtra("selectedposition", "-1");
        List<Members> memberDataList = new ArrayList<Members>();
        Members members = new Members();
        members.setUserpath(path);
        memberDataList.add(members);
        SharedPreferenceManager.setMemberDataList(getApplicationContext(), new Gson().toJson(memberDataList).toString());
        startActivity(intent);

    }

    @Override
    public void onStop() {
        super.onStop();
        MySingleton.getInstance(getApplicationContext()).cancelPendingRequests(Tag);

    }

    @Override
    public void onComplete(String s) {

    }

    @Override
    public void onPhoneViewRequestComplete(String requestid) {

    }


}
