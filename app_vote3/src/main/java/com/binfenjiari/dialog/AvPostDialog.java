package com.binfenjiari.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.binfenjiari.R;
import com.binfenjiari.base.BaseDialog;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/17
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class AvPostDialog extends BaseDialog {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new AlertDialog.Builder(getContext(), R.style.BTTDialog).setView(
                R.layout.dialog_post_av).create();
        dialog.setOnShowListener(this);
        Window w = dialog.getWindow();
        if (w != null) {
            w.setGravity(Gravity.BOTTOM);
        }
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialog) {
        super.onShow(dialog);
        registerClickEvent(R.id.cancel, R.id.video, R.id.audio, R.id.it);
    }

}
