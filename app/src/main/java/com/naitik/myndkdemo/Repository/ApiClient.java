package com.naitik.myndkdemo.Repository;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    static {
        System.loadLibrary("native-lib");
    }

    public static native String baseurl();

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseurl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getRequestHeader())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getRequestHeader() {

        return new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(new MyInterceptor())
                .retryOnConnectionFailure(true)
                .build();
    }
}
