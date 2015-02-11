package com.company.mandeep.wifigarage;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Mandeep on 1/15/2015.
 */
public class RestClient {
    private static API REST_CLIENT;
    private static String ROOT =
            "https://api.spark.io";
    static {
        setupRestClient();
    }

    private RestClient() {}

    public static API get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Authorization","Bearer "+ AppConfig.getSparkCoreAuthToken());
            }
        };

        RestAdapter builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setRequestInterceptor(requestInterceptor)
                .build();

        REST_CLIENT = builder.create(API.class);
    }
}
