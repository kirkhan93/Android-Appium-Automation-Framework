package api.googleplay;

import api.android.Android;
import api.android.AndroidDesktop;
import io.appium.java_client.android.AndroidKeyCode;
import managers.DeviceManager;
import managers.QAManager;
import managers.ReportingManager;
import utilities.UiObject;
import utilities.UiSelector;
import utilities.Utilities;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/8/2015.
 */
public class GooglePlayApplicationPage extends Android{

    private static UiObject

            SEARCH_ICON             = null,
            SEARCH_FIELD            = null,
            UNINSTALL_BUTTON        = null,
            INSTALL_BUTTON          = null,
            LAUNCH_BUTTON           = null,
            BUY_BUTTON              = null,
            DOWNLOAD_PROGRESS_BAR   = null,
            BADGE_CONTAINER         = null,
            APP_TITLE               = null,
            OK_BUTTON               = null,
            OPEN_BUTTON             = null,
            APP_CREATOR             = null;

    private static UiObject searchIcon(){
        if(SEARCH_ICON == null) SEARCH_ICON = new UiSelector().resourceId("com.android.vending:id/search_button").makeUiObject();
        return SEARCH_ICON;
    }

    private static UiObject searchField(){
        if(SEARCH_FIELD == null) SEARCH_FIELD = new UiSelector().resourceId("com.android.vending:id/search_box_text_input").makeUiObject();
        return SEARCH_FIELD;
    }

    private static UiObject uninstallButton(){
        if(UNINSTALL_BUTTON == null) UNINSTALL_BUTTON = new UiSelector().resourceId("com.android.vending:id/uninstall_button").makeUiObject();
        return UNINSTALL_BUTTON;
    }

    private static UiObject installButton(){
        if(INSTALL_BUTTON == null) INSTALL_BUTTON = new UiSelector().resourceId("com.android.vending:id/uninstall_button").makeUiObject();
        return INSTALL_BUTTON;
    }

    private static UiObject launchButton(){
        if(LAUNCH_BUTTON == null) LAUNCH_BUTTON = new UiSelector().resourceId("com.android.vending:id/launch_button").makeUiObject();
        return LAUNCH_BUTTON;
    }

    private static UiObject progressBar(){
        if(BUY_BUTTON == null) BUY_BUTTON = new UiSelector().resourceId("com.android.vending:id/buy_button").makeUiObject();
        return BUY_BUTTON;
    }

    private static UiObject downloadProgressBar(){
        if(DOWNLOAD_PROGRESS_BAR == null) DOWNLOAD_PROGRESS_BAR = new UiSelector().resourceId("com.android.vending:id/progress_bar").makeUiObject();
        return DOWNLOAD_PROGRESS_BAR;
    }

    private static UiObject badgeContainer(){
        if(BADGE_CONTAINER == null) BADGE_CONTAINER = new UiSelector().resourceId("com.android.vending:id/badge_container").makeUiObject();
        return BADGE_CONTAINER;
    }

    private static UiObject appTitle(){
        if(APP_TITLE == null) APP_TITLE = new UiSelector().resourceId("com.android.vending:id/title_title").makeUiObject();
        return APP_TITLE;
    }

    private static UiObject appCreator(){
        if(APP_CREATOR == null) APP_CREATOR = new UiSelector().resourceId("com.android.vending:id/title_creator").makeUiObject();
        return APP_CREATOR;
    }

    private static UiObject openButton(){
        if(OPEN_BUTTON == null) OPEN_BUTTON = new UiSelector().text("OPEN").makeUiObject();
        return OPEN_BUTTON;
    }

    public String getAppTitle(){
        return appTitle().text();
    }

    public String getAppCreator(){
        return appCreator().text();
    }

    public boolean isLoaded(){
        return appCreator().exists() && appTitle().exists();
    }

    public GooglePlayPermissions tapInstall(){
        QAManager.log.debug("Tapping on the Install Button");
        try{
            new UiSelector().text("INSTALL").tap();
        }catch (Exception e){
            throw new AssertionError("Install button is not available in Google Play");
        }
        return new GooglePlayPermissions();
    }

    public void tapOpen(){
        QAManager.log.debug("Tapping on the Open Button");
        openButton().tap();
        for(int ls=5; ls!=0; ls--){
            if(!getActivityInFocus().contains("com.android.vending/com.google.android.finsky.activities.MainActivity")) break;
            Utilities.Wait(1000);
        }
        Utilities.Wait(3000);
    }

    public GooglePlayConfirmationDialog tapUninstall(){
        QAManager.log.debug("Tapping on the Uninstall Button");
        uninstallButton().tap();
        return new GooglePlayConfirmationDialog();
    }

    public GooglePlayApplicationPage waitForInstallToFinish(){
        int maxWaitTime = 60;
        QAManager.log.debug("Waiting for the App to finish installing on the device, this may take some time. Max wait time is set to: "+maxWaitTime*2+" seconds");
        int ls = maxWaitTime;
        while(ls != 0 && !openButton().exists()){
            ls--;
            QAManager.log.debug("");
            if(ls == 0 && !openButton().exists()){
                ReportingManager.addPlayInstallIssue();
                throw new AssertionError("Application Failed to download and install in: "+maxWaitTime*2+" seconds");
            }
        }
        return this;
    }

    public GooglePlayApplicationPage tapSearchIcon(){
        searchIcon().tap();
        return this;
    }

    public GooglePlaySearchResults searchFor(String searchCriteria){
        searchField().clearText().typeText(searchCriteria);
        DeviceManager.getDriver().pressKeyCode(AndroidKeyCode.ENTER);
        return new GooglePlaySearchResults();
    }

    public GooglePlayApplicationPage waitForViewToLoad(){
        appCreator().waitForSelector(25);
        return this;
    }

    public AndroidDesktop closeGooglePlay(){
        pressHomeButton();
        return new AndroidDesktop();
    }
}
