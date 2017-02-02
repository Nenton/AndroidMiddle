package com.nenton.androidmiddle.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nenton.androidmiddle.data.storage.realm.CommentRealm;

import java.util.Date;

/**
 * Created by serge on 02.02.2017.
 */
public class Comment {
    public Comment(CommentRealm comment) {
        this.userName = comment.getUserName();
        this.avatar = comment.getAvatar();
        this.raiting = comment.getRaiting();
        this.comment = comment.getComment();
        this.commentDate = comment.getCommentDate();
    }

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("remoteId")
    @Expose
    private int remoteId;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("raiting")
    @Expose
    private float raiting;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("active")
    @Expose
    private boolean active;
    @SerializedName("commentDate")
    @Expose
    private Date commentDate;

    public String getId() {
        return id;
    }

    public int getRemoteId() {
        return remoteId;
    }

    public String getAvatar() {
        return avatar;
    }

    public float getRaiting() {
        return raiting;
    }

    public String getComment() {
        return comment;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isActive() {
        return active;
    }

    public Date getCommentDate() {
        return commentDate;
    }
}
