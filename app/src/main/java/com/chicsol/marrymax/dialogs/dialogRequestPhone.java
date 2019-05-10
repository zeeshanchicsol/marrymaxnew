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
import com.chicsol.marrymax.interfaces.PhoneRequestCallBackInterface;
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

public class dialogRequestPhone extends DialogFragment {
    // private ListView lv_mycontacts;
    public onCompleteListener mCompleteListener;
    String title, desc, params = null;
    mCheckBox cbAllowPhone, cbAllowPics;
    mTextView tvDesc, tvTitle;
    String userpath, alias;
    boolean replyCheck;
    Button mOkButton;
    Context context;
    private PhoneRequestCallBackInterface listener;

    public void setListener(PhoneRequestCallBackInterface listener) {
        this.listener = listener;
    }

    public static dialogRequestPhone newInstance(String params, String title, String desc) {
        /*       Members member, String userpath, boolean replyCheck, Members member2*/
        dialogRequestPhone frag = new dialogRequestPhone();
        Bundle args = new Bundle();


        args.putString("name", title);
        args.putString("alias", desc);
        args.putString("params", params);
        /*args.putString("param", String.valueOf(member2.getPhone_view()));
        args.putString("my_id", String.valueOf(member2.getMy_id()));
        args.putString("checkedTextView", member.getAlias());
        args.putString("userpath", userpath);
*/

        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        context = activity;
        try {
            if (getTargetFragment() != null) {
                mCompleteListener = (dialogRequestPhone.onCompleteListener) getTargetFragment();
            } else {
                mCompleteListener = (dialogRequestPhone.onCompleteListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }
/*    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            if (getTargetFragment() != null) {
                mCompleteListener = (onCompleteListener) getTargetFragment();
            } else {
                mCompleteListener = (onCompleteListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        //    replyCheck = mArgs.getBoolean("replyCheck");
        title = mArgs.getString("name");
        alias = mArgs.getString("alias");
        params = mArgs.getString("params");
      /*  my_id = mArgs.getString("my_id");
        userpath = mArgs.getString("userpath");
        checkedTextView = mArgs.getString("checkedTextView");*/

     /*   Log.e("name Fragment", name);
        Log.e("desc Fragment", desc);
        Log.e("param Fragment", param);
        Log.e("my_id Fragment", my_id);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_request_phone, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tvTitle = (mTextView) rootView.findViewById(R.id.TextViewDialogRequestPhoneTitle);

        tvDesc = (mTextView) rootView.findViewById(R.id.TextViewDialogRequestPhoneDetails);
        mOkButton = (Button) rootView.findViewById(R.id.ButtonDialogRequestPhone);


        tvTitle.setText(title);


        if (Build.VERSION.SDK_INT >= 24) {
            // for 24 api and more
            tvDesc.setText(Html.fromHtml(alias, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvDesc.setText(Html.fromHtml(alias));
        }

        if (params == null) {

            mOkButton.setVisibility(View.INVISIBLE);
        }

        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                try {
                    requestPermission(new JSONObject(params));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.faButtonShowInterestdismiss);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogRequestPhone.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }


    private void requestPermission(JSONObject params) {


        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        //Log.e("params", params.toString());
        //Log.e("profile path", Urls.submitRequest);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.submitRequest, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Res  request phone ", response + "");

                        try {
                            int responseid = response.getInt("id");


                            if (responseid >= 1) {
                                Toast.makeText(getContext(), "Phone View Requested", Toast.LENGTH_SHORT).show();
                                dialogRequestPhone.this.getDialog().cancel();
                                //  mCompleteListener.onComplete("1");
                                listener.onPhoneViewRequestComplete(responseid + "");

                                pDialog.dismiss();


                            } else if (responseid == -1) {
                                mOkButton.setVisibility(View.GONE);
                                String desctxt = "";
                                if (SharedPreferenceManager.getUserObject(context).getMember_status() == 4) {
                                    desctxt = "\u25CF You have reached the contact limit.\n" +
                                            "\u25CF Please wait 24 hours to send new request.\n" +
                                            "";
                                } else {
                                    desctxt = "\u25CF  Daily sent limit is reached.\n" +
                                            "\u25CF Please wait 24 hours before you can contact new members.\n" +
                                            "";
                                }


                                    tvDesc.setText(desctxt);



                            } else {
                                //  listener.complete();
                                //   mCompleteListener.onComplete("1");
                                dialogRequestPhone.this.getDialog().cancel();
                            }


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            dialogRequestPhone.this.getDialog().cancel();
                            e.printStackTrace();
                        }


                        pDialog.dismiss();
                        //  dialogRequestPhone.this.getDialog().cancel();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                pDialog.dismiss();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq);
    }


    public static interface onCompleteListener {
        public abstract void onComplete(String s);
    }


}


