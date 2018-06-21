package com.tn.wallet.ui.auth;

import android.app.Activity;
import android.content.Intent;

import com.tn.wallet.api.NodeManager;
import com.tn.wallet.api.datafeed.DataFeedManager;
import com.tn.wallet.api.mather.MatherManager;
import com.tn.wallet.db.DBHelper;
import com.tn.wallet.ui.home.MainActivity;

import io.realm.RealmConfiguration;

public class AuthUtil {
    public static boolean startMainActivity(Activity parent, String publicKey) {
        if (NodeManager.createInstance(publicKey) != null) {
            DataFeedManager.createInstance();
            MatherManager.createInstance(publicKey);

            RealmConfiguration config = new RealmConfiguration.Builder()
                    .name(String.format("%s.realm", publicKey))
                    .deleteRealmIfMigrationNeeded()
                    .build();
            DBHelper.getInstance().setRealmConfig(config);

            Intent intent = new Intent(parent, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            parent.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }
}
