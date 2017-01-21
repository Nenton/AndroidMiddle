package com.nenton.androidmiddle.ui.screens.product_details;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.realm.ProductRealm;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.flow.AbstractScreen;
import com.nenton.androidmiddle.ui.screens.product_details.comments.CommentScreen;
import com.nenton.androidmiddle.ui.screens.product_details.description.DescriptionScreen;

import mortar.MortarScope;

/**
 * Created by serge on 05.01.2017.
 */
public class DetailAdapter extends PagerAdapter{
    private final ProductRealm mProductRealm;

    public DetailAdapter(ProductRealm productRealm) {
        mProductRealm = productRealm;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AbstractScreen screen = null;
        switch (position){
            case 0:
                // TODO: 06.01.2017 create screen with scope
                screen = new DescriptionScreen(mProductRealm);
                break;
            case 1:
                // TODO: 06.01.2017 create screen with scope
                screen = new CommentScreen(mProductRealm);
                break;
        }

        MortarScope mortarScope = createScreenScopeFromContext(container.getContext(), screen);
        Context screenContext = mortarScope.createContext(container.getContext());
        View newView = LayoutInflater.from(screenContext).inflate(screen.getLayoutResId(),container,false);
        container.addView(newView);
        return newView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Описание";
                break;
            case 1:
                title = "Комментарии";
                break;
        }
        return title;
    }

    private MortarScope createScreenScopeFromContext(Context context, AbstractScreen screen){
        MortarScope parentScope = MortarScope.getScope(context);
        MortarScope childScope = parentScope.findChild(screen.getScopeName());

        if (childScope == null){
            Object screenComponent = screen.createScreenComponent(parentScope.getService(DaggerService.SERVICE_NAME));
            if (screenComponent == null){
                throw new IllegalStateException(" don't create screen component for " + screen.getScopeName());
            }

            childScope = parentScope.buildChild()
                    .withService(DaggerService.SERVICE_NAME, screenComponent)
                    .build(screen.getScopeName());

        }

        return childScope;
    }

}
