package com.chicsol.marrymax.utils;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


/**
 * Created by muneeb on 6/23/17.
 */

public class ConnectCheck {


    public static boolean isConnected(View view) {

        //  Add these lines for broadcast internet connection
       /*
        ConnectivityReceiver connectivityReceive;
        connectivityReceive = new ConnectivityReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceive, intentFilter);*/
//===OnDestroy
        /*unregisterReceiver(connectivityReceive);*/


        boolean isConnected = isConToInternet(view.getContext());


        if (!isConnected) {
            //Log.e("log==========", "No internet");
            String message;
            int color;
            message = "Please Connect to Internet first";
            color = Color.WHITE;
            Snackbar snackbar = Snackbar
                    .make(view, message, Snackbar.LENGTH_SHORT);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
            return false;

        } else {
            return true;
        }
    }

    private static boolean isConToInternet(Context context) {
/*        ConnectivityManager
                cm = (ConnectivityManager) App.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);*/

        ConnectivityManager
                cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

}
