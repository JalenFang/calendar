package com.jalen.calendar.util;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
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
    private static final Uri CALENDAR_REMIDER_URI = CalendarContract.Reminders.CONTENT_URI;

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
                        Log.d("jalen", "id: " + id + " name: " + name + " accountName: " + accountName + " accountType: " + accountType//
                                + " ownerAccount: " + ownerAccount + " displayName: " + displayName + " timeZone: " + timeZone + " deleted: " + deleted);
                    }

                    if (!calendarCursor.isClosed()) {
                        calendarCursor.close();
                    }
                }

                Uri deleteUri = ContentUris.withAppendedId(CALENDAR_URI, 2);
                context.getContentResolver().delete(deleteUri, null, null);
            }
        }
    }

    //删除本地日历
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

    public void addLocalCalendar(Context context) {
        if (context != null) {
            String CALENDARS_NAME = "test";
            String CALENDARS_ACCOUNT_NAME = "test@gmail.com";
            String CALENDARS_ACCOUNT_TYPE = CalendarContract.ACCOUNT_TYPE_LOCAL;
            String CALENDARS_DISPLAY_NAME = "测试账户";

            TimeZone timeZone = TimeZone.getDefault();
            ContentValues value = new ContentValues();
            value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);

            value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
            value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
            value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
            value.put(CalendarContract.Calendars.VISIBLE, 1);
            value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
            value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
            value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
            value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
            value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
            value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

            Uri calendarUri = CALENDAR_URI;
            calendarUri = calendarUri.buildUpon()
                    .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                    .build();

            Uri result = context.getContentResolver().insert(calendarUri, value);
            long id = result == null ? -1 : ContentUris.parseId(result);
            Log.e("jalen", "添加本地日历的id：" + id);
        }
    }

}
