package com.chicsol.marrymax.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.NotificationSpinnerAdapter;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mProperties;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.other.UserSessionManager;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.mTextView;
import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Android on 12/7/2016.
 */

public class
DrawerActivity extends AppCompatActivity {

    public LinearLayout LinearLayoutDrawerMyProfile;
    public static Members rawSearchObj = null;
    /*   @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onCreateDrawer();
    }*/
    public ImageLoader imageLoader;
    DrawerLayout drawer;
    int count = 0;
    private NotificationSpinnerAdapter m_adapter;
    private ArrayList<mProperties> m_NotificationDataList = new ArrayList<mProperties>();
    private mTextView tcUserName, tvSuccessStories, tvWhyMarryMax,tvAboutMarryMax;
    private Members member;
    private ImageView iv_profile;
    private DisplayImageOptions options;

    // private ProgressDialog pDialog;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        onCreateDrawer();
    }

    private void initialize() {


        {
            MarryMax marryMax = new MarryMax(DrawerActivity.this);
            marryMax.getAppVersion(getApplicationContext());

        }

        LinearLayoutDrawerMyProfile = (LinearLayout) findViewById(R.id.LinearLayoutDrawerMyProfile);


        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));


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


        tcUserName = (mTextView) findViewById(R.id.TextViewNavUserName);
        tvSuccessStories = (mTextView) findViewById(R.id.TextViewNavHeaderSuccessStories);
        tvSuccessStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarryMax marryMax = new MarryMax(DrawerActivity.this);
                marryMax.successstories();
            }
        });


        tvWhyMarryMax = (mTextView) findViewById(R.id.TextViewNavHeaderWhyMarryMax);
        tvWhyMarryMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarryMax marryMax = new MarryMax(DrawerActivity.this);
              //  marryMax.();
            }
        });





        tvAboutMarryMax = (mTextView) findViewById(R.id.TextViewNavHeaderAboutMarryMax);
        tvAboutMarryMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarryMax marryMax = new MarryMax(DrawerActivity.this);
                marryMax.aboutus();
            }
        });






        member = SharedPreferenceManager.getUserObject(getApplicationContext());


        m_adapter = new NotificationSpinnerAdapter(this, R.layout.item_spinner_notifications, m_NotificationDataList);
        tcUserName.setText(member.getPersonal_name());


        iv_profile = (ImageView) findViewById(R.id.imageViewNavDefaultImage);
        imageLoader.displayImage(Urls.baseUrl + "/" + SharedPreferenceManager.getUserObject(getApplicationContext()).getDefault_image(),
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
        getSearchListData();
        // getRawData();


        LinearLayoutDrawerMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProfile();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotificationCount();
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


    public void onDashboardClick(View view) {

        Intent in = new Intent(DrawerActivity.this, DashboarMainActivityWithBottomNav.class);
        startActivity(in);
        finish();

    }


    public void onEditProfile(View view) {
        drawer.closeDrawers();


    }


    private void viewProfile() {

    /*    if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
         if (SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() != 0) {
                if (SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() != 0 || SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() != 7) {

                    if (drawer != null) {
                        drawer.closeDrawers();
                    }
                    if (SharedPreferenceManager.getUserObject(getApplicationContext()).getPath() != null && SharedPreferenceManager.getUserObject(getApplicationContext()).getPath() != "") {
                        Intent intent = new Intent(DrawerActivity.this, MyProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("userpath", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error ! ", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(this, "Please Complete your profile first", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please Complete your profile first", Toast.LENGTH_SHORT).show();
            }*/


        if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
            if (drawer != null) {
                drawer.closeDrawers();
            }


         /*
            boolean acheck = marryMax.checkUserStatusLogin(member);
            if (acheck) {
                Intent intent = new Intent(DrawerActivity.this, MyProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userpath", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

                startActivity(intent);
            }*/


            if (member.getMember_status() == 0 || member.getMember_status() >= 7) {


                MarryMax marryMax = new MarryMax(DrawerActivity.this);
                marryMax.getProfileProgress(getApplicationContext(), member, DrawerActivity.this);
            } else {
                Intent intent = new Intent(DrawerActivity.this, MyProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userpath", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

                startActivity(intent);
            }


        }
    }


    public void onAccountClick(View view) {
        drawer.closeDrawers();
        //   Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        Intent in = new Intent(DrawerActivity.this, AccountSettingActivity.class);
        startActivity(in);
    }

    public void onFAQClick(View view) {
        drawer.closeDrawers();
        Intent in = new Intent(DrawerActivity.this, FaqActivity.class);
        startActivity(in);
        // onLogoutClick(view);
    }

    public void onLogoutClick(View view) {
        //   Toast.makeText(this, "Clicked ", Toast.LENGTH_SHORT).show();

        UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        sessionManager.logoutUser();
    }

    public void onUploadPhotoClick(View view) {
        drawer.closeDrawers();
        //   Toast.makeText(this, "Clicked ", Toast.LENGTH_SHORT).show();
        MarryMax marryMax = new MarryMax(DrawerActivity.this);

        if (marryMax.uploadPhotoCheck(getApplicationContext())) {
            Intent in = new Intent(DrawerActivity.this, PhotoUpload.class);
            startActivity(in);
        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //========adv search=====================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_notifications);
        menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_bell));


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_notifications) {

            if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
                Intent intent = new Intent(DrawerActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
            return true;
        } else if (id == R.id.action_search) {

            if (ConnectCheck.isConnected(findViewById(android.R.id.content))) {
                DrawerActivity.rawSearchObj = new Members();
                MarryMax max = new MarryMax(DrawerActivity.this);
                max.onSearchClicked(getApplicationContext(), 0);
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    private void settNotificationCount(String c) {
        try {
            count = Integer.parseInt(c);

            ShortcutBadger.applyCount(this, count); //for 1.1.4+
            invalidateOptionsMenu();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            Crashlytics.logException(e);
        }
    }
    //......................................


    public void getNotificationCount() {


        //Log.e(" Notification url", Urls.getNotifyCntSta + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
        StringRequest req = new StringRequest(Urls.getNotifyCntSta + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("Notification Count==", "=======================  " + response + " ===== " + "");
                        if (!response.equals(null) && !response.equals("")) {
                            response = response.replace("\"", "");


                            String[] data = response.split(",");
                            String count = data[0];
                            String status = data[1];
                            String remoteVersionName = "1.0";
                            //   String version = data[2];

                            //    MarryMax max = new MarryMax(DrawerActivity.this);

                            //     max.checkVersionUpdate(remoteVersionName);


                            //    Toast.makeText(DrawerActivity.this, "version :" + version, Toast.LENGTH_SHORT).show();

                            settNotificationCount(count);

                            Members member1 = SharedPreferenceManager.getUserObject(getApplicationContext());
                            if (member1.getMember_status() != Long.parseLong(status)) {
                                member1.setMember_status(Long.parseLong(status));
                                SharedPreferenceManager.setUserObject(getApplicationContext(), member1);
                                member = member1;
                            }

                            if (status.equals("5") || status.equals("6") /*|| status.equals("10")*/) {
                                MySingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
                                    @Override
                                    public boolean apply(Request<?> request) {
                                        return true;
                                    }
                                });

                                UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
                                sessionManager.logoutUser();


                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "getNotificationCount: " + error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }


//==================================================SEARCH===========================================================


    private void getSearchListData() {

        //Log.e("Search Lists url", Urls.getSearchListsAdv + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getSearchListsAdv + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Constants.jsonArraySearch = response;
                        //   pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                //  pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
      /*  if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();


    }


}
