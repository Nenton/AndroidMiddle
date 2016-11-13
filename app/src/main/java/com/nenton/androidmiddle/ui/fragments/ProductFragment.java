package com.nenton.androidmiddle.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.components.DaggerPicassoCacheComponent;
import com.nenton.androidmiddle.di.components.PicassoCacheComponent;
import com.nenton.androidmiddle.di.modules.PicassoCacheModule;
import com.nenton.androidmiddle.di.sqopes.ProductScope;
import com.nenton.androidmiddle.mvp.presenters.ProductPresenter;
import com.nenton.androidmiddle.mvp.views.IProductView;
import com.nenton.androidmiddle.ui.activities.RootActivity;
import com.nenton.androidmiddle.utils.AndroidMiddleAplication;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Provides;

public class ProductFragment extends Fragment implements IProductView {

    private static final String TAG = "Product Fragment";

    @BindView(R.id.product_title)
    TextView mProductTitle;
    @BindView(R.id.product_description)
    TextView mProductDescription;
    @BindView(R.id.product_count)
    TextView mProductCount;
    @BindView(R.id.product_price)
    TextView mProductPrice;
    @BindView(R.id.minus_btn)
    ImageButton mMinusBtn;
    @BindView(R.id.plus_btn)
    ImageButton mPlusBtn;
    @BindView(R.id.product_image)
    ImageView mProductImage;

    @Inject
    ProductPresenter mPresenter;
    @Inject
    Picasso mPicasso;

    @OnClick(R.id.minus_btn)
    void minusBtn() {
        mPresenter.clickOnMinus();
    }

    @OnClick(R.id.plus_btn)
    void plusBtn() {
        mPresenter.clickOnPlus();
    }

    public static ProductFragment newInstance(ProductDto product) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("PRODUCT", product);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            ProductDto product = bundle.getParcelable("PRODUCT");
            createComponent(product).inject(this);
            // TODO: 04.11.2016 fix recreate component
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        ButterKnife.bind(this, view);
        readBundle(getArguments());
        mPresenter.takeView(this);
        mPresenter.initView();
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    //region ========================= IProductView =========================


    @Override
    public void showProductView(final ProductDto product) {
        mProductTitle.setText(product.getProductName());
        mProductDescription.setText(product.getDescription());
        mProductCount.setText(String.valueOf(product.getCount()));

        // TODO: 30.10.2016 picasso
        mPicasso.load(product.getUrlProduct())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .centerCrop()
                .into(mProductImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e(TAG,"om Success: load from Cache");
                    }

                    @Override
                    public void onError() {
                        mPicasso.load(product.getUrlProduct())
                                .fit()
                                .centerCrop()
                                .into(mProductImage);
                    }
                });

        if (product.getCount() > 0) {
            mProductPrice.setText(String.valueOf(product.getCount() * product.getPrice() + ".-"));
        } else {
            mProductPrice.setText(String.valueOf(product.getPrice()));
        }

    }

    @Override
    public void updateProductCountView(ProductDto product) {
        mProductCount.setText(String.valueOf(product.getCount()));
        if (product.getCount() > 0) {
            mProductPrice.setText(String.valueOf(product.getCount() * product.getPrice() + ".-"));
        }

    }
//
//    @Override
//    public void showMessage(String message) {
//        getRootActivity().showMessage(message);
//    }
//
//    @Override
//    public void showError(Exception e) {
//        getRootActivity().showError(e);
//    }
//
//    @Override
//    public void showLoad() {
//        getRootActivity().showLoad();
//
//    }
//
//    @Override
//    public void hideLoad() {
//        getRootActivity().hideLoad();
//    }

    //endregion

    private RootActivity getRootActivity() {
        return (RootActivity) getActivity();
    }

    //region ========================= DI =========================

    private Component createComponent(ProductDto product) {
        PicassoCacheComponent component = DaggerService.getComponent(PicassoCacheComponent.class);
        if (component == null) {
            component = DaggerPicassoCacheComponent.builder()
                    .appComponent(AndroidMiddleAplication.getAppComponent())
                    .picassoCacheModule(new PicassoCacheModule())
                    .build();
            DaggerService.registerComponent(PicassoCacheComponent.class, component);
        }

        return DaggerProductFragment_Component.builder()
                .picassoCacheComponent(component)
                .module(new Module(product))
                .build();
    }

    @dagger.Module
    public class Module {
        ProductDto mProductDto;

        public Module(ProductDto productDto) {
            mProductDto = productDto;
        }

        @Provides
        @ProductScope
        ProductPresenter provideProductPresenter() {
            return new ProductPresenter(mProductDto);
        }
    }

    @dagger.Component(dependencies = PicassoCacheComponent.class, modules = Module.class)
    @ProductScope
    interface Component {
        void inject(ProductFragment productFragment);
    }

    //endregion
}
