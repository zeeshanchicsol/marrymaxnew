package com.chicsol.marrymax.activities.search.AdvanceSearchFragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.DrawerActivity;
import com.chicsol.marrymax.activities.search.SearchMainActivity;
import com.chicsol.marrymax.adapters.AdvSearchAdapter;
import com.chicsol.marrymax.modal.mAdvSearchListing;

import java.util.ArrayList;
import java.util.List;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;

public class ListViewAdvSearchFragment extends Fragment implements BasicsFragment.OnChildFragmentInteractionListener, AppearanceFragment.OnChildFragmentInteractionListener, MaritalStatusFragment.OnChildFragmentInteractionListener, EduOccupFragment.OnChildFragmentInteractionListener, EthnicBackgroundFragment.OnChildFragmentInteractionListener, LifeStyle1Fragment.OnChildFragmentInteractionListener, LifeStyle2Fragment.OnChildFragmentInteractionListener, GeographyFragment.OnChildFragmentInteractionListener {

    View lasView = null;
    List<mAdvSearchListing> dataList;
    mAdvSearchListing last_selected_obj;
    //private ArrayAdapter<Item> adapter;
    private AdvSearchAdapter advSearchAdapter;
    private ListView lvItems;
    private OnItemSelectedListener listener;
    private ProgressDialog pDialog;
    boolean basicsSelected = false, appearanceSelected = false, eduOccuSelected = false, ethnicSelected = false, geographySelected = false, lifestyle1Selected = false, lifestyle2Selected = false, maritalSelected = false;

    public static ListViewAdvSearchFragment newInstance() {
        ListViewAdvSearchFragment fragment = new ListViewAdvSearchFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement ListViewAdvSearchFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //   Log.e("Search after", "after");
/*        if (SearchMainActivity.searchKey != null) {
            getSelectedSearchObject(SearchMainActivity.searchKey);
        } else {*/
        ///  getRawData();

        //if (defaultSelectionsObj == null) {
       /*if( DrawerActivity.rawSearchObj!=null) {
           Log.e("not null","raw object");
           defaultSelectionsObj = DrawerActivity.rawSearchObj;
       }*/
        // }
        listener.onItemSelected(dataList.get(0));
        //   }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    private void initialize(View view) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");

        lvItems = (ListView) view.findViewById(R.id.lvItems);

        dataList = new ArrayList<>();
        initDataList();

        //  dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu3, "Snow"));
        //   dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu4, "Storm"));
           /*      = new mAdvSearchListing[]
                {
                       ,
                        new mAdvSearchListing(R.drawable.ic_adv_s_menu2, "Showers"),
                        new mAdvSearchListing(R.drawable.ic_adv_s_menu3, "Snow"),
                        new mAdvSearchListing(R.drawable.ic_adv_s_menu4, "Storm"),

                };*/

        Log.e("Adaper Initialisezddd", "resetttttttttttt");
        advSearchAdapter = new AdvSearchAdapter(getActivity(), R.layout.adv_search_item_listview, dataList);
        lvItems.setAdapter(advSearchAdapter);


    }

