/*
 * Copyright 2013 Kevin Quan (kevin.quan@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevinquan.android.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;

/**
 * Utilities to work with the Java Calendar class
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
@SuppressLint("SimpleDateFormat")
public class CalendarUtils {

    @SuppressWarnings("unused")
    private static final String TAG = CalendarUtils.class.getSimpleName();
    
    public static final SimpleDateFormat DEBUG_CALENDAR_OUTPUT_WITH_TIMEZONE = new SimpleDateFormat("MM/dd/yy HH:mm:ss.SSS Z");
    public static final SimpleDateFormat DEBUG_CALENDAR_OUTPUT = new SimpleDateFormat("MM/dd/yy HH:mm:ss.SSS");
    
    public static final long ONE_MINUTE = 60 * 1000;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    public static final long ONE_WEEK = 7 * ONE_DAY;

    /**
     * Returns the difference between two calendar objects in minutes.  A positive result means that the
     * actual time is later than the expected time
     * @param expectedTime The calendar object to compare against
     * @param actualTime The calendar object to compare
     * @return difference between the two calendar objects in minutes.  If the result is not on a minute boundary, the minute floor will be returned.
     */
    public static int calculateTimeDifferenceInMinutes(Calendar expectedTime, Calendar actualTime) {
        long differenceInMillis = actualTime.getTimeInMillis() - expectedTime.getTimeInMillis();
        return (int)(differenceInMillis/(1000*60));
    }
    
    /**
     * Compares only the month, day, and year of the provided calendars
     * @param date1 The calendar to compare against
     * @param date2 The calendar to compare
     * @return -1 if the first calendar is earlier, 1 if the second calendar is earlier, 0 otherwise
     */
    public static int compareDates(Calendar date1, Calendar date2) {
        if (date1.get(Calendar.YEAR) < date2.get(Calendar.YEAR)) {
            return -1;
        } else if (date1.get(Calendar.YEAR) > date2.get(Calendar.YEAR)) {
            return 1;
        }
        // Years are equal
        if (date1.get(Calendar.MONTH) < date2.get(Calendar.MONTH)) {
            return -1;
        } else if (date1.get(Calendar.MONTH) > date2.get(Calendar.MONTH)) {
            return 1;
        }
        // Years and months are equal
        if (date1.get(Calendar.DAY_OF_MONTH) < date2.get(Calendar.DAY_OF_MONTH)) {
            return -1;
        } else if (date1.get(Calendar.DAY_OF_MONTH) > date2.get(Calendar.DAY_OF_MONTH)) {
            return 1;
        }
        return 0;
    }
    
    /**
     * Compares (only) the time of two Calendar objects
     * @param firstTime The first calendar object
     * @param secondTime The second calendar object
     * @return -1 if the time of the first calendar object is earlier than the second calendar object,
     *          1 if the time of the second calendar object is earlier than the first calendar object,
     *          0 if the times are equal
     */
    public static int compareTime(Calendar firstTime, Calendar secondTime) {
        if (firstTime.get(Calendar.HOUR_OF_DAY) < secondTime.get(Calendar.HOUR_OF_DAY)) {
            return -1;
        } else if (firstTime.get(Calendar.HOUR_OF_DAY) > secondTime.get(Calendar.HOUR_OF_DAY)) {
            return 1;
        }
        if (firstTime.get(Calendar.MINUTE) < secondTime.get(Calendar.MINUTE)) {
            return -1;
        } else if (firstTime.get(Calendar.MINUTE) > secondTime.get(Calendar.MINUTE)) {
            return 1;
        }
        return 0;
    }
    
    /**
     * Takes a calendar object representing a date/time, ignores its current time zone (which should be the default time zone)
     * applies that date/time to the sourceTimeZone and returns the relative date/time in the current time zone. 
     * 
     * For example, given an input of 13:00 EST and source time zone PST, it will return 16:00 EST 
     * 13:00 EST = 18:00 GMT = 10:00 PST
     *  
     * @param calendar
     * @param sourceTimeZone
     * @return
     */
    public static Calendar convertToTimeZone(Calendar calendar, TimeZone sourceTimeZone) {
        Calendar result = Calendar.getInstance();
        // i.e., 13:00 EST becomes 08:00 GMT
        long originalTimeInUtc = calendar.getTimeInMillis()+calendar.getTimeZone().getOffset(calendar.getTimeInMillis());
        // 08:00 GMT becomes 16:00 PST
        long sourceTime = originalTimeInUtc-sourceTimeZone.getOffset(originalTimeInUtc);
        result.setTimeZone(sourceTimeZone);
        result.setTimeInMillis(sourceTime);
        /*
        Log.d(TAG, "Converting "+DEBUG_CALENDAR_OUTPUT.format(new Date(calendar.getTimeInMillis()))
                +" in ["+sourceTimeZone.getDisplayName()+"] to ["+TimeZone.getDefault().getDisplayName()
                +"] resulting in "+DEBUG_CALENDAR_OUTPUT_WITH_TIMEZONE.format(new Date(result.getTimeInMillis())));
        Log.d(TAG, "Original time in UTC = "+DEBUG_CALENDAR_OUTPUT.format(new Date(originalTimeInUtc)));
        Log.d(TAG, "Original time in source time zone = "+DEBUG_CALENDAR_OUTPUT.format(new Date(sourceTime)));
        */
        return result;
    }
    
    /**
     * Retrieve a calendar object set to the specified millisecond time
     * @param timeInMillis the time to set on the calendar
     * @return the calendar object with the specified time.
     */
    public static Calendar getCalendar(long timeInMillis) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timeInMillis);
        return date;
    }
    
    /**
     * Retrieves a printable minute value from the calendar to be used within a time.  For example, minute 3 will be
     * returned as 03 (so to display 10:03 instead of 10:3) 
     * @param calendar The calendar object whose minute value to retrieve
     * @return printable minute value from the calendar object
     */
    public static String getDisplayableMinute(Calendar calendar) {
        return calendar.get(Calendar.MINUTE) < 10 ? "0"+calendar.get(Calendar.MINUTE) : String.valueOf(calendar.get(Calendar.MINUTE));
    }
    
    /**
     * Given a time in miliseconds, returns the time in milliseconds of the start of the current hour
     * @param timeInMillis The time in milliseconds
     * @return The start of the hour in milliseconds
     */
    public static long getStartOfHour(long timeInMillis) {
        return ONE_HOUR*(int)(timeInMillis/ONE_HOUR);
    }
    
    /**
     * Compares whether two calendars refer to the same day.
     * @param date1 The first calendar
     * @param date2 The second calendar
     * @return True if both calendars refer to the same day.
     */
    public static boolean isSameDay(Calendar date1, Calendar date2) {
        return compareDates(date1, date2) == 0;
    }
    
    /**
     * Compares whether two times in milliseconds refer to the same day
     * @param dayMillis1 The first time in milliseconds
     * @param dayMillis2 The second time in milliseconds
     * @return True if both times refer to the same day
     */
    public static boolean isSameDay(long dayMillis1, long dayMillis2) {
        Calendar date1 = getCalendar(dayMillis1);
        Calendar date2 = getCalendar(dayMillis2);
        return compareDates(date1, date2) == 0;
    }
}
