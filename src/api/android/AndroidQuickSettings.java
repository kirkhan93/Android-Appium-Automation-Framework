package api.android;

import managers.QAManager;
import utilities.UiSelector;
import utilities.exceptions.DeviceNotSupported;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/7/2015.
 */
public class AndroidQuickSettings extends Android{

    public static boolean isOpen(){
        return getActivityInFocus().contains("StatusBar");
    }

    private String dataButton(){
        if(getDeviceOsVersion()>=500) return  new UiSelector().descriptionContains("Mobile").compile();
        else if(getDeviceOsVersion()>=442) return new UiSelector().resourceId("com.android.systemui:id/rssi_textview").compile();
        else throw new DeviceNotSupported();
    }

    public AndroidSettingsData tapDataIcon(){
        QAManager.log.debug("Tapped Data Settings Shortcut");
        new UiSelector().uiSelector(dataButton()).tap();
        return new AndroidSettingsData();
    }

    public AndroidQuickSettings waitForViewToLoad(){
        QAManager.log.debug("Waiting for Quick Settings to load");
        new UiSelector().uiSelector(dataButton()).waitForSelector(5);
        return this;
    }

    public AndroidDesktop closeSettings(){
        QAManager.log.debug("Closing Quick Settings");
        pressHomeButton();
        return new AndroidDesktop();
    }
}
