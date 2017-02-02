package com.nenton.androidmiddle.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by serge on 01.02.2017.
 */

public class AvatarUrlRes {

    @SerializedName("avatarUrl")
    @Expose
    private String avatarUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
