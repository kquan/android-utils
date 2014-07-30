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
package com.kevinquan.utils;

/**
 * Helpers to work with primitive types
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class PrimitiveUtils {

    @SuppressWarnings("unused")
    private static final String TAG = PrimitiveUtils.class.getSimpleName();
    
    /**
     * Convert an array of {@link Integer} objects into an equivalent array of primitive ints
     * @param values The array of Integers to convert
     * @return An array of ints
     */
    public static int[] asPrimitive(Integer[] values) {
        if (values == null || values.length == 0) {
            return new int[] {};
        }
        int[] primitiveValues = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            primitiveValues[i] = (Integer)values[i]; 
        }
        return primitiveValues;
    }
    
    /**
     * Convert an array of {@link Float} objects into an equivalent array of primitive floats
     * @param values The array of Floats to convert
     * @return An array of floats
     */
    public static float[] asPrimitive(Float[] values) {
        if (values == null || values.length == 0) {
            return new float[] {};
        }
        float[] primitiveValues = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            primitiveValues[i] = (Float)values[i]; 
        }
        return primitiveValues;
    }

}
