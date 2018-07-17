package com.chicsol.marrymax.fragments.DashMain;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.RecyclerViewAdapter;
import com.chicsol.marrymax.fragments.DashboardMainFragment;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
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


public class DashMembers extends Fragment  implements RecyclerViewAdapter.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private Context context;
    private
    RecyclerView recyclerViewMSLW;
    private int lastPageMSLW = 1;
    private List<Members> membersDataListMSLW;
    private int totalPagesMSLW;
    private RecyclerViewAdapter recyclerAdapterMSLW;
    private AppCompatButton btChangeMatchingAttributeMSLW;

    private Button btLoadMore, btReset;

    private LinearLayout llMatchesNotFoundMSLW,llMyMatchesBottomBar;


    public DashMembers() {
        // Required empty public constructor
    }


    public static DashMembers newInstance(String param1, String param2) {
        DashMembers fragment = new DashMembers();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_dash_members, container, false);
        initialize(rootView);
        setListeners();
        return rootView;
    }


    private void initialize(View view) {
        btChangeMatchingAttributeMSLW = (AppCompatButton) view.findViewById(R.id.ButtonDashboardonChangeMatchingAttributeMSLL);
        llMatchesNotFoundMSLW = (LinearLayout) view.findViewById(R.id.LinearLayoutMemberDMMatchesNotAvailable);

        recyclerViewMSLW = (RecyclerView) view.findViewById(R.id.RecyclerViewLastWeekMatches);
        recyclerViewMSLW.setLayoutManager(new GridLayoutManager(getContext(), 2));


        btLoadMore = (Button) view.findViewById(R.id.ButtonMSLWLoadMore);
        btReset = (Button) view.findViewById(R.id.ButtonMSLWReset);


        membersDataListMSLW = new ArrayList<>();

    }

    private void setListeners() {

    }




    // TODO: Rename method, update argument and hook method into UI event
/*    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }


/*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
*/


   /* private void getMembersListbyTypeMSLW() {

      *//*  if (btReset.getVisibility() == View.VISIBLE) {
            btReset.setVisibility(View.INVISIBLE);

        }
*//*
        JSONObject params = new JSONObject();
        try {
            params.put("page_no", lastPageMSLW);
            params.put("type", "registertoday");
            params.put("member_status", SharedPreferenceManager.getUserObject(getContext()).get_member_status());
            params.put("phone_verified", SharedPreferenceManager.getUserObject(getContext()).get_phone_verified());

            params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());

        } catch (JSONException e) {
            e.printStackTrace();
        }

*//*if(!pDialog.isShowing()) {
    pDialog.show();
}*//*
       // pDialog.setVisibility(View.VISIBLE);
        Log.e("Params", "" + params);
        Log.e("Member List", "" + Urls.getMembersListbyType);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.getMembersListbyType, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("re  update data", response + "");

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            if (jsonArray.length() > 1) {

                                Log.e("Length", jsonArray.getJSONArray(0).length() + "");
                                JSONArray jsonarrayData = jsonArray.getJSONArray(0);
                                JSONArray jsonarrayTotalPages = jsonArray.getJSONArray(1);

                                Gson gson;
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gson = gsonBuilder.create();
                                Type member = new TypeToken<List<Members>>() {
                                }.getType();


                                membersDataListMSLW = (List<Members>) gson.fromJson(jsonarrayData.toString(), member);
                                //     Log.e("Length 56", membersDataListMSLW.size() + "  ");


                                recyclerAdapterMSLW = new RecyclerViewAdapter(membersDataListMSLW, context);
                                recyclerAdapterMSLW.setOnItemClickListener(DashMembers.this);
                                recyclerViewMSLW.setAdapter(recyclerAdapterMSLW);


                               *//* recyclerAdapterMSLL = new RecyclerViewAdapter(membersDataListMSLW, context);
                                recyclerAdapterMSLL.setOnItemClickListener(DashboardMainFragment.this);
                                recyclerViewMSLL.setAdapter(recyclerAdapterMSLL);*//*

                                Gson gsont;
                                GsonBuilder gsonBuildert = new GsonBuilder();
                                gsont = gsonBuildert.create();
                                Type membert = new TypeToken<Members>() {
                                }.getType();
                                Members memberTotalPages = (Members) gson.fromJson(jsonarrayTotalPages.getJSONObject(0).toString(), membert);


                                totalPagesMSLW = memberTotalPages.get_total_pages();
                                Log.e("total pages mlbt", "" + totalPagesMSLW);

                                if (totalPagesMSLW == 0 || totalPagesMSLW == 1) {
                                    llMyMatchesBottomBar.setVisibility(View.GONE);
                                } else if (totalPagesMSLW > 1) {
                                    llMyMatchesBottomBar.setVisibility(View.VISIBLE);
                                }


                                if (membersDataListMSLW.size() == 0) {
                                    recyclerViewMSLW.setVisibility(View.GONE);
                                    llMatchesNotFoundMSLW.setVisibility(View.VISIBLE);
                                    llMyMatchesBottomBar.setVisibility(View.GONE);
                                }

                            } else {
                                //no data
                                recyclerViewMSLW.setVisibility(View.GONE);
                                llMatchesNotFoundMSLW.setVisibility(View.VISIBLE);
                                llMyMatchesBottomBar.setVisibility(View.GONE);

                            }
                            //  pDialog.dismiss();
                          //  pDialog.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                           // pDialog.setVisibility(View.INVISIBLE);
                            // pDialog.dismiss();
                        }

                      //  pDialog.setVisibility(View.INVISIBLE);
                        // pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
               // pDialog.setVisibility(View.INVISIBLE);
                //   pDialog.dismiss();
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq);
    }
*/
    @Override
    public void onItemClick(View view, Members members, int position, List<Members> items,Members member) {
        //  Toast.makeText(getActivity(), members.get_path() + " clicked", Toast.LENGTH_SHORT).show();
 /*       Intent intent = new Intent(getActivity(), UserProfileActivity.class);
        intent.putExtra("userpath", members.getUserpath());
        startActivity(intent);*/

        Log.e("position", "position: " + position);


        Activity activity = (Activity) getContext();
        MarryMax marryMax = new MarryMax(getActivity());
        if (ConnectCheck.isConnected(activity.findViewById(android.R.id.content))) {

            Log.e("Data list ", "" + items.size());
            Gson gson = new Gson();
            marryMax.statusBaseChecks(members, getContext(), 1, getFragmentManager(), DashMembers.this, view, gson.toJson(items), "" + position,null);
        }

    }
}
