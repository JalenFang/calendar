package com.jalen.calendar.bean;

/**
 * Author: Jalen
 * Email: braveJalen@163.com
 * Desc: 本地日历
 */
public class LocalCalendarBean {
    /**
     * 日历id
     */
    public int id;

    /**
     * 日历名字
     */
    public String name;

    /**
     * 账户名字
     */
    public String accountName;

    /**
     * 账户类型
     */
    public String accountType;

    /**
     * 拥有者账号
     */
    public String ownerAccount;

    /**
     * 显示名字
     */
    public String displayName;

    /**
     * 0 不可见
     * 1 可见
     */
    public int visible;

    /**
     * 日程颜色
     */
    public int color;

    /**
     * 管理日程的权限
     */
    public int accessLevel;

    /**
     * 0 不同步该日历以及日历事件
     * 1 同步
     */
    public int syncEvents;

    /**
     * 日历的时区
     */
    public String timeZone;
}
