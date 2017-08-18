package com.biu.modulebase.binfenjiari.fragment.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.biu.modulebase.binfenjiari.R;

/**
 * Created by jhj_Plus on 2016/6/6.
 */
public class CommentDeleteFragment extends DialogFragment implements View.OnClickListener {
    public static final String TAG = "CommentDeleteFragment";

    private Callbacks mCallbacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public interface Callbacks {
        void onSelectionFinished(boolean isOk);
    }


    public void setCallbacks(Callbacks callbacks)
    {
        mCallbacks = callbacks;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_comment_delete,
                null);

        Dialog dialog=new AlertDialog.Builder(getActivity()).setView(view).create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                view.findViewById(R.id.tv_ok).setOnClickListener(CommentDeleteFragment.this);
                view.findViewById(R.id.tv_cancel).setOnClickListener(CommentDeleteFragment.this);
            }
        });

        return dialog;
    }

    @Override
    public void onClick(View v) {
        Dialog dialog = getDialog();
        if (mCallbacks != null) {
            mCallbacks.onSelectionFinished(v.getId() == R.id.tv_ok);
        }
        dialog.dismiss();
    }
}
