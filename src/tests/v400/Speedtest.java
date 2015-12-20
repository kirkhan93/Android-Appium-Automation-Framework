package tests.v400;

import api.android.Android;
import api.hotspotshield.v400.ViewAccount;
import api.hotspotshield.v400.ViewHome;
import api.ookla.speedtest.ViewBeginSpeedtest;
import api.ookla.speedtest.ViewSpeedtestResults;
import managers.QAManager;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;
import utilities.constants.Credentials;
import utilities.constants.Location;
import utilities.constants.Network;

/**
 * Created by Artur Spirin on 12/7/15.
 **/
public class Speedtest extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){
        testInfo.suite("Speedtest").execType("criticClient");
        RETRY_COUNT = 1;
        QAManager.log.debug("Retry count reset to: "+RETRY_COUNT+" for Suite: "+TestInfo.suite());
    }

    @AfterClass
    public static void tearDown(){
        RETRY_COUNT = 3;
        QAManager.log.debug("Retry count reset to default: " + RETRY_COUNT);
    }

    @Test
    public void AC4_719_2() {
        Assume.assumeTrue(Android.isCarrierAvailable());
        testInfo.name("Mobile Network Speed - Japan");
        Integer downloadBefore = null;
        ViewSpeedtestResults speedtest;
        String ping, donwload, upload;
        if(doBaselineSpeedTest){
            Android.turnWifiOff();
            new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                    .openLocationSelector().selectLocationAsEliteUser(Location.JAPAN).verifyLocationChange(Location.JAPAN);
            Android.openHotspotShield().tapDisconnectButton();
            speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
            ping = speedtest.getRawPing();
            donwload = speedtest.getRawDownload();
            upload = speedtest.getRawUpload();
            downloadBefore = speedtest.getDownload();
            testInfo.expected("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
            doBaselineSpeedTest = false;
        }
        Android.openHotspotShield().tapConnectButton();
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
        ping = speedtest.getRawPing(); donwload = speedtest.getRawDownload(); upload = speedtest.getRawUpload();
        int downloadAfter = speedtest.getDownload();
        testInfo.actual("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
        ViewSpeedtestResults.assertDownloadResults(downloadBefore, downloadAfter);
    }

    @Test
    public void AC4_720_2() {
        testInfo.name("Secured Network Speed - Japan");
        Integer downloadBefore = null;
        ViewSpeedtestResults speedtest;
        String ping, donwload, upload;
        if(doBaselineSpeedTest){
            Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
            new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                    .openLocationSelector().selectLocationAsEliteUser(Location.JAPAN).verifyLocationChange(Location.JAPAN);
            Android.openHotspotShield().tapDisconnectButton();
            speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
            ping = speedtest.getRawPing();
            donwload = speedtest.getRawDownload();
            upload = speedtest.getRawUpload();
            downloadBefore = speedtest.getDownload();
            testInfo.expected("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
            doBaselineSpeedTest = false;
        }
        Android.openHotspotShield().tapConnectButton();
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
        ping = speedtest.getRawPing(); donwload = speedtest.getRawDownload(); upload = speedtest.getRawUpload();
        int downloadAfter = speedtest.getDownload();
        testInfo.actual("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
        ViewSpeedtestResults.assertDownloadResults(downloadBefore, downloadAfter);
    }

    @Test
    public void AC4_721_2() {
        testInfo.name("Unsecured Network Speed - Japan");
        Integer downloadBefore = null;
        ViewSpeedtestResults speedtest;
        String ping, donwload, upload;
        if(doBaselineSpeedTest){
            Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1).closeSettings();
            new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                    .openLocationSelector().selectLocationAsEliteUser(Location.JAPAN).verifyLocationChange(Location.JAPAN);
            Android.openHotspotShield().tapDisconnectButton();
            speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
            ping = speedtest.getRawPing();
            donwload = speedtest.getRawDownload();
            upload = speedtest.getRawUpload();
            downloadBefore = speedtest.getDownload();
            testInfo.expected("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
            doBaselineSpeedTest = false;
        }
        Android.openHotspotShield().tapConnectButton();
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
        ping = speedtest.getRawPing(); donwload = speedtest.getRawDownload(); upload = speedtest.getRawUpload();
        int downloadAfter = speedtest.getDownload();
        testInfo.actual("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
        ViewSpeedtestResults.assertDownloadResults(downloadBefore, downloadAfter);
    }

    @Test
    public void AC4_719_3() {
        Assume.assumeTrue(Android.isCarrierAvailable());
        testInfo.name("Mobile Network Speed - UK");
        Integer downloadBefore = null;
        ViewSpeedtestResults speedtest;
        String ping, donwload, upload;
        if(doBaselineSpeedTest){
            Android.turnWifiOff();
            new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                    .openLocationSelector().selectLocationAsEliteUser(Location.UNITED_KINGDOM).verifyLocationChange(Location.UNITED_KINGDOM);
            Android.openHotspotShield().tapDisconnectButton();
            speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
            ping = speedtest.getRawPing();
            donwload = speedtest.getRawDownload();
            upload = speedtest.getRawUpload();
            downloadBefore = speedtest.getDownload();
            testInfo.expected("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
            doBaselineSpeedTest = false;
        }
        Android.openHotspotShield().tapConnectButton();
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
        ping = speedtest.getRawPing(); donwload = speedtest.getRawDownload(); upload = speedtest.getRawUpload();
        int downloadAfter = speedtest.getDownload();
        testInfo.actual("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
        ViewSpeedtestResults.assertDownloadResults(downloadBefore, downloadAfter);
    }

    @Test
    public void AC4_720_3() {
        testInfo.name("Secured Network Speed - UK");
        Integer downloadBefore = null;
        ViewSpeedtestResults speedtest;
        String ping, donwload, upload;
        if(doBaselineSpeedTest){
            Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
            new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                    .openLocationSelector().selectLocationAsEliteUser(Location.UNITED_KINGDOM).verifyLocationChange(Location.UNITED_KINGDOM);
            Android.openHotspotShield().tapDisconnectButton();
            speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
            ping = speedtest.getRawPing();
            donwload = speedtest.getRawDownload();
            upload = speedtest.getRawUpload();
            downloadBefore = speedtest.getDownload();
            testInfo.expected("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
            doBaselineSpeedTest = false;
        }
        Android.openHotspotShield().tapConnectButton();
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
        ping = speedtest.getRawPing(); donwload = speedtest.getRawDownload(); upload = speedtest.getRawUpload();
        int downloadAfter = speedtest.getDownload();
        testInfo.actual("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
        ViewSpeedtestResults.assertDownloadResults(downloadBefore, downloadAfter);
    }

    @Test
    public void AC4_721_3() {
        testInfo.name("Unsecured Network Speed - UK");
        Integer downloadBefore = null;
        ViewSpeedtestResults speedtest;
        String ping, donwload, upload;
        if(doBaselineSpeedTest){
            Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1).closeSettings();
            new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                    .openLocationSelector().selectLocationAsEliteUser(Location.UNITED_KINGDOM).verifyLocationChange(Location.UNITED_KINGDOM);
            Android.openHotspotShield().tapDisconnectButton();
            speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
            ping = speedtest.getRawPing();
            donwload = speedtest.getRawDownload();
            upload = speedtest.getRawUpload();
            downloadBefore = speedtest.getDownload();
            testInfo.expected("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
            doBaselineSpeedTest = false;
        }
        Android.openHotspotShield().tapConnectButton();
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
        ping = speedtest.getRawPing(); donwload = speedtest.getRawDownload(); upload = speedtest.getRawUpload();
        int downloadAfter = speedtest.getDownload();
        testInfo.actual("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
        ViewSpeedtestResults.assertDownloadResults(downloadBefore, downloadAfter);
    }


    @Test
    public void AC4_719_4() {
        Assume.assumeTrue(Android.isCarrierAvailable());
        testInfo.name("Mobile Network Speed - Australia");
        Integer downloadBefore = null;
        ViewSpeedtestResults speedtest;
        String ping, donwload, upload;
        if(doBaselineSpeedTest){
            Android.turnWifiOff();
            new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                    .openLocationSelector().selectLocationAsEliteUser(Location.AUSTRALIA).verifyLocationChange(Location.AUSTRALIA);
            Android.openHotspotShield().tapDisconnectButton();
            speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
            ping = speedtest.getRawPing();
            donwload = speedtest.getRawDownload();
            upload = speedtest.getRawUpload();
            downloadBefore = speedtest.getDownload();
            testInfo.expected("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
            doBaselineSpeedTest = false;
        }
        Android.openHotspotShield().tapConnectButton();
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
        ping = speedtest.getRawPing(); donwload = speedtest.getRawDownload(); upload = speedtest.getRawUpload();
        int downloadAfter = speedtest.getDownload();
        testInfo.actual("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
        ViewSpeedtestResults.assertDownloadResults(downloadBefore, downloadAfter);
    }

    @Test
    public void AC4_720_4() {
        testInfo.name("Secured Network Speed - Australia");
        Integer downloadBefore = null;
        ViewSpeedtestResults speedtest;
        String ping, donwload, upload;
        if(doBaselineSpeedTest){
            Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout).closeSettings();
            new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                    .openLocationSelector().selectLocationAsEliteUser(Location.AUSTRALIA).verifyLocationChange(Location.AUSTRALIA);
            Android.openHotspotShield().tapDisconnectButton();
            speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
            ping = speedtest.getRawPing();
            donwload = speedtest.getRawDownload();
            upload = speedtest.getRawUpload();
            downloadBefore = speedtest.getDownload();
            testInfo.expected("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
            doBaselineSpeedTest = false;
        }
        Android.openHotspotShield().tapConnectButton();
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
        ping = speedtest.getRawPing(); donwload = speedtest.getRawDownload(); upload = speedtest.getRawUpload();
        int downloadAfter = speedtest.getDownload();
        testInfo.actual("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
        ViewSpeedtestResults.assertDownloadResults(downloadBefore, downloadAfter);
    }

    @Test
    public void AC4_721_4() {
        testInfo.name("Unsecured Network Speed - Australia");
        Integer downloadBefore = null;
        ViewSpeedtestResults speedtest;
        String ping, donwload, upload;
        if(doBaselineSpeedTest){
            Android.openSettingsWifi().connectTo(Network.QA_NO_PASS_1).closeSettings();
            new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword())
                    .openLocationSelector().selectLocationAsEliteUser(Location.AUSTRALIA).verifyLocationChange(Location.AUSTRALIA);
            Android.openHotspotShield().tapDisconnectButton();
            speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
            ping = speedtest.getRawPing();
            donwload = speedtest.getRawDownload();
            upload = speedtest.getRawUpload();
            downloadBefore = speedtest.getDownload();
            testInfo.expected("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
            doBaselineSpeedTest = false;
        }
        Android.openHotspotShield().tapConnectButton();
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        speedtest = new ViewBeginSpeedtest().open().tapBeginTest();
        ping = speedtest.getRawPing(); donwload = speedtest.getRawDownload(); upload = speedtest.getRawUpload();
        int downloadAfter = speedtest.getDownload();
        testInfo.actual("Ping: " + ping + " Download: " + donwload + " Upload: " + upload);
        ViewSpeedtestResults.assertDownloadResults(downloadBefore, downloadAfter);
    }
}