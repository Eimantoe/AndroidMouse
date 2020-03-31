package com.example.androidmouse.UI.MainActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.example.androidmouse.BuildConfig;
import com.example.androidmouse.Common.BaseObservable;
import com.example.androidmouse.Common.MouseAction;
import com.example.androidmouse.UI.Common.Dialog.KeyboardInputDialog;
import com.example.androidmouse.UI.Common.Factories.ViewFactory;
import com.example.androidmouse.UseCase.FetchBondedBTDevicesUseCase;
import com.example.androidmouse.UseCase.ReadAccelometerAndSendBTUseCase;

import java.io.IOException;
import java.util.Enumeration;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivityController extends BaseObservable<MainActivityController.Listener>
        implements ReadAccelometerAndSendBTUseCase.Listener, MainActivityView.Listener, KeyboardInputDialog.Listener {

    public interface Listener {
        public void onBackButtonPressed();
    }

    private final ReadAccelometerAndSendBTUseCase mReadAccelometerAndSendBTUseCase;
    private final FetchBondedBTDevicesUseCase mFetchBondedBTDevicesUseCase;
    private final InputMethodManager mInputMethodManager;
    private final ViewFactory mViewFactory;

    private MainActivityView mMainActivityView;
    private BluetoothDevice mBluetoothDevice;

    private final Object LOCK = new Object();

    private enum mStates {
            idle,
            keyboard_input_shown;
    }

    private mStates mControllerState = mStates.idle;

    public MainActivityController(ReadAccelometerAndSendBTUseCase readAccelometerAndSendBTUseCase,
                                  FetchBondedBTDevicesUseCase fetchBondedBTDevicesUseCase,
                                  InputMethodManager inputMethodManager,
                                  ViewFactory viewFactory) {
        mReadAccelometerAndSendBTUseCase = readAccelometerAndSendBTUseCase;
        mFetchBondedBTDevicesUseCase = fetchBondedBTDevicesUseCase;
        mInputMethodManager = inputMethodManager;
        mViewFactory = viewFactory;
    }

    public void bindView(MainActivityView view)
    {
        mMainActivityView = view;
        mMainActivityView.registerListener(this);
    }

    public void bindIntent(Intent intent) {
        String deviceAddr = intent.getStringExtra(MainActivity.DEVICE_ADDRESS);

        BluetoothDevice device = mFetchBondedBTDevicesUseCase.fetchDeviceByAddress(deviceAddr);

        if (device != null) {
            setBluetoothDeviceAndBeginUseCase(device);
        } else {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "MainActivityController.bindIntent() failed to retrieve BT device");
            }

            // Returning to previous activity
            for (Listener listener : getListeners())
            {
                listener.onBackButtonPressed();
            }
        }
    }

    public void onStart() {
        mReadAccelometerAndSendBTUseCase.registerListener(this);
        mReadAccelometerAndSendBTUseCase.beginUseCase();
    }

    public void onStop() {
        mReadAccelometerAndSendBTUseCase.endUseCase();
    }

    public void setBluetoothDeviceAndBeginUseCase(BluetoothDevice device)
    {
        mBluetoothDevice = device;
        mReadAccelometerAndSendBTUseCase.setBluetoothDevice(mBluetoothDevice);
    }

    public void onTouchEvent(int action) {
        switch (action) {
            case (MouseAction.ACTION_UP):
                mReadAccelometerAndSendBTUseCase.addMouseActionToQueue(new MouseAction(MouseAction.ACTION_UP));
                break;
            case (MouseAction.ACTION_DOWN):
                mReadAccelometerAndSendBTUseCase.addMouseActionToQueue(new MouseAction(MouseAction.ACTION_DOWN));
                break;
            case (MouseAction.ACTION_L_CLICK):
                synchronized (LOCK) {
                    mReadAccelometerAndSendBTUseCase.addMouseActionToQueue(new MouseAction(MouseAction.ACTION_L_CLICK));
                }
                break;
            case (MouseAction.ACTION_R_CLICK):
                synchronized (LOCK) {
                    mReadAccelometerAndSendBTUseCase.addMouseActionToQueue(new MouseAction((MouseAction.ACTION_R_CLICK)));
                }
                break;
            case (MouseAction.ACTION_L_RELEASE):
                synchronized (LOCK) {
                    mReadAccelometerAndSendBTUseCase.addMouseActionToQueue(new MouseAction((MouseAction.ACTION_L_RELEASE)));
                }
            default:
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "MainActivityController QueuePoster unexpected action");
                }
        }
    }

    private void notifyBackButtonPressedAndPrintErrorMsg(Exception e) {
        for (Listener listener : getListeners()) {
            listener.onBackButtonPressed();
        }

        if (BuildConfig.DEBUG) {
            Log.d(getClass().getName(), e.getMessage());
        }
    }

    @Override
    public void onBackButtonPressed() {
        for (Listener listener: getListeners())
        {
            listener.onBackButtonPressed();
        }
    }

    private KeyboardInputDialog mKeyboardInputDialog;
    @Override
    public void onKeyboardPressed() {
        if (mControllerState == mStates.idle) {
            mKeyboardInputDialog = mViewFactory.getKeyboardInputDialog();
            mKeyboardInputDialog.registerListener(this);
            mKeyboardInputDialog.show();

            mInputMethodManager.showSoftInput(mKeyboardInputDialog.getEditText(), InputMethodManager.SHOW_IMPLICIT);
            mControllerState = mStates.keyboard_input_shown;
        } else if (mControllerState == mStates.keyboard_input_shown) {
            mInputMethodManager.hideSoftInputFromWindow(mKeyboardInputDialog.getEditText().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            mControllerState = mStates.idle;
        }
    }

    @Override
    public void onCharacterAdded(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void onDisabledBluetoothAdapter() {
    }

    @Override
    public void onConnectionFailed(IOException e) {
        for (Listener listener : getListeners()) {
            listener.onBackButtonPressed();
        }
    }

    @Override
    public void onGetOutputStreamFailed(IOException e) {
        notifyBackButtonPressedAndPrintErrorMsg(e);
    }

    @Override
    public void onBlockingQueuePollFailed(InterruptedException ex) {
        notifyBackButtonPressedAndPrintErrorMsg(ex);
    }

    @Override
    public void onOutputStreamWriteFailed(IOException ex) {
        notifyBackButtonPressedAndPrintErrorMsg(ex);
    }

    @Override
    public void onBluetoothSockedClosingFailed(IOException e) {
        notifyBackButtonPressedAndPrintErrorMsg(e);
    }
}
