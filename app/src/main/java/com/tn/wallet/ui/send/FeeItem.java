package com.tn.wallet.ui.send;

import android.support.annotation.NonNull;

import com.tn.wallet.util.MoneyUtil;

public class FeeItem {
    @NonNull
    public String name;
    @NonNull
    public String fee;
    public long feeAmount;

    public FeeItem(@NonNull String name, long feeAmount) {
        this.name = name;
        this.feeAmount = feeAmount;
        this.fee = MoneyUtil.getDisplayWaves(feeAmount);
    }
}
