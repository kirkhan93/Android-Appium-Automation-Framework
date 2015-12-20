package tests.v400;

import api.android.Android;
import api.googlechrome.GoogleChrome;
import api.googleplay.GooglePlay;
import api.googleplay.GooglePlayApplicationPage;
import api.hotspotshield.v400.ErrorDialog;
import api.hotspotshield.v400.ViewAccount;
import api.hotspotshield.v400.ViewHome;
import api.ookla.speedtest.ViewBeginSpeedtest;
import api.ookla.speedtest.ViewSpeedtestResults;
import api.youtube.YouTube;
import managers.DatabaseManager;
import managers.QAManager;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;
import utilities.Utilities;
import utilities.constants.*;
import utilities.constants.Package;

/**
 * Created by Artur Spirin on 11/6/15.
 **/
public class SmokeCheck extends QAManager{

    private static TestInfo testInfo = new TestInfo();
    private static final String googlePlaySearchTerm = "Hotspot Shield VPN Privacy";
    private static final String searchTerm = "Clash of Clans";
    private static final String testApp = Package.CLASH_OF_CLANS;


    @BeforeClass
    public static void localPreset(){
        testInfo.suite("Smoke Check").execType("criticClient");
        if(DatabaseManager.TEST_SET.toLowerCase().contains("critic")) testInfo.execType("smokeServer");
    }

