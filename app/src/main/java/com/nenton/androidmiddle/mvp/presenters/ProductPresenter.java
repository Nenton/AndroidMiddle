package com.nenton.androidmiddle.mvp.presenters;

import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.ProductScope;
import com.nenton.androidmiddle.mvp.models.ProductModel;
import com.nenton.androidmiddle.mvp.views.IProductView;

import dagger.Provides;

public class ProductPresenter extends AbstractPresenter<IProductView> implements IProductPresenter{

    private ProductModel mProductModel;
    private ProductDto mProduct;

    public ProductPresenter(ProductDto product) {
        Component component = createComponent();
        component.inject(this);
        mProduct = product;
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
    //region ========================= DI =========================
    private Component createComponent(){
        Component component = DaggerService.getComponent(Component.class);
        if (component == null){
            component = DaggerProductPresenter_Component.builder()
                    .module(new Module())
                    .build();
            DaggerService.registerComponent(Component.class,component);
        }
        return component;
    }

    @dagger.Module
    public class Module{
        @Provides
        @ProductScope
        ProductModel provideProductModel() {
            return new ProductModel();
        }
    }

    @dagger.Component(modules = Module.class)
    @ProductScope
    interface Component{
        void inject(ProductPresenter productPresenter);
    }

    //endregion
}
