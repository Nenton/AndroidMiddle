package com.nenton.androidmiddle.mvp.views;

public interface IAuthView extends IView {

    void showLoginBtn();

    void hideLoginBtn();

    void showCatalogScreen();

    String getUserEmail();
    String getUserPassword();

    boolean isIdle();

    void setCustomState(int state);
}