    @Test
    public void AC4_45(){
        runOnlyForThoseTypes("ANDROID_FUN && ANDROID_FUN_SMOKE");
        testInfo.name("Switch Networks WiFi -> WiFi").expected("Client reconnects");
        Android.openHotspotShield().tapConnectButton();
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1).connectTo(Network.AnchorFree_5GHz_Private_optout);
        Android.openHotspotShield();
        Utilities.Wait(5000);
        Assert.assertTrue("Client did not reconnect upon Network Change, WiFi to WiFi", new ViewHome().connected());
        testInfo.actual("Client reconnected");
    }

    @Test
    public void AC4_46(){
        runOnlyForThoseTypes("ANDROID_FUN && ANDROID_FUN_SMOKE"); Assume.assumeTrue(Android.isCarrierAvailable());
        testInfo.name("Switch Networks WiFi -> Cell").expected("Client reconnects");
        Android.openHotspotShield().tapConnectButton();
        Android.turnWifiOn();
        Android.turnWifiOff();
        Android.turnCellDataOn();
        Android.openHotspotShield();
        Utilities.Wait(5000);
        Assert.assertTrue("Client did not reconnect upon Network Change, WiFi to Cell", new ViewHome().connected());
        testInfo.actual("Client reconnected");
    }

    @Test
    public void AC4_47(){
        runOnlyForThoseTypes("ANDROID_FUN && ANDROID_FUN_SMOKE"); Assume.assumeTrue(Android.isCarrierAvailable());
        testInfo.name("Switch Networks Cell -> WiFi").expected("Client reconnects");
        Android.openHotspotShield().tapConnectButton();
        Android.turnWifiOff();
        Android.turnWifiOn();
        Android.openHotspotShield();
        Utilities.Wait(5000);
        Assert.assertTrue("Client did not reconnect upon Network Change, Cell to WiFi", new ViewHome().connected());
        testInfo.actual("Client reconnected");
    }

    @Test
    public void AC4_719() {
        Assume.assumeTrue(Android.isCarrierAvailable());
        testInfo.name("Mobile Network Speed - USA");
        Android.turnWifiOff();
        Integer downloadBefore = null;
        ViewSpeedtestResults speedtest;
        String ping, donwload, upload;
        if(doBaselineSpeedTest){
            Android.openHotspotShield().tapDisconnectButton();
            new ViewAccount().signOut();
            speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
            ping = speedtest.getRawPing();
            donwload = speedtest.getRawDownload();
            upload = speedtest.getRawUpload();
            downloadBefore = speedtest.getDownload();
            testInfo.expected("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
            doBaselineSpeedTest = false;
        }
        Android.openHotspotShield().tapConnectButton();
        try{if(isFunctionalRequest()) new ViewHome().getDebugHost();}catch (Exception e){/**This info is nice to have, but not worthy to fail the test over*/}
        speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
        ping = speedtest.getRawPing(); donwload = speedtest.getRawDownload(); upload = speedtest.getRawUpload();
        int downloadAfter = speedtest.getDownload();
        testInfo.actual("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
        ViewSpeedtestResults.assertDownloadResults(downloadBefore, downloadAfter);
    }

    @Test
    public void AC4_720() {
        testInfo.name("Secured Network Speed - USA");
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Integer downloadBefore = null;
        ViewSpeedtestResults speedtest;
        String ping, donwload, upload;
        if(doBaselineSpeedTest){
            Android.openHotspotShield().tapDisconnectButton();
            new ViewAccount().signOut();
            speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
            ping = speedtest.getRawPing();
            donwload = speedtest.getRawDownload();
            upload = speedtest.getRawUpload();
            downloadBefore = speedtest.getDownload();
            testInfo.expected("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
            doBaselineSpeedTest = false;
        }
        Android.openHotspotShield().tapConnectButton();
        try{if(isFunctionalRequest()) new ViewHome().getDebugHost();}catch (Exception e){/**This info is nice to have, but not worthy to fail the test over*/}
        speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
        ping = speedtest.getRawPing(); donwload = speedtest.getRawDownload(); upload = speedtest.getRawUpload();
        int downloadAfter = speedtest.getDownload();
        testInfo.actual("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
        ViewSpeedtestResults.assertDownloadResults(downloadBefore, downloadAfter);
    }

    @Test
    public void AC4_721() {
        testInfo.name("Unsecured Network Speed - USA");
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1).closeSettings();
        Integer downloadBefore = null;
        ViewSpeedtestResults speedtest;
        String ping, donwload, upload;
        if(doBaselineSpeedTest){
            Android.openHotspotShield().tapDisconnectButton();
            new ViewAccount().signOut();
            speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
            ping = speedtest.getRawPing();
            donwload = speedtest.getRawDownload();
            upload = speedtest.getRawUpload();
            downloadBefore = speedtest.getDownload();
            testInfo.expected("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
            doBaselineSpeedTest = false;
        }
        Android.openHotspotShield().tapConnectButton();
        try{if(isFunctionalRequest()) new ViewHome().getDebugHost();}catch (Exception e){/**This info is nice to have, but not worthy to fail the test over*/}
        speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
        ping = speedtest.getRawPing(); donwload = speedtest.getRawDownload(); upload = speedtest.getRawUpload();
        int downloadAfter = speedtest.getDownload();
        testInfo.actual("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
        ViewSpeedtestResults.assertDownloadResults(downloadBefore, downloadAfter);
    }

    @Test
    public void AC4_722() {
        Assume.assumeTrue(Android.isCarrierAvailable());
        testInfo.name("Install App via Cell Network - USA").expected("Able to install App");
        Android.turnWifiOff();
        Android.openHotspotShield().tapConnectButton();
        try{if(isFunctionalRequest()) new ViewHome().getDebugHost();}catch (Exception e){/**This info is nice to have, but not worthy to fail the test over*/}
        new ViewAccount().signOut();
        Android.uninstallApp(testApp);
        new GooglePlay().open().tapSearchField().searchFor(searchTerm).tapOn(searchTerm).tapInstall().acceptPermissions().waitForInstallToFinish();
        Assert.assertTrue("App did not install!", Android.isAppInstalled(testApp));
        testInfo.actual("App installed");
    }

    @Test
    public void AC4_723() {
        testInfo.name("Install App via Secured Network - USA").expected("Able to install App");
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapConnectButton();
        try{if(isFunctionalRequest()) new ViewHome().getDebugHost();}catch (Exception e){/**This info is nice to have, but not worthy to fail the test over*/}
        new ViewAccount().signOut();
        Android.uninstallApp(testApp);
        new GooglePlay().open().tapSearchField().searchFor(searchTerm).tapOn(searchTerm).tapInstall().acceptPermissions().waitForInstallToFinish();
        Assert.assertTrue("App did not install!", Android.isAppInstalled(testApp));
        testInfo.actual("App installed");
    }

    @Test
    public void AC4_724() {
        testInfo.name("Install App via Unsecured Network - USA").expected("Able to install App");
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapConnectButton();
        try{if(isFunctionalRequest()) new ViewHome().getDebugHost();}catch (Exception e){/**This info is nice to have, but not worthy to fail the test over*/}
        new ViewAccount().signOut();
        Android.uninstallApp(testApp);
        new GooglePlay().open().tapSearchField().searchFor(searchTerm).tapOn(searchTerm).tapInstall().acceptPermissions().waitForInstallToFinish();
        Assert.assertTrue("App did not install!", Android.isAppInstalled(testApp));
        testInfo.actual("App installed");
    }

    @Test
    public void AC4_593_1(){
        runOnlyForThoseTypes("ANDROID_FUN && ANDROID_FUN_SMOKE");
        testInfo.name("USA - YouTube Streaming").expected("Able to watch YouTube");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword()).openLocationSelector().selectLocationAsEliteUser(Location.UNITED_STATES).verifyLocationChange(Location.UNITED_STATES);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout);
        YouTube video = new YouTube().open().clickSearchIcon().searchFor("Funny Videos").tapVideo(1);
        Assert.assertTrue("Video did not play", video.isVideoLoaded());
        testInfo.actual("Video Played");
    }

    @Test
    public void AC4_593_2(){
        runOnlyForThoseTypes("ANDROID_FUN && ANDROID_FUN_SMOKE");
        testInfo.name("USA - Google Play Browsing (HTTPS)").expected("Able to browse HTTPS");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword()).openLocationSelector().selectLocationAsEliteUser(Location.UNITED_STATES).verifyLocationChange(Location.UNITED_STATES);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout);
        GooglePlayApplicationPage appPage = new GooglePlay().open().tapSearchField().searchFor(googlePlaySearchTerm).tapOn(googlePlaySearchTerm);
        Assert.assertTrue("Google Play App Page did not load", appPage.isLoaded());
        testInfo.actual("Browsed fine");
    }

    @Test
    public void AC4_593_3(){
        runOnlyForThoseTypes("ANDROID_FUN && ANDROID_FUN_SMOKE");
        testInfo.name("USA - FindIpInfo (HTTP)").expected("Able to browse HTTP");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword()).openLocationSelector().selectLocationAsEliteUser(Location.UNITED_STATES).verifyLocationChange(Location.UNITED_STATES);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout);
        try{new GoogleChrome().open(Website.FINDIPINFO).getIpAddress(IPv4);}
        catch (AssertionError e){Assert.assertTrue("FindIpInfo Page did not load", false);}
    }

    @Test
    public void AC4_653() {
        runOnlyForThoseTypes("ANDROID_SRV, ANDROID_SRV_SMOKE and ANDROID_FUN");
        testInfo.name("IP Hidden when Connected (HTTP)").expected("VPN IP");
        Android.openHotspotShield().tapConnectButton();
        testInfo.actual(new GoogleChrome().open(Website.FINDIPINFO).getIpAddress(IPv4));
        Assert.assertTrue("Not a VPN IP", Utilities.isVpnIp(TestInfo.actual()));
        testInfo.actual(TestInfo.actual());
    }

    @Test
    public void AC4_726() {
        runOnlyForThoseTypes("ANDROID_SRV, ANDROID_SRV_SMOKE and ANDROID_FUN");
        testInfo.name("IP not Hidden when Disconnected (HTTPS)").expected("Real IP");
        Android.openHotspotShield().tapDisconnectButton().openAppList().disableApplication("Chrome").exitView();
        testInfo.actual(new GoogleChrome().getIpFromGoogleSearchPage());
        Assert.assertFalse("Not a Real IP", Utilities.isVpnIp(TestInfo.actual()));
    }

    @Test
    public void AC4_725() {
        runOnlyForThoseTypes("ANDROID_SRV, ANDROID_SRV_SMOKE and ANDROID_FUN");
        testInfo.name("IP Hidden when Connected (HTTPS)").expected("VPN IP");
        Android.openHotspotShield().tapConnectButton();
        testInfo.actual(new GoogleChrome().getIpFromGoogleSearchPage());
        if(TestInfo.actual().split(":").length == 8) return;
        Assert.assertTrue("Not a VPN IP", Utilities.isVpnIp(TestInfo.actual()));
    }

    @Test
    public void AC4_654() {
        runOnlyForThoseTypes("ANDROID_SRV, ANDROID_SRV_SMOKE and ANDROID_FUN");
        testInfo.name("IP not Hidden when Disconnected (HTTP)").expected("Real IP");
        Android.openHotspotShield().tapDisconnectButton().openAppList().disableApplication("Chrome").exitView();
        testInfo.actual(new GoogleChrome().open(Website.FINDIPINFO).getIpAddress(IPv4));
        Assert.assertFalse("Not a Real IP", Utilities.isVpnIp(TestInfo.actual()));
        testInfo.actual(TestInfo.actual());
    }

    @Test
    public void AC4_547() {
        runOnlyForThoseTypes("ANDROID_FUN && ANDROID_FUN_SMOKE");
        testInfo.name("Disconnect via notification").expected("Client Disconnects");
        Android.openHotspotShield().tapConnectButton();
        Android.openNotifications().tapPause();
        Assert.assertFalse("Client did not disconnect via Notification Shortcut", new ViewHome().connected());
        testInfo.actual("Client disconnected");
    }

    @Test
    public void AC4_548() {
        runOnlyForThoseTypes("ANDROID_FUN && ANDROID_FUN_SMOKE");
        testInfo.name("Connect via notification (no permissions)").expected("Client Connects");
        Android.openHotspotShield().tapConnectButton();
        Android.openNotifications().tapVpn().tapDisconnect();
        Android.openHotspotShield().disconnectionAlgorithm().tapConnectButton();
        Assert.assertTrue("Client did not connect after revoking permissions", new ViewHome().connected());
        testInfo.actual("Client connected");
    }

    @Test
    public void AC4_695() {
        runOnlyForThoseTypes("ANDROID_FUN && ANDROID_FUN_SMOKE");
        testInfo.name("Open HSS via Notification Icon").expected("Home View opens up");
        Android.openHotspotShield().tapConnectButton();
        Android.openNotifications().tapHotspotShieldIcon();
    }

    @Test
    public void AC4_102() {
        testInfo.name("Sign out").expected("User is signed out. UI shows Free");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword());
        new ViewAccount().signOut();
        testInfo.actual("Sign Out Successful");
    }

    @Test
    public void AC4_119() {
        testInfo.name("Sign in with Elite account").expected("User is signed in; UI/Menu shows Elite UI");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signOut();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword());
        testInfo.actual("Elite Sign In Successful");
    }

    @Test
    public void AC4_120() {
        testInfo.name("Sign in with Free account").expected("User is signed in; UI/Menu shows Free UI");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signOut();
        new ViewAccount().signInAsFree(Credentials.freeUsername(), Credentials.freePassword());
        testInfo.actual("Free Sign In Successful");
    }

    @Test
    public void AC4_104() {
        runOnlyForThoseTypes("ANDROID_SC and ANDROID_FUN");
        testInfo.name("Sign in with account that exceeded device quota").expected("Error message about limit of devices");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInToAccountWithExhaustedDeviceLimit();
        testInfo.actual("Free Sign In Successful");
    }

    @Test
    public void AC4_585() {
        runOnlyForThoseTypes("ANDROID_SC and ANDROID_FUN");
        testInfo.name("Create New Account with Existing Username").expected("Error dialog displayed \"Username is already in use\"");
        Android.openHotspotShield().tapConnectButton();
        ErrorDialog error = new ViewAccount().signUpWithExistingUsername();
        Assert.assertTrue("Either there was no error or error was incorrect", error.isUsernameAlreadyInUse());
        testInfo.actual("Expected Error Shown");
    }

    @Test
    public void AC4_109() {
        runOnlyForThoseTypes("ANDROID_SC and ANDROID_FUN");
        testInfo.name("Restore password").expected("Email is sent to the provided address");
        Android.openHotspotShield().openMenuDrawer().openAccount().tapForgotPassword().enterEmailAddress(Credentials.eliteUsername()).tapSendButton();
        testInfo.actual("Password reset works");
    }
}