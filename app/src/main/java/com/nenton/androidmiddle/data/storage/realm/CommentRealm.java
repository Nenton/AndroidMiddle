package com.nenton.androidmiddle.data.storage.realm;

import com.nenton.androidmiddle.data.managers.DataManager;
import com.nenton.androidmiddle.data.managers.PreferencesManager;
import com.nenton.androidmiddle.data.network.res.Comment;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by serge on 09.01.2017.
 */

public class CommentRealm extends RealmObject implements Serializable{

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

    public CommentRealm(float raiting, String comment) {
        this.id = String.valueOf(this.hashCode());
        final PreferencesManager pm = DataManager.getInstance().getPreferencesManager();
        this.userName = pm.getUserName();
        this.avatar = pm.getUserPhoto();
        this.raiting = raiting;
        this.commentDate = new Date();
        this.comment = comment;
    }

    public CommentRealm(Comment comment) {
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
