package com.nenton.androidmiddle.mvp.views;

import android.support.annotation.Nullable;

import com.nenton.androidmiddle.data.storage.UserInfoDto;

public interface IRootView extends IView{
    void showMessage(String message);
    void showError(Throwable e);

    void showLoad();
    void hideLoad();

    @Nullable
    IView getCurrentScreen();

    void initDrawer(UserInfoDto infoDto);
}
