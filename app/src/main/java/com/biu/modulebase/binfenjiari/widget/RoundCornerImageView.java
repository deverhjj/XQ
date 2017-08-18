package com.biu.modulebase.binfenjiari.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.biu.modulebase.binfenjiari.R;


public class RoundCornerImageView extends ImageView {

    private float corner;

    public RoundCornerImageView(Context context) {
	super(context);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
	super(context, attrs);
	TypedArray a = context.obtainStyledAttributes(attrs,
		R.styleable.RoundCornerImageView);
	corner = a.getDimension(R.styleable.RoundCornerImageView_corner, 10);
	a.recycle();
    }

    public RoundCornerImageView(Context context, AttributeSet attrs,
								int defStyle) {
	super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
	Path clipPath = new Path();
	int w = this.getWidth();
	int h = this.getHeight();
	clipPath.addRoundRect(new RectF(0, 0, w, h), corner, corner,
		Path.Direction.CW);
	canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
		| Paint.FILTER_BITMAP_FLAG));
	canvas.clipPath(clipPath);
	super.onDraw(canvas);
    }

    public float getCorner() {
	return corner;
    }

    public void setCorner(float corner) {
	this.corner = corner;
    }
}