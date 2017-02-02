package com.nenton.androidmiddle.mvp.models;

import com.nenton.androidmiddle.data.storage.realm.CommentRealm;
import com.nenton.androidmiddle.jobs.SendMessageJob;

/**
 * Created by serge on 05.01.2017.
 */

public class DetailModel extends AbstractModel {
    public void sendComment(String id, CommentRealm comment) {
        SendMessageJob message = new SendMessageJob(id, comment);
        mJobManager.addJobInBackground(message);
    }
}
