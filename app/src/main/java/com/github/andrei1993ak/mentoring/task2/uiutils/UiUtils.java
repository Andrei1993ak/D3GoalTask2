package com.github.andrei1993ak.mentoring.task2.uiutils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

public class UiUtils {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isContextAlive(final Context pContext) {
        if (pContext == null) {
            return false;
        }
        if (pContext instanceof Activity) {
            final Activity activity = (Activity) pContext;
            if (activity.isFinishing()) {
                return false;
            }
            if (hasJellyBeanMR1()) {
                if (activity.isDestroyed()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
}

