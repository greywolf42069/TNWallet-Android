package com.tn.wallet.api;

import com.tn.wallet.payload.AssetBalances;
import com.tn.wallet.payload.Transaction;
import com.tn.wallet.payload.TransactionsInfo;
import com.tn.wallet.payload.WavesBalance;
import com.tn.wallet.request.IssueTransactionRequest;
import com.tn.wallet.request.LeaseTransactionRequest;
import com.tn.wallet.request.ReissueTransactionRequest;
import com.tn.wallet.request.TransferTransactionRequest;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NodeApi {
    @GET("/assets/balance/{address}")
    Observable<AssetBalances> assetsBalance(@Path("address") String address);

    @GET("/addresses/balance/{address}")
    Observable<WavesBalance> wavesBalance(@Path("address") String address);

    @GET("/transactions/address/{address}/limit/{limit}")
    Observable<List<List<Transaction>>> transactionList(@Path("address") String address, @Path("limit") int limit);

    @GET("transactions/info/{asset}")
    Observable<TransactionsInfo> getTransactionsInfo(@Path("asset") final String asset);

    @POST("/leasing/broadcast/lease")
    Observable<LeaseTransactionRequest> broadcastLease(@Body LeaseTransactionRequest tx);

    @POST("/assets/broadcast/transfer")
    Observable<TransferTransactionRequest> broadcastTransfer(@Body TransferTransactionRequest tx);

    @POST("/assets/broadcast/issue")
    Observable<IssueTransactionRequest> broadcastIssue(@Body IssueTransactionRequest tx);

    @POST("/assets/broadcast/reissue")
    Observable<ReissueTransactionRequest> broadcastReissue(@Body ReissueTransactionRequest tx);

    @GET("/transactions/unconfirmed")
    Observable<List<Transaction>> unconfirmedTransactions();
}
