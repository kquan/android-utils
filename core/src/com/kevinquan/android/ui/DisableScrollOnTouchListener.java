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

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * This listener can help programatically prevent scroll when desired.  For example, it may be undesirable to allow scrolling when animating a ListView.
 * 
 * If used with a ListView, it is useful to also disable OnItemClickListener when scroll is disabled, as an attempt to scroll could be registered as
 * a click.
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class DisableScrollOnTouchListener implements OnTouchListener {

    @SuppressWarnings("unused")
    private static final String TAG = DisableScrollOnTouchListener.class.getSimpleName();
    
    protected boolean mIsDisableScroll;
    protected OnTouchListener mOnTouchListener;
    
    public DisableScrollOnTouchListener() {
        mIsDisableScroll = true;
    }
    
    public DisableScrollOnTouchListener(OnTouchListener actualOnTouchListener) {
        mIsDisableScroll = true;
        mOnTouchListener = actualOnTouchListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!mIsDisableScroll && mOnTouchListener != null) {
            return mOnTouchListener.onTouch(v, event);
        } else if (mIsDisableScroll && event.getAction() == MotionEvent.ACTION_MOVE) {
            // Consume scroll event
            return true;
        }
        return false;
    }

    @NonNull
    public DisableScrollOnTouchListener setConsumeScroll(boolean consumeScroll) {
        mIsDisableScroll = consumeScroll;
        return this;
    }

    @NonNull
    public DisableScrollOnTouchListener setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
        return this;
    }

    public boolean isDisablingScroll() {
        return mIsDisableScroll;
    }

}
