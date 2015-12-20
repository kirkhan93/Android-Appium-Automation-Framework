package api.hotspotshield.v400;

import api.android.Android;
import api.android.AndroidDialogVpnPermissions;
import api.android.AndroidSettingsAppsPermissions;
import managers.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.NoSuchElementException;
import utilities.UiObject;
import utilities.UiSelector;
import utilities.Utilities;

import java.sql.SQLException;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class ViewHome extends Android{

    private static int thresholdConnectClickFailed = 2;

    private static UiObject

            CONNECT_BUTTON          = null,
            TIMER                   = null,
            LOCATION_SELECTOR       = null,
            ADD_APPS_BUTTON         = null,
            MENU_ICON               = null,
            PW_ICON                 = null,
            SHARE_ICON              = null,
            UPSELL_BUTTON           = null,
            TITLE                   = null,
            APP_COUNT               = null,
            APPS_STATUS             = null,
            NETWORK_LABEL           = null,
            DEBUG_MESSAGE           = null,
            SITES_STATUS            = null,
            GRANT_BUTTON            = null,
            OK_BUTTON               = null;

    public static int DEAD_TIME = 0;

    public static boolean firstLaunch = false;

    private static UiObject connectButton(){
        if(CONNECT_BUTTON == null) CONNECT_BUTTON = new UiSelector().resourceId(getAppPackageId() + ":id/view_home_btn_connect_text").makeUiObject();
        return CONNECT_BUTTON;
    }

    private static UiObject timer(){
        if(TIMER == null) TIMER = new UiSelector().resourceId(getAppPackageId()+":id/view_home_btn_connect_chrono").makeUiObject();
        return TIMER;
    }

    private static UiObject locationSelector(){
        if(LOCATION_SELECTOR == null) LOCATION_SELECTOR = new UiSelector().resourceId(getAppPackageId()+":id/view_home_select_vl").makeUiObject();
        return LOCATION_SELECTOR;
    }

    private static UiObject addAppsButton(){
        if(ADD_APPS_BUTTON == null) ADD_APPS_BUTTON = new UiSelector().resourceId(getAppPackageId()+":id/add_more").makeUiObject();
        return ADD_APPS_BUTTON;
    }

    private static UiObject menuIcon(){
        if(MENU_ICON == null) MENU_ICON = new UiSelector().resourceId("android:id/home").makeUiObject();
        return MENU_ICON;
    }

    private static UiObject pwIcon(){
        if(PW_ICON == null) PW_ICON = new UiSelector().resourceId(getAppPackageId()+":id/view_home_pw_lite").makeUiObject();
        return PW_ICON;
    }

    private static UiObject shareIcon(){
        if(SHARE_ICON == null) SHARE_ICON = new UiSelector().resourceId(getAppPackageId()+":id/view_home_share").makeUiObject();
        return SHARE_ICON;
    }

    private static UiObject upsellButton(){
        if(UPSELL_BUTTON == null) UPSELL_BUTTON= new UiSelector().resourceId(getAppPackageId()+":id/view_home_footer_buy").makeUiObject();
        return UPSELL_BUTTON;
    }

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().resourceId(getAppPackageId()+":id/view_home_title").makeUiObject();
        return TITLE;
    }

    private static UiObject appCount(){
        if(APP_COUNT == null) APP_COUNT = new UiSelector().resourceId(getAppPackageId()+":id/view_home_tv_apps_secured_num").makeUiObject();
        return APP_COUNT;
    }

    private static UiObject appsStatus(){
        if(APPS_STATUS == null) APPS_STATUS = new UiSelector().resourceId(getAppPackageId()+":id/view_home_tv_apps_secured_label").makeUiObject();
        return APPS_STATUS;
    }

    private static UiObject networkLabel(){
        if(NETWORK_LABEL == null) NETWORK_LABEL = new UiSelector().resourceId(getAppPackageId()+":id/view_home_tv_network_label").makeUiObject();
        return NETWORK_LABEL;
    }

    private static UiObject debugMessage(){
        if(DEBUG_MESSAGE == null) DEBUG_MESSAGE = new UiSelector().resourceId(getAppPackageId()+":id/view_home_debug_message").makeUiObject();
        return DEBUG_MESSAGE;
    }

    private static UiObject sitesStatus(){
        if(SITES_STATUS == null) SITES_STATUS = new UiSelector().resourceId(getAppPackageId()+":id/view_home_tv_sites_label").makeUiObject();
        return SITES_STATUS;
    }

    private static UiObject grantButton(){
        if(GRANT_BUTTON == null) GRANT_BUTTON = new UiSelector().resourceId(getAppPackageId()+":id/custom_dlg_positiveButton").makeUiObject();
        return GRANT_BUTTON;
    }

    private static UiObject okButton(){
        if(OK_BUTTON == null) OK_BUTTON= new UiSelector().resourceId("android:id/button1").makeUiObject();
        return OK_BUTTON;
    }

    public static boolean isShown(){
        QAManager.log.debug("Checking if ViewHome is shown");
        return title().exists();
    }

    public MenuDrawer openMenuDrawer(){
        QAManager.log.debug("Opening Menu Drawer");
        try{
            if(!MenuDrawer.isOpen()) menuIcon().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't open Menu Drawer, Element absent");
        }
        return new MenuDrawer().waitForDrawerToOpen();
    }

    public ViewInviteSDK openInviteSDK(){
        QAManager.log.debug("Opening Invite SDK");
        try{
            shareIcon().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't open Invite SDK, Element absent");
        }
        return new ViewInviteSDK().waitForViewToLoad();
    }

    public ViewAppLock openAppLock(){
        QAManager.log.debug("Opening App Lock Lite");
        try{
            pwIcon().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't open App Lock Lite, Element absent");
        }
        return new ViewAppLock().waitForViewToLoad();
    }

    public DialogLocationSelector openLocationSelector(){
        QAManager.log.debug("Opening Location Selector");
        try{
            locationSelector().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't open Location Selector, Element absent");
        }
        return new DialogLocationSelector().waitForDialogToLoad();
    }

    public ViewUpSell openUpSell(){
        QAManager.log.debug("Opening Up-Sell");
        try{
            upsellButton().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't open Up-Sell, Element absent");
        }
        return new ViewUpSell().waitForViewToLoad();
    }

    public ViewAppList openAppList(){
        QAManager.log.debug("Opening App List");
        try{
            addAppsButton().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't open App List, Element absent");
        }
        if(new AndroidSettingsAppsPermissions().grantAppsPermissions()) openAppList();
        return new ViewAppList().waitForViewToLoad();
    }

    public ViewHome tapConnectButton(){
        while(DEAD_TIME != 0) Utilities.Wait(500);
        if(!isVpnConnected()){
            QAManager.log.debug("Tapping Connect Button");
            try{
                connectButton().doubleTap();
            }catch (NoSuchElementException e){
                if(thresholdConnectClickFailed == 0){
                    forceStopHotspotShiled();
                    thresholdConnectClickFailed = 2;
                    openHotspotShield();
                    tapConnectButton();
                }
                else {
                    thresholdConnectClickFailed--;
                    assertUiNotDisconnectedFromService();
                }
                throw new AssertionError("Can't tap connect button, Element absent");
            }
            QAManager.log.debug("Tapped connect button, client should be connecting...");
            MonitoringManager.start(MonitoringManager.connectionDeadTimer());
            connectionAlgorithm();
        }else QAManager.log.debug("Client is already connected");
        return this;
    }

    public ViewHome tapDisconnectButton(){
        while(DEAD_TIME != 0) Utilities.Wait(500);
        if(isVpnConnected()){
            QAManager.log.debug("Tapping Disconnect Button");
            try{
                timer().doubleTap();
            }catch (NoSuchElementException e){
                assertUiNotDisconnectedFromService();
                throw new AssertionError("Can't tap disconnect button, Element absent");
            }
            QAManager.log.debug("Tapped timer, client should be disconnecting...");
            MonitoringManager.start(MonitoringManager.connectionDeadTimer());
            disconnectionAlgorithm();
        }else QAManager.log.debug("Client is already disconnected");
        return this;
    }

    public String getTimerText(){
        return timer().text();
    }

    public int getAppCount(){
        return Integer.parseInt(appCount().text());
    }

    public String getNetworkID(){
        QAManager.log.debug("Getting Network ID");
        return networkLabel().text().trim();
    }

    public boolean isConnectedTo(String ssid){
        return getNetworkID().equals(ssid);
    }

    public boolean appsSecured(){
        QAManager.log.debug("Checking if apps are secured");
        return appsStatus().text().equals("Apps Secured");
    }

    public boolean sitesUnblocked(){
        QAManager.log.debug("Checking if Sites UnBlocked");
        if(sitesStatus().exists()) return sitesStatus().text().equals("Sites Unblocked");
        else return false;
    }

    public boolean sitesBlocked(){
        QAManager.log.debug("Checking if Sites are Blocked");
        if(sitesStatus().exists()) return sitesStatus().text().equals("Some Sites Blocked");
        else return false;
    }

    public boolean timerPresent(){
        QAManager.log.debug("Checking if Timer is shown");
        //return new UiSelector().textMatches(Utilities.TIMER_REGEX).exists();
        return timer().exists();
    }

    public boolean appEnabled(String appName){
        return new UiSelector().text(appName).resourceId(getAppPackageId() + ":id/name").exists();
    }

    public ViewHome verifyAppEnabled(String appName){
        QAManager.log.debug("Verifying that application: " + appName+" is enabled");
        if(new UiSelector().text(appName).exists()) return this;
        else throw new AssertionError("App: "+appName+" was not enabled");
    }

    public ViewHome verifyAppDisabled(String appName){
        QAManager.log.debug("Verifying that application: " + appName+" is disabled");
        if(new UiSelector().text(appName).exists()) throw new AssertionError("App: "+appName+" is still enabled");
        else return this;
    }

    public boolean upSellButtonPresent(){
        QAManager.log.debug("Checking if Upsell Button is shown");
        return upsellButton().exists();
    }

    public JSONObject getDebugInfo() throws JSONException {
        QAManager.log.debug("Getting debug info");
        if(!debugMessage().exists()){
            openHotspotShield();
            pressBackButton();
            openHotspotShield();
        }
        assertUiNotDisconnectedFromService();
        String debugInfo = debugMessage().text(), simpleString = "";
        String[] list = debugInfo.split("\n"), badChars = new String[]{"t=","di=","d=","cd=","deb=","type=","st=", "'"};
        for(String i:list){
            for(String chr : badChars) i = i.replaceAll(chr, "");
            simpleString += i.trim() + ", ";
        }
        String[] data = simpleString.split(", ");
        return new JSONObject()
                .put("hash", data[0])
                .put("device_id", data[1])
                .put("country", data[2])
                .put("domain", data[3])
                .put("server_ip", data[4].replace("c", ""))
                .put("debug_server_ip", data[5])
                .put("vpn_mode", data[6])
                .put("state", data[7]);
    }

    public String getDebugHost() {
        QAManager.log.debug("Getting debug host");
        JSONObject json = null;
        try {json = getDebugInfo();} catch (JSONException e) {e.printStackTrace();}
        String IP = null;
        try {DatabaseManager.IP_IN = (String) json.get("server_ip");} catch (JSONException e) {e.printStackTrace();}
        try {return DatabaseManager.resolveIPToHost(DatabaseManager.IP_IN);} catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {throw new AssertionError("Failed to get Host for IP: " + IP);}
    }

    private String getColo(){
        QAManager.log.debug("Auto checking Colo");
        String data = DialogLocationSelector.CURRENT_COLO = locationSelector().text().toLowerCase();
        String colo = "";
        for(String i : data.split(" ")){
            colo = colo + Character.toUpperCase(i.charAt(0)) + i.substring(1)+" ";
        }
        return colo.trim();
    }

    public ViewHome verifyLocationChange(String expectedLocation){
        String actualLocation = getColo();
        QAManager.log.debug("Verifying that location changed, expecting: "+expectedLocation+" actual: "+actualLocation);
        if(!actualLocation.equals(expectedLocation)) {
            ReportingManager.addVlFailedToChange();
            throw new AssertionError("Location did not change. Expected: "+expectedLocation+" but was: "+actualLocation);
        }
        return this;
    }

    public void exitView(){
        QAManager.log.debug("Exiting Home View");
        pressBackButton();
    }

    public boolean connected(){
        return isVpnConnected();
    }

    public ViewHome connectionAlgorithm(){
        QAManager.log.debug("Starting Connection Algorithm");
        Thread timer = new MonitoringManager().timer(35);
        MonitoringManager.start(timer);
        try {
            DialogFeedback.waitForFeedbackDialog(10);
            while (!MonitoringManager.timeUp) {
                grantVpnPermissions();
                if (ErrorDialog.isShown()) {
                    QAManager.log.error("Error was thrown during connection!");
                    throw new AssertionError("Error Thrown on Connect: " + new ErrorDialog().getErrorDescription());
                }
                if (ViewHomeMidPager.isShowingAds()) new ViewHomeMidPager().swipeToInfoState();
                AdView.waitForAd();
                if (sitesUnblocked()) break;
                else{
                    if (MonitoringManager.timeUp) {
                        ReportingManager.addCouldntConnect();
                        ReportingManager.addAnimationIssue();
                        throw new AssertionError("Client failed to Connect");
                    }
                }
            }
        }finally {
            if(!timer.isInterrupted()) timer.interrupt();
        }
        if(!appsSecured()) ReportingManager.addAnimationIssue();
        if(!sitesUnblocked()) ReportingManager.addAnimationIssue();
        if(!timerPresent()) {
            ReportingManager.addCouldntConnect();
            ReportingManager.addAnimationIssue();
            throw new AssertionError("Client failed to Connect");
        }
        if(!isVpnConnected()){
            ReportingManager.addCouldntConnect();
            throw new AssertionError("Client failed to Connect");
        }
        AndroidDialogVpnPermissions.flagExpectPermissions = false;
        QAManager.log.debug("Connected Successfully!");
        return this;
    }

    public ViewHome disconnectionAlgorithm(){
        QAManager.log.debug("Starting Disconnection Algorithm");
        Thread timer = new MonitoringManager().timer(35);
        MonitoringManager.start(timer);
        try {
            for(int ls=10; ls!=0; ls--){
                if(ViewHomeMidPager.isShowingAds()) new ViewHomeMidPager().swipeToInfoState();
                AdView.waitForAd();
                if(sitesBlocked()) break;
                else{
                    if (MonitoringManager.timeUp) {
                        ReportingManager.addCouldntConnect();
                        ReportingManager.addAnimationIssue();
                        throw new AssertionError("Client failed to Connect");
                    }
                }
            }
        }finally {
            if(!timer.isInterrupted()) timer.interrupt();
        }
        if(appsSecured()) ReportingManager.addAnimationIssue();
        if(sitesUnblocked()) ReportingManager.addAnimationIssue();
        if(timerPresent()) {
            ReportingManager.addCouldntDisconnect();
            ReportingManager.addAnimationIssue();
            throw new AssertionError("Client failed to disconnect");
        }
        if(isVpnConnected()){
            ReportingManager.addCouldntDisconnect();
            throw new AssertionError("Client failed to disconnect");
        }
        QAManager.log.debug("Disconnected Successfully!");
        return this;
    }

    public static void assertUiNotDisconnectedFromService(){
        QAManager.log.debug("Checking if Client is in sync with the Service...");
        if(ViewHomeMidPager.isShowingAds()) new ViewHomeMidPager().swipeToInfoState();
        boolean trigger1 = isVpnConnected();
        boolean trigger2 =  new ViewHome().timerPresent() && new ViewHome().sitesUnblocked();
        if(trigger1 != trigger2){
            FailManager.UI_DISCONNECTED_FROM_SERVICE = true;
            ReportingManager.addUiServiceDisconnect();
            throw new AssertionError("Ui Disconnected from Service! VPN Connected: "+trigger1+" but UI Connected: "+trigger2);
        }else{
            FailManager.UI_DISCONNECTED_FROM_SERVICE = false;
        }
    }

    public ViewHome waitForViewToLoad(){
        for(int ls=5; ls!=0; ls--){
            QAManager.log.debug("Waiting for Home View to load: " +ls+" seconds left");
            if(ErrorDialog.isShown()) new ErrorDialog().closeDialog();
            if(title().exists()) break;
            new AndroidSettingsAppsPermissions().grantAppsPermissions();
        }
        DialogLocationSelector.CURRENT_COLO = getColo();
        return this;
    }

    public ViewHome waitForViewToLoad(int seconds){
        for(int ls=seconds; ls!=0; ls--){
            QAManager.log.debug("Waiting for Home View to load: " +ls+" seconds left");
            if(ErrorDialog.isShown()) new ErrorDialog().closeDialog();
            if(title().exists()) break;
            new AndroidSettingsAppsPermissions().grantAppsPermissions();
        }
        DialogLocationSelector.CURRENT_COLO = getColo();
        return this;
    }
}