package com.tn.wallet.ui.dex.details;

import android.content.Context;

import com.tn.wallet.data.Events;
import com.tn.wallet.injection.Injector;
import com.tn.wallet.ui.base.BaseViewModel;
import com.tn.wallet.util.RxEventBus;
import com.tn.wallet.util.SSLVerifyUtil;
import com.tn.wallet.util.annotations.Thunk;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class DexDetailsViewModel extends BaseViewModel {

    private Context context;
    public DexDetailsModel dexDetailsModel;
    @Inject
    RxEventBus mRxEventBus;

    @Thunk
    DataListener dataListener;
    @Inject
    SSLVerifyUtil sslVerifyUtil;

    DexDetailsViewModel(Context context, DataListener dataListener) {

        Injector.getInstance().getDataManagerComponent().inject(this);

        this.context = context;
        this.dataListener = dataListener;
        dexDetailsModel = new DexDetailsModel();

        sslVerifyUtil.validateSSL();
    }

    public void sendEventChangeTimeFrame(CharSequence title) {
        mRxEventBus.post(new Events.ChangeTimeFrame(title.toString()));
    }

    public interface DataListener {

    }

    @Override
    public void onViewReady() {

    }
}
