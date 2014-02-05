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
package com.kevinquan.android.utils.animation.animator;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

/**
 * Evaluator for the animation of layout parameter attributes
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class LayoutParamsAnimator implements AnimatorUpdateListener {

    @SuppressWarnings("unused")
    private static final String TAG = LayoutParamsAnimator.class.getSimpleName();
    
    protected View mView;
    protected ValueAnimator mAnimator;
    
    public LayoutParamsAnimator(View targetView, int... values) {
        mView = targetView;
        mAnimator = getValueAnimator(values);
        mAnimator.addUpdateListener(this);
    }
    
    protected ValueAnimator getValueAnimator(int... values) {
        return ValueAnimator.ofInt(values);
    }
    
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        LayoutParams params = mView.getLayoutParams();
        if (params == null) {
            params = getDefaultLayoutParams();
        }
        updateLayoutParams(params, animation);
        mView.setLayoutParams(params);
    }
    
    protected abstract void updateLayoutParams(LayoutParams params, ValueAnimator animation);
    
    protected LayoutParams getDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
    
    /**
     * Evaluator for the animation of the height of a view
     * 
     * @author Kevin Quan (kevin.quan@gmail.com)
     *
     */
    public static class HeightAnimator extends LayoutParamsAnimator{

        public HeightAnimator(View targetView, int[] values) {
            super(targetView, values);
        }
        
        protected void updateLayoutParams(LayoutParams params, ValueAnimator animation) {
            params.height = (Integer)animation.getAnimatedValue();
        }
    }
    
    /**
     * Evaluator for the animation of the width of a view
     * 
     * @author Kevin Quan (kevin.quan@gmail.com)
     *
     */
    public static class WidthAnimator extends LayoutParamsAnimator{

        public WidthAnimator(View targetView, int[] values) {
            super(targetView, values);
        }
        
        protected void updateLayoutParams(LayoutParams params, ValueAnimator animation) {
            params.width = (Integer)animation.getAnimatedValue();
        }
    }
} 
