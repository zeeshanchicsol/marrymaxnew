package com.chicsol.marrymax.other;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
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
import com.chicsol.marrymax.activities.ActivityLogin;
import com.chicsol.marrymax.activities.ContactAcivity;
import com.chicsol.marrymax.activities.DrawerActivity;
import com.chicsol.marrymax.activities.UserProfileActivityWithSlider;
import com.chicsol.marrymax.activities.WebViewActivity;
import com.chicsol.marrymax.activities.registration.RegisterAppearanceActivity;
import com.chicsol.marrymax.activities.registration.RegisterGeographicActivity;
import com.chicsol.marrymax.activities.registration.RegisterInterest;
import com.chicsol.marrymax.activities.registration.RegisterLifeStyleActivity1;
import com.chicsol.marrymax.activities.registration.RegisterLifeStyleActivity2;
import com.chicsol.marrymax.activities.registration.RegisterPersonalityActivity;
import com.chicsol.marrymax.activities.search.SearchMainActivity;
import com.chicsol.marrymax.activities.subscription.SubscriptionPlanActivity;
import com.chicsol.marrymax.dialogs.dialogContactDetails;
import com.chicsol.marrymax.dialogs.dialogFeedBackPending;
import com.chicsol.marrymax.dialogs.dialogProfileCompletion;
import com.chicsol.marrymax.dialogs.dialogRequestPhone;
import com.chicsol.marrymax.dialogs.dialogWithdrawInterest;
import com.chicsol.marrymax.fragments.inbox.DashboardMessagesDetailActivity;
import com.chicsol.marrymax.interfaces.PhoneRequestCallBackInterface;
import com.chicsol.marrymax.interfaces.RequestCallbackInterface;
import com.chicsol.marrymax.interfaces.WithdrawRequestCallBackInterface;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.modal.mMemPhone;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.urls.Urls;
import com.chicsol.marrymax.utils.Constants;
import com.chicsol.marrymax.utils.Cryptography_Android;
import com.chicsol.marrymax.utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static com.chicsol.marrymax.utils.Constants.defaultSelectionsObj;
import static com.chicsol.marrymax.utils.Constants.jsonArraySearch;

/**
 * Created by muneeb on 6/13/17.
 */

public class MarryMax {
    Activity activity;
    View view;

    private PhoneRequestCallBackInterface phoneRequestCallBackInterface;
    private WithdrawRequestCallBackInterface withdrawRequestCallBackInterface;
    private RequestCallbackInterface requestCallbackInterface;
    private final int phoneViewRequestDetail = 5, sendMessage = 6, blockReportConcernMatchAidFavourite = 7, addNotesAddToList = 8;


    public MarryMax(Activity activity) {
        this.activity = activity;

    }


    public void setPhoneViewRequestInterface(PhoneRequestCallBackInterface phoneViewRequestInterface) {
        this.phoneRequestCallBackInterface = phoneViewRequestInterface;


    }


    public void setWithdrawRequestCallBackInterface(WithdrawRequestCallBackInterface withdrawRequestCallBackInterface) {
        this.withdrawRequestCallBackInterface = withdrawRequestCallBackInterface;


    }

    public void setRequestCallBackInterface(RequestCallbackInterface withdrawRequestCallBackInterface) {
        this.requestCallbackInterface = withdrawRequestCallBackInterface;


    }


