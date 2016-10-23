package com.nenton.androidmiddle.mvp.presenters;


import android.os.Handler;

import com.nenton.androidmiddle.mvp.models.AuthModel;
import com.nenton.androidmiddle.mvp.views.IAuthView;
import com.nenton.androidmiddle.ui.custom_views.AuthPanel;

public class AuthPresenter implements IAuthPresenter {

    private static AuthPresenter ourInstance = new AuthPresenter();
    private IAuthView mIAuthView;
    private AuthModel mAuthModel;

    private AuthPresenter() {
        mAuthModel = new AuthModel();
    }

    public static AuthPresenter getInstance() {
        return ourInstance;
    }

    @Override
    public void takeView(IAuthView mAuthView) {
        mIAuthView = mAuthView;
    }

    @Override
    public void dropView() {
        mIAuthView = null;
    }

    @Override
    public void initView() {
        if (mIAuthView != null) {
            if (checkUserAuth()) {
                mIAuthView.hideLoginBtn();
            } else {
                mIAuthView.showLoginBtn();
            }
        }

    }

    @Override
    public IAuthView getView() {
        return mIAuthView;
    }

    @Override
    public void clickOnVk() {
        if (mIAuthView != null) {
            mIAuthView.showMessage("VK");

        }
    }

    @Override
    public void clickOnFb() {
        if (mIAuthView != null) {
            mIAuthView.showMessage("FB");
        }
    }

    @Override
    public void clickOnTwitter() {
        if (mIAuthView != null) {
            mIAuthView.showMessage("TWITTER");
        }
    }

    @Override
    public void clickOnLogin() {
        if (getView() != null && getView().getAuthPanel() != null){
            if (mIAuthView.getAuthPanel().isIdleState()){
                mIAuthView.getAuthPanel().setCustomState(AuthPanel.LOGIN_STATE);
            } else {
                // TODO: 21.10.2016 авторизация
                if (getView().getAuthPanel().isTextWatcherError()){
                    mAuthModel.loginUser(mIAuthView.getAuthPanel().getUsetEmail(), mIAuthView.getAuthPanel().getUsetPassword());
                    mIAuthView.showMessage("Запрос авторизации пользователя");
                } else {
                    getView().showMessage("Введите корректные данные");
                }
            }
        }
    }

    @Override
    public void clickOnShowCatalog() {
        if (mIAuthView != null) {
            mIAuthView.showMessage("CATALOG");
            getView().showLoad();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getView().hideLoad();
                }
            },3000);
        }
    }

    @Override
    public boolean checkUserAuth() {
        return mAuthModel.isUserAuth();
    }

}
