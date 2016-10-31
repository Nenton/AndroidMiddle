package com.nenton.androidmiddle.mvp.presenters;

import android.support.annotation.Nullable;

import com.nenton.androidmiddle.data.storage.ProductDto;

import java.util.HashMap;
import java.util.Map;

public class ProductPresenterFactory {
    private static final Map<String, ProductPresenter> sPresenterMap = new HashMap<String, ProductPresenter>();

    private static void registerPresenter(ProductDto product, ProductPresenter presenter){
        sPresenterMap.put(String.valueOf(product.getId()),presenter);
    }

    public static ProductPresenter getInstance(ProductDto product){
        ProductPresenter presenter = sPresenterMap.get(String.valueOf(product.getId()));
        if (presenter == null){
            presenter = ProductPresenter.newInstance(product);
            registerPresenter(product,presenter);
        }
        return presenter;
    }
}
