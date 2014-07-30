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
package com.kevinquan.google.play.activityrecoginition.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.util.Log;

import com.kevinquan.android.utils.CursorUtils;
import com.kevinquan.android.utils.JSONUtils;

/**
 * Helpers for working with the Activity Recognition motion model
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class MotionHelper {

    private static final String TAG = MotionHelper.class.getSimpleName();
    
    protected static final String JSON_FIELD_SNAPSHOTS = "snapshots";

    public static List<MotionSnapshot> parseMotionSnapshots(Cursor result, final boolean sortDescending) {
        if (!CursorUtils.hasResults(result)) {
            Log.d(TAG, "No results were provided to parse motion snapshots from");
            return new ArrayList<MotionSnapshot>();
        }
        Hashtable<Long,MotionSnapshot> snapshots = new Hashtable<Long, MotionSnapshot>();
        
        do {
            Motion thisMotion = new Motion(result);
            if (thisMotion.getTimestamp() == 0) {
                Log.w(TAG, "Current motion seems corrupt: "+thisMotion);
                continue;
            }
            if (!snapshots.containsKey(thisMotion.getTimestamp())) {
                MotionSnapshot snapshot = new MotionSnapshot(thisMotion);
                snapshots.put(snapshot.getTimestamp(), snapshot);
            } else {
                if (!snapshots.get(thisMotion.getTimestamp()).addMotion(thisMotion)) {
                    Log.w(TAG, "Could not add motion to snapshot: "+thisMotion.toString());
                }
            }
        } while (result.moveToNext());
        
        List<MotionSnapshot> results = new ArrayList<MotionSnapshot>();
        results.addAll(snapshots.values());
        Collections.sort(results, new Comparator<MotionSnapshot>() {
            @Override public int compare(MotionSnapshot lhs, MotionSnapshot rhs) {
                int result = ((Long)lhs.getTimestamp()).compareTo((Long)rhs.getTimestamp());
                return sortDescending ? -1*result : result;
            }
        });
        return results;
    }
    
    public static JSONObject asJson(List<MotionSnapshot> snapshots) {
        if (snapshots == null || snapshots.size() == 0) {
            Log.w(TAG, "No snapshots were provided to convert.");
            return null;
        }
        JSONObject output = new JSONObject();
        JSONArray snapshotsArray = new JSONArray();
        for (MotionSnapshot snapshot : snapshots) {
            snapshotsArray.put(snapshot.asJson());
        }
        JSONUtils.safePutArray(output, JSON_FIELD_SNAPSHOTS, snapshotsArray);
        return output;
    }
    
    public static List<MotionSnapshot> fromJson(String json) {
        List<MotionSnapshot> snapshots = new ArrayList<MotionSnapshot>();
        JSONObject input = null;
        try {
            input = new JSONObject(json);
        } catch (JSONException je) {
            Log.e(TAG, "Could not parse JSON to hydrate list of motion snapshots.", je);
            return snapshots;
        }
        JSONArray snapshotsArray = JSONUtils.safeGetArray(input, JSON_FIELD_SNAPSHOTS);
        for (int i = 0; i < snapshotsArray.length(); i++) {
            JSONObject snapshotObject = JSONUtils.safeGetJSONObjectFromArray(snapshotsArray, i);
            snapshots.add(new MotionSnapshot(snapshotObject));
        }
        return snapshots;
    }
}
