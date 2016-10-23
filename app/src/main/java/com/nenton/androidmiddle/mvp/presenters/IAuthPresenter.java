package com.nenton.androidmiddle.mvp.presenters;

import android.support.annotation.Nullable;

import com.nenton.androidmiddle.mvp.views.IAuthView;

public interface IAuthPresenter {

    void takeView(IAuthView mAuthView);
    void dropView();
    void initView();

    @Nullable
    IAuthView getView();

    void clickOnVk();
    void clickOnFb();
    void clickOnTwitter();
    void clickOnLogin();
    void clickOnShowCatalog();

    boolean checkUserAuth();
}
