package com.biu.modulebase.binfenjiari.fragment.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.biu.modulebase.binfenjiari.activity.MessageListActivity;


/**
 * @author Lee
 * @Title: {清除缓存DialogFragment}
 * @Description:{描述}
 * @date 2016/2/19
 */
public  class ClearMessageAlertDialogFragment extends DialogFragment {

    public static ClearMessageAlertDialogFragment newInstance( int style) {
        ClearMessageAlertDialogFragment frag = new ClearMessageAlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("style", style);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int style =getArguments().getInt("style");
        return new AlertDialog.Builder(getActivity(),style).setTitle("提示")
        .setMessage("确定要清空消息吗？")
       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
               ((MessageListActivity)getActivity()).doPositiveClick();
           }
       }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((MessageListActivity)getActivity()).doNegativeClick();
                    }
                }).show();
    }
}
