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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.kevinquan.android.utils.JSONUtils;
import com.kevinquan.google.activityrecoginition.model.Motion.MotionType;

/**
 * Models a series of motions reported by Google Play activity recognition at a certain instance in time.
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class MotionSnapshot {

    private static final String TAG = MotionSnapshot.class.getSimpleName();
    
    protected static final String JSON_FIELD_TIMESTAMP = "timestamp";
    protected static final String JSON_FIELD_MOTIONS = "motions";

    protected List<Motion> mMotions;
    protected long mTimestamp;
    
    public MotionSnapshot(Motion firstMotion) {
        mTimestamp = firstMotion.getTimestamp();
        mMotions = new ArrayList<Motion>();
        mMotions.add(firstMotion);
    }
    
    public MotionSnapshot(JSONObject object) {
        mTimestamp = JSONUtils.safeGetLong(object, JSON_FIELD_TIMESTAMP, 0);
        mMotions = new ArrayList<Motion>();
        JSONArray motions = JSONUtils.safeGetArray(object, JSON_FIELD_MOTIONS);
        if (motions != null) {
            for (int i = 0; i < motions.length(); i++) {
                JSONObject motionObject = JSONUtils.safeGetJSONObjectFromArray(motions, i);
                addMotion(new Motion(motionObject, mTimestamp));
            }
        }
    }
    
    public long getTimestamp() {
        return mTimestamp;
    }
    
    public boolean addMotion(Motion aMotion) {
        if (aMotion == null) {
            return false;
        }
        if (aMotion.getTimestamp() == mTimestamp) {
            mMotions.add(aMotion);
            return true;
        } else {
            Log.w(TAG, "Motion could not be added as the timestamp was incorrect.  Expected "+mTimestamp+" but found "+aMotion.getTimestamp());
        }
        return false;
    }
    
    public Motion getMotion(MotionType type) {
        for (Motion motion : mMotions) {
            if (motion.getType() == type) {
                return motion;
            }
        }
        return null;
    }
    
    public Motion getMostProbableMotion() {
        if (mMotions.size() == 0) {
            return null;
        }
        boolean hasTie = false;
        Motion maxConfidenceMotion = null;
        for (Motion motion : mMotions) {
            if (maxConfidenceMotion == null) {
                maxConfidenceMotion = motion;
            } else if (motion.getConfidence() > maxConfidenceMotion.getConfidence()) {
                maxConfidenceMotion = motion;
                hasTie = false;
            } else if (motion.getConfidence() == maxConfidenceMotion.getConfidence()) {
                hasTie = true;
            }
        }
        if (hasTie) {
            // There is no probable activity as multiple activities have the same confidence.
            return null;
        }
        return maxConfidenceMotion;
    }
    
    public int size() {
        return mMotions.size();
    }
    
    public JSONObject asJson() {
        JSONObject output = new JSONObject();
        JSONUtils.safePutLong(output, JSON_FIELD_TIMESTAMP, mTimestamp);
        JSONArray motions = new JSONArray();
        for (Motion motion : mMotions) {
            motions.put(motion.asJson(false));
        }
        JSONUtils.safePutArray(output, JSON_FIELD_MOTIONS, motions);
        return output;
    }
}