    private void initDataList() {
        updateAllSelectionsAccordingToSearchCategories();

        BasicsFragment basicsFragment = new BasicsFragment();
        basicsFragment.setTargetFragment(ListViewAdvSearchFragment.this, 0);

        AppearanceFragment appearanceFragment = new AppearanceFragment();
        appearanceFragment.setTargetFragment(ListViewAdvSearchFragment.this, 0);

        MaritalStatusFragment maritalStatusFragment = new MaritalStatusFragment();
        maritalStatusFragment.setTargetFragment(ListViewAdvSearchFragment.this, 0);

        EduOccupFragment eduOccupFragment = new EduOccupFragment();
        eduOccupFragment.setTargetFragment(ListViewAdvSearchFragment.this, 0);

        EthnicBackgroundFragment ethnicBackgroundFragment = new EthnicBackgroundFragment();
        ethnicBackgroundFragment.setTargetFragment(ListViewAdvSearchFragment.this, 0);

        GeographyFragment geographyFragment = new GeographyFragment();
        geographyFragment.setTargetFragment(ListViewAdvSearchFragment.this, 0);

        LifeStyle1Fragment lifeStyle1Fragment = new LifeStyle1Fragment();
        lifeStyle1Fragment.setTargetFragment(ListViewAdvSearchFragment.this, 0);

        LifeStyle2Fragment lifeStyle2Fragment = new LifeStyle2Fragment();
        lifeStyle2Fragment.setTargetFragment(ListViewAdvSearchFragment.this, 0);


        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu1, R.drawable.ic_adv_s_menu1_active, "Cloudy", basicsFragment, basicsSelected));
        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu2, R.drawable.ic_adv_s_menu2_active, "Showers", appearanceFragment, appearanceSelected));
        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu3, R.drawable.ic_adv_s_menu3_active, "Showers", maritalStatusFragment, maritalSelected));
        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu4, R.drawable.ic_adv_s_menu4_active, "Showers", eduOccupFragment, eduOccuSelected));

        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu5, R.drawable.ic_adv_s_menu5_active, "Showers", ethnicBackgroundFragment, ethnicSelected));
        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu6, R.drawable.ic_adv_s_menu6_active, "Showers", geographyFragment, geographySelected));
        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu7, R.drawable.ic_adv_s_menu7_active, "Showers", lifeStyle1Fragment, lifestyle1Selected));
        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu7, R.drawable.ic_adv_s_menu7_active, "Showers", lifeStyle2Fragment, lifestyle2Selected));


    }

    private boolean checkStringWith0andNull(String str) {
        if (str != null) {
            if (!TextUtils.isEmpty(str)) {

                if (!str.equals("0")) {
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    private void setListeners() {
        lvItems.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long rowId) {

                // Toast.makeText(getActivity().getApplicationContext(), "Response "+position, Toast.LENGTH_SHORT).show();
                // Retrieve item_slider based on position
                //	Item i = adapter.getItem(position);
                // Fire selected event for item_slider

                if (lasView != null) {
                    lasView.findViewById(R.id.ViewHorizontalLine).setVisibility(View.INVISIBLE);
                    lasView.findViewById(R.id.LinearLayoutAdvSearchItem).setBackgroundColor(Color.TRANSPARENT);
                    ((ImageView) lasView.findViewById(R.id.ImageViewAdvSearchIcon)).setImageResource(last_selected_obj.icon);
                }
                view.findViewById(R.id.ViewHorizontalLine).setVisibility(View.VISIBLE);
                view.findViewById(R.id.LinearLayoutAdvSearchItem).setBackgroundColor(Color.WHITE);

                view.setSelected(true);
                lasView = view;
                last_selected_obj = dataList.get(position);
                ((ImageView) lasView.findViewById(R.id.ImageViewAdvSearchIcon)).setImageResource(dataList.get(position).icon_selected);

                listener.onItemSelected(dataList.get(position));


            }
        });
        //   lvItems.setSelection(0);
        /*lvItems.set*/
        //  listener.onItemSelected(dataList.get(0));


    //  getActivity().setMenuUpdateListener
      //    SearchMainActivity.set

    }
    public  void selectSearchFragment( int position){
        listener.onItemSelected(dataList.get(position));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adv_search_fragment_items_list, container,
                false);

        initialize(view);
        setListeners();
        // lvItems.getSelectedView().setSelected(true);
        return view;
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        lvItems.setChoiceMode(
                activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE);
    }

