package com.chicsol.marrymax.activities.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.ActivityLogin;
import com.chicsol.marrymax.dialogs.dialogDosDonts;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.chicsol.marrymax.widgets.mTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegisterPersonalityActivity extends BaseRegistrationActivity implements CompoundButton.OnCheckedChangeListener {
    private Button bt_back, bt_register_free;
    private GridLayout gridLayout;
    private List<WebArd> personalityDataList;
    private boolean updateData = true;
    private Members members_obj;
    private EditText etAboutMyChoice, etAboutMe, etMyStrength, etMostThankfulFor, etWhatIdoFor;
    private mTextView tvDosDont;
    private CheckBox cbDeclaration;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_personality);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initialize();

        GetPersonalityData();
        setListeners();
    }

    private void initialize() {

        bt_back = (Button) findViewById(R.id.buttonBack);
        bt_register_free = (Button) findViewById(R.id.buttonFinishRegistration1);
        personalityDataList = new ArrayList<>();
        gridLayout = (GridLayout) findViewById(R.id.GridlayoutPersonality);

        etAboutMe = (EditText) findViewById(R.id.EditTextAboutMePers);

        etAboutMyChoice = (EditText) findViewById(R.id.EditTextAboutMyChoice);
        etMostThankfulFor = (EditText) findViewById(R.id.EditTextMostThankfulPers);
        etMyStrength = (EditText) findViewById(R.id.EditTextmyStrengthsPers);
        etWhatIdoFor = (EditText) findViewById(R.id.EditTextWhatIDoPers);
        cbDeclaration = (CheckBox) findViewById(R.id.CheckBoxPersonalityDeclaration);


        // tvDeclaration.setText(Html.fromHtml(getResources().getString(R.string.declaration_text_2)));

        WebView webView = (WebView) findViewById(R.id.WebViewPersonalityDeclaration);
        // displaying content in WebView from html file that stored in assets folder
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + "declaration.html");
/*


       tvDeclaration.setText(" Please read it carefully, before posting this Profile!\nI am a Pakistani by origin.\n" +
               "I hereby declare that I am genuinely interested in finding a matrimonial match for this listing. \n" +
               "I am the authorized person to create and list this profile and not related or linked with any other matrimonial service provider. \n" +
               "This profile doesn\'t contain any gender, age, ethnic, religion or any other discriminatory text, unless there is a genuine requirement. \n" +
               "The profiles or matches, which are contacted through this listing, will be used for this matrimonial profile only and not for any other purpose; violation of this term may lead to stern legal action.\n");
        Spannable s =new SpannableString(tvDeclaration.getText());
        s.setSpan(new BulletSpan(), 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new BulletSpan(), 4, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new BulletSpan(), 8, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new BulletSpan(), 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new BulletSpan(), 4, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new BulletSpan(), 8, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDeclaration.setText(s);*/


        etAboutMe.setHorizontallyScrolling(false);
        etAboutMe.setMaxLines(Integer.MAX_VALUE);

        etAboutMyChoice.setHorizontallyScrolling(false);
        etAboutMyChoice.setMaxLines(Integer.MAX_VALUE);

        etMostThankfulFor.setHorizontallyScrolling(false);
        etMostThankfulFor.setMaxLines(Integer.MAX_VALUE);

        etMyStrength.setHorizontallyScrolling(false);
        etMyStrength.setMaxLines(Integer.MAX_VALUE);

        etWhatIdoFor.setHorizontallyScrolling(false);
        etWhatIdoFor.setMaxLines(Integer.MAX_VALUE);


        fabGeographic.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepComplete));
        fabAppearance.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepComplete));
        fabLifestyle.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepComplete));
        fabInterest.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepComplete));
        fabPersonality.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepOnGoing));


        tvGeogrphic.setTextColor(getResources().getColor(R.color.colorRegistrationStepComplete));
        tvAppearance.setTextColor(getResources().getColor(R.color.colorRegistrationStepComplete));
        tvLifestyle.setTextColor(getResources().getColor(R.color.colorRegistrationStepComplete));
        tvInterest.setTextColor(getResources().getColor(R.color.colorRegistrationStepComplete));
        tvPersonality.setTextColor(getResources().getColor(R.color.colorRegistrationStepOnGoing));


        tvDosDont = (mTextView) findViewById(R.id.TextViewDosDont);

        if (marryMax.getUpdateCheck(getApplicationContext())) {

            bt_register_free.setText("Update");
        }
    }

    /*    void showDialog() {


            // DialogFragment.show() will take care of adding the fragment
            // in a transaction.  We also want to remove any currently showing
            // dialog, so make our own transaction and take care of that here.
           *//* FragmentTransaction ft = getSupportFragmentManager();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);*//*

        // Create and show the dialog.
        dialogDosDonts newFragment = dialogDosDonts.newInstance("hell");
     newFragment.show(getSupportFragmentManager(), "dialog");
    }*/
    private void setListeners() {

        etAboutMe.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    etMyStrength.setFocusable(true);
                }
                return false;
            }
        });

        etMyStrength.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    etMostThankfulFor.setFocusable(true);
                }
                return false;
            }
        });

        etMostThankfulFor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    etWhatIdoFor.setFocusable(true);
                }
                return false;
            }
        });
        etWhatIdoFor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    etAboutMyChoice.setFocusable(true);
                }
                return false;
            }
        });
        etWhatIdoFor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    etAboutMyChoice.setFocusable(true);
                }
                return false;
            }
        });


        tvDosDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     Toast.makeText(RegisterPersonalityActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                // getDosDontsData();
                getDosDonts();
                //        showDialog();
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RegisterPersonalityActivity.this, RegisterInterest.class);
                startActivity(in);
                finish();
            }
        });


        bt_register_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGenerator vg = new ViewGenerator(RegisterPersonalityActivity.this);
                cbDeclaration.setError(null);

                if (!checkSelections(v)) {

                    //  Toast.makeText(RegisterPersonalityActivity.this, ""+etAboutMe.length(), Toast.LENGTH_SHORT).show();
                    //
                    String personality_ids = vg.getSelectionFromCheckbox(gridLayout);

                    JSONObject params = new JSONObject();
                    try {


                        params.put("personality_ids", personality_ids);

                        params.put("other_info", etAboutMe.getText().toString());
                        params.put("about_my_choice", etAboutMyChoice.getText().toString());
                        params.put("for_fun", etWhatIdoFor.getText().toString());
                        params.put("good_quality", etMyStrength.getText().toString());
                        params.put("most_thankfull", etMostThankfulFor.getText().toString());
                        params.put("member_status", SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status());
                        params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                        // Log.e("ppp", " param " + params);
                        if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
                            updatePersonality(params);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    private boolean checkSelections(View v) {
        boolean ck = false;

        ViewGenerator vg = new ViewGenerator(RegisterPersonalityActivity.this);

        if (vg.getSelectionFromCheckbox(gridLayout) == "") {
            //   Toast.makeText(this, "Select Personality", Toast.LENGTH_SHORT).show();

            Snackbar.make(v, "Please select Personality", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();


            ck = true;
        }
        if (TextUtils.isEmpty(etAboutMe.getText().toString().trim())) {
            etAboutMe.setError("Please Enter About Me");

            etAboutMe.requestFocus();
            ck = true;
        }
        if (!TextUtils.isEmpty(etAboutMe.getText().toString().trim())) {
            if (etAboutMe.getText().length() < 15 || etAboutMe.getText().length() > 1000) {
                etAboutMe.setError("Min 15 char, max 1000 char");

                etAboutMe.requestFocus();
                ck = true;

            }
        }
        if (!TextUtils.isEmpty(etAboutMyChoice.getText().toString().trim())) {
            if (etAboutMyChoice.getText().length() < 15 || etAboutMyChoice.getText().length() > 1000) {
                etAboutMyChoice.setError("Min 15 char, max 1000 char");

                etAboutMyChoice.requestFocus();
                ck = true;

            }
        }

        if (!TextUtils.isEmpty(etWhatIdoFor.getText().toString().trim())) {
            if (etWhatIdoFor.getText().toString().length() > 140) {
                etWhatIdoFor.setError("Min 15 char, max 1000 char");

                etWhatIdoFor.requestFocus();
                ck = true;
            }

        }
        if (!TextUtils.isEmpty(etMyStrength.getText().toString())) {
            if (etMyStrength.getText().toString().length() > 140) {
                etMyStrength.setError("Min 15 char, max 1000 char");

                etMyStrength.requestFocus();
                ck = true;
            }

        }
        if (!TextUtils.isEmpty(etMostThankfulFor.getText().toString().trim())) {
            if (etMostThankfulFor.getText().toString().length() > 140) {
                etMostThankfulFor.setError("Min 15 char, max 1000 char");

                etMostThankfulFor.requestFocus();
                ck = true;
            }

        }


        if (!updateData) {
            if (!cbDeclaration.isChecked()) {
                cbDeclaration.setError("Please complete all fields with valid information!");
                Toast.makeText(this, "Please complete all fields with valid information!", Toast.LENGTH_SHORT).show();
                cbDeclaration.requestFocus();
                ck = true;
            }
        }


        return ck;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registration_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item_image_slider clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //   NavUtils.navigateUpFromSameTask(this);
                return true;

       *//*     case R.id.action_aboutus:
                Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
                return true;*//*

            //noinspection SimplifiableIfStatement
 *//*       if (id == R.id.action_aboutus) {

     *//**//* Intent intent = new Intent(Main.this, activity_change_password.class);
            startActivity(intent);*//**//*
            return true;
        }
*//*
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    private void selectFormData(Members members_obj) {

        ViewGenerator viewGenerator = new ViewGenerator(RegisterPersonalityActivity.this);

        if (members_obj.getPersonality_ids() != "") {
            viewGenerator.selectCheckRadioFromGridLayout(gridLayout, members_obj.getPersonality_ids());
        }

        etAboutMe.setText(members_obj.getOther_info() + "");
        etAboutMyChoice.setText(members_obj.getAbout_my_choice() + "");
        etWhatIdoFor.setText(members_obj.getFor_fun() + "");
        etMyStrength.setText(members_obj.getGood_quality() + "");
        etMostThankfulFor.setText(members_obj.getMost_thankfull() + "");
    }


    private void getDosDonts() {


        showProgressDialog();

        JsonArrayRequest req = new JsonArrayRequest(Urls.getPersonalityDosDonts,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                        dismissProgressDialog();
                        //        try {
                        dialogDosDonts newFragment = dialogDosDonts.newInstance(response.toString());
                        newFragment.show(getSupportFragmentManager(), "dialog");


/*
                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);
                          //  MyCountryStateDataList.clear();

                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getName();*/


                        // MyCountryStateDataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);


                         /*   MyCountryStateDataList.add(0, new WebArd("-1", "Please Select"));
                            adapter_myCountryStates.updateDataList(MyCountryStateDataList);
                            spMyCountryState.setSelection(0);*/

                       /* } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

                        dismissProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                dismissProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(req);
    }


    private void GetPersonalityData() {
        showProgressDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Urls.getPersonalityUrl + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //  Log.e("res mainnnnnnnnnnn", response + "");
                        try {
                            dismissProgressDialog();

                            JSONArray jsonArrayInterest = response.getJSONArray("personality");


                            Gson gsonc;

                            GsonBuilder gsonBuilderc = new GsonBuilder();

                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();


                            personalityDataList = (List<WebArd>) gsonc.fromJson(jsonArrayInterest.toString(), listType);


                            //=================================parsing object
                            Gson gson;
                            JSONArray jsonGrography = response.getJSONArray("personalitys");
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            //  Log.e("Aliaaaaaaaasss", jsonGrography.get(0).toString());
                            members_obj = gson.fromJson(jsonGrography.get(0).toString(), Members.class);
                            // Log.e("Aliaaaaaaaasss", members_obj.getCountry_id() + "");


                        } catch (JSONException e) {
                            dismissProgressDialog();
                            e.printStackTrace();
                        }

                        //==============DATA SETTING==============================================

                        ViewGenerator viewGenerator = new ViewGenerator(RegisterPersonalityActivity.this);

                        Log.e("Perssssssssss", members_obj.getPersonality_ids() + "");
                        if (members_obj.getPersonality_ids().equals("")) {
                            updateData = false;

                        } else {
                            updateData = true;

                        }


                        if (updateData) {
                            cbDeclaration.setChecked(true);
                            cbDeclaration.setEnabled(false);


                            Point size = new Point();
                            getWindowManager().getDefaultDisplay().getSize(size);
                            viewGenerator.generateCheckBoxesInGridLayout(personalityDataList, gridLayout, size.x - 30);

                            selectFormData(members_obj);


                        } else {
                            Point size = new Point();
                            getWindowManager().getDefaultDisplay().getSize(size);
                            viewGenerator.generateCheckBoxesInGridLayout(personalityDataList, gridLayout, size.x - 30);
                        }

                        int childcount = gridLayout.getChildCount();
                        for (int i = 0; i < childcount; i++) {

                            View sv = gridLayout.getChildAt(i);
                            if (sv instanceof CheckBox) {

                                ((CheckBox) sv).setOnCheckedChangeListener(RegisterPersonalityActivity.this);

                            }
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




/*        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);

    }

    private void updatePersonality(JSONObject params) {

    showProgressDialog();


        Log.e("Params " + Urls.updatePersonalityUrl, "" + params);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.updatePersonalityUrl, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update data", response + "");
                        try {
                            int responseid = response.getInt("id");

                            if (responseid == 1) {


                                Members member = SharedPreferenceManager.getUserObject(getApplicationContext());
                                if (member.getMember_status() == 0) {
                                    member.setMember_status(1);
                                    SharedPreferenceManager.setUserObject(getApplicationContext(), member);
                                }


                                if (!marryMax.getUpdateCheck(getApplicationContext())) {
                                    Intent in = new Intent(RegisterPersonalityActivity.this, ActivityLogin.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(in);

                                } else {

                                    Toast.makeText(RegisterPersonalityActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                }


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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        ViewGenerator vg = new ViewGenerator(RegisterPersonalityActivity.this);
        String personality_ids = vg.getSelectionFromCheckbox(gridLayout);

        if (personality_ids != null) {
            String[] arrayList = personality_ids.split(",");
            if (arrayList.length > 4) {
                Toast.makeText(this, "Only four personality can be selected. To change personality, un-check any selected one and then select attribute of your choice and adjust your personality accordingly.\n" +
                        "              ", Toast.LENGTH_SHORT).show();
                compoundButton.setChecked(!b);
                      /*  Snackbar.make(compoundButton, "Please select Interests", Snackbar.LENGTH_SHORT)
                       .setAction("Action", null).show();*/


            }

        }
    }


    @Override
    public void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(RegisterPersonalityActivity.this);
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
