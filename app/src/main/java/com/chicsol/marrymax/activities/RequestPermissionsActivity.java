package com.chicsol.marrymax.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterRequestPermissions;
import com.chicsol.marrymax.fragments.requestpermission.RequestPermissionFragment;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mRequestPermission;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muneeb on 7/31/17.
 */

public class RequestPermissionsActivity extends AppCompatActivity implements RecyclerViewAdapterRequestPermissions.OnUpdateListener {
    Members member;
    String memberString;

    private RecyclerView recyclerViewRequest, recyclerViewPermission;
    private RecyclerViewAdapterRequestPermissions recyclerAdapterRequest, recyclerAdapterPermission;
    private List<mRequestPermission> mRequestDataList, mPermissionDataList;
    ViewPagerAdapter1 adapter;
    private ViewPager viewPager;
    TabLayout tabLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_permissions);
        String memberString = getIntent().getStringExtra("member");

        Log.e("Member String is ", "" + memberString);
        Gson gson = new Gson();
        this.memberString = memberString;
        member = gson.fromJson(memberString, Members.class);
        Log.e("Alias", member.getAlias());
        initialize();

        //  requestPermission();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void initialize() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout1 = (TabLayout) findViewById(R.id.TablayoutRequestPermission);
        adapter = new ViewPagerAdapter1(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpagerRequestPermission);


        // pDialog = (ProgressBar) findViewById(R.id.ProgressbarProjectMain);
     /*   mRequestDataList = new ArrayList<>();
        mPermissionDataList = new ArrayList<>();
        recyclerViewRequest = (RecyclerView) findViewById(R.id.RecyclerViewRPRequest);

        recyclerViewRequest.setLayoutManager(new GridLayoutManager((getApplicationContext()), 1));

        recyclerAdapterRequest = new RecyclerViewAdapterRequestPermissions(mRequestDataList, getApplicationContext(), false, member.getUserpath(), RequestPermissionsActivity.this, this, member.getAlias());

        recyclerViewRequest.setAdapter(recyclerAdapterRequest);


        recyclerViewPermission = (RecyclerView) findViewById(R.id.RecyclerViewRPPermission);

        recyclerViewPermission.setLayoutManager(new GridLayoutManager((getApplicationContext()), 1));

        recyclerAdapterPermission = new RecyclerViewAdapterRequestPermissions(mPermissionDataList, getApplicationContext(), true, member.getUserpath(), RequestPermissionsActivity.this, this, member.getAlias());

        recyclerViewPermission.setAdapter(recyclerAdapterPermission);
*/
        setupViewPager();
    }

    private void setupViewPager() {


        RequestPermissionFragment rp1 = new RequestPermissionFragment();
        Bundle argsRequest = new Bundle();
        argsRequest.putBoolean("permissioncheck", false);
        // argsRequest.putString("jsonData", jsonArryaResponse1);
        argsRequest.putString("member", memberString);
        rp1.setArguments(argsRequest);


        RequestPermissionFragment rp2 = new RequestPermissionFragment();
        Bundle argsPermission = new Bundle();
        argsPermission.putBoolean("permissioncheck", true);
        // argsRequest.putString("jsonData", jsonArryaResponse1);
        argsPermission.putString("member", memberString);
        rp2.setArguments(argsPermission);


        //  adapter.clearFragments();


        adapter.addFragment(rp2, "Ask For Permissions");


        adapter.addFragment(rp1, "Grant Permissions");
        ///adapter.addFragment(messageHistoryFragment, "Message History");


        // viewPager.setAdapter(null);
        viewPager.setAdapter(adapter);
        tabLayout1.setupWithViewPager(viewPager);
        adapter.notifyDataSetChanged();

        for (int i = tabLayout1.getTabCount() - 1; i >= 0; i--) {
            TabLayout.Tab tab = tabLayout1.getTabAt(i);
            LinearLayout relativeLayout = (LinearLayout)
                    LayoutInflater.from(RequestPermissionsActivity.this).inflate(R.layout.custom_user_tab_item, tabLayout1, false);
            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title1);
            tabTextView.setText(tab.getText());

            if (i == tabLayout1.getTabCount() - 1) {
                View view1 = (View) relativeLayout.findViewById(R.id.tab_view_separator1);
                view1.setVisibility(View.INVISIBLE);
            }
            tab.setCustomView(relativeLayout);
            tab.select();
        }
        {
            //    TabLayout.Tab tabs = tabLayout1.getTabAt(lastSelectedPage);
            //   tabs.select();
        }


    }

    public void setListeners() {


    }

/*    private void requestPermission() {

        final ProgressDialog pDialog = new ProgressDialog(RequestPermissionsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject params = new JSONObject();
        try {


            params.put("userpath", member.getUserpath());
            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Params", Urls.requestPermissions + "=====" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.requestPermissions, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re request permissions", response + "");
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

                            mRequestDataList = (List<mRequestPermission>) gsonc.fromJson(jsonArrayRequests.toString(), listType);
                            mPermissionDataList = (List<mRequestPermission>) gsonc.fromJson(jsonArrayPermissions.toString(), listType);

                            recyclerAdapterRequest.addAll(mRequestDataList);
                            recyclerAdapterPermission.addAll(mPermissionDataList);

                            Log.e("mRequestDataList" + mRequestDataList.size(), "" + mPermissionDataList.size());


                        } catch (JSONException e) {
                            pDialog.hide();
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.hide();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }*/

    @Override
    public void onUpdate(String msg) {
        //   Toast.makeText(this, "Clicked ", Toast.LENGTH_SHORT).show();
        // requestPermission();
    }


    class ViewPagerAdapter1 extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter1(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        public void clearFragments() {
            mFragmentList.clear();
            mFragmentTitleList.clear();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
