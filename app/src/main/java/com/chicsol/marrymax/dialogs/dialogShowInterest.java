package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.interfaces.UpdateMemberFromDialogFragment;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mMemInterest;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mCheckBox;
import com.chicsol.marrymax.widgets.mTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogShowInterest extends DialogFragment {
    // private ListView lv_mycontacts;
    public onCompleteListener mCompleteListener;
    String interested_id, image_view, phone_view, my_id;
    mCheckBox cbAllowPhone, cbAllowPics;
    mTextView tvDesc, tvDialogPhonePicsTitle;
    String userpath, alias;
    boolean replyCheck, subscribe = false;

    private UpdateMemberFromDialogFragment updateMember;
    private Context context;


    private int feedback_due;
    private AppCompatButton btGiveFeedbackInterest;
    private CardView cvFeedbackPending;
    private TextView tvFeedbackPending;
    private AppCompatButton btGiveFeedback;

    public void setListener(UpdateMemberFromDialogFragment listener) {
        updateMember = listener;
    }


    public static dialogShowInterest newInstance(Members member, String userpath, boolean replyCheck, mMemInterest member2, int feedback_due) {

        dialogShowInterest frag = new dialogShowInterest();
        Bundle args = new Bundle();


        args.putBoolean("replyCheck", replyCheck);
        args.putString("name", String.valueOf(member2.getInterested_id()));
        args.putString("desc", String.valueOf(member2.getImage_view()));
        args.putString("param", String.valueOf(member2.getPhone_view()));
        args.putString("my_id", String.valueOf(member2.getMy_id()));
        args.putString("alias", member.getAlias());
        args.putString("userpath", userpath);
        args.putInt("feedback_due", feedback_due);


        frag.setArguments(args);
        return frag;
    }

    /*  @Override
           public void onAttach(Context context) {
               super.onAttach(context);
               try {
                   this.mCompleteListener = (onCompleteListener) context;
               } catch (ClassCastException e) {
                   throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
               }
           }*/
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            context = activity;

            //Log.e("getfragment manager", "======" + getTargetFragment());
            if (getTargetFragment() != null) {
                mCompleteListener = (onCompleteListener) getTargetFragment();
            } else {
                mCompleteListener = (onCompleteListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        replyCheck = mArgs.getBoolean("replyCheck");
        interested_id = mArgs.getString("name");
        image_view = mArgs.getString("desc");
        phone_view = mArgs.getString("param");
        my_id = mArgs.getString("my_id");
        userpath = mArgs.getString("userpath");
        alias = mArgs.getString("alias");
        feedback_due = mArgs.getInt("feedback_due");

      /*  Log.e("name Fragment", interested_id);
        Log.e("desc Fragment", image_view);
        Log.e("param Fragment", phone_view);
        Log.e("my_id Fragment", my_id);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_interest, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tvFeedbackPending = (TextView) rootView.findViewById(R.id.TextViewDashMainFeedbackPending);
        cvFeedbackPending = (CardView) rootView.findViewById(R.id.CardViewDashMainFeedbackPending);
        btGiveFeedback = (AppCompatButton) rootView.findViewById(R.id.ButtonDashMainFeedbackPending);
        btGiveFeedback.setVisibility(View.GONE);


        cbAllowPhone = (mCheckBox) rootView.findViewById(R.id.CheckBoxInterestAllowPhone);
        cbAllowPics = (mCheckBox) rootView.findViewById(R.id.CheckBoxInterestAllowPics);
        tvDesc = (mTextView) rootView.findViewById(R.id.TextViewDialogInterestDetails);
        tvDialogPhonePicsTitle = (mTextView) rootView.findViewById(R.id.TextViewDialogPhonePicsTitle);
        btGiveFeedbackInterest = (AppCompatButton) rootView.findViewById(R.id.mButtonInterestGiveFeedback);


        Button mOkButton = (Button) rootView.findViewById(R.id.mButtonExpressWithDrawInterest);

/*

        String aliass= SharedPreferenceManager.getUserObject(getContext()).getAlias();
        String aliasn = "<font color='#9a0606'>" + aliass + "!</font><br>";
        if (feedback_due == 1) {

            String text = "Dear " + "<b>" + aliasn.toUpperCase() + "</b> your Feedback is Pending. To continue viewing more phone numbers or sending interest you need to provide feedback.";
            tvFeedbackPending.setText(Html.fromHtml(text));
            btGiveFeedbackInterest.setVisibility(View.VISIBLE);
            cvFeedbackPending.setVisibility(View.VISIBLE);

        } else if (feedback_due == 2) {
            String text = "Dear " + "<b>" + aliasn.toUpperCase() + "</b> Your Feedback are due. To continue viewing more phone numbers or sending interest you need to provide feedback.";
            tvFeedbackPending.setBackgroundColor(Color.parseColor("#fff5d7"));
            tvFeedbackPending.setText(Html.fromHtml(text));
            btGiveFeedbackInterest.setVisibility(View.VISIBLE);
            cvFeedbackPending.setVisibility(View.VISIBLE);

        } else {
            btGiveFeedbackInterest.setVisibility(View.GONE);
            cvFeedbackPending.setVisibility(View.GONE);
        }
*/


        MarryMax max = new MarryMax(null);
        if (feedback_due == 0) {
            cvFeedbackPending.setVisibility(View.GONE);
            btGiveFeedbackInterest.setVisibility(View.GONE);

        } else {

            String desc = max.getFeedbackText(feedback_due, context);
            tvFeedbackPending.setText(Html.fromHtml(desc));
            cvFeedbackPending.setVisibility(View.VISIBLE);
            btGiveFeedbackInterest.setVisibility(View.VISIBLE);
        }
        if (feedback_due == 2) {
            tvDesc.setVisibility(View.GONE);
        }


        if (my_id.equals("0")) {
            String txt = "<font color='#9a0606'>" + alias + "</font>";
            tvDesc.setText(Html.fromHtml("At this time, " + "<b>" + txt.toUpperCase() + "</b> is accepting interest only from preferred matches. "));
            mOkButton.setVisibility(View.INVISIBLE);
        } else if (my_id.equals("-1")) {

            String desctxt = "";

            if (SharedPreferenceManager.getUserObject(context).getMember_status() == 3) {

                desctxt = "\u25CF Daily sent limit is reached.\n" +
                        "\u25CF Please wait 24 hours before you can contact new  members.\n";

                mOkButton.setText("Subscribe");
                subscribe = true;



            } else if (SharedPreferenceManager.getUserObject(context).getMember_status() == 4) {


                desctxt = "You have reached the contact limit.\n" +
                        "Please wait 24 hours to send new request.\n";

                mOkButton.setVisibility(View.GONE);
            }


            tvDesc.setText(desctxt);


        } else if (my_id.equals("1")) {

            String txt = "<font color='#9a0606'>" + alias + "</font>";
            //Log.e("insterested",""+txt);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvDesc.setText(Html.fromHtml("Great! You are interested in " + "<b>" + txt.toUpperCase() + "</b>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvDesc.setText(Html.fromHtml("Great! You are interested in " + "<b>" + txt.toUpperCase() + "</b>"));

            }


            if (image_view.equals("2") || phone_view.equals("5")) {

                if (image_view.equals("2")) {
                    if (!replyCheck) {
                        cbAllowPics.setVisibility(View.VISIBLE);
                        tvDialogPhonePicsTitle.setVisibility(View.VISIBLE);
                    } else {
                        cbAllowPics.setVisibility(View.VISIBLE);
                        cbAllowPics.setText("Would you like to give permission to view your image?");
                    }
                    mOkButton.setVisibility(View.VISIBLE);

                }
                if (phone_view.equals("5")) {
                    if (!replyCheck) {
                        tvDialogPhonePicsTitle.setVisibility(View.VISIBLE);
                        cbAllowPhone.setVisibility(View.VISIBLE);
                    } else {
                        cbAllowPhone.setVisibility(View.VISIBLE);
                        cbAllowPhone.setText("Would you like to give permission to view your phone?");
                    }
                    mOkButton.setVisibility(View.VISIBLE);
                }
            }


        }


    /*    lv_mycontacts=(ListView)rootView.findViewById(R.id.ListViewMySavedList);
        List<String> dataList=new ArrayList<>();
        dataList.add("asdsadsa");
        dataList.add("1212321");
        MyContactsListAdapter myContactsListAdapter=new MyContactsListAdapter(getActivity(),R.layout.item_list_my_saved_list,dataList);
        lv_mycontacts.setAdapter(myContactsListAdapter);*/

   /*     Bundle mArgs = getArguments();
        String myValue = mArgs.getString("jsondata");
        try {
            JSONArray jsonArray = new JSONArray(myValue);
            JSONArray dosDataList = jsonArray.getJSONArray(0);
            JSONArray dontsDataList = jsonArray.getJSONArray(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/





     /*   RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.RadioGroupProfileFor);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //  radioGroup.ad
        for (int i = 0; i < DataList.size(); i++) {
            mRadioButton mradiobutton = new mRadioButton(getContext());
            mradiobutton.setText(DataList.get(i));
            mradiobutton.setLayoutParams(layoutParams);

            radioGroup.addView(mradiobutton);
        }*/

        /*//do's
        for (int i=0;i<jsonArray1.length();i++){
            JSONObject jsonObject= jsonArray1.getJSONObject(i);
            Log.e("value",""+jsonObject.get("name").toString());
        }

        //dont's
        for (int i=0;i<jsonArray2.length();i++){
            JSONObject jsonObject= jsonArray2.getJSONObject(i);
            Log.e("value 2",""+jsonObject.get("name").toString());
        }*/

        btGiveFeedbackInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 25);
                // in.putExtra("subtype", "received");
                startActivity(in);
            }
        });


        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                if (!subscribe) {
                    JSONObject params = new JSONObject();
                    try {

                        //   params.put("selectdlist", selectdlist);

                        /*
                         */

                        //1 or 0
                        if (cbAllowPics.isChecked()) {
                            params.put("image_view", 1);
                        }
                        if (cbAllowPhone.isChecked()) {
                            params.put("phone_view", 1);
                        }


                        params.put("alias", SharedPreferenceManager.getUserObject(context).getAlias());
                        params.put("userpath", userpath);
                        params.put("path", SharedPreferenceManager.getUserObject(context).getPath());

                        //Log.e("showInterest params ", "" + params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showInterest(params);
                } else {
                    dialogShowInterest.this.getDialog().cancel();
                    MarryMax m = new MarryMax(getActivity());
                    m.subscribe();
                }


            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.faButtonShowInterestdismiss);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogShowInterest.this.getDialog().cancel();
            }
        });


        return rootView;
    }
/*    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_profilefor, null))


          *//*      // Add action buttons
                .setPositiveButton("Submut", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogDosDonts.this.getDialog().cancel();
                    }
                })*//*;


        return builder.create();

    }*/

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void showInterest(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        //Log.e("show interest params" + "  " + Urls.showInterest, "" + params);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.showInterest, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        //Log.e("re  update interest", response + "");

                        if (dialogShowInterest.this.getDialog() != null) {
                            dialogShowInterest.this.getDialog().dismiss();
                        }
                        try {
                            int responseid = response.getInt("id");


                            if (responseid >= 1) {
                                Toast.makeText(context, "Interest Showed", Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();

                                updateMember.updateInterest(true);
                                //  mCompleteListener.onComplete("done");
                            } else if (responseid == 0) {
                                Toast.makeText(context, " Interest has not been sent successfully", Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                                //  mCompleteListener.onComplete("done");
                            }


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }

                        pDialog.dismiss();
                        //   mCompleteListener.onComplete("");
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq);

    }


    public static interface onCompleteListener {
        public abstract void onComplete(String s);
    }


}


