package com.chicsol.marrymax.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.chicsol.marrymax.dialogs.dialogAddNotes;
import com.chicsol.marrymax.dialogs.dialogAddtoList;
import com.chicsol.marrymax.dialogs.dialogBlock;
import com.chicsol.marrymax.dialogs.dialogDeclineInterest;
import com.chicsol.marrymax.dialogs.dialogMatchAid;
import com.chicsol.marrymax.dialogs.dialogMatchAidUnderProcess;
import com.chicsol.marrymax.dialogs.dialogProfileCompletion;
import com.chicsol.marrymax.dialogs.dialogReplyOnAcceptInterest;
import com.chicsol.marrymax.dialogs.dialogReportConcern;
import com.chicsol.marrymax.dialogs.dialogRequestPhone;
import com.chicsol.marrymax.dialogs.dialogShowInterest;
import com.chicsol.marrymax.fragments.userprofilefragments.BasicInfoFragment;
import com.chicsol.marrymax.fragments.userprofilefragments.PicturesFragment;
import com.chicsol.marrymax.interfaces.PhoneRequestCallBackInterface;
import com.chicsol.marrymax.interfaces.RequestCallbackInterface;
import com.chicsol.marrymax.interfaces.UpdateMemberFromDialogFragment;
import com.chicsol.marrymax.interfaces.WithdrawRequestCallBackInterface;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.other.MarryMax;
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

