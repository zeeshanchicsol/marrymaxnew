package com.chicsol.marrymax.activities.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.MySpinnerAdapter;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.other.ConnectionDetector;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.other.UserSessionManager;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
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
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Redz on 10/21/2016.
 */

public class RegistrationActivity extends AppCompatActivity {

    String gender = null;
    //0 for male and 1 for female
    private NestedScrollView scrollMain;
    private TextView tv_Title, tv_fa_male, tv_fa_female, tv_male, tv_female;
    private Spinner spinner_source, spinner_religion, spinner_profilefor;
    private LinearLayout ll_maleNormal, ll_maleSelected, ll_femaleNormal, ll_femaleSelected;
    private EditText etPasswordView, etEmailView, etProfileName, etName;
    private DatePicker datePicker;
    private MySpinnerAdapter adapter_source, adapter_religion, adapter_profilefor;
    private List<WebArd> ProfileForDataList, ReligionDataList, SourceDataList;
    private Button bt_register_free;
    private CheckBox cbTerms;
    private ConnectionDetector connectionDetector;
    private CoordinatorLayout coordinatorLayout;

    private boolean updateData = false;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_registration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initialize();
        setListenders();


        getRegistrationData();

        //LoginUser("pakhtar99@gmail.com", "12345678");
        //  RegisterUser();
        updateData = getIntent().getBooleanExtra("updateData", false);

        getCurrentIP();
        Log.e("second ip is:", getLocalIpAddress());


    }

    public void getCurrentIP() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://ipecho.net/plain";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //      mTextView.setText("Response is: "+ response.substring(0,500));
                        Log.e("ip is", response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);


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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initialize() {
        pDialog = new ProgressDialog(RegistrationActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        scrollMain = (NestedScrollView) findViewById(R.id.ScrollViewRegActivity);
        bt_register_free = (Button) findViewById(R.id.buttonRegisterFree);
        /*        Typeface tf = Typeface.createFromAsset(getAssets(), Constants.font_centurygothic);*/
        //   tv_Profilefor = (TextView) findViewById(R.id.TextViewProfilefor);
        tv_Title = (TextView) findViewById(R.id.TextViewTitle);


        /*  Typeface tf_fa = Typeface.createFromAsset(getAssets(), Constants.font_fa_awesome);*/
        tv_fa_female = (TextView) findViewById(R.id.tv_fa_female);
        tv_fa_male = (TextView) findViewById(R.id.tv_fa_male);
        tv_male = (TextView) findViewById(R.id.TextViewMale);
        tv_female = (TextView) findViewById(R.id.TextViewFemale);
        datePicker = (DatePicker) findViewById(R.id.datePicker);

        Calendar cal = Calendar.getInstance();
        /*Date today = cal.getTime();*/
        int maxyear = cal.get(Calendar.YEAR) - 18;
        int minyear = cal.get(Calendar.YEAR) - 70;
        cal.set(Calendar.YEAR, maxyear);
        datePicker.setMaxDate(cal.getTimeInMillis());
        cal.set(Calendar.YEAR, minyear);
        datePicker.setMinDate(cal.getTimeInMillis());
/*
        tv_fa_male.setTypeface(tf_fa);
        tv_fa_female.setTypeface(tf_fa);
*/

        ll_maleNormal = (LinearLayout) findViewById(R.id.LinearLayoutMaleNormal);
        ll_maleSelected = (LinearLayout) findViewById(R.id.LinearLayoutMaleSelected);

        ll_femaleNormal = (LinearLayout) findViewById(R.id.LinearLayoutFemaleNormal);
        ll_femaleSelected = (LinearLayout) findViewById(R.id.LinearLayoutFemaleSelected);

        etEmailView = (EditText) findViewById(R.id.EditTextEmail);
        etPasswordView = (EditText) findViewById(R.id.EditTextPassword);
        etPasswordView.setTransformationMethod(new PasswordTransformationMethod());

        etName = (EditText) findViewById(R.id.EditTextName);
        etProfileName = (EditText) findViewById(R.id.EditTextProfileName);

        // tv_datepicker = (TextView) findViewById(R.id.mTextViewDatePicker);

        // spinner_religion = (Spinner) findViewById(R.id.sp_religion);

        ProfileForDataList = new ArrayList<>();
        ReligionDataList = new ArrayList<>();
        SourceDataList = new ArrayList<>();

//================================================

        spinner_religion = (Spinner) findViewById(R.id.sp_religion);
        adapter_religion = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, ReligionDataList);
        adapter_religion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_religion.setAdapter(adapter_religion);

        //====================Second Spinner

        spinner_source = (Spinner) findViewById(R.id.sp_source);
        adapter_source = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, SourceDataList);
        adapter_source.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_source.setAdapter(adapter_source);

