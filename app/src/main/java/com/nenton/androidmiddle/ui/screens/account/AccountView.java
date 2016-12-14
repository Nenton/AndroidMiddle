package com.nenton.androidmiddle.ui.screens.account;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.UserDto;
import com.nenton.androidmiddle.mvp.views.IAccountView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountView extends CoordinatorLayout implements IAccountView {

    public static final int PREVIEW_STATE = 1;
    public static final int EDIT_STATE = 0;

    @Inject
    AccountScreen.AccountPresenter mPresenter;

    @Inject
    Picasso mPicasso;

    @BindView(R.id.profile_name_txt)
    TextView mProfileNameTxt;

    @BindView(R.id.user_avatar_img)
    CircleImageView mUserAvatar;

    @BindView(R.id.user_phone_et)
    EditText mUserPhoneET;

    @BindView(R.id.user_full_name_et)
    EditText mUserFullNameET;

    @BindView(R.id.profile_name_wrapper)
    LinearLayout mProfileNameWrapper;

    @BindView(R.id.address_list)
    RecyclerView mAddressList;

    @BindView(R.id.add_address_btn)
    Button mAddAdressBtn;
    @BindView(R.id.notification_order_sw)
    SwitchCompat mNotificationOrderSW;
    @BindView(R.id.notification_promo_sw)
    SwitchCompat mNotificationPromoSW;

    private AccountScreen mAccountScreen;

    private UserDto mUserDto;
    private TextWatcher mWatcher;

    public AccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        if (!isInEditMode()) {

        }
    }

    private void showViewFromState() {
        if (mAccountScreen.getmCustomState() == PREVIEW_STATE) {
            showPreviewState();
        } else {
            showEditState();
        }

    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            mPresenter.takeView(this);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!isInEditMode()) {
            mPresenter.dropView(this);
        }
    }

    public void initView(UserDto user) {
        mUserDto = user;
        initProfileInfo();
        initList();
        initSettings();
        showViewFromState();
    }

    private void initSettings() {
        mNotificationOrderSW.setChecked(mUserDto.isOrderNotification());
        mNotificationOrderSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.switchOrder(isChecked);
            }
        });
        mNotificationPromoSW.setChecked(mUserDto.isPromoNotification());
        mNotificationPromoSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.switchPromo(isChecked);
            }
        });

    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mAddressList.setLayoutManager(linearLayoutManager);
        // TODO: 08.12.2016 create adapter
//        mAddressList.setAdapter();

    }

    private void initProfileInfo() {
        mProfileNameTxt.setText(mUserDto.getFullname());
        mUserFullNameET.setText(mUserDto.getFullname());
        mUserPhoneET.setText(mUserDto.getPhone());
        mPicasso.load(mUserDto.getAvatar())
                .into(mUserAvatar);

    }

    //region ========================= IAccountView =========================

    @Override
    public void changeState() {
        if (mAccountScreen.getmCustomState() == PREVIEW_STATE) {
            mAccountScreen.setmCustomState(EDIT_STATE);
        } else {
            mAccountScreen.setmCustomState(PREVIEW_STATE);
        }
        showViewFromState();

    }

    @Override
    public void showEditState() {

        mWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mProfileNameTxt.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mProfileNameWrapper.setVisibility(VISIBLE);
        mUserFullNameET.addTextChangedListener(mWatcher);
        mUserPhoneET.setEnabled(true);
        mPicasso.load(R.drawable.ic_add_black_24dp)
                .error(R.drawable.ic_add_black_24dp)
                .into(mUserAvatar);
    }

    @Override
    public void showPreviewState() {
        mProfileNameWrapper.setVisibility(GONE);
        mUserFullNameET.removeTextChangedListener(mWatcher);
        mUserPhoneET.setEnabled(false);
        mPicasso.load(mUserDto.getAvatar())
                .into(mUserAvatar);
    }

    @Override
    public void showPhotoSourceDialog() {

    }

    @Override
    public String getUserName() {
        return String.valueOf(mUserFullNameET.getText());
    }

    @Override
    public String getUserPhone() {
        return String.valueOf(mUserPhoneET.getText());
    }

    @Override
    public boolean viewOnBackPressed() {
        if (mAccountScreen.getmCustomState() == EDIT_STATE) {
            changeState();
            return true;
        } else {
            return false;
        }
    }

    //endregion


}
