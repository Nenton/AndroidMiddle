package com.nenton.androidmiddle.ui.screens.catalog;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.Button;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.dto.ProductDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.mvp.views.AbstractView;
import com.nenton.androidmiddle.mvp.views.ICatalogView;
import com.nenton.androidmiddle.ui.screens.catalog.adapters.CatalogAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class CatalogView extends AbstractView<CatalogScreen.CatalogPresenter> implements ICatalogView{

    @BindView(R.id.add_to_card_btn)
    Button mAddToCardBtn;
    @BindView(R.id.product_pager)
    ViewPager mViewPager;
    @BindView(R.id.circle_indicator)
    CircleIndicator mCircleIndicator;
    private CatalogAdapter mAdapter;

    public CatalogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<CatalogScreen.Component>getDaggerComponent(context).inject(this);
        mAdapter = new CatalogAdapter();
    }

    @OnClick(R.id.add_to_card_btn)
    void clickAddToCart() {
        mPresenter.clickOnBuyButton(mViewPager.getCurrentItem());
    }


    @Override
    public void showCatalogView() {
        mViewPager.setAdapter(mAdapter);
        mCircleIndicator.setViewPager(mViewPager);
    }

    @Override
    public void updateProductCounter() {
    }

    public int getCurrentPagerPosition(){
        return mViewPager.getCurrentItem();
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    public CatalogAdapter getAdapter() {
        return mAdapter;
    }
}
