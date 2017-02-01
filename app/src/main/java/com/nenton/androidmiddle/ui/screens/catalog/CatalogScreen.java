package com.nenton.androidmiddle.ui.screens.catalog;

import android.content.Context;
import android.os.Bundle;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.realm.ProductRealm;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.DaggerScope;
import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.flow.Screen;
import com.nenton.androidmiddle.mvp.models.CatalogModel;
import com.nenton.androidmiddle.mvp.presenters.AbstractPresenter;
import com.nenton.androidmiddle.mvp.presenters.ICatalogPresenter;
import com.nenton.androidmiddle.mvp.presenters.MenuItemHolder;
import com.nenton.androidmiddle.mvp.presenters.RootPresenter;
import com.nenton.androidmiddle.ui.activities.RootActivity;
import com.nenton.androidmiddle.ui.screens.auth.AuthScreen;
import com.nenton.androidmiddle.ui.screens.catalog.adapters.CatalogAdapter;
import com.nenton.androidmiddle.ui.screens.product.ProductScreen;
import com.squareup.picasso.Picasso;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import rx.Subscriber;
import rx.Subscription;

@Screen(R.layout.screen_catalog)
public class CatalogScreen extends AbstractScreen<RootActivity.RootComponent> {
    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerCatalogScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    //region ========================= DI =========================
    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(CatalogScreen.class)
        CatalogModel provideCatalogModel() {
            return new CatalogModel();
        }

        @Provides
        @DaggerScope(CatalogScreen.class)
        CatalogPresenter provideCatalogPresenter() {
            return new CatalogPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(CatalogScreen.class)
    public interface Component {
        void inject(CatalogPresenter presenter);
        void inject(CatalogView view);

        CatalogModel getCatalogModel();
        Picasso getPicasso();
        RootPresenter getRootPresenter();

    }

    //endregion

    //region ========================= Presenter =========================

    public class CatalogPresenter extends AbstractPresenter<CatalogView, CatalogModel> implements ICatalogPresenter {

        private int lastPagerPosition;

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            mCompSubs.add(subscribeOnProductRealmObs());
        }

        @Override
        public void dropView(CatalogView view) {
            lastPagerPosition = getView().getCurrentPagerPosition();
            super.dropView(view);
        }

        @Override
        protected void initActionBar() {
            mRootPresenter.newActionBarBuilder()
                    .setTitle("Каталог")
                    .addAction(new MenuItemHolder("В корзину", R.drawable.ic_shopping_basket_black_24dp, item -> {
                        getRootView().showMessage("Перейти в корзину");
                        return true;
                    }))
                    .build();
        }

        @Override
        protected void initFab() {

        }

        @Override
        public void clickOnBuyButton(int position) {
//            mCatalogModel.getProductObs()
//                    .doOnNext(o -> Log.e("THREAD", "call: " + Thread.currentThread().getName()))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(productDto -> {
//                        Log.e("PRODUCT", "from disk or network:  " + productDto.getProductName());
//                    });
            if (getView() != null) {
                if (checkUserAuth() && getRootView() != null) {
                    getRootView().showMessage("Товар " + "" + " успешно добавлен в корзину");
                    getView().showCatalogView();
                } else {
                    Flow.get(getView()).set(new AuthScreen());
                }
            }
        }

        private Subscription subscribeOnProductRealmObs() {
            if (getRootView() != null) {
                getRootView().showLoad();
            }
            return mModel.getProductObs()
                    .subscribe(new RealmSubscriber());
        }

        @Override
        public boolean checkUserAuth() {
            return mModel.isUserAuth();
        }

        private class RealmSubscriber extends Subscriber<ProductRealm> {
            CatalogAdapter mAdapter = getView().getAdapter();

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getRootView() != null) {
                    getRootView().showError(e);
                }
            }

            @Override
            public void onNext(ProductRealm productRealm) {
                mAdapter.addProduct(productRealm);
                if (mAdapter.getCount() - 1 == lastPagerPosition);
                getRootView().hideLoad();
                getView().showCatalogView();
            }
        }
    }

    //endregion

    public static class Factory {
        public static Context createProductContext(ProductRealm product, Context parentContext) {
            MortarScope parentScope = MortarScope.getScope(parentContext);
            MortarScope childScope = null;
            ProductScreen screen = new ProductScreen(product);
            String scopeName = String.format("%s_%s", screen.getScopeName(), product.getId());

            if (parentScope.findChild(scopeName) == null) {
                childScope = parentScope.buildChild()
                        .withService(DaggerService.SERVICE_NAME,
                                screen.createScreenComponent(DaggerService.<CatalogScreen.Component>getDaggerComponent(parentContext)))
                        .build(scopeName);
            } else {
                childScope = parentScope.findChild(scopeName);
            }
            return childScope.createContext(parentContext);
        }
    }
}
