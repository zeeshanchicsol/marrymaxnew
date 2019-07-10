package com.chicsol.marrymax.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.MySpinnerAdapter;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterUploadPictures;
import com.chicsol.marrymax.dialogs.dialogDosDonts;
import com.chicsol.marrymax.dialogs.dialogRequest;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.other.AppHelper;
import com.chicsol.marrymax.other.VolleyMultipartRequest;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.MySingleton;
import com.chicsol.marrymax.utils.UserPicture;
import com.chicsol.marrymax.utils.functions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadDocuments extends AppCompatActivity implements RecyclerViewAdapterUploadPictures.OnItemClickListener, RecyclerViewAdapterUploadPictures.OnSelectImageListener, dialogRequest.onCompleteListener {

    //    private  RecyclerView recyclerView;
    private List<Members> membersDataList;
    ///   private RecyclerViewAdapterUploadPictures recyclerAdapter;
    private LinearLayout llPicsNotAvailable;
    private TextView tvEmptyStateMessage, tvEmptyStateAlias;
    private ImagePicker imagePicker;
    private static final int SELECT_SINGLE_PICTURE = 101;
    public static final String IMAGE_TYPE = "image/*";
    ProgressDialog mProgressDialog;

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    String path = "";
    String subject = null;
    String userpath = null;


    private ProgressDialog pDialog;
    TextView tvDsdonts;

    private String Tag = "UploadDocuments";

    private Spinner spinner_profilefor;
    // private DatePicker datePicker;
    private MySpinnerAdapter adapter_profilefor;
    private List<WebArd> ProfileForDataList;

    private Button bSelectFile, btUploadDocuments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_documents);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        /*        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                //    uploadPhotoToServer();


            }
        });

        initilize();
        setListeners();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void initilize() {

        pDialog = new ProgressDialog(UploadDocuments.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);


        // getIntent().getStringExtra()

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPhotoUpload);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        membersDataList = new ArrayList<>();

        tvDsdonts = (TextView) findViewById(R.id.TextViewPhotoUploadDosDonts);

        /* tvEmptyStateAlias = (TextView) view.findViewById(R.id.TextViewPicFragmentMainAlias);
         */

        // llPicsNotAvailable = (LinearLayout) view.findViewById(R.id.LinearLayoutMemberUPPicsNotAvailable);
    /*    recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewMemberUploadPictures);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        recyclerAdapter = new RecyclerViewAdapterUploadPictures(membersDataList, getApplicationContext(), UploadDocuments.this, this);
        recyclerAdapter.setOnItemClickListener(this);

        recyclerView.setAdapter(recyclerAdapter);*/

        //  getMemberPics(SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());


        bSelectFile = (AppCompatButton) findViewById(R.id.ButtonUploadDocumentsSelectFile);

        btUploadDocuments = (AppCompatButton) findViewById(R.id.ButtonUploadDocuments);

        ProfileForDataList=new ArrayList<>();
        ProfileForDataList.add(new WebArd("1","NIC"));
        ProfileForDataList.add(new WebArd("2","NIC"));
        spinner_profilefor = (Spinner) findViewById(R.id.sp_profilefor);
        spinner_profilefor.setPrompt("Select Profile For");
        adapter_profilefor = new MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, ProfileForDataList);
        adapter_profilefor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_profilefor.setAdapter(adapter_profilefor);


    }

    private void setListeners() {

        bSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

        btUploadDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvDsdonts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDosDonts();
            }
        });
    }

    private void getDosDonts() {


      /*  final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
   */
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Urls.getPhotoDosDonts,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                        //        try {
                        dialogDosDonts newFragment = dialogDosDonts.newInstance(response.toString());
                        newFragment.show(getSupportFragmentManager(), "dialog");


/*
                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);
                          //  MyCountryStateDataList.clear();

                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<WebArd>>() {
                            }.getName();*/


                        // MyCountryStateDataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);


                         /*   MyCountryStateDataList.add(0, new WebArd("-1", "Please Select"));
                            adapter_myCountryStates.updateDataList(MyCountryStateDataList);
                            spMyCountryState.setSelection(0);*/

                       /* } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

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
        MySingleton.getInstance(this).addToRequestQueue(req, Tag);
    }


    void openImageChooser() {

    }


    @Override
    public void onItemClick(Members members) {
        //  recyclerView.invalidate();
        getMemberPics(SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());


    }

    @Override
    public void onComplete(String s) {

    }

    @Override
    public void onSelectImage(String s) {

        if (ActivityCompat.checkSelfPermission(UploadDocuments.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UploadDocuments.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadDocuments.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(UploadDocuments.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadDocuments.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(UploadDocuments.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.

/*
            int currentVersion = android.os.Build.VERSION.SDK_INT;
            if (currentVersion >= Build.VERSION_CODES.O_MR1) {


                Intent intent = new Intent();
                intent.setType(IMAGE_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "select image"), SELECT_SINGLE_PICTURE);

            } else {*/
            imagePicker = new ImagePicker(UploadDocuments.this);
            //  imagePicker.allowMultiple();
            imagePicker.shouldGenerateMetadata(true);
            imagePicker.shouldGenerateThumbnails(true);
            imagePicker.setImagePickerCallback(new ImagePickerCallback() {
                @Override
                public void onImagesChosen(List<ChosenImage> list) {


                    //Log.e("Image list size", "" + list.size());
                    //Log.e("Image list size", "" + list.get(0).getOriginalPath());

                    if (list.size() > 0) {
                        uploadPhotoToServer(list.get(0).getOriginalPath());
                    }

                }

                @Override
                public void onError(String s) {

                }
            });
            imagePicker.pickImage();
            //    }


        }

        //================================================================================================================


    }
