package com.biu.modulebase.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.BannerVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.util.WebViewManager;
import com.biu.modulebase.common.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/6/13
 */
public class WebViewFragment extends BaseFragment {
    /**加载html网址**/
    public final static String LOAD_HTML_URL ="1";
    /**加载html代码**/
    public final static String LOAD_HTML_CODE ="2";

    private WebView webView;
    /**加载类型**/
    private String loadType;
    /**请求静态Html 类型 2：用户协议 4：发帖必读**/
    private String type;

    private String id;

    private int shareType=-1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_webview, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void getIntentData() {
        Intent intent =getActivity().getIntent();
        loadType= intent.getStringExtra("loadType");
        if(loadType.equals(LOAD_HTML_CODE)){
            id =intent.getStringExtra("id");
//            bannerVO = (BannerVO) intent.getSerializableExtra("bannerVO");
        }else{
            type =intent.getStringExtra(Constant.KEY_TYPE);
        }

    }

    @Override
    protected void initView(View rootView) {
        visibleLoading();
        webView = (WebView) rootView.findViewById(R.id.webView);
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
        WebSettings websetting = webView.getSettings();
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        websetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        websetting.setJavaScriptEnabled(true);
        websetting.setSupportZoom(true); 	// 设置可以支持缩放
        websetting.setBuiltInZoomControls(false);		// 设置出现缩放工具
        websetting.setAllowFileAccess(true);
        websetting.setLoadWithOverviewMode(true);
        websetting.setSavePassword(false);//使用Webview时需要关闭webview的自动保存密码功能，防止用户密码被webview明文存储。
        if(loadType.equals(LOAD_HTML_CODE)){
            websetting.setDefaultTextEncodingName("utf-8"); //设置文本编码
            websetting.setAppCacheEnabled(true);
            websetting.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式

            String href = getActivity().getIntent().getStringExtra("href");
            if(!TextUtils.isEmpty(href)){
                //有现成的标签，直接加载
                inVisibleLoading();

                //展示 投票项 分享
                id =getActivity().getIntent().getStringExtra("id");
                shareType =getActivity().getIntent().getIntExtra("shareType",-1);
                if(shareType!=-1){
//                    setHasOptionsMenu(true);
                }
//                shareType = Constant.SHARE_VOTE_PROJECT;
                webView.loadDataWithBaseURL(null,href.toString(), "text/html", "utf-8",null);
            }else {
                setHasOptionsMenu(true);
                getHtmlCode();
            }
        }else{
            websetting.setUseWideViewPort(true);//关键点
            getStaticHtml();
        }

    }


    @Override
    public void loadData() {

    }

    private void getHtmlCode(){
        JSONObject params = new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_BANNER);
        JSONUtil.put(params,"action",Constant.ACTION_BANNER_DETAIL);
        JSONUtil.put(params,"id",id);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                inVisibleLoading();
                BannerVO bannerVO =JSONUtil.fromJson(jsonString, BannerVO.class);
                StringBuilder href=  new StringBuilder();
                String timeAndPeople = Utils.sec2Date(bannerVO.getCreate_time(),"yyyy-MM-dd")+" 发布人："+bannerVO.getRelease_people();
                href.append("<H2>").append(bannerVO.getTitle()).append("</H2>").append("\n").append("<p style=\"color:blue\">").append(timeAndPeople).append("</p>").append("\n").append(bannerVO.getHref());
                webView.loadDataWithBaseURL(null,href.toString(), "text/html", "utf-8",null);
//                WebViewManager manager = new WebViewManager(webView);
//                manager.setWebViewConfig(getActivity(),getBaseActivity().getToolbar());
//                manager.loadUrl(url, this);
            }

            @Override
            public void onCodeError(int key, String message) {
                showTost(message,1);
                visibleNoData();

            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();

            }
        });

    }
    private void getStaticHtml(){
        JSONObject params = new JSONObject();
        JSONUtil.put(params,"model",Constant.MODEL_COMMON);
        JSONUtil.put(params,"action",Constant.ACTION_LOAD_STATIC_HTML);
        JSONUtil.put(params,"type",type);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                inVisibleLoading();
                try {
                    JSONArray arry =new JSONArray(jsonString);
                    JSONObject object = (JSONObject) arry.get(0);
//                    JSONObject object =new JSONObject(jsonString);
                    String url =JSONUtil.getString(object,"url");
                    WebViewManager manager = new WebViewManager(webView);
                    manager.setWebViewConfig(getActivity(),getBaseActivity().getToolbar());
                    manager.loadUrl(url, getActivity());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCodeError(int key, String message) {
                showTost(message,1);
                visibleNoData();

            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.more, menu);
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_more) {
            if (shareType == Constant.SHARE_VOTE_PROJECT) {
                //投票项 分享
                OtherUtil.showMoreOperate(this, id, Utils.isString(getActivity().getIntent().getStringExtra("title")), Utils.isString(getActivity().getIntent().getStringExtra("title")), null,
                        Constant.SHARE_VOTE_PROJECT, -1, "-1",
                        false, null, -1, null);
            } else {
                OtherUtil.showMoreOperate(this, id, Utils.isString(getActivity().getIntent().getStringExtra("title")), Utils.isString(getActivity().getIntent().getStringExtra("title")), null,
                        Constant.SHARE_BANNER, -1, "-1",
                        false, null, -1, null);
            }


        } else {
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDestroy() {
        webView.removeAllViews();
        webView.destroy();
        super.onDestroy();
    }

}
