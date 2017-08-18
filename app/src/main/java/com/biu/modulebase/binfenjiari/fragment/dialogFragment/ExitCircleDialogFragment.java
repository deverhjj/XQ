package com.biu.modulebase.binfenjiari.fragment.dialogFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.common.base.BaseFragment;
import com.biu.modulebase.binfenjiari.fragment.CircleItemFragment;
import com.biu.modulebase.binfenjiari.model.CircleVO;
import com.biu.modulebase.binfenjiari.util.OtherUtil;


/**
 * @author Lee
 * @Title: {退出圈子Dialog}
 * @Description:{描述}
 * @date 2016/2/19
 */
public  class ExitCircleDialogFragment extends DialogFragment  implements View.OnClickListener {

    int mNum;
    private int position;
    private String id;
    CircleVO circelVO;
   public static ExitCircleDialogFragment newInstance(int num,int position,String id) {
        ExitCircleDialogFragment f = new ExitCircleDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putInt("position", position);
        args.putString("id", id);

//        args.putSerializable("bean",circelVO);
//        args.putString("circleId",circleId);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mNum = bundle.getInt("num");
        position =bundle.getInt("position");
        id =bundle.getString("id");
//        circelVO = (CircleVO) bundle.getSerializable("bean");
        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((mNum-1)%6) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        switch ((mNum-1)%6) {
            case 4: theme = android.R.style.Theme_Holo; break;
            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;
        }
        setStyle(style, theme);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ok_btn) {
            final BaseFragment fragment = (BaseFragment) getTargetFragment();
            OtherUtil.joinCircle(fragment, id, new OtherUtil.JoinCircleCallback() {
                @Override
                public void onFinish(int key) {
                    CircleItemFragment.refresh = true;
                    ExitCircleSuccessDialogFragment shareDialog = ExitCircleSuccessDialogFragment.newInstance(ShareDialogFragment.STYLE_NO_TITLE);
                    shareDialog.show(getActivity().getSupportFragmentManager(), "exit_circle_success");
                    Intent intent = getActivity().getIntent();
                    intent.putExtra("position", position);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    dismiss();

                }
            });

        } else if (i == R.id.cancel_btn) {
            dismiss();

        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.pop_exit_circle);
        dialog.findViewById(R.id.main).setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.shake));
        dialog.findViewById(R.id.ok_btn).setOnClickListener(this);
        dialog.findViewById(R.id.cancel_btn).setOnClickListener(this);
        Window window =dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        //5.0以下的theme中windowbackground不是透明色  会有边框
        window.setBackgroundDrawableResource(R.drawable.bg_transparent_guide);
        setCancelable(true);
        window.getAttributes().windowAnimations =R.style.dialog_middle_anim_style;
        WindowManager m = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
//            p.height = (int) (d.getHeight() * 0.75); // 高度设置为屏幕的0.4
        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.9
        window.setAttributes(p);
        return dialog;
    }

    public interface ExitCircleCallBack{
        void onSuccess();
    }

}