/*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_SINGLE_PICTURE) {

                Uri selectedImageUri = data.getData();
                try {
                    imageView.setImageBitmap(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
                } catch (IOException e) {
                    Log.e(PhotoUpload.class.getSimpleName(), "Failed to load image", e);
                }
                // original code
//                String selectedImagePath = getPath(selectedImageUri);
//                selectedImagePreview.setImageURI(selectedImageUri);
            }
        } else {
            // report failure
            Toast.makeText(getApplicationContext(), " msg_failed_to_get_intent_data ", Toast.LENGTH_LONG).show();
            Log.d(PhotoUpload.class.getSimpleName(), "Failed to get intent data, result code is " + resultCode);
        }
    }
*/


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...

                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(UploadDocuments.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(UploadDocuments.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission for image selection");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(UploadDocuments.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(PhotoUpload.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }*/

        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                imagePicker.submit(data);
            }


            if (requestCode == SELECT_SINGLE_PICTURE) {

                Uri selectedImageUri = data.getData();
                try {


                    //    imageView.setImageBitmap(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
                    //  getRealPathFromDocumentUri

                    //Log.e("URI ", selectedImageUri.toString());
                    //   String selectedImagePath = getRealPathFromDocumentUri(getApplicationContext(), selectedImageUri);
                    Bitmap selectedImagePath = new UserPicture(selectedImageUri, getContentResolver()).getBitmap();
                    //   Log.e("selectedImagePath", selectedImagePath);

                    // uploadPhotoToServer(selectedImagePath);

                } catch (Exception e) {
                    //Log.e(PhotoUpload.class.getSimpleName(), "Failed to load image", e);
                }
                // original code
//                String selectedImagePath = getPath(selectedImageUri);
//                selectedImagePreview.setImageURI(selectedImageUri);
            }


        }

     /*   if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    //  String path =selectedImageUri.getPath();

                    //  getPathFromURI(selectedImageUri);

                    String path = getPathFromUri(getApplicationContext(), selectedImageUri);
                    Log.e(TAG, "Image Path : " + path);
                    this.path = path;


                    // Set the image in ImageView
                    // imgView.setImageURI(selectedImageUri);
                }
            }
        }*/
    }

    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
