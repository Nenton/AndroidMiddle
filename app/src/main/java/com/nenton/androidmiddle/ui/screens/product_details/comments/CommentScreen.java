package com.nenton.androidmiddle.ui.screens.product_details.comments;

import android.os.Bundle;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.dto.CommentDto;
import com.nenton.androidmiddle.data.storage.realm.CommentRealm;
import com.nenton.androidmiddle.data.storage.realm.ProductRealm;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.DaggerScope;
import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.flow.Screen;
import com.nenton.androidmiddle.mvp.models.DetailModel;
import com.nenton.androidmiddle.mvp.presenters.AbstractPresenter;
import com.nenton.androidmiddle.mvp.presenters.RootPresenter;
import com.nenton.androidmiddle.ui.screens.product_details.DetailsScreen;

import java.util.List;

import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import mortar.MortarScope;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by serge on 10.01.2017.
 */
@Screen(R.layout.screen_comments)
public class CommentScreen extends AbstractScreen<DetailsScreen.Component> {
    private ProductRealm mProductRealm;

    public CommentScreen(ProductRealm productRealm) {
        mProductRealm = productRealm;
    }

    @Override
    public Object createScreenComponent(DetailsScreen.Component parentComponent) {
        return DaggerCommentScreen_Component.builder()
                .component(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(CommentScreen.class)
        CommentsPresenter provideCommentsPresenter() {
            return new CommentsPresenter(mProductRealm);
        }
    }

    @dagger.Component(dependencies = DetailsScreen.Component.class, modules = Module.class)
    @DaggerScope(CommentScreen.class)
    public interface Component {
        void inject(CommentsPresenter commentsPresenter);

        void inject(CommentsView commentsView);

        void inject(CommentsAdapter commentsAdapter);
    }

    public class CommentsPresenter extends AbstractPresenter<CommentsView, DetailModel> {

        private final ProductRealm mProduct;
        private RealmChangeListener mListener;

        public CommentsPresenter(ProductRealm productRealm) {
            mProduct = productRealm;
        }

        @Override
        protected void initActionBar() {
            //empty
        }

        @Override
        protected void initFab() {
            mRootPresenter.newFabBuilder()
                    .setColor(R.color.colorPrimary)
                    .setDrawable(R.drawable.ic_add_black_24dp)
                    .setMargins(0, 0, 16, 16)
                    .setOnClickListener(this::clickOnAddComment)
                    .create();
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            mListener = new RealmChangeListener<ProductRealm>() {
                @Override
                public void onChange(ProductRealm element) {
                    CommentsPresenter.this.updateProductList(element);
                }
            };

            mProduct.addChangeListener(mListener);

            RealmList<CommentRealm> comments = mProduct.getCommentRealms();
            Observable<CommentDto> commentObs = Observable.from(comments)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .map(CommentDto::new);

            mCompSubs.add(subscribe(commentObs, new ViewSubscriber<CommentDto>() {
                @Override
                public void onNext(CommentDto commentDto) {
                    getView().getAdapter().addItem(commentDto);
                }
            }));
            getView().initView();
        }

        @Override
        public void dropView(CommentsView view) {
            mProduct.removeChangeListener(mListener);
            mRootPresenter.hideFab();
            super.dropView(view);
        }

        private void updateProductList(ProductRealm element) {
            Observable<List<CommentDto>> obs = Observable.from(element.getCommentRealms())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .map(CommentDto::new)
                    .toList();

            mCompSubs.add(subscribe(obs, new ViewSubscriber<List<CommentDto>>() {
                @Override
                public void onNext(List<CommentDto> commentDtos) {
                    getView().getAdapter().reloadAdapter(commentDtos);
                }
            }));
        }

        public void clickOnAddComment() {
            getView().showAddCommentDialog();
        }

        public void addComment(CommentRealm comment) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> mProduct.getCommentRealms().add(comment));
            realm.close();
        }
    }
}
