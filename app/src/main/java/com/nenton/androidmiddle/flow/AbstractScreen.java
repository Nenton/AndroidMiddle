package com.nenton.androidmiddle.flow;

import android.util.Log;

import com.nenton.androidmiddle.mortar.ScreenScoper;

import flow.ClassKey;

public abstract class AbstractScreen<T>  extends ClassKey {

    private static final String TAG = "AbstractScreen";

    public String getScopeName(){
        return getClass().getName();
    }

    public abstract Object createScreenComponent(T parentComponent);

    // TODO: 27.11.2016 unregister scope
    public void unregisterscope(){
        Log.e(TAG," unregister scope " + this.getScopeName());
        ScreenScoper.destroyScreenScope(getScopeName());
    }
}
