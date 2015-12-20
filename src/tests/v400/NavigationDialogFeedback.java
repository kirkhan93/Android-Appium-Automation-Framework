package tests.v400;

import api.android.Android;
import managers.QAManager;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;

import java.io.IOException;

/**
 * Created by Artur Spirin on 11/12/15.
 **/
public class NavigationDialogFeedback extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){

        testInfo.suite("Nav Feedback Dialog").execType("criticClient");
    }

    @Test
    public void AC4_550() {

        testInfo.name("Feedback dialog").expected("Feedback Dialog is Surfaced");
        Android.openHotspotShield().openMenuDrawer().tapDebugFeedback().closeDialog();
        testInfo.actual("Dialog surfaced");
    }

    @Test
    public void AC4_551(){

        testInfo.name("Feedback dialog help button").expected("Help Page Opens");
        Android.openHotspotShield().openMenuDrawer().tapDebugFeedback().tapNotReally().tapHelp().exitView();
        testInfo.actual("Opened");
    }

    @Test
    public void AC4_552(){

        testInfo.name("Feedback dialog support button").expected("Help Center opens");
        Android.openHotspotShield().openMenuDrawer().tapDebugFeedback().tapNotReally().tapContactSupport().getCurrentUrl();
        testInfo.actual("Opened");
    }

    @Test
    public void AC4_553(){

        testInfo.name("Feedback dialog rate later button").expected("Dialog Closes");
        Android.openHotspotShield().openMenuDrawer().tapDebugFeedback().tapYesThanks().tapMaybeLater();
        testInfo.actual("Closed");
    }

    @Test
    public void AC4_554() throws IOException {

        testInfo.name("Feedback dialog rate now button").expected("Google Play Opens");
        Android.openHotspotShield().openMenuDrawer().tapDebugFeedback().tapYesThanks().tapRateNow();
        testInfo.actual("Opened");
    }
}
