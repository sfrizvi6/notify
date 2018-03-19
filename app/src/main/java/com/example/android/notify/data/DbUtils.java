package com.example.android.notify.data;


import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

class DbUtils {

    static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
