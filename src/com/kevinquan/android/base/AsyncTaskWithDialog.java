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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Async task that throws up a progress dialog with the provided message when executing.
 * @author Kevin (kevin.quan@gmail.com)
 *
 */
public abstract class AsyncTaskWithDialog<Params, Progress, Result> extends AsyncTask<Params,Progress,Result> {

    private static final String TAG = AsyncTaskWithDialog.class.getSimpleName();
    
    protected WeakReference<Context> mContextReference;
    protected String mMessage;
    protected ProgressDialog mDialog;
    
    public AsyncTaskWithDialog(Context context, int messageId) {
        this(context, context != null ? context.getString(messageId) : null);
    }
    
    public AsyncTaskWithDialog(Context context, String message) {
        mContextReference = new WeakReference<Context>(context);
        mMessage = message;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = mContextReference.get();
        mDialog = ProgressDialog.show(context, null, mMessage);
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (mDialog != null && mDialog.isShowing()) {
            try {
                mDialog.dismiss();
            } catch (IllegalArgumentException iae) {
                Log.w(TAG, "Dialog could not be dismissed cleanly.",iae);
            }
        }
    }
    
}
