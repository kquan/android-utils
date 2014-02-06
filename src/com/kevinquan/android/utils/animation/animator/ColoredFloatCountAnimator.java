package com.kevinquan.android.utils.animation.animator;

import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.TextView;
import com.kevinquan.android.constants.ViewConstants;
import com.kevinquan.android.utils.animation.animator.NumberCountAnimator.FloatCountAnimator;
import com.kevinquan.utils.PrimitiveUtils;

/**
 * Counts a float number and changes the color between two values as the float is being counted.
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ColoredFloatCountAnimator extends FloatCountAnimator {

    @SuppressWarnings("unused")
    private static final String TAG = ColoredFloatCountAnimator.class.getSimpleName();
    
    protected static final String PROPERTY_VALUE_COUNT = "count";
    
    protected Integer mStartColor;
    protected Integer mEndColor;
    
    public ColoredFloatCountAnimator(TextView view, int decimalsToDisplay, int startColor, int endColor, Float... values) {
        super(view, decimalsToDisplay, values);
        mStartColor = startColor;
        mEndColor = endColor;
        // Init again to set color property
        init(values);
    }

    protected void init(Float... values) {
        if (mStartColor == null || mEndColor == null) {
            return;
        }
        PropertyValuesHolder countProperty = PropertyValuesHolder.ofFloat(PROPERTY_VALUE_COUNT, PrimitiveUtils.asPrimitive((Float[])values));
        PropertyValuesHolder colorProperty = PropertyValuesHolder.ofObject(ViewConstants.PROPERTY_TEXT_COLOR, new ArgbEvaluator(), mStartColor, mEndColor);
        setValues(countProperty, colorProperty);
    }
    
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        super.onAnimationUpdate(animation);
        mView.setTextColor((Integer)animation.getAnimatedValue(ViewConstants.PROPERTY_TEXT_COLOR));
    }
    
    protected Float getCurrentCountValue(ValueAnimator animation) {
        return (Float)animation.getAnimatedValue(PROPERTY_VALUE_COUNT);
    }
}
