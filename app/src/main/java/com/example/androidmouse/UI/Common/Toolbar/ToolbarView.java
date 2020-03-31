package com.example.androidmouse.UI.Common.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidmouse.R;
import com.example.androidmouse.UI.Common.Views.BaseObservableView;

import androidx.annotation.Nullable;

public class ToolbarView extends BaseObservableView implements View.OnClickListener {

    public interface Listener {
        public void onBackPressed();
        public void onKeyboardPressed();
    }

    private final TextView mTextView;
    private final ImageButton mBackButton;
    private final ImageButton mKeyboardButton;

    public ToolbarView(LayoutInflater layoutInflater, @Nullable ViewGroup parent, @Nullable String toolbarTitle) {
        setRootView(layoutInflater.inflate(R.layout.layout_toolbar, parent, false));

        mTextView = findViewById(R.id.txt_toolbar_title);
        mTextView.setText(toolbarTitle);

        mBackButton = findViewById(R.id.btn_back);
        mBackButton.setOnClickListener(this);

        mKeyboardButton = findViewById(R.id.btn_keyboard);
        mKeyboardButton.setOnClickListener(this);
    }

    private Listener mListener;

    public void enableToolbar(Listener listener, int backButtonVisibility, int keyboardButtonVisibility) {
        mListener = listener;
        mBackButton.setVisibility(backButtonVisibility);
        mKeyboardButton.setVisibility(keyboardButtonVisibility);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mBackButton.getId()) {
            mListener.onBackPressed();
        } else if (v.getId() == mKeyboardButton.getId()) {
            mListener.onKeyboardPressed();
        }
    }
}
