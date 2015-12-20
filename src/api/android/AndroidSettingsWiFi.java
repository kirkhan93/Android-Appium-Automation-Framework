package api.android;

import managers.DeviceManager;
import managers.QAManager;
import org.openqa.selenium.NoSuchElementException;
import utilities.UiObject;
import utilities.UiSelector;

import java.util.concurrent.TimeUnit;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class AndroidSettingsWiFi extends Android{

    private static UiObject

            VIEW        = null,
            SECURITY    = null;

    private static UiObject view(){
        if(VIEW == null) VIEW = new UiSelector().resourceId("com.android.settings:id/content").makeUiObject();
        return VIEW;
    }

    private static UiObject security(){
        if(SECURITY == null) SECURITY = new UiSelector().resourceId("com.android.settings:id/security_fields").makeUiObject();
        return SECURITY;
    }

    public static boolean isShown(){
        return getActivityInFocus().contains("com.android.settings/com.android.settings");
    }

    public AndroidSettingsWiFi enableWifi(){
        if(!isWifiOn()){
            new UiSelector().text("OFF").tap();
            Wait(2000);
            new UiSelector().text("Connected").waitForSelector(25);
        }
        return this;
    }

    public AndroidSettingsWiFi disableWifi(){
        if(isCellDataOn()){
            new UiSelector().text("ON").tap();
            Wait(2000);
        }
        return this;
    }

    public AndroidSettingsWiFi connectTo(String ssid){
        enableWifi();
        if(!isConnected(ssid)){
            try{
                DeviceManager.getDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
                new UiSelector().text(ssid).tap();
            }catch (NoSuchElementException e){
                if(!isShown()) Android.openSettingsWifi();
                new UiSelector().text(ssid).scrollTo().tap();
            }finally {
                DeviceManager.getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            }
            if(security().exists()) throw new AssertionError("Network: " + ssid + " requires authentication. Authentication is not supported by this API");
            if(button1().exists()) button1().tap();
            return this;
        }
        QAManager.log.debug("Already connected to: " + ssid);
        return this;
    }

    private static boolean isConnected(String ssid){
        return new UiSelector().exists("//android.widget.TextView[@text=\""+ssid+"\"]/following-sibling::android.widget.TextView[@text=\"Connected\"]");
    }

    public AndroidSettingsWiFi waitForViewToLoad(){
        //new UiSelector().uiSelector(VIEW).waitForSelector(5);
        return this;
    }

    public AndroidDesktop closeSettings(){
        pressHomeButton();
        return new AndroidDesktop();
    }
}
