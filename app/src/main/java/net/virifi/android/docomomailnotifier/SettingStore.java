package net.virifi.android.docomomailnotifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingStore {
    private static final String PREF_ADDRESS_KEY = "address";
    private static final String PREF_NOTIFY_WHEN_RECEIVE_INTENT = "notify_when_receive_intent";

    private Context mContext;

    public SettingStore(Context context) {
        mContext = context;
    }

    public String loadAddress() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(PREF_ADDRESS_KEY, null);
    }

    public void storeAddress(String address) {
        if (address == null) {
            throw new IllegalArgumentException("address is null");
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_ADDRESS_KEY, address);
        editor.apply();
    }

    public boolean loadNotifiyWhenReceiveIntent() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getBoolean(PREF_NOTIFY_WHEN_RECEIVE_INTENT, false);
    }

    public void storeNotifiyWhenReceiveIntent(boolean notify) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_NOTIFY_WHEN_RECEIVE_INTENT, notify);
        editor.apply();
    }
}
