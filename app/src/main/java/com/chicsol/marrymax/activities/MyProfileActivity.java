package com.chicsol.marrymax.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.ImageSliderPagerAdapter;
import com.chicsol.marrymax.dialogs.dialogBlock;
import com.chicsol.marrymax.dialogs.dialogContactDetails;
import com.chicsol.marrymax.dialogs.dialogDeclineInterest;
import com.chicsol.marrymax.dialogs.dialogMatchAid;
import com.chicsol.marrymax.dialogs.dialogMatchAidUnderProcess;
import com.chicsol.marrymax.dialogs.dialogReplyOnAcceptInterest;
import com.chicsol.marrymax.dialogs.dialogReportConcern;
import com.chicsol.marrymax.dialogs.dialogRequestPhone;
import com.chicsol.marrymax.dialogs.dialogShowInterest;
import com.chicsol.marrymax.dialogs.dialogWithdrawInterest;
import com.chicsol.marrymax.fragments.userprofilefragments.BasicInfoFragment;
import com.chicsol.marrymax.fragments.userprofilefragments.PicturesFragment;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.functions;
import com.chicsol.marrymax.utils.userProfileConstants;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 11/14/2016.
 */

public class MyProfileActivity extends AppCompatActivity implements PicturesFragment.onCompleteListener, dialogShowInterest.onCompleteListener, dialogDeclineInterest.onCompleteListener, dialogReplyOnAcceptInterest.onCompleteListener, dialogReportConcern.onCompleteListener, dialogBlock.onBlockCompleteListener, dialogMatchAid.onCompleteListener, dialogRequestPhone.onCompleteListener {
    public ImageLoader imageLoader;
    //slider

    private int lastSelectedPage = 0;
    Members member;
    private ImageView ivZodiacSign, ivCountrySign, ivPhoneVerified;
    private PopupMenu popupUp;
    //slider
    private ViewPager viewPagerSlider;
    private List<String> sliderImagesDataList;
    private ImageSliderPagerAdapter myCustomPagerAdapter;
    private ImageButton ibSwipeRight, ibSwipeLeft;
    private faTextView faUserDropdown, faAddToFavourites;
    // private Toolbar toolbar;
    private TabLayout tabLayout1;
    private ViewPager viewPager1;
    private mTextView tvMatchAid, tvImagesCount, tvInterest, tvAlias, tvAge, tvLocation, tvProfileFor, tvReligion, tvEducation, tvOccupation, tvMaritalStatus, tvLastLoginDate;
    private DisplayImageOptions options;
    private LayoutInflater inflater;
    private LinearLayout llshowInterest, llBottomshowInterest, llBottomSendMessage, llImagesCount, LineaLayoutUserProfileInterestMessage, LineaLayoutUserProfileTopBar, LinearLayoutUserProfilePhone;
    private JSONArray responsArray;
    private String userpath;
    private ProgressDialog pDialog;
    private ProgressBar progressBar;
    ViewPagerAdapter1 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tabLayout1 = (TabLayout) findViewById(R.id.tabs1);
        tabLayout1.setupWithViewPager(viewPager1);
        viewPager1 = (ViewPager) findViewById(R.id.viewpager1);
        viewPagerSlider = (ViewPager) findViewById(R.id.viewPagerUserProfileSlider);


        userpath = getIntent().getExtras().getString("userpath");



        /*     dialogGeoInfo newFragment = dialogGeoInfo.newInstance(response.toString());
        newFragment.show(getSupportFragmentManager(), "dialog");
*/
        initialize();
        setListenders();


