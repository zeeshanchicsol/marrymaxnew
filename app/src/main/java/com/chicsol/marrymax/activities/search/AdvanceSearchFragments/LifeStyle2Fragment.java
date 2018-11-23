package com.chicsol.marrymax.activities.search.AdvanceSearchFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class LifeStyle2Fragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private LinearLayout LinearLayoutAdvSearchSiblingPosition, LinearLayoutAdvSearchSmoking, LinearLayoutAdvSearchDrink;

    private ViewGenerator viewGenerator;
    private OnChildFragmentInteractionListener fragmentInteractionListener;

    private Button ButtonResetSearchSiblingPosition, ButtonResetSearchSmoking, ButtonResetSearchDrink;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adv_search_fragment_lifestyle2,
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
        LinearLayoutAdvSearchSiblingPosition = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchSiblingPosition);
        LinearLayoutAdvSearchSmoking = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchSmoking);
        LinearLayoutAdvSearchDrink = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchDrink);


        ButtonResetSearchSiblingPosition = (Button) view.findViewById(R.id.ButtonResetSearchSiblingPosition);
        ButtonResetSearchSmoking = (Button) view.findViewById(R.id.ButtonResetSearchSmoking);
        ButtonResetSearchDrink = (Button) view.findViewById(R.id.ButtonResetSearchDrink);

        ButtonResetSearchSiblingPosition.setOnClickListener(this);
        ButtonResetSearchSmoking.setOnClickListener(this);
        ButtonResetSearchDrink.setOnClickListener(this);



        Gson gsonc;
        GsonBuilder gsonBuilderc = new GsonBuilder();
        gsonc = gsonBuilderc.create();
        Type listType = new TypeToken<List<WebArd>>() {
        }.getType();


        try {
            List<WebArd> dataList0 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(19).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLLWithTag(dataList0, LinearLayoutAdvSearchSiblingPosition, "sibling");

            List<WebArd> dataList1 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(20).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLLWithTag(dataList1, LinearLayoutAdvSearchSmoking, "smoke");


            List<WebArd> dataList2 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(4).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLLWithTag(dataList2, LinearLayoutAdvSearchDrink, "drink");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSelection() {

        if (defaultSelectionsObj != null) {

            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchSiblingPosition, defaultSelectionsObj.get_choice_sibling_ids());
            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchSmoking, defaultSelectionsObj.get_choice_smoking_ids());
            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchDrink, defaultSelectionsObj.get_choice_drink_ids());

        }


    }

    private void setListeners() {
        {
            int childcount = LinearLayoutAdvSearchSiblingPosition.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = LinearLayoutAdvSearchSiblingPosition.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }
        {
            int childcount = LinearLayoutAdvSearchSmoking.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = LinearLayoutAdvSearchSmoking.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }
        {
            int childcount = LinearLayoutAdvSearchDrink.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = LinearLayoutAdvSearchDrink.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getTag() != null) {
            //  Log.e("sibling ids",""+ defaultSelectionsObj.get_choice_raised_ids());
            if (buttonView.getTag().equals("sibling")) {
                defaultSelectionsObj.set_choice_sibling_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchSiblingPosition));


            }
            if (buttonView.getTag().equals("smoke")) {
                defaultSelectionsObj.set_choice_smoking_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchSmoking));
            }
            if (buttonView.getTag().equals("drink")) {
                defaultSelectionsObj.set_choice_drink_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchDrink));
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

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("siblingposition")) {
            defaultSelectionsObj.set_choice_sibling_ids("");

        } else if (v.getTag().equals("smoking")) {
            defaultSelectionsObj.set_choice_smoking_ids("");

        } else if (v.getTag().equals("drink")) {
            defaultSelectionsObj.set_choice_drink_ids("");

        }
        setSelection();
    }

    public interface OnChildFragmentInteractionListener {
        void messageFromChildToParent();
    }

    private void updateDot() {
        fragmentInteractionListener.messageFromChildToParent();

    }

}
