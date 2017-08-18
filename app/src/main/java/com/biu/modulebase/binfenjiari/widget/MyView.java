package com.biu.modulebase.binfenjiari.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;

/**
 * Created by jhj_Plus on 2016/5/9.
 */
public class MyView extends TextView {
    private static final String TAG = "MyView";

    private static final float ARCSIZE=1.5f;//dp
    private RectF mArcRect=new RectF();
    private Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mArcSize;

    //总投人数
    private int mTotalNumber=1000;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        final float density=getResources().getDisplayMetrics().density;
        mArcSize=ARCSIZE*density;
    }

    public int getTotalNumber() {
        return mTotalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        mTotalNumber = totalNumber;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        final int width=canvas.getWidth();
        final int height=canvas.getHeight();
        mArcRect.set(mArcSize,mArcSize,width-mArcSize,height-mArcSize);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mArcSize);
        mPaint.setColor(getResources().getColor(R.color.grey_light));

        //绘制内弧
        canvas.drawArc(mArcRect,135,270,false,mPaint);

        //绘制外弧
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
        final String number=getText().toString().split("\n")[0];
        final float sa = Float.valueOf(number) / mTotalNumber * 270;
        canvas.drawArc(mArcRect,135,sa,false,mPaint);
    }

}
