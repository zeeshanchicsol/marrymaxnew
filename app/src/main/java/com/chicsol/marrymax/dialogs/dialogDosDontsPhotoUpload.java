package com.chicsol.marrymax.dialogs;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.utils.ViewGenerator;
import com.chicsol.marrymax.widgets.faTextView;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogDosDontsPhotoUpload extends DialogFragment {
    private  JSONArray dosDataList;
    private  JSONArray dontsDataList;
    private onCompleteListener mCompleteListener;

    public static dialogDosDontsPhotoUpload newInstance(String data) {

        dialogDosDontsPhotoUpload frag = new dialogDosDontsPhotoUpload();
        Bundle args = new Bundle();
        args.putString("name", "Do's & Don't");
        args.putString("jsondata", data);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        String myValue = mArgs.getString("jsondata");
   ///  Log.e("json data", myValue);

        try {
            JSONArray jsonArray = new JSONArray(myValue);

             dosDataList = jsonArray.getJSONArray(0);
        dontsDataList = jsonArray.getJSONArray(1);






        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
/*    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_profilefor, null))


          *//*      // Add action buttons
                .setPositiveButton("Submut", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogDosDonts.this.getDialog().cancel();
                    }
                })*//*;


        return builder.create();

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_profilefor, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
   /*     Bundle mArgs = getArguments();
        String myValue = mArgs.getString("jsondata");
        try {
            JSONArray jsonArray = new JSONArray(myValue);
            JSONArray dosDataList = jsonArray.getJSONArray(0);
            JSONArray dontsDataList = jsonArray.getJSONArray(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        LinearLayout linearLayoutDos = (LinearLayout) rootView.findViewById(R.id.LinearLayoutDos);
        LinearLayout linearLayoutDonts = (LinearLayout) rootView.findViewById(R.id.LinearLayoutDonts);
        ViewGenerator viewGenerator=new ViewGenerator(getContext());

        viewGenerator.generateDosDontsViews(linearLayoutDos,true,dosDataList);
        viewGenerator.generateDosDontsViews(linearLayoutDonts,false,dontsDataList);
        //Log.e("json data1", dosDataList.toString());
        //Log.e("json data2", dontsDataList.toString());

     /*   RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.RadioGroupProfileFor);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //  radioGroup.ad
        for (int i = 0; i < DataList.size(); i++) {
            mRadioButton mradiobutton = new mRadioButton(getContext());
            mradiobutton.setText(DataList.get(i));
            mradiobutton.setLayoutParams(layoutParams);

            radioGroup.addView(mradiobutton);
        }*/

        /*//do's
        for (int i=0;i<jsonArray1.length();i++){
            JSONObject jsonObject= jsonArray1.getJSONObject(i);
            Log.e("value",""+jsonObject.get("name").toString());
        }

        //dont's
        for (int i=0;i<jsonArray2.length();i++){
            JSONObject jsonObject= jsonArray2.getJSONObject(i);
            Log.e("value 2",""+jsonObject.get("name").toString());
        }*/




        Button mOkButton = (Button) rootView.findViewById(R.id.okDialog);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                /*RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.RadioGroupProfileFor);



                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) rootView.findViewById(selectedId);

                Toast.makeText(getActivity().getApplicationContext(),
                        radioButton.getText(), Toast.LENGTH_SHORT).show();*/

                //  Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();

                // When button is clicked, call up to owning activity.
                // When button is clicked, call up to owning activity.
                dialogDosDontsPhotoUpload.this.getDialog().cancel();


            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.dismissBtn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogDosDontsPhotoUpload.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static interface onCompleteListener {
        public abstract void onComplete(String s);
    }
}
