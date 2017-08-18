package com.biu.modulebase.binfenjiari.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.biu.modulebase.binfenjiari.communication.Communications;
import com.biu.modulebase.binfenjiari.communication.RequestCallBack;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.util.JSONUtil;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.util.PreferencesUtils;
import com.biu.modulebase.binfenjiari.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 获取敏感词汇
 *
 * 每天获取一次数据
 */
public class SensitiveWordsService extends Service {

	private Timer timer;

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
//		if(Utils.dateComparator2(Utils.getCurrentDate2(),PreferencesUtils.getString(getApplicationContext(),PreferencesUtils.KEY_SENSITIVE))){
//
//		}
		timer = new Timer();
		// timer.schedule(new sendDevTokenTask(), 5000);// 5秒后启动任务

		SendDevTokenTask secondTask = new SendDevTokenTask();
		timer.schedule(secondTask, 1000, 10000);// 1秒后启动任务,以后每隔3秒执行一次线程

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
			if (PreferencesUtils.getBoolean(getApplicationContext(), PreferencesUtils.KEY_SENSITIVE)) {
				timer.cancel();
				stopSelf();
				this.cancel();
			} else {
				getKeyList();
			}

		}
	}

	private void getKeyList(){
		JSONObject params = new JSONObject();
		try {
			params.put("model", Constant.MODEL_CIRCLE);
			params.put("action",Constant.ACTION_GET_KEY_LIST);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Communications.jsonRequestData(false, params, Constant.SERVERURL, getClass().getSimpleName().toString(),  new RequestCallBack() {
			@Override
			public void onResponse(JSONObject response) {
				LogUtil.LogD("【sensitive words:】" + response.toString());
				String key = JSONUtil.getString(response, "key");
				if (key.equals("1")) {
					JSONArray array =JSONUtil.getJSONArray(response,"result");
					String tomorrowDateAtZeroAM = Utils.getTomorrowDateAtZeroAM();
					PreferencesUtils.putString(getApplicationContext(),  PreferencesUtils.KEY_SENSITIVE_STRING, array.toString());
					PreferencesUtils.putString(getApplicationContext(),  PreferencesUtils.KEY_TOMORROW_ZERO_AM, tomorrowDateAtZeroAM);
					PreferencesUtils.putBoolean(getApplicationContext(),  PreferencesUtils.KEY_SENSITIVE, true);
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
