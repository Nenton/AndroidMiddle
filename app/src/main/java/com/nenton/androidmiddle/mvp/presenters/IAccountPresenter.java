package com.nenton.androidmiddle.mvp.presenters;

public interface IAccountPresenter {

    void clickOnAddress();
    void switchViewState();
    void switchOrder(boolean isChecked);
    void switchPromo(boolean isChecked);
    void takePhoto();
    void chooseCamera();
    void chooseGallery();

}
