package com.example.heightenmodule.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

/**
 * Created by meitu on 2016/9/20.
 */

public class Util {
    /**
     * 获取屏幕宽高
     * @param activity 传入的Activity
     * @return 返回一个带宽高的int[],第一个为宽，第二个为高
     */
    public static int[] getScreenSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;

        int screenHeigh = dm.heightPixels;
        return new int[] {screenWidth, screenHeigh};
    }

    /**
     * 压缩图片
     * @param context 上下文
     * @param soureceId 文件资源Id
     * @param disWidth  目标宽度
     * @param disHeight 目标高度
     * @return 压缩大小后的图片
     */
    public static Bitmap compressBitmap(Context context, int soureceId, float disWidth, float disHeight) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 设置true，仅仅获取到图片大小
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), soureceId, newOpts);
        //读取图片
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float hh = disHeight;
        float ww = disWidth;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
//        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
//            be = (int) (newOpts.outWidth / ww);
//        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
//            be = (int) (newOpts.outHeight / hh);
//        }
//        if (be <= 0)
//            be = 1;
        be = (int) ((w / disWidth + h/ disHeight) / 2F+0.5F);

        newOpts.inSampleSize = be;// 设置缩放比例

        return BitmapFactory.decodeResource(context.getResources(),soureceId,newOpts);
    }
}
