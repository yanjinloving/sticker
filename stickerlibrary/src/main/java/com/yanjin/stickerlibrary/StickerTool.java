package com.yanjin.stickerlibrary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StickerTool {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 布局变成bitmap
     * @param view
     * @return
     */
    public static Bitmap getViewBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 保存截屏
     *
     * @param bitmap
     */
    public static void saveImageToGallery(Context context,Bitmap bitmap) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if(!bitmap.isRecycled()){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ScannerByReceiver(context, file.getAbsolutePath());
        }
    }

    private static void ScannerByReceiver(Context context, String path) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + path)));
    }

}
