package utilities;

import managers.DatabaseManager;
import managers.QAManager;
import org.junit.Assume;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/9/2015.
 */
public class TestInfo {

    private static String

            JIRA,
            NAME,
            SUITE,
            EXPECTED,
            ACTUAL,
            EXECUTION_TYPE,
            NOTES,
            STATUS,
            ID,
            START_TIME,
            INFO,
            COLO;

    public TestInfo name(String value){
        NAME = value;
        QAManager.log.debug("Set Test Case Name: "+ NAME);
        return this;
    }

    public static String name(){
        return NAME;
    }

    public TestInfo id(String value){
        ID = value;
        if(ID.contains("_")) ID = ID.replaceAll("_","-");
        QAManager.log.debug("Set Test Case ID: "+ ID);
        return this;
    }

    public static String id(){
        return ID;
    }

    public TestInfo startTime(String value){
        START_TIME = value;
        QAManager.log.debug("Set Test Case Start Time: " + START_TIME);
        return this;
    }

    public static String startTime(){
        return START_TIME;
    }

    public static String endTime(){
        return Utilities.DATE_FORMAT.format(new java.util.Date());
    }

    public TestInfo expected(String value){
        EXPECTED = value;
        QAManager.log.debug("Set Test Case Expected Result: "+ EXPECTED);
        return this;
    }

    public static String expected(){
        return EXPECTED;
    }

    public TestInfo actual(String value){
        ACTUAL = value;
        QAManager.log.debug("Set Test Case Actual Result: "+ ACTUAL);
        return this;
    }

    public static String actual(){
        return ACTUAL;
    }

    public TestInfo execType(String value){
        EXECUTION_TYPE = value;
        QAManager.log.debug("Set Test Case Execution Type: "+EXECUTION_TYPE);
        return this;
    }

    public static String execType(){
        return EXECUTION_TYPE;
    }

    public TestInfo suite(String value){
        SUITE = value;
        QAManager.log.debug("Set Test Case Suite Name: "+ SUITE);
        return this;
    }

    public static String suite(){
        return SUITE;
    }

    public TestInfo status(String value){
        if(value.equals("PASS")){
            QAManager.log.debug("Test Case Passed");
            QAManager.FAIL_COUNT = 0;
            QAManager.log.debug("Fail Count reset: " + QAManager.FAIL_COUNT);
        }
        else if(value.equals("FAIL")) {
            QAManager.log.debug("Test Case Failed");
            QAManager.FAIL_COUNT++;
            QAManager.log.debug("Fail Count: " + QAManager.FAIL_COUNT);
        }
        STATUS = value;
        return this;
    }

    public static String status(){
        return STATUS;
    }

    public TestInfo notes(String value){
        NOTES = value;
        return this;
    }

    public static String notes(){
        return NOTES;
    }

    public TestInfo info(String value){
        INFO = value;
        return this;
    }

    public static String info(){
        return INFO;
    }

    public TestInfo colo(String value){
        COLO = value;
        return this;
    }

    public static String colo(){
        return COLO;
    }

    public void resetTestInfo(){
        QAManager.log.debug("Test Info Reset");
        NAME = "N/A";
        EXPECTED = "N/A";
        ACTUAL = "N/A";
        NOTES = "N/A";
        STATUS = "N/A";
        ID = "N/A";
        START_TIME = "N/A";
        INFO = "N/A";
    }

    public static TestInfo validFor(String types){
        if(!types.contains(DatabaseManager.REQUEST_TYPE)) Assume.assumeTrue(false);
        return new TestInfo();
    }

    public static TestInfo invalidFor(String types){
        if(types.contains(DatabaseManager.REQUEST_TYPE)) Assume.assumeTrue(false);
        return new TestInfo();
    }

    public TestInfo jira(String value){
        JIRA = value;
        QAManager.log.debug("Set Test Case JIRA: "+ JIRA);
        return this;
    }

    public static String jira(){
        return JIRA;
    }
}
