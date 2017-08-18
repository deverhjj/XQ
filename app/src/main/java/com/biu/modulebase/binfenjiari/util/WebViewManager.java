package com.biu.modulebase.binfenjiari.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//import org.apache.http.util.EncodingUtils;

import java.net.MalformedURLException;
import java.net.URL;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
public class WebViewManager {

	private static final String Tag = "WebViewManager";
	public WebView webView;
//	public RelativeLayout loading;
	private Toolbar titleText;
	private String cookieStr;
	private SharedPreferences sharedPreferences;
//	protected String sessionId;
	private Context context;
//	private ImageButton favButton;
//	private RelativeLayout locBar;
	private String pathUrl;
//	private Button rightButton;

	public WebViewManager(WebView webView) {
		this.webView = webView;
	}
	
	public void setWebViewConfig(Context context,Toolbar titleText) {
		this.context = context;
		this.titleText = titleText;
		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new MyWebChromeClient());

	}

//	public void setWebViewConfig(TextView titleText, RelativeLayout loading,
//			ImageButton favButton) {
//		
//		this.titleText = titleText;
//		this.loading = loading;
//		this.favButton = favButton;
//		webView.setWebViewClient(new MyWebViewClient());
//		webView.setWebChromeClient(new MyWebChromeClient());
//
//	}

//	public void setWebViewConfig(TextView titleText, RelativeLayout loading,
//			ImageButton favButton, RelativeLayout locBar) {
//		
//		this.titleText = titleText;
//		this.loading = loading;
//		this.favButton = favButton;
//		this.locBar = locBar;
//		webView.setWebViewClient(new MyWebViewClient());
//		webView.setWebChromeClient(new MyWebChromeClient());
//
//	}
	
//	public void setWebViewConfig(TextView titleText, Button rightButton,
//			RelativeLayout loading) {
//
//		this.titleText = titleText;
//		this.loading = loading;
//		this.rightButton = rightButton;
//		webView.setWebViewClient(new MyWebViewClient());
//		webView.setWebChromeClient(new MyWebChromeClient());
//
//	}
//	public void setBtnWebViewConfig(TextView titleText, Button rightButton,
//			RelativeLayout loading) {
//
//		this.titleText = titleText;
//		this.loading = loading;
//		this.rightButton = rightButton;
//		webView.setWebViewClient(new MyWebViewClient());
//		webView.setWebChromeClient(new MyBtnWebChromeClient());
//
//	}

	public void loadUrl(String webUrl, Context context) {
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		Log.i(Tag, "【url】=" + webUrl);
		try {
			String[] url = new URL(webUrl).getFile().split("\\?");
			this.pathUrl = url[0];
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.context = context;
		webView.loadUrl(webUrl);
	}
	
	public void loadUrl(String webUrl, String param,Context context) {
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Log.i(Tag, "【url】=" + webUrl);
		this.context = context;
//		webView.postUrl(webUrl, EncodingUtils.getBytes(param, "base64"));
//		webView.loadUrl(webUrl);
	}

	public void loadUrl(String webUrl, Context context,
			final String sessionUrl, boolean isVerify) {
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		Log.i(Tag, "【url】=" + webUrl);

		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		// cookieManager.removeSessionCookie();

		Log.d(Tag, "cookieStr=" + cookieStr);
		cookieManager.setCookie(webUrl, cookieStr);
		CookieSyncManager.getInstance().sync();
		String newSession = cookieManager.getCookie(webUrl);

		Log.d(Tag, ">>>【newSession】" + newSession);
		Log.d(Tag, ">>>【cookieStr】" + cookieStr);
		if (cookieStr != null && (cookieStr).equalsIgnoreCase(newSession)) {
			Log.e(Tag, "【验证通过】");
			webView.loadUrl(webUrl);
		} else {
			Log.e(Tag, "【验证不通过】");
			String urltest = "";
			webView.loadUrl(urltest);
		}

		boolean isCertification = true;
		if (isCertification) {

		}
	}

//	public void verifySession(Context context, final String sessionUrl) {
//		
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				
//				String result = CommonUtil.getResponse(sessionUrl);
//				if (result != null) {
//					try {
//						JSONObject obj = new JSONObject(result);
//						sessionId = obj.getString("sessionId");
//						Log.d(Tag, "sessionId=" + sessionId);
//						mHandler.sendEmptyMessage(0);
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//
//				}
//			}
//		}).start();
//
//	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				break;
			default:
				break;
			}
		}
	};

	public String getCookie() {
		return cookieStr;
	}

	private class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onReceivedTitle(WebView view, String title) {
			
			super.onReceivedTitle(view, title);
//			titleText.setTitle(title);
		
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			
			// final AlertDialog.Builder builder = new AlertDialog.Builder(
			// view.getContext());
			// builder.setTitle("对话框").setMessage(message)
			// .setPositiveButton("确定", null);
			// AlertDialog dialog = builder.create();
			// dialog.show();
			result.confirm();
			Log.e(Tag,"【message】"+message);
			
			// return super.onJsAlert(view, url, message, result);
			return true;
		}

	}
	
	private class MyBtnWebChromeClient extends WebChromeClient {

		@Override
		public void onReceivedTitle(WebView view, String title) {
			
			super.onReceivedTitle(view, title);
			titleText.setTitle(title);
		
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			
			// final AlertDialog.Builder builder = new AlertDialog.Builder(
			// view.getContext());
			// builder.setTitle("对话框").setMessage(message)
			// .setPositiveButton("确定", null);
			// AlertDialog dialog = builder.create();
			// dialog.show();
			result.confirm();
			Log.e(Tag,"【message】"+message);
			
			// return super.onJsAlert(view, url, message, result);
			return true;
		}

	}

	// Web视图
	private class MyWebViewClient extends WebViewClient {
		private String url;

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {		
			super.onReceivedError(view, errorCode, description, failingUrl);
			this.url = failingUrl;
			if(failingUrl.contains("")){
				view.loadUrl(failingUrl);
			}
			Log.e(Tag,"receiveError="+failingUrl);
		}

		// 打开完成后关闭loading界面 类别选择面板
		public void onPageFinished(WebView view, String url) {
			Log.i(Tag, "【当前页面url】" + url);
			
	
			super.onPageFinished(view, url);
//			loading.setVisibility(View.GONE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
//			if (rightButton != null)
//				rightButton.setVisibility(View.GONE);
//			loading.setVisibility(View.VISIBLE);
//			String path = null;
//
//			try {
//				path = new URL(url).getPath();
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//			if (locBar != null && !pathUrl.equals(path)) {
//				locBar.setVisibility(View.GONE);
//			} else if (locBar != null && pathUrl.equals(path)) {
//				locBar.setVisibility(View.VISIBLE);
//			}
		}
	}
}
