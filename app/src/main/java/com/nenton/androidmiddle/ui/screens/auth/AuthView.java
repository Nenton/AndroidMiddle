package com.nenton.androidmiddle.ui.screens.auth;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.mvp.views.AbstractView;
import com.nenton.androidmiddle.mvp.views.IAuthView;
import com.nenton.androidmiddle.utils.ConstantsManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;

public class AuthView extends AbstractView<AuthScreen.AuthPresenter> implements IAuthView {

    public static final int LOGIN_STATE = 0;
    public static final int IDLE_STATE = 1;

    @Inject
    AuthScreen.AuthPresenter mAuthPresenter;

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

    private boolean validateEmail, validatePassword;

    public AuthView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<AuthScreen.Component>getDaggerComponent(context).inject(this);
    }

    private void showViewFromState() {
        if (mPresenter.getCustomState() == LOGIN_STATE) {
            showLoginState();
        } else {
            showIdleState();
        }
    }

    private void showLoginState() {
        animationVisible(mCardView);
        animationGone(mCatalogBtn);
        mLoginBtn.setEnabled(false);
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
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        addValidateFields();
        if (!isInEditMode()) {
            showViewFromState();
        }
    }

    private void addValidateFields() {

        mEmailEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail = s.toString().matches(ConstantsManager.REG_EXP_EMAIL);
                isValidateField();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword = s.length() >= 8 && s.toString().matches(ConstantsManager.REG_EXP_PASSWORD);
                isValidateField();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void isValidateField() {
        if (validateEmail && validatePassword) {
            mLoginBtn.setEnabled(true);
        } else {
            mLoginBtn.setEnabled(false);
        }
    }

    //region ========================= Events =========================

    @OnClick(R.id.vk_btn)
    public void vkClick() {
        mAuthPresenter.clickOnVk();
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
        return mPresenter.getCustomState() == IDLE_STATE;
    }

    @Override
    public void setCustomState(int state) {
        mPresenter.setCustomState(state);
        showViewFromState();
    }

    @Override
    public boolean viewOnBackPressed() {
        if (!isIdle()) {
            setCustomState(IDLE_STATE);
            mLoginBtn.setEnabled(true);
            return true;
        } else {
            return false;
        }
    }

    //endregion
}
