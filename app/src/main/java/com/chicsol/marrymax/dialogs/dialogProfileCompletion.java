package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;

import java.util.Map;

public class dialogProfileCompletion extends DialogFragment {
    String title;

    AppCompatTextView tvDesc, tvTitle;
    String desc, btnText;
    int subscribe = 9;
    int step;
    // private ListView lv_mycontacts;
    private onCompleteListener mCompleteListener;

    public static dialogProfileCompletion newInstance(String title, String desc, String btnText, int step) {

        dialogProfileCompletion frag = new dialogProfileCompletion();
        Bundle args = new Bundle();

        args.putInt("step", step);
        args.putString("name", title);
        args.putString("desc", desc);
        args.putString("btnText", btnText);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {

            // Log.e("getfragment manager", "======" + getTargetFragment());
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

        step = mArgs.getInt("step");
        title = mArgs.getString("name");
        desc = mArgs.getString("desc");
        btnText = mArgs.getString("btnText");
        //Log.e("valll", name + "===" + desc + "==" + btnText);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_profile_completion, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tvDesc = (AppCompatTextView) rootView.findViewById(R.id.TextVewDialogPCDescription);
        tvTitle = (AppCompatTextView) rootView.findViewById(R.id.TextVewDialogPCTitle);
        if (step == subscribe) {

            tvDesc.setText(Html.fromHtml(desc));
        } else if (step == 8 || step == 22) {

            tvDesc.setText(Html.fromHtml(desc));
        } else {
            tvDesc.setText(desc);
        }
        tvTitle.setText(title);

        AppCompatButton mOkButton = (AppCompatButton) rootView.findViewById(R.id.ButtonDialogPCButton);


        mOkButton.setText(btnText);


        if (step == 4) {

            mOkButton.setVisibility(View.GONE);

        }

        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
//email verification request
                if (step == 2) {
                    dialogProfileCompletion.this.getDialog().cancel();
                    //   emailVerificationRequest();
                    //   Toast.makeText(getContext(), "email", Toast.LENGTH_SHORT).show();

               /*     Intent intent = new Intent(activity, SearchMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("searchcheck", true);
                    context.startActivity(intent);*/
                    dialogProfileCompletion.this.getDialog().cancel();
                    Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                    in.putExtra("type", 22);
                    startActivity(in);

                } else if (step == 22) {
                    emailVerificationRequest();
                    //   Toast.makeText(getContext(), "email", Toast.LENGTH_SHORT).show();
                } else if (step == 1) {
                    dialogProfileCompletion.this.getDialog().cancel();
                    //  Intent intent=new Intent(getContext(),Reg)
                /*    MarryMax marryMax = new MarryMax(null);
                    marryMax.getProfileProgress(getContext(), SharedPreferenceManager.getUserObject(getContext()), getActivity());*/
                    Intent in = new Intent(getActivity(), MainDirectiveActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("type", 22);
                    getActivity().startActivity(in);


                } else if (step == 3) {

                    mCompleteListener.onComplete(3);
                    dialogProfileCompletion.this.getDialog().cancel();

                } else if (step == 8) {
                    dialogProfileCompletion.this.getDialog().cancel();
                  /*  MarryMax marryMax = new MarryMax(null);
                    marryMax.getProfileProgress(getContext(), SharedPreferenceManager.getUserObject(getContext()), getActivity());*/
                    Intent in = new Intent(getActivity(), MainDirectiveActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("type", 22);
                    getActivity().startActivity(in);

                } else if (step == subscribe) {
                    MarryMax max = new MarryMax(getActivity());
                    max.subscribe();
                    dialogProfileCompletion.this.getDialog().cancel();
                }

            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.ButtonDialogPCDismiss);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                dialogProfileCompletion.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public static interface onCompleteListener {
        public abstract void onComplete(int s);
    }


    private void emailVerificationRequest() {


        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        Log.e("api path", "" + Urls.getEmailCode + SharedPreferenceManager.getUserObject(getContext()).get_path());

        StringRequest req = new StringRequest(Urls.getEmailCode + SharedPreferenceManager.getUserObject(getContext()).get_path(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Resp email  onse", response.toString());
                        //   try {
                        int responseid = Integer.parseInt(response);

                        //  responseid = response.getJSONArray(0).getInt("id");

                        if (responseid == 1) {

                            Toast.makeText(getContext(), "Your email verification code has been send successfully. Please check your inbox/spam\n", Toast.LENGTH_LONG).show();
                            dialogProfileCompletion.this.getDialog().cancel();
                        } else {

                            Toast.makeText(getContext(), "Failed, Try again", Toast.LENGTH_LONG).show();
                            dialogProfileCompletion.this.getDialog().cancel();
                        }
                      /*  } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
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
        MySingleton.getInstance(getContext()).addToRequestQueue(req);
    }

}


