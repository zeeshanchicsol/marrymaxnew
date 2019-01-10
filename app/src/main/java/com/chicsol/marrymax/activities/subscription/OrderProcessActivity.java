package com.chicsol.marrymax.activities.subscription;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OrderProcessActivity extends AppCompatActivity implements dialogSelectPackage.onChangeSubscriptionPlanListener, dialogEnterPromoCode.onApplyPromoCodeListener {
    ImageButton btEditPackage;
    TextView tvPromoCode;
    Button btPayThroughCreditCard;
    private ProgressBar pDialog;
    private ArrayAdapter acAdapter;
    String ip = "";
    String item_id = "";
    String currency = "";
    String other_item_id = "";
    String procode_code = "", status = "";
    TextView tvName, tvAlias, tvEmail, tvCountry, tvShortDesc, tvTotalPrice, tvPrice, tvPromoCodeName, tvTermsConditions, tvPromoCodeDiscountAmount, tvPromoCodeDiscountName, tvPromoCodeDiscountPercent;
    private boolean isVisible = false;
    LinearLayout llPromoCodeDiscount, llWesterUnion, llEasyPasa, llABL, llCash, llCC;
    RelativeLayout rlMain, rlDetailView;
    private ExpandOrCollapse mAnimationManager;
    //  private AutoCompleteTextView etWhoHelped;
    private RadioButton radioOrderProcessStripe;
    View lastview = null;
    CardInputWidget mCardInputWidget;

    RadioGroup rgOrderProcessMain;
    RadioButton radioButtonOffline = null;
    private boolean cc = false;
    Subscription subscription;
    //   JSONObject params;
    LinearLayout llMain, llErrorMessag;
    TextView tvErrorDescription, tvErrorHeading, tvo1, tvo2, tvo3, tvo4, tvOrderNumber;
    private Spinner spinner_religion;
    private List<WebArd> ReligionDataList;
    private MySpinnerAdapter adapter_religion;
    private RadioGroup rgAssisted;
    private LinearLayout llSpAssisted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process);

        item_id = getIntent().getExtras().getString("item_id");
        other_item_id = getIntent().getExtras().getString("other_item_id");
        currency = getIntent().getExtras().getString("currency");
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
        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);

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


        llEasyPasa = (LinearLayout) findViewById(R.id.LinearlayoutOrderProcessEasypasaView);
        llABL = (LinearLayout) findViewById(R.id.LinearlayoutOrderProcessABL);
        llCash = (LinearLayout) findViewById(R.id.LinearlayoutOrderProcessCash);


        pDialog = (ProgressBar) findViewById(R.id.ProgressbarSubscriptionPlan);
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

        getPaymentPending();


    }


    private JSONObject getParams(boolean cccheck) {

        JSONObject params = new JSONObject();
        try {


            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
            params.put("member_ip", ip);
            //    params.put("status", status);


            if (cccheck || cc) {
                if (currency.equals("p")) {
                    params.put("item_id", other_item_id);

                } else {
                    params.put("item_id", item_id);
                }
            } else {
                params.put("item_id", item_id);
            }


            params.put("procode_code", procode_code);
            params.put("checkout_email_address", SharedPreferenceManager.getUserObject(getApplicationContext()).get_email());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    private void setListeners() {
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


                        lastview = llWesterUnion;
                        generatewithoutCC();
                        break;
                    case R.id.RadioButtonOrderProcessEasyPasa:

                        collapaseView();
                        mAnimationManager.expand1(llEasyPasa);


                        lastview = llEasyPasa;
                        generatewithoutCC();
                        break;
                    case R.id.RadioButtonOrderProcessCash:
                        //   Toast.makeText(OrderProcessActivity.this, "cash", Toast.LENGTH_SHORT).show();
                        collapaseView();
                        mAnimationManager.expand1(llCash);

                        lastview = llCash;
                        generatewithoutCC();
                        break;
                    case R.id.RadioButtonOrderProcessABL:
                        //   Toast.makeText(OrderProcessActivity.this, "ABL", Toast.LENGTH_SHORT).show();
                        collapaseView();
                        mAnimationManager.expand1(llABL);

                        lastview = llABL;
                        generatewithoutCC();
                        break;


                    case R.id.radioOrderProcessStripe:

                        collapaseView();
                        mAnimationManager.expand1(llCC);

                        lastview = llCC;
                        generatewithCC();


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
                Card cardToSave = mCardInputWidget.getCard();
                String selectionRadioValue = "";

                int selectedId = rgAssisted.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButtonSelected = (RadioButton) findViewById(selectedId);
                selectionRadioValue = radioButtonSelected.getTag().toString();


                if (selectionRadioValue.equals("yes") && spinner_religion.getSelectedItemId() == 0) {
                    Toast.makeText(OrderProcessActivity.this, "Please Select Who Helped you ", Toast.LENGTH_SHORT).show();
                } else if (cardToSave == null) {
                    Toast.makeText(OrderProcessActivity.this, "Invalid Card Data", Toast.LENGTH_SHORT).show();
                    // mErrorDialogHandler.showError("Invalid Card Data");
                } else {

                    mPayments payments = new mPayments();

                    if (selectionRadioValue.equals("yes")) {
                        WebArd ObjWhoHelped = (WebArd) spinner_religion.getSelectedItem();
                        payments.setHelp_person(ObjWhoHelped.getName());
                    } else {
                        payments.setHelp_person("N");
                    }


                    payments.setCard_number(Long.parseLong(cardToSave.getNumber()));
                    payments.setCcv_number(Integer.parseInt(cardToSave.getCVC()));
                    payments.setYear(cardToSave.getExpYear());
                    payments.setMonth(cardToSave.getExpMonth());
                    payments.setDescription(subscription.getShort_description());

                    payments.setEmail(subscription.getEmail());

                    payments.setAlias(subscription.getAlias());
                    payments.setPersonal_name(subscription.getName());

                    payments.setTranspath(subscription.getTranspath());
                    payments.setAmount( subscription.getTotal_cost());


                    Gson gson = new Gson();
                    JSONObject params = null;
                    try {
                        params = new JSONObject(gson.toJson(payments));
                        Log.e("params", params.toString());
                        ProcessPaymentRequest(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }
        });
    }

    private void generatewithCC() {
        if (currency.equals("p")) {
            cc = true;
        } else {

            cc = false;
        }
        status = "stp";

        generateCart(getParams(true));

    }

    private void generatewithoutCC() {

        if (currency.equals("p")) {
            cc = false;
            status = "";
        } else {
            cc = false;
            status = "";
        }


        generateCart(getParams(false));

    }


    private void collapaseView() {
        if (lastview != null) {

            mAnimationManager.collapse1(lastview);
        }

    }

    private void getPersonsList() {
        pDialog.setVisibility(View.VISIBLE);


        Log.e("api path", "" + Urls.getPersonsList + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());

        JsonArrayRequest req = new JsonArrayRequest(Urls.getPersonsList,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());
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

                            Log.e("json length", jsonarrayData.length() + "");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       /* if (!refresh) {
                            // pDialog.dismiss();
                            pDialog.setVisibility(View.GONE);
                        }*/
                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.setVisibility(View.GONE);
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
        pDialog.setVisibility(View.VISIBLE);


        Log.e("getPaymentPending path", "" + Urls.getPaymentPending + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.getPaymentPending + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.e("response", Float.parseFloat(response) + "");
                        int v = Math.round(Float.parseFloat(response));
                        Log.e("response", v + "");

                        if (SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status() == 2 || SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status() == 3 && v == 0) {
                            llMain.setVisibility(View.VISIBLE);
                            llErrorMessag.setVisibility(View.GONE);
                            generateCart(getParams(false));
                        } else {
                            llMain.setVisibility(View.GONE);
                            llErrorMessag.setVisibility(View.VISIBLE);


                            long member_status = SharedPreferenceManager.getUserObject(getApplicationContext()).get_member_status();
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
                Log.e("Error", "" + error);
                // mTextView.setText("That didn't work!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };
     /*   JsonArrayRequest req = new JsonArrayRequest(Urls.getPaymentPending + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path(),
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
                            // pDialog.dismiss();
                            pDialog.setVisibility(View.GONE);
                        }*//*
                        pDialog.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                pDialog.setVisibility(View.GONE);
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


/*        final ProgressDialog pDialog = new ProgressDialog(OrderProcessActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        pDialog.setVisibility(View.VISIBLE);
        Log.e("generateCart", params.toString());
        Log.e("generateCart params", Urls.generateCart);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.generateCart, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  generateCart ", response + "");

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
                            //    pDialog.dismiss();
                            pDialog.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                        pDialog.setVisibility(View.GONE);
                        //   pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                //   pDialog.dismiss();
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

        if (cc) {
            other_item_id = itemid;
            item_id = OtherItemId;
        } else {
            item_id = itemid;
            other_item_id = OtherItemId;
        }

        generateCart(getParams(cc));
    }

    @Override
    public void onApplyPromoCode(String s) {
        //   Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
        procode_code = s;
        generateCart(getParams(false));
    }


    private void getPackages() {

      /*  final ProgressDialog pDialog = new ProgressDialog(OrderProcessActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        pDialog.setVisibility(View.VISIBLE);
        JSONObject params = new JSONObject();
        try {

            if (cc) {
                params.put("item_id", other_item_id);
                params.put("payment_method", "cc");
            } else {
                params.put("item_id", item_id);
                params.put("payment_method", "");
            }


            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("getPackages", Urls.packages + "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.packages, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {
                            JSONArray responseArray = response.getJSONArray("data").getJSONArray(0);

                            if (responseArray.length() > 0
                                    ) {
                                if (cc) {
                                    dialogSelectPackage newFragment = dialogSelectPackage.newInstance(responseArray.toString(), other_item_id, "", "", true);
                                    newFragment.show(getSupportFragmentManager(), "dialog");

                                } else {
                                    dialogSelectPackage newFragment = dialogSelectPackage.newInstance(responseArray.toString(), item_id, "", "", false);
                                    newFragment.show(getSupportFragmentManager(), "dialog");

                                }


                            }


                        } catch (JSONException e) {
                            //   pDialog.dismiss();
                            pDialog.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                        pDialog.setVisibility(View.GONE);
                        // pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();
                pDialog.setVisibility(View.GONE);
                //  pDialog.dismiss();
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
        Log.e("params payment process", params.toString());
        Log.e("Payment path", Urls.processPayment);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.processPayment, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Res  interest ", response + "");


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


}
