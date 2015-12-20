package tests.v400;

import api.android.Android;
import managers.QAManager;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;
import utilities.constants.Network;

/**
 * Created by Artur Spirin on 11/13/15.
 **/
public class FunctionalityNetworkStatusBar extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){
        testInfo.suite("Network Status Bar").execType("criticClient");
    }

    @Test
    public void AC4_694() {
        testInfo.name("VPN Connected - Status bar reflects Secured Network ID").expected(Network.AnchorFree_Guest);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapConnectButton();
        Android.openSettingsWifi().connectTo(Network.AnchorFree_Guest).closeSettings();
        testInfo.actual(Android.openHotspotShield().connectionAlgorithm().getNetworkID());
        Assert.assertTrue("Network ID did not equal to: "+Network.AnchorFree_Guest, TestInfo.actual().equals(Network.AnchorFree_Guest));
    }

    @Test
    public void AC4_697(){
        testInfo.name("VPN Disconnected - Status bar reflects Secured Network ID").expected(Network.AnchorFree_Guest);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapDisconnectButton()
                .openMenuDrawer().openNetworks()
                .disableNetwork(Network.UNSECURED).disableNetwork(Network.SECURED).disableNetwork(Network.MOBILE).disableNetwork(Network.OTHER)
                .exitView();
        Android.openSettingsWifi().connectTo(Network.AnchorFree_Guest).closeSettings();
        testInfo.actual(Android.openHotspotShield().disconnectionAlgorithm().getNetworkID());
        Assert.assertTrue("Network ID did not equal to: "+Network.AnchorFree_Guest, TestInfo.actual().equals(Network.AnchorFree_Guest));
    }

    @Test
    public void AC4_709() {
        testInfo.name("VPN Connected - Status bar reflects Unsecured Network ID").expected(Network.QA_NO_PASS_1);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapConnectButton();
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1).closeSettings();
        testInfo.actual(Android.openHotspotShield().connectionAlgorithm().getNetworkID());
        Assert.assertTrue("Network ID did not equal to: "+Network.QA_NO_PASS_1, TestInfo.actual().equals(Network.QA_NO_PASS_1));
    }

    @Test
    public void AC4_708(){
        testInfo.name("VPN Disconnected - Status bar reflects Unsecured Network ID").expected(Network.QA_NO_PASS_1);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapDisconnectButton()
                .openMenuDrawer().openNetworks()
                .disableNetwork(Network.UNSECURED).disableNetwork(Network.SECURED).disableNetwork(Network.MOBILE).disableNetwork(Network.OTHER)
                .exitView();
        Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1).closeSettings();
        testInfo.actual(Android.openHotspotShield().disconnectionAlgorithm().getNetworkID());
        Assert.assertTrue("Network ID did not equal to: "+Network.QA_NO_PASS_1, TestInfo.actual().equals(Network.QA_NO_PASS_1));
    }

    @Test
    public void AC4_693() {
        Assume.assumeTrue("Carrier is not available, skipping this test", Android.isCarrierAvailable());
        testInfo.name("VPN Connected - Status bar reflects Mobile Network ID").expected(Android.getCarrier());
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapConnectButton();
        Android.turnWifiOff();
        testInfo.actual(Android.openHotspotShield().connectionAlgorithm().getNetworkID());
        Assert.assertTrue("Network ID did not equal to: "+Android.getCarrier(), TestInfo.actual().equals(Android.getCarrier()));
    }

    @Test
    public void AC4_698(){
        Assume.assumeTrue("Carrier is not available, skipping this test", Android.isCarrierAvailable());
        testInfo.name("VPN Disconnected - Status bar reflects Mobile Network ID").expected(Android.getCarrier());
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapDisconnectButton()
                .openMenuDrawer().openNetworks()
                .disableNetwork(Network.UNSECURED).disableNetwork(Network.SECURED).disableNetwork(Network.MOBILE).disableNetwork(Network.OTHER)
                .exitView();
        Android.turnWifiOff();
        testInfo.actual(Android.openHotspotShield().disconnectionAlgorithm().getNetworkID());
        Assert.assertTrue("Network ID did not equal to: "+Android.getCarrier(), TestInfo.actual().equals(Android.getCarrier()));
    }
}
