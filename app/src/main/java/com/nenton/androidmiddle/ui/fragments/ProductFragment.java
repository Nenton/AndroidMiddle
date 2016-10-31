package com.nenton.androidmiddle.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.mvp.presenters.ProductPresenter;
import com.nenton.androidmiddle.mvp.presenters.ProductPresenterFactory;
import com.nenton.androidmiddle.mvp.views.IProductView;
import com.nenton.androidmiddle.ui.activities.RootActivity;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindDrawable(R.drawable.product)
    Drawable productDraw;

    private ProductPresenter mPresenter;

    @OnClick(R.id.minus_btn)
    void minusBtn() {
        mPresenter.clickOnMinus();
    }

    @OnClick(R.id.plus_btn)
    void plusBtn() {
        mPresenter.clickOnPlus();
    }

    public static ProductFragment newInstance(ProductDto product){
        Bundle bundle = new Bundle();
        bundle.putParcelable("PRODUCT",product);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle (Bundle bundle){
        if (bundle!=null){
            ProductDto product = bundle.getParcelable("PRODUCT");
            mPresenter = ProductPresenterFactory.getInstance(product);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product,container,false);
        ButterKnife.bind(this,view);
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
    public void showProductView(ProductDto product) {
        mProductTitle.setText(product.getProductName());
        mProductDescription.setText(product.getDescription());
        mProductCount.setText(String.valueOf(product.getCount()));
        // TODO: 30.10.2016 picasso
        mProductImage.setImageDrawable(productDraw);

        if (product.getCount() > 0){
            mProductPrice.setText(String.valueOf(product.getCount() * product.getPrice() + ".-"));
        } else {
            mProductPrice.setText(String.valueOf(product.getPrice()));
        }

    }

    @Override
    public void updateProductCountView(ProductDto product) {
        mProductCount.setText(String.valueOf(product.getCount()));
        if (product.getCount()>0){
            mProductPrice.setText(String.valueOf(product.getCount() * product.getPrice() + ".-"));
        }

    }

    @Override
    public void showMessage(String message) {
        getRootActivity().showMessage(message);
    }

    @Override
    public void showError(Exception e) {
        getRootActivity().showError(e);
    }

    @Override
    public void showLoad() {
        getRootActivity().showLoad();

    }

    @Override
    public void hideLoad() {
        getRootActivity().hideLoad();
    }

    //endregion

    private RootActivity getRootActivity() {
        return (RootActivity) getActivity();
    }
}
