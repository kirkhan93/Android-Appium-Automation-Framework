package managers;

import api.android.Android;
import api.hotspotshield.v400.ViewGeneralSettings;
import api.hotspotshield.v400.ViewHome;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.sikuli.script.FindFailed;
import tests.v400.SmokeCheck;
import tests.v400.primers.Functional;
import tests.v400.primers.Server;
import utilities.Retry;
import utilities.TestInfo;
import utilities.Utilities;
import utilities.constants.Credentials;
import utilities.constants.DistributionList;
import utilities.constants.SettingItem;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by Artur Spirin on 10/8/15.
 **/
public class QAManager extends DatabaseManager {

    private static TestInfo testInfo = new TestInfo();
    public static final Logger log = Logger.getLogger(QAManager.class);
    public static boolean doBaselineSpeedTest = true;

    private static boolean doPreset = true;

    public static boolean

            IPv4 = true,
            IPv6 = false;

    public static int

            RETRY_COUNT = 3, /**Maximum number of times a test case will be retried up on failure*/
            FAIL_COUNT = 0; /**Is incremented during failures. When equal to RETRY_COUNT, test will fail*/

    @BeforeClass
    public static void globalConditions(){
        log.setLevel(Level.DEBUG);
        DeviceManager.getDriver();
    }

    @Before
    public void setUp() throws FindFailed {
        try{
            testInfo.resetTestInfo();
            testInfo.startTime(Utilities.DATE_FORMAT.format(new java.util.Date()));
            if(FailManager.UI_DISCONNECTED_FROM_SERVICE) Android.resetHotspotShield();
            Utilities.quickReset(); //Main troubleshooting method in case bad things happen
            FailManager.startTcpDump();
            FailManager.startLogcat();
            doPreset();
            QAManager.log.debug("Done!");
        }catch (Exception e){e.printStackTrace();}
    }

    @AfterClass
    public static void cleanUp(){
        QAManager.log.debug(String.format("Test Suite: %s is finished!", TestInfo.suite()));
    }

    @Rule
    public Retry retry = new Retry(RETRY_COUNT);

    @Rule
    public TestRule listen = new TestWatcher() {

        @Override
        public void failed(Throwable t, Description description) {

            doBaselineSpeedTest = true;
            testInfo.status("FAIL");
            testInfo.notes(t.getMessage());
            testInfo.id(description.getMethodName());
            printTestResults();
            if(okToFail()){
                QAManager.log.debug("Got the Go Ahead to FAIL the test case!");
                FailManager.stopTcpDump();
                FailManager.stopLogcat();
                FailManager.collectTestArtifacts(t);
                testInfo.info(FailManager.moveLocalFailDir(TestInfo.id()));
                DatabaseManager.addTestResult();
            }else{
                QAManager.log.debug("Did NOT get the Go Ahead to FAIL the test case!");
            }
        }

        @Override
        public void succeeded(Description description) {

            doBaselineSpeedTest = true;
            testInfo.status("PASS");
            testInfo.id(description.getMethodName());
            printTestResults();
            FailManager.stopTcpDump();
            FailManager.stopLogcat();
            DatabaseManager.addTestResult();
        }
    };

