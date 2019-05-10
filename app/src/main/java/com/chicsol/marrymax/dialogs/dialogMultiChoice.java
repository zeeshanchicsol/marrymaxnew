package com.chicsol.marrymax.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.MultiChoiceAdapterRecycler;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.widgets.faTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by zeedr on 24/10/2016.
 */

public class dialogMultiChoice extends DialogFragment {
    private List<WebArd> educationDataList;
    EditText etOtherReason;
    String userpath, selectdlist, jsarray;
    int abtypeid = -1;
    String mDataList;
    private onMultiChoiceSaveListener mCompleteListener;
    private RecyclerView recyclerViewMyChoice;
    private MultiChoiceAdapterRecycler multiChoiceAdapterRecycler;
    int which;
    private TextView tvtitle;
    String title;

    public static dialogMultiChoice newInstance(String dataList, int check, String title) {


        dialogMultiChoice frag = new dialogMultiChoice();
        Bundle args = new Bundle();
        args.putString("dataList", dataList);
        args.putInt("check", check);
        args.putString("title", title);
        // args.putString("mselectedEducationIdDataList", mselectedEducationIdDataList);
        /* args.putString("userpath", userpath);*/


        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        which = mArgs.getInt("check");
        title = mArgs.getString("title");
        mDataList = mArgs.getString("dataList");
        {
            Gson gsonc;
            GsonBuilder gsonBuilderc = new GsonBuilder();
            gsonc = gsonBuilderc.create();
            Type listType = new TypeToken<List<WebArd>>() {
            }.getType();

            educationDataList = (List<WebArd>) gsonc.fromJson(mDataList, listType);
        }
        //Log.e("lissst size is ", educationDataList.size() + "");



       /*
        userpath = mArgs.getString("userpath");*/

    }

   /* @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage("Are you sure?");

        // Edited: Overriding onCreateView is not necessary in your case
        LayoutInflater rootView = LayoutInflater.from(getContext());
        View newFileView = rootView.inflate(R.layout.dialog_multi_choice, null);
        builder.setView(newFileView);

        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }*/

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {


            if (getTargetFragment() != null) {
                mCompleteListener = (onMultiChoiceSaveListener) getTargetFragment();
            } else {
                mCompleteListener = (onMultiChoiceSaveListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnCompleteListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_multi_choice, container, false);
        //  getDialog().getWindow().requestFeature(Window.FEATURE_);

        tvtitle = (TextView) rootView.findViewById(R.id.TextViewChoiceCountriesTitle);
        tvtitle.setText(title);
        recyclerViewMyChoice = (RecyclerView) rootView.findViewById(R.id.RecyclerViewDiualogMultChoice);
        recyclerViewMyChoice.setHasFixedSize(true);
        multiChoiceAdapterRecycler = new MultiChoiceAdapterRecycler(getContext(), educationDataList);
        recyclerViewMyChoice.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMyChoice.setAdapter(multiChoiceAdapterRecycler);
/*

        List<WebArd> dataList = new ArrayList<>();
        try {
            JSONArray jsonCountryStaeObj = new JSONArray(jsarray);
            Gson gsonc;
            GsonBuilder gsonBuilderc = new GsonBuilder();
            gsonc = gsonBuilderc.create();
            Type listType = new TypeToken<List<WebArd>>() {
            }.getType();
            dataList = (List<WebArd>) gsonc.fromJson(jsonCountryStaeObj.toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
*/


        Button mOkButton = (Button) rootView.findViewById(R.id.mButtonDialogBlock);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                List<WebArd> list = multiChoiceAdapterRecycler.getSelection();
                for (int i = 0; i < list.size(); i++) {

                  /*  if (list.get(i).isSelected())
                        Log.e("save  click", list.get(i).getName());*/
                }

                mCompleteListener.onMultiChoiceSave(list, which);
                dialogMultiChoice.this.getDialog().cancel();
            }
        });

        faTextView cancelButton = (faTextView) rootView.findViewById(R.id.mButtonDismissDialogBlock);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "clcieck", Toast.LENGTH_SHORT).show();
                dialogMultiChoice.this.getDialog().cancel();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public static interface onMultiChoiceSaveListener {
        public abstract void onMultiChoiceSave(List<WebArd> s, int c);
    }

}


