package com.nenton.androidmiddle.mvp.views;

import android.support.annotation.Nullable;

public interface IRootView extends IView{
    void showMessage(String message);
    void showError(Exception e);

    void showLoad();
    void hideLoad();

    @Nullable
    IView getCurrentScreen();
}
