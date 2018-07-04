package com.chicsol.marrymax.activities.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class RegisterAppearanceActivity extends BaseRegistrationActivity {
    private Button bt_register_free, bt_back;
    private LinearLayout llcbViewPhysique, llcbViewComplexion, llcbViewHair, llcbViewEye;
    private RadioGroup rgPhysique, rgComplexion, rgHair, rgEye;
    private List<WebArd> bodyDataList, complexionDataList, hairDataList, heightDataList, eyeDataList, ageDataList;
    private NestedScrollView scrollMain;
    private Members members_obj;
    private Spinner spMyHeight, spMyChoiceheightFrom, spMyChoiceheightTo, spMyChoiceAgeFrom, spMyChoiceAgeTo;
    private MySpinnerAdapter spAdapterMyHeight, spAdapterMyChoiceHeightFrom, spAdapterMyChoiceHeightTo, spAdapterMyChoiceAgeFrom, spAdapterMyChoiceAgeTo;
    private boolean updateData = false;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_appearance);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initialize();


        GetAppearanceData();
        setListeners();
    }

    public void initialize() {

        pDialog = new ProgressDialog(RegisterAppearanceActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);


        scrollMain = (NestedScrollView) findViewById(R.id.ScrollViewRegAppearance);


        rgPhysique = (RadioGroup) findViewById(R.id.RadioGroupPhysicque);
        llcbViewPhysique = (LinearLayout) findViewById(R.id.LinearLayoutMyChoicePhysicque);

        rgComplexion = (RadioGroup) findViewById(R.id.RadioGroupComplexion);
        llcbViewComplexion = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceComplexion);

        rgEye = (RadioGroup) findViewById(R.id.RadioGroupEye);
        llcbViewEye = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceEye);

        rgHair = (RadioGroup) findViewById(R.id.RadioGroupHair);
        llcbViewHair = (LinearLayout) findViewById(R.id.LinearLayoutMyChoiceHair);

        bodyDataList = new ArrayList<>();
        complexionDataList = new ArrayList<>();
        eyeDataList = new ArrayList<>();
        hairDataList = new ArrayList<>();
        heightDataList = new ArrayList<>();
        ageDataList = new ArrayList<>();

        spMyHeight = (Spinner) findViewById(R.id.SpinnerAppMyHeight);
        spMyChoiceheightFrom = (Spinner) findViewById(R.id.SpinnerAppMyChoiceHeightFrom);
        spMyChoiceheightTo = (Spinner) findViewById(R.id.SpinnerAppMyChoiceHeightTo);
        spMyChoiceAgeFrom = (Spinner) findViewById(R.id.SpinnerAppMyChoiceAgeFrom);
        spMyChoiceAgeTo = (Spinner) findViewById(R.id.SpinnerAppMyChoiceAgeTo);

        spAdapterMyHeight = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, heightDataList);
        spAdapterMyHeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyHeight.setAdapter(spAdapterMyHeight);

        spAdapterMyChoiceHeightFrom = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, heightDataList);
        spAdapterMyChoiceHeightFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyChoiceheightFrom.setAdapter(spAdapterMyChoiceHeightFrom);

        spAdapterMyChoiceHeightTo = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, heightDataList);
        spAdapterMyChoiceHeightTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyChoiceheightTo.setAdapter(spAdapterMyChoiceHeightTo);


        for (int i = 18; i <= 70; i++) {

            WebArd webArd = new WebArd(String.valueOf(i), i + " Years");
            ageDataList.add(webArd);
        }

        ageDataList.add(0, new WebArd("-1", "Please Select"));
        spAdapterMyChoiceAgeFrom = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, ageDataList);
        spAdapterMyChoiceAgeFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyChoiceAgeFrom.setAdapter(spAdapterMyChoiceAgeFrom);

        spAdapterMyChoiceAgeTo = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, ageDataList);
        spAdapterMyChoiceAgeTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyChoiceAgeTo.setAdapter(spAdapterMyChoiceAgeTo);

        bt_register_free = (Button) findViewById(R.id.buttonContinueStep2);

        bt_back = (Button) findViewById(R.id.buttonBack);


        fabGeographic.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepComplete));
        fabAppearance.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepOnGoing));

        tvAppearance.setTextColor(getResources().getColor(R.color.colorRegistrationStepOnGoing));
        tvGeogrphic.setTextColor(getResources().getColor(R.color.colorRegistrationStepComplete));

        if (marryMax.getUpdateCheck(getApplicationContext())) {

            bt_register_free.setText("Update");
        }
    }


    public void setListeners() {







/*
        spMyChoiceheightTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/


        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(RegisterAppearanceActivity.this, RegisterGeographicActivity.class);
                startActivity(in);
                finish();
            }
        });

        bt_register_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!checkFieldsSelection(v)) {


                    ViewGenerator vg = new ViewGenerator(getApplicationContext());

                    WebArd waMyHeight = (WebArd) spMyHeight.getSelectedItem();
                    String height_id = waMyHeight.getId();


                    WebArd waMyChoiceHeightFrom = (WebArd) spMyChoiceheightFrom.getSelectedItem();
                    String choice_height_from_id = waMyChoiceHeightFrom.getId();
                    if (choice_height_from_id.equals("-1")) {
                        choice_height_from_id = "1";
                    }

                    WebArd waMyChoiceHeightTo = (WebArd) spMyChoiceheightTo.getSelectedItem();
                    String choice_height_to_id = waMyChoiceHeightTo.getId();

                    if (choice_height_to_id.equals("-1")) {
                        choice_height_to_id = "31";
                    }

                    WebArd waMyChoiceAgeFrom = (WebArd) spMyChoiceAgeFrom.getSelectedItem();
                    String choice_age_from = waMyChoiceAgeFrom.getId();
                    if (choice_age_from.equals("-1")) {
                        choice_age_from = "18";
                    }

                    WebArd waMyChoiceAgeTo = (WebArd) spMyChoiceAgeTo.getSelectedItem();
                    String choice_age_upto = waMyChoiceAgeTo.getId();
                    if (choice_age_upto.equals("-1")) {
                        choice_age_upto = "70";
                    }

                    //radios
                    String body_id = String.valueOf(rgPhysique.getCheckedRadioButtonId());
                    String choice_body_ids = vg.getSelectionFromCheckbox(llcbViewPhysique);

                    String eye_color_id = String.valueOf(rgEye.getCheckedRadioButtonId());
                    String choice_eye_color_ids = vg.getSelectionFromCheckbox(llcbViewEye);

                    String complexion_id = String.valueOf(rgComplexion.getCheckedRadioButtonId());
                    String choice_complexion_ids = vg.getSelectionFromCheckbox(llcbViewComplexion);

                    String hair_color_id = String.valueOf(rgHair.getCheckedRadioButtonId());
                    String choice_hair_color_ids = vg.getSelectionFromCheckbox(llcbViewHair);

                    //   Log.e(" b idddssss", "" + choice_hair_color_ids);
                    if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
                        updateAppearance(height_id, choice_height_from_id, choice_height_to_id, choice_age_from, choice_age_upto, body_id, choice_body_ids, eye_color_id, choice_eye_color_ids, complexion_id, choice_complexion_ids, hair_color_id, choice_hair_color_ids);
                    }
                }


            }
        });
    }


    private void setSpinnerListeners() {


        spMyChoiceAgeTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WebArd ard = (WebArd) view.getTag();

                WebArd waMyChoiceAgeFrom = (WebArd) spMyChoiceAgeFrom.getSelectedItem();
                WebArd waMyChoiceAgeTo = (WebArd) spMyChoiceAgeTo.getSelectedItem();
                if (!waMyChoiceAgeFrom.getId().equals("-1")) {
                    Log.e("id is", waMyChoiceAgeFrom.getId());
                    if (Integer.parseInt(waMyChoiceAgeTo.getId()) < Integer.parseInt(waMyChoiceAgeFrom.getId())) {
                        spMyChoiceAgeTo.setSelection(0);
                        Toast.makeText(RegisterAppearanceActivity.this, "Please Select Valid Age To", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    spMyChoiceAgeTo.setSelection(0);
                    Toast.makeText(RegisterAppearanceActivity.this, "Please Select Age From First", Toast.LENGTH_SHORT).show();

                }


//                Toast.makeText(RegisterAppearanceActivity.this, "" + waMyChoiceAgeTo.getName(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spMyChoiceheightTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WebArd ard = (WebArd) view.getTag();

                WebArd waMyChoiceAgeFrom = (WebArd) spMyChoiceheightFrom.getSelectedItem();
                WebArd waMyChoiceAgeTo = (WebArd) spMyChoiceheightTo.getSelectedItem();
                if (!waMyChoiceAgeFrom.getId().equals("-1")) {
                    Log.e("id is", waMyChoiceAgeFrom.getId());
                    if (Integer.parseInt(waMyChoiceAgeTo.getId()) < Integer.parseInt(waMyChoiceAgeFrom.getId())) {
                        spMyChoiceheightTo.setSelection(0);
                        Toast.makeText(RegisterAppearanceActivity.this, "Please Select Valid Height To", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    spMyChoiceheightTo.setSelection(0);
                    Toast.makeText(RegisterAppearanceActivity.this, "Please Select Height From First", Toast.LENGTH_SHORT).show();

                }


//                Toast.makeText(RegisterAppearanceActivity.this, "" + waMyChoiceAgeTo.getName(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    /*    spMyChoiceAgeFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                spMyChoiceAgeTo.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spMyChoiceheightFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spMyChoiceheightTo.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

    }


    private boolean checkFieldsSelection(View v) {
        boolean ck = false;


        if (rgPhysique.getCheckedRadioButtonId() == -1) {
            Snackbar.make(v, "Please select physique", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            ck = true;
        } else if (rgComplexion.getCheckedRadioButtonId() == -1) {

            Snackbar.make(v, "Please select complexion", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            ck = true;
        } else if (rgHair.getCheckedRadioButtonId() == -1) {

            Snackbar.make(v, "Please select hair color", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            //  Toast.makeText(this, "Please select hair color", Toast.LENGTH_SHORT).show();
            ck = true;
        } else if (rgEye.getCheckedRadioButtonId() == -1) {

            Snackbar.make(v, "Please select  eye color", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            ck = true;
        } else if (spMyHeight.getSelectedItemId() == 0) {
            TextView errorText = (TextView) spMyHeight.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
            errorText.setText("Please select height");

            Snackbar.make(v, "Please select height", Snackbar.LENGTH_SHORT)
                    .setAction("Go", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            scrollMain.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollMain.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        }
                    }).show();


            ck = true;
        }


        return ck;
    }


    private void selectFormData(Members members_obj) {

        ViewGenerator viewGenerator = new ViewGenerator(RegisterAppearanceActivity.this);
        viewGenerator.selectSpinnerItemById(spMyHeight, members_obj.get_height_id(), heightDataList);
        viewGenerator.selectSpinnerItemById(spMyChoiceheightFrom, members_obj.get_choice_height_from_id(), heightDataList);
        viewGenerator.selectSpinnerItemById(spMyChoiceheightTo, members_obj.get_choice_height_to_id(), heightDataList);

        viewGenerator.selectSpinnerItemById(spMyChoiceAgeFrom, members_obj.get_choice_age_from(), ageDataList);
        viewGenerator.selectSpinnerItemById(spMyChoiceAgeTo, members_obj.get_choice_age_upto(), ageDataList);

        Members member = SharedPreferenceManager.getUserObject(getApplicationContext());
    /*    if (member.get_member_status() == 3 || member.get_member_status() == 4) {*/
        if (member.get_member_status() >= 2 && member.get_member_status() < 7) {
            spMyHeight.setEnabled(false);

            viewGenerator.selectCheckRadioWithDisabledRadio(rgPhysique, members_obj.get_body_id(), llcbViewPhysique, members_obj.get_choice_body_ids());
            viewGenerator.selectCheckRadioWithDisabledRadio(rgEye, members_obj.get_eye_color_id(), llcbViewEye, members_obj.get_choice_eye_color_ids());
            viewGenerator.selectCheckRadioWithDisabledRadio(rgComplexion, members_obj.get_complexion_id(), llcbViewComplexion, members_obj.get_choice_complexion_ids());
            viewGenerator.selectCheckRadioWithDisabledRadio(rgHair, members_obj.get_hair_color_id(), llcbViewHair, members_obj.get_choice_hair_color_ids());


        } else {
            viewGenerator.selectCheckRadio(rgPhysique, members_obj.get_body_id(), llcbViewPhysique, members_obj.get_choice_body_ids());

            viewGenerator.selectCheckRadio(rgEye, members_obj.get_eye_color_id(), llcbViewEye, members_obj.get_choice_eye_color_ids());

            viewGenerator.selectCheckRadio(rgComplexion, members_obj.get_complexion_id(), llcbViewComplexion, members_obj.get_choice_complexion_ids());

            viewGenerator.selectCheckRadio(rgHair, members_obj.get_hair_color_id(), llcbViewHair, members_obj.get_choice_hair_color_ids());

        }


        //    Log.e("choice countrr", members_obj.get_choice_hair_color_ids() + "");
        /*

        Log.e("choice countrr", members_obj.get_choice_country_ids());
        String[] cids = members_obj.get_choice_country_ids().split(",");
*/


//==============checkbox

//        Log.e("visa", members_obj.get_choice_visa_status_ids());


    }

    /*  @Override
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
      }
  */
    //API CALLS
    private void GetAppearanceData() {

        pDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Urls.RegGetProfileAppearance + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("res mainnnnnnnnnnn", response + "");
                        try {

                            JSONArray jsonArrayBody = response.getJSONArray("body");

                            JSONArray jsonArrayComplexion = response.getJSONArray("complexion");
                            JSONArray jsonArrayHair = response.getJSONArray("hair");
                            JSONArray jsonArrayEye = response.getJSONArray("eye");
                            JSONArray jsonArrayheight = response.getJSONArray("height");
                            Gson gsonc;

                            GsonBuilder gsonBuilderc = new GsonBuilder();

                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();


                            bodyDataList = (List<WebArd>) gsonc.fromJson(jsonArrayBody.toString(), listType);


                            complexionDataList = (List<WebArd>) gsonc.fromJson(jsonArrayComplexion.toString(), listType);
                            hairDataList = (List<WebArd>) gsonc.fromJson(jsonArrayHair.toString(), listType);
                            eyeDataList = (List<WebArd>) gsonc.fromJson(jsonArrayEye.toString(), listType);
                            heightDataList = (List<WebArd>) gsonc.fromJson(jsonArrayheight.toString(), listType);

                            heightDataList.add(0, new WebArd("-1", "Please Select"));

                            spAdapterMyHeight.updateDataList(heightDataList);
                            spAdapterMyChoiceHeightFrom.updateDataList(heightDataList);
                            spAdapterMyChoiceHeightTo.updateDataList(heightDataList);


                            //=================================parsing object
                            Gson gson;
                            JSONArray jsonGrography = response.getJSONArray("appearance");
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            members_obj = gson.fromJson(jsonGrography.get(0).toString(), Members.class);


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }

                        //==============DATA SETTING==============================================

                        ViewGenerator viewGenerator = new ViewGenerator(RegisterAppearanceActivity.this);


                        if (members_obj.get_body_id() == 0) {
                            updateData = false;

                        } else {
                            updateData = true;

                        }


                        if (updateData) {
                          /*  Members member=  SharedPreferenceManager.getUserObject(getApplicationContext());
                            if (member.get_member_status()== 3 ||member.get_member_status()== 4) {
                                viewGenerator.addDynamicCheckRdioButtonsWithRadioDisabled(bodyDataList, rgPhysique, llcbViewPhysique);
                                viewGenerator.addDynamicCheckRdioButtonsWithRadioDisabled(complexionDataList, rgComplexion, llcbViewComplexion);
                                viewGenerator.addDynamicCheckRdioButtonsWithRadioDisabled(hairDataList, rgHair, llcbViewHair);
                                viewGenerator.addDynamicCheckRdioButtonsWithRadioDisabled(eyeDataList, rgEye, llcbViewEye);
                            }
                            else {*/
                            viewGenerator.addDynamicCheckRdioButtons(bodyDataList, rgPhysique, llcbViewPhysique);
                            viewGenerator.addDynamicCheckRdioButtons(complexionDataList, rgComplexion, llcbViewComplexion);
                            viewGenerator.addDynamicCheckRdioButtons(hairDataList, rgHair, llcbViewHair);
                            viewGenerator.addDynamicCheckRdioButtons(eyeDataList, rgEye, llcbViewEye);
                            //  }
                            selectFormData(members_obj);
                            setSpinnerListeners();

                        } else {
                            viewGenerator.addDynamicCheckRdioButtons(bodyDataList, rgPhysique, llcbViewPhysique);
                            viewGenerator.addDynamicCheckRdioButtons(complexionDataList, rgComplexion, llcbViewComplexion);
                            viewGenerator.addDynamicCheckRdioButtons(hairDataList, rgHair, llcbViewHair);
                            viewGenerator.addDynamicCheckRdioButtons(eyeDataList, rgEye, llcbViewEye);

                            spMyChoiceheightFrom.setSelection(0);
                            spMyChoiceheightTo.setSelection(0);
                            //spMyChoiceheightTo.setSelection(heightDataList.size() - 1);

                            spMyChoiceAgeFrom.setSelection(0);
                            spMyChoiceAgeTo.setSelection(0);
                            setSpinnerListeners();
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

    private void updateAppearance(String height_id, String choice_height_from_id, String choice_height_to_id, String choice_age_from, String choice_age_upto, String body_id, String choice_body_ids, String eye_color_id, String choice_eye_color_ids, String complexion_id, String choice_complexion_ids, String hair_color_id, String choice_hair_color_ids) {

        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject params = new JSONObject();
        try {


            params.put("height_id", height_id);
            params.put("choice_height_from_id", choice_height_from_id);
            params.put("choice_height_to_id", choice_height_to_id);
            params.put("choice_age_from", choice_age_from);
            params.put("choice_age_upto", choice_age_upto);
            params.put("body_id", body_id);
            params.put("choice_body_ids", choice_body_ids);
            params.put("eye_color_id", eye_color_id);

            params.put("choice_eye_color_ids", choice_eye_color_ids);
            params.put("complexion_id", complexion_id);
            params.put("choice_complexion_ids", choice_complexion_ids);
            params.put("hair_color_id", hair_color_id);
            params.put("choice_hair_color_ids", choice_hair_color_ids);
            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Params", "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.updateUserAppearanceUrl, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {
                            int responseid = response.getInt("id");


                            if (responseid == 1) {


                                if (!marryMax.getUpdateCheck(getApplicationContext())) {
                                    Intent in = new Intent(RegisterAppearanceActivity.this, RegisterLifeStyleActivity1.class);
                                    startActivity(in);
                                    finish();

                                } else {

                                    Toast.makeText(RegisterAppearanceActivity.this, "Updated", Toast.LENGTH_SHORT).show();
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
