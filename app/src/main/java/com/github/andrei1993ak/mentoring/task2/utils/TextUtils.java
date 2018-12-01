package com.github.andrei1993ak.mentoring.task2.utils;

public class TextUtils {

    public interface Constants {
        String EMPTY = "";
    }

    public static boolean isEmpty(final CharSequence pEditable) {
        return pEditable == null || pEditable.length() == 0;
    }
}
