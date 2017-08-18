package com.biu.modulebase.binfenjiari.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.model.ShareInfoVO;
import com.biu.modulebase.binfenjiari.other.umeng.UmengSocialUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.common.base.BaseFragment;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/5/4
 */
public class MyShareFragment extends BaseFragment {

    private ShareInfoVO shareInfoVO;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_share, container, false);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_share, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        rootView.findViewById(R.id.qq).setOnClickListener(this);
        rootView.findViewById(R.id.sina).setOnClickListener(this);
        rootView.findViewById(R.id.weixin).setOnClickListener(this);
        rootView.findViewById(R.id.weixin_q).setOnClickListener(this);
        rootView.findViewById(R.id.qzone).setOnClickListener(this);
        rootView.findViewById(R.id.fx_copy).setOnClickListener(this);

    }

    /**
     * 加载数据，一切网络请求方法在此方法中写
     */
    @Override
    public void loadData() {
        OtherUtil.getShareInfo(this, null, 1, new OtherUtil.ShareCallback() {
            @Override
            public void onSuccess(ShareInfoVO bean) {
                shareInfoVO = bean;
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(shareInfoVO==null){
            showTost("获取分享信息失败",0);
            return;
        }
        int i = v.getId();
        if (i == R.id.qq) {
            UmengSocialUtil.socialShare(getActivity(), SHARE_MEDIA.QQ, shareInfoVO.getTitle(), shareInfoVO.getContent(), shareInfoVO.getUrl(), shareInfoVO.getPic(), new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA share_media) {

                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    showTost("分享失败", 1);

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {

                }
            });

        } else if (i == R.id.sina) {
            UmengSocialUtil.socialShare(getActivity(), SHARE_MEDIA.SINA, "", shareInfoVO.getTitle() + shareInfoVO.getUrl(), "", shareInfoVO.getPic(), new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA share_media) {

                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    showTost("分享失败", 1);
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {

                }
            });

        } else if (i == R.id.weixin) {
            UmengSocialUtil.socialShare(getActivity(), SHARE_MEDIA.WEIXIN, shareInfoVO.getTitle(), shareInfoVO.getContent(), shareInfoVO.getUrl(), shareInfoVO.getPic(), new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    showTost("分享成功", 1);
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    showTost("分享失败", 1);
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    showTost("取消分享", 1);
                }
            });

        } else if (i == R.id.weixin_q) {
            UmengSocialUtil.socialShare(getActivity(), SHARE_MEDIA.WEIXIN_CIRCLE, shareInfoVO.getTitle(), shareInfoVO.getContent(), shareInfoVO.getUrl(), shareInfoVO.getPic(), new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    showTost("分享成功", 1);
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    showTost("分享失败", 1);
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    showTost("取消分享", 1);
                }
            });

        } else if (i == R.id.qzone) {
            UmengSocialUtil.socialShare(getActivity(), SHARE_MEDIA.QZONE, shareInfoVO.getTitle(), shareInfoVO.getContent(), shareInfoVO.getUrl(), shareInfoVO.getPic(), new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    showTost("分享成功", 1);
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    showTost("分享失败", 1);
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    showTost("取消分享", 1);
                }
            });

        }else if(i ==R.id.fx_copy){
            // 从API11开始android推荐使用android.content.ClipboardManager
            // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本内容放到系统剪贴板里。
            cm.setText(shareInfoVO.getUrl());
            Toast.makeText(getContext(), "复制成功，可以发给朋友们了。", Toast.LENGTH_LONG).show();
        }
    }
}
