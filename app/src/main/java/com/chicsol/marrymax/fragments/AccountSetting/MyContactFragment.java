package com.chicsol.marrymax.fragments.AccountSetting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.MySpinnerAdapter;
import com.chicsol.marrymax.adapters.MySpinnerCSCAdapter;
import com.chicsol.marrymax.dialogs.dialogProfileCompletion;
import com.chicsol.marrymax.dialogs.dialogVerifyphone;
import com.chicsol.marrymax.modal.MatchesCountUpdateEvent;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.PhoneVerificationStatusUpdateEvent;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.modal.WebCSC;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.ConnectCheck;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.chicsol.marrymax.widgets.PrefixEditText;
import com.chicsol.marrymax.widgets.mTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;

/**
 * Created by Android on 11/3/2016.
 */

public class MyContactFragment extends Fragment implements dialogVerifyphone.onCompleteListener, SwipeRefreshLayout.OnRefreshListener {
    String Tag = "MyContactFragment";
    private Button bt_subscribe, bt_viewprofile, bt_viewprofile2;

    private mTextView tvMobile, tvCountry, tvLandline, tvNameOfContactPerson, tvRelationshipWithMember, tvConvenientCallTime, tvLandlineCode, tvMobileCode;

    private AppCompatSpinner spinnerAScontactCountry, spinnerAScontactRelationShipWithMember, spinnerMyAScontactConvTimeToCall;
    private AppCompatEditText EditTextAScontactPersonName;
    private PrefixEditText EditTextAScontactMobileNumber, EditTextAScontactLandlineNumber;
    // private EditText etAsEmail;

    private MySpinnerAdapter adapter_relationship, adapter_convenientTimeToCall;
    private MySpinnerCSCAdapter adapter_countryOfLiving;
    private List<WebArd> relationshipDataList, covenientTimetoCallDataList;
    private List<WebCSC> countryOfLivingDataList;
    private LinearLayout LinearlayoutAccountSettingMyContactEdiDelete, LinearlayoutAccountSettingMCEditContact,
            LinearlayoutAccountSettingMCDeleteContact, llPhoneVerified, llVerifyPhone, llPhoneNotVerified, llEnterCode;
    private CardView LinearlayoutAccountSettingMyContactNoData, LinearlayoutAccountSettingMyContact;
    private AppCompatButton btSave, btCancel;
    private long about_type_id = 0;
    private Members member = null;
    private Snackbar snackbarNotVerified, snackbarNotVerifiedLandLine;
    RelativeLayout tvHeading;
    private String country_id = "";
    private ProgressBar pDialog;
    /*  private AppCompatButton  btResendVerificationEmail, btUpdateEmail, btEmailCancel;*/

    private AppCompatButton btAlreadyHaveCode;

   /* private LinearLayout
            llASEmail;*/

    /*    boolean emailEnabled = false;*/
    LinearLayout llPhoneVerifyLandline;
    AppCompatTextView tvPhoneVerifyLandline;
    ImageView ivPhoneVerifyLandline;

    String snackBarToolTip = "";
    String snackBarToolTipLandLine = "";

