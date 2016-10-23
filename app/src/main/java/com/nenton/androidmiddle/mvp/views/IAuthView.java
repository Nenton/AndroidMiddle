package com.nenton.androidmiddle.mvp.views;

import android.support.annotation.Nullable;

import com.nenton.androidmiddle.mvp.presenters.IAuthPresenter;
import com.nenton.androidmiddle.ui.custom_views.AuthPanel;

public interface IAuthView {
    void showMessage(String message);
    void showError(Exception e);

    void showLoad();
    void hideLoad();

    void showLoginBtn();
    void hideLoginBtn();

    IAuthPresenter getPresenter();

//    void testShowLogin();
    @Nullable
    AuthPanel getAuthPanel();
}
