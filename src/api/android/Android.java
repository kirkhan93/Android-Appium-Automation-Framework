package api.android;


import api.hotspotshield.v400.ViewHome;
import api.hotspotshield.v400.ViewOnBoarding;
import io.appium.java_client.NetworkConnectionSetting;
import io.appium.java_client.android.AndroidKeyCode;
import managers.*;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import utilities.UiObject;
import utilities.UiSelector;
import utilities.Utilities;
import utilities.constants.Activity;
import utilities.constants.Package;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class Android extends SystemManager {

    public static UiObject

            OK_BUTTON1   = null,
            OK_BUTTON2   = null,
            OK_BUTTON3   = null;

    private static String

            HOTSPOT_SHIELD_PACKAGE_NAME = null,
            ANR_LOG_LOCATION = null,
            CARRIER = null,
            MODEL = null,
            MODEL_ID = null,
            MANUFACTURER = null,
            LANGUAGE = null,
            SERIAL = null;

    private static Integer

            HOTSPOT_SHIELD_VERSION = null,
            AGENT_OS = null;

    private static Boolean

            ROOTED_DEVICE = null;

    public static UiObject button1(){
        if(OK_BUTTON1 == null) OK_BUTTON1 = new UiSelector().resourceId("android:id/button1").makeUiObject();
        return OK_BUTTON1;
    }

    public static UiObject button2(){
        if(OK_BUTTON2 == null) OK_BUTTON2 = new UiSelector().resourceId("android:id/button2").makeUiObject();
        return OK_BUTTON2;
    }

    public static UiObject button3(){
        if(OK_BUTTON3 == null) OK_BUTTON3 = new UiSelector().resourceId("android:id/button3").makeUiObject();
        return OK_BUTTON3;
    }

    public static String getDeviceSerial(){
        if(isEmpty(SERIAL)) {
            SERIAL = executeCommand("adb shell getprop ro.serialno");
            QAManager.log.debug("Device Serial is: " + SERIAL);
        }
        return SERIAL;
    }

    public static String getDeviceLanguage(){
        if(isEmpty(LANGUAGE)){
            LANGUAGE =  executeCommand("adb shell getprop user.language");
            QAManager.log.debug("Device Language is set to: " +LANGUAGE);
        }
        return LANGUAGE;
    }

    public static String getDeviceManufacturer(){
        if(isEmpty(MANUFACTURER)){
            MANUFACTURER = executeCommand("adb shell getprop ro.product.manufacturer").trim();
            QAManager.log.debug("Device Manufacturer is: " + MANUFACTURER);
        }
        return MANUFACTURER;
    }

    public static String getDeviceModel(){
        if(isEmpty(MODEL)){
            MODEL = executeCommand("adb shell getprop ro.product.model").trim();
            QAManager.log.debug("Device Model is: " + MODEL);
        }
        return MODEL;
    }

    public static String getDeviceModelID(){
        if(isEmpty(MODEL_ID)){
            MODEL_ID = executeCommand("adb shell getprop ro.boot.hardware.sku").trim();
            QAManager.log.debug("Device Model ID is: " + MODEL_ID);
        }
        return MODEL_ID;
    }

    public static int getDeviceOsVersion(){
        if(AGENT_OS==null){
            String output =  executeCommand("adb shell getprop ro.build.version.release");
            AGENT_OS = Integer.parseInt(output.replaceAll("\\.", "").trim());
            if (AGENT_OS.toString().length() == 2) AGENT_OS = Integer.parseInt(AGENT_OS.toString() + "0");
            QAManager.log.debug("Device OS is: " + AGENT_OS);
        }
        return AGENT_OS;
    }

    public static String getCarrier(){
        if(isEmpty(CARRIER)) {
            if(isCarrierAvailable()){
                CARRIER = executeCommand("adb shell getprop ro.cdma.home.operator.alpha");
                QAManager.log.debug("Device Carrier is: " + CARRIER);
            }else CARRIER = "No Carrier";
        }
        return CARRIER.trim();
    }

    public static void rebootDevice(){
        QAManager.log.debug("Rebooting the device");
        executeCommand("adb reboot");
    }

    public static String getStackTracesLocation(){
        if(isEmpty(ANR_LOG_LOCATION)){
            ANR_LOG_LOCATION = executeCommand("adb shell getprop dalvik.vm.stack-trace-file");
            QAManager.log.debug("On this device log traces are located at: " + ANR_LOG_LOCATION);
        }
        return ANR_LOG_LOCATION;
    }

    public static boolean isCarrierAvailable(){
        // OLD: ro.cdma.home.operator.alpha
        // NEW: gsm.operator.alpha
        String output = executeCommand("adb shell getprop gsm.operator.alpha").trim();
        return !Utilities.isEmpty(output);
    }

    public static boolean isWifiOn(){
        boolean state = executeCommand("adb shell settings get global wifi_on").trim().equals("1");;//DeviceManager.getDriver().getNetworkConnection().wifiEnabled();
        QAManager.log.debug("Is WiFi on: " + state);
        return state;
    }

    public static boolean isAirplaneModeOn(){
        boolean state = DeviceManager.getDriver().getNetworkConnection().airplaneModeEnabled();
        QAManager.log.debug("Is Airplane Mode on: " + state);
        return state;
    }

    public static boolean isCellDataOn(){
        boolean state = DeviceManager.getDriver().getNetworkConnection().dataEnabled();
        QAManager.log.debug("Is Cell Data on: " + state);
        return state;
    }

    public static Boolean isVpnConnected() {
        boolean state = executeCommand("adb shell netcfg").contains("tun0     UP");
        QAManager.log.debug("Is VPN Connected: " + state);
        return state;
    }

    public static Boolean isRooted(){
        if(!isMac()){
            if(ROOTED_DEVICE == null){
                String output = executeCommand("wmic process call create \"adb shell su\"");
                String PID = output.substring(output.indexOf("ProcessId = "), output.indexOf(";")).replaceAll("ProcessId = ", "");
                String process = executeCommand("taskkill /PID " + PID);
                if(process != null && process.contains("SUCCESS")){
                    executeCommand("taskkill /PID " + PID);
                    ROOTED_DEVICE = true;
                }else ROOTED_DEVICE = false;
                QAManager.log.debug("Is device Rooted: "+ROOTED_DEVICE);
            }
        }else {
            QAManager.log.debug("Cant get root status on Mac. Will set root to false by default");
            ROOTED_DEVICE = false;
        }
        return ROOTED_DEVICE;
    }

    public static boolean isLocked(){
        return DeviceManager.getDriver().isLocked();
    }

    public static void unlockDevice(){
        if(isLocked()){
            executeCommand("adb shell am start -n io.appium.unlock/.Unlock");
            //executeCommand("adb shell input keyevent 26");
            //executeCommand("adb shell input keyevent 82");
            Utilities.Wait(2000);
            new Android().pressBackButton();
            QAManager.log.debug("Device is UnLocked");
        }
    }

    public static void lockDevice(){
        if(!isLocked()){
            //DeviceManager.getDriver().lockScreen();
            executeCommand("adb shell input keyevent 26");
            Utilities.Wait(2000);
            QAManager.log.debug("Device is Locked");
        }
    }

    public static String getActivityInFocus() {
        String output =  executeCommand("adb shell dumpsys window windows | grep mCurrentFocus");
        //QAManager.log.debug("Activity in focus: " + output.trim());
        return output;
    }

    public static int getAppVersion(){

        if(HOTSPOT_SHIELD_VERSION == null || HOTSPOT_SHIELD_VERSION == 0) if (getAppPackageId() != null) {
            String output = executeCommand("adb shell dumpsys package " + getAppPackageId() + " | grep -i versioncode");
            if (output.contains("versionCode=")) {
                HOTSPOT_SHIELD_VERSION = Integer.parseInt(output.substring(output.indexOf("="), output.indexOf(" t")).replace("=", ""));
                QAManager.log.debug("Hotspot Shield Version is: " + HOTSPOT_SHIELD_VERSION);}
        } else {
            HOTSPOT_SHIELD_VERSION = 0;
            QAManager.log.warn("Hotspot Shield is not installed!");}
        return HOTSPOT_SHIELD_VERSION;
    }

    public static boolean isHotspotShieldInstalled(){
        String output = executeCommand("adb shell pm list packages | grep hotspotshield.android.vpn");
        if(output != null && output.contains("hotspotshield.android.vpn")) return true;
        else {
            //nullAppPackageId();
            return false;
        }
    }

    public static boolean isAppInstalled(String packageID){
        String output = executeCommand("adb shell pm list packages | grep "+packageID);
        if(output != null && output.contains(packageID)){
            QAManager.log.debug("App is installed: " +output.trim());
            return true;
        }else{
            return false;
        }
    }

    public static String getAppPackageId(){
        /*
        if(isEmpty(HOTSPOT_SHIELD_PACKAGE_NAME)) {
            if (isHotspotShieldInstalled()) {
                HOTSPOT_SHIELD_PACKAGE_NAME = executeCommand("adb shell pm list packages | grep hotspotshield.android.vpn").replaceAll("package:", "").trim();
                QAManager.log.debug("Hotspot Shield Package ID is: " + HOTSPOT_SHIELD_PACKAGE_NAME);
            } else QAManager.log.debug("Hotspot Shield is not installed");
        }
        return HOTSPOT_SHIELD_PACKAGE_NAME;*/
        return "hotspotshield.android.vpn.debug";
    }

    public static void nullAppPackageId(){
        HOTSPOT_SHIELD_PACKAGE_NAME = null;
        QAManager.log.debug("App Package ID was set to: " + HOTSPOT_SHIELD_PACKAGE_NAME);
    }

    public static ViewHome openHotspotShield(){
        //if(!getActivityInFocus().contains(Activity.HOTSPOTSHIELD_HOME_VIEW)){
            //QAManager.log.debug("Hotspot Shield is not in focus, launching the app!");
            QAManager.log.debug("Opening Hotspot Shield");
            new Android().pressBackButton();
            try{DeviceManager.getDriver().hideKeyboard(); QAManager.log.debug("Keyboard hidden");}catch (Exception e) {QAManager.log.debug("Keyboard is not open, nothing to hide!");}
            openApp(getAppPackageId(), Activity.HOTSPOTSHIELD_HOME_VIEW);
            //ViewHome.assertUiNotDisconnectedFromService();
        //}
        return new ViewHome().waitForViewToLoad();
    }

    public static ViewOnBoarding openHotspotShieldFirstTime(){
        openApp(getAppPackageId(), Activity.HOTSPOTSHIELD_HOME_VIEW);
        return new ViewOnBoarding().waitForViewToLoad();
    }

    public static AndroidNotifications openNotifications(){
        if(!getActivityInFocus().contains("com.android.systemui:id/notification_panel")) {
            QAManager.log.debug("Notifications are not in focus, opening notifications");
            DeviceManager.getDriver().openNotifications();
        }
        return new AndroidNotifications().waitForViewToLoad();
    }

    public static AndroidSettingsWiFi openSettingsWifi(){
        if(!getActivityInFocus().contains(Activity.WIFI_SETTINGS)){
            QAManager.log.debug("WiFi Settings are not in focus, opening WiFi settings");
            openApp(Package.SETTINGS, Activity.WIFI_SETTINGS);
        }
        return new AndroidSettingsWiFi().waitForViewToLoad();
    }

    public static AndroidSettingsData openSettingsData(){
        if(!getActivityInFocus().contains(Activity.DATA_SETTINGS)){
            QAManager.log.debug("Data Settings are not in focus, opening Data settings");
            openApp(Package.SETTINGS, Activity.DATA_SETTINGS);
        }
        return new AndroidSettingsData().waitForViewToLoad();
    }

    public static void turnCellDataOn(){
        if(!isCellDataOn()){
            QAManager.log.debug("Turning on Cell Data...");
            openSettingsData().enableData().closeSettings();
            QAManager.log.debug("Cell Data turned on");
            return;
        }QAManager.log.debug("Cell Data is already on");
    }

    public static void turnCellDataOff(){
        if(isCellDataOn()){
            QAManager.log.debug("Turning off Cell Data...");
            openSettingsData().disableData().closeSettings();
            QAManager.log.debug("Cell Data turned off");
            return;
        }QAManager.log.debug("Cell Data is already off");
    }

    public static void turnWifiOn(){
        if(!isWifiOn()){
            openSettingsWifi().enableWifi().closeSettings();
            QAManager.log.debug("WiFi turned on");
            return;
        }QAManager.log.debug("WiFi is already on");
    }

    public static void turnWifiOff(){
        if(isWifiOn()){
            openSettingsWifi().disableWifi().closeSettings();
            QAManager.log.debug("WiFi turned off");
            return;
        }QAManager.log.debug("WiFi is already off");
    }

    public static void turnAirplaneModeOn(){
        if(!isAirplaneModeOn()){
            NetworkConnectionSetting networkConnectionSetting = DeviceManager.getDriver().getNetworkConnection();
            networkConnectionSetting.setWifi(true);
            DeviceManager.getDriver().setNetworkConnection(networkConnectionSetting);
            QAManager.log.debug("Airplane Mode turned on");
            return;
        }QAManager.log.debug("Airplane Mode is already on");
    }
    public static void turnAirplaneModeOff(){
        if(isAirplaneModeOn()){
            NetworkConnectionSetting networkConnectionSetting = DeviceManager.getDriver().getNetworkConnection();
            networkConnectionSetting.setWifi(false);
            DeviceManager.getDriver().setNetworkConnection(networkConnectionSetting);
            QAManager.log.debug("Airplane Mode turned on");
            return;
        }QAManager.log.debug("Airplane Mode is already on");
    }

    public void grantVpnPermissions(){
        if(AndroidDialogVpnPermissions.flagExpectPermissions){
            if( AndroidDialogVpnPermissions.isShown()){
                QAManager.log.debug("Permissions for the VPN were requested");
                new AndroidDialogVpnPermissions().tapAccept();
            }else QAManager.log.debug("Permissions for the VPN were not requested");
        }
    }

    public static void revokeVpnPermissions(){
        if(isVpnConnected()){
            AndroidNotifications androidNotifications = openNotifications();
            androidNotifications.tapVpn().tapDisconnect();
            AndroidDialogVpnPermissions.flagExpectPermissions = true;
        }else QAManager.log.debug("Cannot revoke permissions due to VPN not being connected. Connect to VPN first!");
    }

    public static void resetHotspotShield() {
        QAManager.log.debug("Resetting Hotspot Shield");
        Android.revokeVpnPermissions();
        clearData(getAppPackageId());
        QAManager.resetPreset();
        ViewHome.firstLaunch = true;
        FailManager.UI_DISCONNECTED_FROM_SERVICE = false;
    }

    public static void openApp(String Package, String Activity){
        if(isAppInstalled(Package)){
            if(AndroidNotifications.isOpen() || AndroidQuickSettings.isOpen()) {
                QAManager.log.debug("Notification Menu is open, closing it prior to opening an activity");
                new Android().pressHomeButton();
            }
            SystemManager.executeCommand("adb shell am start -c android.intent.category.LAUNCHER -a android.intent.action.MAIN -n " +Package+"/"+Activity);
            QAManager.log.debug("Opened Activity: " + Package + "/" + Activity);
            //if(ErrorDialog.isShown()) new ErrorDialog().closeDialog();
        }else throw new AssertionError("Application: " + Package + " is not installed!");
    }

    public static void forceStopApp(String packageName){
        SystemManager.executeCommand("adb shell am force-stop " + packageName);
        QAManager.log.debug("Force Stopped: " + packageName);
        Utilities.Wait(1000);
    }

    public static void clearData(String packageName){
        SystemManager.executeCommand("adb shell pm clear " + packageName);
        QAManager.log.debug("Data was Cleared for: " + packageName);
        Utilities.Wait(1000);
    }

    public static ViewOnBoarding installHotspotShield(String pathToFile) throws ZipException {
        QAManager.log.debug("Installing Hotspot Shield...");
        uninstallHotspotShield();
        String targetDir = "BuildFiles";
        String output;
        try {
            if (pathToFile.contains(".apk")) {
                QAManager.log.debug("Installing APK from: " + pathToFile);
                output = SystemManager.executeCommand("adb install " + pathToFile);
                if (!output.contains("Success")) throw new AssertionError("Installation Failed: " + output);
            } else {
                SystemManager.unzip(new File(pathToFile), targetDir, null);
                ArrayList dirContent = SystemManager.getDirectoryContent(targetDir);
                for (Object item : dirContent) {
                    if (item.toString().contains(".apk")) {
                        String file = item.toString();
                        if (file.contains(DatabaseManager.INSTALL)) pathToFile = item.toString();}}
                QAManager.log.debug("Installing APK from: " + pathToFile);
                output = SystemManager.executeCommand("adb install " + pathToFile);
                if (!output.contains("Success")) throw new AssertionError("Installation Failed: " + output);
            }
            QAManager.log.debug("Package was installed successfully!");
            ViewHome.firstLaunch = true;
            //openHotspotShieldFirstTime();
            return new ViewOnBoarding();
        }finally{
            try {FileUtils.deleteDirectory(new File(targetDir));}
            catch (IOException e) {e.printStackTrace();}
            QAManager.log.debug("Done");
        }
    }

    public static void forceStopHotspotShiled(){
        forceStopApp(getAppPackageId());
    }

    public static void uninstallHotspotShield(){
        if(isHotspotShieldInstalled()){
            QAManager.log.debug("Uninstalling Hotspot Shield...");
            SystemManager.executeCommand("adb uninstall " + getAppPackageId());
            QAManager.log.debug("Uninstalled: " + getAppPackageId());
            return;
        }
        QAManager.log.debug("App is not installed");
    }

    public static void uninstallApp(String packageID){
        if(isAppInstalled(packageID)){
            QAManager.log.debug("Uninstalling "+packageID+"...");
            SystemManager.executeCommand("adb uninstall " + packageID);
            QAManager.log.debug("Uninstalled: " + packageID);
            return;
        }
        QAManager.log.debug("App is not installed");
    }

    public static void clearLogBuffer() {
        SystemManager.executeCommand("adb logcat -c");
        QAManager.log.debug("Cleared log buffer!");
    }

    public void pressBackButton(){
        QAManager.log.debug("Pressing Back button");
        DeviceManager.getDriver().pressKeyCode(AndroidKeyCode.BACK);
        Utilities.Wait(500);
    }

    public AndroidDesktop pressHomeButton(){
        QAManager.log.debug("Pressing Home Button");
        DeviceManager.getDriver().pressKeyCode(AndroidKeyCode.BACK);
        return new AndroidDesktop();
    }
}
