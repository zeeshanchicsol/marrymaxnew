package com.chicsol.marrymax.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.Toast;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.activities.subscription.SubscriptionPlanActivity;
import com.chicsol.marrymax.dialogs.dialogProfileCompletion;
import com.chicsol.marrymax.fragments.DashboardAccountSettingFragment;
import com.chicsol.marrymax.fragments.DashboardInboxMainFragment;
import com.chicsol.marrymax.fragments.DashboardMainFragment;
import com.chicsol.marrymax.fragments.DashboardMatchesMainFragment;
import com.chicsol.marrymax.fragments.DashboardMyListMainFragment;
import com.chicsol.marrymax.helperMethods.BottomNavigationViewHelper;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.other.UserSessionManager;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.utils.ConnectCheck;

import java.util.ArrayList;
import java.util.List;

import static com.chicsol.marrymax.fragments.DashboardMatchesMainFragment.context;


public class DashboarMainActivityWithBottomNav extends DrawerActivity implements NavigationView.OnNavigationItemSelectedListener, dialogProfileCompletion.onCompleteListener {


    //  private Fragment fragment;
    //  private FragmentManager fragmentManager;
    ViewPagerAdapter adapter;
    //  private ImageButton ib_MyLists;
    private ViewPager mViewPager;
    //   private Typeface typeface;
    //  private MaterialRippleLayout ripp;
    private Members member;
    private int[] idList;
    int selectedTab;

    private BottomNavigationView bottomNavigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
            switch (item.getItemId()) {

                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(0);
                    setTitle("Dashboard");
                    //mTextMessage.setText(R.string.title_dashboard);
                    break;
                case R.id.navigation_mymatches:
                    mViewPager.setCurrentItem(1);
                    setTitle("Matches");
                    adapter.notifyDataSetChanged();
                    //  mTextMessage.setText(R.string.title_home);
                    break;
                case R.id.navigation_messages:
                    mViewPager.setCurrentItem(2);
                    setTitle("Inbox");
                    ///  mTextMessage.setText(R.string.title_notifications);
                    break;
                case R.id.navigation_mylist:

                    mViewPager.setCurrentItem(3);
                    setTitle("My List");
                    //  fragment = new DashboardMainFragment();
                    break;
                case R.id.navigation_myaccount:
                    mViewPager.setCurrentItem(4);
                    setTitle("Account Settings");
                    //  fragment = new DashboardMainFragment();
                    // mTextMessage.setText(R.string.title_notifications);
                    break;


            }

