package com.github.andrei1993ak.mentoring.task2.core;

import android.support.annotation.WorkerThread;

public interface ISuccess<Model> {

    @WorkerThread
    void onResult(Model pModel);

    @WorkerThread
    void onError(Throwable pThrowable);

    boolean isAlive();
}
