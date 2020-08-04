package com.enricot44.publast.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOUtils {
    public static String saveImageAsFile(Context context, Bitmap image) {
        String url = "trinko_tempimage";

        String fileName = Uri.parse(url).getLastPathSegment();
        File file = null;
        try {
            file = File.createTempFile(fileName, null, context.getCacheDir());


            FileOutputStream fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 90, fOut);

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    public static Bitmap retrieveImageFromFile(Context context, String url) {
        // String url = context.getCacheDir().getAbsolutePath() + "trinko_tempimage";

        return BitmapFactory.decodeFile(url);
    }
}