public class UserProfileActivityFragment extends Fragment implements PicturesFragment.onCompleteListener, dialogShowInterest.onCompleteListener, dialogDeclineInterest.onCompleteListener, dialogReplyOnAcceptInterest.onCompleteListener, dialogReportConcern.onCompleteListener, dialogBlock.onBlockCompleteListener, dialogMatchAid.onCompleteListener, dialogRequestPhone.onCompleteListener, dialogProfileCompletion.onCompleteListener, UpdateMemberFromDialogFragment, WithdrawRequestCallBackInterface, PhoneRequestCallBackInterface, RequestCallbackInterface {
    public ImageLoader imageLoader;
    String Tag = "UserProfileActivityFragment";
    //slider
    MarryMax marryMax;
    private int lastSelectedPage = 0;
    Members member;
    private ImageView ivZodiacSign, ivCountrySign, ivPhoneVerified;
    private PopupMenu popupUp;
    //slider
    private ViewPager viewPagerSlider;
    private List<String> sliderImagesDataList;
    private ImageSliderPagerAdapter myCustomPagerAdapter;
    private ImageButton ibSwipeRight, ibSwipeLeft;
    private faTextView faUserDropdown, faAddToFavourites, faInterestIcon;
    // private Toolbar toolbar;
    private TabLayout tabLayout1;
    private ViewPager viewPager1;
    private mTextView tvShowInterestButtonText, tvMatchAid, tvImagesCount, tvInterest, tvAlias, tvAge, tvLocation, tvProfileFor, tvReligion, tvEducation, tvOccupation, tvMaritalStatus, tvLastLoginDate;
    private DisplayImageOptions options;
    private LayoutInflater inflater;
    private LinearLayout llScreenMain, llScreenWait, llshowInterest, llBottomshowInterest, llBottomSendMessage, llUPSendMessage, llImagesCount, LineaLayoutUserProfileInterestMessage, LineaLayoutUserProfileTopBar;
    private JSONArray responsArray;
    private String userpath;
    private ProgressDialog pDialog;
    //
    ViewPagerAdapter1 adapter;
    ProgressBar progressBar;
    Toolbar toolbar;
    private Context context;
    private View rview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_user_profile_toolbar, menu);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_up_share) {
            //  Toast.makeText(getContext(), "clcccedd", Toast.LENGTH_SHORT).show();
            forwardMemberRequest();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_profile, container, false);


        userpath = getArguments().getString("userpath");



        /*     dialogGeoInfo newFragment = dialogGeoInfo.newInstance(response.toString());
        newFragment.show(getFragmentManager(), "dialog");
*/
        rview = rootView;
        initialize(rootView);
        setListenders();


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        JSONObject params = new JSONObject();
        try {
            params.put("userpath", userpath);
            params.put("member_status", SharedPreferenceManager.getUserObject(getContext()).get_member_status());
            params.put("gender", SharedPreferenceManager.getUserObject(getContext()).get_gender());
            params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
            // Test Images Account
        /*    params.put("userpath", "su8Gt~DnAz3r4UZAiw5DDQ==");
            params.put("member_status", "4");
            params.put("gender", "F");
            params.put("path", "O70ETBVSOFu4qO9tn^YMeA==");*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Params", params.toString() + "");
        getProfileDetail(params);


    }

    private void setInterestButtonText() {
        Members sessionObj = SharedPreferenceManager.getUserObject(context);
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
                tvShowInterestButtonText.setText("Show Interest");

            } else if (member.get_interested_id() != 0) {

                if (member.get_interest_received() == 0) {
                    tvInterest.setText("Withdraw Interest");
                    tvShowInterestButtonText.setText("Withdraw Interest");
                    llshowInterest.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                } else if (member.get_interest_received() == 1) {

                    tvInterest.setText("Reply On Interest");
                    tvShowInterestButtonText.setText("Reply On Interest");
                    llshowInterest.setBackgroundColor(getResources().getColor(R.color.colorDefaultGreen));


                } else if (member.get_interest_received() == 3) {
                    tvInterest.setText("Interest Accepted");
                    tvShowInterestButtonText.setText("Interest Accepted");
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
            //Toast.makeText(getActivity(), "copmp profile", Toast.LENGTH_SHORT).show();
            //comp profile  when click on getActivity()
        }
    }


    private int getItem(int i) {
        return viewPagerSlider.getCurrentItem() + i;
    }

    private void loadSlider(String mainPath) {


        if (member.get_image_count() > 0) {
            llImagesCount.setVisibility(View.VISIBLE);

        } else {
            llImagesCount.setVisibility(View.GONE);

        }


        sliderImagesDataList = new ArrayList<>();
        // sliderImagesDataList.add(mainPath);


        if (responsArray.length() == 5) {
            // llPicsNotAvailable.setVisibility(View.GONE);
            Gson gson;
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
            Type membera = new TypeToken<List<Members>>() {
            }.getType();


            try {

                JSONArray objectsArray = responsArray.getJSONArray(4);


                List<Members> membersDataList = (List<Members>) gson.fromJson(objectsArray.toString(), membera);


                if (membersDataList.size() == 0) {
                    sliderImagesDataList.add(mainPath);

                    ibSwipeLeft.setVisibility(View.GONE);
                    ibSwipeRight.setVisibility(View.GONE);


                } else {
                    if (membersDataList.size() == 1) {
                        ibSwipeLeft.setVisibility(View.GONE);
                        ibSwipeRight.setVisibility(View.GONE);
                    } else {
                        ibSwipeLeft.setVisibility(View.VISIBLE);
                        ibSwipeRight.setVisibility(View.VISIBLE);
                    }


                    Log.e("photozzzzzz count", membersDataList.size() + "  ");
                    for (int i = 0; i < membersDataList.size(); i++) {
                        sliderImagesDataList.add(membersDataList.get(i).get_photo_path());
                        Log.e("photozzzzzz " + i, membersDataList.get(i).get_photo_path());

                    }
                }


                myCustomPagerAdapter = new ImageSliderPagerAdapter(getActivity(), sliderImagesDataList, rview);
                viewPagerSlider.setAdapter(myCustomPagerAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

/*    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/

    private void initialize(View view) {


        //      llScreenWait = (LinearLayout) view.findViewById(R.id.LinearLayoutscreen_wait);
        //  llScreenMain = (LinearLayout) view.findViewById(R.id.LinearLayoutUserProfileMainlayout);
//
        tabLayout1 = (TabLayout) view.findViewById(R.id.tabs1);

        viewPager1 = (ViewPager) view.findViewById(R.id.viewpager1);
        tabLayout1.setupWithViewPager(viewPager1);

        viewPagerSlider = (ViewPager) view.findViewById(R.id.viewPagerUserProfileSlider);


        marryMax = new MarryMax(getActivity());


        marryMax.setWithdrawRequestCallBackInterface(UserProfileActivityFragment.this);
        marryMax.setPhoneViewRequestInterface(UserProfileActivityFragment.this);
        marryMax.setRequestCallBackInterface(UserProfileActivityFragment.this);
        //   marryMax.setPhonRe(UserProfileActivityFragment.this);
        //    marryMax.setWithdrawRequestCallBackInterface(UserProfileActivityFragment.this);


        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        progressBar = (ProgressBar) view.findViewById(R.id.ProgressBarUserprofile);


        toolbar = (Toolbar) view.findViewById(R.id.toolbar1);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //  ((AppCompatActivity)getActivity()). getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();

            }
        });

        toolbar.inflateMenu(R.menu.menu_user_profile_toolbar);

        // toolbar.setDisplayHomeAsUpEnabled
        faUserDropdown = (faTextView) view.findViewById(R.id.faTextViewUserDetailDropdown);
        faAddToFavourites = (faTextView) view.findViewById(R.id.faTextViewAddToFavouriteMember);
        faInterestIcon = (faTextView) view.findViewById(R.id.faTextViewUserProfileInterestIcon);

        tvImagesCount = (mTextView) view.findViewById(R.id.TextViewImagesCount);
        // iv_profile = (ImageView) view.findViewById(R.id.ImageViewUPImage);

        tvAlias = (mTextView) view.findViewById(R.id.TextViewUPAlias);
        tvAge = (mTextView) view.findViewById(R.id.TextViewUPAge);
        tvLocation = (mTextView) view.findViewById(R.id.TextViewUPLocation);
        tvProfileFor = (mTextView) view.findViewById(R.id.TextViewUPProfileFor);
        tvReligion = (mTextView) view.findViewById(R.id.TextViewUPReligion);
        tvEducation = (mTextView) view.findViewById(R.id.TextViewUPEducation);
        tvOccupation = (mTextView) view.findViewById(R.id.TextViewUPOccupation);
        tvMaritalStatus = (mTextView) view.findViewById(R.id.TextViewUPMaritalStatus);
        tvLastLoginDate = (mTextView) view.findViewById(R.id.TextViewUPLastLoginDate);
        tvMatchAid = (mTextView) view.findViewById(R.id.TextViewMatchAid);
        llshowInterest = (LinearLayout) view.findViewById(R.id.LinearLayoutShowInterest);

        tvShowInterestButtonText = (mTextView) view.findViewById(R.id.mTextViewLinearLayoutUserProfileShowInterestText);


        llBottomSendMessage = (LinearLayout) view.findViewById(R.id.LinearLayoutUserProfileSendMessage);
        llBottomshowInterest = (LinearLayout) view.findViewById(R.id.LinearLayoutUserProfileShowInterest);

        llUPSendMessage = (LinearLayout) view.findViewById(R.id.LinearLayoutUPSendMessage);
        llImagesCount = (LinearLayout) view.findViewById(R.id.LinearLayoutImagesCount);

        tvInterest = (mTextView) view.findViewById(R.id.TextViewInterestId);

        ibSwipeRight = (ImageButton) view.findViewById(R.id.imageButtonUPArrowRight);
        ibSwipeLeft = (ImageButton) view.findViewById(R.id.imageButtonUPArrowLeft);

        ivZodiacSign = (ImageView) view.findViewById(R.id.ImageViewUPZodiacSign);
        ivCountrySign = (ImageView) view.findViewById(R.id.ImageViewUPCountrySign);
        ivPhoneVerified = (ImageView) view.findViewById(R.id.ImageViewUPPhoneVerified);


        LineaLayoutUserProfileInterestMessage = (LinearLayout) view.findViewById(R.id.LineaLayoutUserProfileInterestMessage);
        LineaLayoutUserProfileTopBar = (LinearLayout) view.findViewById(R.id.LineaLayoutUserProfileTopBar);


        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));

        inflater = LayoutInflater.from(getContext());

        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)

                .postProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bmp) {

                        Bitmap bmp_sticker;
                        Display display = getActivity().getWindowManager().getDefaultDisplay();
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


        popupUp = new PopupMenu(getActivity(), faUserDropdown);

        popupUp.getMenuInflater()
                .inflate(R.menu.menu_user_profile, popupUp.getMenu());


        adapter = new ViewPagerAdapter1(getChildFragmentManager());


    }


    private void settHeader() {
        //    tvAlias,tvDesc,tvLocation,tvProfileFor,tvReligion,tvEducation,tvOccupation,tvMaritalStatus;
        tvAlias.setText(member.getAlias());
        tvAge.setText("( " + member.get_age() + " Years )");
        String location = "";
        if (member.get_city_name() != null) {
            if (!(member.get_city_name().equals(""))) {
                location = member.get_city_name() + ", ";
            }
        }

        if (member.get_state_name() != null) {
            if (!member.get_state_name().equals("")) {

                location = location + member.get_state_name() + ", ";
            }
        }
        tvLocation.setText(location + member.get_country_name() + ", (" + member.get_visa_status_types() + ")");
        // Log.e("Location", member.get_city_name());
        tvProfileFor.setText(member.get_profile_owner());
        tvReligion.setText(member.getReligious_sec_type());
        tvEducation.setText(member.get_education_types());
        tvOccupation.setText(member.get_occupation_types());
        tvMaritalStatus.setText(member.get_marital_status_types());
        tvLastLoginDate.setText(member.get_last_login_date());


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

        //MenuItem menuItem1 = popupUp.getMenu().findItem(R.id.menu_up_request);

      /*  Log.e("Vall Member", member.get_hide_profile() + " " + member.get_hide_photo());

        if (member.get_hide_profile() == 1) {
            menuItem1.setTitle("Request Profile View");

        }

        if (member.get_hide_photo() == 1) {
            menuItem1.setTitle("Request Photo View");

        }*/

        postSetListener();

    }


    private void postSetListener() {
        ivPhoneVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                marryMax.statusBaseChecks(member, context, 5, getFragmentManager(), UserProfileActivityFragment.this, v, null, null);

            }
        });

    }

    private void setListenders() {

        llBottomSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });
        llBottomshowInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterest(v);
            }
        });
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

                boolean bcheck3 = marryMax.statusBaseChecks(member, context, 7, getFragmentManager(), UserProfileActivityFragment.this, v, null, null);
                if (bcheck3) {
                    matchAid();
                }

            }
        });
        faAddToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(UserProfileActivity.getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //    Log.e("Saved Member", member.get_saved_member() + " ");

                boolean bcheck3 = marryMax.statusBaseChecks(member, context, 7, getFragmentManager(), UserProfileActivityFragment.this, v, null, null);
                if (bcheck3) {
                    if (member.get_saved_member() == 1) {
                        JSONObject params = new JSONObject();
                        try {
                            params.put("id", "1");
                            params.put("userpath", userpath);
                            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addToFavourites(params);
                    } else {
                        JSONObject params = new JSONObject();
                        try {
                            params.put("id", "0");
                            params.put("userpath", userpath);
                            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addToFavourites(params);
                    }
                }


            }
        });
        llUPSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage(v);


            }
        });


        llshowInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showInterest(v);


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
            public void onClick(final View v) {


                //  addRemoveBlockMenu=popup;
                //Inflating the Popup using xml file


                //registering popup with OnMenuItemClickListener
                popupUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_up_ask_questions:
                                //  Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                                boolean qcheck = marryMax.statusBaseChecks(member, context, 7, getFragmentManager(), UserProfileActivityFragment.this, v, null, null);
                                if (qcheck) {
                                    Intent intent = new Intent(getActivity(), QuestionsActivity.class);
                                    intent.putExtra("userpath", userpath);
                                    startActivity(intent);
                                }
                                break;


                            case R.id.menu_up_request_permissions:


                                boolean percheck = marryMax.statusBaseChecks(member, context, 7, getFragmentManager(), UserProfileActivityFragment.this, v, null, null);
                                if (percheck) {
                                    Gson gson = new Gson();
                                    Intent in = new Intent(getActivity(), RequestPermissionsActivity.class);

                                    in.putExtra("member", gson.toJson(member));
                                    startActivity(in);
                                }


                                break;


                            case R.id.menu_up_block:

                                boolean bcheck = marryMax.statusBaseChecks(member, context, 7, getFragmentManager(), UserProfileActivityFragment.this, v, null, null);
                                if (bcheck) {
                                    blockUser();
                                }
                                break;
                            case R.id.menu_up_remove:


                                boolean checkStatus = marryMax.statusBaseChecks(member, context, 3, getFragmentManager(), UserProfileActivityFragment.this, v, null, null);

                                if (checkStatus) {
                                    JSONObject params = new JSONObject();
                                    try {
                                        params.put("id", member.get_removed_member());
                                        params.put("userpath", userpath);
                                        params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    removeMember(params, item);
                                }

                                break;

                            case R.id.menu_up_report_concern:
                                boolean bcheck3 = marryMax.statusBaseChecks(member, context, 7, getFragmentManager(), UserProfileActivityFragment.this, v, null, null);
                                if (bcheck3) {
                                    reportConcern();
                                }

                                break;
                            case R.id.menu_up_add_notes:
                                // dialogAddNotes.newInstance(member, userpath);

                                boolean bcheck4 = marryMax.statusBaseChecks(member, context, 8, getFragmentManager(), UserProfileActivityFragment.this, v, null, null);
                                if (bcheck4) {
                                    dialogAddNotes newFragment = dialogAddNotes.newInstance(userpath);
                                    newFragment.setTargetFragment(UserProfileActivityFragment.this, 0);
                                    newFragment.show(getFragmentManager(), "dialog");
                                }
                                break;

                            case R.id.menu_up_add_to_list:
                                // dialogAddNotes.newInstance(member, userpath);
                                boolean bcheck5 = marryMax.statusBaseChecks(member, context, 8, getFragmentManager(), UserProfileActivityFragment.this, v, null, null);
                                if (bcheck5) {
                                    dialogAddtoList newFragment1 = dialogAddtoList.newInstance(userpath);
                                    newFragment1.setTargetFragment(UserProfileActivityFragment.this, 0);
                                    newFragment1.show(getFragmentManager(), "dialog");
                                    break;
                                }

                        }


                        return true;
                    }

                });
                popupUp.show(); //showing popup menu


            }
        });


    }

    private void sendMessage(View v) {

        marryMax.statusBaseChecks(member, context, 6, getFragmentManager(), UserProfileActivityFragment.this, v, null, null);

    }


    private void showInterest(View v) {

        JSONObject params = new JSONObject();
        try {
            params.put("userpath", userpath);
            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
            /*  params.put("userpath", "jX0GywjuTMhXATJ3f56FIg==");
                    params.put("path", "G2vOHlGTQOrBjneguNPQuA==");*/
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Members sessionObj = SharedPreferenceManager.getUserObject(context);
        // Log.e(functions.checkProfileCompleteStatus(member) + "" + member.get_member_status(), "checcccccccccccccccccccc");


        boolean checkStatus = marryMax.statusBaseChecks(member, context, 2, getFragmentManager(), UserProfileActivityFragment.this, v, null, null);

        if (checkStatus) {
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
                        marryMax.withdrawInterest(params, "Withdraw Interest", desc, null, getFragmentManager(), "5");

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

                Toast.makeText(getActivity(), "Please Complete Your Profile", Toast.LENGTH_SHORT).show();
                //comp profile  when click on getActivity()
            }

        }
    }


    private void setupViewPager(ViewPager viewPager, String jsonArryaResponse1) {
        Log.e("setup viewpager", "setup viewpager" + jsonArryaResponse1);

        Bundle args = new Bundle();
        args.putString("json", jsonArryaResponse1);
        BasicInfoFragment basicInfoFragment = new BasicInfoFragment();
        //   basicInfoFragment.jsona=jsonArryaResponse1;
        basicInfoFragment.setTargetFragment(getParentFragment(), 0);
        //MessageHistoryFragment messageHistoryFragment = new MessageHistoryFragment();
        PicturesFragment picturesFragment = new PicturesFragment();
        picturesFragment.setRequestCallBackInterface(UserProfileActivityFragment.this);
        // picturesFragment.jsonData = jsonArryaResponse1;
        userProfileConstants.jsonArryaResponse = jsonArryaResponse1;

        basicInfoFragment.setArguments(args);

        // messageHistoryFragment.setArguments(args);
        // picturesFragment.setArguments(args);
        // picturesFragment.jsonData = jsonArryaResponse;

        adapter.clearFragments();
        adapter.addFragment(basicInfoFragment, "Basic");
        ///adapter.addFragment(messageHistoryFragment, "Message History");

        Bundle picfrg = new Bundle();
        picfrg.putBoolean("myprofilecheck", false);
        picfrg.putString("json", jsonArryaResponse1);
        picturesFragment.setArguments(picfrg);
        picturesFragment.setTargetFragment(getParentFragment(), 0);

        adapter.addFragment(picturesFragment, "Pictures");


        //   viewPager.setAdapter(null);
        viewPager.setAdapter(adapter);
        tabLayout1.setupWithViewPager(viewPager1);
        adapter.notifyDataSetChanged();

        for (int i = tabLayout1.getTabCount() - 1; i >= 0; i--) {
            TabLayout.Tab tab = tabLayout1.getTabAt(i);
            LinearLayout relativeLayout = (LinearLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.custom_user_tab_item, tabLayout1, false);
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


        PopupMenu popup = new PopupMenu(getActivity(), v);
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
                            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
            /*  params.put("userpath", "jX0GywjuTMhXATJ3f56FIg==");
                    params.put("path", "G2vOHlGTQOrBjneguNPQuA==");*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        replyOnAcceptInterest(params, true);

                        break;

                    case R.id.menu_up_no:


                        dialogDeclineInterest newFragment = dialogDeclineInterest.newInstance(member, userpath);
                        newFragment.setTargetFragment(UserProfileActivityFragment.this, 0);
                        newFragment.show(getFragmentManager(), "dialog");


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
                            newFragment.setTargetFragment(UserProfileActivityFragment.this, 0);
                            newFragment.show(getFragmentManager(), "dialog");


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
        MySingleton.getInstance(getActivity()).addToRequestQueue(req, Tag);
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
                            newFragment.setTargetFragment(UserProfileActivityFragment.this, 0);
                            newFragment.show(getFragmentManager(), "dialog");


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
        MySingleton.getInstance(getActivity()).addToRequestQueue(req, Tag);
    }

    private void matchAid() {

        pDialog.show();
        Log.e("url", Urls.getAssistance + SharedPreferenceManager.getUserObject(context).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getAssistance + SharedPreferenceManager.getUserObject(context).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString() + "  ==   ");

                        try {
                            int res = response.getJSONArray(1).getJSONObject(0).getInt("id");
                            Log.e("ressss", "" + res + "");
                            if (res == 0) {
                                dialogMatchAid newFragment = dialogMatchAid.newInstance(response, userpath, SharedPreferenceManager.getUserObject(context).get_member_status());
                                newFragment.setTargetFragment(UserProfileActivityFragment.this, 0);
                                newFragment.show(getFragmentManager(), "dialog");
                            } else {
                                dialogMatchAidUnderProcess newFragment = dialogMatchAidUnderProcess.newInstance(response, userpath);
                                newFragment.setTargetFragment(UserProfileActivityFragment.this, 0);
                                newFragment.show(getFragmentManager(), "dialog");
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(req, Tag);
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

                            newFragment.setTargetFragment(UserProfileActivityFragment.this, 0);

                            newFragment.show(getFragmentManager(), "dialog");


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
                // Toast.makeText(RegistrationActivity.getActivity(), "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

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
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, Tag);
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
                            newFragment.setListener(UserProfileActivityFragment.this);
                            newFragment.setTargetFragment(UserProfileActivityFragment.this, 0);
                            newFragment.show(getFragmentManager(), "dialog");


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
                // Toast.makeText(RegistrationActivity.getActivity(), "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

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
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, Tag);
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
                                Toast.makeText(getActivity(), "User has been Removed successfully ", Toast.LENGTH_SHORT).show();

                                /*  MenuItem menuItem = addRemoveBlockMenu.getMenu().findItem(R.id.menu_up_remove);
                                 */
                                menuItem.setTitle("Unremove");
                            } else {
                                member.set_removed_member(responseid);
                                Toast.makeText(getActivity(), "User has been unremoved successfully ", Toast.LENGTH_SHORT).show();
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
                // Toast.makeText(RegistrationActivity.getActivity(), "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

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
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, Tag);
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
                                Toast.makeText(getActivity(), "Favourite Updated successfully ", Toast.LENGTH_SHORT).show();

                                member.set_saved_member(responseid);

                            } else {
                                faAddToFavourites.setPressed(false);
                                member.set_saved_member(responseid);
                                Toast.makeText(getActivity(), "Favourite Updated successfully ", Toast.LENGTH_SHORT).show();

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
                // Toast.makeText(RegistrationActivity.getActivity(), "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

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
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, Tag);
    }

    private void getProfileDetail(JSONObject params) {


        //  pDialog.show();

        setWaitScreen(true);

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
                            //      ((AppCompatActivity)getActivity()).  getSupportActionBar().setTitle(member.getAlias());
                            toolbar.setTitle(member.getAlias());


                            if (SharedPreferenceManager.getUserObject(context).get_path() != null && member.get_path() != null) {
                                // Log.e("=========path ========", SharedPreferenceManager.getUserObject(getContext()).get_path() + "======" + member.getUserpath());
                                if (SharedPreferenceManager.getUserObject(context).get_path().equals((member.getUserpath()))) {

                                    LineaLayoutUserProfileInterestMessage.setVisibility(View.GONE);
                                    LineaLayoutUserProfileTopBar.setVisibility(View.GONE);
                                }
                            }


                            setInterestButtonText();

                            if (member != null) {
                                settHeader();
                            }
                            loadSlider(member.get_default_image());

                            setupViewPager(viewPager1, responsArray.toString());
                            // Log.e("Member checkedTextView", "" + member.getAlias());

                        } catch (JSONException e) {
                            setWaitScreen(false);

                            //  pDialog.dismiss();
                            e.printStackTrace();
                        }

                        setWaitScreen(false);
                        //  pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.getActivity(), "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();
                setWaitScreen(false);
                //    pDialog.dismiss();
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, Tag);
    }


    @Override
    public void onComplete(String s) {

        ReLoadProfile();

    }


    private void ReLoadProfile() {
        JSONObject params = new JSONObject();
        try {
            params.put("userpath", userpath);
            params.put("member_status", SharedPreferenceManager.getUserObject(context).get_member_notes_id());
            params.put("gender", SharedPreferenceManager.getUserObject(context).get_gender());
            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Params", params.toString() + "");
        getProfileDetail(params);
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

/*    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }*/

    @Override
    public void onComplete(int s) {

    }

    @Override
    public void onCompleteReportConcern(String s) {
        getActivity().finish();
    }

    @Override
    public void onBlockComplete(String s) {
        getActivity().finish();
    }

    @Override
    public void updateInterest(boolean update) {
        if (update) {
            ReLoadProfile();
        }
    }

    @Override
    public void onWithdrawRequestComplete(String requestid) {

        ReLoadProfile();

       /* int id = Integer.parseInt(requestid);
        switch (id) {
       *//*     case 1:

                items.get(selectedPosition).set_photo_upload_request_id(0);

                break;
            case 2:
                items.get(selectedPosition).set_photo_request_id(0);
                break;
            case 3:
                items.get(selectedPosition).set_profile_request_id(0);
                break;
            case 4:
                items.get(selectedPosition).set_phone_request_id(0);
                break;*//*
            case 5:
                member.set_interested_id(0);
                break;

        }*/

    }

    @Override
    public void onPhoneViewRequestComplete(String requestid) {
        ReLoadProfile();
    }

    @Override
    public void onRequestCallback(String type, String responseid) {
        ReLoadProfile();
    }

    class ViewPagerAdapter1 extends FragmentStatePagerAdapter {

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


    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {
      /*  llScreenMain.setVisibility(set ? View.GONE : View.VISIBLE);
        llScreenWait.setVisibility(set ? View.VISIBLE : View.GONE);
*/
        if (set) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        } else {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        progressBar.setVisibility(set ? View.VISIBLE : View.GONE);
    }

    private void forwardMemberRequest() {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject params = new JSONObject();
        try {


            params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
            params.put("userpath", member.getUserpath());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Params", Urls.forwardMember + " " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.forwardMember, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {
                            // int responseid = response.getInt("id");
                            //    userpath
                            String shareUrl = Urls.baseUrl + "/UserProfile?profileid=" + response.getString("path") + "&fwdid=" + response.getString("userpath");
                            //       http://172.99.1.63:82/UserProfile?profileid=kusDL8neFQI=&fwdid=Y5E8s90NV24=

                            shareUrl = "Click on the link below to See Marrymax.com Matrimonial Profile   \n" + shareUrl;
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            // sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Link of profile");
                            sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);


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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq, Tag);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // below line to be commented to prevent crash on nougat.
        // http://blog.sqisland.com/2016/09/transactiontoolargeexception-crashes-nougat.html
        //
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);

    }
}
