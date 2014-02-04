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
package com.kevinquan.android.utils.animation.evaluators;

import android.animation.IntEvaluator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * Evaluator for the animation of the height of a view
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HeightEvaluator extends IntEvaluator {

    @SuppressWarnings("unused")
    private static final String TAG = HeightEvaluator.class.getSimpleName();
    
    protected View mView;
    
    public HeightEvaluator(View targetView) {
        mView = targetView;
    }

    @Override
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        Integer currentValue = (int)(startInt + fraction * (endValue - startInt));
        LayoutParams params = mView.getLayoutParams();
        if (params == null) {
            return currentValue;
        }
        params.height = currentValue;
        mView.setLayoutParams(params);
        return currentValue;
    }  

}
