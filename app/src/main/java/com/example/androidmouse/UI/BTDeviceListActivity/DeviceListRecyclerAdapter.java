package com.example.androidmouse.UI.BTDeviceListActivity;

import android.bluetooth.BluetoothDevice;
import android.view.ViewGroup;

import com.example.androidmouse.UI.Common.Factories.ViewFactory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class DeviceListRecyclerAdapter extends RecyclerView.Adapter<DeviceListRecyclerAdapter.MyViewHolder>
        implements DeviceListRecyclerItem.Listener {

    public interface Listener {
        public void onDeviceClicked(BluetoothDevice device);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final DeviceListRecyclerItem viewHolder;

        public MyViewHolder(@NonNull DeviceListRecyclerItem recyclerItem) {
            super(recyclerItem.getRootView());
            viewHolder = recyclerItem;
        }
    }

    private final Listener mListener;
    private final ViewFactory mViewFactory;

    private List<BluetoothDevice> deviceList;

    public DeviceListRecyclerAdapter(Listener mListener, ViewFactory mViewFactory) {
        this.mListener = mListener;
        this.mViewFactory = mViewFactory;
    }

    public void bindDevices(ArrayList<BluetoothDevice> list){
        this.deviceList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DeviceListRecyclerItem viewHolderItem = mViewFactory.getDeviceListRecyclerItem(parent);
        viewHolderItem.registerListener(this);

        return new MyViewHolder(viewHolderItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.viewHolder.bindItem(deviceList.get(position));
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    @Override
    public void onItemClicked(BluetoothDevice device) {
        mListener.onDeviceClicked(device);
    }

}
