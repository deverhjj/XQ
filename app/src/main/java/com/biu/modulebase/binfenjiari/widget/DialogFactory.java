package com.biu.modulebase.binfenjiari.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.widget.wheeltime.CityMain;
import com.biu.modulebase.binfenjiari.widget.wheeltime.EmotionMain;
import com.biu.modulebase.binfenjiari.widget.wheeltime.OnOkSelectorListener;
import com.biu.modulebase.binfenjiari.widget.wheeltime.WheelMain;

import java.util.List;


public class DialogFactory{

    private static DialogFactory factory;
    private static Activity context;

    private static Dialog dialog;

    private static Dialog loadDialog;

    public static DialogFactory getInstance(Activity context) {
        DialogFactory.context = context;
        if (factory == null) {
            return new DialogFactory();
        }
        return factory;
    }

    public void showLoadDialog(String msg) {
        if (context == null)
            return;
        if (context.isFinishing()) {
            closeLoadDialog();
            return;
        }
        if (loadDialog == null) {
            //	loadDialog = new Dialog(context, R.style.CustomDialog);
            //	loadDialog.setContentView(R.layout.custom_loading_dialog);
            loadDialog.setCancelable(true);
            loadDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                }
            });
        }
        TextView notice_msg = (TextView) loadDialog.findViewById(R.id.text);
        notice_msg.setText(msg);
        try {
            loadDialog.show();
        } catch (Exception e) {
            System.out.println("loading框show方法出错，异常被捕获");
            e.printStackTrace();
        }
    }

    /**
     * 关闭loading框
     */
    public static void closeLoadDialog() {
        if (loadDialog != null) {
            try {
                loadDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("之前的activity被销毁，dialog关闭异常。该报错可以忽略");
            }
            loadDialog = null;
        }
    }

    /**
     * @param layout        layoutRes
     * @param dialogStyle   dialogStyle dialog 样式
     * @param windowAnimStyle         windowAnimStyle window显示动画
     * @param windowGravity window 显示位置 （Gravity.CENTER_VERTICAL...）
     * @param width         宽度百分比（0~1）
     * @param height        高度百分比（0~1）
     * @param listener      子控件点击事件监听
     */
    public static void showDialog(Context context,int layout, int dialogStyle, int windowAnimStyle, int windowGravity, float width, float height, DialogListener listener) {
        View view = LayoutInflater.from(context).inflate(layout, null);

        Dialog dialog = new Dialog(context, dialogStyle);
        listener.OnInitViewListener(view, dialog);
        Window window = dialog.getWindow();
        window.setContentView(view);
        window.setWindowAnimations(windowAnimStyle);
        window.setGravity(windowGravity);
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        if (height != 0)
            p.height = (int) (d.getHeight() * height); // 高度设置为屏幕的0.4
        if (width != 0)
            p.width = (int) (d.getWidth() * width); // 宽度设置为屏幕的0.9
        window.setAttributes(p);
        if(!dialog.isShowing())
            dialog.show();
    }

    public void showPreImgDialog(int layout, int dialogStyle, int windowAnimStyle, int windowGravity, float width,
                                 float height, DialogListener listener) {

        View view = LayoutInflater.from(context.getApplicationContext()).inflate(layout, null);
        dialog = new Dialog(context, dialogStyle);
        Dialog dialog = new Dialog(context, dialogStyle);
        Window window = dialog.getWindow();
        window.setContentView(view);
        window.setWindowAnimations(windowAnimStyle);
        window.setGravity(windowGravity);
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        if (height != 0)
            p.height = (int) (d.getHeight() * height); // 高度设置为屏幕的0.4
        if (width != 0)
            p.width = (int) (d.getWidth() * width); // 宽度设置为屏幕的0.9
        window.setAttributes(p);
        if(!dialog.isShowing())
            dialog.show();
    }

    /**
     * @param wheelType   日期类型
     * @param starttime   最早时间
     * @param endtime     最晚时间
     * @param defaluttime 默认选择时间
     * @param okLinstener 选择时间回调
     */
    public void showWheelTimeDialog(String wheelType, final String starttime,
                                    String endtime, String defaluttime,
                                    final OnOkSelectorListener okLinstener) {
        // 初始化Dialog中的View
//		ScreenInfo screenInfo = new ScreenInfo(context);
        View view = LayoutInflater.from(context).inflate(
                R.layout.wheel_time_layout, null);
        /** 时间控件 */
        final WheelMain wheelMain = new WheelMain(view, wheelType);

        wheelMain.initDateTimePicker(starttime, endtime, defaluttime);// 初始化时间
//		wheelMain.screenheight = screenInfo.getHeight();

        final Dialog dlg = new Dialog(context, R.style.WheelDialog);
        Window window = dlg.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);//
        window.setContentView(view);

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = d.getWidth();
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.popwin_anim_style);
        window.setGravity(Gravity.BOTTOM);
        // 设置窗口的内容页面
        dlg.show();

        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.select_time_cancle);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
        // 关闭alert对话框架
        TextView cancel = (TextView) view.findViewById(R.id.select_time_sure);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                okLinstener.onOkSelector(wheelMain);
                dlg.cancel();

            }
        });
    }

    /**
     * 非级联选择对话框
     * 情感&&性别选择Dialog
     *
     * @param context
     * @param datas       数据源
     * @param okLinstener 确定按钮点击回调
     *
     */
    public static void showEmotionAlert(Context context,List<String> datas,
                                 final OnOkSelectorListener okLinstener) {
        // 初始化Dialog中的View
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.wheel_emotion_layout, null);
        // ScreenInfo screenInfo = new ScreenInfo(this);
        final EmotionMain wheelMain = new EmotionMain(context, view);
        // wheelMain.screenheight = screenInfo.getHeight();
        wheelMain.initCityPicker(datas);

        final Dialog dlg = new Dialog(context, R.style.WheelDialog);
        Window window = dlg.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);//
        window.setContentView(view);

        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = d.getWidth();
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.popwin_anim_style);
        window.setGravity(Gravity.BOTTOM);
        // 设置窗口的内容页面
        dlg.show();

        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.select_time_cancle);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.select_time_sure);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                okLinstener.onOkSelector(wheelMain);
                dlg.cancel();
            }
        });
    }

    /**
     * 城市级联Dialog
     *
     * @param ,jsonArrayString      json格式字符串
     * @param okLinstener 回调
     */
    public static void showCityDialog(String jsonArrayString, CityMain.CITY_TYPE type,
                               final OnOkSelectorListener okLinstener) {
        // 初始化Dialog中的View
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.wheel_city_layout, null);
        final CityMain wheelMain = new CityMain(context, view, type, jsonArrayString);
        wheelMain.initCityPicker();

        final Dialog dlg = new Dialog(context, R.style.WheelDialog);
        Window window = dlg.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);//
        // 设置窗口的内容页面
        window.setContentView(view);

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = d.getWidth();
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.popwin_anim_style);
        window.setGravity(Gravity.BOTTOM);
        // 设置窗口的内容页面
        dlg.show();

        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.select_time_cancle);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.select_time_sure);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                okLinstener.onOkSelector(wheelMain);
                dlg.cancel();
            }
        });
    }

    public static void dissmissDialog() {
        if (dialog != null && dialog.isShowing() && !context.isFinishing()) {
            dialog.dismiss();
            dialog = null;
        }

        context = null;

    }

    public interface DialogListener {
        void OnInitViewListener(View v, Dialog dialog);
    }

    public interface PayDialogListener {
        /**
         * 支付宝支付
         **/
        void OnAliPayClick();

        void OnAliNetPayClick();

        /**
         * 余额支付
         **/
        void OnExtraPayClick();

        void onDialogDissmiss();

    }

}
