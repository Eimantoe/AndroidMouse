package com.example.androidmouse.UI.Common.Views;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseObservableView<LISTENER_TYPE> extends BaseView
    implements ObservableView<LISTENER_TYPE>{

    private Set<LISTENER_TYPE> mListeners = new HashSet<>();

    @Override
    public void registerListener(LISTENER_TYPE listener) {
        mListeners.add(listener);
    }

    @Override
    public void unregisterListener(LISTENER_TYPE listener) {
        mListeners.remove(listener);
    }

    @Override
    public Set<LISTENER_TYPE> getListeners() {
        return Collections.unmodifiableSet(mListeners);
    }
}
