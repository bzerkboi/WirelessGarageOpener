package com.company.mandeep.wifigarage;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.google.gson.annotations.SerializedName;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GarageMainActivity extends Activity {

    protected final static int GARAGE_OPEN = 1;
    protected final static int GARAGE_CLOSED = 0;
    private final static String GARAGE_IS_CLOSED="1";
    private final static String GARAGE_IS_NOT_CLOSED="0";

    private Handler myHandler;
    Thread doorStatusThread;
        
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_main);
        AppConfig.initialize(this);

        //Need to set up a thread to monitor the garage door status messages
        myHandler = new GarageDoorStatusIndicatorHandler(new WeakReference<>(this));

    }

    public RadioButton getOpen() { return (RadioButton)findViewById(R.id.openGarageIndicator); }
    public RadioButton getClose() { return (RadioButton)findViewById(R.id.closeGarageIndicator); }

    @Override
    protected void onResume()
    {
        super.onResume();
        doorStatusThread=new Thread(new MonitorDoorStatus(myHandler));
        doorStatusThread.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
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

    public void OpenGarageBtnClick(View v)
    {
        new OpenGarage().execute();
    }

    //Create an async class to send an open garage command and handle the returned result
    private class OpenGarage extends AsyncTask<Void,Void,String>
    {
        @Override
        protected String doInBackground(Void... params)
        {
            RestClient.get().controlGarage(AppConfig.getSparkCoreID(), AppConfig.getSparkControlGarageFunction(), new Arguments(ControlCore.ControlGarageCommands.open.toString()), new Callback<ControlCore>() {
                @Override
                public void success(ControlCore controlCore, Response response) 
                {
                    //Do something in here to indicate we opened the garage successfully
                }

                @Override
                public void failure(RetrofitError error) 
                {
                    RestError errorBody = (RestError) error.getBodyAs(RestError.class);
                }
            });

            return "";
        }

    }

    public void CloseGarageBtnClick(View v)
    {
        new CloseGarage().execute();
    }

    private class CloseGarage extends AsyncTask<Void,Void,String>
    {

        @Override
        protected String doInBackground(Void... params) {
            RestClient.get().controlGarage(AppConfig.getSparkCoreID(), AppConfig.getSparkControlGarageFunction(), new Arguments(ControlCore.ControlGarageCommands.close.toString()), new Callback<ControlCore>() {
                @Override
                public void success(ControlCore controlCore, Response response) 
                {
                    //Do something here indicating we closed the garage
                }

                @Override
                public void failure(RetrofitError error) 
                {
                    RestError errorBody = (RestError) error.getBodyAs(RestError.class);
                }
            });
            return "";
        }
    }

    //This class will loop to check the status of the switch, maybe put into its own intent
    private class MonitorDoorStatus implements Runnable
    {
        private final Handler doorStatushandler;

        MonitorDoorStatus(Handler handler)
        {
            doorStatushandler=handler;
        }

        //Log.i("HandlerThread", "Worked");

        public void run()
        {
            while(!Thread.currentThread().isInterrupted()) {
                //Log.i("HandlerThread", "Worked");
                
                //Look at the sensor to see if the door is open or not
                RestClient.get().getCommand(AppConfig.getSparkCoreID(),AppConfig.getSparkGarageClosedFunctionSensor(),new Callback<GetVariable>()
                {
                    @Override
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
                            Log.i("HandlerThread", "Worked");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) 
                    {
                        RestError errorBody= (RestError)error.getBodyAs(RestError.class);
                    }
                });

                sleep();
            }
        }

        private void sleep() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
                        parent.getOpen().setChecked(true);
                        parent.getClose().setChecked(false);
                        break;
                    }
                    case GARAGE_CLOSED:
                    {
                        Log.i("LOG","garage is closed now");
                        parent.getOpen().setChecked(false);
                        parent.getClose().setChecked(true);
                        break;
                    }
                }
            }
        }
    }
}
