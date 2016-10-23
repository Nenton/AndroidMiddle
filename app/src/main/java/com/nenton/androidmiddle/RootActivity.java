package com.nenton.androidmiddle;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import com.nenton.androidmiddle.mvp.presenters.AuthPresenter;
import com.nenton.androidmiddle.mvp.presenters.IAuthPresenter;
import com.nenton.androidmiddle.mvp.views.IAuthView;
import com.nenton.androidmiddle.ui.custom_views.AuthPanel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RootActivity extends AppCompatActivity implements IAuthView {

    IAuthPresenter mIAuthPresenter = AuthPresenter.getInstance();

    @BindView(R.id.show_catalog_btn)
    Button mCatalogBtn;

    @BindView(R.id.login_btn)
    Button mLoginBtn;

    @BindView(R.id.auth_wrap)
    AuthPanel mAuthPanel;

    @BindView(R.id.activity_root)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        ButterKnife.bind(this);
        mIAuthPresenter.takeView(this);
        mIAuthPresenter.initView();
    }

    @Override
    protected void onDestroy() {
        mIAuthPresenter.dropView();
        super.onDestroy();
    }

    @OnClick(R.id.login_btn)
    public void loginBtn() {
        mIAuthPresenter.clickOnLogin();
    }

    @OnClick(R.id.show_catalog_btn)
    public void showCatalogBtn() {
        mIAuthPresenter.clickOnShowCatalog();
    }

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
        // TODO: 21.10.2016 show load
    }

    @Override
    public void hideLoad() {
// TODO: 21.10.2016 hide load
    }

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
    public void onBackPressed() {
        if (mAuthPanel.getCustomState() == AuthPanel.LOGIN_STATE){
            mAuthPanel.setCustomState(AuthPanel.IDLE_STATE);
        } else {
            super.onBackPressed();
        }
    }
}
