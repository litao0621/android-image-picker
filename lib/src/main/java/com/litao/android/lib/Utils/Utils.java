package com.litao.android.lib.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.view.Display;

import java.io.File;
import java.util.UUID;

/**
 * Created by 李涛 on 16/4/28.
 */
public class Utils {
    private static Point getScreenSize(Context activity) {
        Display display = ((Activity) activity).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int getScreenWidth(Context activity) {
        return getScreenSize(activity).x;
    }

    public static int getScreenHeight(Context activity) {
        return getScreenSize(activity).y;
    }

    public static File createTmpFile(Context context) {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String fileName = UUID.randomUUID().toString();
            return new File(pic, fileName + ".jpg");
        } else {
            File cacheDir = context.getCacheDir();
            String fileName = UUID.randomUUID().toString();
            return new File(cacheDir, fileName + ".jpg");
        }

    }

    public static void addMediaToGallery(Context context,Uri uri) {
        if (uri == null) {
            return;
        }
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        context.sendBroadcast(mediaScanIntent);
    }
}
