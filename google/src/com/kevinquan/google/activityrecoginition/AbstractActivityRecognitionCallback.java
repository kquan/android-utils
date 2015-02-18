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
package com.kevinquan.google.activityrecoginition;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;

public abstract class AbstractActivityRecognitionCallback implements ConnectionCallbacks {

    @SuppressWarnings("unused")
    private static final String TAG = AbstractActivityRecognitionCallback.class.getSimpleName();

    protected GoogleApiClient mClient;

    @Override public void onConnectionSuspended(int reason) {}

    public AbstractActivityRecognitionCallback setClient(GoogleApiClient client) {
        mClient = client;
        return this;
    }
}