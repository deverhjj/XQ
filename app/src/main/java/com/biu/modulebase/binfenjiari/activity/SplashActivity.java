package com.biu.modulebase.binfenjiari.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.biu.modulebase.binfenjiari.R;


/**
 * 开屏页
 *
 */
public class SplashActivity extends AppCompatActivity {

	private static final int sleepTime = 2000;

	private boolean hasType = false;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// Translucent navigation bar
//			window.setFlags(
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}

		setContentView(R.layout.activity_splash);
	}

	@Override
	protected void onStart() {
		new Thread(new Runnable() {
			public void run() {
//				if (PreferencesUtils.getBoolean2(getApplicationContext(), "isFirstRun")) {
					// 等待sleeptime时长
					long start = System.currentTimeMillis();
					long costTime = System.currentTimeMillis() - start;
					// 等待sleeptime时长
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
//					startActivity(new Intent(SplashActivity.this, GuideActivity.class));
//				} else {
//					startActivity(new Intent(SplashActivity.this, MainActivity.class));
//				}
				finish();

			}
		}).start();
		super.onStart();

	}

}
