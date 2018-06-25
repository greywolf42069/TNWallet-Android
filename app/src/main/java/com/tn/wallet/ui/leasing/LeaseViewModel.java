package com.tn.wallet.ui.leasing;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.util.Log;

import com.tn.wallet.R;
import com.tn.wallet.api.NodeManager;
import com.tn.wallet.data.access.AccessState;
import com.tn.wallet.data.rxjava.RxUtil;
import com.tn.wallet.injection.Injector;
import com.tn.wallet.payload.AssetBalance;
import com.tn.wallet.request.LeaseTransactionRequest;
import com.tn.wallet.request.TransferTransactionRequest;
import com.tn.wallet.ui.assets.AssetsHelper;
import com.tn.wallet.ui.assets.ItemAccount;
import com.tn.wallet.ui.assets.LeaseConfirmationDetails;
import com.tn.wallet.ui.assets.PaymentConfirmationDetails;
import com.tn.wallet.ui.base.BaseViewModel;
import com.tn.wallet.ui.customviews.ToastCustom;
import com.tn.wallet.util.AddressUtil;
import com.tn.wallet.util.MoneyUtil;
import com.tn.wallet.util.PrefsUtil;
import com.tn.wallet.util.SSLVerifyUtil;
import com.tn.wallet.util.StringUtils;
import com.tn.wallet.util.annotations.Thunk;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class LeaseViewModel extends BaseViewModel {

    private final String TAG = getClass().getSimpleName();

    @Thunk
    DataListener dataListener;
    private Context context;

    public LeaseModel leaseModel;

    @Inject
    PrefsUtil prefsUtil;
    @Inject
    AssetsHelper assetsHelper;
    @Inject
    SSLVerifyUtil sslVerifyUtil;
    @Inject
    StringUtils stringUtils;

    LeaseViewModel(Context context, DataListener dataListener) {

        Injector.getInstance().getDataManagerComponent().inject(this);

        this.context = context;
        this.dataListener = dataListener;

        leaseModel = new LeaseModel();
        leaseModel.defaultSeparator = MoneyUtil.getDefaultDecimalSeparator();

        sslVerifyUtil.validateSSL();
    }

    @Override
    public void onViewReady() {
        // No-op
    }

    @Override
    public void destroy() {
        super.destroy();
        context = null;
        dataListener = null;
    }

    String getDefaultSeparator() {
        return leaseModel.defaultSeparator;
    }

    public interface DataListener {

        void onHideSendingAddressField();

        void onShowInvalidAmount();

        void onShowLeaseDetails(LeaseConfirmationDetails confirmationDetails);

        void onShowToast(@StringRes int message, @ToastCustom.ToastType String toastType);

        void onShowLeaseSuccess(LeaseTransactionRequest signed);

        void finishPage();

        void hideKeyboard();

        void onSetSelection(int pos);
    }

    List<ItemAccount> getAssetsList() {

        ArrayList<ItemAccount> result = new ArrayList<ItemAccount>() {{
            addAll(assetsHelper.getAccountItems());
        }};

        if (result.size() == 1) {
            //Only a single account/address available in wallet
            if (dataListener != null)
                dataListener.onHideSendingAddressField();
            setSendingAssets(result.get(0));
        }

        return result;
    }

    void handleIncomingUri(String strUri) {
        try {
            if (strUri == null) return;

            Uri uri = Uri.parse(strUri);
            if (uri == null) return;

            leaseModel.setDestinationAddress(uri.getHost());
            String assetParam = uri.getQueryParameter("asset");

            List<ItemAccount> assets = getAssetsList();
            AssetBalance selAsset = assets.get(0).accountObject;

            if (assetParam != null) {
                for (int i = 0; i < assets.size(); ++i) {
                    if (assetParam.equals(assets.get(i).accountObject.assetId)) {
                        selAsset = assets.get(i).accountObject;
                        if (dataListener != null) dataListener.onSetSelection(i);
                    }
                }
            }

            String amountParam = uri.getQueryParameter("amount");
            if (amountParam != null) {
                long amount = MoneyUtil.getUnscaledValue(amountParam, 0);
                leaseModel.setAmount(MoneyUtil.getScaledText(amount, selAsset));
            }

            String attachment = uri.getQueryParameter("attachment");
            if (attachment != null) {
                leaseModel.setAttachment(attachment);
            }
        } catch (UnsupportedOperationException e) {
            // TODO: 09.08.17 What is means? Need to reproduce. Hot fix
        }
    }

    /**
     * Wrapper for calculateTransactionAmounts
     */
    void spendAllClicked() {
        leaseModel.setAmount(MoneyUtil.getScaledText(leaseModel.maxAvailable, leaseModel.sendingAsset));
    }


//    private boolean isSameSendingAndFeeAssets() {
//        if (leaseModel.feeAsset != null && leaseModel.sendingAsset != null)
//            return (leaseModel.feeAsset.assetId == null && leaseModel.sendingAsset.assetId == null) ||
//                    leaseModel.feeAsset.assetId.equals(leaseModel.sendingAsset.assetId);
//        else
//            return false;
//    }

    private boolean isSameSendingAndFeeAssets() {
        if (leaseModel.feeAsset != null && leaseModel.sendingAsset != null) {
            if (leaseModel.feeAsset.assetId == null && leaseModel.sendingAsset.assetId == null) {
                return true;
            } else {
                if (leaseModel.feeAsset.assetId != null && leaseModel.sendingAsset.assetId != null)
                    return leaseModel.feeAsset.assetId.equals(leaseModel.sendingAsset.assetId);
            }
        }
        return false;
    }

    /**
     * Update max available. Values are bound to UI, so UI will update automatically
     */
    public void updateMaxAvailable() {
        if (leaseModel.sendingAsset != null) {
            leaseModel.maxAvailable = isSameSendingAndFeeAssets() ? leaseModel.sendingAsset.balance - leaseModel.feeAmount :
                    leaseModel.sendingAsset.balance;

            if (leaseModel.maxAvailable <= 0 && context != null) {
                leaseModel.setMaxAvailable(stringUtils.getString(R.string.insufficient_funds));
            } else {
                String amountFormatted = MoneyUtil.getScaledText(leaseModel.maxAvailable, leaseModel.sendingAsset);
                leaseModel.setMaxAvailable(stringUtils.getString(R.string.max_available) + " " + amountFormatted + " " + leaseModel.sendingAsset.getName());
            }
        }
    }

    void sendClicked() {
        confirmPayment();

        if (dataListener != null)
            dataListener.hideKeyboard();

        int res = validateTransfer(leaseModel.getTxRequest());

        if (res == 0) {
            confirmPayment();
        } else {
            if (dataListener != null)
                dataListener.onShowToast(res, ToastCustom.TYPE_ERROR);
        }
    }

    /**
     * Sets payment confirmation details to be displayed to user and fires callback to display
     * this.
     */
    @Thunk
    void confirmPayment() {
        LeaseConfirmationDetails details = LeaseConfirmationDetails.fromRequest(leaseModel.getTxRequest());

        if (dataListener != null)
            dataListener.onShowLeaseDetails(details);
    }

    private boolean isFundSufficient(LeaseTransactionRequest tx) {
        // TODO: check if enough for leasing.
        return true;
    }

    private int validateTransfer(LeaseTransactionRequest tx) {
        if (!AddressUtil.isValidAddress(tx.recipient)) {
            return R.string.invalid_address;
        } else if (tx.amount <= 0) {
            return R.string.invalid_amount;
        } else if (tx.amount > Long.MAX_VALUE - tx.fee) {
            return R.string.invalid_amount;
        } else if (tx.fee <= 0 || tx.fee < TransferTransactionRequest.MinFee) {
            return R.string.insufficient_fee;
        } else if (!isFundSufficient(tx)) {
            return R.string.insufficient_funds;
        }

        return 0;
    }

    void setSendingAssets(ItemAccount selectedItem) {
        if (selectedItem.accountObject != null) {
            leaseModel.sendingAsset = selectedItem.accountObject;
            updateMaxAvailable();
        }
    }

    public LeaseTransactionRequest signTransaction() {
        byte[] pk = AccessState.getInstance().getPrivateKey();
        if (pk != null) {
            LeaseTransactionRequest signed = leaseModel.getTxRequest();
            signed.sign(pk);
            return signed;
        } else {
            return null;
        }
    }

    public void submitLease(LeaseTransactionRequest signed) {
        NodeManager.get().broadcastLease(signed)
                .compose(RxUtil.applySchedulersToObservable()).subscribe(tx -> {
            if (dataListener != null)
                dataListener.onShowLeaseSuccess(signed);
        }, err -> {
            Log.e(TAG, "submitPayment: ", err);
            if (dataListener != null)
                dataListener.onShowToast(R.string.transaction_failed, ToastCustom.TYPE_ERROR);
        });

    }

    public PrefsUtil getPrefsUtil() {
        return prefsUtil;
    }
}
