package com.kevinquan.google.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.kevinquan.google.PlayServicesHelper;

/**
 * Helper methods for Google Play's FusedLocationProvider
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class FusedLocationProviderHelper {
	
	private static final String TAG = FusedLocationProviderHelper.class.getSimpleName();

	public interface LocationRetrievedListener {
	    void onLocationRetrieved(Location lastLocation);
	}

	public static boolean getCurrentLocation(Context context, final LocationRetrievedListener listener) {
	    if (!PlayServicesHelper.hasUseableGooglePlayServices(context)) {
	        Log.w(TAG, "Google Play is not available.  Cannot return current location.");
	        return false;
	    }
	    final LocationClient client = new LocationClient(context, new ConnectionCallbacks() {
	                @Override public void onConnected(Bundle arg0) {}
	                @Override public void onDisconnected() {}
	            }, 
	            new OnConnectionFailedListener() {
	                @Override public void onConnectionFailed(ConnectionResult arg0) { Log.d(TAG, "LocationClient connection failed"); }
	            });
	    client.registerConnectionCallbacks(new ConnectionCallbacks() {
	                @Override public void onDisconnected() { Log.d(TAG, "LocationClient disconnected"); }
	                @Override public void onConnected(Bundle arg0) {
	                    Log.d(TAG, "LocationClient connected, requesting location updates");
	                    Location lastLocation = client.getLastLocation();
	                    if (listener != null && lastLocation != null) {
	                        listener.onLocationRetrieved(lastLocation);
	                    }
	                }
	            });
	    client.connect();
	    return true;
	}

}
