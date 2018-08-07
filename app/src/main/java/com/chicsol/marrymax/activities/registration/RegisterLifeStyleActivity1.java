package com.chicsol.marrymax.activities.registration;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.BuildConfig;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.MySpinnerAdapter;
import com.chicsol.marrymax.dialogs.dialogMultiChoice;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class RegisterLifeStyleActivity1 extends BaseRegistrationActivity implements dialogMultiChoice.onMultiChoiceSaveListener {
    String about_member_id;
    private Button bt_register_free, bt_back;
    private LinearLayout llMainSecondMarriage, llcbViewEconomy, llcbViewReligious, llcbViewEthnic, llcbViewMarital, llcbViewChildren, llChildren, llcbViewMarriage;
    private RadioGroup rgEconomy, rgReligious, rgEthnic, rgMarital, rgChildren, rgMarriage;
    private List<WebArd> languageDataList, marriageDataList, educationDataList, myEducationDataList, educationFieldDataList, castesDataList, religiousDataList, ethnicDataList, martialDataList, occupationDataList, myOccupationDataList, childrenDataList, incomeDataList, economyDataList, graduationYearDataList;
    private Members members_obj;
    private Spinner spMyLanguage, spMyEducation, spMyEducationalField, spMyGraduationYear, spMyOccupation, spMyGradYear, spMyAnnualIncomeLevel;
    private MySpinnerAdapter spAdapterMyLanguage, spAdapterMyEducation, spAdapterMyGradYear, spAdapterMyEducationalField, spAdapterMyGraduationyear, spAdapterMyOccupation, spAdapterMyAnnualIncome;
    private boolean updateData = true;
    private TextView tvMcMyChoiceEducation, tvMcMyChoiceOccupation, tvMutliChoiceMyChoiceLanguage, tvMutliChoiceMySpokenLanguage;

    private ArrayList selectedEducationIdDataList, selectedOccupationIdDataList, selectedOccupationDataList;
    private EditText etGraduatedFrom, etNoOfGirls, etNoOfBoys, etMinAge, etMaxAge;
    private AutoCompleteTextView acMyCaste;
    private String mcOccupationText = null, mcEducationText = null;

    private ArrayAdapter acAdapter;
    private long my_id;
    private NestedScrollView scrollMain;
    private ProgressDialog pDialog;
    private FloatingActionButton fabLifeStyle1, fabLifeStyle2;
    private AppCompatImageButton btRemoveChildren, btRemoveSchool;

    private long removeChildrenMyid;
    private LinearLayout llLanguage;
    private LinearLayout llChoiceLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_lifestyle_step1);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();

        GetLifeStyleData();
        setListeners();


        //  android:id="@+id/fabLifeStyle1"


    }

    private void initialize() {
        pDialog = new ProgressDialog(RegisterLifeStyleActivity1.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);


        btRemoveChildren = (AppCompatImageButton) findViewById(R.id.ButtonLifeStyleRemoveChildren);
        btRemoveSchool = (AppCompatImageButton) findViewById(R.id.ButtonLifeStyleRemoveSchool);


        scrollMain = (NestedScrollView) findViewById(R.id.ScrollViewRegAppearance);

        bt_register_free = (Button) findViewById(R.id.buttonContinueStep2);
        bt_back = (Button) findViewById(R.id.buttonBack);


        llMainSecondMarriage = (LinearLayout) findViewById(R.id.LinearLayoutRegLifeStyle1Marriage);


        llChildren = (LinearLayout) findViewById(R.id.LinearLayoutChildren);

        rgEconomy = (RadioGroup) findViewById(R.id.RadioGroupEconomy);
        llcbViewEconomy = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceEconomy);

        rgReligious = (RadioGroup) findViewById(R.id.RadioGroupReligious);
        llcbViewEthnic = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceEthnic);

        rgReligious = (RadioGroup) findViewById(R.id.RadioGroupReligious);
        llcbViewReligious = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceReligious);

        rgMarital = (RadioGroup) findViewById(R.id.RadioGroupMartial);
        llcbViewMarital = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceMartial);

        rgChildren = (RadioGroup) findViewById(R.id.RadioGroupChildren);
        llcbViewChildren = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceChildren);

        rgEthnic = (RadioGroup) findViewById(R.id.RadioGroupEthnic);
        llcbViewEthnic = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceEthnic);

        rgMarriage = (RadioGroup) findViewById(R.id.RadioGroupMarriage);
        llcbViewMarriage = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceMarriage);

        fabLifeStyle1 = (FloatingActionButton) findViewById(R.id.fabLifeStyle1);
        fabLifeStyle2 = (FloatingActionButton) findViewById(R.id.fabLifeStyle2);


