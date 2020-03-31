package com.example.androidmouse.UI.BTDeviceListActivity;

import android.bluetooth.BluetoothDevice;

import com.example.androidmouse.Common.BaseObservable;
import com.example.androidmouse.UseCase.FetchBondedBTDevicesUseCase;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;

import androidx.annotation.Nullable;


public class DeviceListActivityController extends BaseObservable<DeviceListActivityController.Listener>
        implements DeviceListView.Listener, FetchBondedBTDevicesUseCase.Listener {

    public interface Listener {
        public void onItemClicked(BluetoothDevice device);
        public void onDisabledBluetooth();;
    }

    public enum ControllerState {
        onStart,
        onStop,
        bindView,
        fetchAndBindData,
        fetchDeviceByAddress,
        fetchDevices,
        onScan
    };

    private ControllerState mControllerState;

    private final FetchBondedBTDevicesUseCase mFetchBondedBTDevicesUseCase;
    private final IntentIntegrator mIntentIntegrator;
    private String lastScannedString;

    public DeviceListActivityController(
            FetchBondedBTDevicesUseCase fetchBondedBTDevicesUseCase,
            IntentIntegrator intentIntegrator
    ) {
        mFetchBondedBTDevicesUseCase = fetchBondedBTDevicesUseCase;
        mIntentIntegrator = intentIntegrator;
    }

    private DeviceListView mDeviceListView;

    public void bindView(DeviceListView deviceListView)
    {
        mDeviceListView = deviceListView;
    }

    public void onStart(){

        mControllerState = ControllerState.onStart;

        mDeviceListView.showProgressIndicator();
        mDeviceListView.registerListener(this);

        mFetchBondedBTDevicesUseCase.registerListener(this);

        fetchAndBindData();
    }

    public void fetchAndBindData() {

        mControllerState = ControllerState.fetchAndBindData;

        ArrayList<BluetoothDevice> arrayOfDevices = mFetchBondedBTDevicesUseCase.fetchDevices();

        mDeviceListView.bindDevices(arrayOfDevices);
        mDeviceListView.hideProgressIndicator();
    }

    public void onStop() {

        mControllerState = ControllerState.onStop;

        mDeviceListView.unregisterListener(this);
        mFetchBondedBTDevicesUseCase.unregisterListener(this);
    }

    public BluetoothDevice fetchDeviceByAddress(@Nullable String address) {
        mControllerState = ControllerState.fetchDeviceByAddress;

        if (lastScannedString != null) {
            return mFetchBondedBTDevicesUseCase.fetchDeviceByAddress(lastScannedString);
        }
        else {
            return mFetchBondedBTDevicesUseCase.fetchDeviceByAddress(address);
        }
    }

    @Override
    public void onDeviceClicked(BluetoothDevice device) {
        for (Listener listener : getListeners())
        {
            listener.onItemClicked(device);
        }
    }

    @Override
    public void onBtnScanClicked() {
        mControllerState = ControllerState.onScan;
        mIntentIntegrator.initiateScan();
    }

    public void retryScan() {
        mControllerState = ControllerState.onScan;
        mIntentIntegrator.initiateScan();
    }

    @Override
    public void onDisabledBluetooth() {
        for (Listener listener: getListeners()) {
            listener.onDisabledBluetooth();
        }
    }

    public ControllerState getControllerState() {
        return mControllerState;
    }

}
