package com.chicsol.marrymax.fragments.requestpermission;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterRequestPermissions;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mRequestPermission;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
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
 * Created by muneeb on 8/2/17.
 */

public class RequestPermissionFragment extends Fragment implements RecyclerViewAdapterRequestPermissions.OnUpdateListener {
    private RecyclerView recyclerViewRequest;
    private RecyclerViewAdapterRequestPermissions recyclerAdapterRequest;
    private List<mRequestPermission> mRequestDataList, mPermissionDataList;
    Members member;
    boolean permissioncheck;
    TextView tvTitle;
    private ProgressBar pDialog;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_request_permissions, container, false);
        initialize(rootView);
        return rootView;
    }

    private void initialize(View view) {
        String memberString = getArguments().getString("member");
        permissioncheck = getArguments().getBoolean("permissioncheck");
        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarRequestPermissions);


        //Log.e("Permission", permissioncheck + "");


        tvTitle = (TextView) view.findViewById(R.id.TextViewRequestPermissionTitle);


        Gson gson = new Gson();
        member = gson.fromJson(memberString, Members.class);


        mRequestDataList = new ArrayList<>();
        mPermissionDataList = new ArrayList<>();
        recyclerViewRequest = (RecyclerView) view.findViewById(R.id.RecyclerViewRPRequest);

        recyclerViewRequest.setLayoutManager(new GridLayoutManager((context), 1));

        recyclerAdapterRequest = new RecyclerViewAdapterRequestPermissions(mRequestDataList, context, permissioncheck, member.getUserpath(), getActivity(), this, member.getAlias(), member);

        recyclerViewRequest.setAdapter(recyclerAdapterRequest);

        if (permissioncheck) {

            tvTitle.setText("Permissions:");
        } else {

            tvTitle.setText("Requests:");
        }

        requestPermission();


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onUpdate(String msg) {
        //Toast.makeText(getContext(), "Clicked ", Toast.LENGTH_SHORT).show();
        requestPermission();
    }

    private void requestPermission() {

        //  final ProgressDialog pDialog = new ProgressDialog(getActivity());
        //   pDialog.setMessage("Loading...");
        //  pDialog.show();

        pDialog.setVisibility(View.VISIBLE);

        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject params = new JSONObject();
        try {


            params.put("userpath", member.getUserpath());
            params.put("path", SharedPreferenceManager.getUserObject(context).getPath());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("Params", Urls.requestPermissions + "=====" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.requestPermissions, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("re request permissions", response + "");
                        try {


                            // int responseid = response.getInt("id");
                            JSONArray jsonArray = response.getJSONArray("data");


                            JSONArray jsonArrayRequests = jsonArray.getJSONArray(0);
                            JSONArray jsonArrayPermissions = jsonArray.getJSONArray(1);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mRequestPermission>>() {
                            }.getType();


                            if (permissioncheck) {
                                mPermissionDataList = (List<mRequestPermission>) gsonc.fromJson(jsonArrayPermissions.toString(), listType);
                                recyclerAdapterRequest.addAll(mPermissionDataList);

                            } else {
                                mRequestDataList = (List<mRequestPermission>) gsonc.fromJson(jsonArrayRequests.toString(), listType);
                                recyclerAdapterRequest.addAll(mRequestDataList);
                            }


                            //

                            //  Log.e("mRequestDataList" + mRequestDataList.size(), "" + mPermissionDataList.size());


                        } catch (JSONException e) {
                            // pDialog.dismiss();

                            pDialog.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        //    pDialog.dismiss();
                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                Toast.makeText(context, "Error Occured.  !", Toast.LENGTH_SHORT).show();
                pDialog.setVisibility(View.GONE);
                //   pDialog.dismiss();
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);

    }
}
