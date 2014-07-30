/*
 * Copyright 2014 Kevin Quan (kevin.quan@gmail.com)
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
package com.kevinquan.android.constants;

import java.util.TimeZone;

/**
 * Convenience constants for various commonly used time zones.
 * 
 * Full list may be found here: https://gist.github.com/arpit/1035596
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public interface TimeZones {
    
    public static final TimeZone PACIFIC = TimeZone.getTimeZone("America/Vancouver");
    public static final TimeZone CENTRAL = TimeZone.getTimeZone("America/Winnipeg");
    public static final TimeZone ATLANTIC = TimeZone.getTimeZone("America/Halifax");
    public static final TimeZone MOUNTAIN = TimeZone.getTimeZone("America/Edmonton");
    public static final TimeZone EASTERN = TimeZone.getTimeZone("America/Toronto");
    public static final TimeZone NEWFOUNDLAND = TimeZone.getTimeZone("America/St_Johns");
    
}