    Context context;
    private SwipeRefreshLayout swipeRefresh;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_as_my_contact, container, false);
        initilize(rootView);
        setListeners();
        return rootView;
    }

    private void initilize(View view) {


        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshDashMainMM);
        swipeRefresh.setOnRefreshListener(this);


        btAlreadyHaveCode = (AppCompatButton) view.findViewById(R.id.AppcompatButtonMyContactAlraadyHaveCode);

        pDialog = (ProgressBar) view.findViewById(R.id.ProgressbarProjectMain);
        pDialog.setVisibility(View.GONE);
        llEnterCode = (LinearLayout) view.findViewById(R.id.LinearlayoutAccountSettingMyContactEnterCode);
        llPhoneVerified = (LinearLayout) view.findViewById(R.id.LinearLayoutAccountSettingMyContactVerified);
        llVerifyPhone = (LinearLayout) view.findViewById(R.id.LinearLayoutAccountSettingMyContactVerifyPhone);
        llPhoneNotVerified = (LinearLayout) view.findViewById(R.id.LinearLayoutAccountSettingMyContactNotVerified);
        // llVerifyPhone.setVisibility(View.GONE);
        llPhoneVerifyLandline = (LinearLayout) view.findViewById(R.id.LinearLayoutAccountSettingMyContactVerifyLandline);
        ivPhoneVerifyLandline = (ImageView) view.findViewById(R.id.ImageViewASContactLandLineNumberVerify);
        tvPhoneVerifyLandline = (AppCompatTextView) view.findViewById(R.id.TextViewASContactLandLineNumberVerify);
        //     ImageViewASContactLandLineNumberVerify
        //  TextViewASContactLandLineNumberVerify


        //AppCompatButton btS
        btSave = (AppCompatButton) view.findViewById(R.id.ButtonAccountSettingContactSave);
        btCancel = (AppCompatButton) view.findViewById(R.id.ButtonAccountSettingContactCancel);


   /*Email

        btResendVerificationEmail = (AppCompatButton) view.findViewById(R.id.ButtonAccountSettingResendVerificationEmail);
        btUpdateEmail = (AppCompatButton) view.findViewById(R.id.ButtonAccountSettingUpdateEmail);
        btEmailCancel = (AppCompatButton) view.findViewById(R.id.ButtonAccountSettingUpdateEmailCancel);*/


        LinearlayoutAccountSettingMyContactNoData = (CardView) view.findViewById(R.id.LinearlayoutAccountSettingMyContactNoData);
        LinearlayoutAccountSettingMyContact = (CardView) view.findViewById(R.id.LinearlayoutAccountSettingMyContact);
        LinearlayoutAccountSettingMyContactEdiDelete = (LinearLayout) view.findViewById(R.id.LinearlayoutAccountSettingMyContactEdiDelete);
        //  llASEmail = (LinearLayout) view.findViewById(R.id.LinearlayoutAccountSettingEmail);

        LinearlayoutAccountSettingMCEditContact = (LinearLayout) view.findViewById(R.id.LinearlayoutAccountSettingMCEditContact);
        LinearlayoutAccountSettingMCDeleteContact = (LinearLayout) view.findViewById(R.id.LinearlayoutAccountSettingMCDeleteContact);

        tvHeading = (RelativeLayout) view.findViewById(R.id.mTextViewAccountSettingMyContactTitle);
        tvMobile = (mTextView) view.findViewById(R.id.TextViewASContactMobileNumber);
        tvMobileCode = (mTextView) view.findViewById(R.id.TextViewASContactMobileNumberCode);
        tvCountry = (mTextView) view.findViewById(R.id.TextViewASContactCountry);
        tvLandline = (mTextView) view.findViewById(R.id.TextViewASContactLandLineNumber);
        tvLandlineCode = (mTextView) view.findViewById(R.id.TextViewASContactLandLineNumberCode);

        tvNameOfContactPerson = (mTextView) view.findViewById(R.id.TextViewASContactNameContact);
        tvRelationshipWithMember = (mTextView) view.findViewById(R.id.TextViewASContactRelationship);
        tvConvenientCallTime = (mTextView) view.findViewById(R.id.TextViewASContactCallTime);


        //input form
        spinnerAScontactCountry = (AppCompatSpinner) view.findViewById(R.id.spinnerAScontactCountry);
        spinnerAScontactRelationShipWithMember = (AppCompatSpinner) view.findViewById(R.id.spinnerAScontactRelationShipWithMember);
        spinnerMyAScontactConvTimeToCall = (AppCompatSpinner) view.findViewById(R.id.spinnerMyAScontactConvTimeToCall);

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };


        EditTextAScontactMobileNumber = (PrefixEditText) view.findViewById(R.id.EditTextAScontactMobileNumber);
        EditTextAScontactMobileNumber.setFilters(new InputFilter[]{filter});
        //  etAsEmail = (EditText) view.findViewById(R.id.EditTextAScontactEmail);

        EditTextAScontactLandlineNumber = (PrefixEditText) view.findViewById(R.id.EditTextAScontactLandlineNumber);
        EditTextAScontactLandlineNumber.setFilters(new InputFilter[]{filter});
        EditTextAScontactPersonName = (AppCompatEditText) view.findViewById(R.id.EditTextAScontactPersonName);

        countryOfLivingDataList = new ArrayList<>();
        relationshipDataList = new ArrayList<>();
        covenientTimetoCallDataList = new ArrayList<>();

        covenientTimetoCallDataList.add(new WebArd("0", "Select"));
        covenientTimetoCallDataList.add(new WebArd("1", "Morning"));
        covenientTimetoCallDataList.add(new WebArd("2", "Afternoon"));
        covenientTimetoCallDataList.add(new WebArd("3", "Evening"));

        adapter_countryOfLiving = new MySpinnerCSCAdapter(getContext(),
                android.R.layout.simple_spinner_item, countryOfLivingDataList);
        spinnerAScontactCountry.setAdapter(adapter_countryOfLiving);

        adapter_relationship = new MySpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item, relationshipDataList);


        spinnerAScontactRelationShipWithMember.setAdapter(adapter_relationship);

        adapter_convenientTimeToCall = new MySpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item, covenientTimetoCallDataList);
        spinnerMyAScontactConvTimeToCall.setAdapter(adapter_convenientTimeToCall);


        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean landlineAdded = false;
                String mobNum = EditTextAScontactMobileNumber.getText().toString();
                String countryCode = EditTextAScontactMobileNumber.getTag().toString();


                String landNum = EditTextAScontactLandlineNumber.getText().toString();

                //    Log.e("mobNum",mobNum);
                //    Log.e("landNum",landNum);


                String personName = EditTextAScontactPersonName.getText().toString();

                if (spinnerAScontactCountry.getSelectedItemPosition() != 0) {
                    countryCode = countryCode.replace("+", "");
                }

                //   Log.e("111 countryCode ", Integer.parseInt(countryCode.trim() )+ "");
                //   Log.e("111 land number is", Integer.parseInt(mobNum.substring(0, countryCode.trim().length())) + "");


                if (!TextUtils.isEmpty(mobNum.trim()) && spinnerAScontactCountry.getSelectedItemPosition() != 0) {
                    if (!countryCodeCheck(Integer.parseInt(countryCode.trim()), Integer.parseInt(mobNum.substring(0, countryCode.trim().length())))) {
                        mobNum = mobNum.substring(countryCode.trim().length(), mobNum.length());


                    }
                }

                //    Log.e("111 mob number is", mobNum);
                if (!TextUtils.isEmpty(landNum.trim()) && spinnerAScontactCountry.getSelectedItemPosition() != 0) {
                    if (!countryCodeCheck(Integer.parseInt(countryCode.trim()), Integer.parseInt(landNum.substring(0, countryCode.trim().length())))) {
                        landNum = landNum.substring(countryCode.trim().length(), landNum.length());

                    }
                }
                //     Log.e("111 land number is", landNum);


              /*  tvMobile.setText(member.getPhone_mobile());
                tvCountry.setText(member.getCountry_name());
                tvLandline.setText(member.getPhone_home());
                tvNameOfContactPerson.setText(member.getPersonal_name());
                tvRelationshipWithMember.setText(member.getProfile_owner());
                tvConvenientCallTime.setText(member.getNotes());*/
                View focusView = null;

                if (spinnerAScontactCountry.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Select Country", Toast.LENGTH_SHORT).show();


                } else if (TextUtils.isEmpty(mobNum.trim())) {
                    EditTextAScontactMobileNumber.setError("Please Enter Mobile Number");
                    focusView = EditTextAScontactMobileNumber;

                    focusView.requestFocus();
                }
                /*else if (!countryCodeCheck(Integer.parseInt(countryCode.trim()), Integer.parseInt(mobNum.substring(0, countryCode.length() - 1)))) {
                    EditTextAScontactMobileNumber.setError("Invalid Mobile Number");
                    focusView = EditTextAScontactMobileNumber;
                    focusView.requestFocus();

                } else if (!TextUtils.isEmpty(landNum.trim()) && !countryCodeCheck(Integer.parseInt(countryCode.trim()), Integer.parseInt(landNum.substring(0, countryCode.length() - 1)))) {

                    //     if (!countryCodeCheck(Integer.parseInt(countryCode.trim()), Integer.parseInt(landNum.substring(0, countryCode.length()-1)))) {
                    EditTextAScontactLandlineNumber.setError("Invalid Landline Number");
                    focusView = EditTextAScontactLandlineNumber;
                    focusView.requestFocus();

                    //  }


                } */


                else if (!isPhone(mobNum)) {
                    EditTextAScontactMobileNumber.setError("Invalid phone format");
                    focusView = EditTextAScontactMobileNumber;
                    focusView.requestFocus();
                } else if (mobNum.length() > 11) {
                    EditTextAScontactMobileNumber.setError("Max 11 Chars allowed");
                    focusView = EditTextAScontactMobileNumber;
                    focusView.requestFocus();
                } else if (landNum.length() > 11) {
                    EditTextAScontactLandlineNumber.setError("Max 11 Chars allowed");
                    focusView = EditTextAScontactLandlineNumber;
                    focusView.requestFocus();
                }

                /* else if (!TextUtils.isEmpty(landNum)) {
                 *//* EditTextAScontactLandlineNumber.setError("Please Enter Old Passwor");
                    focusView = EditTextAScontactLandlineNumber;
                    focusView.requestFocus();*//*
                    if (!isPhone(landNum)) {
                        EditTextAScontactLandlineNumber.setError("Invalid landline format");
                        focusView = EditTextAScontactLandlineNumber;
                        focusView.requestFocus();
                    }
                }*/
                else if (TextUtils.isEmpty(personName.trim())) {
                    EditTextAScontactPersonName.setError("Please Enter Person Name");
                    focusView = EditTextAScontactPersonName;

                    focusView.requestFocus();
                } else if (!isPersonalNameValid(personName)) {
                    EditTextAScontactPersonName.setError("Invalid personal name format");
                    focusView = EditTextAScontactPersonName;
                    focusView.requestFocus();
                } else if (spinnerAScontactRelationShipWithMember.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Select Relationship with Member", Toast.LENGTH_SHORT).show();


                } else if (spinnerMyAScontactConvTimeToCall.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Select Convenient Time to Call", Toast.LENGTH_SHORT).show();


                } else {

                    if (!TextUtils.isEmpty(landNum)) {

                        if (!isPhone(landNum)) {
                            EditTextAScontactLandlineNumber.setError("Invalid landline format");
                            focusView = EditTextAScontactLandlineNumber;
                            focusView.requestFocus();

                        } else {
                            landlineAdded = true;
                        }


                    }


                    Members member = new Members();
                    member.setPath(SharedPreferenceManager.getUserObject(getContext()).getPath());
                    WebCSC selectedCountry = (WebCSC) spinnerAScontactCountry.getSelectedItem();

                    if (selectedCountry != null) {
                        member.setCountry_id(Long.parseLong(selectedCountry.getId()));
                    }

                    WebArd selectedRelationShip = (WebArd) spinnerAScontactRelationShipWithMember.getSelectedItem();
                    member.setProfile_owner_id(Long.parseLong(selectedRelationShip.getId()));
                    WebArd selectedTimeToCall = (WebArd) spinnerMyAScontactConvTimeToCall.getSelectedItem();
                    member.setNotes(selectedTimeToCall.getName());

                    // String mobnum = EditTextAScontactMobileNumber.getText().toString();


                    //Log.e("New Updated Time", "=" + mobNum);

                    mobNum = mobNum.replaceAll("[/\\D/g]", "");

                    member.setPhone_mobile(mobNum);
                    //  String landline = EditTextAScontactLandlineNumber.getText().toString();

                    member.setPhone_home(landNum);
                    String PersonalName = EditTextAScontactPersonName.getText().toString();
                    member.setPersonal_name(PersonalName);

                    Gson gson = new Gson();

                    //  Log.e("Loggg", gson.toJson(member));
                    try {

                        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
                            putRequest(new JSONObject(gson.toJson(member)), landlineAdded);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            getRequest(SharedPreferenceManager.getUserObject(getContext()).getPath());
        }
        snackbarNotVerified = Snackbar.make(getActivity().findViewById(android.R.id.content), snackBarToolTip, Snackbar.LENGTH_SHORT);
        snackbarNotVerifiedLandLine = Snackbar.make(getActivity().findViewById(android.R.id.content), snackBarToolTip, Snackbar.LENGTH_SHORT);

/* email
        etAsEmail.setText(SharedPreferenceManager.getUserObject(getContext()).getEmail());
        etAsEmail.setEnabled(false);*/

    /*    if (SharedPreferenceManager.getUserObject(getContext()).getMember_status() < 3 || SharedPreferenceManager.getUserObject(getContext()).getMember_status() >= 7) {
            checkEmailStatus(getContext());
            llASEmail.setVisibility(View.VISIBLE);
        } else {

            llASEmail.setVisibility(View.GONE);
        }*/

    }

