package com.nenton.androidmiddle.mvp.views;


import com.nenton.androidmiddle.data.storage.ProductDto;

public interface IProductView extends IView{
    void showProductView(ProductDto product);
    void updateProductCountView(ProductDto product);
}
