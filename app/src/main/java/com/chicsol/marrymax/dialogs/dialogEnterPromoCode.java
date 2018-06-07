package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Subscription;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogEnterPromoCode extends DialogFragment {

    public onApplyPromoCodeListener mApplyPromoCodeListener;
    mTextView tvDesc, tvTitle;
    boolean withdrawCheck;

    String title, desc, params = null, btnTitle;
    Button mOkButton, btSubscribe;
    String promocode = "";
    EditText etPromoCode;

    public static dialogEnterPromoCode newInstance(String params, String title, String desc, String btnTitle, boolean withdrawCheck) {
 /*       Members member, String userpath, boolean replyCheck, Members member2*/
        dialogEnterPromoCode frag = new dialogEnterPromoCode();
        Bundle args = new Bundle();


        /*args.putString("name", title);
        args.putString("desc", desc);
        args.putString("params", params);
        args.putString("btnTitle", btnTitle);
        args.putBoolean("withdrawCheck", withdrawCheck);

        frag.setArguments(args);*/
        return frag;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {


            if (getTargetFragment() != null) {
                mApplyPromoCodeListener = (onApplyPromoCodeListener) getTargetFragment();
            } else {
                mApplyPromoCodeListener = (onApplyPromoCodeListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 /*       Bundle mArgs = getArguments();

        title = mArgs.getString("name");
        desc = mArgs.getString("desc");

        params = mArgs.getString("params");
        btnTitle = mArgs.getString("btnTitle");

        withdrawCheck = mArgs.getBoolean("withdrawCheck");*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_enter_promo_code, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        mOkButton = (Button) rootView.findViewById(R.id.mButtonDialogRequest);
        btSubscribe = (Button) rootView.findViewById(R.id.mButtonDialogRequestSubscribe);
        etPromoCode = (EditText) rootView.findViewById(R.id.EditTextPromoCodeEnterPromoCode);


      /*  tvTitle = (mTextView) rootView.findViewById(R.id.TextViewRequestDialogTitle);

        tvDesc = (mTextView) rootView.findViewById(R.id.TextViewRequestDialogDetails);

*/


      /*  if (Build.VERSION.SDK_INT >= 24) {
            // for 24 api and more
            tvDesc.setText(Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvDesc.setText(Html.fromHtml(desc));
        }
        btSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarryMax marryMax = new MarryMax(getActivity());
                marryMax.subscribe();
            }
        });*/

        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                if (!TextUtils.isEmpty( etPromoCode.getText().toString().trim())) {

                    verifyPromoCode(etPromoCode.getText().toString());
                    promocode = etPromoCode.getText().toString();
                } else {

                    Toast.makeText(getContext(), "Enter Promocode First", Toast.LENGTH_SHORT).show();
                }


            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.faButtonRequestdismiss);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogEnterPromoCode.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public static interface onApplyPromoCodeListener {
        public abstract void onApplyPromoCode(String s);
    }

    private void verifyPromoCode(final String promocode) {


        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        //  Log.e("api path", "" + Urls.getPromoCode + promocode);

        JsonArrayRequest req = new JsonArrayRequest(Urls.getPromoCode + promocode,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Log.d("Response", response.toString());
                        try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<Subscription>>() {
                            }.getType();

                            List<Subscription> sDataList = (List<Subscription>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                            Subscription subscription = sDataList.get(0);

                            if (subscription.getItem_id() == 0) {

                                Toast.makeText(getContext(), subscription.getProcode_name(), Toast.LENGTH_SHORT).show();
                            } else {
                                mApplyPromoCodeListener.onApplyPromoCode(promocode);
                                dialogEnterPromoCode.this.getDialog().cancel();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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


