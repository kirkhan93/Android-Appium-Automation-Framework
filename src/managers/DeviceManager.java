package managers;

import api.android.Android;
import io.appium.java_client.android.AndroidDriver;
import org.apache.log4j.Level;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;
import utilities.Utilities;
import utilities.constants.Package;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by Artur Spirin on 11/11/15.
 **/
public class DeviceManager {

    private static AndroidDriver driver = null;
    private static String DEVICE_ID;
    private static final boolean removeUnblockApp = false;
    private static int timeOut = 0;

    public static boolean isDriverExist(){
        return driver!=null;
    }

    public static void setElementLocatorTimeOut(int value, String tag){
        if(value!=timeOut){
            getDriver().manage().timeouts().implicitlyWait(value, TimeUnit.SECONDS);
            QAManager.log.debug("Element locator timeout set to: " + value + " from: " + timeOut + " Tag: " + tag);
            timeOut = value;
        }
    }

    public static void resetDriver(){
        QAManager.log.warn("Resetting Driver!");
        try{driver.quit();}catch (Exception e){QAManager.log.warn("Failed to quit the driver");}
        driver = null;
        getDriver();
    }

    public static String getDeviceId(){
        return DEVICE_ID;
    }

    private static ArrayList getAvailableDevices(){
        String output = Helper.executeCommand("adb devices").replaceAll("List of devices attached", "");
        String[] list = output.split("\n");
        ArrayList devices=new ArrayList();
        QAManager.log.debug("Getting list of connected devices:");
        for(String i : list) {
            i = i.trim();
            if(i.contains("device")){
                String device = i.replaceAll("device", "").trim();
                if(Helper.isNotBusy(device)) {
                    devices.add(device);
                } else{
                    QAManager.log.debug("Device connected but is not suitable for current request: " + device);
                }
            }
        }
        for(Object o : devices) QAManager.log.debug("Device: <" + o + "> is available to run tests");
        if(devices.size()==0){
            QAManager.log.warn("There are no available devices connected to the Host. I will wait for a device to become available. Else, you can connect an Android device and I will start running tests");
            Utilities.Wait(25000);
            return getAvailableDevices();
        }
        return devices;
    }

    public static AndroidDriver getDriver() {
        QAManager.log.setLevel(Level.DEBUG);
        if(driver == null){
            try {Helper.holdWhileInProgress();} catch (IOException e) {e.printStackTrace();}
            try {Helper.setProgressNotification(1);} catch (FileNotFoundException | UnsupportedEncodingException e) {e.printStackTrace();}
            if(removeUnblockApp) Android.uninstallApp(Package.APPIUM_UNLOCK);
            QAManager.log.debug("Creating new Driver");
            ArrayList devices = getAvailableDevices();
            for(Object device : devices){
                DEVICE_ID = device.toString();
                QAManager.log.debug("Trying device: <"+DEVICE_ID+">");
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");
                capabilities.setCapability("deviceName", Android.getDeviceModel());
                capabilities.setCapability("platformVersion", Helper.getDeviceOsVersion());
                capabilities.setCapability("platformName", "Android");
                capabilities.setCapability("app", Helper.getUnlockApp());
                QAManager.log.debug("Driver Capabilities are set!");
                try {
                    String srv = AppiumServers.servers().get(DEVICE_ID).toString();
                    QAManager.log.debug("Configuring Driver for Device: " +DEVICE_ID+" on server: "+ srv);
                    driver = new AndroidDriver(new URL(srv), capabilities);
                    break;
                } catch (JSONException e) {
                    try {Helper.setProgressNotification(0);} catch (FileNotFoundException | UnsupportedEncodingException er) {e.printStackTrace();}
                    throw new ExceptionInInitializerError("(1) Not able to create the Driver. Most likely Appium Server is not running on the given server(s).");
                } catch (MalformedURLException | SessionNotCreatedException | UnreachableBrowserException e) {
                    QAManager.log.warn(e.getMessage());
                }
                if(driver==null){
                    try {Helper.setProgressNotification(0);} catch (FileNotFoundException | UnsupportedEncodingException e) {e.printStackTrace();}
                    throw new ExceptionInInitializerError("(2) Not able to create the Driver. Most likely Appium Server is not running on the given server(s).");
                }
            }
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            DEVICE_ID = driver.getCapabilities().getCapability("deviceName").toString();
            QAManager.log.debug("Driver Created for Session ID: " + driver.getSessionId());
            QAManager.log.debug("-------- MAKE SURE THIS DRIVER INFO IS CORRECT -----------");
            QAManager.log.debug("Device name: "+driver.getCapabilities().getCapability("deviceName"));
            QAManager.log.debug("Platform Version: "+driver.getCapabilities().getCapability("platformVersion"));
            QAManager.log.debug("----------------------------------------------------------");
            try {Helper.setProgressNotification(0);} catch (FileNotFoundException | UnsupportedEncodingException e) {e.printStackTrace();}
        }
        return driver;
    }
}

