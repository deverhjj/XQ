package com.binfenjiari.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.binfenjiari.R;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/4/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class PostLoadingDialog extends DialogFragment implements DialogInterface.OnKeyListener {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog loadingDialog =
                new AlertDialog.Builder(getContext(), R.style.Loading_Post_Dialog).setView(
                        R.layout.part_loading_post).create();
        setCancelable(false);
        loadingDialog.setOnKeyListener(this);
        return loadingDialog;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Fragment parent = getParentFragment();
            if (parent instanceof OnBackPressedListener) {
                OnBackPressedListener listener = (OnBackPressedListener) parent;
                listener.onBackPressed();
                return true;
            }
        }
        return false;
    }

    public interface OnBackPressedListener {
        void onBackPressed();
    }

}
