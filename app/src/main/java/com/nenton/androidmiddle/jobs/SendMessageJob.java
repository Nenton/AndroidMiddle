package com.nenton.androidmiddle.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nenton.androidmiddle.data.managers.DataManager;
import com.nenton.androidmiddle.data.network.res.Comment;
import com.nenton.androidmiddle.data.storage.realm.CommentRealm;
import com.nenton.androidmiddle.data.storage.realm.ProductRealm;
import com.nenton.androidmiddle.utils.AppConfig;

import io.realm.Realm;

/**
 * Created by serge on 01.02.2017.
 */

public class SendMessageJob extends Job {

    private final String mProductId;
    private final CommentRealm mComment;
    private static final String TAG = "SendMessageJob";

    public SendMessageJob(String productId, CommentRealm comment) {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .persist()
                .groupBy("Comments"));
        mComment = comment;
        mProductId = productId;
    }

    @Override
    public void onAdded() {
        Log.e(TAG, "SEND MESSAGE onAdded: ");
        Realm realm = Realm.getDefaultInstance();
        ProductRealm product = realm.where(ProductRealm.class)
                .equalTo("id", mProductId)
                .findFirst();

        realm.executeTransaction(realm1 -> product.getCommentRealms().add(mComment));
        realm.close();

    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, "SEND MESSAGE onRun: ");
        Comment comment = new Comment(mComment);
        DataManager.getInstance().sendComment(mProductId, comment)
                .subscribe(commentRes -> {
                    Realm realm = Realm.getDefaultInstance();
                    CommentRealm localComment = realm.where(CommentRealm.class)
                            .equalTo("id", mComment.getId())
                            .findFirst();
                    ProductRealm product = realm.where(ProductRealm.class)
                            .equalTo("id", mProductId)
                            .findFirst();
                    CommentRealm serverComment = new CommentRealm(commentRes);
                    realm.executeTransaction(realm1 -> {
                        localComment.deleteFromRealm();
                        product.getCommentRealms().add(serverComment);
                    });
                });

    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Log.e(TAG, "SEND MESSAGE onCancel: ");
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        Log.e(TAG, "SEND MESSAGE shouldReRunOnThrowable: ");
        return RetryConstraint.createExponentialBackoff(runCount, AppConfig.INITIAL_BACK_OFF_IN_MS);
    }
}
