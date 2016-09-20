package com.example.heightenmodule.interfaces;

/**
 * 选择区域变化给予SeekBar初始化的回调
 * Created by meitu on 2016/9/20.
 */

public interface ChooseHeightenZoneChangeCallBack {
     //初始化SeekBar
     void initSb(int  num);
     //返回参考线位置
     void getRefHeighs(float heightA,float heightB);
}
