package com.company.mandeep.wifigarage.com.company.mandeep.ParseObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Mandeep on 1/15/2016.
 */
@ParseClassName("Device")
public class Device extends ParseObject{
    public Device(){}

    public String getDisplayName(){
        return getString("name");
    }
    public void setName(String value) {
        put("name",value);
    }

}
