package com.example.androidmouse.UI.ScreenNavigator;

import android.app.Activity;
import android.content.Intent;

import com.example.androidmouse.UI.MainActivity.MainActivity;

public class ScreenNavigator {

    private Activity mContext;

    public ScreenNavigator(Activity activity) {
        mContext = activity;
    }

    private Activity getContext()
    {
        return mContext;
    }

    public void startDeviceMainActivity(String deviceName, String deviceAddress) {

        Intent intent = new Intent(getContext(), MainActivity.class);

        intent.putExtra(MainActivity.DEVICE_NAME, deviceName);
        intent.putExtra(MainActivity.DEVICE_ADDRESS, deviceAddress);

        getContext().startActivity(intent);
    }

    public void backButtonPressed()
    {
        getContext().onBackPressed();
    }
}
