package com.binfenjiari.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;

import com.binfenjiari.R;
import com.binfenjiari.widget.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.binfenjiari.widget.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.biu.modulebase.binfenjiari.util.Utils;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/7/10
 */
public class DividerCommon8dpDecoration implements
        FlexibleDividerDecoration.PaintProvider,
//        FlexibleDividerDecoration.SizeProvider,
//        FlexibleDividerDecoration.ColorProvider,
        FlexibleDividerDecoration.VisibilityProvider,
        HorizontalDividerItemDecoration.MarginProvider {

    private Context mContext;

    public DividerCommon8dpDecoration(Context context){
        this.mContext = context;
    }

    public Context getContext(){
        return mContext;
    }

    @Override
    public int dividerLeftMargin(int position, RecyclerView parent) {
//        if (position > 7) {
//            return Utils.dp2px(getContext(), 15);
//        }
        return 0;
    }

    @Override
    public int dividerRightMargin(int position, RecyclerView parent) {
//        if (position > 7) {
//            return Utils.dp2px(getContext(), 15);
//        }
        return 0;
    }

    @Override
    public boolean shouldHideDivider(int position, RecyclerView parent) {
//        if (position == 7)
//            return true;
        return false;
    }

    @Override
    public Paint dividerPaint(int position, RecyclerView parent) {
        Paint paint = new Paint();
//        if (position >= 1 && position <= 6) {
            Resources resource = (Resources) getContext().getResources();
            ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.colorAppBackground);
            paint.setColor(csl.getDefaultColor());
            paint.setStrokeWidth(Utils.dp2px(getContext(), 8));
            return paint;
//
//        }
//
//        if (position > 7) {
//            Resources resource = (Resources) getContext().getResources();
//            ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.colorDividerLight);
//            paint.setColor(csl.getDefaultColor());
//            paint.setStrokeWidth(Utils.dp2px(getContext(), 1));
//            return paint;
//        }
//        paint.setStrokeWidth(0);
//        return paint;
    }

}
