package com.chicsol.marrymax.activities.search.AdvanceSearchFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;
import static com.chicsol.marrymax.utils.Constants.jsonArraySearch;

public class GeographyFragment extends Fragment implements CheckBoxAdvSearchCSCRAdapter.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    private LinearLayout LinearLayoutAdvSearchVisaStatus;
    private RecyclerView rvCountries, rvStates, rvCities, rvTopCities, rvTopStates;
    private ViewGenerator viewGenerator;
    private List<WebCSC> countriesDataList, statesDataList, citiesDataList, topCitiesDataList, topStatesDataList;
    private CheckBoxAdvSearchCSCRAdapter countriesAdapter, statesAdapter, citiesAdapter, topCitiesAdapter, topStatesAdapter;
    private ProgressDialog pDialog;
    private mTextView tvMsgStates, tvMsgCities;
    private String selectedCountries;
    private int CountryClick = 1, CityClick = 0, StateClick = 2;

    private boolean statesApiRunning = false;
    private boolean citiesApiRunning = false;

    Map<String, String> selectedCountriesMap = new HashMap<String, String>();
    Map<String, String> selectedStatesMap = new HashMap<String, String>();
    Map<String, String> selectedCitiesMap = new HashMap<String, String>();

    private EditText etCountrySearch, etStatesSearch, etCitySearch;

    private OnChildFragmentInteractionListener fragmentInteractionListener;

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


        etCountrySearch = (EditText) view.findViewById(R.id.EditTextAdvSearchGeographyCountrySearch);
        etStatesSearch = (EditText) view.findViewById(R.id.EditTextAdvSearchGeographyStateSearch);
        etCitySearch = (EditText) view.findViewById(R.id.EditTextAdvSearchGeographyCitySearch);

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

        rvTopCities = (RecyclerView) view.findViewById(R.id.RecyclerViewAdvSearchItemTopCities);
        rvTopCities.setLayoutManager(new LinearLayoutManager(getActivity()));
        //  rvTopCities.setHasFixedSize(true);

        rvTopStates = (RecyclerView) view.findViewById(R.id.RecyclerViewAdvSearchItemTopStates);
        rvTopStates.setLayoutManager(new LinearLayoutManager(getActivity()));
        //    rvTopStates.setHasFixedSize(true);


        countriesDataList = new ArrayList<>();
        citiesDataList = new ArrayList<>();
        statesDataList = new ArrayList<>();
        topCitiesDataList = new ArrayList<>();
        topStatesDataList = new ArrayList<>();

        countriesAdapter = new CheckBoxAdvSearchCSCRAdapter(countriesDataList, 1);
        rvCountries.setAdapter(countriesAdapter);
        countriesAdapter.setOnItemClickListener(GeographyFragment.this);

        statesAdapter = new CheckBoxAdvSearchCSCRAdapter(statesDataList, 2);
        rvStates.setAdapter(statesAdapter);
        statesAdapter.setOnItemClickListener(GeographyFragment.this);


        citiesAdapter = new CheckBoxAdvSearchCSCRAdapter(citiesDataList, 0);
        rvCities.setAdapter(citiesAdapter);
        citiesAdapter.setOnItemClickListener(GeographyFragment.this);


        topCitiesAdapter = new CheckBoxAdvSearchCSCRAdapter(topCitiesDataList, 3);
        rvTopCities.setAdapter(topCitiesAdapter);
        topCitiesAdapter.setOnItemClickListener(GeographyFragment.this);


        topStatesAdapter = new CheckBoxAdvSearchCSCRAdapter(topStatesDataList, 4);
        rvTopStates.setAdapter(topStatesAdapter);
        topStatesAdapter.setOnItemClickListener(GeographyFragment.this);


        viewGenerator = new ViewGenerator(getContext());
        LinearLayoutAdvSearchVisaStatus = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchVisaStatus);


        Gson gsonc;
        GsonBuilder gsonBuilderc = new GsonBuilder();
        gsonc = gsonBuilderc.create();
        Type listType = new TypeToken<List<WebArd>>() {
        }.getType();

        Type listTypeWebCsc = new TypeToken<List<WebCSC>>() {
        }.getType();


        //List<WebCSC> countriesDataList = null;
        try {
            List<WebArd> dataList0 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(21).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLL(dataList0, LinearLayoutAdvSearchVisaStatus);

            countriesDataList = (List<WebCSC>) gsonc.fromJson(jsonArraySearch.getJSONArray(3).toString(), listTypeWebCsc);

            Log.e("c size", countriesDataList.size() + "");
            //	viewGenerator.generateDynamicCheckBoxesLL(dataList1, LinearLayoutAdvSearchCountries);

            topCitiesDataList = (List<WebCSC>) gsonc.fromJson(jsonArraySearch.getJSONArray(27).toString(), listTypeWebCsc);
            topStatesDataList = (List<WebCSC>) gsonc.fromJson(jsonArraySearch.getJSONArray(26).toString(), listTypeWebCsc);

            countriesAdapter.updateDataList(countriesDataList);


            topCitiesAdapter.updateDataList(topCitiesDataList);
            topStatesAdapter.updateDataList(topStatesDataList);


		/*	List<WebArd> dataList2 = (List<WebArd>) gsonc.fromJson(DrawerActivity.jsonArraySearch.getJSONArray(23).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLL(dataList2, LinearLayoutAdvSearchCaste);
*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setSelection() {

        if (defaultSelectionsObj != null) {
            selectedCitiesMap.clear();
            selectedStatesMap.clear();
            selectedCountriesMap.clear();

            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchVisaStatus, defaultSelectionsObj.get_choice_visa_status_ids());


            Log.e("states", defaultSelectionsObj.get_choice_state_ids() + "");


            countriesAdapter.selectItem(defaultSelectionsObj.get_choice_country_ids());
            if (defaultSelectionsObj.get_choice_country_ids() != "" && defaultSelectionsObj.get_choice_country_ids() != null) {

                getStates(defaultSelectionsObj.get_choice_country_ids());

                selectStatesGetCities(defaultSelectionsObj.get_choice_state_ids(), defaultSelectionsObj.get_choice_cities_ids());

            }
        }


    }


    @Override
    public void onCheckedChange(String selectedIds, int scCheck, final WebCSC Objcsc, boolean isChecked) {

        switch (scCheck) {
            case 1:
                //countries

                // Log.e("cid", "" + Objcsc.getId());
                if (isChecked) {
                    selectedCountriesMap.put(Objcsc.getId(), Objcsc.getId());


                } else {
                    selectedCountriesMap.remove(Objcsc.getId());
                    if (selectedCountriesMap.size() == 0) {
                        selectedStatesMap.clear();
                        selectedCitiesMap.clear();
                        topStatesAdapter.unCheckAll();
                        topCitiesAdapter.unCheckAll();
                    }


                }


                if (!selectedIds.equals("")) {
                    citiesAdapter.clear();
                    tvMsgCities.setVisibility(View.VISIBLE);

                    selectedCountries = selectedIds;
                    defaultSelectionsObj.set_choice_country_ids(selectedIds);
                    getStates(getComaSeparatedItemsFromMap(selectedCountriesMap));
                    selectStatesGetCities(getComaSeparatedItemsFromMap(selectedStatesMap), getComaSeparatedItemsFromMap(selectedCitiesMap));




                } else {
                    citiesAdapter.clear();
                    tvMsgCities.setVisibility(View.VISIBLE);


                    tvMsgStates.setVisibility(View.VISIBLE);
                    statesAdapter.clear();
                    defaultSelectionsObj.set_choice_country_ids(selectedIds);

                    defaultSelectionsObj.set_choice_state_ids(getComaSeparatedItemsFromMap(selectedStatesMap));
                    defaultSelectionsObj.set_choice_cities_ids(getComaSeparatedItemsFromMap(selectedCitiesMap));

                }
                Log.e("selectedMap  " + defaultSelectionsObj.get_choice_country_ids() + " = " + defaultSelectionsObj.get_choice_state_ids() + " = " + defaultSelectionsObj.get_choice_cities_ids(), "" + getComaSeparatedItemsFromMap(selectedStatesMap) + " --------  " + getComaSeparatedItemsFromMap(selectedCitiesMap));


                break;
            case 2:
                //states

                if (isChecked) {
                    selectedStatesMap.put(Objcsc.getId(), Objcsc.getId());
                    topStatesAdapter.selectItem(Objcsc.getId());

                } else {
                    selectedStatesMap.remove(Objcsc.getId());
                    topStatesAdapter.unCheckItems(Objcsc.getId());
                }
//============================================================
                if (!selectedIds.equals("")) {
                    // getCities(getComaSeparatedItemsFromMap(selectedCountriesMap) + "^" + selectedIds);
                    getCities(countriesAdapter.getCheckedItems() + "^" + statesAdapter.getCheckedItems());
                    selectStatesGetCities(getComaSeparatedItemsFromMap(selectedStatesMap), getComaSeparatedItemsFromMap(selectedCitiesMap));

                    topCitiesAdapter.unCheckAll();

                    defaultSelectionsObj.set_choice_state_ids(getComaSeparatedItemsFromMap(selectedStatesMap));

                } else {
                    tvMsgCities.setVisibility(View.VISIBLE);
                    citiesAdapter.clear();
                    defaultSelectionsObj.set_choice_state_ids(selectedIds);
                }
                break;
            case 0:
//cities
                if (isChecked) {

                    selectedCitiesMap.put(Objcsc.getId(), Objcsc.getId());
                    citiesAdapter.selectItem(getComaSeparatedItemsFromMap(selectedCitiesMap));
                    topCitiesAdapter.selectItem(getComaSeparatedItemsFromMap(selectedCitiesMap));

                } else {
                    topCitiesAdapter.unCheckItems(Objcsc.getId());
                    selectedCitiesMap.remove(Objcsc.getId());
                }
                defaultSelectionsObj.set_choice_cities_ids(selectedIds);

                break;
            case 3:
                //top cities selection

                if (isChecked) {
                    selectedCitiesMap.put(Objcsc.getId(), Objcsc.getId());
                    selectedStatesMap.put(Objcsc.getSid(), Objcsc.getSid());
                    selectedCountriesMap.put(Objcsc.getCid(), Objcsc.getCid());


                    countriesAdapter.selectItem(getComaSeparatedItemsFromMap(selectedCountriesMap));
                    topStatesAdapter.selectItem(getComaSeparatedItemsFromMap(selectedStatesMap
                    ));

                    getStates(getComaSeparatedItemsFromMap(selectedCountriesMap));
                    selectStatesGetCities(getComaSeparatedItemsFromMap(selectedStatesMap), getComaSeparatedItemsFromMap(selectedCitiesMap));
                    Log.e("comma", getComaSeparatedItemsFromMap(selectedCitiesMap));

                } else {
                    //uncheck only city here
                    selectedCitiesMap.remove(Objcsc.getId());
                    citiesAdapter.unCheckItems(Objcsc.getId());
                    Log.e("comma", getComaSeparatedItemsFromMap(selectedCitiesMap));
                }


                defaultSelectionsObj.set_choice_cities_ids(getComaSeparatedItemsFromMap(selectedCitiesMap));

                defaultSelectionsObj.set_choice_state_ids(getComaSeparatedItemsFromMap(selectedStatesMap));

                defaultSelectionsObj.set_choice_country_ids(getComaSeparatedItemsFromMap(selectedCountriesMap));


             /*     if (!selectedIds.equals("")) {



                  //    new GetStates(selectedIds, scCheck, Objcsc).execute();
                    Log.e("cid", "" + Objcsc.getCid() + "   " + selectedIds);
                    countriesAdapter.selectItem(Objcsc.getCid());

              *//*      if (selectedCountries != null) {
                        selectedCountries = selectedCountries + "," +selectedIds;
                    } else {
                        selectedCountries =selectedIds;
                    }*//*

                    // getStates(Objcsc.getCid());

                    Log.e("statesAdapter", statesAdapter.getCheckedItems());
                    String lastCheckedStates = statesAdapter.getCheckedItems();

                    getStates(countriesAdapter.getCheckedItems());
                    selectStatesGetCities(Objcsc, lastCheckedStates);


                } else {
                    //  statesAdapter.clear();
                    tvMsgCities.setVisibility(View.VISIBLE);
                    citiesAdapter.clear();
                    // countriesAdapter.clear();
                    // countriesAdapter.updateDataList(countriesDataList);

                }*/

                //   defaultSelectionsObj.set_choice_cities_ids(selectedIds);

                break;

            case 4:
                //top states selection
                //  Log.e("data obj", "cid: " + Objcsc.getCid() + " sid: " + Objcsc.getSid() + " id: " + Objcsc.getId());

                //cid= country id
                //id == state id

                if (isChecked) {

                    selectedStatesMap.put(Objcsc.getId(), Objcsc.getId());
                    selectedCountriesMap.put(Objcsc.getCid(), Objcsc.getCid());
                    countriesAdapter.selectItem(getComaSeparatedItemsFromMap(selectedCountriesMap));


                    getStates(getComaSeparatedItemsFromMap(selectedCountriesMap));
                    selectStatesGetCities(getComaSeparatedItemsFromMap(selectedStatesMap), getComaSeparatedItemsFromMap(selectedCitiesMap));

                } else {
                    selectedStatesMap.remove(Objcsc.getId());
                    statesAdapter.unCheckItems(Objcsc.getId());

                }


                defaultSelectionsObj.set_choice_state_ids(getComaSeparatedItemsFromMap(selectedStatesMap));

                defaultSelectionsObj.set_choice_country_ids(getComaSeparatedItemsFromMap(selectedCountriesMap));


                //    defaultSelectionsObj.set_choice_cities_ids(selectedIds);

             /*

                if (!selectedIds.equals("")) {
                    //   getCities(selectedCountries + "^" + selectedIds);


                    countriesAdapter.selectItem(Objcsc.getCid());

                    if (selectedCountries != null) {
                        selectedCountries = selectedCountries + "," + Objcsc.getCid();
                    } else {
                        selectedCountries = Objcsc.getCid();
                    }
                    getStates(selectedCountries);

                    getCities(Objcsc.getCid() + "^" + Objcsc.getId());

                    defaultSelectionsObj.set_choice_state_ids(Objcsc.getId());
                } else {
                    tvMsgCities.setVisibility(View.VISIBLE);
                    citiesAdapter.clear();
                    defaultSelectionsObj.set_choice_state_ids(selectedIds);
                }*/


                break;


            default:
                break;
        }

        updateDot();

    }

    public String getComaSeparatedItemsFromMap(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {

            String key = entry.getKey();
            String value = entry.getValue();

            if (stringBuilder.length() > 0)
                stringBuilder.append(",");
            stringBuilder.append(value);

        }
        return stringBuilder.toString();
    }

    private void selectStatesGetCities(final String selectedStates, final String selectedCities) {


        Thread MyThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(500);
                    if (!statesApiRunning) {
                        if (getActivity() == null)
                            return;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                statesAdapter.selectItem(selectedStates);





                              /*  String lastCheckedStatesa;
                                if (lastCheckedStates != null && !lastCheckedStates.equals("")) {
                                    lastCheckedStatesa = lastCheckedStates + "," + Objcsc.getSid();
                                } else {
                                    lastCheckedStatesa = Objcsc.getSid();
                                }*/

                                //   Log.e("countriesAdapter 1", "" + lastCheckedStatesa);
                                //   statesAdapter.selectItem(lastCheckedStatesa);


                                // getCities(Objcsc.getSid());
                                //getCities(Objcsc.getCid() + "^" + Objcsc.getSid());

                                Log.e("countriesAdapter 2", "" + countriesAdapter.getCheckedItems() + "^" + statesAdapter.getCheckedItems());
                                getCities(countriesAdapter.getCheckedItems() + "^" + statesAdapter.getCheckedItems());
                                selectCity(selectedCities);
                                topStatesAdapter.selectItem(selectedStates);
                            }
                        });

                        // MyThread.stop();


                    } else {
                        sleep(500);
                    }
                    //   MyThread.stop();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        MyThread.start();


    }


    private void selectCity(final String sCities) {
        Thread MyThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(500);
                    if (!citiesApiRunning) {

                        if (getActivity() == null)
                            return;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                citiesAdapter.selectItem(sCities);
                                topCitiesAdapter.selectItem(sCities);

                            }
                        });

                        // MyThread.stop();


                    } else {
                        sleep(500);
                    }
                    //   MyThread.stop();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        MyThread.start();

    }


    private void getStates(final String country_id) {

        statesApiRunning = true;
        Log.e("getStates", Urls.getStates + country_id);
        final ProgressDialog pD = new ProgressDialog(getContext());
        pD.setMessage("Loading...");
        pD.setCancelable(false);
        pD.show();

        JsonArrayRequest req = new JsonArrayRequest(Urls.getStates + country_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                        if (!response.isNull(0)) {
                            pD.dismiss();

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
                                statesApiRunning = false;

                            } catch (JSONException e) {
                                e.printStackTrace();
                                statesApiRunning = false;
                            }
                        }

                        pD.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                statesApiRunning = false;
                pD.dismiss();
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
        citiesApiRunning = true;
        final ProgressDialog pD = new ProgressDialog(getContext());
        pD.setMessage("Loading...");
        pD.setCancelable(false);
        pD.show();
        Log.e("getCities", Urls.getCities + country_id);
        JsonArrayRequest req = new JsonArrayRequest(Urls.getCities + country_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                        try {

                            pD.dismiss();
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
                            citiesApiRunning = false;

                        } catch (JSONException e) {
                            e.printStackTrace();
                            citiesApiRunning = false;
                        }

                        // pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pD.dismiss();
                citiesApiRunning = false;
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


        etCountrySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", s.toString() + "");
                countriesAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etStatesSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", s.toString() + "");
                statesAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etCitySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", s.toString() + "");
                citiesAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        defaultSelectionsObj.set_choice_visa_status_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchVisaStatus));
        updateDot();
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {

            if (getTargetFragment() != null) {
                fragmentInteractionListener = (OnChildFragmentInteractionListener) getTargetFragment();
            } else {
                fragmentInteractionListener = (OnChildFragmentInteractionListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }

    public interface OnChildFragmentInteractionListener {
        void messageFromChildToParent();
    }

    private void updateDot() {
        fragmentInteractionListener.messageFromChildToParent();

    }


}
