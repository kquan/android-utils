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
package com.kevinquan.android.base;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Async task that holds a weak reference to the context
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public abstract class AsyncTaskWithContext<Params, Progress, Result> extends AsyncTask<Params,Progress,Result> {

	@SuppressWarnings("unused")
	private static final String TAG = AsyncTaskWithContext.class.getSimpleName();

    protected WeakReference<? extends Context> mContextReference;
    
    public AsyncTaskWithContext(Context context) {
        mContextReference = new WeakReference<Context>(context);
    }
	
}
