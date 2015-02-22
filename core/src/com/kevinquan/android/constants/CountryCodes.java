/*
 * Copyright 2015 Kevin Quan (kevin.quan@gmail.com)
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
package com.kevinquan.android.constants;

import android.text.TextUtils;

/**
 * List of phone number country codes mapped to countries and their two letter abbreviations.
 *
 * Note: This list is incomplete.
 *
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public enum CountryCodes {

    Unknown("", ""),
    Canada("ca","1"),
    SouthKorea("kr","82"),
    Spain("es","34"),
    Sweden("se","46"),
    Switzerland("ch","41"),
    UnitedStates("us","1"),

    ;

    /**
     * Retrieve a phone number country code given the country's two letter abbreviation
     * @param isoCountry The two letter abreviation of the country
     * @return The country code if known.
     */
    public static CountryCodes getFromIsoCountry(String isoCountry) {
        if (TextUtils.isEmpty(isoCountry)) {
            return Unknown;
        }
        for (CountryCodes code: values()) {
            if (code.getIsoCountryLiteral().equalsIgnoreCase(isoCountry)) {
                return code;
            }
        }

        return Unknown;
    }
    protected String mIsoCountry;

    protected String mCountryCode;

    private CountryCodes(String isoCountry, String countryCode) {
        mIsoCountry = isoCountry;
        mCountryCode = countryCode;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public String getIsoCountryLiteral() {
        return mIsoCountry;
    }
}
