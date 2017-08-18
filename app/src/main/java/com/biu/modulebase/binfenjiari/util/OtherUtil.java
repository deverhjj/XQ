package com.biu.modulebase.binfenjiari.util;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.model.UserInfoBean;
import com.biu.modulebase.common.base.BaseActivity;
import com.biu.modulebase.binfenjiari.activity.ReportActivity;
import com.biu.modulebase.common.adapter.BaseAdapter;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack2;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.common.base.BaseFragment;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.MobileNetworkWarnningFragment;
import com.biu.modulebase.binfenjiari.fragment.dialogFragment.ShareDialogFragment;
import com.biu.modulebase.binfenjiari.model.CardVO;
import com.biu.modulebase.binfenjiari.model.PostHeaderParentItem;
import com.biu.modulebase.binfenjiari.model.Region;
import com.biu.modulebase.binfenjiari.model.SensitiveWordVO;
import com.biu.modulebase.binfenjiari.model.ShareInfoVO;
import com.biu.modulebase.binfenjiari.other.umeng.UmengSocialUtil;
import com.biu.modulebase.binfenjiari.widget.DialogFactory;
import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.adapter.ExpandableRecyclerViewAdapter;
import com.biu.modulebase.binfenjiari.widget.media.MediaController;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/2/23.
 */
public class OtherUtil {
    private static final String TAG = "OtherUtil";

    private static String getVerifyStatus(int code) {
        String status = "";
        switch (code) {
            case 1001:
                status = "手机格式不正确";
                break;
            case 1012:
                status = "一分钟内已发送过短信";
                break;
            case 1013:
                status = "短信已发送";
                break;
            case 1003:
                status = "手机号已注册";
                break;
            case 1014:
                status = "手机号未注册";
                break;
            default:
                break;
        }
        return status;
    }

    private static void countdown(final TextView view, final int timeSeconds) {
        final MsgHandler msgHandler = new MsgHandler(view);
        Thread thread = new Thread() {
            @Override
            public void run() {
                int curSecond = timeSeconds;
                while (curSecond > 0) {
                    try {
                        curSecond--;
                        Thread.sleep(1000);
                        Message message = msgHandler.obtainMessage();
                        message.arg1 = curSecond;
                        msgHandler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }




    private static class MsgHandler extends Handler {
        private TextView msgView;
        private static boolean mCountdowning = false;
        public MsgHandler(TextView msgView) {
            this.msgView = msgView;
        }

        @Override
        public void handleMessage(Message msg) {
            int curSecond = msg.arg1;
            msgView.setText(curSecond == 0 ? "获取验证码" : "验证码 " + curSecond + " 秒后失效");
            mCountdowning=curSecond!=0;
        }
    }

//    public static void sendVerifiCodeMsg(final Context context, String phone, String type,
//            final TextView view,final int timeSeconds,
//            final String tag)
//    {
//        boolean isPhoneCorrect = !TextUtils.isEmpty(phone) && Utils.isMobileNO(phone);
//        boolean countdowning = MsgHandler.mCountdowning;
//        if (!isPhoneCorrect || countdowning) {
//            showToast(context, !isPhoneCorrect ? "手机号码错误" : "一分钟内已发送过短信");
//            return;
//        }
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("mobile", phone);
//        params.put("type", type);
//
//        Communications.stringRequestData(false, false,null, params, Constant.URL_VERIFICATION_CODE,
//                Request.Method.POST, tag, new RequestCallBack() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e(tag, "json text------>" + response.toString());
//                        try {
//                            int status=response.getInt("key");
//                            if (status==1013) {
//                                LogUtil.LogI(TAG,"***********status==1013************");
//                               countdown(view,timeSeconds);
//                            }
//                            showToast(context, getVerifyStatus(response.getInt("key")));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onErrorResponse(String errorInfo) {
//                        Log.e(tag, "onErrorResponse------>" + errorInfo);
//                        showToast(context, errorInfo);
//                    }
//
//                    @Override
//                    public void onUnLogin() {
//                        //Log.e(TAG, "onErrorResponse------>" +errorInfo);
//                    }
//                });
//    }

    public static void showToast(Context context, CharSequence msg) {
        if (context != null && !TextUtils.isEmpty(msg)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(Context context, int msgId) {
        if (context != null) {
            Toast.makeText(context, context.getString(msgId), Toast.LENGTH_SHORT).show();
        }
    }

    public interface onDialogStateChangeListener {
        void onDialogStateChanged(int which);
    }

//    public static void showJoinOrExitDialog(Context context, final boolean join,final
//    onDialogStateChangeListener
//            listener) {
//        DialogFactory.showDialog(context, R.layout.pop_join_circle, R.style.CustomDialog, R.style
//                .dialog_middle_anim_style, Gravity.CENTER, 0.8f, 0, new DialogFactory.DialogListener() {
//            @Override
//            public void OnInitViewListener(View v, final Dialog dialog) {
//                ((TextView) v.findViewById(R.id.tv_title)).setText(join ? "确定加入该圈子" : "确定退出该圈子");
//                final View cancel = v.findViewById(android.R.id.button2);
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        listener.onDialogStateChanged(DialogInterface.BUTTON_NEGATIVE);
//                    }
//                });
//                final View ok=v.findViewById(android.R.id.button1);
//                ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        listener.onDialogStateChanged(DialogInterface.BUTTON_POSITIVE);
//                    }
//                });
//            }
//        });
//    }

//    public static void joinOrExitCircle(final BaseFragment context, final int status,
//            final int circleId, final boolean join)
//    {
//        //未登录
//        if (status == 3) {
//            context.showUnloginSnackbar();
//            return;
//        }
//        showJoinOrExitDialog(context.getActivity(), join, new onDialogStateChangeListener() {
//            @Override
//            public void onDialogStateChanged(int which) {
//                if (which ==
//                        DialogInterface.BUTTON_POSITIVE)
//                {
//                    OtherUtil.joinOrExitCircle(
//                            context, circleId,
//                            !joined,
//                            new OtherUtil
//                                    .OnOperationFinishListener() {
//                                @Override
//                                public void
//                                onOperationFinished(
//                                        boolean success)
//                                {
//                                    header.setStatus(
//                                            joined !=
//                                                    success
//                                                    ? 1
//                                                    : 2);
//                                }
//                            });
//                }
//            }
//        });
//    }

//    public interface OnOperationFinishListener {
//        void onOperationFinished(boolean success);
//    }
//    public static  void joinOrExitCircle(final Context context, final int circleId,
//            final boolean join, final OnOperationFinishListener listener)
//    {
//        String token = PreferencesUtils.getString(context, PreferencesUtils.KEY_TOKEN);
//        String url = Constant.URL_CIRCLE_JOIN_EXIT + circleId;
//        Communications.stringRequestData(true, false, token,
//                new HashMap<String, Object>(), url, Request.Method.POST, TAG,
//                new RequestCallBack() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        LogUtil.LogE(TAG, "response--->" + response.toString());
//                        int statusCode = JSONUtil.getInt(response, "key");
//                        if (statusCode == 1 || statusCode == 2) {
//                            OtherUtil.showToast(context, join ? "加入成功" : "退出成功");
//                            listener.onOperationFinished(true);
//                        } else {
//                            OtherUtil.showToast(context, "操作失败,请重试");
//                            listener.onOperationFinished(false);
//                        }
//                    }
//
//                    @Override
//                    public void onErrorResponse(String errorInfo) {
//                        LogUtil.LogE(TAG, "onErrorResponse--->" + errorInfo);
//                        listener.onOperationFinished(false);
//                    }
//
//                    @Override
//                    public void onUnLogin() {
//                        LogUtil.LogE(TAG, "onUnLogin--->");
//                        listener.onOperationFinished(false);
//                    }
//                });
//    }

    public interface onTakePictureFinishListener {
        void onTakePictureFinished(Uri photoUri);
    }

    public static void showPhotoPop(final Fragment fragment, final boolean singleChoice,
            final onTakePictureFinishListener listener) {
        DialogFactory.showDialog(fragment.getActivity(), R.layout.pop_take_photo,
                R.style.WheelDialog, R.style.popwin_anim_style, Gravity.BOTTOM, 0.9f, 0,
                new DialogFactory.DialogListener() {
                    @Override
                    public void OnInitViewListener(View v, final Dialog dialog) {
                        v.findViewById(R.id.take_photo).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Uri photoUri = ImageUtils.takePhoto(fragment.getActivity(), dialog);
                                        if (listener != null) {
                                            listener.onTakePictureFinished(photoUri);
                                        }
                                    }
                                });
                        // 从相册选取照片
                        v.findViewById(R.id.choice_photo).setOnClickListener(
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        ImageUtils.selectPicture(fragment.getActivity(), dialog, singleChoice);
                                    }
                                });
                        v.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }
                });
    }

//    public static void showReplyWindow(Context context,
//            DialogFactory.DialogListener listener)
//    {
//        DialogFactory.showDialog(context, R.layout.pop_reply, R.style.Theme_Dialog_Reply,
//                R.style.popwin_anim_style, Gravity.BOTTOM, 1.0f, 0, listener);
//
//    }



    //---------------------------------弹窗菜单 开始--------------------------------------

    static class Callback implements AdapterView.OnItemClickListener {
        private AdapterView.OnItemClickListener outerListener;
        private ListPopupWindow mPopupWindow;

        public Callback(AdapterView.OnItemClickListener outerListener,
                ListPopupWindow popupWindow)
        {
            this.outerListener = outerListener;
            mPopupWindow = popupWindow;

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (outerListener != null) {
                outerListener.onItemClick(parent, view, position, id);
                mPopupWindow.dismiss();
            }
        }
    }

    private static class PopupWinThread extends Thread {
        ListPopupWindow mPopupWindow;
        Context mContext;
        int mCheckedPos;
        public PopupWinThread(Context context,ListPopupWindow popupWindow,int checkedPos) {
            mPopupWindow = popupWindow;
            mContext=context;
            mCheckedPos=checkedPos;
        }
        @Override
        public void run() {
            while (!mPopupWindow.isShowing()) {
                LogUtil.LogI(TAG, "not Showing");
            }
            LogUtil.LogI(TAG, "Showing");
            setUpListView(mContext,mPopupWindow,mCheckedPos);
        }
    }

    public static void showPopupWin(Context context, View anchor, ListAdapter adapter, int width,
            int height, int checkedPos, AdapterView.OnItemClickListener listener)
    {
        ListPopupWindow listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setWidth(Utils.getScreenWidth((Activity) context));
        listPopupWindow.setHeight(height);
        listPopupWindow.setAnchorView(anchor);
        listPopupWindow.setModal(true);
        listPopupWindow.setAdapter(adapter);
//        listPopupWindow.setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        if (listener != null) {
            listPopupWindow.setOnItemClickListener(new Callback(listener, listPopupWindow));
        }
//        listPopupWindow.setListSelector(new ColorDrawable(context.getResources().getColor(R.color
//                .app_text_color_secondary)));
        listPopupWindow.show();
        if (listPopupWindow.isShowing()) {
            LogUtil.LogI(TAG,"showing#1");
            setUpListView(context, listPopupWindow,checkedPos);
        } else {
            new PopupWinThread(context,listPopupWindow,checkedPos).start();
        }
    }

