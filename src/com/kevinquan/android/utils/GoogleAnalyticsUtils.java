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

/**
 * Utilities for Google Analytics
 * @author Kevin (kevin.quan@gmail.com)
 *
 */
public class GoogleAnalyticsUtils {

	@SuppressWarnings("unused")
	private static final String TAG = GoogleAnalyticsUtils.class.getSimpleName();

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
