package com.chicsol.marrymax.fragments.userprofilefragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.modal.mMemDetail;
import com.chicsol.marrymax.preferences.SharedPreferenceManager;
import com.chicsol.marrymax.widgets.faTextView;
import com.chicsol.marrymax.widgets.mTextView;
import com.crashlytics.android.Crashlytics;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;


public class BasicInfoFragment extends Fragment {
    private JSONArray interestJsonArray, describePersonalityJsonArray;
    private Members member, memberChoice;
    private mTextView tvDesc, tvMostThankful, tvWhatIdoFor, tvMyStrengths, tvABoutMyChoice, tvAge, tvHeight, tvPhysique, tvComplexion, tvEyeColor, tvHairColor, tvChoiceAge,
            tvChoiceHeight, tvChoicePhysique, tvChoiceComplexion, tvChoiceEyeColor, tvChoiceHairColor;
    private mTextView tvMyEducation, tvMyEducationField, tvGraduated, tvOccupation, tvEconomy, tvIncome, tvCastSurname, tvRaised, tvFamilyValues, tvHijab, tvLiving, tvMaritalStatus, tvChildren, tvChildrenDetail, tvEthnicity, tvReligiousSect, tvBrothers, tvSisters, tvSiblingPosiiton, tvSmoke, tvDrink, tvPhysicalChallenges,
            tvChoiceMyEducation, tvChoiceOccupation, tvChoiceEconomy, tvChoiceRaised, tvChoiceFamilyValues, tvChoiceHijab, tvChoiceLiving, tvChoiceMaritalStatus, tvChoiceChildren, tvChoiceEthnicity, tvChoiceReligiousSect, tvChoiceCountry, tvChoiceVisaStatus, tvChoiceSmoke, tvChoiceDrink, tvChoicePhysicalChallenges;

    private TextView pref1, pref2, pref3, pref4;

    private mTextView tvDescribePersonality;
    private FlexboxLayout flexboxLayoutInterest;
    private LinearLayout llMTO, llWIDFF, llMS, llAMC;
    public String jsona = "";
    boolean myProfileCheck;
    LinearLayout LinearLayoutDeclaration, llMemDetail;

    private faTextView faUPPhysicalChallenges, faUPChoicePhysicalChallenges;

    private mMemDetail memDetailObj = null;
    private TextView tvResidenceDetails, tvAboutParents, tvAboutSiblings, tvJobDetails, tvEducationDetail, tvSocialDetai;
    private LinearLayout llResidenceDetails, llAboutParents, llAboutSiblings, llJobDetails, llEducationDetail, llSocialDetai;


    public BasicInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        Log.e("Basic Info Fragment  ", getArguments().getString("json"));
        String json = getArguments().getString("json");

