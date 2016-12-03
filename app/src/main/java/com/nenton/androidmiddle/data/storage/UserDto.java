package com.nenton.androidmiddle.data.storage;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserDto implements Parcelable {
    private int id;
    private String fullname;
    private String avatar;
    private String phone;
    private boolean orderNotification;
    private boolean promoNotification;
    private ArrayList<UserAddressDto> userAddresses;

    protected UserDto(Parcel in) {
        id = in.readInt();
        fullname = in.readString();
        avatar = in.readString();
        phone = in.readString();
        orderNotification = in.readByte() != 0;
        promoNotification = in.readByte() != 0;
        userAddresses = in.createTypedArrayList(UserAddressDto.CREATOR);
    }

    public static final Creator<UserDto> CREATOR = new Creator<UserDto>() {
        @Override
        public UserDto createFromParcel(Parcel in) {
            return new UserDto(in);
        }

        @Override
        public UserDto[] newArray(int size) {
            return new UserDto[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isOrderNotification() {
        return orderNotification;
    }

    public void setOrderNotification(boolean orderNotification) {
        this.orderNotification = orderNotification;
    }

    public boolean isPromoNotification() {
        return promoNotification;
    }

    public void setPromoNotification(boolean promoNotification) {
        this.promoNotification = promoNotification;
    }

    public ArrayList<UserAddressDto> getUserAddresses() {
        return userAddresses;
    }

    public void setUserAddresses(ArrayList<UserAddressDto> userAddresses) {
        this.userAddresses = userAddresses;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fullname);
        dest.writeString(avatar);
        dest.writeString(phone);
        dest.writeByte((byte) (orderNotification ? 1 : 0));
        dest.writeByte((byte) (promoNotification ? 1 : 0));
        dest.writeTypedList(userAddresses);
    }
}
