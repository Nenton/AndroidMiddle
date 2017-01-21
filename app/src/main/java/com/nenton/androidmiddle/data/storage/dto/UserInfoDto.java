package com.nenton.androidmiddle.data.storage.dto;

/**
 * Created by serge on 18.12.2016.
 */

public class UserInfoDto {
    private String name;
    private String phone;
    private String avatar;

    public UserInfoDto(String name, String phone, String avatar) {
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatar() {
        return avatar;
    }
}
