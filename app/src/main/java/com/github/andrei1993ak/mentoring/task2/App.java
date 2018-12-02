package com.github.andrei1993ak.mentoring.task2;

import android.app.Application;

import com.github.andrei1993ak.mentoring.task2.holders.ContextHolder;
import com.orm.SugarApp;

public class App extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();

        ContextHolder.getInstance().setContext(this);
    }
}
