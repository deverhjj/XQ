package com.biu.modulebase.binfenjiari.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.CommVoteDetailNewActivity;
import com.biu.modulebase.binfenjiari.activity.VoteRankActivity;
import com.biu.modulebase.binfenjiari.communication.ContextRequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.model.NewVoteBeanVO;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.OtherUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.common.base.BaseFragment;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/6/13
 */
public class WebViewVoteDetailFragment extends BaseFragment {
    /**
     * 资讯详情
     **/
    public final static int LOAD_TYPE_APP_FINDINFOMATIONINFO = 1;
    /**
     * 投票项详情
     **/
    public final static int LOAD_TYPE_APP_FINDVOTEPROJECTINFO = 2;

    private WebView webView;
    /**
     * 加载类型
     **/
    private String loadType;
    /**
     * 请求静态Html 类型 2：用户协议 4：发帖必读
     **/
    private int type;

    private String id, project_id;
    String title;

    private myWebChromeClient mWebChromeClient;
    private FrameLayout customViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;


    private boolean isVideoFullscreen =false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_webview, container, false);
        return super.onCreateView(inflater, rootView, savedInstanceState);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getActivity().getIntent();
//        loadType= intent.getStringExtra("loadType");
        id = intent.getStringExtra(Constant.KEY_ID);
        project_id = intent.getStringExtra("project_id");
