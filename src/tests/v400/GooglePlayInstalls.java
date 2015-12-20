package tests.v400;

import api.android.Android;
import api.googleplay.GooglePlay;
import api.hotspotshield.v400.ViewAccount;
import api.hotspotshield.v400.ViewHome;
import managers.QAManager;
import org.junit.*;
import utilities.TestInfo;
import utilities.constants.Credentials;
import utilities.constants.Location;
import utilities.constants.Network;
import utilities.constants.Package;

/**
 * Created by Artur Spirin on 12/7/15.
 **/
public class GooglePlayInstalls extends QAManager {

    private static TestInfo testInfo = new TestInfo();
    private static final String searchTerm = "Clash of Clans";
    private static final String testApp = Package.CLASH_OF_CLANS;


    @BeforeClass
    public static void initialization(){
        testInfo.suite("Play Installs").execType("criticClient");
        RETRY_COUNT = 1;
        QAManager.log.debug("Retry count reset to: "+RETRY_COUNT+" for Suite: "+TestInfo.suite());
    }

    @AfterClass
    public static void tearDown(){
        RETRY_COUNT = 3;
        QAManager.log.debug("Retry count reset to default: " + RETRY_COUNT);
    }

    @Test
    public void AC4_722_2() {
        Assume.assumeTrue(Android.isCarrierAvailable());
        testInfo.name("Install App via Cell Network - Japan").expected("Able to install App");
        Android.turnWifiOff();
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                .openLocationSelector().selectLocationAsEliteUser(Location.JAPAN).verifyLocationChange(Location.JAPAN);
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        Android.uninstallApp(testApp);
        new GooglePlay().open().tapSearchField().searchFor(searchTerm).tapOn(searchTerm).tapInstall().acceptPermissions().waitForInstallToFinish();
        Assert.assertTrue("App did not install!", Android.isAppInstalled(testApp));
        testInfo.actual("App installed");
    }

    @Test
    public void AC4_723_2() {
        testInfo.name("Install App via Secured Network - Japan").expected("Able to install App");
        Android.turnWifiOn();
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                .openLocationSelector().selectLocationAsEliteUser(Location.JAPAN).verifyLocationChange(Location.JAPAN);
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        Android.uninstallApp(testApp);
        new GooglePlay().open().tapSearchField().searchFor(searchTerm).tapOn(searchTerm).tapInstall().acceptPermissions().waitForInstallToFinish();
        Assert.assertTrue("App did not install!", Android.isAppInstalled(testApp));
        testInfo.actual("App installed");
    }

    @Test
    public void AC4_724_2() {
        testInfo.name("Install App via Unsecured Network - Japan").expected("Able to install App");
        Android.turnWifiOn();
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                .openLocationSelector().selectLocationAsEliteUser(Location.JAPAN).verifyLocationChange(Location.JAPAN);
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        Android.uninstallApp(testApp);
        new GooglePlay().open().tapSearchField().searchFor(searchTerm).tapOn(searchTerm).tapInstall().acceptPermissions().waitForInstallToFinish();
        Assert.assertTrue("App did not install!", Android.isAppInstalled(testApp));
        testInfo.actual("App installed");
    }

    @Test
    public void AC4_722_3() {
        Assume.assumeTrue(Android.isCarrierAvailable());
        testInfo.name("Install App via Cell Network - UK").expected("Able to install App");
        Android.turnWifiOff();
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                .openLocationSelector().selectLocationAsEliteUser(Location.UNITED_KINGDOM).verifyLocationChange(Location.UNITED_KINGDOM);
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        Android.uninstallApp(testApp);
        new GooglePlay().open().tapSearchField().searchFor(searchTerm).tapOn(searchTerm).tapInstall().acceptPermissions().waitForInstallToFinish();
        Assert.assertTrue("App did not install!", Android.isAppInstalled(testApp));
        testInfo.actual("App installed");
    }

    @Test
    public void AC4_723_3() {
        testInfo.name("Install App via Secured Network - UK").expected("Able to install App");
        Android.turnWifiOn();
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                .openLocationSelector().selectLocationAsEliteUser(Location.UNITED_KINGDOM).verifyLocationChange(Location.UNITED_KINGDOM);
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        Android.uninstallApp(testApp);
        new GooglePlay().open().tapSearchField().searchFor(searchTerm).tapOn(searchTerm).tapInstall().acceptPermissions().waitForInstallToFinish();
        Assert.assertTrue("App did not install!", Android.isAppInstalled(testApp));
        testInfo.actual("App installed");
    }

    @Test
    public void AC4_724_3() {
        testInfo.name("Install App via Unsecured Network - UK").expected("Able to install App");
        Android.turnWifiOn();
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                .openLocationSelector().selectLocationAsEliteUser(Location.UNITED_KINGDOM).verifyLocationChange(Location.UNITED_KINGDOM);
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        Android.uninstallApp(testApp);
        new GooglePlay().open().tapSearchField().searchFor(searchTerm).tapOn(searchTerm).tapInstall().acceptPermissions().waitForInstallToFinish();
        Assert.assertTrue("App did not install!", Android.isAppInstalled(testApp));
        testInfo.actual("App installed");
    }

    @Test
    public void AC4_722_4() {
        Assume.assumeTrue(Android.isCarrierAvailable());
        testInfo.name("Install App via Cell Network - Australia").expected("Able to install App");
        Android.turnWifiOff();
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                .openLocationSelector().selectLocationAsEliteUser(Location.AUSTRALIA).verifyLocationChange(Location.AUSTRALIA);
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        Android.uninstallApp(testApp);
        new GooglePlay().open().tapSearchField().searchFor(searchTerm).tapOn(searchTerm).tapInstall().acceptPermissions().waitForInstallToFinish();
        Assert.assertTrue("App did not install!", Android.isAppInstalled(testApp));
        testInfo.actual("App installed");
    }

    @Test
    public void AC4_723_4() {
        testInfo.name("Install App via Secured Network - Australia").expected("Able to install App");
        Android.turnWifiOn();
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                .openLocationSelector().selectLocationAsEliteUser(Location.AUSTRALIA).verifyLocationChange(Location.AUSTRALIA);
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        Android.uninstallApp(testApp);
        new GooglePlay().open().tapSearchField().searchFor(searchTerm).tapOn(searchTerm).tapInstall().acceptPermissions().waitForInstallToFinish();
        Assert.assertTrue("App did not install!", Android.isAppInstalled(testApp));
        testInfo.actual("App installed");
    }

    @Test
    public void AC4_724_4() {
        testInfo.name("Install App via Unsecured Network - Australia").expected("Able to install App");
        Android.turnWifiOn();
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                .openLocationSelector().selectLocationAsEliteUser(Location.AUSTRALIA).verifyLocationChange(Location.AUSTRALIA);
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        Android.uninstallApp(testApp);
        new GooglePlay().open().tapSearchField().searchFor(searchTerm).tapOn(searchTerm).tapInstall().acceptPermissions().waitForInstallToFinish();
        Assert.assertTrue("App did not install!", Android.isAppInstalled(testApp));
        testInfo.actual("App installed");
    }
}
