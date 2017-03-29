package com.a91coding.delayedTask.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.a91coding.delayedTask.util.FileLogWriter;
import com.a91coding.delayedTask.util.TimeHelper;

import java.util.Arrays;
import java.util.Calendar;
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
        int period = 1 * 60 * 1000; // 这是30分钟的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + period;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        FileLogWriter.testLog("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public void switchWifi(boolean enabled) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enabled);
        if (enabled) {
            Log.e("switchWifi", "enabled");
            FileLogWriter.testLog("switchWifi:enabled 开启");
        } else {
            Log.e("switchWifi", "disabled");
            FileLogWriter.testLog("switchWifi:disabled  关闭");
        }
    }

    public String getStartTime() {
        return "08:20:00";
    }

    public String getEndTime() {
        return "22:00:00";
    }

    public boolean needEnableWIFI() {
        boolean enabled = false;
        String dayOfWeek = String.valueOf(dayOfWeek());
        String[] cycle = getCycle();
        if (verifyCycle(dayOfWeek, cycle)) { //日期符合条件
            // 8:10 ~22:00 开启WIFI
            String pattern = "HH:mm:ss";
            String currentTime = TimeHelper.getCurrentTime(pattern);
            String firstTime = "08:20:00";
            String secondTime = "22:00:00";
            int timeCompare1 = TimeHelper.timeCompare(currentTime, firstTime, pattern);
            int timeCompare2 = TimeHelper.timeCompare(currentTime, secondTime, pattern);
            Log.e("currentTime", currentTime);
            Log.e("timeCompare1", String.valueOf(timeCompare1));
            Log.e("timeCompare2", String.valueOf(timeCompare2));
            FileLogWriter.testLog("timeCompare1:" + String.valueOf(timeCompare1));
            if (timeCompare1 >= 0 && timeCompare2 < 0) {
                enabled = true;
            }
            FileLogWriter.testLog("enabled:" + String.valueOf(enabled));
        } else {
            FileLogWriter.testLog("cycle invalid current dayOfWeek:" + dayOfWeek);
        }
        return enabled;
    }

    /**
     *
     * @param dayOfWeek
     * @param cycle
     * @return
     */
    public boolean verifyCycle(String dayOfWeek, String[] cycle) {
        return Arrays.asList(cycle).contains(dayOfWeek);
    }

    /**
     * 一周第几天
     * 返回值：1=Sunday,2=Monday,,,7=Saturday。
     * @return
     */
    public long dayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        long whichDay = calendar.get(Calendar.DAY_OF_WEEK);
        return whichDay;
    }

    /**
     * todo 这个需要设置
     * @return
     */
    public String[] getCycle() {
        String[] cycle = {"2","3","4","5","6"};// 1=Sunday,2=Monday,,,7=Saturday。
        return cycle;
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
