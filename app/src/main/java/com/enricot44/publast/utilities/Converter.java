package com.enricot44.publast.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import com.bumptech.glide.load.model.ByteArrayLoader;

import java.io.ByteArrayOutputStream;

public class Converter {
    @TypeConverter
    public static Bitmap fromByteArray(byte[] value) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(value, 0, value.length);
        return bitmap;
    }

    @TypeConverter
    public static byte[] bitmapToByte(Bitmap bitmap) {
        Bitmap bmp = bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }
}
