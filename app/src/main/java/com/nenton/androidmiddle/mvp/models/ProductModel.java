package com.nenton.androidmiddle.mvp.models;

import com.nenton.androidmiddle.data.managers.DataManager;
import com.nenton.androidmiddle.data.storage.ProductDto;

public class ProductModel extends AbstractModel{

    public ProductDto getProductById(int productId){
        return mDataManager.getProductById(productId);
    }

    public void updateProduct(ProductDto product){
        mDataManager.updateProduct(product);
    }
}
