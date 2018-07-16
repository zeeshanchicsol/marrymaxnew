package com.chicsol.marrymax.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.chicsol.marrymax.modal.Members;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by redZze on 6/6/2016.
 */
public class SharedPreferenceManager {
    // Shared pref mode
    private static int PRIVATE_MODE = 0;
    private static final String MARRYMAX_MAIN_PREFER_NAME = "marrymaxpref";
    private static final String MARRYMAX_EMAIL_SUGGESTION_PREFER_NAME = "marrymax_email_suggestion_pref";

    public static final String USER_OBJECT = "user_object";
    public static final String MEM_RESULTS_OBJECT = "MEM_RESULTS_OBJECT";
    public static final String USER_MESSAGE_OBJECT = "user_message_object";
    public static final String DEFAULT_SEARCH_OBJECT = "default_search_object";
    public static final String MEMBERS_DATALIST = "members_datalist";
    public static final String SUGGESTION_LIST = "suggestion_list";


    public static final String USER_QUESTION_OBJ = "user_question_object";


    public static Members getUserObject(Context context) {

        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, PRIVATE_MODE);
        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        Members members = gson.fromJson(sharedPreferences.getString(USER_OBJECT, null), Members.class);
        return members;
    }

    public static void setUserObject(Context context, JSONObject userObject) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_OBJECT, userObject.toString());
        editor.commit();
    }

    public static void setUserObject(Context context, Members member) {
        Gson gson = new Gson();
        String memString = gson.toJson(member);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_OBJECT, memString);
        editor.commit();
    }

    public static void setMemberDataList(Context context, String datalist) {

        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MEMBERS_DATALIST, datalist);
        editor.commit();
    }


    public static String getMembersDataList(Context context) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, PRIVATE_MODE);
        String memdatalist = sharedPreferences.getString(MEMBERS_DATALIST, null);
        return memdatalist;
    }


    public static void setEmailSuggestionList(Context context, ArrayList arrayList) {

        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_EMAIL_SUGGESTION_PREFER_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(arrayList);
        Log.e("set json", json);
        editor.putString(SUGGESTION_LIST, json);
        editor.commit();


    }

    public static ArrayList getEmailSuggestionList(Context context) {

        SharedPreferences sharedPrefs = context
                .getSharedPreferences(MARRYMAX_EMAIL_SUGGESTION_PREFER_NAME, PRIVATE_MODE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(SUGGESTION_LIST, null);
        Log.e("get json", json + "");
        ArrayList arrayList = null;

        if (json != null) {
            Type type = new TypeToken<ArrayList>() {
            }.getType();
            return arrayList = gson.fromJson(json, type);
        } else {

            return null;
        }

    }


    public static Members getDefaultSelectionsObj(Context context) {

        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, PRIVATE_MODE);
        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        Members members = gson.fromJson(sharedPreferences.getString(DEFAULT_SEARCH_OBJECT, null), Members.class);
        return members;
    }


    public static void setDefaultSelectionsObj(Context context, Members member) {
        Gson gson = new Gson();
        String memString = gson.toJson(member);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEFAULT_SEARCH_OBJECT, memString);
        editor.commit();
    }


    public static void clearMainPreference(Context context) {
        SharedPreferences settings = context.getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }


    public static String getMessageObject(Context context) {

        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, PRIVATE_MODE);

        return sharedPreferences.getString(USER_MESSAGE_OBJECT, null);

    }

    public static void setMessageObject(Context context, String userObject) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_MESSAGE_OBJECT, userObject);
        editor.commit();
    }


    public static String getQuestionObject(Context context) {

        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, PRIVATE_MODE);

        return sharedPreferences.getString(USER_QUESTION_OBJ, null);

    }

    public static void setQuestionObject(Context context, String userObject) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_QUESTION_OBJ, userObject);
        editor.commit();
    }


    public static void setMemResultsObject(Context context, Members member) {
        Gson gson = new Gson();
        String memString = gson.toJson(member);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MEM_RESULTS_OBJECT, memString);
        editor.commit();
    }

    public static Members getMemResultsObject(Context context) {

        SharedPreferences sharedPreferences = context
                .getSharedPreferences(MARRYMAX_MAIN_PREFER_NAME, PRIVATE_MODE);
        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        Members members = gson.fromJson(sharedPreferences.getString(MEM_RESULTS_OBJECT, null), Members.class);
        return members;
    }


}
