package managers;

import api.android.Android;
import org.apache.commons.io.FileUtils;
import utilities.TestInfo;
import utilities.Utilities;

import java.io.*;

/**
 * Created by Artur Spirin on 7/10/2015.
 **/

/**
 * Only Windows is supported by this class!
 */

public class FailManager {

    protected String localTarget = SystemManager.getCurrentDirectory()+"\\"+ DatabaseManager.REQUEST_ID+"\\"+TestInfo.id();

    public static Integer ERROR_1013 = 0;
    public static int HSS_RESET_TRIGGER = 0;
    public static Boolean UI_DISCONNECTED_FROM_SERVICE = false;
    public static String LOGCAT_PID = null;
    public static String MASTER_LOGCAT_PID = null;
    private static String TCPDUMP_PID = null;
    private static boolean TCP_DUMP_BAT_CREATED = false;
    private static String artifactUid = "";

    private static void setArtifactUid(){
        if(QAManager.isServerSmokeRequest() && Utilities.isEmpty(artifactUid)){
            artifactUid = "_"+DatabaseManager.SVN
                    .replaceAll("-", "")
                    .replaceAll(":", "")
                    .replaceAll(" ", "");
            QAManager.log.debug("Set Test Artifact UID to: "+artifactUid);
        }
    }

    private static String getArtifactUid(){
        setArtifactUid();
        return artifactUid;
    }

    private static String getLocalTargetDir(String testCaseID){
        return String.format(".\\%s\\%s", DatabaseManager.REQUEST_ID, testCaseID);
    }

    private static String getRemoteTargetDir(String testCaseID){
        return String.format("Z:\\QA\\Automation\\Android\\Artifacts\\%s\\%s%s", DatabaseManager.REQUEST_ID, testCaseID, getArtifactUid());
    }

    public static String moveLocalFailDir(String testCaseID){
        if(SystemManager.isWindows()){
            File source = new File(getLocalTargetDir(testCaseID));
            File target = new File(getRemoteTargetDir(testCaseID));
            try {
                QAManager.log.debug("Moving test artifacts, for the failed test case, over to the AFFS2 Server...");
                FileUtils.copyDirectory(source, target);
                QAManager.log.debug("Successfully moved artifacts from: "+source+" to: "+target);
            } catch (IOException e) {
                QAManager.log.debug("Failed to move artifacts from: "+source+" to: "+target);
                e.printStackTrace();
            }
            return "<a target=\"_blank\" href=\"file://///Volumes/Public/QA/Automation/Android/Artifacts/"+DatabaseManager.REQUEST_ID+"/"+testCaseID+getArtifactUid()+"\">Check Logs</a>";
        }else return "Not Supported";
    }

    public static void createStartTcpDumpBat() throws IOException {
        if(!TCP_DUMP_BAT_CREATED){
            QAManager.log.debug("Creating startTcpdump.bat");
            File template = new File("src/assets/startTcpdumpTemplate.txt");
            File outputFile = new File("src/assets/startTcpDump.bat");
            PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
            BufferedReader br = new BufferedReader(new FileReader(template));
            String line;
            while((line = br.readLine()) != null){
                String deviceId = DeviceManager.getDeviceId();
                if(deviceId != null) line = line.replaceAll("adb", "adb -s "+deviceId);
                writer.println(line);
            }
            writer.close();
            br.close();
            TCP_DUMP_BAT_CREATED = true;
        }
    }

