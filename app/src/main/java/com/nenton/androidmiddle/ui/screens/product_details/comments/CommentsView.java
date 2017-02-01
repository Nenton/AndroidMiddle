package com.nenton.androidmiddle.ui.screens.product_details.comments;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.realm.CommentRealm;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.mvp.views.AbstractView;

import butterknife.BindView;

/**
 * Created by serge on 10.01.2017.
 */
public class CommentsView extends AbstractView<CommentScreen.CommentsPresenter>{

    private CommentsAdapter mAdapter = new CommentsAdapter();

    @BindView(R.id.comments_list)
    RecyclerView mCommentsList;

    public CommentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<CommentScreen.Component>getDaggerComponent(context).inject(this);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    public CommentsAdapter getAdapter(){
        return mAdapter;
    }

    public void initView(){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mCommentsList.setLayoutManager(manager);
        mCommentsList.setAdapter(mAdapter);
    }

    public void showAddCommentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_comment, null);

        AppCompatRatingBar ratingBar = (AppCompatRatingBar) view.findViewById(R.id.comment_rb);
        EditText text = (EditText) view.findViewById(R.id.comment_et);

        builder.setTitle("")
                .setView(view)
                .setPositiveButton("Оставить отзыв", (dialog, which) -> {
                    CommentRealm comment = new CommentRealm(ratingBar.getRating(), text.getText().toString());
                    mPresenter.addComment(comment);
                })
                .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel())
                .show();
    }
}