    private static void setUpListView(Context context, ListPopupWindow listPopupWindow,int
            checkedPos) {
        ListView lv = listPopupWindow.getListView();
        if (lv!=null) {
            lv.setBackgroundColor(context.getResources().getColor(R.color.white));
           lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
           lv.setDivider(new ColorDrawable(context.getResources().getColor(R.color
                                  .colorDividerLight)));
            lv.setDividerHeight(
                    context.getResources().getDimensionPixelSize(R.dimen.height_0_5dp));
            if (checkedPos!=-1) {
                lv.setItemChecked(checkedPos,true);
            }
        }
    }


    //---------------------------------弹窗菜单 结束---------------------------------------


    //---------------------------------区域弹窗菜单 开始---------------------------------------

    public interface DataRequest{
        void onFinished(Object data);
    }
    public static void doGetRegionData(BaseFragment context, String tag, final DataRequest callback) {
        JSONObject params=OtherUtil.getJSONObject(context.getActivity(),Constant.MODEL_COMMON,
                Constant.ACTION_GET_AREA_LIST,false);
        context.dataRequest(false, params , Constant.SERVERURL, tag,
                new RequestCallBack2() {

                    @Override
                    public void requestBefore() {

                    }

                    @Override
                    public void onSuccess(String mainJsonString, JSONObject mainJsonObject,
                            JSONObject rootJsonObject)
                    {
                        Region region = JSONUtil.fromJson(rootJsonObject.toString(), Region.class);
                        if (callback!=null) {
                            callback.onFinished(region);
                        }
                    }

                    @Override
                    public void onFail(int key, String message) {

                    }

                    @Override
                    public void requestAfter() {

                    }
                });
    }

    public interface FinishListener{
        void onFinished(String id);
    }
    private static int sCheckedPos = -1;
    private static String sTag;

