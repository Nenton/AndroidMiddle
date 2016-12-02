package com.nenton.androidmiddle.mvp.views;

import com.nenton.androidmiddle.data.storage.ProductDto;

import java.util.List;

public interface ICatalogView extends IView{

    void showCatalogView(List<ProductDto> productList);
    void updateProductCounter();
}
