package com.tn.wallet.injection;

import android.app.Application;
import android.content.Context;

import com.tn.wallet.data.access.AccessState;
import com.tn.wallet.util.AESUtilWrapper;
import com.tn.wallet.util.AppUtil;
import com.tn.wallet.util.PrefsUtil;
import com.tn.wallet.util.StringUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    protected Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    protected PrefsUtil providePrefsUtil() {
        return new PrefsUtil(mApplication);
    }

    @Provides
    @Singleton
    protected AppUtil provideAppUtil() {
        return new AppUtil(mApplication);
    }

    @Provides
    protected AccessState provideAccessState() {
        return AccessState.getInstance();
    }

    @Provides
    protected AESUtilWrapper provideAesUtils() {
        return new AESUtilWrapper();
    }

    @Provides
    protected StringUtils provideStringUtils() {
        return new StringUtils(mApplication);
    }

}
