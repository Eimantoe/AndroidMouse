package com.example.androidmouse.UI.Common.Dialog;

import com.example.androidmouse.Common.DependencyInjection.ControllerRootComposition;
import com.example.androidmouse.UI.AndroidMouseApplication;
import androidx.fragment.app.DialogFragment;

public abstract class BaseDialog extends DialogFragment {

    private ControllerRootComposition mControllerRootComposition;

    protected ControllerRootComposition getControllerRootComposition() {
        if (mControllerRootComposition == null) {
            mControllerRootComposition = new ControllerRootComposition(
                    requireActivity(),
                    ((AndroidMouseApplication) requireActivity().getApplication()).getRootComposition()
            );
        }

        return mControllerRootComposition;
    }
}
