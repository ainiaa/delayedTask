package com.a91coding.delayedtask.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by Administrator on 2017/03/22.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, LongRunningService.class);
        context.startService(i);
        if (intent == null) {
            Log.e("intent null", "ssssssssssssssssss");
        } else {
            switchWifi(context, intent);
        }
    }

    public void switchWifi(Context context, Intent intent) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            Log.e("disable wifi", "222222222222222");
        } else {
            wifiManager.setWifiEnabled(true);
            Log.e("enable wifi", "111111111111111111");
        }
    }
}
