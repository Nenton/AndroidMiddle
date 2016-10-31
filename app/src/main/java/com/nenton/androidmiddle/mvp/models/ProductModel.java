package com.nenton.androidmiddle.mvp.models;

import com.nenton.androidmiddle.data.DataManager;
import com.nenton.androidmiddle.data.storage.ProductDto;

public class ProductModel {
    private DataManager mDataManager = DataManager.getInstanse();

    public ProductDto getProductById(int productId){
        return mDataManager.getProductById(productId);
    }

    public void updateProduct(ProductDto product){
        mDataManager.updateProduct(product);
    }
}
