package com.chicsol.marrymax.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;

import com.chicsol.marrymax.R;


/**
 * Created by nomankhan25dec on 1/12/2016.
 */
public class Dialogs extends Dialog {

    private Dialog dialog;

    public Dialogs(Context context) {
        super(context);
    }


    /* public void showAutoSyncDialog(Context context, View.OnClickListener cancel, View.OnClickListener lisdone){
         if (null!=null)dialog.dismiss();
         dialog = new Dialog(context);

         dialog.setTitle("The Auto Sync Settings");
         dialog.setCancelable(false);
         dialog.setContentView(R.layout.auto_sync_dialog);

         Button dismiss = (Button) dialog.findViewById(R.id.dismissBtn);
         dismiss.setOnClickListener(cancel);
         Button btDone = (Button) dialog.findViewById(R.id.doneBtn);
         btDone.setOnClickListener(lisdone);

         dialog.show();
     }
 */
    public void showAutoSyncDialog(int selected, CharSequence[] items, Context context, DialogInterface.OnClickListener cancel, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener singleChoice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder
                .setCancelable(false)
                .setTitle("The Auto Sync Settings")
                .setNegativeButton("CANCEL", cancel)
                .setPositiveButton("Done", ok)
                .setSingleChoiceItems(items, selected, singleChoice);

        builder.show();

    }

    public void showCallDialog2(boolean[] checked, CharSequence[] items, Context context, DialogInterface.OnClickListener cancel, DialogInterface.OnClickListener ok, DialogInterface.OnMultiChoiceClickListener MulChoice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder
                .setCancelable(false)
                .setTitle("Retrieve Call Log")
                .setNegativeButton("CANCEL", cancel)
                .setPositiveButton("Done", ok)
                .setMultiChoiceItems(items, checked, MulChoice);

        builder.show();

    }

    public void showCallDialog(View.OnClickListener ok, View.OnClickListener cancel) {



        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_profilefor);
        dialog.setCancelable(false);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;




/*
        Button dismiss = (Button) dialog.findViewById(R.id.dismissBtn);
        dismiss.setOnClickListener(cancel);
        Button okDialog = (Button) dialog.findViewById(R.id.okDialog);
        okDialog.setOnClickListener(ok);*/

        dialog.show();

       dialog.getWindow().setAttributes(lp);
    }


    public void dialogDismiss() {
        if (null != dialog && dialog.isShowing()) dialog.dismiss();
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }


    public void showPremiumDialog(Context context, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Get premium features free by simply sharing this app on Facebook.")
                .setCancelable(false)
                .setTitle("Unlock Full Version")
                .setNegativeButton("Dismiss", cancel)
                .setPositiveButton("Share", ok);
        builder.show();
    }

    public void showConfirmDialog(String Message, String Title, Context context, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder.setMessage(Message)
                .setCancelable(false)
                .setTitle(Title)
                .setNegativeButton("Cancel", cancel)
                .setPositiveButton("Yes", ok);
        builder.show();
    }

}
