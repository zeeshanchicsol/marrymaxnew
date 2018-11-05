package com.chicsol.marrymax.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.DashboarMainActivityWithBottomNav;
import com.chicsol.marrymax.fragments.matches.LookingForEachOther;
import com.chicsol.marrymax.fragments.matches.MatchesWithPhotoUpdate2Fragment;
import com.chicsol.marrymax.fragments.matches.MyFavouriteMatches;
import com.chicsol.marrymax.fragments.matches.MyMatchesFragment;
import com.chicsol.marrymax.fragments.matches.PrefferedMatchingProfileFragment;
import com.chicsol.marrymax.fragments.matches.WhoisLookingForMe;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 11/3/2016.
 */

public class DashboardMatchesMainFragment extends Fragment implements DashboarMainActivityWithBottomNav.BottomNavSelected {
    private ViewPager mViewPager;
    Typeface typeface;
    ViewPagerAdapter adapter;
    TabLayout tabLayout;
    View view1;
    public static boolean inittab = false;
    public static Context context;

    public interface MatchesMainFragmentInterface {
        void fragmentBecameVisible(Context context);
    }

    @Override
    public void bottomNavSelected() {
        if (!inittab) {

            setupTab();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Members member = SharedPreferenceManager.getUserObject(context);
        if (member.get_member_status() < 3 || member.get_member_status() >= 7) {
            new MarryMax(null).updateStatus(context);

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_matches_main, container, false);
        view1 = rootView;
        initialize(rootView);
        return rootView;
    }

    private void initialize(View rootView) {

        typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/centurygothic.ttf");


        //  TextView mTitle = (TextView) toolbar.findViewById(R.id.text_toolbar_title);
        //  mTitle.setTypeface(typeface);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        // Set up the ViewPager with the sections adapter.


        adapter = new ViewPagerAdapter(getChildFragmentManager());
        //adapter.

        mViewPager = (ViewPager) rootView.findViewById(R.id.container_matches_main);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs_matches_main);
        if (inittab) {
            setupTab();
            inittab = false;
        }

        //  setupTab();
        // tabLayout.setupWithViewPager(mViewPager);


    }


    private void setupTab() {

        if (tabLayout != null) {

            if (tabLayout.getTabCount() == 0) {
                setupViewPager(mViewPager);
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
            }
        } /*else {
            //  adapter = new ViewPagerAdapter(getChildFragmentManager());
            // FragmentTransaction fragmentTransaction = this.beginTransaction();
            //  mViewPager = (ViewPager) view1.findViewById(R.id.container_matches_main);
            //  tabLayout = (TabLayout) view1.findViewById(R.id.tabs_matches_main);
            //   setupTab();



            if (getActivity() == null) {
                //  Fragment currentFragment = getFragmentManager().findFragmentByTag("FRAGMENT");
                Fragment currentFragment = DashboardMatchesMainFragment.this;

                FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.detach(currentFragment);
                fragTransaction.attach(currentFragment);
                fragTransaction.commit();

            }
            else {
                setupTab();
            }
        }
*/

      /* else {
          TabLayout.Tab tab = tabLayout.getTabAt(0);
          tab.select();

      }
*/


     /*   mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
              *//*  MatchesMainFragmentInterface fragment = (MatchesMainFragmentInterface) adapter.instantiateItem(mViewPager, position);
                if (fragment != null) {
                    fragment.fragmentBecameVisible(getContext());
                }*//*
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
*/

    }


    private void setupViewPager(ViewPager viewPager) {

        //  Toast.makeText(context, "setup view pager", Toast.LENGTH_SHORT).show();
        adapter.addFragment(new PrefferedMatchingProfileFragment(), " Preferred Matches ");
        adapter.addFragment(new MyMatchesFragment(), " My Matches ");
        adapter.addFragment(new WhoisLookingForMe(), " Who is Looking For Me ");
        adapter.addFragment(new LookingForEachOther(), " Looking For Each Other ");
        adapter.addFragment(new MatchesWithPhotoUpdate2Fragment(), " Matches With Photo Update ");
      /*  adapter.addFragment(new MyFavouriteMatches(), " My Favourite Matches ");*/
     /*   adapter.addFragment(new AccpetedMembers(), " Accepted Members ");*/
       /*   adapter.addFragment(new PrivacySettingsFragment(), "Privacy Setting ");
        adapter.addFragment(new EmailNotificationsFragment(), " Email Notifications ");
        adapter.addFragment(new ChangePasswordFragment(), " Change Password ");
        adapter.addFragment(new AccountDeactivationFragment(), " Account Deactivation ");*/

        viewPager.setAdapter(adapter);

        TabLayout.Tab tab = tabLayout.getTabAt(0);

        //  viewPager.setCurrentItem(0);

        //adapter.notifyDataSetChanged();

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class ViewPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
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

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //   Toast.makeText(getActivity(), "pos " + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