//            bannerVO = (BannerVO) intent.getSerializableExtra("bannerVO");
        type = intent.getIntExtra(Constant.KEY_TYPE, 0);

        if (type == 0 || TextUtils.isEmpty(id)) {
            getActivity().finish();
        }
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initView(View rootView) {
        visibleLoading();
        customViewContainer = (FrameLayout)rootView.findViewById(R.id.customViewContainer);
        webView = (WebView) rootView.findViewById(R.id.webView);
        //android webview组件包含3个隐藏的系统接口：searchBoxJavaBridge_, accessibilityTraversal以及accessibility，恶意程序可以利用它们实现远程代码执行。通过显示调用removeJavascriptInterface移除这三个系统隐藏接口。
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
        mWebChromeClient = new myWebChromeClient();

        WebSettings websetting = webView.getSettings();
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        websetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        websetting.setJavaScriptEnabled(true);
        websetting.setSupportZoom(true);    // 设置可以支持缩放
        websetting.setBuiltInZoomControls(false);        // 设置出现缩放工具
        websetting.setAllowFileAccess(true);
        websetting.setLoadWithOverviewMode(true);
//        if(loadType.equals(LOAD_HTML_CODE)){
        websetting.setDefaultTextEncodingName("utf-8"); //设置文本编码
        websetting.setAppCacheEnabled(true);
        websetting.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式
        websetting.setSavePassword(false);//使用Webview时需要关闭webview的自动保存密码功能，防止用户密码被webview明文存储。
        webView.setWebChromeClient(mWebChromeClient );
        if (type == LOAD_TYPE_APP_FINDINFOMATIONINFO) {
            getInfomationCodeHtml();
        } else if (type == LOAD_TYPE_APP_FINDVOTEPROJECTINFO) {
            getVoteDetailHtml();
            //设置本地调用对象及其接口
            webView.addJavascriptInterface(new JavaScriptObject(this.getContext()), "JsTest");
        }
//        }else{
//            websetting.setUseWideViewPort(true);//关键点
//            getStaticHtml();

    }
    private View mCustomView;
    class myWebChromeClient extends WebChromeClient {
        private Bitmap mDefaultVideoPoster;
        private View mVideoProgressView;

        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            onShowCustomView(view, callback);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onShowCustomView(View view,CustomViewCallback callback) {

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            webView.setVisibility(View.GONE);
            customViewContainer.setVisibility(View.VISIBLE);
            customViewContainer.addView(view);
            customViewCallback = callback;
            isVideoFullscreen =true;
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        @Override
        public View getVideoLoadingProgressView() {

            if (mVideoProgressView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                mVideoProgressView = inflater.inflate(R.layout.video_progress, null);
            }
            return mVideoProgressView;
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();    //To change body of overridden methods use File | Settings | File Templates.
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (mCustomView == null)
                return;
           hideCustomView();
        }
    }

    private void hideCustomView(){
        isVideoFullscreen =false;
        webView.setVisibility(View.VISIBLE);
        customViewContainer.setVisibility(View.GONE);

        // Hide the custom view.
        mCustomView.setVisibility(View.GONE);

        // Remove the custom view from its container.
        customViewContainer.removeView(mCustomView);
        customViewCallback.onCustomViewHidden();

        mCustomView = null;
    }

    class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
    @Override
    public void loadData() {

    }

    /**
     * 资讯详情
     */
    private void getInfomationCodeHtml() {
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", "NewVote");
        JSONUtil.put(params, "action", "app_findInfomationInfo");
        JSONUtil.put(params, "id", id);
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                inVisibleLoading();
                try {
                    JSONObject json = new JSONObject(jsonString);
                    String href = json.getString("href");
                    webView.loadDataWithBaseURL(null, href, "text/html", "utf-8", null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCodeError(int key, String message) {
                showTost(message, 1);
                visibleNoData();

            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();

            }
        });

    }

    /**
     * 投票项详情
     */
    private void getVoteDetailHtml() {
        Intent intent=getActivity().getIntent();
        int type1=intent.getIntExtra("type1",1);
        String aa=intent.getStringExtra("???");
        JSONObject params = new JSONObject();
        JSONUtil.put(params, "model", "NewVote");
        JSONUtil.put(params, "action", "app_findVoteProjectInfo");
        JSONUtil.put(params, "id", id);
        JSONUtil.put(params, "type", type1+"");
        JSONUtil.put(params, "dev_type", "2");
        if (!TextUtils.isEmpty(PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN))) {
            JSONUtil.put(params, "token", PreferencesUtils.getString(getActivity(), PreferencesUtils.KEY_TOKEN));
        }
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                inVisibleLoading();
//                    JSONObject json = new JSONObject(jsonString);
//                    String href = json.getString("href");
                webView.loadDataWithBaseURL(null, jsonString, "text/html", "utf-8", null);

            }

            @Override
            public void onCodeError(int key, String message) {
                showTost(message, 1);
                visibleNoData();

            }

            @Override
            public void onConnectError(String message) {
                visibleNoNetWork();

            }
        });

    }

    /**
     * 我要投票
     */
    private void doVoteProcess(){
        if(TextUtils.isEmpty(project_id))
            return;
        showProgress(getClass().getSimpleName());

        JSONObject params =new JSONObject();
        JSONUtil.put(params,"model","NewVote");
        JSONUtil.put(params,"action","app_findNewVoteInfo");
        JSONUtil.put(params,"id",project_id);
        JSONUtil.put(params,"token", PreferencesUtils.getString(getActivity(),PreferencesUtils.KEY_TOKEN));
        jsonRequest(false, params, Constant.SERVERURL, getClass().getSimpleName(), new ContextRequestCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                dismissProgress();
                CommVoteDetailNewActivity.sNewVoteBeanVOList = new ArrayList<NewVoteBeanVO>();
                NewVoteBeanVO bean = JSONUtil.fromJson(jsonString,NewVoteBeanVO.class);
                CommVoteDetailNewActivity.sNewVoteBeanVOList.add(bean);

                JSONArray voteArry = null;
                try {
                    voteArry = JSONUtil.getJSONArray(new JSONObject(jsonString), "viceVote");
                    List<NewVoteBeanVO> hotList = JSONUtil.fromJson(voteArry.toString(), new TypeToken<List<NewVoteBeanVO>>() {
                    }.getType());
                    CommVoteDetailNewActivity.sNewVoteBeanVOList.addAll(hotList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                CommVoteDetailNewActivity.projectId = project_id;
                if(CommVoteDetailNewActivity.sNewVoteBeanVOList==null || CommVoteDetailNewActivity.sNewVoteBeanVOList.size()==0)
                    return;
                if(CommVoteDetailNewActivity.sNewVoteBeanVOList.get(0).getIsopen()==2){
                    showTost("投票已结束",1);
                    return;
                }
                Intent intent = new Intent(getContext(),CommVoteDetailNewActivity.class);
                intent.putExtra(Constant.KEY_POSITION, 0);
                startActivity(intent);

            }

            @Override
            public void onCodeError(int key, String message) {
                dismissProgress();
                showTost(message,1);

            }

            @Override
            public void onConnectError(String message) {
                dismissProgress();
                visibleNoNetWork();
            }
        });
    }


    public class JavaScriptObject {
        Context mContxt;

        public JavaScriptObject(Context mContxt) {
            this.mContxt = mContxt;
        }

        @JavascriptInterface
        public void seeRank() {
            //查看排行榜
            if(TextUtils.isEmpty(project_id))
                return;
            Intent intent1=new Intent(getActivity(), VoteRankActivity.class);
            title=getActivity().getIntent().getStringExtra("title1");

            intent1.putExtra("title",title);

            intent1.putExtra("project_id",project_id);
            startActivity(intent1);

//            getBaseActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    showTost("seeRank",1);
//
//                }
//            });
        }

        @JavascriptInterface
        public void doVote() {

            if(Utils.isEmpty(PreferencesUtils.getString(getContext(), PreferencesUtils.KEY_TOKEN))|| MyApplication.getUserInfo(getContext())==null) {
                showUnLoginSnackbar();
                return;
            }
            doVoteProcess();

//            getBaseActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    showTost("doVote",1);
//
//                }
//            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.more, menu);
    }

    /**
     * 举报的类型问题
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_more) {
            if (type == LOAD_TYPE_APP_FINDINFOMATIONINFO) {
                OtherUtil.showMoreOperate(WebViewVoteDetailFragment.this, id, "资讯", Utils.isString(getActivity().getIntent().getStringExtra("content")), null,
                        Constant.SHARE_VOTE_BREAF_INFO, -1, "-1",
                        false, null, -1, null);
                String title = getActivity().getIntent().getStringExtra("title");
                Log.e("title----->", title + "?????");
                String project_title = getActivity().getIntent().getStringExtra("project_title");
                Log.e("project_title----->", project_title + "?????");

            } else if (type == LOAD_TYPE_APP_FINDVOTEPROJECTINFO) {
                OtherUtil.showMoreOperate(WebViewVoteDetailFragment.this, id, Utils.isString(getActivity().getIntent().getStringExtra("project_title")), Utils.isString(getActivity().getIntent().getStringExtra("content")), null,
                        Constant.SHARE_VOTE_PROJECT, -1, "-1",
                        false, null, -1, null);
                String title = getActivity().getIntent().getStringExtra("title");
                String project_title = getActivity().getIntent().getStringExtra("project_title");
                Log.e("title----->", title + "?????");
                Log.e("project_title----->", project_title + "?????");
            }


        } else {
        }
        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
//                if (customView != null) {
//                    hideCustomView();
//                } else if (webView.canGoBack()) {
//                    webView.goBack();
//                } else {
//                    finish();
//                }
//                return true;
//            default:
//                return super.onKeyUp(keyCode, event);
//        }
//    }

    public boolean onBackPressed() {
        if (isVideoFullscreen) {
            hideCustomView();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        webView.removeAllViews();
        webView.destroy();
        super.onDestroy();
    }
}
