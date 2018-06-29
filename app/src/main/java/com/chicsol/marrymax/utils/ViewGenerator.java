package com.chicsol.marrymax.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.adapters.MySpinnerAdapter;
import com.chicsol.marrymax.adapters.MySpinnerCSCAdapter;
import com.chicsol.marrymax.modal.PrefMatching;
import com.chicsol.marrymax.modal.Subscription;
import com.chicsol.marrymax.modal.WebArd;
import com.chicsol.marrymax.modal.WebArdType;
import com.chicsol.marrymax.modal.WebCSC;
import com.chicsol.marrymax.modal.mAsEmailNotifications;
import com.chicsol.marrymax.modal.mIceBreak;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mCheckBox;
import com.chicsol.marrymax.widgets.mRadioButton;
import com.chicsol.marrymax.widgets.mTextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 12/27/2016.
 */


public class ViewGenerator {


    private Context context;

    public ViewGenerator(Context context) {
        this.context = context;

    }

    public void addDynamicCheckRdioButtons(final List<WebArd> RadioDataList, RadioGroup radioGroup, final LinearLayout CheckBoxGroupView) {


        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams layoutParamsCB = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < RadioDataList.size(); i++) {
            //showLog(RadioDataList.get(i).getName()+" ==  "+RadioDataList.get(i).getId());
            mRadioButton rdbtn = new mRadioButton(context);
            //rdbtn.setBackgroundResource(R.drawable.radiobtn_reg_selector);
            rdbtn.setTextColor(ContextCompat.getColorStateList(context, R.color.colorBlack));

            rdbtn.setId(i);
            if (Integer.parseInt(RadioDataList.get(i).getId()) == 0) {
                rdbtn.setText(RadioDataList.get(i).getName());
                rdbtn.setButtonDrawable(android.R.color.transparent);

            } else {
                rdbtn.setId(Integer.parseInt(RadioDataList.get(i).getId()));
                rdbtn.setText(RadioDataList.get(i).getName());
            }
            ViewCompat.setLayoutDirection(rdbtn, ViewCompat.LAYOUT_DIRECTION_RTL);

            radioGroup.addView(rdbtn, layoutParams);
        }


