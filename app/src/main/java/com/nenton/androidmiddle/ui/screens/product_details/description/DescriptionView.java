package com.nenton.androidmiddle.ui.screens.product_details.description;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.dto.DescriptionDto;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.mvp.views.AbstractView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by serge on 10.01.2017.
 */
public class DescriptionView extends AbstractView<DescriptionScreen.DescriptionPresenter>{

    @BindView(R.id.full_description_txt)
    TextView fullDecriptionTxt;
    @BindView(R.id.product_desc_count)
    TextView productCount;
    @BindView(R.id.product_desc_price)
    TextView productPrice;
    @BindView(R.id.product_rating)
    AppCompatRatingBar productRating;

    public DescriptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<DescriptionScreen.Component>getDaggerComponent(context).inject(this);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    public void initView(DescriptionDto description){
        fullDecriptionTxt.setText(description.getDescription());
        productRating.setRating(description.getRating());
        productCount.setText(String.valueOf(description.getCount()));

        if (description.getCount() > 0){
            productPrice.setText(String.valueOf(description.getCount() * description.getPrice() + ".-"));
        } else {
            productPrice.setText(String.valueOf(description.getPrice() + ".-"));
        }
    }

    //region ========================= Events =========================

    @OnClick(R.id.plus_desc_btn)
    void clickOnPlus(){
        mPresenter.clickOnPlus();
    }

    @OnClick(R.id.minus_desc_btn)
    void clickOnMinus(){
        mPresenter.clickOnMinus();
    }

    //endregion
}
