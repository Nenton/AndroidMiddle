package com.nenton.androidmiddle.ui.screens.product;

import android.os.Bundle;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.dto.ProductDto;
import com.nenton.androidmiddle.data.storage.realm.ProductRealm;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.ProductScope;
import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.flow.Screen;
import com.nenton.androidmiddle.mvp.models.CatalogModel;
import com.nenton.androidmiddle.mvp.presenters.AbstractPresenter;
import com.nenton.androidmiddle.mvp.presenters.IProductPresenter;
import com.nenton.androidmiddle.ui.screens.catalog.CatalogScreen;
import com.nenton.androidmiddle.ui.screens.product_details.DetailsScreen;

import dagger.Provides;
import flow.Flow;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import mortar.MortarScope;

@Screen(R.layout.screen_product)
public class ProductScreen extends AbstractScreen<CatalogScreen.Component> {

    private ProductRealm mProductRealm;

    public ProductScreen(ProductRealm productDto) {
        mProductRealm = productDto;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ProductScreen && mProductRealm.equals(((ProductScreen) o).mProductRealm);
    }

    @Override
    public int hashCode() {
        return mProductRealm.hashCode();
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
    public class Module {
        @Provides
        @ProductScope
        ProductPresenter provideProductPresenter() {
            return new ProductPresenter(mProductRealm);
        }
    }

    @dagger.Component(dependencies = CatalogScreen.Component.class, modules = Module.class)
    @ProductScope
    public interface Component {
        void inject(ProductPresenter productPresenter);

        void inject(ProductView productView);
    }

    //endregion

    //region ========================= Presenter =========================

    public class ProductPresenter extends AbstractPresenter<ProductView, CatalogModel> implements IProductPresenter {

        private ProductRealm mProduct;
        private RealmChangeListener mListener;

        public ProductPresenter(ProductRealm productRealm) {
            this.mProduct = productRealm;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() != null && mProduct.isValid()) {
                getView().showProductView(new ProductDto(mProduct));
                mListener = element -> {
                    if (getView() != null) {
                        getView().showProductView(new ProductDto(mProduct));
                    }
                };
                mProduct.addChangeListener(mListener);
            } else {
                // TODO: 10.01.2017  imptement me
            }
        }

        @Override
        public void dropView(ProductView view) {
            mProduct.removeChangeListener(mListener);
            super.dropView(view);
        }

        @Override
        protected void initActionBar() {
            //empty
        }

        @Override
        protected void initFab() {

        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        public void clickOnMinus() {
            if (getView() != null) {
                if (mProduct.getCount() > 0) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(realm1 -> mProduct.remove());
                    realm.close();
                }
            }
        }

        @Override
        public void clickOnPlus() {
            if (getView() != null) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(realm1 -> mProduct.add());
                realm.close();
            }
        }

        public void clickShowMore() {
            Flow.get(getView()).set(new DetailsScreen(mProduct));
        }

        public void clickFavorite() {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> mProduct.changeFavorite());
            realm.close();
        }
    }

    //endregion

}
