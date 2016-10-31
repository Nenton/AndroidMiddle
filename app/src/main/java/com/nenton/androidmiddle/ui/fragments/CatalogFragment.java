package com.nenton.androidmiddle.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.mvp.presenters.CatalogPresenter;
import com.nenton.androidmiddle.mvp.views.ICatalogView;
import com.nenton.androidmiddle.ui.activities.RootActivity;
import com.nenton.androidmiddle.ui.fragments.adapters.CatalogAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CatalogFragment extends Fragment implements ICatalogView {

    private CatalogPresenter mCatalogPresenter = CatalogPresenter.getInstance();

    @BindView(R.id.add_to_card_btn)
    Button mAddToCardBtn;
    @BindView(R.id.product_pager)
    ViewPager mViewPager;

    public CatalogFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        ButterKnife.bind(this, view);
        mCatalogPresenter.takeView(this);
        mCatalogPresenter.initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        mCatalogPresenter.dropView();
        super.onDestroyView();
    }

    @OnClick
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
    }

    @Override
    public void showAddToCartMessage(ProductDto product) {
        showMessage("Товар " + product.getProductName() + " успешно добавлен в корзину");
    }

    @Override
    public void showAuthScreen() {
// TODO: 30.10.2016 show auth screen 
    }

    @Override
    public void updateProductCounter() {
// TODO: 30.10.2016 update count on cart icon
    }

    //endregion

    //region ========================= IView =========================


    @Override
    public void showMessage(String message) {
        getRootActivity().showMessage(message);
    }

    @Override
    public void showError(Exception e) {
        getRootActivity().showError(e);
    }

    @Override
    public void showLoad() {
        getRootActivity().showLoad();
    }

    @Override
    public void hideLoad() {
        getRootActivity().hideLoad();
    }

    //endregion

    private RootActivity getRootActivity() {
        return (RootActivity) getActivity();
    }
}