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

/**
 * This interface defines unit conversion constants
 * 
 * The expected format is as follows:
 * - To convert x in y units to z units, call x*Y.TO_z
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public interface Conversions {

    public interface Metres {
        
        public static double TO_MILES = 1/1609.34;
        
    }
    
    public interface MetresPerSecond {
        
        public static double TO_KILOMETRES_PER_HOUR = 60d*60d/1000d;
        
    }
    
    public interface MetresPerMillis {
        
        public static double TO_KILOMETRES_PER_HOUR = 1000*60*60/1000;
        
    }
    
    public interface KilometresPerHour {
        
        public static double TO_METRES_PER_MILLIS = 1/MetresPerMillis.TO_KILOMETRES_PER_HOUR;
        public static double TO_METRES_PER_SECOND = 1/MetresPerSecond.TO_KILOMETRES_PER_HOUR;
        
    }
}
