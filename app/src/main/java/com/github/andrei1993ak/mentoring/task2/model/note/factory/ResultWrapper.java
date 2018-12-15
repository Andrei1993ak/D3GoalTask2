package com.github.andrei1993ak.mentoring.task2.model.note.factory;

public class ResultWrapper<K> {

    private final Exception mException;
    private final K mResult;

    ResultWrapper(final K pResult) {
        mResult = pResult;
        mException = null;
    }

    ResultWrapper(final Exception pException) {
        mException = pException;
        mResult = null;
    }

    public Exception getException() {
        return mException;
    }

    public K getResult() {
        return mResult;
    }
}
