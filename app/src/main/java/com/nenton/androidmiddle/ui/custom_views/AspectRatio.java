package com.nenton.androidmiddle.ui.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nenton.androidmiddle.R;

public class AspectRatio extends ImageView{

    private static final float DEFAULT_ASPECT_RATIO = 1.73f;
    private final float mAspectRatio;

    public AspectRatio(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView);
        mAspectRatio = array.getFloat(R.styleable.AspectRatioImageView_aspect_ratio,DEFAULT_ASPECT_RATIO);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth;
        int mHeight;

        mWidth = getMeasuredWidth();
        mHeight = (int) (mWidth/mAspectRatio);

        setMeasuredDimension(mWidth, mHeight);
    }
}
