package com.nenton.androidmiddle.ui.screens.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.AuthScope;
import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.flow.Screen;
import com.nenton.androidmiddle.mvp.models.AuthModel;
import com.nenton.androidmiddle.mvp.presenters.AbstractPresenter;
import com.nenton.androidmiddle.mvp.presenters.IAuthPresenter;
import com.nenton.androidmiddle.mvp.presenters.RootPresenter;
import com.nenton.androidmiddle.mvp.views.IRootView;
import com.nenton.androidmiddle.ui.activities.RootActivity;
import com.nenton.androidmiddle.ui.screens.catalog.CatalogScreen;

import javax.inject.Inject;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import mortar.ViewPresenter;

@Screen(R.layout.screen_auth)
public class AuthScreen extends AbstractScreen<RootActivity.RootComponent> {


    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentRootComponent) {
        return DaggerAuthScreen_Component.builder()
                .rootComponent(parentRootComponent)
                .module(new Module())
                .build();
    }

    //region ========================= DI =========================

    @dagger.Module
    public class Module {
        @Provides
        @AuthScope
        AuthPresenter provideAuthPresenter(){
            return new AuthPresenter();
        }

        @Provides
        @AuthScope
        AuthModel provideAuthModel(){
            return new AuthModel();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @AuthScope
    public interface Component{
        void inject(AuthPresenter presenter);

        void inject(AuthView view);
    }

    //endregion

    //region ========================= Presenter =========================

    public class AuthPresenter extends AbstractPresenter<AuthView, AuthModel> implements IAuthPresenter {

        @Inject
        AuthModel mAuthModel;
        @Inject
        RootPresenter mRootPresenter;

        private int mCustomState;

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            if (getView() != null && getRootView() != null){
                if (checkUserAuth()){
                    Flow.get(getView()).set(new CatalogScreen());
//                    ((SplashActivity) getRootView()).startRootActivity();
//                    getView().hideLoginBtn();
                } else {
                    getView().showLoginBtn();
                }
            }
        }

        @Override
        protected void initActionBar() {
            mRootPresenter.newActionBarBuilder()
                    .setVisable(false)
                    .setTitle("Авторизация")
                    .build();
        }

        @Override
        protected void initFab() {

        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component)scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        public void clickOnVk() {
            if (getRootView() != null) {
                getRootView().showMessage("VK");
            }
        }

        @Override
        public void clickOnFb() {
            if (getRootView() != null) {
                getRootView().showMessage("FB");
            }
        }

        @Override
        public void clickOnTwitter() {
            if (getRootView() != null) {
                getRootView().showMessage("TWITTER");
            }
        }

        @Override
        public void clickOnLogin() {
            if (getView() != null && getRootView() != null){
                if (getView().isIdle()){
                    getView().setCustomState(AuthView.LOGIN_STATE);
                } else {
                    if (mAuthModel.loginUser(getView().getUserEmail(), getView().getUserPassword())){
                        Flow.get(getView()).set(new CatalogScreen());
//                        ((SplashActivity) getRootView()).startRootActivity();
//                        getRootView().showMessage("Запрос авторизации пользователя");
                    } else {
                        getRootView().showMessage("Введите корректные данные");
                    }
                }
            }
        }

        @Override
        public void clickOnShowCatalog() {
            if (getView() != null) {
                getRootView().showMessage("Показать каталог");
                Flow.get(getView()).set(new CatalogScreen());
//                if (getRootView() instanceof SplashActivity){
////                    ((SplashActivity) getRootView()).startRootActivity();
//                } else {
//                    // TODO: 01.12.2016 show catalog screen
//                }
            }
        }

        @Override
        public boolean checkUserAuth() {
            return mAuthModel.isUserAuth();
        }
        public int getCustomState() {
            return mCustomState;
        }

        public void setCustomState(int customState) {
            mCustomState = customState;
        }
    }

    //endregion
}
