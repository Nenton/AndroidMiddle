package com.nenton.androidmiddle.ui.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.nenton.androidmiddle.BuildConfig;
import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.mvp.presenters.AuthPresenter;
import com.nenton.androidmiddle.mvp.presenters.IAuthPresenter;
import com.nenton.androidmiddle.mvp.views.IAuthView;
import com.nenton.androidmiddle.ui.custom_views.AuthPanel;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity implements IAuthView {

    IAuthPresenter mIAuthPresenter = AuthPresenter.getInstance();

    @BindViews({R.id.vk_btn,R.id.fb_btn,R.id.twitter_btn})
    List<ImageButton> mImageButtons;

    @BindView(R.id.show_catalog_btn)
    Button mCatalogBtn;

    @BindView(R.id.login_btn)
    Button mLoginBtn;

    @BindView(R.id.auth_wrap)
    AuthPanel mAuthPanel;

    @BindView(R.id.activity_root)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mIAuthPresenter.takeView(this);
        mIAuthPresenter.initView();
    }

    @Override
    protected void onDestroy() {
        mIAuthPresenter.dropView();
        super.onDestroy();
    }

    @OnClick(R.id.vk_btn)
    public void clickVk(){
        getPresenter().clickOnVk();
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        mImageButtons.get(0).startAnimation(animation);
    }

    @OnClick(R.id.fb_btn)
    public void clickFb(){
        getPresenter().clickOnFb();
    }

    @OnClick(R.id.twitter_btn)
    public void clickTwitter(){
        getPresenter().clickOnTwitter();
    }

    @OnClick(R.id.login_btn)
    public void loginBtn() {
        mIAuthPresenter.clickOnLogin();
    }

    @OnClick(R.id.show_catalog_btn)
    public void showCatalogBtn() {
        mIAuthPresenter.clickOnShowCatalog();
//        showLoad();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hideLoad();
//            }
//        },3000);
    }

    //region ========================= IView =========================

    @Override
    public void showMessage(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(Exception e) {
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
        mProgressBar.setVisibility(View.VISIBLE);
        mLoginBtn.setEnabled(false);
        mCatalogBtn.setEnabled(false);
        for (ImageButton imageButton : mImageButtons) {
            imageButton.setEnabled(false);
        }
        // TODO: 21.10.2016 show load animation
    }

    @Override
    public void hideLoad() {
        mProgressBar.setVisibility(View.GONE);
        mLoginBtn.setEnabled(true);
        mCatalogBtn.setEnabled(true);
        for (ImageButton imageButton : mImageButtons) {
            imageButton.setEnabled(true);
        }
// TODO: 21.10.2016 hide load animation
    }

    //endregion

    @Override
    public void showLoginBtn() {
        mLoginBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginBtn() {
        mLoginBtn.setVisibility(View.GONE);
    }

    @Override
    public IAuthPresenter getPresenter() {
        return mIAuthPresenter;
    }

    @Override
    public AuthPanel getAuthPanel() {
        return mAuthPanel;
    }

    @Override
    public void showCatalogScreen() {
        Intent intent = new Intent(this, RootActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mAuthPanel.getCustomState() == AuthPanel.LOGIN_STATE) {
            mAuthPanel.setCustomState(AuthPanel.IDLE_STATE);
        } else {
            super.onBackPressed();
        }
    }
}