/*        if (BuildConfig.FLAVOR.equals("alfalah")) {

            llLanguage = (LinearLayout) findViewById(R.id.LinearLayoutRegLifeStyle1Language);
            llChoiceLanguage = (LinearLayout) findViewById(R.id.LinearLayoutRegLifeStyle1ChoiceLanguage);
            llLanguage.setVisibility(View.VISIBLE);
            llChoiceLanguage.setVisibility(View.VISIBLE);

            spMyLanguage = (Spinner) findViewById(R.id.spinnerMyLanguage);

            languageDataList = new ArrayList<>();

            spAdapterMyLanguage = new MySpinnerAdapter(this,
                    android.R.layout.simple_spinner_item, languageDataList);
            spAdapterMyLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spMyLanguage.setAdapter(spAdapterMyLanguage);


            tvMutliChoiceMyChoiceLanguage = (TextView) findViewById(R.id.MutliChoiceMyChoiceLanguage);

            tvMutliChoiceMySpokenLanguage = (TextView) findViewById(R.id.MutliChoiceMySpokenLanguage);

        }*/


        marriageDataList = new ArrayList<>();
        educationDataList = new ArrayList<>();
        myEducationDataList = new ArrayList<>();
        educationFieldDataList = new ArrayList<>();

        religiousDataList = new ArrayList<>();

        incomeDataList = new ArrayList<>();
        castesDataList = new ArrayList<>();

        ethnicDataList = new ArrayList<>();
        martialDataList = new ArrayList<>();
        occupationDataList = new ArrayList<>();
        myOccupationDataList = new ArrayList<>();

        childrenDataList = new ArrayList<>();

        incomeDataList = new ArrayList<>();
        economyDataList = new ArrayList<>();

        graduationYearDataList = new ArrayList<>();


        Calendar calendar = Calendar.getInstance();
        int current_year = calendar.get(Calendar.YEAR);

        for (int i = current_year; i > current_year - 60; i--) {

            WebArd webArd = new WebArd(String.valueOf(i), String.valueOf(i) + "");
            graduationYearDataList.add(webArd);
        }
        graduationYearDataList.add(0, new WebArd("0", "Please Select"));

        spMyEducation = (Spinner) findViewById(R.id.spinnerMyEducation);
        spMyEducationalField = (Spinner) findViewById(R.id.spinnerMyEducationalField);
        spMyOccupation = (Spinner) findViewById(R.id.spinnerMyOccupation);
        spMyGraduationYear = (Spinner) findViewById(R.id.spinnerGraduationYear1);

        spMyAnnualIncomeLevel = (Spinner) findViewById(R.id.spinnerMyAnnualIncome);


        spAdapterMyEducation = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, myEducationDataList);
        spAdapterMyEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyEducation.setAdapter(spAdapterMyEducation);

        spAdapterMyEducationalField = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, educationFieldDataList);
        spAdapterMyEducationalField.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyEducationalField.setAdapter(spAdapterMyEducationalField);

        spAdapterMyOccupation = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, myOccupationDataList);
        spAdapterMyOccupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyOccupation.setAdapter(spAdapterMyOccupation);

        spAdapterMyGraduationyear = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, graduationYearDataList);
        spAdapterMyGraduationyear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyGraduationYear.setAdapter(spAdapterMyGraduationyear);


        spAdapterMyAnnualIncome = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, incomeDataList);
        spAdapterMyAnnualIncome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyAnnualIncomeLevel.setAdapter(spAdapterMyAnnualIncome);


        tvMcMyChoiceEducation = (TextView) findViewById(R.id.MutliChoiceMyChoiceEducation);
        tvMcMyChoiceOccupation = (TextView) findViewById(R.id.MutliChoiceMyChoiceOccupation);
        selectedEducationIdDataList = new ArrayList();
        //   selectedEducatonDataList = new ArrayList();
        selectedOccupationIdDataList = new ArrayList();
        selectedOccupationDataList = new ArrayList();
        //   spAdapterMyGraduationyear.updateDataList(graduationYearDataList);


        acMyCaste = (AutoCompleteTextView) findViewById(R.id.EditTextMyCaste);
        acMyCaste.setThreshold(1);


        etGraduatedFrom = (EditText) findViewById(R.id.EditTextGraduatedFrom);

        etNoOfBoys = (EditText) findViewById(R.id.EditTextNumOfBoyz);
        etNoOfGirls = (EditText) findViewById(R.id.EditTextNumOfGirls);
        etMaxAge = (EditText) findViewById(R.id.EditTextMaxAge);
        etMinAge = (EditText) findViewById(R.id.EditTextMinAge);



/*   acAdapter = new ArrayAdapter<WebArd>
                (this,android.R.layout.simple_list_item_1,castesDataList);*/

