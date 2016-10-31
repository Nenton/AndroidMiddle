package com.nenton.androidmiddle.ui.fragments.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.ui.fragments.ProductFragment;

import java.util.ArrayList;
import java.util.List;

public class CatalogAdapter extends FragmentPagerAdapter{

    private List<ProductDto> mProducts = new ArrayList<>();

    public CatalogAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return ProductFragment.newInstance(mProducts.get(position));
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    public void addProduct(ProductDto product){
        mProducts.add(product);
        notifyDataSetChanged();
    }
}
