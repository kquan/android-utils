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

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;


/**
 * Utilities for working with a content provider
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class ContentProviderUtils {

    /**
     * Insert the operations into a content provider.  All operations must be applied on the same content provider
     * @param context The context to use
     * @param authority The authority of the target content provider
     * @param operations The operations to apply
     * @return An array of {@link ContentProviderResult} that map to each operation
     */
    @NonNull
    public static ContentProviderResult[] applyBatchSynchronously(Context context, String authority, ArrayList<ContentProviderOperation> operations) {
        ContentProviderResult[] results = new ContentProviderResult[0];
        if (context == null) {
            return results;
        }
        if (operations != null && operations.size() > 0) {
            try {
                results = context.getContentResolver().applyBatch(authority, operations);
                for (int i = 0; i < results.length; i++) {
                    ContentProviderResult r = results[i];
                    //ContentProviderOperation operation = operations.get(i);
                    if (r.uri != null) {
                    	// TODO: Report inserts
                    } else if (r.count > 0) {
                		// TODO: Report updates
                    } else {
                        // What should we do about this?
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Could not apply batch operation.", e);
            }
        } else {
            Log.w(TAG, "No operations to process");
        }
        return results;
    }

    /**
     * Shorthand to apply a single {@link ContentProviderOperation} into a content provider
     * @param context The context to use
     * @param authority The authority of the target content provider
     * @param operation The operation to apply
     * @return The result of the operation
     */
    @Nullable
    public static ContentProviderResult applyOperation(Context context, String authority, ContentProviderOperation operation) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        operations.add(operation);
        ContentProviderResult[] results = applyBatchSynchronously(context, authority, operations);
        if (results == null || results.length < 1 || results[0] == null) {
            return null;
        }
        return results[0];
    }

    private static final String TAG = ContentProviderUtils.class.getSimpleName();

}
