package com.nenton.androidmiddle.ui.screens.account;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.nenton.androidmiddle.data.storage.UserInfoDto;
import com.nenton.androidmiddle.data.storage.UserSettingsDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.mvp.views.IAccountView;
import com.nenton.androidmiddle.ui.screens.address.AddressesAdapter;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import flow.Flow;

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
    private AddressesAdapter mAddressesAdapter;
    private Uri mAvatarUri;

    public AddressesAdapter getAddressesAdapter() {
        return mAddressesAdapter;
    }

    public AccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            mAccountScreen = Flow.getKey(this);
            DaggerService.<AccountScreen.Component>getDaggerComponent(context).inject(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

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

    public void initView() {
        showViewFromState();
        mAddressesAdapter = new AddressesAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mAddressList.setLayoutManager(linearLayoutManager);
        mAddressList.setAdapter(mAddressesAdapter);
        initSwipe();
    }

    private void initSwipe() {
        ItemSwipeCallback itemSwipeCallback = new ItemSwipeCallback(getContext(), 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    showRemoveAddressDialog(position);
                } else {
                    showEditAddressDialog(position);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mAddressList);
    }

    private void showEditAddressDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Перейти к редактированию адресса")
                .setMessage("Вы действительно хотите редактировать адресс?")
                .setPositiveButton("Редактировать", (dialog, which) -> {mPresenter.editAddress(position);})
                .setNegativeButton("Отмена", (dialog, which) -> {dialog.cancel();})
                .setOnCancelListener(dialog -> mAddressesAdapter.notifyDataSetChanged())
                .show();
    }

    private void showRemoveAddressDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Удаление адресса")
                .setMessage("Вы действительно хотите удалить адресс?")
                .setPositiveButton("Удалить", (dialog, which) -> {mPresenter.removeAddress(position);})
                .setNegativeButton("Отмена", (dialog, which) -> {dialog.cancel();})
                .setOnCancelListener(dialog -> mAddressesAdapter.notifyDataSetChanged())
                .show();
    }

    public void initSettings(UserSettingsDto settings) {
        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> mPresenter.switchSettings();
        mNotificationOrderSW.setChecked(settings.isOrderNotification());
        mNotificationPromoSW.setChecked(settings.isPromoNotification());
        mNotificationOrderSW.setOnCheckedChangeListener(listener);
        mNotificationPromoSW.setOnCheckedChangeListener(listener);

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
        if (mAvatarUri != null){
            insertAvatar();
        }
    }

    @Override
    public void showPhotoSourceDialog() {
        String source[] = {"Загрузить из галпереи", "Сделать фото", "Отмена"};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Установить фото");
        alertDialog.setItems(source, (dialog, which) -> {
            switch (which) {
                case 0:
                    mPresenter.chooseGallery();
                    break;
                case 1:
                    mPresenter.chooseCamera();
                    break;
                case 2:
                    dialog.cancel();
                    break;
            }
        });
        alertDialog.show();
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
            mPresenter.switchViewState();
//            changeState();
            return true;
        } else {
            return false;
        }
    }


    public UserSettingsDto getSettings() {
        return new UserSettingsDto(mNotificationOrderSW.isChecked(), mNotificationPromoSW.isChecked());
    }

    public void updateAvatarPhoto(Uri uri) {
        mAvatarUri = uri;
        insertAvatar();
    }

    private void insertAvatar() {
        mPicasso.load(mAvatarUri)
                .resize(140, 140)
                .centerCrop()
                .into(mUserAvatar);
    }

    public UserInfoDto getUserProfileInfo() {
        return new UserInfoDto(mUserFullNameET.getText().toString(), mUserPhoneET.getText().toString(), String.valueOf(mAvatarUri));
    }

    public void updateProfileInfo(UserInfoDto infoDto) {
        mProfileNameTxt.setText(infoDto.getName());
        mUserFullNameET.setText(infoDto.getName());
        mUserPhoneET.setText(infoDto.getPhone());
        if (mAccountScreen.getmCustomState() == PREVIEW_STATE){
            mAvatarUri = Uri.parse(infoDto.getAvatar());
            insertAvatar();
        }
    }

    //endregion

    //region ========================= Event =========================

    @OnClick(R.id.account_user_profile_photo)
    void testEditMode() {
        mPresenter.switchViewState();
    }

    @OnClick(R.id.add_address_btn)
    void clickAddAddress() {
        mPresenter.clickOnAddress();
    }

    @OnClick(R.id.user_avatar_img)
    void clickChangeAvatar(){
        if (mAccountScreen.getmCustomState() == EDIT_STATE){
            mPresenter.takePhoto();
        }
    }

    //endregion


}
