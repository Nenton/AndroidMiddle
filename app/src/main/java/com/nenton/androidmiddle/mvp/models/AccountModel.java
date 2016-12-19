package com.nenton.androidmiddle.mvp.models;

import com.nenton.androidmiddle.data.storage.UserAddressDto;
import com.nenton.androidmiddle.data.storage.UserInfoDto;
import com.nenton.androidmiddle.data.storage.UserSettingsDto;

import java.util.ArrayList;
import java.util.Map;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

import static com.nenton.androidmiddle.data.managers.PreferencesManager.*;

public class AccountModel extends AbstractModel{

    private PublishSubject<UserInfoDto> mUserInfoObs = PublishSubject.create();

    public AccountModel() {
        mUserInfoObs.onNext(getUserProfileInfo());
    }
    //region ========================= Addresses =========================

    public Observable<UserAddressDto> getAddressObs(){
        return Observable.from(getUserAddresses());
    }

    private ArrayList<UserAddressDto> getUserAddresses(){
        return mDataManager.getUserAddresses();
    }

    public void updateOrInsertAddress(UserAddressDto address){
        mDataManager.updateOrInsertAddress(address);
    }

    public void removeAddress(UserAddressDto address){
        mDataManager.removeAddress(address);
    }

    public UserAddressDto getAddressFromPosition(int position){
        return getUserAddresses().get(position);
    }

    //endregion

    //region ========================= Settings =========================

    public Observable<UserSettingsDto> getUserSettingsObs(){
        return Observable.just(getUserSettings());
    }

    private UserSettingsDto getUserSettings(){
        Map<String, Boolean> userSettings = mDataManager.getUserSettings();
        return new UserSettingsDto(userSettings.get(NOTIFICATION_ORDER_KEY), userSettings.get(NOTIFICATION_PROMO_KEY));
    }

    public void saveSettings(UserSettingsDto settings) {
        mDataManager.saveSetting(NOTIFICATION_ORDER_KEY, settings.isOrderNotification());
        mDataManager.saveSetting(NOTIFICATION_PROMO_KEY, settings.isPromoNotification());
    }

    //endregion

    //region ========================= User =========================

    public void saveProfileInfo(UserInfoDto infoDto){
        mDataManager.saveProfileInfo(infoDto.getName(), infoDto.getPhone(), infoDto.getAvatar());
        mUserInfoObs.onNext(infoDto);
    }

    public UserInfoDto getUserProfileInfo(){
        Map<String, String> map = mDataManager.getUserProfileInfo();
        return new UserInfoDto(map.get(PROFILE_FULL_NAME_KEY),
                map.get(PROFILE_PHONE_KEY),
                map.get(PROFILE_AVATAR_KEY));
    }

    public Observable<UserInfoDto> getUserInfoObs(){
        return mUserInfoObs;
    }
    //endregion

}