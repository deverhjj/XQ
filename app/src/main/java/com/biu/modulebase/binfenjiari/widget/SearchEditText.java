package com.biu.modulebase.binfenjiari.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

import com.biu.modulebase.binfenjiari.R;

public class SearchEditText extends EditText {

	float searchSize = 0;
	float textSize = 0;
	int textColor = 0xFF000000;
	String textHint = "";
	Drawable mDrawable;
	Paint paint;

	public SearchEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
//		InitResource(context, attrs);
//		InitPaint();
	}

	private void InitResource(Context context, AttributeSet attrs) {
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.searchedit);
		float density = context.getResources().getDisplayMetrics().density;
//		searchSize = mTypedArray.getDimension(
//				R.styleable.searchedit_imagewidth, 20 * density + 0.5F);
		textColor = mTypedArray.getColor(R.styleable.searchedit_hintTextColor,
				getResources().getColor(R.color.colorTextGray));
		textHint = mTypedArray.getString(R.styleable.searchedit_textHint);
		textSize = getTextSize();
		mTypedArray.recycle();
	}

	private void InitPaint() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		DrawSearchIcon(canvas);
	}

	private void DrawSearchIcon(Canvas canvas) {
		if (this.getText().toString().length() == 0) {
			float textWidth = paint.measureText(textHint);
			float textHeight = getFontLeading(paint);

			float dx = (getWidth() - searchSize - textWidth - 8) / 2;
			float dy = (getHeight() - searchSize) / 2;

			canvas.save();
			canvas.translate(getScrollX() + dx, getScrollY() + dy);
			if (mDrawable != null) {
				mDrawable.draw(canvas);
			}
			canvas.drawText(textHint, getScrollX() + searchSize + 8,
					getScrollY()
							+ (getHeight() - (getHeight() - textHeight) / 2)
							- paint.getFontMetrics().bottom - dy, paint);
			canvas.restore();
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mDrawable == null) {
			try {
				mDrawable = getContext().getResources().getDrawable(
						R.mipmap.icon_toolbar_search);
				mDrawable.setBounds(0, 0, (int) searchSize, (int) searchSize);
			} catch (Exception e) {

			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mDrawable != null) {
			mDrawable.setCallback(null);
			mDrawable = null;
		}
		super.onDetachedFromWindow();
	}

	public float getFontLeading(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.bottom - fm.top;
	}

	public void setTextHint(String textHint) {
		this.textHint = textHint;
	}
}