        try {
            JSONArray jsonArray = new JSONArray(json);

            JSONObject firstJsonObj = jsonArray.getJSONArray(0).getJSONObject(0);
            JSONObject secondJsonObj = jsonArray.getJSONArray(1).getJSONObject(0);

            interestJsonArray = jsonArray.getJSONArray(3);
            describePersonalityJsonArray = jsonArray.getJSONArray(2);


            Bundle bundle = getArguments();

            myProfileCheck = bundle.getBoolean("myprofilecheck", false);



/*
            <com.chicsol.marrymax.widgets.mTextView
                    android:layout_margin="5dp"
            android:background="@drawable/border_dash_userprofile"
            android:padding="5dp"
            android:text="Movies/ Theator/ Television"
            android:textColor="@color/colorBlack" />*/


            Gson gson;
            GsonBuilder gsonBuilder = new GsonBuilder();

            gson = gsonBuilder.create();
            Type type = new TypeToken<Members>() {
            }.getType();
            member = (Members) gson.fromJson(firstJsonObj.toString(), type);
            memberChoice = (Members) gson.fromJson(secondJsonObj.toString(), type);


            Gson gson2;
            GsonBuilder gsonBuilder2 = new GsonBuilder();
            gson2 = gsonBuilder2.create();
            if (jsonArray.getJSONArray(5).length() > 0) {
                JSONObject memDetailJsonObj = jsonArray.getJSONArray(5).getJSONObject(0);
                if (memDetailJsonObj.length() > 0) {
                    memDetailObj = (mMemDetail) gson2.fromJson(memDetailJsonObj.toString(), mMemDetail.class);

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_userprpfile_basic_info, container, false);
        initilize(rootView);
        setListener();
        setValuee();
        setInterestValues();
        setPreferencesValues();
        setTvDescribePersonality();
        return rootView;
    }

    private void initilize(View view) {

        LinearLayoutDeclaration = (LinearLayout) view.findViewById(R.id.LinearLayoutDeclaration);
        llMemDetail = (LinearLayout) view.findViewById(R.id.LinearLayoutProfileDetailMemDetail);

        if (myProfileCheck) {
            if (SharedPreferenceManager.getUserObject(getContext()).getMember_status() >= 3 && SharedPreferenceManager.getUserObject(getContext()).getMember_status() <= 4) {
                LinearLayoutDeclaration.setVisibility(View.VISIBLE);
            }


        } else {
            LinearLayoutDeclaration.setVisibility(View.GONE);
        }


        WebView webView = (WebView) view.findViewById(R.id.WebViewBasicsyDeclaration);
        // displaying content in WebView from html file that stored in assets folder
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + "declaration.html");


        faUPPhysicalChallenges = (faTextView) view.findViewById(R.id.faTextViewUPPhysicalChallenges);
        faUPChoicePhysicalChallenges = (faTextView) view.findViewById(R.id.faTextViewUPChoicePhysicalChallenges);

        tvDesc = (mTextView) view.findViewById(R.id.TextViewUPDescription);
        tvMostThankful = (mTextView) view.findViewById(R.id.TextViewUPMostThankful);
        tvWhatIdoFor = (mTextView) view.findViewById(R.id.TextViewUPWhatIdo);
        tvMyStrengths = (mTextView) view.findViewById(R.id.TextViewUPMyStrengths);
        tvABoutMyChoice = (mTextView) view.findViewById(R.id.TextViewUPAboutMyChoice);


        tvAge = (mTextView) view.findViewById(R.id.TextViewUPAge);
        tvHeight = (mTextView) view.findViewById(R.id.TextViewUPHeight);
        tvPhysique = (mTextView) view.findViewById(R.id.TextViewUPPhysique);
        tvComplexion = (mTextView) view.findViewById(R.id.TextViewUPComplexion);

        tvEyeColor = (mTextView) view.findViewById(R.id.TextViewUPEyeColor);
        tvHairColor = (mTextView) view.findViewById(R.id.TextViewUPHairColor);


        tvChoiceAge = (mTextView) view.findViewById(R.id.TextViewUPChoiceAge);
        tvChoiceHeight = (mTextView) view.findViewById(R.id.TextViewUPChoiceHeight);
        tvChoiceHairColor = (mTextView) view.findViewById(R.id.TextViewUPChoiceHairColor);
        tvChoiceEyeColor = (mTextView) view.findViewById(R.id.TextViewUPChoiceEyeColor);
        tvChoicePhysique = (mTextView) view.findViewById(R.id.TextViewUPChoicePhysique);
        tvChoiceComplexion = (mTextView) view.findViewById(R.id.TextViewUPChoiceComplexion);


        tvMyEducation = (mTextView) view.findViewById(R.id.TextViewUPEducationDetail);
        tvMyEducationField = (mTextView) view.findViewById(R.id.TextViewUPEducationalField);
        tvGraduated = (mTextView) view.findViewById(R.id.TextViewUPGraduatedFrom);
        tvOccupation = (mTextView) view.findViewById(R.id.TextViewUPOccupationDetail);
        tvEconomy = (mTextView) view.findViewById(R.id.TextViewUPEconomy);
        tvIncome = (mTextView) view.findViewById(R.id.TextViewUPIncome);
        tvCastSurname = (mTextView) view.findViewById(R.id.TextViewUPCastSurname);


        tvRaised = (mTextView) view.findViewById(R.id.TextViewUPRaised);
        tvFamilyValues = (mTextView) view.findViewById(R.id.TextViewUPFamilyValues);
        tvHijab = (mTextView) view.findViewById(R.id.TextViewUPHijaab);
        tvLiving = (mTextView) view.findViewById(R.id.TextViewUPLivingArrangements);
        tvMaritalStatus = (mTextView) view.findViewById(R.id.TextViewUPMaritalStatus);
        tvChildren = (mTextView) view.findViewById(R.id.TextViewUPChildren);
        tvChildrenDetail = (mTextView) view.findViewById(R.id.TextViewUPChildrenDetail);
        tvEthnicity = (mTextView) view.findViewById(R.id.TextViewUPEthnicity);
        tvReligiousSect = (mTextView) view.findViewById(R.id.TextViewUPReligiousSect);
        tvBrothers = (mTextView) view.findViewById(R.id.TextViewUPBrothers);
        tvSisters = (mTextView) view.findViewById(R.id.TextViewUPSisters);
        tvSiblingPosiiton = (mTextView) view.findViewById(R.id.TextViewUPSiblingPosition);
        tvSmoke = (mTextView) view.findViewById(R.id.TextViewUPSmoke);
        tvDrink = (mTextView) view.findViewById(R.id.TextViewUPDrink);
        tvPhysicalChallenges = (mTextView) view.findViewById(R.id.TextViewUPPhysicalChallenges);

        //choice
        tvChoiceMyEducation = (mTextView) view.findViewById(R.id.TextViewUPChoiceEducationDetail);

        tvChoiceOccupation = (mTextView) view.findViewById(R.id.TextViewUPChoiceOccupationDetail);
        tvChoiceEconomy = (mTextView) view.findViewById(R.id.TextViewUPChoiceEconomy);
        tvChoiceRaised = (mTextView) view.findViewById(R.id.TextViewUPChoiceRaised);
        tvChoiceFamilyValues = (mTextView) view.findViewById(R.id.TextViewUPChoiceChoiceFamilyValues);
        tvChoiceHijab = (mTextView) view.findViewById(R.id.TextViewUPChoiceHijab);
        tvChoiceLiving = (mTextView) view.findViewById(R.id.TextViewUPChoiceLivingArrangements);
        tvChoiceMaritalStatus = (mTextView) view.findViewById(R.id.TextViewUPChoiceMaritalStatus);
        tvChoiceChildren = (mTextView) view.findViewById(R.id.TextViewUPChoiceChildren);
        tvChoiceEthnicity = (mTextView) view.findViewById(R.id.TextViewUPChoiceEthnicity);
        tvChoiceReligiousSect = (mTextView) view.findViewById(R.id.TextViewUPChoiceReligiousSect);

        tvChoiceCountry = (mTextView) view.findViewById(R.id.TextViewUPChoiceCountry);
        tvChoiceVisaStatus = (mTextView) view.findViewById(R.id.TextViewUPChoiceVisaStatus);


        tvChoiceSmoke = (mTextView) view.findViewById(R.id.TextViewUPChoiceSmoke);
        tvChoiceDrink = (mTextView) view.findViewById(R.id.TextViewUPChoiceDrink);

        tvChoicePhysicalChallenges = (mTextView) view.findViewById(R.id.TextViewUPChoicePhysicalChallenges);


//member info details
        tvResidenceDetails = (TextView) view.findViewById(R.id.TextViewUPResidenceDetails);
        tvAboutParents = (TextView) view.findViewById(R.id.TextViewUPAboutParents);
        tvAboutSiblings = (TextView) view.findViewById(R.id.TextViewUPAboutSiblings);
        tvJobDetails = (TextView) view.findViewById(R.id.TextViewUPJobDetails);
        tvEducationDetail = (TextView) view.findViewById(R.id.TextViewUPMIEducationDetail);
        tvSocialDetai = (TextView) view.findViewById(R.id.TextViewUPSocialDetail);

        llResidenceDetails = (LinearLayout) view.findViewById(R.id.LinearLayoutUserprofileResidenceDetails);
        llAboutParents = (LinearLayout) view.findViewById(R.id.LinearLayoutUserprofileAboutParents);
        llAboutSiblings = (LinearLayout) view.findViewById(R.id.LinearLayoutUserprofileAboutSiblings);
        llJobDetails = (LinearLayout) view.findViewById(R.id.LinearLayoutUserprofileJobDetails);
        llEducationDetail = (LinearLayout) view.findViewById(R.id.LinearLayoutUserprofileEducationDetails);
        llSocialDetai = (LinearLayout) view.findViewById(R.id.LinearLayoutUserprofileSocialDetails);



        //==========Layouts

        flexboxLayoutInterest = (FlexboxLayout) view.findViewById(R.id.FlexboxLaoutInterests);

//==========
        pref1 = (TextView) view.findViewById(R.id.PrefChoice1);
        pref2 = (TextView) view.findViewById(R.id.PrefChoice2);
        pref3 = (TextView) view.findViewById(R.id.PrefChoice3);
        pref4 = (TextView) view.findViewById(R.id.PrefChoice4);
//==============
        tvDescribePersonality = (mTextView) view.findViewById(R.id.TextViewDescribePersonality);


        //=====================================
        llMS = (LinearLayout) view.findViewById(R.id.LinearLayoutUserProfileMS);
        llWIDFF = (LinearLayout) view.findViewById(R.id.LinearLayoutUserProfileWIDFF);
        llMTO = (LinearLayout) view.findViewById(R.id.LinearLayoutUserProfileMTO);
        llAMC = (LinearLayout) view.findViewById(R.id.LinearLayoutUserProfileAMC);

    }