/*        acAdapter = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, incomeDataList);
        acAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        acMyCaste.setAdapter(acAdapter);*/

        fabGeographic.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepComplete));
        fabAppearance.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepComplete));
        fabLifestyle.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepOnGoing));


        tvGeogrphic.setTextColor(getResources().getColor(R.color.colorRegistrationStepComplete));
        tvAppearance.setTextColor(getResources().getColor(R.color.colorRegistrationStepComplete));
        tvLifestyle.setTextColor(getResources().getColor(R.color.colorRegistrationStepOnGoing));


        if (marryMax.getUpdateCheck(getApplicationContext())) {

            bt_register_free.setText("Update");
        }


    }


    public void setListeners() {

        btRemoveSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject params = new JSONObject();
                try {
                    params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());

                    removeSchool(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        btRemoveChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject params = new JSONObject();
                try {
                    params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());

                    params.put("id", removeChildrenMyid);

                    removeChildren(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        fabLifeStyle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class cls = RegisterLifeStyleActivity1.class;
                marryMax.getProfileProgress(cls, RegisterLifeStyleActivity1.this, getApplicationContext(), SharedPreferenceManager.getUserObject(getApplicationContext()));
            }
        });
        fabLifeStyle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class cls = RegisterLifeStyleActivity2.class;
                marryMax.getProfileProgress(cls, RegisterLifeStyleActivity1.this, getApplicationContext(), SharedPreferenceManager.getUserObject(getApplicationContext()));
            }
        });

        acMyCaste.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //etPasswordView.setFocusable(true);
                }
                return false;
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterLifeStyleActivity1.this, RegisterAppearanceActivity.class);
                startActivity(intent);
                finish();
            }
        });


        rgChildren.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == 1) {

                    llChildren.setVisibility(View.INVISIBLE);

                    etNoOfBoys.setText("");
                    etNoOfGirls.setText("");
                    etMinAge.setText("");
                    etMaxAge.setText("");


                } else {
                    llChildren.setVisibility(View.VISIBLE);
                }
            }
        });
        rgMarital.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == 5) {
                    rgMarriage.clearCheck();
                    Members member = SharedPreferenceManager.getUserObject(getApplicationContext());
                    if (member.get_member_status() >= 2) {
                        llMainSecondMarriage.setVisibility(View.GONE);
                    } else {
                        llMainSecondMarriage.setVisibility(View.VISIBLE);
                    }

                } else {
                    rgMarriage.clearCheck();
                    llMainSecondMarriage.setVisibility(View.GONE);
                }
            }
        });


        tvMcMyChoiceEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Gson gson = new Gson();
                dialogMultiChoice newFragment = dialogMultiChoice.newInstance(gson.toJson(educationDataList), 1, "My Choice Education");
                newFragment.show(getSupportFragmentManager(), "dialog");


            }
        });
        tvMcMyChoiceOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                dialogMultiChoice newFragment = dialogMultiChoice.newInstance(gson.toJson(occupationDataList), 2, "My Choice Occupation");
                newFragment.show(getSupportFragmentManager(), "dialog");


            }
        });
/*        if (BuildConfig.FLAVOR.equals("alfalah")) {

            tvMutliChoiceMyChoiceLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    dialogMultiChoice newFragment = dialogMultiChoice.newInstance(gson.toJson(occupationDataList), 2, "My Choice Occupation");
                    newFragment.show(getSupportFragmentManager(), "dialog");

                }
            });

            tvMutliChoiceMySpokenLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    dialogMultiChoice newFragment = dialogMultiChoice.newInstance(gson.toJson(occupationDataList), 2, "My Choice Occupation");
                    newFragment.show(getSupportFragmentManager(), "dialog");

                }
            });


        }*/

