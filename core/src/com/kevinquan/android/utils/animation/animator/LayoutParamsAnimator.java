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

import com.kevinquan.utils.PrimitiveUtils;

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
public abstract class LayoutParamsAnimator<E> extends ValueAnimator implements AnimatorUpdateListener {

    @SuppressWarnings("unused")
    private static final String TAG = LayoutParamsAnimator.class.getSimpleName();
    
    protected View mView;
    
    public LayoutParamsAnimator(View targetView, E... values) {
        mView = targetView;
        init(values);
        addUpdateListener(this);
    }
    
    protected void init(E... values) {
        if (values instanceof Integer[]) {
            setIntValues(PrimitiveUtils.asPrimitive((Integer[])values));
        } else if (values instanceof Float[]) {
            setFloatValues(PrimitiveUtils.asPrimitive((Float[])values));            
        }
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
    public static class HeightAnimator extends LayoutParamsAnimator<Integer> {

        public HeightAnimator(View targetView, Integer... values) {
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
    public static class WidthAnimator extends LayoutParamsAnimator<Integer> {

        public WidthAnimator(View targetView, Integer... values) {
            super(targetView, values);
        }
        
        protected void updateLayoutParams(LayoutParams params, ValueAnimator animation) {
            params.width = (Integer)animation.getAnimatedValue();
        }
    }
} 
