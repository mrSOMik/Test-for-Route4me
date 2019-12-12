package com.somsoft.route4me_test.Retrofit2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JSONPlaceHolderApi {

    @GET("latest")
    public Call<AllRates> getAllRates(@Query("base") String base);

    @GET("history")
    public Call<History> getHystory(@Query("start_at") String start_at, @Query("end_at") String end_at,
                                    @Query("base") String base, @Query("symbols") String symbol);

}
