package com.chicsol.marrymax.activities.subscription;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.DashboarMainActivityWithBottomNav;
import com.chicsol.marrymax.modal.Subscription;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

public class OrderConfirmationActivity extends AppCompatActivity {

    private Button btgotBack;
    private TextView tvTotalAmount, tvTopTitle, tvProfileName, tvName, tvEmailAddress, tvDate, tvOrderNumber, tvPaymentMethod, tvPlanName, tvPlanPrice, tvDiscountName, tvDiscount;
    String transpath = "";
    private TextView tvMatchesCount, tvPhoneNumbersCount, tvMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        initialize();

        transpath = getIntent().getExtras().getString("transpath");

        JSONObject params = new JSONObject();
        try {
            params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
            // params.put("transpath", "AN~ZyeoBPTY=");
            params.put("transpath", transpath);

            loadData(params);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void initialize() {
        getSupportActionBar().setTitle("Order Confirmation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btgotBack = (Button) findViewById(R.id.ButtonGotoDashboard);

        tvPlanPrice = (TextView) findViewById(R.id.TextViewOrderConfirmationPackagePrice);

        tvProfileName = (TextView) findViewById(R.id.TextViewOrderConfirmationProfileName);
        tvName = (TextView) findViewById(R.id.TextViewOrderConfirmationName);
        tvEmailAddress = (TextView) findViewById(R.id.TextViewOrderConfirmationEmail);
        tvDate = (TextView) findViewById(R.id.TextViewOrderConfirmationDate);
        tvOrderNumber = (TextView) findViewById(R.id.TextViewOrderConfirmationOrderNumber);
        tvPaymentMethod = (TextView) findViewById(R.id.TextViewOrderConfirmationPaymentMethod);
        tvPlanName = (TextView) findViewById(R.id.TextViewOrderConfirmationPlanName);
        tvDiscountName = (TextView) findViewById(R.id.TextViewOrderConfirmationDiscountName);
        tvDiscount = (TextView) findViewById(R.id.TextViewOrderConfirmationDiscount);
        tvTotalAmount = (TextView) findViewById(R.id.TextViewOrderConfirmationTotalAmount);
        tvTopTitle = (TextView) findViewById(R.id.TextViewOrderConfirmationTopTitle);


        tvMatchesCount = (TextView) findViewById(R.id.TextViewOCMatchesCount);

        tvPhoneNumbersCount = (TextView) findViewById(R.id.TextViewOCPhoneNumberCount);

        tvMessages = (TextView) findViewById(R.id.TextViewSubscriptionPlanMessages);


        btgotBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderConfirmationActivity.this, DashboarMainActivityWithBottomNav.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setData(Subscription sub) {
        tvProfileName.setText(sub.getAlias());
        tvName.setText(sub.getName());
        tvEmailAddress.setText(sub.getCheckout_email());
        tvDate.setText(sub.getCart_date());
        tvOrderNumber.setText(sub.getTrans_order_id() + "");
        tvPaymentMethod.setText(sub.getPayment_method());
        tvPlanName.setText(sub.getItem_name());
        tvPlanPrice.setText(sub.item_currency + " " + sub.getItem_price());

        if (sub.getDiscount_amount() != 0) {
            tvDiscountName.setText(sub.getProcode_code());
            tvDiscount.setText(sub.item_currency + " " + sub.getDiscount_amount() + "");
        } else {
            tvDiscountName.setVisibility(View.GONE);
            tvDiscount.setVisibility(View.GONE);
        }

        tvTotalAmount.setText(sub.item_currency + " " + sub.getOrder_cost() + "");


        tvTopTitle.setText("You have paid " + sub.item_currency + " " + sub.getItem_price() + " for " + sub.getItem_name() + " .");


        tvPhoneNumbersCount.setText("Get " + sub.other_item_id + " Verified Phone Numbers ");
        tvMatchesCount.setText( sub.applied_force + " Matches Messaging ");

        if (sub.getName().equals("Silver")) {
            tvMessages.setText("Read Messages For 6 Months");
        } else {
            tvMessages.setText("Read Messages For 9 Months");
        }

    }

    private void loadData(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(OrderConfirmationActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        Log.e("params", params.toString());
        Log.e("profile path", Urls.printCart);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.printCart, params,
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
                            Subscription subscription = (Subscription) gson.fromJson(responseObject.toString(), type);


                            Log.e("interested id", "" + subscription.getAlias() + "====================");

                            setData(subscription);

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
