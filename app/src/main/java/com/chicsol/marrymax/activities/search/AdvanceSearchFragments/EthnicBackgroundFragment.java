package com.chicsol.marrymax.activities.search.AdvanceSearchFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.List;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;
import static com.chicsol.marrymax.utils.Constants.jsonArraySearch;

public class EthnicBackgroundFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private LinearLayout LinearLayoutAdvSearchEthnicBackground, LinearLayoutAdvSearchReligiousSect, LinearLayoutAdvSearchCaste;

    private ViewGenerator viewGenerator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adv_search_fragment_ethnic_background,
                container, false);
        initialize(view);


        return view;
    }

    private void initialize(View view) {

        viewGenerator = new ViewGenerator(getContext());
        LinearLayoutAdvSearchEthnicBackground = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchEthnicBackground);
        LinearLayoutAdvSearchReligiousSect = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchReligiousSect);
        LinearLayoutAdvSearchCaste = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchCaste);

        Gson gsonc;
        GsonBuilder gsonBuilderc = new GsonBuilder();
        gsonc = gsonBuilderc.create();
        Type listType = new TypeToken<List<WebArd>>() {
        }.getType();


        try {
            List<WebArd> dataList0 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(6).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLLWithTag(dataList0, LinearLayoutAdvSearchEthnicBackground, "ethnic");

            List<WebArd> dataList1 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(18).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLLWithTag(dataList1, LinearLayoutAdvSearchReligiousSect, "religious");


            List<WebArd> dataList2 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(23).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLLWithTag(dataList2, LinearLayoutAdvSearchCaste, "caste");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setSelection();
        setListeners();
    }

    private void setSelection() {

        if (defaultSelectionsObj != null) {

            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchEthnicBackground, defaultSelectionsObj.get_choice_ethnic_bground_ids());
            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchReligiousSect, defaultSelectionsObj.get_choice_religious_sect_ids());
            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchCaste, defaultSelectionsObj.get_choice_caste_ids());
        }

    }

    private void setListeners() {
        {
            int childcount = LinearLayoutAdvSearchEthnicBackground.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = LinearLayoutAdvSearchEthnicBackground.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }
        {
            int childcount = LinearLayoutAdvSearchReligiousSect.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = LinearLayoutAdvSearchReligiousSect.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }
        {
            int childcount = LinearLayoutAdvSearchCaste.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = LinearLayoutAdvSearchCaste.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView != null) {

            if (buttonView.getTag() != null) {

                if (buttonView.getTag().equals("ethnic")) {
                    defaultSelectionsObj.set_choice_ethnic_bground_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchEthnicBackground));
                }
                if (buttonView.getTag().equals("religious")) {
                    defaultSelectionsObj.set_choice_religious_sect_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchReligiousSect));
                }
                if (buttonView.getTag().equals("caste")) {
                    defaultSelectionsObj.set_choice_caste_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchCaste));
                }
            }
        }
    }
}
