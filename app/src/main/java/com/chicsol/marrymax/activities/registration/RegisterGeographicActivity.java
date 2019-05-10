package com.chicsol.marrymax.activities.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.chicsol.marrymax.BuildConfig;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.MySpinnerAdapter;
import com.chicsol.marrymax.dialogs.dialogGeoInfo;
import com.chicsol.marrymax.dialogs.dialogMultiChoice;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.modal.cModel;
import com.chicsol.marrymax.other.MarryMax;
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

/**
 * Created by Redz on 10/18/2016.
 */

public class RegisterGeographicActivity extends BaseRegistrationActivity implements dialogMultiChoice.onMultiChoiceSaveListener {

    private Spinner spMyCountry, spCountryOrigin, spMyCountryState, spMyCountryCity;
    private List<cModel> MyCountryDataList2;
    private List<WebArd> MyCountryDataList, MyCountryStateDataList, MyCountryCityDataList, MyChoiceCountryDataList, MyChoiceOriginCountryDataList, VisaDataList;
    private MySpinnerAdapter adapter_myCountry, adapter_country_origin, adapter_myCountryStates, adapter_myCountryCity, adapter_myChoiceCountry;
    private LinearLayout llCheckboxView;
    private RadioGroup radioGroup;
    private String SelectedCountry, SelectedState;

    private mTextView tvSpMultiChoice, tvSpMultiChoiceOrigin;
    private Button bt_register_free;
    // seletedCountriesDataList
    private ArrayList seletedCountriesDataListTemp, seletedCountriesIdDataList;
    private boolean updateData = false;
    private AdapterView.OnItemSelectedListener spMyCountryListener, spMyCountryStateListener, spMyCountryCityListener;
    private AdapterView.OnItemSelectedListener statesListener, countriesListener;
    private ProgressDialog pDialog;

