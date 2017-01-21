package com.nenton.androidmiddle.ui.screens.product_details.comments;

import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.dto.CommentDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by serge on 10.01.2017.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>{

    private List<CommentDto> mCommentList = new ArrayList<>();

    @Inject
    Picasso mPicasso;

    public void addItem(CommentDto commentDto){
        mCommentList.add(commentDto);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        DaggerService.<CommentScreen.Component>getDaggerComponent(recyclerView.getContext()).inject(this);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CommentsAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(CommentsAdapter.CommentViewHolder holder, int position) {
        CommentDto comment = mCommentList.get(position);
        holder.userNameTxt.setText(comment.getUserName());
        holder.commentTxt.setText(comment.getComment());
        holder.dateTxt.setText(comment.getCommentDate());
        holder.rating.setRating(comment.getRating());

        mPicasso.load(comment.getAvatarUri())
                .fit()
                .into(holder.commentAvatarImg);

    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.comment_avatar_img)
        ImageView commentAvatarImg;
        @BindView(R.id.user_name_txt)
        TextView userNameTxt;
        @BindView(R.id.date_txt)
        TextView dateTxt;
        @BindView(R.id.item_comment_txt)
        TextView commentTxt;
        @BindView(R.id.rating)
        AppCompatRatingBar rating;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