        for (int i = 0; i < RadioDataList.size(); i++) {
            AppCompatCheckBox cbox = new AppCompatCheckBox(context);
            cbox.setId(Integer.parseInt(RadioDataList.get(i).getId()));
            //  checkboxDatalist.add(cbox);

            cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ///    Log.e("checked " + checkboxDatalist.size(), isChecked + "" + buttonView.getId());

                    if (buttonView.getId() == Integer.parseInt(RadioDataList.get(RadioDataList.size() - 1).getId()) && buttonView.isChecked()) {
                        clearAllCheckboxSelections(CheckBoxGroupView);
                        buttonView.setChecked(true);
                    } else {

                        unCheckLastSelections(CheckBoxGroupView);
                       /* if (buttonView.isChecked()) {
                            buttonView.setChecked(false);
                        }
*/
                    }
                    //   Toast.makeText(RegisterGeographicActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            CheckBoxGroupView.addView(cbox, layoutParamsCB);
        }


    }

    public void addDynamicCheckRdioButtonsWithRadioDisabled(final List<WebArd> RadioDataList, RadioGroup radioGroup, final LinearLayout CheckBoxGroupView) {


        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams layoutParamsCB = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < RadioDataList.size(); i++) {
            //showLog(RadioDataList.get(i).getName()+" ==  "+RadioDataList.get(i).getId());
            mRadioButton rdbtn = new mRadioButton(context);
            rdbtn.setId(i);
            //    rdbtn.setClickable(false);
            rdbtn.setEnabled(false);

            rdbtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
            if (Integer.parseInt(RadioDataList.get(i).getId()) == 0) {
                rdbtn.setText(RadioDataList.get(i).getName());
                rdbtn.setButtonDrawable(android.R.color.transparent);

            } else {
                rdbtn.setId(Integer.parseInt(RadioDataList.get(i).getId()));
                rdbtn.setText(RadioDataList.get(i).getName());
            }
            ViewCompat.setLayoutDirection(rdbtn, ViewCompat.LAYOUT_DIRECTION_RTL);

            radioGroup.addView(rdbtn, layoutParams);
        }


        for (int i = 0; i < RadioDataList.size(); i++) {
            AppCompatCheckBox cbox = new AppCompatCheckBox(context);
            cbox.setId(Integer.parseInt(RadioDataList.get(i).getId()));
            //  checkboxDatalist.add(cbox);

            cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ///    Log.e("checked " + checkboxDatalist.size(), isChecked + "" + buttonView.getId());

                    if (buttonView.getId() == Integer.parseInt(RadioDataList.get(RadioDataList.size() - 1).getId()) && buttonView.isChecked()) {
                        clearAllCheckboxSelections(CheckBoxGroupView);
                        buttonView.setChecked(true);
                    } else {

                        unCheckLastSelections(CheckBoxGroupView);
                       /* if (buttonView.isChecked()) {
                            buttonView.setChecked(false);
                        }
*/
                    }
                    //   Toast.makeText(RegisterGeographicActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            CheckBoxGroupView.addView(cbox, layoutParamsCB);
        }


    }

    private void clearAllCheckboxSelections(LinearLayout linearLayout) {
        int childcount = linearLayout.getChildCount();
        for (int i = 0; i < childcount; i++) {

            View sv = linearLayout.getChildAt(i);
            if (sv instanceof CheckBox) {

                if (((CheckBox) sv).isChecked()) {
                    ((CheckBox) sv).setChecked(false);

                }

            }
        }

    }


    private void unCheckLastSelections(LinearLayout linearLayout) {
        int childcount = linearLayout.getChildCount();
        View sv = linearLayout.getChildAt(childcount - 1);

        if (((CheckBox) sv).isChecked()) {
            ((CheckBox) sv).setChecked(false);

        }

    }

    public void selectSpinnerItemByValue(Spinner spnr, String value, List<WebArd> mList) {
        MySpinnerAdapter adapter = (MySpinnerAdapter) spnr.getAdapter();
        for (int position = 0; position < mList.size(); position++) {
            if (mList.get(position).getName() == value) {
                spnr.setSelection(position);
                return;
            }
        }
    }

    public void selectSpinnerItemById(Spinner spnr, long value, List<WebArd> mList) {
        MySpinnerAdapter adapter = (MySpinnerAdapter) spnr.getAdapter();
        for (int position = 0; position < mList.size(); position++) {
            if (Long.parseLong(mList.get(position).getId()) == value) {
                spnr.setSelection(position);
                return;
            }
        }
    }

    public void selectSpinnerItemByIdWebCSC(Spinner spnr, long value, List<WebCSC> mList) {
        MySpinnerCSCAdapter adapter = (MySpinnerCSCAdapter) spnr.getAdapter();
        for (int position = 0; position < mList.size(); position++) {
            if (Long.parseLong(mList.get(position).getId()) == value) {
                spnr.setSelection(position);
                return;
            }
        }
    }


    public void selectCheckRadio(RadioGroup radioGroup, int radioId, LinearLayout llCheckboxView, String checkBoxIds) {

        radioGroup.check(radioId);


        String[] visa_status_check_ids = checkBoxIds.split(",");
        if (visa_status_check_ids.length > 0) {
            int childcount = llCheckboxView.getChildCount();
            for (int i = 0; i < childcount; i++) {

                View sv = llCheckboxView.getChildAt(i);
                if (sv instanceof CheckBox) {
                    int id = (((CheckBox) sv).getId());
                    for (int j = 0; j < visa_status_check_ids.length; j++) {

                        if (id == Integer.parseInt(visa_status_check_ids[j])) {
                            ((CheckBox) sv).setChecked(true);

                        }


                    }

                }
            }

        }
    }

    public void selectCheckRadioWithDisabledRadio(RadioGroup radioGroup, int radioId, LinearLayout llCheckboxView, String checkBoxIds) {

        radioGroup.check(radioId);

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
        }


        String[] visa_status_check_ids = checkBoxIds.split(",");
        if (visa_status_check_ids.length > 0) {
            int childcount = llCheckboxView.getChildCount();
            for (int i = 0; i < childcount; i++) {

                View sv = llCheckboxView.getChildAt(i);
                if (sv instanceof CheckBox) {
                    int id = (((CheckBox) sv).getId());
                    for (int j = 0; j < visa_status_check_ids.length; j++) {

                        if (id == Integer.parseInt(visa_status_check_ids[j])) {
                            ((CheckBox) sv).setChecked(true);

                        }


                    }

                }
            }

        }
    }


    public void selectCheckRadioFromGridLayout(GridLayout glCheckboxView, String checkBoxIds) {


        String[] visa_status_check_ids = checkBoxIds.split(",");
        if (visa_status_check_ids.length > 0) {
            int childcount = glCheckboxView.getChildCount();
            for (int i = 0; i < childcount; i++) {

                View sv = glCheckboxView.getChildAt(i);
                if (sv instanceof CheckBox) {
                    int id = (((CheckBox) sv).getId());
                    for (int j = 0; j < visa_status_check_ids.length; j++) {

                        if (id == Integer.parseInt(visa_status_check_ids[j])) {
                            ((CheckBox) sv).setChecked(true);

                        }


                    }

                }
            }

        }
    }

    private StringBuilder clearCommarEnd(StringBuilder stringBuilder) {

        Character ch = new Character(',');
        if (stringBuilder.length() > 0) {

            if (stringBuilder.charAt(stringBuilder.length() - 1) == ch) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
        }

        return stringBuilder;
    }


    public String getSelectionFromCheckbox(LinearLayout llCheckboxView) {

        StringBuilder sbSelectedVisaMyChoice = new StringBuilder();
        int childcount = llCheckboxView.getChildCount();
        for (int i = 0; i < childcount; i++) {

            View sv = llCheckboxView.getChildAt(i);
            if (sv instanceof CheckBox) {

                if (((CheckBox) sv).isChecked()) {
                    sbSelectedVisaMyChoice.append(((CheckBox) sv).getId());
                    if (i != childcount - 1) {
                        sbSelectedVisaMyChoice.append(",");
                    }
                }

            }
        }
        if (sbSelectedVisaMyChoice.length() == 0) {

            return "0";
        }

        Character ch = new Character(',');
        if (sbSelectedVisaMyChoice.length() > 0) {
            if (sbSelectedVisaMyChoice.charAt(sbSelectedVisaMyChoice.length() - 1) == ch) {
                sbSelectedVisaMyChoice.deleteCharAt(sbSelectedVisaMyChoice.length() - 1);
            }

        }

        return clearCommarEnd(sbSelectedVisaMyChoice).toString();
    }


    public String getSelectionFromCheckbox(GridLayout llCheckboxView) {

        StringBuilder sbSelectedVisaMyChoice = new StringBuilder();
        int childcount = llCheckboxView.getChildCount();
        for (int i = 0; i < childcount; i++) {

            View sv = llCheckboxView.getChildAt(i);
            if (sv instanceof CheckBox) {

                if (((CheckBox) sv).isChecked()) {
                    sbSelectedVisaMyChoice.append(((CheckBox) sv).getId());
                    if (i != childcount - 1) {
                        sbSelectedVisaMyChoice.append(",");
                    }
                }

            }
        }

        if (sbSelectedVisaMyChoice.length() != 0) {
            Character ch = new Character(',');
            if (sbSelectedVisaMyChoice.charAt(sbSelectedVisaMyChoice.length() - 1) == ch) {
                sbSelectedVisaMyChoice.deleteCharAt(sbSelectedVisaMyChoice.length() - 1);
            }
        }

        return sbSelectedVisaMyChoice.toString();
    }


    public void generatePrefMatchingCheckBoxesInGridLayout(final List<PrefMatching> checkDataList, GridLayout glGeo, GridLayout glLife, GridLayout glAppearnc, int w) {

        GridLayout glMain = null;
        //0.44
        LinearLayout.LayoutParams layoutParamsCB = new LinearLayout.LayoutParams((int) (w * 0.30), ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParamsCB.setMargins(10, 10, 10, 10);


        for (int i = 0; i < checkDataList.size(); i++) {

            PrefMatching mPref = checkDataList.get(i);

            switch (mPref.getAbout_type_id()) {
                case 1:

                    glMain = glGeo;
                    break;

                case 2:
                    glMain = glAppearnc;
                    break;

                case 3:
                    glMain = glLife;
                    break;


            }

            mCheckBox cbox = new mCheckBox(context);
            cbox.setTextSize(10);
            cbox.setId(Integer.parseInt(checkDataList.get(i).getId()));
            cbox.setText(checkDataList.get(i).getType());
            //    cbox.setGravity(Gravity.FILL);
            // cbox.setBackground(context.getResources().getDrawable(R.drawable.checkboxborder));

            cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                }
            });

            glMain.addView(cbox, layoutParamsCB);
        }

    }


    public void generateCheckBoxesInGridLayout(final List<WebArd> checkDataList, GridLayout gridLayout, int w) {

        //  TableRow.LayoutParams layoutParamsCB = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1f);

        LinearLayout.LayoutParams layoutParamsCB = new LinearLayout.LayoutParams((int) (w * 0.44), ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParamsCB.setMargins(10, 10, 10, 10);

    /* ((GridLayout.LayoutParams) gridLayout.getLayoutParams()).columnSpec =
             GridLayout.spec(GridLayout.UNDEFINED, 1f);*/

        //  GridLayout.LayoutParams layoutParams=new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 2),GridLayout.spec(GridLayout.UNDEFINED, 2));
        // layoutParams.setMargins(10,10,10,10);
    /* layoutParams.width=ViewGroup.LayoutParams.WRAP_CONTENT;
     layoutParams.height=ViewGroup.LayoutParams.WRAP_CONTENT;
   layoutParams  .columnSpec =             GridLayout.spec(GridLayout.UNDEFINED, 1f);*/
        // layoutParams.setGravity(Gravity.FILL);
        //layoutParams.c

        for (int i = 0; i < checkDataList.size(); i++) {
            mCheckBox cbox = new mCheckBox(context);
            //cbox.setTextSize(13);
            cbox.setId(Integer.parseInt(checkDataList.get(i).getId()));
            cbox.setText(checkDataList.get(i).getName());
            //    cbox.setGravity(Gravity.FILL);
            cbox.setBackground(context.getResources().getDrawable(R.drawable.checkboxborder));
            //  checkboxDatalist.add(cbox);

            cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ///    Log.e("checked " + checkboxDatalist.size(), isChecked + "" + buttonView.getId());


                    //   Toast.makeText(RegisterGeographicActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            gridLayout.addView(cbox, layoutParamsCB);
        }

    }


    public String getSelectedIdsFromsList(ArrayList selectedArrayList, List<WebArd> dataList) {

        StringBuilder sbSelectedCountries = new StringBuilder();
        for (int i = 0; i < selectedArrayList.size(); i++) {
            sbSelectedCountries.append(dataList.get((Integer) selectedArrayList.get(i)).getId());
            if (i != selectedArrayList.size() - 1) {
                sbSelectedCountries.append(",");
            }
        }
        return sbSelectedCountries.toString();
    }

    public void generateDosDontsViews(LinearLayout linearLayout, boolean dos, JSONArray dodontDataLst) {

/*        <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:orientation="horizontal"
        android:paddingBottom="3dp"

        android:paddingTop="3dp">

        <com.chicsol.marrymax.widgets.faTextView

        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="5dp"
        android:text="@string/fa_icon_accepted"
        android:textColor="@color/colorDefaultGreen" />

        <com.chicsol.marrymax.widgets.mTextView

        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="5dp"
        android:text="Something that is interesting"
        android:textColor="@color/colorDashboardMainNormalText" />
        </LinearLayout>
        */


        for (int i = 0; i < dodontDataLst.length(); i++) {
            LinearLayout linearLayout1 = new LinearLayout(context);
            linearLayout1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            faTextView faTextView = new faTextView(context);
            faTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (dos) {
                faTextView.setText(context.getResources().getString(R.string.fa_icon_accepted));
                faTextView.setTextColor(context.getResources().getColor(R.color.colorDefaultGreen));
            } else {
                faTextView.setTextColor(context.getResources().getColor(R.color.colorTextRed));
                faTextView.setText(context.getResources().getString(R.string.fa_icon_cross));
            }
            mTextView mTv = new mTextView(context);
            mTv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTv.setTextColor(context.getResources().getColor(R.color.colorDashboardMainNormalText));
            mTv.setPadding(5, 5, 5, 5);

            try {
                mTv.setText(dodontDataLst.getJSONObject(i).get("name").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            linearLayout1.addView(faTextView);
            linearLayout1.addView(mTv);
            linearLayout.addView(linearLayout1);
        }
    }

  /* public boolean isSelectedRadioCheck(RadioGroup radioGroup,LinearLayout llCheckboxView ){

      // radioGroup.isSelected()


      boolean checkboxcheck=false;
       int childcount = llCheckboxView.getChildCount();
       for (int i = 0; i < childcount; i++) {
           View sv = llCheckboxView.getChildAt(i);
              if (((CheckBox) sv).isChecked()) {
                  CheckBox checkBox=((CheckBox) sv);
                   if(checkBox.isChecked()){
                       checkboxcheck=true;
                       break;
                   }
               }
       }

     // if( radioGroup.isSelected() && checkboxcheck

       return false;
   }
*/


    public void generateDynamicRadiosForDialogs(final List<WebArd> RadioDataList, RadioGroup radioGroup) {


        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < RadioDataList.size(); i++) {
            //showLog(RadioDataList.get(i).getName()+" ==  "+RadioDataList.get(i).getId());
            mRadioButton rdbtn = new mRadioButton(context);
            rdbtn.setId(i);
            if (Integer.parseInt(RadioDataList.get(i).getId()) == 0) {
                rdbtn.setText(RadioDataList.get(i).getName());
                rdbtn.setButtonDrawable(android.R.color.transparent);

            } else {
                rdbtn.setId(Integer.parseInt(RadioDataList.get(i).getId()));
                rdbtn.setText(RadioDataList.get(i).getName());
            }
            //   ViewCompat.setLayoutDirection(rdbtn, ViewCompat.LAYOUT_DIRECTION_RTL);

            radioGroup.addView(rdbtn, layoutParams);
        }


    }

    public void generateDynamicRadiosForSubscriptionDialog(final List<Subscription> RadioDataList, RadioGroup radioGroup, boolean ccCheck) {


        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < RadioDataList.size(); i++) {
            //showLog(RadioDataList.get(i).getName()+" ==  "+RadioDataList.get(i).getId());
            mRadioButton rdbtn = new mRadioButton(context);
            rdbtn.setId(i);

   /*         if(ccCheck){
                rdbtn.setId(Integer.parseInt(RadioDataList.get(i).getOther_item_id()));
            }
            else {*/
            rdbtn.setId((int) RadioDataList.get(i).getItem_id());
            //        }

            rdbtn.setTag(RadioDataList.get(i));
            rdbtn.setText(RadioDataList.get(i).getName());

            //   ViewCompat.setLayoutDirection(rdbtn, ViewCompat.LAYOUT_DIRECTION_RTL);

            radioGroup.addView(rdbtn, layoutParams);
        }


    }

    public void generateQuestionChoices(final List<mIceBreak> RadioDataList, RadioGroup radioGroup) {


 /*       RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < RadioDataList.size(); i++) {
            //showLog(RadioDataList.get(i).getName()+" ==  "+RadioDataList.get(i).getId());
            mRadioButton rdbtn = new mRadioButton(context);
            //rdbtn.setBackgroundResource(R.drawable.radiobtn_reg_selector);
            rdbtn.setTextColor(ContextCompat.getColorStateList(context, R.color.colorBlack));

            rdbtn.setId(i);
            if (Integer.parseInt(RadioDataList.get(i).getId()) == 0) {
                rdbtn.setText(RadioDataList.get(i).getName());
                rdbtn.setButtonDrawable(android.R.color.transparent);

            } else {
                rdbtn.setId(Integer.parseInt(RadioDataList.get(i).getId()));
                rdbtn.setText(RadioDataList.get(i).getName());
            }
            ViewCompat.setLayoutDirection(rdbtn, ViewCompat.LAYOUT_DIRECTION_RTL);

            radioGroup.addView(rdbtn, layoutParams);
        }*/

        ////========================================================


        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, 5, 0, 5);
        //    layoutParams.gravity = Gravity.TOP;
        for (int i = 0; i < RadioDataList.size(); i++) {
            //showLog(RadioDataList.get(i).getName()+" ==  "+RadioDataList.get(i).getId());
            AppCompatRadioButton rdbtn = new AppCompatRadioButton(context);
            rdbtn.setBackgroundColor(context.getResources().getColor(R.color.colorTextRed));

            //  mRadioButton rdbtn = new mRadioButton(context);
            //   rdbtn.setGravity(Gravity.TOP | Gravity.BOTTOM);
            // rdbtn.setGravity(Gravity.LEFT);

            rdbtn.setId(i);

         /*   if (disabled) {
                rdbtn.setEnabled(false);
            }*/
            rdbtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
     /*       if (Integer.parseInt(RadioDataList.get(i).getId()) == 0) {
                rdbtn.setText(RadioDataList.get(i).getType());
                rdbtn.setButtonDrawable(android.R.color.transparent);

            } else {*/
            //  rdbtn.setId(Integer.parseInt(RadioDataList.get(i).getQuestion_id()));
            rdbtn.setText(RadioDataList.get(i).getAnswer());


            //}
            //  ViewCompat.setLayoutDirection(rdbtn, ViewCompat.LAYOUT_DIRECTION_RTL);

            radioGroup.addView(rdbtn, layoutParams);
        }

    }


    public void generateDynamicRadiosForDialogsForMatchAid(final List<WebArdType> RadioDataList, RadioGroup radioGroup, boolean disabled) {


        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, 5, 0, 5);
        //    layoutParams.gravity = Gravity.TOP;
        for (int i = 0; i < RadioDataList.size(); i++) {
            //showLog(RadioDataList.get(i).getName()+" ==  "+RadioDataList.get(i).getId());
            AppCompatRadioButton rdbtn = new AppCompatRadioButton(context);
            rdbtn.setGravity(Gravity.TOP | Gravity.BOTTOM);
            // rdbtn.setGravity(Gravity.LEFT);

            rdbtn.setId(i);

            if (disabled) {
                rdbtn.setEnabled(false);
            }
            rdbtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
            if (Integer.parseInt(RadioDataList.get(i).getId()) == 0) {
                rdbtn.setText(RadioDataList.get(i).getType());
                rdbtn.setButtonDrawable(android.R.color.transparent);

            } else {
                rdbtn.setId(Integer.parseInt(RadioDataList.get(i).getId()));
                rdbtn.setText(RadioDataList.get(i).getType());
            }
            //   ViewCompat.setLayoutDirection(rdbtn, ViewCompat.LAYOUT_DIRECTION_RTL);

            radioGroup.addView(rdbtn, layoutParams);
        }


    }

    public void generateDynamicCheckBoxesLL(final List<WebArd> checkDataList, LinearLayout linearLayout) {
        LinearLayout.LayoutParams layoutParamsCB = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //layoutParamsCB.setMargins(10, 10, 10, 10);

        for (int i = 0; i < checkDataList.size(); i++) {
            mCheckBox cbox = new mCheckBox(context);
            cbox.setId(Integer.parseInt(checkDataList.get(i).getId()));
            cbox.setText(checkDataList.get(i).getName());
            cbox.setTextColor(context.getResources().getColor(R.color.colorBlack));
            cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });

            linearLayout.addView(cbox, layoutParamsCB);
        }

    }


    public void generateDynamicSwitchCompats(final List<mAsEmailNotifications> dataList, LinearLayout linearLayout, CompoundButton.OnCheckedChangeListener onCheckedChangeListener, boolean disalbled) {
        LinearLayout.LayoutParams layoutParamsCB = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsCB.setMargins(0, 10, 0, 0);

        for (int i = 0; i < dataList.size(); i++) {
            SwitchCompat cbox = new SwitchCompat(context);
            cbox.setId(Integer.parseInt(dataList.get(i).getId()));
            cbox.setText(dataList.get(i).getType());
            cbox.setTag(dataList.get(i).getIsedit());


            //cbox.setTextColor(context.getResources().getColor(R.color.colorBlack));

            linearLayout.addView(cbox, layoutParamsCB);

            if (dataList.get(i).getIsedit().equals("0")) {
                //    Log.e("is edit ", dataList.get(i).getIsedit()+"==" + dataList.get(i).getType());

                if (disalbled) {
                    cbox.setEnabled(false);
                } else {
                    cbox.setChecked(true);
                    cbox.setOnCheckedChangeListener(onCheckedChangeListener);
                }
            } else {
                if (disalbled) {
                    cbox.setEnabled(false);
                    cbox.setOnCheckedChangeListener(onCheckedChangeListener);
                } else {
                    cbox.setChecked(false);
                    cbox.setOnCheckedChangeListener(onCheckedChangeListener);
                }

            }

        }

    }


    public void generateDynamicCheckBoxesLLWithTag(final List<WebArd> checkDataList, LinearLayout linearLayout, String tag) {
        LinearLayout.LayoutParams layoutParamsCB = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //layoutParamsCB.setMargins(10, 10, 10, 10);

        for (int i = 0; i < checkDataList.size(); i++) {
            mCheckBox cbox = new mCheckBox(context);
            cbox.setId(Integer.parseInt(checkDataList.get(i).getId()));
            cbox.setText(checkDataList.get(i).getName());
            cbox.setTag(tag);
            cbox.setTextColor(context.getResources().getColor(R.color.colorBlack));
            cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });

            linearLayout.addView(cbox, layoutParamsCB);
        }

    }

