package com.example.androidmouse.UI.BTDeviceListActivity;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.androidmouse.UI.Common.Factories.ViewFactory;
import com.example.androidmouse.R;
import com.example.androidmouse.UI.Common.Views.BaseObservableView;
import com.example.androidmouse.UI.Common.Toolbar.ToolbarView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceListViewImpl extends BaseObservableView<DeviceListView.Listener>
        implements DeviceListView, DeviceListRecyclerAdapter.Listener, View.OnClickListener {

    private final String VIEW_NAME;

    private final RecyclerView mRecyclerView;
    private final DeviceListRecyclerAdapter mRecyclerViewAdapter;
    private final ProgressBar mProgressBar;
    private final Toolbar mToolbar;
    private final ToolbarView mToolbarView;
    private final FloatingActionButton mBtnScan;

    public DeviceListViewImpl(ViewFactory viewFactory, LayoutInflater inflater, @Nullable ViewGroup viewGroup) {

        setRootView(inflater.inflate(R.layout.layout_device_list_activity, viewGroup, false));

        VIEW_NAME = getString(R.string.deviceListViewName);

        mRecyclerView = findViewById(R.id.device_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewAdapter = new DeviceListRecyclerAdapter(this, viewFactory);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mProgressBar = findViewById(R.id.progress_bar);

        mToolbar = findViewById(R.id.toolbar);

        mToolbarView = viewFactory.getToolbarView(null, mToolbar, VIEW_NAME);
        mToolbar.addView(mToolbarView.getRootView());

        mBtnScan = findViewById(R.id.act_btn_scan);
        mBtnScan.setOnClickListener(this);
    }
    
    @Override
    public void showProgressIndicator() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressIndicator() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void bindDevices(ArrayList<BluetoothDevice> devices) {
        mRecyclerViewAdapter.bindDevices(devices);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mBtnScan.getId())
        {
            for (Listener listener: getListeners())
            {
                listener.onBtnScanClicked();
            }
        }
    }

    @Override
    public void onDeviceClicked(BluetoothDevice device) {
        for (Listener listener : getListeners())
        {
            listener.onDeviceClicked(device);
        }
    }
}
