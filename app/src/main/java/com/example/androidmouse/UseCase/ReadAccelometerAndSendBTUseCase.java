package com.example.androidmouse.UseCase;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.androidmouse.BuildConfig;
import com.example.androidmouse.Common.BaseObservable;
import com.example.androidmouse.Common.MouseAction;
import com.example.androidmouse.Common.ThreadWithAbort;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ReadAccelometerAndSendBTUseCase extends BaseObservable<ReadAccelometerAndSendBTUseCase.Listener>
        implements SensorEventListener {

    public interface Listener
    {
        public void onDisabledBluetoothAdapter();
        public void onConnectionFailed(IOException e);
        public void onGetOutputStreamFailed(IOException e);
        public void onBlockingQueuePollFailed(InterruptedException ex);
        public void onOutputStreamWriteFailed(IOException ex);
        public void onBluetoothSockedClosingFailed(IOException e);
    }

    private final UUID      mUseCaseUUID = UUID.fromString("882f88b3-2020-43c5-b02e-75c177202d4c");

    private Object BLUETOOTH_DEVICE_LOCK = new Object();

    private final Sensor            mAccelerometerSensor;
    private final Sensor            mGyroscopeSensor;
    private final SensorManager     mSensorManager;

    private final boolean           isKeyboardShown = false;

    public ReadAccelometerAndSendBTUseCase(
            SensorManager sensorManager,
            Sensor accelerometerSensor,
            Sensor gyroscopeSensor) {

        mAccelerometerSensor = accelerometerSensor;
        mSensorManager = sensorManager;
        mGyroscopeSensor = gyroscopeSensor;
    }

    private volatile BluetoothDevice     mBluetoothDevice;
    private volatile BluetoothSocket     mBluetoothSocket;

    public void beginUseCase() {

        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);

        if (!mCommunicationThread.isAlive()) {
            mCommunicationThread.start();
        }
    }

    public void endUseCase() {
        mSensorManager.unregisterListener(this);
        mCommunicationThread.abort();

        mEndCommunicationThread.start();
    }

    public void setBluetoothDevice(BluetoothDevice device) {
        mBluetoothDevice = device;

        synchronized (BLUETOOTH_DEVICE_LOCK) {
            BLUETOOTH_DEVICE_LOCK.notifyAll();
        }
    }

    private volatile BlockingQueue<MouseAction> mBlockingQueue = new LinkedBlockingQueue<>();

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (isKeyboardShown) {
            if (BuildConfig.DEBUG) {
                Log.d(getClass().getName(), "Keybaord shown on screen; no mouse actions are recorded");
            }
            return;
        }

        switch (event.sensor.getType())
        {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                handleAccelerationEvent(event);
                break;

            case Sensor.TYPE_GYROSCOPE:
                handleGyroscopeEvent(event);
                break;
        }

    }

    private boolean mIsDeviceStationary;

    private final Object SENSOR_LOCK            = new Object();
    private final float GYRO_LOW_PASS_FILTER    = 0.05F;

    private void handleGyroscopeEvent(SensorEvent event) {
        PointF gyroPoint = new PointF(event.values[0], event.values[1]);

        if (BuildConfig.DEBUG) {
            Log.d("SENSOR_CHANGED", gyroPoint.x + " : " + gyroPoint.y );
        }

        if (gyroPoint.x < GYRO_LOW_PASS_FILTER
                && gyroPoint.y < GYRO_LOW_PASS_FILTER) {
            synchronized (SENSOR_LOCK) {
                mIsDeviceStationary = true;
            }
        }
        else if (gyroPoint.x < GYRO_LOW_PASS_FILTER
            && gyroPoint.y > GYRO_LOW_PASS_FILTER) {
            synchronized (SENSOR_LOCK) {
                mIsDeviceStationary = false;
            }
        }
        else if (gyroPoint.x > GYRO_LOW_PASS_FILTER
                && gyroPoint.y < GYRO_LOW_PASS_FILTER) {
            synchronized (SENSOR_LOCK) {
                mIsDeviceStationary = false;
            }
        }
        else if (gyroPoint.x > GYRO_LOW_PASS_FILTER
                && gyroPoint.y > GYRO_LOW_PASS_FILTER) {
            synchronized (SENSOR_LOCK) {
                mIsDeviceStationary = false;
            }
        }

    }

    private float[] mAcceHistoryPoint = new float[]{0,0,0};
    private float[] mDeltaValues = new float[]{0,0,0};
    private final float ACCE_LOW_PASS_FILTER = 0.35f;

    private void handleAccelerationEvent(SensorEvent event) {

        synchronized (SENSOR_LOCK)
        {
            if (!mIsDeviceStationary)
            {
                return;
            }
        }

        MouseAction p = new MouseAction(
                event.values[0],
                event.values[1],
                event.values[2]);

        Boolean movedHorizontalAxis;

        mAcceHistoryPoint[0] = p.x;
        mAcceHistoryPoint[1] = p.y;
        mAcceHistoryPoint[2] = p.z;

        mDeltaValues[0] = p.x - mDeltaValues[0];
        mDeltaValues[1] = p.y - mDeltaValues[1];

        movedHorizontalAxis = Math.abs(mDeltaValues[0]) > Math.abs(mDeltaValues[1]) ? true : false;

        if (BuildConfig.DEBUG) {
            Log.d("SENSOR_CHANGED", p.getJSON());
        }

        if (((p.y < ACCE_LOW_PASS_FILTER && p.y > -ACCE_LOW_PASS_FILTER) && !movedHorizontalAxis)
        || ((p.x < ACCE_LOW_PASS_FILTER && p.x > -ACCE_LOW_PASS_FILTER) && movedHorizontalAxis)) {
            return;
        }

        mBlockingQueue.add(p);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void addMouseActionToQueue(MouseAction mouseAction)
    {
        mBlockingQueue.add(mouseAction);
    }

    private ThreadWithAbort mEndCommunicationThread = new ThreadWithAbort(){

        @Override
        public void run() {
            super.run();

            if (mBluetoothSocket != null
            && mBluetoothSocket.isConnected())
            {
                try {
                    mBluetoothSocket.close();
                } catch (IOException e) {
                    for (Listener listener : getListeners()) {
                        listener.onBluetoothSockedClosingFailed(e);
                    }
                }
            }
        }

    };

    private ThreadWithAbort mCommunicationThread = new ThreadWithAbort(){

        @Override
        public void run() {
            super.run();

            if (BuildConfig.DEBUG) {
                Log.d("UUID: ", mUseCaseUUID.toString());
            }

            if (mBluetoothDevice == null) {
                synchronized (BLUETOOTH_DEVICE_LOCK) {
                    try {
                        BLUETOOTH_DEVICE_LOCK.wait();
                    } catch (InterruptedException e) {
                        if (BuildConfig.DEBUG) {
                            Log.d(getName(), e.getMessage());
                        }
                    }
                }
            }

            try {
                mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(mUseCaseUUID);
                mBluetoothSocket.connect();
            } catch (IOException e) {
                for (Listener listener : getListeners()) {
                    listener.onConnectionFailed(e);
                }
            }

            if (mBluetoothSocket.isConnected())
            {
                OutputStream mOutputStream = null;

                try {
                    mOutputStream = mBluetoothSocket.getOutputStream();
                } catch (IOException e) {
                    for (Listener listener : getListeners()) {
                        listener.onGetOutputStreamFailed(e);
                    }
                }

                while (getIsRunning()
                    && mBluetoothSocket.isConnected())
                {
                    MouseAction mouseAction = null;

                    try {
                        mouseAction = mBlockingQueue.poll(1, TimeUnit.SECONDS);
                    } catch (InterruptedException ex) {
                        for (Listener listener : getListeners()) {
                            listener.onBlockingQueuePollFailed(ex);
                        }
                        continue;
                    }

                    if (!getIsRunning()
                    || !mBluetoothSocket.isConnected())
                    {
                        return;
                    }

                    try {
                        if (mouseAction == null) {
                            continue;
                        }
                        mOutputStream.write(mouseAction.getJSON().getBytes());
                        mOutputStream.flush();
                    } catch (IOException ex) {
                        for (Listener listener : getListeners()) {
                            listener.onOutputStreamWriteFailed(ex);
                        }
                    }
                }
            }
        }
    };

}
