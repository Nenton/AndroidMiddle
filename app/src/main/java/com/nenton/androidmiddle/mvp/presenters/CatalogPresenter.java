package com.nenton.androidmiddle.mvp.presenters;

import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.CatalogScope;
import com.nenton.androidmiddle.mvp.models.CatalogModel;
import com.nenton.androidmiddle.mvp.views.ICatalogView;
import com.nenton.androidmiddle.mvp.views.IRootView;
import com.nenton.androidmiddle.ui.activities.RootActivity;

import java.util.List;

import javax.inject.Inject;

import dagger.Provides;

public class CatalogPresenter extends AbstractPresenter<ICatalogView> implements ICatalogPresenter{

    @Inject
    RootPresenter mRootPresenter;

    @Inject
    CatalogModel mCatalogModel;

    private List<ProductDto> mProducts;

    public CatalogPresenter() {
        createComponent().inject(this);
    }

    private IRootView getRootView(){
        return mRootPresenter.getView();
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
                getRootView().showMessage("Товар " + mProducts.get(position).getProductName() + " успешно добавлен в корзину");
//                getView().showAddToCartMessage(mProducts.get(position));
            } else {
                getView().showAuthScreen();
            }
        }

    }

    @Override
    public boolean checkUserAuth() {
        return mCatalogModel.isUserAuth();
    }

    //region ========================= DI =========================

    private Component createComponent(){
        Component component = DaggerService.getComponent(Component.class);
        if (component == null){
            component = DaggerCatalogPresenter_Component.builder()
                    .component(DaggerService.getComponent(RootActivity.Component.class))
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
        CatalogModel provideCatalogModel(){
            return new CatalogModel();
        }
    }

    @dagger.Component(dependencies = RootActivity.Component.class, modules = Module.class)
    @CatalogScope
    interface Component{
        void inject(CatalogPresenter presenter);
    }

    //endregion
}
