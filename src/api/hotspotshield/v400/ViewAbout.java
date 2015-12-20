package api.hotspotshield.v400;

import api.android.Android;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 **/
public class ViewAbout extends Android{

    private static UiObject

            TITLE = null,
            TERMS_OF_SERVICE = null,
            PRIVACY_POLICY = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().resourceId("android:id/action_bar_title").makeUiObject();
        return TITLE;
    }

    private static UiObject termsOfService(){
        if(TERMS_OF_SERVICE == null) TERMS_OF_SERVICE = new UiSelector().text("Terms of Service").makeUiObject();
        return TERMS_OF_SERVICE;
    }

    private static UiObject privacyPolicy(){
        if(PRIVACY_POLICY == null) PRIVACY_POLICY = new UiSelector().text("Terms of Service").makeUiObject();
        return PRIVACY_POLICY;
    }

    public ViewTermsOfService openTermsOfService(){
        termsOfService().tap();
        return new ViewTermsOfService().waitForViewToLoad();
    }

    public ViewPrivacyPolicy openPrivacyPolicy(){
        privacyPolicy().tap();
        return new ViewPrivacyPolicy().waitForViewToLoad();
    }

    public ViewAbout waitForViewToLoad(){
        title().waitForSelector(5);
        return this;
    }

    public ViewHome exitView(){
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
