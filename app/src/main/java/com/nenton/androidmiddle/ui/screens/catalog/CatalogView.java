package com.nenton.androidmiddle.ui.screens.catalog;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.mvp.views.ICatalogView;
import com.nenton.androidmiddle.ui.screens.catalog.adapters.CatalogAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class CatalogView extends RelativeLayout implements ICatalogView{

    @BindView(R.id.add_to_card_btn)
    Button mAddToCardBtn;
    @BindView(R.id.product_pager)
    ViewPager mViewPager;
    @BindView(R.id.circle_indicator)
    CircleIndicator mCircleIndicator;

    @Inject
    CatalogScreen.CatalogPresenter mCatalogPresenter;

    public CatalogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()){
            DaggerService.<CatalogScreen.Component>getDaggerComponent(context).inject(this);
        }
    }

    @OnClick(R.id.add_to_card_btn)
    void clickAddToCart() {
        mCatalogPresenter.clickOnBuyButton(mViewPager.getCurrentItem());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        if (!isInEditMode()) {
//            showViewFromState();
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            mCatalogPresenter.takeView(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!isInEditMode()) {
            mCatalogPresenter.dropView(this);
        }
    }

    @Override
    public void showCatalogView(List<ProductDto> productList) {
        CatalogAdapter catalogAdapter = new CatalogAdapter();
        for (ProductDto productDto : productList) {
            catalogAdapter.addProduct(productDto);
        }
        mViewPager.setAdapter(catalogAdapter);
        mCircleIndicator.setViewPager(mViewPager);
    }

    @Override
    public void updateProductCounter() {

    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }
}
