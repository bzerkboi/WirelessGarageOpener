package com.company.mandeep.wifigarage;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.company.mandeep.wifigarage.com.company.mandeep.ParseObjects.SparkDevice;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GarageMainActivity extends Activity {

    protected final static int GARAGE_OPEN = 1;
    protected final static int GARAGE_CLOSED = 0;
    private final static int GARAGE_STATE_UNKNOWN=-1;

    private final static String GARAGE_IS_CLOSED="0";
    private final static String GARAGE_IS_NOT_CLOSED="1";

    private Handler myHandler;
    Thread doorStatusThread;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_main);
        //setContentView(R.layout.activity_garage_opener);

        findViewById(R.id.btnAddDevice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparkDevice sparkDevice = new SparkDevice();
                EditText sparkCoreID=(EditText)findViewById(R.id.txtSparkCoreID);
                EditText sparkAuthToken= (EditText)findViewById(R.id.txtSparkAuthToken);

                sparkDevice.setSparkCoreID("54ff6e066672524818221267");
                sparkDevice.setSparkAuthToken("de359d9f45098701c5d6141a4cca39e16e1136fd");
                sparkDevice.setDeviceName("HomeGarageOpener");
                sparkDevice.setDeviceType(SparkDevice.TypeOfDevice.GarageDoorOpener);

                ParseACL acl = new ParseACL();
                acl.setPublicReadAccess(true);
                sparkDevice.setACL(acl);

                sparkDevice.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        finish();
                    }
                });
            }
        });

        //Setup Retrofit
        AppConfig.initialize(this);

        //Init a handler to handle mesaeges being sent in the app, passing in a reference of this (parent)
        myHandler= new GarageDoorStatusIndicatorHandler(new WeakReference<>(this));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Crete a new thread to monitor the door status passing in the handler created during the init
        doorStatusThread=new Thread(new MonitorDoorStatus(myHandler));

        //Start the thread
        doorStatusThread.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        //Stop the thread
        doorStatusThread.interrupt();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_garage_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public TextView getGarageStateForTextView()
    {
        //Used to get the textview so something other than the parent can modify it
        return (TextView) findViewById(R.id.garageStatus);
    }

    //Button handler for when a user clicks the button to move the garage
    public void MoveGarageClick(View v)
    {
        //Execute an async class which contains the code to move the garage
        new MoveGarage().execute();
    }

    //Create an async class to send an open garage command and handle the returned result
    private class MoveGarage extends AsyncTask<Void,Void,String>
    {
        @Override
        protected String doInBackground(Void... params)
        {
            //Issue a request to the Spark to move the garage
            RestClient.get().controlGarage(AppConfig.getSparkCoreID(), AppConfig.getSparkControlGarageFunction(), new Arguments(ControlCore.ControlGarageCommands.move.toString()), new Callback<ControlCore>() {
                @Override
                public void success(ControlCore controlCore, Response response)
                {
                    //Do something in here to indicate we opened the garage successfully
                    switch (controlCore.getReturn_value())
                    {
                        case 1:
                            Toast.makeText(getApplicationContext(),"Garage has moved",Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(),"Garage did not move",Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void failure(RetrofitError error)
                {
                    //There was an error trying to move the garage door
                    RestError errorBody = (RestError) error.getBodyAs(RestError.class);
                    Toast.makeText(getApplicationContext(),"Error moving garage door",Toast.LENGTH_SHORT).show();
                }
            });

            return "";
        }
    }

    //This class will loop to check the status of the switch, maybe put into its own intent
    private class MonitorDoorStatus implements Runnable
    {
        //The handler for messages
        private final Handler doorStatushandler;

        //Pass in the handler
        MonitorDoorStatus(Handler handler)
        {
            doorStatushandler=handler;
        }

        //run the thread
        public void run()
        {
            while(!Thread.currentThread().isInterrupted()) {
                //Log.i("HandlerThread", "Worked");

                //Look at the sensor to see if the door is open or not
                RestClient.get().getCommand(AppConfig.getSparkCoreID(),AppConfig.getSparkGarageClosedFunctionSensor(),new Callback<GetVariable>()
                {
                    @Override
                    //These methods are called when we can successfuly get the status of the door
                    public void success(GetVariable getVariable, Response response) {
                        if(getVariable.getResult()==GARAGE_IS_NOT_CLOSED)
                        {
                            doorStatushandler.sendMessage(doorStatushandler.obtainMessage(GARAGE_OPEN));
                        }
                        else if(getVariable.getResult()==GARAGE_IS_CLOSED)
                        {
                            doorStatushandler.sendMessage(doorStatushandler.obtainMessage(GARAGE_CLOSED));
                        }
                        else
                        {
                            //get this when we don't know what the status of the door is
                            doorStatushandler.sendMessage(doorStatushandler.obtainMessage(GARAGE_STATE_UNKNOWN));
                        }
                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                        RestError errorBody= (RestError)error.getBodyAs(RestError.class);
                        doorStatushandler.sendMessage(doorStatushandler.obtainMessage(GARAGE_STATE_UNKNOWN));
                    }
                });

                //sleep for a set amount of time
                sleep();
            }
        }

        private void sleep() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //This is a handler class which handles the messages being sent in the app
    private static class GarageDoorStatusIndicatorHandler extends Handler
    {
        WeakReference<GarageMainActivity> mParent;

        public GarageDoorStatusIndicatorHandler(WeakReference<GarageMainActivity> mParent)
        {
            this.mParent=mParent;
        }

        @Override
        public void handleMessage(Message msg)
        {
            GarageMainActivity parent=mParent.get();
            if(parent != null)
            {
                switch (msg.what)
                {
                    case GARAGE_OPEN:
                    {
                        Log.i("LOG","garage is open now");
                        parent.getGarageStateForTextView().setText("Garage is open");
                        break;
                    }
                    case GARAGE_CLOSED:
                    {
                        Log.i("LOG","garage is closed now");
                        parent.getGarageStateForTextView().setText("Garage is closed");
                        break;
                    }
                    case GARAGE_STATE_UNKNOWN:
                        parent.getGarageStateForTextView().setText("Garage state is unknown");
                        break;
                }
            }
        }
    }

    class RestError {
        @SerializedName("code")
        public int code;
        @SerializedName("error")
        public String error;
        @SerializedName("error_description")
        public String error_description;
    }
}
