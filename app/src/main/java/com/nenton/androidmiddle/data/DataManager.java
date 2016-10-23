package com.nenton.androidmiddle.data;


import android.content.SharedPreferences;

import com.nenton.androidmiddle.utils.AndroidMiddleAplication;
import com.nenton.androidmiddle.utils.Constants;

public class DataManager {

    private static DataManager INSTANCE = null;

    private SharedPreferences mSharedPreferences;

    private DataManager() {
        mSharedPreferences = AndroidMiddleAplication.getSharedPreferences();
    }

    public static DataManager getInstanse() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public String getAuthToken(){
        return mSharedPreferences.getString(Constants.USER_AUTH_TOKEN,"");
    }

    public void setAuthToken(String token){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.USER_AUTH_TOKEN,token);
        editor.apply();
    }

    public boolean equalsToken(String token){
        return token.equals(mSharedPreferences.getString(Constants.USER_AUTH_TOKEN,""));
    }
}
