package com.nenton.androidmiddle.ui.screens.account;

import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.mvp.presenters.IAccountPresenter;
import com.nenton.androidmiddle.ui.activities.RootActivity;

import mortar.ViewPresenter;

public class AccountScreen extends AbstractScreen<RootActivity.RootComponent>{

    private int mCustomState = 1;

    public int getmCustomState() {
        return mCustomState;
    }

    public void setmCustomState(int mCustomState) {
        this.mCustomState = mCustomState;
    }

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return null;
    }



    //region ===================Presenter=======================

    public class AccountPresenter extends ViewPresenter<AccountView> implements IAccountPresenter{
        @Override
        public void clickOnAddress() {

        }

        @Override
        public void switchViewState() {

        }

        @Override
        public void switchOrder(boolean isChecked) {

        }

        @Override
        public void switchPromo(boolean isChecked) {

        }

        @Override
        public void takePhoto() {

        }

        @Override
        public void chooseCamera() {

        }

        @Override
        public void chooseGallery() {

        }
    //endregion


    }
}
