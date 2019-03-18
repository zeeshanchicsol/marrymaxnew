package com.chicsol.marrymax.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.BuildConfig;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.DashboarMainActivityWithBottomNav;
import com.chicsol.marrymax.dialogs.dialogFeedBackPending;
import com.chicsol.marrymax.fragments.inbox.DashboardMessagesFragment;
import com.chicsol.marrymax.fragments.inbox.DashboardQuestionsFragment;
import com.chicsol.marrymax.fragments.inbox.interests.DashboardMyInterestsMainFragment;
import com.chicsol.marrymax.fragments.inbox.permissions.DashboardMyPermissionsMainFragment;
import com.chicsol.marrymax.fragments.inbox.permissions.DashboardPermissionsFragment;
import com.chicsol.marrymax.fragments.inbox.requests.DashboardMyRequestsMainFragment;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mComCount;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 11/3/2016.
 */

public class DashboardInboxMainFragment extends Fragment implements DashboarMainActivityWithBottomNav.BottomNavSelected {
    private ViewPager mViewPager;
    Typeface typeface;
    private Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserVisibleHint(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox_main, container, false);
        initialize(rootView);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (!getUserVisibleHint()) {
            return;
        }

        Log.e("called", "called");
        getCommunicationCount();


      /*  Members member = SharedPreferenceManager.getUserObject(context);
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


        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs_inbox_main);

        // tabLayout.setupWithViewPager(mViewPager);

        mViewPager = (ViewPager) rootView.findViewById(R.id.container_inbox_main);
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

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new DashboardMessagesFragment(), " My Messages ");
        adapter.addFragment(new DashboardMyInterestsMainFragment(), " Interests ");
        adapter.addFragment(new DashboardMyRequestsMainFragment(), " Requests ");
        adapter.addFragment(new DashboardQuestionsFragment(), " Questions ");


        viewPager.setAdapter(adapter);

    }

    @Override
    public void bottomNavSelected() {
        //  Toast.makeText(context, "Inbox Selected", Toast.LENGTH_SHORT).show();
        //  getCommunicationCount();
    }

    private void getCommunicationCount() {
       /* final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        Log.e("getCommuni url", Urls.getCommunicationCount + SharedPreferenceManager.getUserObject(context).get_path());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getCommunicationCount + SharedPreferenceManager.getUserObject(context).get_path(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("getSavedList ", response.toString() + "  ==   ");
                        Gson gsonc;
                        GsonBuilder gsonBuilderc = new GsonBuilder();
                        gsonc = gsonBuilderc.create();
                        Type listType = new TypeToken<mComCount>() {
                        }.getType();
                        try {


                            mComCount comCount = (mComCount) gsonc.fromJson(response.getJSONArray(0).getJSONObject(0).toString(), listType);

                            //   Log.e("ressssss", comCount.getNew_interests_count() + "");
                            //    new_messages_count = (int) comCount.getNew_messages_count();


                            Members member = SharedPreferenceManager.getUserObject(context);
                            String alias = "<font color='#9a0606'>" + member.getAlias() + "!</font><br>";


                            if (Integer.parseInt(comCount.getFeedback_pending()) == 1) {
                                String text = "Dear " + "<b>" + alias.toUpperCase() + "</b> your Feedback is Pending.To view more profiles please give your previous feedback";

                                dialogFeedBackPending newFragment = dialogFeedBackPending.newInstance(text, false);
                                //    newFragment.setTargetFragment(MyProfileSettingFragment.this, 3);
                                newFragment.show(getFragmentManager(), "dialog");

                            } else if (Integer.parseInt(comCount.getFeedback_pending()) == 2) {
                                String text = "Dear " + "<b>" + alias.toUpperCase() + "</b> your Feedbacks are Pending.To view more profiles please give your previous feedbacks";

                                dialogFeedBackPending newFragment = dialogFeedBackPending.newInstance(text, true);
                                //    newFragment.setTargetFragment(MyProfileSettingFragment.this, 3);
                                newFragment.show(getFragmentManager(), "dialog");

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        //   pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());

                //     pDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(req, "DashboardInboxMainFragment");
    }

    /**
     * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
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
