package com.chicsol.marrymax.utils;


import android.util.SparseBooleanArray;

import com.chicsol.marrymax.modal.Members;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zeedr on 24/10/2016.
 */

public class Constants {
    public static boolean searchFromSavedListings = false;
    public static Members defaultSelectionsObj = new Members();
    public static Members defaultSearchsObj = new Members();

    public static Members defaultMatchesObject = null;
    public static JSONArray jsonArraySearch = null;
    public static String font_centurygothic = "fonts/Roboto_Regular.ttf";
    public static String font_centurygothic_bold = "fonts/Roboto_Bold.ttf";
    public static String font_centurygothic_italic = "fonts/centurygothic_Italic.ttf";
    public static String font_fa_awesome = "fonts/fontawesome_webfont.ttf";
    public static String accessToken = null;
    public static Map<String, String> headerMap;

    /*    static {
            //  params = new HashMap<String, String>();
            Map<String, String> aMap = new HashMap<String, String>();

            aMap.put("Accept", "application/json");
            aMap.put("Content-Type", "application/json; charset=utf-8");

            new functions().checkMinutes();

            aMap.put("token", accessToken);


            headerMap = aMap;
        }*/

    public static SparseBooleanArray selectedQuestions=new SparseBooleanArray();



    public static Map<String, String> getHashMap() {

        functions fun = new functions();


        Map<String, String> aMap = new HashMap<String, String>();

        aMap.put("Accept", "application/json");
        aMap.put("Content-Type", "application/json; charset=utf-8");


        aMap.put("token", fun.getToken());

        return aMap;

    }


}
