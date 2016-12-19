package com.nenton.androidmiddle.mvp.presenters;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.nenton.androidmiddle.data.storage.ActivityResultDto;
import com.nenton.androidmiddle.data.storage.UserInfoDto;
import com.nenton.androidmiddle.mvp.models.AccountModel;
import com.nenton.androidmiddle.mvp.views.IRootView;
import com.nenton.androidmiddle.ui.activities.RootActivity;
import com.nenton.androidmiddle.utils.AndroidMiddleAplication;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class RootPresenter extends AbstractPresenter<IRootView>{

    private PublishSubject<ActivityResultDto> mActivityResultDtoObs = PublishSubject.create();
    @Inject
    AccountModel mAccountModel;

    public RootPresenter() {
        AndroidMiddleAplication.getRootActivityRootComponent().inject(this);
    }

    public PublishSubject<ActivityResultDto> getActivityResultDtoObs(){
        return mActivityResultDtoObs;
    }
    @Override
    public void initView() {
        mAccountModel.getUserInfoObs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new UserInfoSubscriber());
    }

    @RxLogSubscriber
    private class UserInfoSubscriber extends Subscriber<UserInfoDto>{

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (getView() != null){
                getView().showError(e);
            }
        }

        @Override
        public void onNext(UserInfoDto infoDto) {
            if (getView() != null){
                getView().initDrawer(infoDto);
            }
        }
    }

    public boolean checkPermissionsAndRequestIfNotGranted(@NonNull String[] permissions, int requestCode){
        boolean allGranted = true;
        for (String permission : permissions){
            int selfPermission = ContextCompat.checkSelfPermission(((RootActivity) getView()), permission);
            if (selfPermission != PackageManager.PERMISSION_GRANTED){
                allGranted = false;
                break;
            }
        }

        if (!allGranted){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((RootActivity) getView()).requestPermissions(permissions, requestCode);
            }
            return false;
        }
        return allGranted;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        mActivityResultDtoObs.onNext(new ActivityResultDto(requestCode, resultCode, intent));
    }

    public void onRequestPermissionResult(int requetCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        // TODO: 18.12.2016 implement me
    }


}
