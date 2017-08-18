package com.binfenjiari.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.binfenjiari.R;
import com.biu.modulebase.binfenjiari.util.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/12
 */
public class SliderViewBanner extends FrameLayout {

    /**
     * Banner上一个被选中的小圆点的索引，默认值为0
     */
    private int preDotPositionBanner = 0;
    /**
     * Banner的切换下一个page的间隔时间
     */
    private long scrollTimeOffset = 4000;
    private MyHandler mHandler;
    private int currentPage = 0;
    /**
     * 自动轮播viewpager
     **/
    private static final int AUTO_CHANGE_VIEWPAGER = 100;

    private RelativeLayout bannersliderview_container;

    private ViewPager bannerViewPager;

    private TextView bannersliderview_tv_banner_text_desc;

    private LinearLayout bannersliderview_ll_dot_group_banner;

    public SliderViewBanner(Context context) {
        this(context, null);
    }

    public SliderViewBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SliderViewBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.widget_banner_silder_view, this);
        bannersliderview_container = (RelativeLayout) view.findViewById(R.id.bannersliderview_container);
        bannerViewPager = (ViewPager) view.findViewById(R.id.bannersliderview_viewpager);
        bannersliderview_tv_banner_text_desc = (TextView) view.findViewById(R.id.bannersliderview_tv_banner_text_desc);
        bannersliderview_ll_dot_group_banner = (LinearLayout) view.findViewById(R.id.bannersliderview_ll_dot_group_banner);

//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView);
//        boolean refreshAble = typedArray.getBoolean(R.styleable.RefreshRecyclerView_refresh_able, true);
//        loadMoreAble = typedArray.getBoolean(R.styleable.RefreshRecyclerView_load_more_able,true);
//        if(!refreshAble){
//            mSwipeRefreshLayout.setEnabled(false);
//        }

        initData();
    }

    public void initData(){
        List<String> datas = new ArrayList<>();
        datas.add("1");datas.add("2");datas.add("3");
        loadBannerData(datas);
    }

    public void setBannerHeight(Activity activity){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bannerViewPager.getLayoutParams();
        layoutParams.height = Utils.getScreenWidth(activity)/5*3;
        bannerViewPager.setLayoutParams(layoutParams);
    }

    public int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public void loadBannerData(List<String> banners) {

        int size = banners.size();
        if (bannersliderview_ll_dot_group_banner.getChildCount() == size) {
            return;
        }
        for (int i = 0; i < size; i++) {

            // 每循环一次添加一个点到线行布局中
            View dot = new View(getContext());
            dot.setBackgroundResource(R.drawable.selector_banner_dot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(getContext(), 6), dp2px(getContext(), 6));
            params.leftMargin = 10;
            dot.setEnabled(false);
            dot.setLayoutParams(params);
            bannersliderview_ll_dot_group_banner.addView(dot); // 向线性布局中添加"点"
        }
        BannerAdapter bannerAdapter = new BannerAdapter(bannersliderview_ll_dot_group_banner, banners);
        bannerViewPager.setAdapter(bannerAdapter);
        bannerViewPager.addOnPageChangeListener(bannerAdapter);

        bannersliderview_ll_dot_group_banner.getChildAt(0).setSelected(true);
        bannerViewPager.setCurrentItem(0);
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
        mHandler = new MyHandler(bannerViewPager);
        startBanner();
    }


    /**
     * ViewPager的适配器
     */
    private class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private LinearLayout llDotGroup;

        private List<String> banners;

        public BannerAdapter(LinearLayout llDotGroup, List<String> banners) {
            this.llDotGroup = llDotGroup;
            this.banners = banners;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            int index = position % banners.size();
            View view = View.inflate(getContext(), R.layout.widget_banner_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_banner);
            final String banneritem = banners.get(index);
//            Communications.setNetImage(banneritem.getImgUrl(), imageView, Communications.TAG_IMG_DEFAULT);
//            Glide.with(getContext()).load(Constant.IMAGE_URL + banneritem.getImg()).into(imageView);
//            ImageDisplayUtil.LoadNetImage(imageView, getContext(), banneritem.getImg());
            // 为每一个page添加点击事件

            view.setOnClickListener(new MbanOnclickListener(banneritem));
//            ViewGroup v = (ViewGroup) view.getParent();
//            if (v != null) {
//                v.removeView(view);
//            }else{
//                Log.e("Allen", String.valueOf(position + 1));
//            }
            container.addView(view);
            return view;
        }

        public class MbanOnclickListener implements View.OnClickListener {

            String mfocus;

            public MbanOnclickListener(String mfocus) {
                this.mfocus = mfocus;
            }

            @Override
            public void onClick(View v) {
                if (mfocus == null)
                    return;
//                if (mfocus.getType() == 1) {
//                    Intent intent = new Intent(getActivity(), BehaviourDetailActivity.class);
//                    intent.putExtra(Constant.KEY_ID, mfocus.getActivityId());
//                    getActivity().startActivity(intent);
//                } else {
////                    showTost("富文本",1);
//                    Intent intent = new Intent(getActivity(), WebViewMessageActivity.class);
//                    intent.putExtra(Constant.KEY_TYPE, WebViewMessageActivity.TYPE_RICH_MESSAGE_BANNER);
//                    intent.putExtra(Constant.KEY_DATA, mfocus.getInfo());
//                    getActivity().startActivity(intent);
//                }

            }
        }

        @Override
        public int getCount() {
            if (banners.size() > 1) {
                return Integer.MAX_VALUE;
            } else {
                return 1;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // 取余后的索引，得到新的page的索引
            int newPositon = position % banners.size();
            // 根据索引设置图片的描述
//            tvBannerTextDesc.setText(bannerTextDescArray[newPositon]);
            // 把上一个点设置为被选中
            llDotGroup.getChildAt(preDotPositionBanner).setSelected(false);
            // 根据索引设置那个点被选中
            llDotGroup.getChildAt(newPositon).setSelected(true);
            // 新索引赋值给上一个索引的位置
            preDotPositionBanner = newPositon;
        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class MyHandler extends Handler {
        private ViewPager mViewPager;

        public MyHandler(ViewPager mViewPager) {
            this.mViewPager = mViewPager;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUTO_CHANGE_VIEWPAGER:
                    if (mViewPager.getAdapter().getCount() > 1) {
                        int newIndex = mViewPager.getCurrentItem() + 1;
                        mViewPager.setCurrentItem(newIndex, true);
                        currentPage = newIndex;
                        // 每4秒钟发送一个message，用于切换viewPager中的图片
                        this.sendEmptyMessageDelayed(AUTO_CHANGE_VIEWPAGER, scrollTimeOffset);
                    }
                    break;
            }
            super.handleMessage(msg);
        }

        public ViewPager getmViewPager() {
            return mViewPager;
        }

    }

    private void startBanner() {
        if (mHandler == null)
            return;
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(AUTO_CHANGE_VIEWPAGER, scrollTimeOffset);
    }

    public void onBannerResume() {
        if (mHandler != null) {
            mHandler.getmViewPager().setCurrentItem(currentPage);
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(AUTO_CHANGE_VIEWPAGER, 4000);
        }
    }

    public void onBannerPause() {
        if (mHandler != null) {
            currentPage = mHandler.getmViewPager().getCurrentItem();
            // 停止viewPager中图片的自动切换
            mHandler.removeCallbacksAndMessages(null);
        }
    }


}
