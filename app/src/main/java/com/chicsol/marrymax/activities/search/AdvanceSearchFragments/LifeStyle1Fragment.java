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


public class LifeStyle1Fragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private LinearLayout LinearLayoutAdvSearchRaisedWhere, LinearLayoutAdvSearchHijab, LinearLayoutAdvSearchFamilyValues, LinearLayoutAdvSearchLivingArrangement;

    private ViewGenerator viewGenerator;
    private OnChildFragmentInteractionListener fragmentInteractionListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adv_search_fragment_lifestyle1,
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

        viewGenerator = new ViewGenerator(getContext());
        LinearLayoutAdvSearchRaisedWhere = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchRaisedWhere);
        LinearLayoutAdvSearchHijab = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchHijab);
        LinearLayoutAdvSearchFamilyValues = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchFamilyValues);
        LinearLayoutAdvSearchLivingArrangement = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchLivingArrangement);


        Gson gsonc;
        GsonBuilder gsonBuilderc = new GsonBuilder();
        gsonc = gsonBuilderc.create();
        Type listType = new TypeToken<List<WebArd>>() {
        }.getType();


        try {
            List<WebArd> dataList0 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(17).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLLWithTag(dataList0, LinearLayoutAdvSearchRaisedWhere, "raised");

            List<WebArd> dataList1 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(11).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLLWithTag(dataList1, LinearLayoutAdvSearchHijab, "hijab");


            List<WebArd> dataList2 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(8).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLLWithTag(dataList2, LinearLayoutAdvSearchFamilyValues, "family");

            List<WebArd> dataList3 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(13).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLLWithTag(dataList3, LinearLayoutAdvSearchLivingArrangement, "living");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSelection() {

        if (defaultSelectionsObj != null) {

            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchRaisedWhere, defaultSelectionsObj.get_choice_raised_ids());
            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchHijab, defaultSelectionsObj.get_choice_hijab_ids());
            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchFamilyValues, defaultSelectionsObj.get_choice_family_values_ids());
            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchLivingArrangement, defaultSelectionsObj.get_choice_living_arangment_ids());

        }



    }

    private void setListeners() {
        {
            int childcount = LinearLayoutAdvSearchRaisedWhere.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = LinearLayoutAdvSearchRaisedWhere.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }
        {
            int childcount = LinearLayoutAdvSearchHijab.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = LinearLayoutAdvSearchHijab.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }
        {
            int childcount = LinearLayoutAdvSearchFamilyValues.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = LinearLayoutAdvSearchFamilyValues.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }
        {
            int childcount = LinearLayoutAdvSearchLivingArrangement.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = LinearLayoutAdvSearchLivingArrangement.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getTag() != null) {
            if (buttonView.getTag().equals("raised")) {
                defaultSelectionsObj.set_choice_raised_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchRaisedWhere));
            } else if (buttonView.getTag().equals("hijab")) {
                defaultSelectionsObj.set_choice_hijab_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchHijab));
            } else if (buttonView.getTag().equals("family")) {
                defaultSelectionsObj.set_choice_family_values_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchFamilyValues));

            } else if (buttonView.getTag().equals("living")) {
                defaultSelectionsObj.set_choice_living_arangment_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchLivingArrangement));
            }
        }
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
