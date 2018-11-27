package com.chicsol.marrymax.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.registration.RegistrationActivity;
import com.chicsol.marrymax.adapters.MySpinnerAdapter;
import com.chicsol.marrymax.adapters.MySpinnerAdapterContactUS;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.modal.WebCSC;
import com.chicsol.marrymax.modal.mCountryCode;
import com.chicsol.marrymax.other.ConnectionDetector;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.PrefixEditText;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactAcivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private android.support.v7.widget.Toolbar toolbar;

    Spinner sp_contactFor, sp_country;
    private MySpinnerAdapterContactUS adapter_contactfor, adapter_country;

    private List<mCountryCode> contactForDataList, countryDataList;
    private EditText etName, etEmail, etQuestion;

    private AppCompatButton ButtonContactSubmit;


    private PrefixEditText EditTextAScontactMobileNumber;

    private ConnectionDetector connectionDetector;
    private LinearLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_acivity);
        init();

    }

    private void init() {

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Contact Us");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pDialog = new ProgressDialog(ContactAcivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);


        contactForDataList = new ArrayList<>();

        countryDataList = new ArrayList<>();


        sp_contactFor = (Spinner) findViewById(R.id.sp_contactfor);
        adapter_contactfor = new MySpinnerAdapterContactUS(this,
                R.layout.spinner_white, contactForDataList);
        adapter_contactfor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_contactFor.setAdapter(adapter_contactfor);


        sp_country = (Spinner) findViewById(R.id.sp_contactCountry);
        adapter_country = new MySpinnerAdapterContactUS(this,
                R.layout.spinner_white, countryDataList);
        adapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_country.setAdapter(adapter_country);

        etName = (EditText) findViewById(R.id.EditTextContactName);
        etEmail = (EditText) findViewById(R.id.EditTextContactEmail);
        etQuestion = (EditText) findViewById(R.id.EditTextContactQuestion);
        etQuestion.setHorizontallyScrolling(false);
        etQuestion.setMaxLines(Integer.MAX_VALUE);

        ButtonContactSubmit = findViewById(R.id.ButtonContactSubmit);
        EditTextAScontactMobileNumber = findViewById(R.id.EditTextcontactMobileNumber);

        EditTextAScontactMobileNumber.setText("");
        connectionDetector = new ConnectionDetector(getApplicationContext());

        coordinatorLayout = (LinearLayout) findViewById(R.id
                .coossrdinatorLayoutReg);


        getContactsData();
        setListeners();
    }

    private void setListeners() {


        sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCountryCode ard = (mCountryCode) view.getTag();
                EditTextAScontactMobileNumber.setTag("+" + ard.getCode() + " ");
                EditTextAScontactMobileNumber.setText(EditTextAScontactMobileNumber.getText());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ButtonContactSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean cancel = false;

                    View focusView = null;

                    String email = etEmail.getText().toString();
                    String question = etQuestion.getText().toString();
                    String name = etName.getText().toString();
                    String mobNum = EditTextAScontactMobileNumber.getText().toString();
                    String countryCode = EditTextAScontactMobileNumber.getTag().toString();

            //   Log.e(""+     Integer.parseInt(countryCode.trim()), Integer.parseInt(mobNum.trim().substring(0, countryCode.length() ))+"" );

                    if (!TextUtils.isEmpty(mobNum.trim()) && !countryCodeCheck(Integer.parseInt(countryCode.trim()), Integer.parseInt(mobNum.substring(0, countryCode.trim().length() - 1)))) {
                        mobNum = mobNum.substring(countryCode.trim().length() - 1, mobNum.length());

                    }


                    if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
                        if (sp_contactFor.getSelectedItemId() == 0) {


                            TextView errorText = (TextView) sp_contactFor.getSelectedView();
                            errorText.setError("");
                            errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
                            errorText.setText("Please select contact for");

                        } else if (sp_country.getSelectedItemId() == 0) {
                            TextView errorText = (TextView) sp_country.getSelectedView();
                            errorText.setError("");
                            errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
                            errorText.setText("Please select country");

                        } else if (TextUtils.isEmpty(name.trim())) {
                            etName.setError("Please Enter Name");
                            focusView = etName;
                            focusView.requestFocus();
                        } else if (!isNameValid(name)) {
                            etName.setError("Invalid name format");
                            focusView = etName;
                            focusView.requestFocus();
                        }
                        // Check for a valid email address.
                        else if (TextUtils.isEmpty(email)) {
                            etEmail.setError("Please Enter Email");
                            focusView = etEmail;
                            focusView.requestFocus();
                        } else if (!isEmailValid(email)) {
                            etEmail.setError(getString(R.string.error_invalid_email));
                            focusView = etEmail;

                            focusView.requestFocus();
                        } else if (TextUtils.isEmpty(question)) {
                            etQuestion.setError("Please Enter Your Query");
                            focusView = etQuestion;
                            focusView.requestFocus();
                        } else if (TextUtils.isEmpty(mobNum.trim())) {
                            EditTextAScontactMobileNumber.setError("Please Enter Mobile Number");
                            focusView = EditTextAScontactMobileNumber;

                            focusView.requestFocus();
                        } /*else if (!countryCodeCheck(Integer.parseInt(countryCode.trim()), Integer.parseInt(mobNum.trim().substring(0, countryCode.length() - 2)))) {
                            EditTextAScontactMobileNumber.setError("Invalid Mobile Number");
                            focusView = EditTextAScontactMobileNumber;
                            focusView.requestFocus();

                        }*/ else if (!isPhone(mobNum)) {
                            EditTextAScontactMobileNumber.setError("Invalid  phone format");
                            focusView = EditTextAScontactMobileNumber;
                            focusView.requestFocus();
                        } else if (mobNum.length() > 16) {
                            EditTextAScontactMobileNumber.setError("Max 16 Chars allowed");
                            focusView = EditTextAScontactMobileNumber;
                            focusView.requestFocus();
                        } else {
                            // Intent in = new Intent(RegistrationActivity.this, RegisterGeographicActivity.class);
                            //   startActivity(in);

                            mCountryCode countryObj = (mCountryCode) sp_country.getSelectedItem();
                            mCountryCode cforObj = (mCountryCode) sp_contactFor.getSelectedItem();
                            //  String other_info = datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();

                            if (connectionDetector.isConnectingToInternet()) {

                                /*  Toast.makeText(RegistrationActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                                 */
                                JSONObject params = new JSONObject();
                                try {

                                    params.put("contact_phone", mobNum);
                                    params.put("contact_ip", getLocalIpAddress());
                                    params.put("emailaddress", email);
                                    params.put("contact_category_id", cforObj.getId());
                                    params.put("contact_name", name);

                                    params.put("contact_message", question);
                                    params.put("contact_country_id", countryObj.getId());

                                    if (SharedPreferenceManager.getUserObject(getApplicationContext()) != null) {
                                        params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                               contactUs(params);


                            } else {

                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                                        .setAction("RETRY", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                            }
                                        });

                                // Changing message text color
                                snackbar.setActionTextColor(Color.RED);

                                // Changing action button text color
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);

                                snackbar.show();

                            }

                        }


                    }
                } catch (Exception e)

                {
                    Log.e("e", e.toString());
                    Crashlytics.logException(e);
                    Toast.makeText(ContactAcivity.this, "There is system error Please try again", Toast.LENGTH_SHORT).show();
                }


            }
        });

     /*   try {


            if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
                //  RegisterUser();
                // Intent in = new Intent(RegistrationActivity.this, RegisterGeographicActivity.class);
                // startActivity(in);

                // Reset errors.
                //etEmailView.setError(null);
                //  etPasswordView.setError(null);

                // Store values at the time of the login attempt.
                String email = etEmailView.getText().toString();
                String password = etPasswordView.getText().toString();
                String name = etName.getText().toString();
                String profilename = etProfileName.getText().toString();
                String other_info = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();


                boolean cancel = false;

                View focusView = null;


                if (spinner_profilefor.getSelectedItemId() == 0) {


                    TextView errorText = (TextView) spinner_profilefor.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
                    errorText.setText("Please select profile for");

                } else if (spinner_religion.getSelectedItemId() == 0) {

                    TextView errorText = (TextView) spinner_religion.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
                    errorText.setText("Please select religion");

                } else if (spinner_source.getSelectedItemId() == 0) {
                    scrollMain.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollMain.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                    TextView errorText = (TextView) spinner_source.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
                    errorText.setText("Please select source");


                } else if (TextUtils.isEmpty(name.trim())) {
                    etName.setError("Please Enter Name");
                    focusView = etName;
                    focusView.requestFocus();
                } else if (!isNameValid(name)) {
                    etName.setError("Invalid name format");
                    focusView = etName;
                    focusView.requestFocus();
                } else if (TextUtils.isEmpty(profilename.trim())) {
                    etProfileName.setError("Please Enter Profile Name");
                    focusView = etProfileName;

                    focusView.requestFocus();
                } else if (!isProfileNameValid(profilename)) {
                    etProfileName.setError("Invalid profile name format");
                    focusView = etProfileName;
                    focusView.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    etPasswordView.setError("Please Enter Password");
                    focusView = etPasswordView;
                    focusView.requestFocus();
                } else if (!isPasswordValid(password)) {
                    etPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = etPasswordView;
                    focusView.requestFocus();
                }
                // Check for a valid email address.
                else if (TextUtils.isEmpty(email)) {
                    etEmailView.setError("Please Enter Email");
                    focusView = etEmailView;
                    focusView.requestFocus();
                } else if (!isEmailValid(email)) {
                    etEmailView.setError(getString(R.string.error_invalid_email));
                    focusView = etEmailView;

                    focusView.requestFocus();
                } else if (!cbTerms.isChecked()) {
                    scrollMain.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollMain.fullScroll(View.FOCUS_DOWN);
                        }
                    });

                    cbTerms.setError("Please Accept Terms and Condition");
                    focusView = cbTerms;
                    focusView.requestFocus();

                    //    Toast.makeText(RegistrationActivity.this, "Not Selected", Toast.LENGTH_SHORT).show();

                } else if (gender == null) {


                    Toast.makeText(RegistrationActivity.this, "Please select gender !", Toast.LENGTH_SHORT).show();
                }


                else {


                    WebArd profileForDataObj = (WebArd) spinner_profilefor.getSelectedItem();
                    WebArd sourceDataObj = (WebArd) spinner_source.getSelectedItem();
                    WebArd religionDataObj = (WebArd) spinner_religion.getSelectedItem();
                    //  String other_info = datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();

                    if (connectionDetector.isConnectingToInternet()) {

                        *//*  Toast.makeText(RegistrationActivity.this, "Correct", Toast.LENGTH_SHORT).show();
         *//*
                        JSONObject params = new JSONObject();
                        try {

                            params.put("profile_owner_id", profileForDataObj.getId());
                            params.put("personal_name", name);
                            params.put("email", email);
                            params.put("password", password);
                            params.put("alias", profilename);
                            params.put("religious_sect_id", religionDataObj.getId());
                            params.put("gender", gender);
                            params.put("other_info", other_info);
                            params.put("member_source", sourceDataObj.getId());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("params", "" + params);
                        RegisterUser(email, password, params);

                    } else {

                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });

                        // Changing message text color
                        snackbar.setActionTextColor(Color.RED);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);

                        snackbar.show();

                    }

                }

            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(RegistrationActivity.this, "There is system error Please try again", Toast.LENGTH_SHORT).show();

        }*/

    }


    private boolean countryCodeCheck(int countryCode, int mobNum) {

        Log.e("countryCodeCheck", countryCode + "   " + mobNum);

        if (countryCode == mobNum) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void getContactsData() {


        final ProgressDialog pDialog = new ProgressDialog(ContactAcivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        Log.e("api path", "" + Urls.getContactList);

        JsonArrayRequest req = new JsonArrayRequest(Urls.getContactList,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());
                        try {


                            JSONArray jsonCountryObj = response.getJSONArray(0);
                            JSONArray jsoncontactForObj = response.getJSONArray(1);

                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mCountryCode>>() {
                            }.getType();

                            contactForDataList = (List<mCountryCode>) gsonc.fromJson(jsoncontactForObj.toString(), listType);
                            countryDataList = (List<mCountryCode>) gsonc.fromJson(jsonCountryObj.toString(), listType);


                            Log.e("size", contactForDataList.size() + "");
                            Log.e("size", countryDataList.size() + "");
                            contactForDataList.add(0, new mCountryCode("-1", "Select Contact For", "0"));
                            adapter_contactfor.updateDataList(contactForDataList);


                            countryDataList.add(0, new mCountryCode("-1", "Select Country", "0"));
                            adapter_country.updateDataList(countryDataList);

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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }


    private void contactUs(JSONObject params) {

        Log.e("params", "" + params);


        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        Log.e("params url", Urls.contactUs + "  ==  " + params);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.contactUs, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("res", response + "");
                        pDialog.dismiss();

                        try {
                            int responseid = response.getInt("id");

                            if (responseid == 1) {
                                Toast.makeText(ContactAcivity.this, "Contact info has been sent successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }

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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);

    }

    private boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }

    private boolean isNameValid(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^([a-zA-Z ]){3,30}$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }

    private boolean isPhone(String phone) {
        Pattern pattern;
        Matcher matcher;
        String Phone_PATTERN = "^(?:(?:\\(?(?:00|\\+)([1-4]\\d\\d|[1-9]\\d?)\\)?)?[\\-\\.\\ \\\\\\/]?)?((?:\\(?\\d{1,}\\)?[\\-\\.\\ \\\\\\/]?){0,})(?:[\\-\\.\\ \\\\\\/]?(?:#|ext\\.?|extension|x)[\\-\\.\\ \\\\\\/]?(\\d+))?$";
        pattern = Pattern.compile(Phone_PATTERN);
        matcher = pattern.matcher(phone);
        return matcher.matches();

    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }

}
