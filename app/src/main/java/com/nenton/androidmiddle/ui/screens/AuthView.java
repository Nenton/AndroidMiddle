package com.nenton.androidmiddle.ui.screens;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.mvp.presenters.AuthPresenter;
import com.nenton.androidmiddle.mvp.views.IAuthView;
import com.nenton.androidmiddle.ui.custom_views.AuthPanel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;

public class AuthView extends RelativeLayout implements IAuthView {

    public static final int LOGIN_STATE = 0;
    public static final int IDLE_STATE = 1;

    @Inject
    AuthPresenter mAuthPresenter;

    @BindViews({R.id.vk_btn, R.id.fb_btn, R.id.twitter_btn})
    List<ImageButton> mImageButtons;

    @BindView(R.id.show_catalog_btn)
    Button mCatalogBtn;

    @BindView(R.id.login_btn)
    Button mLoginBtn;

    @BindView(R.id.auth_card)
    CardView mCardView;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.login_email_et)
    EditText mEmailEt;

    @BindView(R.id.login_password_et)
    EditText mPasswordEt;

    private AuthScreen mAuthScreen;

    public AuthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            mAuthScreen = Flow.getKey(this);
        }
        // TODO: 27.11.2016 getMScreen and dagger component
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        if (!isInEditMode()) {
            showViewFromState();
        }
    }

    private void showViewFromState() {
        if (mAuthScreen.getCustomState() == LOGIN_STATE) {
            showLoginState();
        } else {
            showIdleState();
        }
    }

    private void showLoginState() {
        animationVisible(mCardView);
        animationGone(mCatalogBtn);
    }

    private void showIdleState() {
        animationGone(mCardView);
        animationVisible(mCatalogBtn);
    }

    private void animationVisible(View view) {
        Animation animation = AnimationUtils.makeInAnimation(getContext(), true);
        view.setVisibility(VISIBLE);
        view.startAnimation(animation);
    }

    private void animationGone(View view) {
        Animation animation = AnimationUtils.makeOutAnimation(getContext(), true);
        view.startAnimation(animation);
        view.setVisibility(GONE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) {
            mAuthPresenter.takeView(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isInEditMode()) {
            mAuthPresenter.dropView();
        }
    }

    //region ========================= Events =========================

    @OnClick(R.id.vk_btn)
    public void vkClick() {
        mAuthPresenter.clickOnVk();
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        mImageButtons.get(0).startAnimation(animation);
    }

    @OnClick(R.id.fb_btn)
    public void fbClick() {
        mAuthPresenter.clickOnFb();
    }

    @OnClick(R.id.twitter_btn)
    public void twitterClick() {
        mAuthPresenter.clickOnTwitter();
    }

    @OnClick(R.id.login_btn)
    public void loginClick() {
        mAuthPresenter.clickOnLogin();
    }

    @OnClick(R.id.show_catalog_btn)
    public void showCatalogClick() {
        mAuthPresenter.clickOnShowCatalog();
    }

    //endregion

    //region ========================= IAuthView =========================

    @Override
    public void showLoginBtn() {
        mLoginBtn.setVisibility(VISIBLE);
    }

    @Override
    public void hideLoginBtn() {
        mLoginBtn.setVisibility(GONE);
    }

    @Override
    public void showCatalogScreen() {
        mAuthPresenter.clickOnShowCatalog();
    }

    @Override
    public String getUserEmail() {
        return String.valueOf(mEmailEt.getText());
    }

    @Override
    public String getUserPassword() {
        return String.valueOf(mPasswordEt.getText());
    }

    @Override
    public boolean isIdle() {
        return mAuthScreen.getCustomState() == IDLE_STATE;
    }

    @Override
    public void setCustomState(int state) {
        mAuthScreen.setCustomState(state);
        showViewFromState();
    }


    @Override
    public boolean viewOnBackPressed() {
        if (!isIdle()) {
            setCustomState(IDLE_STATE);
            return true;
        } else {
            return false;
        }
    }

    //endregion
}
