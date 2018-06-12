package com.chicsol.marrymax.activities.registration;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.DashboarMainActivityWithBottomNav;
import com.chicsol.marrymax.activities.DrawerActivity;
import com.chicsol.marrymax.activities.FaqActivity;
import com.chicsol.marrymax.activities.MyProfileActivity;
import com.chicsol.marrymax.activities.PhotoUpload;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.activities.search.SearchMainActivity;
import com.chicsol.marrymax.dialogs.dialogRequestProfileUpdate;
import com.chicsol.marrymax.fragments.DashboardMatchesMainFragment;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.other.UserSessionManager;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.mTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;

import java.util.Map;

import static com.chicsol.marrymax.utils.Constants.jsonArraySearch;

/**
 * Created by Android on 1/12/2017.
 */

public class BaseRegistrationActivity extends DrawerActivity implements NavigationView.OnNavigationItemSelectedListener {
    public LinearLayout geographicView, appearanceView, lifestyleView, interestView, personalityView;
    public FloatingActionButton fabAppearance, fabGeographic, fabLifestyle, fabInterest, fabPersonality;
    public mTextView tvGeogrphic, tvAppearance, tvLifestyle, tvPersonality, tvInterest;
    public LinearLayout LinearLayoutDrawerMyProfile;
    private Members member;
    DrawerLayout drawer;

