package com.example.android.notify.data;


import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

class DbUtils {

    static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    static byte[] getCharSequenceAsByteArray(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        return charSequence.toString().getBytes(Charset.forName("UTF-8"));
    }
}
