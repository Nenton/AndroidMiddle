package com.nenton.androidmiddle.ui.screens.account;

import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.ui.activities.RootActivity;

import mortar.ViewPresenter;

public class AccountScreen extends AbstractScreen<RootActivity.RootComponent>{
    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return null;
    }

    public class AccountPresenter extends ViewPresenter<AccountView>{
    }
}
