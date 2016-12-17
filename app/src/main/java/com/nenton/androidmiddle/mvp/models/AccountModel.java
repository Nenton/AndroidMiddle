package com.nenton.androidmiddle.mvp.models;

import android.net.Uri;

import com.nenton.androidmiddle.data.managers.PreferencesManager;
import com.nenton.androidmiddle.data.storage.UserAddressDto;
import com.nenton.androidmiddle.data.storage.UserDto;

import java.util.ArrayList;
import java.util.Map;

public class AccountModel extends AbstractModel{

    public UserDto getUserDto(){
        return new UserDto(getUserProfileInfo(), getUserAddresses(), getUserSettings());
    }

    private Map<String, String> getUserProfileInfo(){
        return mDataManager.getUserProfileInfo();
    }

    private ArrayList<UserAddressDto> getUserAddresses(){
        return mDataManager.getUserAddresses();
    }

    private Map<String, Boolean> getUserSettings(){
        return mDataManager.getUserSettings();
    }

    public void saveProfileInfo(String name, String phone){
        mDataManager.saveProfileInfo(name, phone);
    }

    public void saveAvatarPhoto(Uri photoUri){
        // TODO: 13.12.2016 implement this
    }

    public void savePromoNotification(boolean isChecked){
        mDataManager.saveSetting(PreferencesManager.NOTIFICATION_PROMO_KEY, isChecked);
    }

    public void saveOrderNotification(boolean isChecked){
        mDataManager.saveSetting(PreferencesManager.NOTIFICATION_ORDER_KEY, isChecked);
    }

    public void addAddress(UserAddressDto userAddressDto){
        mDataManager.addAddress(userAddressDto);
    }

    // TODO: 13.12.2016 remove address
}
