package com.nenton.androidmiddle.ui.screens.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.AuthScope;
import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.flow.Screen;
import com.nenton.androidmiddle.mvp.models.AuthModel;
import com.nenton.androidmiddle.mvp.presenters.IAuthPresenter;
import com.nenton.androidmiddle.mvp.presenters.RootPresenter;
import com.nenton.androidmiddle.mvp.views.IRootView;
import com.nenton.androidmiddle.ui.activities.RootActivity;
import com.nenton.androidmiddle.ui.activities.SplashActivity;

import javax.inject.Inject;

import dagger.Provides;
import mortar.MortarScope;
import mortar.ViewPresenter;

@Screen(R.layout.screen_auth)
public class AuthScreen extends AbstractScreen<RootActivity.RootComponent> {


    private int mCustomState;

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentRootComponent) {
        return DaggerAuthScreen_Component.builder()
                .rootComponent(parentRootComponent)
                .module(new Module())
                .build();
    }

    public int getCustomState() {
        return mCustomState;
    }

    public void setCustomState(int customState) {
        mCustomState = customState;
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

    public class AuthPresenter extends ViewPresenter<AuthView> implements IAuthPresenter {

        @Inject
        AuthModel mAuthModel;

        @Inject
        RootPresenter mRootPresenter;

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            ((Component)scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            if (getView()!=null){
                if (checkUserAuth()){
                    getView().hideLoginBtn();
                } else {
                    getView().showLoginBtn();
                }
            }
        }

        @Nullable
        private IRootView getRootView(){
            return mRootPresenter.getView();
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
                    // TODO: 21.10.2016 авторизация
                    if (true){
                        mAuthModel.loginUser(getView().getUserEmail(), getView().getUserPassword());
                        getRootView().showMessage("Запрос авторизации пользователя");
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
                if (getRootView() instanceof SplashActivity){
                    ((SplashActivity) getRootView()).startRootActivity();
                } else {
                    // TODO: 01.12.2016 show catalog screen
                }
            }
        }

        @Override
        public boolean checkUserAuth() {
            return mAuthModel.isUserAuth();
        }
    }

    //endregion
}
