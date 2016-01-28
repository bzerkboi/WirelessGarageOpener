package com.company.mandeep.wifigarage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Mandeep on 1/23/2016.
 */
public class ParseCloudPushReceiver extends ParsePushBroadcastReceiver {
    String TAG="ParseCloudPushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        String data = intent.getExtras().getString("com.parse.Data");
        Intent sendIntent = new Intent(context,ParseCloudPushReceiver.class);
        sendIntent.setAction("TableChanged");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sendIntent);
    }
}
