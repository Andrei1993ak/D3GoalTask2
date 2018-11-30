package com.github.andrei1993ak.mentoring.task2.core;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public interface ICallExecutor<Model> {
    void enqueue(ISuccess<? super Model> pSusses);

    class Impl {
        private static final Executor sDefaultExecutor = new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());

        public static <Model> ICallExecutor<Model> newInstance(final ICallable<Model> pCall) {
            return newInstance(pCall, sDefaultExecutor);
        }

        public static <Model> ICallExecutor<Model> newInstance(final ICallable<Model> pCall, final Executor pExecutor) {
            return new ICallExecutor<Model>() {

                @Override
                public void enqueue(final ISuccess<? super Model> pSusses) {
                    if (pSusses.isAlive()) {
                        pExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final Model model = pCall.call();
                                    if (pSusses.isAlive()) {
                                        pSusses.onResult(model);
                                    }
                                } catch (final Exception pE) {
                                    if (pSusses.isAlive()) {
                                        pSusses.onError(pE);
                                    }
                                }
                            }
                        });
                    }
                }
            };
        }
    }
}