/*        spinner_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(RegistrationActivity.this, "selected" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        spinner_profilefor = (Spinner) findViewById(R.id.sp_profilefor);
        spinner_profilefor.setPrompt("Select Profile For");
        adapter_profilefor = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, ProfileForDataList);
        adapter_profilefor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_profilefor.setAdapter(adapter_profilefor);


        cbTerms = (CheckBox) findViewById(R.id.checkBoxTerms);

        connectionDetector = new ConnectionDetector(getApplicationContext());

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayoutReg);


    }

    private void setListenders() {

        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    etEmailView.setFocusable(true);
                }
                return false;
            }
        });
        etEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    etPasswordView.setFocusable(true);
                }
                return false;
            }
        });
        etPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    etProfileName.setFocusable(true);
                }
                return false;
            }
        });


        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isNameValid(etName.getText().toString())) {
                    etName.setError("Invalid name format");
                }


            }
        });


        etProfileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!isProfileNameValid(etProfileName.getText().toString())) {
                    etProfileName.setError("Invalid profile name format");

                }


            }
        });


        etEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                if (!isEmailValid(etEmailView.getText().toString())) {
                    etEmailView.setError(getString(R.string.error_invalid_email));

                }


            }
        });


        etPasswordView.addTextChangedListener(new TextWatcher() {
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

                if (!isPasswordValid(etPasswordView.getText().toString())) {
                    etPasswordView.setError("Min 8 characters");
                }


            }
        });
        cbTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbTerms.setError(null);
                } else {
                    cbTerms.setError("Please Accept Terms and Condition");
                }
            }
        });


        spinner_profilefor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(RegistrationActivity.this, "selected" + ProfileForDataList.get(position).getId(), Toast.LENGTH_SHORT).show();


                if (ProfileForDataList.get(position).getId().equals("2") || ProfileForDataList.get(position).getId().equals("4")) {
                    Log.e("mmmmmm", "mmmmmmmm");
                    selectmale();
                    disbaleGenderClickListeners();
                } else if (ProfileForDataList.get(position).getId().equals("3") || ProfileForDataList.get(position).getId().equals("5")) {

                    selectfemale();
                    Log.e("ffffff", "fffffffff");
                    disbaleGenderClickListeners();
                } else {
                    enableGenderClickListeners();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ll_maleNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectmale();
            }
        });
        ll_maleSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectfemale();
            }
        });
        ll_femaleNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectfemale();
            }
        });
        ll_femaleSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectmale();
            }
        });


        bt_register_free.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    try {


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
                                                      /*  else if(other_info.equals("2000-4-10")){


                                                            Toast.makeText(RegistrationActivity.this, "Please select date !", Toast.LENGTH_SHORT).show();
                                                        }
*/

                                                            else {
                                                                // Intent in = new Intent(RegistrationActivity.this, RegisterGeographicActivity.class);
                                                                //   startActivity(in);

                                                                WebArd profileForDataObj = (WebArd) spinner_profilefor.getSelectedItem();
                                                                WebArd sourceDataObj = (WebArd) spinner_source.getSelectedItem();
                                                                WebArd religionDataObj = (WebArd) spinner_religion.getSelectedItem();
                                                                //  String other_info = datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();

                                                                if (connectionDetector.isConnectingToInternet()) {

                                                                    /*  Toast.makeText(RegistrationActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                                                                     */
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

                                                    }
                                                }
                                            }

        );

    }


    private void disbaleGenderClickListeners()

    {
        ll_maleNormal.setEnabled(false);
        ll_maleSelected.setEnabled(false);
        ll_femaleSelected.setEnabled(false);
        ll_femaleNormal.setEnabled(false);
    }

    private void enableGenderClickListeners()

    {
        ll_maleNormal.setEnabled(true);
        ll_maleSelected.setEnabled(true);
        ll_femaleSelected.setEnabled(true);
        ll_femaleNormal.setEnabled(true);
    }

    private void selectmale() {
        ll_maleNormal.setVisibility(View.GONE);
        ll_maleSelected.setVisibility(View.VISIBLE);
        ll_femaleSelected.setVisibility(View.GONE);
        ll_femaleNormal.setVisibility(View.VISIBLE);
        gender = "M";
    }

    private void selectfemale() {
        ll_maleNormal.setVisibility(View.VISIBLE);
        ll_maleSelected.setVisibility(View.GONE);
        ll_femaleNormal.setVisibility(View.GONE);
        ll_femaleSelected.setVisibility(View.VISIBLE);
        gender = "F";
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

    private boolean isProfileNameValid(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^\\d*[a-zA-Z][a-zA-Z_\\d]*$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return (password.length() >= 8 && password.length() <= 15);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registration_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MarryMax marryMax = new MarryMax(RegistrationActivity.this);

        switch (item.getItemId()) {

            case R.id.action_home:
                finish();

                return true;

            case R.id.action_aboutus:
                marryMax.aboutus();

                return true;
            case R.id.action_benefits:
                marryMax.benefits();
                return true;
        /*    case R.id.action_personalized_matching:
                marryMax.personalizedMatching();
                return true;*/
            case R.id.action_faq:
                marryMax.faq();
                return true;
            case R.id.action_contact:
                marryMax.contact();
                return true;

            case R.id.action_privacy_policy:
                marryMax.PrivacyPolicy();
                return true;
            case R.id.action_profile_guideline:
                marryMax.ProfileGuideline();
                return true;
            case R.id.action_security_tip:
                marryMax.SecurityTip();
                return true;
            case R.id.action_terms_of_use:
                marryMax.TermsofUse();
                return true;
            case R.id.action_why_marrymax:
                marryMax.WhyMarryMax();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void getRegistrationData() {
        String url = Urls.reg_Listing;


        pDialog.show();

        Log.e("url", "" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        Log.d("res", response.toString());
                        try {

                            Gson gsonc;

                            GsonBuilder gsonBuilderc = new GsonBuilder();

                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();


                            JSONArray profile_owner_array = response.getJSONArray("profile_owner");
                            ProfileForDataList = (List<WebArd>) gsonc.fromJson(profile_owner_array.toString(), listType);
                            ProfileForDataList.add(0, new WebArd("-1", "Select Profile For"));
                            adapter_profilefor.updateDataList(ProfileForDataList);


                            JSONArray religion_array = response.getJSONArray("religion");
                            ReligionDataList = (List<WebArd>) gsonc.fromJson(religion_array.toString(), listType);
                            ReligionDataList.add(0, new WebArd("-1", "Select Religion"));
                            adapter_religion.updateDataList(ReligionDataList);


                            JSONArray source_array = response.getJSONArray("source");
                            SourceDataList = (List<WebArd>) gsonc.fromJson(source_array.toString(), listType);
                            SourceDataList.add(0, new WebArd("-1", "Source"));
                            adapter_source.updateDataList(SourceDataList);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("res", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {


                return Constants.getHashMap();
            }
        };

// Adding request to request queue
        //  AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);

    }


    private void RegisterUser(final String email, final String password, JSONObject params) {


        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        Log.e("params url", Urls.RegistrationUrl + "  ==  " + params);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.RegistrationUrl, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("res", response + "");
                        pDialog.dismiss();

                        try {
                            int responseid = response.getInt("id");

                            if (responseid == 1) {
                                //   Toast.makeText(RegistrationActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                LoginUser(email, password);
                            } else if (responseid == 3) {
                                Toast.makeText(RegistrationActivity.this, "Some field is incomplete", Toast.LENGTH_SHORT).show();
                            } else if (responseid == 4) {

                                Toast.makeText(RegistrationActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();

                            } else if (responseid == 5) {
                                Toast.makeText(RegistrationActivity.this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                            } else if (responseid == 6) {
                                Toast.makeText(RegistrationActivity.this, "Profile Name Already Exists", Toast.LENGTH_SHORT).show();
                            } else if (responseid == 7) {
                                Toast.makeText(RegistrationActivity.this, "Email Not Valid", Toast.LENGTH_SHORT).show();
                            } else if (responseid == 8) {
                                Toast.makeText(RegistrationActivity.this, "Profile Name Not Valid", Toast.LENGTH_SHORT).show();
                            }
                            Log.e("Response id", responseid + "");
                            //    Log.d("Alias", response.get("alias").toString());
                            ///   Log.d("member status", response.getInt("member_status") + "");
                                       /*   Intent in = new Intent(RegistrationActivity.this, RegisterGeographicActivity.class);
                                                            startActivity(in);*/


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

    private void LoginUser(String email, String password) {


        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.LoginUrl, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("res", response.toString());
                        pDialog.dismiss();
                        try {

                            if (response.get("status").equals("success")) {

                                SharedPreferenceManager.setUserObject(getApplicationContext(), response.getJSONObject("data"));


                                UserSessionManager session = new UserSessionManager(
                                        getApplicationContext());


                                session.createUserLoginSession(response.getJSONObject("data").get("alias").toString(), response.getJSONObject("data").getString("path") + "", response.getJSONObject("data").getInt("member_status") + "");


                                Intent intent = new Intent(RegistrationActivity.this, RegisterGeographicActivity.class);

                                intent.putExtra("updateData", false);

                                startActivity(intent);
                                finish();


                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                //VolleyLog.e("res err", "Error: " +networkResponse);
                Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
