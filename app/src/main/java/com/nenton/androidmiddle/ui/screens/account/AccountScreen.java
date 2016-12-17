package com.nenton.androidmiddle.ui.screens.account;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.AccountScope;
import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.flow.Screen;
import com.nenton.androidmiddle.mvp.models.AccountModel;
import com.nenton.androidmiddle.mvp.presenters.IAccountPresenter;
import com.nenton.androidmiddle.mvp.presenters.RootPresenter;
import com.nenton.androidmiddle.mvp.views.IRootView;
import com.nenton.androidmiddle.ui.activities.RootActivity;
import com.nenton.androidmiddle.ui.screens.address.AddressScreen;

import javax.inject.Inject;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import mortar.ViewPresenter;

@Screen(R.layout.screen_account)
public class AccountScreen extends AbstractScreen<RootActivity.RootComponent> {

    private int mCustomState = 1;

    public int getmCustomState() {
        return mCustomState;
    }

    public void setmCustomState(int mCustomState) {
        this.mCustomState = mCustomState;
    }

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerAccountScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    //region ========================= DI =========================

    @dagger.Module
    public class Module {
        @Provides
        @AccountScope
        AccountModel provideAccountModel(){
            return new AccountModel();
        }

        @Provides
        @AccountScope
        AccountPresenter provideAccountPresenter(){
            return new AccountPresenter();
        }

    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @AccountScope
    public interface Component {
        void inject(AccountPresenter accountPresenter);
        void inject(AccountView accountView);

        RootPresenter getRootPresenter();
        AccountModel getAccountModel();
    }

    //endregion

    //region ========================= Presenter =========================


    public class AccountPresenter extends ViewPresenter<AccountView> implements IAccountPresenter {

        @Inject
        RootPresenter mRootPresenter;
        @Inject
        AccountModel mAccountModel;
        private Uri mAvatarUri;

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() != null){
                getView().initView(mAccountModel.getUserDto());
            }
        }

        @Override
        public void clickOnAddress() {
            Flow.get(getView()).set(new AddressScreen());
            // TODO: 13.12.2016 flow open new screen AddressScreen
        }

        @Override
        public void switchViewState() {
            if (getmCustomState() == AccountView.EDIT_STATE && getView() != null){
                mAccountModel.saveProfileInfo(getView().getUserName(), getView().getUserPhone());
                mAccountModel.saveAvatarPhoto(mAvatarUri);
            }

            if (getView() != null){
                getView().changeState();
            }
        }

        @Override
        public void switchOrder(boolean isChecked) {
            mAccountModel.saveOrderNotification(isChecked);
        }

        @Override
        public void switchPromo(boolean isChecked) {
            mAccountModel.savePromoNotification(isChecked);
        }

        @Override
        public void takePhoto() {
            if (getView() != null){
                getView().showPhotoSourceDialog();
            }
        }

        @Override
        public void chooseCamera() {
            if (getRootView() != null){
                getRootView().showMessage("chooseCamera");
            }
            // TODO: 13.12.2016 choose camera
        }

        @Override
        public void chooseGallery() {
            if (getRootView() != null){
                getRootView().showMessage("chooseGallery");
            }
            // TODO: 13.12.2016 choose gallery
        }

        @Nullable
        private IRootView getRootView(){
            return mRootPresenter.getView();
        }

    }

    //endregion

}