/*    public void checkEmailStatus(final Context context) {

        // pDialog.show();
        Log.e("status URL", Urls.getProfileCompletion + SharedPreferenceManager.getUserObject(context).getPath());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getProfileCompletion + SharedPreferenceManager.getUserObject(context).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());


                        try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);
                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<Dashboards>() {
                            }.getType();

                            Dashboards dashboards = (Dashboards) gsonc.fromJson(jsonCountryStaeObj.getJSONObject(0).toString(), listType);


                            boolean pCOmpleteStatus = true;
                            Members members = SharedPreferenceManager.getUserObject(context);
                            //   if (dashboards.getAdmin_approved_status().equals("1") && members.getMember_status() < 3) {

                            if (dashboards.getEmail_complete_status().equals("1")) {
                                //hide update email
                                etAsEmail.setKeyListener(null);
                                etAsEmail.setEnabled(false);
                                llASEmail.setVisibility(View.GONE);

                            } else {
                                //show
                                llASEmail.setVisibility(View.VISIBLE);

                            }

                                *//*else if (members.getMember_status() == 2) {
                                    if (dashboards.getEmail_complete_status().equals("1")  && dashboards.getProfile_complete_status().equals("100")) {
                                        members.setMember_status(3);
                                        SharedPreferenceManager.setUserObject(context,members);
                                    }

                                }*//*
                            //  }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //pDialog.dismiss();

                        }

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

        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(req, Tag);
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private boolean countryCodeCheck(int countryCode, int mobNum) {

        //Log.e("countryCodeCheck", countryCode + "   " + mobNum);

        if (countryCode == mobNum) {
            return false;
        } else {
            return true;
        }

    }

    private void setEditValues(Members member) {
        //  spinnerAScontactCountry, EditTextAScontactMobileNumber, EditTextAScontactLandlineNumber, EditTextAScontactPersonName,
        // spinnerAScontactRelationShipWithMember , spinnerMyAScontactConvTimeToCall

        LinearlayoutAccountSettingMyContact.setVisibility(View.GONE);
        LinearlayoutAccountSettingMyContactNoData.setVisibility(View.VISIBLE);
        btCancel.setVisibility(View.VISIBLE);
        ViewGenerator viewGenerator = new ViewGenerator(getContext());
        viewGenerator.selectSpinnerItemByIdWebCSC(spinnerAScontactCountry, member.getCountry_id(), countryOfLivingDataList);
        viewGenerator.selectSpinnerItemById(spinnerAScontactRelationShipWithMember, member.getProfile_owner_id(), relationshipDataList);


        //Log.e("Evening aaaaa", "" + member.getNotes() + "===");

        viewGenerator.selectSpinnerItemByValue(spinnerMyAScontactConvTimeToCall, member.getNotes(), covenientTimetoCallDataList);

        if (member.getNotes().equals("Morning")) {
            spinnerMyAScontactConvTimeToCall.setSelection(1);
        } else if (member.getNotes().equals("Afternoon")) {
            spinnerMyAScontactConvTimeToCall.setSelection(2);
        } else if (member.getNotes().equals("Evening")) {
            spinnerMyAScontactConvTimeToCall.setSelection(3);

        } else {
            spinnerMyAScontactConvTimeToCall.setSelection(0);
        }

        //Log.e("getPhone_mobile", "" + member.getPhone_mobile());

        //adapter_countryOfLiving
        String mobile = member.getPhone_mobile();

        if (mobile.contains("-")) {
            String[] sad = mobile.split("-");
            if (sad.length > 0) {
                EditTextAScontactMobileNumber.setTag("+" + sad[0] + " ");
                EditTextAScontactMobileNumber.setText(sad[1]);
            } else {
                EditTextAScontactMobileNumber.setText(member.getPhone_mobile());

            }
        } else {
            EditTextAScontactMobileNumber.setText(mobile);
        }


        String landline = member.getPhone_home();
        if (!landline.equals("")) {

            if (landline.contains("-")) {
                String[] sad1 = landline.split("-");
                if (sad1.length > 0) {
                    EditTextAScontactLandlineNumber.setTag("+" + sad1[0] + " ");
                    EditTextAScontactLandlineNumber.setText(sad1[1]);
                } else {
                    EditTextAScontactLandlineNumber.setText(landline);

                }
            } else {
                EditTextAScontactLandlineNumber.setText(landline);
            }


        }
        //  tvCountry.setText(member.getCountry_name());
        //    EditTextAScontactLandlineNumber.setText(member.getPhone_home());


        EditTextAScontactPersonName.setText(member.getPersonal_name());
        // tvRelationshipWithMember.setText(member.getProfile_owner());
        //   tvConvenientCallTime.setText(member.getNotes());
    }

    private void resetValues() {
        //  spinnerAScontactCountry, EditTextAScontactMobileNumber, EditTextAScontactLandlineNumber, EditTextAScontactPersonName,
        // spinnerAScontactRelationShipWithMember , spinnerMyAScontactConvTimeToCall


        EditTextAScontactMobileNumber.setText("");
        //  tvCountry.setText(member.getCountry_name());
        EditTextAScontactLandlineNumber.setText("");
        EditTextAScontactPersonName.setText("");
        spinnerAScontactCountry.setSelection(0);
        spinnerAScontactRelationShipWithMember.setSelection(0);
        spinnerMyAScontactConvTimeToCall.setSelection(0);
        // tvRelationshipWithMember.setText(member.getProfile_owner());
        //   tvConvenientCallTime.setText(member.getNotes());
    }

    private void setListeners() {

        spinnerAScontactCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WebCSC ard = (WebCSC) view.getTag();
                EditTextAScontactMobileNumber.setTag("+" + ard.getCid() + " ");
                EditTextAScontactMobileNumber.setText(EditTextAScontactMobileNumber.getText());
                EditTextAScontactLandlineNumber.setTag("+" + ard.getCid() + " ");
                EditTextAScontactLandlineNumber.setText(EditTextAScontactLandlineNumber.getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

      /*
       Email
       btEmailCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAsEmail.setText(SharedPreferenceManager.getUserObject(getContext()).getEmail());
                etAsEmail.setEnabled(false);
                etAsEmail.setError(null);
                emailEnabled = false;
                btResendVerificationEmail.setVisibility(View.VISIBLE);
                btEmailCancel.setVisibility(View.GONE);
                btUpdateEmail.setText("Update");
            }
        });
        btUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailEnabled) {

                    if (!isEmailValid(etAsEmail.getText().toString())) {
                        etAsEmail.setError(getString(R.string.error_invalid_email));

                    } else {

                        etAsEmail.setEnabled(false);
                        emailEnabled = false;
                        btResendVerificationEmail.setVisibility(View.VISIBLE);
                        btEmailCancel.setVisibility(View.GONE);
                        btUpdateEmail.setText("Update Email");
                        updateEmail();
                    }
                } else {
                    //edit enabling
                    etAsEmail.setEnabled(true);
                    emailEnabled = true;
                    btUpdateEmail.setText("Save");

                    btResendVerificationEmail.setVisibility(View.GONE);

                    btEmailCancel.setVisibility(View.VISIBLE);

                }


            }
        });
        btResendVerificationEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Verify Your Email", "Here is your email address that needs to be verified.<br /> <b>  <font color=#216917>" + SharedPreferenceManager.getUserObject(getContext()).getEmail() + "</font></b><br /> " +
                        "Please verify your email by using the link, we had emailed you.<br /> <font color=#9a0606> (In case you didn't receive any email,  please check your spam/junk folder or click \"Resend Verification Email\" )</font> ", "Resend Verification Email", 22);
                dialogP.show(getFragmentManager(), "d");
            }
        });
*/
        btAlreadyHaveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (member != null) {
                    dialogVerifyphone newFragment = dialogVerifyphone.newInstance(member.getPhone_mobile(), country_id, false);
                    newFragment.setTargetFragment(MyContactFragment.this, 3);
                    newFragment.show(getFragmentManager(), "dialog");
                } else {
                    Toast.makeText(getContext(), "Click Again", Toast.LENGTH_SHORT).show();
                }
            }

        });
  /*      llPhoneVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        llPhoneNotVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                snackbarNotVerified.show();
            }
        });
        llPhoneVerifyLandline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbarNotVerifiedLandLine.show();
            }
        });


        llVerifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SharedPreferenceManager.getUserObject(getContext()).getMember_status() == 0 || SharedPreferenceManager.getUserObject(getContext()).getMember_status() >= 7) {
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(getContext()).getAlias() + "</font></b>, please complete your profile first then we can send you verification code on your Mobile number.", "Complete Profile", 8);
                    dialogP.show(getFragmentManager(), "d");

                } else {

                    getValidCode(SharedPreferenceManager.getUserObject(getContext()).getPath());


                }
            }


        });


        LinearlayoutAccountSettingMCEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(), "clicked ed", Toast.LENGTH_SHORT).show();

                if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
                    if (member != null) {


                        setEditValues(member);
                    }
                }

            }
        });
        LinearlayoutAccountSettingMCDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (about_type_id != 0) {


                    new AlertDialog.Builder(getActivity())
                            .setTitle("Confirm")
                            .setMessage("Do you really want to delete contact details?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //      Toast.makeText(getActivity(), "Yaay", Toast.LENGTH_SHORT).show();

                                    if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
                                        Members member = new Members();
                                        member.setRequest_id(about_type_id);

                                        member.setPath(SharedPreferenceManager.getUserObject(context).getPath());

                                        Gson gson = new Gson();


                                        try {
                                            putDeleteContactRequest(new JSONObject(gson.toJson(member)));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();


                }
            }
        });


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearlayoutAccountSettingMyContact.setVisibility(View.VISIBLE);
                LinearlayoutAccountSettingMyContactNoData.setVisibility(View.GONE);
            }
        });


    }

/*    private boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }*/

    private boolean isPersonalNameValid(String name) {
        Pattern pattern;
        Matcher matcher;
        String NAME_PATTERN = "^([a-zA-Z ]){3,30}$";
        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(name);
        return matcher.matches();

    }

    private boolean isPhone(String phone) {
        Pattern pattern;
        Matcher matcher;
        String Phone_PATTERN = "^(?:(?:\\(?(?:00|\\+)([1-4]\\d\\d|[1-9]\\d?)\\)?)?[\\-\\.\\ \\\\\\/]?)?((?:\\(?\\d{1,}\\)?[\\-\\.\\ \\\\\\/]?){0,})(?:[\\-\\.\\ \\\\\\/]?(?:#|ext\\.?|extension|x)[\\-\\.\\ \\\\\\/]?(\\d+))?$";
        pattern = Pattern.compile(Phone_PATTERN);
        matcher = pattern.matcher(phone);
        return matcher.matches();

    }

    private void getRequest(final String path) {
        pDialog.setVisibility(View.VISIBLE);

        //Log.e("path", "" + Urls.getProfileContact + path);
        JsonArrayRequest req = new JsonArrayRequest(Urls.getProfileContact + path,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("getProfileContact", response.toString());
                        try {


                            JSONArray jsonData = response.getJSONArray(0);


                            //Log.e(" jsonData ", "" + jsonData.length());
                            llPhoneNotVerified.setVisibility(View.GONE);


                            if (jsonData.length() != 0) {


                                //Log.d("length 0", jsonData.length() + "");
                                Gson gsonc;
                                GsonBuilder gsonBuilderc = new GsonBuilder();
                                gsonc = gsonBuilderc.create();
                                Type listType = new TypeToken<Members>() {
                                }.getType();
                                member = new Members();
                                member = (Members) gsonc.fromJson(jsonData.getJSONObject(0).toString(), listType);
                                //Log.e("Phone Verified " + member.getPhone_view(), "" + member.getPhone_verified());


                                //      phone_verified!=2 && phone_view!=2  // show

                                if (member.getPhone_verified() != 2 && member.getPhone_view() != 2) {

                                    //edit and delte buttons visible
                                    LinearlayoutAccountSettingMyContactEdiDelete.setVisibility(View.VISIBLE);
                                }


                               /* if (member.getPhone_verified() == 2 ) {
                                    LinearlayoutAccountSettingMyContactEdiDelete.setVisibility(View.GONE);
                                    //edit and delete buttons hide
                                }
*/


                                setValues(member);


                                //Gson gsonl;
                                //   GsonBuilder gsonBuilderl = new GsonBuilder();
                                // gsonc = gsonBuilderc.create();
                                Type listTyplCSC = new TypeToken<List<WebCSC>>() {
                                }.getType();
                                Type listTypl = new TypeToken<List<WebArd>>() {
                                }.getType();

                                //Log.e("response issss", "" + response.getJSONArray(2).toString());
                                countryOfLivingDataList = (List<WebCSC>) gsonc.fromJson(response.getJSONArray(2).toString(), listTyplCSC);
                                countryOfLivingDataList.add(0, new WebCSC("0", "0", "0", "Select"));
                                adapter_countryOfLiving.updateDataList(countryOfLivingDataList);


                                relationshipDataList = (List<WebArd>) gsonc.fromJson(response.getJSONArray(1).toString(), listTypl);
                                relationshipDataList.add(0, new WebArd("0", "Select"));
                                adapter_relationship.updateDataList(relationshipDataList);


                            } else {


                                resetValues();
                                LinearlayoutAccountSettingMyContactEdiDelete.setVisibility(View.INVISIBLE);
                                LinearlayoutAccountSettingMyContactNoData.setVisibility(View.VISIBLE);
                                LinearlayoutAccountSettingMyContact.setVisibility(View.GONE);
                                //Log.e("array length", "" + response.length() + "");

                                Gson gsonc;
                                GsonBuilder gsonBuilderc = new GsonBuilder();
                                gsonc = gsonBuilderc.create();
                                Type listType = new TypeToken<List<WebArd>>() {
                                }.getType();
                                Type listTypeCSC = new TypeToken<List<WebCSC>>() {
                                }.getType();

                         /*   Members mem = (Members) gsonc.fromJson(jsonCountryStaeObj.get(0).toString(), listType);
                            //  setValues(mem);*/

                        /*    Gson gson;
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gson = gsonBuilderc.create();
                            Type listTyp = new TypeToken<List<WebArd>>() {
                            }.getType();*/


                                countryOfLivingDataList = (List<WebCSC>) gsonc.fromJson(response.getJSONArray(2).toString(), listTypeCSC);
                                countryOfLivingDataList.add(0, new WebCSC("0", " ", "0", "Select"));
                                adapter_countryOfLiving.updateDataList(countryOfLivingDataList);

                      /*      Gson gsoncsc;
                                                      GsonBuilder gsonBuilder = new GsonBuilder();
                            gson = gsonBuilderc.create();
                            Type listTyp = new TypeToken<List<WebArd>>() {
                            }.getType();
*/

                                relationshipDataList = (List<WebArd>) gsonc.fromJson(response.getJSONArray(1).toString(), listType);
                                relationshipDataList.add(0, new WebArd("0", "Select"));
                                adapter_relationship.updateDataList(relationshipDataList);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefresh.setRefreshing(false);
                        }

                        swipeRefresh.setRefreshing(false);
                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(req, Tag);
    }


    private void setValues(Members member) {
        tvLandlineCode.setText("  ");
        LinearlayoutAccountSettingMyContact.setVisibility(View.VISIBLE);
        LinearlayoutAccountSettingMyContactNoData.setVisibility(View.GONE);

        country_id = member.getCountry_id() + "";

        String mobile = member.getPhone_mobile();
        //Log.e("mob", "mob==" + mobile);
        // if()
        if (!mobile.equals("") && mobile.contains("-")) {
            String[] sad = mobile.split("-");
            if (sad.length > 0) {

                tvMobile.setText(sad[1]);
                tvMobileCode.setText(sad[0] + "-");
            } else {
                tvMobile.setText(mobile);

            }
        } else {
            tvMobile.setText(mobile);

        }

        tvCountry.setText(member.getCountry_name());


        String landline = member.getPhone_home();
        if (!landline.equals("")) {
            String[] sad1 = landline.split("-");
            if (sad1.length > 1) {

                tvLandline.setText(sad1[1]);
                tvLandlineCode.setText(sad1[0] + "-");
            } else {
                tvLandline.setText(landline);

            }
        } else {
            tvLandlineCode.setText("  ");
            tvLandline.setText("");
        }

        llPhoneVerifyLandline.setVisibility(View.GONE);
     /*   LinearLayout llPhoneVerifyLandline;
        AppCompatTextView tvPhoneVerifyLandline;
        ImageView ivPhoneVerifyLandline;
        */


        if (SharedPreferenceManager.getUserObject(context).getMember_status() < 3 || SharedPreferenceManager.getUserObject(context).getMember_status() >= 7) {


            if (member.getPhone_view() == 1) {
                if (!landline.equals("")) {

                    llPhoneVerifyLandline.setVisibility(View.VISIBLE);
                    tvPhoneVerifyLandline.setText("Not Verified");
                    ivPhoneVerifyLandline.setImageDrawable(getResources().getDrawable(R.drawable.no_number_icon_60));

                    snackBarToolTipLandLine = "Verification Pending - MarryMax Support will call to verify";
                    TextView tvSnackbarText = snackbarNotVerifiedLandLine.getView().findViewById(android.support.design.R.id.snackbar_text);
                    tvSnackbarText.setText(snackBarToolTipLandLine);
                    llPhoneVerifyLandline.setClickable(true);
                }
                //pending


            } else if (member.getPhone_view() == 2) {
                //verified
                llPhoneVerifyLandline.setVisibility(View.VISIBLE);
                tvPhoneVerifyLandline.setText("Verified");
                ivPhoneVerifyLandline.setImageDrawable(getResources().getDrawable(R.drawable.ic_num_verified_icon_60));
                llPhoneVerifyLandline.setClickable(false);


            } else if (member.getPhone_view() == 3) {

                if (!landline.equals("")) {


                    llPhoneVerifyLandline.setVisibility(View.VISIBLE);
                    tvPhoneVerifyLandline.setText("Not Verified");
                    ivPhoneVerifyLandline.setImageDrawable(getResources().getDrawable(R.drawable.no_number_icon_60));

                    snackBarToolTipLandLine = "Unable to verify. Please contact MarryMax support.";
                    TextView tvSnackbarText = snackbarNotVerifiedLandLine.getView().findViewById(android.support.design.R.id.snackbar_text);
                    tvSnackbarText.setText(snackBarToolTipLandLine);
                    llPhoneVerifyLandline.setClickable(true);
                }
                //not verified

            }

        } else {


            if (member.getPhone_view() == 2) {

                //verified
                llPhoneVerifyLandline.setVisibility(View.VISIBLE);
                tvPhoneVerifyLandline.setText("Verified");
                ivPhoneVerifyLandline.setImageDrawable(getResources().getDrawable(R.drawable.ic_num_verified_icon_60));
                llPhoneVerifyLandline.setClickable(false);

            } else {
                if (!landline.equals("")) {

                    llPhoneVerifyLandline.setVisibility(View.VISIBLE);
                    tvPhoneVerifyLandline.setText("Not Verified");
                    ivPhoneVerifyLandline.setImageDrawable(getResources().getDrawable(R.drawable.no_number_icon_60));

                    snackBarToolTipLandLine = "Unable to verify. Please contact MarryMax support.";
                    TextView tvSnackbarText = snackbarNotVerifiedLandLine.getView().findViewById(android.support.design.R.id.snackbar_text);
                    tvSnackbarText.setText(snackBarToolTipLandLine);
                    llPhoneVerifyLandline.setClickable(true);
                }
                //not verified
            }
        }


        //  tvLandline.setText(member.getPhone_home());


        tvNameOfContactPerson.setText(member.getPersonal_name());
        tvRelationshipWithMember.setText(member.getProfile_owner());
        tvConvenientCallTime.setText(member.getNotes());
        about_type_id = member.getAbout_type_id();


        if (SharedPreferenceManager.getUserObject(context).getMember_status() < 3 || SharedPreferenceManager.getUserObject(context).getMember_status() >= 7) {


            if (member.getPhone_verified() == 3) {
                //message to show
                // Unable to verify. Please contact MarryMax support.

                //phone not verified
                snackBarToolTip = "Unable to verify. Please contact MarryMax support.";
                TextView tvSnackbarText = snackbarNotVerified.getView().findViewById(android.support.design.R.id.snackbar_text);
                tvSnackbarText.setText(snackBarToolTip);


                llPhoneNotVerified.setVisibility(View.VISIBLE);
                llEnterCode.setVisibility(View.GONE);
                llPhoneVerified.setVisibility(View.GONE);
                llVerifyPhone.setVisibility(View.GONE);
            }
            if (member.getPhone_verified() == 2) {
                //verified
                llPhoneVerified.setVisibility(View.VISIBLE);
                llEnterCode.setVisibility(View.GONE);
                llPhoneNotVerified.setVisibility(View.GONE);
                llVerifyPhone.setVisibility(View.GONE);

                LinearlayoutAccountSettingMyContactEdiDelete.setVisibility(View.INVISIBLE);
            }
            if (member.getPhone_verified() == 1) {

                if (member.getAccept_message() == 1) {

//verify now
                    llVerifyPhone.setVisibility(View.VISIBLE);
                    llEnterCode.setVisibility(View.VISIBLE);
                    llPhoneNotVerified.setVisibility(View.GONE);
                    llPhoneVerified.setVisibility(View.GONE);
                } else {
                    //message show
                    // Mobile verification is pending. Please contact MarryMax support.
                    snackBarToolTip = "Mobile verification is pending. Please contact MarryMax support.";
                    TextView tvSnackbarText = snackbarNotVerified.getView().findViewById(android.support.design.R.id.snackbar_text);
                    tvSnackbarText.setText(snackBarToolTip);


                    llPhoneVerified.setVisibility(View.VISIBLE);
                    llEnterCode.setVisibility(View.GONE);
                    llPhoneNotVerified.setVisibility(View.GONE);
                    llVerifyPhone.setVisibility(View.GONE);
                }

                //blue icon


            }

        } else {
            if (member.getPhone_verified() == 2) {
                //verified
                llPhoneVerified.setVisibility(View.VISIBLE);
                llEnterCode.setVisibility(View.GONE);
                llPhoneNotVerified.setVisibility(View.GONE);
                llVerifyPhone.setVisibility(View.GONE);
            } else {
                snackBarToolTip = "Unable to verify. Please contact MarryMax support.";
                TextView tvSnackbarText = snackbarNotVerified.getView().findViewById(android.support.design.R.id.snackbar_text);
                tvSnackbarText.setText(snackBarToolTip);


                llPhoneNotVerified.setVisibility(View.VISIBLE);
                llEnterCode.setVisibility(View.GONE);
                llPhoneVerified.setVisibility(View.GONE);
                llVerifyPhone.setVisibility(View.GONE);
            }


        }
        //Log.e("getPhone_home", member.getPhone_verified() + "");
    }


    private void putRequest(JSONObject params, final boolean landlineAdded) {

        pDialog.setVisibility(View.VISIBLE);
        //Log.e("params", params.toString());
        //Log.e("request url ", Urls.editContact);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.editContact, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Response ", response.toString() + "");

                        //phone_verified   ==> 1 mobile was add/update, 0 if no add/update,  -1 if no add, update and not verified
                        //landline_verified   ==> 1 landline was add/update, 0 if no add/update,  -1 if no update , update and not verified

                        try {

                            int phone_verified = response.getInt("phone_verified");
                            int landline_verified = response.getInt("landline_verified");

                            //    Log.e("aphone_verified", "" + phone_verified);
                            //    Log.e("alandline_verified", "" + landline_verified);

                            int res = response.getInt("about_type_id");

                            if (res != 0) {

                                dismissKeyboard();
                                if (phone_verified == 0 || phone_verified == -1) {
                                    Toast.makeText(context, "Unable to Add Mobile Number. Please contact MarryMax support", Toast.LENGTH_SHORT).show();
                                }
                                if (landlineAdded) {
                                    if (landline_verified == 0 || landline_verified == -1) {
                                        Toast.makeText(context, "Unable to Add Landline Number. Please contact MarryMax support", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                getRequest(SharedPreferenceManager.getUserObject(getContext()).getPath());

                            } else {
                                Toast.makeText(context, "Unable to add phone number. Please contact MarryMax support", Toast.LENGTH_SHORT).show();
                            }
                         /*   Gson gson;
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            Type type = new TypeToken<Members>() {
                            }.getType();
                            Members member2 = (Members) gson.fromJson(responseObject.toString(), type);
                            //  Log.e("interested id", "" + member.getAlias() + "====================");

                            dialogShowInterest newFragment = dialogShowInterest.newInstance(member, member.getUserpath(), replyCheck, member2);
                            newFragment.show(frgMngr, "dialog");*/


                        } catch (JSONException e) {
                            pDialog.setVisibility(View.GONE);
                            e.printStackTrace();
                        }


                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                pDialog.setVisibility(View.GONE);
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, Tag);
    }


    private void putDeleteContactRequest(JSONObject params) {

        pDialog.setVisibility(View.VISIBLE);
        //Log.e("params", params.toString());
        //Log.e("request url ", Urls.deleteContact);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.deleteContact, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Response ", response.toString() + "");

                        //try {
                        //  int res = response.getInt("about_type_id");

                        //if(res!=0){

                        Toast.makeText(context, "Contact Details Updated", Toast.LENGTH_SHORT).show();
                        getRequest(SharedPreferenceManager.getUserObject(context).getPath());
                        // }
                         /*   Gson gson;
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            Type type = new TypeToken<Members>() {
                            }.getType();
                            Members member2 = (Members) gson.fromJson(responseObject.toString(), type);
                            //  Log.e("interested id", "" + member.getAlias() + "====================");

                            dialogShowInterest newFragment = dialogShowInterest.newInstance(member, member.getUserpath(), replyCheck, member2);
                            newFragment.show(frgMngr, "dialog");*/


                  /*      } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }*/


                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                pDialog.setVisibility(View.GONE);
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, Tag);
    }


    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        if (null != getActivity().getCurrentFocus())
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }


    /*    private void updateEmail() {

            pDialog.setVisibility(View.VISIBLE);
            //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

            JSONObject params = new JSONObject();
            try {


                params.put("name", "" + etAsEmail.getText().toString());
                params.put("path", SharedPreferenceManager.getUserObject(getContext()).getPath());


            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("updateEmail", Urls.updateEmail + " == " + params);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                    Urls.updateEmail, params,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("re  update appearance", response + "");


                            Gson gson;
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            Type type = new TypeToken<WebArd>() {
                            }.getType();
                            WebArd webArd = (WebArd) gson.fromJson(response.toString(), type);
                            if (webArd.getId().equals("0")) {
                                Toast.makeText(getContext(), "Email Not updated", Toast.LENGTH_SHORT).show();

                            } else if (webArd.getId().equals("1")) {
                                Toast.makeText(getContext(), "Email Updated", Toast.LENGTH_SHORT).show();

                            } else if (webArd.getId().equals("2")) {
                                Toast.makeText(getContext(), "Not a valid Email", Toast.LENGTH_SHORT).show();

                            } else if (webArd.getId().equals("3")) {
                                Toast.makeText(getContext(), "Email  Exists", Toast.LENGTH_SHORT).show();

                            }


                            pDialog.setVisibility(View.GONE);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {


                    VolleyLog.e("res err", "Error: " + error);
                    // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                    pDialog.setVisibility(View.GONE);
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
            MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, Tag);

        }*/
    private void getValidCode(final String path) {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        //Log.e("path", "" + Urls.getValidCode + path);
        StringRequest req = new StringRequest(Urls.getValidCode + path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                   //   Log.e("getValidCode", response.toString() + "==");
                        if (response != null) {

                            if (Long.parseLong(response) == 0) {
                                dialogVerifyphone newFragment = dialogVerifyphone.newInstance(member.getPhone_mobile(), country_id, false);
                                newFragment.setTargetFragment(MyContactFragment.this, 3);
                                newFragment.show(getFragmentManager(), "dialog");

                            } else if (Long.parseLong(response) == 1) {

                                dialogVerifyphone newFragment = dialogVerifyphone.newInstance(member.getPhone_mobile(), country_id, true);
                                newFragment.setTargetFragment(MyContactFragment.this, 3);
                                newFragment.show(getFragmentManager(), "dialog");
                            } else if (Long.parseLong(response) == 2) {
                                //Dear "alias", We got the contact details, MarryMax Associate will contact you to verify details within one business day.
                                Toast.makeText(context, "Dear " + SharedPreferenceManager.getUserObject(context).getAlias() + ", We got the contact details, MarryMax Associate will contact you to verify details within one business day.", Toast.LENGTH_SHORT).show();
                                //      "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(getContext()).getAlias() + "</font></b>, please complete your profile first then we can send you verification code on your Mobile number.", "Complete Profile", 8);


                            }
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


        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(req);

    }

    @Override
    public void onComplete(String s) {
        getRequest(SharedPreferenceManager.getUserObject(getContext()).getPath());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        MySingleton.getInstance(getContext()).cancelPendingRequests(Tag);
    }

    @Override
    public void onRefresh() {
        if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
            getRequest(SharedPreferenceManager.getUserObject(getContext()).getPath());
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(PhoneVerificationStatusUpdateEvent event) {

        //  Log.e("event", "" + event.getMessage());
        if (event.getMessage().equals("success")) {
            if (ConnectCheck.isConnected(getActivity().findViewById(android.R.id.content))) {
                getRequest(SharedPreferenceManager.getUserObject(getContext()).getPath());
            }
        }

       /* if (event.getMessage().equals("filterCount")) {
            if (filterCount == 0) {
                getSupportActionBar().setTitle("Filter Results");
            } else {
                getSupportActionBar().setTitle("Filter Results (" + filterCount + ")");
            }
        } else if (event.getMessage().equals("getCount")) {
            String params;
            Members memberSearchObj = defaultSelectionsObj;
            if (bestMatchCheck) {
              *//*  Intent intent = new Intent(SearchMainActivity.this, SearchYourBestMatchResultsActivity.class);
                startActivity(intent);*//*
                if (memberSearchObj != null) {
                    memberSearchObj.setPage_no(1);
                    memberSearchObj.setType("");

                    Gson gson = new Gson();
                    params = gson.toJson(memberSearchObj);

                    loadCounter(params);

                }

            } else {
               *//* Intent intent = new Intent(SearchMainActivity.this, SearchResultsActivity.class);
                startActivity(intent);*//*

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
        }*/

        ///    Toast.makeText(this, "" + event.getMessage(), Toast.LENGTH_SHORT).show();
    }


}
