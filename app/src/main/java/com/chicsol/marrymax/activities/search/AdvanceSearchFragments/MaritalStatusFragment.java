package com.chicsol.marrymax.activities.search.AdvanceSearchFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

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


public class MaritalStatusFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    //private Item item_slider;
    private LinearLayout LinearLayoutAdvSearchMaritalStatus, LinearLayoutAdvSearchChildren;

    private ViewGenerator viewGenerator;
    private OnChildFragmentInteractionListener fragmentInteractionListener;

    private Button ButtonResetSearchMartialStatus, ButtonResetSearchChildren;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //item_image_slider = (Item) getArguments().getSerializable("item_image_slider");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adv_search_fragment_martial_status,
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
        LinearLayoutAdvSearchMaritalStatus = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchMaritalStatus);
        LinearLayoutAdvSearchChildren = (LinearLayout) view.findViewById(R.id.LinearLayoutAdvSearchChildren);

        ButtonResetSearchMartialStatus = (Button) view.findViewById(R.id.ButtonResetSearchMartialStatus);
        ButtonResetSearchChildren = (Button) view.findViewById(R.id.ButtonResetSearchChildren);

        ButtonResetSearchMartialStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    resetSelections(v);

                defaultSelectionsObj.set_choice_marital_status_ids("");
                if (defaultSelectionsObj != null) {
                    viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchMaritalStatus, defaultSelectionsObj.get_choice_marital_status_ids());

                }
            }
        });
        ButtonResetSearchChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
                //   resetSelections(v);

                //    Log.e("before", "=  " + defaultSelectionsObj.get_choice_children_ids());
                defaultSelectionsObj.set_choice_children_ids("");
                //  Log.e("after", "=  " + defaultSelectionsObj.get_choice_children_ids());
                if (defaultSelectionsObj != null) {
                    viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchChildren, defaultSelectionsObj.get_choice_children_ids());


                }
            }
        });

        Gson gsonc;
        GsonBuilder gsonBuilderc = new GsonBuilder();
        gsonc = gsonBuilderc.create();
        Type listType = new TypeToken<List<WebArd>>() {
        }.getType();


        try {
            List<WebArd> dataList0 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(14).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLL(dataList0, LinearLayoutAdvSearchMaritalStatus);

            List<WebArd> dataList1 = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(1).toString(), listType);
            viewGenerator.generateDynamicCheckBoxesLL(dataList1, LinearLayoutAdvSearchChildren);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSelection() {


        if (defaultSelectionsObj != null) {
            //   Log.e("set selection "+defaultSelectionsObj.get_choice_marital_status_ids(), "sett selection "+defaultSelectionsObj.get_choice_children_ids());
            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchMaritalStatus, defaultSelectionsObj.get_choice_marital_status_ids());

            viewGenerator.selectCheckBoxes(LinearLayoutAdvSearchChildren, defaultSelectionsObj.get_choice_children_ids());

           /* if (defaultSelectionsObj.get_choice_marital_status_ids().equals("")) {
                viewGenerator.resetCheckBoxes(LinearLayoutAdvSearchMaritalStatus);
            } else if (defaultSelectionsObj.get_choice_children_ids().equals("")) {
                viewGenerator.resetCheckBoxes(LinearLayoutAdvSearchChildren);
            }*/
        }


    }

    private void setListeners() {
        {
            int childcount = LinearLayoutAdvSearchMaritalStatus.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = LinearLayoutAdvSearchMaritalStatus.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }
        {
            int childcount = LinearLayoutAdvSearchChildren.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View sv = LinearLayoutAdvSearchChildren.getChildAt(i);
                if (sv instanceof CheckBox) {
                    ((CheckBox) sv).setOnCheckedChangeListener(this);
                }
            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        defaultSelectionsObj.set_choice_marital_status_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchMaritalStatus));
        defaultSelectionsObj.set_choice_children_ids(viewGenerator.getSelectionFromCheckbox(LinearLayoutAdvSearchChildren));
        updateDot();
    }

/*    // ItemDetailFragment.newInstance(item_image_slider)
    public static BasicsFragment newInstance(String item_image_slider) {
    	BasicsFragment fragmentDemo = new BasicsFragment();
      //  Bundle args = new Bundle();
     //   args.putString("item_image_slider", item_image_slider);
    //    fragmentDemo.setArguments(args);
        return fragmentDemo;
    }*/


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


    private void resetSelections(View v) {
        if (v.getTag().equals("martial_status")) {
            defaultSelectionsObj.set_choice_marital_status_ids("");


        } else if (v.getTag().equals("children")) {
            defaultSelectionsObj.set_choice_children_ids("");
        }
        //   Log.e("defaultSelectionsObj", defaultSelectionsObj.get_choice_children_ids());

        setSelection();
    }

    /*@Override
    public void onClick(View v) {

    }*/

    public interface OnChildFragmentInteractionListener {
        void messageFromChildToParent();
    }

    private void updateDot() {
        fragmentInteractionListener.messageFromChildToParent();

    }

}
