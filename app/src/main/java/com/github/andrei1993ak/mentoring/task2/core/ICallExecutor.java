package com.github.andrei1993ak.mentoring.task2.core;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public interface ICallExecutor<Model> {
    void enqueueCall(ISusses<Model> pSusses, ICallable<Model> pCallable);

    class Impl {
        private static final Executor sDefaultExecutor = new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());

        public static <Model> ICallExecutor<Model> newInstance(final ICallable<Model> pCall) {
            return newInstance(pCall, sDefaultExecutor);
        }

        public static <Model> ICallExecutor<Model> newInstance(final ICallable<Model> pCall, final Executor pExecutor) {
            return new ICallExecutor<Model>() {

                @Override
                public void enqueueCall(final ISusses<Model> pSusses, final ICallable<Model> pCallable) {
                    if (pSusses.isAlive()) {
                        sDefaultExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final Model model = pCallable.call();
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

