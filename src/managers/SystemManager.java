package managers;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import utilities.Utilities;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Artur Spirin on 10/9/15.
 **/
public class SystemManager extends Utilities{

    public static String OS = null;
    public static String SOURCE_FOLDER = null;
    private static boolean debug1 = true;
    private static boolean debug2 = true;

    public static String getOperatingSystem() {
        if(OS==null){
            OS = System.getProperty("os.name");
            QAManager.log.debug("Host Operating System is: " + OS);
        }
        return OS;
    }

    public static boolean isWindows() {
        return getOperatingSystem().startsWith("Windows");
    }

    public static boolean isMac() {
        return getOperatingSystem().startsWith("Mac");
    }

    public static String executeCommand(String command) {

        String output = null;
        if(command.contains("adb") && DeviceManager.getDeviceId() != null){
            //QAManager.log.debug("Command was intended for Android debug bridge(adb): " + command);
            command = command.replace("adb", "adb -s " + DeviceManager.getDeviceId());
            if(debug1 && !command.contains("wmic")){
                QAManager.log.debug("Modified command: " + command);
                debug1 = false;
            }
            else if(debug2 && !command.contains("wmic")){
                QAManager.log.debug("Modified command: " + command);
                debug2 = false;
            }
        }
        if(getOperatingSystem().toLowerCase().contains("mac") && command.startsWith("adb")){
            command = "../../Library/Android/sdk/platform-tools/"+command;
        }
        try {
            Scanner s = new Scanner(Runtime.getRuntime().exec(command).getInputStream()).useDelimiter("\\A");
            if(s.hasNext()) output = s.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * This method will create path to the directory that contains either the APK file or the .zip archive that contains the APK file
     * @param targetFile either the APK or .zip File (ex: hss_v406_google_1012-1604.zip or v406_debug_google_10-08_09-43_dev.apk)
     *                   zip archives is the new way to package files after build is completed in Gradle. This param should
     *                   be passed in to the sst request as ...&svn=<name of zip/apk file>!
     * @return
     */
    public static String createPathToFile(String targetFile){

        FileSystemView sysView = FileSystemView.getFileSystemView();
        File[] paths = File.listRoots();
        String pathToTargetFile = null;
        try{
            for(File path:paths) {
                String description = sysView.getSystemTypeDescription(path);
                if(description.toLowerCase().equals("network drive")){
                    String sourceFolder;
                    if(!targetFile.contains(".zip")){
                        sourceFolder = targetFile.substring(targetFile.indexOf("v"), targetFile.indexOf("_"));
                    }else{
                        String[] list = targetFile.split("_");
                        //sourceFolder = targetFile.substring(targetFile.indexOf("hss_v"), targetFile.indexOf("_")).replace("hss_", "");
                        sourceFolder = list[1];
                        SOURCE_FOLDER = sourceFolder;
                    }
                    pathToTargetFile = path + String.format("Android\\builds\\%s\\%s", sourceFolder, targetFile);
                }
            }
        }catch(StringIndexOutOfBoundsException e){
            QAManager.log.debug("Failed to resolve targetFile path");
        }
        QAManager.log.debug("File Path to the targetFile: " + pathToTargetFile);
        return pathToTargetFile;
    }

    public static String getCurrentDirectory(){return System.getProperty("user.dir");}

    public static void unzip(File zipFile, String targetFileDirectory, String password) throws ZipException {

        QAManager.log.debug("Unzipping: " + zipFile + " To: " + targetFileDirectory);
        ZipFile ZippedFile = new ZipFile(zipFile);
        if (ZippedFile.isEncrypted()) {
            ZippedFile.setPassword(password);
        }
        ZippedFile.extractAll(targetFileDirectory);
        QAManager.log.debug("Unzip Done!");

    }

    public static ArrayList getDirectoryContent(String dirLocation){

        File directory = new File(dirLocation);
        return new ArrayList<File>(Arrays.asList(directory.listFiles()));
    }

    public static void addShutdownHook(){

        Runtime.getRuntime().addShutdownHook(new Thread(){

            @Override
            public void run(){
                    QAManager.log.debug("Application Shutdown detected. Cleaning up");
                    FailManager.stopTcpDump();
                    FailManager.stopLogcat();
                    FailManager.stopMasterLogcat();
                    DeviceManager.getDriver().quit();
                    QAManager.log.debug("Shutdown successful!");
                }
        });
    }

    public static void killProcess(String processID){
        if(processID!=null) processID = processID.trim();
        String process = executeCommand("taskkill /PID " + processID);
        if(process!=null && process.contains("SUCCESS")) QAManager.log.debug("Sent termination to PID: <"+processID+"> and got: SUCCESS");
        else{
            if(process!=null) QAManager.log.debug("Sent termination to PID: <"+processID+"> and got: "+process);
            QAManager.log.warn("Got null when tried to stop process!");
        }
    }
}