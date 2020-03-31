package com.example.androidmouse.UI.Common.Factories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.androidmouse.UI.BTDeviceListActivity.DeviceListRecyclerItem;
import com.example.androidmouse.UI.BTDeviceListActivity.DeviceListRecyclerItemImpl;
import com.example.androidmouse.UI.BTDeviceListActivity.DeviceListView;
import com.example.androidmouse.UI.BTDeviceListActivity.DeviceListViewImpl;
import com.example.androidmouse.UI.Common.Dialog.InfoDialog;
import com.example.androidmouse.UI.Common.Dialog.KeyboardInputDialog;
import com.example.androidmouse.UI.Common.Toolbar.ToolbarView;
import com.example.androidmouse.UI.MainActivity.MainActivityView;
import com.example.androidmouse.UI.MainActivity.MainActivityViewImpl;

import androidx.annotation.Nullable;

public class ViewFactory {

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public ViewFactory(LayoutInflater mLayoutInflater, Context context) {
        this.mLayoutInflater = mLayoutInflater;
        this.mContext = context;
    }

    public DeviceListView getDeviceListView(@Nullable ViewGroup parent) {
        return new DeviceListViewImpl(this, mLayoutInflater, parent);
    }

    public DeviceListRecyclerItem getDeviceListRecyclerItem(@Nullable ViewGroup viewGroup) {
        return new DeviceListRecyclerItemImpl(mLayoutInflater, viewGroup);
    }

    public MainActivityView getMainActivityView(String deviceName, @Nullable ViewGroup viewGroup) {
        return new MainActivityViewImpl(this, mLayoutInflater, deviceName, viewGroup);
    }

    public ToolbarView getToolbarView(ToolbarView.Listener listener, @Nullable ViewGroup viewGroup, @Nullable String toolbarTitle) {
        return new ToolbarView(mLayoutInflater, viewGroup, toolbarTitle);
    }

    public InfoDialog getInfoDialog(String title, String body, String btnCaption) {
        return InfoDialog.newInfoDialog(title, body, btnCaption);
    }

    public KeyboardInputDialog getKeyboardInputDialog(){
        return new KeyboardInputDialog(mContext);
    }
}
