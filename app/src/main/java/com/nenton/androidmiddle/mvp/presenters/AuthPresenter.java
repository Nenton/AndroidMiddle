package com.nenton.androidmiddle.mvp.presenters;

import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.AuthScope;
import com.nenton.androidmiddle.mvp.models.AuthModel;
import com.nenton.androidmiddle.mvp.views.IAuthView;
import com.nenton.androidmiddle.ui.custom_views.AuthPanel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Inject;

import dagger.Provides;

public class AuthPresenter extends AbstractPresenter<IAuthView> implements IAuthPresenter {

    @Inject
    AuthModel mAuthModel;

    public AuthPresenter() {
        Component component = DaggerService.getComponent(Component.class);
        if (component == null){
            component = createAuthPresenterComponent();
            DaggerService.registerComponent(Component.class, component);
        }
        component.inject(this);
    }



    @Override
    public void initView() {
        if (getView() != null) {
            if (checkUserAuth()) {
                getView().hideLoginBtn();
            } else {
                getView().showLoginBtn();
            }
        }

    }

    @Override
    public void clickOnVk() {
        if (getView() != null) {
            getView().showMessage("VK");

        }
    }

    @Override
    public void clickOnFb() {
        if (getView() != null) {
            getView().showMessage("FB");
        }
    }

    @Override
    public void clickOnTwitter() {
        if (getView() != null) {
            getView().showMessage("TWITTER");
        }
    }

    @Override
    public void clickOnLogin() {
        if (getView() != null && getView().getAuthPanel() != null){
            if (getView().getAuthPanel().isIdleState()){
                getView().getAuthPanel().setCustomState(AuthPanel.LOGIN_STATE);
            } else {
                // TODO: 21.10.2016 авторизация
                if (getView().getAuthPanel().isTextWatcherError()){
                    mAuthModel.loginUser(getView().getAuthPanel().getUsetEmail(), getView().getAuthPanel().getUsetPassword());
                    getView().showMessage("Запрос авторизации пользователя");
                } else {
                    getView().showMessage("Введите корректные данные");
                }
            }
        }
    }

    @Override
    public void clickOnShowCatalog() {
        if (getView() != null) {
            // TODO: 27.10.2016 if update data complited go to in screen catalog
            getView().showCatalogScreen();
        }
    }

    @Override
    public boolean checkUserAuth() {
        return mAuthModel.isUserAuth();
    }

    //region ========================= DI =========================

    @dagger.Module
    public class Module{
        @Provides
        @AuthScope
        AuthModel provideAuthModel(){
            return new AuthModel();
        }
    }

    @dagger.Component(modules = AuthPresenter.Module.class)
    @AuthScope
    interface Component{
        void inject(AuthPresenter presenter);
    }

    private Component createAuthPresenterComponent(){
        return DaggerAuthPresenter_Component.builder()
                .module(new Module())
                .build();
    }

    //endregion

}
