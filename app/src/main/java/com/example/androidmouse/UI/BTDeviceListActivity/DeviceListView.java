package com.example.androidmouse.UI.BTDeviceListActivity;

import android.bluetooth.BluetoothDevice;

import com.example.androidmouse.UI.Common.Views.ObservableView;

import java.util.ArrayList;

public interface DeviceListView extends ObservableView<DeviceListView.Listener> {

    public interface Listener {
        public void onDeviceClicked(BluetoothDevice device);
        public void onBtnScanClicked();
    }

    public void showProgressIndicator();
    public void hideProgressIndicator();
    public void bindDevices(ArrayList<BluetoothDevice> devices);
}
