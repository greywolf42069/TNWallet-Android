package com.tn.wallet.payload;

import com.google.common.base.Optional;
import com.tn.wallet.util.MoneyUtil;

public class LeaseCancelTransaction extends Transaction {
    public String leaseId;

    public LeaseCancelTransaction(int type, String id, String sender, long timestamp,
                                  long fee, String leaseId) {
        //super(type, id, sender, timestamp, amount, fee);
        this.type = type;
        this.id = id;
        this.sender = sender;
        this.timestamp = timestamp;
        this.fee = fee;
        this.leaseId = leaseId;
    }


    /*@Override
    public String getDisplayAmount() {
        return MoneyUtil.getScaledText(amount, 8);
    }

    @Override
    public int getDecimals() {
        return 8;
    }

    @Override
    public Optional<String> getConterParty() {
        return Optional.of(recipient);
    }*/

}
