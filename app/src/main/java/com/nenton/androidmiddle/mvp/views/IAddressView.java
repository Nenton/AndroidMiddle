package com.nenton.androidmiddle.mvp.views;

import com.nenton.androidmiddle.data.storage.UserAddressDto;

public interface IAddressView extends IView{
    void showInputError();
    UserAddressDto getUserAddress();
}
