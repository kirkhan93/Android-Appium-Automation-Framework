package api.hotspotshield.v400;

import api.android.Android;
import api.googlechrome.hss.elite.PrePurchasePageMobileOverlay;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin on 11/13/15.
 **/
public class WebViewBandwidthWall2 extends Android{

    private static UiObject

            LOGO                    = null,
            OTHER_OPTIONS_BUTTON    = null,
            RESET_COUNT             = null,
            GOOGLE_PLAY             = null;

    private static UiObject logo(){
        if(LOGO == null) LOGO = new UiSelector().description("bandwidth").makeUiObject();
        return LOGO;
    }

    private static UiObject otherOptionsButton(){
        if(OTHER_OPTIONS_BUTTON == null) OTHER_OPTIONS_BUTTON = new UiSelector().description("Other Options").makeUiObject();
        return OTHER_OPTIONS_BUTTON;
    }

    private static UiObject resetCount(){
        if(RESET_COUNT == null) RESET_COUNT = new UiSelector().resourceId("bandwidth-reset").makeUiObject();
        return RESET_COUNT;
    }

    private static UiObject googlePlay(){
        if(GOOGLE_PLAY == null) GOOGLE_PLAY = new UiSelector().description("Google Play").makeUiObject();
        return GOOGLE_PLAY;
    }

    public boolean isResetCounterPresent(){
        return resetCount().exists();
    }

    public PrePurchasePageMobileOverlay tapOtherOptions(){
        QAManager.log.debug("Tapping on the Other Options button");
        otherOptionsButton().tap();
        return new PrePurchasePageMobileOverlay().waitForViewToLoad();
    }

    public ViewUpSell tapGooglePlay(){
        QAManager.log.debug("Tapping on the Google Play button");
        googlePlay().tap();
        return new ViewUpSell().waitForViewToLoad();
    }

    public WebViewBandwidthWall2 waitForViewToLoad(){
        QAManager.log.debug("Waiting for Bandwidth Wall load...");
        logo().waitForSelector(25);
        return this;
    }

    public ViewHome exitView(){
        QAManager.log.debug("Exiting Bandwidth WebView to ViewHome");
        pressBackButton();
        return new ViewHome();
    }
}
