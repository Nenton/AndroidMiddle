package com.nenton.androidmiddle.data.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nenton.androidmiddle.data.storage.dto.ProductLocalInfo;

public class PreferencesManager {

    public static final String PROFILE_FULL_NAME_KEY = "PROFILE_FULL_NAME_KEY";
    public static final String PROFILE_PHONE_KEY = "PROFILE_PHONE_KEY";
    public static final String PROFILE_AVATAR_KEY = "PROFILE_AVATAR_KEY";
    public static final String USER_ADDRESSES_KEY = "USER_ADDRESSES_KEY";
    public static final String NOTIFICATION_PROMO_KEY = "NOTIFICATION_PROMO_KEY";
    public static final String NOTIFICATION_ORDER_KEY = "NOTIFICATION_ORDER_KEY";
    private static final String PRODUCT_LAST_UPDATE_KEY = "PRODUCT_LAST_UPDATE_KEY";
    private final SharedPreferences mSharedPreferences;

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public PreferencesManager(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getLastProductUpdate(){
        return mSharedPreferences.getString(PRODUCT_LAST_UPDATE_KEY, "Wed, 15 Nov 1995 04:58:08 GMT");
    }

    public void saveLastProductUpdate(String lastModified){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PRODUCT_LAST_UPDATE_KEY, lastModified);
        editor.apply();
    }

    public ProductLocalInfo getLocalInfo(int remoteId) {
        return null;
    }
}
