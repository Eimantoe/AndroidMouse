package com.example.androidmouse.UI.MainActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidmouse.UI.Common.Factories.ViewFactory;
import com.example.androidmouse.R;
import com.example.androidmouse.UI.Common.Views.BaseObservableView;
import com.example.androidmouse.UI.Common.Toolbar.ToolbarView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class MainActivityViewImpl extends BaseObservableView<MainActivityView.Listener>
        implements MainActivityView, ToolbarView.Listener {

    private final String VIEW_NAME;

    private final Toolbar mToolbar;
    private final ToolbarView mToolbarView;

    public MainActivityViewImpl(ViewFactory viewFactory, LayoutInflater inflater, String deviceName, @Nullable ViewGroup viewGroup) {
        setRootView(inflater.inflate(R.layout.layout_main_activity, viewGroup, false));

        VIEW_NAME = deviceName;

        mToolbar = findViewById(R.id.toolbar);

        mToolbarView = viewFactory.getToolbarView(this, mToolbar, VIEW_NAME);
        mToolbarView.enableToolbar(this, View.VISIBLE, View.VISIBLE);

        mToolbar.addView(mToolbarView.getRootView());
    }

    @Override
    public void onBackPressed() {
        for (Listener listener: getListeners())
        {
            listener.onBackButtonPressed();
        }
    }

    @Override
    public void onKeyboardPressed() {
        for (Listener listener: getListeners()) {
            listener.onKeyboardPressed();
        }
    }
}
