package com.nenton.androidmiddle.mvp.presenters;


import android.os.Handler;

import com.nenton.androidmiddle.mvp.models.AuthModel;
import com.nenton.androidmiddle.mvp.views.IAuthView;
import com.nenton.androidmiddle.ui.custom_views.AuthPanel;

public class AuthPresenter extends AbstractPresenter<IAuthView> implements IAuthPresenter {

    private static AuthPresenter ourInstance = new AuthPresenter();
    private AuthModel mAuthModel;

    private AuthPresenter() {
        mAuthModel = new AuthModel();
    }

    public static AuthPresenter getInstance() {
        return ourInstance;
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
//            mIAuthView.showMessage("CATALOG");
//            getView().showLoad();
            // TODO: 27.10.2016 if update data complited go to in screen catalog
            getView().showCatalogScreen();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    getView().hideLoad();
//                }
//            },3000);
        }
    }

    @Override
    public boolean checkUserAuth() {
        return mAuthModel.isUserAuth();
    }

}