    /**
     * @param context
     * @param anchor
     * @param tag
     * @param regionItems
     * @param listener
     */
    public static void popRegionMenuWindow(Context context, View anchor, String tag, final Region
            .RegionItem[] regionItems, final FinishListener listener) {

        if (!tag.equals(sTag)) {
            sCheckedPos = -1;
            sTag = tag;
        }

        if (regionItems==null) {
            OtherUtil.showToast(context,"区域数据获取失败");
            return;
        }

        String[] regionNames = new String[regionItems.length + 1];
        regionNames[0]="全部";
        for (int i = 0; i < regionItems.length; i++) {
            regionNames[i + 1] = regionItems[i].getName();
        }

        ArrayAdapter adapter=new ArrayAdapter(context,R.layout.item_menu2,regionNames);

        showPopupWin(context, anchor, adapter, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                sCheckedPos, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id)
                    {
                        sCheckedPos=position;
                        if (listener!=null) {
                            Region.RegionItem regionItem =
                                    position == 0 ? null : regionItems[position - 1];
                            listener.onFinished(position==0?"-1":regionItem.getId());
                        }
                    }
                });
    }


    //---------------------------------区域弹窗菜单 结束---------------------------------------


    public static void showShareDialog(final Activity activity) {
        DialogFactory.showDialog(activity, R.layout.dialog_share, R.style.CustomDialog,
                R.style.popwin_anim_style, Gravity.BOTTOM, 1, 0,
                new DialogFactory.DialogListener() {
                    @Override
                    public void OnInitViewListener(View v, final Dialog dialog) {
                        LinearLayout line1 = (LinearLayout) v.findViewById(R.id.line1);
                        LinearLayout line2 = (LinearLayout) v.findViewById(R.id.line2);
                        for (int i = 0; i < line1.getChildCount(); i++) {
                            final View child = line1.getChildAt(i);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child,
                                            "translationY", 250, 0);
                                    fadeAnim.setDuration(700);
                                    KickBackAnimator kickAnimator = new KickBackAnimator();
                                    kickAnimator.setDuration(300);
                                    fadeAnim.setEvaluator(kickAnimator);
                                    fadeAnim.start();
                                }
                            },i*10);
                        }
                        for (int i = 0; i < line2.getChildCount(); i++) {
                            final View child = line2.getChildAt(i);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child,
                                            "translationY", 250, 0);
                                    fadeAnim.setDuration(700);
                                    KickBackAnimator kickAnimator = new KickBackAnimator();
                                    kickAnimator.setDuration(300);
                                    fadeAnim.setEvaluator(kickAnimator);
                                    fadeAnim.start();
                                }
                            },i*10);
                        }
                        v.findViewById(R.id.btn_cancel).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                        v.findViewById(R.id.report).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(activity, ReportActivity.class);
                                activity.startActivity(intent);
                            }
                        });
                    }
                });
    }

    /**
     * 分享、举报，删除diaolg
     * @param fragment
     * @param shareInfoVO 分享信息
     * @param id   操作的对象id
     * @param reportType 举报的模块所属类型 Constant.REPORT_...  reportType=-1 则不显示举报  比如 投票模块没有举报功能 则传-1
     * @param deleteType 删除的模块所属类型  Constant.DELETE_NEWS OR Constant.DELETE_QUESTION 没有删除入口 可传null
     * @param deletable  是否显示删除入口  只有自己发布的新鲜事 \问答才会有删除功能
     */
    public static void showMoreOperateDialog(final BaseFragment fragment,
                                             final ShareInfoVO shareInfoVO, final String id, final String userId,
                                             final int reportType, final String deleteType, final boolean deletable,
                                             final BaseAdapter
            baseAdapter, final int deletePosition,
                                             final ExpandableRecyclerViewAdapter
                    expandableRecyclerViewAdapter)
    {
        DialogFactory.showDialog(fragment.getActivity(), R.layout.dialog_share, R.style.CustomDialog,
                R.style.popwin_anim_style, Gravity.BOTTOM, 1, 0,
                new DialogFactory.DialogListener() {
                    @Override
                    public void OnInitViewListener(View v, final Dialog dialog) {
                        TextView delete = (TextView) v.findViewById(R.id.delete);
                        if(!deletable && reportType==-1){
                            v.findViewById(R.id.line2).setVisibility(View.GONE);
                        }
                        if(deletable){

                            //这里继续判断是否是自己发布的东西
                            final UserInfoBean userInfo= MyApplication.getUserInfo(fragment
                                    .getActivity());
                            boolean reallyCanDelete = userInfo != null && userInfo.getId().equals(
                                    userId);

                            if (reallyCanDelete) {
                                delete.setVisibility(View.VISIBLE);
                                delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        deletePost(fragment,"",id,deleteType, baseAdapter,deletePosition,
                                        expandableRecyclerViewAdapter);
                                    }
                                });
                            }
                        }
                        TextView report =(TextView) v.findViewById(R.id.report);
                        if(reportType!=-1){//举报
                            report.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(fragment.getActivity(), ReportActivity.class);
                                    intent.putExtra("project_id",id);
                                    intent.putExtra("type",reportType);
                                    fragment.getActivity().startActivity(intent);
                                }
                            });
                        }else{
                            report.setVisibility(View.GONE);
                        }
                        LinearLayout line1 = (LinearLayout) v.findViewById(R.id.line1);
                        LinearLayout line2 = (LinearLayout) v.findViewById(R.id.line2);
                        for (int i = 0; i < line1.getChildCount(); i++) {
                            final View child = line1.getChildAt(i);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child,
                                            "translationY", 250, 0);
                                    fadeAnim.setDuration(700);
                                    KickBackAnimator kickAnimator = new KickBackAnimator();
                                    kickAnimator.setDuration(300);
                                    fadeAnim.setEvaluator(kickAnimator);
                                    fadeAnim.start();
                                }
                            },i*10);
                        }
                        for (int i = 0; i < line2.getChildCount(); i++) {
                            final View child = line2.getChildAt(i);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child,
                                            "translationY", 250, 0);
                                    fadeAnim.setDuration(700);
                                    KickBackAnimator kickAnimator = new KickBackAnimator();
                                    kickAnimator.setDuration(300);
                                    fadeAnim.setEvaluator(kickAnimator);
                                    fadeAnim.start();
                                }
                            },i*10);
                        }
                        v.findViewById(R.id.qq).setOnClickListener(new ShareOnClickListener(dialog,fragment.getBaseActivity(),shareInfoVO,SHARE_MEDIA.QQ));
                        v.findViewById(R.id.weibo).setOnClickListener(new ShareOnClickListener(dialog,fragment.getBaseActivity(),shareInfoVO,SHARE_MEDIA.SINA));
                        v.findViewById(R.id.weixin).setOnClickListener(new ShareOnClickListener(dialog,fragment.getBaseActivity(),shareInfoVO,SHARE_MEDIA.WEIXIN));
                        v.findViewById(R.id.weixinq).setOnClickListener(new ShareOnClickListener(dialog,fragment.getBaseActivity(),shareInfoVO,SHARE_MEDIA.WEIXIN_CIRCLE));
                        v.findViewById(R.id.qzone).setOnClickListener(new ShareOnClickListener(dialog,fragment.getBaseActivity(),shareInfoVO,SHARE_MEDIA.QZONE));
                        v.findViewById(R.id.btn_cancel).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                    }
                });
    }

    /**
     * 帖子更多操作dialog  比其他多设置精华帖 设置推荐帖操作
     *
     */
    public static void shoPostMoreOperateDialog(final BaseFragment fragment,
                                             final ShareInfoVO shareInfoVO, final String circle_id,final String id, final String userId,
                                             final int reportType, final String deleteType, final boolean deletable,
                                             final BaseAdapter
                                                     baseAdapter, final int deletePosition,
                                             final ExpandableRecyclerViewAdapter
                                                     expandableRecyclerViewAdapter)
    {
        DialogFactory.showDialog(fragment.getActivity(), R.layout.dialog_post_share, R.style.CustomDialog,
                R.style.popwin_anim_style, Gravity.BOTTOM, 1, 0,
                new DialogFactory.DialogListener() {
                    @Override
                    public void OnInitViewListener(View v, final Dialog dialog) {
                        TextView delete = (TextView) v.findViewById(R.id.delete);
                        if(!deletable && reportType==-1){
                            v.findViewById(R.id.line2).setVisibility(View.GONE);
                        }
                        PostHeaderParentItem headBean =null;
                        CardVO cardVO =null;

                        if(expandableRecyclerViewAdapter!=null)
                            headBean = (PostHeaderParentItem) expandableRecyclerViewAdapter.getParent(0);
                        if(baseAdapter!=null)
                            cardVO= (CardVO) baseAdapter.getData(deletePosition);
                        boolean isAdmin =headBean ==null?cardVO.getIs_admin().equals("1"):headBean.getIs_admin().equals("1");
                        if(deletable){
                            //这里继续判断是否是自己发布的东西或者是管理员
                            final UserInfoBean userInfo=MyApplication.getUserInfo(fragment
                                    .getActivity());
                            boolean reallyCanDelete = userInfo != null && userInfo.getId().equals(
                                    userId);
                            if (reallyCanDelete || isAdmin) {
                                delete.setVisibility(View.VISIBLE);
                                delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        deletePost(fragment,circle_id,id,deleteType, baseAdapter,deletePosition,
                                                expandableRecyclerViewAdapter);
                                    }
                                });
                            }
                        }

                        //管理员可设置精华帖和推荐帖
                        TextView essence =(TextView) v.findViewById(R.id.essence);
                        TextView recommend =(TextView) v.findViewById(R.id.recommend);
                        if(isAdmin){
                            final boolean isEssence =cardVO ==null?headBean.getIs_essence().equals("1"):cardVO.getIs_essence().equals("1");
                            final boolean isRecommend =cardVO ==null?headBean.getIs_commend().equals("1"):cardVO.getIs_commend().equals("1");
                            essence.setText(isEssence?"取消精华帖":"设置精华帖");
                            recommend.setText(isRecommend?"取消推荐帖":"设置推荐帖");
                            essence.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    setEssencePost(fragment,id,isEssence,deletePosition, baseAdapter,expandableRecyclerViewAdapter);
                                }
                            });
                            recommend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    setRecommendPost(fragment,id,isRecommend,deletePosition,baseAdapter, expandableRecyclerViewAdapter);
                                }
                            });
