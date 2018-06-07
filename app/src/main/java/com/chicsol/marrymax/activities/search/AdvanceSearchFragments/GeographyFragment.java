package com.chicsol.marrymax.activities.search.AdvanceSearchFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.CheckBoxAdvSearchCSCRAdapter;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.modal.WebCSC;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.chicsol.marrymax.widgets.mTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;
import static com.chicsol.marrymax.utils.Constants.jsonArraySearch;

public class GeographyFragment extends Fragment implements CheckBoxAdvSearchCSCRAdapter.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    private LinearLayout LinearLayoutAdvSearchVisaStatus;
    private RecyclerView rvCountries, rvStates, rvCities;
    private ViewGenerator viewGenerator;
    private List<WebCSC> countriesDataList, statesDataList, citiesDataList;
    private CheckBoxAdvSearchCSCRAdapter countriesAdapter, statesAdapter, citiesAdapter;
    private ProgressDialog pDialog;
    private mTextView tvMsgStates, tvMsgCities;
    private String selectedCountries;
    private int CountryClick = 1, CityClick = 0, StateClick = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adv_search_fragment_geography,
                container, false);
        initialize(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setSelection();
        setListeners();
    }

    private void initialize(View view) {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        tvMsgStates = (mTextView) view.findViewById(R.id.TextViewAdvSearchStateError);
        tvMsgCities = (mTextView) view.findViewById(R.id.TextViewAdvSearchCityError);

        rvCountries = (RecyclerView) view.findViewById(R.id.RecyclerViewAdvSearchItemCountries);
        rvCountries.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCountries.setHasFixedSize(true);

        rvStates = (RecyclerView) view.findViewById(R.id.RecyclerViewAdvSearchItemStates);
        rvStates.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvStates.setHasFixedSize(true);

        rvCities = (RecyclerView) view.findViewById(R.id.RecyclerViewAdvSearchItemCities);
        rvCities.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCities.setHasFixedSize(true);


        countriesDataList = new ArrayList<>();
        citiesDataList = new ArrayList<>();
        statesDataList = new ArrayList<>();


        countriesAdapter = new CheckBoxAdvSearchCSCRAdapter(countriesDataList, 1);
        rvCountries.setAdapter(countriesAdapter);
        countriesAdapter.setOnItemClickListener(GeographyFragment.this);

        statesAdapter = new CheckBoxAdvSearchCSCRAdapter(statesDataList, 2);
        rvStates.setAdapter(statesAdapter);
        statesAdapter.setOnItemClickListener(GeographyFragment.this);


        citiesAdapter = new CheckBoxAdvSearchCSCRAdapter(citiesDataList, 0);
        rvCities.setAdapter(citiesAdapter);
        citiesAdapter.setOnItemClickListener(GeographyFragment.this);


        viewGenerator = new ViewGenerator(getContext());
        LinearLayoutAdvSearchVisaStatus = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchVisaStatus);


        Gson gsonc;
        GsonBuilder gsonBuilderc = new GsonBuilder();
        gsonc = gsonBuilderc.create();
        Type listType = new TypeToken<List<WebArd>>() {
        }.getType();

        Type listTypeWebCsc = new TypeToken<List<WebCSC>>() {
        }.getType();


        List<WebCSC> countriesDataList = null;
        try {
            List<WebArd> dataList0 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(21).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLL(dataList0, LinearLayoutAdvSearchVisaStatus);

            countriesDataList = (List<WebCSC>) gsonc.fromJson(jsonArraySearch.getJSONArray(3).toString(), listTypeWebCsc);

            Log.e("c size", countriesDataList.size() + "");
            //	viewGenerator.generateDynamicCheckBoxesLL(dataList1, LinearLayoutAdvSearchCountries);

            countriesAdapter.updateDataList(countriesDataList);


		/*	List<WebArd> dataList2 = (List<WebArd>) gsonc.fromJson(DrawerActivity.jsonArraySearch.getJSONArray(23).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLL(dataList2, LinearLayoutAdvSearchCaste);
*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setSelection() {

        if (defaultSelectionsObj != null) {

            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchVisaStatus, defaultSelectionsObj.get_choice_visa_status_ids());
            countriesAdapter.selectItem(defaultSelectionsObj.get_choice_country_ids());
            if (defaultSelectionsObj.get_choice_country_ids() != "" && defaultSelectionsObj.get_choice_country_ids() != null) {
                getStates(defaultSelectionsObj.get_choice_country_ids());
            }
        }

    }


    @Override
    public void onCheckedChange(String selectedIds, int scCheck) {

        switch (scCheck) {
            case 1:
                if (!selectedIds.equals("")) {
                    selectedCountries = selectedIds;
                    defaultSelectionsObj.set_choice_country_ids(selectedIds);
                    getStates(selectedIds);
                } else {
                    tvMsgStates.setVisibility(View.VISIBLE);
                    statesAdapter.clear();
                    defaultSelectionsObj.set_choice_country_ids(selectedIds);
                }
                break;
            case 2:
                if (!selectedIds.equals("")) {
                    getCities(selectedCountries + "^" + selectedIds);
                    defaultSelectionsObj.set_choice_state_ids(selectedIds);
                } else {
                    tvMsgCities.setVisibility(View.VISIBLE);
                    citiesAdapter.clear();
                    defaultSelectionsObj.set_choice_state_ids(selectedIds);
                }
                break;
            case 0:
                defaultSelectionsObj.set_choice_cities_ids(selectedIds);

                break;
            default:
                break;
        }

    }


    private void getStates(final String country_id) {

        Log.e("getStates", Urls.getStates + country_id);
       // pDialog.setMessage("states loading....");
       // pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Urls.getStates + country_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                        if (!response.isNull(0)) {


                            try {
                             //   pDialog.dismiss();

                                JSONArray jsonCountryStaeObj = response.getJSONArray(0);


                                Gson gsonc;
                                GsonBuilder gsonBuilderc = new GsonBuilder();
                                gsonc = gsonBuilderc.create();
                                Type listType = new TypeToken<List<WebCSC>>() {
                                }.getType();

                                List<WebCSC> MyCountryStateDataList = (List<WebCSC>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                                if (MyCountryStateDataList.size() > 0) {
                                    tvMsgStates.setVisibility(View.GONE);
                                    statesAdapter.updateDataList(MyCountryStateDataList);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                      //  pDialog.dismiss();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(req);
    }

    private void getCities(final String country_id) {
      //  pDialog.setMessage("cities loading...");
      //  pDialog.show();
        Log.e("getCities", Urls.getCities + country_id);
        JsonArrayRequest req = new JsonArrayRequest(Urls.getCities + country_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                        try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebCSC>>() {
                            }.getType();

                            List<WebCSC> MyCountryStateDataList = (List<WebCSC>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                            if (MyCountryStateDataList.size() > 0) {
                                tvMsgCities.setVisibility(View.GONE);
                                citiesAdapter.updateDataList(MyCountryStateDataList);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                       // pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
               // pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(req);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }


    private void setListeners() {
        {
            int childcount = LinearLayoutAdvSearchVisaStatus.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = LinearLayoutAdvSearchVisaStatus.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        defaultSelectionsObj.set_choice_visa_status_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchVisaStatus));
    }
}
