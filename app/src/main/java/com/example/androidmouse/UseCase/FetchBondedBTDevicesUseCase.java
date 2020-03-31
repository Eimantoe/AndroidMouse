package com.example.androidmouse.UseCase;

import android.bluetooth.BluetoothDevice;

import com.example.androidmouse.Common.BaseObservable;
import com.example.androidmouse.DataModel.BTDeviceModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FetchBondedBTDevicesUseCase extends BaseObservable<FetchBondedBTDevicesUseCase.Listener> {

    public interface Listener {
        public void onDisabledBluetooth();
    }

    private final BTDeviceModel mBTDeviceModel;
    private Set<BluetoothDevice> mSetOfDevices;

    public FetchBondedBTDevicesUseCase(BTDeviceModel btDeviceModel) {
        mBTDeviceModel = btDeviceModel;
    }

    public ArrayList<BluetoothDevice> fetchDevices() {

        ArrayList<BluetoothDevice> arrayOfDevices = new ArrayList<>();

        try {
            mSetOfDevices = mBTDeviceModel.getBondedDevices();

            for (BluetoothDevice device : mSetOfDevices)
            {
                arrayOfDevices.add(device);
            }

        } catch (IOException e) {
            for (Listener listener: getListeners()) {
                listener.onDisabledBluetooth();
            }
        }

        return arrayOfDevices;
    }

    public BluetoothDevice fetchDeviceByAddress(String addr) {

        Set<BluetoothDevice> setOfDevices = new HashSet<>();

        try {
            setOfDevices = mBTDeviceModel.getBondedDevices();

            for (BluetoothDevice device : setOfDevices)
            {
                if (device
                        .getAddress()
                        .replace(":", "")
                        .equalsIgnoreCase(addr.replace(":", "")))
                {
                    return device;
                }
            }
        } catch (IOException e) {
            for (Listener listener: getListeners()) {
                listener.onDisabledBluetooth();
            }
        }

        return null;
    }

}
