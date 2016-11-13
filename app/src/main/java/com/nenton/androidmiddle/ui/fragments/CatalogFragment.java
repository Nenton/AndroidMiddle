package com.nenton.androidmiddle.ui.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.CatalogScope;
import com.nenton.androidmiddle.mvp.presenters.CatalogPresenter;
import com.nenton.androidmiddle.mvp.views.ICatalogView;
import com.nenton.androidmiddle.ui.activities.RootActivity;
import com.nenton.androidmiddle.ui.fragments.adapters.CatalogAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Provides;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class CatalogFragment extends Fragment implements ICatalogView {

    @Inject
    CatalogPresenter mCatalogPresenter;

    @BindView(R.id.add_to_card_btn)
    Button mAddToCardBtn;
    @BindView(R.id.product_pager)
    ViewPager mViewPager;
    @BindView(R.id.circle_indicator)
    CircleIndicator mCircleIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        ButterKnife.bind(this, view);
        createComponent().inject(this);
        mCatalogPresenter.takeView(this);
        mCatalogPresenter.initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        mCatalogPresenter.dropView();
        super.onDestroyView();
    }

    @OnClick(R.id.add_to_card_btn)
    void clickOnBuyBtn() {
        mCatalogPresenter.clickOnBuyButton(mViewPager.getCurrentItem());
    }


    //region ========================= ICatalogView =========================

    @Override
    public void showCatalogView(List<ProductDto> productList) {
        CatalogAdapter catalogAdapter = new CatalogAdapter(getChildFragmentManager());
        for (ProductDto productDto : productList) {
            catalogAdapter.addProduct(productDto);
        }
        mViewPager.setAdapter(catalogAdapter);
        mCircleIndicator.setViewPager(mViewPager);
    }
//
//    @Override
//    public void showAddToCartMessage(ProductDto product) {
//        showMessage();
//    }

    @Override
    public void showAuthScreen() {
//        getRootActivity().showAuthScreen();
// TODO: 30.10.2016 show auth screen
    }

    @Override
    public void updateProductCounter() {
// TODO: 30.10.2016 update count on cart icon
    }

    //endregion



    //region ========================= DI =========================

    private Component createComponent(){
        Component component = DaggerService.getComponent(Component.class);
        if (component == null){
            component = DaggerCatalogFragment_Component.builder()
                    .module(new Module())
                    .build();
            DaggerService.registerComponent(Component.class, component);
        }
        return component;
    }

    @dagger.Module
    public class Module{
        @Provides
        @CatalogScope
        CatalogPresenter provideCatalogPresenter(){
            return new CatalogPresenter();
        }
    }

    @dagger.Component(modules = Module.class)
    @CatalogScope
    interface Component{
        void inject(CatalogFragment catalogFragment);
    }

    //endregion

}