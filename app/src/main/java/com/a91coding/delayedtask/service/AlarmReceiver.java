package com.a91coding.delayedTask.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.a91coding.delayedTask.util.FileLogWriter;

/**
 * Created by Administrator on 2017/03/22.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        FileLogWriter.testLog("AlarmReceiver.onReceive");
        Intent i = new Intent(context, LongRunningService.class);
        context.startService(i);
    }
}
