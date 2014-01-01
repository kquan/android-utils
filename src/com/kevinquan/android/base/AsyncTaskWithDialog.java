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
