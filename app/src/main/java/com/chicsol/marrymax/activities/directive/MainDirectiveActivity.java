package com.chicsol.marrymax.activities.directive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.search.SearchMainActivity;
import com.chicsol.marrymax.dialogs.dialogProfileCompletion;
import com.chicsol.marrymax.fragments.AccountSetting.MatchingAttributeFragment;
import com.chicsol.marrymax.fragments.AccountSetting.MyContactFragment;
import com.chicsol.marrymax.fragments.AccountSetting.MyProfileStatusFragment;
import com.chicsol.marrymax.fragments.inbox.DashboardMessagesFragment;
import com.chicsol.marrymax.fragments.inbox.interests.DashboardMyInterestsMainFragment;
import com.chicsol.marrymax.fragments.inbox.permissions.DashboardMyPermissionsMainFragment;
import com.chicsol.marrymax.fragments.inbox.requests.DashboardMyRequestsMainFragment;
import com.chicsol.marrymax.fragments.list.BlockedListFragment;
import com.chicsol.marrymax.fragments.list.RecommendedMatches;
import com.chicsol.marrymax.fragments.list.RemovedFromSearchFragment;
import com.chicsol.marrymax.fragments.list.myContacts.MyContactsMainFragment;
import com.chicsol.marrymax.fragments.matches.AccpetedMembers;
import com.chicsol.marrymax.fragments.matches.LookingForEachOther;
import com.chicsol.marrymax.fragments.matches.MatchesWithPhotoUpdate2Fragment;
import com.chicsol.marrymax.fragments.matches.MyFavouriteMatches;
import com.chicsol.marrymax.fragments.matches.PrefferedMatchingProfileFragment;
import com.chicsol.marrymax.fragments.matches.SavedNotes;
import com.chicsol.marrymax.fragments.matches.WhoViewedMe;
import com.chicsol.marrymax.fragments.matches.WhoisLookingForMe;
import com.chicsol.marrymax.fragments.matches.WhomIViewed;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.MySingleton;

import org.json.JSONArray;

import static com.chicsol.marrymax.utils.Constants.jsonArraySearch;

public class MainDirectiveActivity extends AppCompatActivity implements dialogProfileCompletion.onCompleteListener {
    String subType = null;
    Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preffered_matching);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        int type = getIntent().getExtras().getInt("type");
        subType = getIntent().getExtras().getString("subtype");

        if (subType != null) {
            data = new Bundle();//create bundle instance
            data.putString("subtype", subType);//put string to pass with a PassPhraseArdAp value
        }
        Log.e("Log", "" + type);

        Fragment fragment = null;
        switch (type) {
            case 1:

                getSupportActionBar().setTitle("Preferred Matching Profile");
                fragment = new PrefferedMatchingProfileFragment();

                break;
            case 2:
                getSupportActionBar().setTitle("Matches With Photo Update");
                fragment = new MatchesWithPhotoUpdate2Fragment();
                break;

            case 3:
                getSupportActionBar().setTitle("Matches Looking For me");
                fragment = new LookingForEachOther();

                break;
            //mem loogking for me
            case 4:
                getSupportActionBar().setTitle("Members Looking For me");
                fragment = new WhoisLookingForMe();
                break;

            case 5:
                getSupportActionBar().setTitle("Messages");
                fragment = new DashboardMessagesFragment();
                break;

            case 6:
                getSupportActionBar().setTitle("Interests");
                fragment = new DashboardMyInterestsMainFragment();
                break;

            case 7:
                getSupportActionBar().setTitle("Requests");
                fragment = new DashboardMyRequestsMainFragment();
                break;

            case 8:
                getSupportActionBar().setTitle("Accepted Members");
                fragment = new AccpetedMembers();
                break;
            case 9:
                getSupportActionBar().setTitle("My Favourites");
                fragment = new MyFavouriteMatches();
                break;
            case 10:
                getSupportActionBar().setTitle("Removed From Search");
                fragment = new RemovedFromSearchFragment();
                break;
            case 11:
                getSupportActionBar().setTitle("Whom I Viewed");
                fragment = new WhomIViewed();
                break;


            case 12:
                getSupportActionBar().setTitle("Who Viewed Me");
                fragment = new WhoViewedMe();
                break;

            case 13:
                getSupportActionBar().setTitle("Matching Attributes");
                fragment = new MatchingAttributeFragment();
                break;
            case 14:
                getSupportActionBar().setTitle("My Contact ");
                fragment = new MyContactFragment();
                break;

            case 15:
                getSupportActionBar().setTitle("Blocked List");
                fragment = new BlockedListFragment();
                break;

            case 16:
                getSupportActionBar().setTitle("Saved Notes");
                fragment = new SavedNotes();
                break;
            case 17:
                getSupportActionBar().setTitle("Contacts");
                fragment = new MyContactsMainFragment();
                if (subType != null) {
                    fragment.setArguments(data);
                }

                break;
            case 18:
                getSupportActionBar().setTitle("Interests");
                fragment = new DashboardMyInterestsMainFragment();
                if (subType != null) {
                    fragment.setArguments(data);
                }
                break;
            case 19:
                getSupportActionBar().setTitle("Requests");
                fragment = new DashboardMyRequestsMainFragment();
                if (subType != null) {
                    fragment.setArguments(data);
                }
                break;
            case 20:
                getSupportActionBar().setTitle("Permissions");
                fragment = new DashboardMyPermissionsMainFragment();
                if (subType != null) {
                    fragment.setArguments(data);
                }
                break;
            case 21:
                getSupportActionBar().setTitle("Recommended Matches");
                fragment = new RecommendedMatches();
                break;
            case 22:
                getSupportActionBar().setTitle("My Profile Status");
                fragment = new MyProfileStatusFragment();
                break;
        }


        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //PrefferedMatchingProfileFragment fragment = new PrefferedMatchingProfileFragment();
        fragmentTransaction.add(R.id.FrameMainContainer, fragment);
        fragmentTransaction.commit();


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onGoToSearchClick(View view) {
        if (jsonArraySearch == null) {
            getData();
        } else {

            Intent intent = new Intent(MainDirectiveActivity.this, SearchMainActivity.class);
            startActivityForResult(intent, 2);
            // overridePendingTransition(R.anim.enter, R.anim.right_to_left);
        }
    }

    public void onCompleteProfile(View view) {
        MarryMax marryMax = new MarryMax(this);
        marryMax.getProfileProgress(getApplicationContext(), SharedPreferenceManager.getUserObject(getApplicationContext()), this);

    }

    public void onSubscribeClick(View view) {
        MarryMax marryMax = new MarryMax(MainDirectiveActivity.this);
        marryMax.subscribe();

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
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

    @Override
    public void onComplete(int s) {

    }
}
