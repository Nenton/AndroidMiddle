package com.nenton.androidmiddle.ui.screens.product;

import android.os.Bundle;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.ProductScope;
import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.flow.Screen;
import com.nenton.androidmiddle.mvp.models.CatalogModel;
import com.nenton.androidmiddle.mvp.presenters.IProductPresenter;
import com.nenton.androidmiddle.ui.screens.catalog.CatalogScreen;

import javax.inject.Inject;

import dagger.Provides;
import mortar.MortarScope;
import mortar.ViewPresenter;

@Screen(R.layout.screen_product)
public class ProductScreen extends AbstractScreen<CatalogScreen.Component>{

    private ProductDto mProductDto;

    public ProductScreen(ProductDto productDto) {
        mProductDto = productDto;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ProductScreen && mProductDto.equals(((ProductScreen) o).mProductDto);
    }

    @Override
    public int hashCode() {
        return mProductDto.hashCode();
    }

    @Override
    public Object createScreenComponent(CatalogScreen.Component parentComponent) {
        return DaggerProductScreen_Component.builder()
                .component(parentComponent)
                .module(new Module())
                .build();
    }

    //region ========================= DI =========================

    @dagger.Module
    public class Module{
        @Provides
        @ProductScope
        ProductPresenter provideProductPresenter(){
            return new ProductPresenter(mProductDto);
        }
    }

    @dagger.Component(dependencies = CatalogScreen.Component.class, modules = Module.class)
    @ProductScope
    public interface Component{
        void inject(ProductPresenter productPresenter);
        void inject(ProductView productView);
    }

    //endregion

    //region ========================= Presenter =========================

    public class ProductPresenter extends ViewPresenter<ProductView> implements IProductPresenter{

        private ProductDto mProduct;

        @Inject
        CatalogModel mCatalogModel;

        public ProductPresenter(ProductDto productDto) {
            this.mProduct = productDto;
        }

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            ((Component)scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() != null){
                getView().showProductView(mProduct);
            }
        }

        @Override
        public void clickOnMinus() {
            if (mProduct.getCount()>0){
                mProduct.deleteCountProduct();
                mCatalogModel.updateProduct(mProduct);
                if (getView()!=null){
                    getView().updateProductCountView(mProduct);
                }
            }
        }

        @Override
        public void clickOnPlus() {
            mProduct.addCountProduct();
            mCatalogModel.updateProduct(mProduct);
            if (getView()!=null){
                getView().updateProductCountView(mProduct);
            }
        }
    }

    //endregion

}
