package com.chicsol.marrymax.dialogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.DashboarMainActivityWithBottomNav;
import com.chicsol.marrymax.activities.registration.RegisterAppearanceActivity;
import com.chicsol.marrymax.activities.registration.RegisterGeographicActivity;
import com.chicsol.marrymax.activities.registration.RegisterLifeStyleActivity1;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.PrefMatching;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 11/3/2016.
 */

public class dialogMatchingAttributeFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener {

    private AppCompatButton btSave;

    private GridLayout glLifeStyle, glAppearance, glGeography;
    private ViewGenerator viewGenerator;

    private ProgressBar pDialog;
    private mTextView tvLifeStyleSetting, tvGeographySetting, tvAppearanceSetting;
    private String selectedIds = null;
    private onMatchPreferenceCompleteListener mCompleteListener;
    private String Tag = "dialogMatchingAttributeFragment";
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static dialogMatchingAttributeFragment newInstance(String userpath) {

        dialogMatchingAttributeFragment frag = new dialogMatchingAttributeFragment();
        Bundle args = new Bundle();
        args.putString("name", "Do's & Don't");
        args.putString("userpath", userpath);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_matching_attributes, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        initilize(rootView);

        return rootView;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            context=activity;

            if (getTargetFragment() != null) {
                mCompleteListener = (onMatchPreferenceCompleteListener) getTargetFragment();
            } else {
                mCompleteListener = (onMatchPreferenceCompleteListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }

/*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            if (isVisibleToUser) {

                if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
                    getPrefRequest();
                }
            }
        }


    }*/


    private void initilize(View view) {
        viewGenerator = new ViewGenerator(context);
        glLifeStyle = (GridLayout) view.findViewById(R.id.GridlayoutAccountSettingPrefferedMatchingLifeStyle);
        glAppearance = (GridLayout) view.findViewById(R.id.GridlayoutAccountSettingPrefferedMatchingAppearance);
        glGeography = (GridLayout) view.findViewById(R.id.GridlayoutAccountSettingPrefferedMatchingGeography);
        btSave = (AppCompatButton) view.findViewById(R.id.ButtonAccountSettingMatchPrefSavePref);

        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarProjectMain);

        tvLifeStyleSetting = (mTextView) view.findViewById(R.id.mTextViewAccountSettingPrefferedMatchingLifestyle);
        tvGeographySetting = (mTextView) view.findViewById(R.id.mTextViewAccountSettingPrefferedMatchingGeography);
        tvAppearanceSetting = (mTextView) view.findViewById(R.id.mTextViewAccountSettingPrefferedMatchingAppearance);

        //  getPrefRequest();
        faTextView cancelButton = (faTextView) view.findViewById(R.id.dismissBtnGeoInfo);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogMatchingAttributeFragment.this.getDialog().cancel();
            }
        });


        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            // FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            getPrefRequest();
        }

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void setListeners() {


        setCBListener(glAppearance);
        setCBListener(glGeography);
        setCBListener(glLifeStyle);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (selectedIds != null) {
                    String[] arrayList = selectedIds.split(",");

                    if (arrayList.length < 4) {
                        Toast.makeText(context, "minimium four matching attributes should be selected", Toast.LENGTH_SHORT).show();

                    } else if (arrayList.length > 4) {
                        Toast.makeText(context, "Max four matching attributes should be selected", Toast.LENGTH_SHORT).show();

                    } else if (!selectedIds.equals("")) {

                        Members mem = new Members();
                        mem.setPath(SharedPreferenceManager.getUserObject(context).getPath());
                        mem.setChoice_preferences_ids(selectedIds);
                        Gson gson = new Gson();
                        String memString = gson.toJson(mem);
                        try {

                            updatePrefRequest(new JSONObject(memString));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {

                        Toast.makeText(context, "Please check some boxes first.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


        tvLifeStyleSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Class cls = RegisterLifeStyleActivity1.class;
                new MarryMax(getActivity()).getProfileProgress(cls, getActivity(), context, SharedPreferenceManager.getUserObject(context));
            }
        });
        tvGeographySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Class cls = RegisterGeographicActivity.class;
                new MarryMax(getActivity()).getProfileProgress(cls, getActivity(), context, SharedPreferenceManager.getUserObject(context));

            }
        });
        tvAppearanceSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class cls = RegisterAppearanceActivity.class;
                new MarryMax(getActivity()).getProfileProgress(cls, getActivity(), context, SharedPreferenceManager.getUserObject(context));

            }
        });

    }


    private void selectCheckBoxes(String selectedc) {


        viewGenerator.selectCheckRadioFromGridLayout(glAppearance, selectedc);
        viewGenerator.selectCheckRadioFromGridLayout(glGeography, selectedc);
        viewGenerator.selectCheckRadioFromGridLayout(glLifeStyle, selectedc);


    }


    private void getPrefRequest() {


        pDialog.setVisibility(View.VISIBLE);
        //Log.e("getPrefRequest ", "" + Urls.getPreferences + SharedPreferenceManager.getUserObject(context).getPath());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getPreferences + SharedPreferenceManager.getUserObject(context).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("Response", response.toString());
                        try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray(1);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<PrefMatching>>() {
                            }.getType();

                            List<PrefMatching> dataList = (List<PrefMatching>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                            //Log.e("Response", dataList.size() + "");
                            ViewGenerator viewGenerator = new ViewGenerator(context);
                            Point size = new Point();
                            if (getActivity() != null) {
                                getActivity().getWindowManager().getDefaultDisplay().getSize(size);
                            }


                            if (dataList.size() > 0) {
                                try {


                                    if (size != null) {
                                        glGeography.removeAllViews();
                                        glLifeStyle.removeAllViews();
                                        glAppearance.removeAllViews();
                                        viewGenerator.generatePrefMatchingCheckBoxesInGridLayout(dataList, glGeography, glLifeStyle, glAppearance, size.x - 30);
                                        String preids = response.getJSONArray(0).getJSONObject(0).getString("choice_preferences_ids");

                                        selectCheckBoxes(preids);
                                        setListeners();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                    /*Intent in=new Intent(getActivity().getApplicationContext(), DashboarMainActivityWithBottomNav.class);
                                    startActivity(in);
                                    getActivity().finish();*/
                                }
                            }


                        } catch (JSONException e) {
                            Intent in = new Intent(getActivity().getApplicationContext(), DashboarMainActivityWithBottomNav.class);
                            startActivity(in);
                            getActivity().finish();
                            e.printStackTrace();
                        }

                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(req, Tag);
    }


    public void setCBListener(GridLayout gl) {


        int childcount = gl.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View sv = gl.getChildAt(i);
            if (sv instanceof CheckBox) {
                ((CheckBox) sv).setOnCheckedChangeListener(this);
            }
        }


    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        String personality_ids = getSelectionString();

        if (personality_ids != null) {
            String[] arrayList = personality_ids.split(",");


            if (arrayList.length > 4) {
                Toast.makeText(context, " Only four matching attributes can be selected. To change matching attributes, un-check any selected one and then select attribute of your choice and adjust your preferences accordingly.", Toast.LENGTH_LONG).show();
                compoundButton.setChecked(!b);
                // Snackbar snackbarNotVerified = Snackbar.make(getActivity().findViewById(android.R.id.content), " Only four matching attributes can be selected. To change matching attributes, un-check any selected one and then select attribute of your choice and adjust your preferences accordingly.", Snackbar.LENGTH_SHORT);
                // snackbarNotVerified.show();

            }

        }

    }


    private String getSelectionString() {

        selectedIds = "";
        String appear = viewGenerator.getSelectionFromCheckbox(glAppearance);
        String life = viewGenerator.getSelectionFromCheckbox(glLifeStyle);
        String geogr = viewGenerator.getSelectionFromCheckbox(glGeography);


        if (!life.equals("")) {
            selectedIds = life;
        }

        if (!appear.equals("")) {
            if (!selectedIds.equals("")) {
                selectedIds = selectedIds + "," + appear;
            } else {
                selectedIds = appear;
            }
        }

        if (!geogr.equals("")) {
            if (!selectedIds.equals("")) {
                selectedIds = selectedIds + "," + geogr;

            } else {
                selectedIds = geogr;
            }


        }
        //Log.e("Log   a", "" + selectedIds);
        return selectedIds;


    }


    private void updatePrefRequest(JSONObject params) {

        pDialog.setVisibility(View.VISIBLE);
        //Log.e("params", params.toString());
        //Log.e("profile path", Urls.editPreferences);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.editPreferences, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Res  interest ", response + "");


                        try {
                            int responseid = response.getInt("id");

                            if (responseid == 1) {
                                mCompleteListener.onPreferenceComplete("");
                                Toast.makeText(context, "Matching Preference Successfully Updated", Toast.LENGTH_SHORT).show();
                                dialogMatchingAttributeFragment.this.getDialog().cancel();

                            } else {
                                Toast.makeText(context, "Error Occurred. Try again", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            pDialog.setVisibility(View.GONE);
                            e.printStackTrace();
                        }


                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                pDialog.setVisibility(View.GONE);
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq, Tag);
    }

    @Override
    public void onStop() {
        super.onStop();
        //  MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);

    }

    public static interface onMatchPreferenceCompleteListener {
        public abstract void onPreferenceComplete(String s);
    }
}
