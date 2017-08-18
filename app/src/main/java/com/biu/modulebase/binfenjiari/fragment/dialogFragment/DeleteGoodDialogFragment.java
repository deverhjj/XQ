package com.biu.modulebase.binfenjiari.fragment.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author Lee
 * @Title: {删除购物侧商品DialogFragment}
 * @Description:{描述}
 * @date 2016/2/19
 */
public  class DeleteGoodDialogFragment extends DialogFragment {

    public static DeleteGoodDialogFragment newInstance(String title,String content,int style) {
        DeleteGoodDialogFragment frag = new DeleteGoodDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);
        args.putInt("style", style);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String content =getArguments().getString("content");
        int style =getArguments().getInt("style");
        return new AlertDialog.Builder(getActivity(),style).setTitle(title)
        .setMessage(content)
       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
//               ((ShoppingCartActivity)getActivity()).doPositiveClick();
           }
       }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        ((ShoppingCartActivity)getActivity()).doNegativeClick();
                    }
                }).show();
    }
}
