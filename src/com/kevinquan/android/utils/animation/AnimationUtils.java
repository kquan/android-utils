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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.kevinquan.android.utils.BuildUtils;

/**
 * Collection of animation utilities
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class AnimationUtils {

    private static final String TAG = AnimationUtils.class.getSimpleName();
    
    public static final String VIEW_FINAL_VISIBILITY = "ViewFinalVisibility";
    public static final String VIEW_FINAL_ALPHA = "ViewFinalAlpha";
    public static final String VIEW_INITIAL_ALPHA = "ViewInitialAlpha";
    
    /**
     * On API12 or higher, fades out a view by animating the alpha to 0, and then sets the 
     * visibility to the provided end state.  On API12 or lower, it will just set the view's
     * visibility to the provided end state immediately.
     * 
     *  Options should include values for:
     *  VIEW_FINAL_VISIBILITY ({@link View.GONE} or {@link View.INVISIBLE}, default to GONE) 
     *  VIEW_FINAL_ALPHA ([0..1f], default to 0f)
     *  VIEW_INITIAL_ALPHA ([0..1f], default to 1f)
     * 
     * @param view The view to fade out
     * @param options The options that govern the animation
     * @return The animator if animation can happen, or null otherwise
     */
    @SuppressLint("NewApi")
    public static ViewPropertyAnimator fadeOutView(final View view, AnimationUtilOptions options) {
        if (view.getVisibility() != View.VISIBLE) {
            Log.i(TAG, "No fade out performed as view is already not visible.");
            return null;
        }
        if (options == null) {
            Log.i(TAG, "No options provided, using defaults.");
            options = new AnimationUtilOptions.Builder().build();
        }
        int viewEndState = options.getIntOption(VIEW_FINAL_VISIBILITY, View.GONE);
        final float initialAlpha = options.getFloatOption(VIEW_INITIAL_ALPHA, 1f);
        float finalAlpha = options.getFloatOption(VIEW_FINAL_ALPHA, 0f);
        if (viewEndState != View.GONE && viewEndState != View.INVISIBLE) {
            Log.w(TAG, "Setting to end state to GONE as input was unexpected: "+ viewEndState);
            viewEndState = View.GONE;
        }
        if (BuildUtils.isHoneycombMR1OrGreater()) {
            final int finalViewEndState = viewEndState;
            ViewPropertyAnimator animator = view.animate()
                                                .alpha(finalAlpha)
                                                .setDuration(options.getDuration())
                                                .setInterpolator(options.getInterpolator())
                                                .setStartDelay(options.getStartDelay());
            animator.setListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                    view.setVisibility(finalViewEndState);
                    view.setAlpha(initialAlpha);
                }
            });
            if (options.shouldStartImmediately()) {
                animator.start();
            }
            return animator;
        } else {
            view.setVisibility(viewEndState);
        }
        return null;
    }

}
