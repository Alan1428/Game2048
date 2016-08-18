package com.example.air.game2048;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by gcx on 16/7/24.
 */
public class MyPhoto {
    Bitmap bitmap;
    int bitmapSize;

    public MyPhoto(Resources resources, int bitmapSize, int number) {
        this.bitmapSize = bitmapSize;
        bitmap = decodeBitmap(resources , Logic.ids[number], bitmapSize, bitmapSize);
    }

    public static Bitmap decodeBitmap(Resources resources,
                                      int id, int displayWidth, int displayHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeResource(resources, id, options);
        // op.inJustDecodeBounds = true;表示我们只读取Bitmap的宽高等信息，不读取像素。

        // op.outWidth表示的是图像真实的宽度
        // op.inSamplySize 表示的是缩小的比例
        // op.inSamplySize = 4,表示缩小1/4的宽和高，1/16的像素，android认为设置为2是最快的。
        // 获取比例大小
        if(displayWidth == 0)
            displayWidth = 100;
        if(displayHeight == 0)
            displayHeight = 100;
        int wRatio = (int) Math.ceil(options.outWidth / (float) displayWidth);
        int hRatio = (int) Math.ceil(options.outHeight / (float) displayHeight);
        // 如果超出指定大小，则缩小相应的比例
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                // 如果太宽，我们就缩小宽度到需要的大小，注意，高度就会变得更加的小。
                options.inSampleSize = wRatio;
            } else {
                options.inSampleSize = hRatio;
            }
        }
        options.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeResource(resources, id, options);
// 从原Bitmap创建一个给定宽高的Bitmap
        return Bitmap.createScaledBitmap(bmp, displayWidth, displayHeight, true);
    }
}