//Seaarch

    public void selectCheckBoxes(LinearLayout glCheckboxView, String checkBoxIds) {
        Log.e("Valueee", "" + checkBoxIds);
        if (checkBoxIds != null && checkBoxIds != "") {
            resetCheckBoxes(glCheckboxView);
            String[] visa_status_check_ids = checkBoxIds.split(",");
            if (visa_status_check_ids.length > 0) {
                int childcount = glCheckboxView.getChildCount();
                for (int i = 0; i < childcount; i++) {

                    View sv = glCheckboxView.getChildAt(i);
                    if (sv instanceof CheckBox) {
                        int id = (((CheckBox) sv).getId());
                        for (int j = 0; j < visa_status_check_ids.length; j++) {

                            if (id == Integer.parseInt(visa_status_check_ids[j])) {
                                //   Log.e("matched", "" + id);
                                ((CheckBox) sv).setChecked(true);

                            }


                        }

                    }
                }

            }
        } else {

            resetCheckBoxes(glCheckboxView);
        }

    }

    public void resetCheckBoxes(LinearLayout glCheckboxView) {
        int childcount = glCheckboxView.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View sv = glCheckboxView.getChildAt(i);
            if (sv instanceof CheckBox) {
                ((CheckBox) sv).setChecked(false);

            }
        }


    }


