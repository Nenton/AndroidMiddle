package com.nenton.androidmiddle.ui.screens.product_details;

import android.os.Bundle;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.realm.ProductRealm;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.DaggerScope;
import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.flow.Screen;
import com.nenton.androidmiddle.mvp.models.DetailModel;
import com.nenton.androidmiddle.mvp.presenters.AbstractPresenter;
import com.nenton.androidmiddle.mvp.presenters.MenuItemHolder;
import com.nenton.androidmiddle.mvp.presenters.RootPresenter;
import com.nenton.androidmiddle.ui.screens.catalog.CatalogScreen;
import com.squareup.picasso.Picasso;

import dagger.Provides;
import flow.TreeKey;
import mortar.MortarScope;

/**
 * Created by serge on 05.01.2017.
 */
@Screen(R.layout.screen_detail)
public class DetailsScreen extends AbstractScreen<CatalogScreen.Component> implements TreeKey {

    private final ProductRealm mProductRealm;

    public DetailsScreen(ProductRealm product) {
        mProductRealm = product;
    }

    @Override
    public Object createScreenComponent(CatalogScreen.Component parentComponent) {
        return DaggerDetailsScreen_Component.builder()
                .component(parentComponent)
                .module(new Module())
                .build();
    }

    @Override
    public Object getParentKey() {
        return new CatalogScreen();
    }

    //region ========================= DI =========================

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(DetailsScreen.class)
        DetailPresenter provideDetailPresenter() {
            return new DetailPresenter(mProductRealm);
        }

        @Provides
        @DaggerScope(DetailsScreen.class)
        DetailModel provideDetailModel() {
            return new DetailModel();
        }

    }

    @dagger.Component(dependencies = CatalogScreen.Component.class, modules = Module.class)
    @DaggerScope(DetailsScreen.class)
    public interface Component {

        void inject(DetailPresenter detailPresenter);
        void inject(DetailView detailView);

        DetailModel getDetailModel();
        RootPresenter getRootPresenter();
        Picasso getPicasso();

    }

    //endregion

    public class DetailPresenter extends AbstractPresenter<DetailView, DetailModel> {

        private final ProductRealm mProduct;

        public DetailPresenter(ProductRealm productRealm) {
            mProduct = productRealm;
        }

        @Override
        protected void initActionBar() {
            mRootPresenter.newActionBarBuilder()
                    .setTitle(mProduct.getProductName())
                    .setBackArrow(true)
                    .addAction(new MenuItemHolder("В корзину", R.drawable.ic_shopping_basket_black_24dp, item -> {
                        getRootView().showMessage("Перейти в корзину");
                        return true;
                    }))
                    .setTab(getView().getViewPager())
                    .build();
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            getView().initView(mProduct);
        }
    }
}
