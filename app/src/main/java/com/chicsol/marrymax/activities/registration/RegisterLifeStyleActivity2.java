package com.chicsol.marrymax.activities.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.MySpinnerAdapter;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebArd;
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
import java.util.List;
import java.util.Map;

public class RegisterLifeStyleActivity2 extends BaseRegistrationActivity {
    private Button bt_register_free, bt_back;
    private LinearLayout llcbViewFamilyValues, llcbViewLivingArrangements, llcbViewRaisedWhere, llcbViewHijab, llcbViewSmoke, llcbViewDrink;
    private RadioGroup rgFamilyValues, rgLivingArrangements, rgRaisedWhere, rgHijab, rgSmoke, rgDrink;

    private List<WebArd> familyValuesDataList, livingArrangementsDataList, raisedWhereDataList, hijabDataList, smokeDataList, drinkDataList, siblingDataList;

    private Members members_obj;
    private Spinner spMySiblingPosition;
    private MySpinnerAdapter spAdapterMySiblingPosition;
    private boolean updateData = true;
    private EditText etNoOfSisters, etNoOfBrothers;
    private ProgressDialog pDialog;


    private FloatingActionButton fabLifeStyle1, fabLifeStyle2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_lifestyle_step2);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bt_back = (Button) findViewById(R.id.buttonBack);


        initialize();

        GetLifeStyleData();
        setListeners();


    }

    private void initialize() {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");

        rgFamilyValues = (RadioGroup) findViewById(R.id.RadioGroupFamilyValues);
        llcbViewFamilyValues = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceFamilyValues);

        rgLivingArrangements = (RadioGroup) findViewById(R.id.RadioGroupLiving);
        llcbViewLivingArrangements = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceLiving);

        rgRaisedWhere = (RadioGroup) findViewById(R.id.RadioGroupRaisedWhere);
        llcbViewRaisedWhere = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceRaisedWhere);

        rgHijab = (RadioGroup) findViewById(R.id.RadioGroupHijab);
        llcbViewHijab = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceHijab);


        rgSmoke = (RadioGroup) findViewById(R.id.RadioGroupSmoke);
        llcbViewSmoke = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceSmoke);

        rgDrink = (RadioGroup) findViewById(R.id.RadioGroupDrink);
        llcbViewDrink = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceDrink);

        fabLifeStyle1 = (FloatingActionButton) findViewById(R.id.fabLifeStyle1);
        fabLifeStyle2 = (FloatingActionButton) findViewById(R.id.fabLifeStyle2);

        familyValuesDataList = new ArrayList<>();
        livingArrangementsDataList = new ArrayList<>();

        raisedWhereDataList = new ArrayList<>();
        drinkDataList = new ArrayList<>();


        hijabDataList = new ArrayList<>();
        smokeDataList = new ArrayList<>();
        drinkDataList = new ArrayList<>();


        spMySiblingPosition = (Spinner) findViewById(R.id.SpinnerSiblingPosition);

        spAdapterMySiblingPosition = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, familyValuesDataList);
        spAdapterMySiblingPosition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMySiblingPosition.setAdapter(spAdapterMySiblingPosition);

        etNoOfBrothers = (EditText) findViewById(R.id.EditTextnumberOfBrothers);
        etNoOfSisters = (EditText) findViewById(R.id.EditTextnumberOfSisters);

        bt_register_free = (Button) findViewById(R.id.buttonContinueStep2);

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

    private void setListeners() {
        fabLifeStyle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class cls = RegisterLifeStyleActivity1.class;
                marryMax.getProfileProgress(cls, RegisterLifeStyleActivity2.this, getApplicationContext(), SharedPreferenceManager.getUserObject(getApplicationContext()));
            }
        });
        fabLifeStyle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class cls = RegisterLifeStyleActivity2.class;
                marryMax.getProfileProgress(cls, RegisterLifeStyleActivity2.this, getApplicationContext(), SharedPreferenceManager.getUserObject(getApplicationContext()));
            }
        });

        bt_register_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkFieldsSelection(v)) {

                    ViewGenerator vg = new ViewGenerator(RegisterLifeStyleActivity2.this);
                    WebArd mMySibObj = (WebArd) spMySiblingPosition.getSelectedItem();
                    String sibling_id = mMySibObj.getId();


                    String brothers_count = etNoOfBrothers.getText().toString();
                    String sisters_count = etNoOfSisters.getText().toString();


                    String family_values_id = String.valueOf(rgFamilyValues.getCheckedRadioButtonId());
                    String choice_family_values_ids = vg.getSelectionFromCheckbox(llcbViewFamilyValues);

                    String living_arrangement_id = String.valueOf(rgLivingArrangements.getCheckedRadioButtonId());
                    String choice_living_arangment_ids = vg.getSelectionFromCheckbox(llcbViewLivingArrangements);

                    String raised_id = String.valueOf(rgRaisedWhere.getCheckedRadioButtonId());
                    String choice_raised_ids = vg.getSelectionFromCheckbox(llcbViewRaisedWhere);

                    String hijab_id = String.valueOf(rgHijab.getCheckedRadioButtonId());
                    String choice_hijab_ids = vg.getSelectionFromCheckbox(llcbViewHijab);

                    String drink_id = String.valueOf(rgDrink.getCheckedRadioButtonId());
                    String choice_drink_ids = vg.getSelectionFromCheckbox(llcbViewDrink);

                    String smoking_id = String.valueOf(rgSmoke.getCheckedRadioButtonId());
                    String choice_smoking_ids = vg.getSelectionFromCheckbox(llcbViewSmoke);


                    JSONObject params = new JSONObject();
                    try {


                        params.put("sibling_id", sibling_id);

                        if (TextUtils.isEmpty(brothers_count)) {
                            brothers_count = "0";
                        }
                        if (TextUtils.isEmpty(sisters_count)) {
                            sisters_count = "0";
                        }
                        params.put("brothers_count", brothers_count);

                        params.put("sisters_count", sisters_count);

                        params.put("family_values_id", family_values_id);
                        params.put("choice_family_values_ids", choice_family_values_ids);

                        params.put("living_arrangement_id", living_arrangement_id);
                        params.put("choice_living_arangment_ids", choice_living_arangment_ids);

                        params.put("raised_id", raised_id);
                        params.put("choice_raised_ids", choice_raised_ids);

                        params.put("hijab_id", hijab_id);
                        params.put("choice_hijab_ids", choice_hijab_ids);

                        params.put("drink_id", drink_id);
                        params.put("choice_drink_ids", choice_drink_ids);


                        params.put("smoking_id", smoking_id);
                        params.put("choice_smoking_ids", choice_smoking_ids);


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


        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in = new Intent(RegisterLifeStyleActivity2.this, RegisterLifeStyleActivity1.class);
                startActivity(in);
                finish();
            }
        });

    }


    private void selectFormData(Members members_obj) {

        ViewGenerator viewGenerator = new ViewGenerator(RegisterLifeStyleActivity2.this);

        //Log.e("eddddddddd", "" + members_obj.get_about_type_id());


        viewGenerator.selectSpinnerItemById(spMySiblingPosition, members_obj.get_sibling_id(), siblingDataList);

        viewGenerator.selectCheckRadio(rgFamilyValues, members_obj.get_family_values_id(), llcbViewFamilyValues, members_obj.get_choice_family_values_ids());
        viewGenerator.selectCheckRadio(rgLivingArrangements, members_obj.get_living_arrangement_id(), llcbViewLivingArrangements, members_obj.get_choice_living_arangment_ids());
        viewGenerator.selectCheckRadio(rgRaisedWhere, members_obj.get_raised_id(), llcbViewRaisedWhere, members_obj.get_choice_raised_ids());
        viewGenerator.selectCheckRadio(rgHijab, members_obj.get_hijab_id(), llcbViewHijab, members_obj.get_choice_hijab_ids());


        Members member = SharedPreferenceManager.getUserObject(getApplicationContext());
        if (member.get_member_status() >= 2 && member.get_member_status() < 7) {
            /*  if (member.get_member_status() == 3 || member.get_member_status() == 4) {*/


            viewGenerator.selectCheckRadioWithDisabledRadio(rgSmoke, members_obj.get_smoking_id(), llcbViewSmoke, members_obj.get_choice_smoking_ids());
            viewGenerator.selectCheckRadioWithDisabledRadio(rgDrink, members_obj.get_drink_id(), llcbViewDrink, members_obj.get_choice_drink_ids());


        } else {

            viewGenerator.selectCheckRadio(rgSmoke, members_obj.get_smoking_id(), llcbViewSmoke, members_obj.get_choice_smoking_ids());
            viewGenerator.selectCheckRadio(rgDrink, members_obj.get_drink_id(), llcbViewDrink, members_obj.get_choice_drink_ids());


        }

        etNoOfBrothers.setText(members_obj.get_brothers_count() + "");
        etNoOfSisters.setText(members_obj.get_sisters_count() + "");


    }

    private boolean checkFieldsSelection(View v) {
        boolean ck = false;

        if (spMySiblingPosition.getSelectedItemId() == 0) {
            TextView errorText = (TextView) spMySiblingPosition.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
            errorText.setText("Please select ");
            Snackbar.make(v, "Please select Sibling Position", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            ck = true;
        } else if (rgFamilyValues.getCheckedRadioButtonId() == -1) {
            Snackbar.make(v, "Please select Family Values", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            ck = true;
        } else if (rgLivingArrangements.getCheckedRadioButtonId() == -1) {
            Snackbar.make(v, "Please select Living Arrangements", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            ck = true;
        } else if (rgRaisedWhere.getCheckedRadioButtonId() == -1) {
            Snackbar.make(v, "Please select Raised Where", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            ck = true;
        } else if (rgHijab.getCheckedRadioButtonId() == -1) {
            Snackbar.make(v, "Please select  Hijab", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            ck = true;
        } else if (rgSmoke.getCheckedRadioButtonId() == -1) {
            Snackbar.make(v, "Please select Smoke", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            ck = true;
        } else if (rgDrink.getCheckedRadioButtonId() == -1) {
            Snackbar.make(v, "Please select Drink", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            ck = true;
        }
        return ck;
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


    private void GetLifeStyleData() {

        pDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Urls.RegGetLifeStyle1Url2 + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("res mainnnnnnnnnnn", response + "");
                        try {

                            JSONArray jsonArrayFamily = response.getJSONArray("family");
                            JSONArray jsonArrayLiving = response.getJSONArray("living");
                            JSONArray jsonArrayHijab = response.getJSONArray("hijab");
                            JSONArray jsonArrayRaised = response.getJSONArray("raised");
                            JSONArray jsonArraySmoke = response.getJSONArray("smoking");
                            JSONArray jsonArrayDrink = response.getJSONArray("drink");
                            JSONArray jsonArraySibling = response.getJSONArray("sibling");

                            Gson gsonc;

                            GsonBuilder gsonBuilderc = new GsonBuilder();

                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();


                            familyValuesDataList = (List<WebArd>) gsonc.fromJson(jsonArrayFamily.toString(), listType);
                            livingArrangementsDataList = (List<WebArd>) gsonc.fromJson(jsonArrayLiving.toString(), listType);
                            hijabDataList = (List<WebArd>) gsonc.fromJson(jsonArrayHijab.toString(), listType);

                            raisedWhereDataList = (List<WebArd>) gsonc.fromJson(jsonArrayRaised.toString(), listType);

                            smokeDataList = (List<WebArd>) gsonc.fromJson(jsonArraySmoke.toString(), listType);
                            drinkDataList = (List<WebArd>) gsonc.fromJson(jsonArrayDrink.toString(), listType);

                            siblingDataList = (List<WebArd>) gsonc.fromJson(jsonArraySibling.toString(), listType);
                            siblingDataList.add(0, new WebArd("-1", "Please select"));

                            spAdapterMySiblingPosition.updateDataList(siblingDataList);

                            //=================================parsing object
                            Gson gson;
                            JSONArray jsonGrography = response.getJSONArray("lifestyle2");
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            Log.e("Aliaaaaaaaasss", jsonGrography.get(0).toString());
                            members_obj = gson.fromJson(jsonGrography.get(0).toString(), Members.class);
                            Log.e("Aliaaaaaaaasss", members_obj.get_country_id() + "");


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }

                        //==============DATA SETTING==============================================

                        ViewGenerator viewGenerator = new ViewGenerator(RegisterLifeStyleActivity2.this);

                        if (members_obj.get_family_values_id() == 0) {
                            updateData = false;

                        } else {
                            updateData = true;

                        }

                        if (updateData) {
                            viewGenerator.addDynamicCheckRdioButtons(familyValuesDataList, rgFamilyValues, llcbViewFamilyValues);
                            viewGenerator.addDynamicCheckRdioButtons(livingArrangementsDataList, rgLivingArrangements, llcbViewLivingArrangements);
                            viewGenerator.addDynamicCheckRdioButtons(raisedWhereDataList, rgRaisedWhere, llcbViewRaisedWhere);
                            viewGenerator.addDynamicCheckRdioButtons(hijabDataList, rgHijab, llcbViewHijab);
                            viewGenerator.addDynamicCheckRdioButtons(smokeDataList, rgSmoke, llcbViewSmoke);
                            viewGenerator.addDynamicCheckRdioButtons(drinkDataList, rgDrink, llcbViewDrink);

                            selectFormData(members_obj);

                        } else {
                            viewGenerator.addDynamicCheckRdioButtons(familyValuesDataList, rgFamilyValues, llcbViewFamilyValues);
                            viewGenerator.addDynamicCheckRdioButtons(livingArrangementsDataList, rgLivingArrangements, llcbViewLivingArrangements);
                            viewGenerator.addDynamicCheckRdioButtons(raisedWhereDataList, rgRaisedWhere, llcbViewRaisedWhere);
                            viewGenerator.addDynamicCheckRdioButtons(hijabDataList, rgHijab, llcbViewHijab);
                            viewGenerator.addDynamicCheckRdioButtons(smokeDataList, rgSmoke, llcbViewSmoke);
                            viewGenerator.addDynamicCheckRdioButtons(drinkDataList, rgDrink, llcbViewDrink);
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


        Log.e("Params", "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.updateLifestyleUrl2, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {

                                //updating status
                                Members member = SharedPreferenceManager.getUserObject(getApplication());


                                if (member.get_member_status() == 0) {
                                    member.set_member_status(1);
                                    SharedPreferenceManager.setUserObject(getApplicationContext(), member);
                                }


                                if (!marryMax.getUpdateCheck(getApplicationContext())) {
                                    Intent in = new Intent(RegisterLifeStyleActivity2.this, RegisterInterest.class);
                                    startActivity(in);
                                    finish();

                                } else {

                                    Toast.makeText(RegisterLifeStyleActivity2.this, "Updated", Toast.LENGTH_SHORT).show();
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


}
