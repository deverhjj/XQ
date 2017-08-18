package com.biu.modulebase.binfenjiari.fragment.dialogFragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.widget.jazzviewpager.CirclePageIndicator;
import com.biu.modulebase.binfenjiari.widget.jazzviewpager.JazzyViewPager;
import com.biu.modulebase.binfenjiari.widget.jazzviewpager.OutlineContainer;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Lee
 * @Title: {引导页}
 * @Description:{描述}
 * @date 2016/2/19
 */
public  class GuideDialogFragment extends DialogFragment  implements View.OnClickListener {

    int mNum;
    private Handler mHandler = new Handler();

    private JazzyViewPager guidePager;
    private List<View> mPageViews;
    private CirclePageIndicator mIndicator = null;

   public static GuideDialogFragment newInstance(int num, int layout) {
        GuideDialogFragment f = new GuideDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putInt("layout", layout);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((mNum-1)%6) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        switch ((mNum-1)%6) {
            case 4: theme = android.R.style.Theme_Holo; break;
            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;
        }
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //5.0以下的theme中windowbackground不是透明色  会有边框
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent_guide);
        setCancelable(true);
        View v = inflater.inflate(getArguments().getInt("layout"), container, false);
        initViewPager(v);
        return v;
    }
    private void initViewPager(View v) {
        guidePager = (JazzyViewPager) v.findViewById(R.id.mViewPager);
        mIndicator = (CirclePageIndicator) v.findViewById(R.id.main_indicator);
        mIndicator.setPageColor(getResources().getColor(R.color.white_dark));
        mIndicator.setFillColor(getResources().getColor(R.color.colorAccent));
        mIndicator.setStrokeWidth(0);
        mPageViews = new ArrayList<>();
        LayoutInflater mInflater =getActivity().getLayoutInflater();
        mPageViews.add(mInflater.inflate(R.layout.guide_page_1, null));
        mPageViews.add(mInflater.inflate(R.layout.guide_page_2, null));
        mPageViews.add(mInflater.inflate(R.layout.guide_page_3, null));

        guidePager.setTransitionEffect(JazzyViewPager.TransitionEffect.FlipHorizontal);
        guidePager.setAdapter(new ViewPagerAdapter());
        guidePager.setTransitionEffect(JazzyViewPager.TransitionEffect.Standard);// 设置效果模式
        mIndicator.setCentered(true);
        mIndicator.setRadius(8);
        mIndicator.setViewPager(guidePager);

    }

    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(mPageViews.get(position), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            guidePager.setObjectForPosition(mPageViews.get(position), position);
            return mPageViews.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {
            container.removeView(guidePager.findViewFromObject(position));
        }

        @Override
        public int getCount() {
            return mPageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            if (view instanceof OutlineContainer) {
                return ((OutlineContainer) view).getChildAt(0) == obj;
            } else {
                return view == obj;
            }
        }

    }

    @Override
    public void onClick(View v) {

    }
}
