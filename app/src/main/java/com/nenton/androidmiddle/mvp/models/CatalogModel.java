package com.nenton.androidmiddle.mvp.models;

import com.nenton.androidmiddle.data.DataManager;
import com.nenton.androidmiddle.data.storage.ProductDto;

import java.util.List;

public class CatalogModel {

    private DataManager mDataManager = DataManager.getInstanse();

    public CatalogModel() {
    }

    public List<ProductDto> getProductList(){
        return mDataManager.getListProduct();
    }

    public boolean isUserAuth(){
        return mDataManager.isUserAuth();
    }
}