        JSONObject params = new JSONObject();
        try {
            params.put("userpath", userpath);
            params.put("member_status", SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_notes_id());
            params.put("gender", SharedPreferenceManager.getUserObject(getApplicationContext()).get_gender());
            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
            // Test Images Account
        /*    params.put("userpath", "su8Gt~DnAz3r4UZAiw5DDQ==");
            params.put("member_status", "4");
            params.put("gender", "F");
            params.put("path", "O70ETBVSOFu4qO9tn^YMeA==");*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Params", params.toString() + "");
        getLifestyle(params);


    }

    private void setInterestButtonText() {
        Members sessionObj = SharedPreferenceManager.getUserObject(getApplicationContext());
        Log.e(functions.checkProfileCompleteStatus(member) + "" + member.get_member_status(), "checcccccccccccccccccccc");
        if (functions.checkProfileCompleteStatus(sessionObj)) {
       /*     if (sessionObj.get_member_status() < 3) {

                tvInterest.setText("Show Interest");
                llshowInterest.setBackgroundColor(R.color.colorUserProfileTextGreen);

            }*/

            Log.e("interested id", member.get_interested_id() + "");
            Log.e("interested receieved", member.get_interest_received() + "");
            if (member.get_interested_id() == 0) {
                tvInterest.setText("Show Interest");
                llshowInterest.setBackgroundColor(getResources().getColor(R.color.colorUserProfileTextGreen));

            } else if (member.get_interested_id() != 0) {

                if (member.get_interest_received() == 0) {
                    tvInterest.setText("Withdraw Interest");
                    llshowInterest.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                } else if (member.get_interest_received() == 1) {

                    tvInterest.setText("Reply On Interest");
                    llshowInterest.setBackgroundColor(getResources().getColor(R.color.colorDefaultGreen));


                } else if (member.get_interest_received() == 3) {
                    tvInterest.setText("Interest Accepted");
                    llshowInterest.setBackgroundColor(getResources().getColor(R.color.colorUserProfileTextGreen));
                }
            }
            /*else if (member.get_interested_id() == 0 && member.get_interest_received() == 0) {

                tvInterest.setText("Withdraw Interest");
                llshowInterest.setBackgroundColor(R.color.colorGrey);

            } else if (member.get_interested_id() == 0 && member.get_interest_received() == 3) {
                tvInterest.setText("Interest Accepted");
                llshowInterest.setBackgroundColor(R.color.colorUserProfileTextGreen);
            }*/
        } else {
            // Toast.makeText(this, "copmp profile", Toast.LENGTH_SHORT).show();
            //comp profile  when click on this
        }
    }


    private int getItem(int i) {
        return viewPagerSlider.getCurrentItem() + i;
    }

    private void loadSlider(String mainPath) {
        sliderImagesDataList = new ArrayList<>();
        sliderImagesDataList.add(mainPath);


        if (responsArray.length() == 5) {
            // llPicsNotAvailable.setVisibility(View.GONE);
            Gson gson;
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
            Type member = new TypeToken<List<Members>>() {
            }.getType();


            try {

                JSONArray objectsArray = responsArray.getJSONArray(4);


                List<Members> membersDataList = (List<Members>) gson.fromJson(objectsArray.toString(), member);
                Log.e("Length 56", membersDataList.size() + "  ");
                for (int i = 0; i < membersDataList.size(); i++) {
                    sliderImagesDataList.add(membersDataList.get(i).get_photo_path());

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        myCustomPagerAdapter = new ImageSliderPagerAdapter(MyProfileActivity.this, sliderImagesDataList, findViewById(android.R.id.content));
        viewPagerSlider.setAdapter(myCustomPagerAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initialize() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");


        progressBar = (ProgressBar) findViewById(R.id.ProgressBarUserprofile);
        progressBar.setVisibility(View.GONE);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar1);

        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LineaLayoutUserProfileInterestMessage = (LinearLayout) findViewById(R.id.LineaLayoutUserProfileInterestMessage);
        LineaLayoutUserProfileTopBar = (LinearLayout) findViewById(R.id.LineaLayoutUserProfileTopBar);
        LinearLayoutUserProfilePhone = (LinearLayout) findViewById(R.id.LinearLayoutUserProfilePhone);
        LinearLayoutUserProfilePhone.setVisibility(View.GONE);
        LineaLayoutUserProfileInterestMessage.setVisibility(View.GONE);
        LineaLayoutUserProfileTopBar.setVisibility(View.GONE);
        faUserDropdown = (faTextView) findViewById(R.id.faTextViewUserDetailDropdown);
        faAddToFavourites = (faTextView) findViewById(R.id.faTextViewAddToFavouriteMember);
        tvImagesCount = (mTextView) findViewById(R.id.TextViewImagesCount);
        // iv_profile = (ImageView) findViewById(R.id.ImageViewUPImage);

        tvAlias = (mTextView) findViewById(R.id.TextViewUPAlias);
        tvAge = (mTextView) findViewById(R.id.TextViewUPAge);
        tvLocation = (mTextView) findViewById(R.id.TextViewUPLocation);
        tvProfileFor = (mTextView) findViewById(R.id.TextViewUPProfileFor);
        tvReligion = (mTextView) findViewById(R.id.TextViewUPReligion);
        tvEducation = (mTextView) findViewById(R.id.TextViewUPEducation);
        tvOccupation = (mTextView) findViewById(R.id.TextViewUPOccupation);
        tvMaritalStatus = (mTextView) findViewById(R.id.TextViewUPMaritalStatus);
        tvLastLoginDate = (mTextView) findViewById(R.id.TextViewUPLastLoginDate);
        tvMatchAid = (mTextView) findViewById(R.id.TextViewMatchAid);
        llshowInterest = (LinearLayout) findViewById(R.id.LinearLayoutShowInterest);
        llImagesCount = (LinearLayout) findViewById(R.id.LinearLayoutImagesCount);


        tvInterest = (mTextView) findViewById(R.id.TextViewInterestId);
        llBottomSendMessage = (LinearLayout) findViewById(R.id.LinearLayoutUserProfileSendMessage);
        llBottomshowInterest = (LinearLayout) findViewById(R.id.LinearLayoutUserProfileShowInterest);

        llBottomSendMessage.setVisibility(View.GONE);
        llBottomshowInterest.setVisibility(View.GONE);
        ibSwipeRight = (ImageButton) findViewById(R.id.imageButtonUPArrowRight);
        ibSwipeLeft = (ImageButton) findViewById(R.id.imageButtonUPArrowLeft);

        ivZodiacSign = (ImageView) findViewById(R.id.ImageViewUPZodiacSign);
        ivCountrySign = (ImageView) findViewById(R.id.ImageViewUPCountrySign);
        ivPhoneVerified = (ImageView) findViewById(R.id.ImageViewUPPhoneVerified);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

        inflater = LayoutInflater.from(getApplicationContext());

        options = new DisplayImageOptions.Builder()
                //   .showImageOnLoading(resize(R.drawable.loading_sm))
                // .showImageOnLoading(resize(R.drawable.loading))
                // .showImageForEmptyUri(resize(R.drawable.oops_sm))
                // .showImageForEmptyUri(resize(R.drawable.no_image))
                //.showImageOnFail(resize(R.drawable.oops_sm))
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)

                .postProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bmp) {

                        Bitmap bmp_sticker;
                        Display display = getWindowManager().getDefaultDisplay();
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
                }).build();


        userpath = getIntent().getExtras().getString("userpath");
        popupUp = new PopupMenu(MyProfileActivity.this, faUserDropdown);

        popupUp.getMenuInflater()
                .inflate(R.menu.menu_user_profile, popupUp.getMenu());


        adapter = new ViewPagerAdapter1(getSupportFragmentManager());

    }


    private void settHeader() {
        //    tvAlias,tvDesc,tvLocation,tvProfileFor,tvReligion,tvEducation,tvOccupation,tvMaritalStatus;

      //  Log.e("alias", member.getAlias());
     //   tvAlias.setText(member.getAlias());
        tvAlias.setText(SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias());
        tvAge.setText("( " + member.get_age() + " Years )");
        String location = "";


        if (!(member.get_city_name().equals(""))) {
            location = member.get_city_name() + ", ";
        }
        if (!member.get_state_name().equals("")) {

            location = location + member.get_state_name() + ", ";
        }
        tvLocation.setText(location + member.get_country_name() + ", (" + member.get_visa_status_types() + ")");
        // Log.e("Location", member.get_city_name());
        tvProfileFor.setText(member.get_profile_owner());
        tvReligion.setText(member.getReligious_sec_type());
        tvEducation.setText(member.get_education_types());
        tvOccupation.setText(member.get_occupation_types());
        tvMaritalStatus.setText(member.get_marital_status_types());
        tvLastLoginDate.setText(member.get_last_login_date());


        if (member.get_image_count() > 0) {
            llImagesCount.setVisibility(View.VISIBLE);
            ibSwipeLeft.setVisibility(View.VISIBLE);
            ibSwipeRight.setVisibility(View.VISIBLE);
        } else {
            llImagesCount.setVisibility(View.GONE);
            ibSwipeLeft.setVisibility(View.GONE);
            ibSwipeRight.setVisibility(View.GONE);
        }

        tvImagesCount.setText(Long.toString(member.get_image_count()));

        //  Log.e("zodaic", "" + member.get_sign_name());
        //  Log.e("country flag", "" + member.get_country_flag());
        imageLoader.displayImage(Urls.baseUrl + "/images/zodiac/" + member.get_sign_name() + ".png", ivZodiacSign, options);
        imageLoader.displayImage(Urls.baseUrl + "/images/flags/" + member.get_country_flag() + ".gif", ivCountrySign, options);

        if (member.get_phone_verified() == 2) {
            if (member.get_hide_phone() == 0 || member.get_allow_phone_view() > 0) {
                ivPhoneVerified.setImageDrawable(getResources().getDrawable(R.drawable.ic_num_verified_icon_60));
                //greeen
            } else {
                ivPhoneVerified.setImageDrawable(getResources().getDrawable(R.drawable.num_not_verified_icon_60));
                //    orange
            }

        } else {
            ivPhoneVerified.setImageDrawable(getResources().getDrawable(R.drawable.no_number_icon_60));
            //default
        }

      /*imageLoader.displayImage(Urls.baseUrl + "/" + member.get_default_image(),
              ivZodiacSign, options,
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
*/


        if (member.get_removed_member() == 1) {
            MenuItem menuItem = popupUp.getMenu().findItem(R.id.menu_up_remove);

            menuItem.setTitle("Unremove");
        } else {
            MenuItem menuItem = popupUp.getMenu().findItem(R.id.menu_up_remove);
            menuItem.setTitle("Remove");

        }
       /* if (member.get_is == 1) {
            MenuItem menuItem = popupUp.getMenu().findItem(R.id.menu_up_remove);

            menuItem.setTitle("Unremove");
        } else {
            MenuItem menuItem = popupUp.getMenu().findItem(R.id.menu_up_remove);
            menuItem.setTitle("Remove");

        }*/

        Log.e("Saved Member", member.get_saved_member() + " ");

        if (member.get_saved_member() == 1) {

            faAddToFavourites.setPressed(true);
        }

        // MenuItem menuItem1 = popupUp.getMenu().findItem(R.id.menu_up_request);

        Log.e("Vall Member", member.get_hide_profile() + " " + member.get_hide_photo());

        /*if (member.get_hide_profile() == 1) {
            menuItem1.setTitle("Request Profile View");

        }

        if (member.get_hide_photo() == 1) {
            menuItem1.setTitle("Request Photo View");

        }
*/
        postSetListener();

    }


    private void postSetListener() {
        ivPhoneVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (member.get_phone_request_id() == 0) {
                    if (member.get_phone_verified() == 2) {
                        if (member.get_hide_phone() == 0 || member.get_allow_phone_view() > 0) {
                            //greeen


                            JSONObject params = new JSONObject();
                            try {
                                params.put("userpath", member.getUserpath());
                                params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            getMobileInfo(params);


                        } else {

                            /// if value available
                            //   member.req

                            //     member.phone_request_id
                            //if 0  request  else withdraw id  and call withdraw request


                            //    orange
                            JSONObject params = new JSONObject();
                            try {
                                params.put("alias", SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias());
                                params.put("type", "5");
                                params.put("userpath", member.getUserpath());
                                params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String desc = "Request <b> <font color=#216917>" + member.getAlias() + "</font></b>" + " for  Contact Details";

                            dialogRequestPhone newFragment = dialogRequestPhone.newInstance(params.toString(), "Request Contact Details", desc);
                            newFragment.show(getSupportFragmentManager(), "dialog");

                        }


                    } else {

                        //default
                        String desc = "Contact details of <b> <font color=#216917>" + member.getAlias() + "</font></b>" + " are not available to public.";
                        dialogRequestPhone newFragment = dialogRequestPhone.newInstance(null, "Notification", desc);
                        newFragment.show(getSupportFragmentManager(), "dialog");
                    }
                } else {
                    JSONObject params = new JSONObject();
                    try {
                        params.put("userpath", userpath);
                        params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                        params.put("interested_id", member.get_phone_request_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String desc = "Are you sure to withdraw your  request for <b> <font color=#216917>" + member.getAlias() + "</font></b>";

                   // withdrawInterest(params, "Withdraw Contact Details", desc);

                }
            }
        });

    }

    private void setListenders() {

        viewPager1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                lastSelectedPage = position;

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tvMatchAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchAid();
            }
        });
        faAddToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(UserProfileActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                //    Log.e("Saved Member", member.get_saved_member() + " ");
                if (member.get_saved_member() == 1) {
                    JSONObject params = new JSONObject();
                    try {
                        params.put("id", "1");
                        params.put("userpath", userpath);
                        params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    addToFavourites(params);
                } else {
                    JSONObject params = new JSONObject();
                    try {
                        params.put("id", "0");
                        params.put("userpath", userpath);
                        params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    addToFavourites(params);
                }

            }
        });

        llshowInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject params = new JSONObject();
                try {
                    params.put("userpath", userpath);
                    params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
            /*  params.put("userpath", "jX0GywjuTMhXATJ3f56FIg==");
                    params.put("path", "G2vOHlGTQOrBjneguNPQuA==");*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Members sessionObj = SharedPreferenceManager.getUserObject(getApplicationContext());
                Log.e(functions.checkProfileCompleteStatus(member) + "" + member.get_member_status(), "checcccccccccccccccccccc");
                if (functions.checkProfileCompleteStatus(sessionObj)) {
                    if (member.get_interested_id() == 0) {
                        showInterest(params, false);
                      /*  tvInterest.setText("Show Interest");
                        llshowInterest.setBackgroundColor(getResources().getColor(R.color.colorUserProfileTextGreen));*/

                    } else if (member.get_interested_id() != 0) {

                        if (member.get_interest_received() == 0) {
                           /* tvInterest.setText("Withdraw Interest");
                            llshowInterest.setBackgroundColor(getResources().getColor(R.color.colorGrey));*/
                            try {
                                params.put("interested_id", member.get_interested_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String desc = "Are you sure to withdraw your interest for  <font color=#216917>" + member.getAlias() + "</font>";
                          //  withdrawInterest(params, "Withdraw Interest", desc);

                        } else if (member.get_interest_received() == 1) {
                            replyOnInterest(v);
/*
                            tvInterest.setText("Reply On Interest");
                            llshowInterest.setBackgroundColor(getResources().getColor(R.color.colorDefaultGreen));*/


                        } else if (member.get_interest_received() == 3) {
                          /*  tvInterest.setText("Interest Accepted");
                            llshowInterest.setBackgroundColor(getResources().getColor(R.color.colorUserProfileTextGreen));*/
                        }
                    }

                } else {

                    Toast.makeText(MyProfileActivity.this, "Please Complete Your Profile", Toast.LENGTH_SHORT).show();
                    //comp profile  when click on this
                }


            }
        });
        ibSwipeRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPagerSlider.setCurrentItem(getItem(+1), true);
            }
        });
        ibSwipeLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPagerSlider.setCurrentItem(getItem(-1), true);
            }
        });

        faUserDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //  addRemoveBlockMenu=popup;
                //Inflating the Popup using xml file


                //registering popup with OnMenuItemClickListener
                popupUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                           /* case R.id.menu_up_request:

                                break;*/
                            case R.id.menu_up_block:
                                blockUser();
                                break;
                            case R.id.menu_up_remove:
                                JSONObject params = new JSONObject();
                                try {
                                    params.put("id", member.get_removed_member());
                                    params.put("userpath", userpath);
                                    params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                removeMember(params, item);
                                break;

                            case R.id.menu_up_report_concern:

                                reportConcern();
                                break;

                        }


                        return true;
                    }

                });
                popupUp.show(); //showing popup menu


            }
        });


    }


    private void setupViewPager(ViewPager viewPager, String jsonArryaResponse1) {


        Bundle args = new Bundle();
        args.putString("json", jsonArryaResponse1);
        BasicInfoFragment basicInfoFragment = new BasicInfoFragment();
        //MessageHistoryFragment messageHistoryFragment = new MessageHistoryFragment();
        PicturesFragment picturesFragment = new PicturesFragment();
        picturesFragment.jsonData = jsonArryaResponse1;
        userProfileConstants.jsonArryaResponse = jsonArryaResponse1;
        basicInfoFragment.setArguments(args);
        // messageHistoryFragment.setArguments(args);
        // picturesFragment.setArguments(args);
        // picturesFragment.jsonData = jsonArryaResponse;

        adapter.clearFragments();
        adapter.addFragment(basicInfoFragment, "Basic");
        ///adapter.addFragment(messageHistoryFragment, "Message History");

        Bundle picfrg = new Bundle();
        picfrg.putString("json", jsonArryaResponse1);
        picfrg.putBoolean("myprofilecheck", true);
        picturesFragment.setArguments(picfrg);

        adapter.addFragment(picturesFragment, "Pictures");

        //   viewPager.setAdapter(null);
        viewPager.setAdapter(adapter);
        tabLayout1.setupWithViewPager(viewPager1);
        adapter.notifyDataSetChanged();

        for (int i = tabLayout1.getTabCount() - 1; i >= 0; i--) {
            TabLayout.Tab tab = tabLayout1.getTabAt(i);
            LinearLayout relativeLayout = (LinearLayout)
                    LayoutInflater.from(this).inflate(R.layout.custom_user_tab_item, tabLayout1, false);
            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title1);
            tabTextView.setText(tab.getText());

            if (i == tabLayout1.getTabCount() - 1) {
                View view1 = (View) relativeLayout.findViewById(R.id.tab_view_separator1);
                view1.setVisibility(View.INVISIBLE);
            }
            tab.setCustomView(relativeLayout);
            tab.select();
        }
        {
            TabLayout.Tab tabs = tabLayout1.getTabAt(lastSelectedPage);
            tabs.select();
        }

        // picturesFragment.loadData(jsonArryaResponse);
    }

    private void replyOnInterest(View v) {


        PopupMenu popup = new PopupMenu(MyProfileActivity.this, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.menu_yes_no, popup.getMenu());


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_up_yes:
                        JSONObject params = new JSONObject();
                        try {
                            params.put("userpath", userpath);
                            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
            /*  params.put("userpath", "jX0GywjuTMhXATJ3f56FIg==");
                    params.put("path", "G2vOHlGTQOrBjneguNPQuA==");*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        replyOnAcceptInterest(params, true);

                        break;

                    case R.id.menu_up_no:


                        dialogDeclineInterest newFragment = dialogDeclineInterest.newInstance(member, userpath);
                        newFragment.show(getSupportFragmentManager(), "dialog");


                        break;


                }


                return true;
            }

        });
        popup.show(); //showing popup menu


    }

    private void blockUser() {

        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Urls.getBlockReasonData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());


                        try {
                            JSONArray responseJSONArray = response.getJSONArray(0);

                            dialogBlock newFragment = dialogBlock.newInstance(responseJSONArray, userpath, member);
                            newFragment.show(getSupportFragmentManager(), "dialog");


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }

                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(req);
    }


    private void reportConcern() {

        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Urls.getReportConcernData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString() + "  ==   " + Urls.getReportConcernData);


                        try {
                            JSONArray responseJSONArray = response.getJSONArray(0);

                            dialogReportConcern newFragment = dialogReportConcern.newInstance(responseJSONArray, userpath, member);
                            newFragment.show(getSupportFragmentManager(), "dialog");


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }

                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(req);
    }

    private void matchAid() {

        pDialog.show();
        Log.e("url", Urls.getAssistance + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getAssistance + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString() + "  ==   ");

                        try {
                            int res = response.getJSONArray(1).getJSONObject(0).getInt("id");
                            Log.e("ressss", "" + res + "");
                            if (res == 0) {
                                dialogMatchAid newFragment = dialogMatchAid.newInstance(response, userpath, SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status());
                                newFragment.show(getSupportFragmentManager(), "dialog");
                            } else {
                                dialogMatchAidUnderProcess newFragment = dialogMatchAidUnderProcess.newInstance(response, userpath);
                                newFragment.show(getSupportFragmentManager(), "dialog");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(req);
    }



    private void replyOnAcceptInterest(JSONObject params, final boolean replyCheck) {


        pDialog.show();
        Log.e("params", params.toString());
        Log.e("profile path", Urls.interestProvisions);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.interestProvisions, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  interest ", response + "");

                        try {
                            JSONObject responseObject = response.getJSONArray("data").getJSONArray(0).getJSONObject(0);


                            Gson gson;
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            Type type = new TypeToken<Members>() {
                            }.getType();
                            Members member2 = (Members) gson.fromJson(responseObject.toString(), type);
                            Log.e("interested id", "" + member.getAlias() + "====================");

                            dialogReplyOnAcceptInterest newFragment = dialogReplyOnAcceptInterest.newInstance(member, userpath, replyCheck, member2);
                            newFragment.show(getSupportFragmentManager(), "dialog");


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
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);
    }

    private void showInterest(JSONObject params, final boolean replyCheck) {


        pDialog.show();
        Log.e("params", params.toString());
        Log.e("profile path", Urls.interestProvisions);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.interestProvisions, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  interest ", response + "");

                        try {
                            JSONObject responseObject = response.getJSONArray("data").getJSONArray(0).getJSONObject(0);


                            Gson gson;
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            Type type = new TypeToken<Members>() {
                            }.getType();
                            Members member2 = (Members) gson.fromJson(responseObject.toString(), type);
                            Log.e("interested id", "" + member.getAlias() + "====================");

                            dialogShowInterest newFragment = dialogShowInterest.newInstance(member, userpath, replyCheck, member2);
                            newFragment.show(getSupportFragmentManager(), "dialog");


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
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);
    }

    private void removeMember(JSONObject params, final MenuItem menuItem) {


        pDialog.show();
        Log.e("params", params.toString());
        //      Log.e("profile path", Urls.interestProvisions);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.removeMember, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  ", response + "");

                        try {
                            int responseid = response.getInt("id");
                            if (responseid == 1) {

                                member.set_removed_member(responseid);
                                Toast.makeText(MyProfileActivity.this, "User has been Removed successfully ", Toast.LENGTH_SHORT).show();

                              /*  MenuItem menuItem = addRemoveBlockMenu.getMenu().findItem(R.id.menu_up_remove);
                              */
                                menuItem.setTitle("Unremove");
                            } else {
                                member.set_removed_member(responseid);
                                Toast.makeText(MyProfileActivity.this, "User has been unremoved successfully ", Toast.LENGTH_SHORT).show();
                            /*    MenuItem menuItem = addRemoveBlockMenu.getMenu().findItem(R.id.menu_up_remove);
                               */
                                menuItem.setTitle("Remove");
                            }


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
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);
    }

    private void addToFavourites(JSONObject params) {


        pDialog.show();
        Log.e("params", params.toString());
        //      Log.e("profile path", Urls.interestProvisions);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.addRemoveFavorites, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  ", response + "");

                        try {
                            int responseid = response.getInt("id");
                            if (responseid == 1) {
                                faAddToFavourites.setPressed(true);
                                Toast.makeText(MyProfileActivity.this, "Favourite Updated successfully ", Toast.LENGTH_SHORT).show();

                            } else {
                                faAddToFavourites.setPressed(false);

                            }
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
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);
    }

    private void getLifestyle(JSONObject params) {


        pDialog.show();
        Log.e("params", params.toString());
        Log.e("profile path", Urls.getProfileDetail);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.getProfileDetail, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update lifestyle", response + "");

                        try {
                            responsArray = response.getJSONArray("jdata");

                            JSONObject firstJsonObj = responsArray.getJSONArray(0).getJSONObject(0);


                            Gson gson;
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            Type type = new TypeToken<Members>() {
                            }.getType();
                            member = (Members) gson.fromJson(firstJsonObj.toString(), type);

                            setInterestButtonText();
                            settHeader();
                            loadSlider(member.get_default_image());

                            setupViewPager(viewPager1, responsArray.toString());
                            // Log.e("Member checkedTextView", "" + member.getAlias());

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
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);
    }


    @Override
    public void onComplete(String s) {


        JSONObject params = new JSONObject();
        try {
            params.put("userpath", userpath);
            params.put("member_status", SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_notes_id());
            params.put("gender", SharedPreferenceManager.getUserObject(getApplicationContext()).get_gender());
            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Params", params.toString() + "");
        getLifestyle(params);
    }


    private void getMobileInfo(JSONObject params) {


        pDialog.show();
        Log.e("params mobile", params.toString());
        Log.e("mobile U RL ", Urls.mobileInfo);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.mobileInfo, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  ", response + "");

                        try {
                            JSONObject responseObject = response.getJSONArray("data").getJSONArray(0).getJSONObject(0);


                            ///Log.e("interested id", "" + member.getAlias() + "====================");

                            dialogContactDetails newFragment = dialogContactDetails.newInstance(responseObject.toString(), member.getAlias());
                            newFragment.show(getSupportFragmentManager(), "dialog");

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
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
// Adding request to request queue
        ///   rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);
    }


    private Bitmap resizeImage(final Bitmap image, int maxHeight) {
        Bitmap resizedImage = null;
        if (image != null) {
            //  int maxHeight = 80; //actual image height coming from internet
            int maxWidth = 300; //actual image width coming from internet

            int imageHeight = image.getHeight();
            if (imageHeight > maxHeight)
                imageHeight = maxHeight;
            int imageWidth = (imageHeight * image.getWidth()) / image.getHeight();
            if (imageWidth > maxWidth) {
                imageWidth = maxWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }
            resizedImage = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);
        }
        return resizedImage;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }

    @Override
    public void onCompleteReportConcern(String s) {
        finish();
    }

    @Override
    public void onBlockComplete(String s) {
        finish();
    }

    class ViewPagerAdapter1 extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter1(FragmentManager manager) {
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


        public void clearFragments() {
            mFragmentList.clear();
            mFragmentTitleList.clear();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
