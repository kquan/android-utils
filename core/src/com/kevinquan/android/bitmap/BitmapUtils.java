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
package com.kevinquan.android.bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

import com.kevinquan.android.utils.FileUtils;
import com.kevinquan.utils.IOUtils;

/**
 * Collection of utilities when working with bitmaps
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();
    
    /**
     * Retrieve the bitmap dimensions and type for an image located at @param bitmapLocation
     * @param bitmapLocation The location of the image
     * @return The metadata for the image
     */
    public static Options loadBitmapMetadataFrom(File bitmapLocation) {
        BitmapFactory.Options loadOptions = new BitmapFactory.Options();
        loadOptions.inJustDecodeBounds = true;
        if (bitmapLocation == null || !bitmapLocation.exists()) {
            return loadOptions;
        }
        BitmapFactory.decodeFile(bitmapLocation.getAbsolutePath(), loadOptions);
        return loadOptions;
    }
    
    /**
     * Calculate the reduction factor necessary to load an image within 
     * @param metadata
     * @param targetSideInPixels
     * @return
     */
    public static int calculateReductionFactor(Options metadata, int targetSideInPixels, boolean fitUnderTarget) {
        if (metadata == null || targetSideInPixels < 1) {
            return 0;
        }
        if (metadata.outWidth < targetSideInPixels && metadata.outHeight < targetSideInPixels) {
            // Bitmap is already too small, so load as-is.
            return 1;
        }
        
        int reductionFactor = 1;
        int targetSide = fitUnderTarget ?
                            // longest side will be less than the target side
                            Math.max(metadata.outWidth, metadata.outHeight) : 
                            // shortest side will be one factor of 2 larger than the target side
                            Math.min(metadata.outWidth, metadata.outHeight)/2;
        // reduction factor must be a power of 2 as per http://developer.android.com/training/displaying-bitmaps/load-bitmap.html#load-bitmap 
        while (targetSide / reductionFactor > targetSideInPixels) {
            reductionFactor *= 2;
        }
        Log.d(TAG, "Reduction factor of "+reductionFactor+" required to load "+metadata.outWidth+"x"+metadata.outHeight+"px bitmap into "+targetSideInPixels+"x"+targetSideInPixels+"px");
        return reductionFactor;
    }
    
    /**
     * Loads a bitmap from file, scaled with the given reduction factor
     * @param bitmapLocation The location of the image
     * @param reductionFactor The factor to reduce the image by
     * @return The loaded bitmap
     */
    public static Bitmap loadScaledBitmapFrom(File bitmapLocation, int reductionFactor) {
        if (bitmapLocation == null || !bitmapLocation.exists()) {
            return null;
        }
        BitmapFactory.Options options =  new BitmapFactory.Options();
        options.inSampleSize = reductionFactor;
        return BitmapFactory.decodeFile(bitmapLocation.getAbsolutePath(), options);
    }
    
    /**
     * Serialize a bitmap to disk
     * @param bitmap The bitmap to serialize
     * @param bitmapLocation The location to serialize to
     * @param format The format to save the bitmap as
     * @param quality The quality of the saved bitmap
     * @return True if the bitmap was saved successfully.
     */
    public static boolean writeBitmaptoDisk(Bitmap bitmap, File bitmapLocation, CompressFormat format, int quality) {
        if (bitmap == null || bitmapLocation == null) {
            return false;
        }
        if (format == null) {
            format = CompressFormat.JPEG;
        }
        FileUtils.ensureParentFoldersCreated(bitmapLocation, true);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(bitmapLocation);
        } catch (FileNotFoundException fnfe) {
            Log.e(TAG, "Could not create output location to write bitmap at "+bitmapLocation.getAbsolutePath());
            return false;
        }
        
        try {
            bitmap.compress(format, quality, fos);
        } finally {
            IOUtils.safeClose(fos);
        }
        
        return true;
    }

}
