package com.biu.modulebase.binfenjiari.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.activity.PhotoViewActivity;
import com.biu.modulebase.binfenjiari.communication.ImageDisplayUtil;
import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.model.EvaluateItem;
import com.biu.modulebase.binfenjiari.model.ImageItem;
import com.biu.modulebase.binfenjiari.util.LogUtil;
import com.biu.modulebase.binfenjiari.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/6/3.
 */
public class HeaderHandler implements View.OnClickListener {
    private static final String TAG = "HeaderHandler";
    private static final String TAG_IMAGE="image";
    private Context mAppContext;

    private static final int[] LABEL_BACKGROUND_RESOUECE_ID={R.drawable.bg_tv_label_0,R.drawable
            .bg_tv_label_1,R.drawable.bg_tv_label_2,R.drawable.bg_tv_label_3,R.drawable
            .bg_tv_label_4,R.drawable.bg_tv_label_5,R.drawable.bg_tv_label_6,R.drawable
            .bg_tv_label_7};

    public HeaderHandler(Context appContext) {
        mAppContext = appContext;
    }

    /**
     *  动态创建并绑定 image+text 视图布局数据
     * @param itLayout
     * @param imageList
     */
    public void createBindImageViews(LinearLayout itLayout, List<ImageItem> imageList) {
        if (itLayout==null||imageList==null) return;

        itLayout.removeAllViews();

        final int count=imageList.size();

        ArrayList<String> imgs = new ArrayList<>();//图片路径集 ，用于图片预览界面

        for(int i=0;i<count;i++){
            ImageItem imageItem=imageList.get(i);
            if(imageItem.getType()!=1){
                imgs.add(imageItem.getImg());
            }
        }

        for (int i = 0; i < count; i++) {

            ImageItem imageItem=imageList.get(i);

            final int type=imageItem.getType();

            View itView= LayoutInflater.from(mAppContext).inflate(R.layout
                    .part_image_text_detail,itLayout,false);

            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) itView.getLayoutParams();

            if (i != 0) {
                layoutParams.setMargins(0, mAppContext.getResources().getDimensionPixelSize(R.dimen.view_margin_4dp), 0, 0);
            }

            if (type != 1) {
                itView.setId(i + 1 << 8);
                itView.setTag(TAG_IMAGE);
                itView.setOnClickListener(this);
            }

            itLayout.addView(itView);

            ImageView iv_image= (ImageView) itView.findViewById(R.id.iv_image);
            iv_image.setVisibility(type != 1 ? View.VISIBLE : View.GONE);
            if (iv_image.getVisibility()==View.VISIBLE) {
                int position =-1;
                String path = imageItem.getImg();
                for(int j=0;j<imgs.size();j++){//遍历获取图片position
                    if(path.equals(imgs.get(j))){
                        position =j;
                    }
                }
                //TODO 注意url
                ImageDisplayUtil.displayImage(Constant.IMG_COMPRESS,path, iv_image,
                        ImageDisplayUtil.DISPLAY_BIG_IMAGE);
                iv_image.setOnClickListener(new MyImageViewOnClickListener(position,imgs));
            }

            TextView tv_content= (TextView) itView.findViewById(R.id.tv_content);
            tv_content.setVisibility(type != 2 ? View.VISIBLE : View.GONE);

            LogUtil.LogE(TAG,"createBindImageViews==???");

            if (tv_content.getVisibility()==View.VISIBLE) {
                tv_content.setText(imageItem.getDescription());
            }
        }

    }

    /**
     *  动态创建并绑定评星视图数据
     * @param ratingLayout
     * @param evaluateList
     */
    public void createBindRatingViews(LinearLayout ratingLayout, List<EvaluateItem> evaluateList) {

        if (ratingLayout==null||evaluateList==null) return;

        ratingLayout.removeAllViews();

        final int count=evaluateList.size();

        for (int i = 0; i < count; i++) {

            EvaluateItem evaluateItem = evaluateList.get(i);

            View ratingView = LayoutInflater.from(mAppContext).inflate(
                    R.layout.part_rantings_index, ratingLayout, false);

            LinearLayout.LayoutParams layoutParams=
                    (LinearLayout.LayoutParams) ratingView.getLayoutParams();

            if (i != 0) {
                layoutParams.setMargins(0, mAppContext.getResources().getDimensionPixelSize(R.dimen
                        .margin_top_8dp),
                        0, 0);
            }

            ((TextView) ratingView.findViewById(R.id.tv_evaluate)).setText(
                    evaluateItem.getDescription());

            final float rating = Float.valueOf(
                    String.format("%.1f", Float.valueOf(evaluateItem.getEvaluate_number())));

            ((RatingBar) ratingView.findViewById(R.id.rantingBar)).setRating(Math.round(rating));

            ((TextView) ratingView.findViewById(R.id.tv_score)).setText(rating + "");

            ratingLayout.addView(ratingView,layoutParams);
        }
    }

    /**
     * 动态创建标签并绑定数据
     * @param labelStr
     */
    public void createBindLabelViews(FlowLayout labelLayout, String labelStr) {

        if (labelLayout == null || TextUtils.isEmpty(labelStr)) {
            return;
        }

        labelLayout.removeAllViews();

        final String[] labels=labelStr.split(",");

        final int count = labels.length;

        for (int i = 0; i < count; i++) {

            TextView label = (TextView) LayoutInflater.from(mAppContext).inflate(
                    R.layout.part_service_tag_two, labelLayout, false);
            label.setText(labels[i]);

            //   int randomBgIndex= new Random().nextInt(8);

//            int bgResId = LABEL_BACKGROUND_RESOUECE_ID[i % 8];

            //            switch (randomBgIndex) {
            //                case 0:
            //                    bgResId=R.drawable.bg_tv_label_0;
            //                    break;
            //                case 1:
            //                    bgResId=R.drawable.bg_tv_label_1;
            //                    break;
            //                case 2:
            //                    bgResId=R.drawable.bg_tv_label_2;
            //                    break;
            //                case 3:
            //                    bgResId=R.drawable.bg_tv_label_3;
            //                    break;
            //                case 4:
            //                    bgResId=R.drawable.bg_tv_label_4;
            //                    break;
            //                case 5:
            //                    bgResId=R.drawable.bg_tv_label_5;
            //                    break;
            //                case 6:
            //                    bgResId=R.drawable.bg_tv_label_6;
            //                    break;
            //                case 7:
            //                    bgResId=R.drawable.bg_tv_label_7;
            //                    break;
            //                default:
            //                    break;
            //            }

//            label.setBackgroundResource(bgResId);

            labelLayout.addView(label);
        }
    }

    @Override
    public void onClick(View v) {

    }

    class MyImageViewOnClickListener implements View.OnClickListener{

        private int position;
        private ArrayList<String> imgList;

        public MyImageViewOnClickListener(int position,ArrayList<String> imgs){
            this.position= position;
            this.imgList =imgs;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mAppContext, PhotoViewActivity.class);
            intent.putExtra("position",position);
            intent.putStringArrayListExtra("imgs",imgList);
            mAppContext.startActivity(intent);
        }
    }

}
