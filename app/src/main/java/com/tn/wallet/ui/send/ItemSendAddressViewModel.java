package com.tn.wallet.ui.send;

import android.databinding.BaseObservable;

import com.tn.wallet.ui.assets.ItemAccount;
import com.tn.wallet.ui.base.ViewModel;

public class ItemSendAddressViewModel extends BaseObservable implements ViewModel {

    private ItemAccount addressItem;

    public ItemSendAddressViewModel(ItemAccount address) {
        addressItem = address;
    }

    public String getLabel() {
        return addressItem.label;
    }

    public String getBalance() {
        return addressItem.displayBalance;
    }

    public void setAddress(ItemAccount address) {
        addressItem = address;
        notifyChange();
    }

    @Override
    public void destroy() {
        addressItem = null;
    }

}
