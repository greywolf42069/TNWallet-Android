package com.tn.wallet.ui.dex.watchlist_markets;

import android.content.Context;
import android.support.annotation.StringRes;

import com.tn.wallet.api.datafeed.DataFeedManager;
import com.tn.wallet.data.rxjava.RxUtil;
import com.tn.wallet.db.DBHelper;
import com.tn.wallet.injection.Injector;
import com.tn.wallet.payload.AmountAssetInfo;
import com.tn.wallet.payload.Market;
import com.tn.wallet.payload.PriceAssetInfo;
import com.tn.wallet.payload.TickerMarket;
import com.tn.wallet.payload.TradesMarket;
import com.tn.wallet.payload.WatchMarket;
import com.tn.wallet.ui.base.BaseViewModel;
import com.tn.wallet.ui.customviews.ToastCustom;
import com.tn.wallet.util.SSLVerifyUtil;
import com.tn.wallet.util.annotations.Thunk;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmResults;

@SuppressWarnings("WeakerAccess")
public class WatchlistMarketsViewModel extends BaseViewModel {

    @Thunk
    DataListener dataListener;
    private Context context;
    public final String defaultAmount = "TN";
    public final String defaultPrice = "5Asy9P3xjcvBAgbeyiitZhBRJZJ2TPGSZJz9ihDTnB3d";
    private Market defaultMarket;

    @Inject
    SSLVerifyUtil sslVerifyUtil;

    WatchlistMarketsViewModel(Context context, DataListener dataListener) {

        Injector.getInstance().getDataManagerComponent().inject(this);

        this.context = context;
        this.dataListener = dataListener;

        defaultMarket = new Market(defaultAmount, defaultAmount, defaultPrice, "BTC", new AmountAssetInfo(8), new PriceAssetInfo(8));

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

    public interface DataListener {
        void onShowToast(@StringRes int message, @ToastCustom.ToastType String toastType);

        void successfullyGetTickerByPair(int position, TickerMarket markets);

        void successfullyGetTradesByPair(int position, TradesMarket markets);

        void finishPage();
    }

    public void getTickerByPair(int position, WatchMarket watchMarket) {
        compositeDisposable.add(DataFeedManager.get().getTickerByPair(watchMarket.market.amountAsset, watchMarket.market.priceAsset)
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe(tickerMarket -> {
                    if (dataListener != null)
                        dataListener.successfullyGetTickerByPair(position, tickerMarket);
                }, Throwable::printStackTrace));

    }

    public void getTradesByPair(int position, WatchMarket watchMarket) {
        compositeDisposable.add(DataFeedManager.get().getTradesByPair(watchMarket.market.amountAsset, watchMarket.market.priceAsset)
                .subscribeOn(Schedulers.from(Executors.newSingleThreadExecutor()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tradesMarket -> {
                    if (dataListener != null)
                        dataListener.successfullyGetTradesByPair(position, tradesMarket);
                }, throwable -> {
                    if (dataListener != null)
                        dataListener.successfullyGetTradesByPair(position, new TradesMarket());
                }));

    }

    public RealmResults<Market> getCurrentWatchlistMarkets() {
        RealmResults<Market> all = DBHelper.getInstance().getRealm().where(Market.class)
                .equalTo("amountAsset", defaultAmount)
                .equalTo("priceAsset", defaultPrice)
                .findAll();
        if (all.isEmpty()) DBHelper.getInstance().getRealm().executeTransaction(realm -> realm.copyToRealm(defaultMarket));
        return DBHelper.getInstance().getRealm().where(Market.class).findAll();
    }

    public void deleteMarketFromWathclist(Market market) {
        DBHelper.getInstance().getRealm().executeTransaction(realm -> {
            findMarket(market).deleteFromRealm();
        });
    }

    public Market findMarket(Market market) {
        return DBHelper.getInstance().getRealm().where(Market.class)
                .equalTo("id", market.id)
                .findFirst();
    }

    public void updateCurrentWatchlistMarkets(ArrayList<Market> list) {
        DBHelper.getInstance().getRealm().executeTransaction(realm -> {
            for (Market market : list) {
                market.id = market.amountAsset + market.priceAsset;
                if (realm.where(Market.class).equalTo("id", market.id).count() == 0) {
                    realm.copyToRealm(market);
                }
            }
        });
    }


}
