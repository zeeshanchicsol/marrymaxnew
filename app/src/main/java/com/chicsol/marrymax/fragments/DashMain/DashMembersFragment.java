package com.chicsol.marrymax.fragments.DashMain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.adapters.RecyclerViewAdapter;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.NpaGridLayoutManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;


public class DashMembersFragment extends Fragment implements RecyclerViewAdapter.OnItemClickListener, RecyclerViewAdapter.OnLoadMoreListener {
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

    // private Button btLoadMore, btReset;

    private LinearLayout llMatchesNotFoundMSLW, llMyMatchesBottomBar;
    private String type = "", msg = "";

    private String Tag = "DashMembersFragment";
    //load more
    private int lastPage = 1;
    private int totalPages = 0;

    public String params = "";


    public DashMembersFragment() {
        // Required empty public constructor
    }


    public static DashMembersFragment newInstance(String param1, String param2) {
        DashMembersFragment fragment = new DashMembersFragment();
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
    public void onResume() {
        super.onResume();
        lastPage = 1;
        totalPages = 0;
        LoadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            if (isVisibleToUser) {

                LoadData();

            }
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
        type = getArguments().getString("type");
        msg = getArguments().getString("msg");

        TextView tvError = (TextView) view.findViewById(R.id.TextViewMemberFragmentError);
        tvError.setText(msg);
        btChangeMatchingAttributeMSLW = (AppCompatButton) view.findViewById(R.id.ButtonDashboardonChangeMatchingAttributeMSLL);

        llMatchesNotFoundMSLW = (LinearLayout) view.findViewById(R.id.LinearLayoutMemberDMMatchesNotAvailable);
        llMyMatchesBottomBar = (LinearLayout) view.findViewById(R.id.LinearLayoutDMMyMatchesBottomBarMSLL);

        recyclerViewMSLW = (RecyclerView) view.findViewById(R.id.RecyclerViewLastLoginMatches);
        //  recyclerViewMSLW.setLayoutManager();


        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getContext(), 2);




  /*      recyclerAdapterMSLW = new RecyclerViewAdapter();
        recyclerAdapterMSLW.setOnItemClickListener(DashMembersFragment.this);
        recyclerViewMSLW.setAdapter(recyclerAdapterMSLW);*/


        recyclerAdapterMSLW = new RecyclerViewAdapter(context, this);
        recyclerAdapterMSLW.setLinearLayoutManager(gridLayoutManager);
        recyclerViewMSLW.setLayoutManager(gridLayoutManager);

        recyclerAdapterMSLW.setRecyclerView(recyclerViewMSLW);

        recyclerViewMSLW.setAdapter(recyclerAdapterMSLW);
        recyclerAdapterMSLW.setOnItemClickListener(DashMembersFragment.this);




        /*btLoadMore = (Button) view.findViewById(R.id.ButtonMSLWLoadMore);
        btReset = (Button) view.findViewById(R.id.ButtonMSLWReset);
*/

        membersDataListMSLW = new ArrayList<>();

    }

