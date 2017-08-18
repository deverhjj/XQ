package com.biu.modulebase.binfenjiari.other.umeng;

import android.app.Activity;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * @author Lee
 * @Title: {UMeng社会化分享sdk5.0+ API}
 * @Description:{包括分享、授权、取消授权、获取平台信息}
 * @date 2016/3/8
 */

public class UmengSocialUtil {

/**
     * 社会化分享
     * @param activity
     * @param platform SHARE_MEDIA
     * @param title  title参数对新浪、人人、豆瓣不生效
     * @param text
     * @param targetUrl
     * @param umShareListener
     *
     *      值得注意的是，分享也应该重写
     *      onActivityResult()
             @Override
             protected void onActivityResult(int requestCode, int resultCode, Intent data) {
             super.onActivityResult(requestCode, resultCode, data);
             UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);
             }
     */

    public static void socialShare(Activity activity, SHARE_MEDIA platform, String title, String text, String targetUrl,String pic, UMShareListener umShareListener){
        if(Utils.isEmpty(pic)){
            new ShareAction(activity).setPlatform(platform).setCallback(umShareListener)
                    .withTitle(title)
                    .withText(text)
                    .withMedia(new UMImage(activity, R.mipmap.ic_launcher))//默认用logo
                    .withTargetUrl(targetUrl)
                    .share();
        }else{
            new ShareAction(activity).setPlatform(platform).setCallback(umShareListener)
                    .withTitle(title)
                    .withText(text)
                    .withMedia(new UMImage(activity, pic))//默认用logo
                    .withTargetUrl(targetUrl)
                    .share();
        }

    }


/**
     * 第三方授权
     * @param activity
     * @param platform
     * @param umAuthListener
     *
     * 值得注意的是，分享也应该重写
     * onActivityResult()
         @Override
         protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         mShareAPI.onActivityResult(requestCode, resultCode, data);
         }
     */
    public static void doAuth(Activity activity, SHARE_MEDIA platform, UMAuthListener umAuthListener){
        UMShareAPI umShareAPI= MyApplication.getUMShareAPI();
        umShareAPI.doOauthVerify(activity, platform, umAuthListener);
    }

/**
     * 取消第三方授权
     * @param activity
     * @param platform
     * @param umdelAuthListener
     */

    public static void deleteAuth(Activity activity, SHARE_MEDIA platform, UMAuthListener umdelAuthListener){
        UMShareAPI umShareAPI= MyApplication.getUMShareAPI();
        umShareAPI.deleteOauth(activity, platform, umdelAuthListener);
    }

/**
     * 获取第三方平台账号信息
     * @param activity
     * @param platform
     * @param mListener
     */

    public static void getPlatformInfo(Activity activity, SHARE_MEDIA platform, UMAuthListener mListener){
        UMShareAPI umShareAPI= MyApplication.getUMShareAPI();
        umShareAPI.getPlatformInfo(activity, platform, mListener);
    }
}