    private void setListener() {

    }


    private void setTvDescribePersonality() {

        if (describePersonalityJsonArray != null) {

            StringBuilder stringBuilder = new StringBuilder();
            //  Log.e("describePersonali", describePersonalityJsonArray.toString()+"");
            for (int i = 0; i < describePersonalityJsonArray.length(); i++) {

                if (i != describePersonalityJsonArray.length() - 1) {
                    try {
                        stringBuilder.append(describePersonalityJsonArray.getJSONObject(i).get("type") + ","
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        stringBuilder.append(describePersonalityJsonArray.getJSONObject(i).get("type")
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            tvDescribePersonality.setText(stringBuilder.toString());

        }
    }


    private void setInterestValues() {
        try {


            for (int i = 0; i < interestJsonArray.length(); i++) {
                TextView mTextView = new TextView(getActivity());
                mTextView.setBackground(getResources().getDrawable(R.drawable.shape_chip_drawable));
                mTextView.setTextColor(getResources().getColor(R.color.colorBlack));
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(5, 5, 5, 5);

                mTextView.setLayoutParams(params);

                //mTextView.setPadding(5, 3, 5, 3);
                mTextView.setGravity(Gravity.CENTER);
                try {
                    mTextView.setText("  " + interestJsonArray.getJSONObject(i).get("type").toString() + "  ");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
                flexboxLayoutInterest.addView(mTextView);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "There is some error. Please try again", Toast.LENGTH_SHORT).show();
        }

    }

    private void setPreferencesValues() {
        try {

            pref1.setText(" " + memberChoice.getPref1() + " ");
            pref2.setText(" " + memberChoice.getPref2() + " ");
            pref3.setText(" " + memberChoice.getPref3() + " ");
            pref4.setText(" " + memberChoice.getPref4() + " ");

        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    private void setValuee() {

        try {


            tvDesc.setText(Html.fromHtml(member.getOther_info()));
            Log.e("what i dooo", "==" + member.getFor_fun());
            Log.e("what i dooo", "==" + member.getFor_fun());
            if (member.getFor_fun() != null && member.getFor_fun() != "") {
                tvWhatIdoFor.setText(Html.fromHtml(member.getFor_fun()));
            } else {
                llWIDFF.setVisibility(View.GONE);

            }
            if (member.getGood_quality() != null && member.getGood_quality() != "") {
                tvMyStrengths.setText(Html.fromHtml(member.getGood_quality()));
            } else {
                llMS.setVisibility(View.GONE);

            }

            if (member.getMost_thankfull() != null && member.getMost_thankfull() != "") {
                tvMostThankful.setText(Html.fromHtml(member.getMost_thankfull()));
            } else {
                llMTO.setVisibility(View.GONE);

            }

            if (member.getAbout_my_choice() != null && member.getAbout_my_choice() != "") {
                tvABoutMyChoice.setText(Html.fromHtml(member.getAbout_my_choice()));
            } else {
                llAMC.setVisibility(View.GONE);

            }


            tvAge.setText(member.getAge());
            tvHeight.setText(member.getHeight_description());
            tvPhysique.setText(member.getBody_types());
            tvComplexion.setText(member.getComplexion_types());

            tvEyeColor.setText(member.getEye_color_types());
            tvHairColor.setText(member.getHair_color_types());

            //choice
            tvChoiceAge.setText(memberChoice.getChoice_age_from() + " Year To " + memberChoice.getChoice_age_upto() + " Years");

            tvChoicePhysique.setText(memberChoice.getChoice_body());
            tvChoiceComplexion.setText(memberChoice.getChoice_complexion());
            tvChoiceEyeColor.setText(memberChoice.getChoice_eye_color());
            tvChoiceHairColor.setText(memberChoice.getChoice_hair_color());
            tvChoiceHeight.setText(memberChoice.getChoice_height_from() + " To " + memberChoice.getChoice_height_to());


            tvMyEducation.setText(member.getEducation_types());
            tvMyEducationField.setText(member.getEducation_field_types());

            String in = "";
            if (!member.getAdmin_notes().equals("")) {

                in = " in " + member.getAdmin_notes();

            }
            tvGraduated.setText(member.getNotes() + in);
            tvOccupation.setText(member.getOccupation_types());
            tvEconomy.setText(member.getAbout_type());
            tvIncome.setText(member.getIncome_level());
            tvCastSurname.setText(member.getCaste_name());


            tvRaised.setText(member.getRaised_types());
            tvFamilyValues.setText(member.getFamily_values_types());
            tvHijab.setText(member.getHijab_types());
            tvLiving.setText(member.getLiving_arrabgements_types());
            tvMaritalStatus.setText(member.getMarital_status_types());
            tvChildren.setText(member.getChildren_types());
            tvChildrenDetail.setText(member.getChoice_children());
            tvEthnicity.setText(member.getEthnic_background_types());
            tvReligiousSect.setText(member.getReligious_sec_type());
            tvBrothers.setText(member.getBrothers_count());
            tvSisters.setText(member.getSisters_count());
            tvSiblingPosiiton.setText(member.getSibling_types());
            tvSmoke.setText(member.getSmoking_types());
            tvDrink.setText(member.getDrinks_types());
            tvPhysicalChallenges.setText(member.getPhysics_types());
            if (member.getPhysics_types().equals("No") || member.getPhysics_types().equals("Any")) {
                faUPPhysicalChallenges.setVisibility(View.GONE);
            } else {
                faUPPhysicalChallenges.setVisibility(View.GONE);
            }


            tvChoiceMyEducation.setText(memberChoice.getChoice_education());

            tvChoiceOccupation.setText(memberChoice.getChoice_occupation());
            tvChoiceEconomy.setText(memberChoice.getChoice_economy_ids());
            tvChoiceRaised.setText(memberChoice.getChoice_raised());
            tvChoiceFamilyValues.setText(memberChoice.getChoice_family_values());
            tvChoiceHijab.setText(memberChoice.getChoice_hijab());
            tvChoiceLiving.setText(memberChoice.getChoice_living_arrangements());
            tvChoiceMaritalStatus.setText(memberChoice.getChoice_marital_status());
            tvChoiceChildren.setText(memberChoice.getChoice_children());
            tvChoiceEthnicity.setText(memberChoice.getChoice_ethnic_background());
            tvChoiceReligiousSect.setText(memberChoice.getChoice_religious_sec());

            tvChoiceCountry.setText(memberChoice.getChoice_country_names());
            tvChoiceVisaStatus.setText(memberChoice.getChoice_visa_status());
            tvChoiceSmoke.setText(memberChoice.getChoice_smoking());
            tvChoiceDrink.setText(memberChoice.getChoice_drinks());

            tvChoicePhysicalChallenges.setText(memberChoice.getChoice_physics());

            if (memberChoice.getChoice_physics().equals("No") || memberChoice.getChoice_physics().equals("Any")) {
                faUPChoicePhysicalChallenges.setVisibility(View.GONE);
            } else {
                faUPChoicePhysicalChallenges.setVisibility(View.GONE);
            }


            if (memDetailObj != null) {

                llMemDetail.setVisibility(View.VISIBLE);

                if (!memDetailObj.getResidence().trim().isEmpty()) {
                    tvResidenceDetails.setText(memDetailObj.getResidence());
                    llResidenceDetails.setVisibility(View.VISIBLE);
                } else {
                    llResidenceDetails.setVisibility(View.GONE);
                }


                if (!memDetailObj.getParents().trim().isEmpty()) {
                    tvAboutParents.setText(memDetailObj.getParents());
                    llAboutParents.setVisibility(View.VISIBLE);
                } else {
                    llAboutParents.setVisibility(View.GONE);
                }


                if (!memDetailObj.getSiblings().trim().isEmpty()) {
                    tvAboutSiblings.setText(memDetailObj.getSiblings());
                    llAboutSiblings.setVisibility(View.VISIBLE);
                } else {
                    llAboutSiblings.setVisibility(View.GONE);
                }


                if (!memDetailObj.getJobinfo().trim().isEmpty()) {
                    tvJobDetails.setText(memDetailObj.getJobinfo());
                    llJobDetails.setVisibility(View.VISIBLE);
                } else {
                    llJobDetails.setVisibility(View.GONE);
                }


                if (!memDetailObj.getEducation().trim().isEmpty()) {
                    tvEducationDetail.setText(memDetailObj.getEducation());
                    llEducationDetail.setVisibility(View.VISIBLE);
                } else {
                    llEducationDetail.setVisibility(View.GONE);
                }


                if (!memDetailObj.getSocial().trim().isEmpty()) {
                    tvSocialDetai.setText(memDetailObj.getSocial());
                    llSocialDetai.setVisibility(View.VISIBLE);
                } else {
                    llSocialDetai.setVisibility(View.GONE);
                }


            } else {
                llMemDetail.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();

            getActivity().finish();
        }
    }
}
