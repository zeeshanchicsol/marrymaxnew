package com.chicsol.marrymax.fragments.inbox;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.chicsol.marrymax.adapters.RecyclerViewAdapterQuestionsList;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mCommunication;
import com.chicsol.marrymax.modal.mIceBreak;
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
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 11/3/2016.
 */

public class DashboardQuestionsDetailActivity extends AppCompatActivity implements RecyclerViewAdapterQuestionsList.OnItemClickListener {
    private TextView tvAge, tvAlias, tvEthnic, tvReligious, tvMarital, tvCountry;
    RecyclerView recyclerView;
    private RecyclerViewAdapterQuestionsList recyclerAdapter;
    private List<mCommunication> items;

    LinearLayout ll_DeleteChat, llMessageDetail, llReadQuota;

    mCommunication objCom;
    private ProgressDialog pDialog;
    private TextView tvReadQuotaHeading, tvReadQuotaSubHeading, tvSubject;
    AppCompatButton btSubscribe;
    MarryMax marryMax;
    AppCompatButton btSendAnswers;

    private String Tag = "DashboardMessagesDetailActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard_questions_detail2);


        String obh = SharedPreferenceManager.getQuestionObject(getApplicationContext());

        //    Log.e(" obh", "" + obh);

        int objtype = 0;
        //getIntent().getIntExtra("objtype", 0);

        Gson gson;
        GsonBuilder gsonBuildert = new GsonBuilder();
        gson = gsonBuildert.create();
        Type membert = new TypeToken<mCommunication>() {
        }.getType();
        objCom = (mCommunication) gson.fromJson(obh, membert);

        //   Log.e("Loggg", "" + obh);

        //   Log.e(objCom.getGender() + "=====Loggg" + objtype, "" + objCom.request_type_id);

        //Log.e(" objCom.request_type_id", "" + objCom.request_type_id);

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
        toolbar.setTitle("You are Asked Question(s)!");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ll_DeleteChat = (LinearLayout) findViewById(R.id.LinearLayoutMessageDetailDeleteChat);
        llMessageDetail = (LinearLayout) findViewById(R.id.LinearLayoutMessageDetailData);
        llReadQuota = (LinearLayout) findViewById(R.id.LinearLayoutMMessagesReadQuota);

        btSubscribe = (AppCompatButton) findViewById(R.id.ButtonMessageDetailSubscribe);


        tvSubject = (TextView) findViewById(R.id.TextViewQuestionDetailSubject);
        tvAge = (TextView) findViewById(R.id.TextViewMessageDetailAge);
        tvAlias = (TextView) findViewById(R.id.TextViewMessageDetailAlias);
        tvEthnic = (TextView) findViewById(R.id.TextViewMessageDetailEthnicbg);
        tvReligious = (TextView) findViewById(R.id.TextViewMessageDetailReligious);
        tvMarital = (TextView) findViewById(R.id.TextViewMessageDetailMaritalStatus);
        tvCountry = (TextView) findViewById(R.id.TextViewMessageDetailLivingCountry);
        tvReadQuotaHeading = (TextView) findViewById(R.id.TextViewReadQuotaHeading);
        tvReadQuotaSubHeading = (TextView) findViewById(R.id.TextViewReadQuotaSubHeading);


        btSendAnswers = (AppCompatButton) findViewById(R.id.ButtonSendAnswers);

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
        tvReligious.setText(objCom.getReligious_sect_type());

        if (objtype == 1) {
            tvMarital.setVisibility(View.VISIBLE);
            tvMarital.setText(objCom.getGender() + " , ");
        } else {
            tvMarital.setVisibility(View.GONE);
        }


