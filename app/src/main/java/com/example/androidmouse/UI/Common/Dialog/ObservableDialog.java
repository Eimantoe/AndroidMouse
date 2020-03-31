package com.example.androidmouse.UI.Common.Dialog;

import android.app.Dialog;
import android.content.Context;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class ObservableDialog<LISTENER_TYPE> extends Dialog {

    private Set<LISTENER_TYPE> mListeners = new HashSet<>();

    public ObservableDialog(@NonNull Context context) {
        super(context);
    }

    public void registerListener(LISTENER_TYPE listener) {
        mListeners.add(listener);
    }

    public void unregisterListener(LISTENER_TYPE listener) {
        mListeners.remove(listener);
    }

    public Set<LISTENER_TYPE> getListeners() {
        return Collections.unmodifiableSet(mListeners);
    }

}
