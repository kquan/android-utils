package com.kevinquan.android.utils;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PreferenceUtils {

	@SuppressWarnings("unused")
	private static final String TAG = PreferenceUtils.class.getSimpleName();
	
	public interface BasePreferences {
		public static final String RATEAPP = "RateApp"; //$NON-NLS-1$
		public static final String CHANGELOG = "ShowChangelog"; //$NON-NLS-1$
		public static final String CHANGELOG_PATH = "file:///android_asset/changelog.html";
		public static final String ABOUT_AUTHOR = "AboutAuthor"; //$NON-NLS-1$
		public static final String ABOUT_AUTHOR_WEBSITE = "http://www.kevinquan.com/"; //$NON-NLS-1$	
	}

	public static Intent getGooglePlayIntent(Context context) {
		Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+context.getPackageName()));
        // Make sure back button comes back to app
        rateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        return rateIntent;
	}
	
	public static Intent canHandleGooglePlayIntent(Context context) {
        Intent rateIntent = getGooglePlayIntent(context);
        // Don't show rate-my-app if no Google Play installed
        List<ResolveInfo> marketHandlers = context.getPackageManager().queryIntentActivities(rateIntent, 0);
        if (marketHandlers == null || marketHandlers.isEmpty()) {
        	return null;
        }
        return rateIntent;
	}
	
	public static void openDialogForLocalUri(Context context, String title, String uri) {
        final WebView webView = new WebView(context);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
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
	
	public static void showWebsite(Context context, String url) {
    	Intent showMyWebsite = new Intent(Intent.ACTION_VIEW);
        // Make sure back button comes back to app
    	showMyWebsite.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
    	showMyWebsite.setData(Uri.parse(url));
    	context.startActivity(showMyWebsite);
	}
}
