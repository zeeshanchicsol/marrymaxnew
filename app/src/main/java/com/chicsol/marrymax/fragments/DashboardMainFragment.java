package com.chicsol.marrymax.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.DashboarMainActivityWithBottomNav;
import com.chicsol.marrymax.activities.DrawerActivity;
import com.chicsol.marrymax.activities.MyProfileActivity;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.activities.subscription.SubscriptionPlanActivity;
import com.chicsol.marrymax.adapters.RecyclerViewAdapter;
import com.chicsol.marrymax.dialogs.dialogContactSupport;
import com.chicsol.marrymax.dialogs.dialogMatchingAttributeFragment;
import com.chicsol.marrymax.dialogs.dialogProfileCompletion;
import com.chicsol.marrymax.fragments.DashMain.DashMembersFragment;
import com.chicsol.marrymax.modal.Dashboards;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.modal.mDshCount;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.mTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 11/3/2016.
 */

public class DashboardMainFragment extends Fragment implements RecyclerViewAdapter.OnItemClickListener, DashboarMainActivityWithBottomNav.BottomNavSelected, dialogMatchingAttributeFragment.onMatchPreferenceCompleteListener, dialogContactSupport.onCompleteListener {

    String Tag = "DashboardMainFragment";
    public ImageLoader imageLoader;

    private DisplayImageOptions options;
    private LayoutInflater inflater;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayout llScreenWait, llMatchesNotFoundMSLW, llWhoViewedMe, llWhomIViewed, llPrefferedMatchingProfiles, llMatchesWithPhotoUpdate, llMembersLookingForMe, llMatchesLookingForMe, llNewMessages, llNewQuestions, llNewInterests, llNewRequests;
    private FrameLayout llProfileIncomplete, llVerifyPhone, llVerifyEmail, llReviewPending;

    private mTextView tv_alias;
    private ImageView iv_profile;


    private ProgressBar pDialog;
    private TextView tvWIV, tvWVM, tvPMP, tvMWPU, tvMemLFM, tvMatchesLFM, tvNewMessages, tvNewRequests, tvNewInterests, tvNewQuestions;
    private TextView tvAcceptedMembers, tvMyFavourites, tvMyNotes, tvRemoveFromSearch, tvBlocked, tvAaccMemCount, tvMyMatchesCount, tvMFavCount, tvMyNotesCount, tvRecommenedMatchesCount, tvRemovedFromSearchCount, tvBlockedCount, tvProfileCompleteion, tvFeedbackPending;
    private ImageView ivCompleleProfile, ivVerifyPhone, ivVerifyEmail, ivReviewPending, ivReviewPendingOrange;
    private CardView cardViewProfileCompletionStatus, cvPromoCode, cvFeedbackPending, cvEmailPhoneVerificationPending;
    private AppCompatButton btPhoneVerificationPending, btEmailVerificationPending;
    private AppCompatButton btSubscribe;
    private NestedScrollView NvScreenMain;
    private FrameLayout FrameLayoutDashMainContainer;
    private String adminNotes = "Under normal circumstances, a new profile is reviewed within 24 hours. MarryMax.com reviews each profile before making it live and allow you to communicate with members.";
    private Context context;


    private AppCompatButton btPhoneRecievedCount, btRequestecievedCount, btInterestRecievedCount, btPermissionsRecievedCount, btPhoneSentCount, btInterestSentCount, btRequestSentCount, btPermissionsSentCount, btGiveFeedback;

    private TextView tvCount1, tvCount2, tvCount3, tvCount4, tvPromoMessageTitle;
    private RelativeLayout rlAcceptedMem, rlMyMatches, rlMyFav, rlMyNotes, rlRemoveFromSearch, rlBlocked, rlRecommededMatches;
    private SwipeRefreshLayout swipeRefreshLayout;

    // FragmentTransaction transaction;

