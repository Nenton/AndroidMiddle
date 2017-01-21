package com.nenton.androidmiddle.ui.screens.product_details;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.realm.ProductRealm;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.mvp.views.AbstractView;

import butterknife.BindView;

/**
 * Created by serge on 05.01.2017.
 */

public class DetailView extends AbstractView <DetailsScreen.DetailPresenter>{

    @BindView(R.id.detail_pager)
    protected ViewPager mViewPager;

    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<DetailsScreen.Component>getDaggerComponent(context).inject(this);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void initView(ProductRealm productRealm){
        DetailAdapter adapter = new DetailAdapter(productRealm);
        mViewPager.setAdapter(adapter);
    }
}
