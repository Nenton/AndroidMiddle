package com.nenton.androidmiddle.ui.screens.account;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.dto.ActivityResultDto;
import com.nenton.androidmiddle.data.storage.dto.UserAddressDto;
import com.nenton.androidmiddle.data.storage.dto.UserInfoDto;
import com.nenton.androidmiddle.data.storage.dto.UserSettingsDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.AccountScope;
import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.flow.Screen;
import com.nenton.androidmiddle.mvp.models.AccountModel;
import com.nenton.androidmiddle.mvp.presenters.IAccountPresenter;
import com.nenton.androidmiddle.mvp.presenters.RootPresenter;
import com.nenton.androidmiddle.mvp.presenters.SubscribePresenter;
import com.nenton.androidmiddle.mvp.views.IRootView;
import com.nenton.androidmiddle.ui.activities.RootActivity;
import com.nenton.androidmiddle.ui.screens.address.AddressScreen;
import com.nenton.androidmiddle.utils.ConstantsManager;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import rx.Observable;
import rx.Subscription;

import static android.Manifest.permission.*;
import static android.os.Environment.DIRECTORY_PICTURES;
import static java.text.DateFormat.MEDIUM;

@Screen(R.layout.screen_account)
public class AccountScreen extends AbstractScreen<RootActivity.RootComponent> {

    private int mCustomState = 1;

    private Subscription addressSubscription;
    private Subscription settingsSubscription;
    private File mPhotoFile;
    private Subscription mActivityResultSub;

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
        AccountPresenter provideAccountPresenter() {
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


    public class AccountPresenter extends SubscribePresenter<AccountView> implements IAccountPresenter {

        @Inject
        RootPresenter mRootPresenter;
        @Inject
        AccountModel mAccountModel;
        private Uri mAvatarUri;
        private Subscription mUserInfoObs;

        //region ========================= LifeCycle =========================

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
            subscribeOnAddressObs();
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() != null) {
                getView().initView();
            }
            subscribeOnAddressObs();
            subscribeOnActivityResult();
            subscribeOnUserInfoObs();
        }

        @Override
        protected void onSave(Bundle outState) {
            super.onSave(outState);
            addressSubscription.unsubscribe();
            settingsSubscription.unsubscribe();
            mUserInfoObs.unsubscribe();
        }

        @Override
        protected void onExitScope() {
            mActivityResultSub.unsubscribe();
            super.onExitScope();
        }

        //endregion

        //region ========================= subscriprion =========================

        private void subscribeOnAddressObs() {

            addressSubscription = subscribe(mAccountModel.getAddressObs(), new ViewSubscriber<UserAddressDto>() {
                @Override
                public void onNext(UserAddressDto addressDto) {
                    if (getView() != null) {
                        getView().getAddressesAdapter().addItem(addressDto);
                    }
                }
            });
        }

        private void updateListView() {
            getView().getAddressesAdapter().reloadAdapter();
            subscribeOnAddressObs();
        }

        private void subscribeOnSettingsObs() {
            settingsSubscription = subscribe(mAccountModel.getUserSettingsObs(), new ViewSubscriber<UserSettingsDto>() {
                @Override
                public void onNext(UserSettingsDto userSettingsDto) {
                    if (getView() != null) {
                        getView().initSettings(userSettingsDto);
                    }
                }
            });
        }

        private void subscribeOnActivityResult() {
            Observable<ActivityResultDto> activityResultObs = mRootPresenter.getActivityResultDtoObs()
                    .filter(activityResultDto -> activityResultDto.getResultCode() == Activity.RESULT_OK);
            mActivityResultSub = subscribe(activityResultObs, new ViewSubscriber<ActivityResultDto>() {
                @Override
                public void onNext(ActivityResultDto activityResultDto) {
                    handleActivityResult(activityResultDto);
                }
            });
        }

