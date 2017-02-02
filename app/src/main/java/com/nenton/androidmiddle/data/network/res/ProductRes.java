package com.nenton.androidmiddle.data.network.res;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductRes {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("remoteId")
    @Expose
    private int remoteId;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("raiting")
    @Expose
    private float raiting;
    @SerializedName("active")
    @Expose
    private boolean active;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;

    public int getRemoteId() {
        return remoteId;
    }

    public String getProductName() {
        return productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public float getRaiting() {
        return raiting;
    }

    public boolean isActive() {
        return active;
    }

    public String getId() {
        return id;
    }

    public List<Comment> getComments() {
        return comments;
    }

}