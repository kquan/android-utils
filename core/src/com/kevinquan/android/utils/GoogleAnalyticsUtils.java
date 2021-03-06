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
        public static final String CATEGORY_API = "API";
        public static final String CATEGORY_ERRORS = "Errors";
		public static final String CATEGORY_NAVIGATION = "Navigation";
		public static final String CATEGORY_NOTIFICATIONS = "Notifications";
        public static final String CATEGORY_PREFERENCES = "Preferences";

        public static final String ACTION_BACK_PRESSED = "BackButtonPress";
        public static final String ACTION_BROADCAST_RECEIVED = "BroadcastReceived";
		public static final String ACTION_BUTTON_PRESS = "ButtonPress";
		public static final String ACTION_CANCEL = "Cancel";
        public static final String ACTION_DISABLE = "Disable";
        public static final String ACTION_EDIT = "Edit";
        public static final String ACTION_ENABLE = "Enable";
		public static final String ACTION_ERROR = "Error";
        public static final String ACTION_ORIENTATION = "Orientation";
        public static final String ACTION_LONG_PRESS = "LongPress";
		public static final String ACTION_NOTIFICATION = "Notification";
        public static final String ACTION_SEARCH = "Search";
        public static final String ACTION_SELECTION = "Select";
        public static final String ACTION_SCHEDULED = "ScheduledAction";
        public static final String ACTION_SLIDE = "Slide";
        public static final String ACTION_SWIPE = "Swipe";
        public static final String ACTION_TAP = "Tap";
        public static final String ACTION_CLICK = ACTION_TAP;
        public static final String ACTION_UPGRADE_CHECK = "UpgradeCheck";
        public static final String ACTION_VIEW = "View";

		public static final String ERROR_NO_NETWORK = "No Network";

		public static final String ORIENTATION_LANDSCAPE = "Landscape";
		public static final String ORIENTATION_PORTRAIT = "Portrait";

		public static final String BUTTON_ABOUT_APP = "AboutApp";
        public static final String BUTTON_AUTHOR_WEBSITE = "AuthorWebsite";
        public static final String BUTTON_CHANGELOG = "Changelog";
        public static final String BUTTON_LICENSES = "OpenSourceLicenses";
		public static final String BUTTON_RATE = "RateApp";
		public static final String BUTTON_REQUESTED_RATE = "RequestedRateApp";

		public static final long VALUE_TRUE = 1;
		public static final long VALUE_FALSE = 0;
	}
}
