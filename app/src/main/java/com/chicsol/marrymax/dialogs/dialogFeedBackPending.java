package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogFeedBackPending extends DialogFragment {
    private String text = null;
    private TextView tvText;
    private AppCompatButton btGiveFeedback, btClose;
    ///  public onCompleteListener mCompleteListener;
    private boolean disabled = false;
    private Context context;
    faTextView cancelButton;


    public static dialogFeedBackPending newInstance(String text, boolean disabled) {

        dialogFeedBackPending frag = new dialogFeedBackPending();
        Bundle args = new Bundle();

        args.putString("text", text);
        args.putBoolean("disabled", disabled);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        text = mArgs.getString("text");
        disabled = mArgs.getBoolean("disabled");


        // Log.e("text", text);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
       /* try {


            if (getTargetFragment() != null) {
                mCompleteListener = (onCompleteListener) getTargetFragment();
            } else {
                mCompleteListener = (onCompleteListener) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_feedback_pending, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        cancelButton = (faTextView) rootView.findViewById(R.id.dismissBtnVerifyPhone);

        tvText = (TextView) rootView.findViewById(R.id.TextViewDialogFeedBackPendingText);

        tvText.setText(Html.fromHtml(text));

        btGiveFeedback = (AppCompatButton) rootView.findViewById(R.id.ButtonDialogFeedBackPendingGiveFeedBack);
        btClose = (AppCompatButton) rootView.findViewById(R.id.ButtonDialogFeedBackPendingClose);
        btGiveFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 25);
                // in.putExtra("subtype", "received");
                startActivity(in);

            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFeedBackPending.this.getDialog().cancel();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                dialogFeedBackPending.this.getDialog().cancel();
            }
        });

        Log.e("disabled", disabled + "");

      /*  if (disabled) {
            //    getDialog().setCancelable(false);
            setCancelable(false);
            btClose.setClickable(false);
            cancelButton.setClickable(false);
        }*/


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


  /*  public static interface onCompleteListener {
        public abstract void onComplete(String s);
    }*/


}