/*        int currentVersion = android.os.Build.VERSION.SDK_INT;
        if (currentVersion >= Build.VERSION_CODES.O_MR1) {


            Intent intent = new Intent();
            intent.setType(IMAGE_TYPE);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "select image"), SELECT_SINGLE_PICTURE);

        } else {*/
        imagePicker = new ImagePicker(UploadDocuments.this);
        imagePicker.allowMultiple();
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(true);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {


                //Log.e("Image list size", "" + list.size());
                //Log.e("Image list size", "" + list.get(0).getOriginalPath());

                if (list.size() > 0) {

                    //list.get(0).
                    uploadPhotoToServer(list.get(0).getOriginalPath());
                }

            }

            @Override
            public void onError(String s) {

            }
        });
        imagePicker.pickImage();
        // }
    }


    private void uploadPhotoToServer(final String path) {
        // loading or check internet connection or something...
        // ... then
        final String filename = new File(path).getName();
        //Log.e("File Name", "" + filename);


        String url;
        if (subject != null) {
            url = Urls.fileUpload + "/" + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath() + "/" + userpath + "/" + subject;
        } else {
            url = Urls.fileUpload + "/" + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath() + "/0/0";
        }

        //   Log.e("url", "" + url);


        pDialog.show();


        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                //Log.e("Messsage", "=======================" + resultResponse + "==============");
                if (resultResponse.equals("1")) {
                    Toast.makeText(UploadDocuments.this, "Image Uploaded", Toast.LENGTH_SHORT).show();


                    getMemberPics(SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                    Toast.makeText(UploadDocuments.this, "Your pictures will be available in your Profile as soon as the site Admin reviews the pictures and approves them.", Toast.LENGTH_SHORT).show();


                } else {
                    getMemberPics(SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
                    Toast.makeText(UploadDocuments.this, "Error. Please try again", Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();
              /*  try {
                    JSONObject result = new JSONObject(resultResponse);
                    String type = result.getString("type");
                    String message = result.getString("message");
                    Log.e("Messsage", "======================="+message+"==============");
                    if (type.equals("success")) {
                        // tell everybody you have succed upload image and post strings
                        Log.e("Messsage", message);
                    } else {
                        Log.e("Unexpected", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    pDialog.dismiss();
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    pDialog.dismiss();
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("type");
                        String message = response.getString("message");

                        //Log.e("Error Status", status);
                        //Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        pDialog.dismiss();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //     params.put("api_token", "gh659gjhvdyudo973823tt9gvjf7i6ric75r76");
                params.put("path", SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
             /*   params.put("location", mLocationInput.getText().toString());
                params.put("about", mAvatarInput.getText().toString());
                params.put("contact", mContactInput.getText().toString());*/
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> aMap = new HashMap<String, String>();
                functions fun = new functions();
                aMap.put("token", fun.getAccessToken());
                return aMap;

            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("image1", new DataPart(filename, AppHelper.getFileDataFromPath(getBaseContext(), path)));
                //params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest, Tag);
    }

    private void getMemberPics(final String path) {


     /*   final ProgressDialog pDialog = new ProgressDialog(PhotoUpload.this);
        pDialog.setMessage("Loading...");*/
        pDialog.show();
        //   Log.e("upload path", "" + Urls.getMembersPictures + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());

        JsonArrayRequest req = new JsonArrayRequest(Urls.getMembersPictures + path,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("Response", response.toString());
                        try {

                            List<Members> mDataList = new ArrayList<>();
                            JSONArray jsonCountryStaeObj = response.getJSONArray(0);


                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<List<Members>>() {
                            }.getType();

                            mDataList = (List<Members>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                           /* if (mDataList.size() == 0) {


                                mDataList.add(new Members());
                                mDataList.add(new Members());
                                mDataList.add(new Members());
                                mDataList.add(new Members());
                                recyclerAdapter.addAll(mDataList);
                            } else if (mDataList.size() == 1) {
                                mDataList.add(new Members());
                                mDataList.add(new Members());
                                mDataList.add(new Members());
                                recyclerAdapter.addAll(mDataList);
                            } else if (mDataList.size() == 2) {

                                mDataList.add(new Members());
                                mDataList.add(new Members());
                                recyclerAdapter.addAll(mDataList);

                            } else if (mDataList.size() == 3) {

                                mDataList.add(new Members());
                                recyclerAdapter.addAll(mDataList);

                            } else if (mDataList.size() == 4) {

                                recyclerAdapter.addAll(mDataList);

                            }*/


                        } catch (JSONException e) {
                            e.printStackTrace();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req, Tag);
    }

    @Override
    public void onStop() {
        super.onStop();
        MySingleton.getInstance(getApplicationContext()).cancelPendingRequests(Tag);

    }


    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {

        // just some safety built in
        if (uri == null) {
            // perform some logging or show user feedback
            Toast.makeText(getApplicationContext(), "Failed to get picture", Toast.LENGTH_LONG).show();
            //Log.d(PhotoUpload.class.getSimpleName(), "Failed to parse image path from image URI " + uri);
            return null;
        }

        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here, thanks to the answer from @mad indicating this is needed for
        // working code based on images selected using other file managers
        return uri.getPath();
    }

    public static String getRealPathFromDocumentUri(Context context, Uri uri) {
        String filePath = "";

        Pattern p = Pattern.compile("(\\d+)$");
        Matcher m = p.matcher(uri.toString());
        if (!m.find()) {
            //Log.e(PhotoUpload.class.getSimpleName(), "ID for requested image not found: " + uri.toString());
            return filePath;
        }
        String imgId = m.group();

        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{imgId}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }


}
