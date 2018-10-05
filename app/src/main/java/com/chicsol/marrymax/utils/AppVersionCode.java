package com.chicsol.marrymax.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.chicsol.marrymax.preferences.PDefaultValue;


public class AppVersionCode
{

    /**
     * @return actual version code of apk
     */
    public static int getApkVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
           // PackageInfo packageInfo=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return PDefaultValue.VERSION_CODE;
    }
}