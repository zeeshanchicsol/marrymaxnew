package com.chicsol.marrymax.activities.search;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.ActivityLogin;
import com.chicsol.marrymax.activities.DrawerActivity;
import com.chicsol.marrymax.activities.search.AdvanceSearchFragments.ListViewAdvSearchFragment;
import com.chicsol.marrymax.activities.searchyourbestmatch.SearchYourBestMatchResultsActivity;
import com.chicsol.marrymax.dialogs.dialogLoginToContinue;
import com.chicsol.marrymax.dialogs.dialogSaveSearch;
import com.chicsol.marrymax.modal.MatchesCountUpdateEvent;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mAdvSearchListing;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;

/**
 * Created by Android on 2/22/2017.
 */

public class SearchMainActivity extends AppCompatActivity implements ListViewAdvSearchFragment.OnItemSelectedListener, dialogLoginToContinue.onCompleteLoginListener {

    Fragment fragmentItem;
    Fragment fragmentItemListi;
    ListViewAdvSearchFragment mManagerFragment;
    private LinearLayout llSearch, llResetSearch, llSaveSearch;
    private ProgressDialog pDialog;
    private Toolbar toolbar;
    private AppCompatButton btSearchProfile;
    private AppCompatEditText etSearchByProfile;
    private boolean bestMatchCheck;
    private View.OnTouchListener onTouchListener;
    //   private OnMenuUpdatedListener onMenuUpdatedListener;
    private TextView tvCounter;
    public static int filterCount = 0;

    private ImageView icMainSearch, icResetSearch, icSaveSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.adv_search_main_activity);

        //      fromResultsCheck

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        bestMatchCheck = getIntent().getBooleanExtra("searchcheck", false);

        boolean searchcheck = getIntent().getBooleanExtra("fromResultsCheck", false);

        initalize();
        setListeners();

