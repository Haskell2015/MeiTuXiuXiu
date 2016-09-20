package com.example.heightenmodule.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.heightenmodule.R;
import com.example.heightenmodule.interfaces.ChooseHeightenZoneChangeCallBack;
import com.example.heightenmodule.util.Const;
import com.example.heightenmodule.util.Util;
import com.example.heightenmodule.view.BottomPicOperateView;
import com.example.heightenmodule.view.ChooseHeightenZoneView;

public class HeightenHomeActivity extends Activity implements View.OnClickListener {

    private BottomPicOperateView mBottomPicOperateView;
    private ChooseHeightenZoneView mChooseHeightenZoneView;
    private SeekBar mHeightenSb;
    private Button mBtnReset;
    private TextView mTvCompare;
    // 滑杆增高量
    private int mHeightenVar;

    private Bitmap mSourceBmp, mIconNormal, mIconPressed;
    private int mScreenWidth, mScreenHeight;
    private int mRefHeightDownCurrent, mRefHeightUpCurrent;
    private int mRefHeightUp, mRefHeightDown;

    private boolean isFirstIn;
    private boolean isBottomChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heighten_home);
        getScreenSize(this);
        initRefHeight();
        getBitmap();
        intiLayersView();
        initView();
    }

    private void initRefHeight() {
        mRefHeightDown = (int) (mScreenHeight * Const.OPERATE_PIC_HEIGHT_RATE * Const.REF_B_LOCATION_RATE);
        mRefHeightUp = (int) (mScreenHeight * Const.OPERATE_PIC_HEIGHT_RATE * Const.REF_A_LOCATION_RATE);
    }

    private void getBitmap() {
        mSourceBmp = Util.compressBitmap(getApplicationContext(), R.drawable.pic, mScreenWidth,
            mScreenHeight * Const.OPERATE_PIC_HEIGHT_RATE);
        mIconNormal = BitmapFactory.decodeResource(getResources(), R.drawable.heighten_indicator);
        mIconPressed = BitmapFactory.decodeResource(getResources(), R.drawable.heighten_indicator_pressed);
    }

    private void getScreenSize(Activity activity) {
        int[] screenSize = Util.getScreenSize(activity);
        mScreenWidth = screenSize[0];
        mScreenHeight = screenSize[1];
    }

    /**
     * 初始化各图层
     */
    private void intiLayersView() {
        intiBottomView();
        initMidView();
    }

    /**
     * 初始化中间选择区域层
     */
    private void initMidView() {
        // TODO 用Builder模式优化
        mChooseHeightenZoneView = (ChooseHeightenZoneView) findViewById(R.id.choosezone_heighten);
        mChooseHeightenZoneView.setDrawWidth(mScreenWidth);
        mChooseHeightenZoneView.setDrawHeight((int) (mScreenHeight * Const.OPERATE_PIC_HEIGHT_RATE));
        mChooseHeightenZoneView.setHeightA(mRefHeightUp);
        mChooseHeightenZoneView.setHeightB(mRefHeightDown);
        mChooseHeightenZoneView.setmBmpIconNormal(mIconNormal);
        mChooseHeightenZoneView.setmBmpIcoPressed(mIconPressed);
        mChooseHeightenZoneView.setmInitSbCallBack(new ChooseHeightenZoneChangeCallBack() {
            @Override
            public void initSb(int b) {
                mHeightenSb.setProgress(b);
            }

            @Override
            public void getRefHeighs(float heightA, float heightB) {
                isBottomChange = false;
                mRefHeightDown = (int) Math.max(heightA, heightB);
                mRefHeightUp = (int) Math.min(heightA, heightB);
            }
        });
        mChooseHeightenZoneView.invalidate();
    }

    /**
     * 初始化底层图片操作层
     */
    private void intiBottomView() {
        mBottomPicOperateView = (BottomPicOperateView) findViewById(R.id.heighten_bottom_pic_operate_myview);
        mBottomPicOperateView.setReset(true);
        mBottomPicOperateView.setmSourceBmp(mSourceBmp);
        mBottomPicOperateView.setmDrawHeight((int) (mScreenHeight * Const.OPERATE_PIC_HEIGHT_RATE));
        mBottomPicOperateView.setmDrawWidth(mScreenWidth);
        mBottomPicOperateView.invalidate();
    }

    private void initView() {
        isFirstIn = true;

        mBtnReset = (Button) findViewById(R.id.heighten_reset_btn);
        mBtnReset.setOnClickListener(this);

        mTvCompare = (TextView) findViewById(R.id.heighten_compare_tv);
        mTvCompare.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        mBottomPicOperateView.setReset(true);
                        mBottomPicOperateView.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        mBottomPicOperateView.setReset(false);
                        mBottomPicOperateView.invalidate();
                        break;
                    }
                    default:
                        break;
                }
                // 拦截自己处理
                return true;
            }
        });

        mHeightenSb = (SeekBar) findViewById(R.id.heighten_heighten_sb);
        mHeightenSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                isFirstIn = false;
                mHeightenVar = (int) (progress * Const.SEEKBAR_TO_HEIGHTEN_RATE);
                mRefHeightDownCurrent = mRefHeightDown + mHeightenVar;
                mRefHeightUpCurrent = mRefHeightUp - mHeightenVar;

                if (isBottomChange) {// 不处理则是滑倒0的效果
                    mBottomPicOperateView.setmHeightenVar(mHeightenVar);
                    // mBottomPicOperateView.setmHeightUp(mRefHeightUpCurrent);
                    // mBottomPicOperateView.setmHeightDown(mRefHeightDownCurrent);
                    mBottomPicOperateView.setmHeightUp(mRefHeightUp);
                    mBottomPicOperateView.setmHeightDown(mRefHeightDown);

                    Log.d("HeightenHomeActivity", "mHeightenVar:" + mHeightenVar);
                    Log.d("HeightenHomeActivity", "mRefHeightUp:" + mRefHeightUp);
                    Log.d("HeightenHomeActivity", "mRefHeightDown:" + mRefHeightDown);

                    mBottomPicOperateView.invalidate();
                }

                mChooseHeightenZoneView.setHeightA(mRefHeightUpCurrent);
                mChooseHeightenZoneView.setHeightB(mRefHeightDownCurrent);
                mChooseHeightenZoneView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isBottomChange = true;
                mChooseHeightenZoneView.setZoneShow(false);
                mChooseHeightenZoneView.invalidate();
                mBottomPicOperateView.setReset(isFirstIn || false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mChooseHeightenZoneView.setZoneShow(true);
                mChooseHeightenZoneView.invalidate();
                mBtnReset.setEnabled(true);
            }
        });

    }

    /**
     * 点击事件处理
     * @param v 点击的控件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.heighten_reset_btn: {
                isFirstIn = true;
                initRefHeight();
                mHeightenVar = 0;
                mHeightenSb.setProgress(0);
                intiLayersView();
                mBtnReset.setEnabled(false);
                break;
            }
            default:
                break;
        }
    }
}
