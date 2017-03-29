package com.a91coding.delayedTask.model;

public class TaskData {
    private String startTime;
    private String endTime;
    private int cycleType = 2; //0:一次 1：每天 2:周一~周五 3:法定工作日 4:自定义（列表）
    private String[] customerCycle;
    private int actionType;//0：开关WIFI 1：移动流量-WIFI切换 2:闹钟
    private boolean enabled = true;// false：关闭状态， true:开启状态

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getCycleType() {
        return cycleType;
    }

    public void setCycleType(int cycleType) {
        this.cycleType = cycleType;
    }

    public String[] getCustomerCycle() {
        return customerCycle;
    }

    public void setCustomerCycle(String[] customerCycle) {
        this.customerCycle = customerCycle;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
}
