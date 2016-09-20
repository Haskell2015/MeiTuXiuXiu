package com.example.heightenmodule.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 最底层的图片操作类</br>
 *
 * Created by meitu on 2016/9/19.
 */
public class BottomPicOperateView extends View {
    private Paint mPaint;
    // 截图区域
    private Rect mSourRect;
    // 绘制区域
    private Rect mDisRect;
    // 操作的原图
    private Bitmap mSourceBmp;
    // 增高量
    private int mHeightenVar;
    private boolean isReset;
    // 上面一条参考线的高度
    private int mHeightUp;
    // 下面一条参考线的高度
    private int mHeightDown;
    // 绘制所需要的总宽高
    private int mDrawWidth;
    private int mDrawHeight;
    //最大增高量
    private static final int MAX_HEIGHTEN_VAR=20;

    public BottomPicOperateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mSourRect = new Rect();
        mDisRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isReset) {
            resetBitmap(canvas);
        } else {
            heightenBitmap(canvas);
        }
    }

    private void resetBitmap(Canvas canvas) {
        canvas.drawBitmap(mSourceBmp, new Rect(0, 0, mSourceBmp.getWidth(), mSourceBmp.getHeight()),
            new Rect(0, 0, mDrawWidth, mDrawHeight), mPaint);
    }

    /**
     * 增高Bitmap
     */
    private void heightenBitmap(Canvas canvas) {
        // 画上面
        mSourRect.set(0, 0, mSourceBmp.getWidth(), mHeightUp);
        mDisRect.set(0, 0, mDrawWidth, mHeightUp-mHeightenVar);
        canvas.drawBitmap(mSourceBmp, mSourRect, mDisRect, mPaint);
        // 画中间
        mSourRect.set(0, mHeightUp, mSourceBmp.getWidth(), mHeightDown);
        mDisRect.set(0, mHeightUp-mHeightenVar, mDrawWidth, mHeightDown + mHeightenVar);
        canvas.drawBitmap(mSourceBmp, mSourRect, mDisRect, mPaint);
        // 画下面
        mSourRect.set(0, mHeightDown, mSourceBmp.getWidth(), mSourceBmp.getHeight());
        mDisRect.set(0, mHeightDown + mHeightenVar, mDrawWidth, mDrawHeight+mHeightenVar);
        canvas.drawBitmap(mSourceBmp, mSourRect, mDisRect, mPaint);
        Log.d("BottomPicOperateView", "mHeightUp:" + mHeightUp);
        Log.d("BottomPicOperateView", "mHeightDown:" + mHeightDown);
        Log.d("BottomPicOperateView", "mDrawHeight:" + mDrawHeight);
        Log.d("BottomPicOperateView", "(mDrawHeight+mHeightenVar):" + (mDrawHeight + mHeightenVar));
    }

    public Bitmap getmSourceBmp() {
        return mSourceBmp;
    }

    public void setmSourceBmp(Bitmap mSourceBmp) {
        this.mSourceBmp = mSourceBmp;
    }


    public void setmHeightenVar(int mHeightenVar) {
        this.mHeightenVar = mHeightenVar;
    }

    public void setmDrawWidth(int mDrawWidth) {
        this.mDrawWidth = mDrawWidth;
    }

    public int getmDrawHeight() {
        return mDrawHeight;
    }

    public void setmDrawHeight(int mDrawHeight) {
        this.mDrawHeight = mDrawHeight;
    }

    public boolean isReset() {
        return isReset;
    }

    public void setReset(boolean reset) {
        isReset = reset;
    }

    public int getmHeightUp() {
        return mHeightUp;
    }

    public void setmHeightUp(int mHeightUp) {
        this.mHeightUp = mHeightUp;
    }

    public int getmHeightDown() {
        return mHeightDown;
    }

    public void setmHeightDown(int mHeightDown) {
        this.mHeightDown = mHeightDown;
    }
}
