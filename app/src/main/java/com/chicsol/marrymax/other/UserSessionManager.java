package com.chicsol.marrymax.other;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.chicsol.marrymax.activities.ActivityLogin;
import com.chicsol.marrymax.activities.DrawerActivity;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;

import java.io.File;
import java.util.HashMap;

import me.leolin.shortcutbadger.ShortcutBadger;

public class UserSessionManager {

    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "marrymaxpref";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_MEMBER_ID = "memberid";
    public static final String KEY_MEMBER_STATUS = "memberstatus";


    // Constructor
    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String alias, String memberid,
                                       String memberstatus) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_ALIAS, alias);
        editor.putString(KEY_MEMBER_ID, memberid);
        editor.putString(KEY_MEMBER_STATUS, memberstatus);

        editor.commit();
    }

    /**
     * Check login method will check user login status If false it will redirect
     * user to login page Else do anything
     * */
    /*
     * public boolean checkLogin(){ // Check login status
     * if(!this.isUserLoggedIn()){
     *
     * // user is not logged in redirect him to Login Activity Intent i = new
     * Intent(_context, Login.class);
     *
     * // Closing all the Activities from stack
     * i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     *
     * // Add new Flag to start new Activity
     * i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     *
     * // Staring Login Activity _context.startActivity(i);
     *
     * return true; } else{
     *
     * Log.e("log in","loooooog"); Intent in=new
     * Intent(_context,MainScreen.class);
     * in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     *
     * _context.startActivity(in);
     *
     * } return false; }
     */

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {

        // Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ALIAS, pref.getString(KEY_ALIAS, null));
        user.put(KEY_MEMBER_STATUS, pref.getString(KEY_MEMBER_STATUS, null));
        user.put(KEY_MEMBER_ID, pref.getString(KEY_MEMBER_ID, null));

        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        ShortcutBadger.applyCount(_context, 0); //for 1.1.4+
        DrawerActivity.rawSearchObj = new Members();

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();
        // clearApplicationData();
        clearPreference();


        //_context.stopService(new Intent(_context, Data_Service.class));

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, ActivityLogin.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }

    public void clearPreference() {

        SharedPreferenceManager.clearMainPreference(_context);
    }

    public void clearApplicationData() {
        File cache = _context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG",
                            "**************** File /data/data/APP_PACKAGE/" + s
                                    + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}