            //  }
           /* final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content_dash_main_bottom_nav, fragment).commit();*/
            return true;
        }

    };

    @Override
    public void onComplete(int step) {
        if (step == 3) {

            mViewPager.setCurrentItem(4);
            setTitle("Account Settings");
        }

    }


    public interface BottomNavSelected {
        void bottomNavSelected();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main_bottom_navigation);

        //  typeface = Typeface.createFromAsset(getAssets(), Constants.font_centurygothic);


        selectedTab = getIntent().getIntExtra("name", 0);
        //   Toast.makeText(this, "sss is " + selectedTab, Toast.LENGTH_SHORT).show();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);


        initialize();
        setListenders();
        setSelectedTab();
        //  determinePaneLayout();
    }

    private void setSelectedTab() {

        if (selectedTab != 0) {
            mViewPager.setCurrentItem(selectedTab);
            bottomNavigation.setSelectedItemId(selectedTab);
        }


    }

    private void initialize() {
/*        ripp=(MaterialRippleLayout)findViewById(R.id.ripple);

     MaterialRippleLayout.on(ripp)
                .rippleColor(Color.BLACK)
                .create();*/
        //ib_MyLists = (ImageButton) findViewById(R.id.ImageButtonMyList);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        idList = new int[5];
        idList[0] = R.id.navigation_dashboard;
        idList[1] = R.id.navigation_mymatches;
        idList[2] = R.id.navigation_messages;
        idList[3] = R.id.navigation_mylist;
        idList[4] = R.id.navigation_myaccount;

        mViewPager = (ViewPager) findViewById(R.id.container_dashmain_nav);
        setupViewPager(mViewPager);
        member = SharedPreferenceManager.getUserObject(getApplication());

        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
        //    fragmentManager = getSupportFragmentManager();
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }


    private void setListenders() {

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //  Log.e("onPageScrolled",""+position);
            }

            @Override
            public void onPageSelected(int position) {
                // Log.e("onPageSelected","==============================   "+position);

                bottomNavigation.setSelectedItemId(idList[position]);

                BottomNavSelected fragment = (BottomNavSelected) adapter.instantiateItem(mViewPager, position);
                if (fragment != null) {
                    //if (selectedTab != 2) {
                    DashboarMainActivityWithBottomNav.super.getNotificationCount();
                    fragment.bottomNavSelected();
                    //}
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void setupViewPager(ViewPager viewPager) {

        adapter.addFragment(new DashboardMainFragment(), " Dashboard ");
        adapter.addFragment(new DashboardMatchesMainFragment(), " Matches ");
        adapter.addFragment(new DashboardInboxMainFragment(), " Inbox ");
        adapter.addFragment(new DashboardMyListMainFragment(), " My List ");
        adapter.addFragment(new DashboardAccountSettingFragment(), "AccountSetting");

        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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

       /* @Override
        public Parcelable saveState() {
            return null;
        }*/

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.action_search) {
            if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
                MarryMax max = new MarryMax(DashboarMainActivityWithBottomNav.this);
                max.onSearchClicked(getApplicationContext(), 0);

            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

/*
    //for getting default search data
    private void getData() {

        //  Log.e("url", Urls.getSearchLists + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getSearchLists + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        jsonArraySearch = response;
                        //  pDialog.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());

                // pDialog.setVisibility(View.INVISIBLE);
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }
*/


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main_dashboard) {
            mViewPager.setCurrentItem(0);
            setTitle("Dashboard");
           /* Intent in = new Intent(DashboarMainActivityWithBottomNav.this, DashboarMainActivityWithBottomNav.class);
            startActivity(in);
            finish();*/
        } else if (id == R.id.nav_main_mymatches) {
            mViewPager.setCurrentItem(1);
            setTitle("Matches");
        } else if (id == R.id.nav_main_mymessages) {
            mViewPager.setCurrentItem(2);
            setTitle("Inbox");
        } else if (id == R.id.nav_main_mylist) {
            mViewPager.setCurrentItem(3);
            setTitle("My List");
        } else if (id == R.id.nav_main_myaccount) {
            mViewPager.setCurrentItem(4);
            setTitle("Account Settings");
        } else if (id == R.id.nav_main_uploadphotos) {
            MarryMax marryMax = new MarryMax(DashboarMainActivityWithBottomNav.this);
            if (marryMax.uploadPhotoCheck(getApplicationContext())) {
                Intent in = new Intent(DashboarMainActivityWithBottomNav.this, PhotoUpload.class);
                startActivity(in);
            }

        } else if (id == R.id.nav_main_editprofile) {
            //  getProfileProgress();
            MarryMax marryMax = new MarryMax(DashboarMainActivityWithBottomNav.this);
            marryMax.getProfileProgress(getApplicationContext(), member, DashboarMainActivityWithBottomNav.this);

        } else if (id == R.id.nav_main_profile_settings) {

            Intent in = new Intent(getApplicationContext(), MainDirectiveActivity.class);
            in.putExtra("type", 22);
            startActivity(in);

        } else if (id == R.id.nav_main_advsearch) {
            MarryMax max = new MarryMax(DashboarMainActivityWithBottomNav.this);
            max.onSearchClicked(getApplicationContext(), 0);

        } else if (id == R.id.nav_main_logout) {
            UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
            sessionManager.logoutUser();
        } else if (id == R.id.nav_main_faq) {
            Intent in = new Intent(DashboarMainActivityWithBottomNav.this, FaqActivity.class);
            startActivity(in);
            finish();
        } else if (id == R.id.nav_main_subscription_plan) {
            Intent in = new Intent(DashboarMainActivityWithBottomNav.this, SubscriptionPlanActivity.class);
            startActivity(in);

        } else if (id == R.id.nav_main_rate_app) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            }

        } else if (id == R.id.nav_main_contact_us) {
            MarryMax max = new MarryMax(DashboarMainActivityWithBottomNav.this);
            max.contact();

        }


        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press Back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 4000);
    }

}
