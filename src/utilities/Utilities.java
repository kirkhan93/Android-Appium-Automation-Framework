package utilities;

import api.android.Android;
import api.googlechrome.GoogleChrome;
import api.hotspotshield.v400.ViewHome;
import managers.FailManager;
import managers.QAManager;
import managers.SystemManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import utilities.constants.Website;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;

/**
 * Created by Artur Spirin on 7/10/2015.
 **/
public class Utilities {

    private static final int BUFFER_SIZE = 4096;
    private static final Integer WAIT_TO_PROPOGATE = 6000;

    public static String NUMBERS_REGEX = "(\\d+)";
    public static String IPV4_REGEX = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    public static String IPV6_REGEX = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";
    public static String TIMER_REGEX = "^(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?[1-9])$";
    //public static String TIMER_REGEX = "^24:(59:59|0[1-9]:[0-5][0-9]|00:[0-5][1-9])$";
    public static java.util.regex.Pattern NUMBERS_PATTERN = java.util.regex.Pattern.compile(NUMBERS_REGEX);
    public static java.util.regex.Pattern IPV4_PATTERN = java.util.regex.Pattern.compile(IPV4_REGEX);
    public static java.util.regex.Pattern IPV6_PATTERN = java.util.regex.Pattern.compile(IPV6_REGEX);
    public static java.util.regex.Pattern TIMER_PATTERN = java.util.regex.Pattern.compile(TIMER_REGEX);
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String SALT = "fnkio24u3gqpfna0f431fn034";
    private static String PROTOCOL = null;
    public static final String DEBUG_ID = "17055";

    public static String getProtocol() throws IOException {

        if(Utilities.isEmpty(PROTOCOL) || PROTOCOL.equals("timeOut") || PROTOCOL.equals("DSCNCTD")) {
            boolean success = false;
            if (Curl.isAvailable()) {
                String data = SystemManager.executeCommand("adb shell /data/local/tmp/curl -L -k --max-time 10 --insecure " + Website.HTTP_X_AF_TUNNEL);
                QAManager.log.debug("Raw Output: " + data);
                try {
                    PROTOCOL = data.substring(data.indexOf(">"), data.indexOf("</h1>")).replaceAll(">", "").trim().toUpperCase();
                    success = true;
                } catch (StringIndexOutOfBoundsException e) {
                    PROTOCOL = "timeOut";
                }
                if (PROTOCOL.equals("")) {
                    PROTOCOL = "DSCNCTD";
                }
            }
            if(!success){
                PROTOCOL = new GoogleChrome().open(Website.HTTP_X_AF_TUNNEL).getContent();
            }
        }
        return PROTOCOL;
    }

    public static String extractIP(String source, Boolean IPv4){

        Matcher matcher;
        String IP = null;
        if(IPv4) matcher = IPV4_PATTERN.matcher(source);
        else matcher = IPV6_PATTERN.matcher(source);
        if (matcher.find()) {
            QAManager.log.debug("Extracted IP Address: " + matcher.group());
            IP = matcher.group();
        }
        return  IP;
    }

    public static Boolean isVpnIp(String IP){

        WebDriver driver = new HtmlUnitDriver();
        driver.get("http://10.0.0.148/ip.php");
        driver.findElement(By.name("ip")).sendKeys(IP);
        driver.findElement(By.name("get IP")).click();
        String text = driver.findElement(By.tagName("body")).getText();
        String[] list = text.split("\n");
        try{
            String[] value = list[5].split(":"); //for headless driver use 5, for other drivers use 3
            StringBuffer Server = new StringBuffer();
            Server.append(value[1]);
            String  Server_ID = Server.toString().trim();
            if (Server_ID.contains("hss")){
                QAManager.log.debug("The IP " + IP + " is hosted by: " + Server);
                driver.quit();
                return Boolean.TRUE;
            }
            else{
                QAManager.log.debug("IP " + IP + " is not recognized");
                driver.quit();
                return Boolean.FALSE;
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            QAManager.log.debug("IP " + IP + " is not hosted by us.");
            driver.quit();
            return Boolean.FALSE;
        }
    }

    public static Integer generateRandomNumber(Integer min, Integer max){
        return new Random().nextInt(min - max) + min;
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public static StringBuffer urlopen(String url) {
        try {
            URL u = new URL(url);
            StringBuffer sb = new StringBuffer();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(u.openStream()));
            String str = null;
            while((str = in.readLine())!=null) {
                sb.append(str);
            }
            in.close();
            return sb;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void Wait(long milliseconds){

        try {
            //QAManager.log.debug("Waiting for: " + milliseconds + " milliseconds");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isEmpty(String string){
        return string == null || string.equals("null") || string.equals("") || string.equals(" ") || string.split(" ").length <= 0 || string.equals("N/A");
    }

    public static void quickReset(){

        if(!ViewHome.firstLaunch && FailManager.HSS_RESET_TRIGGER >= 2){
            QAManager.log.debug("Performing reset of HSS due to Connection issues");
            Android.resetHotspotShield();
            FailManager.HSS_RESET_TRIGGER = 0;
        }
        if(ViewHome.firstLaunch){
            QAManager.log.debug("Checking if suite is exempt from Quick Reset");
            String[] ignoreSuites = new String[2];
            ignoreSuites[0] = "First User Experience";
            ignoreSuites[1] = "Clean Install";
            for(String suite:ignoreSuites){
                if(suite.equals(TestInfo.suite())) {
                    QAManager.log.warn("Suite: " +TestInfo.suite()+" is part of the ignore suites list, quick reset will not be applied to this suite!");
                    ViewHome.firstLaunch = false;
                    return;
                }
            }
            try{
                QAManager.log.debug("Doing quick reset");
                try{
                    Android.openHotspotShieldFirstTime().tapNextButton().exitView().exitView();
                }catch (NoSuchElementException e){
                    QAManager.log.warn("Ignoring this error because it happen in the preset, info will be included in the report though");
                    QAManager.log.warn("Error info from preset: "+e.getMessage());
                }
            }catch (Exception e){
                QAManager.log.error("Expected Opt-In activity and Tutorial after but one or the other did not show");
            }finally {
                QAManager.log.debug("Done");
                ViewHome.firstLaunch = false;
            }
        }
    }

    public static void downloadFile(String fileURL, String saveDir)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }

    public void broski(long timestamp, String message, long diffSeconds){
        long currentTimestamp = new Date().getSeconds();
        long diff = currentTimestamp - timestamp;
        QAManager.log.debug("Time Stamp: " + timestamp);
        QAManager.log.debug("Current Time Stamp: " + currentTimestamp);
        QAManager.log.debug("Difference: " + diff);
        if(diff>diffSeconds){
            QAManager.log.error("UI XML Elements are taking too long to update or Something is wrong with the applications state!");
            throw new AssertionError(message);
        }
    }
}
