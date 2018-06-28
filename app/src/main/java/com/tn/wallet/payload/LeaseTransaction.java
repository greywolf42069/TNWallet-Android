package com.tn.wallet.payload;

import com.google.common.base.Optional;
import com.tn.wallet.api.NodeManager;
import com.tn.wallet.util.MoneyUtil;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

public class LeaseTransaction extends Transaction {
    public String recipient;

    public LeaseTransaction(int type, String id, String sender, long timestamp, long amount,
                            long fee, String recipient) {
        super(type, id, sender, timestamp, amount, fee);
        this.recipient = recipient;
    }


    @Override
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
    }

}
