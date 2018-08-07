package com.chicsol.marrymax.activities.whoislookingformesearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.ActivityLogin;
import com.chicsol.marrymax.activities.searchyourbestmatch.SearchYourBestMatchResultsActivity;
import com.chicsol.marrymax.adapters.MySpinnerAdapter;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
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

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;


/**
 * Created by Redz on 10/21/2016.
 */

public class WhoIsLookingForMeSearchActivity extends AppCompatActivity {


    String gender = null;
    private TextView tv_RegisterNewActivity;
    private Spinner spinner_source, spMyChoiceAgeFrom, spinner_religion, spinner_education, spinner_country, spinner_ethnic, spinner_religioussect;
    private Button bt_back, buttonSearchMatches;
    private LinearLayout ll_maleNormal, ll_maleSelected, ll_femaleNormal, ll_femaleSelected;
    private List<WebArd> ageDataList, religionDatalist, ethnicDatalist, relgiousSectDatalist, countryDatalist, educationDatalist;
    private MySpinnerAdapter spAdapterMyChoiceAgeFrom,  adapter_religion, adapter_religioussect, adapter_country, adapter_ethnic, adapter_education;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (defaultSelectionsObj == null) {
        defaultSelectionsObj = new Members();
        // }
        setContentView(R.layout.actvity_search_who_is_looking_for_me);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initialize();
        setListenders();

        getRegistrationData();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initialize() {


        buttonSearchMatches = (Button) findViewById(R.id.buttonSearchMatches);


        ageDataList = new ArrayList<>();
        educationDatalist = new ArrayList<>();
        ethnicDatalist = new ArrayList<>();
        religionDatalist = new ArrayList<>();
        relgiousSectDatalist = new ArrayList<>();
        countryDatalist = new ArrayList<>();


        //tv_RegisterNewActivity = (TextView) findViewById(R.id.TextViewRegisterNewActivity);


        ll_maleNormal = (LinearLayout) findViewById(R.id.LinearLayoutMaleNormal);
        ll_maleSelected = (LinearLayout) findViewById(R.id.LinearLayoutMaleSelected);

        ll_femaleNormal = (LinearLayout) findViewById(R.id.LinearLayoutFemaleNormal);
        ll_femaleSelected = (LinearLayout) findViewById(R.id.LinearLayoutFemaleSelected);

        spMyChoiceAgeFrom = (Spinner) findViewById(R.id.SpinnerAppMyChoiceAgeFrom);
     //   spMyChoiceAgeTo = (Spinner) findViewById(R.id.SpinnerAppMyChoiceAgeTo);

        spinner_education = (Spinner) findViewById(R.id.sp_bestmatch_education);
        spinner_ethnic = (Spinner) findViewById(R.id.sp_bestmatch_ethnic_background);
        spinner_religion = (Spinner) findViewById(R.id.sp_bestmatch_religion);
        spinner_religioussect = (Spinner) findViewById(R.id.sp_bestmatch_religious_sect);
        spinner_country = (Spinner) findViewById(R.id.sp_bestmatch_country);

        for (int i = 18; i <= 70; i++) {

            WebArd webArd = new WebArd(String.valueOf(i), i + " Years");
            ageDataList.add(webArd);
        }

        ageDataList.add(0, new WebArd("-1", "Please Select"));
        spAdapterMyChoiceAgeFrom = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, ageDataList);
        spAdapterMyChoiceAgeFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyChoiceAgeFrom.setAdapter(spAdapterMyChoiceAgeFrom);

     /*   spAdapterMyChoiceAgeTo = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, ageDataList);
        spAdapterMyChoiceAgeTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyChoiceAgeTo.setAdapter(spAdapterMyChoiceAgeTo);
*/

        bt_back = (Button) findViewById(R.id.ButtonBack);


        spinner_religion = (Spinner) findViewById(R.id.sp_bestmatch_religion);
        spinner_religion.setPrompt("Select Religion");
        adapter_religion = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, religionDatalist);
        adapter_religion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_religion.setAdapter(adapter_religion);


