package com.biu.modulebase.binfenjiari.adapter;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.biu.modulebase.binfenjiari.util.LogUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by jhj_Plus on 2016/1/22.
 */

public abstract class BaseBannerAdapter extends PagerAdapter implements View.OnClickListener {

    private static final String TAG = "BaseBannerAdapter";

    private int mBannerLayoutId;

    private int mImageViewId;

    private ViewPager mViewPager;
    private final Object mLock = new Object();
    private boolean mCarouselBanner;
    private CarouseBannerHandler mBannerHandler;
    private ScheduledExecutorService mCarouselTask;

    public BaseBannerAdapter(ViewPager viewPager,int bannerLayoutId, int imageViewId) {
        mViewPager=viewPager;
        mBannerLayoutId = bannerLayoutId;
        mImageViewId = imageViewId;
        init();
    }

    public abstract int getBannerCount();

    public abstract void setImage(int position,ImageView banner);

    public abstract void onBannerClicked(int position);

    @Override
    public void onClick(View v) {
        onBannerClicked(mViewPager.getCurrentItem());
    }

    @Override
    public int getCount() {
        return getBannerCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View pageView = container.getChildAt(position);
        Object tag=pageView!=null?pageView.getTag():null;
        if (tag == null || !tag.equals(getTag(position))) {
            pageView = LayoutInflater.from(container.getContext()).inflate(mBannerLayoutId,
                    container, false);
            pageView.setOnClickListener(this);
            pageView.setTag(getTag(position));

            ImageView iv_banner = (ImageView) pageView.findViewById(mImageViewId);
            setImage(position, iv_banner);

            container.addView(pageView);
//            Log.e(TAG, "*************instantiateItem***************>" + position);
        }

//        Log.e(TAG, "***************>" + position);
        return pageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }


    public void notifyCarouselBanner(boolean carouselBanner) {
        LogUtil.LogE(TAG, "********notifyCarouselBanner *******  " + carouselBanner);
        if (mCarouselTask == null && carouselBanner) {
            LogUtil.LogE(TAG,
                    "********CarouselBannerTask Dead,Restarting*******  " + carouselBanner);
            init();
        }
        mCarouselBanner = carouselBanner;
        if (carouselBanner && mBannerHandler != null) {
            mBannerHandler.obtainMessage().sendToTarget();
        }
    }

    public void notifyShutdownCarouselTask() {
        if (mCarouselTask != null) {
            LogUtil.LogE(TAG, "******** notifyShutdownCarouselTask *******  ");
            mCarouselTask.shutdownNow();
            mCarouselTask = null;
            mBannerHandler = null;
        }
    }

    private void init() {
        LogUtil.LogE(TAG,"********init *******  ");
        mBannerHandler = new CarouseBannerHandler();
        mCarouselTask = Executors.newScheduledThreadPool(1);
        mCarouselTask.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (mLock) {
                        Log.i(TAG, "scheduleWithFixedDelay------------>" + mCarouselBanner);
                        if (mCarouselBanner && mBannerHandler != null) {
                            mBannerHandler.obtainMessage().sendToTarget();
                            LogUtil.LogE(TAG,"scheduleWithFixedDelay wait");
                            mLock.wait();
                        }
                    }
                    // Log.e(TAG,"------------run----------------");
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }, 3000, 3000, TimeUnit.MILLISECONDS);
    }


    private class CarouseBannerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage------------>" + mCarouselBanner);
            if (mViewPager!=null && getCount()!=0) {
                int currentItem = mViewPager.getCurrentItem();
                int toPos=++currentItem % getCount();
                mViewPager.setCurrentItem(toPos, toPos != 0);
                synchronized (mLock) {
                    LogUtil.LogE(TAG,"handleMessage notifyAll");
                    mLock.notifyAll();
                }
            }
        }
    }

    private String getTag(int position){
        return getClass().getSimpleName()+"#"+position;
    }
}

