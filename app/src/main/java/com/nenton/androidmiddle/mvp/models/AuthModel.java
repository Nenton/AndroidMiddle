package com.nenton.androidmiddle.mvp.models;

public class AuthModel extends AbstractModel{

    public AuthModel() {
    }

    public boolean isUserAuth(){
        if (!mDataManager.getPreferencesManager().getToken().isEmpty()){
            return mDataManager.isUserAuth(mDataManager.getPreferencesManager().getToken());
        } else {
            return false;
        }
    }

    public boolean loginUser(String email, String password){
        return true;
        // TODO: 21.10.2016 Вход пользователя
    }
}
