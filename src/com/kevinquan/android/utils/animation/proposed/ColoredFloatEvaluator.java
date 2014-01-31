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
package com.kevinquan.android.utils.animation.proposed;

import java.lang.ref.WeakReference;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.TextView;

/**
 * Evaluates an animation of a float between two values and two color values
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ColoredFloatEvaluator extends FloatEvaluator {

    @SuppressWarnings("unused")
    private static final String TAG = ColoredFloatEvaluator.class.getSimpleName();
    
    public static final int DEFAULT_DECIMAL_PLACES = -1;
    protected static final int NO_COLOR = 0;
    protected static final int NO_STRING_RESOURCE = -1;

    protected WeakReference<TextView> mViewToUpdateReference;
    
    protected int mStringResource;
    
    protected int mDecimalPlaces;
    
    protected int mStartColor;
    protected int mEndColor;
    protected ArgbEvaluator mColorEvaluator;
    
    public ColoredFloatEvaluator(TextView viewToUpdate) {
        mViewToUpdateReference = new WeakReference<TextView>(viewToUpdate);
        mColorEvaluator = new ArgbEvaluator();
        mStartColor = NO_COLOR;
        mEndColor = NO_COLOR;
        mDecimalPlaces = DEFAULT_DECIMAL_PLACES;
    }
    
    /**
     * Set the start and end color of the color animation
     * @param startColor The start color
     * @param endColor The end color
     * @return This object for chaining
     */
    public ColoredFloatEvaluator setColorAnimation(int startColor, int endColor) {
        mStartColor = startColor;
        mEndColor = endColor;
        return this;
    }
    
    /**
     * Sets the number of decimal places that should be displayed for the float
     * @param decimalPlaces The number of decimal places
     * @return This object for chaining
     */
    public ColoredFloatEvaluator setDecimalPlaces(int decimalPlaces) {
        mDecimalPlaces = decimalPlaces;
        return this;
    }
    
    /**
     * Sets the string resource where the float value will be inserted.  The float will be inserted in the first 
     * position, and as a string (as it is formatted to the desired number of decimal places)
     * @param resourceId The string resouce id
     * @return This object for chaining
     */
    public ColoredFloatEvaluator setStringResource(int resourceId) {
        mStringResource = resourceId;
        return this;
    }
    
    @Override
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        Float currentValue = startValue.floatValue() + fraction * (endValue.floatValue() - startValue.floatValue());
        TextView viewToUpdate = mViewToUpdateReference.get();
        if (viewToUpdate == null) {
            return currentValue;
        }
        
        String floatAsString = null;
        if (mDecimalPlaces != DEFAULT_DECIMAL_PLACES) {
            floatAsString = String.format("%."+mDecimalPlaces+"f", currentValue);
        } else {
            floatAsString = String.valueOf(currentValue);
        }
        
        populateString(viewToUpdate, floatAsString);
        if (mStartColor != mEndColor) {
            Integer newColor = (Integer)mColorEvaluator.evaluate(fraction, mStartColor, mEndColor);
            viewToUpdate.setTextColor(newColor);
        }
        return currentValue;
    }
    
    protected void populateString(TextView view, String value) {
        if (getStringResource() != NO_STRING_RESOURCE) {
            view.setText(view.getContext().getString(getStringResource(), value));
        } else {
            view.setText(value);
        }
    }
    
    protected int getStringResource() {
        return NO_STRING_RESOURCE;
    }

}

