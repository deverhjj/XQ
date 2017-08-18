package com.biu.modulebase.binfenjiari.fragment.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.biu.modulebase.binfenjiari.R;

/**
 * Created by jhj_Plus on 2016/6/14.
 */
public class MobileNetworkWarnningFragment extends DialogFragment implements View.OnClickListener {
    public static final String TAG = "MobileNetworkWarnningFragment";
    private Callbacks mCallbacks;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public interface Callbacks {
        void onSelectionFinished(boolean isOk);
    }

    public void setCallbacks(Callbacks callbacks){
        mCallbacks=callbacks;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_mobile_network_warnning,
                null);

        Dialog dialog=new AlertDialog.Builder(getActivity()).setView(view).create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                view.findViewById(R.id.tv_ok).setOnClickListener(MobileNetworkWarnningFragment.this);
                view.findViewById(R.id.tv_cancel).setOnClickListener(MobileNetworkWarnningFragment.this);
            }
        });

        return dialog;
    }

    @Override
    public void onClick(View v) {
        Dialog dialog=getDialog();
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            if (dialog != null) {
                if (mCallbacks != null) {
                    mCallbacks.onSelectionFinished(false);
                }
                dialog.dismiss();
            }

        } else if (i == R.id.tv_ok) {
            if (dialog != null) {
                if (mCallbacks != null) {
                    mCallbacks.onSelectionFinished(true);
                }
                dialog.dismiss();
            }

        } else {
        }
    }
}