        spinner_religioussect = (Spinner) findViewById(R.id.sp_bestmatch_religious_sect);
        spinner_religioussect.setPrompt("Select Religious Sect");
        adapter_religioussect = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, relgiousSectDatalist);
        adapter_religioussect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_religioussect.setAdapter(adapter_religioussect);

        spinner_country = (Spinner) findViewById(R.id.sp_bestmatch_country);
        spinner_country.setPrompt("Select Country");
        adapter_country = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, countryDatalist);
        adapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_country.setAdapter(adapter_country);

        spinner_education = (Spinner) findViewById(R.id.sp_bestmatch_education);
        spinner_education.setPrompt("Select Education");
        adapter_education = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, educationDatalist);
        adapter_education.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_education.setAdapter(adapter_education);


        spinner_ethnic = (Spinner) findViewById(R.id.sp_bestmatch_ethnic_background);
        spinner_ethnic.setPrompt("Select Education");
        adapter_ethnic = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, ethnicDatalist);
        adapter_ethnic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ethnic.setAdapter(adapter_ethnic);

        getSearchListData();


    }

    private void setListenders() {


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


        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(WhoIsLookingForMeSearchActivity.this, ActivityLogin.class);
                startActivity(in);
            }
        });

        buttonSearchMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean cancel = false;

                View focusView = null;


                if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {


                    if (spinner_religion.getSelectedItemId() == 0) {


                        TextView errorText = (TextView) spinner_religion.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(getResources().getColor(R.color.colorTextRed));//just to highlight that this is an error
                        errorText.setText("Please Select Religion");
                    }

                    if (gender == null) {
                        Toast.makeText(WhoIsLookingForMeSearchActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();

                    }

                    if (spinner_religion.getSelectedItemId() != 0 && gender != null) {


                        defaultSelectionsObj.set_gender(gender);


                        WebArd sReligion = (WebArd) spinner_religion.getSelectedItem();
                        defaultSelectionsObj.set_religion_id(Long.parseLong(sReligion.getId()));
                        if (spinner_education.getSelectedItemId() != 0) {
                            WebArd srr = (WebArd) spinner_education.getSelectedItem();
                            defaultSelectionsObj.set_choice_education_ids(srr.getId());
                        }
                        if (spinner_ethnic.getSelectedItemId() != 0) {
                            WebArd srr = (WebArd) spinner_ethnic.getSelectedItem();
                            defaultSelectionsObj.set_choice_ethnic_bground_ids(srr.getId());
                        }
                        if (spinner_religioussect.getSelectedItemId() != 0) {
                            WebArd srr = (WebArd) spinner_religioussect.getSelectedItem();
                            defaultSelectionsObj.set_choice_religious_sect_ids(srr.getId());
                        }
                        if (spinner_country.getSelectedItemId() != 0) {
                            WebArd srr = (WebArd) spinner_country.getSelectedItem();
                            defaultSelectionsObj.set_choice_country_ids(srr.getId());
                        }
                        if (spMyChoiceAgeFrom.getSelectedItemId() != 0) {
                            WebArd srr = (WebArd) spMyChoiceAgeFrom.getSelectedItem();
                            defaultSelectionsObj.set_min_age(Long.parseLong(srr.getId()));
                        }
                      /*  if (spMyChoiceAgeTo.getSelectedItemId() != 0) {
                            WebArd srr = (WebArd) spMyChoiceAgeTo.getSelectedItem();
                            defaultSelectionsObj.set_choice_age_upto(Long.parseLong(srr.getId()));
                        }
*/

                        Gson gson = new Gson();
                        String params = gson.toJson(defaultSelectionsObj);
                        Log.e("params  ", params + "");

                        // SharedPreferenceManager.setDefaultSelectionsObj(getApplicationContext(),defaultSelectionsObj);

                        Intent in = new Intent(WhoIsLookingForMeSearchActivity.this, WhoIsLookingForMeResultsActivity.class);
                        startActivity(in);


                    }
                }

            }
        });


      /*  tv_RegisterNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              *//*  Intent in = new Intent(SearchYourBestMatchActivity.this, RegistrationActivity.class);
                startActivity(in);*//*


            }
        });*/

        setSpinnerListeners();
    }


    private void setSpinnerListeners() {


/*        spMyChoiceAgeTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //   WebArd ard = (WebArd) view.getTag();

                WebArd waMyChoiceAgeFrom = (WebArd) spMyChoiceAgeFrom.getSelectedItem();
                WebArd waMyChoiceAgeTo = (WebArd) spMyChoiceAgeTo.getSelectedItem();
                if (!waMyChoiceAgeFrom.getId().equals("-1")) {
                    Log.e("id is", waMyChoiceAgeFrom.getId());
                    if (Integer.parseInt(waMyChoiceAgeTo.getId()) < Integer.parseInt(waMyChoiceAgeFrom.getId())) {
                        spMyChoiceAgeTo.setSelection(0);
                        Toast.makeText(WhoIsLookingForMeSearchActivity.this, "Please Select Valid Age To", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    spMyChoiceAgeTo.setSelection(0);
                    //  Toast.makeText(SearchYourBestMatchActivity.this, "Please Select Age From First", Toast.LENGTH_SHORT).show();

                }


//                Toast.makeText(RegisterAppearanceActivity.this, "" + waMyChoiceAgeTo.getName(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


     /*   spMyChoiceheightTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });*/

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registration_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MarryMax marryMax = new MarryMax(WhoIsLookingForMeSearchActivity.this);

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
            case R.id.action_personalized_matching:
                marryMax.personalizedMatching();
                return true;
            case R.id.action_faq:
                marryMax.faq();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public static void doPositiveClick() {
        // Toast.makeText( "clcieck", Toast.LENGTH_SHORT).show();

        // Do stuff here.
        Log.e("FragmentAlertDialog", "Positive click!");
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.e("FragmentAlertDialog", "Negative click!");
    }

/*    public static class DialogProfileFor1 extends DialogFragment {


        public static DialogProfileFor1 newInstance(int title) {
            DialogProfileFor1 frag = new DialogProfileFor1();
            Bundle args = new Bundle();
            args.putInt("name", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.dialog_profilefor, container, false);


            Button mOkButton = (Button) rootView.findViewById(R.id.okDialog);
            mOkButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {
                    //         Toast.makeText(getActivity().getApplicationContext(), "my activity", Toast.LENGTH_SHORT).show();


                    SearchYourBestMatchActivity.doPositiveClick();


                }
            });

            Button cancelButton = (Button) rootView.findViewById(R.id.dismissBtn);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {
                    Toast.makeText(getActivity().getApplicationContext(), "neg", Toast.LENGTH_SHORT).show();
                    DialogProfileFor1.this.getDialog().cancel();
                }
            });


            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }*/


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


    private void setDefaultSelections() {

     //   spMyChoiceAgeFrom.setSelection(4);
     //   spMyChoiceAgeTo.setSelection(12);
        selectfemale();
        spinner_religion.setSelection(1);

    }

    private void getRegistrationData() {

        String url = Urls.reg_Listing;
        Log.e("Url", "" + url);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //religious

                        Log.d("res", response.toString());
                        try {
                            JSONArray religionarray = response.getJSONArray("religion");
                            JSONArray religioussectarray = response.getJSONArray("religious");
                            JSONArray ethnicarray = response.getJSONArray("ethnic");
                            JSONArray educationarray = response.getJSONArray("education");
                            JSONArray countryarray = response.getJSONArray("country");
                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();

                            religionDatalist = (List<WebArd>) gsonc.fromJson(religionarray.toString(), listType);
                            religionDatalist.add(0, new WebArd("-1", "Select Religion"));
                            Log.e("size", "" + religionDatalist.size());
                            adapter_religion.updateDataList(religionDatalist);

                            relgiousSectDatalist = (List<WebArd>) gsonc.fromJson(religioussectarray.toString(), listType);
                            relgiousSectDatalist.add(0, new WebArd("-1", "Select Religious Sect"));

                            adapter_religioussect.updateDataList(relgiousSectDatalist);

                            ethnicDatalist = (List<WebArd>) gsonc.fromJson(ethnicarray.toString(), listType);
                            ethnicDatalist.add(0, new WebArd("-1", "Select Ethnic Background"));
                            adapter_ethnic.updateDataList(ethnicDatalist);

                            countryDatalist = (List<WebArd>) gsonc.fromJson(countryarray.toString(), listType);
                            countryDatalist.add(0, new WebArd("-1", "Select Country"));
                            adapter_country.updateDataList(countryDatalist);

                            educationDatalist = (List<WebArd>) gsonc.fromJson(educationarray.toString(), listType);
                            educationDatalist.add(0, new WebArd("-1", "Select Education"));
                            adapter_education.updateDataList(educationDatalist);
                            setDefaultSelections();

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

    private void getSearchListData() {
        // Log.e("get message","lllllllkkasdksadk");
        //  String.Max
//pDialog.setMessage("getMessage");
        //  pDialog.show();
        Log.e("Search Lists url", Urls.getSearchLists + 0);
        JsonArrayRequest req = new JsonArrayRequest(Urls.getSearchLists + 0,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("response", response.toString());
                        Constants.jsonArraySearch = response;
                        //   pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                //  pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }
}