    private String countryCode = null;
    //pr

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_geographic);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();
        //api calls
        GetGeographyData();
        //  UpdateGeoGraphy();
        setListeners();


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);


        outState.putBoolean("updateData", updateData);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        updateData = savedInstanceState.getBoolean("updateData");
    }

    public void showLog(String logtext) {
        //Log.e("MarryM", "" + logtext);
    }

    private void initialize() {


        pDialog = new ProgressDialog(RegisterGeographicActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);


        llCheckboxView = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceVisa);
        //Lists
        MyCountryDataList = new ArrayList<>();
        MyCountryStateDataList = new ArrayList<>();
        MyCountryCityDataList = new ArrayList<>();
        MyChoiceCountryDataList = new ArrayList<>();
        MyChoiceOriginCountryDataList = new ArrayList<>();

        VisaDataList = new ArrayList<>();
        //seletedCountriesDataList = new ArrayList();
        //seletedCountriesDataListTemp = new ArrayList();
        seletedCountriesIdDataList = new ArrayList();
        //spinners
        spMyCountry = (Spinner) findViewById(R.id.spinnerMyCountryg);


        spMyCountryState = (Spinner) findViewById(R.id.spinnerMyCountryStateg);
        spMyCountryCity = (Spinner) findViewById(R.id.spinnerMyCountryCityg);


        //MyCountry============================

        adapter_myCountry = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, MyCountryDataList);
        adapter_myCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyCountry.setAdapter(adapter_myCountry);

        //2=========
        adapter_myCountryStates = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, MyCountryStateDataList);
        adapter_myCountryStates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyCountryState.setAdapter(adapter_myCountryStates);
        //3=========
        adapter_myCountryCity = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, MyCountryCityDataList);
        adapter_myCountryCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyCountryCity.setAdapter(adapter_myCountryCity);
        //================MyChoice Spinner


        radioGroup = (RadioGroup) findViewById(R.id.RadioGroupdGeogrpahic);


        //button==========
        bt_register_free = (Button) findViewById(R.id.buttonContinueStep2);

        tvSpMultiChoice = (mTextView) findViewById(R.id.MutliChoiceButton);

        // fabGeographic.getBackgroundTintMode().get
        fabGeographic.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepOnGoing));
        tvGeogrphic.setTextColor(getResources().getColor(R.color.colorRegistrationStepOnGoing));

        if (marryMax.getUpdateCheck(getApplicationContext())) {

            bt_register_free.setText("Update");
        }


        if (BuildConfig.FLAVOR.equals("alfalah")) {


            tvSpMultiChoiceOrigin = (mTextView) findViewById(R.id.MutliChoiceOriginCountryButton);
            //spinners
            spCountryOrigin = (Spinner) findViewById(R.id.spinnerOriginCountry);

            adapter_country_origin = new MySpinnerAdapter(this,
                    android.R.layout.simple_spinner_item, MyCountryDataList);
            adapter_country_origin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCountryOrigin.setAdapter(adapter_country_origin);

        }


    }


    public void setListeners() {

        countriesListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                WebArd ard = (WebArd) spMyCountry.getSelectedItem();
                if (position != -1 && position != 0) {
                    cModel c = MyCountryDataList2.get(position - 1);
                    //   Log.e("country data code", "" + c.getCode());


                    if (SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() == 0 || SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() == 1) {

                        if (countryCode == null) {
                            putRequestCountryCode(c.getCode());
                        } else {
                            if (!c.getCode().equals(countryCode)) {
                                dialogGeoInfo newFragment = dialogGeoInfo.newInstance("");
                                newFragment.show(getSupportFragmentManager(), "dialog");
                            }

                        }
                    }

                }
                if (Integer.parseInt(ard.getId()) != 0 && updateData != true) {
                    MyCountryStateDataList.clear();
                    MyCountryCityDataList.clear();
                    adapter_myCountryStates.clear();
                    adapter_myCountryCity.clear();


                    getStates(ard.getId());


                } else {

                    //   Toast.makeText(RegisterGeographicActivity.this, "Please Select Country First", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };


        statesListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WebArd ardState = (WebArd) spMyCountryState.getSelectedItem();
                WebArd ardCountry = (WebArd) spMyCountry.getSelectedItem();

             //   Log.e("city state ", Integer.parseInt(ardState.getId()) + "  ----- " + Integer.parseInt(ardCountry.getId()) + "  " + updateData);

                //&& updateData != true
                if (Integer.parseInt(ardState.getId()) != 0 && Integer.parseInt(ardCountry.getId()) != 0) {
                    getStateCities(ardCountry.getId(), ardState.getId());

                } else {
                    //   Toast.makeText(RegisterGeographicActivity.this, "Please Select Country & State First", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };


        spMyCountry.setOnItemSelectedListener(countriesListener);
        spMyCountryState.setOnItemSelectedListener(statesListener);


        tvSpMultiChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                dialogMultiChoice newFragment = dialogMultiChoice.newInstance(gson.toJson(MyChoiceCountryDataList), 1, "My Choice Countries");
                newFragment.show(getSupportFragmentManager(), "dialog");
            }
        });


        bt_register_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validation = checkSelections(v);


             //   Log.e("validation", "" + validation);

                if (!validation) {


                    WebArd mCountryObj = (WebArd) spMyCountry.getSelectedItem();
                    String country_id = mCountryObj.getId();

                    String state_id = "";

                    WebArd mStateObj = (WebArd) spMyCountryState.getSelectedItem();
                    if (mStateObj != null) {
                        state_id = mStateObj.getId();
                    }


                    WebArd mCountryCityObj = (WebArd) spMyCountryCity.getSelectedItem();
                    String city_id = "";
                    if (mCountryCityObj != null) {
                        city_id = mCountryCityObj.getId();
                    }

                    //Selected Countries
                    StringBuilder sbSelectedCountries = new StringBuilder();


                    MarryMax max = new MarryMax(null);

                    sbSelectedCountries = max.getSelectedIdsFromList(MyChoiceCountryDataList);


                    showLog("selected country ids :  " + sbSelectedCountries.toString() + "");


                    //================== Visa Reidency my choice


                    String visa_status_id = String.valueOf(radioGroup.getCheckedRadioButtonId());
                    // showLog("Radio seleciton  " + radioButtonID);

                    //===============CheckBoxes


                    ViewGenerator viewGenerator = new ViewGenerator(getApplicationContext());
                    String sbSelectedVisaMyChoice = viewGenerator.getSelectionFromCheckbox(llCheckboxView);
                    if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
                        UpdateGeoGraphy(country_id, state_id, city_id, visa_status_id, sbSelectedCountries.toString(), sbSelectedVisaMyChoice.toString());
                    }
                }
            }
        });

        if (BuildConfig.FLAVOR.equals("alfalah")) {

            tvSpMultiChoiceOrigin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson = new Gson();
                    dialogMultiChoice newFragment = dialogMultiChoice.newInstance(gson.toJson(MyChoiceOriginCountryDataList), 2, "Select Origin Country of your Choice");
                    newFragment.show(getSupportFragmentManager(), "dialog");
                }
            });

        }
    }

    private boolean checkSelections(View v) {
        boolean ck = false;
        // tvSpMultiChoice.setError(null);

        if (spMyCountry.getSelectedItemId() == 0) {
            TextView errorText = (TextView) spMyCountry.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
            errorText.setText("Please select Country");
            ck = true;
        } else if (spMyCountryState.getSelectedItemId() == 0) {
            TextView errorText = (TextView) spMyCountryState.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
            errorText.setText("Please select State");
            ck = true;
        } else if (spMyCountryCity.getSelectedItemId() == 0) {
            TextView errorText = (TextView) spMyCountryCity.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
            errorText.setText("Please select City");
            ck = true;
        }
       /* if (tvSpMultiChoice.getText().toString().equals(getResources().getString(R.string.tv_name_reg_geogrphic))) {
            Log.e("same", tvSpMultiChoice.getText().toString());
            tvSpMultiChoice.setError("Please Select My Choice Countries");
            tvSpMultiChoice.requestFocus();
            ck = true;

        }*/
        else if (radioGroup.getCheckedRadioButtonId() == -1) {
            //  Toast.makeText(this, "Please select visa type", Toast.LENGTH_SHORT).show();

            Snackbar.make(v, "Please select Visa Type", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            ck = true;
        }

        //work      here


        return ck;
    }

    private void selectFormData(Members members_obj) {

        spMyCountry.setOnItemSelectedListener(null);
        spMyCountryState.setOnItemSelectedListener(null);

        selectSpinnerItemByValue(spMyCountry, members_obj.getCountry_id(), MyCountryDataList);
        //disable country


        Members member = SharedPreferenceManager.getUserObject(getApplicationContext());
        //  Log.e("status isss  ",member.getMember_status()+"");
        if (member.getMember_status() > 1 && member.getMember_status() < 5) {

            spMyCountry.setEnabled(false);
        }

        selectSpinnerItemByValue(spMyCountryState, members_obj.getState_id(), MyCountryStateDataList);
        selectSpinnerItemByValue(spMyCountryCity, members_obj.getCity_id(), MyCountryCityDataList);
      //  Log.e("choice countrr", members_obj.getChoice_country_ids());
        String[] cids = members_obj.getChoice_country_ids().split(",");


    //    Log.e("c idddddssssss", "" + cids.length);

        //multi choice selection
        seletedCountriesIdDataList.clear();
        for (int i = 0; i < cids.length; i++) {
            seletedCountriesIdDataList.add((cids[i]));
        }
     //   Log.e("c idddddssssss", "" + seletedCountriesIdDataList.size());


        if (!seletedCountriesIdDataList.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();

            if (seletedCountriesIdDataList.size() == 0) {

                tvSpMultiChoice.setText(R.string.tv_name_reg_geogrphic);
            } else {


                for (int i = 0; i < seletedCountriesIdDataList.size(); i++) {
                    int id = Integer.parseInt(seletedCountriesIdDataList.get(i).toString());

                    for (int j = 0; j < MyChoiceCountryDataList.size(); j++) {

                        if (Integer.parseInt(MyChoiceCountryDataList.get(j).getId()) == id) {
                            MyChoiceCountryDataList.get(j).setSelected(true);


                        }

                    }
                }


            }
            MarryMax max = new MarryMax(null);

            tvSpMultiChoice.setText(max.getSelectedTextFromList(MyChoiceCountryDataList, "My Choice Countries"));

        }
        //=======end

        radioGroup.check((int) members_obj.getVisa_status_id());
//==============checkbox

        //Log.e("visa", members_obj.getChoice_visa_status_ids());


        String[] visa_status_check_ids = members_obj.getChoice_visa_status_ids().split(",");
        if (visa_status_check_ids.length > 0) {
            int childcount = llCheckboxView.getChildCount();
            for (int i = 0; i < childcount; i++) {

                View sv = llCheckboxView.getChildAt(i);
                if (sv instanceof CheckBox) {
                    int id = (((CheckBox) sv).getId());
                    for (int j = 0; j < visa_status_check_ids.length; j++) {

                        if (id == Integer.parseInt(visa_status_check_ids[j])) {
                            ((CheckBox) sv).setChecked(true);

                        }


                    }

                }
            }

        }


        pDialog.show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                spMyCountry.setOnItemSelectedListener(countriesListener);
                spMyCountryState.setOnItemSelectedListener(statesListener);
                updateData = false;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (RegisterGeographicActivity.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17
                        return;
                    }
                }

                if (pDialog != null) {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }


            }
        }, 2000);


    }


    private void GetGeographyData() {

    //    Log.e("RegisterGeography", "" + Urls.RegisterGeography + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

        pDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                Urls.RegisterGeography + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                   //     Log.e("res mainnnnnnnnnnn", response + "");
                        try {


                            JSONArray jsonCountryObj = response.getJSONArray("country");
                            JSONArray jsonCountryStaeObj = response.getJSONArray("state");
                            JSONArray jsonCountryCityObj = response.getJSONArray("city");
                            JSONArray jsonVisaObj = response.getJSONArray("visa");

                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();
                            Type listTypeCountry = new TypeToken<List<cModel>>() {
                            }.getType();

                            MyCountryDataList = (List<WebArd>) gsonc.fromJson(jsonCountryObj.toString(), listType);
                            MyCountryDataList2 = (List<cModel>) gsonc.fromJson(jsonCountryObj.toString(), listTypeCountry);
                            MyCountryStateDataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                            MyCountryCityDataList = (List<WebArd>) gsonc.fromJson(jsonCountryCityObj.toString(), listType);
                            VisaDataList = (List<WebArd>) gsonc.fromJson(jsonVisaObj.toString(), listType);
                            MyChoiceCountryDataList.addAll(MyCountryDataList);
                            MyChoiceCountryDataList.add(0, new WebArd("-1", "Any"));

                            MyChoiceOriginCountryDataList.addAll(MyCountryDataList);
                            MyChoiceOriginCountryDataList.add(0, new WebArd("-1", "Any"));

                            MyCountryDataList.add(0, new WebArd("-1", "Please Select"));
                            MyCountryStateDataList.add(0, new WebArd("-1", "Please Select"));
                            MyCountryCityDataList.add(0, new WebArd("-1", "Please Select"));

                            adapter_myCountry.updateDataList(MyCountryDataList);

                            if (BuildConfig.FLAVOR.equals("alfalah")) {
                                adapter_country_origin.updateDataList(MyCountryDataList);

                            }


                            adapter_myCountryStates.updateDataList(MyCountryStateDataList);
                            adapter_myCountryCity.updateDataList(MyCountryCityDataList);


                            //=================================parsing main object
                            Gson gson;
                            JSONArray jsonGrography = response.getJSONArray("geogrophy");
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gson = gsonBuilder.create();
                            Members members_obj = gson.fromJson(jsonGrography.get(0).toString(), Members.class);

                            //   Log.e("member   "+MyCountryDataList.get(0).getId(),""+members_obj.getCountry_id()+" ");

                            if (members_obj.getCountry_id() == 0) {
                                updateData = false;

                            } else {
                                updateData = true;

                            }


                            // adding checkboxes and radios
                            ViewGenerator viewGenerator = new ViewGenerator(RegisterGeographicActivity.this);
                            viewGenerator.addDynamicCheckRdioButtons(VisaDataList, radioGroup, llCheckboxView);
                            //close

                            if (updateData) {

                                if (MyCountryCityDataList.size() == 1) {
                                    MyCountryCityDataList.clear();
                                    adapter_myCountryCity.updateDataList(MyCountryCityDataList);
                                }

                                //    spMyCountry.setOnItemClickListener(null);
                                //     spMyCountryState.setOnItemClickListener(null);
                                //    spMyCountryCity.setOnItemClickListener(null);

                                selectFormData(members_obj);

                            } else {
                                spMyCountry.setSelection(0);

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




/*        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);

    }


    public void selectSpinnerItemByValue(Spinner spnr, long value, List<WebArd> mList) {
        MySpinnerAdapter adapter = (MySpinnerAdapter) spnr.getAdapter();
        for (int position = 0; position < mList.size(); position++) {
            if (Long.parseLong(mList.get(position).getId()) == value) {
                spnr.setSelection(position);
                return;
            }
        }
    }

    private void getStates(final String country_id) {


        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Urls.getStatesUrl + country_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                     //   Log.e("Response states", response.toString());

                        try {


                            if (response.length() != 0) {
                                JSONArray jsonCountryStaeObj = response.getJSONArray(0);
                                MyCountryStateDataList.clear();

                                Gson gsonc;
                                GsonBuilder gsonBuilderc = new GsonBuilder();
                                gsonc = gsonBuilderc.create();
                                Type listType = new TypeToken<List<WebArd>>() {
                                }.getType();


                                MyCountryStateDataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);


                                MyCountryStateDataList.add(0, new WebArd("-1", "Please Select"));
                                adapter_myCountryStates.updateDataList(MyCountryStateDataList);
                                spMyCountryState.setSelection(0);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
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
        MySingleton.getInstance(this).addToRequestQueue(req);
    }


    private void getStateCities(final String country_id, final String state_id) {


        pDialog.show();
    //    Log.e("url", Urls.getStateCitiesUrl + country_id + "," + state_id);
        JsonArrayRequest req = new JsonArrayRequest(Urls.getStateCitiesUrl + country_id + "," + state_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
        //                Log.e("Response", response.toString());
                        try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);
                            MyCountryCityDataList.clear();

                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();

                            MyCountryCityDataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
             //               Log.e("city Size is ", "" + MyCountryCityDataList.size());
                            if (MyCountryCityDataList.size() > 0) {
                                MyCountryCityDataList.add(0, new WebArd("-1", "Please Select "));
                                adapter_myCountryCity.updateDataList(MyCountryCityDataList);
                                spMyCountryCity.setSelection(0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            MyCountryCityDataList.clear();
                            adapter_myCountryCity.updateDataList(MyCountryCityDataList);
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
        MySingleton.getInstance(this).addToRequestQueue(req);
    }


    private void UpdateGeoGraphy(String country_id, String state_id, String city_id, String visa_status_id, String choice_country_ids, String choice_visa_status_ids) {

        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject params = new JSONObject();
        try {
            params.put("country_id", country_id);
            params.put("state_id", state_id);
            params.put("city_id", city_id);
            params.put("visa_status_id", visa_status_id);
            params.put("postal_code", "");
            params.put("choice_country_ids", choice_country_ids);
            params.put("choice_visa_status_ids", choice_visa_status_ids);
            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
        } catch (JSONException e) {
            e.printStackTrace();
        }
   //     Log.e("Params update", params + "");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.updateUserGeographyUrl, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
           //             Log.e("re  update geograpy", response + "");
                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {

                                if (!marryMax.getUpdateCheck(getApplicationContext())) {

                                    Intent intent = new Intent(RegisterGeographicActivity.this, RegisterAppearanceActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    Toast.makeText(RegisterGeographicActivity.this, "Updated", Toast.LENGTH_SHORT).show();
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

/*
    public void getCountryCode(final String selectedCountryCode) {


        Log.e("api path", Urls.getCntCode);
        StringRequest req = new StringRequest(Urls.getCntCode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", "=======================  " + response);
                        Log.e("country code  " + selectedCountryCode, response);

                        String cc = response;
                        response = response.replaceAll("^\"|\"$", "");

                        if (!selectedCountryCode.toLowerCase().equals(response.toLowerCase())) {
                            dialogGeoInfo newFragment = dialogGeoInfo.newInstance(response.toString());
                            newFragment.show(getSupportFragmentManager(), "dialog");
                        }

                        //   getIp(cc);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);


    }
*/

    private void putRequestCountryCode(final String selectedCountryCode) {

        final ProgressDialog pDialog = new ProgressDialog(RegisterGeographicActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject params = new JSONObject();
        try {

            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
            params.put("member_status", SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status());

        } catch (JSONException e) {
            e.printStackTrace();
        }
     //   Log.e("Params", "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.cntCode, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  update appearance", response + "");
                        try {
                            String responseName = response.getString("name");

                            countryCode = responseName.replaceAll("^\"|\"$", "");

                            if (!selectedCountryCode.toLowerCase().equals(responseName.toLowerCase())) {
                                dialogGeoInfo newFragment = dialogGeoInfo.newInstance(response.toString());
                                newFragment.show(getSupportFragmentManager(), "dialog");
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }


   /* public void getIp(final String cc) {
        Log.e("api path", Urls.getIpAddress);
        StringRequest req = new StringRequest(Urls.getIpAddress,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", "=======================  " + response);
                        //  Log.e("country code", response.get("countryCode").toString());
                        Toast.makeText(RegisterGeographicActivity.this, "CountryCode & IP = "+cc + "  " + response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);


    }*/

/*    private void getCountryCodeX(final String selectedCountryCode) {
        // getCurrentIP();

        pDialog.show();
        //   Log.e("clientip", "http://geoip.nekudo.com/api/" + clientip + "/en/short ");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                "http://ip-api.com/json", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("res mainnnnnnnnnnn", response + "");
                        try {


                            Log.e("country code", response.get("countryCode").toString());
                            if (!selectedCountryCode.equals(response.get("countryCode").toString())) {
                                dialogGeoInfo newFragment = dialogGeoInfo.newInstance(response.toString());
                                newFragment.show(getSupportFragmentManager(), "dialog");


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
                pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }*/

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
    public void onMultiChoiceSave(List<WebArd> s, int c) {


        switch (c) {
            case 1:

                MyChoiceCountryDataList.clear();
                MyChoiceCountryDataList.addAll(s);


                MarryMax max = new MarryMax(null);

                tvSpMultiChoice.setText(max.getSelectedTextFromList(MyChoiceCountryDataList, "My Choice Countries"));


                break;

            case 2:
                MyChoiceOriginCountryDataList.clear();
                MyChoiceOriginCountryDataList.addAll(s);


                MarryMax maxa = new MarryMax(null);

                tvSpMultiChoiceOrigin.setText(maxa.getSelectedTextFromList(MyChoiceOriginCountryDataList, "My Choice Origin Countries"));
                break;


        }


    }
}
