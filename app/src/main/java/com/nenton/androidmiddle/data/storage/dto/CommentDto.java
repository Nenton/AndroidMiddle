package com.nenton.androidmiddle.data.storage.dto;

import com.nenton.androidmiddle.data.storage.realm.CommentRealm;

import java.text.SimpleDateFormat;

/**
 * Created by serge on 10.01.2017.
 */
public class CommentDto {

    private String userName;
    private String avatarUri;
    private float rating;
    private String commentDate;
    private String comment;

    public CommentDto(CommentRealm commentRealm) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
        this.userName = commentRealm.getUserName();
        this.avatarUri = commentRealm.getAvatar();
        this.rating = commentRealm.getRaiting();
        this.commentDate = df.format(commentRealm.getCommentDate());
        this.comment = commentRealm.getComment();
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public float getRating() {
        return rating;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public String getComment() {
        return comment;
    }
}