        tvCountry.setText(objCom.getCountry_name() + " | ");
        items = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewMessageListChatMain);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // mLayoutManager.setStackFromEnd(true);
        // mLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterQuestionsList(getApplicationContext(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(this);


        JSONObject params = new JSONObject();
        try {

         /*   session_id: request_type_id,
                    answered: answered,
                    num: self*/
            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
            params.put("userpath", obj.getUserpath());
            params.put("session_id", obj.getRequest_type_id());
            params.put("answered", obj.getAnswered());
            params.put("num", obj.getSelf());

            params.put("alias", obj.getAlias());
            //   path,  userpath, alias, questionids
            getChatRequest(params);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (obj.getAnswered() == 0 && obj.getSelf() == 1) {
            btSendAnswers.setText("Send Answer");
        } else {
            //  btSendAnswers.setText("Send Message");
            btSendAnswers.setVisibility(View.GONE);
        }


        //Log.e("Subject", "" + obj.getSubject());

        tvSubject.setText(obj.getSubject());

        setListeners(obj);
    }

    private void setListeners(final mCommunication obj) {


        btSendAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (obj.getAnswered() == 0 && obj.getSelf() == 1) {

                    if (recyclerAdapter != null) {


                        StringBuilder stringBuilder = new StringBuilder();
                        int count = recyclerAdapter.getItemCount();


                        HashMap<String, String> ansList = recyclerAdapter.getmCheckedAnswersList();

                        if (count != ansList.size()) {
                            Toast.makeText(DashboardQuestionsDetailActivity.this, "  Please answer all questions", Toast.LENGTH_SHORT).show();
                        } else {

                            Iterator it = ansList.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pair = (Map.Entry) it.next();
                                // System.out.println(pair.getKey() + " = " + pair.getValue());

                                if (it.hasNext()) {
                                    stringBuilder.append(pair.getValue() + ",");
                                } else {
                                    stringBuilder.append(pair.getValue());
                                }


                                // it.remove(); // avoids a ConcurrentModificationException
                            }

                            //Log.e("answerids", stringBuilder.toString());

                            //path, alias, answerids, session_id
                            //  putSendAnswer();

                            JSONObject params = new JSONObject();
                            try {
                                params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                                params.put("alias", SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias());
                                params.put("answerids", stringBuilder.toString());
                                params.put("session_id", objCom.getRequest_type_id());
                                putSendAnswer(params);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }


                } else {
                    btSendAnswers.setText("Send Message");
                    Intent in = new Intent(DashboardQuestionsDetailActivity.this, DashboardMessagesDetailActivity.class);
                    Gson gson = new Gson();
                    String memString = gson.toJson(obj);
                    //in.putExtra("obj", memString);
                    SharedPreferenceManager.setMessageObject(getApplicationContext(), memString);
                    in.putExtra("objtype", 1);
                    startActivity(in);


                }


            }
        });


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
                MarryMax marryMax = new MarryMax(DashboardQuestionsDetailActivity.this);
                marryMax.subscribe();
            }
        });


        ll_DeleteChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardQuestionsDetailActivity.this);

                builder.setTitle("Delete Questions");
                builder.setMessage("Are you sure you want to remove this questioning session between you and  " + objCom.getAlias() + " ?");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        try {


                            JSONObject params1 = new JSONObject();
                            params1.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                            params1.put("id", objCom.getRequest_type_id());
                            deleteQuestion(params1);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

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

    private void putSendAnswer(JSONObject params) {

        showProgressDialog();


        //Log.e("Params", Urls.sendAnswer + "    " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.sendAnswer, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  message sent", response + "");
                        try {

                            int responseid = response.getInt("id");
                            if (responseid >= 1) {
                                Toast.makeText(DashboardQuestionsDetailActivity.this, "Answers Sent", Toast.LENGTH_SHORT).show();
                                recyclerAdapter.clear();
                                finish();

                             /*   JSONObject params1 = new JSONObject();
                                params1.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                                params1.put("userpath", objCom.getUserpath());
                                getChatRequest(params1);*/


                            }

                        } catch (JSONException e) {
                            dismissProgressDialog();

                            e.printStackTrace();
                        }


                   /*     try {

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

                                        Toast.makeText(DashboardQuestionsDetailActivity.this, "Dear " + SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias() + ", you have send too many messages. In order to avoid spam please wait for sometime to send more messages.", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(DashboardQuestionsDetailActivity.this, "Dear " + SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias() + ", your complementary free messaging quota is exhausted. As a free member you can send only one message in three days.", Toast.LENGTH_LONG).show();

                                    }

                                } else if (mCommunication.write_quota == 1) {
                                    //  etSendMessage.setText("");
                                    JSONObject params1 = new JSONObject();


                                    params1.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                                    params1.put("userpath", objCom.getUserpath());
                                    params1.put("request_type_id", objCom.getRequest_type_id());
                                    params1.put("alias", objCom.getAlias());
                                    getChatRequest(params1);
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog1.hide();
                        }*/

                        dismissProgressDialog();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();
                dismissProgressDialog();

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

    private void deleteQuestion(JSONObject params) {
        showProgressDialog();


        //Log.e("Params", Urls.deleteQuestion + "    " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.deleteQuestion, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  update appearance", response + "");
                        try {

                            int responseid = response.getInt("id");
                            if (responseid >= 1) {
                                Toast.makeText(DashboardQuestionsDetailActivity.this, "Questions Deleted", Toast.LENGTH_SHORT).show();
                                recyclerAdapter.clear();
                                finish();

                             /*   JSONObject params1 = new JSONObject();
                                params1.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                                params1.put("userpath", objCom.getUserpath());
                                getChatRequest(params1);*/


                            }

                        } catch (JSONException e) {
                            dismissProgressDialog();

                            e.printStackTrace();
                        }

                        dismissProgressDialog();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                dismissProgressDialog();
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
        final List<List<mIceBreak>> QuestionChoiceList = new ArrayList<>();
        final List<mIceBreak> QuestionsList = new ArrayList<>();

        showProgressDialog();
     //   Log.e("questionDetails", Urls.questionDetails + "    " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.questionDetails, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  getChatRequest", response + "");
                        try {

                            JSONArray jsonObj = response.getJSONArray("data");
                            //Log.e("re  getChatRequest", jsonObj.length() + "");


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mIceBreak>>() {
                            }.getType();

                            for (int i = 0; i < jsonObj.length(); i++) {
                                if (i != jsonObj.length() - 1) {

                                    if (jsonObj.getJSONArray(i).length() > 0) {

                                        List<mIceBreak> dlist = (List<mIceBreak>) gsonc.fromJson(jsonObj.getJSONArray(i).toString(), listType);

                                        QuestionChoiceList.add(dlist);

                                        //Log.e("ssssss", dlist.size() + "");
                                    }

                                } else {
                                    //last array
                                    List<mIceBreak> dlist = (List<mIceBreak>) gsonc.fromJson(jsonObj.getJSONArray(i).toString(), listType);

                                    QuestionsList.addAll(dlist);
                                }


                            }
                            recyclerAdapter.setQuestionChoiceList(QuestionChoiceList);
                            recyclerAdapter.addAll(QuestionsList);


                            //     Log.e("QuestionChoiceList  ", QuestionChoiceList.size() + "");
                            //     Log.e("QuestionsList  ", QuestionsList.size() + "");

              /*


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mCommunication>>() {
                            }.getType();

                            List<mCommunication> dlist = (List<mCommunication>) gsonc.fromJson(jsonObj.getJSONArray(0).toString(), listType);

                            List<mCommunication> dlist2 = (List<mCommunication>) gsonc.fromJson(jsonObj.getJSONArray(1).toString(), listType);
*/

                     /*       if (dlist.size() > 0) {
                                ll_DeleteChat.setVisibility(View.VISIBLE);

                                if (SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() == 3) {
                                    if (dlist2.size() > 0) {
                                        mCommunication mCom = dlist2.get(0);
                                        if (mCom.read_quota == 0 && mCom.count > 0) {
                                            llReadQuota.setVisibility(View.VISIBLE);
                                            String headertxt = "<b>" + mCom.getCount() + "</b> unread messages from <font color='#9a0606'>" + "<b>" + objCom.getAlias().toUpperCase() + "</b></font>";
                                            tvReadQuotaHeading.setText(Html.fromHtml(headertxt));
                                            String subheadertxt = "Dear <font color='#9a0606'><b>" + SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias() + "<b></font> , your free message quota is exhausted. <font color='#9a0606'>" + "<b>" + "</b></font>";
                                            tvReadQuotaSubHeading.setText(Html.fromHtml(subheadertxt));
                                            if (SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() >= 4) {
                                                btSubscribe.setVisibility(View.GONE);
                                            }

                                        }
                                    }
                                } else {
                                    llReadQuota.setVisibility(View.GONE);

                                }
                                recyclerAdapter.addAll(dlist);
                                scrollToBottom();
                            } else {
                                ll_DeleteChat.setVisibility(View.GONE);

                            }*/
                        } catch (Exception e) {

                            //Log.e("Exception here", "Exception");
                            e.printStackTrace();
                            dismissProgressDialog();
                        }/* catch (TransactionTooLargeException e)
                        {
                            Log.e("Exception here","Exception");
                            e.printStackTrace();
                            pDialog.dismiss();
                        }*/

                        dismissProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                dismissProgressDialog();
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

        Toast.makeText(this, "asdsadsa", Toast.LENGTH_SHORT).show();


    }

    public void onSubscribeClick(View view) {
        MarryMax marryMax = new MarryMax(DashboardQuestionsDetailActivity.this);
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
    public void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(DashboardQuestionsDetailActivity.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
        }
        pDialog.show();
    }

    private void dismissProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }


}
