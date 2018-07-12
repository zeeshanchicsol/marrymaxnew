package com.chicsol.marrymax.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.ProfileSliderPagerAdapter;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.crashlytics.android.Crashlytics;
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

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;

public class UserProfileActivityWithSlider extends AppCompatActivity {
    private ViewPager viewPagerProfileSlider;
    private ProfileSliderPagerAdapter profileSliderPagerAdapter;
    int selectedposition = -1;
    private String params;
    public List<Members> membersDataList;

    ViewPagerAdapter vAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_with_slider);

        viewPagerProfileSlider = (ViewPager) findViewById(R.id.viewPagerUserProfilesLeftRight);

        vAdapter = new ViewPagerAdapter(getSupportFragmentManager());


        String memdatalist = SharedPreferenceManager.getMembersDataList(getApplicationContext());
        //getIntent().getExtras().getString("memberdatalist");


        selectedposition = Integer.parseInt(getIntent().getExtras().getString("selectedposition"));
        Log.e("selec", "=- " + selectedposition);
        Log.e("selec", "=- " + lastDigit(selectedposition));


        Gson gsonc;
        GsonBuilder gsonBuilderc = new GsonBuilder();
        gsonc = gsonBuilderc.create();


        Type listType = new TypeToken<List<Members>>() {
        }.getType();

        membersDataList = (List<Members>) gsonc.fromJson(memdatalist, listType);
        Log.e("mem list size", membersDataList.size() + "" + selectedposition);


        Members memberSearchObj = defaultSelectionsObj;
        if (memberSearchObj != null) {
            memberSearchObj.set_path(SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
            memberSearchObj.set_member_status(SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status());
            memberSearchObj.set_phone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_phone_verified());
            memberSearchObj.set_email_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_email_verified());
            //page and type
            //  memberSearchObj.set_page_no(1);
            memberSearchObj.set_type("");

            Gson gson = new Gson();
            String params = gson.toJson(memberSearchObj);
            listProfiles(params);


        }

        setListener();
    }


    private void setListener() {

        viewPagerProfileSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
           //     Log.e("onPageScrolled", "" + position + "    " + positionOffset + "    " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
               Log.e("onPageSelected", "" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
           //     Log.e("onPageScrollStateChange", "" + state);
            }
        });

    }

    public int lastDigit(int number) {
        return number % 10;
    }

    private void setupViewPager(ViewPager viewPager, List<String> membersDataList) {


        if (membersDataList.size() > 0) {
            for (int i = 0; i < membersDataList.size(); i++) {


                Bundle bundle = new Bundle();
                Gson gson = new Gson();


                Log.e("mem " + i, membersDataList.get(i));


                bundle.putString("userpath", membersDataList.get(i));

                UserProfileActivityFragment userProfileActivityFragment = new UserProfileActivityFragment();
                userProfileActivityFragment.setArguments(bundle);
                vAdapter.addFragment(userProfileActivityFragment, "ONE" + i);
            }

        }


        viewPager.setAdapter(vAdapter);

/*
        int spos = lastDigit(selectedposition);
        if (spos != -1) {
            viewPager.setCurrentItem(spos);
        }

        Log.e("possss", membersDataList.get(spos));*/
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

      /*  @Override
        public Parcelable saveState() {
            Bundle bundle = (Bundle) super.saveState();
            bundle.putParcelableArray("states", null); // Never maintain any states from the base class, just null it out
            return bundle;
        }*/

    /*    @Override
        public Parcelable saveState() {
            Bundle bundle = (Bundle) super.saveState();
            if (bundle != null) {
                Parcelable[] states = bundle.getParcelableArray("states"); // Subset only last 3 states
                if (states != null)
                    states = Arrays.copyOfRange(states, states.length > 3 ? states.length - 3 : 0, states.length - 1);
                bundle.putParcelableArray("states", states);
            } else bundle = new Bundle();
            return bundle;
        }*/

        @Override
        public Parcelable saveState() {
            Bundle bundle = (Bundle) super.saveState();
            if (bundle != null) {
                // Never maintain any states from the base class, just null it out
                bundle.putParcelableArray("states", null);
            } else {
                // do nothing
            }
            return bundle;
        }

    }

    private void listProfiles(String paramsString) {

        JSONObject params = null;
        try {
            params = new JSONObject(paramsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ProgressDialog pDialog = new ProgressDialog(UserProfileActivityWithSlider.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        Log.e("listProfiles params", params.toString());
        Log.e("listProfiles  path", Urls.listProfiles);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.listProfiles, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  listProfiles ", response + "");

                        try {
                            // JSONObject responseObject = response.getJSONArray("data").getJSONArray(0).getJSONObject(0);
                            int page_num = response.getInt("page_no");
                            Log.e("listProfiles page_num", "" + page_num);
                            int count = response.getInt("count");
                            Log.e(" listProfiles", "" + count);
                            JSONArray jsonArray = response.getJSONArray("prfids");

                            List<String> pathDataList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                pathDataList.add(jsonArray.get(i).toString());

                                Log.e("listProfiles size", "" + jsonArray.get(i).toString() + "====================");
                            }


                            Log.e("listProfiles size", "" + pathDataList.size() + "====================");
                            try {
                                setupViewPager(viewPagerProfileSlider, pathDataList);
                            } catch (Exception e) {

                                Crashlytics.logException(e);
                                e.printStackTrace();
                                finish();
                            }


                         /*   Gson gson;
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            Type type = new TypeToken<Members>() {
                            }.getType();
                            Members member2 = (Members) gson.fromJson(responseObject.toString(), type);
                            //  Log.e("interested id", "" + member.get_alias() + "====================");

                            dialogShowInterest newFragment = dialogShowInterest.newInstance(member, member.getUserpath(), replyCheck, member2);
                            newFragment.show(frgMngr, "dialog");*/


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }


                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                pDialog.dismiss();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }


}