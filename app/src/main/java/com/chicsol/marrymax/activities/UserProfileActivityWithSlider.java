package com.chicsol.marrymax.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.ProfileSliderPagerAdapter;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserProfileActivityWithSlider extends AppCompatActivity {
    private ViewPager viewPagerProfileSlider;
    private ProfileSliderPagerAdapter profileSliderPagerAdapter;
    int selectedposition = -1;
    public List<Members> membersDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_with_slider);

        viewPagerProfileSlider = (ViewPager) findViewById(R.id.viewPagerUserProfilesLeftRight);

        String memdatalist = SharedPreferenceManager.getMembersDataList(getApplicationContext());
        //getIntent().getExtras().getString("memberdatalist");


        selectedposition = Integer.parseInt(getIntent().getExtras().getString("selectedposition"));
        Log.e("selec", "=- " + selectedposition);
        Gson gsonc;
        GsonBuilder gsonBuilderc = new GsonBuilder();
        gsonc = gsonBuilderc.create();


        Type listType = new TypeToken<List<Members>>() {
        }.getType();

        membersDataList = (List<Members>) gsonc.fromJson(memdatalist, listType);
        Log.e("mem list size", membersDataList.size() + "" + selectedposition);
        try {
            setupViewPager(viewPagerProfileSlider, membersDataList);
        } catch (Exception e) {

            Crashlytics.logException(e);
            e.printStackTrace();
            finish();
        }

    }

    private void setupViewPager(ViewPager viewPager, List<Members> membersDataList) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (membersDataList.size() > 0) {
            for (int i = 0; i < membersDataList.size(); i++) {


                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                Log.e("mem " + i, gson.toJson(membersDataList.get(i)));
                bundle.putString("userpath", membersDataList.get(i).getUserpath());

                UserProfileActivityFragment userProfileActivityFragment = new UserProfileActivityFragment();
                userProfileActivityFragment.setArguments(bundle);
                adapter.addFragment(userProfileActivityFragment, "ONE" + i);
            }

        }


        viewPager.setAdapter(adapter);
        if (selectedposition != -1) {
            viewPager.setCurrentItem(selectedposition);
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
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }



    }



}