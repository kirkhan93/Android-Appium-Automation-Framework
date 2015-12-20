package api.android;

import api.hotspotshield.v400.ViewHome;
import managers.QAManager;
import utilities.UiSelector;
import utilities.exceptions.DeviceNotSupported;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/7/2015.
 */
public class AndroidNotifications extends Android{

    public static boolean isOpen(){
        return getActivityInFocus().contains("StatusBar");
    }

    private static String dismissButton(){
        if(getDeviceOsVersion()>=500) return new UiSelector().resourceId("com.android.systemui:id/dismiss_text").compile();
        else if(getDeviceOsVersion() >= 442) return new UiSelector().resourceId("com.android.systemui:id/clear_all_button").compile();
        else throw new DeviceNotSupported();
    }

    private static String quickSettingsButton(){
        if(getDeviceOsVersion()>=500) return new UiSelector().resourceId("com.android.systemui:id/quick_settings_container").compile();
        else if(getDeviceOsVersion() >= 442) return new UiSelector().resourceId("com.android.systemui:id/settings_button").compile();
        else throw new DeviceNotSupported();
    }

    public ViewHome tapHotspotShieldIcon(){
        if(new UiSelector().text("Hotspot Shield").exists()) new UiSelector().text("Hotspot Shield").tap();
        else new UiSelector().text("Hotspot Shield is OFF").tap();
        return new ViewHome();
    }

    public ViewHome tapProtectNow(){
        if(new UiSelector().text("PROTECT NOW").exists()){
            QAManager.log.debug("Tapped the PROTECT NOW button, client should start connecting...");
            new UiSelector().text("PROTECT NOW").tap();
            new ViewHome().connectionAlgorithm();
        }else{
            QAManager.log.debug("App is already connected");
            closeNotifications();
            openHotspotShield();
        }
        return new ViewHome().waitForViewToLoad();
    }

    public ViewHome tapPause(){
        if(new UiSelector().text("PAUSE").exists()){
            QAManager.log.debug("Tapped the PAUSE button, client should start disconnecting...");
            new UiSelector().text("PAUSE").tap();
            new ViewHome().disconnectionAlgorithm();
        }else{
            QAManager.log.debug("App is already disconnected");
            closeNotifications();
            openHotspotShield();
        }
        return new ViewHome().waitForViewToLoad();
    }

    public AndroidQuickSettings openQuickSettings(){
        QAManager.log.debug("Opening quick settings");
        new UiSelector().uiSelector(quickSettingsButton()).tap();
        return new AndroidQuickSettings();
    }

    public AndroidDesktop dismissNotifications(){
        QAManager.log.debug("Dismissing all the unnecessary notifications");
        new UiSelector().uiSelector(dismissButton()).tap();
        return new AndroidDesktop();
    }

    public AndroidDialogVpnPermissions tapVpn(){
        if(getDeviceOsVersion()>=500){
            new AndroidNotifications().openQuickSettings();
            new UiSelector().text("Network may be monitored").tap();
        }else if(getDeviceOsVersion()>=442) new UiSelector().text("Touch to manage the network.").tap();
        return new AndroidDialogVpnPermissions();
    }

    public AndroidNotifications waitForViewToLoad(){
        QAManager.log.debug("Waiting for the notifications to load");
        new UiSelector().uiSelector(quickSettingsButton()).waitForSelector(5);
        return this;
    }

    public AndroidDesktop closeNotifications(){
        QAManager.log.debug("Closing notifications");
        pressHomeButton();
        return new AndroidDesktop();
    }
}
