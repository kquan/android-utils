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
package com.kevinquan.android.profile;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.kevinquan.android.utils.CursorUtils;
import com.kevinquan.android.utils.DeviceUtils;
import com.kevinquan.android.utils.DeviceUtils.Permissions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

/**
 * A collection of utilities that work with the user's profile on their phone
 *
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class ProfileUtils {

	/**
	 * Dump all profiles for the user into the log for debugging
	 * @param context The context to use
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void debug_dumpProfile(Context context) {
		if (context == null) {
			return;
		}
        if (!DeviceUtils.hasPermission(context, Permissions.READ_PROFILE)) {
        	Log.w(TAG, "App does not have permission to retrieve profile");
        	return;
        }
        Cursor profile = null;
        try {
            profile = getProfile(context);
            if (CursorUtils.hasResults(profile)) {
                Log.d(TAG, "There are "+profile.getCount()+" entries.");
                do {
                    for (int i = 0; i < profile.getColumnCount(); i++) {
                        String value = "not_a_string";
                        try {
                            value = profile.getString(i);
                        } catch (Exception e) {
                            // Squelch
                        }
                        Log.d(TAG, "Column " + i + ": " + profile.getColumnName(i) + " has value " + value);
                    }
                } while (profile.moveToNext());
            }
        } finally {
            CursorUtils.safeClose(profile);
        }
    }

	/**
	 * Retrieve the display name for the user
	 * @param context The context to use
	 * @return The user name or null/empty.
	 */
    @Nullable
	public static String getDisplayName(Context context) {
		if (!DeviceUtils.hasPermission(context, Permissions.READ_PROFILE)) {
			Log.w(TAG, "App does not have permission to retrieve profile");
			return new String();
		}
		Cursor profile = null;
		try {
			profile = getProfile(context);
			if (!CursorUtils.hasResults(profile)) {
				return null;
			}
			String displayName = null;
			do {
				displayName = CursorUtils.safeGetString(profile, "display_name");
				if (!TextUtils.isEmpty(displayName)) {
					return displayName;
				}
			} while (profile.moveToNext());
		} finally {
			CursorUtils.safeClose(profile);
		}
		return null;
	}

	/**
	 * Retrieves a Cursor containing all of the user's profiles
	 * @param context The context to use
	 * @return A cursor with all of the user's profiles
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Nullable
	public static Cursor getProfile(Context context) {
		// From http://stackoverflow.com/a/2175688/1339200
		if (context == null) {
			return null;
		}
		if (!DeviceUtils.hasPermission(context, Permissions.READ_PROFILE)) {
			Log.w(TAG, "App does not have permission to retrieve profile");
			return null;
		}
		Uri uri = Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, ContactsContract.Contacts.Data.CONTENT_DIRECTORY);
		return context.getContentResolver().query(uri, null, null, null, ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	/**
	 * Retrieves all emails that are in the user's profile
	 * @param context The context to use
	 * @return A collection of email addresses.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @NonNull
    public static Collection<String> getProfileEmails(Context context) {
		HashSet<String> emails = new HashSet<String>();
		Cursor profile = null;
		try {
			profile = getProfile(context);
			if (!CursorUtils.hasResults(profile)) {
				return emails;
			}
			do {
				String email = CursorUtils.safeGetString(profile, ContactsContract.CommonDataKinds.Email.ADDRESS);
				// User could have entered garbage
				if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
					emails.add(email.toLowerCase(Locale.getDefault()).trim());
				}
			} while (profile.moveToNext());
		} finally {
			CursorUtils.safeClose(profile);
		}
		return emails;
	}

	/**
	 * Returns the first profile photo URI for the user
	 * @param context  The context to use
	 * @return The first profile photo URI if one exists
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Nullable
	public static Uri getProfilePhotoUri(Context context) {
		Cursor profile = null;
		try {
			profile = getProfile(context);
			if (!CursorUtils.hasResults(profile)) {
				return null;
			}
			String uri = CursorUtils.safeGetString(profile, ContactsContract.CommonDataKinds.Photo.PHOTO_URI);
			if (!TextUtils.isEmpty(uri)) {
				return Uri.parse(uri);
			}
		} finally {
			CursorUtils.safeClose(profile);
		}
		return null;
	}

	private static final String TAG = ProfileUtils.class.getSimpleName();
}
