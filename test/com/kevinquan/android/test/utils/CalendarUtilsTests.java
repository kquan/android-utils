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
