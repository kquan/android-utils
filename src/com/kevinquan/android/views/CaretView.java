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
package com.kevinquan.android.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.kevinquan.android.utils.DeviceUtils;
import com.kevinquan.android.utils.R;

/**
 * View that draws a caret.
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class CaretView extends View {

    protected enum Orientation {
        NoRotation,
        rotation90,
        rotation180,
        rotation270,
        ;
        
        public static Orientation fromOrdinal(int ordinal) {
            if (ordinal < 0 || ordinal > values().length) {
                return NoRotation;
            }
            return values()[ordinal];
        }
    }
    
    protected enum Style {
        Outline(Paint.Style.STROKE),
        Filled(Paint.Style.FILL),
        ;
        
        public static Style fromOrdinal(int ordinal) {
            if (ordinal < 0 || ordinal > values().length) {
                return Outline;
            }
            return values()[ordinal];
        }
        
        protected Paint.Style mPaintStyle;
        
        private Style(Paint.Style paintStyle) {
            mPaintStyle = paintStyle;
        }
        
        public Paint.Style getPaintStyle() {
            return mPaintStyle;
        }
    }
    
    @SuppressWarnings("unused")
    private static final String TAG = CaretView.class.getSimpleName();
    
    protected static final int DEFAULT_THICKNESS_IN_DP = 1;
    
    protected int mColor;
    protected Orientation mOrientation;
    protected float mThickness;
    protected Style mStyle;
    
    protected Paint mPaint;
    protected PointF mStartPoint;
    protected PointF mMidPoint;
    protected PointF mEndPoint;
    protected Path mPath;

    public CaretView(Context context) {
        super(context);
        init(null);
    }
    
    public CaretView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    
    public CaretView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }    
    
    /**
     * Gets the color that the caret will be drawn with
     * @return The color of the caret
     */
    public int getColor() {
        return mColor;
    }
    
    /**
     * Gets the orientation that the caret will be drawn with
     * @return The orientation of the caret
     */
    public Orientation getOrientation() {
        return mOrientation;
    }

    /**
     * Gets the style that the caret will be drawn with
     * @return The style of the caret
     */
    public Style getStyle() {
        return mStyle;
    }

    /**
     * Gets the thickness that the caret will be drawn with if the style is outline
     * @return The thickness of the caret
     */
    public float getThickness() {
        return mThickness;
    }

    protected void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CaretView);
            mColor = a.getInt(R.styleable.CaretView_color, getContext().getResources().getColor(android.R.color.primary_text_light));
            mOrientation = Orientation.fromOrdinal(a.getInt(R.styleable.CaretView_orientation, Orientation.NoRotation.ordinal()));
            mThickness = a.getDimensionPixelSize(R.styleable.CaretView_thickness, DeviceUtils.convertDpToPixels(getContext(), DEFAULT_THICKNESS_IN_DP));
            mStyle = Style.fromOrdinal(a.getInt(R.styleable.CaretView_style, Style.Outline.ordinal()));
            a.recycle();
        } else {
            mColor = getContext().getResources().getColor(android.R.color.primary_text_light);
            mOrientation = Orientation.NoRotation;
            mThickness = DeviceUtils.convertDpToPixels(getContext(), DEFAULT_THICKNESS_IN_DP);
            mStyle = Style.Outline;
        }
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mThickness);
        mPaint.setStyle(mStyle.getPaintStyle());
        
        mStartPoint = new PointF();
        mMidPoint = new PointF();
        mEndPoint = new PointF();
        
        mPath = new Path();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LayoutParams params = getLayoutParams();
        if (params == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        // Take user provided parameters, but if they don't exist then measure.
        int width = -1;
        if (params.width != LayoutParams.FILL_PARENT && params.width != LayoutParams.MATCH_PARENT && params.width != LayoutParams.WRAP_CONTENT) {
            width = resolveSize(params.width, widthMeasureSpec);
        }
        int height = -1;
        if (params.height != LayoutParams.FILL_PARENT && params.height!= LayoutParams.MATCH_PARENT && params.height!= LayoutParams.WRAP_CONTENT) {
            height = resolveSize(params.height, heightMeasureSpec);
        }
        super.onMeasure(width == -1 ? widthMeasureSpec : MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                        height== -1 ? heightMeasureSpec : MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        
        return;
    }

    protected void onDraw(Canvas canvas) {
        canvas.save();
        
        switch (mOrientation) {
            case NoRotation:
                mStartPoint.x = getPaddingLeft();
                mStartPoint.y = getHeight() - getPaddingBottom();
                mMidPoint.x = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight())/2;
                mMidPoint.y = getPaddingTop();
                mEndPoint.x = getWidth() - getPaddingRight();
                mEndPoint.y = mStartPoint.y;
                break;
            case rotation90:
                mStartPoint.x = getPaddingLeft();
                mStartPoint.y = getPaddingTop();
                mMidPoint.x = getWidth() - getPaddingRight();
                mMidPoint.y = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom())/2;
                mEndPoint.x = mStartPoint.x;
                mEndPoint.y = getHeight() - getPaddingBottom();
                break;
            case rotation180:
                mStartPoint.x = getPaddingLeft();
                mStartPoint.y = getPaddingTop();
                mMidPoint.x = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight())/2;
                mMidPoint.y = getHeight() - getPaddingBottom();
                mEndPoint.x = getWidth() - getPaddingRight();
                mEndPoint.y = mStartPoint.y;
                break;
            case rotation270:
                mStartPoint.x = getWidth() - getPaddingRight();
                mStartPoint.y = getPaddingTop();
                mMidPoint.x = getPaddingLeft();
                mMidPoint.y = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom())/2;
                mEndPoint.x = mStartPoint.x;
                mEndPoint.y = getHeight() - getPaddingBottom();
                break;
        }

        mPath.moveTo(mStartPoint.x, mStartPoint.y);
        mPath.lineTo(mMidPoint.x, mMidPoint.y);
        mPath.lineTo(mEndPoint.x, mEndPoint.y);
        if (mStyle == Style.Filled) {
            mPath.close();
        }
        canvas.drawPath(mPath, mPaint);
        
        canvas.restore();
    }

    /**
     * Sets the color that the caret will be drawn with
     * @param color The new color of the caret
     * @return This view for chaining
     */
    public CaretView setColor(int color) {
        mColor = color;
        mPaint.setColor(mColor);
        return this;
    }

    /**
     * Sets the orientation that the caret will be drawn with
     * @param color The new orientation of the caret
     * @return This view for chaining
     */
    public CaretView setOrientation(Orientation orientation) {
        mOrientation = orientation;
        return this;
    }

    /**
     * Sets the style that the caret will be drawn with
     * @param color The new style of the caret
     * @return This view for chaining
     */
    public CaretView setStyle(Style style) {
        mStyle = style;
        mPaint.setStyle(mStyle.getPaintStyle());
        return this;
    }

    /**
     * Sets the thickness that the caret will be drawn with.  
     * The thickness is meaningless if the caret style is set to filled
     * 
     * @param color The new thickness of the caret
     * @return This view for chaining
     */
    public CaretView setThickness(float thickness) {
        mThickness = thickness;
        mPaint.setStrokeWidth(mThickness);
        return this;
    }

}
