package com.example.androidmouse.UI.BTDeviceListActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import com.example.androidmouse.UI.Common.Views.BaseActivity;
import com.example.androidmouse.Common.Constants;
import com.example.androidmouse.UI.ScreenNavigator.ScreenNavigator;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.annotation.Nullable;

public class DeviceListActivity extends BaseActivity implements DeviceListActivityController.Listener {

    private DeviceListActivityController mController;
    private DeviceListView mDeviceListView;
    private ScreenNavigator mScreenNavigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScreenNavigator = getControllerRootComposition().getScreenNavigator();

        mDeviceListView = getControllerRootComposition().getViewFactory().getDeviceListView(null);

        mController = getControllerRootComposition().getDeviceListActivityController();
        mController.bindView(mDeviceListView);

        setContentView(mDeviceListView.getRootView());
    }

    @Override
    protected void onStart() {
        super.onStart();

        mController.registerListener(this);
        mController.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mController.unregisterListener(this);
        mController.onStop();
    }

    private IntentResult mIntentResult;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.INTENT_INTEGRATOR_REQUEST_CODE) {

            mIntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (mIntentResult != null)
            {
                BluetoothDevice device = mController.fetchDeviceByAddress(mIntentResult.getContents());
                mScreenNavigator.startDeviceMainActivity(device.getName(), device.getAddress());
            }
        }
        else if (requestCode == Constants.ENABLE_BT_REQUEST_CODE) {
            if (mController.getControllerState() == DeviceListActivityController.ControllerState.fetchAndBindData) {
                mController.fetchAndBindData();
            }
            else if (mController.getControllerState() == DeviceListActivityController.ControllerState.fetchDeviceByAddress)
            {
                mController.fetchDeviceByAddress(null);
            }
        }

    }

    @Override
    public void onItemClicked(BluetoothDevice device) {
        String deviceName = device.getName();
        String deviceAddress = device.getAddress();

        mScreenNavigator.startDeviceMainActivity(deviceName, deviceAddress);
    }

    @Override
    public void onDisabledBluetooth() {
        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intentBtEnabled, Constants.ENABLE_BT_REQUEST_CODE);
    }


}
