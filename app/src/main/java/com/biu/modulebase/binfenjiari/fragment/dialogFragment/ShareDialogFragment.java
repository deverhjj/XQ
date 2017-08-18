package com.biu.modulebase.binfenjiari.fragment.dialogFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.model.ShareInfoVO;
import com.biu.modulebase.binfenjiari.other.umeng.UmengSocialUtil;
import com.biu.modulebase.binfenjiari.util.AnimatorUtils;
import com.biu.modulebase.binfenjiari.util.Utils;
import com.biu.modulebase.binfenjiari.widget.arclayout.ArcLayout;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/2/19
 */
public  class ShareDialogFragment extends DialogFragment  implements View.OnClickListener {

    private Activity activity;

    int mNum;
    private FrameLayout main;
    private ArcLayout arcLayout;
    private TextView centerItem;
    private Handler mHandler = new Handler();

    private boolean ready1 =false;
    private boolean ready2 =false;

   private static ShareInfoVO shareInfoVO;

   public static ShareDialogFragment newInstance(int num, int layout, ShareInfoVO shareInfoVO) {
        ShareDialogFragment f = new ShareDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putSerializable("shareInfo",shareInfoVO);
        args.putInt("layout", layout);
        f.setArguments(args);

        return f;
    }

    public static ShareDialogFragment newInstance(int num, int layout) {
        ShareDialogFragment f = new ShareDialogFragment();
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
        if(isAdded())
            activity = (Activity) getActivity();
        shareInfoVO = (ShareInfoVO) getArguments().getSerializable("shareInfo");
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //5.0以下的theme中windowbackground不是透明色  会有边框
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        setCancelable(true);
        View v = inflater.inflate(getArguments().getInt("layout"), container, false);
        centerItem = (TextView) v.findViewById(R.id.title);
        main =(FrameLayout)v.findViewById(R.id.main);
        arcLayout = (ArcLayout) v.findViewById(R.id.arcLayout);
        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(this);
        }
        centerItem.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            /**
             * Callback method to be invoked when the global layout state or the visibility of views
             * within the view tree changes
             */
            @Override
            public void onGlobalLayout() {
                ready1 =true;
                if(ready1&& ready2){
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            show();
                        }
                    },200);
                }

            }
        });
        arcLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            /**
             * Callback method to be invoked when the global layout state or the visibility of views
             * within the view tree changes
             */
            @Override
            public void onGlobalLayout() {
                ready2 = true;
                if(ready1&& ready2){
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            show();
                        }
                    },200);
                }

            }
        });
        setListener(v);
        return v;
    }

    private void setListener(View v){
        v.findViewById(R.id.qq).setOnClickListener(this);
        v.findViewById(R.id.wx).setOnClickListener(this);
        v.findViewById(R.id.wxq).setOnClickListener(this);
        v.findViewById(R.id.qzone).setOnClickListener(this);
        v.findViewById(R.id.sina).setOnClickListener(this);

    }

    private void show(){
        main.setVisibility(View.VISIBLE);
        List<Animator> animList = new ArrayList<>();
        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
//        animSet.playTogether(animList);
        animSet.playSequentially(animList);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.setDuration(200);
        animSet.start();
    }


    private void hide() {
        List<Animator> animList = new ArrayList<>();
        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(200);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                main.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();

    }

    private Animator createShowItemAnimator(View item) {
        final float dx = (centerItem.getX()+centerItem.getWidth()/2) - (item.getX()+item.getWidth()/2);
        final float dy = (centerItem.getY()+centerItem.getHeight()/2) - (item.getY()+item.getHeight()/2);

//        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);
        ValueAnimator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(0f, 720f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );
//        KickBackAnimator evaluator = new KickBackAnimator();
//        evaluator.setDuration(150);
//        anim.setEvaluator(evaluator);
        return anim;
    }
    private Animator createHideItemAnimator(final View item) {
        final float dx = (centerItem.getX()+centerItem.getWidth()/2) - (item.getX()+item.getWidth()/2);
        final float dy = (centerItem.getY()+centerItem.getHeight()/2) - (item.getY()+item.getHeight()/2);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(720f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });

        return anim;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
//        hide();
        super.onCancel(dialog);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        String pic =shareInfoVO.getPic();
        String content = Utils.isEmpty(shareInfoVO.getContent())==true?shareInfoVO.getTitle():shareInfoVO.getContent();
        int i = v.getId();
        if (i == R.id.qq) {
            UmengSocialUtil.socialShare(getActivity(), SHARE_MEDIA.QQ, shareInfoVO.getTitle(), content, shareInfoVO.getUrl(), shareInfoVO.getPic(), new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    Toast.makeText(activity, "分享失败", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (i == R.id.qzone) {
            UmengSocialUtil.socialShare(getActivity(), SHARE_MEDIA.QZONE, shareInfoVO.getTitle(), content, shareInfoVO.getUrl(), shareInfoVO.getPic(), new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    Toast.makeText(activity, "分享失败", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (i == R.id.wx) {
            UmengSocialUtil.socialShare(getActivity(), SHARE_MEDIA.WEIXIN, shareInfoVO.getTitle(), content, shareInfoVO.getUrl(), shareInfoVO.getPic(), new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    Toast.makeText(activity, "分享失败", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (i == R.id.wxq) {
            UmengSocialUtil.socialShare(getActivity(), SHARE_MEDIA.WEIXIN_CIRCLE, shareInfoVO.getTitle(), content, shareInfoVO.getUrl(), shareInfoVO.getPic(), new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    Toast.makeText(activity, "分享失败", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (i == R.id.sina) {
            UmengSocialUtil.socialShare(getActivity(), SHARE_MEDIA.SINA, "", Utils.isEmpty(shareInfoVO.getTitle()) == true ? shareInfoVO.getContent() + shareInfoVO.getUrl() : shareInfoVO.getTitle() + shareInfoVO.getUrl(), "", shareInfoVO.getPic(), new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    Toast.makeText(activity, "分享失败", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    @Override
    public void dismiss() {
//        hide();
        super.dismiss();
    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroyView() {
//        hide();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
