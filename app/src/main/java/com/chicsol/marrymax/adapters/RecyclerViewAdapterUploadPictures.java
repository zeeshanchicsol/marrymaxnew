/*
 * Copyright (C) 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chicsol.marrymax.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.widgets.SquareImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class RecyclerViewAdapterUploadPictures extends RecyclerView.Adapter<RecyclerViewAdapterUploadPictures.ViewHolder> implements View.OnClickListener {
    public ImageLoader imageLoader;
    Context context;
    private DisplayImageOptions options;
    private LayoutInflater inflater;
    private Activity activity;
    private List<Members> items;
    private OnItemClickListener onItemClickListener;
    private OnSelectImageListener onSelectImageListener;


    public RecyclerViewAdapterUploadPictures(List<Members> items, final Context context, Activity activity, OnSelectImageListener onSelectImageListener) {
        this.activity = activity;
        this.items = items;
        this.context = context;
        this.onSelectImageListener = onSelectImageListener;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        inflater = LayoutInflater.from(context);

        options = new DisplayImageOptions.Builder()
                //   .showImageOnLoading(resize(R.drawable.loading_sm))
                // .showImageOnLoading(resize(R.drawable.loading))
                // .showImageForEmptyUri(resize(R.drawable.oops_sm))
                // .showImageForEmptyUri(resize(R.drawable.no_image))
                //.showImageOnFail(resize(R.drawable.oops_sm))
                .cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)

/*                .postProcessor(new BitmapProcessor() {
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
                            //     iv.getLayoutParams().width = (int) (widthScreen * 0.10);
                            // Log.e("wid " + widthScreen + "  " + heightScreen, "");
                            bmp_sticker = resizeImage(bmp, h);
                        } else {
                            int h = (int) (heightScreen * 0.080);//it set the height of image 10% of your screen
                            //   iv.getLayoutParams().width = (int) (widthScreen * 0.15);
                            bmp_sticker = resizeImage(bmp, h);
                            // Log.e("wid " + widthScreen + "  " + heightScreen, "");
                        }

                        return bmp_sticker;
                    }
                })*/.build();


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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_photo_upload, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);


        //  return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Members obj = items.get(position);

        holder.tvphotoUploadStatus.setVisibility(View.GONE);


        if (obj.get_default_image() != null) {
            if (obj.get_default_image().equals("1")) {
                holder.defaultCheckbox.setVisibility(View.GONE);
                holder.tvDefaultImage.setVisibility(View.VISIBLE);
            } else {
                holder.defaultCheckbox.setVisibility(View.VISIBLE);
                holder.defaultCheckbox.setChecked(false);
                holder.tvDefaultImage.setVisibility(View.GONE);
            }
        }
        if (obj.get_photo_path() == null) {
            holder.tvDefaultImage.setVisibility(View.GONE);
            holder.defaultCheckbox.setVisibility(View.GONE);
            holder.imageDeleteImage.setVisibility(View.GONE);
            holder.imagePerView.setTag("default_image_no");
            holder.imagePerView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_upload_no_img));

        } else {

            if(obj.get_photo_name().equals("Approved")){

                holder.tvphotoUploadStatus.setVisibility(View.VISIBLE);
                holder.tvphotoUploadStatus.setText(obj.get_photo_name());
                holder.tvphotoUploadStatus.setTextColor(context.getResources().getColor(R.color.colorDefaultGreen));

            }
            else {
                holder.tvphotoUploadStatus.setVisibility(View.VISIBLE);
                holder.tvphotoUploadStatus.setText("New picture; Not reviewed yet!");
                holder.tvphotoUploadStatus.setTextColor(context.getResources().getColor(R.color.colorTextRed));


            }

            holder.imageDeleteImage.setVisibility(View.VISIBLE);
          holder.imagePerView.setTag("-");
            imageLoader.displayImage(Urls.baseUrl + "/" + obj.get_photo_path(),
                    holder.imagePerView, options,
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


        holder.imageDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject params = new JSONObject();
                try {
                    //    path, photo_id,photo_path_thb,photo_server_name

                    params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                    params.put("photo_id", obj.get_photo_id());
                    params.put("photo_path_thb", obj.get_photo_path_thb());
                    params.put("photo_server_name", obj.get_photo_server_name());

                    deleteImageRequest(params, obj, position, holder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        holder.defaultCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    JSONObject params = new JSONObject();
                    try {

                        //  path, ID,ID2
                        params.put("path", SharedPreferenceManager.getUserObject(context).get_path());
                        params.put("ID", obj.get_photo_id());
                        params.put("ID2", 1);
                        //  Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();

                        changeDefaultImage(params, obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        holder.imagePerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.imagePerView.getTag().equals("default_image_no")) {
                   // Toast.makeText(context, "default", Toast.LENGTH_SHORT).show();

                    onSelectImageListener.onSelectImage("");


                }
                //      android:tag="default_image_no"
            }
        });


        holder.itemView.setTag(obj);
    }

    private void changeDefaultImage(JSONObject params, final Members obj) {

        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        Log.e("Params", "" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.defaultPicture, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {
                            int responseid = response.getInt("id");

                            if (responseid == 1) {

                                onItemClickListener.onItemClick(obj);
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);

    }

    private void deleteImageRequest(JSONObject params, final Members obj, final int position, final ViewHolder holder) {

        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        Log.e("del Params",  Urls.deletePics+ "----" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.deletePics, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("re  update appearance", response + "");
                        try {
                            int responseid = response.getInt("id");

                            if (responseid == 1) {

                                onItemClickListener.onItemClick(obj);
/*
                                items.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, items.size());
                                holder.itemView.setVisibility(View.GONE);*/

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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(final View v) {
        //  onItemClickListener.onItemClick((Members) v.getTag());
    }

    public interface OnItemClickListener {

        void onItemClick(Members members);

    }


    public interface OnSelectImageListener {

        void onSelectImage(String s);

    }

    public void addAll(List<Members> lst) {
        items.clear();

        items.addAll(lst);
        notifyDataSetChanged();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageDeleteImage;
        public AppCompatCheckBox defaultCheckbox;
        public SquareImageView imagePerView;
        public TextView tvDefaultImage,tvphotoUploadStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            tvphotoUploadStatus=(TextView) itemView.findViewById(R.id.TextViewPhotoUploadPhotoStatus);
            imagePerView = (SquareImageView) itemView.findViewById(R.id.imageViewPhotoUploadPerview);
            imageDeleteImage = (ImageView) itemView.findViewById(R.id.ImagePhotoUploadViewDeleteImage);
            defaultCheckbox = (AppCompatCheckBox) itemView.findViewById(R.id.CheckBoxPhotoUploadDefaultImage);
            tvDefaultImage = (TextView) itemView.findViewById(R.id.TextViewPhotoUploadDefaultImageText);
        }
    }





}