/*    //search selection data e
    private void getSelectedSearchObject(String searchid) {


        pDialog.show();
        Log.e("getRawData started=====", "=========================" + Urls.getSrhRawData + searchid);
        JsonArrayRequest req = new JsonArrayRequest(Urls.getSrhRawData + searchid,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response SRH", response.toString());
                        Log.e("getRawData finished===", "==========================");
                        listener.onItemSelected(dataList.get(0));
                        try {


                            JSONObject jsonCountryStaeObj = response.getJSONArray(0).getJSONObject(0);
                            Log.e("Response 222", jsonCountryStaeObj.toString());

                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<Members>() {
                            }.getType();

                            defaultSelectionsObj = gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                            Log.e("body ids rawww", ListViewAdvSearchFragment.defaultSelectionsObj.get_choice_smoking_ids() + "  --");
                            listener.onItemSelected(dataList.get(0));
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
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(req);
    }

    //search selection data e
    private void getRawData() {


        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        Log.e("getRawData started", Urls.getRawData + SharedPreferenceManager.getUserObject(getContext()).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getRawData + SharedPreferenceManager.getUserObject(getContext()).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                        try {
                            Log.e("getRawData finished===", "==========================");

                            JSONObject jsonCountryStaeObj = response.getJSONArray(0).getJSONObject(0);
                            Log.e("Response 222", jsonCountryStaeObj.toString());

                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<Members>() {
                            }.getType();

                            defaultSelectionsObj = gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                            //      Log.e("body ids rawww", ListViewAdvSearchFragment.defaultSelectionsObj.get_choice_body_ids() + "  --");
                            listener.onItemSelected(dataList.get(0));
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
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(req);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }

    public void resetSearch() {

        if (advSearchAdapter != null) {
            advSearchAdapter.reset();
        } else {

            ///  Toast.makeText(getContext(), "Null", Toast.LENGTH_SHORT).show();
        }

        //Toast.makeText(getContext(), "Clickec", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void messageFromChildToParent() {

        advSearchAdapter.clear();
        initDataList();


        advSearchAdapter.notifyDataSetChanged();
    }



    public interface OnItemSelectedListener {
        public void onItemSelected(mAdvSearchListing i);
    }


    private void updateAllSelectionsAccordingToSearchCategories() {

        if (defaultSelectionsObj != null) {

//===============Basics=======================

            if (defaultSelectionsObj.get_pictureonly() != 0 || defaultSelectionsObj.get_opentopublic() != 0 || defaultSelectionsObj.get_registration_within_id() != 0 || defaultSelectionsObj.get_last_login_date_id() != 0 || checkStringWith0andNull(defaultSelectionsObj.get_choice_profile_owner_Ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_zodiac_sign_ids())) {
                basicsSelected = true;
            } else {
                basicsSelected = false;
            }


//================Appearance===============
            if (defaultSelectionsObj.get_choice_age_from() != 18 || defaultSelectionsObj.get_choice_age_upto() != 70 || defaultSelectionsObj.get_choice_height_from_id() != 1 || defaultSelectionsObj.get_choice_height_to_id() != 31 || checkStringWith0andNull(defaultSelectionsObj.get_choice_body_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_complexion_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_hair_color_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_eye_color_ids())
                    ) {
                appearanceSelected = true;
            } else {
                appearanceSelected = false;
            }
//================Marital Status===============
            if (checkStringWith0andNull(defaultSelectionsObj.get_choice_marital_status_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_children_ids())) {
                maritalSelected = true;
            } else {
                maritalSelected = false;
            }
//================Edu Occupation===============
            if (checkStringWith0andNull(defaultSelectionsObj.get_choice_education_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_occupation_ids())) {
                eduOccuSelected = true;
            } else {
                eduOccuSelected = false;
            }
//================Ethnic Background===============
            if (checkStringWith0andNull(defaultSelectionsObj.get_choice_ethnic_bground_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_religious_sect_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_caste_ids())) {
                ethnicSelected = true;
            } else {
                ethnicSelected = false;
            }
//================Lifestyle 1 ===============
            if (checkStringWith0andNull(defaultSelectionsObj.get_choice_raised_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_hijab_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_family_values_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_living_arangment_ids())) {
                lifestyle1Selected = true;
            } else {
                lifestyle1Selected = false;
            }
//================Lifestyle 2 ===============
            if (checkStringWith0andNull(defaultSelectionsObj.get_choice_sibling_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_smoking_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_drink_ids())) {
                lifestyle2Selected = true;
            } else {
                lifestyle2Selected = false;
            }
//================Geography  ===============
            if (checkStringWith0andNull(defaultSelectionsObj.get_choice_country_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_state_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_cities_ids()) || checkStringWith0andNull(defaultSelectionsObj.get_choice_visa_status_ids())) {
                geographySelected = true;
            } else {
                geographySelected = false;
            }

        }
    }


}
