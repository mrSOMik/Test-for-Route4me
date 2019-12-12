package com.somsoft.route4me_test.Retrofit2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.TreeMap;

public class AllRates {
/*
    {"rates":
        {"CAD":1.3177695968,
         "HKD":7.8048846188,
         "ISK":122.7440064649,
         ...
    },
     "base":"USD",
     "date":"2019-12-12"}
*/

    @SerializedName("rates")
    @Expose
    TreeMap<String, Float> rates;

    public TreeMap<String, Float> getRates() {
        return rates;
    }

}