//        resetSearch();

        if (!searchcheck) {


            if (DrawerActivity.rawSearchObj != null) {
                defaultSelectionsObj = DrawerActivity.rawSearchObj;
            }

         /*   if (DrawerActivity.rawSearchObj != null) {
                Log.e("not null", "raw object");
                defaultSelectionsObj = DrawerActivity.rawSearchObj;
            }
*/

        } else {


            Members mem = defaultSelectionsObj;
            /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flDetailContainer, fragmentItem);
            ft.detach(fragmentItem);
            ft.attach(fragmentItem);
            ft.commit();*/

        }

        MarryMax marryMax = new MarryMax(SearchMainActivity.this);
        marryMax.setHeighAgeChecks();

    }

 /*   public void setMenuUpdateListener(OnMenuUpdatedListener listener) {
        onMenuUpdatedListener = listener;
    }
*/

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(MatchesCountUpdateEvent event) {

        //  Log.e("event", "" + event.getMessage());


        if (event.getMessage().equals("filterCount")) {
            if (filterCount == 0) {
                getSupportActionBar().setTitle("Filter Results");
            } else {
                getSupportActionBar().setTitle("Filter Results (" + filterCount + ")");
            }
        } else if (event.getMessage().equals("getCount")) {
            String params;
            Members memberSearchObj = defaultSelectionsObj;
            if (bestMatchCheck) {
              /*  Intent intent = new Intent(SearchMainActivity.this, SearchYourBestMatchResultsActivity.class);
                startActivity(intent);*/
                if (memberSearchObj != null) {
                    memberSearchObj.setPage_no(1);
                    memberSearchObj.setType("");

                    Gson gson = new Gson();
                    params = gson.toJson(memberSearchObj);

                    loadCounter(params);

                }

            } else {
               /* Intent intent = new Intent(SearchMainActivity.this, SearchResultsActivity.class);
                startActivity(intent);*/

                memberSearchObj.setPath(SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                memberSearchObj.setMember_status(SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status());
                memberSearchObj.setPhone_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getPhone_verified());
                memberSearchObj.setEmail_verified(SharedPreferenceManager.getUserObject(getApplicationContext()).getEmail_verified());
                //page and type
                memberSearchObj.setPage_no(1);
                memberSearchObj.setType("");

                Gson gson = new Gson();
                params = gson.toJson(memberSearchObj);

                loadCounter(params);
            }

            //loadCounter();
        } else {
            //
            tvCounter.setText("View " + event.getMessage() + " Matches");

        }

        ///    Toast.makeText(this, "" + event.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onResetButtonClicked(View v) {
        // do whatever needs to be done. For example:
        // Toast.makeText(getApplicationContext(), v.getTag() + "  profile_created_for", Toast.LENGTH_SHORT).show();
        //============Basics=======================
      /*  if (v.getTag().equals("select_profile_with")) {
            defaultSelectionsObj.setPictureonly(0);
            defaultSelectionsObj.setOpentopublic(0);
        } else if (v.getTag().equals("profile_created_for")) {
            defaultSelectionsObj.setChoice_profile_owner_Ids("");
        } else if (v.getTag().equals("zodiac")) {
            defaultSelectionsObj.setChoice_zodiac_sign_ids("");

        }*/

        //==============Appearance=====================
    /*     if (v.getTag().equals("physique")) {
            defaultSelectionsObj.setChoice_body_ids("");

        } else if (v.getTag().equals("complexion")) {
            defaultSelectionsObj.setChoice_complexion_ids("");

        } else if (v.getTag().equals("eye_color")) {
            defaultSelectionsObj.setChoice_eye_color_ids("");


        } else if (v.getTag().equals("hair_color")) {
            defaultSelectionsObj.setChoice_hair_color_ids("");

        }*/
        //==============MaritalStatus=====================


      /*   if (v.getTag().equals("martial_status")) {
            defaultSelectionsObj.setChoice_marital_status_ids("");


        } else if (v.getTag().equals("children")) {
            defaultSelectionsObj.setChoice_children_ids("");
        }*/


        //==============education occupation=====================
    /*     if (v.getTag().equals("education")) {
            defaultSelectionsObj.setChoice_education_ids("");
        } else if (v.getTag().equals("occupation")) {
            defaultSelectionsObj.setChoice_occupation_ids("");

        }*/

        //==============ethnic_background=====================
       /* if (v.getTag().equals("ethnic_background")) {
            defaultSelectionsObj.setChoice_ethnic_bground_ids("");
        } else if (v.getTag().equals("religious_sect")) {
            defaultSelectionsObj.setChoice_religious_sect_ids("");
        } else if (v.getTag().equals("caste")) {
            defaultSelectionsObj.setChoice_caste_ids("");
        }
*/

        //==============LifeStyle 1=====================
       /*  if (v.getTag().equals("raisedwhere")) {
            defaultSelectionsObj.setChoice_raised_ids("");
        } else if (v.getTag().equals("hijab")) {
            defaultSelectionsObj.setChoice_hijab_ids("");
        } else if (v.getTag().equals("familyvalue")) {
            defaultSelectionsObj.setChoice_family_values_ids("");
        } else if (v.getTag().equals("living_arrangement")) {
            defaultSelectionsObj.setChoice_living_arangment_ids("");
        }
*/

        //==============LifeStyle2=====================
   /*    if (v.getTag().equals("siblingposition")) {
            defaultSelectionsObj.setChoice_sibling_ids("");

        } else if (v.getTag().equals("smoking")) {
            defaultSelectionsObj.setChoice_smoking_ids("");

        } else if (v.getTag().equals("drink")) {
            defaultSelectionsObj.setChoice_drink_ids("");

        }*/
        //==============Geography=====================
       /*  if (v.getTag().equals("top_locations")) {
            defaultSelectionsObj.setChoice_country_ids("");
            defaultSelectionsObj.setChoice_state_ids("");
            defaultSelectionsObj.setChoice_cities_ids("");

        } else if (v.getTag().equals("country")) {
            defaultSelectionsObj.setChoice_country_ids("");
            defaultSelectionsObj.setChoice_state_ids("");
            defaultSelectionsObj.setChoice_cities_ids("");


        } else if (v.getTag().equals("state")) {
            defaultSelectionsObj.setChoice_state_ids("");

        } else if (v.getTag().equals("city")) {
            defaultSelectionsObj.setChoice_cities_ids("");

        } else if (v.getTag().equals("visa_status")) {
            defaultSelectionsObj.setChoice_visa_status_ids("");

        }*/


        // refreshViews();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void initalize() {
        etSearchByProfile = (AppCompatEditText) findViewById(R.id.EditTextAdvSearchMainProfileSearch);
        btSearchProfile = (AppCompatButton) findViewById(R.id.ButtonAdvSearchMainProfileSearch);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Filter Results");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pDialog = new ProgressDialog(SearchMainActivity.this);
        pDialog.setMessage("Loading...");


        icSaveSearch = (ImageView) findViewById(R.id.ImageViewAdvSearchSaveicon);
        icMainSearch = (ImageView) findViewById(R.id.ImageViewAdvSearchMainIcon);
        icResetSearch = (ImageView) findViewById(R.id.ImageViewAdvSearchResetIcon);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            icSaveSearch.setVisibility(View.GONE);
            icMainSearch.setVisibility(View.GONE);
            icResetSearch.setVisibility(View.GONE);
        }


        llSaveSearch = (LinearLayout) findViewById(R.id.LinearLayoutAdvSearchMainSaveSearch);
        llResetSearch = (LinearLayout) findViewById(R.id.LinearLayoutAdvSearchMainResetSearch);
        llSearch = (LinearLayout) findViewById(R.id.LinearLayoutAdvSearchMainSearch);

        tvCounter = (TextView) findViewById(R.id.TextViewAdvanceSearchCounter);


        //    mManagerFragment = new ListViewAdvSearchFragment().newInstance();
        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etSearchByProfile.getRight() - etSearchByProfile.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        //  Toast.makeText(SearchMainActivity.this, "clicked", Toast.LENGTH_SHORT).show();

                        if (ConnectCheck.isConnected(v)) {


                            if (bestMatchCheck) {


                                if (!etSearchByProfile.getText().toString().isEmpty()) {

                                    Members members = new Members();
                                    members.setAlias(etSearchByProfile.getText().toString());
                                    //  defaultSelectionsObj=
                                    defaultSelectionsObj = members;
                                    Intent intent = new Intent(SearchMainActivity.this, SearchYourBestMatchResultsActivity.class);


                                    Bundle b = new Bundle();
                                    b.putBoolean("aliascheck", true);
                                    intent.putExtra("bundle", b);
                                    startActivity(intent);
                                    // finish();
                                }


                            } else {


                                if (!etSearchByProfile.getText().toString().isEmpty()) {

                                    Members members = new Members();
                                    members.setAlias(etSearchByProfile.getText().toString());
                                    defaultSelectionsObj = members;
                                    Intent intent = new Intent(SearchMainActivity.this, SearchResultsActivity.class);
                                    startActivity(intent);
                                    // finish();
                                }
                            }
                        }


                        return true;
                    }
                }
                return false;
            }
        };

    }


    private void setListeners() {

        etSearchByProfile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("log " + charSequence, "" + charSequence.length());
                if (charSequence.length() > 0) {
                    //  Drawable img = getResources().getDrawable(R.drawable.ic_search_black_24dp);

                    Drawable drawable;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_search_black_24dp);
                    } else {
                        drawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_search_black_24dp);
                        // drawable = getResources().getDrawable(R.drawable.ic_search_black_24dp);
                    }

                    //  StateListDrawable stateListDrawable = (StateListDrawable) drawable;

                    etSearchByProfile.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    etSearchByProfile.setOnTouchListener(onTouchListener);
                    //  etSearchByProfile.setCompoundDrawables(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_search_black_24dp), null);

                } else {
                    etSearchByProfile.setOnTouchListener(null);
                    //   etSearchByProfile.setCompoundDrawables(null, null, null, null);
                    etSearchByProfile.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


     /*   btSearchProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectCheck.isConnected(v)) {


                    if (bestMatchCheck) {


                        if (!etSearchByProfile.getText().toString().isEmpty()) {

                            Members members = new Members();
                            members.setAlias(etSearchByProfile.getText().toString());
                            defaultSelectionsObj = members;
                            Intent intent = new Intent(SearchMainActivity.this, SearchYourBestMatchResultsActivity.class);


                            Bundle b = new Bundle();
                            b.putBoolean("aliascheck", true);
                            intent.putExtra("bundle", b);
                            startActivity(intent);
                            // finish();
                        }


                    } else {


                        if (!etSearchByProfile.getText().toString().isEmpty()) {

                            Members members = new Members();
                            members.setAlias(etSearchByProfile.getText().toString());
                            defaultSelectionsObj = members;
                            Intent intent = new Intent(SearchMainActivity.this, SearchResultsActivity.class);
                            startActivity(intent);
                            // finish();
                        }
                    }
                }
            }
        });
*/
        llSaveSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   dialogBlock newFragment = dialogBlock.newInstance(responseJSONArray, userpath, member);
                //  newFragment.show(getSupportFragmentManager(), "dialog");
                if (!bestMatchCheck) {

                    if (ConnectCheck.isConnected(v)) {
                        dialogSaveSearch dialog = dialogSaveSearch.newInstance();
                        dialog.show(getSupportFragmentManager(), "Save Search");
                    }

                } else {
                    dialogLoginToContinue newFragment = dialogLoginToContinue.newInstance();
                    newFragment.show(getSupportFragmentManager(), "dialog");

                }

            }
        });

        llResetSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectCheck.isConnected(v)) {

                    Members members = new Members();
                    // members.setMin_age(18);
                    //  members.setMax_age(70);
      /*              members.setChoice_height_from_id(70);
                    members.setChoice_height_to_id(70);*/
                    defaultSelectionsObj = members;

                    MarryMax marryMax = new MarryMax(SearchMainActivity.this);
                    marryMax.setHeighAgeChecks();

                    refreshViews();

                  /*  Fragment frg = null;
                    frg = getSupportFragmentManager().findFragmentByTag("ListViewAdvSearchFragment");
                    final FragmentTransaction fta = getSupportFragmentManager().beginTransaction();
                    fta.detach(frg);
                    fta.attach(frg);
                    fta.commit();
*/
        /*            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentItemsList);
                   // Fragment currentFragment = getFragmentManager().findFragmentByTag("YourFragmentTag");
                    FragmentTransaction fragmentTransactiona = getFragmentManager().beginTransaction();
                    fragmentTransactiona.detach(currentFragment);
                    fragmentTransactiona.attach(currentFragment);
                    fragmentTransactiona.commit();*/


                    Toast.makeText(SearchMainActivity.this, "Reset Done", Toast.LENGTH_SHORT).show();

                }

            }
        });

        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (bestMatchCheck) {

                    Intent intent = new Intent(SearchMainActivity.this, SearchYourBestMatchResultsActivity.class);
                    startActivity(intent);

                } else {
                    if (ConnectCheck.isConnected(v)) {
                        Intent intent = new Intent(SearchMainActivity.this, SearchResultsActivity.class);
                        startActivity(intent);
                        // finish();
                    }
                }

            }
        });


    }

    private void refreshViews() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flDetailContainer, fragmentItem);
        ft.detach(fragmentItem);
        ft.attach(fragmentItem);
        ft.commit();


        fragmentItemListi = new ListViewAdvSearchFragment();
        FragmentTransaction fta = getSupportFragmentManager().beginTransaction();
        fta.replace(R.id.fragmentItemsList, fragmentItemListi);
        fta.detach(fragmentItemListi);
        fta.attach(fragmentItemListi);
        fta.commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // overridePendingTransition(R.anim.enter, R.anim.right_to_left);
    }

    @Override
    public void onItemSelected(mAdvSearchListing item) {
        // single activity with list and detail
        // Replace frame layout with correct detail fragment
        fragmentItem = item.getFragment();

        //ItemDetailFragment.newInstance(item_image_slider);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flDetailContainer, fragmentItem);
        ft.commit();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }

 /*   private void resetSearch() {
        Members members = new Members();
        defaultSelectionsObj = members;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flDetailContainer, fragmentItem);
        ft.detach(fragmentItem);
        ft.attach(fragmentItem);
        ft.commit();


    }*/


    @Override
    public void onCompleteLogin(String s) {
        Intent intent = new Intent(SearchMainActivity.this, ActivityLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }


    private void loadCounter(String paramsString) {


        //  pDialog.setVisibility(View.VISIBLE);
        //      setLoader(true);
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject params = null;
        try {
            params = new JSONObject(paramsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("Params search" + " " + Urls.searchedCount, "" + params);

        //Log.e("Params search" + " " + Urls.searchProfiles, "");
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.searchedCount, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  serrr appearance", response + "");
                        try {
                            int id = Math.round(Float.parseFloat(response.get("id").toString()));
                            EventBus.getDefault().postSticky(new MatchesCountUpdateEvent(id + ""));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //  if (!refresh) {
                        // pDialog.dismiss();
                        //   pDialog.setVisibility(View.INVISIBLE);
                        //   setLoader(false);
                        //  }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                //   if (!refresh) {
                //pDialog.dismiss();
                //   pDialog.setVisibility(View.INVISIBLE);
                //           // setLoader(false);
                //    }
                // LinearLayoutMMMatchesNotFound.setVisibility(View.VISIBLE);
                //  findViewById(R.id.ButtonOnSearchClick).setVisibility(View.GONE);
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }




/*    private void setHeighAgeChecks() {
        if (defaultSelectionsObj.getChoice_age_from() == 0) {
            defaultSelectionsObj.setChoice_age_from(18);
        }
        if (defaultSelectionsObj.getChoice_age_upto() == 0) {
            defaultSelectionsObj.setChoice_age_upto(70);
        }

        Gson gsonc;
        GsonBuilder gsonBuilderc = new GsonBuilder();
        gsonc = gsonBuilderc.create();
        Type listType = new TypeToken<List<WebArd>>() {
        }.getType();
        List<WebArd> dataListHeight = new ArrayList<>();
        try {
            dataListHeight = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(10).toString(), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (dataListHeight.size() > 0) {
            if (defaultSelectionsObj.getChoice_height_from_id() == 0) {
                defaultSelectionsObj.setChoice_height_from_id(Long.parseLong(dataListHeight.get(1).getId()));
            }
            if (defaultSelectionsObj.getChoice_height_to_id() == 0) {
                defaultSelectionsObj.setChoice_height_to_id(Long.parseLong(dataListHeight.get(dataListHeight.size() - 1).getId()));
            }
        }

    }*/


   /* public interface OnMenuUpdatedListener {
        public void onMenuUpdted(String i);
    }*/
}
