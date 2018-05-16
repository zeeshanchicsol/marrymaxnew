package com.chicsol.marrymax.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.fragments.AccountSetting.AccountDeactivationFragment;
import com.chicsol.marrymax.fragments.AccountSetting.ChangePasswordFragment;
import com.chicsol.marrymax.fragments.AccountSetting.EmailNotificationsFragment;
import com.chicsol.marrymax.fragments.AccountSetting.MatchingAttributeFragment;
import com.chicsol.marrymax.fragments.AccountSetting.MyContactFragment;
import com.chicsol.marrymax.fragments.AccountSetting.MySubscriptionFragment;
import com.chicsol.marrymax.fragments.AccountSetting.PrivacySettingsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 11/29/2016.
 */

public class AccountSettingActivity extends DrawerActivity  {
    private ViewPager mViewPager;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting_main);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/centurygothic.ttf");
//asdasd

        //  TextView mTitle = (TextView) toolbar.findViewById(R.id.text_toolbar_title);
        //  mTitle.setTypeface(typeface);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        // Set up the ViewPager with the sections adapter.


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_account_setting);

        tabLayout.setupWithViewPager(mViewPager);

        mViewPager = (ViewPager) findViewById(R.id.container_account_setting);
        setupViewPager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs_account_setting);
        tabLayout.setupWithViewPager(mViewPager);


        for (int i = tabLayout.getTabCount() - 1; i >= 0; i--) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            LinearLayout relativeLayout = (LinearLayout)
                    LayoutInflater.from(this).inflate(R.layout.custom_tab_item, tabLayout, false);

            if (i == tabLayout.getTabCount() - 1) {
                View view1 = (View) relativeLayout.findViewById(R.id.tab_view_separator);
                view1.setVisibility(View.INVISIBLE);
            }
            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title);
            tabTextView.setText(tab.getText());
            //tabTextView.setTypeface(Typeface.create("sans-serif-light", Typeface.BOLD));

            tab.setCustomView(relativeLayout);
            tab.select();
        }





    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyContactFragment(), " My Contact ");
        adapter.addFragment(new MySubscriptionFragment(), " My Subscription ");
        adapter.addFragment(new MatchingAttributeFragment(), " Matching Attributes ");
        adapter.addFragment(new PrivacySettingsFragment(), "Privacy Setting ");
        adapter.addFragment(new EmailNotificationsFragment(), " Email Notifications ");
        adapter.addFragment(new ChangePasswordFragment(), " Change Password ");
        adapter.addFragment(new AccountDeactivationFragment(), " Account Deactivation ");

        viewPager.setAdapter(adapter);

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
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
