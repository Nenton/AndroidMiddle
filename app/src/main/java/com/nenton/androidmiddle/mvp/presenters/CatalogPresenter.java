package com.nenton.androidmiddle.mvp.presenters;

import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.mvp.models.CatalogModel;
import com.nenton.androidmiddle.mvp.views.ICatalogView;

import java.util.List;

public class CatalogPresenter extends AbstractPresenter<ICatalogView> implements ICatalogPresenter{
    private static CatalogPresenter ourInstance = new CatalogPresenter();

    private CatalogModel mCatalogModel;
    private List<ProductDto> mProducts;

    public static CatalogPresenter getInstance() {
        return ourInstance;
    }

    private CatalogPresenter() {
        mCatalogModel = new CatalogModel();
    }

    @Override
    public void initView() {
        if (mProducts == null){
            mProducts = mCatalogModel.getProductList();
        }

        if (getView()!=null){
            getView().showCatalogView(mProducts);
        }

    }

    @Override
    public void clickOnBuyButton(int position) {
        if (getView()!=null){
            if (checkUserAuth()){
                getView().showAddToCartMessage(mProducts.get(position));
            } else {
                getView().showAuthScreen();
            }
        }

    }

    @Override
    public boolean checkUserAuth() {
        return mCatalogModel.isUserAuth();
    }
}