//==========================================================================


        bt_register_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkFieldsSelection(v)) {
                    //Toast.makeText(RegisterLifeStyleActivity1.this, "Update Clicked", Toast.LENGTH_SHORT).show();
                    ViewGenerator vg = new ViewGenerator(RegisterLifeStyleActivity1.this);

                    WebArd mMyEducationObj = (WebArd) spMyEducation.getSelectedItem();
                    String education_id = mMyEducationObj.getId();


                    WebArd mMyEducationFieldObj = (WebArd) spMyEducationalField.getSelectedItem();
                    String education_field_id = mMyEducationFieldObj.getId();

                    WebArd mMyGraduationYearObj = (WebArd) spMyGraduationYear.getSelectedItem();
                    String about_type_id = mMyGraduationYearObj.getId();
                    if (about_type_id.equals("-1")) {
                        about_type_id = "0";

                    }


                    WebArd mMyOccupationObj = (WebArd) spMyOccupation.getSelectedItem();
                    String occupation_id = mMyOccupationObj.getId();


                    WebArd mMyAnnualIncomeObj = (WebArd) spMyAnnualIncomeLevel.getSelectedItem();
                    String income_level_id = mMyAnnualIncomeObj.getId();

                    //mychoice edu==========
                    MarryMax max = new MarryMax(null);


                    //Selected edu
                    StringBuilder sbSelectedMyChoiceOccupation = new StringBuilder();
                    sbSelectedMyChoiceOccupation = max.getSelectedIdsFromList(occupationDataList);
                    Log.e("sel Occupation ids :", sbSelectedMyChoiceOccupation + "");
                 /*   for (int i = 0; i < selectedOccupationDataList.size(); i++) {
                        sbSelectedMyChoiceOccupation.append(occupationDataList.get((Integer) selectedOccupationDataList.get(i)).getId());
                        if (i != selectedOccupationDataList.size() - 1) {
                            sbSelectedMyChoiceOccupation.append(",");
                        }
                    }
                    Log.e("occupation idsss", "ids :  " + sbSelectedMyChoiceOccupation.toString());
                    String choice_occupation_ids = clearCommarEnd(sbSelectedMyChoiceOccupation).toString();

                    if (sbSelectedMyChoiceOccupation.length() == 0) {
                        choice_occupation_ids = "0";
                    }*/


                    //mychoice edu==========

                    //   String mChoiceEducation = vg.getSelectedIdsFromsList(selectedEducatonDataList, educationDataList);

                    //    Log.e("sel EducatonData ids :", mChoiceEducation + "");

                    //==================
                    String economy_id = String.valueOf(rgEconomy.getCheckedRadioButtonId());
                    String choice_economy_ids = vg.getSelectionFromCheckbox(llcbViewEconomy);

                    String religious_sect_id = String.valueOf(rgReligious.getCheckedRadioButtonId());
                    String choice_religious_sect_ids = vg.getSelectionFromCheckbox(llcbViewReligious);

                    String ethnic_background_id = String.valueOf(rgEthnic.getCheckedRadioButtonId());
                    String choice_ethnic_bground_ids = vg.getSelectionFromCheckbox(llcbViewEthnic);

                    String marital_status_id = String.valueOf(rgMarital.getCheckedRadioButtonId());
                    String choice_marital_status_ids = vg.getSelectionFromCheckbox(llcbViewMarital);

                    String children_id = String.valueOf(rgChildren.getCheckedRadioButtonId());
                    String choice_children_ids = vg.getSelectionFromCheckbox(llcbViewChildren);

                    String second_marriage_reason_id = String.valueOf(rgMarriage.getCheckedRadioButtonId());

                    //EditTexts
                    String about_type = etGraduatedFrom.getText().toString();


                    String girls_count = "0", boys_count = "0", min_age = "0", max_age = "0";


                    if (children_id.equals("2") || children_id.equals("3")) {
                        girls_count = etNoOfGirls.getText().toString();
                        boys_count = etNoOfBoys.getText().toString();
                        min_age = etMinAge.getText().toString();
                        max_age = etMaxAge.getText().toString();


                    }


                    //Selected Education
                    StringBuilder sbSelectedMyChoiceEdu = new StringBuilder();

                    sbSelectedMyChoiceEdu = max.getSelectedIdsFromList(educationDataList);


                    JSONObject params = new JSONObject();
                    try {


                        params.put("education_id", education_id);
                        params.put("choice_education_ids", sbSelectedMyChoiceEdu);

                        params.put("education_field_id", education_field_id);

                        params.put("about_type", about_type);
                        params.put("about_type_id", about_type_id);

                        params.put("occupation_id", occupation_id);
                        params.put("choice_occupation_ids", sbSelectedMyChoiceOccupation);

                        params.put("economy_id", economy_id);
                        params.put("choice_economy_ids", choice_economy_ids);

                        params.put("religious_sect_id", religious_sect_id);
                        params.put("choice_religious_sect_ids", choice_religious_sect_ids);

                        params.put("ethnic_background_id", ethnic_background_id);
                        params.put("choice_ethnic_bground_ids", choice_ethnic_bground_ids);


                        params.put("marital_status_id", marital_status_id);
                        params.put("choice_marital_status_ids", choice_marital_status_ids);

                        params.put("children_id", children_id);
                        params.put("choice_children_ids", choice_children_ids);


                        params.put("girls_count", girls_count);
                        params.put("boys_count", boys_count);

                        params.put("min_age", min_age);
                        params.put("max_age", max_age);

                        params.put("income_level_id", income_level_id);

                        params.put("income_level_unit", "Y");
                        params.put("caste_name", acMyCaste.getText().toString());
                        params.put("my_id", my_id);
                        params.put("about_member_id", about_member_id);


                        if (Integer.parseInt(second_marriage_reason_id) == -1) {
                            second_marriage_reason_id = "0";
                        }
                        params.put("second_marriage_reason_id", second_marriage_reason_id);


                        params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());

                        Log.e("params", "" + params);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
                        updateLifestyle(params);

                    }
                }

            }
        });


    }

    @SuppressLint("ResourceType")
    private boolean checkFieldsSelection(View v) {
        boolean ck = false;

/*

        if (BuildConfig.FLAVOR.equals("alfalah")) {

            if (spMyLanguage.getSelectedItemId() == 0) {
                TextView errorText = (TextView) spMyLanguage.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
                errorText.setText("Please select Language");

         */
/*   Snackbar.make(v, "Please select Education", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
*//*

                ck = true;
            }


        }
*/

        if (spMyEducation.getSelectedItemId() == 0) {
            TextView errorText = (TextView) spMyEducation.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
            errorText.setText("Please select Education");

         /*   Snackbar.make(v, "Please select Education", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
*/
            ck = true;
        }

        if (spMyEducationalField.getSelectedItemId() == 0) {
            TextView errorText = (TextView) spMyEducationalField.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
            errorText.setText("Please select Educational Field");

         /*   Snackbar.make(v, "Please select Educational Field", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
*/

            ck = true;
        }
        if (spMyOccupation.getSelectedItemId() == 0) {
            TextView errorText = (TextView) spMyOccupation.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
            errorText.setText("Please select Occupation");

         /*   Snackbar.make(v, "Please select Occupation", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

*/
            ck = true;
        }
        if (spMyAnnualIncomeLevel.getSelectedItemId() == 0) {
            TextView errorText = (TextView) spMyAnnualIncomeLevel.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
            errorText.setText("Please select Annual Income");
           /* Snackbar.make(v, "Please select Annual Income", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();*/
            ck = true;
        }
       /* if (spMyGraduationYear.getSelectedItemId() == 0) {
            TextView errorText = (TextView) spMyGraduationYear.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
            errorText.setText("Please select graduaction year");
            ck = true;
        }*/

        //last update

        if (spMyEducation.getSelectedItemId() == 0) {
            Snackbar.make(v, "Please select Education", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

            ck = true;
        } else if (spMyEducationalField.getSelectedItemId() == 0) {

            Snackbar.make(v, "Please select Educational Field", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();


            ck = true;
        } else if (spMyOccupation.getSelectedItemId() == 0) {

            Snackbar.make(v, "Please select Occupation", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();


            ck = true;
        } else if (spMyAnnualIncomeLevel.getSelectedItemId() == 0) {

            Snackbar.make(v, "Please select Annual Income", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            ck = true;
        }


        //last udpate
        else if (rgEconomy.getCheckedRadioButtonId() == -1) {
            ck = true;
            Snackbar.make(v, "Please select Economy", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } else if (rgReligious.getCheckedRadioButtonId() == -1) {
            ck = true;
            Snackbar.make(v, "Please select Religious sect", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } else if (rgEthnic.getCheckedRadioButtonId() == -1) {
            ck = true;
            Snackbar.make(v, "Please select Ethnic Background", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } else if (rgMarital.getCheckedRadioButtonId() == -1) {
            ck = true;
            Snackbar.make(v, "Please select Marital status", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } else if (rgMarital.getCheckedRadioButtonId() == 5 && rgMarriage.getCheckedRadioButtonId() == -1) {
            ck = true;
            Snackbar.make(v, "Please select Second Marriage reason", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

        } else if (rgChildren.getCheckedRadioButtonId() == -1) {
            ck = true;
            Snackbar.make(v, "Please select  Children ", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
        return ck;
    }

/*    private StringBuilder clearCommarEnd(StringBuilder stringBuilder) {

        Character ch = new Character(',');
        if (stringBuilder.length() > 0) {
            if (stringBuilder.charAt(stringBuilder.length() - 1) == ch) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
        }
        return stringBuilder;
    }*/

    private void selectFormData(Members members_obj) {
        btRemoveChildren.setVisibility(View.GONE);
        removeChildrenMyid = members_obj.get_my_id();
        //   Log.e("removeChildrenMyid", "" + removeChildrenMyid);
        if (removeChildrenMyid != 0) {

            btRemoveChildren.setVisibility(View.VISIBLE);
        } else {

            btRemoveChildren.setVisibility(View.GONE);
        }


        Log.e("get_about_type", "" + members_obj.get_about_type() + "  --   " + members_obj.get_about_type_id());

        //  if (members_obj.get_about_type() != "" || members_obj.get_about_type_id() != 0) {
        if (members_obj.get_about_type() != "" && members_obj.get_about_type() != null) {
            btRemoveSchool.setVisibility(View.VISIBLE);
        } else {
            btRemoveSchool.setVisibility(View.GONE);
        }


        ViewGenerator viewGenerator = new ViewGenerator(RegisterLifeStyleActivity1.this);
        my_id = members_obj.get_my_id();

        about_member_id = String.valueOf(members_obj.get_about_member_id());
        Log.e("eddddddddd", "" + members_obj.get_about_type_id());
        viewGenerator.selectSpinnerItemById(spMyEducation, members_obj.get_education_id(), myEducationDataList);
        viewGenerator.selectSpinnerItemById(spMyEducationalField, members_obj.get_education_field_id(), educationFieldDataList);
        viewGenerator.selectSpinnerItemById(spMyOccupation, members_obj.get_occupation_id(), myOccupationDataList);
        viewGenerator.selectSpinnerItemById(spMyGraduationYear, members_obj.get_about_type_id(), graduationYearDataList);
        viewGenerator.selectSpinnerItemById(spMyAnnualIncomeLevel, members_obj.get_income_level_id(), incomeDataList);


        Members member = SharedPreferenceManager.getUserObject(getApplicationContext());
        if (member.get_member_status() >= 2 && member.get_member_status() < 7) {
            spMyEducation.setEnabled(false);
            viewGenerator.selectCheckRadioWithDisabledRadio(rgEconomy, members_obj.get_economy_id(), llcbViewEconomy, members_obj.get_choice_economy_ids());
            viewGenerator.selectCheckRadioWithDisabledRadio(rgReligious, members_obj.get_religious_sect_id(), llcbViewReligious, members_obj.get_choice_religious_sect_ids());
            viewGenerator.selectCheckRadioWithDisabledRadio(rgEthnic, members_obj.get_ethnic_background_id(), llcbViewEthnic, members_obj.get_choice_ethnic_bground_ids());
            viewGenerator.selectCheckRadioWithDisabledRadio(rgMarital, members_obj.get_marital_status_id(), llcbViewMarital, members_obj.get_choice_marital_status_ids());
            viewGenerator.selectCheckRadioWithDisabledRadio(rgChildren, members_obj.get_children_id(), llcbViewChildren, members_obj.get_choice_children_ids());


            viewGenerator.selectCheckRadioWithDisabledRadio(rgMarriage, members_obj.getSecond_marriage_reason_id(), llcbViewMarital, members_obj.get_choice_children_ids());


        } else {
            viewGenerator.selectCheckRadio(rgEconomy, members_obj.get_economy_id(), llcbViewEconomy, members_obj.get_choice_economy_ids());

            viewGenerator.selectCheckRadio(rgReligious, members_obj.get_religious_sect_id(), llcbViewReligious, members_obj.get_choice_religious_sect_ids());

            viewGenerator.selectCheckRadio(rgEthnic, members_obj.get_ethnic_background_id(), llcbViewEthnic, members_obj.get_choice_ethnic_bground_ids());

            viewGenerator.selectCheckRadio(rgMarital, members_obj.get_marital_status_id(), llcbViewMarital, members_obj.get_choice_marital_status_ids());

            viewGenerator.selectCheckRadio(rgChildren, members_obj.get_children_id(), llcbViewChildren, members_obj.get_choice_children_ids());
            viewGenerator.selectCheckRadio(rgMarriage, members_obj.getSecond_marriage_reason_id(), llcbViewMarital, members_obj.get_choice_children_ids());


        }

        ((RadioButton) rgChildren.getChildAt(0)).setEnabled(true);

        if (members_obj.get_children_id() == 2 || members_obj.get_children_id() == 3) {

            ((RadioButton) rgChildren.getChildAt(0)).setEnabled(false);
            btRemoveChildren.setVisibility(View.VISIBLE);

            if (members_obj.get_girls_count() == 0 && members_obj.get_boys_count() == 0 && members_obj.get_min_age() == 0 && members_obj.get_max_age() == 0) {
                btRemoveChildren.setVisibility(View.GONE);
            } else {
                etNoOfGirls.setText(members_obj.get_girls_count() + "");
                etNoOfBoys.setText(+members_obj.get_boys_count() + "");
                etMinAge.setText("" + members_obj.get_min_age());
                etMaxAge.setText("" + members_obj.get_max_age());
            }


        }

        etGraduatedFrom.setText(members_obj.get_about_type());
        acMyCaste.setText(members_obj.get_caste_name());

        Log.e("caste id", "castt  " + members_obj.get_caste_id() + "  lllll");
        if (members_obj.get_caste_id() != 0) {

            Log.e("caste id", "castt  " + members_obj.get_caste_id() + "  zzzzzzzzzzz");

            //  acMyCaste.setText(members_obj.get_caste);
        }


        ///=================choice education
        {
            Log.e("choice Education", members_obj.get_choice_education_ids());
            String[] cids = members_obj.get_choice_education_ids().split(",");
            //multi choice selection

            for (int i = 0; i < cids.length; i++) {
                selectedEducationIdDataList.add((cids[i]));
            }


            if (!selectedEducationIdDataList.isEmpty()) {

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < selectedEducationIdDataList.size(); i++) {
                    int id = Integer.parseInt(selectedEducationIdDataList.get(i).toString());

                    for (int j = 0; j < educationDataList.size(); j++) {

                        if (Integer.parseInt(educationDataList.get(j).getId()) == id) {
                            educationDataList.get(j).setSelected(true);
                            stringBuilder.append(educationDataList.get(j).getName());
                            if (i != selectedEducationIdDataList.size() - 1) {
                                stringBuilder.append(",");
                            }


                        }

                    }
                }
                tvMcMyChoiceEducation.setText(stringBuilder);


            }

        }
//=-====================================================================

        {
            Log.e("choice Ocu", members_obj.get_choice_occupation_ids());
            String[] cids = members_obj.get_choice_occupation_ids().split(",");
            //multi choice selection
            selectedOccupationDataList.clear();
            for (int i = 0; i < cids.length; i++) {
                selectedOccupationIdDataList.add((cids[i]));
            }
            if (!selectedOccupationIdDataList.isEmpty()) {

                for (int i = 0; i < occupationDataList.size(); i++) {

                    for (int j = 0; j < selectedOccupationIdDataList.size(); j++) {

                        if (occupationDataList.get(i).getId().equals(selectedOccupationIdDataList.get(j))) {
                            occupationDataList.get(j).setSelected(true);

                        }

                    }

                }


                tvMcMyChoiceOccupation.setText(marryMax.getSelectedTextFromList(occupationDataList, "My Choice Occupation"));

            }

        }


    }

   /* @Override
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    //API CALLS
    private void GetLifeStyleData() {

        pDialog.show();

        Log.e("GetLifeStyleData par", "" + Urls.RegGetLifeStyle1Url + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Urls.RegGetLifeStyle1Url + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("res mainnnnnnnnnnn", response + "");
                        try {

                            JSONArray jsonArrayEducation = response.getJSONArray("education");
                            JSONArray jsonArrayEducationField = response.getJSONArray("education_field");
                            JSONArray jsonArrayCastes = response.getJSONArray("castes");
                            JSONArray jsonArrayEthnic = response.getJSONArray("ethnic");
                            JSONArray jsonArrayReligious = response.getJSONArray("religious");

                            JSONArray jsonArrayMarital = response.getJSONArray("marital");
                            JSONArray jsonArrayOccupation = response.getJSONArray("occupation");
                            JSONArray jsonArrayChildren = response.getJSONArray("children");
                            JSONArray jsonArrayIncome = response.getJSONArray("income");
                            JSONArray jsonArrayEconomy = response.getJSONArray("economy");
                            JSONArray jsonArrayMarriage = response.getJSONArray("marriage");


                            Gson gsonc;

                            GsonBuilder gsonBuilderc = new GsonBuilder();

                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();


                            educationDataList = (List<WebArd>) gsonc.fromJson(jsonArrayEducation.toString(), listType);
                            myEducationDataList = (List<WebArd>) gsonc.fromJson(jsonArrayEducation.toString(), listType);
                            educationFieldDataList = (List<WebArd>) gsonc.fromJson(jsonArrayEducationField.toString(), listType);

                            castesDataList = (List<WebArd>) gsonc.fromJson(jsonArrayCastes.toString(), listType);
                            ethnicDataList = (List<WebArd>) gsonc.fromJson(jsonArrayEthnic.toString(), listType);
                            religiousDataList = (List<WebArd>) gsonc.fromJson(jsonArrayReligious.toString(), listType);

                            martialDataList = (List<WebArd>) gsonc.fromJson(jsonArrayMarital.toString(), listType);
                            occupationDataList = (List<WebArd>) gsonc.fromJson(jsonArrayOccupation.toString(), listType);
                            myOccupationDataList = (List<WebArd>) gsonc.fromJson(jsonArrayOccupation.toString(), listType);

                            childrenDataList = (List<WebArd>) gsonc.fromJson(jsonArrayChildren.toString(), listType);
                            incomeDataList = (List<WebArd>) gsonc.fromJson(jsonArrayIncome.toString(), listType);
                            economyDataList = (List<WebArd>) gsonc.fromJson(jsonArrayEconomy.toString(), listType);
                            marriageDataList = (List<WebArd>) gsonc.fromJson(jsonArrayMarriage.toString(), listType);


                            myEducationDataList.remove(0);
                            myOccupationDataList.remove(0);


                            myEducationDataList.add(0, new WebArd("-1", "Please Select"));
                            myOccupationDataList.add(0, new WebArd("-1", "Please Select"));
                            incomeDataList.add(0, new WebArd("-1", "Please Select"));
                            educationFieldDataList.add(0, new WebArd("-1", "Please Select"));


                            spAdapterMyAnnualIncome.updateDataList(incomeDataList);

                            spAdapterMyEducation.updateDataList(myEducationDataList);
                            spAdapterMyEducationalField.updateDataList(educationFieldDataList);
                            spAdapterMyOccupation.updateDataList(myOccupationDataList);


                            ArrayList mcList = new ArrayList();
                            for (WebArd value : castesDataList) {
                                mcList.add(value.getName());

                            }

                            acAdapter = new ArrayAdapter<String>(RegisterLifeStyleActivity1.this, android.R.layout.simple_list_item_1, mcList);

                            acMyCaste.setAdapter(acAdapter);


                            //=================================parsing object
                            Gson gson;
                            JSONArray jsonGrography = response.getJSONArray("lifestyle1");
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            //  0001-01-01T00:00:00
                            //  gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                            //  gsonBuilder.setDateFormat("yyyy-dd-M hh:mm a");
                            //  12/1/2016 6:30:00 AM

                            gson = gsonBuilder.create();
                            Log.e("Aliaaaaaaaasss", jsonGrography.get(0).toString());
                            members_obj = gson.fromJson(jsonGrography.get(0).toString(), Members.class);
                            Log.e("Aliaaaaaaaasss", members_obj.get_country_id() + "");


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }

                        //==============DATA SETTING==============================================

                        ViewGenerator viewGenerator = new ViewGenerator(RegisterLifeStyleActivity1.this);

                        if (members_obj.get_education_id() == 0) {
                            updateData = false;

                        } else {
                            updateData = true;

                        }

                        if (updateData) {


                            viewGenerator.addDynamicCheckRdioButtons(economyDataList, rgEconomy, llcbViewEconomy);
                            viewGenerator.addDynamicCheckRdioButtons(religiousDataList, rgReligious, llcbViewReligious);
                            viewGenerator.addDynamicCheckRdioButtons(ethnicDataList, rgEthnic, llcbViewEthnic);
                            viewGenerator.addDynamicCheckRdioButtons(martialDataList, rgMarital, llcbViewMarital);
                            viewGenerator.addDynamicCheckRdioButtons(childrenDataList, rgChildren, llcbViewChildren);
                            viewGenerator.addDynamicCheckRdioButtons(marriageDataList, rgMarriage, llcbViewMarriage);

                            selectFormData(members_obj);

                        } else {
                            viewGenerator.addDynamicCheckRdioButtons(economyDataList, rgEconomy, llcbViewEconomy);
                            viewGenerator.addDynamicCheckRdioButtons(religiousDataList, rgReligious, llcbViewReligious);
                            viewGenerator.addDynamicCheckRdioButtons(ethnicDataList, rgEthnic, llcbViewEthnic);
                            viewGenerator.addDynamicCheckRdioButtons(martialDataList, rgMarital, llcbViewMarital);
                            viewGenerator.addDynamicCheckRdioButtons(childrenDataList, rgChildren, llcbViewChildren);
                            viewGenerator.addDynamicCheckRdioButtons(marriageDataList, rgMarriage, llcbViewMarriage);
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




/*        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);

    }

    private void updateLifestyle(JSONObject params) {
        pDialog.show();
        Log.e("updateLifestyle", Urls.updateLifestyleUrl + "=======" + params.toString() + "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.updateLifestyleUrl, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update lifestyle", response + "");

                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {


                                if (!marryMax.getUpdateCheck(getApplicationContext())) {
                                    Intent in = new Intent(RegisterLifeStyleActivity1.this, RegisterLifeStyleActivity2.class);
                                    startActivity(in);
                                    finish();

                                } else {

                                    Toast.makeText(RegisterLifeStyleActivity1.this, "Updated", Toast.LENGTH_SHORT).show();
                                   /* Intent in = new Intent(RegisterLifeStyleActivity1.this, RegisterLifeStyleActivity2.class);
                                    startActivity(in);
                                    finish();*/
                                }

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

    private void removeSchool(JSONObject params) {
        pDialog.show();
        Log.e("updateLifestyle", Urls.removeSchool + "=======" + params.toString() + "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.removeSchool, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update lifestyle", response + "");

                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {

                                Toast.makeText(RegisterLifeStyleActivity1.this, "Removed", Toast.LENGTH_SHORT).show();
                                GetLifeStyleData();

                            } else {
                                Toast.makeText(RegisterLifeStyleActivity1.this, "Error", Toast.LENGTH_SHORT).show();


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

    private void removeChildren(JSONObject params) {
        pDialog.show();
        Log.e("removeChildren", Urls.removeChildren + "=======" + params.toString() + "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.removeChildren, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update lifestyle", response + "");

                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {

                                // Toast.makeText(RegisterLifeStyleActivity1.this, "Removed", Toast.LENGTH_SHORT).show();
                                //  GetLifeStyleData();
                                etNoOfGirls.setText("");
                                etNoOfBoys.setText("");
                                etMinAge.setText("");
                                etMaxAge.setText("");
                                btRemoveChildren.setVisibility(View.GONE);
                                ((RadioButton) rgChildren.getChildAt(0)).setEnabled(true);

                            } else {
                                Toast.makeText(RegisterLifeStyleActivity1.this, "Error", Toast.LENGTH_SHORT).show();


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


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }


    @Override
    public void onMultiChoiceSave(List s, int which) {


        if (which == 1) {

            educationDataList.clear();
            educationDataList.addAll(s);


            MarryMax max = new MarryMax(null);

            tvMcMyChoiceEducation.setText(max.getSelectedTextFromList(educationDataList, "My Choice Education"));
            // Log.e("selected id is", max.getSelectedIdsFromList(educationDataList) + "");
        } else if (which == 2) {

            occupationDataList.clear();
            occupationDataList.addAll(s);


            MarryMax max = new MarryMax(null);

            tvMcMyChoiceOccupation.setText(max.getSelectedTextFromList(occupationDataList, "My Choice Occupation"));
            //  Log.e("selected id is", max.getSelectedIdsFromList(occupationDataList) + "");
        }

    }


}
