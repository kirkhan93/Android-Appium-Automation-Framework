package api.hotspotshield.v400;

import api.android.Android;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;
import utilities.constants.SettingItem;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class ViewGeneralSettings extends Android{

    public static String

            LAST_VPN_MODE   = "",
            LAST_DOMAIN     = "";

    private static UiObject

            TITLE           = null,
            VPN_MODE        = null,
            VPN_MODE_SD     = null,
            VPN_MODE_OVPN   = null,
            VPN_MODE_HYDRA  = null,
            DOMAIN          = null,
            EDIT_FIELD      = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().resourceId("android:id/action_bar_title").makeUiObject();
        return TITLE;
    }

    private static UiObject editField(){
        if(EDIT_FIELD == null) EDIT_FIELD = new UiSelector().resourceId("android:id/edit").makeUiObject();
        return EDIT_FIELD;
    }

    private static UiObject vpnMode(){
        if(VPN_MODE == null) VPN_MODE = new UiSelector().text("VPN mode").makeUiObject();
        return VPN_MODE;
    }

    private static UiObject vpnModeSd(){
        if(VPN_MODE_SD == null) VPN_MODE_SD = new UiSelector().text("Use SD").makeUiObject();
        return VPN_MODE_SD;
    }

    private static UiObject vpnModeOvpn(){
        if(VPN_MODE_OVPN == null) VPN_MODE_OVPN = new UiSelector().text("Open VPN").makeUiObject();
        return VPN_MODE_OVPN;
    }

    private static UiObject vpnModeHydra(){
        if(VPN_MODE_HYDRA == null) VPN_MODE_HYDRA = new UiSelector().text("Hydra").makeUiObject();
        return VPN_MODE_HYDRA;
    }

    private static UiObject domain(){
        if(DOMAIN == null) DOMAIN = new UiSelector().text(SettingItem.DOMAIN).makeUiObject();
        return DOMAIN;
    }

    private static UiObject settingItem(String settingItem){
        return new UiSelector().text(settingItem).makeUiObject();
    }

    public ViewGeneralSettings setVpnMode(String modeName){
        //LAST_VPN_MODE = modeName;
        modeName = modeName.toLowerCase();
        vpnMode().tap();
        if(modeName.contains("sd")){
            vpnModeSd().tap();
            LAST_VPN_MODE = "sd";
        }
        else if(modeName.contains("ovpn")){
            vpnModeOvpn().tap();
            LAST_VPN_MODE = "ovpn";
        }
        else{
            vpnModeHydra().tap();
            LAST_VPN_MODE = "hydra";
        }
        QAManager.log.debug("Set VPN Mode to: " + modeName);
        return this;
    }

    public ViewGeneralSettings enterDebugInfo(String settingName, String param){
        QAManager.log.debug("Changing Debug Info for: " +settingName+" to: " + param);
        settingItem(settingName).tap();
        editField().clearText().typeText(param);
        button1().tap();
        if(settingName.equals(SettingItem.DOMAIN)) setLastDomain(param.toLowerCase());
        QAManager.log.debug("General Settings set for debug option "+settingName+" with param: " +param);
        return this;
    }

    public ViewGeneralSettings enableSettings(String settingName){
        UiObject settingItem = settingItem(settingName).scrollTo();
        if(!new UiSelector().isChecked(".//*[@text=\"" + settingName + "\"]/parent::*[1]/following::*[1]/*[@class=\"android.widget.CheckBox\"]")){
            if(settingItem.exists()) settingItem.tap();
            else settingItem.scrollTo().tap();
            QAManager.log.debug("Enabled general settings for: " + settingName);
        }else QAManager.log.debug("General settings already enabled for: " + settingName);
        if(settingName.equals(SettingItem.SHOW_POP_UPS)) AdView.setFlagAdExpected(true);
        return this;
    }

    public ViewGeneralSettings disableSettings(String settingName){
        UiObject settingItem = settingItem(settingName).scrollTo();
        if(new UiSelector().isChecked(".//*[@text=\"" + settingName + "\"]/parent::*[1]/following::*[1]/*[@class=\"android.widget.CheckBox\"]")){
            if(settingItem.exists()) settingItem.tap();
            else settingItem.scrollTo().tap();
            QAManager.log.debug("Disabled general settings for: " + settingName);
        }else QAManager.log.debug("General settings already disabled for: " + settingName);
        if(settingName.equals(SettingItem.SHOW_POP_UPS)) AdView.setFlagAdExpected(false);
        return this;
    }

    private static void setLastDomain(String value){
        LAST_DOMAIN = value;
        QAManager.log.debug("Last Domain set to: " + LAST_DOMAIN);
    }

    public ViewGeneralSettings waitForViewToLoad(){
        title().waitForSelector(5);
        return this;
    }

    public ViewHome exitView(){
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}