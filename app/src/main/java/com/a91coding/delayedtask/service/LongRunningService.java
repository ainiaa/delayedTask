package com.a91coding.delayedtask.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.a91coding.delayedtask.util.TimeHelper;

import java.util.Date;

public class LongRunningService extends Service {
    public LongRunningService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new MyRunnable(this)).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int period = 30 * 60 * 1000; // 这是30分钟的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + period;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    public void switchWifi(boolean enabled) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enabled);
        if (enabled) {
            Log.e("switchWifi", "enabled");
        } else {
            Log.e("switchWifi", "disabled");
        }
    }

    public boolean needEnableWIFI() {
        boolean enabled = false;
        // 8:10 ~22:00 开启WIFI
        String pattern = "HH:mm:ss";
        String currentTime = TimeHelper.getCurrentTime(pattern);
        String firstTime = "08:20:00";
        String secondTime = "22:00:00";
        Log.e("currentTime", currentTime);
        Log.e("timeCompare1", String.valueOf(TimeHelper.timeCompare(currentTime, firstTime, pattern)));
        Log.e("timeCompare2", String.valueOf(TimeHelper.timeCompare(currentTime, secondTime, pattern)));
        if (TimeHelper.timeCompare(currentTime, firstTime, pattern) >= 0 && TimeHelper.timeCompare(currentTime, secondTime, pattern) < 0) {
            enabled = true;
        }
        return enabled;
    }
}

class MyRunnable implements Runnable {

    private LongRunningService longRunningService;
    public MyRunnable(LongRunningService longRunningService) {
        this.longRunningService = longRunningService;
    }

    @Override
    public void run() {
        boolean needEnable = longRunningService.needEnableWIFI();
        longRunningService.switchWifi(needEnable);
    }
}
