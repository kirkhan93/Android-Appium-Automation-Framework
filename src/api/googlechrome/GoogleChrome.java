package api.googlechrome;

import api.android.Android;
import managers.DeviceManager;
import managers.QAManager;
import org.apache.log4j.Level;
import org.openqa.selenium.NoSuchElementException;
import utilities.UiObject;
import utilities.UiSelector;
import utilities.Utilities;
import utilities.constants.Package;

import java.util.regex.Matcher;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 **/
public class GoogleChrome extends Android {

    private String lastWebsite;
    private String curlData;
    private static boolean useCurl = false;
    private static UiObject

            ALLOW_BUTTON    = null,
            URL_BAR         = null,
            WEB_PAGE        = null,
            WARNING         = null,
            ADVANCED_LINK   = null,
            PROCEED_LINK    = null,
            TERMS           = null,
            NO_THANKS       = null,
            PROGRESS_BAR    = null;

    private static UiObject urlBar(){
        if(URL_BAR == null) URL_BAR = new UiSelector().resourceId("com.android.chrome:id/url_bar").makeUiObject();
        return URL_BAR;
    }

    private static UiObject allowButton(){
        if(ALLOW_BUTTON == null) ALLOW_BUTTON = new UiSelector().resourceId("com.android.chrome:id/button_primary").makeUiObject();
        return ALLOW_BUTTON;
    }

    private static UiObject webPage(){
        if(WEB_PAGE == null) WEB_PAGE = new UiSelector().className("android.view.View").clickable(true).makeUiObject();
        return WEB_PAGE;
    }

    private static UiObject warning(){
        if(WARNING == null) WARNING = new UiSelector().description("Your connection is not private").makeUiObject();
        return WARNING;
    }

    private static UiObject advancedLink(){
        if(ADVANCED_LINK == null) ADVANCED_LINK = new UiSelector().description("Advanced").makeUiObject();
        return ADVANCED_LINK;
    }

    private static UiObject proceedList(){
        if(PROCEED_LINK == null) PROCEED_LINK = new UiSelector().descriptionContains("Proceed to").makeUiObject();
        return PROCEED_LINK;
    }

    private static UiObject terms(){
        if(TERMS == null) TERMS = new UiSelector().resourceId("com.android.chrome:id/terms_accept").makeUiObject();
        return TERMS;
    }

    private static UiObject noThanks(){
        if(NO_THANKS == null) NO_THANKS = new UiSelector().resourceId("com.android.chrome:id/negative_button").makeUiObject();
        return NO_THANKS;
    }

    private static UiObject progressBar(){
        if(PROGRESS_BAR == null) PROGRESS_BAR = new UiSelector().resourceId("com.android.chrome:id/progress").makeUiObject();
        return PROGRESS_BAR;
    }

    private static boolean loading(){
        return progressBar().exists();
    }

    public String getIpAddress(Boolean IPv4) {

        QAManager.log.setLevel(Level.ALL);
        String content, IP;
        if(!useCurl) content = getContent();
        else content = curlData;
        Matcher matcher;
        if(IPv4) matcher = IPV4_PATTERN.matcher(content);
        else matcher = IPV6_PATTERN.matcher(content);
        if (matcher.find()) {
            IP = matcher.group();
            QAManager.log.debug("Extracted IP Address: " + IP);
        }else{
            matcher = IPV4_PATTERN.matcher(content);
            if(!IPv4 && matcher.find()){
                IP = matcher.group();
                QAManager.log.debug("Extracted IPv4 Address instead of IPv6: " + IP);
                throw new AssertionError("Expected IPv6 but got IPv4: " + IP);
            }else throw new AssertionError("Failed to extract IP from <"+content+">");
        }
        return  IP;
    }

    public String getIpFromGoogleSearchPage(){
        String webAddress = "https://www.google.com/search?q=what+is+my#q=my+ip";
        //String xPath = "//*[@resource-id='rso']/android.view.View/android.widget.ListView/android.view.View"; //works with Lollipop and up
        //String xPathTest = "//*[@content-desc='Your public IP address']";
        String xPath = "//*[@content-desc='Your public IP address']/preceding::*[1]";
        open(webAddress);
        if(allowButton().exists()) allowButton().tap();
        //System.out.println("Content Desc: " + DeviceManager.getDriver().findElementByXPath(xPathTest).getAttribute("name"));
        try{
            return DeviceManager.getDriver().findElementByXPath(xPath).getAttribute("name");
        }catch (NoSuchElementException e){
            throw new AssertionError("Failed to extract IP from the current webpage");
        }
    }

    public void reset() {
        forceStopApp(Package.CHROME);
        QAManager.log.debug("Google Chrome was Reset!");
    }

    public GoogleChrome open(String website) {
        QAManager.log.debug("Google Chrome is Opening: " + website);
        lastWebsite = website; useCurl = false;
        if(isMac()){
            executeCommand("../../Library/Android/sdk/platform-tools/adb shell am start -a android.intent.action.VIEW -d " + website);
        }else executeCommand("adb shell am start -a android.intent.action.VIEW -d " + website);
        if(terms().exists()) terms().tap();
        if(noThanks().exists()) noThanks().tap();
        /*if(certificateWarningShown()){
            QAManager.log.debug("Unable to open "+website+" via Google Chrome due to Certificate warning, trying cURL");
            useCurl = true;
            curlData = new Curl().get(website).getContent();
        }*/
        return waitForPageToLoad();
    }

    public static boolean certificateWarningShown(){
        return warning().exists();
    }

    public String getContent(){
        String content;
        if(!useCurl) content = webPage().getAttribute("name");//DeviceManager.getDriver().findElementByAndroidUIAutomator(WEB_PAGE).getAttribute("name");
        else content = curlData;
        QAManager.log.debug("Got following content from the page in Google Chrome: "+content);
        return content;
    }

    public String getCurrentUrl(){
        String URL;
        if(!useCurl){
            urlBar().tap();
            URL = urlBar().text();
            QAManager.log.debug("Current URL in Google Chrome is: " + URL);
            pressBackButton();
        }else URL = lastWebsite;
        return URL;
    }

    public GoogleChrome waitForPageToLoad() {

        QAManager.log.debug("Waiting for page to load...");
        for(int ls=25; ls!=0; ls--){
            if(!loading()) break;
            if(certificateWarningShown()) break;
        }
        if(Utilities.isEmpty(getContent()) && loading()) throw new AssertionError("Web Page failed to load!");
        QAManager.log.debug("Page Loaded Successfully!");
        return new GoogleChrome();
    }
}