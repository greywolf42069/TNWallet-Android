package com.tn.wallet.injection;

import android.content.Context;

import com.tn.wallet.data.datamanagers.ReceiveDataManager;
import com.tn.wallet.data.datamanagers.TransactionListDataManager;
import com.tn.wallet.data.fingerprint.FingerprintAuthImpl;
import com.tn.wallet.data.stores.TransactionListStore;
import com.tn.wallet.ui.assets.AssetsHelper;
import com.tn.wallet.ui.fingerprint.FingerprintHelper;
import com.tn.wallet.util.PrefsUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class DataManagerModule {


    @Provides
    @ViewModelScope
    protected ReceiveDataManager provideReceiveDataManager() {
        return new ReceiveDataManager();
    }

    @Provides
    @ViewModelScope
    protected AssetsHelper provideWalletAccountHelper(PrefsUtil prefsUtil) {
        return new AssetsHelper(prefsUtil);
    }

    @Provides
    @ViewModelScope
    protected TransactionListDataManager provideTransactionListDataManager(TransactionListStore transactionListStore) {
        return new TransactionListDataManager(transactionListStore);
    }

    @Provides
    @ViewModelScope
    protected FingerprintHelper provideFingerprintHelper(Context applicationContext,
                                                         PrefsUtil prefsUtil) {
        return new FingerprintHelper(applicationContext, prefsUtil, new FingerprintAuthImpl());
    }

}
