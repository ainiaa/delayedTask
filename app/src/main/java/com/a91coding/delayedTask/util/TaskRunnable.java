package com.a91coding.delayedTask.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.a91coding.delayedTask.model.TaskData;
import com.a91coding.delayedTask.service.LongRunningService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/03/30.
 */

public class TaskRunnable implements Runnable {
    public static final int MONDAY_INDEX_OF_WEEK = 2;
    public static final int FRIDAY_INDEX_OF_WEEK = 6;
    private LongRunningService longRunningService;

    /**
     * todo 数据可以从配置好的地方进行获取
     * @return
     */
    private List<TaskData> getTaskList() {
        TaskData taskData = new TaskData();
        taskData.setActionType(0);//开关WIFI
        taskData.setStartTime("08:30:00");
        taskData.setEndTime("22:00:00");
        taskData.setCycleType(2); //周一~周五
        List<TaskData> taskDataList = new ArrayList<>();
        taskDataList.add(taskData);
        return taskDataList;
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

    public void switchWIFI(boolean enabled) {
        WifiManager wifiManager = (WifiManager) longRunningService.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean wifiState = wifiManager.isWifiEnabled();
        if (wifiState) {
            if (enabled) {
                Log.e("switchWIFI", " 当前WIFI已开启，无需重复开启");
                FileLogWriter.testLog("switchWIFI: 当前WIFI已开启，无需重复开启");
            } else {
                FileLogWriter.testLog("switchWIFI: 当前WIFI已开启，关闭WIFI...");
            }
        } else {
            if (enabled) {
                Log.e("switchWIFI", "当前WIFI已关闭，开启WIFI...");
                FileLogWriter.testLog("switchWIFI:当前WIFI已关闭，开启WIFI...");
            } else {
                FileLogWriter.testLog("switchWIFI: 当前WIFI已关闭，无需重复关闭");
            }
        }
        if (!String.valueOf(wifiState).equals(String.valueOf(enabled))) {//当前状态不一致，修改之
            wifiManager.setWifiEnabled(enabled);
        }
    }

    public boolean verifyCycle(TaskData taskData) {
        int cycleType = taskData.getCycleType();
        boolean verified = false;
        long dayOfWeek;
        switch (cycleType) {
            case 0://0:一次 1：每天 2:周一~周五 3:法定工作日 4:自定义（列表）
                verified = true;
                break;
            case 1://每天
                verified = true;
                break;
            case 2://周一~周五
                dayOfWeek = dayOfWeek();
                if (dayOfWeek >= MONDAY_INDEX_OF_WEEK && dayOfWeek <= FRIDAY_INDEX_OF_WEEK) {
                    verified = true;
                }
                break;
            case 3://法定工作日  todo 还没有实现
                break;
            case 4://自定义（列表）一周7天自己选择
                dayOfWeek = dayOfWeek();
                String[] customCycle = taskData.getCustomerCycle();
                verified = Arrays.asList(customCycle).contains(String.valueOf(dayOfWeek));
                break;
            default:
                //其他类型
        }
        return verified;
    }

    /**
     *
     * @param taskData
     * @return
     */
    public boolean needEnableWIFI(TaskData taskData) {
        boolean enabled = false;
        boolean cycleVerified = verifyCycle(taskData);
        String dayOfWeek = String.valueOf(dayOfWeek());
        if (cycleVerified) { //日期符合条件
            // 8:10 ~22:00 开启WIFI
            String pattern = "HH:mm:ss";
            String currentTime = TimeHelper.getCurrentTime(pattern);
            String firstTime = taskData.getStartTime();
            String secondTime = taskData.getEndTime();
            int startTimeCompare = TimeHelper.timeCompare(currentTime, firstTime, pattern);
            int endTimeCompare = TimeHelper.timeCompare(currentTime, secondTime, pattern);
            Log.e("currentTime", currentTime);
            Log.e("startTimeCompare", String.valueOf(startTimeCompare));
            Log.e("endTimeCompare", String.valueOf(endTimeCompare));
            FileLogWriter.testLog("startTimeCompare:" + String.valueOf(startTimeCompare));
            FileLogWriter.testLog("endTimeCompare:" + String.valueOf(endTimeCompare));
            if (startTimeCompare >= 0 && endTimeCompare < 0) {
                enabled = true;
            }
            FileLogWriter.testLog("enabled:" + String.valueOf(enabled));
        } else {
            FileLogWriter.testLog("cycle invalid current dayOfWeek:" + dayOfWeek);
        }
        return enabled;
    }

    public TaskRunnable(LongRunningService longRunningService) {
        this.longRunningService = longRunningService;
    }

    /**
     * 处理任务
     */
    public void processTaskList() {
        List<TaskData> taskDataList = getTaskList();
        int taskCount = taskDataList.size();
        if (taskCount > 0) {
            //遍历任务
            for(int i = 0; i < taskCount;i++) {
                TaskData taskData = taskDataList.get(i);
                boolean enabled = taskData.isEnabled();
                if (enabled) { //任务为开启状态，处理之
                    processSingleTask(taskData);
                }
            }
        } else { //没有任务
            FileLogWriter.testLog("no task need process");
        }
    }

    public void processSwitchWIFITask(TaskData taskData) {
        boolean needEnable = needEnableWIFI(taskData);
        switchWIFI(needEnable);
    }

    /**
     * todo 还没有实现
     *  移动流量-WIFI切换
     * @param taskData
     */
    public void processSwitchMobileAndWIFITask(TaskData taskData) {

    }

    /**
     * todo 还没有实现
     *  闹钟
     * @param taskData
     */
    public void processAlarmClockTask(TaskData taskData) {

    }

    public void processSingleTask(TaskData taskData) {
        switch (taskData.getActionType()) {
            case 0://开关WIFI
                processSwitchWIFITask(taskData);
                break;
            case 1://移动流量-WIFI切换
                processSwitchMobileAndWIFITask(taskData);
                break;
            case 2://闹钟
                processAlarmClockTask(taskData);
                break;
            default:
                FileLogWriter.testLog("current task actionType:" + taskData.getActionType());
        }
    }

    @Override
    public void run() {
        processTaskList();
    }
}
