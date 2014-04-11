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
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;

/**
 * Listener that reports how far we've scrolled up or down. 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class ScrollDirectionListener implements OnTouchListener {

    public interface OnScrollDirectionListener {
        void onScrollUp(int slop, float distance);
        void onScrollDown(int slop, float distance);
        void onScrollLeft(int slop, float distance);
        void onScrollRight(int slop, float distance);
        void onStartScrolling();
        void onStopScrolling();
        void onFling();
    }
    
    @SuppressWarnings("unused")
    private static final String TAG = ScrollDirectionListener.class.getSimpleName();
    
    protected static final int NO_DOWN_SET = -1;
    
    protected boolean mIgnoreSlop;
    protected int mSwipeSlop;
    protected float mDownY;
    protected float mDownX;
    protected boolean mScrolling;
    
    protected GestureDetector mGestureDetector;
    
    protected OnScrollDirectionListener mListener;
    protected OnTouchListener mOnTouchListener;
    
    public ScrollDirectionListener(Activity activity) {
        mSwipeSlop = ViewConfiguration.get(activity).getScaledTouchSlop();
        mDownX = NO_DOWN_SET;
        mDownY = NO_DOWN_SET;
        mScrolling = false;
        mIgnoreSlop = false;
        mGestureDetector = new GestureDetector(activity, new SimpleOnGestureListener() {                
            @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (mListener != null) {
                    mListener.onFling();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean detectedFling = mGestureDetector.onTouchEvent(event);
        if (detectedFling) {
            // Gesture detector handled the listener
            return false;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (mDownY != NO_DOWN_SET) {
                    break;
                }
                setInitialScrollState(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                clearScrollState();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDownY == NO_DOWN_SET) {
                    // If the view has children that have OnClickListeners, then they will consume the ACTION_DOWN event and we will only get
                    // ACTION_MOVE events.
                    setInitialScrollState(event);
                    break;
                }
                float distanceY = event.getY() - mDownY;
                float distanceX = event.getX() - mDownY;
                if (mListener != null &&
                        (mIgnoreSlop || Math.abs(distanceY) > mSwipeSlop)) {
                    if (!mScrolling && mListener != null) {
                        mScrolling = true;
                        mListener.onStartScrolling();
                    }
                    if (distanceY < 0) {
                        mListener.onScrollDown(mSwipeSlop, distanceY);
                    } else {
                        mListener.onScrollUp(mSwipeSlop, distanceY);
                    }
                }
                if (mListener != null &&
                        (mIgnoreSlop || Math.abs(distanceX) > mSwipeSlop)) {
                    if (!mScrolling && mListener != null) {
                        mScrolling = true;
                        mListener.onStartScrolling();
                    }
                    if (distanceX < 0) {
                        mListener.onScrollRight(mSwipeSlop, distanceX);
                    } else {
                        mListener.onScrollLeft(mSwipeSlop, distanceX);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                clearScrollState();
                break;
        }
        if (mOnTouchListener != null) {
            return mOnTouchListener.onTouch(v, event);
        }
        return false;
    }
    
    protected void setInitialScrollState(MotionEvent event) {
        mDownX = event.getX();
        mDownY = event.getY();
        if (mIgnoreSlop) {
            mScrolling = true;
            if (mListener != null) {
                mListener.onStartScrolling();
            }
        }
    }
    
    protected void clearScrollState() {
        mScrolling = false;
        mDownX = NO_DOWN_SET;
        mDownY = NO_DOWN_SET;
        if (mListener != null) {
            mListener.onStopScrolling();
        }
    }

    public ScrollDirectionListener setOnScrollFeedbackListener(OnScrollDirectionListener listener) {
        mListener = listener;
        return this;
    }

    public ScrollDirectionListener setIgnoreTouchSlop(boolean ignoreSlop) {
        mIgnoreSlop = ignoreSlop;
        return this;
    }

    public ScrollDirectionListener setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
        return this;
    }

}