    public static void sendEmail(String[] TO, String TITLE, String MESSAGE){

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", Credentials.anchorfreeHost);
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Credentials.anchorfreeUsername, Credentials.anchorfreePassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("Artur Spirin <a.spirin@anchorfree.com>"));
            for(String address:TO){
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                        address));
            }
            message.setSubject(TITLE);
            message.setText(MESSAGE);
            Transport.send(message);
            log.debug("Sent E-Mail successfully!");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static void sendTestCycleStartedEmail(String[] TO){

        String Subject = "Test Cycle Started!";
        String Message = "RESULTS WILL BE HERE: http://10.0.0.148/serverside.php?req="+REQUEST_ID+"\n"+
                         "VERSION BEING TESTED: "+Android.getAppVersion()+"\n"+
                         "AGENT: "+Android.getDeviceModel()+"\n";
        sendEmail(TO, Subject, Message);
    }

    private static String buildStatus(Double percentFailed){

        // Calculating status of the build
        String BUILD_STATUS;
        if(percentFailed >= 40.0 && percentFailed > 35.0) BUILD_STATUS = "Unacceptable!";
        else if(percentFailed <= 35.0 && percentFailed > 30.0) BUILD_STATUS = "Very Bad!";
        else if(percentFailed <= 30.0 && percentFailed > 25.0) BUILD_STATUS = "Bad";
        else if(percentFailed <= 25.0 && percentFailed > 20.0) BUILD_STATUS = "Meh...";
        else if(percentFailed <= 20.0 && percentFailed > 15.0) BUILD_STATUS = "Acceptable";
        else if(percentFailed <= 15.0 && percentFailed > 10.0) BUILD_STATUS = "Good";
        else if(percentFailed <= 10.0 && percentFailed > 5.0) BUILD_STATUS = "Very Good!";
        else if(percentFailed <= 5.0) BUILD_STATUS = "Excellent!";
        else BUILD_STATUS = "Not calculated";
        return BUILD_STATUS;
    }

    public static void sendTestResults(Result results, String[] TO){

        // Converting milliseconds to readable format
        long runTime = results.getRunTime();
        String time = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(runTime),
                TimeUnit.MILLISECONDS.toMinutes(runTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(runTime)),
                TimeUnit.MILLISECONDS.toSeconds(runTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(runTime)));
        double TOTAL_TESTS_RAN = results.getRunCount() - Retry.discardedTests;
        // Get Failed %
        double TESTS_FAILED = results.getFailureCount();
        double TESTS_FAILED_PERCENT = Math.round(TESTS_FAILED / TOTAL_TESTS_RAN * 100);
        // Get Passed %
        double TESTS_PASSED = TOTAL_TESTS_RAN - TESTS_FAILED;
        double TESTS_PASSED_PERCENT = Math.round(TESTS_PASSED / TOTAL_TESTS_RAN * 100);
        String version;
        if(!Utilities.isEmpty(SVN) && (SVN.contains(".zip") || SVN.contains(".apk"))) version = SVN;
        else  version = String.valueOf(Android.getAppVersion());
        String Message =
                "BUILD STATUS: " + buildStatus(TESTS_FAILED_PERCENT)+"\n"+
                        "Details: http://10.0.0.148/serverside.php?req="+REQUEST_ID+"\n"+
                        "Version that was tested: "+version+"\n"+
                        "Tests were ran on: " + Android.getDeviceModel()+"("+Android.getDeviceModelID()+")\n"+
                        "Total number of tests executed: "+(int)TOTAL_TESTS_RAN+"\n"+
                        "Tests Passed: "+(int)TESTS_PASSED+"("+TESTS_PASSED_PERCENT+"%)"+"\n"+
                        "Tests Failed: "+(int)TESTS_FAILED+"("+TESTS_FAILED_PERCENT+"%)"+"\n"+
                        "Request Type: "+ DatabaseManager.REQUEST_TYPE+"\n"+
                        "Protocol: "+ ViewGeneralSettings.LAST_VPN_MODE+"\n"+
                        "This test cycle took: "+time+"\n\n"+
                        ReportingManager.getReport();
        String TITLE = "Test Cycle Finished: " + REQUEST_ID;
        QAManager.sendEmail(TO, TITLE, Message);
    }

    private static void listDebugParams(){
        QAManager.log.debug("I see that you want to debug! Checking Debug Settings...");
        QAManager.log.debug("Request ID:    <"+REQUEST_ID+">");
        QAManager.log.debug("Request Type:  <"+REQUEST_TYPE+">");
        QAManager.log.debug("Protocol:      <"+PROTOCOL+">");
        QAManager.log.debug("SVN:           <"+SVN+">");
        QAManager.log.debug("Host:          <"+HOST+">");
        QAManager.log.debug("Notes:         <"+NOTES+">");
    }

    public static void initiateTestCycle() throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        if(STATUS.toUpperCase().equals("OPEN")){
            if(REQUEST_ID.equals(Utilities.DEBUG_ID)) listDebugParams();
            if(DatabaseManager.takeMasterLogcat) FailManager.startMasterLogcat();
            Result results = null;
            switch (REQUEST_TYPE) {
                case "ANDROID_SRV":
                case "ANDROID_SRV_SMOKE":
                    updateRequestStatus(REQUEST_ID, IN_PROGRESS_STATUS);
                    results = startServerTests();
                    break;
                case "ANDROID_FUN":
                case "ANDROID_FUN_SMOKE":
                    updateRequestStatus(REQUEST_ID, IN_PROGRESS_STATUS);
                    results = startFunctionalTests();
                    break;
                case "ANDROID_SC":
                    results = startEliteTests();
                    break;
            }
            QAManager.log.debug("Test cycle is finished!");
            FailManager.stopTcpDump(); FailManager.stopLogcat();
            if(results != null) QAManager.sendTestResults(results, DistributionList.getDistributionList());
            updateRequestStatus(REQUEST_ID, COMPLETED_STATUS);
            DeviceManager.getDriver().quit();
            if(DatabaseManager.takeMasterLogcat) {FailManager.stopMasterLogcat(); FailManager.getMasterLogcat();}
        }
    }

    private static Result startEliteTests(){
        log.debug("Starting Elite Smoke Check!");
        QAManager.sendTestCycleStartedEmail(DistributionList.ELITE);
        return JUnitCore.runClasses(SmokeCheck.class);
    }

    private static Result startServerTests() throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        if (TEST_SET.toLowerCase().contains("critic")) {
            log.debug("Requester asked only for Smoke Check test suite. Initiating SRV SmokeCheck test suite...");
            return JUnitCore.runClasses(SmokeCheck.class);
        } else {
            log.debug("Requester did not specify which suite to run. Initiating SmokeCheck, IPv4, IPv6, Headers, and Fallback suites!");
            return JUnitCore.runClasses(Server.class);
        }
    }

    private static Result startFunctionalTests(){
        if(isFunctionalSmokeRequest()){
            log.debug("Requester asked only for Smoke Check test suite. Initiating FUN SmokeCheck test suite...");
            return JUnitCore.runClasses(SmokeCheck.class);
        }
        else if (TEST_SET.toLowerCase().equals("stresscon")) {
            log.debug("Starting Connection Stress Testing!");
            return JUnitCore.runClasses();
        } else if (TEST_SET.toLowerCase().equals("stresssign")) {
            log.debug("Starting Sign In/Out Stress Testing!");
            return JUnitCore.runClasses();
        } else {
            log.debug("Starting Full Functional Regression Cycle!");
            return JUnitCore.runClasses(Functional.class);
        }
    }

    private static boolean okToFail(){
        if(FAIL_COUNT == RETRY_COUNT){
            FAIL_COUNT = 0;
            QAManager.log.debug("Giving the Go Ahead to FAIL the test case. Fail Count Reset: " + FAIL_COUNT);
            return true;
        }
        return false;
    }

    private void printTestResults(){
        log.debug("Test Case Name: " + TestInfo.name());
        log.debug("Test Case ID: " + TestInfo.id());
        log.debug("Test Case Start Time: " + TestInfo.startTime());
        log.debug("Test Case Suite Name: " + TestInfo.suite());
        log.debug("Test Case Execution Type: " + TestInfo.execType());
        log.debug("Test Case Expected Result: " + TestInfo.expected());
        log.debug("Test Case Actual Result: " + TestInfo.actual());
        log.debug("Test Case Notes: " + TestInfo.notes());
    }

    public static void runOnlyForThoseTypes(String types){
        if(!types.contains(DatabaseManager.REQUEST_TYPE)) Assume.assumeTrue(false);
    }

    public static void parseRequestType(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        QAManager.log.debug("Parsing Request Type...");
        for(String i:args){
            if(i.toUpperCase().contains("ANDROID_FUN_SMOKE")) DatabaseManager.REQUEST_TYPE = "ANDROID_FUN_SMOKE";
            else if(i.toUpperCase().contains("ANDROID_SRV_SMOKE")) DatabaseManager.REQUEST_TYPE = "";
            else if(i.toUpperCase().contains("ANDROID_SRV")) DatabaseManager.REQUEST_TYPE = "ANDROID_SRV";
            else if (i.toUpperCase().contains("ANDROID_SC")) DatabaseManager.REQUEST_TYPE = "ANDROID_SC";
            else DatabaseManager.REQUEST_TYPE = "ANDROID_FUN";
        }
        QAManager.log.debug("Done! Request type: " + DatabaseManager.REQUEST_TYPE);
    }

    public static void resetPreset(){
        log.debug("Do Preset is reset to: TRUE");
        doPreset = true;
    }

    private void doPreset() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        if(doPreset && !TestInfo.suite().equals("Installation")){
            log.debug("Do Preset is set to: TRUE - Preset will be done");
            Android.openHotspotShield().tapDisconnectButton();
            ViewGeneralSettings generalSettings = new ViewHome().openMenuDrawer().openGeneralSettings();
            String vpnServer = "";
            String vpnDomain = "";
            if(HOST.toLowerCase().contains("hss")) IP_IN = DatabaseManager.resolveHostToIP(HOST);
            switch (REQUEST_TYPE) {
                case "ANDROID_SC":
                    if (NOTES != null) vpnDomain = NOTES;
                    vpnServer = IP_IN;
                    break;
                case "ANDROID_SRV":
                case "ANDROID_SRV_SMOKE":
                case "ANDROID_FUN_SMOKE":
                    vpnServer = IP_IN;
                    vpnDomain = IP_IN;
                    break;
                case "ANDROID_FUN":
                    vpnDomain = NOTES;
                    vpnServer = IP_IN;
                    break;
            }
            generalSettings.setVpnMode(PROTOCOL);
            generalSettings.enterDebugInfo(SettingItem.DOMAIN, vpnDomain);
            generalSettings.enterDebugInfo(SettingItem.VPN_SERVER, vpnServer);
            if(!Utilities.isEmpty(vpnDomain)) generalSettings.disableSettings(SettingItem.RESOLVE_DOMAINS);
            else generalSettings.enableSettings(SettingItem.RESOLVE_DOMAINS);
            generalSettings.disableSettings(SettingItem.SHOW_POP_UPS);
            generalSettings.exitView();
            doPreset = false;
            new ViewHome().tapConnectButton();
        }else log.debug("Do Preset is set to: FALSE - Preset will not be done");
    }

    public static boolean isFunctionalRequest(){
        return DatabaseManager.REQUEST_TYPE.equals("ANDROID_FUN");
    }

    public static boolean isServerRequest(){
        return DatabaseManager.REQUEST_TYPE.equals("ANDROID_SRV");
    }

    public static boolean isServerSmokeRequest(){
        return DatabaseManager.REQUEST_TYPE.equals("ANDROID_SRV_SMOKE");
    }

    public static boolean isFunctionalSmokeRequest(){
        return DatabaseManager.REQUEST_TYPE.equals("ANDROID_FUN_SMOKE");
    }

    public static boolean isEliteRequest(){
        return DatabaseManager.REQUEST_TYPE.equals("ANDROID_SC");
    }
}
