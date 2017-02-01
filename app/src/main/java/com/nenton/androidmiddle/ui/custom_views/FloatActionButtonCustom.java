package com.nenton.androidmiddle.ui.custom_views;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nenton.androidmiddle.ui.activities.RootActivity;

/**
 * Created by serge on 27.01.2017.
 */

public class FloatActionButtonCustom {

    static public class Builder {
        private FrameLayout.LayoutParams params;
        private final Activity activity;
        int gravity = Gravity.BOTTOM | Gravity.RIGHT; // default bottom right
        Drawable drawable;
        int color = Color.WHITE;
        int size = 0;
        float scale = 0;

        public Builder(Activity context) {
            scale = context.getResources().getDisplayMetrics().density;
            size = (int) (72 * scale + 0.5f); // default size is 72dp by 72dp
            params = new FrameLayout.LayoutParams(size, size);
            params.gravity = gravity;
            this.activity = context;
        }

        /**
         * Sets the gravity for the FAB
         */
        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * Sets the FAB drawable
         */
        public Builder setDrawable(final Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        /**
         * Sets the FAB color
         */
        public Builder setColor(final int color) {
            this.color = color;
            return this;
        }


        public FloatingActionButton create() {
            final FloatingActionButton button = new FloatingActionButton(activity);
            button.setBackgroundColor(this.color);
            button.setImageDrawable(this.drawable);
            params.gravity = this.gravity;
            ViewGroup root = (ViewGroup) activity.findViewById(android.R.id.content);
            root.addView(button, params);
            return button;
        }
    }
}
