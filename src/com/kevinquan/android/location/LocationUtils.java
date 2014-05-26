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
package com.kevinquan.android.location;

import android.content.SharedPreferences;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

/**
 * A collection of utilities for dealing with location
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class LocationUtils {

    private static final String TAG = LocationUtils.class.getSimpleName();
    
    public static final double NO_VALUE = Double.MAX_VALUE;
    
    public static final String CONSTRUCTED_LOCATION_PROVIDER_NAME = LocationUtils.class.getSimpleName();
    public static final String CONSTRUCTED_FROM_PREFERENCES_PROVIDER_NAME = LocationUtils.class.getSimpleName()+"FromPreferences";
    
    /**
     * Constructs a location object with the given lat/lng value
     * @param latitude The latitude value to use
     * @param longitude The longitude value to use
     * @return A location object
     */
    public static Location asLocation(double latitude, double longitude) {
        Location location = new Location(CONSTRUCTED_LOCATION_PROVIDER_NAME);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
    
    /**
     * Parses a string stored in the provided preferences under the provided preference key as a double.
     * 
     * If the value does not exist or cannot be parsed as a double, the provided default value will be returned.
     * 
     * This method is useful for retrieving latitude and longitudes, which are typically modelled as doubles.  However
     * Android's preferences cannot store double values.
     * 
     * @param preferences The preferences to query
     * @param preferenceKey The preference key being used to store the value
     * @param defaultValue The default value if the preference doesn't exist
     * @return The double value from preference or the default value
     */
    public static double getValueFromPreference(SharedPreferences preferences, String preferenceKey, String defaultValue) {
        double defaultValueAsDouble = parseLocationValueFromString(defaultValue);
        if (preferences == null || TextUtils.isEmpty(preferenceKey) || !preferences.contains(preferenceKey)) {
            return defaultValueAsDouble;
        }
        String currentValue = preferences.getString(preferenceKey, defaultValue);
        return parseLocationValueFromString(currentValue);
    }
    
    /**
     * Parses a double from a string that is meant to represent a latitude or longitude value. 
     * @param value The value to parse from
     * @return The double representing the value or {@link LocationUtils}.NO_VALUE if one cannot be parsed
     */
    public static double parseLocationValueFromString(String value) {
        double valueAsDouble = NO_VALUE;
        if (!TextUtils.isEmpty(value)) {
            try {
                valueAsDouble = Double.parseDouble(value);
            } catch (NumberFormatException nfe) {
                Log.w(TAG, "Could not parse lat/lng value as double from "+value, nfe);
            }
        }
        return valueAsDouble;
    }
    
    /**
     * Saves the lat/lng of a location to the preferences
     * @param editor An editor for the preferences file
     * @param location The location to store
     * @param latitudeKey The preference key for the latitude value
     * @param longitudeKey The preference key for the longitude value
     */
    public static void saveLocationToPreference(SharedPreferences.Editor editor, Location location, String latitudeKey, String longitudeKey) {
        if (editor == null || TextUtils.isEmpty(latitudeKey) || TextUtils.isEmpty(longitudeKey)) {
            return;
        }
        if (location == null) {
            editor.remove(latitudeKey);
            editor.remove(longitudeKey);
            editor.commit();
            return;
        }
        editor.putString(latitudeKey, String.valueOf(location.getLatitude()));
        editor.putString(longitudeKey, String.valueOf(location.getLongitude()));
        editor.commit();
    }
    
    /**
     * Helper to retrieve a lat/lng location from a stored preference key
     * @param prefs The preference file to retrieve from
     * @param latitudeKey The preference key that is storing the latitude value
     * @param longitudeKey The preference key that is storing the longitude value
     * @return A location object from the stored value, or if null if no value is stored
     */
    public static Location getLocationFromPreference(SharedPreferences prefs, String latitudeKey, String longitudeKey) {
        if (prefs == null || TextUtils.isEmpty(latitudeKey) || TextUtils.isEmpty(longitudeKey)) {
            return null;
        }
        double latitude = getValueFromPreference(prefs, latitudeKey, null);
        double longitude = getValueFromPreference(prefs, longitudeKey, null);
        if (latitude != NO_VALUE && longitude != NO_VALUE) {
            Location result = new Location(CONSTRUCTED_FROM_PREFERENCES_PROVIDER_NAME);
            result.setLatitude(latitude);
            result.setLongitude(longitude);
            return result;
        }
        return null;
    }
}
