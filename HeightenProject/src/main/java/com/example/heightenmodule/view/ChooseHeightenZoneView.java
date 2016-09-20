package com.example.heightenmodule.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.heightenmodule.R;
import com.example.heightenmodule.interfaces.ChooseHeightenZoneChangeCallBack;
import com.example.heightenmodule.util.Const;

/**
 * 选择区域自定义TextView
 * Created by meitu on 2016/9/14.
 */
public class ChooseHeightenZoneView extends TextView {

    private ChooseHeightenZoneChangeCallBack mInitSbCallBack;
    // 区分参考线的标志与高度
    private static final int REFERENCE_A = 0;
    private static final int REFERENCE_B = 1;

    // 文字的大小
    private static final int TEXT_SIZE = 30;
    private Paint mPaint;
    // 参考线的高度，A是上面的，B是下面的
    private float mHeightA, mHeightB;
    // 中间选择区域的文字内容
    private String text;
    // 触摸的位置
    private float xPoint;
    private float yPoint;
    private Bitmap mBmp, mBmpIconNormal, mBmpIcoPressed;
    // 绘制所需的宽高
    private int drawWidth, drawHeight;

    // 参考线是否被触摸
    private boolean isTextShow;

    public boolean isZoneShow() {
        return isZoneShow;
    }

    public void setZoneShow(boolean zoneShow) {
        isZoneShow = zoneShow;
    }

    private boolean isZoneShow = true;
    private Matrix mMatrix;

    public ChooseHeightenZoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (false == isZoneShow) {
            return;
        }
        drawRefLineAndIcon(canvas, mHeightA, isTextShow);
        drawRefLineAndIcon(canvas, mHeightB, isTextShow);
        drawChooseZoneFrameAndText(canvas, isTextShow);
    }

    /**
     * 绘制参考线
     */
    private void drawRefLineAndIcon(Canvas canvas, float height, boolean isPressed) {
        mBmp = isPressed ? mBmpIcoPressed : mBmpIconNormal;
        mPaint.setColor(Color.RED);
        canvas.drawLine(0, height, drawWidth - mBmp.getWidth() / 2, height, mPaint);
        mMatrix.setValues(new float[] {Const.ICON_SCALE_RATE, 0, drawWidth - mBmp.getWidth() / 2, 0,
            Const.ICON_SCALE_RATE, height - mBmp.getHeight() * Const.ICON_SCALE_RATE / 2, 0, 0, 1});
        canvas.drawBitmap(mBmp, mMatrix, mPaint);
    }

    /**
     * 绘制选择区域的外框
     */
    private void drawChooseZoneFrameAndText(Canvas canvas, boolean isTextShow) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(isTextShow ? getResources().getColor(R.color.colorHalfTransparent)
            : getResources().getColor(R.color.colorAllTransparent));
        canvas.drawRect(0, mHeightA, drawWidth, mHeightB, mPaint);

        // 绘制中间的文字
        text = isTextShow ? getResources().getString(R.string.heighten_choose_zone_text) : "";
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(TEXT_SIZE);// 设置文字的大小
        if (isTextShow && Const.TEXT_DISMISS_DISTANCE < Math.abs(mHeightA - mHeightB)) {
            canvas.drawText(text, drawWidth / 2 - mPaint.measureText(text) / 2, (mHeightA + mHeightB) / 2, mPaint);
        }
    }

    public void setHeightA(float height) {
        this.mHeightA = height;
    }

    public void setHeightB(float height) {
        this.mHeightB = height;
    }

    public void setmBmpIconNormal(Bitmap mBmpIconNormal) {
        this.mBmpIconNormal = mBmpIconNormal;
    }

    public void setmBmpIcoPressed(Bitmap mBmpIcoPressed) {
        this.mBmpIcoPressed = mBmpIcoPressed;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        xPoint = event.getX();
        yPoint = event.getY();

        if (xPoint <= drawWidth - mBmp.getWidth() * Const.ICON_SCALE_RATE) {
            return true;
        }
        // 区分移动的是参考线A还是参考线B
        int moveLineType = Math.abs(yPoint - mHeightA) < Math.abs(yPoint - mHeightB) ? REFERENCE_A : REFERENCE_B;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                isTextShow = true;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                refreshMyView(moveLineType, yPoint);
                break;
            }
            case MotionEvent.ACTION_UP: {
                isTextShow = false;
                // 顺序不能倒了
                mInitSbCallBack.getRefHeighs(mHeightA, mHeightB);
                mInitSbCallBack.initSb(0);
                break;
            }
            default:
                break;
        }
        invalidate();
        Log.d("ChooseHeightenZoneView", "drawHeight:" + drawHeight);
        Log.d("ChooseHeightenZoneView", "getHeight():" + getHeight());
        return true;
    }

    /**
     * 根据距离的远近选择移动的参考线
     */
    private void refreshMyView(int type, float height) {
        switch (type) {
            case REFERENCE_A: {
                setHeightA(height);
                break;
            }
            case REFERENCE_B: {
                setHeightB(height);
                break;
            }
            default:
                break;
        }
    }

    public void setmInitSbCallBack(ChooseHeightenZoneChangeCallBack mInitSbCallBack) {
        this.mInitSbCallBack = mInitSbCallBack;
    }

    public void setDrawWidth(int drawWidth) {
        this.drawWidth = drawWidth;
    }

    public void setDrawHeight(int drawHeight) {
        this.drawHeight = drawHeight;
    }
}
