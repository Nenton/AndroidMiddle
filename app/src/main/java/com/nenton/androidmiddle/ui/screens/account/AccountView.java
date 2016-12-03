package com.nenton.androidmiddle.ui.screens.account;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

import com.nenton.androidmiddle.data.storage.UserDto;
import com.nenton.androidmiddle.mvp.views.IAccountView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class AccountView extends CoordinatorLayout implements IAccountView{

    @Inject
    AccountScreen.AccountPresenter mPresenter;

    @Inject
    Picasso mPicasso;

    private UserDto mUserDto;

    public AccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        if (!isInEditMode()) {

        }
    }

    private void showViewFromState() {

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

    public void initView(UserDto user){
        mUserDto = user;
        initProfileInfo();
        initList();
        initSettings();
        showViewFromState();
    }

    //region ========================= IAccountView =========================

    @Override
    public void changeState() {

    }

    @Override
    public void showEditState() {

    }

    @Override
    public void showPreviewState() {

    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public String getUserPhone() {
        return null;
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    //endregion


}
