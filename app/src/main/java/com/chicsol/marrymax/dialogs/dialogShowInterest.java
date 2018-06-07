package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.interfaces.UpdateMemberFromDialogFragment;
import com.chicsol.marrymax.modal.Members;
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
    mTextView tvDesc;
    String userpath, alias;
    boolean replyCheck, subscribe = false;

    private UpdateMemberFromDialogFragment updateMember;


    public void setListener(UpdateMemberFromDialogFragment listener) {
        updateMember = listener;
    }


    public static dialogShowInterest newInstance(Members member, String userpath, boolean replyCheck, Members member2) {

        dialogShowInterest frag = new dialogShowInterest();
        Bundle args = new Bundle();


        args.putBoolean("replyCheck", replyCheck);
        args.putString("name", String.valueOf(member2.get_interested_id()));
        args.putString("desc", String.valueOf(member2.get_image_view()));
        args.putString("param", String.valueOf(member2.get_phone_view()));
        args.putString("my_id", String.valueOf(member2.get_my_id()));
        args.putString("alias", member.getAlias());
        args.putString("userpath", userpath);


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


            Log.e("getfragment manager", "======" + getTargetFragment());
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

      /*  Log.e("name Fragment", interested_id);
        Log.e("desc Fragment", image_view);
        Log.e("param Fragment", phone_view);
        Log.e("my_id Fragment", my_id);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_interest, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        cbAllowPhone = (mCheckBox) rootView.findViewById(R.id.CheckBoxInterestAllowPhone);
        cbAllowPics = (mCheckBox) rootView.findViewById(R.id.CheckBoxInterestAllowPics);
        tvDesc = (mTextView) rootView.findViewById(R.id.TextViewDialogInterestDetails);
        Button mOkButton = (Button) rootView.findViewById(R.id.mButtonExpressWithDrawInterest);
        Log.e("MyId", "" + my_id);


        if (my_id.equals("0")) {
            String txt = "<font color='#9a0606'>" + alias + "</font>";
            tvDesc.setText(Html.fromHtml("At this time, " + "<b>" + txt.toUpperCase() + "</b> is accepting interest only from preferred matches. "));
            mOkButton.setVisibility(View.INVISIBLE);
        } else if (my_id.equals("-1")) {

            String desctxt = "";

            if (SharedPreferenceManager.getUserObject(getContext()).get_member_status() == 3) {

                desctxt = "<ul><li>Your complimentary free member communication quota is exhausted.</li>\n" +
                        "<br><li>You need to wait 72 hours before you can send new request.</li>\n" +
                        "<br><li>To maximize your options and communicate immediately, please subscribe.</li>\n" +
                        "</ul>";

                mOkButton.setText("Subscribe");
                subscribe = true;

            } else if (SharedPreferenceManager.getUserObject(getContext()).get_member_status() == 4) {


                desctxt = "<ul><li>Your complimentary paid member communication quota is exhausted.</li>\n" +
                        "<br><li>You need to wait 24 hours before you can send new request.</li>\n" +
                        "</ul>";

                mOkButton.setVisibility(View.GONE);
            }


            if (Build.VERSION.SDK_INT >= 24) {
                // for 24 api and more
                tvDesc.setText(Html.fromHtml(desctxt, Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvDesc.setText(Html.fromHtml(desctxt));
            }


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
                    } else {
                        cbAllowPics.setVisibility(View.VISIBLE);
                        cbAllowPics.setText("Would you like to give permission to view your image?");
                    }
                    mOkButton.setVisibility(View.VISIBLE);

                }
                if (phone_view.equals("5")) {
                    if (!replyCheck) {
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


                        params.put("alias", SharedPreferenceManager.getUserObject(getContext()).getAlias());
                        params.put("userpath", userpath);
                        params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());

                        Log.e("showInterest params ", "" + params);
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


        Log.e("show interest params" + "  " + Urls.showInterest, "" + params);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.showInterest, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("re  update interest", response + "");
                        dialogShowInterest.this.getDialog().cancel();
                        try {
                            int responseid = response.getInt("id");


                            if (responseid >= 1) {
                                Toast.makeText(getContext(), "Interest Showed", Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();

                                updateMember.updateInterest(true);
                                //  mCompleteListener.onComplete("done");
                            } else if (responseid == 0) {
                                Toast.makeText(getContext(), " Interest has not been sent successfully", Toast.LENGTH_SHORT).show();
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


