package com.nenton.androidmiddle.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.androidmiddle.BuildConfig;
import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.dto.UserInfoDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.components.AppComponent;
import com.nenton.androidmiddle.di.modules.PicassoCacheModule;
import com.nenton.androidmiddle.di.modules.RootModule;
import com.nenton.androidmiddle.di.sqopes.RootScope;
import com.nenton.androidmiddle.flow.TreeKeyDispatcher;
import com.nenton.androidmiddle.mvp.models.AccountModel;
import com.nenton.androidmiddle.mvp.presenters.MenuItemHolder;
import com.nenton.androidmiddle.mvp.presenters.RootPresenter;
import com.nenton.androidmiddle.mvp.views.IActionBarView;
import com.nenton.androidmiddle.mvp.views.IRootView;
import com.nenton.androidmiddle.mvp.views.IView;

import com.nenton.androidmiddle.ui.screens.account.AccountScreen;
import com.nenton.androidmiddle.ui.screens.auth.AuthScreen;
import com.nenton.androidmiddle.ui.screens.catalog.CatalogScreen;
import com.nenton.androidmiddle.utils.App;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import flow.Flow;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

public class RootActivity extends AppCompatActivity implements IRootView, NavigationView.OnNavigationItemSelectedListener, IActionBarView {

    @BindView(R.id.drawer_root_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.root_coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.nav_root_view)
    NavigationView mNavigationView;
    @BindView(R.id.root_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.root_frame)
    FrameLayout mFrameContainer;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBar;

    @Inject
    RootPresenter mRootPresenter;
    @Inject
    Picasso mPicasso;

    private ActionBarDrawerToggle mToggle;
    private ActionBar mActionBar;
    private List<MenuItemHolder> mActionBarMenuItems;

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = Flow.configure(newBase,this)
                .defaultKey(new AuthScreen())
                .dispatcher(new TreeKeyDispatcher(this))
                .install();
        super.attachBaseContext(newBase);
    }

    @Override
    public Object getSystemService(String name) {
        MortarScope rootActivityScope = MortarScope.findChild(getApplicationContext(), RootActivity.class.getName());
        return rootActivityScope.hasService(name) ? rootActivityScope.getService(name) : super.getSystemService(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);
        ButterKnife.bind(this);
        RootComponent rootComponent = DaggerService.getDaggerComponent(this);
        rootComponent.inject(this);
        initToolbar();
        mRootPresenter.takeView(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        mRootPresenter.dropView(this);
        super.onDestroy();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Object key = null;
        switch (item.getItemId()) {
            case R.id.nav_account:
                key = new AccountScreen();
                break;
            case R.id.nav_catalog:
                key = new CatalogScreen();
                break;
            case R.id.nav_favorites:
                break;
            case R.id.nav_orders:
                break;
            case R.id.nav_notifications:
                break;
        }

        if (key != null){
            Flow.get(this).set(key);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //region ========================= IRootView =========================

    @Override
    public void showMessage(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(Throwable e) {
        if (BuildConfig.DEBUG) {
            showMessage(e.getMessage());
            e.printStackTrace();
        } else {
            showMessage("Что-то пошло не так. Попробуйте повторить позже");
            // TODO: 21.10.2016 send error stacktrace
        }

    }

    @Override
    public void showLoad() {

        // TODO: 21.10.2016 show load animation
    }

    @Override
    public void hideLoad() {

        // TODO: 21.10.2016 hide load animation
    }

    @Nullable
    @Override
    public IView getCurrentScreen() {
        return (IView) mFrameContainer.getChildAt(0);
    }

    @Override
    public void initDrawer(UserInfoDto infoDto) {
        View header = mNavigationView.getHeaderView(0);
        ImageView avatar = (ImageView) header.findViewById(R.id.drawer_user_avatar);
        TextView userName = (TextView) header.findViewById(R.id.drawer_user_name);

        mPicasso.load(infoDto.getAvatar())
                .fit()
                .centerCrop()
                .into(avatar);

        userName.setText(infoDto.getName());
    }

    //endregion
    @Override
    public void onBackPressed() {
        if (getCurrentScreen() != null && !getCurrentScreen().viewOnBackPressed() && !Flow.get(this).goBack()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Выход")
                    .setPositiveButton("Да", (dialog, which) -> super.onBackPressed())
                    .setNegativeButton("Нет", (dialog, which) -> dialog.cancel())
                    .setMessage("Вы действительно хотите выйти?")
                    .show();
        }
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRootPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRootPresenter.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    //region ========================= ActionBar =========================

    @Override
    public void setVisable(boolean visable) {
        if (visable){
            mActionBar.show();
        } else {
            mActionBar.hide();
        }
        // TODO: 05.01.2017 implement me
    }

    @Override
    public void setBackArrow(boolean enabled) {
        if (mToggle != null && mActionBar != null){
            if (enabled){
                mToggle.setDrawerIndicatorEnabled(false);
                mActionBar.setDisplayHomeAsUpEnabled(true);
                if (mToggle.getToolbarNavigationClickListener() == null){
                    mToggle.setToolbarNavigationClickListener(v -> onBackPressed());
                }
            } else {
                mToggle.setDrawerIndicatorEnabled(true);
                mActionBar.setDisplayHomeAsUpEnabled(false);
                mToggle.setToolbarNavigationClickListener(null);
            }

            mDrawerLayout.setDrawerLockMode(enabled ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED);
            mToggle.syncState();
        }
    }

    @Override
    public void setMenuItem(List<MenuItemHolder> items) {
        mActionBarMenuItems = items;
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mActionBarMenuItems != null && !mActionBarMenuItems.isEmpty()){
            for (MenuItemHolder menuItem: mActionBarMenuItems) {
                MenuItem item = menu.add(menuItem.getTitle());
                item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                        .setIcon(menuItem.getIconResId())
                        .setOnMenuItemClickListener(menuItem.getListener());
            }
        } else {
            menu.clear();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTabLayout(ViewPager pager) {
        TabLayout tabView = new TabLayout(this);
        tabView.setupWithViewPager(pager);
        mAppBar.addView(tabView);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabView));
    }

    @Override
    public void removeTabLayout() {
        View tabView = mAppBar.getChildAt(1);
        if (tabView != null && tabView instanceof TabLayout){
            mAppBar.removeView(tabView);
        }

    }

    //endregion

    //region ========================= DI =========================

    @dagger.Component(dependencies = AppComponent.class, modules = {RootModule.class, PicassoCacheModule.class})
    @RootScope
    public interface RootComponent {
        void inject(RootActivity rootActivity);
        void inject(RootPresenter rootPresenter);
        AccountModel getAccountModel();
        RootPresenter getRootPresenter();
        Picasso getPicasso();
    }
    //endregion

}