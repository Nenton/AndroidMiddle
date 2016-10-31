package com.nenton.androidmiddle.mvp.views;

import android.support.annotation.Nullable;

import com.nenton.androidmiddle.mvp.presenters.IAuthPresenter;
import com.nenton.androidmiddle.ui.custom_views.AuthPanel;

public interface IAuthView extends IView{

    void showLoginBtn();
    void hideLoginBtn();

    IAuthPresenter getPresenter();

    @Nullable
    AuthPanel getAuthPanel();

    void showCatalogScreen();
}
