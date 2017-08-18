package com.binfenjiari.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.binfenjiari.utils.Views;


/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/6/15
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class BaseDialog extends DialogFragment
        implements DialogInterface.OnShowListener, View.OnClickListener
{
    private OnClickListener mClickListener;
    private OnShowListener mShowListener;

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setOnShowListener(OnShowListener showListener) {
        mShowListener = showListener;
    }

    public void registerClickEvent(int... viewIds) {
        Dialog d = getDialog();
        if (d == null) return;
        for (int id : viewIds) {
            Views.find(d, id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (mClickListener != null) {
            mClickListener.onClick(getDialog(), v);
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (mShowListener != null) {
            mShowListener.onShow(dialog);
        }
    }

    public interface OnClickListener {
        void onClick(DialogInterface dialog, View v);
    }

    public interface OnShowListener {
        void onShow(DialogInterface dialog);
    }

}
