package com.chicsol.marrymax.fragments.inbox.interests;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.DashboarMainActivityWithBottomNav;
import com.chicsol.marrymax.fragments.inbox.interests.DashboardInterestsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 11/3/2016.
 */

public class DashboardMyInterestsMainFragment extends Fragment implements DashboarMainActivityWithBottomNav.BottomNavSelected {
    private ViewPager mViewPager;
    Typeface typeface;
    Fragment last;
    private Context context;
    String subtype = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox_interests_main, container, false);
        initialize(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    /*    Members member = SharedPreferenceManager.getUserObject(context);
        if (member.getMember_status() < 3 || member.getMember_status() >= 7) {
            new MarryMax(null).updateStatus(context);

        }*/
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    private void initialize(View rootView) {

        typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/centurygothic.ttf");


        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs_account_setting);

        tabLayout.setupWithViewPager(mViewPager);

        mViewPager = (ViewPager) rootView.findViewById(R.id.container_account_setting);
        setupViewPager(mViewPager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs_account_setting);
        tabLayout.setupWithViewPager(mViewPager);


        for (int i = tabLayout.getTabCount() - 1; i >= 0; i--) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            LinearLayout relativeLayout = (LinearLayout)
                    LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_item, tabLayout, false);

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
        if (getArguments() != null) {
            subtype = getArguments().getString("subtype");

        }
        if (subtype != null) {
            if (subtype.equals("sent")) {
                TabLayout.Tab tab = tabLayout.getTabAt(1);
                tab.select();
            }

        }



    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString("type", "interest");
        bundle.putBoolean("withdrawcheck", false);


        Bundle bundle2 = new Bundle();
        bundle2.putString("type", "interestsent");
        bundle2.putBoolean("withdrawcheck", true);


        Fragment receieved = new DashboardInterestsFragment();
        receieved.setArguments(bundle);


        Fragment sent = new DashboardInterestsFragment();
        sent.setArguments(bundle2);

        adapter.addFragment(receieved, " Interests Received ");

        adapter.addFragment(sent, " Interests Sent ");

        viewPager.setAdapter(adapter);

    }

    @Override
    public void bottomNavSelected() {

    }

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
