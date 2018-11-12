package com.chicsol.marrymax.activities.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.ActivityLogin;
import com.chicsol.marrymax.activities.DrawerActivity;
import com.chicsol.marrymax.activities.search.AdvanceSearchFragments.GeographyFragment;
import com.chicsol.marrymax.activities.search.AdvanceSearchFragments.ListViewAdvSearchFragment;
import com.chicsol.marrymax.activities.searchyourbestmatch.SearchYourBestMatchResultsActivity;
import com.chicsol.marrymax.dialogs.dialogLoginToContinue;
import com.chicsol.marrymax.dialogs.dialogSaveSearch;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mAdvSearchListing;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.utils.ConnectCheck;

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
    protected void onResume() {
        super.onResume();

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
        toolbar.setTitle("Advance Search");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pDialog = new ProgressDialog(SearchMainActivity.this);
        pDialog.setMessage("Loading...");

        llSaveSearch = (LinearLayout) findViewById(R.id.LinearLayoutAdvSearchMainSaveSearch);
        llResetSearch = (LinearLayout) findViewById(R.id.LinearLayoutAdvSearchMainResetSearch);
        llSearch = (LinearLayout) findViewById(R.id.LinearLayoutAdvSearchMainSearch);

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
                    // members.set_min_age(18);
                    //  members.set_max_age(70);
      /*              members.set_choice_height_from_id(70);
                    members.set_choice_height_to_id(70);*/
                    defaultSelectionsObj = members;

                    MarryMax marryMax = new MarryMax(SearchMainActivity.this);
                    marryMax.setHeighAgeChecks();

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

/*    private void setHeighAgeChecks() {
        if (defaultSelectionsObj.get_choice_age_from() == 0) {
            defaultSelectionsObj.set_choice_age_from(18);
        }
        if (defaultSelectionsObj.get_choice_age_upto() == 0) {
            defaultSelectionsObj.set_choice_age_upto(70);
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
            if (defaultSelectionsObj.get_choice_height_from_id() == 0) {
                defaultSelectionsObj.set_choice_height_from_id(Long.parseLong(dataListHeight.get(1).getId()));
            }
            if (defaultSelectionsObj.get_choice_height_to_id() == 0) {
                defaultSelectionsObj.set_choice_height_to_id(Long.parseLong(dataListHeight.get(dataListHeight.size() - 1).getId()));
            }
        }

    }*/


   /* public interface OnMenuUpdatedListener {
        public void onMenuUpdted(String i);
    }*/
}
