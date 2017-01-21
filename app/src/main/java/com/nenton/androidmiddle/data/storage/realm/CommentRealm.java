package com.nenton.androidmiddle.data.storage.realm;

import com.nenton.androidmiddle.data.network.res.ProductRes;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by serge on 09.01.2017.
 */

public class CommentRealm extends RealmObject{

    @PrimaryKey
    private String id;
    private String userName;
    private String avatar;
    private float raiting;
    private Date commentDate;
    private String comment;

    //Необходим для Реалма
    public CommentRealm() {
    }

    public CommentRealm(ProductRes.Comment comment) {
        this.id = comment.getId();
        this.userName = comment.getUserName();
        this.avatar = comment.getAvatar();
        this.raiting = comment.getRaiting();
        this.commentDate = comment.getCommentDate();
        this.comment = comment.getComment();
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public float getRaiting() {
        return raiting;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public String getComment() {
        return comment;
    }
}
