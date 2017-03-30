package com.a91coding.delayedTask.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.a91coding.delayedTask.util.FileLogWriter;
import com.a91coding.delayedTask.util.TaskRunnable;

public class LongRunningService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);
        return START_REDELIVER_INTENT;
    }

    /**
     *
     * @param intent
     */
    public void handleCommand(Intent intent) {
        new Thread(new TaskRunnable(this)).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int period = 2 * 60 * 1000; // 这是30分钟的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + period;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        FileLogWriter.testLog("LongRunningService.handleCommand");
    }

    @Override
    public void onDestroy() {
        FileLogWriter.testLog("LongRunningService.onDestroy");
        Intent localIntent = new Intent();
        localIntent.setClass(this, LongRunningService.class);  //销毁时重新启动Service
        this.startService(localIntent);
    }
}
