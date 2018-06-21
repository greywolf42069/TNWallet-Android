package com.tn.wallet.injection;

import com.tn.wallet.BlockchainApplication;
import com.tn.wallet.ui.auth.EnvironmentManager;
import com.tn.wallet.util.AppUtil;
import com.tn.wallet.util.RxEventBus;
import com.tn.wallet.util.exceptions.LoggingExceptionHandler;

import javax.inject.Singleton;

import dagger.Component;

@SuppressWarnings("WeakerAccess")
@Singleton
@Component(modules = {
        ApplicationModule.class,
        ApiModule.class
})
public interface ApplicationComponent {

    DataManagerComponent plus(DataManagerModule userModule);

    void inject(AppUtil appUtil);

    void inject(LoggingExceptionHandler loggingExceptionHandler);

    void inject(EnvironmentManager environmentManager);

    void inject(BlockchainApplication blockchainApplication);

    RxEventBus eventBus();
}
