package com.biu.modulebase.binfenjiari.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.biu.modulebase.binfenjiari.R;

/**
 * @author Lee
 * @Title: {自定义侧滑菜单，实现3D翻转显示/隱藏效果}
 * @Description:{描述}
 * @date 2016/5/10
 */
public class MyNavigationView extends FrameLayout implements View.OnClickListener{

    /**普通隐藏标识**/
    private final int HIDE_COMMON =-1;

    private Context mContext;
    private NavigationViewCallBack callBack;

    private int currClickPosition =HIDE_COMMON;

    private FrameLayout nav_main;

    private FrameLayout info;
    private FrameLayout huodong ;
    private FrameLayout shangcheng;
    private FrameLayout shoucang ;
    private FrameLayout circle;
    private FrameLayout erweima;
    private FrameLayout share ;
    private FrameLayout shezhi;

    private AnimatorSet setShow1;
    private AnimatorSet setShow2;
    private AnimatorSet setShow3;
    private AnimatorSet setShow4;
    private AnimatorSet setShow5;
    private AnimatorSet setShow6;
    private AnimatorSet setShow7;
    private AnimatorSet setShow8;

    private boolean isHiding =false;
    private boolean isShowing =false;

    private AnimatorSet sethide1;
    private AnimatorSet sethide2;
    private AnimatorSet sethide3;
    private AnimatorSet sethide4;
    private AnimatorSet sethide5;
    private AnimatorSet sethide6;
    private AnimatorSet sethide7;
    private AnimatorSet sethide8;


    public MyNavigationView(Context context) {
        super(context);
        initView(context);
    }

    public MyNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mContext =context;
        View.inflate(context, R.layout.fragment_drawer_left_layout, this);
        nav_main = (FrameLayout) findViewById(R.id.nav_main);
        findViewById(R.id.nav_main).setOnClickListener(this);
        info = (FrameLayout) findViewById(R.id.info);
        huodong = (FrameLayout) findViewById(R.id.huodong);
        shangcheng = (FrameLayout)findViewById(R.id.shangcheng);
        shoucang = (FrameLayout) findViewById(R.id.shoucang);
        circle = (FrameLayout) findViewById(R.id.circle);
        erweima = (FrameLayout) findViewById(R.id.erweima);
        share = (FrameLayout) findViewById(R.id.share);
        shezhi = (FrameLayout)findViewById(R.id.shezhi);
        info.setOnClickListener(this);
        huodong.setOnClickListener(this);
        shangcheng.setOnClickListener(this);
        shoucang.setOnClickListener(this);
        circle.setOnClickListener(this);
        erweima.setOnClickListener(this);
        share.setOnClickListener(this);
        shezhi.setOnClickListener(this);

        initAnimation(context);
    }

    private void initAnimation(Context context){
        setShow1 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_show);
        setShow2 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_show2);
        setShow3 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_show3);
        setShow4 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_show4);
        setShow5 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_show5);
        setShow6 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_show6);
        setShow7 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_show7);
        setShow8 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_show8);

        setShow1.setTarget(info);
        setShow2.setTarget(huodong);
        setShow3.setTarget(shangcheng);
        setShow4.setTarget(shoucang);
        setShow5.setTarget(circle);
        setShow6.setTarget(erweima);
        setShow7.setTarget(share);
        setShow8.setTarget(shezhi);
        setShow8.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isShowing =false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        sethide1 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_hide1);
        sethide2 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_hide2);
        sethide3 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_hide3);
        sethide4 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_hide4);
        sethide5 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_hide5);
        sethide6 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_hide6);
        sethide7 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_hide7);
        sethide8 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.nav_hide8);
        sethide1.setTarget(info);
        sethide2.setTarget(huodong);
        sethide3.setTarget(shangcheng);
        sethide4.setTarget(shoucang);
        sethide5.setTarget(circle);
        sethide6.setTarget(erweima);
        sethide7.setTarget(share);
        sethide8.setTarget(shezhi);
        sethide8.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isHiding =false;
                nav_main.setVisibility(View.GONE);
                callBack.OnNavItemClick(currClickPosition);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void show(){
        if(!isShowing){
            isShowing =true;
            nav_main.setVisibility(View.VISIBLE);
            setShow1.start();
            setShow2.start();
            setShow3.start();
            setShow4.start();
            setShow5.start();
            setShow6.start();
            setShow7.start();
            setShow8.start();
        }
    }

    public void hide(){
        if(!isHiding){
            isHiding =true;
            sethide1.start();
            sethide2.start();
            sethide3.start();
            sethide4.start();
            sethide5.start();
            sethide6.start();
            sethide7.start();
            sethide8.start();
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.nav_main) {
            currClickPosition = HIDE_COMMON;
            hide();

        } else if (i == R.id.info) {
            currClickPosition = 0;
            hide();

        } else if (i == R.id.huodong) {
            currClickPosition = 1;
            hide();

        } else if (i == R.id.shangcheng) {
            currClickPosition = 2;
            hide();

        } else if (i == R.id.shoucang) {
            currClickPosition = 3;
            hide();

        } else if (i == R.id.circle) {
            currClickPosition = 4;
            hide();

        } else if (i == R.id.shezhi) {
            currClickPosition = 5;
            hide();

        } else if (i == R.id.erweima) {
            currClickPosition = 6;
            hide();

        } else if (i == R.id.share) {
            currClickPosition = 7;
            hide();

        }
    }

    public void setCallBack(NavigationViewCallBack callBack) {
        this.callBack = callBack;
    }

    public interface NavigationViewCallBack{
        /**
         * 默认为 HIDE_COMMON
         * @param currClickPosition
         */
        void OnNavItemClick(int currClickPosition);
    }


}
