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
    
    public enum CardinalDirections {
        Unknown(-1),
        N(0),
        NNE(22.5f),
        NE(45),
        ENE(67.5f),
        E(90),
        ESE(112.5f),
        SE(135),
        SSE(157.5f),
        S(180),
        SSW(202.5f),
        SW(225),
        WSW(247.5f),
        W(270),
        WNW(292.5f),
        NW(315),
        NNW(337.5f),
        ;
        // See http://en.wikipedia.org/wiki/Points_of_the_compass for limits 
        protected float mMiddleDegrees;
        
        private CardinalDirections(float degrees) {
            mMiddleDegrees = degrees;
        }
        
        public float getDegrees() {
            return mMiddleDegrees;
        }
    }
    
    /**
     * Constant for no longitude or latitude value
     */
    public static final double NO_VALUE = Double.MAX_VALUE;
    
    // Reference: http://en.wikipedia.org/wiki/Earth_radius
    public static final int EARTH_MEAN_RADIUS = 6371000; // In metres
    
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
    
    /**
     * Returns a normalized value for the bearing between the from and to location provided in the parameters 
     * @param from The location to calculate bearing from
     * @param to The location to calculate bearing to
     * @return Value from [0-360] which represents the degrees of the bearing.  0 Should be north.
     */
    public static float getNormalizedBearing(Location from, Location to) {
        if (from == null || to == null) {
            return 0;
        }
        return normalizeBearing(from.bearingTo(to));
    }
    
    /**
     * Normalize a bearing value so that it is in the range [0,360]
     * @param bearing The bearing to normalize
     * @return The normalized bearing
     */
    public static float normalizeBearing(float bearing) {
        if (bearing < 0) {
            bearing += 360;
        }
        return bearing % 360;
    }
    
    /**
     * Returns the closest cardinal direction for the given normalized bearing
     * @param normalizedBearing The normalized bearing
     * @return The closest cardinal direction
     */
    public static CardinalDirections getCardinalDirection(float normalizedBearing) {
        // http://stackoverflow.com/a/2131294/1339200
        int cardinalDegrees = Math.abs(225 * (int)Math.round(10*normalizedBearing/225));
        switch (cardinalDegrees) {
            case 0: return CardinalDirections.N;
            case 225: return CardinalDirections.NNE;
            case 450: return CardinalDirections.NE;
            case 675: return CardinalDirections.ENE;
            case 900: return CardinalDirections.E;
            case 1125: return CardinalDirections.ESE;
            case 1350: return CardinalDirections.SE;
            case 1575: return CardinalDirections.SSE;
            case 1800: return CardinalDirections.S;
            case 2025: return CardinalDirections.SSW;
            case 2250: return CardinalDirections.SW;
            case 2475: return CardinalDirections.WSW;
            case 2700: return CardinalDirections.W;
            case 2925: return CardinalDirections.WNW;
            case 3150: return CardinalDirections.NW;
            case 3375: return CardinalDirections.NNW;
            case 3600: return CardinalDirections.N;
        }
        return CardinalDirections.Unknown;
    }
    
    /**
     * Checks whether the provided latitude/longitude values are valid
     * @param latitude The latitude to check
     * @param latitude The longitude to check
     * @return True if both values are valid
     */
    public static boolean isValid(double latitude, double longitude) {
        if (latitude == NO_VALUE || longitude == NO_VALUE) {
            return false;
        }
        if (Math.abs(latitude) > 90) {
            // Latitude can go from -90 to +90
            return false;
        }
        if (Math.abs(longitude) > 180) {
            // Latitude can go from -180 to +180
            return false;
        }
        return true;
    }
}
