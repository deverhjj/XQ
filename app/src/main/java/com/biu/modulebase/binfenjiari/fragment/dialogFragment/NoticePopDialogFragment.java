package com.biu.modulebase.binfenjiari.fragment.dialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.model.AnnounceVO;
import com.biu.modulebase.binfenjiari.util.OtherUtil;


/**
 * @author Lee
 * @Title: {公告Dialog}
 * @Description:{描述}
 * @date 2016/2/19
 */
public  class NoticePopDialogFragment extends DialogFragment  implements View.OnClickListener {

    int mNum;

    private AnnounceVO bean;

   public static NoticePopDialogFragment newInstance(int num, AnnounceVO bean) {
        NoticePopDialogFragment f = new NoticePopDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putSerializable("bean",bean);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");
        bean = (AnnounceVO) getArguments().getSerializable("bean");
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
        if (i == R.id.close) {
            dismiss();

        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        Window window =dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        //5.0以下的theme中windowbackground不是透明色  会有边框
        window.setBackgroundDrawableResource(R.drawable.bg_transparent_guide);
        setCancelable(false);
        window.getAttributes().windowAnimations =R.style.dialog_middle_anim_style;
        dialog.setContentView(R.layout.pop_notice);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView content = (TextView) dialog.findViewById(R.id.content);
        title.setText(OtherUtil.filterSensitives(getActivity(),bean.getTitle()));
        content.setText(OtherUtil.filterSensitives(getActivity(),bean.getContent()));
        dialog.findViewById(R.id.close).setOnClickListener(this);
        WindowManager m = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
//            p.height = (int) (d.getHeight() * 0.75); // 高度设置为屏幕的0.4
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.9
        window.setAttributes(p);
        return dialog;
    }

}
