package api.youtube;

import api.android.Android;
import api.android.AndroidDesktop;
import io.appium.java_client.android.AndroidKeyCode;
import managers.DeviceManager;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;
import utilities.constants.Activity;
import utilities.constants.Package;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/2/2015.
 **/
public class YouTube extends Android {

    private static boolean flagExpectTutorial = true;

    private static UiObject

            SEARCH_ICON                 = null,
            SEARCH_FIELD                = null,
            HOME_ICON                   = null,
            SUBSCRIPTION_ICON           = null,
            ACCOUNT_ICON                = null,
            AD                          = null,
            VIDEO_LOADING_PROGRESS_BAR  = null,
            SUGGESTED_VIDEO_LOADING_BAR = null,
            GOT_IT_BUTTON               = null;

    private static UiObject searchIcon(){
        if(SEARCH_ICON == null) SEARCH_ICON  =  new UiSelector().resourceId("com.google.android.youtube:id/menu_search").makeUiObject();
        return SEARCH_ICON ;
    }

    private static UiObject searchField(){
        if(SEARCH_FIELD == null) SEARCH_FIELD  =  new UiSelector().resourceId("com.google.android.youtube:id/search_edit_text").makeUiObject();
        return SEARCH_FIELD ;
    }

    private static UiObject homeIcon(){
        if(HOME_ICON == null) HOME_ICON  =  new UiSelector().description("Home").makeUiObject();
        return HOME_ICON ;
    }

    private static UiObject subscriptionIcon(){
        if(SUBSCRIPTION_ICON== null) SUBSCRIPTION_ICON =  new UiSelector().description("Subscriptions").makeUiObject();
        return SUBSCRIPTION_ICON;
    }

    private static UiObject accountIcon(){
        if(ACCOUNT_ICON == null) ACCOUNT_ICON  =  new UiSelector().description("Account").makeUiObject();
        return ACCOUNT_ICON ;
    }

    private static UiObject ad(){
        if(AD == null) AD  =  new UiSelector().resourceId("com.google.android.youtube:id/ad_text").makeUiObject();
        return AD ;
    }

    private static UiObject videoLoadingProgressBar(){
        if(VIDEO_LOADING_PROGRESS_BAR == null) VIDEO_LOADING_PROGRESS_BAR  =  new UiSelector().resourceId("com.google.android.youtube:id/player_loading_view").makeUiObject();
        return VIDEO_LOADING_PROGRESS_BAR ;
    }

    private static UiObject suggestedVideoLoadingBar(){
        if(SUGGESTED_VIDEO_LOADING_BAR == null) SUGGESTED_VIDEO_LOADING_BAR  =  new UiSelector().resourceId("com.google.android.youtube:id/load_progress").makeUiObject();
        return SUGGESTED_VIDEO_LOADING_BAR ;
    }

    private static UiObject gotItButton(){
        if(GOT_IT_BUTTON == null) GOT_IT_BUTTON  =  new UiSelector().resourceId("com.google.android.youtube:id/ok").makeUiObject();
        return GOT_IT_BUTTON ;
    }

    private static boolean isNotrificationRequestShown(){
        QAManager.log.debug("Checking if Notification Request Shown");
        return button2().exists();
    }

    private static boolean isTutorialShown(){
        QAManager.log.debug("Checking if Tutorial Shown");
        return gotItButton().exists();
    }

    public YouTube clickSearchIcon(){
        QAManager.log.debug("Clicking Search Icon");
        if(isTutorialShown()) gotItButton().tap();
        searchIcon().tap();
        return this;
    }

    public YouTube searchFor(String searchCriteria){
        QAManager.log.debug("Searching for: " + searchCriteria);
        searchField().clearText().typeText(searchCriteria);
        DeviceManager.getDriver().pressKeyCode(AndroidKeyCode.ENTER);
        return this;
    }

    public YouTube tapVideo(int videoIndex){
        QAManager.log.debug("Tapping Video #"+videoIndex);
        if(isTutorialShown()) gotItButton().tap();
        new UiSelector().uiSelector(new UiSelector().resourceId("com.google.android.youtube:id/compact_video_item").index(videoIndex).compile()).tap();
        return this;
    }

    public boolean isVideoLoaded(){
        QAManager.log.debug("Checking if Video is loaded");
        if(isTutorialShown()) gotItButton().tap();
        ad().waitForSelectorToVanish(25);
        videoLoadingProgressBar().waitForSelectorToVanish(25);
        suggestedVideoLoadingBar().waitForSelectorToVanish(25);
        return true;
    }

    public YouTube open(){
        QAManager.log.debug("Opening YouTube");
        forceStopApp(Package.YOUTUBE);
        openApp(Package.YOUTUBE, Activity.YOUTUBE);
        //waitForViewToLoad();
        if(isNotrificationRequestShown()) button2().tap();
        return waitForViewToLoad();
    }

    public YouTube waitForViewToLoad(){
        QAManager.log.debug("Waiting for YouTube to load");
        if(isTutorialShown()) gotItButton().tap();
        searchIcon().waitForSelector(5);
        return this;
    }

    public AndroidDesktop closeYouTube(){
        QAManager.log.debug("Exiting YouTube");
        pressHomeButton();
        return new AndroidDesktop();
    }
}
