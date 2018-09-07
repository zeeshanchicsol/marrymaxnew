package com.chicsol.marrymax.activities.search.AdvanceSearchFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.SearchCheckBoxAdapter;
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

public class EthnicBackgroundFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, SearchCheckBoxAdapter.ContactsAdapterListener {

    private LinearLayout LinearLayoutAdvSearchEthnicBackground, LinearLayoutAdvSearchReligiousSect, LinearLayoutAdvSearchCaste;

    private ViewGenerator viewGenerator;
    private EditText etCasteSearch;

    private RecyclerView recycler_view_caste;
    private SearchCheckBoxAdapter mAdapter;

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
        setListenersD();

        return view;
    }

    private void initialize(View view) {


        viewGenerator = new ViewGenerator(getContext());
        LinearLayoutAdvSearchEthnicBackground = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchEthnicBackground);
        LinearLayoutAdvSearchReligiousSect = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchReligiousSect);
        LinearLayoutAdvSearchCaste = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchCaste);

        etCasteSearch = (EditText) view.findViewById(R.id.EditTextAdvSearchEthnicBackgrounCasteSearch);
        recycler_view_caste = (RecyclerView) view.findViewById(R.id.recycler_view_caste);


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
            //    viewGenerator.generateDynamicCheckBoxesLLWithTag(dataList2, LinearLayoutAdvSearchCaste, "caste");
            if (defaultSelectionsObj.get_choice_caste_ids() != null) {
                dataList2 = checkTrueSelected(dataList2);
            }

            //  defaultSelectionsObj.get_choice_caste_ids()


            mAdapter = new SearchCheckBoxAdapter(getContext(), dataList2, this);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recycler_view_caste.setLayoutManager(mLayoutManager);
            recycler_view_caste.setItemAnimator(new DefaultItemAnimator());
            // recycler_view_caste.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
            recycler_view_caste.setAdapter(mAdapter);

            Log.e("Logggeeee", "" + defaultSelectionsObj.get_choice_caste_ids());


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

    private void setListenersD() {


        etCasteSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", s.toString() + "");
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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


    private List<WebArd> checkTrueSelected(List<WebArd> dataList2) {

        String[] seletedIds = defaultSelectionsObj.get_choice_caste_ids().split(",");

        if (seletedIds.length > 0) {

            for (int i = 0; i < dataList2.size(); i++) {
                WebArd obja = dataList2.get(i);

                for (int j = 0; j < seletedIds.length; j++) {
                    if (obja.getId().equals(seletedIds[j])) {
                        dataList2.get(i).setSelected(true);
                    }
                }

            }
            return dataList2;

        } else {
            return dataList2;
        }

    }

    @Override
    public void onContactSelected(List<WebArd> dataList) {




        StringBuilder sbSelectedVisaMyChoice = new StringBuilder();

        for (int i = 0; i < dataList.size(); i++) {


            if (dataList.get(i).isSelected()) {
                sbSelectedVisaMyChoice.append(dataList.get(i).getId());
                if (i != dataList.size() - 1) {
                    sbSelectedVisaMyChoice.append(",");
                }
            }


        }
        if (sbSelectedVisaMyChoice.length() == 0) {


            defaultSelectionsObj.set_choice_caste_ids("0");
        }

        Character ch = new Character(',');
        if (sbSelectedVisaMyChoice.length() > 0) {
            if (sbSelectedVisaMyChoice.charAt(sbSelectedVisaMyChoice.length() - 1) == ch) {
                sbSelectedVisaMyChoice.deleteCharAt(sbSelectedVisaMyChoice.length() - 1);
            }

        }


        defaultSelectionsObj.set_choice_caste_ids(clearCommarEnd(sbSelectedVisaMyChoice).toString());

    }

    private StringBuilder clearCommarEnd(StringBuilder stringBuilder) {

        Character ch = new Character(',');
        if (stringBuilder.length() > 0) {

            if (stringBuilder.charAt(stringBuilder.length() - 1) == ch) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
        }

        return stringBuilder;
    }
}
