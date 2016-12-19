package com.nenton.androidmiddle.di.modules;

import com.nenton.androidmiddle.di.sqopes.RootScope;
import com.nenton.androidmiddle.mvp.models.AccountModel;
import com.nenton.androidmiddle.mvp.presenters.RootPresenter;

import dagger.Provides;

@dagger.Module
public class RootModule {
    @Provides
    @RootScope
    RootPresenter provideRootPresenter() {
        return new RootPresenter();
    }

    @Provides
    @RootScope
    AccountModel provideAccountModel(){
        return new AccountModel();
    }
}
