package com.chicsol.marrymax.fragments.AccountSetting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.other.UserSessionManager;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.mButton2;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Android on 11/3/2016.
 */

public class ChangePasswordFragment extends Fragment {
    private AppCompatEditText etOldPassword, etNewPassword, etConfirmNewPassword;
    String password;
    private mButton2 btSavePassword;
    private ProgressBar pDialog;

    private String Tag = "DashMembersFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_as_change_password, container, false);
        initilize(rootView);
        setListeners();
        return rootView;
    }

    private void initilize(View view) {
        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarProjectMain);
        pDialog.setVisibility(View.GONE);
        etOldPassword = (AppCompatEditText) view.findViewById(R.id.EditTextOldPasswordCP);
        etOldPassword.setTransformationMethod(new PasswordTransformationMethod());

        etNewPassword = (AppCompatEditText) view.findViewById(R.id.EditTextNewPasswordCP);
        etNewPassword.setTransformationMethod(new PasswordTransformationMethod());

        etConfirmNewPassword = (AppCompatEditText) view.findViewById(R.id.EditTextConfirmNewPasswordCP);
        etConfirmNewPassword.setTransformationMethod(new PasswordTransformationMethod());

        btSavePassword = (mButton2) view.findViewById(R.id.mButtonSavePassword);
    }

    private void setListeners() {

        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


               /* if (!isEmailValid(etEmailView.getText().toString())) {
                    etEmailView.setError(getString(R.string.error_invalid_email));

                }*/


                if (!TextUtils.isEmpty(etNewPassword.getText().toString())) {
                    if (!isPasswordValid(etNewPassword.getText().toString())) {
                        etNewPassword.setError("Min 8 Characters");
                    }
                }


            }
        });


        etConfirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


               /* if (!isEmailValid(etEmailView.getText().toString())) {
                    etEmailView.setError(getString(R.string.error_invalid_email));

                }*/
                if (!TextUtils.isEmpty(etConfirmNewPassword.getText().toString())) {
                    if (!isPasswordValid(etConfirmNewPassword.getText().toString())) {
                        etConfirmNewPassword.setError("Min 8 Characters");
                    }
                }


            }
        });


        password = etConfirmNewPassword.getText().toString();
        btSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {


                    Log.e("Loggggg", etOldPassword.getText().toString());
                    Log.e("Loggggg", SharedPreferenceManager.getUserObject(getContext()).get_password());
                } catch (Exception e) {
                    UserSessionManager sessionManager = new UserSessionManager(getContext());
                    sessionManager.logoutUser();
                }
                View focusView = null;
                String oldpass = etOldPassword.getText().toString();
                String newpass = etNewPassword.getText().toString();
                String confirmnewpass = etConfirmNewPassword.getText().toString();

                if (TextUtils.isEmpty(oldpass)) {
                    etOldPassword.setError("Please Enter Old Password");
                    focusView = etOldPassword;

                    focusView.requestFocus();
                } else if (TextUtils.isEmpty(newpass)) {
                    etNewPassword.setError("Please Enter New Password");
                    focusView = etNewPassword;

                    focusView.requestFocus();
                } else if (TextUtils.isEmpty(confirmnewpass)) {
                    etConfirmNewPassword.setError("Please Enter Confirm New Password");
                    focusView = etConfirmNewPassword;

                    focusView.requestFocus();
                } else if (!isPasswordValidX(oldpass)) {
                    etOldPassword.setError(getString(R.string.error_invalid_password));
                    focusView = etOldPassword;

                    focusView.requestFocus();
                } else if (!isPasswordValid(newpass)) {
                    etNewPassword.setError("Min 8 Characters");
                    focusView = etNewPassword;

                    focusView.requestFocus();
                } else if (!isPasswordValid(confirmnewpass)) {
                    etConfirmNewPassword.setError("Min 8 Characters");
                    focusView = etConfirmNewPassword;

                    focusView.requestFocus();
                } else if (!oldpass.equals(SharedPreferenceManager.getUserObject(getContext()).get_password())) {
                    Toast.makeText(getContext(), "Old Password Incorrect", Toast.LENGTH_SHORT).show();
                } else if (!newpass.equals(confirmnewpass)) {
                    Toast.makeText(getContext(), "New Password Does Not Match", Toast.LENGTH_SHORT).show();
                } else {

                    Members members = new Members();
                    members.set_password(etOldPassword.getText().toString());
                    members.set_new_password(etNewPassword.getText().toString());
                    members.set_path(SharedPreferenceManager.getUserObject(getContext()).get_path());
                    Gson gson = new Gson();
                    String memString = gson.toJson(members);


                    try {
                        JSONObject params = new JSONObject(memString);

                        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
                            putRequest(params);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }


    private void putRequest(final JSONObject params)

    {

        pDialog.setVisibility(View.VISIBLE);
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());


        Log.e("Params", "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.updatePassword, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {
                            int responseid = response.getInt("id");
                            if (responseid == 1) {
                                Toast.makeText(getContext(), "Password Updated", Toast.LENGTH_SHORT).show();


                                Members member = SharedPreferenceManager.getUserObject(getContext());
                                member.set_password(password);

                                SharedPreferenceManager.setUserObject(getContext(), member);

                                etNewPassword.setText("");
                                etOldPassword.setText("");
                                etConfirmNewPassword.setText("");
                            } else {
                                Toast.makeText(getContext(), "Error Updating Password.Try Again", Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, Tag);

    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return (password.length() >= 8 && password.length() <= 15);
    }

    private boolean isPasswordValidX(String password) {
        //TODO: Replace this with your own logic
        return (password.length() >= 6 && password.length() <= 15);
    }

    @Override
    public void onStop() {
        super.onStop();
        MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);

    }


}
