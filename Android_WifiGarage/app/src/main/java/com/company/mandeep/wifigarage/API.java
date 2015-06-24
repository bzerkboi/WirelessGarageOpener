package com.company.mandeep.wifigarage;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Mandeep on 1/15/2015.
 */
public interface API {

    @POST("/v1/devices/{DEVICE_ID}/{FUNCTION}")
    void controlGarage(@Path("DEVICE_ID") String deviceID,
                       @Path("FUNCTION") String function,
                       @Body Arguments args,
                       Callback<ControlCore> callback);

    @GET("/v1/devices/{DEVICE_ID}/{VARIABLE}")
    void getCommand(@Path("DEVICE_ID") String deviceID,
                    @Path("VARIABLE") String variable,
                    Callback<GetVariable> callback);
}
