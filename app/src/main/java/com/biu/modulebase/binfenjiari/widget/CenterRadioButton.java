package com.biu.modulebase.binfenjiari.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.RadioButton;

import com.biu.modulebase.binfenjiari.R;


/**
 * Created by jhj_Plus on 2015/12/15.
 * <p>
 *    后期需要实现的属性：
 *    <p>
 *        内容文本和 ButtonDrawable 的显示顺序可以切换(四方向)
 *    </p>
 *    <p>
 *        内容文本根据 ToggleButton 选中的状态调整字体颜色或大小
 *    </p>
 * </p>
 */
public class CenterRadioButton extends RadioButton {
    private static final String TAG = "MyRadioBtn";

    private static final int COMPOUNDBUTTON_MODE_RADIOBUTTON=0X00000080;

    private static final int COMPOUNDBUTTON_MODE_TOGGLEBUTTON=0X00000090;

    private static final int DEFAULT_COMPOUNDBUTTON_MODE=COMPOUNDBUTTON_MODE_RADIOBUTTON;

    private static final int DEFAULT_BUTTON_PADDING=0;

    private static final int DEFAULT_TEXT_COLOR=0XFFFFFFFF;

    private static final int TEXT_MODE_INSIDE=0X00000010;

    private static final int TEXT_MODE_OUTSIDE=0X00000020;

    private static int DEFAULT_TEXT_MODE_OUTSIDE=TEXT_MODE_OUTSIDE;

    private static final int DEFAULT_BUTTON_GRAVITY=Gravity.BOTTOM;

    private int mCompoundButtonMode;

    /**
     * 控件是否含有图标
     */
    private boolean hasDrawable;
    /**
     * 控件是否含有文本
     */
    private boolean hasText;


    private int mTextMode;


    /**
     * 控件按钮的 Drawable
     */
    private Drawable mButtonDrawable;
    /**
     * 控件文字和按钮 Drawable 之间的边距
     */
    private int mButtonPadding;

    private int mButtonGravity;

    /**
     * 内容文本画笔
     */
    private TextPaint mTextPaint = null;

  //  private Rect mTextBounds;

    private Paint.FontMetrics mFontMetrics;

    /**
     * 内容文本
     */
    private String mText;
    /**
     * 内容文本的宽度
     */
    private int mTextWidth;
    /**
     * 内容文本的高度
     */
    private int mTextHeight;
    /**
     * 在 XML 里定义的根据控件的状态定义内容文本的不同显示颜色的颜色列表
     */
    private  ColorStateList mTextColors;
    /**
     * 控件 unchecked时 的内容文本颜色
     */
    private int mUncheckedTextColor;
    /**
     * 控件 checked时 的内容文本颜色
     */
    private int mCheckedtextColor;
    /**
     * 控件按钮的宽度
     */
    private int mDrawableWidth;
    /**
     * 控件按钮的高度
     */
    private int mDrawableHeight;

    private int mGravity;

    private int mPaddingLeft;

    private int mPaddingTop;

    private int mPaddingRight;

    private int mPaddingBottom;

    private static final int[] COMPOUNDBUTTON_MODE_SET={R.attr.compoundButtonMode};

    public CenterRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenterRadioButton(Context context) {
        this(context, null);
    }

    public CenterRadioButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context,attrs,defStyleAttr);
        //设为可点击状态否则不可接收到点击回调
        setClickable(true);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CenterRadioButton,
                defStyleAttr, 0);

        mCompoundButtonMode=a.getInt(R.styleable.CenterRadioButton_compoundButtonMode,
                DEFAULT_COMPOUNDBUTTON_MODE);

        mButtonPadding = a.getDimensionPixelSize(R.styleable.CenterRadioButton_buttonPadding,
                0);
        mUncheckedTextColor=a.getColor(R.styleable.CenterRadioButton_uncheckedColor,
                DEFAULT_TEXT_COLOR);
        mCheckedtextColor=a.getColor(R.styleable.CenterRadioButton_checkedColor,DEFAULT_TEXT_COLOR);

        mTextMode=a.getInt(R.styleable.CenterRadioButton_textMode,DEFAULT_TEXT_MODE_OUTSIDE);

        mButtonGravity=a.getInt(R.styleable.CenterRadioButton_buttonsGravity,DEFAULT_BUTTON_GRAVITY);

        Log.i(TAG,"TextMode------------->"+mTextMode);
