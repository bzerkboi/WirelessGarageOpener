package com.company.mandeep.wifigarage;

import com.parse.ui.ParseLoginDispatchActivity;

/**
 * Created by Mandeep on 6/23/2015.
 */
public class DispatchActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {
        //return GarageMainActivity.class;
        return MainActivity.class;
    }
}
