package com.example.androidmouse.UI.Common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidmouse.R;
import com.example.androidmouse.UI.Common.Views.ObservableView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class KeyboardInputDialog extends ObservableDialog<KeyboardInputDialog.Listener> implements TextWatcher {

    public interface Listener {
        public void onCharacterAdded(CharSequence s, int start, int before, int count);
    }

    private EditText mEditText;

    public KeyboardInputDialog(@NonNull Context context) {
        super(context);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.dialog_keyboard_input);

        mEditText = findViewById(R.id.edit_keyboard_input);
        mEditText.addTextChangedListener(this);
    }

    public EditText getEditText() {
        return mEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        for (Listener listener : getListeners()) {
            listener.onCharacterAdded(s, start, before, count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
