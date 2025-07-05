package com.example.rxjava_java.utils;

import android.content.Context;
import android.view.View;

import com.example.rxjava_java.R;
import com.google.android.material.snackbar.Snackbar;

public abstract class SnackBarHelper {
    public static Snackbar getSnackbarNegative(Context context, View view, String msg) {
        return Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(context.getResources().getColor(R.color.red));
    }
    public static Snackbar getSnackbarPositive(Context context, View view, String msg) {
        return Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(context.getResources().getColor(R.color.green_light));
    }
    public static Snackbar getSnackbarNeutral(Context context, View view, String msg) {
        return Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(context.getResources().getColor(R.color.white));
    }
}
