package tests.v400;

import api.android.Android;
import managers.QAManager;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;
import utilities.Utilities;
import utilities.constants.Network;

/**
 * Created by Artur Spirin on 11/12/15.
 **/
public class AutoOnNetworks extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){
        testInfo.suite("Auto-On Networks").execType("criticClient");
    }

    @Test
    public void AC4_630() {
        testInfo.name("Auto-On: Secured WiFi ON").expected("Client reconnects");
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1).closeSettings();
        Android.openHotspotShield().openMenuDrawer().openNetworks()
                .enableNetwork(Network.SECURED).disableNetwork(Network.UNSECURED).disableNetwork(Network.MOBILE).disableNetwork(Network.OTHER)
                .exitView().tapDisconnectButton();
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Utilities.Wait(5000);
        Assert.assertTrue("Client did not reconnect", Android.openHotspotShield().connected());
        testInfo.actual("Client reconnected");
    }

    @Test
    public void AC4_631() {
        testInfo.name("Auto-On: Unsecured WiFi ON").expected("Client reconnects");
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().openMenuDrawer().openNetworks()
                .enableNetwork(Network.UNSECURED).disableNetwork(Network.SECURED).disableNetwork(Network.MOBILE).disableNetwork(Network.OTHER)
                .exitView().tapDisconnectButton();
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1).closeSettings();
        Utilities.Wait(5000);
        Assert.assertTrue("Client did not reconnect", Android.openHotspotShield().connected());
        testInfo.actual("Client reconnected");
    }

    @Test
    public void AC4_627() {
        Assume.assumeTrue(Android.isCarrierAvailable());
        testInfo.name("Auto-On: Mobile Network ON").expected("Client Reconnects");
        Android.turnWifiOff();
        Android.openHotspotShield().openMenuDrawer().openNetworks()
                .enableNetwork(Network.MOBILE).disableNetwork(Network.UNSECURED).disableNetwork(Network.SECURED).disableNetwork(Network.OTHER)
                .exitView().tapDisconnectButton();
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Utilities.Wait(5000);
        Assert.assertTrue("Client did not reconnect", Android.openHotspotShield().connected());
        testInfo.actual("Client reconnected");
    }

    @Test
    public void AC4_650() {
        testInfo.name("Auto-On: Secured WiFi OFF").expected("Client remains disconnected");
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1).closeSettings();
        Android.openHotspotShield().openMenuDrawer().openNetworks()
                .disableNetwork(Network.MOBILE).disableNetwork(Network.UNSECURED).disableNetwork(Network.SECURED).disableNetwork(Network.OTHER)
                .exitView().tapDisconnectButton();
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Utilities.Wait(5000);
        Assert.assertFalse("Client reconnected", Android.openHotspotShield().connected());
        testInfo.actual("Remained disconnected");
    }

    @Test
    public void AC4_651() {
        testInfo.name("Auto-On: Unsecured WiFi OFF").expected("Client remains disconnected");
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().openMenuDrawer().openNetworks()
                .disableNetwork(Network.MOBILE).disableNetwork(Network.UNSECURED).disableNetwork(Network.SECURED).disableNetwork(Network.OTHER)
                .exitView().tapDisconnectButton();
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1).closeSettings();
        Utilities.Wait(5000);
        Assert.assertFalse("Client reconnected", Android.openHotspotShield().connected());
        testInfo.actual("Remained disconnected");
    }

    @Test
    public void AC4_652() {
        Assume.assumeTrue(Android.isCarrierAvailable());
        testInfo.name("Auto-On: Mobile Network OFF").expected("Client remains disconnected");
        Android.turnWifiOff();
        Android.openHotspotShield().openMenuDrawer().openNetworks()
                .disableNetwork(Network.MOBILE).disableNetwork(Network.UNSECURED).disableNetwork(Network.SECURED).disableNetwork(Network.OTHER)
                .exitView().tapDisconnectButton();
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Utilities.Wait(5000);
        Assert.assertFalse("Client reconnected", Android.openHotspotShield().connected());
        testInfo.actual("Remained disconnected");
    }
}
