package com.example.androidmouse.UI.Common.Views;

import com.example.androidmouse.Common.DependencyInjection.ControllerRootComposition;
import com.example.androidmouse.UI.AndroidMouseApplication;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private ControllerRootComposition mControllerRootComposition;

    public ControllerRootComposition getControllerRootComposition() {
        if (mControllerRootComposition == null)
        {
            mControllerRootComposition = new ControllerRootComposition(
                    this,
                    ((AndroidMouseApplication) getApplication()).getRootComposition());
        }

        return mControllerRootComposition;
    }
}
