package com.chicsol.marrymax.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.chicsol.marrymax.utils.Constants;

/**
 * Created by Android on 10/26/2016.
 */


/*public class mTextView extends TextView {


    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public mTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            applyCustomFont(context, attrs); //whatever added functionality you are trying to add to Widget, call that inside this condition.
        }

    }

    public mTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
        applyCustomFont(context, attrs);}
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {


        String textStyle = String.valueOf(attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL));

        Typeface customFont = selectTypeface(context, Integer.parseInt(textStyle));
        setTypeface(customFont);
    }

    private Typeface selectTypeface(Context context, int textStyle) {
        *//*
        * information about the TextView textStyle:
        * http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
        *//*
        switch (textStyle) {
            case Typeface.BOLD: // bold
                return FontCache.getTypeface(Constants.font_centurygothic_bold, context);

           *//* case Typeface.ITALIC: // italic
                return FontCache.getTypeface("SourceSansPro-Italic.ttf", context);

            case Typeface.BOLD_ITALIC: // bold italic
                return FontCache.getTypeface("SourceSansPro-BoldItalic.ttf", context);*//*

            case Typeface.NORMAL: // regular
            default:
                return FontCache.getTypeface(Constants.font_centurygothic, context);
        }
    }
}*/


public class mTextView extends AppCompatTextView {
    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public mTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public mTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public mTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(Constants.font_centurygothic, context);
        setTypeface(customFont);
    }


    private void applyCustomFont(Context context, AttributeSet attrs) {

        String style = "0x0";

        try {
            style = attrs.getAttributeValue(ANDROID_SCHEMA, "textStyle");
        } catch (Exception e) {
            e.printStackTrace();
        }

        int textStyle;

        if (style != null && style.equals("0x1")) {
            textStyle = Typeface.BOLD;
        } else {
            textStyle = Typeface.NORMAL;
        }


        Typeface customFont = selectTypeface(context, textStyle);
        setTypeface(customFont);


    }


    private Typeface selectTypeface(Context context, int textStyle) {

/*
  if (fontName.contentEquals(context.getString(R.string.font_name_centurygothic))) {
            return FontCache.getTypeface("fontawesome.ttf", context);
        } else if (fontName.contentEquals(context.getString(R.string.font_name_centurygothicBold))) {
*/

  /*
              information about the TextView textStyle:
              http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
      */

        switch (textStyle) {
            case Typeface.BOLD: // bold
                return FontCache.getTypeface(Constants.font_centurygothic_bold, context);


      /*      case Typeface.ITALIC: // italic
                return FontCache.getTypeface("SourceSansPro-Italic.ttf", context);

            case Typeface.BOLD_ITALIC: // bold italic
                return FontCache.getTypeface("SourceSansPro-BoldItalic.ttf", context);*/


            case Typeface.NORMAL: // regular
                return FontCache.getTypeface(Constants.font_centurygothic, context);
            default:
                return FontCache.getTypeface(Constants.font_centurygothic, context);
        }

    } /*else {
            // no matching font found
            // return null so Android just uses the standard font (Roboto)
            return null;
        }*/

}


