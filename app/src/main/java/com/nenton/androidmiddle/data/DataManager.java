package com.nenton.androidmiddle.data;


import android.content.SharedPreferences;

import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.utils.AndroidMiddleAplication;
import com.nenton.androidmiddle.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static DataManager INSTANCE = null;

    private SharedPreferences mSharedPreferences;

    private List<ProductDto> mProductDto;

    private boolean mUserAuth;

    private DataManager() {
        mSharedPreferences = AndroidMiddleAplication.getSharedPreferences();
        generateProducts();
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

    public String getAuthToken() {
        return mSharedPreferences.getString(Constants.USER_AUTH_TOKEN, "");
    }

    public void setAuthToken(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.USER_AUTH_TOKEN, token);
        editor.apply();
    }

    public boolean equalsToken(String token) {
        return token.equals(mSharedPreferences.getString(Constants.USER_AUTH_TOKEN, ""));
    }

    public ProductDto getProductById(int productId) {
        return null;
    }

    public void updateProduct(ProductDto product) {

    }

    public List<ProductDto> getListProduct(){
        return mProductDto;
    }


    private void generateProducts() {
        mProductDto = new ArrayList<>();
        mProductDto.add(new ProductDto(1, "Name_1", "", "Description_1", 100, 1));
        mProductDto.add(new ProductDto(2, "Name_2", "", "Description_1", 200, 1));
        mProductDto.add(new ProductDto(3, "Name_3", "", "Description_1", 300, 1));
        mProductDto.add(new ProductDto(4, "Name_4", "", "Description_1", 400, 1));
        mProductDto.add(new ProductDto(5, "Name_5", "", "Description_1", 500, 1));
        mProductDto.add(new ProductDto(6, "Name_6", "", "Description_1", 600, 1));
        mProductDto.add(new ProductDto(7, "Name_7", "", "Description_1", 700, 1));
        mProductDto.add(new ProductDto(8, "Name_8", "", "Description_1", 800, 1));
        mProductDto.add(new ProductDto(9, "Name_9", "", "Description_1", 900, 1));
        mProductDto.add(new ProductDto(10, "Name_10", "", "Description_1", 1000, 1));
    }

    public boolean isUserAuth() {
        // TODO: 30.10.2016 auth user 
        return mUserAuth;
    }
}
