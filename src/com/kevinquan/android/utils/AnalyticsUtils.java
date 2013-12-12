package com.kevinquan.android.utils;

public class AnalyticsUtils {

	@SuppressWarnings("unused")
	private static final String TAG = AnalyticsUtils.class.getSimpleName();

	public static interface BaseAnalytics {
		public static final String CATEGORY_ABOUT_APP = "AboutApp";

		public static final String ACTION_VIEW = "Click";
		public static final String ACTION_EDIT = "Edit";
		public static final String ACTION_ENABLE = "Enable";
		public static final String ACTION_DISABLE = "Disable";
		public static final String ACTION_ERROR = "Error";
		public static final String ACTION_SEARCH = "Search";
		
		public static final String ERROR_NO_NETWORK = "No Network";

		public static final String BUTTON_RATE = "RateApp";
		public static final String BUTTON_REQUESTED_RATE = "RequestedRateApp";
		public static final String BUTTON_CHANGELOG = "Changelog";
		public static final String BUTTON_LICENSES = "OpenSourceLicenses";
		public static final String BUTTON_AUTHOR_WEBSITE = "AuthorWebsite";

		public static final long VALUE_TRUE = 1;
		public static final long VALUE_FALSE = 0;
	}
}
