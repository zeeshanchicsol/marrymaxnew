package com.chicsol.marrymax.fragments.list;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.BlockListAdapter;
import com.chicsol.marrymax.modal.WebArdList;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 12/2/2016.
 */


public class BlockedListFragment extends Fragment implements BlockListAdapter.onCompleteListener {
    private ListView lv_mycontacts;
    BlockListAdapter myListAdapter;
    LinearLayout llEmpty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_list_blocked_users, container, false);
        initialize(rootView);
        return rootView;
    }

    private void initialize(View v) {
        llEmpty = (LinearLayout) v.findViewById(R.id.LinearLayoutEmptyBlockedList);


        lv_mycontacts = (ListView) v.findViewById(R.id.ListViewBlocked);
        myListAdapter = new BlockListAdapter(getActivity(), R.layout.item_list_blocked, null, BlockedListFragment.this);
        getData();


      /*  MyContactsListAdapter myContactsListAdapter=new MyContactsListAdapter(this,R.layout.item_list_blocked,dataList);
        lv_mycontacts.setAdapter(myContactsListAdapter);
        */


    }

    private void getData() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        Log.e("url", Urls.getBlockedList + SharedPreferenceManager.getUserObject(getActivity().getApplicationContext()).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getBlockedList + SharedPreferenceManager.getUserObject(getActivity().getApplicationContext()).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString() + "  ==   ");
                        Gson gsonc;
                        GsonBuilder gsonBuilderc = new GsonBuilder();
                        gsonc = gsonBuilderc.create();
                        Type listType = new TypeToken<List<WebArdList>>() {
                        }.getType();
                        List<WebArdList> dataList;

                        try {
                            Log.e("Response", response.getJSONArray(0).toString());

                            dataList = (List<WebArdList>) gsonc.fromJson(response.getJSONArray(0).toString(), listType);
                            myListAdapter = new BlockListAdapter(getActivity(), R.layout.item_list_blocked, dataList, BlockedListFragment.this);
                            lv_mycontacts.setAdapter(myListAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            myListAdapter.clear();
                            lv_mycontacts.setVisibility(View.GONE);
                            llEmpty.setVisibility(View.VISIBLE);

                        }


                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(req);
    }

    private void setListeners() {


    }

    @Override
    public void onComplete(int s) {
        //  Toast.makeText(getActivity().getApplicationContext(), ""+s+"", Toast.LENGTH_SHORT).show();
        getData();
    }
}