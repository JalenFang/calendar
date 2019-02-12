package com.jalen.calendar.util;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.TimeZone;

/**
 * Author: Jalen
 * Email: braveJalen@163.com
 */
public class LocalCalendarUtil {
    private static LocalCalendarUtil util;

    private static final Uri CALENDAR_URI = CalendarContract.Calendars.CONTENT_URI;
    private static final Uri CALENDAR_EVENT_URI = CalendarContract.Events.CONTENT_URI;
    private static final Uri CALENDAR_REMINDER_URI = CalendarContract.Reminders.CONTENT_URI;

    private LocalCalendarUtil() {

    }

    public static LocalCalendarUtil getInstance() {
        if (util == null) {
            synchronized (LocalCalendarUtil.class) {
                util = new LocalCalendarUtil();
            }
        }

        return util;
    }

    //检查是否有日历账户
    public boolean judgeHaveCalendarAccount(Context context) {
        if (context != null) {
            Cursor calendarCursor = context.getContentResolver().query(CALENDAR_URI, null, null, null, null);
            try {
                if (calendarCursor == null) {
                    return false;
                }

                int count = calendarCursor.getCount();
                if (count > 0) {
                    return true;
                }
            } finally {
                if (calendarCursor != null) {
                    calendarCursor.close();
                }
            }
        }
        return false;
    }

    //获取本地日历账户
    public void getLocalCalendar(Context context) {
        if (context != null) {
            if (judgeHaveCalendarAccount(context)) {
                Cursor calendarCursor = context.getContentResolver().query(CALENDAR_URI, null, null, null, null);
                if (calendarCursor != null) {
                    while (calendarCursor.moveToNext()) {
                        int id = calendarCursor.getInt(calendarCursor.getColumnIndex(CalendarContract.Calendars._ID));
                        String name = calendarCursor.getString(calendarCursor.getColumnIndex(CalendarContract.Calendars.NAME));
                        String accountName = calendarCursor.getString(calendarCursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME));
                        String accountType = calendarCursor.getString(calendarCursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE));
                        String ownerAccount = calendarCursor.getString(calendarCursor.getColumnIndex(CalendarContract.Calendars.OWNER_ACCOUNT));
                        String displayName = calendarCursor.getString(calendarCursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME));
                        String timeZone = calendarCursor.getString(calendarCursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_TIME_ZONE));
                        String deleted = calendarCursor.getString(calendarCursor.getColumnIndex(CalendarContract.Calendars.DELETED));
                        int visible = calendarCursor.getInt(calendarCursor.getColumnIndex(CalendarContract.Calendars.VISIBLE));
                        int syncEvents = calendarCursor.getInt(calendarCursor.getColumnIndex(CalendarContract.Calendars.SYNC_EVENTS));
                        int canOrganizerRespond = calendarCursor.getInt(calendarCursor.getColumnIndex(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND));
                        Log.d("jalen", "id: " + id + " name: " + name + " accountName: " + accountName + " accountType: " + accountType//
                                + " ownerAccount: " + ownerAccount + " displayName: " + displayName + " timeZone: " + timeZone //
                                + " visible: " + visible + " deleted: " + deleted + " canOrganizerRespond: " + canOrganizerRespond//
                                + " syncEvents: " + syncEvents);
                    }

                    if (!calendarCursor.isClosed()) {
                        calendarCursor.close();
                    }
                }
            }
        }
    }

    //删除本地日历账户
    public void deleteLocalCalendar(Context context, int id) {
        if (context != null && id > 0) {
            Uri deleteUri = ContentUris.withAppendedId(CALENDAR_URI, id);
            int delete = context.getContentResolver().delete(deleteUri, null, null);
            if (delete == -1) {
                Log.e("jalen", "删除本地日历失败");
            } else {
                Log.e("jalen", "删除本地日历成功");
            }
        }
    }

    //添加本地日历账户
    public void addLocalCalendar(Context context) {
        if (context != null) {
            String name = "jalen";
            String accountName = "jalen";
            String accountType = CalendarContract.ACCOUNT_TYPE_LOCAL;
            String displayName = "jalen";

            TimeZone timeZone = TimeZone.getDefault();
            ContentValues value = new ContentValues();
            value.put(CalendarContract.Calendars.NAME, name);

            value.put(CalendarContract.Calendars.ACCOUNT_NAME, accountName);
            value.put(CalendarContract.Calendars.ACCOUNT_TYPE, accountType);
            value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, displayName);
            value.put(CalendarContract.Calendars.VISIBLE, 1);
            value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
            value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
            value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
            value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
            value.put(CalendarContract.Calendars.OWNER_ACCOUNT, accountName);
            value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

            Uri calendarUri = CALENDAR_URI;
            calendarUri = calendarUri.buildUpon()
                    .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType)
                    .build();

            Uri result = context.getContentResolver().insert(calendarUri, value);
            long id = result == null ? -1 : ContentUris.parseId(result);
            Log.e("jalen", "添加本地日历的id：" + id);
        }
    }

    //添加日程事件
    public void addCalendarEvent(Context context) {
        if (context != null) {
            String calID = "1";

            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();

            //开始时间和结束时间设置一样
//            values.put(CalendarContract.Events.DTSTART, startMillis);
//            values.put(CalendarContract.Events.DTEND, startMillis);

            values.put(CalendarContract.Events.TITLE, "【全民减价】提醒您：今天别忘记签到哦");
            values.put(CalendarContract.Events.DESCRIPTION, "");
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            values.put(CalendarContract.Events.HAS_ALARM, 1);//设置有闹钟提醒
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                Uri addCalendarUri = cr.insert(CALENDAR_EVENT_URI, values);

                if (addCalendarUri == null) {
                    Log.e("jalen", "添加本地日历的id：");
                    return;
                }

                //事件提醒的设定
                ContentValues remindersContentValues = new ContentValues();
                remindersContentValues.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(addCalendarUri));
                // 提前0分钟提醒
                remindersContentValues.put(CalendarContract.Reminders.MINUTES, 0);
                remindersContentValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                Uri uri = context.getContentResolver().insert(CALENDAR_REMINDER_URI, remindersContentValues);
                if (uri == null) {
                    Log.e("jalen", "添加日程提醒失败");
                }
            }
        }
    }


}