class Helper{

    protected static String executeCommand(String command) {

        String output = null;
        if(getOperatingSystem().toLowerCase().contains("mac") && command.startsWith("adb")){
            command = "../../Library/Android/sdk/platform-tools/"+command;
        }
        try {
            Scanner s = new Scanner(Runtime.getRuntime().exec(command).getInputStream()).useDelimiter("\\A");
            if(s.hasNext()) output = s.next();
        } catch (IOException e) {e.printStackTrace();}
        return output;
    }

    protected static String getDeviceOsVersion(){
        String output = executeCommand("adb shell getprop ro.build.version.release");
        return output;

    }

    private static String getOperatingSystem() {
        return System.getProperty("os.name");
    }

    protected static boolean isNotBusy(String deviceID){
        String output = executeCommand("adb -s " + deviceID + " shell pm list packages | grep " + Package.APPIUM_UNLOCK);
        //QAManager.log.debug(output);
        return Utilities.isEmpty(output);
    }

    protected static File getUnlockApp(){
        //String app = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("assets/unlock_apk-debug.apk"));
        String app = "src/assets/unlock_apk-debug.apk";
        QAManager.log.debug("Installing Unblock App from: " + app);
        return new File(app);
    }

    protected static Boolean isRooted(String deviceID){
        if(!SystemManager.isMac()){
            String output = executeCommand("wmic process call create \"adb -s "+deviceID+" shell su\"");
            String PID = output.substring(output.indexOf("ProcessId = "), output.indexOf(";")).replaceAll("ProcessId = ", "");
            String process = executeCommand("taskkill /PID " + PID);
            if(process != null && process.contains("SUCCESS")){
                executeCommand("taskkill /PID " + PID);
                QAManager.log.debug("Found rooted device");
                return true;
            }else return false;
        }else {
            QAManager.log.debug("Cant get root status on Mac. Will set root to false by default");
            return false;
        }
    }

    protected static void setProgressNotification(int value) throws FileNotFoundException, UnsupportedEncodingException {
        if(value==1) QAManager.log.debug("Informing other builds that I'm trying to create Driver");
        else if(value==0) QAManager.log.debug("Informing other builds that I'm done creating Driver");
        File outputFile = new File("src/assets/driverController.txt");
        PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
        writer.println(value);
        writer.close();
        QAManager.log.debug("Done");
    }

    protected static boolean inProgress() throws IOException {
        File file = new File("src/assets/driverController.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        if((line = br.readLine()) != null) br.close();
        return line != null && line.contains("1");
    }

    protected static void holdWhileInProgress() throws IOException {
        int ls = 30;
        while(ls != 0 && inProgress()){
            if(ls==30) QAManager.log.warn("Holding driver creation, another driver is in the process of being created");
            if(ls==10) QAManager.log.warn("Seems like another driver is still being created, will hold for "+ls+" seconds longer");
            if(ls==5) QAManager.log.warn("Seems like another driver is still being created, will hold for "+ls+" seconds longer");
            if(ls==1) QAManager.log.warn("Driver status did not update but I'm not going to wait any longer, proceeding on to driver creation");
            ls--;
            Utilities.Wait(1500);
        }
    }
}

class AppiumServers{

    protected static JSONObject servers() {

        try {
            return new JSONObject()

                    .put("ZX1G22BV8Z", "http://0.0.0.0:4722/wd/hub")            // Nexus 6
                    .put("ac168492", "http://0.0.0.0:4722/wd/hub")              // Galaxy S3 (Rooted)
                    .put("TA9911V2BP", "http://127.0.0.1:4723/wd/hub")          // MotoX 2
                    .put("T01130LYXF", "http://127.0.0.2:4725/wd/hub")        // MotoX
                    //.put("T01130LYXF", "http://0.0.0.0:4722/wd/hub")            // MotoX
                    .put("04f0440861d243e7", "http://127.0.0.3:4726/wd/hub")    // Nexus 4 (Rooted)
                    .put("T076000YPQ", "http://127.0.0.4:4727/wd/hub");         // MotoX (Rooted)

        } catch (JSONException e) {
            return null;
        }
    }
}