//                            essence.setVisibility(View.VISIBLE);
//                            recommend.setVisibility(View.VISIBLE);
                        }else{
                            essence.setVisibility(View.GONE);
                            recommend.setVisibility(View.GONE);
                        }

                        TextView report =(TextView) v.findViewById(R.id.report);
                        if(reportType!=-1){//举报
                            report.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(fragment.getActivity(), ReportActivity.class);
                                    intent.putExtra("project_id",id);
                                    intent.putExtra("type",reportType);
                                    fragment.getActivity().startActivity(intent);
                                }
                            });
                        }else{
                            report.setVisibility(View.GONE);
                        }
                        LinearLayout line1 = (LinearLayout) v.findViewById(R.id.line1);
                        LinearLayout line2 = (LinearLayout) v.findViewById(R.id.line2);
                        for (int i = 0; i < line1.getChildCount(); i++) {
                            final View child = line1.getChildAt(i);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child,
                                            "translationY", 250, 0);
                                    fadeAnim.setDuration(700);
                                    KickBackAnimator kickAnimator = new KickBackAnimator();
                                    kickAnimator.setDuration(300);
                                    fadeAnim.setEvaluator(kickAnimator);
                                    fadeAnim.start();
                                }
                            },i*10);
                        }
                        for (int i = 0; i < line2.getChildCount(); i++) {
                            final View child = line2.getChildAt(i);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child,
                                            "translationY", 250, 0);
                                    fadeAnim.setDuration(700);
                                    KickBackAnimator kickAnimator = new KickBackAnimator();
                                    kickAnimator.setDuration(300);
                                    fadeAnim.setEvaluator(kickAnimator);
                                    fadeAnim.start();
                                }
                            },i*10);
                        }
                        v.findViewById(R.id.qq).setOnClickListener(new ShareOnClickListener(dialog,fragment.getBaseActivity(),shareInfoVO,SHARE_MEDIA.QQ));
                        v.findViewById(R.id.weibo).setOnClickListener(new ShareOnClickListener(dialog,fragment.getBaseActivity(),shareInfoVO,SHARE_MEDIA.SINA));
                        v.findViewById(R.id.weixin).setOnClickListener(new ShareOnClickListener(dialog,fragment.getBaseActivity(),shareInfoVO,SHARE_MEDIA.WEIXIN));
                        v.findViewById(R.id.weixinq).setOnClickListener(new ShareOnClickListener(dialog,fragment.getBaseActivity(),shareInfoVO,SHARE_MEDIA.WEIXIN_CIRCLE));
                        v.findViewById(R.id.qzone).setOnClickListener(new ShareOnClickListener(dialog,fragment.getBaseActivity(),shareInfoVO,SHARE_MEDIA.QZONE));
                        v.findViewById(R.id.btn_cancel).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                    }
                });
    }

    static class ShareOnClickListener implements View.OnClickListener{
        private BaseActivity activity;
        private ShareInfoVO shareInfo;
        private SHARE_MEDIA shareMedia;
        private Dialog dialog;
        public ShareOnClickListener(Dialog dialog,BaseActivity activity,ShareInfoVO shareInfoVO,SHARE_MEDIA shareMedia){
            this.dialog =dialog;
            this.activity =activity;
            this.shareInfo= shareInfoVO;
            this.shareMedia =shareMedia;

        }

        @Override
        public void onClick(View v) {
            if (dialog!=null)
                dialog.dismiss();
            String content =Utils.isEmpty(shareInfoVO.getContent())==true?shareInfoVO.getTitle():shareInfoVO.getContent();
            if(shareMedia ==SHARE_MEDIA.SINA){
                UmengSocialUtil.socialShare(activity,shareMedia,"",Utils.isEmpty(shareInfo.getTitle()) ==true?shareInfo.getContent()+shareInfo.getUrl():shareInfo.getTitle()+shareInfo.getUrl(),"",shareInfo.getPic(), new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        activity.showTost("分享成功",1);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        LogUtil.LogD("分享失败消息");
                        activity.showTost("分享失败",1);

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        activity.showTost("取消分享",1);
                    }
                });
            }else{
                UmengSocialUtil.socialShare(activity,shareMedia,shareInfo.getTitle(), content,shareInfo.getUrl(),shareInfo.getPic(), new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        activity.showTost("分享成功",1);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        LogUtil.LogD("分享失败消息");
                        activity.showTost("分享失败",1);

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        activity.showTost("取消分享",1);
                    }
                });
            }

        }
    }

    public static void showReplyWindow(Context context,
            DialogFactory.DialogListener listener)
    {
        DialogFactory.showDialog(context, R.layout.pop_reply, R.style.Theme_Dialog_Reply,
                R.style.popwin_anim_style, Gravity.BOTTOM, 1.0f, 0, listener);
    }

//    public static void doComment(final BaseFragment context,final CommentLoader commentLoader,
//            final JSONObject initParams,int parentPosition)
//    {
//
//        //这里先检查是否登录再是否显示评论窗口，而不是评论之后再检查
//        if (!hasLogin(context.getActivity())) {
//            context.showUnLoginSnackbar();
//            return;
//        }
//
//        DialogFactory.showDialog(context.getActivity(), R.layout.part_action_footer_5, R.style
//                .Theme_Dialog_Reply,
//                -1, Gravity.BOTTOM, 1.0f, 0, new DialogFactory.DialogListener() {
//                    @Override
//                    public void OnInitViewListener(View v, final Dialog dialog) {
//                         dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                             @Override
//                             public void onShow(DialogInterface dialog) {
//                                 context.showSoftKeyboard();
//                             }
//                         });
//
//                        final EditText et_comment= (EditText) v.findViewById(R.id.et_comment);
//                        final Button btn_send= (Button) v.findViewById(R.id.btn_send);
//                        et_comment.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence s, int start, int count,
//                                    int after)
//                            {
//
//                            }
//
//                            @Override
//                            public void onTextChanged(CharSequence s, int start, int before,
//                                    int count)
//                            {
//                                btn_send.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
//                            }
//
//                            @Override
//                            public void afterTextChanged(Editable s) {
//
//                            }
//                        });
//
//                        btn_send.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String comment = et_comment.getText().toString();
//                                JSONUtil.put(initParams,"content",comment);
//                                commentLoader.comment(initParams);
//                                dialog.dismiss();
//                            }
//                        });
//
//                    }
//                });
//    }


