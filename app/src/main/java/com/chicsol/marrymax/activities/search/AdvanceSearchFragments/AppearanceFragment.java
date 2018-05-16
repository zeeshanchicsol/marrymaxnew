package com.chicsol.marrymax.activities.search.AdvanceSearchFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.MySpinnerAdapter;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;
import static com.chicsol.marrymax.utils.Constants.jsonArraySearch;

public class AppearanceFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private LinearLayout llAdvSearchEyeColor, llAdvSearchHairColor, llAdvSearchComplexion, llAdvSearchPhysique;
    private ViewGenerator viewGenerator;
    private Spinner spAgeFrom, spAgeTo, spHeightFrom, spHeightTo;
    private MySpinnerAdapter spinnerAdapterAgeFrom, spinnerAdapterAgeTo, spinnerAdapterHeightFrom, spinnerAdapterHeightTo;
    private List<WebArd> agefromDataList, ageToDataList, heightFromDataList, heightToDataList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adv_search_fragment_appearance,
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
        List<WebArd> physiqueDataList;
        viewGenerator = new ViewGenerator(getContext());
        llAdvSearchEyeColor = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchEyeColor);
        llAdvSearchHairColor = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchHairColor);
        llAdvSearchComplexion = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchComplexion);
        llAdvSearchPhysique = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchPhysique);
        spAgeFrom = (Spinner) view.findViewById(R.id.SpinnerAdvSearchAgeFrom);
        spAgeTo = (Spinner) view.findViewById(R.id.SpinnerAdvSearchAgeTo);
        spHeightFrom = (Spinner) view.findViewById(R.id.SpinnerAdvSearchHeightFrom);
        spHeightTo = (Spinner) view.findViewById(R.id.SpinnerAdvSearchHeightTo);

        llAdvSearchEyeColor.removeAllViews();


        agefromDataList = new ArrayList<>();
        ageToDataList = new ArrayList<>();
        heightFromDataList = new ArrayList<>();
        heightToDataList = new ArrayList<>();


        spinnerAdapterAgeFrom = new MySpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item, agefromDataList);
        spinnerAdapterAgeFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAgeFrom.setAdapter(spinnerAdapterAgeFrom);

        spinnerAdapterAgeTo = new MySpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item, ageToDataList);
        spinnerAdapterAgeTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAgeTo.setAdapter(spinnerAdapterAgeTo);


        spinnerAdapterHeightFrom = new MySpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item, heightFromDataList);
        spinnerAdapterHeightFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHeightFrom.setAdapter(spinnerAdapterHeightFrom);

        spinnerAdapterHeightTo = new MySpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item, heightToDataList);
        spinnerAdapterHeightTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHeightTo.setAdapter(spinnerAdapterHeightTo);


        Gson gsonc;
        GsonBuilder gsonBuilderc = new GsonBuilder();
        gsonc = gsonBuilderc.create();
        Type listType = new TypeToken<List<WebArd>>() {
        }.getType();


        try {
            physiqueDataList = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(0).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLL(physiqueDataList, llAdvSearchPhysique);

            List<WebArd> dataList1 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(2).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLL(dataList1, llAdvSearchComplexion);


            List<WebArd> dataList2 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(9).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLL(dataList2, llAdvSearchHairColor);


            List<WebArd> dataList3 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(7).toString(), listType);


            viewGenerator.generateDynamicCheckBoxesLL(dataList3, llAdvSearchEyeColor);


            List<WebArd> dataListHeight = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(10).toString(), listType);

            if (dataListHeight.size() > 0) {

                dataListHeight.add(0, new WebArd("0", "Select"));
            }

            spinnerAdapterHeightFrom.updateDataList(dataListHeight);
            spinnerAdapterHeightTo.updateDataList(dataListHeight);


            List<WebArd> ageDataList = new ArrayList<>();
            for (int i = 17; i <= 70; i++) {

                if (i != 17) {

                    WebArd webArd = new WebArd(String.valueOf(i), i + " Years");
                    ageDataList.add(webArd);
                } else {
                    WebArd webArd = new WebArd(String.valueOf(0), " Select");
                    ageDataList.add(webArd);

                }


            }

            spinnerAdapterAgeFrom.updateDataList(ageDataList);
            spinnerAdapterAgeTo.updateDataList(ageDataList);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSelection() {


        if (defaultSelectionsObj != null) {

            viewGenerator.selectCheckBoxes(llAdvSearchPhysique, defaultSelectionsObj.get_choice_body_ids());
            viewGenerator.selectCheckBoxes(llAdvSearchComplexion, defaultSelectionsObj.get_choice_complexion_ids());
            viewGenerator.selectCheckBoxes(llAdvSearchHairColor, defaultSelectionsObj.get_choice_hair_color_ids());
            viewGenerator.selectCheckBoxes(llAdvSearchEyeColor, defaultSelectionsObj.get_choice_eye_color_ids());
            //viewGenerator.selectCheckBoxes(llAdvSearchComplexion, members.get_choice_zodiac_sign_ids());

            Log.e("Eye colorrrrr", "Selection  height  " + defaultSelectionsObj.get_choice_height_from_id());
         //   Toast.makeText(getContext(), "== "+defaultSelectionsObj.get_choice_age_from(), Toast.LENGTH_SHORT).show();

            if(defaultSelectionsObj.get_choice_age_from()==0){
                defaultSelectionsObj.set_choice_age_from(18);
            }
            if(defaultSelectionsObj.get_choice_age_upto()==0){
                defaultSelectionsObj.set_choice_age_upto(70);
            }

            if(defaultSelectionsObj.get_choice_height_from_id()==0){
                defaultSelectionsObj.set_choice_height_from_id(Long.parseLong(heightFromDataList.get(1).getId()));
            }
            if(defaultSelectionsObj.get_choice_height_to_id()==0){
                defaultSelectionsObj.set_choice_height_to_id(Long.parseLong(heightFromDataList.get(heightFromDataList.size()-1).getId()));
            }

            viewGenerator.selectSpinnerItemById(spAgeFrom, defaultSelectionsObj.get_choice_age_from(), agefromDataList);
            viewGenerator.selectSpinnerItemById(spAgeTo, defaultSelectionsObj.get_choice_age_upto(), ageToDataList);

            viewGenerator.selectSpinnerItemById(spHeightFrom, defaultSelectionsObj.get_choice_height_from_id(), heightFromDataList);
            viewGenerator.selectSpinnerItemById(spHeightTo, defaultSelectionsObj.get_choice_height_to_id(), heightToDataList);

        }

    }

    private void setListeners() {

        spAgeFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    WebArd selectedItem = (WebArd) spAgeFrom.getSelectedItem();
                    defaultSelectionsObj.set_choice_age_from(Long.parseLong(selectedItem.getId()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spAgeTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    WebArd selectedItem = (WebArd) spAgeTo.getSelectedItem();
                    defaultSelectionsObj.set_choice_age_upto(Long.parseLong(selectedItem.getId()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spHeightFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    WebArd selectedItem = (WebArd) spHeightFrom.getSelectedItem();
                    defaultSelectionsObj.set_choice_height_from_id(Long.parseLong(selectedItem.getId()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spHeightTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    WebArd selectedItem = (WebArd) spHeightTo.getSelectedItem();
                    defaultSelectionsObj.set_choice_height_to_id(Long.parseLong(selectedItem.getId()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        {
            int childcount = llAdvSearchPhysique.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = llAdvSearchPhysique.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }


        {
            int childcount = llAdvSearchComplexion.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = llAdvSearchComplexion.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }

        {
            int childcount = llAdvSearchHairColor.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = llAdvSearchHairColor.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }

        {
            int childcount = llAdvSearchEyeColor.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = llAdvSearchEyeColor.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }


    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        defaultSelectionsObj.set_choice_body_ids(viewGenerator.getSelectionFromCheckbox(llAdvSearchPhysique));
        defaultSelectionsObj.set_choice_complexion_ids(viewGenerator.getSelectionFromCheckbox(llAdvSearchComplexion));
        defaultSelectionsObj.set_choice_hair_color_ids(viewGenerator.getSelectionFromCheckbox(llAdvSearchHairColor));
        defaultSelectionsObj.set_choice_eye_color_ids(viewGenerator.getSelectionFromCheckbox(llAdvSearchEyeColor));
    }

/*    // ItemDetailFragment.newInstance(item_image_slider)
    public static AppearanceFragment newInstance(String item_image_slider) {
    	AppearanceFragment fragmentDemo = new AppearanceFragment();
        Bundle args = new Bundle();
        args.putString("item_image_slider", item_image_slider);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }*/

}
