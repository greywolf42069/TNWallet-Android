package com.tn.wallet.request;

import android.util.Log;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;
import com.tn.wallet.crypto.Base58;
import com.tn.wallet.crypto.CryptoProvider;
import com.tn.wallet.crypto.Hash;
import com.tn.wallet.payload.LeaseCancelTransaction;
import com.tn.wallet.payload.LeaseTransaction;
import com.tn.wallet.util.AddressUtil;

public class LeaseCancelTransactionRequest {
    public static int SignatureLength = 64;
    public static int KeyLength = 32;
    public static int MinFee = 2000000;

    public String senderPublicKey;
    //public String recipient;
    //public long amount
    public String leaseId;
    public long timestamp;
    public long fee;
    public String signature;

    public transient final int txType = 9;

    public LeaseCancelTransactionRequest() {
    }

    public LeaseCancelTransactionRequest(String senderPublicKey, String leaseId,
                                         long timestamp, long fee) {
        this.senderPublicKey = senderPublicKey;
        //this.recipient = recipient;
        //this.amount = amount;
        this.leaseId = leaseId;
        this.timestamp = timestamp;
        this.fee = fee;
    }

    public byte[] toSignBytes() {
        try {
            byte[] timestampBytes  = Longs.toByteArray(timestamp);
            //byte[] amountBytes     = Longs.toByteArray(amount);
            byte[] feeBytes        = Longs.toByteArray(fee);

            return Bytes.concat(new byte[] {txType},
                    Base58.decode(senderPublicKey),
                    feeBytes,
                    timestampBytes,
                    Base58.decode(leaseId));
        } catch (Exception e) {
            Log.e("Wallet", "Couldn't create signature", e);
            return new byte[0];
        }
    }

    public void sign(byte[] privateKey)  {
        if (signature == null) {
            signature = Base58.encode(CryptoProvider.sign(privateKey, toSignBytes()));
        }
    }

    public LeaseCancelTransaction createDisplayTransaction() {
        LeaseCancelTransaction tt = new LeaseCancelTransaction(9, Base58.encode(Hash.fastHash(toSignBytes())),
                AddressUtil.addressFromPublicKey(senderPublicKey), timestamp, fee,
                leaseId);
        tt.isPending = true;
        return tt;
    }


}