        private void handleActivityResult(ActivityResultDto activityResultDto) {
            // TODO: 18.12.2016 fixme override in to RX
            switch (activityResultDto.getResultCode()){
                case ConstantsManager.REQUEST_PROFILE_PHOTO_PICTURE:
                    if (activityResultDto.getIntent() != null){
                        String photoUri = activityResultDto.getIntent().getData().toString();
                        getView().updateAvatarPhoto(Uri.parse(photoUri));
                    }
                    break;
                case ConstantsManager.REQUEST_PROFILE_PHOTO_CAMERA:
                    if (mPhotoFile != null){
                        getView().updateAvatarPhoto(Uri.fromFile(mPhotoFile));
                    }
                    break;
            }
        }

        private void subscribeOnUserInfoObs(){
            mUserInfoObs = subscribe(mAccountModel.getUserInfoObs(), new ViewSubscriber<UserInfoDto>() {
                @Override
                public void onNext(UserInfoDto infoDto) {
                    if (getView() != null){
                        getView().updateProfileInfo(infoDto);
                    }
                }
            });
        }

        //endregion

        @Override
        public void clickOnAddress() {
            Flow.get(getView()).set(new AddressScreen(null));
            // TODO: 13.12.2016 flow open new screen AddressScreen
        }

        @Override
        public void switchViewState() {
            if (getmCustomState() == AccountView.EDIT_STATE && getView() != null) {
                mAccountModel.saveProfileInfo(getView().getUserProfileInfo());
//                mAccountModel.saveAvatarPhoto(mAvatarUri);
            }

            if (getView() != null) {
                getView().changeState();
            }
        }

        @Override
        public void takePhoto() {
            if (getView() != null) {
                getView().showPhotoSourceDialog();
            }
        }
        //region ========================= CAMERA =========================

        @Override
        public void chooseCamera() {
            if (getRootView() != null) {
                String[] permissions = new String[]{CAMERA, WRITE_EXTERNAL_STORAGE};
                if (mRootPresenter.checkPermissionsAndRequestIfNotGranted(permissions, ConstantsManager.REQUEST_PERMISSION_CAMERA)) {
                    mPhotoFile = createFileForPhoto();
                    if (mPhotoFile == null) {
                        getRootView().showMessage("Фотография не может быть создана");
                        return;
                    }
                    takePhotoFromCamera();
                }
            }
        }

        private void takePhotoFromCamera() {
            Uri uri = FileProvider.getUriForFile(((RootActivity) getRootView()), ConstantsManager.FILE_PROVIDER_AUTHORITY, mPhotoFile);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            ((RootActivity) getRootView()).startActivityForResult(takePictureIntent, ConstantsManager.REQUEST_PROFILE_PHOTO_CAMERA);
        }

        private File createFileForPhoto() {
            DateFormat dateFormat = SimpleDateFormat.getTimeInstance(MEDIUM);
            String timeStamp = dateFormat.format(new Date());
            String imageFileName = "IMG_" + timeStamp;
            File storageDir = getView().getContext().getExternalFilesDir(DIRECTORY_PICTURES);
            File fileImage;
            try {
                fileImage = File.createTempFile(imageFileName, ".jpg", storageDir);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return fileImage;
        }

        //endregion

        //region ========================= GALLERY =========================

        @Override
        public void chooseGallery() {
            if (getRootView() != null) {
                String[] permissions = new String[]{READ_EXTERNAL_STORAGE};
                if (mRootPresenter.checkPermissionsAndRequestIfNotGranted(permissions,
                        ConstantsManager.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)) {
                    takePhotoFromGallery();
                }
            }
        }

        private void takePhotoFromGallery() {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT < 19) {
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
            } else {
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
            }
            ((RootActivity) getRootView()).startActivityForResult(intent, ConstantsManager.REQUEST_PROFILE_PHOTO_PICTURE);
        }

        //endregion

        @Override
        public void removeAddress(int position) {
            mAccountModel.removeAddress(mAccountModel.getAddressFromPosition(position));
            updateListView();
        }

        @Override
        public void editAddress(int position) {
            Flow.get(getView()).set(new AddressScreen(mAccountModel.getAddressFromPosition(position)));
        }

        @Nullable
        @Override
        protected IRootView getRootView() {
            return mRootPresenter.getRootView();
        }

        public void switchSettings() {
            mAccountModel.saveSettings(getView().getSettings());
        }
    }


    //endregion

}
