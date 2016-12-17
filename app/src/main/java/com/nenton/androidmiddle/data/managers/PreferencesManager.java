package com.nenton.androidmiddle.data.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesManager {

    public static final String PROFILE_FULL_NAME_KEY = "PROFILE_FULL_NAME_KEY";
    public static final String PROFILE_PHONE_KEY = "PROFILE_PHONE_KEY";
    public static final String PROFILE_AVATAR_KEY = "PROFILE_AVATAR_KEY";
    public static final String USER_ADDRESSES_KEY = "USER_ADDRESSES_KEY";
    public static final String NOTIFICATION_PROMO_KEY = "NOTIFICATION_PROMO_KEY";
    public static final String NOTIFICATION_ORDER_KEY = "NOTIFICATION_ORDER_KEY";
    private final SharedPreferences mSharedPreferences;

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public PreferencesManager(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
}
