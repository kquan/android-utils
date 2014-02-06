package com.kevinquan.android.utils.animation.animator;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.TextView;

import com.kevinquan.utils.MathUtils;
import com.kevinquan.utils.PrimitiveUtils;

/**
 * Animator that animates the counting of values from the start to the end values on a TextView
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 * @param <E> One of {Integer or Float}
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NumberCountAnimator<E> extends ValueAnimator implements AnimatorUpdateListener {

    @SuppressWarnings("unused")
    private static final String TAG = NumberCountAnimator.class.getSimpleName();
    
    protected TextView mView;
    
    public NumberCountAnimator(TextView view, E... values) {
        mView = view;
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
        E value = getCurrentCountValue(animation);
        setText(value);
    }
    
    @SuppressWarnings("unchecked")
    protected E getCurrentCountValue(ValueAnimator animation) {
        return (E)animation.getAnimatedValue();
    }
    
    protected void setText(E value) {
        mView.setText(String.valueOf(value));
    }
    
    /**
     * Animates the counting of an integer and displays in a TextView
     * @author Kevin Quan (kevin.quan@gmail.com)
     *
     */
    public static class IntCountAnimator extends NumberCountAnimator<Integer> {
        public IntCountAnimator(TextView view, Integer... values) {
            super(view, values);
        }
    }
    
    /**
     * Animates the counting of a float to the required decimal places and displays in a TextView
     * @author Kevin Quan (kevin.quan@gmail.com)
     *
     */
    public static class FloatCountAnimator extends NumberCountAnimator<Float> {

        protected int mDecimalsToDisplay;
        
        public FloatCountAnimator(TextView view, int decimalsToDisplay, Float... values) {
            super(view, values);
            mDecimalsToDisplay = decimalsToDisplay;
        }

        protected void setText(Float value) {
            value = MathUtils.round(value, mDecimalsToDisplay);
            super.setText(value);
        }
    }

}
