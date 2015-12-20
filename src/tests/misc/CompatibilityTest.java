package tests.misc;

import api.android.Android;
import api.android.AndroidSystemUpdate;
import api.googlechrome.GoogleChrome;
import api.googleplay.GooglePlay;
import api.googleplay.GooglePlayApplicationPage;
import api.hotspotshield.v400.AdView;
import api.hotspotshield.v400.ViewAccount;
import api.hotspotshield.v400.ViewHome;
import api.youtube.YouTube;
import io.appium.java_client.android.AndroidKeyCode;
import managers.DeviceManager;
import managers.QAManager;
import managers.ReportingManager;
import managers.SystemManager;
import net.lingala.zip4j.exception.ZipException;
import org.apache.log4j.Level;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sikuli.script.FindFailed;
import utilities.TestInfo;
import utilities.Utilities;
import utilities.constants.*;

import java.io.IOException;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/2/2015.
 */
public class CompatibilityTest {
    private static TestInfo testInfo = new TestInfo();
    private static final boolean getDriverFirst = true;

    @AfterClass
    public static void tearDown(){

        if(getDriverFirst) DeviceManager.getDriver().quit();
    }
    @BeforeClass
    public static void install() throws ZipException {
        testInfo.suite("Compatibility Check").execType("criticClient");
        QAManager.log.setLevel(Level.ALL);
        if(getDriverFirst){
            DeviceManager.getDriver();
            SystemManager.addShutdownHook();
        }
    }

    @Before
    public void preset(){
       Android.openHotspotShield();
    }

    @AfterClass
    public static void conclusion(){
        DeviceManager.getDriver().quit();
    }

    @Test
    public void testGoogleChrome() throws IOException, FindFailed {
        testInfo.name("Google Chrome").expected("GoogleChrome class is working as expected");
        GoogleChrome googleChrome = new GoogleChrome();
        googleChrome.open(Website.HTTPS_SHOWNETWORKIP).getIpAddress(true);
        googleChrome.getCurrentUrl();
        googleChrome.open(Website.BBC_IPLAYER);
        googleChrome.getCurrentUrl();
        googleChrome.open(Website.HTTPS_BRAINRADIATION).getIpAddress(true);
        googleChrome.getCurrentUrl();
        googleChrome.open(Website.HTTP_X_AF_TUNNEL);
        googleChrome.getCurrentUrl();
        googleChrome.open(Website.BRAINRADIATION).getIpAddress(true);
        googleChrome.getCurrentUrl();
        googleChrome.reset();
    }

    @Test
    public void testHomeView(){
        testInfo.name("Home View").expected("ViewHome class is working as expected");
        new ViewAccount().signOut();
        new ViewHome()
                .openAppLock().exitView()
                .openAppList().exitView()
                .openUpSell().exitView()
                .openInviteSDK().exitView()
                .openMenuDrawer().closeDrawer()
                .openLocationSelector().closeDialog()
                .tapConnectButton().tapDisconnectButton()
                .tapDisconnectButton().tapDisconnectButton()
                .tapConnectButton().tapDisconnectButton()
                .tapConnectButton().tapDisconnectButton();
        System.out.println(ReportingManager.getReport());
    }

    @Test
    public void testMenuDrawer(){
        testInfo.name("Menu Drawer").expected("MenuDrawer class is working as expected");
        new ViewHome()
                .openMenuDrawer().openAccount().exitView()
                .openMenuDrawer().openAppList().exitView()
                .openMenuDrawer().openHelp().exitView()
                .openMenuDrawer().openNetworks().exitView()
                .openMenuDrawer().openAbout().exitView()
                .openMenuDrawer().openGeneralSettings().exitView()
                .openMenuDrawer().tapDebugMenu().closeDialog()
                .openMenuDrawer().tapDebugTestAds().closeDialog();
    }

    @Test
    public void testNetworks(){
        testInfo.name("Networks").expected("ViewNetworks class is working as expected");
        new ViewHome()
                .openMenuDrawer().openNetworks()
                    .disableNetwork("Unsecured Wi-Fi")
                    .enableNetwork("Unsecured Wi-Fi")
                    .disableNetwork("Secured Wi-Fi")
                    .enableNetwork("Secured Wi-Fi")
                    .disableNetwork("Mobile Networks")
                    .enableNetwork("Mobile Networks")
                    .disableNetwork("Other Networks")
                    .enableNetwork("Other Networks")
                .exitView();
    }

    @Test
    public void testGeneralSettings(){
        testInfo.name("General Settings").expected("ViewGeneralSettings class is working as expected");
        new ViewHome()
                .openMenuDrawer().openGeneralSettings()
                .setVpnMode("HYDRA")
                .enterDebugInfo(SettingItem.VPN_SERVER, "")
                .enterDebugInfo(SettingItem.DOMAIN, "")
                .enableSettings(SettingItem.RESOLVE_DOMAINS)
                .disableSettings(SettingItem.SHOW_POP_UPS);
    }

    @Test
    public void testLocatorSelector(){
        testInfo.name("Locator Selector").expected("DialogLocatorSelector class is working as expected");
        new ViewAccount().signOut();
        new ViewHome()
                .openLocationSelector()
                    .selectLocationAsFreeUser(Location.JAPAN).exitView()
                .openLocationSelector()
                    .selectLocationAsFreeUser(Location.AUSTRALIA).exitView()
                .openLocationSelector()
                    .selectLocationAsFreeUser(Location.UNITED_KINGDOM).exitView();
    }

