package com.chicsol.marrymax.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.fragments.DashboardMainFragment;
import com.chicsol.marrymax.fragments.inbox.interests.DashboardInterestsFragment;
import com.chicsol.marrymax.fragments.inbox.DashboardMessagesFragment;
import com.chicsol.marrymax.fragments.inbox.requests.DashboardRequestsFragment;
import com.chicsol.marrymax.fragments.list.myContacts.MyContactsFragment;
import com.chicsol.marrymax.fragments.list.MyNotesFragment;
import com.chicsol.marrymax.fragments.list.MySavedListsFragment;
import com.chicsol.marrymax.fragments.list.MySavedSearchesFragment;
import com.chicsol.marrymax.fragments.matches.MyMatchesFragment;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/*import com.balysv.materialripple.MaterialRippleLayout;*/

public class DashboarMainActivity extends DrawerActivity {


    private ImageButton ib_MyLists;
    private ViewPager mViewPager;
    private Typeface typeface;
    //  private MaterialRippleLayout ripp;
    private Members member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main1);

        typeface = Typeface.createFromAsset(getAssets(), Constants.font_centurygothic);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);

        //  TextView mTitle = (TextView) toolbar.findViewById(R.id.text_toolbar_title);
        //  mTitle.setTypeface(typeface);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        // Set up the ViewPager with the sections adapter.


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(mViewPager);

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
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
            tabTextView.setTypeface(typeface);

            tab.setCustomView(relativeLayout);
            tab.select();
        }
      /*  TabLayout.Tab tab = tabLayout1.getTabAt(2);

        tab.select();
*/




/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


/*        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/


        //  drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
/*        navigationView.setNavigationItemSelectedListener(this);*/

        initialize();
        setListenders();
        //  determinePaneLayout();
    }


    private void initialize() {
/*        ripp=(MaterialRippleLayout)findViewById(R.id.ripple);

     MaterialRippleLayout.on(ripp)
                .rippleColor(Color.BLACK)
                .create();*/
        ib_MyLists = (ImageButton) findViewById(R.id.ImageButtonMyList);
        member = SharedPreferenceManager.getUserObject(getApplication());


    }


    private void setListenders() {


        ib_MyLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), ib_MyLists);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu_main_tabs, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {


                        switch (item.getItemId()) {
                            case R.id.menu_myFavourites:
                              /*  Intent intentf = new Intent(DashboarMainActivity.this, MyFavouritesFragment.class);
                                startActivity(intentf);
                              */  break;

                            case R.id.menu_accpetedMember:
                            /*    Intent intenta = new Intent(DashboarMainActivity.this, AcceptedMembersFragment.class);
                                startActivity(intenta);*/
                                break;


                            case R.id.menu_myContacts:
                                Intent intent = new Intent(DashboarMainActivity.this, MyContactsFragment.class);
                                startActivity(intent);
                                break;

                            case R.id.menu_myNotes:
                                Intent intentn = new Intent(DashboarMainActivity.this, MyNotesFragment.class);
                                startActivity(intentn);
                                break;

                            case R.id.menu_mySavedLists:
                                Intent intents = new Intent(DashboarMainActivity.this, MySavedListsFragment.class);
                                startActivity(intents);
                                break;

                            case R.id.menu_mySavedSearches:
                                Intent intentsav = new Intent(getApplicationContext(), MySavedSearchesFragment.class);
                                startActivityForResult(intentsav, 2);
                                break;

                        }


                        return true;
                    }

                });
                popup.show(); //showing popup menu
            }
        });
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
           *//* if (resultCode == RESULT_OK) {*//*
            String message = data.getStringExtra("MESSAGE");
            Log.e("===========", "==================");
            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
            //   textView1.setText(message);
        }

    }*/

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DashboardMainFragment(), " Dashboard ");
        adapter.addFragment(new MyMatchesFragment(), " My Matches ");
        adapter.addFragment(new DashboardMessagesFragment(), " Messages ");
        adapter.addFragment(new DashboardInterestsFragment(), " Interest ");
        adapter.addFragment(new DashboardRequestsFragment(), "Request");

        viewPager.setAdapter(adapter);

    }

 /*   @Override
    public void onComplete(String s) {
        Log.e("clicked", "Clicked");
        Log.e("--------------------", "---------------------------------------------------");
    }
*/
    //.............Notification Icon toolbar


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //====================================================================================
    @Override
    public boolean onOptionsItemSelected(MenuItem item_image_slider) {
        // Handle action bar item_image_slider clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item_image_slider.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_hotlist) {
            return true;
        }

        return super.onOptionsItemSelected(item_image_slider);
    }
*/

/*

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
*/


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
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

   /* @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item_image_slider) {
        // Handle navigation view item_image_slider clicks here.
        int id = item_image_slider.getItemId();

       *//* if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*//*

  *//*      DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*//*
        return true;
    }

*/
    //========adv search=====================================================
  /*  private void determinePaneLayout() {


        ListViewAdvSearchFragment fragmentItemsList =
                (ListViewAdvSearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentItemsList);
        fragmentItemsList.setActivateOnItemClick(true);

    }

    @Override
    public void onItemSelected(mAdvSearchListing item_image_slider) {
        // single activity with list and detail
        // Replace frame layout with correct detail fragment
        Fragment fragmentItem = item_image_slider.getFragment();

        //ItemDetailFragment.newInstance(item_image_slider);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flDetailContainer, fragmentItem);
        ft.commit();
    }*/

/*

    public void onMySaevdSearches(View view) {
        Intent in = new Intent(DashboarMainActivity.this, MySavedSearchesFragment.class);
        startActivity(in);

    }


    public void onFAQClick(View view) {
        Intent in = new Intent(DashboarMainActivity.this, FaqActivity.class);
        startActivity(in);

    }
*/
    //=====================================
 /*   public void showListPopup(View anchor) {
        ListPopupWindow popup = new ListPopupWindow(this);
        popup.setAnchorView(anchor);

        ListAdapter adapter = new MyAdapter(this);
        popup.setAdapter(adapter);
        popup.show();
    }


    public static class MyAdapter extends BaseAdapter implements ListAdapter {
        private final String[] list = new String[] {"one","two","three"};
        private Activity activity;
        public MyAdapter(Activity activity) {
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public Object getItem(int position) {
            return list[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private static int textid = 1234;
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView text = null;
            if (convertView == null) {
                LinearLayout layout = new LinearLayout(activity);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                text = new TextView(activity);
                text.setId(textid);
                layout.addView(text);
                convertView = layout;
            } else {
                text = (TextView)convertView.findViewById(android.R.layout.simple_dropdown_item_1line);
            }
            text.setText(list[position]);
            return convertView;
        }
    }
*/

}
