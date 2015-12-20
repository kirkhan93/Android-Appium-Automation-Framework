package tests.v400;

import api.android.Android;
import api.hotspotshield.v400.ViewAccount;
import managers.QAManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;
import utilities.UiSelector;

/**
 * Created by Artur Spirin on 11/12/15.
 **/
public class NavigationMenuDrawer extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){

        testInfo.suite("Nav Menu Drawer").execType("criticClient");
    }

    @Test
    public void AC4_635() {

        testInfo.name("Open Account Activity").expected("Account Activity Opens");
        Android.openHotspotShield().openMenuDrawer().openAccount().exitView();
        testInfo.actual("Opened");
    }

    @Test
    public void AC4_633(){

        testInfo.name("No Quit Button (Connected)").expected("No quit button");
        Android.openHotspotShield().tapConnectButton().openMenuDrawer();
        new UiSelector().text("Help").scrollTo();
        Assert.assertFalse("Quit button is visible", new UiSelector().text("Quit").exists());
        testInfo.actual("No quit button");
    }

    @Test
    public void AC4_634(){

        testInfo.name("Quit Button (Disconnected)").expected("Quit button visible");
        Android.openHotspotShield().tapDisconnectButton().openMenuDrawer().tapQuit().tapCancel();
        testInfo.actual("Quit Button Visible");
    }

    @Test
    public void AC4_634_2(){

        testInfo.name("Quit and reconnect").expected("Able to quick and reconnect after").jira("DROID-1798");
        Android.openHotspotShield().tapDisconnectButton().openMenuDrawer().tapQuit().tapCancel();
        testInfo.actual("Quit Successful.");
        Android.openHotspotShield().tapConnectButton();
        testInfo.actual(TestInfo.actual()+ " Reconnect successful");
    }

    @Test
    public void AC4_643(){

        testInfo.name("Open Up-Sell Page via Menu Drawer").expected("Up-Sell opens");
        Android.openHotspotShield();
        new ViewAccount().signOut().openMenuDrawer().openUpSell().exitView();
        testInfo.actual("Opened");
    }

    @Test
    public void AC4_644(){

        testInfo.name("Open Help").expected("Help opens");
        Android.openHotspotShield().openMenuDrawer().openHelp().exitView();
        testInfo.actual("Opened");
    }

    @Test
    public void AC4_645(){

        testInfo.name("Open General Settings").expected("General Settings opens");
        Android.openHotspotShield().openMenuDrawer().openGeneralSettings().exitView();
        testInfo.actual("Opened");
    }

    @Test
    public void AC4_646(){

        testInfo.name("Open App List via Menu Drawer").expected("App List opens");
        Android.openHotspotShield().openMenuDrawer().openAppList().exitView();
        testInfo.actual("Opened");
    }

    @Test
    public void AC4_647(){

        testInfo.name("Open Network").expected("Network Activity opens");
        Android.openHotspotShield().openMenuDrawer().openNetworks();
        testInfo.actual("Opened");
    }

    @Test
    public void AC4_648(){

        testInfo.name("Open About").expected("About opens");
        Android.openHotspotShield().openMenuDrawer().openAbout();
        testInfo.actual("Opened");
    }

    @Test
    public void AC4_649(){

        testInfo.name("Contact Support").expected("Email client opens");
        Android.openHotspotShield().openMenuDrawer().tapContactSupport();
        testInfo.actual("Opened");
    }
}
