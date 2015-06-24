package com.company.mandeep.wifigarage;

import android.content.Context;

import com.parse.Parse;

/**
 * Created by Mandeep on 6/23/2015.
 */
public class Application extends  android.app.Application{

    public Application() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, this.getString(R.string.parse_app_id),
                this.getString(R.string.parse_client_key));

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

    }
}
