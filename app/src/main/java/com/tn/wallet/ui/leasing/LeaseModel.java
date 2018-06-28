package com.tn.wallet.ui.leasing;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.tn.wallet.BR;
import com.tn.wallet.api.NodeManager;
import com.tn.wallet.payload.AssetBalance;
import com.tn.wallet.request.LeaseTransactionRequest;
import com.tn.wallet.request.TransferTransactionRequest;
import com.tn.wallet.util.MoneyUtil;

public class LeaseModel extends BaseObservable {

    //Views
    private String destinationAddress;
    private String maxAvailableString;
    private String customFee;
    private String amount;
    private String attachment;

    public AssetBalance sendingAsset;
    public AssetBalance feeAsset = NodeManager.get().wavesAsset;

    public String defaultSeparator;//Decimal separator based on locale

    public long maxAvailable;
    public long feeAmount;

    public String verifiedSecondPassword;

    public LeaseTransactionRequest getTxRequest() {
        LeaseTransactionRequest tx = new LeaseTransactionRequest(
                NodeManager.get().getPublicKeyStr(),
                destinationAddress,
                MoneyUtil.getUnscaledWaves(amount),
                System.currentTimeMillis(),
                2000000);
        return tx;
    }

    //Vars used for warning user of large tx
    public static final int LARGE_TX_SIZE = 516;//kb
    public static final long LARGE_TX_FEE = 80000;//USD
    public static final double LARGE_TX_PERCENTAGE = 1.0;//%

    @Bindable
    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
        notifyPropertyChanged(BR.destinationAddress);
    }

    @Bindable
    public String getMaxAvailable() {
        return maxAvailableString;
    }

    public void setMaxAvailable(String maxAvailable) {
        maxAvailableString = maxAvailable;
        notifyPropertyChanged(BR.maxAvailable);
    }

    @Bindable
    public String getCustomFee() {
        return customFee;
    }

    public void setCustomFee(String customFee) {
        this.customFee = customFee;
        notifyPropertyChanged(BR.customFee);
    }

    @Bindable
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
        notifyPropertyChanged(BR.amount);
    }

    @Bindable
    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
        notifyPropertyChanged(BR.attachment);
    }

}