package com.nenton.androidmiddle.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by serge on 01.02.2017.
 */

public class ProductAddRes {

    @SerializedName("remoteId")
    @Expose
    public int remoteId;
    @SerializedName("productName")
    @Expose
    public String productName;
    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("price")
    @Expose
    public int price;
    @SerializedName("raiting")
    @Expose
    public int raiting;
    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("active")
    @Expose
    public boolean active;
    @SerializedName("comments")
    @Expose
    public List<Object> comments = null;
}
