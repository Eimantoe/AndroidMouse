package com.example.androidmouse.UI.BTDeviceListActivity;

import android.bluetooth.BluetoothDevice;

import com.example.androidmouse.UI.Common.Views.ObservableView;

public interface DeviceListRecyclerItem extends ObservableView<DeviceListRecyclerItem.Listener> {

    public interface Listener {
        void onItemClicked(BluetoothDevice device);
    }

    void bindItem(BluetoothDevice device);

}
