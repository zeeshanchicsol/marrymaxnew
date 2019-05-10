package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.MyAddToListAdapter;
import com.chicsol.marrymax.modal.mMemList;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.faTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogAddtoList extends DialogFragment implements MyAddToListAdapter.OnUpdateListener {
    MyAddToListAdapter mListAdapter;
    String userpath;
    private ListView lv_mycontacts;
    List<mMemList> dataList;
    EditText etNewListText;
    Button btCreateList;


    public static dialogAddtoList newInstance(String data) {

        dialogAddtoList frag = new dialogAddtoList();
        Bundle args = new Bundle();
        args.putString("userpath", data);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        userpath = mArgs.getString("userpath");
        ///  Log.e("json data", myValue);


    }
/*    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_profilefor, null))


          *//*      // Add action buttons
                .setPositiveButton("Submut", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogDosDonts.this.getDialog().cancel();
                    }
                })*//*;


        return builder.create();

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_add_to_list, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        lv_mycontacts = (ListView) rootView.findViewById(R.id.ListViewMySavedListPopup);
    /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lv_mycontacts.setNestedScrollingEnabled(true);
        }*/
        etNewListText = (EditText) rootView.findViewById(R.id.EditTextAddtoListCreateList);
        btCreateList = (Button) rootView.findViewById(R.id.ButtonAddtoListCreate);

        dataList = new ArrayList<>();
        mListAdapter = new MyAddToListAdapter(getActivity(), R.layout.item_list_add_to_list, dataList, dialogAddtoList.this, dialogAddtoList.this, userpath);
        lv_mycontacts.setAdapter(mListAdapter);


        List<String> dataList = new ArrayList<>();
        dataList.add("asdsadsa");
        dataList.add("1212321");


        getData();

        // MyContactsListAdapter myContactsListAdapter=new MyContactsListAdapter(getActivity(),R.layout.item_list_my_saved_list,dataList);
        //lv_mycontacts.setAdapter(myContactsListAdapter);

   /*     Bundle mArgs = getArguments();
        String myValue = mArgs.getString("jsondata");
        try {
            JSONArray jsonArray = new JSONArray(myValue);
            JSONArray dosDataList = jsonArray.getJSONArray(0);
            JSONArray dontsDataList = jsonArray.getJSONArray(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/





     /*   RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.RadioGroupProfileFor);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //  radioGroup.ad
        for (int i = 0; i < DataList.size(); i++) {
            mRadioButton mradiobutton = new mRadioButton(getContext());
            mradiobutton.setText(DataList.get(i));
            mradiobutton.setLayoutParams(layoutParams);

            radioGroup.addView(mradiobutton);
        }*/

        /*//do's
        for (int i=0;i<jsonArray1.length();i++){
            JSONObject jsonObject= jsonArray1.getJSONObject(i);
            Log.e("value",""+jsonObject.get("name").toString());
        }

        //dont's
        for (int i=0;i<jsonArray2.length();i++){
            JSONObject jsonObject= jsonArray2.getJSONObject(i);
            Log.e("value 2",""+jsonObject.get("name").toString());
        }*/


        btCreateList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String listname = etNewListText.getText().toString();
                if (!TextUtils.isEmpty(listname.trim())) {


                    addEditList(listname, "0");

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etNewListText.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                } else {
                    Toast.makeText(getContext(), "Please enter text first !", Toast.LENGTH_SHORT).show();
                }

            }
        });


        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.dismissBtnGeoInfo);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogAddtoList.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    private void getData() {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();


        JSONObject params = new JSONObject();
        try {


            params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
            params.put("userpath", userpath);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("Params", Urls.myList + "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.myList, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re  update appearance", response + "");
                        try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray("data").getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mMemList>>() {
                            }.getType();

                            dataList = (List<mMemList>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);

                            //Log.e("AddtoListList", "" + dataList.size());
                            if (dataList.size() > 0) {
                               /* LinearLayoutMMMatchesNotFound.setVisibility(View.GONE);
                                llmainLayout.setVisibility(View.VISIBLE);
                                tvHeadingRemoveFromSerch.setVisibility(View.VISIBLE);*/
                                mListAdapter.updateDataList(dataList);

                                Utility utility = new Utility();
                                utility.setListViewHeightBasedOnChildren(lv_mycontacts);


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };

// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq);

    }


    @Override
    public void onUpdate(String msg) {
       // dialogAddtoList.this.getDialog().cancel();
    }


    private void addEditList(String notes, String id) {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();


        JSONObject params = new JSONObject();
        try {


            params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
            params.put("notes", notes);
            params.put("id", id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("Params", Urls.addEditList + "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.addEditList, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re   myList", response + "");




                      int responseid = 0;
                        try {
                            responseid = response.getInt("id");
                            //Log.e("id is ",""+responseid);
                            if (responseid >= 0) {

                              //  Toast.makeText(getContext(), "Member added to lists successfully", Toast.LENGTH_SHORT).show();
                                etNewListText.setText("");
                                AddMemList("0",responseid+"");


                            }
                            else {
                                Toast.makeText(getContext(), "Member removed from lists successfully", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                       /* try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray("data").getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mMemList>>() {
                            }.getType();

                            dataList = (List<mMemList>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);

                            Log.e("MyCountryStateDataList", "" + dataList.size());
                            if (dataList.size() > 0) {
                               *//* LinearLayoutMMMatchesNotFound.setVisibility(View.GONE);
                                llmainLayout.setVisibility(View.VISIBLE);
                                tvHeadingRemoveFromSerch.setVisibility(View.VISIBLE);*//*
                                mListAdapter.updateDataList(dataList);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };

// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq);

    }

    public class Utility {
        public void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            int count = listAdapter.getCount();
            if (count > 3) {
                count = 3;
            }
            if (count == 2 || count == 1) {
                count = 1;
            }
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            for (int i = 0; i < count; i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }



    private void AddMemList(String my_id, String id) {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();


        JSONObject params = new JSONObject();
        try {


            params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());
            params.put("my_id", my_id);
            params.put("id", id);
            params.put("userpath", userpath);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("Params", Urls.addMemList + "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.addMemList, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re   myList", response + "");
                        getData();

                        int responseid = 0;
                        try {
                            responseid = response.getInt("id");
                            // Log.e("id is ",""+responseid);
                            if (responseid == 1) {
                                getData();
                           //     Toast.makeText(getContext(), "Member added to lists successfully", Toast.LENGTH_SHORT).show();

                            }
                            else {
                            //    Toast.makeText(getContext(), "Member removed from lists successfully", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };

// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq);

    }



}


