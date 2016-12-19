package com.nenton.androidmiddle.ui.screens.address;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.UserAddressDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.AddressScope;
import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.flow.Screen;
import com.nenton.androidmiddle.mvp.models.AccountModel;
import com.nenton.androidmiddle.mvp.presenters.IAddressPresenter;
import com.nenton.androidmiddle.ui.activities.RootActivity;
import com.nenton.androidmiddle.ui.screens.account.AccountScreen;

import javax.inject.Inject;

import dagger.Provides;
import flow.Flow;
import flow.TreeKey;
import mortar.MortarScope;
import mortar.ViewPresenter;

@Screen(R.layout.screen_address)
public class AddressScreen extends AbstractScreen<AccountScreen.Component> implements TreeKey{

    @Nullable
    private UserAddressDto mAddress;

    public AddressScreen(@Nullable UserAddressDto address) {
        mAddress = address;
    }

    @Override
    public boolean equals(Object o) {
        if (mAddress != null ){
            return o instanceof AddressScreen && mAddress.equals(((AddressScreen)o).mAddress);
        } else {
            return super.equals(o);
        }
    }

    @Override
    public int hashCode() {
        return mAddress != null ? mAddress.hashCode() : super.hashCode();
    }

    @Override
    public Object createScreenComponent(AccountScreen.Component parentComponent) {
        return DaggerAddressScreen_Component.builder()
                .component(parentComponent)
                .module(new Module())
                .build();
    }

    @Override
    public Object getParentKey() {
        return new AccountScreen();
    }

    //region ========================= DI =========================

    @dagger.Module
    public class Module{
        @Provides
        @AddressScope
        AddressPresenter provideAddressPresenter(){
            return new AddressPresenter();
        }

    }

    @dagger.Component(dependencies = AccountScreen.Component.class, modules = Module.class)
    @AddressScope
    public interface Component{
        void inject(AddressPresenter addressPresenter);
        void inject(AddressView addressView);

    }

    //endregion

    //region ========================= Presenter =========================

    public class AddressPresenter extends ViewPresenter<AddressView> implements IAddressPresenter{

        @Inject
        AccountModel mAccountModel;

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (mAddress != null && getView() != null){
                getView().initView(mAddress);
            }
        }

        @Override
        public void clickOnAddAddress() {
            // TODO: 14.12.2016 save addres in model
            if (getView() != null){
                mAccountModel.updateOrInsertAddress(getView().getUserAddress());
                Flow.get(getView()).goBack();
            }
        }
    }

    //endregion
}
