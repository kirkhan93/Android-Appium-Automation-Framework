package tests.v400;

import api.android.Android;
import api.hotspotshield.v400.ViewAccount;
import managers.QAManager;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;
import utilities.constants.Location;

/**
 * Created by Artur Spirin on 11/12/15.
 **/
public class NavigationViewHome extends QAManager {

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){

        testInfo.suite("Nav Home View").execType("criticClient");
    }

    @Test
    public void AC4_636() {

        testInfo.name("Open Menu via Menu Icon").expected("Menu opens");
        Android.openHotspotShield().openMenuDrawer().closeDrawer();
        testInfo.actual("opened");
    }

    @Test
    public void AC4_637() {

        testInfo.name("Open App List via Add More button").expected("App List opens");
        Android.openHotspotShield().openMenuDrawer().openAppList().exitView();
        testInfo.actual("opened");
    }

    @Test
    public void AC4_638() {

        testInfo.name("Open Up-Sell Page via Upgrade Button").expected("Up-Sell Page opens up");
        new ViewAccount().signOut();
        Android.openHotspotShield().openMenuDrawer().closeDrawer();
        testInfo.actual("opened");
    }

    @Test
    public void AC4_639() {

        testInfo.name("Open VL selector").expected("VL selector opens up");
        Android.openHotspotShield().openLocationSelector().closeDialog();
        testInfo.actual("opened");
    }

    @Test
    public void AC4_640() {

        testInfo.name("Open Share Activity").expected("Invite view opens");
        Android.openHotspotShield().openInviteSDK().exitView();
        testInfo.actual("opened");
    }

    @Test
    public void AC4_495() {

        testInfo.name("Virtual Location (Free)").expected("Up-Sell Page opens");
        new ViewAccount().signOut();
        Android.openHotspotShield().openLocationSelector().selectLocationAsFreeUser(Location.JAPAN).exitView();
        testInfo.actual("opened");
    }
}
