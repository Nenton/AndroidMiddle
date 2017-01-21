package com.nenton.androidmiddle.mvp.views;

import com.nenton.androidmiddle.data.storage.dto.ProductDto;

import java.util.List;

public interface ICatalogView extends IView{

    void showCatalogView();
    void updateProductCounter();
}
