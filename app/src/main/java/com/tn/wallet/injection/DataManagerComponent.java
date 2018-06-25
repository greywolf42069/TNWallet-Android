package com.tn.wallet.injection;

import com.tn.wallet.data.datamanagers.AddressBookManager;
import com.tn.wallet.ui.assets.IssueViewModel;
import com.tn.wallet.ui.auth.LandingViewModel;
import com.tn.wallet.ui.auth.PinEntryViewModel;
import com.tn.wallet.ui.balance.TransactionsViewModel;
import com.tn.wallet.ui.dex.details.DexDetailsViewModel;
import com.tn.wallet.ui.dex.details.chart.ChartViewModel;
import com.tn.wallet.ui.dex.details.orderbook.OrderBookViewModel;
import com.tn.wallet.ui.dex.details.last_trades.LastTradesViewModel;
import com.tn.wallet.ui.dex.details.my_orders.MyOrdersViewModel;
import com.tn.wallet.ui.dex.markets.MarketsViewModel;
import com.tn.wallet.ui.dex.markets.add.AddMarketViewModel;
import com.tn.wallet.ui.dex.watchlist_markets.WatchlistMarketsViewModel;
import com.tn.wallet.ui.fingerprint.FingerprintDialogViewModel;
import com.tn.wallet.ui.home.MainViewModel;
import com.tn.wallet.ui.launcher.LauncherViewModel;
import com.tn.wallet.ui.leasing.LeaseViewModel;
import com.tn.wallet.ui.pairing.PairingViewModel;
import com.tn.wallet.ui.dex.details.order.PlaceOrderViewModel;
import com.tn.wallet.ui.receive.ReceiveViewModel;
import com.tn.wallet.ui.send.SendViewModel;
import com.tn.wallet.ui.transactions.ExchangeTransactionDetailViewModel;
import com.tn.wallet.ui.transactions.IssueDetailViewModel;
import com.tn.wallet.ui.transactions.ReissueDetailViewModel;
import com.tn.wallet.ui.transactions.TransactionDetailViewModel;
import com.tn.wallet.ui.transactions.UnknownDetailViewModel;

import dagger.Subcomponent;

/**
 * Subcomponents have access to all upstream objects in the graph but can have their own scope -
 * they don't need to explicitly state their dependencies as they have access anyway
 */
@SuppressWarnings("WeakerAccess")
@ViewModelScope
@Subcomponent(modules = DataManagerModule.class)
public interface DataManagerComponent {

    void inject(LauncherViewModel launcherViewModel);

    void inject(AddMarketViewModel addMarketViewModel);

    void inject(OrderBookViewModel orderBookViewModel);

    void inject(WatchlistMarketsViewModel watchlistMarketsViewModel);

    void inject(MarketsViewModel marketsViewModel);

    void inject(SendViewModel sendViewModel);

    void inject(LeaseViewModel leaseViewModel);

    void inject(ChartViewModel chartViewModel);

    void inject(PinEntryViewModel pinEntryViewModel);

    void inject(MainViewModel mainViewModel);

    void inject(TransactionsViewModel transactionsViewModel);

    void inject(PairingViewModel pairingViewModel);

    void inject(ReceiveViewModel receiveViewModel);

    void inject(TransactionDetailViewModel transactionDetailViewModel);

    void inject(FingerprintDialogViewModel fingerprintDialogViewModel);

    void inject(LandingViewModel landingViewModel);

    void inject(AddressBookManager addressBookManager);

    void inject(IssueDetailViewModel issueDetailViewModel);

    void inject(IssueViewModel issueViewModel);

    void inject(ReissueDetailViewModel reissueDetailViewModel);

    void inject(ExchangeTransactionDetailViewModel exchangeTransactionDetailViewModel);

    void inject(UnknownDetailViewModel unknownDetailViewModel);

    void inject(PlaceOrderViewModel placeOrderViewModel);

    void inject(DexDetailsViewModel dexDetailsViewModel);

    void inject(LastTradesViewModel lastTradesViewModel);

    void inject(MyOrdersViewModel myOrdersViewModel);
}