    public AppCompatButton btRequestProfileUpdate;
    public AppCompatImageButton btExpandRequestUpdateLayout;
    LinearLayout llRequestProfileUpdaye;
    MarryMax marryMax;
    private DisplayImageOptions options;
    private LayoutInflater inflater;
    private boolean visible = false;
    public TextView tvFabCount1, tvFabCount2, tvFabCount3, tvFabCount4, tvFabCount5;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        onCreateDrawer();
        onCreateReg();
    }


    public void onCreateReg() {

        initialize();
        setListeners();
        Log.e("Main Class", this.getClass().getSimpleName() + "");
    }

    public void onCreateDrawer() {
        initialize();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    private void initialize() {
        marryMax = new MarryMax(BaseRegistrationActivity.this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);
        member = SharedPreferenceManager.getUserObject(getApplication());

        LinearLayoutDrawerMyProfile = (LinearLayout) findViewById(R.id.LinearLayoutDrawerMyProfile);


        mTextView tcUserName = (mTextView) findViewById(R.id.TextViewNavUserName);

        tcUserName.setText(member.get_personal_name());


        geographicView = (LinearLayout) findViewById(R.id.ViewGeographic);
        appearanceView = (LinearLayout) findViewById(R.id.ViewAppearance);
        lifestyleView = (LinearLayout) findViewById(R.id.ViewLifeStyle);
        interestView = (LinearLayout) findViewById(R.id.ViewInterest);
        personalityView = (LinearLayout) findViewById(R.id.ViewPersonality);


        fabGeographic = (FloatingActionButton) findViewById(R.id.fabGeographic);
        fabAppearance = (FloatingActionButton) findViewById(R.id.fabAppearance);
        fabLifestyle = (FloatingActionButton) findViewById(R.id.fabLifeStyle);
        fabInterest = (FloatingActionButton) findViewById(R.id.fabInterest);
        fabPersonality = (FloatingActionButton) findViewById(R.id.fabPersonality);
        llRequestProfileUpdaye = (LinearLayout) findViewById(R.id.LinearLayoutRequestProfileUpdate);

        btRequestProfileUpdate = (AppCompatButton) findViewById(R.id.ButtonRequestProfileUpdate);
        btExpandRequestUpdateLayout = (AppCompatImageButton) findViewById(R.id.ImageButtonRequestUpdateExpand);

        tvGeogrphic = (mTextView) findViewById(R.id.TextViewRegTopGeography);
        tvAppearance = (mTextView) findViewById(R.id.TextViewRegTopAppearance);
        tvLifestyle = (mTextView) findViewById(R.id.TextViewRegTopLifeStyle);
        tvInterest = (mTextView) findViewById(R.id.TextViewRegTopInterest);
        tvPersonality = (mTextView) findViewById(R.id.TextViewRegTopPersonality);

        tvFabCount1 = (TextView) findViewById(R.id.fabCount1);
        tvFabCount2 = (TextView) findViewById(R.id.fabCount2);
        tvFabCount3 = (TextView) findViewById(R.id.fabCount3);
        tvFabCount4 = (TextView) findViewById(R.id.fabCount4);
        tvFabCount5 = (TextView) findViewById(R.id.fabCount5);

        if (marryMax.getUpdateCheck(getApplicationContext())) {

            fabGeographic.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepComplete));
            fabAppearance.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepComplete));
            fabLifestyle.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepComplete));
            fabInterest.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepComplete));
            fabPersonality.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRegistrationStepComplete));


            tvGeogrphic.setTextColor(getResources().getColor(R.color.colorRegistrationStepComplete));
            tvAppearance.setTextColor(getResources().getColor(R.color.colorRegistrationStepComplete));
            tvLifestyle.setTextColor(getResources().getColor(R.color.colorRegistrationStepComplete));
            tvInterest.setTextColor(getResources().getColor(R.color.colorRegistrationStepComplete));
            tvPersonality.setTextColor(getResources().getColor(R.color.colorRegistrationStepComplete));

            tvFabCount1.setVisibility(View.GONE);
            tvFabCount2.setVisibility(View.GONE);
            tvFabCount3.setVisibility(View.GONE);
            tvFabCount4.setVisibility(View.GONE);
            tvFabCount5.setVisibility(View.GONE);
        }

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

        inflater = LayoutInflater.from(getApplicationContext());
        options = new DisplayImageOptions.Builder()
                //   .showImageOnLoading(resize(R.drawable.loading_sm))
                // .showImageOnLoading(resize(R.drawable.loading))
                // .showImageForEmptyUri(resize(R.drawable.oops_sm))
                // .showImageForEmptyUri(resize(R.drawable.no_image))
                //.showImageOnFail(resize(R.drawable.oops_sm))
                .cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)

             /*   .postProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bmp) {

                        Bitmap bmp_sticker;
                        Display display =getContext().getWindowManager().getDefaultDisplay();
                        DisplayMetrics metrics = new DisplayMetrics();

                        display.getMetrics(metrics);

                        int widthScreen = metrics.widthPixels;
                        int heightScreen = metrics.heightPixels;
                        if (widthScreen > heightScreen) {
                            int h = (int) (heightScreen * 0.046);//it set the height of image 10% of your screen
                            //     iv.getLayoutParams().width = (int) (widthScreen * 0.10);
                            Log.e("wid " + widthScreen + "  " + heightScreen, "");
                            bmp_sticker = resizeImage(bmp, h);
                        } else {
                            int h = (int) (heightScreen * 0.027);//it set the height of image 10% of your screen
                            //   iv.getLayoutParams().width = (int) (widthScreen * 0.15);
                            bmp_sticker = resizeImage(bmp, h);
                            Log.e("wid " + widthScreen + "  " + heightScreen, "");
                        }

                        return bmp_sticker;
                    }
                })*/.build();

        ImageView iv_profile = (ImageView) findViewById(R.id.imageViewNavDefaultImage);
        imageLoader.displayImage(Urls.baseUrl + "/" + SharedPreferenceManager.getUserObject(getApplicationContext()).get_default_image(),
                iv_profile, options,
                new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        //  holder.progressBar.setVisibility(View.VISIBLE);
                        // holder.RLprogress1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        // holder.RLprogress1.setVisibility(View.GONE);
                        //   holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri,
                                                  View view, Bitmap loadedImage) {
                        // holder.progressBar.setVisibility(View.GONE);
                        // holder.RLprogress1.setVisibility(View.GONE);
                        //   holder.progressBar.setVisibility(View.GONE);
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri,
                                                 View view, int current, int total) {
                        // holder.RLprogress1.setProgress(Math.round(100.0f
                        // * current / total));
                    }
                });



        if (member.get_member_status() == 3 || member.get_member_status() == 4) {
            //  show request update
            btExpandRequestUpdateLayout.setVisibility(View.VISIBLE);

        } else {

            btExpandRequestUpdateLayout.setVisibility(View.GONE);


        }


    }

    private void setListeners() {

        btExpandRequestUpdateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (visible) {
                    llRequestProfileUpdaye.setVisibility(View.GONE);
                    visible = false;

                } else {
                    llRequestProfileUpdaye.setVisibility(View.VISIBLE);
                    visible = true;
                }
            }
        });
        btRequestProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRequestProfileUpdate newFragment = dialogRequestProfileUpdate.newInstance("Request Contact Details");
                newFragment.show(getSupportFragmentManager(), "dialog");

            }
        });

        LinearLayoutDrawerMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProfile();
                //      Toast.makeText(BaseRegistrationActivity.this, "Clicked", Toast.LENGTH_SHORT).show();

            }
        });


        geographicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterGeographicActivity.class);
                startActivity(intent);
                finish();
                // cls=
            }
        });


        appearanceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Intent intent = new Intent(getApplicationContext(), RegisterAppearanceActivity.class);
                startActivity(intent);
                finish();*/

                Class cls = RegisterAppearanceActivity.class;
                marryMax.getProfileProgress(cls, BaseRegistrationActivity.this, getApplicationContext(), member);
            }
        });


        lifestyleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class cls = RegisterLifeStyleActivity1.class;
                marryMax.getProfileProgress(cls, BaseRegistrationActivity.this, getApplicationContext(), member);

            }
        });


        interestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class cls = RegisterInterest.class;
                marryMax.getProfileProgress(cls, BaseRegistrationActivity.this, getApplicationContext(), member);

            }
        });


        personalityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class cls = RegisterPersonalityActivity.class;
                marryMax.getProfileProgress(cls, BaseRegistrationActivity.this, getApplicationContext(), member);


            }
        });

    }

    /* private void mainListener(Class activity) {
         Intent intent = new Intent(getApplicationContext(), activity);
         startActivity(intent);
         finish();

     }

    */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main_dashboard) {
            // mViewPager.setCurrentItem(0);
            // setTitle("Dashboard");
            Intent in = new Intent(BaseRegistrationActivity.this, DashboarMainActivityWithBottomNav.class);
            startActivity(in);
            finish();
        } else if (id == R.id.nav_main_mymatches) {
       /*     Intent in = new Intent(BaseRegistrationActivity.this, DashboarMainActivityWithBottomNav.class);
            startActivity(in);
            finish();*/
            DashboardMatchesMainFragment.inittab = true;
            Intent in = new Intent(BaseRegistrationActivity.this, DashboarMainActivityWithBottomNav.class);
            in.putExtra("name", 1);
            startActivity(in);
            finish();

        /*    mViewPager.setCurrentItem(1);
            setTitle("Matches");*/
        } else if (id == R.id.nav_main_mymessages) {

            Intent in = new Intent(BaseRegistrationActivity.this, DashboarMainActivityWithBottomNav.class);
            in.putExtra("name", 2);
            startActivity(in);
            finish();


          /*  mViewPager.setCurrentItem(2);
            setTitle("Messages");*/
        } else if (id == R.id.nav_main_mylist) {
            Intent in = new Intent(BaseRegistrationActivity.this, DashboarMainActivityWithBottomNav.class);
            in.putExtra("name", 3);
            startActivity(in);
            finish();
          /*  mViewPager.setCurrentItem(3);
            setTitle("My List");*/
        } else if (id == R.id.nav_main_myaccount) {
            Intent in = new Intent(BaseRegistrationActivity.this, DashboarMainActivityWithBottomNav.class);
            in.putExtra("name", 4);
            startActivity(in);
            finish();
           /* mViewPager.setCurrentItem(4);
            setTitle("Account Settings");*/
        } else if (id == R.id.nav_main_uploadphotos) {
          /*  Intent in = new Intent(BaseRegistrationActivity.this, PhotoUpload.class);
            startActivity(in);*/

            if (marryMax.uploadPhotoCheck(getApplicationContext())) {
                Intent in = new Intent(BaseRegistrationActivity.this, PhotoUpload.class);
                startActivity(in);
            }

        } else if (id == R.id.nav_main_editprofile) {
            Class cls = RegisterGeographicActivity.class;
            marryMax.getProfileProgress(cls, BaseRegistrationActivity.this, getApplicationContext(), member);
        }


        else if (id == R.id.nav_main_profile_settings) {

            Intent in = new Intent(getApplicationContext(), MainDirectiveActivity.class);
            in.putExtra("type", 22);
            startActivity(in);

        }


        else if (id == R.id.nav_main_advsearch) {

            if (jsonArraySearch == null) {
                getData();
            } else {
                Intent intent = new Intent(BaseRegistrationActivity.this, SearchMainActivity.class);
                startActivityForResult(intent, 2);
                // overridePendingTransition(R.anim.enter, R.anim.right_to_left);
            }
        } else if (id == R.id.nav_main_logout) {
            UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
            sessionManager.logoutUser();
        } else if (id == R.id.nav_main_faq) {
            Intent in = new Intent(BaseRegistrationActivity.this, FaqActivity.class);
            startActivity(in);
        }

        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    //for getting default search data
    private void getData() {
        //  String.Max
        //  pDialog.setVisibility(View.VISIBLE);
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }


    // boolean doubleBackToExitPressedOnce = false;
    public void onBackPressed() {
        Intent in = new Intent(BaseRegistrationActivity.this, DashboarMainActivityWithBottomNav.class);
        startActivity(in);
        finish();
    }


    private void viewProfile() {
        if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {


            if (SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status() != 0) {
                if (drawer != null) {
                    drawer.closeDrawers();
                }
                if (SharedPreferenceManager.getUserObject(getApplicationContext()).get_path() != null && SharedPreferenceManager.getUserObject(getApplicationContext()).get_path() != "") {
                    Intent intent = new Intent(BaseRegistrationActivity.this, MyProfileActivity.class);

                    intent.putExtra("userpath", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());

                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Error ! ", Toast.LENGTH_SHORT).show();

                }
            } else {
                if (drawer != null) {
                    drawer.closeDrawers();
                }
                Toast.makeText(this, "Please Complete Your Profile", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
