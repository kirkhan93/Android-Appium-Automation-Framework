package utilities.exceptions;

import api.android.Android;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/9/2015.
 */
public class DeviceNotSupported extends RuntimeException {

    public DeviceNotSupported(){
        super("Device's Operating System is "+Android.getDeviceOsVersion()+" and it is not, currently, supported");
    }
    public DeviceNotSupported(String message){
        super(message);
    }
}
