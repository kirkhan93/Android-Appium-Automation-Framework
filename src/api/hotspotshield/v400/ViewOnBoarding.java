package api.hotspotshield.v400;

import api.android.Android;
import api.android.AndroidDesktop;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class ViewOnBoarding extends Android{

    private static UiObject

            NEXT_BUTTON             = null,
            TERMS_OF_SERVICE_LINK   = null,
            LOGO                    = null;

    private static UiObject nextButton(){
        if(NEXT_BUTTON == null) NEXT_BUTTON = new UiSelector().resourceId(getAppPackageId()+":id/view_onboard_next_tv").makeUiObject();
        return NEXT_BUTTON;
    }

    private static UiObject termsOfServiceLink(){
        if(TERMS_OF_SERVICE_LINK == null) TERMS_OF_SERVICE_LINK = new UiSelector().resourceId(getAppPackageId()+":id/view_onboard_terms_tv").makeUiObject();
        return TERMS_OF_SERVICE_LINK;
    }

    private static UiObject logo(){
        if(LOGO == null) LOGO = new UiSelector().resourceId(getAppPackageId()+":id/view_onboard_logo").makeUiObject();
        return LOGO;
    }

    public ViewOptIn tapNextButton(){
        nextButton().tap();
        return new ViewOptIn().waitForViewToLoad();
    }

    public ViewTermsOfService openTermsOfService(){
        termsOfServiceLink().tap();
        return new ViewTermsOfService().waitForViewToLoad();
    }

    public ViewOnBoarding waitForViewToLoad(){
        logo().waitForSelector(25);
        return this;
    }

    public AndroidDesktop exitView(){
        pressHomeButton();
        return new AndroidDesktop();
    }
}
