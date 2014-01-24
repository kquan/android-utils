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
package com.kevinquan.android.constants.canada;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.location.Location;
import android.text.TextUtils;

import com.kevinquan.android.constants.TimeZones;

/**
 * Models large cities within Canada (such as capitals).  Lat/Lng data is from Wikipedia
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public enum City {

    StJohns("St. John's", Province.NewfoundlandAndLabrador, 47.5675, -52.707222, TimeZone.getTimeZone("America/St_Johns")),
    Charlottetown("Charlottetown", Province.PrinceEdwardIsland, 46.233333, -63.15, TimeZones.ATLANTIC),
    Halifax("Halifax", Province.NovaScotia, 44.854444, -63.199167, TimeZone.getTimeZone("America/Halifax")),
    SaintJohn("Saint John", Province.NewBrunswick, 45.280556, -66.076111, TimeZones.ATLANTIC),
    Fredericton("Fredericton", Province.NewBrunswick, 45.957319, -66.647818, TimeZones.ATLANTIC),
    Quebec("Québec City", "Ville de Québec", Province.Quebec, 46.816667, -71.216667, TimeZones.EASTERN),
    Montreal("Montreal", "Montréal", Province.Quebec, 45.5, -73.566667, TimeZone.getTimeZone("America/Montreal")),
    Ottawa("Ottawa", Province.Ontario, 45.417, -75.7, TimeZones.EASTERN),
    Toronto("Toronto", Province.Ontario, 43.7, -79.4, TimeZone.getTimeZone("America/Toronto")),
    ThunderBay("Thunder Bay", Province.Ontario, 48.382222, -89.246111, TimeZone.getTimeZone("America/Thunder_Bay")),
    Winnipeg("Winnipeg", Province.Manitoba, 49.899444, -97.139167, TimeZone.getTimeZone("America/Winnipeg")),
    Regina("Regina", Province.Saskatchewan, 50.454722, -104.606667, TimeZone.getTimeZone("America/Regina")),
    Saskatoon("Saskatoon", Province.Saskatchewan, 52.133333, -106.683333, TimeZones.CENTRAL),
    Edmonton("Edmonton", Province.Alberta, 53.533333, -113.5, TimeZone.getTimeZone("America/Edmonton")),
    Calgary("Calgary", Province.Alberta, 51.05, -114.066667, TimeZones.MOUNTAIN),
    Vancouver("Vancouver", Province.BritishColumbia, 49.25, -123.1, TimeZone.getTimeZone("America/Vancouver")),
    Victoria("Victoria", Province.BritishColumbia, 48.422151, -123.3657, TimeZones.PACIFIC),
    Whitehorse("Whitehorse", Province.Yukon, 60.716667, -135.05, TimeZone.getTimeZone("America/Whitehorse")),
    Yellowknife("Yellowknife", Province.NorthwestTerritories, 62.442222, -114.3975, TimeZone.getTimeZone("America/Yellowknife")),
    Iqaluit("Iqaluit", Province.Nunavut, 63.748611, -68.519722, TimeZone.getTimeZone("America/Iqaluit")),
    ;
    
    protected static final String LOCATION_PROVIDER = "android-utils-kquan";

    protected String mName;
    protected String mFrenchName;
    protected Province mProvince;
    protected Location mCenter;
    protected TimeZone mTimeZone;
    
    private City(String fullName, Province province, double latitude, double longitude, TimeZone timeZone) {
        mName = fullName;
        mProvince = province;
        mCenter = new Location(LOCATION_PROVIDER);
        mCenter.setLatitude(latitude);;
        mCenter.setLongitude(longitude);
        mTimeZone = timeZone;
    }
    
    private City(String fullName, String frenchName, Province province, double latitude, double longitude, TimeZone timeZone) {
        mName = fullName;
        mFrenchName = frenchName;
        mProvince = province;
        mCenter = new Location(LOCATION_PROVIDER);
        mCenter.setLatitude(latitude);;
        mCenter.setLongitude(longitude);
        mTimeZone = timeZone;
    }

    /**
     * Returns the display name of the city.  If French locale is detected, the french name (if available) will be returned
     * @return The display name of the city
     */
    public String getName() {
        if (!TextUtils.isEmpty(mFrenchName)) {
            Locale currentLocale = Locale.getDefault();
            if (Locale.FRENCH.equals(currentLocale) || Locale.CANADA_FRENCH.equals(currentLocale)) {
                return mFrenchName;
            }
        }
        return mName;
    }

    public Province getProvince() {
        return mProvince;
    }

    /**
     * Returns a lat/lng point of the center of the city as provided by Wikipedia
     * @return A location with only the latitude and longitude filled
     */
    public Location getCenter() {
        return mCenter;
    }
    
    /**
     * Returns all cities as an unsorted list
     * @return A list of cities
     */
    public static List<City> asList() {
        List<City> cities = new ArrayList<City>(City.values().length);
        for (City city : City.values()) {
            cities.add(city);
        }
        return cities;
    }
    
    /**
     * Returns the closest city to the given point
     * @param latitude The latitude of the point
     * @param longitude The longitude of the point
     * @return The closest city
     */
    public static City getClosestCityTo(double latitude, double longitude) {
        Location point = new Location(LOCATION_PROVIDER);
        point.setLatitude(latitude);
        point.setLongitude(longitude);
        double shortestDistance = Double.MAX_VALUE;
        City closestCity = null;
        for (City city : City.values()) {
            double distance = point.distanceTo(city.getCenter());
            if (distance < shortestDistance) {
                closestCity = city;
                shortestDistance = distance;
            }
        }
        return closestCity;
    }
    
    /**
     * Returns a sorted list of the closest cities to the given point
     * @param latitude The latitude of the point
     * @param longitude The longitude of the point
     * @return A sorted list of cities with the closest city first
     */
    public static List<City> getClosestCities(double latitude, double longitude) {
        List<City> cities = asList();
        final Location point = new Location(LOCATION_PROVIDER);
        point.setLatitude(latitude);
        point.setLongitude(longitude);
        Collections.sort(cities, new Comparator<City>() {
            @Override public int compare(City lhs, City rhs) {
                double lhsDistance = point.distanceTo(lhs.getCenter());
                double rhsDistance = point.distanceTo(rhs.getCenter());
                if (lhsDistance < rhsDistance) {
                    return -1;
                } else if (rhsDistance < lhsDistance) {
                    return 1;
                }
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        return cities;
    }
}
