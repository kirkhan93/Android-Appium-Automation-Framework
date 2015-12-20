package api.hotspotshield.v400;

import api.android.Android;
import managers.QAManager;
import managers.ReportingManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 12/11/2015.
 **/
public class InterstitialAppLock extends Android{

    private static UiObject

            LOGO            = null,
            SET_MY_PASSWORD = null,
            BULLET1         = null,
            BULLET2         = null,
            BULLET3         = null;

    private static UiObject logo(){
        if(LOGO == null) LOGO = new UiSelector().resourceId(getAppPackageId()+":id/view_onboard_logo").makeUiObject();
        return LOGO;
    }

    private static UiObject setMyPassword(){
        if(SET_MY_PASSWORD == null) SET_MY_PASSWORD = new UiSelector().resourceId(getAppPackageId() + ":id/confirm").makeUiObject();
        return SET_MY_PASSWORD;
    }

    private static UiObject bullet1(){
        if(BULLET1 == null) BULLET1 = new UiSelector().text("Lock Your Personal Apps").makeUiObject();
        return BULLET1;
    }

    private static UiObject bullet2(){
        if(BULLET2 == null) BULLET2 = new UiSelector().text("Hide Photos & Messages").makeUiObject();
        return BULLET2;
    }

    private static UiObject bullet3(){
        if(BULLET3 == null) BULLET3 = new UiSelector().text("Protect Your Privacy").makeUiObject();
        return BULLET3;
    }

    public ViewAppLock tapSetMyPassword(){
        QAManager.log.debug("Tapping Set My Password Button");
        setMyPassword().tap();
        return new ViewAppLock().waitForViewToLoad();
    }

    public InterstitialAppLock waitForViewToLoad(){
        QAManager.log.debug("Waiting for App Lock Interstitial to open");
        try{
            logo().waitForSelector(5);
        }catch (Exception e){
            ReportingManager.addPWInterstitialNotShown();
            throw new AssertionError("App Lock interstitial did not show");
        }
        return this;
    }

    public ViewHome exitView(){
        QAManager.log.debug("Closing App Lock Interstitial");
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
