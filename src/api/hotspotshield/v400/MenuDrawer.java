package api.hotspotshield.v400;

import api.android.Android;
import api.android.AndroidSettingsAppsPermissions;
import api.gmail.GMailComposeActivity;
import managers.QAManager;
import org.openqa.selenium.NoSuchElementException;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class MenuDrawer extends Android{

    private static UiObject

            LOGO                = null,
            ACCOUNT_NAME        = null,
            ACCOUNT_TYPE        = null,
            UP_SELL             = null,
            APP_LOCK            = null,
            FACEBOOK            = null,
            SETTINGS            = null,
            APP_LIST            = null,
            NETWORK             = null,
            GENERAL_SETTINGS    = null,
            ABOUT               = null,
            CONTACT_SUPPORT     = null,
            HELP                = null,
            QUIT                = null,
            FEEDBACK            = null,
            TEST_ELITE          = null,
            TEST_PERF           = null,
            TEST_ADS            = null,
            TEST_MENU           = null,
            TEST_UI             = null,
            TEST_BANDWIDTHWALL  = null;

    private static UiObject logo(){
        if(LOGO == null) LOGO = new UiSelector().resourceId(getAppPackageId()+":id/about_logo").makeUiObject();
        return LOGO;
    }

    private static UiObject accountName(){
        if(ACCOUNT_NAME == null) ACCOUNT_NAME = new UiSelector().resourceId(getAppPackageId()+":id/about_account_name").makeUiObject();
        return ACCOUNT_NAME;
    }

    private static UiObject accountType(){
        if(ACCOUNT_TYPE == null) ACCOUNT_TYPE = new UiSelector().resourceId(getAppPackageId()+":id/about_account_type").makeUiObject();
        return ACCOUNT_TYPE;
    }

    private static UiObject upSell(){
        if(UP_SELL == null) UP_SELL = new UiSelector().text("Upgrade to Elite").makeUiObject();
        return UP_SELL;
    }

    private static UiObject appLock(){
        if(APP_LOCK == null) APP_LOCK = new UiSelector().text("App Lock").makeUiObject();
        return APP_LOCK;
    }

    private static UiObject facebook(){
        if(FACEBOOK == null) FACEBOOK = new UiSelector().text("Like Us on Facebook").makeUiObject();
        return FACEBOOK;
    }

    private static UiObject settings(){
        if(SETTINGS == null) SETTINGS = new UiSelector().text("Settings").makeUiObject();
        return SETTINGS;
    }

    private static UiObject appList(){
        if(APP_LIST == null) APP_LIST = new UiSelector().text("App List").makeUiObject();
        return APP_LIST;
    }

    private static UiObject network(){
        if(NETWORK == null) NETWORK = new UiSelector().text("Network").makeUiObject();
        return NETWORK;
    }

    private static UiObject generalSettings(){
        if(GENERAL_SETTINGS == null) GENERAL_SETTINGS = new UiSelector().text("General Settings").makeUiObject();
        return GENERAL_SETTINGS;
    }

    private static UiObject about(){
        if(ABOUT == null) ABOUT = new UiSelector().text("About").makeUiObject();
        return ABOUT;
    }

    private static UiObject contactSupport(){
        if(CONTACT_SUPPORT == null) CONTACT_SUPPORT = new UiSelector().text("Contact Support").makeUiObject();
        return CONTACT_SUPPORT;
    }

    private static UiObject help(){
        if(HELP == null) HELP = new UiSelector().text("Help").makeUiObject();
        return HELP;
    }

    private static UiObject quit(){
        if(QUIT == null) QUIT = new UiSelector().text("Quit").makeUiObject();
        return QUIT;
    }

    private static UiObject feedback(){
        if(FEEDBACK == null) FEEDBACK = new UiSelector().text("DEBUG> Feedback").makeUiObject();
        return FEEDBACK;
    }

    private static UiObject testAds(){
        if(TEST_ADS == null) TEST_ADS = new UiSelector().text("DEBUG> Test ADS").makeUiObject();
        return TEST_ADS;
    }

    private static UiObject testElite(){
        if(TEST_ELITE == null) TEST_ELITE = new UiSelector().text("DEBUG> Test Elite").makeUiObject();
        return TEST_ELITE;
    }

    private static UiObject testPerf(){
        if(TEST_PERF == null) TEST_PERF = new UiSelector().text("DEBUG> Perf").makeUiObject();
        return TEST_PERF;
    }

    private static UiObject testMenu(){
        if(TEST_MENU == null) TEST_MENU = new UiSelector().text("DEBUG> Test MENU").makeUiObject();
        return TEST_MENU;
    }

    private static UiObject testBandwidthWall(){
        if(TEST_BANDWIDTHWALL == null) TEST_BANDWIDTHWALL = new UiSelector().text("DEBUG> Test Bandwidthwall").makeUiObject();
        return TEST_BANDWIDTHWALL;
    }

    private static UiObject testUi(){
        if(TEST_UI == null) TEST_UI = new UiSelector().text("DEBUG> Test UI").makeUiObject();
        return TEST_UI;
    }

    public static boolean isOpen(){
        QAManager.log.debug("Checking if Menu Drawer is already open");
        return logo().exists();
    }

    public ViewAccount openAccount(){
        QAManager.log.debug("Opening Account Activity");
        accountName().tap();
        return new ViewAccount().waitForViewToLoad();
    }

    public MenuDrawer expandSettings(){
        QAManager.log.debug("Expanding Settings...");
        try{
            if(settings().exists())settings().tap();
            else settings().scrollTo().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't Expand Settings, Element absent");
        }
        return this;
    }

    public ViewAppList openAppList(){
        expandSettings();
        QAManager.log.debug("Opening App List");
        try{
            if(appList().exists())appList().tap();
            else appList().scrollTo().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't Open App List, Element absent");
        }
        if(new AndroidSettingsAppsPermissions().grantAppsPermissions()) openAppList();
        return new ViewAppList().waitForViewToLoad();
    }

    public ViewUpSell openUpSell(){
        QAManager.log.debug("Opening Up-Sell");
        try{
            upSell().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't open Up-Sell, Element absent");
        }
        return new ViewUpSell().waitForViewToLoad();
    }

    public ViewAppLock openAppLock(){
        QAManager.log.debug("Opening App Lock");
        try{
            appLock().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't open App Lock, Element absent");
        }
        return new ViewAppLock().waitForViewToLoad();
    }

    public ViewNetworks openNetworks(){
        expandSettings();
        QAManager.log.debug("Opening Network");
        try{
            if(network().exists())network().tap();
            else network().scrollTo().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't open Networks, Element absent");
        }
        return new ViewNetworks().waitForViewToLoad();
    }

    public ViewGeneralSettings openGeneralSettings(){
        expandSettings();
        QAManager.log.debug("Opening General Settings");
        try{
            if(generalSettings().exists())generalSettings().tap();
            else generalSettings().scrollTo().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't open General Settings, Element absent");
        }
        return new ViewGeneralSettings().waitForViewToLoad();
    }

    public ViewAbout openAbout(){
        expandSettings();
        QAManager.log.debug("Opening About");
        try{
            if(about().exists())about().tap();
            else about().scrollTo().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't open About, Element absent");
        }
        return new ViewAbout().waitForViewToLoad();
    }

    public GMailComposeActivity tapContactSupport(){
        QAManager.log.debug("Tapping Contact Support");
        try{
            if(contactSupport().exists())contactSupport().tap();
            else contactSupport().scrollTo().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't tap Contact Support, Element absent");
        }
        return new GMailComposeActivity().waitForViewToLoad();
    }

    public ViewHelp openHelp(){
        QAManager.log.debug("Opening Help");
        try{
            if(help().exists())help().tap();
            else help().scrollTo().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't open Help, Element absent");
        }
        return new ViewHelp().waitForViewToLoad();
    }

    public DialogFeedback tapDebugFeedback(){
        QAManager.log.debug("Tapping Debug Feedback Dialog");
        try{
            if(feedback().exists())feedback().tap();
            else feedback().scrollTo().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't tap Debug Feedback Dialog, Element absent");
        }
        return new DialogFeedback().waitForDialogToLoad();
    }

    public DialogDebugTestAds tapDebugTestAds(){
        QAManager.log.debug("Tapping Debug Test Ads");
        try{
            if(testAds().exists())testAds().tap();
            else testAds().scrollTo().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't tap Debug Test Ads, Element absent");
        }
        return new DialogDebugTestAds();
    }

    public DialogDebugMenu tapDebugMenu(){
        QAManager.log.debug("Tapping Debug Menu");
        try{
            if(testMenu().exists())testMenu().tap();
            else testMenu().scrollTo().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't tap Debug Menu, Element absent");
        }
        return new DialogDebugMenu().waitForDialogToLoad();
    }

    public WebViewBandwidthWall2 tapDebugTestBandwidthWall(){
        QAManager.log.debug("Tapping Debug Bandwidth Wall");
        try{
            if(testBandwidthWall().exists())testBandwidthWall().tap();
            else testBandwidthWall().scrollTo().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't tap Debug Test Bandwidth Wall, Element absent");
        }
        return new WebViewBandwidthWall2().waitForViewToLoad();
    }

    public DialogQuit tapQuit(){
        QAManager.log.debug("Tapping Quit");
        try{
            if(quit().exists())quit().tap();
            else quit().scrollTo().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't tap Quit, Element absent");
        }
        return new DialogQuit().waitForDialogToLoad().waitForDialogToLoad();
    }

    public MenuDrawer waitForDrawerToOpen(){
        QAManager.log.debug("Waiting for the Menu Drawer to open");
        logo().waitForSelector(5);
        return this;
    }

    public ViewHome closeDrawer(){
        QAManager.log.debug("Closing Menu Drawer");
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }

    public boolean isElite(){
        return accountType().text().contains("Elite");
    }

    public boolean isSignedOut(){
        return accountName().text().contains("Sign in or create account");
    }
}