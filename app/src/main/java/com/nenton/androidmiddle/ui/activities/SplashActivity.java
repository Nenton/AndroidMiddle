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

import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.AuthScope;
import com.nenton.androidmiddle.mvp.presenters.AuthPresenter;
import com.nenton.androidmiddle.mvp.presenters.IAuthPresenter;
import com.nenton.androidmiddle.mvp.views.IAuthView;
import com.nenton.androidmiddle.ui.custom_views.AuthPanel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Provides;

public class SplashActivity extends AppCompatActivity implements IAuthView {

    @Inject
    AuthPresenter mAuthPresenter;

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

        Component component = DaggerService.getComponent(Component.class);
        if (component == null){
            component = createAuthViewComponent();
            DaggerService.registerComponent(Component.class, component);
        }
        component.inject(this);

        mAuthPresenter.takeView(this);
        mAuthPresenter.initView();
    }

    @Override
    protected void onDestroy() {
        mAuthPresenter.dropView();
        if (isFinishing()){
            DaggerService.unregisterScope(AuthScope.class);
        }
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
        mAuthPresenter.clickOnLogin();
    }

    @OnClick(R.id.show_catalog_btn)
    public void showCatalogBtn() {
        mAuthPresenter.clickOnShowCatalog();
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
        return mAuthPresenter;
    }

    @Override
    public AuthPanel getAuthPanel() {
        return mAuthPanel;
    }

    @Override
    public void showCatalogScreen() {
        Intent intent = new Intent(this, RootActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mAuthPanel.getCustomState() == AuthPanel.LOGIN_STATE) {
            mAuthPanel.setCustomState(AuthPanel.IDLE_STATE);
        } else {
            super.onBackPressed();
        }
    }

    //region ========================= DI =========================

    @dagger.Module
    public class Module{
        @Provides
        @AuthScope
        AuthPresenter provideAuthPresenter(){
            return new AuthPresenter();
        }
    }

    @dagger.Component(modules = SplashActivity.Module.class)
    @AuthScope
    interface Component{
        void inject(SplashActivity activity);
    }

    private Component createAuthViewComponent(){
        return DaggerSplashActivity_Component.builder()
        .module(new Module())
        .build();
    }
    //endregion
}
