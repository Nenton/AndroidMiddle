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
import com.nenton.androidmiddle.ui.screens.product_details.DetailsScreen;

import dagger.Provides;
import io.realm.RealmList;
import mortar.MortarScope;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

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
    public class Module{
        @Provides
        @DaggerScope(CommentScreen.class)
        CommentsPresenter provideCommentsPresenter(){
            return new CommentsPresenter(mProductRealm);
        }
    }

    @dagger.Component(dependencies = DetailsScreen.Component.class, modules = Module.class)
    @DaggerScope(CommentScreen.class)
    public interface Component{
        void inject(CommentsPresenter commentsPresenter);
        void inject(CommentsView commentsView);
        void inject(CommentsAdapter commentsAdapter);
    }

    public class CommentsPresenter extends AbstractPresenter<CommentsView, DetailModel>{

        private final ProductRealm mProduct;

        public CommentsPresenter(ProductRealm productRealm) {
            mProduct = productRealm;
        }

        @Override
        protected void initActionBar() {
            //empty
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

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
    }
}
