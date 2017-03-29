package com.a91coding.delayedTask.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/03/22.
 */

public class AppInfo {
    private int versionCode = 0; //名称
    private String appname = ""; //包
    private String packagename = "";
    private String versionName = ""; //图标
    private Drawable appicon = null;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Drawable getAppicon() {
        return appicon;
    }

    public void setAppicon(Drawable appicon) {
        this.appicon = appicon;
    }

    public String toString() {
        return "appname:" + appname + "   packagename:" + packagename + " versionName:" + versionName + " appicon:" + appicon;
    }
}
