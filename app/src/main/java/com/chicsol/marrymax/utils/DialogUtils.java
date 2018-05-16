package com.chicsol.marrymax.utils;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Noman on 1/3/2016.
 */
public class DialogUtils
{
    public static void showAlertDialog(Context context, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Alert");

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true).setPositiveButton("Ok", null);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }
}
