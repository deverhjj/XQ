package com.biu.modulebase.binfenjiari.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class DevTokenService2 extends Service {

	private Timer timer;

	private String umengToken ="";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// code to handle to create service
		// ......
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// code to handler to start service
		// ......
		umengToken = MyApplication.deviceToken;
		timer = new Timer();
		// timer.schedule(new sendDevTokenTask(), 5000);// 5秒后启动任务

		SendDevTokenTask secondTask = new SendDevTokenTask();
		timer.schedule(secondTask, 1000, 5000);// 5秒后启动任务,以后每隔3秒执行一次线程

		// Date date = new Date();
		// timer.schedule(new OneTask(3), new Date(date.getTime() + 1000));//
		// 以date为参数，指定某个时间点执行线程
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public class SendDevTokenTask extends TimerTask {

		@Override
		public void run() {
			if (PreferencesUtils.getBoolean(getApplicationContext(), PreferencesUtils.KEY_UMENG_TOKEN)) {
				timer.cancel();
				stopSelf();
				this.cancel();
			} else {
				if(umengToken ==null){
					umengToken = MyApplication.mPushAgent.getRegistrationId();
				}
				LogUtil.LogE("【DEV_token:】" + umengToken);
				sedDevToken();
			}


		}
	}

	private void sedDevToken() {
		JSONObject params = new JSONObject();
		String token = PreferencesUtils.getString(getApplicationContext(), "token");
		if (Utils.isEmpty(umengToken) || Utils.isEmpty(token))
			return;
		try {
			params.put("model", Constant.MODEL_REG);
			params.put("action", Constant.ACTION_UP_UMENG_TOKEN);
			params.put("token", token);
			params.put("send_token",umengToken);
			params.put("type", "2");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Communications.jsonRequestData(true, params, Constant.SERVERURL, getClass().getSimpleName(), new RequestCallBack() {
			@Override
			public void onResponse(JSONObject response) {
				LogUtil.LogE("【DEV_token:】" + response.toString());
				String key = JSONUtil.getString(response, "key");
				if (key.equals("1")) {
					String tomorrowDateAtZeroAM =Utils.getTomorrowDateAtZeroAM();
					PreferencesUtils.putString(getApplicationContext(),  PreferencesUtils.KEY_UMENG_TOKEN_TOMORROW_ZERO_AM, tomorrowDateAtZeroAM);
					PreferencesUtils.putBoolean(getApplicationContext(),  PreferencesUtils.KEY_UMENG_TOKEN, true);
					PreferencesUtils.putBoolean(getApplicationContext(), "dev_token", true);
				} else {

				}
			}

			@Override
			public void onErrorResponse(String errorInfo) {

			}

			@Override
			public void onUnLogin() {
				timer.cancel();
				stopSelf();
			}
		});
	}

	class MyBinder extends Binder implements DevTokenListener {

		@Override
		public void onDevTokenSendSuccess() {
			// TODO Auto-generated method stub

		}

	}

	interface DevTokenListener {
		void onDevTokenSendSuccess();
	}

}
