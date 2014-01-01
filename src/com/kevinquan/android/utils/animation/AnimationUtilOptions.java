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
package com.kevinquan.android.utils.animation;

import java.util.Hashtable;

import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Convenience class to house animation options when passed to animation utilities 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class AnimationUtilOptions {

    private static final String TAG = AnimationUtilOptions.class.getSimpleName();
    
    public static class Builder {
        
        protected static final Interpolator DEFAULT_INTERPOLATOR = new LinearInterpolator();
        protected static final long DEFAULT_DURATION = android.R.integer.config_shortAnimTime;
        protected static final long DEFAULT_START_DELAY = 0;
        protected static final boolean DEFAULT_START_IMMEDIATELY = false;
        
        protected Interpolator mInterpolator;
        protected long mDurationInMillis;
        protected long mStartDelayInMillis;
        protected boolean mStartImmediately;
        
        protected Hashtable<String, Object> mExtendedOptions;
        
        public Builder() {
            mInterpolator = DEFAULT_INTERPOLATOR;
            mDurationInMillis = DEFAULT_DURATION;
            mStartDelayInMillis = DEFAULT_START_DELAY;
            mStartImmediately = DEFAULT_START_IMMEDIATELY;
            mExtendedOptions = new Hashtable<String, Object>();
        }
        
        public AnimationUtilOptions build() {
            AnimationUtilOptions options = new AnimationUtilOptions(mInterpolator, mDurationInMillis, mStartDelayInMillis, mStartImmediately, mExtendedOptions);
            return options;
        }

        public Builder setInterpolator(Interpolator interpolator) {
            mInterpolator = interpolator;
            return this;
        }

        public Builder setDuration(long durationInMillis) {
            mDurationInMillis = durationInMillis;
            return this;
        }

        public Builder setStartDelay(long startDelayInMillis) {
            mStartDelayInMillis = startDelayInMillis;
            return this;
        }

        public Builder setStart(boolean startImmediately) {
            mStartImmediately = startImmediately;
            return this;
        }
        
        public Builder addOption(String key, Object value) {
            mExtendedOptions.put(key, value);
            return this;
        }
    }
    
    protected Interpolator mInterpolator;
    protected long mDurationInMillis;
    protected long mStartDelayInMillis;
    protected boolean mStartImmediately;
    protected Hashtable<String, Object> mExtendedOptions;
    
    protected AnimationUtilOptions(Interpolator interpolator, long duration, long delay, boolean startImmediately, Hashtable<String,Object> extendedOptions) {
        mInterpolator = interpolator;
        mDurationInMillis = duration;
        mStartDelayInMillis = delay;
        mStartImmediately = startImmediately;
        mExtendedOptions = extendedOptions;
    }

    public Interpolator getInterpolator() {
        return mInterpolator;
    }

    public long getDuration() {
        return mDurationInMillis;
    }

    public long getStartDelay() {
        return mStartDelayInMillis;
    }

    public boolean shouldStartImmediately() {
        return mStartImmediately;
    }
    
    public Object getOption(String key) {
        return mExtendedOptions.get(key);
    }
    
    public int getIntOption(String key, int defaultValue) {
        if (!mExtendedOptions.containsKey(key)) {
            return defaultValue;
        }
        try {
            return (Integer)mExtendedOptions.get(key);
        } catch (Exception e) {
            Log.w(TAG, "Could not retrieve int value for "+key+" from options");
            return defaultValue;
        }
    }

    public float getFloatOption(String key, float defaultValue) {
        if (!mExtendedOptions.containsKey(key)) {
            return defaultValue;
        }
        try {
            return (Float)mExtendedOptions.get(key);
        } catch (Exception e) {
            Log.w(TAG, "Could not retrieve float value for "+key+" from options");
            return defaultValue;
        }
    }
}
