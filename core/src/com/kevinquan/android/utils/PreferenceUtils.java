/*
 * Copyright 2013 Kevin Quan (kevin.quan@gmail.com)
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
package com.kevinquan.android.utils;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.List;

/**
 * Collection of utilities related to preferences
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class PreferenceUtils {

	/**
	 * Constants to be used for preferences (such as preference keys or preference values)
	 * @author Kevin Quan (kevin.quan@gmail.com)
	 *
	 */
	public interface BasePreferences {
		public static final String RATEAPP = "RateApp"; //$NON-NLS-1$
		public static final String HAS_RATED = "HasRatedApp"; //$NON-NLS-1$
	    public static final String SHOULD_PROMPT_TO_RATEAPP = "HasPromptedToRateApp"; //$NON-NLS-1$
		
		public static final String CHANGELOG = "ShowChangelog"; //$NON-NLS-1$
		public static final String CHANGELOG_PATH = "file:///android_asset/changelog.html"; //$NON-NLS-1$
		public static final String LICENSES = "ShowLicenses"; //$NON-NLS-1$
		public static final String LICENSES_PATH = "file:///android_asset/licenses.html"; //$NON-NLS-1$
		public static final String ABOUT_AUTHOR = "AboutAuthor"; //$NON-NLS-1$
		public static final String ABOUT_AUTHOR_WEBSITE = "http://www.kevinquan.com/"; //$NON-NLS-1$	
	}

	@SuppressWarnings("unused")
	private static final String TAG = PreferenceUtils.class.getSimpleName();

	/**
	 * Checks whether the current device can open an Google Play intent.  If it can, then an intent to open 
	 * the current app will be returned
	 * @param context The context that can identify the current app
	 * @return An intent to open the current app in Google Play if the device supports it, otherwise null
	 */
    @Nullable
	public static Intent canHandleGooglePlayIntent(Context context) {
	    if (context == null) return null;
        Intent rateIntent = getGooglePlayIntent(context);
        // Don't show rate-my-app if no Google Play installed
        List<ResolveInfo> marketHandlers = context.getPackageManager().queryIntentActivities(rateIntent, 0);
        if (marketHandlers == null || marketHandlers.isEmpty()) {
        	return null;
        }
        return rateIntent;
	}
	
	/**
	 * Retrieves an intent to open the app that provided the context in Google Play
	 * @param context The context that can identify the current app
	 * @return An intent that can be used to open Google Play, or null if the intent cannot be created. 
	 */
    @Nullable
	public static Intent getGooglePlayIntent(Context context) {
	    if (context == null) return null;
		Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+context.getPackageName()));
        // Make sure back button comes back to app
        rateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        return rateIntent;
	}
	
	/**
	 * Opens a URI in a dialog 
	 * @param context The context to be used to open the dialog
	 * @param title The title of the dialog
	 * @param uri The URI to open
	 */
	public static void openDialogForLocalUri(Context context, String title, String uri) {
	    if (context == null || TextUtils.isEmpty(uri)) {
	        return;
	    }
        final WebView webView = new WebView(context);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setView(webView);
        builder.setPositiveButton(android.R.string.ok, null);
        
        webView.setWebViewClient(new WebViewClient() { 
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                builder.show();
            }
        });
        webView.loadUrl(uri);
	}
	
	/**
	 * Attempts to show a URL using the Android ACTION_VIEW intent (i.e., shows in device's browser)
	 * @param context The context needed to show a URL
	 * @param url The URL to show
	 */
	public static void showWebsite(Context context, String url) {
	    if (context == null || TextUtils.isEmpty(url)) return;
    	Intent showMyWebsite = new Intent(Intent.ACTION_VIEW);
        // Make sure back button comes back to app
    	showMyWebsite.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
    	showMyWebsite.setData(Uri.parse(url));
    	context.startActivity(showMyWebsite);
	}
	
    /**
     * Retrieve preferences but check whether the preference file has been updated by another process before loading
     * @param context The context needed to load preferences
     * @param preferenceFile The preference file to load
     * @return The shared preferences
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @NonNull
    public static SharedPreferences getMultiProcessAwarePreferences(Context context, String preferenceFile) {
        if (BuildUtils.isHoneycombOrGreater()) {
            return context.getSharedPreferences(preferenceFile, Context.MODE_MULTI_PROCESS);
        } else {
            // In Gingerbread and below, Context.MODE_MULTI_PROCESS is by default on
            return context.getSharedPreferences(preferenceFile, 0);
        }
    }
}
