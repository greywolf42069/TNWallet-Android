package com.tn.wallet.ui.leasing;

import com.tn.wallet.payload.LeaseTransaction;

import java.util.List;

/**
 * Created by marcjansen on 04.07.18.
 */

public interface ActiveLeaseListener {

    public void activeLeasesReceived(LeaseTransaction[] leaseTransactions);

}
