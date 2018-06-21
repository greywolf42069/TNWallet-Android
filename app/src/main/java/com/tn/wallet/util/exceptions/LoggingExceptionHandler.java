package com.tn.wallet.util.exceptions;

import javax.inject.Inject;

import com.tn.wallet.injection.Injector;
import com.tn.wallet.util.AppUtil;

@SuppressWarnings("WeakerAccess")
public class LoggingExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Thread.UncaughtExceptionHandler mRootHandler;
    @Inject protected AppUtil mAppUtil;

    public LoggingExceptionHandler() {
        Injector.getInstance().getAppComponent().inject(this);
        mRootHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        mAppUtil.restartApp();

        // Re-throw the exception so that the system can fail as it normally would, and so that
        // Firebase can log the exception automatically
        mRootHandler.uncaughtException(thread, throwable);
    }
}
