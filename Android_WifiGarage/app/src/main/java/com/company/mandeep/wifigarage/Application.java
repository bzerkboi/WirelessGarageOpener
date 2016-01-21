package com.company.mandeep.wifigarage;

import android.content.Context;

import com.company.mandeep.wifigarage.com.company.mandeep.ParseObjects.Device;
import com.company.mandeep.wifigarage.com.company.mandeep.ParseObjects.SparkDevice;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import java.io.File;

/**
 * Created by Mandeep on 6/23/2015.
 */
public class Application extends  android.app.Application{

    public Application() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.


        deleteInstallationCache(this);
        Parse.enableLocalDatastore(this);

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseObject.registerSubclass(SparkDevice.class);
        ParseObject.registerSubclass(Device.class);

        Parse.initialize(this, this.getString(R.string.parse_app_id),
                this.getString(R.string.parse_client_key));

        //ParseUser.enableAutomaticUser();
        ParseInstallation.getCurrentInstallation();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);



    }

    public static boolean deleteInstallationCache(Context context) {
        boolean deletedParseFolder = false;
        File cacheDir = context.getCacheDir();
        File parseApp = new File(cacheDir.getParent(),"app_Parse");
        File installationId = new File(parseApp,"installationId");
        File currentInstallation = new File(parseApp,"currentInstallation");
        if(installationId.exists()) {
            deletedParseFolder = deletedParseFolder || installationId.delete();
        }
        if(currentInstallation.exists()) {
            deletedParseFolder = deletedParseFolder && currentInstallation.delete();
        }
        return deletedParseFolder;
    }
}
