package com.biu.modulebase.binfenjiari.service;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.datastructs.MyApplication;
import com.biu.modulebase.binfenjiari.util.Utils;

import java.io.File;
import java.io.IOException;

public class UpdateService extends Service {

	/** 安卓系统下载类 **/
	DownloadManager manager;

	/** 接收下载完的广播 **/
	DownloadCompleteReceiver receiver;

	private String apkName = Constant.APK_NAME;

	/** 初始化下载器 **/
	private void initDownManager(String url) {

		manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

		receiver = new DownloadCompleteReceiver();

		// 设置下载地址
		Request down = new Request(
				Uri.parse(url.trim()));

		// 设置允许使用的网络类型，这里是移动网络和wifi都可以
		down.setAllowedNetworkTypes(Request.NETWORK_MOBILE
				| Request.NETWORK_WIFI);

		// 下载时，通知栏显示途中
		down.setNotificationVisibility(Request.VISIBILITY_VISIBLE);

		// 显示下载界面
		down.setVisibleInDownloadsUi(true);

		// 判断是否存在SD卡
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			down.setDestinationInExternalFilesDir(this,
					Constant.STORAGE_HOME_PATH, apkName);
		} else {
			// 设置下载后文件存放的位置
			String path = Environment.DIRECTORY_DOWNLOADS;
			String permission="666";

			try {
			    String command = "chmod " + permission + " " + path;
			    Runtime runtime = Runtime.getRuntime();
			    runtime.exec(command);
			} catch (IOException e) {
			    e.printStackTrace();
			}
			down.setDestinationInExternalFilesDir(this,path, apkName);
		}

		// 将下载请求放入队列
		manager.enqueue(down);

		// 注册下载广播
		registerReceiver(receiver, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(MyApplication.getUpdateVO()!=null){
			String url = MyApplication.getUpdateVO().getUp_url();
			if (!Utils.isEmpty(url)) {
				// 调用下载
				initDownManager(url);
			}
		}else{
			Toast.makeText(getApplicationContext(),"更新数据格式错误，请稍后再试...",Toast.LENGTH_SHORT);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {

		// 注销下载广播
		if (receiver != null)
			unregisterReceiver(receiver);

		super.onDestroy();
	}

	// 接受下载完成后的intent
	class DownloadCompleteReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
				long downloadId = intent.getLongExtra(
						DownloadManager.EXTRA_DOWNLOAD_ID, 0);
				DownloadManager.Query query = new DownloadManager.Query();
				query.setFilterById(downloadId);
				DownloadManager dm = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
				Cursor downloadResult = dm.query(query);

				if (downloadResult.moveToFirst()) {
					int statusColumnIndex = downloadResult.getColumnIndex(DownloadManager.COLUMN_STATUS);
					int status = downloadResult.getInt(statusColumnIndex);

					if (status == DownloadManager.STATUS_SUCCESSFUL) {
						//download completed successfully
						int localFileNameId = downloadResult.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);

						String downloadPathFile = downloadResult.getString(localFileNameId);

						Intent intents = new Intent(Intent.ACTION_VIEW);
						intents.setDataAndType(Uri.fromFile(new File(downloadPathFile)), "application/vnd.android.package-archive");
						intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intents);
						// 停止服务并关闭广播
						UpdateService.this.stopSelf();
					}
				}
			}

		}


	}
}