package com.tn.wallet.ui.transactions;

import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.tn.wallet.R;
import com.tn.wallet.api.NodeManager;
import com.tn.wallet.data.datamanagers.TransactionListDataManager;
import com.tn.wallet.injection.Injector;
import com.tn.wallet.payload.AssetBalance;
import com.tn.wallet.payload.IssueTransaction;
import com.tn.wallet.payload.LeaseTransaction;
import com.tn.wallet.payload.Transaction;
import com.tn.wallet.ui.base.BaseViewModel;
import com.tn.wallet.util.MoneyUtil;
import com.tn.wallet.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import static com.tn.wallet.ui.assets.AssetsActivity.KEY_MY_ASSETS_LIST_POSITION;
import static com.tn.wallet.ui.balance.TransactionsFragment.KEY_TRANSACTION_LIST_POSITION;
import static com.tn.wallet.ui.balance.TransactionsFragment.TX_AS_JSON;
import static com.tn.wallet.ui.transactions.IssueDetailActivity.KEY_INTENT_ACTIONS_ENABLED;

@SuppressWarnings("WeakerAccess")
public class LeaseDetailViewModel extends BaseViewModel {

    private LeaseDataListener mDataListener;
    @Inject StringUtils mStringUtils;
    @Inject TransactionListDataManager mTransactionListDataManager;

    private Context context;

    @VisibleForTesting
    LeaseTransaction mTransaction;
    AssetBalance mAssetBalance;
    boolean actionsEnabled;

    public interface LeaseDataListener {

        Intent getPageIntent();

        void pageFinish();

    }

    public LeaseDetailViewModel(Context context, LeaseDataListener listener) {
        this.context = context;
        Injector.getInstance().getDataManagerComponent().inject(this);
        mDataListener = listener;
    }

    @Override
    public void onViewReady() {
        if (mDataListener.getPageIntent() != null) {
            if (mDataListener.getPageIntent().hasExtra(TX_AS_JSON)) {
                Gson gson = new Gson();

                mTransaction = gson.fromJson(mDataListener.getPageIntent().getStringExtra(TX_AS_JSON), LeaseTransaction.class);
            } else if (mDataListener.getPageIntent().hasExtra(KEY_TRANSACTION_LIST_POSITION)) {
                mTransaction = (LeaseTransaction) mTransactionListDataManager.getTransactionList().get(
                        mDataListener.getPageIntent().getIntExtra(KEY_TRANSACTION_LIST_POSITION, 0));
            } else if (mDataListener.getPageIntent().hasExtra(KEY_MY_ASSETS_LIST_POSITION)) {
                /*mAssetBalance = NodeManager.get().getAllAssets().get(
                        mDataListener.getPageIntent().getIntExtra(KEY_MY_ASSETS_LIST_POSITION, 0));
                mTransaction = mAssetBalance.issueTransaction;*/
                mTransaction = (LeaseTransaction) mTransactionListDataManager.getTransactionList().get(
                        mDataListener.getPageIntent().getIntExtra(KEY_MY_ASSETS_LIST_POSITION, 0));
            }
            actionsEnabled = mDataListener.getPageIntent().getBooleanExtra(KEY_INTENT_ACTIONS_ENABLED, false);
        } else {
            mDataListener.pageFinish();
        }
    }

    /*@Bindable
    public String getAssetName() {
        return mTransaction.getAssetName();
    }

    @Bindable
    public String getQuantity() {
        return MoneyUtil.getScaledText(mAssetBalance != null ? mAssetBalance.quantity : mTransaction.quantity,
                mTransaction.getDecimals());
    }

    @Bindable
    public String getIdentifier() {
        return mTransaction.id;
    }

    public boolean isAssetReissuable() {
        return mAssetBalance != null ? mAssetBalance.reissuable : mTransaction.reissuable;
    }

    /*@Bindable
    public String getReissuable() {
        return  isAssetReissuable() ? "Yes" : "No";
    }

    @Bindable
    public String getTransactionFee() {
        return mStringUtils.getString(R.string.transaction_detail_fee) +
                MoneyUtil.getWavesStripZeros(mTransaction.fee) + " TN";
    }*/

    @Bindable
    public String getNode() {
        return mTransaction.recipient;
    }

    @Bindable
    public String getConfirmationStatus() {
        if (mTransaction.isPending) return mStringUtils.getString(R.string.transaction_detail_pending);
        else return mStringUtils.getString(R.string.transaction_detail_confirmed);
    }

    @Bindable
    public String getTransactionDate() {
        Date date = new Date(mTransaction.timestamp);
        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.LONG);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
        String dateText = dateFormat.format(date);
        String timeText = timeFormat.format(date);

        return dateText + " @ " + timeText;
    }

    @Bindable
    public String getAmount() {
        return "" + (mTransaction.amount / Math.pow(10, 8));
    }

    /*@Bindable
    public String getIssuer() {
        return  mTransaction.sender;
    }

    @Bindable
    public String getIssuerLabel() {
        return null;
    }

    @Bindable
    public String getDescription() {
        return mTransaction.description;
    }*/

    /*public boolean isActionsEnabled() {
        return actionsEnabled && isAssetReissuable() ;
    }*/

    /*public int getDecimals() {
        return mTransaction.decimals;
    }*/

    public long getTotalQuantity() {
        return mAssetBalance != null ? mAssetBalance.quantity : -1;
    }
}
