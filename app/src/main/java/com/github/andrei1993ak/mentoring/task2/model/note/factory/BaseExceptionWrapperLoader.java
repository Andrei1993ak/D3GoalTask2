package com.github.andrei1993ak.mentoring.task2.model.note.factory;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class BaseExceptionWrapperLoader<T> extends AsyncTaskLoader<ResultWrapper<T>> {

    public BaseExceptionWrapperLoader(final Context context) {
        super(context);
    }

    @Override
    public ResultWrapper<T> loadInBackground() {
        try {
            final T result = loadResultDataInBackground();
            return new ResultWrapper<>(result);
        } catch (final Exception pE) {
            return new ResultWrapper<>(pE);
        }
    }

    public abstract T loadResultDataInBackground() throws Exception;
}
