package api.android;

import managers.DeviceManager;
import managers.QAManager;
import org.openqa.selenium.By;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class AndroidSettingsAppsPermissions extends Android{

    private static UiObject GRANT_BUTTON = null;

    private static UiObject grantButton(){
        if(GRANT_BUTTON == null) GRANT_BUTTON = new UiSelector().resourceId(getAppPackageId()+":id/custom_dlg_positiveButton").makeUiObject();
        return GRANT_BUTTON;
    }

    public boolean grantAppsPermissions(){

        if(grantButton().exists()){
            QAManager.log.debug("Permissions for the apps were requested");
            grantButton().tap();
            new UiSelector().text("Hotspot Shield").tap();
            button1().tap();
            pressBackButton();
            openHotspotShield();
            QAManager.log.debug("App Permissions were granted");
            return true;
        }else {
            QAManager.log.debug("Permissions for the apps were not requested");
            return false;
        }
    }

    public boolean grantAppsPermissions(String appName){

        if(grantButton().exists()){
            QAManager.log.debug("Permissions for the apps were requested");
            grantButton().tap();
            DeviceManager.getDriver().findElement(By.xpath("//android.widget.TextView[@text=\""+appName+"\"]/android.widget.Switch")).click();
            button1().tap();
            pressBackButton();
            //openHotspotShield();
            QAManager.log.debug("Permissions for the apps were granted");
            return true;
        }else{
            QAManager.log.debug("Permissions for the apps were not requested");
            return false;
        }
    }
}
