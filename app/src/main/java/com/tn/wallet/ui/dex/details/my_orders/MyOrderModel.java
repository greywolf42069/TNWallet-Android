package com.tn.wallet.ui.dex.details.my_orders;

import com.tn.wallet.payload.WatchMarket;

public class MyOrderModel {

    private WatchMarket pairModel;

    public WatchMarket getPairModel() {
        return pairModel;
    }

    public void setPairModel(WatchMarket pairModel) {
        this.pairModel = pairModel;
    }
}
