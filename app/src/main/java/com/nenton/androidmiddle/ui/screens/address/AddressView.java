package com.nenton.androidmiddle.ui.screens.address;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.dto.UserAddressDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.mvp.views.AbstractView;
import com.nenton.androidmiddle.mvp.views.IAddressView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressView extends AbstractView<AddressScreen.AddressPresenter> implements IAddressView {

    @BindView(R.id.address_name_et)
    EditText mAddressName;
    @BindView(R.id.address_street_et)
    EditText mAddressStreet;
    @BindView(R.id.address_apartment_et)
    EditText mAddressApartment;
    @BindView(R.id.address_house_et)
    EditText mAddressHouse;
    @BindView(R.id.address_flour_et)
    EditText mAddressFlour;
    @BindView(R.id.address_comment_et)
    EditText mAddressComment;
    @BindView(R.id.add_btn)
    Button mButton;

    @Inject
    AddressScreen.AddressPresenter mPresenter;

    private int mAddressId;

    public AddressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<AddressScreen.Component>getDaggerComponent(context).inject(this);
    }

    public void initView(@Nullable UserAddressDto address){
        if (address != null){
            mAddressId = address.getId();
            mAddressName.setText(address.getName());
            mAddressStreet.setText(address.getStreet());
            mAddressHouse.setText(address.getHouse());
            mAddressApartment.setText(address.getApartment());
            mAddressFlour.setText(String.valueOf(address.getFloor()));
            mAddressComment.setText(address.getComment());
            mButton.setText("Сохранить");
        }
    }

    //region ========================= IAddressView =========================

    @Override
    public void showInputError() {
        // TODO: 14.12.2016 implement this
    }

    @Override
    public UserAddressDto getUserAddress() {
        return new UserAddressDto(
                mAddressId,
                mAddressName.getText().toString(),
                mAddressStreet.getText().toString(),
                mAddressHouse.getText().toString(),
                mAddressApartment.getText().toString(),
                Integer.parseInt(mAddressFlour.getText().toString()),
                mAddressComment.getText().toString());
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }
    
    //endregion

    //region ========================= Events =========================

    @OnClick(R.id.add_btn)
    void addAddress(){
        mPresenter.clickOnAddAddress();
    }

    //endregion

}
