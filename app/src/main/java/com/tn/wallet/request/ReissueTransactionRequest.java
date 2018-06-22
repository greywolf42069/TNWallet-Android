package com.tn.wallet.request;

import android.util.Log;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;
import com.tn.wallet.crypto.Base58;
import com.tn.wallet.crypto.CryptoProvider;
import com.tn.wallet.crypto.Hash;
import com.tn.wallet.payload.ReissueTransaction;
import com.tn.wallet.util.AddressUtil;

public class ReissueTransactionRequest {
    public static int MinFee = 1000000000;

    final public String assetId;
    final public String senderPublicKey;
    final public long quantity;
    final public boolean reissuable;
    final public long fee;
    final public long timestamp;
    public String signature;

    public transient final int txType = 5;

    public ReissueTransactionRequest(String assetId, String senderPublicKey, long quantity,
                                     boolean reissuable, long timestamp) {
        this.assetId = assetId;
        this.senderPublicKey = senderPublicKey;
        this.quantity = quantity;
        this.reissuable = reissuable;
        this.fee = MinFee;
        this.timestamp = timestamp;
    }

    public static byte[] arrayWithSize(byte[] b) {
        return Bytes.concat(Shorts.toByteArray((short) b.length), b);
    }

    public byte[] toSignBytes() {
        try {
            byte[] reissuableBytes = reissuable ? new byte[]{1} : new byte[]{0};

            return Bytes.concat(new byte[] {txType},
                    Base58.decode(senderPublicKey),
                    Base58.decode(assetId),
                    Longs.toByteArray(quantity),
                    reissuableBytes,
                    Longs.toByteArray(fee),
                    Longs.toByteArray(timestamp));
        } catch (Exception e) {
            Log.e("ReissueRequest", "Couldn't create toSignBytes", e);
            return new byte[0];
        }
    }

    public void sign(byte[] privateKey)  {
        if (signature == null) {
            signature = Base58.encode(CryptoProvider.sign(privateKey, toSignBytes()));
        }
    }

    public ReissueTransaction createDisplayTransaction() {
        ReissueTransaction tx = new ReissueTransaction(3, Base58.encode(Hash.fastHash(toSignBytes())),
                AddressUtil.addressFromPublicKey(senderPublicKey), assetId, timestamp, quantity, fee,
                quantity, reissuable);
        tx.isPending = true;
        return tx;
    }
}
