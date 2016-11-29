package com.nenton.androidmiddle.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nenton.androidmiddle.R;

public class Behavior extends CoordinatorLayout.Behavior<ImageView>{

//    private int finalHeight;

    private int mStartHeight;
    private int mStartYPosition;
    private int mFinalYPosition;
    private float mChangeBehaviorPoint;
    private float mCustomFinalHeight;
    private float mStartToolbarPosition;

    private final Context mContext;

    public Behavior(Context context, AttributeSet attrs) {
        mContext = context;
        mCustomFinalHeight = context.getResources().getDimension(R.dimen.custom_height);

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof ImageView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        maybeInitProperties(child, dependency);

        final int maxScrollDistance = (int) (mStartToolbarPosition);
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;
        if (expandedPercentageFactor < mChangeBehaviorPoint) {
        float heightFactor = (mChangeBehaviorPoint - expandedPercentageFactor) / mChangeBehaviorPoint;

        float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                * (1f - expandedPercentageFactor)) + (child.getHeight()/2);

        child.setY(mStartYPosition - distanceYToSubtract);

        float heightToSubtract = ((mStartHeight - mCustomFinalHeight) * heightFactor);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.width = (int) (mStartHeight - heightToSubtract);
        lp.height = (int) (mStartHeight - heightToSubtract);
        child.setLayoutParams(lp);
                    } else {
            float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + (mStartHeight/2);

            child.setY(mStartYPosition - distanceYToSubtract);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = (int) (mStartHeight);
            lp.height = (int) (mStartHeight);
            child.setLayoutParams(lp);
        }
        return true;
    }

    private void maybeInitProperties(ImageView child, View dependency) {
        if (mStartYPosition == 0)
            mStartYPosition = (int) (dependency.getY());

        if (mFinalYPosition == 0)
            mFinalYPosition = (dependency.getHeight() /2);

        if (mStartHeight == 0)
            mStartHeight = child.getHeight();

        if (mStartToolbarPosition == 0)
            mStartToolbarPosition = dependency.getY();

        if (mChangeBehaviorPoint == 0) {
            mChangeBehaviorPoint = (child.getHeight() - mCustomFinalHeight) / (2f * (mStartYPosition - mFinalYPosition));
        }

    }
}
