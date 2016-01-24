package com.company.mandeep.wifigarage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Mandeep on 1/23/2016.
 */
public class ParseCloudPushReceiver extends BroadcastReceiver {
    String TAG="ParseCloudPushReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        String data = intent.getExtras().getString("com.parse.Data");
    }
}
