package com.nenton.androidmiddle.ui.screens.address;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.UserAddressDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.mvp.views.IAddressView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressView extends RelativeLayout implements IAddressView {

    @Inject
    AddressScreen.AddressPresenter mPresenter;

    public AddressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            DaggerService.<AddressScreen.Component>getDaggerComponent(context).inject(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            mPresenter.takeView(this);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!isInEditMode()) {
            mPresenter.dropView(this);
        }
    }

    //region ========================= IAddressView =========================

    @Override
    public void showInputError() {
        // TODO: 14.12.2016 implement this
    }

    @Override
    public UserAddressDto getUserAddress() {
        // TODO: 14.12.2016 implement this 
        return null;
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }
    
    //endregion

    //region ========================= Events =========================

    @OnClick(R.id.add_address_btn)
    void addAddress(){
        mPresenter.clickOnAddAddress();
    }

    //endregion

}
