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
package com.kevinquan.android.ui;

import android.app.Activity;
import android.util.Log;
import android.view.View;

/**
 * Helper class to maintain system UI visibility across activity lifecycle
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class SystemUiVisibilityController {

    private static final String TAG = SystemUiVisibilityController.class.getSimpleName();
    
    protected int mVisibility;
    
    public SystemUiVisibilityController(int visibility) {
        mVisibility = visibility;
    }
    
    /**
     * Apply the desired system UI visibility.  The desired system UI visibility will be restored if a change is detected
     * @param activity The activity to apply the settings to
     * @return
     */
    public SystemUiVisibilityController apply(Activity activity) {
        if (activity == null) {
            Log.w(TAG, "No activity available to apply system visibility to.");
            return this;
        }
        final View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(mVisibility);
        registerListener(activity);
        return this;
    }
    
    /**
     * Register a listener to restore the desired system UI visibility if/when it is changed
     * @param activity The activity to apply the settings to
     * @return
     */
    public SystemUiVisibilityController registerListener(Activity activity) {
        if (activity == null) {
            Log.w(TAG, "No activity available to register system UI visibility listener to.");
            return this;
        }
        final View decorView = activity.getWindow().getDecorView();
        // Based on http://stackoverflow.com/questions/21164836/immersive-mode-navigation-becomes-sticky-after-volume-press-or-minimise-restore
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override public void onSystemUiVisibilityChange(int visibility) {
                //Log.d(TAG, "Re-applying system UI visibility");
                decorView.setSystemUiVisibility(mVisibility);
            }
        });
        return this;
    }

}
