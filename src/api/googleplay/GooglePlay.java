package api.googleplay;

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
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 **/
public class GooglePlay extends Android{

    private static UiObject

            PLAY_CARD            = null,
            SEARCH_FIELD_IDLE    = null,
            SEARCH_FIELD_INPUT   = null,
            PROGRESS_BAR         = null,
            ACCEPT_BUTTON        = null;

    private static UiObject playCard(){
        if(PLAY_CARD == null) PLAY_CARD = new UiSelector().resourceId("com.android.vending:id/play_card").makeUiObject();
        return PLAY_CARD;
    }

    private static UiObject searchFieldIdle(){
        if(SEARCH_FIELD_IDLE == null) SEARCH_FIELD_IDLE = new UiSelector().resourceId("com.android.vending:id/search_box_idle_text").makeUiObject();
        return SEARCH_FIELD_IDLE;
    }

    private static UiObject searchFieldInput(){
        if(SEARCH_FIELD_INPUT == null) SEARCH_FIELD_INPUT = new UiSelector().resourceId("com.android.vending:id/search_box_text_input").makeUiObject();
        return SEARCH_FIELD_INPUT;
    }

    private static UiObject progressBar(){
        if(PROGRESS_BAR == null) PROGRESS_BAR = new UiSelector().resourceId("com.android.vending:id/progress_bar").makeUiObject();
        return PROGRESS_BAR;
    }

    private static UiObject acceptButton(){
        if(ACCEPT_BUTTON == null) ACCEPT_BUTTON = new UiSelector().resourceId("com.android.vending:id/positive_button").makeUiObject();
        return ACCEPT_BUTTON;
    }

    public GooglePlay tapSearchField(){
        QAManager.log.debug("Tapping Search Field in Google Play");
        if(acceptButton().exists()) acceptButton().tap();
        searchFieldIdle().tap();
        return this;
    }

    public GooglePlaySearchResults searchFor(String searchCriteria){
        QAManager.log.debug("Searching for: "+searchCriteria+" in Google Play");
        if(acceptButton().exists()) acceptButton().tap();
        searchFieldInput().clearText().typeText(searchCriteria);
        DeviceManager.getDriver().pressKeyCode(AndroidKeyCode.ENTER);
        return new GooglePlaySearchResults();
    }

    public GooglePlay open(){
        QAManager.log.debug("Opening Google Play");
        forceStopApp(Package.GOOGLE_PLAY);
        openApp(Package.GOOGLE_PLAY, Activity.GOOGLE_PLAY);
        return waitForViewToLoad();
    }

    public GooglePlay waitForViewToLoad(){
        QAManager.log.debug("Waiting for Google Play to load");
        for(int ls=13; ls!=0; ls--){
            QAManager.log.debug("Waiting for Google Play to load: "+ls+" Seconds");
            if(acceptButton().exists()) acceptButton().tap();
            if(playCard().exists() && !progressBar().exists()) break;
        }
        return this;
    }

    public AndroidDesktop closeGooglePlay(){
        pressHomeButton();
        return new AndroidDesktop();
    }
}