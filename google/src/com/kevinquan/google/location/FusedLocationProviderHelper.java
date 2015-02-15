package com.kevinquan.google.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.kevinquan.google.PlayServicesHelper;

/**
 * Helper methods for Google Play's FusedLocationProvider
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class FusedLocationProviderHelper {

	public interface LocationRetrievedListener {
	    void onLocationRetrieved(Location lastLocation);
	}

	public static boolean getCurrentLocation(Context context, final LocationRetrievedListener listener) {
		if (context == null) {
			return false;
		}
	    if (!PlayServicesHelper.hasUseableGooglePlayServices(context)) {
	        Log.w(TAG, "Google Play is not available.  Cannot return current location.");
	        return false;
	    }
	    final GoogleApiClient client = new GoogleApiClient.Builder(context).addApi(LocationServices.API)
											.addOnConnectionFailedListener(new OnConnectionFailedListener() {
									                @Override public void onConnectionFailed(ConnectionResult arg0) { Log.w(TAG, "Google Location Services connection failed"); }
											})
											.build();
	    client.registerConnectionCallbacks(new ConnectionCallbacks() {
			@Override public void onConnected(Bundle arg0) {
                Log.d(TAG, "LocationClient connected, requesting location updates");
				Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
                if (listener != null && lastLocation != null) {
                    listener.onLocationRetrieved(lastLocation);
                }
			}

			@Override public void onConnectionSuspended(int arg0) {}
		});
	    client.connect();
	    return true;
	}

	private static final String TAG = FusedLocationProviderHelper.class.getSimpleName();

}
