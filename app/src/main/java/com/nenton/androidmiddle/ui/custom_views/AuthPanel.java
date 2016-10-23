package com.nenton.androidmiddle.ui.custom_views;

import android.animation.Animator;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.nenton.androidmiddle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthPanel extends LinearLayout {

    private static final String TAG = "AuthPanel";

    public static final int LOGIN_STATE = 0;
    public static final int IDLE_STATE = 1;

    private int mCustomState = 1;
    private boolean corectEmail, corectPass;

    @BindView(R.id.auth_card)
    CardView mAuthCard;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.show_catalog_btn)
    Button mShowCatalog;
    @BindView(R.id.login_email_et)
    EditText emailEt;
    @BindView(R.id.login_password_et)
    EditText passwordEt;

    @BindView(R.id.login_email_TIL)
    TextInputLayout mLoginEmailTil;
    @BindView(R.id.login_password_TIL)
    TextInputLayout mLoginPasswordTil;

    private Context mContext;

    public AuthPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    // TODO: 21.10.2016 save email password in state

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setTextWatchers();
        showViewFromState();
    }

    private void setTextWatchers() {
        emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                corectEmail = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (String.valueOf(s).contains("@")) {
                    corectEmail = true;
                    mLoginEmailTil.setErrorEnabled(false);
                } else {
                    mLoginEmailTil.setError("В email отсутствует \"@\"");
                }
            }
        });
        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                corectPass = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (String.valueOf(s).length() >= 8) {
                    corectPass = true;
                    mLoginPasswordTil.setErrorEnabled(false);
                } else {
                    mLoginPasswordTil.setError("Длина пароля должна быть больше 8");
                }
            }
        });
    }

    public boolean isTextWatcherError() {
        return corectEmail && corectPass;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.state = mCustomState;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(state);
        setCustomState(savedState.state);
    }

    public void setCustomState(int customState) {
        mCustomState = customState;
        showViewFromState();
    }

    private void showLoginState() {
//        mAuthCard.setVisibility(VISIBLE);
        animationVisible(mAuthCard);
        animationGone(mShowCatalog);
//        mShowCatalog.setVisibility(GONE);
    }

    private void showIdleState() {
//        mAuthCard.setVisibility(GONE);
//        mShowCatalog.setVisibility(VISIBLE);
        animationGone(mAuthCard);
        animationVisible(mShowCatalog);
    }

    public int getCustomState() {
        return mCustomState;
    }

    private void showViewFromState() {
        if (mCustomState == LOGIN_STATE) {
            showLoginState();
        } else {
            showIdleState();
        }
    }

    public boolean isIdleState() {
        return mCustomState == IDLE_STATE;
    }

    private void animationVisible(View view) {
        Animation animation = AnimationUtils.makeInAnimation(mContext, true);
        view.setVisibility(VISIBLE);
        view.startAnimation(animation);
    }

    private void animationGone(View view) {
        Animation animation = AnimationUtils.makeOutAnimation(mContext, true);
        view.startAnimation(animation);
        view.setVisibility(GONE);
    }

    public String getUsetEmail() {
        return String.valueOf(emailEt.getText());
    }

    public String getUsetPassword() {
        return String.valueOf(passwordEt.getText());
    }

    static class SavedState extends BaseSavedState {

        private int state;

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            state = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(state);
        }
    }
}
