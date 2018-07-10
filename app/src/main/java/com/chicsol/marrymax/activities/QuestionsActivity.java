package com.chicsol.marrymax.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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
import com.chicsol.marrymax.adapters.ParentAdapter;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mChild;
import com.chicsol.marrymax.modal.mContacts;
import com.chicsol.marrymax.modal.mMemList;
import com.chicsol.marrymax.modal.mParentChild;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewParent;
    public ImageLoader imageLoader;
    ParentAdapter parentAdapter;
    ArrayList<mParentChild> parentChildObj;
    private String Tag = "QuestionsActivity";
    AppCompatButton ButtonSendQuestions;
    private String userpath = "";
    private Members member;
    private DisplayImageOptions options;
    private TextView tvAlias, tvAge, tvQuestionLimit;
    private CircleImageView ivMain;
    private int height = 0;

    LinearLayout llProfile;

    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        userpath = getIntent().getStringExtra("userpath");
        initialize();
        setListeners();
    }


    private void initialize() {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int heightScreen = metrics.heightPixels;
        height = (int) (heightScreen * 0.13);


        member = new Members();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPhotoUpload);
        toolbar.setTitle("Ice-Break Questions");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButtonSendQuestions = (AppCompatButton) findViewById(R.id.ButtonSendQuestions);

        recyclerViewParent = (RecyclerView) findViewById(R.id.RecyclerViewQuestions);

        ivMain = (CircleImageView) findViewById(R.id.imageViewQuestionsUser);
        llProfile = (LinearLayout) findViewById(R.id.LinearLayoutQuestionMyProfile);


        tvAlias = (TextView) findViewById(R.id.TextViewQuestionsAlias);
        tvAge = (TextView) findViewById(R.id.TextViewQuestionsAge);

        tvQuestionLimit = (TextView) findViewById(R.id.TextViewQuestionsLimit);
        //  tvLastLogin = (TextView) findViewById(R.id.TextViewQuestionsLastLogin);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewParent.setLayoutManager(manager);
        recyclerViewParent.setHasFixedSize(true);

        parentAdapter = new ParentAdapter(this, new ArrayList<mParentChild>());
        recyclerViewParent.setAdapter(parentAdapter);


        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        inflater = LayoutInflater.from(getApplicationContext());
        options = new DisplayImageOptions.Builder()
                //   .showImageOnLoading(resize(R.drawable.loading_sm))
                // .showImageOnLoading(resize(R.drawable.loading))
                // .showImageForEmptyUri(resize(R.drawable.oops_sm))
                // .showImageForEmptyUri(resize(R.drawable.no_image))
                //.showImageOnFail(resize(R.drawable.oops_sm))
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)

                .postProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bmp) {

                        Bitmap bmp_sticker;
                        //    Display display =context.getApplicationContext().getWindowManager().getDefaultDisplay();
                        DisplayMetrics metrics = getResources().getDisplayMetrics();

                        //  display.getMetrics(metrics);

                        int widthScreen = metrics.widthPixels;
                        int heightScreen = metrics.heightPixels;
                        if (widthScreen > heightScreen) {
                            int h = (int) (heightScreen * 0.046);//it set the height of image 10% of your screen

                            bmp_sticker = resizeImage(bmp, h);
                        } else {
                            int h = (int) (heightScreen * 0.13);//it set the height of image 10% of your screen


                            bmp_sticker = resizeImageToHeight(bmp, h);

                        }

                        return bmp_sticker;
                    }
                }).build();


        getRequest();

        JSONObject paramsm = new JSONObject();
        try {

            paramsm.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
            paramsm.put("userpath", userpath);
            getMemberData(paramsm);
            ///Log.e("Q",""+params.toString());
            //  sendQuestion(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initHeade(Members member) {

        String desc = "  You can send upto five questions to  <b> <font color=#216917>" + member.getAlias() + "</font></b> from many given below.This is an opportunity to set expectations so please be frank.";
        tvQuestionLimit.setText(Html.fromHtml(desc));

        tvAlias.setText(member.getAlias());
        tvAge.setText(member.get_min_age() + "");
        //  tvLastLogin.setText(member.get_last_login_date());


        ivMain.setMinimumHeight(height);

        imageLoader.displayImage(Urls.baseUrl + "/" + member.get_default_image(),
                ivMain, options,
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

    }

    private void setListeners() {


        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserProfile(member);
            }
        });


        ButtonSendQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Toast.makeText(QuestionsActivity.this, "zzz " + Constants.selectedQuestions.size(), Toast.LENGTH_SHORT).show();
                if (Constants.selectedQuestions.size() > 0) {

                    // Log.e("ids are", getQuestionsIds());
                    JSONObject params = new JSONObject();
                    try {

                        params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());
                        params.put("userpath", userpath);
                        params.put("alias", SharedPreferenceManager.getUserObject(getApplicationContext()).getAlias());
                        params.put("questionids", getQuestionsIds());
///Log.e("Q",""+params.toString());
                        sendQuestion(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(getApplicationContext(), "You have selected 0 question(s). You have to select at least 1 question", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private String getQuestionsIds() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < Constants.selectedQuestions.size(); i++) {

            if (i != Constants.selectedQuestions.size() - 1) {
                stringBuilder.append(Constants.selectedQuestions.keyAt(i) + ",");
            } else {
                stringBuilder.append(Constants.selectedQuestions.keyAt(i));
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    private void getRequest() {


  /*      final ProgressDialog pDialog = new ProgressDialog(getApplicationContext());
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        Log.e("api path", "" + Urls.getQuestionAnswers + SharedPreferenceManager.getUserObject(getApplicationContext()).get_path());

        JsonArrayRequest req = new JsonArrayRequest(Urls.getQuestionAnswers,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                        try {

                            Log.e("array length is", response.length() + "");
                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mMemList>>() {
                            }.getType();

                            List<mMemList> questDataList = (List<mMemList>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                            Log.e("MyCountryStateDataList", "" + questDataList.size());


                            parentChildObj = new ArrayList<>();

                            for (int i = 0; i < questDataList.size(); i++) {
                                mMemList objMem = questDataList.get(i);

                                mParentChild pc1 = new mParentChild();
                                pc1.setTitle(objMem.getName());
                                pc1.setId(objMem.getId());


                                ArrayList<mChild> MQList = new ArrayList<>();
                                /*    for (int j = 1; i < response.length(); j++) {*/


                            /*    mParentChild pc1 = new mParentChild();
                                pc1.setChild(list1);
                                pc1.setTitle("c1");
                                parentChildObj.add(pc1);*/


                                JSONArray jsonArraySub = response.getJSONArray(i + 1);


                                Gson gsonq;
                                GsonBuilder gsonBuilderq = new GsonBuilder();
                                gsonq = gsonBuilderq.create();
                                Type listTypeq = new TypeToken<List<mChild>>() {
                                }.getType();

                                MQList = (ArrayList<mChild>) gsonq.fromJson(jsonArraySub.toString(), listTypeq);
                                pc1.setChild(MQList);
                                parentChildObj.add(pc1);
                                //  Log.e("MQList", "" + MQList.size());

                                //  }

                            }

                            parentAdapter.addAll(parentChildObj);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //  pDialog.dismiss();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req, Tag);
    }

    private void sendQuestion(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(QuestionsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());


        Log.e("Params " + Urls.sendQuestion, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.sendQuestion, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {
                            int responseid = response.getInt("id");
                            if (responseid >= 0) {
                                Toast.makeText(QuestionsActivity.this, "Questions Sent", Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }

    private void showUserProfile(Members obj) {
        Intent intent = new Intent(getApplicationContext(), UserProfileActivityWithSlider.class);

        intent.putExtra("selectedposition", "-1");
        List<Members> memberDataList = new ArrayList<Members>();
        Members members = new Members();
        members.setUserpath(obj.getUserpath());
        memberDataList.add(members);
        SharedPreferenceManager.setMemberDataList(getApplicationContext(), new Gson().toJson(memberDataList).toString());
        startActivity(intent);

    }


    private void getMemberData(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(QuestionsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());


        Log.e("Params Member Data" + Urls.memberData, "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.memberData, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {
                            //JSONArray jaData = response.getJSONArray("data");

                            JSONObject responseObject = response.getJSONArray("data").getJSONArray(0).getJSONObject(0);
                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<Members>() {
                            }.getType();

                            member = (Members) gsonc.fromJson(responseObject.toString(), listType);

                            if (member != null) {
                                initHeade(member);
                            }
                            Log.e("getAlias", "" + member.get_age() + "   " + member.get_last_login_date());


                        } catch (JSONException e) {
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

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

    private Bitmap resizeImageToHeight(final Bitmap image, int maxHeight) {
        Bitmap resizedImage = null;
        if (image != null) {
            //  int maxHeight = 80; //actual image height coming from internet
            int maxWidth = 300; //actual image width coming from internet

            int imageHeight = image.getHeight();
            if (imageHeight > maxHeight)
                imageHeight = maxHeight;
            int imageWidth = (imageHeight * image.getWidth()) / image.getHeight();

            /*if (imageWidth > maxWidth) {
                imageWidth = maxWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }*/
            resizedImage = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);
        }
        return resizedImage;
    }

}
