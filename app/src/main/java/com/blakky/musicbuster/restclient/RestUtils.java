package com.blakky.musicbuster.restclient;

import com.blakky.musicbuster.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sangrambankar on 4/5/17.
 */

public class RestUtils {

    public static final String QUERY = "q";
    public static final String LIMIT = "offset";
    public static final String ID = "client_id";
    public static final String BASE_URL  = "https://api.soundcloud.com";
    public static final String TRACK_URL = "/tracks?&limit=20";
    public static final String API_KEY = BuildConfig.API_KEY;

    public static IRestClient createRestClient(){
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(IRestClient.class);
    }
}
