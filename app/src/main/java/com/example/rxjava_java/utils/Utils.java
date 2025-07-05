package com.example.rxjava_java.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public abstract class Utils {
    public static void closeSoftKeyboard(Context context, View view) {
        InputMethodManager imm = context.getSystemService(InputMethodManager.class);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String splitGetName(String fullName) {
        try {
            if (fullName == null || fullName.isEmpty()) return "";
            String[] strings = fullName.split("\\s+");
            return strings[strings.length - 1];
        } catch (Exception ex) {
            Log.e("ERROR_LOGIC", "Utils splitByName: " + ex.getMessage());
            return "";
        }

    }
}