/*    public void generateTextViewWithIconx(LinearLayout llMain, String text) {

        RelativeLayout.LayoutParams layoutParamsRL = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(layoutParamsRL);


        RelativeLayout.LayoutParams layoutParamsTV = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layoutParamsIV = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParamsIV.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParamsIV.setMargins(10, 0, 0, 0);
        //  layoutParamsTV.addRule(RelativeLayout);
        layoutParamsTV.setMargins(18, 0, 0, 0);


        AppCompatImageView imageView = new AppCompatImageView(context);
        layoutParamsTV.addRule(RelativeLayout.LEFT_OF, imageView.getId());
        imageView.setLayoutParams(layoutParamsIV);

        imageView.setImageResource(R.drawable.ic_check_circle_black_24dp);
        // layoutParamsTV.
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setLayoutParams(layoutParamsTV);

        relativeLayout.addView(imageView);
        relativeLayout.addView(textView);
        llMain.addView(relativeLayout);


    }*/

    public void generateTextViewWithIcon(LinearLayout llMain, String text) {

        RelativeLayout.LayoutParams layoutParamsRL = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //  layoutParamsRL.addRule(Gravity.CENTER);

        RelativeLayout relativeLayout = new RelativeLayout(context);

        relativeLayout.setLayoutParams(layoutParamsRL);


        AppCompatImageView imageView = new AppCompatImageView(context);
        // imageView.(Gravity.CENTER);
        relativeLayout.addView(imageView);
        RelativeLayout.LayoutParams layoutParamsIV = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParamsIV.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParamsIV.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParamsIV.leftMargin = 20;
        imageView.setLayoutParams(layoutParamsIV);
        imageView.setImageResource(R.drawable.ic_check_circle_black_24dp);


        // layoutParamsTV.
        RelativeLayout.LayoutParams layoutParamsTV = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //  layoutParamsTV.addRule(RelativeLayout);

        //  layoutParamsTV.setMargins(18, 0, 0, 0);
        layoutParamsTV.leftMargin = 120;
        layoutParamsTV.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView textView = new TextView(context);
        //  textView.setGravity(Gravity.CENTER);
        //  layoutParamsTV.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        textView.setText(text);
        relativeLayout.addView(textView);
        textView.setTextColor(context.getResources().getColor(R.color.colorGrey));

        textView.setLayoutParams(layoutParamsTV);
        layoutParamsTV.addRule(RelativeLayout.LEFT_OF, imageView.getId());


        llMain.addView(relativeLayout);


    }


}
