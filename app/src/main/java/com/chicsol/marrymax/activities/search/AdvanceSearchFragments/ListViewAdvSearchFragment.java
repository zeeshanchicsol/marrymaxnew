package com.chicsol.marrymax.activities.search.AdvanceSearchFragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.DrawerActivity;
import com.chicsol.marrymax.adapters.AdvSearchAdapter;
import com.chicsol.marrymax.modal.mAdvSearchListing;

import java.util.ArrayList;
import java.util.List;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;

public class ListViewAdvSearchFragment extends Fragment {

    View lasView = null;
    List<mAdvSearchListing> dataList;
    mAdvSearchListing last_selected_obj;
    //private ArrayAdapter<Item> adapter;
    private AdvSearchAdapter advSearchAdapter;
    private ListView lvItems;
    private OnItemSelectedListener listener;
    private ProgressDialog pDialog;

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
        Log.e("Search after", "after");
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

        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu1, R.drawable.ic_adv_s_menu1_active, "Cloudy", new BasicsFragment()));
        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu2, R.drawable.ic_adv_s_menu2_active, "Showers", new AppearanceFragment()));
        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu3, R.drawable.ic_adv_s_menu3_active, "Showers", new MaritalStatusFragment()));
        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu4, R.drawable.ic_adv_s_menu4_active, "Showers", new EduOccupFragment()));

        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu5, R.drawable.ic_adv_s_menu5_active, "Showers", new EthnicBackgroundFragment()));
        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu6, R.drawable.ic_adv_s_menu6_active, "Showers", new GeographyFragment()));
        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu7, R.drawable.ic_adv_s_menu7_active, "Showers", new LifeStyle1Fragment()));
        dataList.add(new mAdvSearchListing(R.drawable.ic_adv_s_menu7, R.drawable.ic_adv_s_menu7_active, "Showers", new LifeStyle2Fragment()));


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

        //   listener.onItemSelected(dataList.get(0));

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

                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.hide();
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

                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.hide();
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
            Log.e("Null", "Nullllllllllllllllllll");
            ///  Toast.makeText(getContext(), "Null", Toast.LENGTH_SHORT).show();
        }

        //Toast.makeText(getContext(), "Clickec", Toast.LENGTH_SHORT).show();
    }

    public interface OnItemSelectedListener {
        public void onItemSelected(mAdvSearchListing i);
    }
}