    ViewPagerAdapter1 adapterMemFragment;
    private ViewPager viewPagerMemFragment;
    TabLayout tabLayoutMemFragment;
    TextView tvEditProfile, tvAccStatus;
    //, tvViewProfile
    private AppCompatButton btDashboardGetOfferNow, btDashboardDismissBanner;
    private RelativeLayout rlUpgrade;
    private LinearLayout llMatchPreference;
    private ImageView ivLetsTalk;
    private Dashboards dash = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_main, container, false);
        initilize(rootView);
        setListener();

        //   getf.getClass().getSimpleName();
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        DrawerActivity.rawSearchObj = new Members();
    }

    private final void focusOnView() {
        NvScreenMain.post(new Runnable() {
            @Override
            public void run() {

                if (cvEmailPhoneVerificationPending.getVisibility() == View.VISIBLE) {
                    // Its visible
                    NvScreenMain.scrollTo(0, cvEmailPhoneVerificationPending.getTop());
                } else if (cardViewProfileCompletionStatus.getVisibility() == View.VISIBLE) {
                    // Either gone or invisible
                    NvScreenMain.scrollTo(0, cardViewProfileCompletionStatus.getTop());
                }


            }
        });
    }

    private void initilize(View view) {

  /*      pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");*/


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);


        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarDashMain1);
        pDialog.setVisibility(View.GONE);


        ivLetsTalk = (ImageView) view.findViewById(R.id.ImageViewContactSupport);


        btDashboardGetOfferNow = (AppCompatButton) view.findViewById(R.id.ButtonDashboardGetOfferNow);
        btDashboardDismissBanner = (AppCompatButton) view.findViewById(R.id.ButtonDashboardDismissBanner);


        btPhoneRecievedCount = (AppCompatButton) view.findViewById(R.id.ButtonDashboardPhoneReceivedCount);
        btRequestecievedCount = (AppCompatButton) view.findViewById(R.id.ButtonDashboardRequestReceivedCount);
        btInterestRecievedCount = (AppCompatButton) view.findViewById(R.id.ButtonDashboardInterestReceivedCount);
        btPermissionsRecievedCount = (AppCompatButton) view.findViewById(R.id.ButtonDashboardPermissionsReceivedCount);

        btPhoneSentCount = (AppCompatButton) view.findViewById(R.id.ButtonDashboardPhoneSentCount);
        btInterestSentCount = (AppCompatButton) view.findViewById(R.id.ButtonDashboardInterestSentCount);
        btRequestSentCount = (AppCompatButton) view.findViewById(R.id.ButtonDashboardRequestSentCount);
        btPermissionsSentCount = (AppCompatButton) view.findViewById(R.id.ButtonDashboardPermissionSentCount);

        btGiveFeedback = (AppCompatButton) view.findViewById(R.id.ButtonDashMainFeedbackPending);


        cardViewProfileCompletionStatus = (CardView) view.findViewById(R.id.CardViewProfileCompletionStatus);
        cvPromoCode = (CardView) view.findViewById(R.id.CardViewPromoCode);
        cvFeedbackPending = (CardView) view.findViewById(R.id.CardViewDashMainFeedbackPending);
        btEmailVerificationPending = (AppCompatButton) view.findViewById(R.id.ButtonDashMainEmailVerificationPending);
        btPhoneVerificationPending = (AppCompatButton) view.findViewById(R.id.ButtonDashMainPhoneVerificationPending);
        cvEmailPhoneVerificationPending = (CardView) view.findViewById(R.id.CardViewDashMainEmailPhoneVerificationPending);


        cvEmailPhoneVerificationPending = (CardView) view.findViewById(R.id.CardViewDashMainEmailPhoneVerificationPending);


        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayoutDashMainFragment);
        FrameLayoutDashMainContainer = (FrameLayout) view.findViewById(R.id.FrameLayoutDashMainContainer);

        llScreenWait = (LinearLayout) view.findViewById(R.id.screen_wait);
        NvScreenMain = (NestedScrollView) view.findViewById(R.id.screen_main);


        llMatchesNotFoundMSLW = (LinearLayout) view.findViewById(R.id.LinearLayoutMemberDMMatchesNotAvailable);

        llProfileIncomplete = (FrameLayout) view.findViewById(R.id.LinearLayoutDMProfileIncomplete);
        llVerifyEmail = (FrameLayout) view.findViewById(R.id.LinearLayoutDMVerifyEmail);
        llVerifyPhone = (FrameLayout) view.findViewById(R.id.LinearLayoutDMVerifyPhone);
        llReviewPending = (FrameLayout) view.findViewById(R.id.LinearLayoutDMReviewPending);
        llWhomIViewed = (LinearLayout) view.findViewById(R.id.LinearlayoutDMWhomIviewedCount);
        llWhoViewedMe = (LinearLayout) view.findViewById(R.id.LinearlayoutDMWhomViewedMeCount);
        llPrefferedMatchingProfiles = (LinearLayout) view.findViewById(R.id.LinearLayoutDMPrefferedMatchingProfilesCount);
        llMatchesWithPhotoUpdate = (LinearLayout) view.findViewById(R.id.LinearLayoutDMMatchesWithPhotoUpdateCount);
        llMembersLookingForMe = (LinearLayout) view.findViewById(R.id.LinearLayoutDMMembersLookingForMeCount);
        llMatchesLookingForMe = (LinearLayout) view.findViewById(R.id.LinearLayoutDMMatchesLookingForMeCount);
        llMatchPreference = (LinearLayout) view.findViewById(R.id.LinearLayoutDashMainMatchPreference);


        // ,,,,;
        rlAcceptedMem = (RelativeLayout) view.findViewById(R.id.RelativeLayoutDashAcceptedMembers);
        rlMyMatches = (RelativeLayout) view.findViewById(R.id.RelativeLayoutDashMyMatches);

        rlMyFav = (RelativeLayout) view.findViewById(R.id.RelativeLayoutDashMyFavourites);
        rlMyNotes = (RelativeLayout) view.findViewById(R.id.RelativeLayoutDashMyNotes);
        rlRemoveFromSearch = (RelativeLayout) view.findViewById(R.id.RelativeLayoutDashRemoveFromSearch);
        rlBlocked = (RelativeLayout) view.findViewById(R.id.RelativeLayoutDashBlocked);
        rlRecommededMatches = (RelativeLayout) view.findViewById(R.id.RelativeLayoutDashRecommendedMatch);
        rlUpgrade = (RelativeLayout) view.findViewById(R.id.RelativeLayoutDashUpgrade);


        llNewMessages = (LinearLayout) view.findViewById(R.id.LinearLayoutDMNewMessages);
        llNewInterests = (LinearLayout) view.findViewById(R.id.LinearLayoutDMNewInterest);
        llNewRequests = (LinearLayout) view.findViewById(R.id.LinearLayoutDMNewRequests);
        llNewQuestions = (LinearLayout) view.findViewById(R.id.LinearLayoutDMNewQuestions);


        ivCompleleProfile = (ImageView) view.findViewById(R.id.imageViewDMCompleteProfile);
        ivVerifyEmail = (ImageView) view.findViewById(R.id.imageViewDMVerifyEmail);
        ivVerifyPhone = (ImageView) view.findViewById(R.id.imageViewDMVerifyPhone);
        ivReviewPending = (ImageView) view.findViewById(R.id.imageViewDMReviewPending);
        ivReviewPendingOrange = (ImageView) view.findViewById(R.id.imageViewDMReviewPendingOrange);

        tvWIV = (TextView) view.findViewById(R.id.TextViewDMWhomIviewedCount);
        tvWVM = (TextView) view.findViewById(R.id.TextViewDMWhomViewedMeCount);
        tvPMP = (TextView) view.findViewById(R.id.TextViewDMPrefferedMatchingProfilesCount);
        tvMWPU = (TextView) view.findViewById(R.id.TextViewDMMatchesWithPhotoUpdateCount);
        tvMemLFM = (TextView) view.findViewById(R.id.TextViewDMMembersLookingForMeCount);
        tvMatchesLFM = (TextView) view.findViewById(R.id.TextViewDMMatchesLookingForMeCount);
        tvNewMessages = (TextView) view.findViewById(R.id.TextViewDMNewMessagesCount);
        tvNewRequests = (TextView) view.findViewById(R.id.TextViewDMNewRequestsCount);
        tvNewInterests = (TextView) view.findViewById(R.id.TextViewDMNewInteretsCount);
        tvNewQuestions = (TextView) view.findViewById(R.id.TextViewDMNewQuestionsCount);

        tvFeedbackPending = (TextView) view.findViewById(R.id.TextViewDashMainFeedbackPending);

        tvCount1 = (TextView) view.findViewById(R.id.TextViewDashboardCount1);
        tvCount2 = (TextView) view.findViewById(R.id.TextViewDashboardCount2);
        tvCount3 = (TextView) view.findViewById(R.id.TextViewDashboardCount3);
        tvCount4 = (TextView) view.findViewById(R.id.TextViewDashboardCount4);


        tvEditProfile = (TextView) view.findViewById(R.id.TextViewDashMainEditProfile);
        // tvViewProfile = (TextView) view.findViewById(R.id.TextViewDashMainAddPhoto);
        tvAccStatus = (TextView) view.findViewById(R.id.TextViewDashMainAccStatus);


        tvPromoMessageTitle = (TextView) view.findViewById(R.id.TextViewDashboardPromoMessage);


        tvAcceptedMembers = (TextView) view.findViewById(R.id.TextViewAcceptedMemCount);
        tvMyFavourites = (TextView) view.findViewById(R.id.TextViewFavourite);
        tvMyNotes = (TextView) view.findViewById(R.id.TextViewNotes);
        tvRemoveFromSearch = (TextView) view.findViewById(R.id.TextViewRemFromSearch);
        tvBlocked = (TextView) view.findViewById(R.id.TextViewBlocked);


        tvAaccMemCount = (TextView) view.findViewById(R.id.TextViewDMAcceptedMemCount);
        tvMyMatchesCount = (TextView) view.findViewById(R.id.TextViewDMMyMatchesCount);

        tvMFavCount = (TextView) view.findViewById(R.id.TextViewDMFavouriteCount);
        tvMyNotesCount = (TextView) view.findViewById(R.id.TextViewDMNotesCount);
        tvRecommenedMatchesCount = (TextView) view.findViewById(R.id.TextViewDMRecommendedMatchesCount);
        tvRemovedFromSearchCount = (TextView) view.findViewById(R.id.TextViewDMRemFromSearchCount);
        tvBlockedCount = (TextView) view.findViewById(R.id.TextViewDMBlockedCount);

        tvProfileCompleteion = (TextView) view.findViewById(R.id.TextViewProfileCompletionStatus);

        btSubscribe = (AppCompatButton) view.findViewById(R.id.ButtonSubscribe);


        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));

        inflater = LayoutInflater.from(getContext());

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


        Members member = SharedPreferenceManager.getUserObject(getActivity().getApplicationContext());


        // ll_blocked = (LinearLayout) view.findViewById(R.id.LinearLayoutdmBlocked);
        // ll_removed_from_search = (LinearLayout) view.findViewById(R.id.LinearLayoutdmRemovedFromSearch);
        tv_alias = (mTextView) view.findViewById(R.id.TextViewdmAlias);
        tv_alias.setText(member.getPersonal_name());
        iv_profile = (ImageView) view.findViewById(R.id.ImageViewDefaultImage);


        imageLoader.displayImage(Urls.baseUrl + "/" + member.getDefault_image(),
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


        tabLayoutMemFragment = (TabLayout) view.findViewById(R.id.TablayoutMembersFragment);
        adapterMemFragment = new ViewPagerAdapter1(getFragmentManager());
        viewPagerMemFragment = (ViewPager) view.findViewById(R.id.viewpagerMembersFragment);


        LoadData();




   /*     FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.FrameMainFeatureContainer, frg, "Frag_Top_tagg");

        transaction.commit();*/


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                setupViewPager();
            }
        }, 1000);
    }


    private void LoadData() {


        rlUpgrade.setVisibility(View.GONE);
        if (SharedPreferenceManager.getUserObject(context).getMember_status() == 3) {

            rlUpgrade.setVisibility(View.VISIBLE);
        }


        if (SharedPreferenceManager.getUserObject(context).getMember_status() != 4) {
            tvAccStatus.setVisibility(View.VISIBLE);
        } else {
            tvAccStatus.setVisibility(View.GONE);
        }


        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {

            //  getStatus();


            getDashboardData();

            Members member = SharedPreferenceManager.getUserObject(context);

            //Log.e("Completion Status", member.getMember_status() + "");
            if (member.getMember_status() < 3 || member.getMember_status() >= 7) {
                // new MarryMax(null).updateStatus(context);


                getProfileCompletion();


            } else {

                cardViewProfileCompletionStatus.setVisibility(View.GONE);
            }
            setupViewPager();


        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            if (isVisibleToUser) {
                //  Log.e("PREFFERED MATCHINGS" + isVisibleToUser, "created");

                if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {

                    //    getStatus();
                    getDashboardData();
                    Members member = SharedPreferenceManager.getUserObject(context);

                    //Log.e("Completion Status", member.getMember_status() + "");
                    if (member.getMember_status() < 3 || member.getMember_status() >= 7) {

                        //   new MarryMax(null).updateStatus(getContext());
                        //updateStatus();
                        getProfileCompletion();


                    } else {

                        cardViewProfileCompletionStatus.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    setupViewPager();
                }


            }
        }


    }


    //======================Listeners===============================================================


    public void setListener() {

        btEmailVerificationPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, MainDirectiveActivity.class);
                in.putExtra("type", 22);
                startActivity(in);
            }
        });

        btPhoneVerificationPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, MainDirectiveActivity.class);
                in.putExtra("type", 14);
                startActivity(in);
            }
        });

        ivLetsTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dash != null) {
                    if (Integer.parseInt(dash.getSent_count()) == 1) {
                        if (SharedPreferenceManager.getUserObject(context).getPhone_verified() == 1) {
                            // show lets talk
                            contactSupport();
                        } else if (SharedPreferenceManager.getUserObject(context).getPhone_verified() == 0) {
                            //show phone verified dialog
                            dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Verify Phone", "Please verify your Phone number using verification code we send to your Mobile number.", "My Contact Details", 3);
                            dialogP.show(getFragmentManager(), "d");
                        }


                    }
                }


            }
        });


        btGiveFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 25);
                // in.putExtra("subtype", "received");
                startActivity(in);
            }
        });

        llMatchPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMatchingAttributeFragment dialogFragment = dialogMatchingAttributeFragment.newInstance("asd");
                dialogFragment.setTargetFragment(DashboardMainFragment.this, 0);
                dialogFragment.show(getFragmentManager(), "dialog");
            }
        });


        rlUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), SubscriptionPlanActivity.class);
                startActivity(in);
            }
        });
        tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Members member = SharedPreferenceManager.getUserObject(context);
                MarryMax marryMax = new MarryMax(getActivity());
                marryMax.getProfileProgress(context, member, getActivity());

            }
        });
       /* tvViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProfile();
              *//*  MarryMax marryMax = new MarryMax(getActivity());
                if (marryMax.uploadPhotoCheck(context)) {
                    Intent in = new Intent(context, PhotoUpload.class);
                    startActivity(in);
                }*//*
            }
        });
*/

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               /* if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
                  //  getStatus();

                }*/
                LoadData();
            }
        });

        btPhoneRecievedCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 17);
                in.putExtra("subtype", "received");
                startActivity(in);
            }
        });
        btRequestecievedCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 19);
                in.putExtra("subtype", "received");
                startActivity(in);
            }
        });
        btInterestRecievedCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 18);
                in.putExtra("subtype", "received");
                startActivity(in);
            }
        });
        btPermissionsRecievedCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 20);
                in.putExtra("subtype", "received");
                startActivity(in);
            }
        });
        btPhoneSentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 17);
                in.putExtra("subtype", "sent");
                startActivity(in);
            }
        });
        btInterestSentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 18);
                in.putExtra("subtype", "sent");
                startActivity(in);
            }
        });
        btRequestSentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 19);
                in.putExtra("subtype", "sent");
                startActivity(in);
            }
        });
        btPermissionsSentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 20);
                in.putExtra("subtype", "sent");
                startActivity(in);
            }
        });

        //======================================


        tv_alias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProfile();
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProfile();
            }
        });


        btSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarryMax marryMax = new MarryMax(getActivity());

                marryMax.subscribe();
            }
        });


        tvWIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(context, " wi v", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 11);
                startActivity(in);
            }
        });
        tvWVM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 12);
                startActivity(in);
            }
        });

