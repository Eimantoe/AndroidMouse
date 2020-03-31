package com.example.androidmouse.UI.MainActivity;

import com.example.androidmouse.UI.Common.Views.ObservableView;

public interface MainActivityView extends ObservableView<MainActivityView.Listener> {

    public interface Listener {
        public void onBackButtonPressed();
        public void onKeyboardPressed();
    }

}
