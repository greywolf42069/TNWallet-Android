package com.tn.wallet.ui.auth;

import com.tn.wallet.injection.Injector;
import com.tn.wallet.ui.base.BaseViewModel;
import com.tn.wallet.util.PrefsUtil;

import javax.inject.Inject;

public class LandingViewModel extends BaseViewModel {
    @Inject protected PrefsUtil prefsUtil;

    public LandingViewModel() {
        Injector.getInstance().getDataManagerComponent().inject(this);
    }

    private String[] guids;

    public boolean isNoStoredKeys() {
        return guids.length == 0;
    }

    @Override
    public void onViewReady() {
        guids = prefsUtil.getGlobalValueList(EnvironmentManager.get().current().getName() + PrefsUtil.LIST_WALLET_GUIDS);
    }

}
