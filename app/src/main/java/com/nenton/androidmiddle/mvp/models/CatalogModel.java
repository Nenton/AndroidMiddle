package com.nenton.androidmiddle.mvp.models;

import com.nenton.androidmiddle.data.managers.DataManager;
import com.nenton.androidmiddle.data.storage.ProductDto;

import java.util.List;

public class CatalogModel extends AbstractModel{

    public CatalogModel() {
    }

    public List<ProductDto> getProductList(){
        return mDataManager.getListProduct();
    }

    public boolean isUserAuth(){
        return mDataManager.isUserAuth();
    }
}