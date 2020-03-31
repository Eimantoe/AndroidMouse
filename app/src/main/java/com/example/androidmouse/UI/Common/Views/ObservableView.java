package com.example.androidmouse.UI.Common.Views;

import java.util.Set;

public interface ObservableView<LISTENER_TYPE> extends MVCView{
    void registerListener(LISTENER_TYPE listener);
    void unregisterListener(LISTENER_TYPE listener);
    Set<LISTENER_TYPE> getListeners();
}
