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
package com.kevinquan.android.utils;

import java.io.File;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**
 * Collection of utilities for working with the SD card
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class SDUtils {

    private static final String TAG = SDUtils.class.getSimpleName();

    /**
     * Saves a string representation of an object to the SD card at the provided destination.
     * @param destination The relative path from the SD root where the object should be written to.  If any intermediate folders do not exist, they will be created.
     * @param content The content to be written to.  The result of the toString() method on the object will be written to disk.
     * @return A file object of the destination file, or null if the write did not succeed.
     */
    public static File saveObjectToSD(String destination, Object content) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.w(TAG, "Could not write to SD card as media is not ready (mounted).");
            return null;
        }
        if (TextUtils.isEmpty(destination)) {
            Log.w(TAG, "Could not write to SD card as no destination was provided.");
            return null;
        }
        if (content == null) {
            Log.w(TAG, "Could not write to SD card as content to write was empty.");
            return null;
        }
        File outputFile = new File(Environment.getExternalStorageDirectory(), destination);
        if (FileUtils.writeContentToFile(outputFile, content)) {
            return outputFile;
        }
        return null;
    }
}