//================

        rlAcceptedMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 8);
                startActivity(in);
            }
        });


        rlMyMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 25);
                startActivity(in);
            }
        });


        rlMyFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 9);
                startActivity(in);
            }
        });

        rlMyNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 16);
                startActivity(in);
            }
        });
        rlRecommededMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 21);
                startActivity(in);
            }
        });

        rlRemoveFromSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 10);
                startActivity(in);
            }
        });
        rlBlocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 15);
                startActivity(in);
            }
        });

//=====================
        llNewMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 5);
                startActivity(in);
            }
        });


        llNewInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 6);
                startActivity(in);
            }
        });


        llNewRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 7);
                startActivity(in);
            }
        });


        llNewQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 27);
                startActivity(in);
            }
        });


        llPrefferedMatchingProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(), "pref matching profile", Toast.LENGTH_SHORT).show();


                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 1);
                startActivity(in);


            }
        });
        llMatchesWithPhotoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 2);
                startActivity(in);

            }
        });

        llMembersLookingForMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if (SharedPreferenceManager.getUserObject(getContext()).getMember_status() != 0) {

                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 4);
                startActivity(in);
                //   }

            }
        });
        llMatchesLookingForMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if (SharedPreferenceManager.getUserObject(getContext()).getMember_status() != 0) {

                Intent in = new Intent(getContext(), MainDirectiveActivity.class);
                in.putExtra("type", 3);
                startActivity(in);
                //  }

            }
        });


        llWhomIViewed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        llWhoViewedMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        llProfileIncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Profile Incomplete", "To communicate and interact with potential matches, Please Complete your profile.", "Complete Profile", 11);
                dialogP.show(getFragmentManager(), "d");
            }
        });

        llVerifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Verify Phone", "Please verify your Phone number using verification code we send to your Mobile number.", "My Contact Details", 3);
                dialogP.show(getFragmentManager(), "d");
            }
        });
        llVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Verify Email", "Please verify your email address by using the link we emailed you. If unable to find the email,", "Request New Email", 2);
                dialogP.show(getFragmentManager(), "d");
            }
        });
        llReviewPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Members member = SharedPreferenceManager.getUserObject(context);
                String reviewStatus = "";
                if (member.getMember_status() == 7) {
                    reviewStatus = "Review Notes";

                } else {
                    reviewStatus = "Review Pending";

                }
                dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance(reviewStatus, adminNotes, "Complete Profile", 4);
                dialogP.show(getFragmentManager(), "d");


            }
        });


       /* btResetMSLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPageMSLL = 1;
                getMembersListbyTypeMSLL();

            }
        });

        btLoadMoreMSLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lastPageMSLL != totalPagesMSLL && lastPageMSLL < totalPagesMSLL) {
                    lastPageMSLL = lastPageMSLL + 1;

                    getMembersListbyTypeByPageMSLL(lastPageMSLL);

                }

            }
        });*/


    }


    private void setSentReceivedCount(JSONArray jsonArray) {


        try {
            JSONObject jsonObj = jsonArray.getJSONArray(1).getJSONObject(0);
            Gson gson;
            GsonBuilder gsonBuildert = new GsonBuilder();
            gson = gsonBuildert.create();
            Type membert = new TypeToken<mDshCount>() {
            }.getType();
            mDshCount dash0 = (mDshCount) gson.fromJson(jsonArray.getJSONArray(1).getJSONObject(0).toString(), membert);
            btPhoneRecievedCount.setText(dash0.getReceived_count());
            btPhoneSentCount.setText(dash0.getSent_count());
            tvCount1.setText(dash0.getName());

            if (Integer.parseInt(dash0.getReceived_count()) == 0) {
                btPhoneRecievedCount.setClickable(false);
            } else {
                btPhoneRecievedCount.setClickable(true);
            }
            if (Integer.parseInt(dash0.getSent_count()) == 0) {
                btPhoneSentCount.setClickable(false);
            } else {
                btPhoneSentCount.setClickable(true);
            }


            mDshCount dash1 = (mDshCount) gson.fromJson(jsonArray.getJSONArray(1).getJSONObject(1).toString(), membert);
            btInterestRecievedCount.setText(dash1.getReceived_count());
            btInterestSentCount.setText(dash1.getSent_count());
            tvCount2.setText(dash1.getName());

            btInterestRecievedCount.setClickable(true);
            btInterestSentCount.setClickable(true);
            if (Integer.parseInt(dash1.getReceived_count()) == 0) {
                btInterestRecievedCount.setClickable(false);
            }
            if (Integer.parseInt(dash1.getSent_count()) == 0) {
                btInterestSentCount.setClickable(false);
            }


            mDshCount dash2 = (mDshCount) gson.fromJson(jsonArray.getJSONArray(1).getJSONObject(2).toString(), membert);
            btRequestecievedCount.setText(dash2.getReceived_count());
            btRequestSentCount.setText(dash2.getSent_count());
            tvCount3.setText(dash2.getName());

            btRequestecievedCount.setClickable(true);
            btRequestSentCount.setClickable(true);
            if (Integer.parseInt(dash2.getReceived_count()) == 0) {
                btRequestecievedCount.setClickable(false);
            }
            if (Integer.parseInt(dash2.getSent_count()) == 0) {
                btRequestSentCount.setClickable(false);
            }


            mDshCount dash3 = (mDshCount) gson.fromJson(jsonArray.getJSONArray(1).getJSONObject(3).toString(), membert);
            btPermissionsRecievedCount.setText(dash3.getReceived_count());
            btPermissionsSentCount.setText(dash3.getSent_count());
            tvCount4.setText(dash3.getName());
            btPermissionsRecievedCount.setClickable(true);
            btPermissionsSentCount.setClickable(true);
            if (Integer.parseInt(dash3.getReceived_count()) == 0) {
                btPermissionsRecievedCount.setClickable(false);
            }
            if (Integer.parseInt(dash3.getSent_count()) == 0) {
                btPermissionsSentCount.setClickable(false);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void contactSupport() {

        pDialog.setVisibility(View.VISIBLE);

        JsonArrayRequest req = new JsonArrayRequest(Urls.getContactData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   Log.e("getContactData", response.toString());


                        try {
                            JSONArray responseJSONArray = response.getJSONArray(0);

                            dialogContactSupport newFragment = dialogContactSupport.newInstance(responseJSONArray);
                            newFragment.setTargetFragment(DashboardMainFragment.this, 0);
                            newFragment.show(getFragmentManager(), "dialog");


                        } catch (JSONException e) {
                            pDialog.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }

                        pDialog.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.setVisibility(View.INVISIBLE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(req);
    }


/*    private void contactSupport() {


        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        Log.e("api path", "" + Urls.getContactData);

        JsonArrayRequest req = new JsonArrayRequest(Urls.getContactData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                   *//*     try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();

                            List<WebArd> MyCountryStateDataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
*//*
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
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(req);
    }*/


    //  GetDashboardData
    private void getDashboardData() {



    /*    if (btReset.getVisibility() == View.VISIBLE) {
            btReset.setVisibility(View.INVISIBLE);

        }*/

        JSONObject params = new JSONObject();
        try {
            params.put("member_status", SharedPreferenceManager.getUserObject(context).getMember_status());
            params.put("phone_verified", SharedPreferenceManager.getUserObject(context).getPhone_verified());
            params.put("path", SharedPreferenceManager.getUserObject(context).getPath());
            params.put("email_verified", SharedPreferenceManager.getUserObject(context).getEmail_verified());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  pDialog.show();
        pDialog.setVisibility(View.VISIBLE);

        //Log.e("getdash Params", "" + params);
        //Log.e("getdash List", "" + Urls.getDashboardData);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.getDashboardData, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        //Log.d("getdash  update data", response + "");

                        try {
                            Members member = SharedPreferenceManager.getUserObject(context);
                            JSONArray jsonArray = response.getJSONArray("data");

                            if (jsonArray.length() > 0) {

                                //Log.e("Length", jsonArray.getJSONArray(0).length() + "");
                                JSONObject jsonObj = jsonArray.getJSONArray(0).getJSONObject(0);

                                Gson gson;
                                GsonBuilder gsonBuildert = new GsonBuilder();
                                gson = gsonBuildert.create();
                                Type membert = new TypeToken<Dashboards>() {
                                }.getType();
                                dash = (Dashboards) gson.fromJson(jsonObj.toString(), membert);


                                MarryMax max = new MarryMax(null);
                                if (Integer.parseInt(dash.getFeedback_pending()) == 0) {
                                    cvFeedbackPending.setVisibility(View.GONE);
                                } else {

                                    String desc = max.getFeedbackText(Integer.parseInt(dash.getFeedback_pending()), context);
                                    tvFeedbackPending.setText(Html.fromHtml(desc));
                                    cvFeedbackPending.setVisibility(View.VISIBLE);
                                }


                                if (Integer.parseInt(dash.getSent_count()) == 1) {
                                    ivLetsTalk.setVisibility(View.VISIBLE);
                                } else {
                                    ivLetsTalk.setVisibility(View.GONE);
                                }


                                tvWIV.setClickable(true);
                                tvWIV.setText("Whom I Viewed (" + dash.getVisited_members_count() + ")");
                                if (Integer.parseInt(dash.getVisited_members_count()) == 0) {
                                    tvWIV.setClickable(false);
                                }
                                tvWVM.setClickable(true);
                                tvWVM.setText("Who Viewed Me (" + dash.getVisiting_members_count() + ")");
                                if (Integer.parseInt(dash.getVisiting_members_count()) == 0) {
                                    tvWVM.setClickable(false);
                                }

                                llPrefferedMatchingProfiles.setClickable(true);
                                tvPMP.setText(dash.getMatches_count());
                                if (Integer.parseInt(dash.getMatches_count()) == 0) {
                                    llPrefferedMatchingProfiles.setClickable(false);
                                }

                                llMatchesWithPhotoUpdate.setClickable(true);
                                tvMWPU.setText(dash.getWith_picture_count());
                                if (Integer.parseInt(dash.getWith_picture_count()) == 0) {
                                    llMatchesWithPhotoUpdate.setClickable(false);
                                }

                                llMembersLookingForMe.setClickable(true);
                                tvMemLFM.setText(dash.getLooking_4me());
                                if (Integer.parseInt(dash.getLooking_4me()) == 0) {
                                    llMembersLookingForMe.setClickable(false);
                                }

                                llMatchesLookingForMe.setClickable(true);
                                tvMatchesLFM.setText(dash.getLooking_eachother());
                                if (Integer.parseInt(dash.getLooking_eachother()) == 0) {
                                    llMatchesLookingForMe.setClickable(false);
                                }

                                llNewMessages.setClickable(true);
                                tvNewMessages.setText(dash.getNew_messages_count());
                                if (Integer.parseInt(dash.getNew_messages_count()) == 0) {
                                    llNewMessages.setClickable(false);
                                    tvNewMessages.setVisibility(View.GONE);
                                } else {
                                    tvNewMessages.setVisibility(View.VISIBLE);
                                }

                                llNewRequests.setClickable(true);
                                tvNewRequests.setText(dash.getNew_requests_count());
                                if (Integer.parseInt(dash.getNew_requests_count()) == 0) {
                                    llNewRequests.setClickable(false);
                                    tvNewRequests.setVisibility(View.GONE);
                                } else {
                                    tvNewRequests.setVisibility(View.VISIBLE);
                                }

                                llNewInterests.setClickable(true);
                                tvNewInterests.setText(dash.getNew_interests_count());
                                if (Integer.parseInt(dash.getNew_interests_count()) == 0) {
                                    llNewInterests.setClickable(false);
                                    tvNewInterests.setVisibility(View.GONE);
                                } else {
                                    tvNewInterests.setVisibility(View.VISIBLE);
                                }

                                llNewQuestions.setClickable(true);
                                tvNewQuestions.setText(dash.getNew_questions_count());
                                if (Integer.parseInt(dash.getNew_questions_count()) == 0) {
                                    llNewQuestions.setClickable(false);
                                    tvNewQuestions.setVisibility(View.GONE);
                                } else {
                                    tvNewQuestions.setVisibility(View.VISIBLE);
                                }


                                rlAcceptedMem.setClickable(true);
                                tvAaccMemCount.setText(dash.getAccepted_count());
                                if (Integer.parseInt(dash.getAccepted_count()) == 0) {
                                    rlAcceptedMem.setClickable(false);
                                }


                                rlMyMatches.setClickable(true);
                                tvMyMatchesCount.setText(dash.getCount());
                                if (Integer.parseInt(dash.getCount()) == 0) {
                                    rlMyMatches.setClickable(false);
                                }


                                rlMyFav.setClickable(true);
                                tvMFavCount.setText(dash.getSaved_members_count());
                                if (Integer.parseInt(dash.getSaved_members_count()) == 0) {
                                    rlMyFav.setClickable(false);
                                }

                                rlMyNotes.setClickable(true);
                                tvMyNotesCount.setText(dash.getNotes_count());
                                if (Integer.parseInt(dash.getNotes_count()) == 0) {
                                    rlMyNotes.setClickable(false);
                                }

                                rlRecommededMatches.setClickable(true);
                                tvRecommenedMatchesCount.setText(dash.getRecommendcount());
                                if (Integer.parseInt(dash.getRecommendcount()) == 0) {
                                    rlRecommededMatches.setClickable(false);
                                }

                                rlRemoveFromSearch.setClickable(true);
                                tvRemovedFromSearchCount.setText(dash.getRemoved_from_search());
                                if (Integer.parseInt(dash.getRemoved_from_search()) == 0) {
                                    rlRemoveFromSearch.setClickable(false);
                                }

                                rlBlocked.setClickable(true);
                                tvBlockedCount.setText(dash.getBlocked_members());
                                if (Integer.parseInt(dash.getBlocked_members()) == 0) {
                                    rlBlocked.setClickable(false);
                                }


                                if (!dash.getDetail().equals("")) {
                                    if (member.getMember_status() == 2 || member.getMember_status() == 3) {

                                        cvPromoCode.setVisibility(View.VISIBLE);
                                        tvPromoMessageTitle.setText(Html.fromHtml(dash.getDetail()));


                                    }
                                }


                                setSentReceivedCount(jsonArray);

                                //pDialog.dismiss();
                                pDialog.setVisibility(View.INVISIBLE);
                                swipeRefreshLayout.setRefreshing(false);
                                //  Log.e("total pages", "" + dashboards.getLooking_4me());

                                //   }
                            } else {
                                //no data
                                //pDialog.dismiss();
                                pDialog.setVisibility(View.INVISIBLE);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pDialog.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                        //    pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error.getMessage());
                pDialog.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                // pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return Constants.getHashMap();
            }
        };
// Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, Tag);
    }


    private void getProfileCompletion() {
        pDialog.setVisibility(View.VISIBLE);
        setWaitScreen(true);
        // pDialog.show();
        //Log.e("URL", Urls.getProfileCompletion + SharedPreferenceManager.getUserObject(context).getPath());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getProfileCompletion + SharedPreferenceManager.getUserObject(context).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e("Response", response.toString());
                        Members member = SharedPreferenceManager.getUserObject(context);

                        try {
                            swipeRefreshLayout.setRefreshing(false);

                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);
                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<Dashboards>() {
                            }.getType();

                            Dashboards dashboards = (Dashboards) gsonc.fromJson(jsonCountryStaeObj.getJSONObject(0).toString(), listType);


                            //Log.e("Email Complete Status", "" + dashboards.getEmail_complete_status());

                            tvProfileCompleteion.setText(dashboards.getProfile_complete_status() + "% Complete");

                            boolean pCOmpleteStatus = true;

                            if (dashboards.getProfile_complete_status().equals("100")) {

                                ivCompleleProfile.setImageResource(R.drawable.ver_step1_active);
                                ivCompleleProfile.setBackgroundResource(R.drawable.border_dash_main_profilecombox_green);


                                ivCompleleProfile.setOnClickListener(null);
                            } else {
                                pCOmpleteStatus = false;

                                ivCompleleProfile.setImageResource(R.drawable.ver_step1);
                                ivCompleleProfile.setBackgroundResource(R.drawable.border_dash_main_profilecombox);
                            }


                            Log.e("email phone", dashboards.getEmail_complete_status() + "-----" + dashboards.getPhone_complete_status());


                            if ((SharedPreferenceManager.getUserObject(context).getMember_status() < 3 && SharedPreferenceManager.getUserObject(context).getMember_status() > 0) && (dashboards.getPhone_complete_status().equals("0") || dashboards.getEmail_complete_status().equals("0"))) {
                                cvEmailPhoneVerificationPending.setVisibility(View.VISIBLE);
                                if (dashboards.getEmail_complete_status().equals("0")) {
                                    btEmailVerificationPending.setVisibility(View.VISIBLE);
                                } else {
                                    btEmailVerificationPending.setVisibility(View.GONE);
                                }
                                if (dashboards.getPhone_complete_status().equals("0")) {
                                    btPhoneVerificationPending.setVisibility(View.VISIBLE);
                                } else {
                                    btPhoneVerificationPending.setVisibility(View.GONE);
                                }

                            } else {
                                cvEmailPhoneVerificationPending.setVisibility(View.GONE);
                            }


                            if (dashboards.getEmail_complete_status().equals("0")) {

                                // ivVerifyEmail.setImageDrawable(getResources().getDrawable(R.drawable.ver_step2));
                                ivVerifyEmail.setImageResource(R.drawable.ver_step2);
                                ivVerifyEmail.setBackgroundResource(R.drawable.border_dash_main_profilecombox);
                                pCOmpleteStatus = false;
                            } else {
                                ivVerifyEmail.setImageResource(R.drawable.ver_step2_active);
                                ivVerifyEmail.setOnClickListener(null);
                                ivVerifyEmail.setBackgroundResource(R.drawable.border_dash_main_profilecombox_green);
                                Members memberObj = SharedPreferenceManager.getUserObject(context);

                                if (memberObj.getEmail_verified() == 0) {
                                    memberObj.setEmail_verified(1);
                                    SharedPreferenceManager.setUserObject(context, memberObj);
                                }


                            }


                            if (dashboards.getPhone_complete_status().equals("0")) {
                                pCOmpleteStatus = false;
                                ivVerifyPhone.setImageResource(R.drawable.ver_step3);
                                ivVerifyPhone.setBackgroundResource(R.drawable.border_dash_main_profilecombox);
                            } else {
                                ivVerifyPhone.setImageResource(R.drawable.ver_step3_active);
                                ivVerifyPhone.setBackgroundResource(R.drawable.border_dash_main_profilecombox_green);
                                ivVerifyPhone.setOnClickListener(null);
                                Members memberObj = SharedPreferenceManager.getUserObject(context);

                                if (memberObj.getPhone_verified() == 0) {
                                    memberObj.setPhone_verified(1);
                                    SharedPreferenceManager.setUserObject(context, memberObj);
                                }


                            }
                            if (dashboards.getAdmin_approved_status().equals("0")) {
                                pCOmpleteStatus = false;
                                //Log.e("mem status", member.getMember_status() + "");
                                if (member.getMember_status() == 7) {
                                    ivReviewPending.setVisibility(View.GONE);
                                    ivReviewPendingOrange.setVisibility(View.VISIBLE);

                                    //    ivReviewPending.setImageResource(R.drawable.ver_step4);
                                    //    ivReviewPending.setBackgroundResource(R.drawable.border_dash_main_profilecombox_orange);


                                    getAdminNotes();


                                } else {
                                    ivReviewPendingOrange.setVisibility(View.GONE);
                                    ivReviewPending.setImageResource(R.drawable.ver_step4);
                                    ivReviewPending.setBackgroundResource(R.drawable.border_dash_main_profilecombox);

                                }


                            } else {
                                ivReviewPendingOrange.setVisibility(View.GONE);
                                ivReviewPending.setImageResource(R.drawable.ver_step4_active);
                                ivReviewPending.setBackgroundResource(R.drawable.border_dash_main_profilecombox_green);
                                ivReviewPending.setOnClickListener(null);

                             /*    Members memberObj = SharedPreferenceManager.getUserObject(context);

                               if (memberObj.getMember_status() <= 1) {
                                    memberObj.setMember_status(2);
                                    SharedPreferenceManager.setUserObject(context, memberObj);
                                }*/


                            }

                            if (pCOmpleteStatus) {
                                cardViewProfileCompletionStatus.setVisibility(View.GONE);
                               /* Members memberObj = SharedPreferenceManager.getUserObject(context);
                                memberObj.setMember_status(3);
                                SharedPreferenceManager.setUserObject(context, memberObj);*/
                            } else {
                                focusOnView();
                            }
                            //    NvScreenMain.setFocusable(true);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //pDialog.dismiss();
                            pDialog.setVisibility(View.INVISIBLE);
                            setWaitScreen(false);
                            swipeRefreshLayout.setRefreshing(false);

                        } catch (NullPointerException nullPointerException) {
                            nullPointerException.printStackTrace();
                            //pDialog.dismiss();
                            pDialog.setVisibility(View.INVISIBLE);
                            setWaitScreen(false);
                            swipeRefreshLayout.setRefreshing(false);

                        }

                        pDialog.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                        setWaitScreen(false);
                        //   pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                //  pDialog.dismiss();
                pDialog.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                setWaitScreen(false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(req, Tag);
    }


    @Override
    public void onItemClick(View view, Members members, int position, List<Members> items, Members memResultsObj) {
        //  Toast.makeText(getActivity(), members.getPath() + " clicked", Toast.LENGTH_SHORT).show();
 /*       Intent intent = new Intent(getActivity(), UserProfileActivity.class);
        intent.putExtra("userpath", members.getUserpath());
        startActivity(intent);*/

        //Log.e("position", "position: " + position);


        Activity activity = (Activity) getContext();
        MarryMax marryMax = new MarryMax(getActivity());
        if (ConnectCheck.isConnected(activity.findViewById(android.R.id.content))) {

            //Log.e("Data list ", "" + items.size());
            Gson gson = new Gson();
            marryMax.statusBaseChecks(members, getContext(), 1, getFragmentManager(), DashboardMainFragment.this, view, gson.toJson(items), "" + position, memResultsObj, Tag);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        /*if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }*/
    }


    @Override
    public void bottomNavSelected() {

    }

    private void viewProfile() {
        Members member = SharedPreferenceManager.getUserObject(context);
      /*  if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {

            if (SharedPreferenceManager.getUserObject(getContext()).getMember_status() != 0) {
                if (SharedPreferenceManager.getUserObject(getContext()).getPath() != null && SharedPreferenceManager.getUserObject(getContext()).getPath() != "") {
                    Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                    intent.putExtra("userpath", SharedPreferenceManager.getUserObject(getContext()).getPath());
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Error ! ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please Complete Your Profile", Toast.LENGTH_SHORT).show();
            }

        }*/


        if (member.getMember_status() == 0 || member.getMember_status() >= 7) {
            MarryMax marryMax = new MarryMax(getActivity());
            marryMax.getProfileProgress(context, member, getActivity());
        } else {
            Intent intent = new Intent(getActivity(), MyProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("userpath", SharedPreferenceManager.getUserObject(context).getPath());
            startActivity(intent);
        }


    }


    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {
      /*  NvScreenMain.setVisibility(set ? View.GONE : View.VISIBLE);
        llScreenWait.setVisibility(set ? View.VISIBLE : View.GONE);*/
    }


    private void getAdminNotes() {



    /*    final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();*/

        pDialog.setVisibility(View.VISIBLE);
        //Log.e("getAdminNotes path", "" + Urls.getAdminNotes + SharedPreferenceManager.getUserObject(context).getPath());

        JsonArrayRequest req = new JsonArrayRequest(Urls.getAdminNotes + SharedPreferenceManager.getUserObject(context).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("getAdminNotes", response.toString());
                        try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();

                            List<WebArd> dataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);


                            //  JSONArray jsonCountryStaeObj = response.getJSONArray(0);
                            //    JSONObject jsObject = jsonCountryStaeObj.getJSONObject(0);

                            //   adminNotes = jsObject.getString("name");
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < dataList.size(); i++) {
                                WebArd ard = dataList.get(i);
                                builder.append(ard.getName() + " <br><br>");
                            }
                            adminNotes = builder.toString();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.setVisibility(View.GONE);
                        //  pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                //   pDialog.dismiss();
                pDialog.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(req, Tag);
    }

    @Override
    public void onPreferenceComplete(String s) {
        setupViewPager();
    }

    @Override
    public void onComplete(String s) {

    }

    /*private void getStatus() {


        Log.e("getStatus ", "" + Urls.getStatus + SharedPreferenceManager.getUserObject(getContext()).getPath());
        StringRequest req = new StringRequest(Urls.getStatus + SharedPreferenceManager.getUserObject(context).getPath(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getStatus Response", response.toString());


                        Members member1 = SharedPreferenceManager.getUserObject(context);
                        if (member1.getMember_status() != Long.parseLong(response)) {
                            member1.setMember_status(Long.parseLong(response));
                            SharedPreferenceManager.setUserObject(context, member1);
                            member = member1;
                        }

                        if (response.equals("5")) {
                            MySingleton.getInstance(context).getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
                                @Override
                                public boolean apply(Request<?> request) {
                                    return true;
                                }
                            });

                            UserSessionManager sessionManager = new UserSessionManager(context);
                            sessionManager.logoutUser();


                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(req, Tag);
    }*/

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

    private void setupViewPager() {

        Bundle argsRequest = new Bundle();
        argsRequest.putString("type", "mymatches");
        argsRequest.putString("msg", "We haven’t found any match.");


        Bundle argsPermission = new Bundle();
        argsPermission.putString("type", "registertoday");
        argsPermission.putString("msg", "We haven't found new matches since last week.");


        DashMembersFragment rp1 = new DashMembersFragment();

        DashMembersFragment rp2 = new DashMembersFragment();

        rp1.setArguments(argsRequest);


        rp2.setArguments(argsPermission);


        adapterMemFragment.clearFragments();


        adapterMemFragment.addFragment(rp1, " My Matches  ");
        adapterMemFragment.addFragment(rp2, " Matches From Last Week ");
        ///adapter.addFragment(messageHistoryFragment, "Message History");


        // viewPager.setAdapter(null);
        viewPagerMemFragment.setAdapter(adapterMemFragment);
        tabLayoutMemFragment.setupWithViewPager(viewPagerMemFragment);
        adapterMemFragment.notifyDataSetChanged();

        for (int i = tabLayoutMemFragment.getTabCount() - 1; i >= 0; i--) {
            TabLayout.Tab tab = tabLayoutMemFragment.getTabAt(i);
            LinearLayout relativeLayout = (LinearLayout)
                    LayoutInflater.from(context).inflate(R.layout.custom_user_tab_item, tabLayoutMemFragment, false);
            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title1);
            tabTextView.setText(tab.getText());

            if (i == tabLayoutMemFragment.getTabCount() - 1) {
                View view1 = (View) relativeLayout.findViewById(R.id.tab_view_separator1);
                view1.setVisibility(View.INVISIBLE);
            }
            tab.setCustomView(relativeLayout);
            tab.select();
        }
        {
            //    TabLayout.Tab tabs = tabLayout1.getTabAt(lastSelectedPage);
            //   tabs.select();
        }


        Bundle argsRequest1 = new Bundle();
        argsRequest1.putString("type", "featured");
        argsRequest1.putString("msg", "We haven’t found any match.");
        DashMembersFragment frg = new DashMembersFragment();
        frg.setArguments(argsRequest1);

        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.FrameMainFeatureContainer, frg);
        //   fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();


    }


    @Override
    public void onStop() {
        super.onStop();
        MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);

    }

}
