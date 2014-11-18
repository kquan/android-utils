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
package com.kevinquan.google.activityrecoginition.model;

import org.json.JSONObject;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;
import com.kevinquan.android.utils.CursorUtils;
import com.kevinquan.android.utils.JSONUtils;

/**
 * Models a Google Play activity recognition activity
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class Motion implements Parcelable {
    
    public static final Parcelable.Creator<Motion> CREATOR = new Parcelable.Creator<Motion>() {
        public Motion createFromParcel(Parcel in) { return new Motion(in); }
        public Motion[] newArray(int size) { return new Motion[size]; }
    };

    private static final String TAG = Motion.class.getSimpleName();
    
    protected static final String JSON_FIELD_MOTIONTYPE = "motionType";
    protected static final String JSON_FIELD_CONFIDENCE = "confidence";
    protected static final String JSON_FIELD_TIMESTAMP = "timestamp";
    protected static final String JSON_FIELD_MILLIS_SINCE_BOOT = "millisSinceBoot";
    
    public static final String COLUMN_ACTIVITY_TYPE = "activityType";
    public static final String COLUMN_CONFIDENCE = "confidence";
    public static final String COLUMN_MILLIS_SINCE_BOOT = "millisSinceBoot";
    public static final String COLUMN_RECORDED_AT = "recordedAt";
    
    public enum MotionType {
        Vehicle(DetectedActivity.IN_VEHICLE),
        Bicycling(DetectedActivity.ON_BICYCLE),
        WalkingOrRunning(DetectedActivity.ON_FOOT),
        Walking(DetectedActivity.WALKING),
        Running(DetectedActivity.RUNNING),
        Still(DetectedActivity.STILL),
        Tilting(DetectedActivity.TILTING),
        Unknown(DetectedActivity.UNKNOWN),
        ;
        
        protected int mGoogleId;
        
        private MotionType(int googleId) {
            mGoogleId = googleId;
        }
        
        public int getGoogleActivityId() {
            return mGoogleId;
        }
        
        public static MotionType fromGoogleId(int id) {
            for (MotionType type : MotionType.values()) {
                if (type.getGoogleActivityId() == id) {
                    return type;
                }
            }
            Log.w(TAG, "Could not find activity type with id: "+id);
            return MotionType.Unknown;
        }
        
        public static MotionType fromValue(String value) {
            if (TextUtils.isEmpty(value)) {
                return MotionType.Unknown;
            }
            for (MotionType type : MotionType.values()) {
                if (type.toString().equals(value)) {
                    return type;
                }
            }
            Log.w(TAG, "Could not find motion type with value: "+value);
            return MotionType.Unknown;
        }
    }
    
    protected MotionType mType;
    protected int mConfidence;
    protected long mMillisSinceBoot;
    protected long mTimestamp;

    public Motion(DetectedActivity activity) {
        mType = MotionType.fromGoogleId(activity.getType());
        mConfidence = activity.getConfidence();
    }
    
    public Motion(DetectedActivity activity, long timestamp, long millisSinceBoot) {
        this(activity);
        setTimes(timestamp, millisSinceBoot);
    }
    
    public Motion(Cursor result) {
        mType = MotionType.fromValue(CursorUtils.safeGetString(result, COLUMN_ACTIVITY_TYPE));
        mConfidence = CursorUtils.safeGetInt(result, COLUMN_CONFIDENCE, 0);
        mTimestamp = CursorUtils.safeGetLong(result, COLUMN_RECORDED_AT, 0);
        mMillisSinceBoot = CursorUtils.safeGetLong(result, COLUMN_MILLIS_SINCE_BOOT, Long.MAX_VALUE);
    }
    
    public Motion(Parcel in) {
        mType = MotionType.fromGoogleId(in.readInt());
        mConfidence = in.readInt();
        mTimestamp = in.readLong();
        mMillisSinceBoot = in.readLong();
    }
    
    public Motion(JSONObject object, long timestamp) {
        mType = MotionType.fromValue(JSONUtils.safeGetString(object, JSON_FIELD_MOTIONTYPE));
        mConfidence = JSONUtils.safeGetInt(object, JSON_FIELD_CONFIDENCE, 0);
        mMillisSinceBoot = JSONUtils.safeGetLong(object, JSON_FIELD_MILLIS_SINCE_BOOT, Long.MAX_VALUE);
        mTimestamp = JSONUtils.safeGetLong(object, JSON_FIELD_TIMESTAMP, timestamp);
        if (mTimestamp <= 0) {
            Log.w(TAG, "Timestamp was not parsed from JSON or was not provided: "+timestamp);
        }
    }
    
    public MotionType getType() {
        return mType;
    }

    public int getConfidence() {
        return mConfidence;
    }
    
    public Motion setTimes(long timestamp, long millisSinceBoot) {
        mTimestamp = timestamp;
        mMillisSinceBoot = millisSinceBoot;
        return this;
    }
    
    public long getMillisSinceBoot() {
        return mMillisSinceBoot;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mType.getGoogleActivityId());
        dest.writeInt(mConfidence);
        dest.writeLong(mTimestamp);
        dest.writeLong(mMillisSinceBoot);
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Motion)) {
            return false;
        }
        Motion other = (Motion)o;
        return other.getType() == mType && other.getConfidence() == mConfidence && other.getTimestamp() == mTimestamp; 
    }
    
    @Override
    public String toString() {
        String result = null;
        if (mType == null) {
            result = "Motion of unknown type: ";
        } else {
            result = mType.toString()+":";
        }
        result += mConfidence+"/100 at ";
        result += mTimestamp+" ("+mMillisSinceBoot+")";
        return result;
    }
    
    public JSONObject asJson(boolean includeTimestamp) {
        JSONObject output = new JSONObject();
        JSONUtils.safePutString(output, JSON_FIELD_MOTIONTYPE, mType.toString());
        JSONUtils.safePutInt(output, JSON_FIELD_CONFIDENCE, mConfidence);
        if (includeTimestamp) {
            JSONUtils.safePutLong(output, JSON_FIELD_TIMESTAMP, mTimestamp);
        }
        JSONUtils.safePutLong(output, JSON_FIELD_MILLIS_SINCE_BOOT, mMillisSinceBoot);
        return output;
    }

}
