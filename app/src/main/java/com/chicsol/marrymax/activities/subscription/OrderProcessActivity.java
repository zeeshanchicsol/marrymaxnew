package com.chicsol.marrymax.activities.subscription;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.chicsol.marrymax.activities.registration.RegisterPersonalityActivity;
import com.chicsol.marrymax.adapters.MySpinnerAdapter;
import com.chicsol.marrymax.dialogs.dialogEnterPromoCode;
import com.chicsol.marrymax.dialogs.dialogSelectPackage;
import com.chicsol.marrymax.dialogs.dialogTermsConditions;
import com.chicsol.marrymax.modal.Subscription;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.modal.mPayments;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.ExpandOrCollapse;
import com.chicsol.marrymax.utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderProcessActivity extends AppCompatActivity implements dialogSelectPackage.onChangeSubscriptionPlanListener, dialogEnterPromoCode.onApplyPromoCodeListener {
    ImageButton btEditPackage;
    TextView tvPromoCode;
    Button btPayThroughCreditCard;
    private ProgressBar pBar;
    private ProgressDialog pDialog;
    private ArrayAdapter acAdapter;
    String ip = "";
    String item_id = "";
    String currency = "";
    String other_item_id = "";
    String procode_code = "", status = "";
    TextView tvName, tvAlias, tvEmail, tvCountry, tvShortDesc, tvTotalPrice, tvPrice, tvPromoCodeName, tvTermsConditions, tvPromoCodeDiscountAmount, tvPromoCodeDiscountName, tvPromoCodeDiscountPercent;
    private boolean isVisible = false;
    LinearLayout llPromoCodeDiscount, llWesterUnion, llEasyPasa, llJazzCash, llABL, llCash, llCC;
    RelativeLayout rlMain, rlDetailView;
    private ExpandOrCollapse mAnimationManager;
    //  private AutoCompleteTextView etWhoHelped;
    private RadioButton radioOrderProcessStripe;
    View lastview = null;
    CardInputWidget mCardInputWidget;

    RadioGroup rgOrderProcessMain;
    RadioButton radioButtonOffline = null;
    private boolean cc = false;
    private boolean creditCardCheck = false;
    Subscription subscription;
    //   JSONObject params;
    LinearLayout llMain, llErrorMessag;
    TextView tvErrorDescription, tvErrorHeading, tvo1, tvo2, tvo3, tvo4, tvOrderNumber, tvOrderNumberJazzCash;
    private Spinner spinner_religion;
    private List<WebArd> ReligionDataList;
    private MySpinnerAdapter adapter_religion;
    private RadioGroup rgAssisted;
    private LinearLayout llSpAssisted;

    private EditText etCVV, etCardNumber;
    // etExpiry
    private Spinner spMonth, spYear;

    private List<WebArd> yearDataList, monthDataList;

    private MySpinnerAdapter spAdapterMonth, spAdapterYear;

    private ArrayList<String> listOfPattern = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process);

        item_id = getIntent().getExtras().getString("item_id");
        other_item_id = getIntent().getExtras().getString("other_item_id");
        currency = getIntent().getExtras().getString("currency");
        //Log.e("currency", currency);
        if (currency.equals("u")) {
            creditCardCheck = true;
        }

        initialize();
        setListeners();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initialize() {
        getSupportActionBar().setTitle("Order Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //    mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);


        monthDataList = new ArrayList<>();
        yearDataList = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {


            if (i < 10) {

                String text = (i < 10 ? "0" : "") + i;
                WebArd webArd = new WebArd(String.valueOf(i), text + "");
                monthDataList.add(webArd);
            } else {
                WebArd webArd = new WebArd(String.valueOf(i), i + "");
                monthDataList.add(webArd);
            }
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        for (int i = year; i <= year + 10; i++) {

            WebArd webArd = new WebArd(String.valueOf(i), i + "");
            yearDataList.add(webArd);
        }

        spMonth = (Spinner) findViewById(R.id.SpinnerAppCardExpiryMonth);
        spYear = (Spinner) findViewById(R.id.SpinnerAppCardExpiryYear);


        monthDataList.add(0, new WebArd("-1", "Select"));
        yearDataList.add(0, new WebArd("-1", "Select"));
        spAdapterMonth = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, monthDataList);
        spAdapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(spAdapterMonth);

        spAdapterYear = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, yearDataList);
        spAdapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(spAdapterYear);


        //cc
        etCardNumber = (EditText) findViewById(R.id.EditTextCardNumber);
        etCVV = (EditText) findViewById(R.id.EditTextCVV);
        /*etExpiry = (EditText) findViewById(R.id.EditTextExpiration);
        etExpiry.addTextChangedListener(new AutoAddTextWatcher(etExpiry,
                "/",
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                },
                2));*/
        //end cc

        ReligionDataList = new ArrayList<>();

        rgAssisted = (RadioGroup) findViewById(R.id.RadioGroupOrderProcessAssited);

        RadioButton r = (RadioButton) rgAssisted.getChildAt(1);
        r.setChecked(true);


        llSpAssisted = (LinearLayout) findViewById(R.id.LinearLayoutOrderProcessSpinner);
        llSpAssisted.setVisibility(View.INVISIBLE);
        llMain = (LinearLayout) findViewById(R.id.LinearLayoutOrderProcessMain);
        llErrorMessag = (LinearLayout) findViewById(R.id.LinearLayoutOrderProcessError);

        tvErrorDescription = (TextView) findViewById(R.id.TextViewOrderProcessErrorDescription);
        tvErrorHeading = (TextView) findViewById(R.id.TextViewOrderProcessErrorHeading);


        rgOrderProcessMain = (RadioGroup) findViewById(R.id.RadioGroupOrderProcessMain);
        //   radioOrderProcessStripe = (RadioButton) findViewById(R.id.radioOrderProcessStripe);

        mAnimationManager = new ExpandOrCollapse();
        llPromoCodeDiscount = (LinearLayout) findViewById(R.id.LinearLayoutOrderProcessPromoCode);
        llWesterUnion = (LinearLayout) findViewById(R.id.LinearlayoutOrderProcessWDetailView);

        llCC = (LinearLayout) findViewById(R.id.LinearlayoutOrderProcessCCDetailView);

        llJazzCash = (LinearLayout) findViewById(R.id.LinearlayoutOrderProcessJazzCashView);
        llEasyPasa = (LinearLayout) findViewById(R.id.LinearlayoutOrderProcessEasypasaView);
        llABL = (LinearLayout) findViewById(R.id.LinearlayoutOrderProcessABL);
        llCash = (LinearLayout) findViewById(R.id.LinearlayoutOrderProcessCash);


        pBar = (ProgressBar) findViewById(R.id.ProgressbarSubscriptionPlan);
        btEditPackage = (ImageButton) findViewById(R.id.ImageButtonEditPlackage);
        tvPromoCode = (TextView) findViewById(R.id.TextViewOrderProcessPromocode);

        btPayThroughCreditCard = (Button) findViewById(R.id.ButtonPayThroughCreditCard);

        //   etWhoHelped = (AutoCompleteTextView) findViewById(R.id.EditTextOrderProcessWhoHelped);
        //   etWhoHelped.setThreshold(1);

        tvTermsConditions = (TextView) findViewById(R.id.TextViewTermsConditions);

        tvPromoCodeName = (TextView) findViewById(R.id.TextViewOrderProcessPromocodeName);
        tvPromoCodeDiscountAmount = (TextView) findViewById(R.id.TextViewOrderProcessPromoCodeDiscountAmount);
        tvPromoCodeDiscountName = (TextView) findViewById(R.id.TextViewOrderProcessPromoCodeDiscountName);
        tvPromoCodeDiscountPercent = (TextView) findViewById(R.id.TextViewOrderProcessPromocodeDiscountPercent);
        //tvPromoCodeName=(TextView) findViewById(R.id.TextViewOrderProcessName);


        tvName = (TextView) findViewById(R.id.TextViewOrderProcessName);

        tvAlias = (TextView) findViewById(R.id.TextViewOrderProcessAlias);
        tvEmail = (TextView) findViewById(R.id.TextViewOrderProcessEmail);
        tvCountry = (TextView) findViewById(R.id.TextViewOrderProcessCountry);
        tvShortDesc = (TextView) findViewById(R.id.TextViewOrderProcessShortDescription);
        tvTotalPrice = (TextView) findViewById(R.id.TextViewOrderProcessTotalPrice);
        tvPrice = (TextView) findViewById(R.id.TextViewOrderProcessPrice);

        tvOrderNumber = (TextView) findViewById(R.id.TextViewOrderProcessOrderNumber);


        tvOrderNumberJazzCash = (TextView) findViewById(R.id.OrderNumberJazzCash);

        tvo1 = (TextView) findViewById(R.id.OrderNumber1);
        tvo2 = (TextView) findViewById(R.id.OrderNumber2);
        tvo3 = (TextView) findViewById(R.id.OrderNumber3);
        tvo4 = (TextView) findViewById(R.id.OrderNumber4);


        spinner_religion = (Spinner) findViewById(R.id.sp_assisting_members);
        adapter_religion = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, ReligionDataList);
        adapter_religion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_religion.setAdapter(adapter_religion);


        getPersonsList();
        // getPaymentPending();

        ip = getIPAddress(true);





   /*     electron: /^(4026|417500|4405|4508|4844|4913|4917)\d+$/,
             maestro: /^(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\d+$/,
               dankort: /^(5019)\d+$/,
               interpayment: /^(636)\d+$/,
                unionpay: /^(62|88)\d+$/,
                visa: /^4[0-9]{12}(?:[0-9]{3})?$/,
                mastercard: /^5[1-5][0-9]{14}$/,
                amex: /^3[47][0-9]{13}$/,
                diners: /^3(?:0[0-5]|[68][0-9])[0-9]{11}$/,
                discover: /^6(?:011|5[0-9]{2})[0-9]{12}$/,
                jcb: /^(?:2131|1800|35\d{3})\d{11}$/*/


        String ptVisa = "^4[0-9]{12}(?:[0-9]{3})?$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{14}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{13}$";
        listOfPattern.add(ptAmeExp);

        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
        listOfPattern.add(ptDinClb);

        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{12}$";
        listOfPattern.add(ptDiscover);

        String ptJcb = "^(?:2131|1800|35\\d{3})\\d{11}$/*";
        listOfPattern.add(ptJcb);

        String ptElectron = "^(4026|417500|4405|4508|4844|4913|4917)\\d+$";
        listOfPattern.add(ptElectron);

        String ptMaestro = "^(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d+$";
        listOfPattern.add(ptMaestro);

        String ptDankort = "^(5019)\\d+$";
        listOfPattern.add(ptDankort);

        String ptInterpayment = "^(636)\\d+$";
        listOfPattern.add(ptInterpayment);

        String ptUnionpay = "^(62|88)\\d+$";
        listOfPattern.add(ptUnionpay);


        getPaymentPending();


        //creddd


    }


    private JSONObject getParams(boolean cccheck, boolean defaultt, boolean changeSubscriptionPlan) {

        JSONObject params = new JSONObject();
        try {


            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
            params.put("member_ip", ip);
            //    params.put("status", status);


            if (!defaultt) {


                if (cccheck || cc) {
                    if (currency.equals("p")) {
                        params.put("item_id", other_item_id);

                    } else {
                        params.put("item_id", item_id);
                    }
                } else {

                    if (currency.equals("u")) {
                        params.put("item_id", other_item_id);

                    } else {
                        params.put("item_id", item_id);
                    }

                }

                if (!cccheck) {
                    params.put("payment_method", "wu");
                } else {
                    params.put("payment_method", "cc");
                }

            } else if (changeSubscriptionPlan) {
                if (creditCardCheck) {
                    params.put("payment_method", "cc");
                } else {
                    params.put("payment_method", "wu");
                }
                params.put("item_id", item_id);

            } else {
                params.put("item_id", item_id);
                params.put("payment_method", "");
            }


            params.put("procode_code", procode_code);
            params.put("checkout_email_address", SharedPreferenceManager.getUserObject(getApplicationContext()).getEmail());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    private void setListeners() {

/*
        etCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("DEBUG", "afterTextChanged : "+s);
                String ccNum = s.toString();
                for(String p:listOfPattern){
                    if(ccNum.matches(p)){
                        Log.d("DEBUG", "afterTextChanged : discover");
                        break;
                    }
                }

            }
        });*/

        rgAssisted.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.RadioButtonOrderYes) {

                    llSpAssisted.setVisibility(View.VISIBLE);
                } else {

                    llSpAssisted.setVisibility(View.INVISIBLE);
                }
            }
        });

       /* radioOrderProcessStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderProcessActivity.this, ""+radioOrderProcessStripe.isChecked(), Toast.LENGTH_SHORT).show();
            }
        });
        radioOrderProcessStripe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("radio", isChecked + "");


                if (isChecked) {
                    buttonView.setChecked(true);

                    if (rgOrderProcessMain.getCheckedRadioButtonId() == -1) {
                        // no radio buttons are checked
                    } else {
                        // one of the radio buttons is checked

                        if (radioButtonOffline != null) {
                            if (radioButtonOffline.isChecked()) {
                                radioButtonOffline.setChecked(false);


                            }
                        }
                    }
                    collapaseView();
                    mAnimationManager.expand1(llCC);


                    lastview = llCC;
                }

                else
                {
                    buttonView.setChecked(false);
                }
                *//*else {

                    buttonView.setChecked(true);
                }*//*

                //    buttonView.setChecked(true);

            }
        });*/


        tvTermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rgOrderProcessMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

    /*         if (radioOrderProcessStripe.isChecked()) {
                    radioOrderProcessStripe.setChecked(false);

                }*/

                int selectedId = group.getCheckedRadioButtonId();
                radioButtonOffline = (RadioButton) findViewById(selectedId);


                switch (checkedId) {
                    case R.id.RadioButtonOrderProcessWesternUnion:
                        // listItem.setAnswerID(1);
                        // Toast.makeText(OrderProcessActivity.this, "Wester", Toast.LENGTH_SHORT).show();

                        collapaseView();
                        mAnimationManager.expand1(llWesterUnion);

                        creditCardCheck = false;
                        lastview = llWesterUnion;
                        generatewithoutCC();
                        break;
                    case R.id.RadioButtonOrderProcessEasyPasa:

                        collapaseView();
                        mAnimationManager.expand1(llEasyPasa);

                        creditCardCheck = false;
                        lastview = llEasyPasa;
                        generatewithoutCC();
                        break;
                    case R.id.RadioButtonOrderProcessJazzCash:

                        collapaseView();
                        mAnimationManager.expand1(llJazzCash);
                        lastview = llJazzCash;
                        creditCardCheck = false;
                        generatewithoutCC();
                        break;


                    case R.id.RadioButtonOrderProcessCash:
                        //   Toast.makeText(OrderProcessActivity.this, "cash", Toast.LENGTH_SHORT).show();
                        collapaseView();
                        mAnimationManager.expand1(llCash);

                        lastview = llCash;
                        creditCardCheck = false;
                        generatewithoutCC();
                        break;
                    case R.id.RadioButtonOrderProcessABL:
                        //   Toast.makeText(OrderProcessActivity.this, "ABL", Toast.LENGTH_SHORT).show();
                        collapaseView();
                        mAnimationManager.expand1(llABL);
                        creditCardCheck = false;
                        lastview = llABL;
                        generatewithoutCC();
                        break;


                    case R.id.radioOrderProcessStripe:

                        collapaseView();
                        mAnimationManager.expand1(llCC);

                        lastview = llCC;
                        generatewithCC();
                        creditCardCheck = true;

                        break;


                }

                radioButtonOffline.setChecked(true);
            }
        });







       /* rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {


                    //   mAnimationManager.collapse(rlDetailView, 1000, -200);
                    mAnimationManager.collapse1(llWesterUnion);
                    // rlDetailView.setVisibility(View.GONE);
                    isVisible = false;
                } else if (!isVisible) {
                    //   mAnimationManager.expand(rlDetailView, 1000, 200);
                    mAnimationManager.expand1(llWesterUnion);
                    isVisible = true;
                }
            }
        });*/


       /* etWhoHelped.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etWhoHelped.showDropDown();
            }
        });*/

        tvTermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTermsConditions newFragment = dialogTermsConditions.newInstance();
                newFragment.show(getSupportFragmentManager(), "dialog");

            }
        });
        tvPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEnterPromoCode newFragment = dialogEnterPromoCode.newInstance("", "Request Contact Details", "", "", false);
                newFragment.show(getSupportFragmentManager(), "dialog");

            }
        });

        btEditPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getPackages();


            }
        });

        btPayThroughCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// validateCardNumber()
                if (validateCardInfo() && validateCardNumber() && validateCardType()) {
                    WebArd wamonth = (WebArd) spMonth.getSelectedItem();
                    String expmonth = wamonth.getName();


                    WebArd wayear = (WebArd) spYear.getSelectedItem();
                    String expyear = wayear.getId();

                    SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
                    Date strDate = null;
                    try {
                        strDate = sdf.parse(expmonth + "/" + expyear);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    Calendar calendar = Calendar.getInstance();
                    int cyear = calendar.get(Calendar.YEAR);
                    int cmonth = calendar.get(Calendar.MONTH);
                    cmonth = cmonth + 1;
                    boolean dateCheck = false;
                    if (cyear == Integer.parseInt(expyear)) {

                        //  Log.e("expmonth", Integer.parseInt(expmonth) + "     " + cmonth);

                        if (Integer.parseInt(expmonth) < cmonth) {
                            dateCheck = true;
                        }


                    }


                    String cvv = etCVV.getText().toString().trim();
                    String cardNumber = etCardNumber.getText().toString().trim();

                    //    Card cardToSave = mCardInputWidget.getCard();
                    String selectionRadioValue = "";

                    int selectedId = rgAssisted.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    RadioButton radioButtonSelected = (RadioButton) findViewById(selectedId);
                    selectionRadioValue = radioButtonSelected.getTag().toString();

                    //  Log.e("System",System.currentTimeMillis() +"  "+ strDate.getTime());


                    if (selectionRadioValue.equals("yes") && spinner_religion.getSelectedItemId() == 0) {
                        Toast.makeText(OrderProcessActivity.this, "Please Select Who Helped you ", Toast.LENGTH_SHORT).show();
                    } else if (dateCheck) {
                        Toast.makeText(OrderProcessActivity.this, "Invalid Expiry Date ", Toast.LENGTH_SHORT).show();

                    } else {

                        mPayments payments = new mPayments();

                        if (selectionRadioValue.equals("yes")) {
                            WebArd ObjWhoHelped = (WebArd) spinner_religion.getSelectedItem();
                            payments.setHelp_person(ObjWhoHelped.getName());
                        } else {
                            payments.setHelp_person("N");
                        }

                        payments.setCard_number(Long.parseLong(cardNumber));
                        payments.setCcv_number(Integer.parseInt(cvv));
                        payments.setYear(Integer.parseInt(expyear));
                        payments.setMonth(Integer.parseInt(expmonth));

              /*      payments.setCard_number(Long.parseLong(cardToSave.getNumber()));
                    payments.setCcv_number(Integer.parseInt(cardToSave.getCVC()));
                    payments.setYear(cardToSave.getExpYear());
                    payments.setMonth(cardToSave.getExpMonth());*/


                        payments.setDescription(subscription.getShort_description());

                        payments.setEmail(subscription.getEmail());

                        payments.setAlias(subscription.getAlias());
                        payments.setPersonal_name(subscription.getName());

                        payments.setTranspath(subscription.getTranspath());
                        payments.setAmount(subscription.getTotal_cost());


                        Gson gson = new Gson();
                        JSONObject params = null;
                        try {
                            params = new JSONObject(gson.toJson(payments));
                           // Log.e("params", params.toString());
                          ProcessPaymentRequest(params);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }


            }
        });
    }


    private boolean validateCardNumber() {

        String ccNumber = etCardNumber.getText().toString().trim();
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        if (sum % 10 != 0) {
            Toast.makeText(this, "Invalid Card Number", Toast.LENGTH_SHORT).show();
        }
        return (sum % 10 == 0);

    }


    private boolean validateCardType() {
        boolean validCard = false;
        Pattern pattern;
        Matcher matcher;

        String ccNum = etCardNumber.getText().toString();
        //  Log.d("DEBUG", "afterTextChanged : " + ccNum);
        for (String p : listOfPattern) {

            pattern = Pattern.compile(p);
            matcher = pattern.matcher(ccNum);
            if (matcher.matches()) {
                //   Log.d("DEBUG", pattern + " afterTextChanged : discover");

                validCard = true;
            }

        }
        if (!validCard) {
            Toast.makeText(this, "Invalid Card Number", Toast.LENGTH_SHORT).show();
        }

        return validCard;

    }

    private boolean validateCardInfo() {


        if (etCVV.getText().toString().trim().length() < 3) {
            Toast.makeText(this, "Incorrect CVV", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etCardNumber.getText().toString().trim().length() < 12 || etCardNumber.getText().toString().trim().length() > 16) {
            Toast.makeText(this, "Incorrect Card Number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (spMonth.getSelectedItemId() == 0) {
            Toast.makeText(this, "Please select expiry month", Toast.LENGTH_SHORT).show();

            return false;
        } else if (spYear.getSelectedItemId() == 0) {
            Toast.makeText(this, "Please select expiry year", Toast.LENGTH_SHORT).show();
            return false;
        } /*else if (spYear.getSelectedItemId() != 0 && spMonth.getSelectedItemId() != 0) {



            return false;
        }*/ else {
            return true;
        }

    }

    private void generatewithCC() {
        if (currency.equals("p")) {
            cc = true;
        } else {

            cc = false;
        }
        status = "stp";

        generateCart(getParams(true, false, false));

    }

    private void generatewithoutCC() {

        if (currency.equals("p")) {
            cc = false;
            status = "";
        } else {
            cc = false;
            status = "";
        }


        generateCart(getParams(false, false, false));

    }


    private void collapaseView() {
        if (lastview != null) {

            mAnimationManager.collapse1(lastview);
        }

    }

    private void getPersonsList() {
        pBar.setVisibility(View.VISIBLE);


        //Log.e("api path", "" + Urls.getPersonsList + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

        JsonArrayRequest req = new JsonArrayRequest(Urls.getPersonsList,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("Response", response.toString());
                        try {
                            JSONArray jsonarrayData = response.getJSONArray(0);

                            Gson gsonc;

                            GsonBuilder gsonBuilderc = new GsonBuilder();

                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();
                            List<WebArd> personsDataList = (List<WebArd>) gsonc.fromJson(jsonarrayData.toString(), listType);
                            personsDataList.add(0, new WebArd("-1", "Please Select "));

                      /*      ArrayList mcList = new ArrayList();
                            for (WebArd value : personsDataList) {
                                mcList.add(value.getName());

                            }*/
                            //  acAdapter = new ArrayAdapter<String>(OrderProcessActivity.this, android.R.layout.simple_list_item_1, mcList);

                            //    etWhoHelped.setAdapter(acAdapter);
                            adapter_religion.updateDataList(personsDataList);

                            //Log.e("json length", jsonarrayData.length() + "");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       /* if (!refresh) {
                            // pBar.dismiss();
                            pBar.setVisibility(View.GONE);
                        }*/
                        pBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

    private void getPaymentPending() {
        pBar.setVisibility(View.VISIBLE);


        //Log.e("getPaymentPending path", "" + Urls.getPaymentPending + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.getPaymentPending + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //Log.e("response", Float.parseFloat(response) + "");
                        int v = Math.round(Float.parseFloat(response));
                        //Log.e("response", v + "");

                        if (SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() == 2 || SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() == 3 && v == 0) {
                            llMain.setVisibility(View.VISIBLE);
                            llErrorMessag.setVisibility(View.GONE);
                            generateCart(getParams(false, true, false));
                        } else {
                            llMain.setVisibility(View.GONE);
                            llErrorMessag.setVisibility(View.VISIBLE);


                            long member_status = SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status();
                            if (member_status == 0 || member_status == 1) {

                                tvErrorHeading.setText("You need to complete and verify your profile before you can purchase subscription.");
                                tvErrorDescription.setVisibility(View.GONE);


                            }
                            if (member_status == 2 || member_status == 3) {
                                tvErrorHeading.setText("Already Subscription Purchased");
                                tvErrorDescription.setVisibility(View.VISIBLE);
                                tvErrorDescription.setText("Your Payment is pending for approval, which will be done within 24 hours");


                            }
                            if (member_status == 4) {
                                tvErrorHeading.setText("Already Subscription Purchased");
                                tvErrorDescription.setVisibility(View.VISIBLE);
                                tvErrorDescription.setText("You need to contact marrymax support to purchase subscription");


                            }
                            if (member_status > 4) {
                                tvErrorHeading.setText("Cannot Purchase Subscription");
                                tvErrorDescription.setVisibility(View.VISIBLE);
                                tvErrorDescription.setText("You need to contact marrymax support to purchase subscription");


                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error", "" + error);
                // mTextView.setText("That didn't work!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
     /*   JsonArrayRequest req = new JsonArrayRequest(Urls.getPaymentPending + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());
                        try {
                            JSONArray jsonarrayData = response.getJSONArray(0);



                                int responseid = response.getInt("id");

                                if (responseid == 0) {






*//*
                            Gson gsonc;

                            GsonBuilder gsonBuilderc = new GsonBuilder();

                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();
                            List<WebArd>   personsDataList = (List<WebArd>) gsonc.fromJson(jsonarrayData.toString(), listType);


                            ArrayList mcList = new ArrayList();
                            for (WebArd value : personsDataList) {
                                mcList.add(value.getName());

                            }
                            acAdapter = new ArrayAdapter<String>(OrderProcessActivity.this, android.R.layout.simple_list_item_1, mcList);

                            etWhoHelped.setAdapter(acAdapter);*//*

                            Log.e("json length", jsonarrayData.length() + "");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       *//* if (!refresh) {
                            // pBar.dismiss();
                            pBar.setVisibility(View.GONE);
                        }*//*
                        pBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };*/
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    private void generateCart(JSONObject params) {
        //    path,item_id,member_ip,procode_code,checkout_email_address,payment_method,pp_paykey


/*        final ProgressDialog pBar = new ProgressDialog(OrderProcessActivity.this);
        pBar.setMessage("Loading...");
        pBar.show();*/
        //pBar.setVisibility(View.VISIBLE);
        showProgressDialog();
        //Log.e("generateCart", params.toString());
        //Log.e("generateCart params", Urls.generateCart);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.generateCart, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Res  generateCart ", response + "");

                        try {
                            JSONObject responseObject = response.getJSONArray("data").getJSONArray(0).getJSONObject(0);


                            Gson gson;
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            Type type = new TypeToken<Subscription>() {
                            }.getType();
                            subscription = (Subscription) gson.fromJson(responseObject.toString(), type);


                            //  Log.e("interested id", "" + subscription.getShort_description() + "====================");

                            DecimalFormat format = new DecimalFormat("#");
                            format.setMinimumFractionDigits(2);

                            tvName.setText("(" + subscription.getName() + ")");
                            tvAlias.setText(subscription.getAlias());
                            tvEmail.setText(subscription.getEmail());
                            tvCountry.setText(subscription.getCountry_name());
                            tvShortDesc.setText(subscription.getShort_description());
                            tvTotalPrice.setText(subscription.getItem_currency() + " " + format.format(subscription.getOrder_cost()) + "");
                            tvPrice.setText(subscription.getItem_currency() + " " + format.format(subscription.getItem_price()) + "");
                            String OrderId = "" + subscription.getTrans_order_id();
                            tvOrderNumber.setText("Your Order Number is : " + OrderId);
                            tvo1.setText("Your Order Number is : " + OrderId);
                            tvo2.setText("Your Order Number is : " + OrderId);
                            tvo3.setText("Your Order Number is : " + OrderId);
                            tvo4.setText("Your Order Number is : " + OrderId);
                            tvOrderNumberJazzCash.setText("Your Order Number is : " + OrderId);
                        /*    if (subscription.getItem_currency().equals("USD")) {
                                creditCardCheck = true;
                            }*/


                            if (subscription.getPromocode_info() != "") {


                                llPromoCodeDiscount.setVisibility(View.VISIBLE);

                                tvPromoCodeDiscountPercent.setText(subscription.getPromo_discount_percentage() + " %");
                                tvPromoCodeDiscountName.setText(subscription.getPromocode_info());


                                //answer.setText(format.format(data2));
                                tvPromoCodeDiscountAmount.setText(subscription.getItem_currency() + " " + format.format(subscription.getDiscount_amount()) + "");
                                tvPromoCodeName.setText(subscription.getProcode_code());

                                tvPromoCode.setVisibility(View.GONE);


                            } else {
                                tvPromoCode.setVisibility(View.VISIBLE);
                                llPromoCodeDiscount.setVisibility(View.GONE);
                            }
                            /* dialogShowInterest newFragment = dialogShowInterest.newInstance(member, member.getUserpath(), replyCheck, member2);
                            newFragment.show(frgMngr, "dialog");*/


                        } catch (JSONException e) {
                            //    pBar.dismiss();
                          dismissProgressDialog();
                            e.printStackTrace();
                        }

                        dismissProgressDialog();
                        //   pBar.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                //   pBar.dismiss();
                dismissProgressDialog();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    @Override
    public void onChangeSubscriptionPackage(String itemid, String OtherItemId) {

        //Log.e("onChangeSubscript", "" + creditCardCheck);

    /*    if (creditCardCheck) {
            other_item_id = itemid;
            item_id = OtherItemId;
        } else {
            item_id = itemid;
            other_item_id = OtherItemId;
        }
*/

        item_id = itemid;
        other_item_id = OtherItemId;

        generateCart(getParams(creditCardCheck, false, true));
    }

    @Override
    public void onApplyPromoCode(String s) {
        //   Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
        procode_code = s;
        generateCart(getParams(false, false, false));
    }


    private void getPackages() {

      /*  final ProgressDialog pBar = new ProgressDialog(OrderProcessActivity.this);
        pBar.setMessage("Loading...");
        pBar.show();*/
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        showProgressDialog();
        JSONObject params = new JSONObject();
        try {

            //    Log.e("packages", cc + "");
            if (creditCardCheck) {
                // params.put("item_id", other_item_id);
                params.put("payment_method", "cc");
            } else {
                //  params.put("item_id", item_id);
                params.put("payment_method", "wu");
            }


            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("getPackages", Urls.packages + "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.packages, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("getPackages", response + "");
                        try {
                            JSONArray responseArray = response.getJSONArray("data").getJSONArray(0);
                            //Log.e("getPackages", other_item_id + "  " + item_id);

                            if (responseArray.length() > 0) {
                                //   if (creditCardCheck) {
                                dialogSelectPackage newFragment = dialogSelectPackage.newInstance(responseArray.toString(), item_id, "", "", true);
                                newFragment.show(getSupportFragmentManager(), "dialog");

                                // } else {
                                  /*  dialogSelectPackage newFragment = dialogSelectPackage.newInstance(responseArray.toString(), item_id, "", "", false);
                                    newFragment.show(getSupportFragmentManager(), "dialog");
*/
                                // }


                            }


                        } catch (JSONException e) {
                            //   pBar.dismiss();
                            dismissProgressDialog();
                            e.printStackTrace();
                        }

                        dismissProgressDialog();
                        // pBar.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
                //  pBar.dismiss();
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


    private void ProcessPaymentRequest(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(OrderProcessActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //Log.e("params payment process", params.toString());
        //Log.e("Payment path", Urls.processPayment);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.processPayment, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Res  interest ", response + "");


                        try {
                            int responseid = response.getInt("id");

                            if (responseid == 0) {


                                Toast.makeText(OrderProcessActivity.this, "Payment failed", Toast.LENGTH_LONG).show();
                            } else if (responseid == 2) {
                                Toast.makeText(OrderProcessActivity.this, "Payment successful, but some problem with our server", Toast.LENGTH_LONG).show();

                            } else if (responseid == 1) {
                                Toast.makeText(OrderProcessActivity.this, "Payment successful", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(OrderProcessActivity.this, OrderConfirmationActivity.class);

                                intent.putExtra("transpath", subscription.getTranspath() + "");
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
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
                pDialog.dismiss();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }





    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(OrderProcessActivity.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
        }
        pDialog.show();
    }

    private void dismissProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

}
