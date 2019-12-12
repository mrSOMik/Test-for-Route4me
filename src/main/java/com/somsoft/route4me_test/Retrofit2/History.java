package com.somsoft.route4me_test.Retrofit2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.TreeMap;

public class History {
/*
    {"rates":{
      "2019-11-27":{"CAD":1.3266418385},
      "2019-11-28":{"CAD":1.3289413903},
      "2019-12-03":{"CAD":1.3320386596},
      "2019-12-02":{"CAD":1.3295835979},
      "2019-11-29":{"CAD":1.3307230013}},
      "start_at":"2019-11-27",
      "base":"USD",
      "end_at":"2019-12-03"}
*/

    @SerializedName("rates")
    @Expose
    TreeMap<String, TreeMap<String, Float>> history;

    public TreeMap<String, TreeMap<String, Float>> getHistory() {
        return history;
    }
}