//    public static void doReply(final boolean replyParent, final BaseFragment context,
//            final BaseAdapter adapter, final int position,
//            final JSONObject initParams)
//    {
//        //这里先检查是否登录再是否显示评论窗口，而不是评论之后再检查
//        if (!hasLogin(context.getActivity())) {
//            context.showUnLoginSnackbar();
//            return;
//        }
//
//        ReplyDetailItem replyItem=null;
//
//        if (!replyParent) {
//
//            replyItem= (ReplyDetailItem) adapter.getData(position);
//
//            //回复Id
//            JSONUtil.put(initParams, "reply_id", replyItem.getId());
//
//            LogUtil.LogE(TAG, "to_user_id===========>" +  replyItem.getUser_id());
//
//            //回复项的人的Id
//            JSONUtil.put(initParams, "to_user_id", replyItem.getUser_id());
//
//        }
//
//        final ReplyDetailItem finalReplyItem = replyItem;
//        context.dataRequest(true, initParams, Constant.SERVERURL, TAG, new RequestCallBack2() {
//            @Override
//            public void requestBefore() {
//                context.showProgress(TAG);
//            }
//
//            @Override
//            public void onSuccess(String mainJsonString, JSONObject mainJsonObject,
//                    JSONObject rootJsonObject)
//            {
//                final UserInfoBean userInfo= MyApplication.getUserInfo(context.getActivity());
//
//                if (userInfo==null) { OtherUtil.showToast(context.getActivity(),"评论失败，无法获取用户信息");
//                    return;}
//
//                //回复内容
//                final String comment = JSONUtil.getString(initParams,"content");
//
//                ReplyDetailItem reply = new ReplyDetailItem();
//                final String id = JSONUtil.getString(mainJsonObject, "id");
//                LogUtil.LogE(TAG, "id===========>" + id);
//
//                reply.setId(id);
//                reply.setCreate_time(OtherUtil.getTimeSecs());
//                reply.setUser_pic(userInfo.getUser_pic());
//                reply.setUsername(userInfo.getUsername());
//                reply.setUser_id(userInfo.getId());
//                reply.setTo_user_id("-1");
//                reply.setTo_name(null);
//                reply.setContent(comment);
//                reply.setCanDelete(true);
//
//                if (!replyParent) {
//                    LogUtil.LogE(TAG,
//                            "finalReplyItem.getUsername()==>" + finalReplyItem.getUsername() +
//                                    "finalReplyItem.getTo_user_id()" +
//                                    finalReplyItem.getTo_user_id());
//                    reply.setTo_name(finalReplyItem.getUsername());
//                    reply.setTo_user_id(finalReplyItem.getUser_id());
//                }
//
//                adapter.addData(1,reply);
//
//            }
//
//            @Override
//            public void onFail(int key, String msg) {
//                if (key != RequestCallBack2.KEY_FAIL) {
//                    OtherUtil.showToast(context.getActivity(), msg);
//                }
//            }
//
//            @Override
//            public void requestAfter() {
//                context.dismissProgress();
//            }
//        });
//    }

