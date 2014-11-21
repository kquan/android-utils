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

import org.json.JSONObject;

import android.database.Cursor;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.kevinquan.android.location.SimpleRecordedLocation;

/**
 * Models a record location as a lat/lng position in time.
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class Position extends SimpleRecordedLocation {
    
    public static final Parcelable.Creator<Position> CREATOR = new Parcelable.Creator<Position>() {
        public Position createFromParcel(Parcel in) { return new Position(in); }
        public Position[] newArray(int size) { return new Position[size]; }
    };
    
    @SuppressWarnings("unused")
    private static final String TAG = Position.class.getSimpleName();

    public Position(Cursor row) {
        super(row);
    }

    public Position(JSONObject object) {
        super(object);
    }

    public Position(Location location, LocationProviderType provider) {
        super(location, provider);
    }

    public Position(Location location) {
        super(location);
    }

    public Position(Parcel in) {
        super(in);
    }
    
    public Position(Position original) {
    	super(original);
    }

    public LatLng asLatLng() {
        return new LatLng(mLatitude, mLongitude);
    }
    
    public boolean isFromGps() {
        return mProvider == LocationProviderType.GPS;
    }
}
