package tests.v400;

import api.android.Android;
import api.googlechrome.GoogleChrome;
import api.googleplay.GooglePlay;
import api.youtube.YouTube;
import managers.QAManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;
import utilities.Utilities;
import utilities.constants.Activity;
import utilities.constants.Apps;
import utilities.constants.Network;
import utilities.constants.Website;

/**
 * Created by Artur Spirin on 11/12/15.
 **/
public class AutoOnAppsUnsecuredNetwork extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){
        testInfo.suite("Apps Auto-On Unsecured Network").execType("criticClient");
        try{
            if(!Android.isAppInstalled(utilities.constants.Package.FACEBOOK)){
                new GooglePlay().open().tapSearchField().searchFor("Facebook").tapOn("Facebook").tapInstall().acceptPermissions().waitForInstallToFinish();
            }
        }catch (Exception e){e.printStackTrace();}
    }

    @Test
    public void AC4_692() {
        testInfo.name("Auto-On OFF: Unsecured Network - Chrome Launch").expected("VPN remains disconnected");
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1);
        Android.openHotspotShield().tapDisconnectButton().openAppList()
                .enableApplication(Apps.FACEBOOK).enableApplication(Apps.YOUTUBE).enableApplication(Apps.WHATSAPP).disableApplication(Apps.CHROME)
                .exitView()
                .verifyAppEnabled(Apps.FACEBOOK).verifyAppEnabled(Apps.YOUTUBE).verifyAppEnabled(Apps.WHATSAPP).verifyAppDisabled(Apps.CHROME);
        Utilities.Wait(5000);
        new GoogleChrome().open(Website.YOUTUBE);
        Utilities.Wait(5000);
        Assert.assertFalse("Client reconnected", Android.openHotspotShield().connected());
        testInfo.actual("Remained disconnected");
    }

    @Test
    public void AC4_688() {
        testInfo.name("Auto-On OFF: Unsecured Network - WhatsApp Launch").expected("VPN remains disconnected");
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1);
        Android.openHotspotShield().tapDisconnectButton().openAppList()
                .enableApplication(Apps.FACEBOOK).enableApplication(Apps.YOUTUBE).enableApplication(Apps.CHROME).disableApplication(Apps.WHATSAPP)
                .exitView()
                .verifyAppEnabled(Apps.FACEBOOK).verifyAppEnabled(Apps.YOUTUBE).verifyAppEnabled(Apps.CHROME).verifyAppDisabled(Apps.WHATSAPP);
        Utilities.Wait(5000);
        Android.openApp(utilities.constants.Package.WHATSAPP, Activity.WHATSAPP);
        Utilities.Wait(5000);
        Assert.assertFalse("Client reconnected", Android.openHotspotShield().connected());
        testInfo.actual("Remained disconnected");
    }

    @Test
    public void AC4_685() {
        testInfo.name("Auto-On OFF: Unsecured Network - YouTube Launch").expected("VPN remains disconnected");
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1);
        Android.openHotspotShield().tapDisconnectButton().openAppList()
                .enableApplication(Apps.FACEBOOK).enableApplication(Apps.CHROME).enableApplication(Apps.WHATSAPP).disableApplication(Apps.YOUTUBE)
                .exitView()
                .verifyAppEnabled(Apps.FACEBOOK).verifyAppEnabled(Apps.CHROME).verifyAppEnabled(Apps.WHATSAPP).verifyAppDisabled(Apps.YOUTUBE);
        Utilities.Wait(5000);
        new YouTube().open();
        Utilities.Wait(5000);
        Assert.assertFalse("Client reconnected", Android.openHotspotShield().connected());
        testInfo.actual("Remained disconnected");
    }

    @Test
    public void AC4_689() {
        testInfo.name("Auto-On OFF: Unsecured Network - Facebook Launch").expected("VPN remains disconnected");
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1);
        Android.openHotspotShield().tapDisconnectButton().openAppList()
                .enableApplication(Apps.CHROME).enableApplication(Apps.YOUTUBE).enableApplication(Apps.WHATSAPP).disableApplication(Apps.FACEBOOK)
                .exitView()
                .verifyAppEnabled(Apps.CHROME).verifyAppEnabled(Apps.YOUTUBE).verifyAppEnabled(Apps.WHATSAPP).verifyAppDisabled(Apps.FACEBOOK);
        Utilities.Wait(5000);
        Android.openApp(utilities.constants.Package.FACEBOOK, Activity.FACEBOOK);
        Utilities.Wait(5000);
        Assert.assertFalse("Client reconnected", Android.openHotspotShield().connected());
        testInfo.actual("Remained disconnected");
    }

    @Test
    public void AC4_691() {
        testInfo.name("Auto-On ON: Unsecured Network - Chrome Launch").expected("VPN auto connects");
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1);
        Android.openHotspotShield().tapDisconnectButton().openAppList()
                .disableApplication(Apps.FACEBOOK).disableApplication(Apps.YOUTUBE).disableApplication(Apps.WHATSAPP).enableApplication(Apps.CHROME)
                .exitView()
                .verifyAppDisabled(Apps.FACEBOOK).verifyAppDisabled(Apps.YOUTUBE).verifyAppDisabled(Apps.WHATSAPP).verifyAppEnabled(Apps.CHROME);
        Utilities.Wait(5000);
        new GoogleChrome().open(Website.YOUTUBE);
        Utilities.Wait(5000);
        Assert.assertTrue("Client remained disconnected", Android.openHotspotShield().connected());
        testInfo.actual("Auto connected");
    }

    @Test
    public void AC4_687() {
        testInfo.name("Auto-On ON: Unsecured Network - WhatsApp Launch").expected("VPN auto connects");
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1);
        Android.openHotspotShield().tapDisconnectButton().openAppList()
                .disableApplication(Apps.FACEBOOK).disableApplication(Apps.YOUTUBE).disableApplication(Apps.CHROME).enableApplication(Apps.WHATSAPP)
                .exitView()
                .verifyAppDisabled(Apps.FACEBOOK).verifyAppDisabled(Apps.YOUTUBE).verifyAppDisabled(Apps.CHROME).verifyAppEnabled(Apps.WHATSAPP);
        Utilities.Wait(5000);
        Android.openApp(utilities.constants.Package.WHATSAPP, Activity.WHATSAPP);
        Utilities.Wait(5000);
        Assert.assertTrue("Client remained disconnected", Android.openHotspotShield().connected());
        testInfo.actual("Auto connected");
    }

    @Test
    public void AC4_686() {
        testInfo.name("Auto-On ON: Unsecured Network - YouTube Launch").expected("VPN auto connects");
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1);
        Android.openHotspotShield().tapDisconnectButton().openAppList()
                .disableApplication(Apps.FACEBOOK).disableApplication(Apps.CHROME).disableApplication(Apps.WHATSAPP).enableApplication(Apps.YOUTUBE)
                .exitView()
                .verifyAppDisabled(Apps.FACEBOOK).verifyAppDisabled(Apps.CHROME).verifyAppDisabled(Apps.WHATSAPP).verifyAppEnabled(Apps.YOUTUBE);
        Utilities.Wait(5000);
        new YouTube().open();
        Utilities.Wait(5000);
        Assert.assertTrue("Client remained disconnected", Android.openHotspotShield().connected());
        testInfo.actual("Auto connected");
    }

    @Test
    public void AC4_690() {
        testInfo.name("Auto-On ON: Unsecured Network - Facebook Launch").expected("VPN auto connects");
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1);
        Android.openHotspotShield().tapDisconnectButton().openAppList()
                .disableApplication(Apps.CHROME).disableApplication(Apps.YOUTUBE).disableApplication(Apps.WHATSAPP).enableApplication(Apps.FACEBOOK)
                .exitView()
                .verifyAppDisabled(Apps.CHROME).verifyAppDisabled(Apps.YOUTUBE).verifyAppDisabled(Apps.WHATSAPP).verifyAppEnabled(Apps.FACEBOOK);
        Utilities.Wait(5000);
        Android.openApp(utilities.constants.Package.FACEBOOK, Activity.FACEBOOK);
        Utilities.Wait(5000);
        Assert.assertTrue("Client remained disconnected", Android.openHotspotShield().connected());
        testInfo.actual("Auto connected");
    }
}
