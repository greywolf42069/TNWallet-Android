package com.tn.wallet.data.stores;

import com.tn.wallet.payload.Transaction;
import com.tn.wallet.payload.TransactionMostRecentDateComparator;

import java.util.List;

public class TransactionListStore extends ListStore<Transaction> {

    public TransactionListStore() {
        // Empty constructor
    }

    public void insertTransactionIntoListAndSort(Transaction transaction) {
        insertObjectIntoList(transaction);
        sort(new TransactionMostRecentDateComparator());
    }

    public void insertTransactions(List<Transaction> transactions) {
        insertBulk(transactions);
    }
}
