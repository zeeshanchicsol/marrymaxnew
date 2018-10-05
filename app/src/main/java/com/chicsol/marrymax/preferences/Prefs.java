package com.chicsol.marrymax.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.chicsol.marrymax.App;

public final class Prefs {

    private static SharedPreferences get(final Context context) {
        return context.getSharedPreferences(App.package_name, Context.MODE_PRIVATE);
    }

    static String getPref(final Context context, String pref, String def) {
        SharedPreferences prefs = Prefs.get(context);
        String val = prefs.getString(pref, def);

        if (val == null || val.equals("") || val.equals("null"))
            return def;
        else
            return val;
    }

    static void putPref(final Context context, String pref, String val) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(pref, val);
        editor.apply();
    }

    private static int getIntPref(final Context context, String pref, int def) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getInt(pref, def);
    }

    private static void putIntPref(final Context context, String pref, int val) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(pref, val);
        editor.apply();
    }

    //================================================================================================
    private static int encryptInteger(int i) {
        /*todo*/
        return i;
    }

    private static int decryptInteger(int i) {
        /*todo*/
        return i;
    }

    //++++++++++++++++++++++ ENCRYPTED PREFERENCES START (1)++++++++++++++++++++++++++++++++++++++++

    //________________________________________________________________________________________ STAGE
    public static int getStage(final Context context) {
        int encryptedValue = Prefs.getIntPref(context, PKey.STAGE, encryptInteger(PDefaultValue.STAGE));
        return decryptInteger(encryptedValue);
    }

    public static void putStage(final Context context, int stage) {
        Prefs.putIntPref(context, PKey.STAGE, encryptInteger(stage));
    }
    //------------------------- ENCRYPTED PREFERENCES END (1) --------------------------------------


    public static int getApplicationVersionCode(final Context context) {
        return Prefs.getIntPref(context, PKey.VERSION_CODE, PDefaultValue.VERSION_CODE);
    }

    public static void putApplicationVersionCode(final Context context, int version_code) {
        Prefs.putIntPref(context, PKey.VERSION_CODE, version_code);
    }




}
