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
package com.kevinquan.google.location;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.kevinquan.android.location.LocationUtils;

/**
 * Additional location utilities when working in the Google stack
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class GoogleLocationUtils extends LocationUtils {

    @SuppressWarnings("unused")
    private static final String TAG = GoogleLocationUtils.class.getSimpleName();
    
    public static Location asLocation(LatLng point) {
        if (point == null) {
            return null;
        }
        Location location = new Location(CONSTRUCTED_LOCATION_PROVIDER_NAME);
        location.setLatitude(point.latitude);
        location.setLongitude(point.longitude);
        return location;
    }
    
    
    /**
     * Convert a {@link Location} to a {@link LatLng} object
     * @param point The point to convert
     * @return The LatLng value
     */
    public static LatLng asLatLng(Location point) {
        if (point == null) {
            return null;
        }
        return new LatLng(point.getLatitude(), point.getLongitude());
    }
    
    /**
     * Calculates a point that is atDistance (in meteres) away from the provided point, at bearing atBearing
     * @param location The location to calculate from
     * @param atBearing The bearing of the new point from the original location
     * @param atDistance The distance to the new location from the original location
     * @return The new location
     */
    public static Location computePointFrom(Location location, float atBearing, float atDistance) { 
        // From http://stackoverflow.com/a/14332842/1339200
        double radius = EARTH_MEAN_RADIUS; // Radius and distance should be same units, in this case metres
        double lat1 = Math.toRadians(location.getLatitude()); 
        double lng1 = Math.toRadians(location.getLongitude()); 
        double brng = Math.toRadians(atBearing); 
        double lat2 = Math.asin( Math.sin(lat1)*Math.cos(atDistance/radius) + Math.cos(lat1)*Math.sin(atDistance/radius)*Math.cos(brng) ); 
        double lng2 = lng1 + Math.atan2(Math.sin(brng)*Math.sin(atDistance/radius)*Math.cos(lat1), Math.cos(atDistance/radius)-Math.sin(lat1)*Math.sin(lat2)); 
        lng2 = (lng2+Math.PI)%(2*Math.PI) - Math.PI;  

        Location newLocation = new Location(CONSTRUCTED_LOCATION_PROVIDER_NAME);
        // normalize to -180...+180 
        if (lat2 == 0 || lng2 == 0) {
            newLocation.setLatitude(0);
            newLocation.setLongitude(0);
        } else {
            newLocation.setLatitude(Math.toDegrees(lat2));
            newLocation.setLongitude(Math.toDegrees(lng2));
        }

        return newLocation;
    };

}
