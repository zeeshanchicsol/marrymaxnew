package com.chicsol.marrymax.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.chicsol.marrymax.utils.Constants;

/**
 * Created by Android on 10/26/2016.
 */

public class faTextView extends AppCompatTextView {

    public faTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public faTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public faTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(Constants.font_fa_awesome, context);
        setTypeface(customFont);
    }
}
