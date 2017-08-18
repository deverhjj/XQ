package com.biu.modulebase.binfenjiari.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.widget.photoview.PhotoView;
import com.biu.modulebase.common.base.BaseActivity;

import java.util.ArrayList;

public class PhotoViewActivity extends BaseActivity implements OnClickListener {

	private ArrayList<String> imgs = new ArrayList<String>();
	private ImageView[] mImageViews;
	private int position;

	@Override
	protected Fragment getFragment() {
		return null;
	}

	@Override
	protected String getToolbarTitle() {
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photoview);
		getIntentData();
		initView();
	}

	private void getIntentData() {
		Bundle bunlde = getIntent().getExtras();
		position = bunlde.getInt("position");
		imgs = bunlde.getStringArrayList("imgs");
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		mImageViews = new ImageView[imgs.size()];

		ViewPager mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		final TextView curr_position = (TextView) findViewById(R.id.curr_position);
		TextView total = (TextView) findViewById(R.id.total);
		curr_position.setText(position + 1 + "");
		total.setText("/" + imgs.size());
		mViewPager.setAdapter(new PagerAdapter() {

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				PhotoView imageView = new PhotoView(PhotoViewActivity.this);
				imageView.setScaleType(ScaleType.FIT_CENTER);
				imageView.enable();
				ImageDisplayUtil.displayImage(Constant.IMG_SOURCE,imgs.get(position),imageView,ImageDisplayUtil.DISPLAY_BIG_IMAGE);
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
				container.addView(imageView);
				mImageViews[position] = imageView;

				return imageView;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(mImageViews[position]);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return imgs.size();
			}
		});
		mViewPager.setCurrentItem(position);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				curr_position.setText(arg0 + 1 + "");

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		mViewPager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		findViewById(R.id.main).setOnClickListener(this);

	}
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.fade_out_quick);
	}

	@Override
	public void onClick(View v) {
		finish();
		int i = v.getId();
		if (i == R.id.main) {
			finish();

		}

	}

}
