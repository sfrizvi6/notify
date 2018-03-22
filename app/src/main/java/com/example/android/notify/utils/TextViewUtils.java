package com.example.android.notify.utils;

import android.view.View;
import android.widget.TextView;

public class TextViewUtils {

    public static void setTextAndVisibility(TextView textView, CharSequence text) {
        if (text == null || text.length() < 1) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        }
    }
}