package com.kevinquan.android.utils;

import java.lang.reflect.Field;

import android.text.TextUtils;
import android.util.Log;

/**
 * A collection of utilities using reflection
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class ReflectionUtils {

    private static final String TAG = ReflectionUtils.class.getSimpleName();

    /**
     * Retrieve an inaccessible field (i.e., private, protected, default visibility) from a class.
     * @param object The object to retrieve the field from
     * @param fieldName The name of the field
     * @return The contents of the field
     */
    public static Object getHiddenField(Object object, String fieldName) {
        if (TextUtils.isEmpty(fieldName)) {
            return null;
        }
        Class<? extends Object> objectClass = object.getClass();
        try {
            Field hiddenField = null;
            boolean terminate = false;
            while (!terminate) {
                try {
                    // Will throw exception if not found
                    hiddenField = objectClass.getDeclaredField(fieldName);
                    terminate = true;
                } catch (NoSuchFieldException nsfe) {
                    if (objectClass.getSuperclass() != null) {
                        // Check in parent class
                        objectClass = objectClass.getSuperclass();
                    } else {
                        // Terminate condition
                        terminate = true;
                    }
                }
            }
            if (hiddenField != null) {
                hiddenField.setAccessible(true);
                return hiddenField.get(object);
            }
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve hidden field: "+fieldName+" in object of class "+object.getClass() +" or its parent classes", e);
        }
        return null;
    }
}