    private void setListeners() {


        btChangeMatchingAttributeMSLW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 13);
                startActivity(in);
            }
        });



   /*     btLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lastPageMSLW != totalPagesMSLW && lastPageMSLW < totalPagesMSLW) {
                    lastPageMSLW = lastPageMSLW + 1;

                //    getMembersListbyTypeByPageMSLW(lastPageMSLW);

                }

            }
        });
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   lastPageMSLW = 1;
             //   getMembersListbyTypeMSLW();

            }
        });*/

    }

    private void LoadData() {
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            Members memberSearchObj = new Members();

            /*      try {*/
   /*             params.put("page_no", lastPage);
                params.put("type", type);
                params.put("member_status", SharedPreferenceManager.getUserObject(getContext()).get_member_status());
                params.put("phone_verified", SharedPreferenceManager.getUserObject(getContext()).get_phone_verified());
                params.put("email_verified", SharedPreferenceManager.getUserObject(getContext()).get_email_verified());

                params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());*/


            memberSearchObj.set_path(SharedPreferenceManager.getUserObject(context).get_path());
            memberSearchObj.set_member_status(SharedPreferenceManager.getUserObject(context).get_member_status());
            memberSearchObj.set_phone_verified(SharedPreferenceManager.getUserObject(context).get_phone_verified());
            memberSearchObj.set_email_verified(SharedPreferenceManager.getUserObject(context).get_email_verified());
            //page and type
            memberSearchObj.set_page_no(1);
            memberSearchObj.set_type(type);


            Gson gson = new Gson();

            params = gson.toJson(memberSearchObj);


            //     argsRequest.putString("type", "mymatches");
            //     argsPermission.putString("type", "registertoday");


            if (type.equals("mymatches")) {
                // memberSearchObj.set_type();
                //  PM
                Members members = memberSearchObj;
                members.set_type("PM");

                recyclerAdapterMSLW.setMemResultsObj(members);
            } else {

                Members members = memberSearchObj;
                members.set_type("MBW");
                recyclerAdapterMSLW.setMemResultsObj(members);
            }



      /*      } catch (JSONException e) {
                e.printStackTrace();
            }*/


            getMembersListbyTypeMSLW();


        }
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


    private void getMembersListbyTypeMSLW() {
        JSONObject paramsa = null;
        try {
            paramsa = new JSONObject(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

      /*  if (btReset.getVisibility() == View.VISIBLE) {
            btReset.setVisibility(View.INVISIBLE);

        }
*/

/*if(!pDialog.isShowing()) {
    pDialog.show();
}*/
        // pDialog.setVisibility(View.VISIBLE);
        Log.e("MemberList Params", "" + paramsa);
        Log.e("MemberList", "" + Urls.getMembersListbyType);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.getMembersListbyType, paramsa,
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
                                Log.e("membersDataListMSLW 56", membersDataListMSLW.size() + "  ");
                                recyclerAdapterMSLW.addAll(membersDataListMSLW);

                                Log.e("getMoreLoading", recyclerAdapterMSLW.getMoreLoading() + "");
                                recyclerAdapterMSLW.setMoreLoading(false);
                               /* recyclerAdapterMSLL = new RecyclerViewAdapter(membersDataListMSLW, context);
                                recyclerAdapterMSLL.setOnItemClickListener(DashboardMainFragment.this);
                                recyclerViewMSLL.setAdapter(recyclerAdapterMSLL);*/

                                Gson gsont;
                                GsonBuilder gsonBuildert = new GsonBuilder();
                                gsont = gsonBuildert.create();
                                Type membert = new TypeToken<Members>() {
                                }.getType();
                                Members memberTotalPages = (Members) gson.fromJson(jsonarrayTotalPages.getJSONObject(0).toString(), membert);


                                // totalPagesMSLW = memberTotalPages.get_total_pages();
                                Log.e("total pages mlbt", "" + totalPagesMSLW);

                                totalPages = memberTotalPages.get_total_pages();
                                lastPage = 1;


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
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, Tag);
    }


    private void getMembersListbyTypeByPageMSLW(int pageNumber) {
       recyclerAdapterMSLW.setProgressMore(true);
        // btReset.setVisibility(View.VISIBLE);
        JSONObject params = new JSONObject();
        try {
            params.put("page_no", pageNumber);
            params.put("type", type);
            params.put("member_status", SharedPreferenceManager.getUserObject(context).get_member_status());
            params.put("phone_verified", SharedPreferenceManager.getUserObject(context).get_phone_verified());
            params.put("email_verified", SharedPreferenceManager.getUserObject(context).get_email_verified());

            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //pDialog.show();
        //   pDialog.setVisibility(View.VISIBLE);

        Log.e("Params", "" + params);
        Log.e("Member List", "" + Urls.getMembersListbyType);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.getMembersListbyType, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("new response data", response + "");

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            if (jsonArray.length() > 1) {

                                Log.e("Length", jsonArray.getJSONArray(0).length() + "");
                                JSONArray jsonarrayData = jsonArray.getJSONArray(0);

                                Gson gson;
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gson = gsonBuilder.create();
                                Type member = new TypeToken<List<Members>>() {
                                }.getType();


                                List<Members> datalist = (List<Members>) gson.fromJson(jsonarrayData.toString(), member);
                                //  int scrollPosition = (membersDataListMSLW.size() + datalist.size()) - datalist.size();


                            /*    membersDataListMSLW.addAll(datalist);
                                recyclerAdapterMSLW.notifyDataSetChanged();
                                recyclerViewMSLW.scrollToPosition(scrollPosition);*/

                                recyclerAdapterMSLW.setProgressMore(false);
                                // membersDataList.clear();

                                // Log.e("Length 56", membersDataList.size() + "  ");
                                recyclerAdapterMSLW.addItemMore(datalist);
                                recyclerAdapterMSLW.setMoreLoading(false);



                              /*  Log.e("data lis size before",membersDataListMSLW.size()+"");
                                recyclerViewMSLW.invalidate();


                                recyclerAdapterMSLW = new RecyclerViewAdapter(membersDataListMSLW, getContext());
                                recyclerAdapterMSLW.setOnItemClickListener(DashboardMainFragment.this);
                                recyclerViewMSLW.setAdapter(recyclerAdapterMSLW);*/

                            } else {
                                //no data
                                recyclerAdapterMSLW.setMoreLoading(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            recyclerAdapterMSLW.setMoreLoading(false);
                        }
                        recyclerAdapterMSLW.setMoreLoading(false);
                        //    pDialog.setVisibility(View.INVISIBLE);
                        //  pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();
                //  pDialog.setVisibility(View.INVISIBLE);
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, Tag);
    }

    @Override
    public void onLoadMore() {
        if (lastPage != totalPages && lastPage < totalPages) {
            lastPage = lastPage + 1;




         /*   Gson gsont;
            GsonBuilder gsonBuildert = new GsonBuilder();
            gsont = gsonBuildert.create();
            Type membert = new TypeToken<Members>() {
            }.getType();
            Members memberObj = (Members) gsont.fromJson(params, membert);
            memberObj.set_page_no(lastPage);
            gsont.toString();*/

            getMembersListbyTypeByPageMSLW(lastPage);

        } else {
            recyclerAdapterMSLW.setMoreLoading(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);

    }


    @Override
    public void onItemClick(View view, Members members, int position, List<Members> items, Members memResultsObj) {
        //  Toast.makeText(getActivity(), members.get_path() + " clicked", Toast.LENGTH_SHORT).show();
 /*       Intent intent = new Intent(getActivity(), UserProfileActivity.class);
        intent.putExtra("userpath", members.getUserpath());
        startActivity(intent);*/

        Log.e("position", "position: " + position);
        items.clear();
        items.add(members);

        Activity activity = (Activity) getContext();
        MarryMax marryMax = new MarryMax(getActivity());
        if (ConnectCheck.isConnected(activity.findViewById(android.R.id.content))) {

            Log.e("Data list ", "" + items.size());
            Gson gson = new Gson();
            marryMax.statusBaseChecks(members, getContext(), 1, getFragmentManager(), DashMembersFragment.this, view, gson.toJson(items), position + "", memResultsObj, Tag);
        }

    }


}
