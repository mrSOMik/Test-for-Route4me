package com.somsoft.route4me_test;

public class Pair {

    private String currency;
    private Float rate;

    public Pair(String key, Float value) {
        currency = key;
        rate = value;
    }

    public String getCurrency() {
        return currency;
    }
    public Float getRate() {
        return rate;
    }
}
