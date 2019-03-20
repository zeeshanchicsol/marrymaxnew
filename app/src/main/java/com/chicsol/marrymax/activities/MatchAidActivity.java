package com.chicsol.marrymax.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.directive.MainDirectiveActivity;
import com.chicsol.marrymax.dialogs.dialogMatchAidFeedback;
import com.chicsol.marrymax.modal.mLfm;
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

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class MatchAidActivity extends AppCompatActivity implements dialogMatchAidFeedback.onCompleteListener {
    public ImageLoader imageLoader;
    LinearLayout ll_Main;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private int height = 0;
    private Toolbar toolbar;

    LinearLayout llNoMatches, llEmptyMatches;

    AppCompatButton btMatchAidSubscribe, btFindMatches;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_aid);
        initialize();
        setListeners();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initialize() {


        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Match Aid");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btMatchAidSubscribe = (AppCompatButton) findViewById(R.id.ButtonMatchAidSubscribe);
        btFindMatches = (AppCompatButton) findViewById(R.id.ButtonMatchAidFindMatches);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int heightScreen = metrics.heightPixels;
        height = (int) (heightScreen * 0.13);
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

        ll_Main = (LinearLayout) findViewById(R.id.LinearLayoutMatchAidMain);

        llNoMatches = (LinearLayout) findViewById(R.id.LinearLayoutMAtchAidNoMatches);
        llEmptyMatches = (LinearLayout) findViewById(R.id.LinearLayoutMAtchAidEmpty);
        llEmptyMatches.setVisibility(View.GONE);

        if (SharedPreferenceManager.getUserObject(getApplicationContext()).getMember_status() <= 3) {
            llNoMatches.setVisibility(View.VISIBLE);

        } else {
            llNoMatches.setVisibility(View.GONE);
            getRequest();
        }
    }

    private void setListeners() {
        btMatchAidSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MarryMax(MatchAidActivity.this).subscribe();
            }
        });


        btFindMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), MainDirectiveActivity.class);
                in.putExtra("type", 24);
                startActivity(in);
            }
        });
    }

    private void getRequest() {


        final ProgressDialog pDialog = new ProgressDialog(MatchAidActivity.this);
        pDialog.setMessage("Loading...");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (MatchAidActivity.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17


                pDialog.show();
            }
        }



        Log.e("getRequest path", "" + Urls.getAssistanceList + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

        JsonArrayRequest req = new JsonArrayRequest(Urls.getAssistanceList + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("getRequest Response", response.toString());
                        try {


                            JSONArray jsonArray1 = response.getJSONArray(0);
                            JSONArray jsonArray2 = response.getJSONArray(1);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<mLfm>>() {
                            }.getType();

                            List<mLfm> lis1 = (List<mLfm>) gsonc.fromJson(jsonArray1.toString(), listType);
                            List<mLfm> lis2 = (List<mLfm>) gsonc.fromJson(jsonArray2.toString(), listType);

                            Log.e("s 1", lis1.size() + "");
                            Log.e("s 2", lis2.size() + "");


                            if (lis1.size() == 0) {

                                llEmptyMatches.setVisibility(View.VISIBLE);
                            }

                            if (ll_Main.getChildCount() > 0) {
                                ll_Main.removeAllViews();
                            }
                            for (int i = 0; i < lis1.size(); i++) {
                                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_matchaid, null);

                                final mLfm lfm = lis1.get(i);
                                mViewHolder vHolder = new mViewHolder(view);
                                vHolder.alias.setText(lfm.getName());
                                vHolder.status.setText("Status: " + lfm.getStatus());
                                vHolder.date.setText(lfm.getDate() + " ");
                                vHolder.description.setText(lfm.getType());

                                if (lfm.getVisting_memberid() == 2 && lfm.getMemberid() == 0) {
                                    vHolder.btFeedback.setVisibility(View.VISIBLE);
                                    vHolder.btFeedback.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //    Toast.makeText(MatchAidActivity.this, "Clicked", Toast.LENGTH_SHORT).show();


                                            dialogMatchAidFeedback newFragment = dialogMatchAidFeedback.newInstance(lfm);
                                            //   newFragment.setTargetFragment(getSupportFragmentManager(), 0);
                                            newFragment.show(getSupportFragmentManager(), "dialog");


                                        }
                                    });

                                } else {
                                    vHolder.btFeedback.setVisibility(View.GONE);
                                }


                                vHolder.llItemMain.setId(i);
                                LinearLayout lastllLayout = vHolder.llItemMain;
                                ll_Main.addView(view);


                                vHolder.image.setMinimumHeight(height);
                                imageLoader.displayImage(Urls.baseUrl + "/" + lfm.getDefault_image(),
                                        vHolder.image, options,
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

                                for (int j = 0; j < lis2.size(); j++) {
                                    mLfm slfm = lis2.get(j);
                                    Log.e("id for match", lfm.getId() + "   " + slfm.getId2());

                                    if (lfm.getId() == slfm.getId2()) {
                                        Log.e("equal", lfm.getId() + "   " + slfm.getId2());
                                        View subview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_matchaid_subitem, null);
                                        mViewHolder vSubHoler = new mViewHolder(subview);
                                        vSubHoler.description.setText(slfm.getText());
                                        vSubHoler.date.setText(slfm.getDate());
                                        vSubHoler.alias.setText("MarryMax Associate");
                                        // lastllLayout.ad
                                        lastllLayout.addView(subview);
                                    }
                                }

                            }


                            //  mViewHolder1

                            //  mViewHolder2.alias.setText("zeeshan 2");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            if (MatchAidActivity.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17
                                return;
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

    @Override
    public void onComplete(String s) {
        getRequest();
    }


    protected static class mViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView alias, description, status, date;
        public LinearLayout llItemMain;
        public AppCompatButton btFeedback;


        public mViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.ImageViewMatchAidmImage);
            alias = (TextView) itemView.findViewById(R.id.TextViewMachAidmAlias);
            description = (TextView) itemView.findViewById(R.id.TextViewMachAidmDescription);

            status = (TextView) itemView.findViewById(R.id.TextViewMachAidmStatus);
            btFeedback = (AppCompatButton) itemView.findViewById(R.id.ButtonMatchAidFeedback);

            date = (TextView) itemView.findViewById(R.id.TextViewMachAidmDate);
            llItemMain = (LinearLayout) itemView.findViewById(R.id.LinearLayoutMatchAidItemMain);

        }
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