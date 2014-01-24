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
package com.kevinquan.android.constants.canada;

import android.text.TextUtils;
import android.util.Log;

/**
 * Models each province and territory in Canada.
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public enum Province {

    Ontario(City.Toronto, "ON"),
    Quebec(City.Quebec, "QC"),
    NovaScotia(City.Halifax, "NS"),
    NewBrunswick(City.Fredericton, "NB"),
    Manitoba(City.Winnipeg, "MB"),
    BritishColumbia(City.Victoria, "BC"),
    PrinceEdwardIsland(City.Charlottetown, "PE"),
    Saskatchewan(City.Regina, "SK"),
    Alberta(City.Edmonton, "AB"),
    NewfoundlandAndLabrador(City.StJohns, "NL"),
    NorthwestTerritories(City.Yellowknife, "NT"),
    Yukon(City.Whitehorse, "YT"),
    Nunavut(City.Iqaluit, "NU"),
    ;
    
    protected City mCapital;
    protected String mAbbreviation;
    
    private Province(City capital, String abbreviation) {
        mCapital = capital;
        mAbbreviation = abbreviation;
    }

    /**
     * Returns the capital city of the province or territority
     * @return The capital city of the province or territority
     */
    public City getCapital() {
        return mCapital;
    }

    /**
     * Returns the postal abbreviation used by the province or territority
     * @return The postal abbreviation used by the province or territority
     */
    public String getAbbreviation() {
        return mAbbreviation;
    }
    
    /**
     * Retrieve a province by postal abbreviation.  If an unknown abbreviation is provided, null will be returned.
     * The abbreviation is NOT case sensitive
     * @param abbreviation The abbreviation to look up
     * @return The province which uses the abbreviation, or null if not found.
     */
    public static Province getByAbbreviation(String abbreviation) {
        if (TextUtils.isEmpty(abbreviation)) {
            return null;
        }
        for (Province province : Province.values()) {
            if (province.getAbbreviation().equalsIgnoreCase(abbreviation)) {
                return province;
            }
        }
        Log.w("ProvinceEnum", "Unknown abbreviation: "+abbreviation);
        return null;
    }
}
