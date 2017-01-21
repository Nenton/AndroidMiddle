package com.nenton.androidmiddle.ui.screens.catalog.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.realm.ProductRealm;
import com.nenton.androidmiddle.ui.screens.catalog.CatalogScreen;

import java.util.ArrayList;
import java.util.List;

import mortar.MortarScope;

public class CatalogAdapter extends PagerAdapter {

    private static final String TAG = "Catalog Adapter";

    private List<ProductRealm> mProducts = new ArrayList<>();

    public CatalogAdapter() {

    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public void addProduct(ProductRealm product){
        mProducts.add(product);
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ProductRealm productDto = mProducts.get(position);
        Context productContext = CatalogScreen.Factory.createProductContext(productDto, container.getContext());
        View newView = LayoutInflater.from(productContext).inflate(R.layout.screen_product, container, false);
        container.addView(newView);
        return newView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        MortarScope screenScope = MortarScope.getScope(((View)object).getContext());
        container.removeView((View)object);
        screenScope.destroy();
        Log.e(TAG, "DestroyItem with name: " + screenScope.getName());
    }
}
