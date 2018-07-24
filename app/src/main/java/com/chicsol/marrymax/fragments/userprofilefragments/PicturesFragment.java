package com.chicsol.marrymax.fragments.userprofilefragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.PhotoUpload;
import com.chicsol.marrymax.adapters.RecyclerViewAdapterUPPictures;
import com.chicsol.marrymax.dialogs.dialogProfileCompletion;
import com.chicsol.marrymax.dialogs.dialogRequest;
import com.chicsol.marrymax.interfaces.RequestCallbackInterface;
import com.chicsol.marrymax.interfaces.WithdrawRequestCallBackInterface;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.other.MarryMax;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.utils.userProfileConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class PicturesFragment extends Fragment implements RecyclerViewAdapterUPPictures.OnItemClickListener, dialogRequest.onCompleteListener, dialogProfileCompletion.onCompleteListener, WithdrawRequestCallBackInterface {
    public String jsonData;
    View view;
    private
    RecyclerView recyclerView;
    private List<Members> membersDataList;
    private RecyclerViewAdapterUPPictures recyclerAdapter;
    private LinearLayout llPicsNotAvailable, llPicsNotAvailableMyProfile;
    private TextView tvEmptyStateMessage, tvEmptyStateAlias;
    private AppCompatButton btEmptyState, btUploadPics;
    private Members member = null;
    private onCompleteListener mCompleteListener;
    boolean myProfileCheck;
    private MarryMax marryMax;

    private RequestCallbackInterface requestCallbackInterface;
    String json = null;

    public PicturesFragment() {
        // Required empty public constructor
    }

    public void setRequestCallBackInterface(RequestCallbackInterface requestCallbackInterface) {
        this.requestCallbackInterface = requestCallbackInterface;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttachToParentFragment(getParentFragment());

        //  Log.e("jsonn  ", jsonData);
        //  jsonData = getArguments().getString("json");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_userprofile_pictures, container, false);
        marryMax = new MarryMax(getActivity());
        marryMax.setWithdrawRequestCallBackInterface(PicturesFragment.this);

        // jsonData
   /*     try {
            JSONArray jsonArray = new JSONArray(jsonData);
            if (jsonArray.length() == 5) {
                JSONArray objectsArray = jsonArray.getJSONArray(4);
             Log.e("lennnnnn",  objectsArray.length()+"");

            } else {

                //No Images
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        view = rootView;
        initilize(rootView);
        setListeners();
        json = getArguments().getString("json");
        Log.e("pictures fragment", "" + json);
        if (json != null) {
            loadData(json);
        }
        return rootView;
    }


    public void initilize(View view) {

        tvEmptyStateAlias = (TextView) view.findViewById(R.id.TextViewPicFragmentMainAlias);
        tvEmptyStateMessage = (TextView) view.findViewById(R.id.TextViewPicFragmentMainMessage);
        btEmptyState = (AppCompatButton) view.findViewById(R.id.ButtonPicFragmentMain);
        btUploadPics = (AppCompatButton) view.findViewById(R.id.ButtonPicFragmentMainPersonalProfile);


        llPicsNotAvailable = (LinearLayout) view.findViewById(R.id.LinearLayoutMemberUPPicsNotAvailable);
        llPicsNotAvailableMyProfile = (LinearLayout) view.findViewById(R.id.LinearLayoutMemberUPPicsNotAvailablePersonalProfile);

        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewMemberUPPictures);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));


        Bundle bundle = getArguments();

        myProfileCheck = bundle.getBoolean("myprofilecheck", false);

        //  Toast.makeText(getContext(), "profile cehck is " + myProfileCheck, Toast.LENGTH_SHORT).show();

        // loadData(jsonData);
    }

    public void loadData(String jsArray) {
        JSONArray jsonArray = null;
        JSONArray objectsArray1 = null;

        try {
            jsonArray = new JSONArray(jsArray);
            objectsArray1 = jsonArray.getJSONArray(4);
            JSONArray memArray = jsonArray.getJSONArray(0);
            Gson gson;
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
            Type mem = new TypeToken<Members>() {
            }.getType();
            member = (Members) gson.fromJson(memArray.getJSONObject(0).toString(), mem);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (objectsArray1.length() != 0) {
            llPicsNotAvailable.setVisibility(View.GONE);
            llPicsNotAvailableMyProfile.setVisibility(View.GONE);
            Gson gson;
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
            Type membert = new TypeToken<List<Members>>() {
            }.getType();


            try {

                JSONArray objectsArray = jsonArray.getJSONArray(4);


                membersDataList = (List<Members>) gson.fromJson(objectsArray.toString(), membert);


                recyclerAdapter = new RecyclerViewAdapterUPPictures(membersDataList, getContext());
                recyclerAdapter.setOnItemClickListener(PicturesFragment.this);
                recyclerView.setAdapter(recyclerAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            if (myProfileCheck) {
                llPicsNotAvailableMyProfile.setVisibility(View.VISIBLE);
            } else {
                llPicsNotAvailable.setVisibility(View.VISIBLE);
            }
            recyclerView.setVisibility(View.GONE);
         /*   if (memb.get_photo_upload_request_id() == 0) {
                Log.e("geupload_request_id 0", "  ");

            }*/

            tvEmptyStateAlias.setText(SharedPreferenceManager.getUserObject(getActivity()).getAlias());

            String txt = "<font color='#9a0606'>" + member.getAlias() + "</font>";


            if (member.get_image_count() == 0) {
                if (member.get_photo_upload_request_id() == 0) {
                    //request  photo view
                    Log.e("Request Photo", "Request Photo Upload");
                    tvEmptyStateMessage.setText(Html.fromHtml("Currently no pictures of " + "<b>" + txt.toUpperCase() + "</b> are available."));

                    btEmptyState.setText("Request Photo Upload");

                    //popup.getMenu().getItem(0).setTitle("Request Photo");
                } else if (member.get_photo_upload_request_id() > 0) {
                    // popup.getMenu().getItem(0).setTitle("Withdraw Request");

                    tvEmptyStateMessage.setText(Html.fromHtml("Your request to view pictures of " + "<b>" + txt.toUpperCase() + "</b>  is waiting for approval."));

                    //  Log.e("Withdraw Request ", "Withdraw Request");
                    btEmptyState.setText("Withdraw Request");
                }

            } else if (member.get_image_count() > 0 && member.get_hide_photo() > 0) {
                if (member.get_photo_request_id() == 0) {
                    //request  photo view
                    // popup.getMenu().getItem(0).setTitle("Request Photo View");
                    //     Log.e("Request Photo View ", "Request Photo View");
                    tvEmptyStateMessage.setText(Html.fromHtml("Pictures of " + "<b>" + txt.toUpperCase() + "</b> are private. Request to view them."));

                    btEmptyState.setText("Request to View Photo");
                } else if (member.get_photo_request_id() > 0) {
                    // popup.getMenu().getItem(0).setTitle("Withdraw Request");
                    //   Log.e("Withdraw Request ", "Withdraw Request");
                    tvEmptyStateMessage.setText(Html.fromHtml("Your request to view pictures of " + "<b>" + txt.toUpperCase() + "</b>  is waiting for approval."));

                    btEmptyState.setText("Withdraw Request");
                }
            }


        }


    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(userProfileConstants.jsonArryaResponse);
    }

    private void setListeners() {

        btUploadPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhotoUpload.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        btEmptyState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String type = null, title = null, btTitile = null, desc = null;


                boolean checkStatus = marryMax.statusBaseChecks(member, getContext(), 2, getFragmentManager(), PicturesFragment.this, v, null, null,null,null);

                if (checkStatus) {
                    if (member.get_image_count() == 0) {
                        if (member.get_photo_upload_request_id() == 0) {
                            //request  photo view
                            Log.e("Request Photo", "Request Photo Upload");


                            type = "1";
                            title = "Photo Request";
                            btTitile = "Request Photo";
                            desc = "Request <b> <font color=#216917>" + member.getAlias() + "</font></b>" + " to upload pictures.";

                            request(member, title, desc, btTitile, type);


                        } else if (member.get_photo_upload_request_id() > 0) {

                            Log.e("Withdraw Request ", "Withdraw Request");
                            JSONObject params = new JSONObject();
                            try {

                                params.put("userpath", member.getUserpath());
                                params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                                params.put("interested_id", member.get_photo_upload_request_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            desc = "Are you sure you are not interested in viewing photos of  <b> <font color=#216917>" + member.getAlias() + "</font></b>?";

                            marryMax.withdrawInterest(params, "Withdraw Photo Request", desc, null, getFragmentManager(), "1");


                        }

                    } else if (member.get_image_count() > 0 && member.get_hide_photo() > 0) {
                        if (member.get_photo_request_id() == 0) {
                            //request  photo view

                            Log.e("Request Photo View ", "Request Photo View");
                            type = "2";
                            title = "Photo View Request";
                            btTitile = "Request Photo";
                            desc = "Request <b> <font color=#216917>" + member.getAlias() + "</font></b>" + " to view pictures.";
                            request(member, title, desc, btTitile, type);


                        } else if (member.get_photo_request_id() > 0) {

                            //Log.e("Withdraw Request ", "Withdraw Request");
                            JSONObject params = new JSONObject();
                            try {

                                params.put("userpath", member.getUserpath());
                                params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
                                params.put("interested_id", member.get_photo_request_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            desc = "Are you sure you are not interested in viewing photos of  <b> <font color=#216917>" + member.getAlias() + "</font></b>?";

                            marryMax.withdrawInterest(params, "Withdraw Photo View Request", desc, null, getFragmentManager(), "2");


                        }
                    }
                }

            }
        });
    }


    @Override
    public void onItemClick(View view1, Members members) {
        ImageView imageView = (ImageView) view1.findViewById(R.id.ImageViewMemberDashUPPictures);
        loadPhoto(imageView);
    }


    private void loadPhoto(ImageView imageView) {

        ImageView tempImageView = imageView;


        AlertDialog.Builder imageDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                (ViewGroup) view.findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageDrawable(tempImageView.getDrawable());
        imageDialog.setView(layout);
        imageDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });


        imageDialog.create();
        imageDialog.show();
    }

    /*  private void withdrawInterest(JSONObject params, String title, String desc) {


          dialogWithdrawInterest newFragment = dialogWithdrawInterest.newInstance(params, title, desc);
          newFragment.setTargetFragment(PicturesFragment.this, 3);
          newFragment.show(getFragmentManager(), "dialog");

      }
  */
    private void request(Members member, String title, String desc, String btTitle, String type) {


        JSONObject params = new JSONObject();
        try {
            params.put("alias", SharedPreferenceManager.getUserObject(getContext()).getAlias());
            params.put("type", type);
            params.put("userpath", member.getUserpath());
            params.put("path", SharedPreferenceManager.getUserObject(getContext()).get_path());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialogRequest newFragment = dialogRequest.newInstance(params.toString(), title, desc, btTitle, true);
        newFragment.setTargetFragment(PicturesFragment.this, 0);
        newFragment.setListener(requestCallbackInterface);

        newFragment.show(getFragmentManager(), "dialog");


    }

    @Override
    public void onComplete(String s) {
        Log.e("Data welocomeeee", "==== " + s);
        // loadData();
        if (Integer.parseInt(s) == 0) {
            // Toast.makeText(getContext(), "Request has not been sent successfully ", Toast.LENGTH_SHORT).show();
        } else {
            mCompleteListener.onComplete(s);
        }
        // Toast.makeText(getContext(), "refresh  "+s, Toast.LENGTH_SHORT).show();
    }

    /*  @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            try {


                if (getTargetFragment() != null) {
                    mCompleteListener = (onCompleteListener) getTargetFragment();
                } else {
                    mCompleteListener = (onCompleteListener) context;
                }
            } catch (ClassCastException e) {
                throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
            }
        }*/
    public void onAttachToParentFragment(Fragment fragment) {
        try {
            mCompleteListener = (onCompleteListener) fragment;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }

    @Override
    public void onComplete(int s) {

    }


    public static interface onCompleteListener {
        public abstract void onComplete(String s);
    }


    @Override
    public void onWithdrawRequestComplete(String requestid) {
        mCompleteListener.onComplete("");
        //  Toast.makeText(context, "" + requestid, Toast.LENGTH_SHORT).show();
       /* int id = Integer.parseInt(requestid);
        switch (id) {
            case 1:
                member.set_photo_upload_request_id(0);
                if (json != null) {
                    loadData(json);
                }
                break;
            case 2:
                member.set_photo_request_id(0);
                if (json != null) {
                    loadData(json);
                }
                break;
      *//*       case 3:
                items.get(selectedPosition).set_profile_request_id(0);
                break;
            case 4:
                items.get(selectedPosition).set_phone_request_id(0);
                break;*//*
            case 5:
                //  items.get(selectedPosition).set_interested_id(0);
                break;

        }*/


    }


}
