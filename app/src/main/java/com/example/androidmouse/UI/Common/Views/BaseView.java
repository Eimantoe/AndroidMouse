package com.example.androidmouse.UI.Common.Views;

import android.content.Context;
import android.view.View;

import androidx.annotation.StringRes;

public abstract class BaseView implements MVCView {

    private View mView;

    @Override
    public View getRootView() {
        return mView;
    }

    public void setRootView(View view) {
        mView = view;
    }

    public Context getContext()
    {
        return getRootView().getContext();
    }

    public String getString(@StringRes int id)
    {
        return getContext().getString(id);
    }

    protected <TYPE extends View> TYPE findViewById(int id){
        return getRootView().findViewById(id);
    }
}