    private static String getTcpDumpPid() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("pid.txt"));
        TCPDUMP_PID = br.readLine().replaceAll("\"", "");
        br.close();
        QAManager.log.debug("Started Tcpdump on PID: <"+TCPDUMP_PID+">");
        return TCPDUMP_PID;
    }

    public static void startTcpDump() throws IOException {
        if(TCPDUMP_PID == null && Android.isRooted() && SystemManager.isWindows()){
            createStartTcpDumpBat();
            QAManager.log.debug("Starting Tcpdump...");
            SystemManager.executeCommand("src/assets/startTcpdump.bat");
            TCPDUMP_PID = getTcpDumpPid();
            QAManager.log.debug("Sending Start of Test Ping...");
            SystemManager.executeCommand("adb shell ping -c 1 START_OF_TEST");
        }else{
            if(!Android.isRooted()) QAManager.log.warn("Device is not suitable to run tcpdump");
            else if(SystemManager.isMac()) QAManager.log.warn("System Environment is not suitable to run tcpdump");
            else QAManager.log.warn("Tcpdump already running, not starting another process!");
        }
    }

    public static void stopTcpDump(){
        if(TCPDUMP_PID != null){
            QAManager.log.debug("Sending End of Test Ping...");
            SystemManager.executeCommand("adb shell ping -c 1 END_OF_TEST");
            QAManager.log.debug("Stopping Tcpdump on PID: <"+TCPDUMP_PID+">");
            SystemManager.killProcess(TCPDUMP_PID);
            TCPDUMP_PID = null;
            getTcpDump();
        }else QAManager.log.warn("Tcpdump process is not running, nothing to stop!");
    }

    private static void getTcpDump(){
        QAManager.log.debug("Getting tcpdump...");
        SystemManager.executeCommand(String.format("adb pull /sdcard/tcpdump.pcap %s\\%s_Tcpdump.pcap", new FailManager().localTarget, TestInfo.id()));
        SystemManager.executeCommand("adb shell rm /sdcard/tcpdump.pcap");
        QAManager.log.debug("Done!");
    }

    private static void getScreenshot(String targetDir){
        QAManager.log.debug("Taking Screenshot...");
        SystemManager.executeCommand(String.format("adb shell screencap -p /sdcard/%s.png", TestInfo.id()));
        SystemManager.executeCommand(String.format("adb pull /sdcard/%s.png %s\\%s.png", TestInfo.id(), targetDir, TestInfo.id()));
        SystemManager.executeCommand(String.format("adb shell rm /sdcard/%s.png", TestInfo.id()));
        QAManager.log.debug("Done!");
    }

    private static void getStacktrace(String targetDir, Throwable error){
        QAManager.log.debug("Taking StackTrace...");
        File file = new File(String.format("%s\\%s_StackTrace.txt", targetDir, TestInfo.id()));
        try {
            PrintStream ps = new PrintStream(file);
            error.printStackTrace(ps);
        } catch (FileNotFoundException e) {e.printStackTrace();}
        QAManager.log.debug("Done!");
    }

    public static void collectTestArtifacts(Throwable ERROR){
        if(SystemManager.isWindows()){
            String targetDir = String.format("%s/%s/", DatabaseManager.REQUEST_ID, TestInfo.id());
            QAManager.log.debug("Collecting test artifacts, for the failed test case! Target dir: " + targetDir);
            getLogcat(targetDir);
            getScreenshot(targetDir);
            if(ERROR != null) getStacktrace(targetDir, ERROR);
            QAManager.log.debug("Artifacts collected!");
        }
    }

    public static void startLogcat(){
        if(LOGCAT_PID == null && SystemManager.isWindows()){
            Android.clearLogBuffer();
            QAManager.log.debug("Starting Logcat ...");
            String output = SystemManager.executeCommand("wmic process call create \"adb logcat -v threadtime -f /sdcard/LOG.txt\"");
            LOGCAT_PID = output.substring(output.indexOf("ProcessId = "), output.indexOf(";")).replaceAll("ProcessId = ", "");
            QAManager.log.debug("Started Logcat on PID: <"+LOGCAT_PID+">");
        }else{
            if(SystemManager.isMac()) QAManager.log.warn("System Environment is not suitable to run logcat");
            else QAManager.log.warn("Logcat already running, not starting another process!");
        }
    }

    public static void stopLogcat(){
        if(LOGCAT_PID != null){
            QAManager.log.debug("Stopping Logcat on PID: <"+LOGCAT_PID+">");
            SystemManager.killProcess(LOGCAT_PID);
            LOGCAT_PID = null;
        }else QAManager.log.debug("Logcat process is not running, nothing to stop!");
    }

    private static void getLogcat(String targetDir){
        QAManager.log.debug("Getting logcat...");
        SystemManager.executeCommand(String.format("adb pull /sdcard/LOG.txt %s\\%s_Log.txt", targetDir, TestInfo.id()));
        SystemManager.executeCommand("adb shell rm /sdcard/LOG.txt");
        QAManager.log.debug("Done!");
    }

    public static void startMasterLogcat(){
        if(MASTER_LOGCAT_PID == null && SystemManager.isWindows()){
            Android.clearLogBuffer();
            QAManager.log.debug("Starting Master Logcat...");
            String output = SystemManager.executeCommand("wmic process call create \"adb logcat -v threadtime -f /sdcard/FULL_LOG.txt\"");
            MASTER_LOGCAT_PID = output.substring(output.indexOf("ProcessId = "), output.indexOf(";")).replaceAll("ProcessId = ", "");
            QAManager.log.debug("Started Master Logcat on PID: <"+ MASTER_LOGCAT_PID +">");
        }else{
            if(SystemManager.isMac()) QAManager.log.warn("System Environment is not suitable to run logcat");
            else QAManager.log.warn("Master Logcat already running, not starting another process!");
        }
    }

    public static void stopMasterLogcat(){
        if(MASTER_LOGCAT_PID != null){
            QAManager.log.debug("Stopping Master Logcat on PID: <"+ MASTER_LOGCAT_PID +">...");
            SystemManager.killProcess(MASTER_LOGCAT_PID);
            MASTER_LOGCAT_PID = null;
        }else QAManager.log.debug("Nothing to stop. Master Logcat is not running.");
    }

    public static void getMasterLogcat(){
        if(SystemManager.isWindows()){
            QAManager.log.debug("Retrieving Master Log from devices memory...");
            String targetDir = String.format(".\\%s\\", DatabaseManager.REQUEST_ID);
            SystemManager.executeCommand(String.format("adb pull /sdcard/FULL_LOG.txt %s\\FULL_LOG.txt", targetDir));
            QAManager.log.debug("Removing Master Log from devices memory...");
            SystemManager.executeCommand("adb shell rm /sdcard/FULL_LOG.txt");
            QAManager.log.debug("Making a copy of the Master Log on AFFS2...");
            File source = new File(String.format(".\\%s\\", DatabaseManager.REQUEST_ID));
            File target = new File(String.format("Z:\\QA\\Automation\\Android\\Artifacts\\%s\\", DatabaseManager.REQUEST_ID));
            try {
                FileUtils.copyDirectory(source, target);
                QAManager.log.debug("Moved successfully!");
            } catch (IOException e) {
                QAManager.log.debug("Failed to move files!");
                e.printStackTrace();
            }
            QAManager.log.debug("Done!");
        }
    }

    //TODO Implement
    public static void getTracesAnr(){}
}
