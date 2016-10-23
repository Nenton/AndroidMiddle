package com.nenton.androidmiddle.ui.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.nenton.androidmiddle.R;


public class TextViewCustom extends TextView {

    private static final String TAG = "TextViewCustom";

    public TextViewCustom(Context context) {
        super(context);
    }

    public TextViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public TextViewCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setCustomFont(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String font = typedArray.getString(R.styleable.CustomTextView_font);
        setCustomFont(context, font);
        typedArray.recycle();
    }

    private boolean setCustomFont(Context context, String s) {
        Typeface typeface = null;
        try {
            typeface = Typeface.createFromAsset(context.getAssets(), s);
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
            return false;
        }
        setTypeface(typeface);
        return true;
    }

}
