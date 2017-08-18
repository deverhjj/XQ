package com.biu.modulebase.binfenjiari.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.model.UpdateVO;
import com.biu.modulebase.binfenjiari.service.UpdateService;
import com.biu.modulebase.binfenjiari.widget.DialogFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class CheckUpdate {
	private Context mContext;
	private static final int UPDATE = 1;// 可更新
	private static final int CONTINUE = 2;// 更新进程出现异常或者用户取消更新操作
	private static final int UPDATE_BACKGROUND = 3;// 更新进程在运行
	private Dialog noticeDialog;// 提示用户更新对话框
	private double currentVersionCode;// 当前应用版本号
	private String currentVersionName;// 当前应用版本名称
	private CharSequence updateDesc;// 记录更新描述
	private Handler mHandler;
	protected String url;

	public CheckUpdate(Context context, Handler handler) {
		this.mContext = context;
		this.mHandler = handler;
	}

	/**
	 * 判断Service是否在运行
	 */
	public boolean isServiceRun(Context context, String serviceName) {
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> list = am.getRunningServices(30);
		for (RunningServiceInfo info : list) {
			if (info.service.getClassName().equals(serviceName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate() {
		if (!isServiceRun(mContext.getApplicationContext(), "UpdateService")) {
			update();
//			new Thread() {
//				public void run() {
//					if (isUpdate()) {
//						// 显示提示对话框
//						mHandler.sendEmptyMessage(UPDATE);
//					} else {
//						mHandler.sendEmptyMessage(CONTINUE);
//					}
//				};
//			}.start();
		} else {
			mHandler.sendEmptyMessage(UPDATE_BACKGROUND);
		}
	}

	boolean hasNewVersion =false;

	/**
	 * 检查软件是否有更新版本
	 */
	private void update() {
		try {
			// 获取当前软件版本
			PackageManager pm = mContext.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
			currentVersionName = pi.versionName;
			currentVersionCode = pi.versionCode;

			JSONObject params = new JSONObject();
			params.put("model",Constant.MODEL_UPDATE);
			params.put("action",Constant.ACTION_UPDATE);
			Communications.jsonRequestData(false, params, Constant.SERVERURL, mContext.getClass().getSimpleName(), new RequestCallBack() {
				@Override
				public void onResponse(JSONObject response) {
					if (JSONUtil.getString(response, "key").equals("1")) {
//						JSONObject info = JSONUtil.getJSONObject(response, "result");
						JSONArray info = JSONUtil.getJSONArray(response, "result");
						UpdateVO bean = JSONUtil.fromJson(JSONUtil.getJSONObject(info,0).toString(), UpdateVO.class);
						if (currentVersionCode > 0 && bean != null) {
							int newVersionCode = bean.getSequence();
							if (newVersionCode > currentVersionCode) {
								MyApplication.updateVO = bean;
								hasNewVersion =true;
								// 显示提示对话框
								mHandler.sendEmptyMessage(UPDATE);
							}

						}

					}else{
						mHandler.sendEmptyMessage(CONTINUE);
					}
				}

				@Override
				public void onErrorResponse(String errorInfo) {
					mHandler.sendEmptyMessage(CONTINUE);
				}

				@Override
				public void onUnLogin() {

				}
			});

//			URL url = new URL(Constant.ACTION_UPDATE);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setReadTimeout(3 * 1000);
//			conn.setConnectTimeout(3 * 1000);
//			InputStream inStream = conn.getInputStream();
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			int i = -1;
//			while ((i = inStream.read()) != -1) {
//				baos.write(i);
//			}
//			JSONObject response = new JSONObject(baos.toString());
//			if (response.isNull("result"))
//				return false;
//			if (JSONUtil.getString(response, "key").equals("1")) {
//				JSONObject info = JSONUtil.getJSONObject(response, "data");
//				UpdateVO bean = JSONUtil.fromJson(info.toString(), UpdateVO.class);
//				if (currentVersionCode > 0 && bean != null) {
//					int newVersionCode = bean.getSequence();
//					if (newVersionCode > currentVersionCode) {
//						MyApplication.updateVO = bean;
//						return true;
//					}
//
//				}
//
//			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	/**
	 * 显示软件更新对话框
	 */
	public void showNoticeDialog() {
		DialogFactory.getInstance((Activity) mContext).showDialog(mContext, R.layout.custom_alert_dialog, R.style.CustomDialog,
				R.style.dialog_middle_anim_style, Gravity.CENTER, 0.8f, 0, new DialogFactory.DialogListener() {

					@Override
					public void OnInitViewListener(View v, final Dialog dialog) {
						TextView alertTitle = (TextView) v.findViewById(R.id.alertTitle);
						alertTitle.setText("软件更新");
						TextView message = (TextView) v.findViewById(R.id.message);
						message.setText(MyApplication.updateVO==null?"":MyApplication.updateVO.getUp_content());
						Button button1 = (Button) v.findViewById(R.id.button1);
						button1.setText("稍后更新");
						Button button2 = (Button) v.findViewById(R.id.button2);
						button2.setText("更新");

						v.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();
								MyApplication.allow_update = false;
								mHandler.sendEmptyMessage(CONTINUE);
							}
						});
						v.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();

								Intent intent = new Intent(mContext, UpdateService.class);
								mContext.startService(intent);
								mHandler.sendEmptyMessage(CONTINUE);
							}
						});
					}
				});

	}

}
