package com.github.andrei1993ak.mentoring.task2;

import android.app.Application;

import com.github.andrei1993ak.mentoring.task2.holders.ContextHolder;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ContextHolder.getInstance().setContext(this);
    }
}