    public void getProfileProgress(final Context context, final Members member, final Activity activity) {
        //   Log.e("URL----", "" + Urls.getProgressbar + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
        // pDialog.setMessage("getProfileProgress");
        //pDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Urls.getProgressbar + SharedPreferenceManager.getUserObject(context).getPath(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("res progress", response + "");
                        try {


                            int registration_within_id = response.getInt("id");


                            if (member.getMember_status() == 0) {

                                if (registration_within_id == 0) {
                                    Intent intent = new Intent(activity, RegisterGeographicActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    activity.finish();
                                } else if (registration_within_id == 20) {
                                    Intent intent = new Intent(activity, RegisterAppearanceActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    activity.finish();
                                } else if (registration_within_id == 35) {
                                    Intent intent = new Intent(activity, RegisterLifeStyleActivity1.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    activity.finish();
                                } else if (registration_within_id == 55) {
                                    Intent intent = new Intent(activity, RegisterLifeStyleActivity2.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    activity.finish();
                                } else if (registration_within_id == 70) {
                                    Intent intent = new Intent(activity, RegisterInterest.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    activity.finish();
                                } else if (registration_within_id == 80) {
                                    Intent intent = new Intent(activity, RegisterPersonalityActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    activity.finish();
                                }


                            } else if (member.getMember_status() < 3) {
                                if (registration_within_id < 100) {
                                    if (registration_within_id == 70) {
                                        Intent intent = new Intent(activity, RegisterInterest.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                        activity.finish();
                                    } else if (registration_within_id == 80) {
                                        Intent intent = new Intent(activity, RegisterPersonalityActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                        activity.finish();
                                    } else if (registration_within_id == 90) {
                                        Intent intent = new Intent(activity, RegisterInterest.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                        activity.finish();
                                    } else {
                                        Intent intent = new Intent(activity, RegisterGeographicActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                        activity.finish();
                                    }
                                } else {
                                    Intent intent = new Intent(activity, RegisterGeographicActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    activity.finish();

                               /*     Intent in = new Intent(activity, MainDirectiveActivity.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.putExtra("type", 22);
                                    context.startActivity(in);*/
                                    //  activity.finish();

                                }

                            } else {
                                Intent intent = new Intent(activity, RegisterGeographicActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                activity.finish();

                              /*  Intent in = new Intent(activity, MainDirectiveActivity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.putExtra("type", 22);
                                context.startActivity(in);*/
                                //  activity.finish();

                            }

                        } catch (JSONException e) {
                            //   pDialog.dismiss();
                            e.printStackTrace();
                        }

                        //  pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                //  pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };




/*        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);

    }


    public void getProfileProgress(final Class cls, final Activity activity, final Context context, final Members member) {
        //  Log.e("URL----", "" + Urls.getProgressbar + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
        //   final ProgressDialog pDialog = new ProgressDialog(activity);
        //   pDialog.setMessage("Loading...");
//        pDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Urls.getProgressbar + SharedPreferenceManager.getUserObject(context).getPath(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("res progress", response + "");
                        try {


                            int registration_within_id = response.getInt("id");


                            if (member.getMember_status() == 0) {


                                switch (cls.getSimpleName()) {

                                    case "RegisterGeographicActivity": {
                                        //     pDialog.dismiss();
                                        Intent intent = new Intent(activity, cls);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                        activity.finish();
                                    }
                                    break;
                                    case "RegisterAppearanceActivity":

                                        if (registration_within_id >= 20) {
                                            //     pDialog.dismiss();

                                            Intent intent = new Intent(activity, cls);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                            activity.finish();
                                        }
                                        //error message
                                        //  else {}

                                        break;

                                    case "RegisterLifeStyleActivity1":
                                        if (registration_within_id >= 35) {
                                            //   pDialog.dismiss();
                                            Intent intent = new Intent(activity, cls);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                            activity.finish();
                                        }
                                        break;
                                    case "RegisterLifeStyleActivity2":
                                        if (registration_within_id >= 55) {
                                            //    pDialog.dismiss();
                                            Intent intent = new Intent(activity, cls);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                            activity.finish();
                                        }
                                        break;

                                    case "RegisterInterest":
                                        if (registration_within_id >= 70) {
                                            //   pDialog.dismiss();
                                            Intent intent = new Intent(activity, cls);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                            activity.finish();
                                        }
                                        break;
                                    case "RegisterPersonalityActivity":
                                        if (registration_within_id >= 80) {
                                            //   pDialog.dismiss();
                                            Intent intent = new Intent(activity, cls);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                            activity.finish();
                                        }
                                        break;


                                }


                            } else {
                                Intent intent = new Intent(activity, cls);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                activity.finish();

                            }

                        } catch (JSONException e) {
                            // pDialog.dismiss();
                            e.printStackTrace();
                        }

                        //      pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e("res err", "Error: " + error);
                // Toast.makeText(RegistrationActivity.this, "Incorrect Email or Password !", Toast.LENGTH_SHORT).show();

                // pDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Constants.getHashMap();
            }
        };




/*    jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);

    }


    //cat 1=view profile
    public boolean statusBaseChecks(Members member, Context context, int category, final FragmentManager frgMngr, Fragment fragment, View view, String memberDataList, String selectedPosition, Members memResultsObj, String TAG) {

        Members smember = SharedPreferenceManager.getUserObject(context);

        //  Log.e("member type", smember.getMember_status() + "-------" + member.getOpen_message());
        switch (category) {

            //View Profile
            case 1:

                String compUptoSSeventyText = "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to <b> <font color=#9a0606>Complete Your Profile up to 70% and Verify Phone or Email </font></b> before you can view the complete profile details.";
                if (smember.getMember_status() == 0) {

                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", compUptoSSeventyText, "Complete Profile", 8);
                    dialogP.show(frgMngr, "d");
                    return false;

                } else if (smember.getMember_status() < 3 && smember.getPhone_verified() == 0 && smember.getEmail_verified() == 0) {
                    // Toast.makeText(context, "Clicked ", Toast.LENGTH_SHORT).show();

                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", compUptoSSeventyText, "Complete Profile", 8);
                    dialogP.show(frgMngr, "d");
                    return false;

                } else if (smember.getMember_status() >= 7) {

                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, Please review notes as MarryMax team advised and update your profile or contact us for further assistance", "Complete Profile", 8);
                    dialogP.show(frgMngr, "d");
                    return false;

                } else {

                    memResultsObj.setUserpath(member.getUserpath());
                    Intent intent = new Intent(context, UserProfileActivityWithSlider.class);


                    //Log.e("Tag is", TAG + "==");
                    if (TAG.equals("SavedNotes") || TAG.equals("AccpetedMembers") || TAG.equals("FavouriteMembers") || TAG.equals("searchByAlias") || TAG.equals("featured")) {


                        Gson gsonc;
                        GsonBuilder gsonBuilderc = new GsonBuilder();
                        gsonc = gsonBuilderc.create();


                        Type listType = new TypeToken<List<Members>>() {
                        }.getType();

                        List<Members> membersDataLista = (List<Members>) gsonc.fromJson(memberDataList, listType);

                        Members seletedMember = membersDataLista.get(Integer.parseInt(selectedPosition));
                        membersDataLista.clear();
                        membersDataLista.add(seletedMember);


                        //Log.e("membersDataLista", membersDataLista.size() + "");

                        intent.putExtra("selectedposition", "-1");
                        SharedPreferenceManager.setMemberDataList(context, gsonc.toJson(membersDataLista));
                    } else {
                        intent.putExtra("selectedposition", selectedPosition);
                        SharedPreferenceManager.setMemberDataList(context, memberDataList);
                    }
                  /*  intent.putExtra("selectedposition", selectedPosition);
                    SharedPreferenceManager.setMemberDataList(context, memberDataList);*/

                    //Log.e("Path", "" + member.getPath());
                    //Log.e("UserPath", "" + member.getUserpath());

                    Gson gson = new Gson();
                    intent.putExtra("memresult", gson.toJson(memResultsObj));
                    intent.putExtra("userpath", member.getUserpath());

                    //  SharedPreferenceManager.setMemResultsObject(context, memResultsObj);
                    context.startActivity(intent);
                    return false;
                }


                //user interaction
            case 2:


                if (smember.getMember_status() <= 1 && smember.getPhone_verified() == 0) {

                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to <b> <font color=#9a0606>Complete Your Profile, Verify Phone and Email</font></b> before you can start interacting with other members.", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;

                } else if (smember.getMember_status() < 3) {
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to <b> <font color=#9a0606>Complete Your Profile, Verify Phone and Email</font></b> before you can start interacting with other members.", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;

                } else if (smember.getMember_status() == 3) {
                    return true;

                } else if (smember.getMember_status() == 4) {
                    return true;


                } else if (smember.getMember_status() == 7 || smember.getMember_status() == 8) {

                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, Please review notes as MarryMax team advised and update your profile or contact us for further assistance", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;

                }

                break;
//Remove User
            case 3:


                if (smember.getMember_status() <= 1 && smember.getPhone_verified() == 0) {

                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to <b> <font color=#9a0606>Complete Your Profile, Verify Phone and Email</font></b> before you can start interacting with other members.", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;

                } else if (smember.getMember_status() < 3) {
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to <b> <font color=#9a0606>Complete Your Profile, Verify Phone and Email</font></b> before you can start interacting with other members.", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;

                } else if (smember.getMember_status() == 3) {
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to upgrade your account and become a subscriber to use this feature.", "Subscribe", 9);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    /*you need to upgrade your account and become a subscriber to use this feature.*/
                    return false;

                } else if (smember.getMember_status() == 4) {
                    return true;


                } else if (smember.getMember_status() == 7 || smember.getMember_status() == 8) {

                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, Please review notes as MarryMax team advised and update your profile or contact us for further assistance", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;

                }
                break;

            //PhoneView  Request
            case 4:
                if (smember.getMember_status() <= 1 && smember.getPhone_verified() == 0) {

                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to <b> <font color=#9a0606>Complete Your Profile, Verify Phone and Email</font></b> before you can start interacting with other members.", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;

                } else if (smember.getMember_status() < 3) {
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to <b> <font color=#9a0606>Complete Your Profile, Verify Phone and Email</font></b> before you can start interacting with other members.", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;
                } else if (smember.getMember_status() == 3) {
                    viewRequestPhone(member, context, frgMngr, fragment, 3);
                    return false;

                } else if (smember.getMember_status() == 4) {
                    viewRequestPhone(member, context, frgMngr, fragment, 4);
                    return false;
                } else if (smember.getMember_status() == 7 || smember.getMember_status() == 8) {
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, Please review notes as MarryMax team advised and update your profile or contact us for further assistance", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;

                }
                break;
            //PhoneView  Request Detail
            case phoneViewRequestDetail:
                /*        if (feedback_due == 0) {*/


                if (smember.getMember_status() <= 1 && smember.getPhone_verified() == 0) {

                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to <b> <font color=#9a0606>Complete Your Profile, Verify Phone and Email</font></b> before you can start interacting with other members.", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;

                } else if (smember.getMember_status() < 3) {
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to <b> <font color=#9a0606>Complete Your Profile, Verify Phone and Email</font></b> before you can start interacting with other members.", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;
                } else if (smember.getMember_status() == 3) {
                    viewRequestPhoneDetail(member, context, frgMngr, fragment, 3);
                    return false;

                } else if (smember.getMember_status() == 4) {
                    viewRequestPhoneDetail(member, context, frgMngr, fragment, 4);
                    return false;
                } else if (smember.getMember_status() == 7 || smember.getMember_status() == 8) {
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, Please review notes as MarryMax team advised and update your profile or contact us for further assistance", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;

                }

             /*   } else {
                    //show feedback pending dialog
                }*/

                break;

            case sendMessage:


                if (smember.getMember_status() < 3 || smember.getMember_status() >= 7) {
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to <b> <font color=#9a0606>Complete Your Profile, Verify Phone and Email</font></b> before you can start interacting with other members.", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;
                } else if (smember.getMember_status() == 3) {

                  /*  Intent in = new Intent(activity, DashboardMessagesDetailActivity.class);
                    Gson gson = new Gson();
                    String memString = gson.toJson(member);
                    //  in.putExtra("obj", memString);
                    SharedPreferenceManager.setMessageObject(context, "");
                    SharedPreferenceManager.setMessageObject(context, memString);
                    in.putExtra("objtype", 0);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);*/
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Message", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>. Please subscribe to send personalized message and connect with the potential matches immediately.", member.getAccept_message() + "", 23);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;
                } /*else if (smember.getMember_status() ==4 && member.getOpen_message() == 0) {
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, User accept message only from his own matches.", "Subscribe", 9);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");

                } */ else if (smember.getMember_status() == 4) {

                    if (member.getOpen_message() == 0) {
                        dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, User accept message only from his own matches.", "", 10);
                        dialogP.setTargetFragment(fragment, 0);
                        dialogP.show(frgMngr, "d");

                    } else {

                        Intent in = new Intent(activity, DashboardMessagesDetailActivity.class);
                        Gson gson = new Gson();
                        String memString = gson.toJson(member);
                        // in.putExtra("obj", memString);
                        SharedPreferenceManager.setMessageObject(context, "");
                        SharedPreferenceManager.setMessageObject(context, memString);
                        in.putExtra("objtype", 0);
                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(in);

                        return false;
                    }
                }

            case blockReportConcernMatchAidFavourite:
                if (smember.getMember_status() < 3) {
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to <b> <font color=#9a0606>Complete Your Profile, Verify Phone and Email</font></b> before you can start interacting with other members.", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;
                } else {

                    return true;
                }


            case addNotesAddToList:

                if (smember.getMember_status() <= 2 || smember.getMember_status() >= 7) {

                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to <b> <font color=#9a0606>Complete Your Profile, Verify Phone and Email</font></b> before you can start interacting with other members or use available options.", "Complete Profile", 8);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");
                    return false;

                } else if (smember.getMember_status() == 3) {
                    dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, you need to upgrade your account and become a subscriber to use this feature.", "Subscribe", 9);
                    dialogP.setTargetFragment(fragment, 0);
                    dialogP.show(frgMngr, "d");

                    return false;

                } else if (smember.getMember_status() == 4) {
                    return true;


                }
                break;


            default:
                return true;


        }

        return false;
    }

    private void viewRequestPhoneDetail(Members member, Context context, FragmentManager frgMngr, Fragment fragment, int status) {
        if (member.getPhone_request_id() == 0) {
            if (member.getPhone_verified() == 2) {
                if (member.getHide_phone() == 0 || member.getAllow_phone_view() > 0) {
                    //greeen

                    if (status == 4) {
                        JSONObject params = new JSONObject();
                        try {
                            params.put("userpath", member.getUserpath());
                            params.put("path", SharedPreferenceManager.getUserObject(context).getPath());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getMobileInfo(params, member.getAlias(), context, fragment, frgMngr);
                    } else {
                        dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, verified contact phone number is available to subscribers only. Please upgrade your account.", "Subscribe", 9);
                        dialogP.setTargetFragment(fragment, 0);
                        dialogP.show(frgMngr, "d");

                    }

                } else {


                    //     member.phone_request_id
                    //if 0  request  else withdraw id  and call withdraw request


                    //    orange
                    JSONObject params = new JSONObject();
                    try {
                        params.put("alias", SharedPreferenceManager.getUserObject(context).getAlias());
                        params.put("type", "5");
                        params.put("userpath", member.getUserpath());
                        params.put("path", SharedPreferenceManager.getUserObject(context).getPath());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String desc = "Request <b> <font color=#216917>" + member.getAlias() + "</font></b>" + " for  Contact Details";

                    dialogRequestPhone newFragment = dialogRequestPhone.newInstance(params.toString(), "Request Contact Details", desc);

                    newFragment.setListener(phoneRequestCallBackInterface);

                    if (fragment != null) {
                        newFragment.setTargetFragment(fragment, 0);
                    }
                    newFragment.show(frgMngr, "dialog");

                }

            } else {

                //default
                String desc = "Contact details of <b> <font color=#216917>" + member.getAlias() + "</font></b>" + " are not available to public.";
                dialogRequestPhone newFragment = dialogRequestPhone.newInstance(null, "Notification", desc);

                newFragment.setListener(phoneRequestCallBackInterface);
                if (fragment != null) {

                    newFragment.setTargetFragment(fragment, 0);
                }
                newFragment.show(frgMngr, "dialog");
            }
        } else {
            JSONObject params = new JSONObject();
            try {
                params.put("userpath", member.getUserpath());
                params.put("path", SharedPreferenceManager.getUserObject(context).getPath());
                params.put("interested_id", member.getPhone_request_id());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String desc = "Are you sure to withdraw your  request for <b> <font color=#216917>" + member.getAlias() + "</font></b>";

            withdrawInterest(params, "Withdraw Contact Details", desc, fragment, frgMngr, "4");

        }


    }

    private void viewRequestPhone(Members member, Context context, FragmentManager frgMngr, Fragment fragment, int status) {

        if (member.getPhone_view() == 2 || member.getPhone_privilege_id() > 0) {
            //see mobile  green
            // Log.e("Green", "Green");
            if (status == 4) {

                JSONObject params = new JSONObject();
                try {
                    params.put("userpath", member.getUserpath());
                    params.put("path", SharedPreferenceManager.getUserObject(context).getPath());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getMobileInfo(params, member.getAlias(), context, fragment, frgMngr);
            } else {
                dialogProfileCompletion dialogP = dialogProfileCompletion.newInstance("Notification", "Dear <b> <font color=#216917>" + SharedPreferenceManager.getUserObject(context).getAlias() + "</font></b>, verified contact phone number is available to subscribers only. Please upgrade your account.", "Subscribe", 9);
                dialogP.setTargetFragment(fragment, 0);
                dialogP.show(frgMngr, "d");

            }


        } else if (member.getPhone_view() == 1 && member.getPhone_privilege_id() == 0) {
            //orange
            if (member.getPhone_request_id() > 0) {

                JSONObject params = new JSONObject();
                try {

                    // userpath
                    params.put("userpath", member.getUserpath());
                    params.put("path", SharedPreferenceManager.getUserObject(context).getPath());
                    params.put("interested_id", member.getPhone_request_id());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String desc = "Are you sure to withdraw your  request for <b> <font color=#216917>" + member.getAlias() + "</font></b>";

                withdrawInterest(params, "Withdraw Contact Details", desc, fragment, frgMngr, "4");


            } else {


                JSONObject params = new JSONObject();
                try {
                    params.put("alias", SharedPreferenceManager.getUserObject(context).getAlias());
                    params.put("type", "5");
                    params.put("userpath", member.getUserpath());
                    params.put("path", SharedPreferenceManager.getUserObject(context).getPath());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String desc = "Request <b> <font color=#216917>" + member.getAlias() + "</font></b>" + " for  Contact Details";

                dialogRequestPhone newFragment = dialogRequestPhone.newInstance(params.toString(), "Request Contact Details", desc);
                newFragment.setListener(phoneRequestCallBackInterface);
                if (fragment != null) {
                    newFragment.setTargetFragment(fragment, 0);
                }
                newFragment.show(frgMngr, "dialog");

            }
        } else if (member.getPhone_view() == 0 && member.getPhone_privilege_id() == 0) {
            //grey
            //default
            String desc = "Contact details of <b> <font color=#216917>" + member.getAlias() + "</font></b>" + " are not available to public.";
            dialogRequestPhone newFragment = dialogRequestPhone.newInstance(null, "Notification", desc);

            newFragment.setListener(phoneRequestCallBackInterface);
            if (fragment != null) {
                newFragment.setTargetFragment(fragment, 0);
            }
            newFragment.show(frgMngr, "dialog");
        }

    }

    public void getMobileInfo(JSONObject params, final String alias, Context context, final Fragment fragment, final FragmentManager frgMngr) {

        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //Log.e("params mobile", params.toString());
        //Log.e("mobile U RL ", Urls.mobileInfo);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Urls.mobileInfo, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("mobileInfo  ", response + "");

                        try {
                            JSONObject responseObject = response.getJSONArray("data").getJSONArray(0).getJSONObject(0);


                            ///Log.e("interested id", "" + member.getAlias() + "====================");


                            Gson gson;
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gson = gsonBuilder.create();
                            Type type = new TypeToken<mMemPhone>() {
                            }.getType();
                            mMemPhone memPhone = (mMemPhone) gson.fromJson(responseObject.toString(), type);

                            //    if (memPhone.getFeedback_due() == 0) {

                            dialogContactDetails newFragment = dialogContactDetails.newInstance(responseObject.toString(), alias, memPhone.getFeedback_due());
                            if (fragment != null) {
                                newFragment.setTargetFragment(fragment, 3);
                            }
                            newFragment.show(frgMngr, "dialog");
                        /*    } else {


                                String aliasn = "<font color='#9a0606'>" + alias + "!</font><br>";


                       *//*         if (memPhone.getFeedback_due() == 1) {
                                    String text = "Dear " + "<b>" + aliasn.toUpperCase() + "</b> your Feedback is Pending.To view more profiles please give your previous feedback";

                                    dialogFeedBackPending newFragment = dialogFeedBackPending.newInstance(text, false);
                                    newFragment.setTargetFragment(fragment, 3);

                                    newFragment.show(frgMngr, "dialog");


                                } else if (memPhone.getFeedback_due() == 2) {*//*


                                String text = "Dear " + "<b>" + aliasn.toUpperCase() + "</b> Your feedbacks are due. To view more profiles please give your previous feedbacks";

                                dialogFeedBackPending newFragment = dialogFeedBackPending.newInstance(text, true);
                                if (fragment != null) {
                                    newFragment.setTargetFragment(fragment, 3);
                                }
                                newFragment.show(frgMngr, "dialog");


                                //  }


                            }*/

                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(activity, "Your have reached the maximum limit of your phone numbers quota.", Toast.LENGTH_LONG).show();
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
                //return Constants.getHashMap();
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

    public void withdrawInterest(JSONObject params, String title, String desc, Fragment fragment, FragmentManager frgMngr, String type) {


        dialogWithdrawInterest newFragment = dialogWithdrawInterest.newInstance(params, title, desc, type);
        newFragment.setListener(withdrawRequestCallBackInterface);
        if (fragment != null) {
            newFragment.setTargetFragment(fragment, 3);
        }
        newFragment.show(frgMngr, "dialog");

    }

    public void subscribe() {
        // Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.subscriptionUrl));
        //  activity.startActivity(browserIntent);

        Intent intent = new Intent(activity, SubscriptionPlanActivity.class);
        activity.startActivity(intent);

    }

    public void aboutus() {
      /*  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.about));
        activity.startActivity(browserIntent);*/

        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", Urls.about);
        intent.putExtra("title", "About");
        activity.startActivity(intent);


    }

    public void benefits() {
      /*  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.benefits));
        activity.startActivity(browserIntent);*/


        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", Urls.benefits);
        intent.putExtra("title", "Membership Plans");
        activity.startActivity(intent);


    }

    public void personalizedMatching() {
      /*  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.matchMaking));
        activity.startActivity(browserIntent);*/

        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("title", "Personalized Matching");
        intent.putExtra("url", Urls.matchMaking);
        activity.startActivity(intent);


    }

    public void faq() {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", Urls.faqs);
        intent.putExtra("title", "FAQs");
        activity.startActivity(intent);

    }


    public void PrivacyPolicy() {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", Urls.privacyPolicy);
        intent.putExtra("title", "Privacy Policy");
        activity.startActivity(intent);

    }

    public void ProfileGuideline() {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", Urls.profileGuideline);
        intent.putExtra("title", "Profile Guideline");
        activity.startActivity(intent);

    }

    public void SecurityTip() {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", Urls.securityTip);
        intent.putExtra("title", "Security Tip");
        activity.startActivity(intent);

    }

    public void TermsofUse() {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", Urls.termsofUse);
        intent.putExtra("title", "Terms Of Use");
        activity.startActivity(intent);

    }

    public void WhyMarryMax() {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", Urls.whyMarryMax);
        intent.putExtra("title", "Why MarryMax");
        activity.startActivity(intent);

    }


    public void contact() {
       /* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.faqs));
        activity.startActivity(browserIntent);*/

        Intent intent = new Intent(activity, ContactAcivity.class);
        //  intent.putExtra("url", Urls.faqs);
        //    intent.putExtra("title", "FAQs");
        activity.startActivity(intent);

    }

    public boolean uploadPhotoCheck(Context context) {
        if (SharedPreferenceManager.getUserObject(context).getMember_status() != 0) {
            return true;
        } else {
            Toast.makeText(context, "Please complete your profile first to upload photos", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public int getTimeDifference(Long ticks) {

        Calendar cTime = Calendar.getInstance();

        Calendar xTime = Calendar.getInstance();
        xTime.setTimeInMillis(ticks);


        Long mVal = ticks - 621355968000000000L;
        Long mSecValue = mVal / 10000;

        return (int) (((cTime.getTimeInMillis() - mSecValue) / (1000 * 60)));


    }

    public String getAccessToken() {
        Calendar cl2 = Calendar.getInstance();

        Long ticks = 621355968000000000L + cl2.getTimeInMillis() * 10000;
        Log.e("ticks ", ticks + "");
        Cryptography_Android crypt = new Cryptography_Android();
        String encryptedValue = null;
        try {
            encryptedValue = crypt.Encrypt(Urls.ARDTOKEN + "***" + ticks, Urls.PassPhraseArdAp);
            //120 minutes check
            //    Log.e("encrypted value ", encryptedValue + "");
            return "ARD" + encryptedValue + "-345";
            //addd  start    ARD  end
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }

    public String decryptAccessToken(String encrypted) {

        if (encrypted != null) {
            //Log.e("strin after encryption", encrypted);

            encrypted = encrypted.replace("ARD", " ");
            encrypted = encrypted.replace("-345", " ");
            // Log.e("strin after encryption", encrypted);

            String decryptedValue;
            Cryptography_Android crypt = new Cryptography_Android();

            try {
                decryptedValue = crypt.Decrypt(encrypted, Urls.PassPhraseArdAp);

                decryptedValue = decryptedValue.replace(Urls.ARDTOKEN + "***", "");
                //Log.e("Decrypted value ", decryptedValue + "");

                int min = getTimeDifference(Long.parseLong(decryptedValue));
                //Log.e("minutes ", min + "");

                return decryptedValue;
            } catch (Exception e) {

                e.printStackTrace();

                return null;
            }
        }
        return null;
    }


    public StringBuilder getSelectedTextFromList(List<WebArd> mDataLis, String option) {
        StringBuilder stringBuilder = new StringBuilder("");

        for (int i = 0; i < mDataLis.size(); i++) {

            if (mDataLis.get(i).isSelected()) {

                stringBuilder.append(mDataLis.get(i).getName());

                if (i != mDataLis.size() - 1) {
                    stringBuilder.append(",");
                }


            }

        }


        if (stringBuilder == null || stringBuilder.toString().equals("")) {

            return stringBuilder.append(option);
        } else {
            if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
                return stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            } else {
                return stringBuilder;
            }

        }


    }


    public StringBuilder getSelectedIdsFromList(List<WebArd> mDataLis) {
        StringBuilder stringBuilder = new StringBuilder("");

        for (int i = 0; i < mDataLis.size(); i++) {

            if (mDataLis.get(i).isSelected()) {

                stringBuilder.append(mDataLis.get(i).getId());

                if (i != mDataLis.size() - 1) {
                    stringBuilder.append(",");
                }


            }

        }


        if (stringBuilder == null || stringBuilder.toString().equals("")) {

            return stringBuilder.append("0");
        } else {
            if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
                return stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            } else {
                return stringBuilder;
            }

        }


    }


    public void getRawData(Context context, int matchPageType) {

       /* if (!pDialog.isShowing()) {
          //  pDialog.show();
           // pDialog.setMessage("getraw data");
        }*/
        String matchType = "";
        if (matchPageType == 1) {
            matchType = "/pm";
            //   //Log.e("==pm", "pm");
        } else if (matchPageType == 2) {
            matchType = "/lfm";
            //Log.e("==lfm", "lfm");
        } else {
            matchType = "/0";
            //Log.e("==0----", "0----");
        }
        //Log.e("getRawData started", Urls.getRawData + SharedPreferenceManager.getUserObject(context).getPath() + matchType);

        JsonArrayRequest req = new JsonArrayRequest(Urls.getRawData + SharedPreferenceManager.getUserObject(context).getPath() + matchType,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e("getRawData Response", response.toString());
                        try {
                            //       Log.e("getRawData finished===", "==========================");

                            JSONObject jsonCountryStaeObj = response.getJSONArray(0).getJSONObject(0);
                            //     Log.e("Response 222", jsonCountryStaeObj.toString());

                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<Members>() {
                            }.getType();
                            Constants.defaultMatchesObject = null;
                            DrawerActivity.rawSearchObj = null;
                            Constants.defaultSelectionsObj = gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                            DrawerActivity.rawSearchObj = gsonc.fromJson(jsonCountryStaeObj.toString(), listType);
                            //     pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
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
        MySingleton.getInstance(context).addToRequestQueue(req);
    }


    public void onSearchClicked(Context context, int check) {

        switch (check) {
            case 0://default
                if (jsonArraySearch == null) {
                    getSearchListData(context);
                } else {
                    Intent intent = new Intent(activity, SearchMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    // overridePendingTransition(R.anim.enter, R.anim.right_to_left);
                }
                break;
//for best match
            case 1://called in search searchyourbestmatchActivity
                Intent intent = new Intent(activity, SearchMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("searchcheck", true);
                context.startActivity(intent);

                break;


        }

    }

    //for getting default search data
    private void getSearchListData(final Context context) {

        //  Log.e("url", Urls.getSearchLists + SharedPreferenceManager.getUserObject(getApplicationContext()).getPath());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getSearchListsAdv + SharedPreferenceManager.getUserObject(context).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {

                            jsonArraySearch = response;
                        } else {
                            Intent intent = new Intent(activity, SearchMainActivity.class);
                            context.startActivity(intent);
                        }

                        //  pDialog.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());

                // pDialog.setVisibility(View.INVISIBLE);
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(req);
    }

  /*  public void updateStatus(final Context context) {

        // pDialog.show();
        Log.e("status URL", Urls.getProfileCompletion + SharedPreferenceManager.getUserObject(context).getPath());
        JsonArrayRequest req = new JsonArrayRequest(Urls.getProfileCompletion + SharedPreferenceManager.getUserObject(context).getPath(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("updateStatus Response", response.toString());


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
                            if (dashboards.getAdmin_approved_status().equals("1") && members.getMember_status() < 3) {
                                if (members.getMember_status() == 1) {
                                    if (dashboards.getEmail_complete_status().equals("1") && dashboards.getPhone_complete_status().equals("1") && dashboards.getProfile_complete_status().equals("100")) {
                                       *//* members.setMember_status(3);

                                        SharedPreferenceManager.setUserObject(context, members);*//*
                                    } else {
                                       *//* members.setMember_status(2);
                                        SharedPreferenceManager.setUserObject(context, members);*//*
                                    }

                                } else if (members.getMember_status() == 2) {
                                    if (dashboards.getEmail_complete_status().equals("1") && dashboards.getPhone_complete_status().equals("1") && dashboards.getProfile_complete_status().equals("100")) {
                                       *//* members.setMember_status(3);
                                        SharedPreferenceManager.setUserObject(context, members);*//*
                                    }

                                }
                            }

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
        MySingleton.getInstance(context).addToRequestQueue(req);
    }*/


    public boolean getUpdateCheck(Context context) {

        if (SharedPreferenceManager.getUserObject(context).getMember_status() == 3 || SharedPreferenceManager.getUserObject(context).getMember_status() == 4) {
            return true;
        } else {

            return false;
        }
    }


    public String convertUTCTimeToLocal(String inputDate) {
        boolean am = false;
        if (inputDate.contains("AM")) {
            am = true;
        }

        try {
            String dateStr = inputDate;
            //"01/17/2018 09:20:16 AM";
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a", Locale.ENGLISH);
            SimpleDateFormat df2 = new SimpleDateFormat("dd MMM, yyyy h:mm a", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;

            date = df.parse(dateStr);

            df.setTimeZone(TimeZone.getDefault());
            String formattedDate = df2.format(date);

           /* if (am) {
                if (formattedDate.contains("PM")) {
                    formattedDate = formattedDate.replace("PM", "AM");
                }
            } else {
                if (formattedDate.contains("AM")) {
                    formattedDate = formattedDate.replace("AM", "PM");
                }
            }
*/

            //
            //
            //  Log.e("local time is", formattedDate);
            return formattedDate;


        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }


    }


    public String convertUTCTimeToLocalWithAgo(String inputDate) {
        boolean am = false;
        if (inputDate.contains("AM")) {
            am = true;
        }

        try {
            String dateStr = inputDate;
            //"01/17/2018 09:20:16 AM";
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a", Locale.ENGLISH);
            SimpleDateFormat df2 = new SimpleDateFormat("dd MMM, yyyy h:mm a", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;

            date = df.parse(dateStr);

            df.setTimeZone(TimeZone.getDefault());


            long time = date.getTime();
            long now = System.currentTimeMillis();

            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

            //  Log.e("local time is", formattedDate);
            return ago.toString();


        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }


    }


    public boolean checkUserStatusLogin(Members member) {
        if (member.getRegistration_within_id() != 5 && member.getMember_status() == 0) {
            if (member.getRegistration_within_id() == -1) {
                Intent intent = new Intent(activity, RegisterGeographicActivity.class);
                activity.startActivity(intent);
                activity.finish();
                return false;
                //  HttpContext.Current.Response.Redirect("~/1_Geographic", false);
            } else if (member.getRegistration_within_id() == 0) {
                Intent intent = new Intent(activity, RegisterAppearanceActivity.class);
                activity.startActivity(intent);
                activity.finish();
                return false;
                // HttpContext.Current.Response.Redirect("~/2_Appearance", false);
            } else if (member.getRegistration_within_id() == 1) {
                Intent intent = new Intent(activity, RegisterLifeStyleActivity1.class);
                activity.startActivity(intent);
                activity.finish();
                return false;
                //   HttpContext.Current.Response.Redirect("~/3_LifeStyle", false);
            } else if (member.getRegistration_within_id() == 2) {
                Intent intent = new Intent(activity, RegisterLifeStyleActivity2.class);
                activity.startActivity(intent);
                activity.finish();
                return false;
                //  HttpContext.Current.Response.Redirect("~/3_LifeStyle2", false);
            } else if (member.getRegistration_within_id() == 3) {
                Intent intent = new Intent(activity, RegisterInterest.class);
                activity.startActivity(intent);
                activity.finish();
                return false;
                // HttpContext.Current.Response.Redirect("~/4_Interest", false);
            } else if (member.getRegistration_within_id() == 4) {
                Intent intent = new Intent(activity, RegisterPersonalityActivity.class);
                activity.startActivity(intent);
                activity.finish();
                return false;
                //  HttpContext.Current.Response.Redirect("~/5_Personality", false);
            }


        } else {
            return true;


        }

        return false;
    }


    public void setHeighAgeChecks() {
        if (jsonArraySearch != null) {
            if (defaultSelectionsObj.getChoice_age_from() == 0) {
                defaultSelectionsObj.setChoice_age_from(18);
            }
            if (defaultSelectionsObj.getChoice_age_upto() == 0) {
                defaultSelectionsObj.setChoice_age_upto(70);
            }

            Gson gsonc;
            GsonBuilder gsonBuilderc = new GsonBuilder();
            gsonc = gsonBuilderc.create();
            Type listType = new TypeToken<List<WebArd>>() {
            }.getType();
            List<WebArd> dataListHeight = new ArrayList<>();
            try {
                dataListHeight = (List<WebArd>) gsonc.fromJson(jsonArraySearch.getJSONArray(10).toString(), listType);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (dataListHeight.size() > 0) {
                if (defaultSelectionsObj.getChoice_height_from_id() == 0) {
                    defaultSelectionsObj.setChoice_height_from_id(Long.parseLong(dataListHeight.get(0).getId()));
                }
                if (defaultSelectionsObj.getChoice_height_to_id() == 0) {
                    defaultSelectionsObj.setChoice_height_to_id(Long.parseLong(dataListHeight.get(dataListHeight.size() - 1).getId()));
                }
            }
        }
    }


    public void getAppVersion(final Context context) {


        //Log.e(" getAppVersion url", Urls.getAppVersion);
        StringRequest req = new StringRequest(Urls.getAppVrsn,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                 //     Log.e("getAppVersion ==", "=======================  " + remoteVersionName);
                     //   remoteVersionName = remoteVersionName.replaceAll("^\"|\"$", "");

                        //String remoteVersionName = "1.0";
                        //   String version = data[2];


                        try {




                            Gson gsonc;
                            GsonBuilder gsonBuilderc = new GsonBuilder();
                            gsonc = gsonBuilderc.create();
                            Type listType = new TypeToken<WebArd>() {
                            }.getType();

                           WebArd obj = (WebArd) gsonc.fromJson(response.toString(), listType);



                            try {
                                PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
                                String localVersion = pInfo.versionName;

                             //   obj.setName("2.0");
                               //   Log.e("getAppVersion local", localVersion + " ----  " + obj.getName());

                                if (!obj.getName().equals(localVersion)) {
                                 //   Log.e("updatev now", response);
                                    updateVersion(context);
                                }


                                //   Log.e("versionName", "" + version);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }





                        } catch (Exception e) {
                            e.printStackTrace();
                        }







                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());

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
    }

  /*  public void checkVersionUpdate(String remoteVersionName) {

        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            String localVersion = pInfo.versionName;
            //Log.e("localVersion", localVersion + " ----  " + remoteVersionName);

            if (!remoteVersionName.equals(localVersion)) {
                //   Log.e("updatev now", remoteVersionName);
                updateVersion();
            }


            //   Log.e("versionName", "" + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    public String getFeedbackText(int peedback_pending, Context context) {
        Members membera = SharedPreferenceManager.getUserObject(context);

        String desc = null;
        if (peedback_pending == 1) {

            String txt = "<font color='#9a0606'>" + membera.getAlias() + "!</font>";
            return desc = " <b> Dear " + "" + txt.toUpperCase() + "</b> Please give us the feedback of a recently contacted match.";


        } else if (peedback_pending == 2) {

            String txt = "<font color='#9a0606'>" + membera.getAlias() + "!</font>";
            return desc = " <b> Dear " + "" + txt.toUpperCase() + "</b>  <font color='#9a0909'> Please give us the pending match feedback so we can help you better, thank you.";


        } else {
            return null;
        }
    }

//==================================================Update===========================================================

    public void updateVersion(Context context) {

      /*  if(!((Activity) context).isFinishing())
        {*/
            //show dialog

        AlertDialog dialog = new AlertDialog.Builder(activity, R.style.MyDialogTheme)
                .setTitle("New version available")
                .setCancelable(false)
                .setMessage("Please, update app to new version to continue .")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //redirectStore(updateUrl);

                                final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }

                            }
                        }).setNegativeButton("Cancel ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        }).create();
        dialog.show();

    }

//}

}
