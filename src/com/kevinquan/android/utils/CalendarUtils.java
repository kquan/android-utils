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
}
