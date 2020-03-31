package com.example.androidmouse.DataModel;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.io.IOException;
import java.util.Set;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BTDeviceModel {

    private BluetoothAdapter mBluetoothAdapter;

    public BTDeviceModel() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public Set<BluetoothDevice> getBondedDevices() throws IOException {

        if (!mBluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Log.d(TAG, getClass().getName() + " getBondedDevices() found bluetooth desibled");
            throw new IOException();
        }

        return mBluetoothAdapter.getBondedDevices();
    }

}
