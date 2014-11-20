package com.kevinquan.android.location;

import android.content.Context;
import android.location.LocationManager;

/**
 * Utilities to work with the location providers on a device
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class LocationProviderUtils {

	@SuppressWarnings("unused")
	private static final String TAG = LocationProviderUtils.class.getSimpleName();
	
	/**
	 * Checks whether GPS is enabled on the device
	 * @param context The context to check with
	 * @return True if GPS is enabled
	 */
	public static boolean isGpsProviderEnabled(Context context) {
		if (context == null) {
			return false;
		}
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	/**
	 * Checks whether network location is enabled on the device
	 * @param context The context to check with
	 * @return True if network location is enabled
	 */
	public static boolean isNetworkProviderEnabled(Context context) {
		if (context == null) {
			return false;
		}
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
	
	/**
	 * Checks whether passive location is enabled on the device
	 * @param context The context to check with
	 * @return True if passive location is enabled
	 */
	public static boolean isPassiveProviderEnabled(Context context) {
		if (context == null) {
			return false;
		}
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
	}
	
	/**
	 * Checks whether both GPS and network location is enabled on the device
	 * @param context The context to check with
	 * @return True if both providers are enabled
	 */
	public static boolean isHighAccuracyEnabled(Context context) {
		return isGpsProviderEnabled(context) && isNetworkProviderEnabled(context);
	}
}
