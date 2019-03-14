package com.chicsol.marrymax.dialogs;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.chicsol.marrymax.adapters.RecyclerViewAdapterFeedbackAnswerDetails;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterFeedbackQuestions;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.modal.cModel;
import com.chicsol.marrymax.modal.mLfm;
import com.chicsol.marrymax.modal.mUsrFeedback;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
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
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class dialogFeedbackDetail extends DialogFragment {

    public onCompleteListener mCompleteListener;
    String userpath, params;
    mLfm lfm;

    AppCompatRatingBar mRbar;
    Context context;
    RecyclerView recyclerView;
    List<cModel> questionsDataList;
    private RecyclerViewAdapterFeedbackAnswerDetails recyclerAdapter;
    private String id = "";

    private TextView tvAlias, tvAge, tvDate, tvCountry, pref1, pref2, pref3, pref4, prefValue1, prefValue2, prefValue3, prefValue4, tvAboutMe, tvFeedbackRating;
    private ImageView image;
    public ImageLoader imageLoader;
    private DisplayImageOptions options, optionsNormalImage;
    private LayoutInflater inflater;
    private int height = 0;

    public static dialogFeedbackDetail newInstance(String params) {

        Gson gson = new Gson();
        dialogFeedbackDetail frag = new dialogFeedbackDetail();
        Bundle args = new Bundle();
        args.putString("params", params);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        context = activity;


        try {
            if (getTargetFragment() != null) {
                mCompleteListener = (onCompleteListener) getTargetFragment();
            } else {
                mCompleteListener = (onCompleteListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();

        params = mArgs.getString("params");


        try {
            getUserFeedback(new JSONObject(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int heightScreen = metrics.heightPixels;
        height = (int) (heightScreen * 0.13);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        inflater = LayoutInflater.from(context);
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
                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

                        //  display.getMetrics(metrics);

                        int widthScreen = metrics.widthPixels;
                        int heightScreen = metrics.heightPixels;
                        if (widthScreen > heightScreen) {
                            int h = (int) (heightScreen * 0.046);//it set the height of image 10% of your screen

                            bmp_sticker = resizeImage(bmp, h);
                        } else {
                            int h = (int) (heightScreen * 0.13);//it set the height of image 10% of your screen

                            bmp_sticker = resizeImage(bmp, h);

                        }

                        return bmp_sticker;
                    }
                }).build();
        optionsNormalImage = new DisplayImageOptions.Builder()
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
                        //  Display display =getWindowManager().getDefaultDisplay();
                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();


                        // display.getMetrics(metrics);

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
                }).build();



      /*  Gson gson = new Gson();


        jsarray = mArgs.getString("obj");
        lfm = gson.fromJson(jsarray, mLfm.class);*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_new_feedback_detail, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        image = (ImageView) rootView.findViewById(R.id.ImageViewFeedbackProfile);
        tvAlias = (TextView) rootView.findViewById(R.id.TextVewFeedbackAlias);
        tvAge = (TextView) rootView.findViewById(R.id.TextViewFeedbackAge);
        tvDate = (TextView) rootView.findViewById(R.id.TextViewFeedbackDate);

        pref1 = (mTextView) rootView.findViewById(R.id.TextViewFeedbackPref1);
        pref2 = (mTextView) rootView.findViewById(R.id.TextViewFeedbackPref2);
        pref3 = (mTextView) rootView.findViewById(R.id.TextViewFeedbackPref3);
        pref4 = (mTextView) rootView.findViewById(R.id.TextViewFeedbackPref4);

        prefValue1 = (mTextView) rootView.findViewById(R.id.TextViewFeedbackPrefValue1);
        prefValue2 = (mTextView) rootView.findViewById(R.id.TextViewFeedbackPrefValue2);
        prefValue3 = (mTextView) rootView.findViewById(R.id.TextViewFeedbackPrefValue3);
        prefValue4 = (mTextView) rootView.findViewById(R.id.TextViewFeedbackPrefValue4);

        tvAboutMe = (TextView) rootView.findViewById(R.id.TextViewFeedbackAboutMe);

        tvFeedbackRating = (TextView) rootView.findViewById(R.id.TextViewFeedbackRating);


        mRbar = (AppCompatRatingBar) rootView.findViewById(R.id.dialog_ratingbar);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerViewFeedbackQuestions);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        // mLayoutManager.setStackFromEnd(true);
        // mLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new RecyclerViewAdapterFeedbackAnswerDetails(context);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(recyclerAdapter);


        Button cancelButton = (Button) rootView.findViewById(R.id.mButtonDialogMatchAidUPCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                dialogFeedbackDetail.this.getDialog().cancel();
            }
        });


        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static interface onCompleteListener {
        public abstract void onComplete(String s);
    }
/*
    private void getFeedbackData() {


        //Log.e("api path", "" + Urls.getFeedbackData );

        JsonArrayRequest req = new JsonArrayRequest(Urls.getFeedbackData + "/" + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   Log.d("Response", response.toString());
                        try {


                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getType();

                            questionsDataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                            recyclerAdapter.addAll(questionsDataList);
                            Log.e("getFeedbackData", questionsDataList.size() + "");


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
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(req);
    }*/

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

    private void getUserFeedback(JSONObject params) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        Log.e("params", params.toString());
        Log.e("profile path", Urls.usrFeedback);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.usrFeedback, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("usrFeedback ", response + "");

                        mUsrFeedback obj = new mUsrFeedback();
                        Gson gson;
                        GsonBuilder gsonBuildert = new GsonBuilder();
                        gson = gsonBuildert.create();


                        try {
          /*  Type mUsFdbk = new TypeToken<mUsrFeedback>() {
            }.getType();*/


                            obj = (mUsrFeedback) gson.fromJson(response.getJSONArray("jdata").getJSONArray(0).getJSONObject(0).toString(), mUsrFeedback.class);

                            JSONArray jsonCountryStaeObj = response.getJSONArray("jdata").getJSONArray(1);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<cModel>>() {
                            }.getType();
                            questionsDataList = (List<cModel>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);

                            mRbar.setRating(obj.getRating());
                            //    mRbar.setClickable(false);
                            mRbar.setIsIndicator(true);

                            tvAlias.setText(obj.getAlias() + " ");
                            tvAge.setText("( " + obj.getAge() + " Years )");
                            tvDate.setText(obj.getDate());

                            pref1.setText("Marital Status: ");
                            pref2.setText("Religious Sect: ");
                            pref3.setText("Ethnicity: ");
                            pref4.setText("Education: ");

                            prefValue1.setText(obj.getMarital_type());
                            prefValue2.setText(obj.getReligious_type());
                            prefValue3.setText(obj.getEthnic_type());
                            prefValue4.setText(obj.getEducation_type());

                            tvAboutMe.setText(obj.getNotes());
                            tvFeedbackRating.setText("Rating "+obj.getRating() + " out of 5");


                            image.setMinimumHeight(height);
                            imageLoader.displayImage(Urls.baseUrl + "/" + obj.getDefault_image(),
                                    image, options,
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


                            //  recyclerAdapter.setOnItemClickListener(dialogFeedback.this);


                            recyclerAdapter.addAll(questionsDataList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // onUpdateListener.onUpdate("Error occurred, Try again.");

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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

}


