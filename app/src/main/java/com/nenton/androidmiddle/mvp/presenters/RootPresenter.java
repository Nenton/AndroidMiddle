package com.nenton.androidmiddle.mvp.presenters;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.dto.ActivityResultDto;
import com.nenton.androidmiddle.data.storage.dto.UserInfoDto;
import com.nenton.androidmiddle.mvp.models.AccountModel;
import com.nenton.androidmiddle.mvp.views.IRootView;
import com.nenton.androidmiddle.ui.activities.RootActivity;
import com.nenton.androidmiddle.ui.activities.SplashActivity;
import com.nenton.androidmiddle.ui.custom_views.FloatActionButtonCustom;
import com.nenton.androidmiddle.utils.AndroidMiddleAplication;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import mortar.Presenter;
import mortar.bundler.BundleService;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class RootPresenter extends Presenter<IRootView> {

    private PublishSubject<ActivityResultDto> mActivityResultDtoObs = PublishSubject.create();
    @Inject
    AccountModel mAccountModel;
    private Subscription userInfoSub;

    private static int DEFAULT_MODE = 0;
    private static int TAB_MODE = 1;

    FloatingActionButton mButton;

    public RootPresenter() {
        AndroidMiddleAplication.getRootActivityRootComponent().inject(this);
    }

    @Override
    protected BundleService extractBundleService(IRootView view) {
        return (view instanceof RootActivity) ?
                BundleService.getBundleService((RootActivity) view) :
                BundleService.getBundleService((SplashActivity) view);
    }

    public PublishSubject<ActivityResultDto> getActivityResultDtoObs() {
        return mActivityResultDtoObs;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);
        if (getView() instanceof RootActivity) {
            userInfoSub = subscribeOnUserInfoObs();
        }
    }

    @Override
    public void dropView(IRootView view) {
        if (userInfoSub != null) {
            userInfoSub.unsubscribe();
        }
        super.dropView(view);
    }

    private Subscription subscribeOnUserInfoObs() {
        return mAccountModel.getUserInfoObs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new UserInfoSubscriber());
    }

    @Nullable
    public IRootView getRootView() {
        return getView();
    }

    public ActionBarBuilder newActionBarBuilder() {
        return this.new ActionBarBuilder();
    }

    public FabBuilder newFabBuilder() {
        return this.new FabBuilder();
    }

    public void hideFab() {
        if (getView() != null && mButton != null) {
            RootActivity activity = (RootActivity) getView();
            ViewGroup root = (ViewGroup) activity.findViewById(R.id.root_frame);
            root.removeView(mButton);
            mButton = null;
        }
    }
    //    @Override
//    public void initView() {
//        mAccountModel.getUserInfoObs()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new UserInfoSubscriber());
//    }

    @RxLogSubscriber
    private class UserInfoSubscriber extends Subscriber<UserInfoDto> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (getView() != null) {
                getView().showError(e);
            }
        }

        @Override
        public void onNext(UserInfoDto infoDto) {
            if (getView() != null) {
                getView().initDrawer(infoDto);
            }
        }
    }

    public boolean checkPermissionsAndRequestIfNotGranted(@NonNull String[] permissions, int requestCode) {
        boolean allGranted = true;
        for (String permission : permissions) {
            int selfPermission = ContextCompat.checkSelfPermission(((RootActivity) getView()), permission);
            if (selfPermission != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (!allGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((RootActivity) getView()).requestPermissions(permissions, requestCode);
            }
            return false;
        }
        return allGranted;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        mActivityResultDtoObs.onNext(new ActivityResultDto(requestCode, resultCode, intent));
    }

    public void onRequestPermissionResult(int requetCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        // TODO: 18.12.2016 implement me
    }

    public class ActionBarBuilder {
        private boolean isGoBack = false;
        private boolean isVisable = true;
        private CharSequence title;
        private List<MenuItemHolder> items = new ArrayList<>();
        private ViewPager pager;
        private int toolbarMode = DEFAULT_MODE;

        public ActionBarBuilder setBackArrow(boolean enable) {
            this.isGoBack = enable;
            return this;
        }

        public ActionBarBuilder setVisable(boolean visibale) {
            this.isVisable = visibale;
            return this;
        }

        public ActionBarBuilder addAction(MenuItemHolder menuItem) {
            this.items.add(menuItem);
            return this;
        }

        public ActionBarBuilder setTab(ViewPager pager) {
            this.toolbarMode = TAB_MODE;
            this.pager = pager;
            return this;
        }

        public ActionBarBuilder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public void build() {
            if (getView() != null) {
                RootActivity activity = (RootActivity) getView();
                activity.setVisable(isVisable);
                activity.setTitle(title);
                activity.setBackArrow(isGoBack);
                activity.setMenuItem(items);
                if (toolbarMode == TAB_MODE) {
                    activity.setTabLayout(pager);
                } else {
                    activity.removeTabLayout();
                }
            }
        }

    }

    public class FabBuilder {
        private FrameLayout.LayoutParams params;
        int gravity = Gravity.BOTTOM | Gravity.RIGHT; // default bottom right
        int drawable;
        int color = Color.WHITE;
        int size = 0;
        float scale = 0;
        View.OnClickListener mListener;

        public FabBuilder() {
            if (getView() != null) {
                RootActivity activity = (RootActivity) getView();
                scale = activity.getResources().getDisplayMetrics().density;
                size = (int) (72 * scale + 0.5f); // default size is 72dp by 72dp
                params = new FrameLayout.LayoutParams(size, size);
            }
        }

        /**
         * Sets the gravity for the FAB
         */
        public FabBuilder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * Sets the FAB drawable
         */
        public FabBuilder setDrawable(int drawable) {
            this.drawable = drawable;
            return this;
        }

        public FabBuilder setOnClickListener(ClickFab clickFab) {
            this.mListener = v -> {
                clickFab.run();
            };
            return this;
        }

        public FabBuilder setMargins(int left, int top, int right, int bottom) {
            params.setMargins((int) (left * scale + 0.5f), (int) (top * scale + 0.5f),
                    (int) (right * scale + 0.5f), (int) (bottom * scale + 0.5f));
            return this;
        }

        /**
         * Sets the FAB color
         */
        public FabBuilder setColor(final int color) {
            this.color = color;
            return this;
        }

        public void create() {
            if (getView() != null) {
                RootActivity activity = (RootActivity) getView();
//                scale = activity.getResources().getDisplayMetrics().density;
//                size = (int) (72 * scale + 0.5f); // default size is 72dp by 72dp
//                params = new FrameLayout.LayoutParams(size, size);

                mButton = new FloatingActionButton(activity);
                mButton.setBackgroundColor(this.color);
                mButton.setImageDrawable(activity.getResources().getDrawable(this.drawable));
                mButton.setOnClickListener(mListener);

                params.gravity = this.gravity;

                ViewGroup root = (ViewGroup) activity.findViewById(R.id.root_frame);
                root.addView(mButton, params);
            }
        }
    }

    public interface ClickFab{
        void run();
    }
}
