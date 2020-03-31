package com.example.androidmouse.UI.BTDeviceListActivity;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidmouse.R;
import com.example.androidmouse.UI.Common.Views.BaseObservableView;

import androidx.annotation.Nullable;

public class DeviceListRecyclerItemImpl extends BaseObservableView<DeviceListRecyclerItem.Listener>
        implements DeviceListRecyclerItem {

    private final TextView mFriendlyName;
    private final TextView mBTAddress;
    private BluetoothDevice mBTDevice;

    public DeviceListRecyclerItemImpl(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup) {
        setRootView(layoutInflater.inflate(R.layout.layout_device_list_item, viewGroup, false));

        mFriendlyName   = findViewById(R.id.item_friendly_name);
        mBTAddress      = findViewById(R.id.item_bt_address);

        getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Listener listener : getListeners())
                {
                    listener.onItemClicked(mBTDevice);
                }
            }
        });
    }


    @Override
    public void bindItem(BluetoothDevice device) {
        mBTDevice = device;

        mFriendlyName.setText(device.getName());
        mBTAddress.setText(device.getAddress());
    }
}
