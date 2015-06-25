package com.company.mandeep.wifigarage.com.company.mandeep.ParseObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Mandeep on 6/24/2015.
 */
@ParseClassName("DeviceStatusChangePost")
public class DeviceStatusChangePost extends ParseObject {

    public ParseUser getUser()
    {
        return getParseUser("user");
    }
    public void setUser(ParseUser value)
    {
        put("user",value);
    }

    public String getDeviceType()
    {
        return getString("deviceType");
    }
    public void setDeviceType(TypeOfDevice value)
    {
        put("deviceType",value.toString());
    }

    public String getCurrentDeviceStatus()
    {
        return getString("currentDeviceStatus");
    }
    public void setCurrentDeviceStatus(String value)
    {
        put("currentDeviceStatus",value);
    }

    public String getDeviceRequest()
    {
        return getString("deviceRequest");
    }
    public void setGarageRequest(String value)
    {
        put("deviceRequest",value);
    }

    public String getRequestTime()
    {
        return getString("requestTime");
    }
    public void setRequestTime(String value)
    {
        put("requestTime",value);
    }

    public enum TypeOfDevice
    {
        GarageDoorOpener
    }
}
