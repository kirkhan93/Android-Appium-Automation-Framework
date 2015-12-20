package tests.v400;

import api.android.Android;
import managers.QAManager;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;

/**
 * Created by Artur Spirin on 11/12/15.
 **/
public class NavigationViewAbout extends QAManager {

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){

        testInfo.suite("Nav About").execType("criticClient");
    }

    @Test
    public void AC4_655() {

        testInfo.name("Open Privacy Page").expected("Privacy page opens");
        Android.openHotspotShield().openMenuDrawer().openAbout().openPrivacyPolicy().exitView();
        testInfo.actual("Opened");
    }

    @Test
    public void AC4_656(){

        testInfo.name("Open Terms of Service").expected("Terms of Service opens");
        Android.openHotspotShield().openMenuDrawer().openAbout().openTermsOfService().exitView();
        testInfo.actual("Opened");
    }
}
