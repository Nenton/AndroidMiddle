package com.nenton.androidmiddle.mvp.views;

public interface IAccountView extends IView{
    void changeState();

    void showEditState();

    void showPreviewState();

    String getUserName();
    String getUserPhone();
}
