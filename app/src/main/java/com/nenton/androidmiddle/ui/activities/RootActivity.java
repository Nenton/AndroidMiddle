package com.nenton.androidmiddle.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.nenton.androidmiddle.BuildConfig;
import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.sqopes.RootScope;
import com.nenton.androidmiddle.mvp.presenters.RootPresenter;
import com.nenton.androidmiddle.mvp.views.IRootView;
import com.nenton.androidmiddle.mvp.views.IView;
import com.nenton.androidmiddle.ui.fragments.AccountFragment;

import com.nenton.androidmiddle.ui.fragments.AddressFragment;
import com.nenton.androidmiddle.ui.fragments.CatalogFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Provides;

public class RootActivity extends AppCompatActivity implements IRootView, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_root_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.root_coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.nav_root_view)
    NavigationView mNavigationView;
    @BindView(R.id.root_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;

    @Inject
    RootPresenter mRootPresenter;

    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        ButterKnife.bind(this);
        initDrawer();
        initToolbar();
        createComponent().inject(this);
        mRootPresenter.initView();
        // TODO: 05.11.2016 init view
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null){
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new CatalogFragment())
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        mRootPresenter.dropView();
        super.onDestroy();
    }

    private void initToolbar() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void initDrawer() {
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_account:
                fragment = new AccountFragment();
                break;
            case R.id.nav_catalog:
                fragment = new CatalogFragment();
                break;
            case R.id.nav_favorites:
                break;
            case R.id.nav_orders:
                break;
            case R.id.nav_notifications:
                break;
        }
        if (fragment != null){
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack(null)
                    .commit();
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
    public void showError(Exception e) {
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

    //endregion
    @Override
    public void onBackPressed() {
    showDialog(1);
//        super.onBackPressed();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вы действительно хотите выйти?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    finish();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    }
                });
        return builder.create();
    }

    //region ========================= DI =========================

    private Component createComponent(){
        Component component = DaggerService.getComponent(Component.class);
        if (component == null){
            component = DaggerRootActivity_Component.builder()
                    .module(new Module())
                    .build();
            DaggerService.registerComponent(Component.class,component);
        }
        return component;
    }

    @dagger.Module
    public class Module{
        @Provides
        @RootScope
        RootPresenter provideRootPresenter(){
            return new RootPresenter();
        }
    }

    @dagger.Component(modules = Module.class)
    @RootScope
    public interface Component{
        void inject(RootActivity rootActivity);
        RootPresenter getRootPresenter();
    }

    //endregion





}
