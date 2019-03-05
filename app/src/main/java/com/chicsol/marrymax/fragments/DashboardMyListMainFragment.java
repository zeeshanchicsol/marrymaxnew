package com.chicsol.marrymax.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.DashboarMainActivityWithBottomNav;
import com.chicsol.marrymax.fragments.list.MyFeedbackFragment;
import com.chicsol.marrymax.fragments.list.MySavedListsFragment;
import com.chicsol.marrymax.fragments.list.MySavedSearchesFragment;
import com.chicsol.marrymax.fragments.list.RecommendedMatches;
import com.chicsol.marrymax.fragments.list.RemovedFromSearchFragment;
import com.chicsol.marrymax.fragments.list.myContacts.MyContactsMainFragment;
import com.chicsol.marrymax.fragments.matches.AccpetedMembers;
import com.chicsol.marrymax.fragments.matches.FavouriteMembers;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 11/3/2016.
 */

public class DashboardMyListMainFragment extends Fragment implements DashboarMainActivityWithBottomNav.BottomNavSelected {
    private ViewPager mViewPager;
    Typeface typeface;
private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mylist_main, container, false);
        initialize(rootView);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
      /*  Members member= SharedPreferenceManager.getUserObject(context);
        if (member.get_member_status() < 3 || member.get_member_status() >= 7) {
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


        //  TextView mTitle = (TextView) toolbar.findViewById(R.id.text_toolbar_title);
        //  mTitle.setTypeface(typeface);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        // Set up the ViewPager with the sections adapter.


        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs_mylist_main);

        // tabLayout.setupWithViewPager(mViewPager);

        mViewPager = (ViewPager) rootView.findViewById(R.id.container_mylist_main);
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
            //tab.select();
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
     //  adapter.addFragment(new MyFavouritesFragment(), " My Favourites ");
        adapter.addFragment(new FavouriteMembers(), " Favourite Members ");
        adapter.addFragment(new AccpetedMembers(), " Accepted Members ");
       adapter.addFragment(new MyContactsMainFragment(), " My Contacts ");
       adapter.addFragment(new MySavedListsFragment(), " My Saved List ");
      //  adapter.addFragment(new MyNotesFragment(), " My Notes ");
        adapter.addFragment(new MySavedSearchesFragment(), " My Saved Searches ");
        adapter.addFragment(new RemovedFromSearchFragment(), " Removed From Search ");
        adapter.addFragment(new RecommendedMatches(), " Recommended Matches ");
        adapter.addFragment(new MyFeedbackFragment(), "Feedback");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void bottomNavSelected() {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private long baseId = 0;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return baseId + position;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
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
