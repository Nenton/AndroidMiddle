package com.nenton.androidmiddle.mvp.views;

public interface IRootView extends IView{
    void showMessage(String message);
    void showError(Exception e);

    void showLoad();
    void hideLoad();
}
