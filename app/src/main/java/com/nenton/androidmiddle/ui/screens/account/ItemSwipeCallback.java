package com.nenton.androidmiddle.ui.screens.account;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.View;

import com.nenton.androidmiddle.R;

/**
 * Created by kefir on 28.11.2016.
 */

// TODO: 01.12.2016 add inner shadow
public abstract class ItemSwipeCallback extends ItemTouchHelper.SimpleCallback {
    private static final String TAG = "ItemSwipeCallback";

    private float mDensity;
    private float mScaleDensity;
    private final Drawable mRightIcon;
    private final Drawable mLeftIcon;
    private final int mRightColor;
    private final int mLeftColor;
    private final String mRightText;
    private final String mLeftText;
    private final float mHorizontalTextMarginPx;
    private final float mTextOffsetY;
    private final float mTextSizePx;
    private final Paint mTextPaint;
    private final float mHorizontalIconMarginPx;
    private final float mIconSizePx;

    //Settings
    private final int RIGHT_ICON = R.drawable.ic_delete_24dp; //use only square vector drawables
    private final int LEFT_ICON = R.drawable.ic_build_24dp; //use only square vector drawables
    private final int RIGHT_COLOR = R.color.colorPrimaryDark;
    private final int LEFT_COLOR = R.color.colorAccent;
    private final int RIGHT_TEXT = R.string.account_remove;
    private final int LEFT_TEXT = R.string.account_edit;
    private final int TEXT_COLOR = R.color.colorPrimary;
    private final float TEXT_SIZE_SP = 15f;
    private final float ICON_SIZE_DP = 24f;
    private final float HORIZONTAL_MARGIN_ICON_DP = 16f;
    private final float HORIZONTAL_MARGIN_TEXT_DP = 48f;
    private final float VERTICAL_OFFSET_TEXT_DP = 1f;

    public ItemSwipeCallback(Context context, int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mDensity = metrics.density;
        mScaleDensity = metrics.scaledDensity;

        mRightIcon = AppCompatDrawableManager.get().getDrawable(context, RIGHT_ICON);
        mLeftIcon = AppCompatDrawableManager.get().getDrawable(context, LEFT_ICON);
        mRightColor = ContextCompat.getColor(context, RIGHT_COLOR);
        mLeftColor = ContextCompat.getColor(context, LEFT_COLOR);
        mRightText = context.getString(RIGHT_TEXT).toUpperCase();
        mLeftText = context.getString(LEFT_TEXT).toUpperCase();

        mHorizontalTextMarginPx = dpToPx(HORIZONTAL_MARGIN_TEXT_DP);
        mTextOffsetY = dpToPx(VERTICAL_OFFSET_TEXT_DP);
        mTextSizePx = spToPx(TEXT_SIZE_SP);

        mTextPaint = new Paint();
        mTextPaint.setColor(ContextCompat.getColor(context, TEXT_COLOR));
        mTextPaint.setTextSize(mTextSizePx);
        mTextPaint.setAntiAlias(true);

        mHorizontalIconMarginPx = dpToPx(HORIZONTAL_MARGIN_ICON_DP);
        mIconSizePx = dpToPx(ICON_SIZE_DP);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public abstract void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;
            float itemHeightPx = (float) itemView.getBottom() - (float) itemView.getTop();

            if (dX > 0) {
                //swipe to right

                //drawing background
                float leftBound = (float) itemView.getLeft();
                float topBound = (float) itemView.getTop();
                float rightBound = dX;
                float bottomBound = (float) itemView.getBottom();
                RectF clipShape = new RectF(leftBound, topBound, rightBound, bottomBound);
                c.clipRect(clipShape);
                c.drawColor(mLeftColor);

                //drawing text
                mTextPaint.setTextAlign(Paint.Align.LEFT);
                int textX = (int) (itemView.getLeft() + mHorizontalTextMarginPx);
                int textY = (int) (itemView.getTop() + itemHeightPx / 2 + mTextSizePx / 2 - mTextOffsetY);
                c.drawText(mLeftText, textX, textY, mTextPaint);

                //drawing icon
                int iconLeft = (int) (itemView.getLeft() + mHorizontalIconMarginPx);
                int iconTop = (int) (itemView.getTop() + itemHeightPx / 2 - mIconSizePx / 2);
                int iconRight = (int) (itemView.getLeft() + mHorizontalIconMarginPx + mIconSizePx);
                int iconBottom = (int) (itemView.getBottom() - itemHeightPx / 2 + mIconSizePx / 2);
                mLeftIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                mLeftIcon.draw(c);

            } else {
                //swipe to left

                //drawing background
                float leftBound = (float) itemView.getRight() + dX;
                float topBound = (float) itemView.getTop();
                float rightBound = (float) itemView.getRight();
                float bottomBound = (float) itemView.getBottom();
                RectF clipShape = new RectF(leftBound, topBound, rightBound, bottomBound);
                c.clipRect(clipShape);
                c.drawColor(mRightColor);

                //drawing text
                mTextPaint.setTextAlign(Paint.Align.RIGHT);
                int textX = (int) (itemView.getRight() - mHorizontalTextMarginPx);
                int textY = (int) (itemView.getTop() + itemHeightPx / 2 + mTextSizePx / 2 - mTextOffsetY);
                c.drawText(mRightText, textX, textY, mTextPaint);

                //drawing icon
                int iconLeft = (int) (itemView.getRight() - mHorizontalIconMarginPx - mIconSizePx);
                int iconTop = (int) (itemView.getTop() + itemHeightPx / 2 - mIconSizePx / 2);
                int iconRight = (int) (itemView.getRight() - mHorizontalIconMarginPx);
                int iconBottom = (int) (itemView.getBottom() - itemHeightPx / 2 + mIconSizePx / 2);
                mRightIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                mRightIcon.draw(c);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private float dpToPx(float dp) {
        return dp * mDensity;
    }

    private float spToPx(float sp) {
        return sp * mScaleDensity;
    }
}