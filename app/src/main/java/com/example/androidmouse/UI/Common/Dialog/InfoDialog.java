package com.example.androidmouse.UI.Common.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.androidmouse.Common.Constants;
import com.example.androidmouse.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

public class InfoDialog extends BaseDialog implements View.OnClickListener {

    public static InfoDialog newInfoDialog(String title, String message, String buttonCaption) {
        InfoDialog infoDialog = new InfoDialog();
        Bundle bundle = new Bundle(3);
        bundle.putString(Constants.DIALOG_TITLE_KEY, title);
        bundle.putString(Constants.DIALOG_MESSAGE_KEY, message);
        bundle.putString(Constants.DIALOG_BUTTON_CAPTION_KEY, buttonCaption);
        infoDialog.setArguments(bundle);
        return infoDialog;
    }

    private TextView mTxtTitle;
    private TextView mTxtMessage;
    private AppCompatButton mBtnPositive;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_info);

        mTxtTitle = dialog.findViewById(R.id.txt_title);
        mTxtTitle.setText(bundle.getString(Constants.DIALOG_TITLE_KEY));

        mTxtMessage = dialog.findViewById(R.id.txt_title);
        mTxtMessage.setText(bundle.getString(Constants.DIALOG_MESSAGE_KEY));

        mBtnPositive = dialog.findViewById(R.id.btn_positive);
        mBtnPositive.setText(bundle.getString(Constants.DIALOG_BUTTON_CAPTION_KEY));
        mBtnPositive.setOnClickListener(this);

        return dialog;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
