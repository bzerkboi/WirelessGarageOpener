package com.company.mandeep.wifigarage;

import android.content.Context;

/**
 * Created by Mandeep on 2/10/2015.
 */
public class AppConfig {

    private static Context ctx;

    public static void initialize(Context context)
    {        
        ctx=context.getApplicationContext();
    }

    public static String getSparkCoreID()
    {
        return ctx.getString(R.string.sparkCoreID);
    }
    
    public static String getSparkCoreAuthToken()
    {
        return ctx.getString(R.string.sparkCoreAuthToken);
    }
    
    public static String getSparkControlGarageFunction()
    {
        return ctx.getString(R.string.sparkControlGarageFunction);
        
    }
    
    public static String getSparkGarageClosedFunctionSensor()
    {
        return ctx.getString(R.string.sparkGarageClosedFunctionSensor);
        
    }
}
