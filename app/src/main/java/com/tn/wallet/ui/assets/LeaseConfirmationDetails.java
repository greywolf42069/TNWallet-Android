package com.tn.wallet.ui.assets;

import com.tn.wallet.api.NodeManager;
import com.tn.wallet.payload.AssetBalance;
import com.tn.wallet.request.LeaseTransactionRequest;
import com.tn.wallet.request.TransferTransactionRequest;
import com.tn.wallet.util.MoneyUtil;

public class LeaseConfirmationDetails {

    public String fromLabel;
    public String toLabel;
    public String amount;
    public String fee;

    @Override
    public String toString() {
        return "PaymentConfirmationDetails{" +
                "fromLabel='" + fromLabel + '\'' +
                ", toLabel='" + toLabel + '\'' +
                ", amount='" + amount + '\'' +
                ", fee='" + fee + '\'' +
                '}';
    }

    public static LeaseConfirmationDetails fromRequest(LeaseTransactionRequest req) {
        System.out.println("req.fee: " + req.fee);
        LeaseConfirmationDetails d = new LeaseConfirmationDetails();
        d.fromLabel = NodeManager.get().getAddress();
        d.toLabel = req.recipient;
        d.amount = MoneyUtil.getDisplayWaves(req.amount);
        d.fee = MoneyUtil.getDisplayWaves(req.fee);
        System.out.println ("d.fee: " + d.fee);
        return d;

    }
}
