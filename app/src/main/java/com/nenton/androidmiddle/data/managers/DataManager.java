package com.nenton.androidmiddle.data.managers;


import android.content.SharedPreferences;

import com.nenton.androidmiddle.data.network.RestService;
import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.di.DaggerService;

import com.nenton.androidmiddle.di.components.DaggerDataManagerComponent;
import com.nenton.androidmiddle.di.components.DataManagerComponent;
import com.nenton.androidmiddle.di.modules.LocalModule;
import com.nenton.androidmiddle.di.modules.NetworkModule;
import com.nenton.androidmiddle.utils.AndroidMiddleAplication;
import com.nenton.androidmiddle.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DataManager {

    @Inject
    RestService mRestService;
    @Inject
    PreferencesManager mPreferencesManager;

    private List<ProductDto> mProductDto;

    private boolean mUserAuth;

    public DataManager() {
        DataManagerComponent component = DaggerService.getComponent(DataManagerComponent.class);
        if (component == null){
            component = DaggerDataManagerComponent.builder()
            .appComponent(AndroidMiddleAplication.getAppComponent())
            .localModule(new LocalModule())
            .networkModule(new NetworkModule())
            .build();
            DaggerService.registerComponent(DataManagerComponent.class,component);
        }
        component.inject(this);
//        mSharedPreferences = AndroidMiddleAplication.getSharedPreferences();
        generateProducts();
    }


    public String getAuthToken() {
        return mPreferencesManager.getSharedPreferences().getString(Constants.USER_AUTH_TOKEN, "");
    }

    public void setAuthToken(String token) {
        SharedPreferences.Editor editor = mPreferencesManager.getSharedPreferences().edit();
        editor.putString(Constants.USER_AUTH_TOKEN, token);
        editor.apply();
    }

    public boolean equalsToken(String token) {
        return token.equals(mPreferencesManager.getSharedPreferences().getString(Constants.USER_AUTH_TOKEN, ""));
    }

    public ProductDto getProductById(int productId) {
        return null;
    }

    public void updateProduct(ProductDto product) {

    }

    public List<ProductDto> getListProduct() {
        return mProductDto;
    }


    private void generateProducts() {
        mProductDto = new ArrayList<>();
        mProductDto.add(new ProductDto(1, "Name_1", "http://cdn-4.nikon-cdn.com/en_INC/o/PressRelease/J_4uHt00U-sY95iCnB8fzhvkokM/S2_11_27.5_YW_front.high.jpg", "Description_1", 100, 1));
        mProductDto.add(new ProductDto(2, "Name_2", "http://veleks.ru/wp-content/uploads/2016/01/IMG_8295-1.png", "Description_1", 200, 1));
        mProductDto.add(new ProductDto(3, "Name_3", "http://ocodeco.ru/d/687051/d/диван.jpg", "Description_1", 300, 1));
        mProductDto.add(new ProductDto(4, "Name_4", "http://s019.radikal.ru/i601/1205/02/143ebfe4fbad.jpg", "Description_1", 400, 1));
        mProductDto.add(new ProductDto(5, "Name_5", "http://www.ixbt.com/mobile/cat-b100/cat-b100-box.jpg", "Description_1", 500, 1));
        mProductDto.add(new ProductDto(6, "Name_6", "http://img2.korablik.ru/upload/catalog/25912/25912_1000.jpg", "Description_1", 600, 1));
//        mProductDto.add(new ProductDto(7, "Name_7", "", "Description_1", 700, 1));
//        mProductDto.add(new ProductDto(8, "Name_8", "", "Description_1", 800, 1));
//        mProductDto.add(new ProductDto(9, "Name_9", "", "Description_1", 900, 1));
//        mProductDto.add(new ProductDto(10, "Name_10", "", "Description_1", 1000, 1));
    }

    public boolean isUserAuth() {
        // TODO: 30.10.2016 auth user 
        return mUserAuth;
    }
}