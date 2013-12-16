package com.kevinquan.android.test.utils;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.kevinquan.android.utils.CalendarUtils;

public class CalendarUtilsTests extends TestCase {

    public void testfindTopOfHour() {
        long expectedValue = 1384347600000L; // 11/13/2013 13:00:00 GMT
        // Exact top of hour
        Assert.assertEquals("Unchanged hour was not returned.", expectedValue, CalendarUtils.getStartOfHour(expectedValue));
        Assert.assertEquals("Hour was not returned when a couple of milliseconds passed.", expectedValue, CalendarUtils.getStartOfHour(expectedValue+250));
        Assert.assertEquals("Hour was not returned when a couple of minutes passed.", expectedValue, CalendarUtils.getStartOfHour(expectedValue+60*3*1000));
        Assert.assertFalse("Hour was incorrectly returned when an hour has passed.", expectedValue == CalendarUtils.getStartOfHour(expectedValue+60*62*1000));        
    }
    
}