    @Test
    public void testAppList(){
        testInfo.name("App List").expected("ViewAppList class is working as expected");
        new ViewHome().openAppList()
                .enableApplication("Chrome")
                .enableApplication("YouTube")
                .enableApplication("Facebook")
                //.enableApplication("WhatsApp")
        .exitView().openAppList()
                .disableApplication("Chrome")
                .disableApplication("YouTube")
                .disableApplication("Facebook")
                //.disableApplication("WhatsApp")
        .exitView();
    }

    @Test
    public void testAccount(){
        testInfo.name("Account").expected("ViewAccount class is working as expected");
        ViewAccount viewAccount = new ViewAccount();
        new ViewHome()
                .openMenuDrawer()
                .openAccount()
                .tapForgotPassword()
                .exitView()
                .tapSignUpTab()
                .tapSignInTab()
                .exitView();
        viewAccount.signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword());
        viewAccount.signInToAccountWithExhaustedDeviceLimit().closeDialog();
        DeviceManager.getDriver().pressKeyCode(AndroidKeyCode.BACK);
        viewAccount.signInAsFree(Credentials.freeUsername(), Credentials.freePassword());
        viewAccount.signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword());
    }

    @Test
    public void testNetworkConnections(){
        testInfo.name("Network management").expected("Android Network settings are changed successfully");
        Android.openSettingsWifi().connectTo(Network.ASTAnash).closeSettings();
        Android.turnWifiOff();
        Android.turnWifiOff();
        Android.turnWifiOn();
        Android.turnWifiOn();
        Android.turnCellDataOff();
        Android.turnCellDataOff();
        Android.turnCellDataOn();
        Android.turnCellDataOn();
        //Android.turnAirplaneModeOn();
        //Android.turnAirplaneModeOff();
    }

    @Test
    public void testPermissions(){
        testInfo.name("VPN Permissions").expected("Able to revoke and grant VPN permissions");
        Android.openHotspotShield().tapDisconnectButton();
        Android.revokeVpnPermissions();
        Android.openHotspotShield().tapConnectButton();
        Android.revokeVpnPermissions();
        Android.openHotspotShield().disconnectionAlgorithm().tapConnectButton();
    }

    @Test
    public void testNotifications(){
        testInfo.name("Notifications").expected("Notification API works as expected");
        Android.openNotifications().tapProtectNow();
        Android.openNotifications().tapProtectNow();
        Android.openNotifications().tapPause();
        Android.openNotifications().tapPause();
        Android.openNotifications().tapProtectNow();
        Android.openNotifications().tapPause();
        Android.openNotifications().tapProtectNow();
        Android.openNotifications().tapPause();
    }

    @Test
    public void testYouTube(){
        testInfo.name("YouTube").expected("YouTube API works as expected");
       new YouTube().open().clickSearchIcon().searchFor("Funny videos").tapVideo(1).isVideoLoaded();
    }

    @Test
    public void testGooglePlay(){
        testInfo.name("GooglePlay").expected("GooglePlay API works as expected");
        GooglePlayApplicationPage appPage = new GooglePlay().open().tapSearchField().searchFor("Clash of Clans").tapOn("Clash of Clans");
        System.out.println("App Title: " + appPage.getAppTitle());
        System.out.println("App Creator: " + appPage.getAppCreator());
        appPage.closeGooglePlay();
    }

    @Test
    public void testDriverResetTimeout(){
        testInfo.name("Driver Reset").expected("");
        if(DeviceManager.getDriver() != null) QAManager.log.debug("Driver exists! (1)");
        else QAManager.log.debug("Driver server closed! (1)");
        QAManager.log.debug("Waiting...");
        Utilities.Wait(110000);
        if(DeviceManager.getDriver() != null) QAManager.log.debug("Driver exists! (2)");
        else QAManager.log.debug("Driver server closed! (2)");
        DeviceManager.resetDriver();
        if(DeviceManager.getDriver() != null) QAManager.log.debug("Driver exists! (3)");
        else QAManager.log.debug("Driver server closed! (3)");
    }

    @Test
    public void testDriverReset(){
        testInfo.name("Driver Reset").expected("");
        DeviceManager.getDriver();
        DeviceManager.resetDriver();
        if(DeviceManager.getDriver() != null) QAManager.log.debug("Driver exists!");
        else QAManager.log.debug("Driver server closed!");
    }

    @Test
    public void testUnlock(){
        testInfo.name("Unlock").expected("Device Unlocks");
        Android.unlockDevice();
        System.out.println("Locked: " + Android.isLocked());
        Android.lockDevice();
        System.out.println("Locked: " + Android.isLocked());
        Android.unlockDevice();
        System.out.println("Locked: " + Android.isLocked());
    }

    @Test
     public void testSystemUpdateMonitoring(){
        while(!AndroidSystemUpdate.isShown()){
            QAManager.log.debug("Waiting for the system Update");
            Utilities.Wait(1000);
        }
        if(AndroidSystemUpdate.isShown()){
            QAManager.log.debug("System Update detected. Resetting update time");
            new AndroidSystemUpdate().tapInstallLater().selectTime().tapDone();
            QAManager.log.debug("Reset System Update time by 24 hours");
        }
    }

    @Test
    public void testAdMonitoring(){
        Android.openHotspotShield().openMenuDrawer().tapDebugTestAds();
        QAManager.log.debug("Waiting");
        Utilities.Wait(15000);
        if(AdView.isShown()){
            QAManager.log.debug("Ad detected. Closing");
            new Android().pressHomeButton();
            Android.openHotspotShield();
        }
    }
}