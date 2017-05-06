package com.blakky.musicbuster.restclient;

import android.util.Log;

import com.blakky.musicbuster.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sangrambankar on 4/5/17.
 */

public class RestUtils {

    public static final String QUERY = "q";
    public static final String GENRE = "genres";
    public static final String LIMIT = "offset";
    public static final String ID = "client_id";
    public static final String BASE_URL  = "https://api.soundcloud.com";
    public static final String BASE_URL_V2  = "https://api-v2.soundcloud.com";
    public static final String TRACK_URL = "/search/tracks?&limit=20";
    public static final String TOP_URL = "/charts?&kind=top&limit=50";
    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String STREAM_URL = "/i1/tracks/{id}/streams?client_id="+API_KEY;



    public static IRestClient createRestClient(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override public void log(String message) {
                Log.d("MyTAG", "OkHttp: " + message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okClient = new OkHttpClient.Builder().addInterceptor(logging).build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_V2)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
        return retrofit.create(IRestClient.class);
    }

    public static IRestClient createRestClientMP3(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override public void log(String message) {
                Log.d("MyTAG", "OkHttp: " + message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okClient = new OkHttpClient.Builder().addInterceptor(logging).build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
        return retrofit.create(IRestClient.class);
    }
}
