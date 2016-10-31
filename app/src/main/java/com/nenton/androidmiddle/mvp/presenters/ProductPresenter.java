package com.nenton.androidmiddle.mvp.presenters;

import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.mvp.models.ProductModel;
import com.nenton.androidmiddle.mvp.views.IProductView;

public class ProductPresenter extends AbstractPresenter<IProductView> implements IProductPresenter{

    private ProductModel mProductModel;
    private ProductDto mProduct;

    public static ProductPresenter newInstance(ProductDto product) {
        return new ProductPresenter(product);
    }

    private ProductPresenter(ProductDto product) {
        mProduct = product;
        mProductModel = new ProductModel();
    }

    @Override
    public void initView() {
        if (getView() != null){
            getView().showProductView(mProduct);
        }

    }

    @Override
    public void clickOnMinus() {
        if (mProduct.getCount()>0){
            mProduct.deleteCountProduct();
            mProductModel.updateProduct(mProduct);
            if (getView()!=null){
                getView().updateProductCountView(mProduct);
            }
        }
    }

    @Override
    public void clickOnPlus() {
        mProduct.addCountProduct();
        mProductModel.updateProduct(mProduct);
        if (getView()!=null){
            getView().updateProductCountView(mProduct);
        }
    }
}
