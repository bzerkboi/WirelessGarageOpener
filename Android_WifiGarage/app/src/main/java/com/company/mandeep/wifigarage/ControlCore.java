package com.company.mandeep.wifigarage;

/**
 * Created by Mandeep on 1/15/2015.
 */
public class ControlCore {

    private String id;
    private String name;
    private boolean connected;
    private int return_value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public int getReturn_value() {
        return return_value;
    }

    public void setReturn_value(int return_value) {
        this.return_value = return_value;
    }
    
    public static enum ControlGarageCommands
    {
        open,
        close
        
    }
}
