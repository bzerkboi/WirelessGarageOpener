package com.company.mandeep.wifigarage.com.company.mandeep.ParseObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Mandeep on 6/24/2015.
 */

@ParseClassName("SparkDevice")
public class SparkDevice extends ParseObject{

    public String getSparkCoreID(){return getString("sparkCoreID");}
    public void setSparkCoreID(String value){put("sparkCoreID",value);}

    public String getSparkAuthToken(){return getString("sparkAuthToken");}
    public void setSparkAuthToken(String value){put("sparkAuthToken",value);}

    public String getDeviceType(){return getString("deviceType");}
    public void setDeviceType(TypeOfDevice value) { put("deviceType",value.toString());}

    public String getDeviceName(){return getString("deviceName");}
    public void setDeviceName(String value){put("deviceName",value);}

    public SparkDevice (){}

    public enum TypeOfDevice
    {
        GarageDoorOpener
    }
}
