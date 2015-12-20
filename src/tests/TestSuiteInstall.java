package tests;

import api.android.Android;
import managers.DatabaseManager;
import managers.MonitoringManager;
import managers.QAManager;
import managers.SystemManager;
import net.lingala.zip4j.exception.ZipException;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;
import utilities.Utilities;
import utilities.constants.DistributionList;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/4/2015.
 */
public class TestSuiteInstall extends QAManager{
    private static TestInfo testInfo = new TestInfo();
    public static Boolean APP_INSTALLED = true;
    public static String apkLocation;

    @BeforeClass
    public static void initialization() {
        testInfo.suite("Installation").execType("criticClient");
    }

    @Test
    public void AC4_15() throws ZipException {
        Assume.assumeFalse(DatabaseManager.runDebugRequest);
        APP_INSTALLED = false;
        apkLocation = SystemManager.createPathToFile(DatabaseManager.SVN);
        Assume.assumeTrue(!Utilities.isEmpty(DatabaseManager.SVN));
        testInfo.name("Clean Install").expected("Hotspot Shield installs on device");
        Android.installHotspotShield(apkLocation);
        APP_INSTALLED = true;
        testInfo.actual("Installed: " + DatabaseManager.SVN);
    }

    @AfterClass
    public static void tearDown(){
        stopTestCycleIfInstallFailed();
    }

    public static void stopTestCycleIfInstallFailed(){
        if(!TestSuiteInstall.APP_INSTALLED){
            QAManager.log.debug("Stopping Test Cycle. Build did not pass installation!");
            QAManager.log.debug("Path used to install build: " + TestSuiteInstall.apkLocation);
            QAManager.sendEmail(DistributionList.DEBUG, "Build did not pass Installation", "Test Cycle was stopped!\n" +
                    "Failed to install APK from: " + TestSuiteInstall.apkLocation);
            MonitoringManager.MONITOR = false;
            int ls = 0;
            while((!MonitoringManager.ENVIRONMENT_MONITORING_STOPPED
                    || !MonitoringManager.UPDATE_MONITORING_STOPPED) && ls != 10){
                ls++;
                QAManager.log.debug("Waiting for Monitoring Manager to stop its activity: " + ls + "/10");
                Utilities.Wait(1500);
            }
            System.exit(0);
        }
    }
}
