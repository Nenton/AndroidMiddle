package com.nenton.androidmiddle.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.nenton.androidmiddle.BuildConfig;
import com.nenton.androidmiddle.R;

import com.nenton.androidmiddle.data.storage.dto.UserInfoDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.flow.TreeKeyDispatcher;
import com.nenton.androidmiddle.mortar.ScreenScoper;
import com.nenton.androidmiddle.mvp.presenters.RootPresenter;
import com.nenton.androidmiddle.mvp.views.IRootView;
import com.nenton.androidmiddle.mvp.views.IView;
import com.nenton.androidmiddle.ui.screens.auth.AuthScreen;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import flow.Flow;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

public class SplashActivity extends AppCompatActivity implements IRootView {

    @BindView(R.id.activity_root)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.root_frame)
    FrameLayout mRootFrame;

    @Inject
    RootPresenter mRootPresenter;

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = Flow.configure(newBase,this)
                .defaultKey(new AuthScreen())
                .dispatcher(new TreeKeyDispatcher(this))
                .install();
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        DaggerService.<RootActivity.RootComponent>getDaggerComponent(this).inject(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    public Object getSystemService(String name) {
        MortarScope rootActivityScope = MortarScope.findChild(getApplicationContext(), RootActivity.class.getName());
        return rootActivityScope.hasService(name) ? rootActivityScope.getService(name) : super.getSystemService(name);
    }

    @Override
    protected void onStart() {
        mRootPresenter.takeView(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        mRootPresenter.dropView(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()){
            ScreenScoper.destroyScreenScope(AuthScreen.class.getName());
        }
        super.onDestroy();
    }
    //region ========================= IView =========================

    @Override
    public void showMessage(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(Throwable e) {
        if (BuildConfig.DEBUG) {
            showMessage(e.getMessage());
            e.printStackTrace();
        } else {
            showMessage("Что-то пошло не так. Попробуйте повторить позже");
            // TODO: 21.10.2016 send error stacktrace
        }

    }

    @Override
    public void showLoad() {
//        mProgressBar.setVisibility(View.VISIBLE);
//        mLoginBtn.setEnabled(false);
//        mCatalogBtn.setEnabled(false);
//        for (ImageButton imageButton : mImageButtons) {
//            imageButton.setEnabled(false);
//        }
        // TODO: 21.10.2016 show load animation
    }

    @Override
    public void hideLoad() {
//        mProgressBar.setVisibility(View.GONE);
//        mLoginBtn.setEnabled(true);
//        mCatalogBtn.setEnabled(true);
//        for (ImageButton imageButton : mImageButtons) {
//            imageButton.setEnabled(true);
//        }
// TODO: 21.10.2016 hide load animation
    }

    @Nullable
    @Override
    public IView getCurrentScreen() {
        return (IView) mRootFrame.getChildAt(0);
    }

    @Override
    public void initDrawer(UserInfoDto infoDto) {

    }

    //endregion

    @Override
    public void onBackPressed() {
        if (getCurrentScreen() != null && !getCurrentScreen().viewOnBackPressed() && !Flow.get(this).goBack()){
            super.onBackPressed();
        }
    }

    public void startRootActivity(){
        Intent intent = new Intent(this,RootActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }
}
