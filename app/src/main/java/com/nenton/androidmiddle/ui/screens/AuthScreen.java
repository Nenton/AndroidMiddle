package com.nenton.androidmiddle.ui.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.di.sqopes.AuthScope;
import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.flow.Screen;
import com.nenton.androidmiddle.mvp.models.AuthModel;
import com.nenton.androidmiddle.mvp.presenters.IAuthPresenter;
import com.nenton.androidmiddle.mvp.presenters.RootPresenter;
import com.nenton.androidmiddle.mvp.views.IAuthView;
import com.nenton.androidmiddle.mvp.views.IRootView;
import com.nenton.androidmiddle.ui.activities.RootActivity;
import com.nenton.androidmiddle.ui.custom_views.AuthPanel;

import javax.inject.Inject;

import dagger.Provides;
import mortar.ViewPresenter;

@Screen(R.layout.screen_auth)
public class AuthScreen extends AbstractScreen<RootActivity.Component> {


    private int mCustomState;

    @Override
    public Object createScreenComponent(RootActivity.Component parentComponent) {
        return null;
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

    @dagger.Component(dependencies = RootActivity.Component.class, modules = Module.class)
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
                    getView().setCustomState(mCustomState);
                } else {
                    // TODO: 21.10.2016 авторизация
                    if (getView().isTextWatcherError()){
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
                // TODO: 27.11.2016 start RootActivity
            }
        }

        @Override
        public boolean checkUserAuth() {
            return mAuthModel.isUserAuth();
        }
    }

    //endregion
}
