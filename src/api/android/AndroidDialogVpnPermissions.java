package api.android;

import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;
import utilities.exceptions.DeviceNotSupported;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class AndroidDialogVpnPermissions extends Android{

    public static boolean flagExpectPermissions = true;

    private static UiObject

            TITLE       = null,
            CHECK_BOX   = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().resourceId("android:id/alertTitle").makeUiObject();
        return TITLE;
    }

    private static UiObject checkBox(){
        if(CHECK_BOX == null) CHECK_BOX = new UiSelector().resourceId("com.android.vpndialogs:id/check").makeUiObject();
        return CHECK_BOX;
    }

    private UiObject disconnectButton(){
        if(getDeviceOsVersion()>=500){
            return button2();
        } else if(getDeviceOsVersion()>=442){
            return button3();
        } else throw new DeviceNotSupported();
    }

    public AndroidDialogVpnPermissions tapCheckBox(){
        QAManager.log.debug("Tapping on the VPN permissions checkbox");
        checkBox().tap();
        return this;
    }

    public void tapAccept(){
        QAManager.log.debug("Accepting VPN permissions");
        if(getDeviceOsVersion() < 500) checkBox().tap();
        QAManager.log.debug("Tapping on the OK button");
        button1().tap();
    }

    public void tapDisconnect(){
        QAManager.log.debug("Tapping on the disable VPN button");
        disconnectButton().tap();
        AndroidDialogVpnPermissions.flagExpectPermissions = true;
    }

    public static boolean isShown(){
        return title().exists();
    }

    public AndroidDialogVpnPermissions waitForViewToLoad(){
        QAManager.log.debug("Waiting for VPN Permission Dialog to load");
        title().waitForSelector(5);
        return this;
    }

    public void closeDialog(){
        QAManager.log.debug("Closing VPN Permissions Dialog");
        pressBackButton();
    }
}