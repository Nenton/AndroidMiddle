package com.nenton.androidmiddle.ui.screens.product;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.ProductDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.mvp.views.IProductView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductView extends LinearLayout implements IProductView{

    private static final String TAG = "Product View";

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
    ProductScreen.ProductPresenter mProductPresenter;
    @Inject
    Picasso mPicasso;

    public ProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()){
            DaggerService.<ProductScreen.Component>getDaggerComponent(context).inject(this);
        }
    }

    @OnClick(R.id.minus_btn)
    void clickMinus() {
        mProductPresenter.clickOnMinus();
    }

    @OnClick(R.id.plus_btn)
    void clickPlus() {
        mProductPresenter.clickOnPlus();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        if (!isInEditMode()) {
            //showViewFromState();
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            mProductPresenter.takeView(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!isInEditMode()) {
            mProductPresenter.dropView(this);
        }
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

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }
    //endregion

}
