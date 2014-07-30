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

import java.util.Comparator;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.kevinquan.android.utils.CursorUtils;
import com.kevinquan.android.utils.JSONUtils;

/**
 * This class models a recorded physical location position. 
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class SimpleRecordedLocation implements Parcelable {

    public static final Parcelable.Creator<SimpleRecordedLocation> CREATOR = new Parcelable.Creator<SimpleRecordedLocation>() {
        public SimpleRecordedLocation createFromParcel(Parcel in) { return new SimpleRecordedLocation(in); }
        public SimpleRecordedLocation[] newArray(int size) { return new SimpleRecordedLocation[size]; }
    };
	
    public static class PositionTimestampComparator implements Comparator<SimpleRecordedLocation> {

        protected boolean mIsDescending;
        
        public PositionTimestampComparator() {
            mIsDescending = false;
        }
        
        public PositionTimestampComparator(boolean isDescending) {
            mIsDescending = isDescending;
        }
        
        @Override
        public int compare(SimpleRecordedLocation lhs, SimpleRecordedLocation rhs) {
            int result = ((Long)lhs.getRecordedAt()).compareTo(rhs.getRecordedAt());
            return mIsDescending ? -1*result : result;
        }
        
    }
    
    private static final String TAG = SimpleRecordedLocation.class.getSimpleName();
    
    public enum LocationProviderType {
        Unknown(0),
        GooglePlayServices(1),
        GooglePlayServicesHighAccuracy(2),
        CellID(3),
        WiFi(4),
        GPS(5),
        CreatedProgramatically(6),
        CreatedFromPreferences(7),
        CreatedFromSelf(8), // From SimpleRecordedLocation.asLocation()
        ;
        
        protected static final String PLAY_SERVICES_LITERAL = "fused";
        // The functionality around this field is preventing this class from being pushed into utils library.
        protected static final String CREATED_PROGRAMATICALLY_LITERAL = LocationUtils.CONSTRUCTED_LOCATION_PROVIDER_NAME;
        protected static final String CREATED_FROM_PREFERENCES_LITERAL = LocationUtils.CONSTRUCTED_FROM_PREFERENCES_PROVIDER_NAME;
        
        private int mDbId;
        
        private LocationProviderType(int dbId) {
            mDbId = dbId;
        }
        
        public int getDbId() {
            return mDbId;
        }
        
        public static LocationProviderType fromDbId(int dbId) {
            switch (dbId) {
                case 1: return GooglePlayServices;
                case 2: return GooglePlayServicesHighAccuracy;
                case 3: return CellID;
                case 4: return WiFi;
                case 5: return GPS;
                case 6: return CreatedProgramatically;
                case 7: return CreatedFromPreferences;
                case 8: return CreatedFromSelf;
                default: return Unknown;
            }
        }
        
        public static LocationProviderType fromLiteral(String value) {
            if (TextUtils.isEmpty(value)) {
                return LocationProviderType.Unknown;
            }
            if (PLAY_SERVICES_LITERAL.equals(value)) {
            	return LocationProviderType.GooglePlayServices;
            } else if (CREATED_PROGRAMATICALLY_LITERAL.equals(value)) {
                return LocationProviderType.CreatedProgramatically;
            } else if (CREATED_FROM_PREFERENCES_LITERAL.equals(value)) {
                return LocationProviderType.CreatedFromPreferences;
            } else if (TAG.equals(value)) {
                return LocationProviderType.CreatedFromSelf;
            }
            Log.w(TAG, "Could not find location provider type with literal value: "+value);
            return LocationProviderType.Unknown;
        }
        
        public static LocationProviderType fromValue(String value) {
            if (TextUtils.isEmpty(value)) {
                return LocationProviderType.Unknown;
            }
            for (LocationProviderType type : LocationProviderType.values()) {
                if (type.toString().equals(value)) {
                    return type;
                }
            }
            Log.w(TAG, "Could not find location provider type with value: "+value);
            return LocationProviderType.Unknown;
        }
    }
    
    public static final String JSON_FIELD_LATITUDE = "latitude";
    public static final String JSON_FIELD_LONGITUDE = "longitude";
    public static final String JSON_FIELD_ACCURACY = "accuracy";
    public static final String JSON_FIELD_ALTITUDE = "altitude";
    public static final String JSON_FIELD_BEARING = "bearing";
    public static final String JSON_FIELD_SPEED = "speed";
    public static final String JSON_FIELD_RECORDED_AT = "recordedAt";
    public static final String JSON_FIELD_PROVIDER_ID = "providerId";
    
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_ACCURACY = "accuracy";
    public static final String COLUMN_ALTITUDE = "altitude";
    public static final String COLUMN_BEARING = "bearing";
    public static final String COLUMN_SPEED = "speed";
    public static final String COLUMN_RECORDED_AT = "recordedAt";
    // Note: This field should actually store {@link LocationProviderType}.getDbId()
    public static final String COLUMN_PROVIDER_ID = "provider";
    
    public static final float UNKNOWN_ACCURACY = -1;
    public static final double UNKNOWN_ALTITUDE = -1;
    public static final float UNKNOWN_BEARING = -1;
    public static final float UNKNOWN_SPEED = -1;
    
    protected double mLatitude;
    protected double mLongitude;
    protected float mAccuracy;
    protected double mAltitude;
    protected float mBearing;
    protected float mSpeed;
    protected long mRecordedAt;
    protected LocationProviderType mProvider;
    
    protected SimpleRecordedLocation () {
        mLatitude = LocationUtils.NO_VALUE;
        mLongitude = LocationUtils.NO_VALUE;
        mAccuracy = UNKNOWN_ACCURACY;
        mAltitude = UNKNOWN_ALTITUDE;
        mBearing = UNKNOWN_BEARING;
        mSpeed = UNKNOWN_SPEED;
        mRecordedAt = 0;
        mProvider = LocationProviderType.Unknown;
    }

    public SimpleRecordedLocation(Location location) {
        this(location, location != null ? LocationProviderType.fromLiteral(location.getProvider()) : LocationProviderType.Unknown);
    }
    
    public SimpleRecordedLocation(Location location, LocationProviderType provider) {
        this();
        if (location == null) {
            Log.w(TAG, "Location provided to construct position was null.");
            return;
        }
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        mAccuracy = location.getAccuracy();
        mAltitude = location.getAltitude();
        mBearing = location.getBearing();
        mSpeed = location.getSpeed();
        mRecordedAt = location.getTime();
        mProvider = provider;
    }
    
    public SimpleRecordedLocation(Cursor row) {
        this();
        if (row == null) {
            Log.w(TAG, "Cursor provided to construct position was null.");
            return;
        }
        mLatitude = LocationUtils.parseLocationValueFromString(CursorUtils.safeGetString(row, COLUMN_LATITUDE));
        mLongitude = LocationUtils.parseLocationValueFromString(CursorUtils.safeGetString(row, COLUMN_LONGITUDE));
        mAccuracy = CursorUtils.safeGetFloat(row, COLUMN_ACCURACY, UNKNOWN_ACCURACY);
        mAltitude = CursorUtils.safeGetDouble(row, COLUMN_ALTITUDE, UNKNOWN_ALTITUDE);
        mBearing = CursorUtils.safeGetFloat(row, COLUMN_BEARING, UNKNOWN_BEARING);
        mSpeed = CursorUtils.safeGetFloat(row, COLUMN_SPEED, UNKNOWN_SPEED);
        mRecordedAt = CursorUtils.safeGetLong(row, COLUMN_RECORDED_AT, 0);
        mProvider = LocationProviderType.fromDbId(CursorUtils.safeGetInt(row, COLUMN_PROVIDER_ID, LocationProviderType.Unknown.getDbId()));
    }
    
    public SimpleRecordedLocation(Parcel in) {
        this();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mAccuracy = in.readFloat();
        mAltitude = in.readDouble();
        mBearing = in.readFloat();
        mSpeed = in.readFloat();
        mRecordedAt = in.readLong();
        mProvider = LocationProviderType.fromValue(in.readString());
    }
    
    public SimpleRecordedLocation(JSONObject object) {
        this();
    	mLatitude = JSONUtils.safeGetDouble(object, JSON_FIELD_LATITUDE, LocationUtils.NO_VALUE);
    	mLongitude = JSONUtils.safeGetDouble(object, JSON_FIELD_LONGITUDE, LocationUtils.NO_VALUE);
    	mAccuracy = (float)JSONUtils.safeGetDouble(object, JSON_FIELD_ACCURACY, UNKNOWN_ACCURACY);
    	mAltitude = JSONUtils.safeGetDouble(object, JSON_FIELD_ALTITUDE, UNKNOWN_ALTITUDE);
    	mBearing = (float)JSONUtils.safeGetDouble(object, JSON_FIELD_BEARING, UNKNOWN_BEARING);
    	mSpeed = (float)JSONUtils.safeGetDouble(object, JSON_FIELD_SPEED, UNKNOWN_SPEED);
    	mRecordedAt = JSONUtils.safeGetLong(object, JSON_FIELD_RECORDED_AT, 0);
    	mProvider = LocationProviderType.fromDbId(JSONUtils.safeGetInt(object, JSON_FIELD_PROVIDER_ID, LocationProviderType.Unknown.getDbId()));
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public float getAccuracy() {
        return mAccuracy;
    }

    public double getAltitude() {
        return mAltitude;
    }

    public float getBearing() {
        return mBearing;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public long getRecordedAt() {
        return mRecordedAt;
    }

    public LocationProviderType getProvider() {
        return mProvider;
    }
    
    public Location asLocation() {
        Location location = new Location(TAG);
        location.setLatitude(mLatitude);
        location.setLongitude(mLongitude);
        location.setAccuracy(mAccuracy);
        location.setAltitude(mAltitude);
        location.setBearing(mBearing);
        location.setSpeed(mSpeed);
        location.setTime(mRecordedAt);
        return location;
    }
    
    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeFloat(mAccuracy);
        dest.writeDouble(mAltitude);
        dest.writeFloat(mBearing);
        dest.writeFloat(mSpeed);
        dest.writeLong(mRecordedAt);
        dest.writeString(mProvider.toString());
    }

    @Override public int describeContents() { return 0; }
    
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SimpleRecordedLocation)) {
			return false;
		}
		SimpleRecordedLocation other = (SimpleRecordedLocation)o;
		return mRecordedAt == other.getRecordedAt()
				&& mLatitude == other.getLatitude() 
				&& mLongitude == other.getLongitude() 
				&& mAccuracy == other.getAccuracy() 
				&& mAltitude == other.getAltitude()
				&& mBearing == other.getBearing()
				&& mSpeed == other.getSpeed()
				&& mProvider == other.getProvider();
	}
	
	public JSONObject asJson(boolean concise) {
		JSONObject output = new JSONObject();
		JSONUtils.safePutDouble(output, JSON_FIELD_LATITUDE, mLatitude);
		JSONUtils.safePutDouble(output, JSON_FIELD_LONGITUDE, mLongitude);
		JSONUtils.safePutFloat(output, JSON_FIELD_ACCURACY, mAccuracy);
		if (!concise) {
			JSONUtils.safePutDouble(output, JSON_FIELD_ALTITUDE, mAltitude);
			JSONUtils.safePutFloat(output, JSON_FIELD_BEARING, mBearing);
			JSONUtils.safePutFloat(output, JSON_FIELD_SPEED, mSpeed);
		}
		JSONUtils.safePutLong(output, JSON_FIELD_RECORDED_AT, mRecordedAt);
		JSONUtils.safePutInt(output, JSON_FIELD_PROVIDER_ID, mProvider.getDbId());
		return output;
	}
	
	public ContentValues getInsertContentValues() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LATITUDE, getLatitude());
        values.put(COLUMN_LONGITUDE, getLongitude());
        values.put(COLUMN_ACCURACY, getAccuracy());
        values.put(COLUMN_ALTITUDE, getAltitude());
        values.put(COLUMN_BEARING, getBearing());
        values.put(COLUMN_SPEED, getSpeed());
        values.put(COLUMN_RECORDED_AT, getRecordedAt());
        values.put(COLUMN_PROVIDER_ID, getProvider().getDbId());
        return values;
	}
}