//        mButtonGravity=a.getInt(R.styleable.CenterRadioButton_buttonGravity,Gravity.NO_GRAVITY);
//
//        hasButtonGravity=mButtonGravity!=Gravity.NO_GRAVITY;

        a.recycle();

        mPaddingLeft=getPaddingLeft();

        mPaddingTop=getPaddingTop();

        mPaddingRight=getPaddingRight();

        mPaddingBottom=getPaddingBottom();

        String text = getText().toString();

        setupText(text);

    }

    private void setupText(String text) {
        if (hasText = !TextUtils.isEmpty(text)) {

            mGravity = getGravity();

            int ver = Gravity.VERTICAL_GRAVITY_MASK & mGravity;
            int hor = Gravity.HORIZONTAL_GRAVITY_MASK & mGravity;
            Log.i(TAG, "TextGravity********************************>" + mGravity + ",vor=" + ver +
                    "," +
                    "hor=" + hor);

            Log.i(TAG, "----------------------------->" + 0X00000001);

            mText = text;

            mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.density = getResources().getDisplayMetrics().density;
            mTextPaint.setTextSize(getTextSize());
            mTextColors = getTextColors();
            Rect textBounds = new Rect();
            mTextPaint.getTextBounds(mText, 0, mText.length(), textBounds);
            mFontMetrics = mTextPaint.getFontMetrics();

            mTextWidth = (int) mTextPaint.measureText(mText);
            mTextHeight = (int) (mFontMetrics.descent-mFontMetrics.ascent);

            Log.i(TAG,"text="+mText+",TextHeight="+mTextHeight+",ButtonPadding="
                    +mButtonPadding+",TextHeight_bound="+textBounds.height()
                    +",ascent="+mFontMetrics.ascent+",descent="+mFontMetrics.descent
            +",top="+mFontMetrics.top+",bottom="+mFontMetrics.bottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (hasDrawable = mButtonDrawable != null) {
            mDrawableWidth = mButtonDrawable.getIntrinsicWidth();
            mDrawableHeight = mButtonDrawable.getIntrinsicHeight();
        }

        if (!hasDrawable || !hasText) {
            mButtonPadding = 0;
        }

        // Log.e(TAG,"textHeight--------->"+textHeight+",text--->"+getText().toString());
        //   final float textWidth=mTextPaint.measureText(getText().toString());

        int measuredWidth =
                mTextMode == TEXT_MODE_INSIDE ? mTextWidth + mPaddingLeft + mPaddingRight
                        : Math.max(mDrawableWidth, mTextWidth);
        int measuredHeight = 0;
        if (mTextMode == TEXT_MODE_INSIDE) {
            measuredHeight = Math.max(mDrawableHeight, mTextHeight);
        } else if (mTextMode == TEXT_MODE_OUTSIDE) {
            measuredHeight =
                    mDrawableHeight + mTextHeight + mButtonPadding;
        }

        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? widthSize : measuredWidth,
                (heightMode == MeasureSpec.EXACTLY) ? heightSize : measuredHeight);

        Log.e(TAG, "onMeasure----Drawable height----------------->" + mDrawableHeight+","+mText);
        Log.e(TAG, "onMeasure----height----------------->" + measuredHeight);
    }



    @Override
    public void setButtonDrawable(int resId) {
        super.setButtonDrawable(resId);
        final Drawable d;
        if (resId != 0) {
            d = getResources().getDrawable(resId);
        } else {
            d = null;
        }
        mButtonDrawable = d;
        Log.e(TAG, "--------------setButtonDrawable resId-----------------");
    }

    @Override
    public void setButtonDrawable(Drawable drawable) {
        super.setButtonDrawable(drawable);
        mButtonDrawable = drawable;
        Log.e(TAG, "--------------setButtonDrawable Drawable-----------------");
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
       // Toast.makeText(getContext(), "onTextChanged", Toast.LENGTH_SHORT).show();
        setupText(text.toString());
        invalidate();
    }



    /**
     * @param canvas
     * 根据 RadioButton 的点击的状态绘制不同点击状态下的 ButtonDrawable 和内容文本，居中显示
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "---------------onDraw--------------height-->" + getHeight());

        int viewWidth = getWidth();
        int viewHeight = getHeight();

        final int gravity = mGravity;

//        final int paddingLeft = mPaddingLeft;
//        final int paddingTop = mPaddingTop;
//        final int paddingRight = mPaddingRight;
//        final int paddingBottom = mPaddingBottom;

        int drawableTop = 0;
        float textX = 0;
        float textY = 0;

        if (mTextMode==TEXT_MODE_INSIDE) {

//            drawableTop = viewHeight - mDrawableHeight>>1;
            drawableTop=0;
            switch (gravity) {

                case Gravity.TOP | Gravity.LEFT:
                    Log.i(TAG, "-------------------TOP|LEFT--------------------");
                case Gravity.TOP | Gravity.START:
                    Log.i(TAG, "-------------------TOP|START--------------------");
                    textX=0;
                    textY=mTextHeight-mFontMetrics.bottom;
                    break;
                case Gravity.TOP | Gravity.RIGHT:
                    Log.i(TAG, "-------------------TOP|RIGHT--------------------");
                case Gravity.TOP | Gravity.END:
                    Log.i(TAG, "-------------------TOP|END--------------------");
                    textX=viewWidth-mTextWidth;
                    textY=mTextHeight-mFontMetrics.bottom;
                    break;

                case Gravity.BOTTOM|Gravity.LEFT:
                    Log.i(TAG,"-------------------BOTTOM|LEFT--------------------");

                case Gravity.BOTTOM|Gravity.START:
                    Log.i(TAG,"-------------------BOTTOM|START--------------------");
                    textX=0;
                    textY=viewHeight-mFontMetrics.bottom;
                    break;
                case Gravity.BOTTOM|Gravity.RIGHT:
                    Log.i(TAG,"-------------------BOTTOM|RIGHT--------------------");

                case Gravity.BOTTOM|Gravity.END:
                    Log.i(TAG,"-------------------BOTTOM|END--------------------");
                    textX=viewWidth-mTextWidth;
                    textY=viewHeight-mFontMetrics.bottom;
                    break;

                case Gravity.CENTER:
                    Log.i(TAG,"-------------------CENTER--------------------");
                    textX=viewWidth-mTextWidth>>1;
                    textY=viewHeight/2+(Math.abs(mFontMetrics.ascent)-mFontMetrics.descent)/2;
                    break;

                case Gravity.CENTER_VERTICAL | Gravity.START:
                    Log.i(TAG, "-------------------CENTER_VERTICA|START--------------------");
                case Gravity.LEFT | Gravity.CENTER_VERTICAL:
                    Log.i(TAG, "-------------------LEFT|CENTER_VERTICAL--------------------");
                    textX=0;
                    textY=viewHeight/2+(Math.abs(mFontMetrics.ascent)-mFontMetrics.descent)/2;
                    break;
                case Gravity.RIGHT | Gravity.CENTER_VERTICAL:
                    Log.i(TAG, "-------------------RIGHT|CENTER_VERTICAL--------------------");

                case Gravity.END | Gravity.CENTER_VERTICAL:
                    Log.i(TAG, "-------------------END|CENTER_VERTICAL--------------------");
                    textX=viewWidth-mTextWidth;
                    textY=viewHeight/2+(Math.abs(mFontMetrics.ascent)-mFontMetrics.descent)/2;
                    break;


                case Gravity.TOP|Gravity.CENTER_HORIZONTAL:
                    Log.i(TAG,"-------------------TOP|CENTER_HORIZONTAL--------------------");
                    textX=viewWidth-mTextWidth>>1;
                    textY=mTextHeight-mFontMetrics.bottom;
                    break;
                case Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL:
                    Log.i(TAG,"-------------------BOTTOM|CENTER_HORIZONTAL--------------------");
                    textX=viewWidth-mTextWidth>>1;
                    textY=viewHeight-mFontMetrics.bottom;
                    break;

                default:
                    textX=0;
                    textY = 0;
            }

        } else if (mTextMode==TEXT_MODE_OUTSIDE) {
            final int buttonGravity=mButtonGravity;
            textX = viewWidth - mTextWidth >> 1;
            switch (buttonGravity) {
                case Gravity.TOP:

                    drawableTop = hasText ? (viewHeight - mTextHeight - mButtonPadding -
                            mDrawableHeight >> 1) + mTextHeight+mButtonPadding
                            : viewHeight - mDrawableHeight >> 1;

                    textY = hasDrawable ? drawableTop - mButtonPadding -
                            mFontMetrics.bottom : viewHeight / 2 + (Math.abs(mFontMetrics.ascent) -
                            mFontMetrics.descent) / 2;


                    Log.i(TAG,"ButtonGravity_TOP,textY------->"+textY+"," +
                            "drawableTop------>"+drawableTop);

                    break;
                case Gravity.BOTTOM:
                    drawableTop =
                            hasText ? viewHeight - mTextHeight - mButtonPadding - mDrawableHeight >>
                                    1 : viewHeight - mDrawableHeight >> 1;
                    textY = hasDrawable ? drawableTop + mDrawableHeight +
                            mButtonPadding - mFontMetrics.top+mFontMetrics.bottom-mFontMetrics.descent
                            : viewHeight / 2 + (Math.abs(mFontMetrics.ascent) -
                                    mFontMetrics.descent) / 2;
                    Log.i(TAG,"ButtonGravity_BOTTOM,textY------->"+textY+"," +
                            "drawableTop------>"+drawableTop+",hasDrowable=>"+hasDrawable);
                    Log.i(TAG,"top-->"+mFontMetrics.top+",dec-->"+mFontMetrics.descent);
                    break;
                case Gravity.LEFT:

                    break;
                case Gravity.RIGHT:

                    break;
                default:
                    break;
            }
        }

        if (hasDrawable) {

            //add
            boolean textInsideMode=mTextMode==TEXT_MODE_INSIDE;

            final Drawable buttonDrawable = mButtonDrawable;
            final int drawableHeight = textInsideMode ? viewHeight : mDrawableHeight;
            final int drawableWidth = textInsideMode ? viewWidth : mDrawableWidth;

            final int drawableLeft = (viewWidth - drawableWidth) / 2;
            final int drawableRight = drawableWidth + drawableLeft;
            final int drawableBottom = drawableTop + drawableHeight;

            Log.e(TAG, "viewHeight=" + viewHeight + ",drawablePadding=" + mButtonPadding +
                    ",textHeight=" + mTextHeight + ",drawableHeight" + drawableHeight +
                    ",drawableTop----->" + drawableTop+",text="+mText);
            buttonDrawable.setBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
            buttonDrawable.draw(canvas);
        }

        if (hasText) {

            mTextPaint.setColor(mTextColors.isStateful() ? mTextColors
                    .getColorForState(getDrawableState(), DEFAULT_TEXT_COLOR)
                    : isChecked() ? mCheckedtextColor : mUncheckedTextColor);
            canvas.drawText(mText, textX, textY, mTextPaint);
        }
    }


//    @Override
//    public void toggle() {
//        if (mCompoundButtonMode == COMPOUNDBUTTON_MODE_TOGGLEBUTTON) {
//            setChecked(!isChecked());
//            return;
//        }
//        super.toggle();
//    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
    }

}
