package com.example.android.notify.data;


import android.graphics.Bitmap;
import android.text.TextUtils;

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

    static byte[] getCharSequenceAsByteArray(CharSequence[] charSequences) {
        if (charSequences == null) {
            return null;
        }
        CharSequence charSequence = getCharSequenceFromCharSequenceArray(charSequences);
        if (charSequence != null) {
            return charSequence.toString().getBytes(Charset.forName("UTF-8"));
        }
        return null;
    }

    static CharSequence getCharSequenceFromCharSequenceArray(CharSequence[] charSequences) {
        // TODO: convert this to RV later instead of adding '\n' to end of every string
        if (charSequences != null) {
            for (int i = 0; i < charSequences.length - 1; i++) {
                CharSequence[] array = { charSequences[i], "\n" };
                charSequences[i] = TextUtils.concat(array);
            }
            return TextUtils.concat(charSequences);
        }
        return null;
    }
}
