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
    boolean firstTime = true;
    ViewPagerAdapter adapter;
    boolean addBackward = false;
    //load more
    int total_pages = 0;
    int current_page = 0;
    //===end

    String TAG = "UserProfileActivityWithSlider ";


    Members memberSearchObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_with_slider);

        viewPagerProfileSlider = (ViewPager) findViewById(R.id.viewPagerUserProfilesLeftRight);
        // viewPagerProfileSlider.setOffscreenPageLimit(1);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerProfileSlider.setAdapter(adapter);

        String memdatalist = SharedPreferenceManager.getMembersDataList(getApplicationContext());
        //getIntent().getExtras().getString("memberdatalist");

        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();


        memberSearchObj = gson.fromJson(getIntent().getStringExtra("memresult"), Members.class);


        Log.e(TAG + " TYPE", "=- " + memberSearchObj.get_type());
        Log.e(TAG + " memresult", "=- " + getIntent().getStringExtra("memresult"));


        selectedposition = Integer.parseInt(getIntent().getExtras().getString("selectedposition"));
        float t = ((selectedposition - 1) / 10) + 1;
        int pageNumber = (int) t;


        Log.e(TAG + " selectedposition", "=- " + selectedposition);
        Log.e(TAG + "selec", "=- " + lastDigit(selectedposition));


        Log.e(TAG + "pageNumber", "" + pageNumber);


        Gson gsonc;
        GsonBuilder gsonBuilderc = new GsonBuilder();
        gsonc = gsonBuilderc.create();


        Type listType = new TypeToken<List<Members>>() {
        }.getType();

        membersDataList = (List<Members>) gsonc.fromJson(memdatalist, listType);
        Log.e(TAG + "selectedposition", "" + selectedposition);


  /*      Members memberSearchObj = defaultSelectionsObj;
        if (memberSearchObj != null) {
            memberSearchObj.set_path(SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
            memberSearchObj.set_member_status(SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status());
            memberSearchObj.set_phone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_phone_verified());
            memberSearchObj.set_email_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).get_email_verified());
            //page and type
            memberSearchObj.set_page_no(pageNumber);
            memberSearchObj.set_type("");

            Log.e(TAG + " page_no", "= " + memberSearchObj.get_page_no());
            current_page = (int) memberSearchObj.get_page_no();

            Gson gson = new Gson();
            String params = gson.toJson(memberSearchObj);
            listUserProfiles(params);

        }*/


        current_page = pageNumber;
      /*  Members memberSearchObj = SharedPreferenceManager.getMemResultsObject(getApplicationContext());
        memberSearchObj.set_page_no(pageNumber);
        Gson gson = new Gson();*/


        memberSearchObj.set_page_no(pageNumber);
        String params = gson.toJson(memberSearchObj);
        Log.e(TAG + "params  memberSearchObj", "=- " + params);
        listUserProfiles(params);


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
                Log.e(TAG + "ff onPageSelected", "" + position);
                Log.e(TAG + "ff adapter.getCount()", "" + adapter.getCount());

                if (position == (adapter.getCount() - 1) && current_page <= total_pages) {
                    Log.e("ff in", "in ");

                    addBackward = false;
                    firstTime = false;


                    //  Members memberSearchObj = SharedPreferenceManager.getMemResultsObject(getApplicationContext());
                    memberSearchObj.set_page_no(current_page + 1);
                    Gson gson = new Gson();
                    String params = gson.toJson(memberSearchObj);
                    Log.e(TAG + " params string", params + "==");

                    listUserProfiles(params);


                }


                if (position == (1) && current_page <= total_pages) {

                    addBackward = true;
                    Log.e("ff in", "in ");

                    firstTime = false;
                    Log.e("page minus in", current_page - 1+"");


                    memberSearchObj.set_page_no(current_page - 1);
                    Gson gson = new Gson();
                    String params = gson.toJson(memberSearchObj);
                    Log.e(TAG + " params string", params + "==");

                    listUserProfiles(params);




                }


                //   Log.e("adapter size", "" +    adapter.getCount());


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

    private void updateDataList(List<String> membersDataList) {


        if (membersDataList.size() > 0) {
            for (int i = 0; i < membersDataList.size(); i++) {


                Bundle bundle = new Bundle();
                Gson gson = new Gson();


                Log.e("mem " + i, membersDataList.get(i));


                bundle.putString("userpath", membersDataList.get(i));

                UserProfileActivityFragment userProfileActivityFragment = new UserProfileActivityFragment();
                userProfileActivityFragment.setArguments(bundle);
                adapter.addFragment(userProfileActivityFragment, "ONE" + i);
            }
            adapter.notifyDataSetChanged();
        }
        // viewPager.setAdapter(adapter);
      /*  if (spos != -1) {
            viewPager.setCurrentItem(spos);
        }*/

    }

    private void setCurrentPosition() {

        int spos = lastDigit(selectedposition);
        if (spos != -1) {
            viewPagerProfileSlider.setCurrentItem(spos);
        }
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

            if (addBackward) {
                mFragmentList.add(0, fragment);
                mFragmentTitleList.add(0, title);
            } else {
                mFragmentList.add(fragment);
                mFragmentTitleList.add(title);
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


        public void clear() {
            mFragmentList.clear();
            mFragmentTitleList.clear();

            notifyDataSetChanged();
        }


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

    private void listUserProfiles(String paramsString) {

        JSONObject params = null;
        try {
            params = new JSONObject(paramsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ProgressDialog pDialog = new ProgressDialog(UserProfileActivityWithSlider.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        Log.e(TAG + "listProfiles", params.toString());
        Log.e(TAG + "listProfilespath", Urls.listProfiles);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.listProfiles, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  listProfiles ", response + "");

                        try {
                            // JSONObject responseObject = response.getJSONArray("data").getJSONArray(0).getJSONObject(0);
                            int page_num = response.getInt("page_no");
                            Log.e(TAG + "listProfiles page_num", "" + page_num);
                            int count = response.getInt("count");
                            Log.e(TAG + " listProfiles", "" + count);

                            total_pages = Math.round(count / 10);

                            Log.e(TAG + "total_pages aa", total_pages + "");
                            JSONArray jsonArray = response.getJSONArray("prfids");

                            List<String> pathDataList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                pathDataList.add(jsonArray.get(i).toString());

                                Log.e(TAG + "listProfiles size", "" + jsonArray.get(i).toString() + "====================");
                            }


                            Log.e(TAG + "listProfiles size", "" + pathDataList.size() + "====================");
                            try {
                                //  up(viewPagerProfileSlider, pathDataList);
                                updateDataList(pathDataList);

                                if (firstTime) {
                                    setCurrentPosition();
                                }
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