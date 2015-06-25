package com.company.mandeep.wifigarage;

import com.company.mandeep.wifigarage.com.company.mandeep.ParseObjects.DeviceStatusChangePost;
import com.company.mandeep.wifigarage.com.company.mandeep.ParseObjects.SparkDevice;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Mandeep on 6/23/2015.
 */
public class Application extends  android.app.Application{

    public Application() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseObject.registerSubclass(SparkDevice.class);
        Parse.initialize(this, this.getString(R.string.parse_app_id),
                this.getString(R.string.parse_client_key));



    }
}