//    public static void doDeleteComment(final int deleteType,final BaseFragment context,
//            final CommentAvailableAdapter adapter,final BaseAdapter baseAdapter,
//            final JSONObject params)
//    {
//
//        context.dataRequest(true, params, Constant.SERVERURL, TAG, new RequestCallBack2() {
//            @Override
//            public void requestBefore() {
//                context.showProgress(TAG);
//            }
//
//            @Override
//            public void onSuccess(String mainJsonString, JSONObject mainJsonObject,
//                    JSONObject rootJsonObject)
//            {
//                if (deleteType == CommentDeleteFragment.TYPE_DELETE_LIST && adapter != null) {
//                    adapter.removeParent(JSONUtil.getInt(params, Constant.KEY_PARENT_POSITION));
//                } else if (deleteType == CommentDeleteFragment.TYPE_DELETE_DETAIL &&
//                        baseAdapter != null)
//                {
//                    boolean isDeleteAll = JSONUtil.getBoolean(params, Constant.KET_IS_DELETE_ALL);
//                    if (isDeleteAll) {
//                        //删除自己的主评论后自动退出当前界面
//                        baseAdapter.removeAllData();
//                        context.getActivity().finish();
//
//                    } else {
//                        baseAdapter.removeData(
//                                JSONUtil.getInt(params, Constant.KEY_CHILD_POSITION));
//                    }
//                }
//
//                OtherUtil.showToast(context.getActivity(), "内容已删除");
//            }
//
//            @Override
//            public void onFail(int key, String message) {
//                if (key != RequestCallBack2.KEY_FAIL) {
//                    OtherUtil.showToast(context.getActivity(), message);
//                }
//            }
//
//            @Override
//            public void requestAfter() {
//                context.dismissProgress();
//            }
//        });
//
//    }
//
//    public static void deleteComment(final int deleteType, final BaseFragment context,
//            final CommentAvailableAdapter adapter, final BaseAdapter baseAdapter, JSONObject params)
//    {
//
//        //这里先检查是否登录再是否显示评论窗口，而不是评论之后再检查
//        if (!hasLogin(context.getActivity())) {
//            context.showUnLoginSnackbar();
//            return;
//        }
//
//        CommentDeleteFragment fragment =
//                (CommentDeleteFragment) context.getActivity().getSupportFragmentManager()
//                        .findFragmentByTag(CommentDeleteFragment.TAG);
//        LogUtil.LogE(TAG,"params 1==>"+params);
//        if (fragment == null) {
//            fragment = new CommentDeleteFragment();
//            fragment.setArgs(deleteType,context,adapter,baseAdapter,params);
//            LogUtil.LogE(TAG,"params 2==>"+params);
//        }
//        fragment.show(context.getActivity().getSupportFragmentManager(),
//                CommentDeleteFragment.TAG);
//
//    }



    /**
     * 获取当前时间(秒)
     * @return
     */
    public static long getTimeSecs()
    {
        return new Date().getTime() / 1000;
    }
    /**
     * 获取当前时间(毫秒)
     * @return
     */
    public static long getTimeMillisecs() {
        return new Date().getTime();
    }

    public static String getDataString(long timeSecs) {
        return DateFormat.format("yyyy-MM-dd", timeSecs * 1000).toString();
    }

    public static String getToken(Context context) {
        return PreferencesUtils.getString(context, PreferencesUtils.KEY_TOKEN);
    }

    public static boolean hasLogin(Context context) {
        return !TextUtils.isEmpty(getToken(context));
    }

    /**
     * 判断给定的 ID 是否是自己
     * @param context
     * @param userId
     * @return
     */
    public static boolean isAuthor(Context context, String userId) {
        UserInfoBean userInfo = MyApplication.getUserInfo(context);
//        LogUtil.LogE(TAG,"userInfo.getId()==========>"+userInfo.getId());
        return userInfo != null && userInfo.getId().equals(userId);
    }

    public static JSONObject getJSONObject(Context context, Object modelValue, Object actionValue,
            boolean requestToken)
    {
        boolean hasLogin = OtherUtil.hasLogin(context);
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", modelValue);
        JSONUtil.put(params, "action", actionValue);
        if (requestToken || hasLogin) {
            JSONUtil.put(params, "token", OtherUtil.getToken(context));
        }
        return params;
    }


    private static class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            LogUtil.LogE(TAG,"onLocationChanged"+location.toString());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogUtil.LogE(TAG,"onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogUtil.LogE(TAG,"onProviderEnabled==>"+provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogUtil.LogE(TAG,"onProviderDisabled==>"+provider);
        }
    }

    public static void getLocation(Context context, Double[] location) {
        LocationManager locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        double latitude=0;
        double longitude=0;
        final Location lastKnownLocation;


        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            lastKnownLocation =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            lastKnownLocation = locationManager.getLastKnownLocation(
                    LocationManager.NETWORK_PROVIDER);
        }
        LogUtil.LogE(TAG,"lastKnownLocation==null?"+(lastKnownLocation ==null));
        if (lastKnownLocation != null) {
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();
            LogUtil.LogE(TAG,"latitude="+latitude+",longitude="+longitude);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,new
                    MyLocationListener() );
        }
        location[0] = longitude;
        location[1] = latitude;

    }


    /**
     * 点赞
     * @return -1.请求失败 1.点赞成功 2.取消点赞成功
     */
    public interface LikeCallback{
        void onFinished(int backKey);
    }
    public static void like(final BaseFragment context, final CheckBox likeBox,JSONObject
            initParams, final LikeCallback callback)
    {
        final int backKey[]=new int[1];

        context.dataRequest(true, initParams, Constant.SERVERURL, context.getClass().getSimpleName(),
                new RequestCallBack2() {

                    int likeNum = Integer.valueOf(likeBox.getText().toString());

                    @Override
                    public void requestBefore() {
                        context.showProgress(context.getClass().getSimpleName());
                    }

                    @Override
                    public void onSuccess(String mainJsonString,JSONObject mainJsonObject,JSONObject rootJsonObject) {
                        //点赞成功
                        likeBox.setChecked(true);
                        likeBox.setText(++likeNum+"");
                        backKey[0] =1;
                        context.showTost("点赞成功",0);
                    }

                    @Override
                    public void onFail(int key,String message) {
                        if (key == 5) {
                            context.showTost("取消点赞",0);
                            //此处代表 取消点赞成功回调
                            likeBox.setChecked(false);

                            if (likeNum > 0) {
                                likeBox.setText(--likeNum + "");
                            }

                        }
                        backKey[0] = key == 5 ? 2 : -1;

                        if (key != 5 && key != RequestCallBack2.KEY_FAIL) {
                            OtherUtil.showToast(context.getActivity(), message);
                        }
                    }

                    @Override
                    public void requestAfter() {
                        context.dismissProgress();
                        if (callback!=null) {
                            callback.onFinished(backKey[0]);
                        }
                    }
                });
    }

    /**
     * 收藏
     * @param context
     * @param likeBox
     * @param id
     * @param modelValue
     * @param actionValue
     * @return -1.请求失败 1.收藏成功 2.取消收藏成功
     */
    public static int collect(final BaseFragment context, final CheckBox likeBox,final String id, Object
            modelValue,
                           Object actionValue)
    {
        final int backKey[]=new int[1];

        final String token = OtherUtil.getToken(context.getActivity());
        if (TextUtils.isEmpty(token)) {
            context.showUnLoginSnackbar();
            return -1;
        }

        context.showProgress(context.getClass().getSimpleName());

        JSONObject params = OtherUtil.getJSONObject(context.getActivity(), modelValue, actionValue,
                true);
        JSONUtil.put(params, "id", id);
        context.jsonRequest(true, params, Constant.SERVERURL, context.getClass().getSimpleName(),
                new ContextRequestCallBack() {
                    @Override
                    public void onSuccess(String jsonString) {
                        //点赞成功
                        context.showTost("收藏成功",0);
                        likeBox.setChecked(true);
                        backKey[0] =1;
                        context.dismissProgress();

                    }

                    @Override
                    public void onCodeError(int key, String message) {
                        context.dismissProgress();
                        if(key ==6){
                            context.showTost("取消收藏",0);
                            //此处代表 取消点赞成功回调
                            likeBox.setChecked(false);
                            backKey[0] =2;

                        }else if(key==0){
                            context.showTost("操作失败",1);
                        }else{
                            context.showTost(message,1);
                        }

                    }

                    @Override
                    public void onConnectError(String message) {
                        OtherUtil.showToast(context.getActivity(),message);
                        context.dismissProgress();
                        backKey[0] =-1;
                    }
                });
        return backKey[0];
    }

    /**
     *   根据shareType 及id 判断是否需要重新获取分享信息
     * @param fragment
     * @param id  操作对象id
     * @param shareType 获取分享信息模块类型
     */
    public static void showShareFragment(final BaseFragment fragment, String id,final String shareTitle,final String shareContent, int shareType) {
        if (!(id.equals(oldId) && shareType == oldShareType) || shareInfoVO == null) {
            oldId =id;oldShareType =shareType;
            OtherUtil.getShareInfo(fragment,id, shareType, new OtherUtil.ShareCallback() {
                @Override
                public void onSuccess(ShareInfoVO shareInfo) {
//                    shareInfoVO = shareInfo;
                    shareInfoVO.setContent(shareContent);
                    shareInfoVO.setTitle(shareTitle);
//                    shareInfoVO.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.binfenjiari");
//                    if (shareInfo != null) {
                    shareInfoVO.setUrl(shareInfo==null?"http://a.app.qq.com/o/simple.jsp?pkgname=com.binfenjiari":shareInfo.getUrl());
                    shareInfoVO.setPic(shareInfo==null?"":shareInfo.getPic());
//                    if (shareInfoVO != null) {
                        ShareDialogFragment shareDialog = ShareDialogFragment.newInstance(ShareDialogFragment.STYLE_NO_TITLE, R.layout.pop_share,shareInfoVO);
                        shareDialog.show(fragment.getActivity().getSupportFragmentManager(), "share");
//                    } else {
//                        fragment.showTost("获取分享信息失败,请稍后再试...", 1);
//                    }
                }
            });
        } else {
            ShareDialogFragment shareDialog = ShareDialogFragment.newInstance(ShareDialogFragment.STYLE_NO_TITLE, R.layout.pop_share,shareInfoVO);
            shareDialog.show(fragment.getActivity().getSupportFragmentManager(), "share");
        }
    }


    private static String oldId="";
    private static int oldShareType=-1;
    private static ShareInfoVO shareInfoVO =new ShareInfoVO();
    /**
     *
     * @param fragment
     * @param id 操作对象id
     * @param shareType 获取分享信息模块类型  根据shareType 及id 判断是否需要重新获取分享信息
     * @param reportType 举报模块类型
     * @param deleteType 删除模块类型
     * @param deletable 是否显示删除入口  只有自己发布的新鲜事 \问答才会有删除功能
     */
    public static void showMoreOperate(final BaseFragment fragment, final String id,final String shareTitle,final String shareContent,
            final String userId, int shareType, final int reportType, final String deleteType,
            final boolean deletable, final BaseAdapter baseAdapter, final int deletePosition,
            final ExpandableRecyclerViewAdapter
            expandableRecyclerViewAdapter)
    {
        if (!(id.equals(oldId) && shareType == oldShareType) || shareInfoVO == null) {
            oldId =id; oldShareType =shareType;
            getShareInfo(fragment,id, shareType , new OtherUtil.ShareCallback() {
                @Override
                public void onSuccess(ShareInfoVO shareInfo) {
                    if(shareInfo==null)
                        return;
                    shareInfoVO = shareInfo;

                    if(!TextUtils.isEmpty(shareContent)) {
                        shareInfoVO.setContent(shareContent);
                    }
                    if(!TextUtils.isEmpty(shareTitle)) {
                        shareInfoVO.setTitle(shareTitle);
                    }
//                    shareInfoVO.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.binfenjiari");
                    if (shareInfo == null) {
                        shareInfoVO.setUrl(shareInfo == null ? "http://a.app.qq.com/o/simple.jsp?pkgname=com.binfenjiari" : shareInfo.getUrl());
                    }
                        OtherUtil.showMoreOperateDialog(fragment, shareInfoVO, id, userId,
                                reportType,deleteType,deletable,baseAdapter,deletePosition,expandableRecyclerViewAdapter);
//                    } else {
//                        fragment.showTost("获取分享信息失败,请稍后再试...", 1);
//                    }
                }
            });
        }else{
            OtherUtil.showMoreOperateDialog(fragment, shareInfoVO, id, userId,reportType, deleteType,
                    deletable,baseAdapter,deletePosition,expandableRecyclerViewAdapter);
        }
    }

    /**
     * 帖子更多操作
     * @param fragment
     * @param id
     * @param shareTitle
     * @param shareContent
     * @param userId
     * @param shareType
     * @param reportType
     * @param deleteType
     * @param deletable
     * @param baseAdapter
     * @param deletePosition
     * @param expandableRecyclerViewAdapter
     */
    public static void showPostMoreOperate(final BaseFragment fragment,final String circle_id, final String id,final String shareTitle,final String shareContent,
                                       final String userId, int shareType, final int reportType, final String deleteType,
                                       final boolean deletable, final BaseAdapter baseAdapter, final int deletePosition,
                                       final ExpandableRecyclerViewAdapter
                                               expandableRecyclerViewAdapter)
    {
        if (!(id.equals(oldId) && shareType == oldShareType) || shareInfoVO == null) {
            oldId =id; oldShareType =shareType;
            getShareInfo(fragment,id, shareType , new OtherUtil.ShareCallback() {
                @Override
                public void onSuccess(ShareInfoVO shareInfo) {
//                    shareInfoVO = shareInfo;

                    shareInfoVO.setContent(shareContent);
                    shareInfoVO.setTitle(shareTitle);
//                    shareInfoVO.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.binfenjiari");
//                    if (shareInfo != null) {
                    shareInfoVO.setUrl(shareInfo==null?"http://a.app.qq.com/o/simple.jsp?pkgname=com.binfenjiari":shareInfo.getUrl());
                    OtherUtil.shoPostMoreOperateDialog(fragment, shareInfoVO, circle_id,id, userId,
                            reportType,deleteType,deletable,baseAdapter,deletePosition,expandableRecyclerViewAdapter);
//                    } else {
//                        fragment.showTost("获取分享信息失败,请稍后再试...", 1);
//                    }
                }
            });
        }else{
            OtherUtil.shoPostMoreOperateDialog(fragment, shareInfoVO,circle_id, id, userId,reportType, deleteType,
                    deletable,baseAdapter,deletePosition,expandableRecyclerViewAdapter);
        }
    }
    /**
     * 获取分享信息回调
     */
    public interface ShareCallback{
        void onSuccess(ShareInfoVO bean);
    }
    /**
     *获取分享信息，如果分享信息为null，则用详情界面的title，content，img，logo为分享内容
     * @param context
     * @param id  各类分享对象的id
     * @param type 1个人中心(无需传id) 2基地 3活动 4新鲜事 5投票 6问答 7视频、语音视听 8图文视听  9帖子 10投票资讯 11投票项详情 12投票页面
     * @return
     */
    public static void  getShareInfo(final BaseFragment context,String id,int type,final ShareCallback shareCallback){
        context.showProgress(context.getClass().getSimpleName());
        JSONObject params = new JSONObject();
        JSONUtil.put(params,"device_type",2);//2：android
        if(id!=null)
            JSONUtil.put(params,"id",id);
        JSONUtil.put(params,"type",type);
        JSONUtil.put(params,"model",Constant.MODEL_COMMON);
        JSONUtil.put(params,"action",Constant.ACTION_GET_SHARE_INFO);
        context.jsonRequest(false, params, Constant.SERVERURL, context.getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                context.dismissProgress();
                ShareInfoVO shareInfo =JSONUtil.fromJson(jsonString,ShareInfoVO.class);
                shareCallback.onSuccess(shareInfo);
            }

            @Override
            public void onCodeError(int key, String message) {
                shareCallback.onSuccess(null);
//                if(key==3){
//                    context.showTost("获取分享信息失败,请稍后再试",1);
//                }else{
//                    context.showTost(message,1);
//                }
                context.dismissProgress();
            }

            @Override
            public void onConnectError(String message) {
                shareCallback.onSuccess(null);
                context.dismissProgress();
//                context.showTost(message,1);
//                context.dismissProgress();
            }
        });
    }


    /**
     * 删除新鲜事、问答成功回调
     */
    public interface DeleteCallback{
        void onSuccess();
    }
    /**
     * 删除新鲜事 or 问答 or 帖子
     * @param fragment
     * @param  circle_id  帖子删除需要
     * @param id
     * @param deleteType
     */
    private static void deletePost(final BaseFragment fragment,String circle_id , String id, final String
            deleteType, final BaseAdapter baseAdapter, final int deletePosition,
            final ExpandableRecyclerViewAdapter
                    expandableRecyclerViewAdapter){
        fragment.showProgress(fragment.getClass().getSimpleName());
        JSONObject params = new JSONObject();
        JSONUtil.put(params,"token",PreferencesUtils.getString(fragment.getActivity(),PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params,"id",id);
        if(deleteType!=null&&deleteType.equals(Constant.DELETE_NEWS)){
            JSONUtil.put(params,"model",Constant.MODEL_NEWS);
            JSONUtil.put(params,"action",Constant.ACTION_NEWS_DELETE);
        }else if(deleteType!=null&&deleteType.equals(Constant.DELETE_QUESTION)){
            JSONUtil.put(params,"model",Constant.MODEL_QUESTION);
            JSONUtil.put(params,"action",Constant.ACTION_QUESTION_DELETE);
        }else if(deleteType!=null && deleteType.equals(Constant.DELETE_POST)){
            JSONUtil.put(params,"circle_id",circle_id);
            JSONUtil.put(params,"model",Constant.MODEL_POST);
            JSONUtil.put(params,"action",Constant.ACTION_POST_DELETE);
        }
        fragment.jsonRequest(true, params, Constant.SERVERURL, fragment.getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                fragment.dismissProgress();
                fragment.showTost("删除成功",0);

                if (baseAdapter!=null && deletePosition!=-1) {
                    baseAdapter.removeData(deletePosition);
                } else if (expandableRecyclerViewAdapter!=null) {
                    //直接 finish 托管Activity
                    Intent intent = new Intent();
                    intent.putExtra(Constant.KEY_TYPE,Constant.DELETE);
                    intent.putExtra(Constant.KEY_POSITION,deletePosition);
                    fragment.getActivity().setResult(Activity.RESULT_OK,intent);
                    fragment.getActivity().finish();
                }
            }

            @Override
            public void onCodeError(int key, String message) {
                fragment.dismissProgress();
                fragment.showTost(message,1);
            }

            @Override
            public void onConnectError(String message) {
                fragment.dismissProgress();
            }
        });

    }

    /**
     * 设置取消推荐帖
     * @param fragment
     * @param id 帖子id
     * @param isRecommend 是否为推荐帖
     * @param expandableRecyclerViewAdapter
     */
    private static void setRecommendPost(final BaseFragment fragment , String id ,final boolean isRecommend,final int adapterPosition,final BaseAdapter baseAdapter,final ExpandableRecyclerViewAdapter expandableRecyclerViewAdapter){
        fragment.showProgress(fragment.getClass().getSimpleName());
        JSONObject params = new JSONObject();
        JSONUtil.put(params,"token",PreferencesUtils.getString(fragment.getActivity(),PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params,"id",id);
        JSONUtil.put(params,"type",isRecommend?"2":"1");//1设置推荐 2取消推荐
        JSONUtil.put(params,"model",Constant.MODEL_CIRCLE_MANAGE);
        JSONUtil.put(params,"action",Constant.ACTION_SET_RECOMMEND_POST);
        fragment.jsonRequest(true, params, Constant.SERVERURL, fragment.getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                fragment.dismissProgress();
                String message = isRecommend?"取消推荐帖成功":"设置推荐帖成功";
                fragment.showTost(message,0);
                if(expandableRecyclerViewAdapter!=null){
                    PostHeaderParentItem headerParentItem = (PostHeaderParentItem) expandableRecyclerViewAdapter.getParent(0);
                    headerParentItem.setIs_commend(isRecommend?"2":"1");
                    expandableRecyclerViewAdapter.notifyItemChanged(0);
                }
                if(baseAdapter!=null){
                    CardVO cardVO = (CardVO) baseAdapter.getData(adapterPosition);
                    cardVO.setIs_commend(isRecommend?"2":"1");
                    baseAdapter.changeData(adapterPosition,cardVO);
                }

//                    Intent intent = new Intent();
//                    intent.putExtra(Constant.KEY_TYPE,Constant.DELETE);
//                    intent.putExtra(Constant.KEY_POSITION,deletePosition);
//                    fragment.getActivity().setResult(Activity.RESULT_OK,intent);
            }

            @Override
            public void onCodeError(int key, String message) {
                fragment.dismissProgress();
                fragment.showTost(message,1);
            }

            @Override
            public void onConnectError(String message) {
                fragment.dismissProgress();
            }
        });

    }

    /**
     * 设置取消精华帖
     * @param fragment
     * @param id 帖子id
     * @param isEssence 是否为精华帖
     * @param expandableRecyclerViewAdapter
     */
    private static void setEssencePost(final BaseFragment fragment , String id ,final boolean isEssence,final int adapterPosition,final BaseAdapter baseAdapter,final ExpandableRecyclerViewAdapter expandableRecyclerViewAdapter){
        fragment.showProgress(fragment.getClass().getSimpleName());
        JSONObject params = new JSONObject();
        JSONUtil.put(params,"token",PreferencesUtils.getString(fragment.getActivity(),PreferencesUtils.KEY_TOKEN));
        JSONUtil.put(params,"id",id);
        JSONUtil.put(params,"type",isEssence?"2":"1");//1设置精华 2取消精华
        JSONUtil.put(params,"model",Constant.MODEL_CIRCLE_MANAGE);
        JSONUtil.put(params,"action",Constant.ACTION_SET_ESSENCE_POST);
        fragment.jsonRequest(true, params, Constant.SERVERURL, fragment.getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                fragment.dismissProgress();
                String message = isEssence?"取消精华帖成功":"设置精华帖成功";
                fragment.showTost(message,0);
                if(expandableRecyclerViewAdapter!=null){
                    PostHeaderParentItem headerParentItem = (PostHeaderParentItem) expandableRecyclerViewAdapter.getParent(0);
                    headerParentItem.setIs_essence(isEssence?"2":"1");
                    expandableRecyclerViewAdapter.notifyItemChanged(0);
                }
                if(baseAdapter!=null){
                    if(baseAdapter!=null){
                        CardVO cardVO = (CardVO) baseAdapter.getData(adapterPosition);
                        cardVO.setIs_essence(isEssence?"2":"1");
                        baseAdapter.changeData(adapterPosition,cardVO);
                    }
                }

//                    Intent intent = new Intent();
//                    intent.putExtra(Constant.KEY_TYPE,Constant.DELETE);
//                    intent.putExtra(Constant.KEY_POSITION,deletePosition);
//                    fragment.getActivity().setResult(Activity.RESULT_OK,intent);
            }

            @Override
            public void onCodeError(int key, String message) {
                fragment.dismissProgress();
                fragment.showTost(message,1);
            }

            @Override
            public void onConnectError(String message) {
                fragment.dismissProgress();
            }
        });

    }

    /**
     * 加入、退出圈子回调
     */
    public interface JoinCircleCallback{
        void onFinish(int key);
    }
    public static void joinCircle(final BaseFragment fragment,String id,final JoinCircleCallback callback){
        fragment.showProgress(TAG);
        final String token = PreferencesUtils.getString(fragment.getActivity(), PreferencesUtils.KEY_TOKEN);
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"token",token);
        JSONUtil.put(params,"id",id);
        JSONUtil.put(params,"model",Constant.MODEL_CIRCLE);
        JSONUtil.put(params,"action",Constant.ACTION_JOIN_CIRCLE);
        fragment.jsonRequest(false, params, Constant.SERVERURL, TAG, new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                fragment.dismissProgress();
                LogUtil.LogE(TAG,"response--->"+jsonString.toString());
                fragment.showTost("加入成功",0);
                callback.onFinish(1);

            }

            @Override
            public void onCodeError(int key, String message) {
                fragment.dismissProgress();
                if(key==20){//退出成功
                    callback.onFinish(0);
                }else{
                    fragment.showTost(message,0);
                }

            }

            @Override
            public void onConnectError(String message) {
                fragment.dismissProgress();
            }
        });
    }

    public static String getVideoTime(String time) {

        String[] e=time.split("\\.");

        if (e.length!=2) return "00:00";

        int totalMinutes = Integer.valueOf(e[0]);

        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        String seconds = e[1];
        String finalSeconds = seconds.length()==1 ? seconds + "0" : seconds;

        return hours > 0 ? String.format("%1$s:%2$s:%3$s", hours < 10 ? "0" + hours : hours + "",
                minutes < 10 ? "0" + minutes : minutes + "", finalSeconds) : String.format("%1$s:%2$s",
                minutes < 10 ? "0" + minutes : minutes + "", finalSeconds);
    }


    public static boolean checkNetwork(Activity context, final MediaController mediaController) {
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            OtherUtil.showToast(context,"网络不可用");
            return false;
        }

        if (networkInfo.getType()==ConnectivityManager.TYPE_MOBILE) {

            MobileNetworkWarnningFragment warnningFragment= (MobileNetworkWarnningFragment) context.getFragmentManager()
                    .findFragmentByTag(MobileNetworkWarnningFragment.TAG);

            if (warnningFragment==null) {
               warnningFragment=new MobileNetworkWarnningFragment();
            }

            warnningFragment.show(context.getFragmentManager(),MobileNetworkWarnningFragment.TAG);

            warnningFragment.setCallbacks(new MobileNetworkWarnningFragment.Callbacks() {
                @Override
                public void onSelectionFinished(boolean isOk) {
                    if (isOk) {
                        MyApplication.isInMobileConnectPlayVideo=true;
                        mediaController.reallyPlayNewVideo(0);
                    }
                }
            });

            return false;
        }

        return true;
    }

    /**
     * 成员操作回调
     */
    public interface MemeberOperateCallback{
        void onsuccess(String operayteType );
    }
    /**
     * 圈子成员操作
     * @param circleId
     * @param userIdList
     * @param type 必须 1转让圈子 2设置管理员 3取消管理员 4移除成员 5拉黑 6取消拉黑
     */
   public static void memeberOperate(final BaseFragment fragment,String circleId, List<String> userIdList,final String type,final MemeberOperateCallback callback){
        fragment.showProgress(TAG);
        StringBuilder userIdString = new StringBuilder();
        for(int i=0;i<userIdList.size();i++){
            userIdString.append(userIdList.get(i)).append(",");
        }
        String userIds =userIdString.substring(0,userIdString.length()-1);
        final String token = PreferencesUtils.getString(fragment.getActivity(), PreferencesUtils.KEY_TOKEN);
        JSONObject params =new JSONObject();
        JSONUtil.put(params,"token",token);
        JSONUtil.put(params,"id",circleId);
        JSONUtil.put(params,"type",type);
        JSONUtil.put(params,"userIds",userIds);
        JSONUtil.put(params,"model",Constant.MODEL_CIRCLE_MANAGE);
        JSONUtil.put(params,"action",Constant.ACTION_MEMEBER_OPERATE);
        fragment.jsonRequest(false, params, Constant.SERVERURL, TAG, new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                fragment.dismissProgress();
                callback.onsuccess(type);
                LogUtil.LogE(TAG,"response--->"+jsonString.toString());

            }

            @Override
            public void onCodeError(int key, String message) {
                fragment.dismissProgress();
                fragment.showTost(message,0);

            }

            @Override
            public void onConnectError(String message) {
                fragment.dismissProgress();
            }
        });
    }

    /**
     * 过滤敏感词汇
     * @param content 需要过滤的内容
     * @return
     */
    public static String filterSensitives(Context context,String content){
        String filterContent =content;
        List<SensitiveWordVO> list =MyApplication.getSensitiveList(context);
        if(list==null)
            return filterContent;
        for(int i=0;i<list.size();i++){
            String sensitive =list.get(i).getContent();
            filterContent= filterContent.replace(sensitive,"****");
        }
        return  filterContent;
    }

}
