package com.tn.wallet.ui.launcher;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.tn.wallet.injection.Injector;
import com.tn.wallet.ui.base.BaseViewModel;
import com.tn.wallet.util.AppUtil;
import com.tn.wallet.util.PrefsUtil;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class LauncherViewModel extends BaseViewModel {

    @Inject protected AppUtil mAppUtil;

    @Inject protected PrefsUtil mPrefsUtil;
    private DataListener mDataListener;

    public interface DataListener {

        Intent getPageIntent();

        void onNotLoggedIn();

        void onRequestPin();

        void onCorruptPayload();

        boolean onStartMainActivity(String publicKey);

        void finish();

    }

    public LauncherViewModel(DataListener listener) {
        Injector.getInstance().getDataManagerComponent().inject(this);
        mDataListener = listener;
    }

    @Override
    public void onViewReady() {
        storeIncomingURI();

        String loggedInGuid = mPrefsUtil.getGlobalValue(PrefsUtil.GLOBAL_LOGGED_IN_GUID, "");
        String pubKey = mPrefsUtil.getValue(PrefsUtil.KEY_PUB_KEY, "");
        if (loggedInGuid.isEmpty() || pubKey.isEmpty()
                || !mDataListener.onStartMainActivity(pubKey)) {
            mDataListener.onNotLoggedIn();
        }
    }

    private void storeIncomingURI() {
        String action = mDataListener.getPageIntent().getAction();
        String scheme = mDataListener.getPageIntent().getScheme();
        if (action != null && Intent.ACTION_VIEW.equals(action) && scheme != null && scheme.equals("TN")) {
            mPrefsUtil.setGlobalValue(PrefsUtil.GLOBAL_SCHEME_URL, mDataListener.getPageIntent().getData().toString());
        }
    }

    @NonNull
    public AppUtil getAppUtil() {
        return mAppUtil;
    }
}
