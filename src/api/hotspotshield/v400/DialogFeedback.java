package api.hotspotshield.v400;

import api.android.Android;
import api.googlechrome.GoogleChrome;
import api.googleplay.GooglePlayApplicationPage;
import managers.QAManager;
import org.junit.Assert;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class DialogFeedback extends Android{

    private static UiObject

            MAIN_TITLE              = null,
            NEGATIVE_TITLE          = null,
            POSITIVE_TITLE          = null,
            YES_THANKS_BUTTON       = null,
            NO_NOT_REALLY_BUTTON    = null,
            MAYBE_LATER_BUTTON      = null,
            RATE_NOW_BUTTON         = null,
            VISIT_HELP_BUTTON       = null,
            CONTACT_SUPPORT_BUTTON  = null;

    private static UiObject mainTitle(){
        if(MAIN_TITLE == null) MAIN_TITLE  =  new UiSelector().text("Do you love Hotspot Shield?").makeUiObject();
        return MAIN_TITLE ;
    }

    private static UiObject negativeTitle(){
        if(NEGATIVE_TITLE == null) NEGATIVE_TITLE  =  new UiSelector().text("Do you love Hotspot Shield?").makeUiObject();
        return NEGATIVE_TITLE ;
    }

    private static UiObject positiveTitle(){
        if(POSITIVE_TITLE == null) POSITIVE_TITLE  =  new UiSelector().text("Rate Hotspot Shield").makeUiObject();
        return POSITIVE_TITLE ;
    }

    private static UiObject yesThanksButton(){
        if(YES_THANKS_BUTTON== null) YES_THANKS_BUTTON =  new UiSelector().resourceId(getAppPackageId()+":id/view_dialog_love_pos_btn").makeUiObject();
        return YES_THANKS_BUTTON;
    }

    private static UiObject noNotReallyButton(){
        if(NO_NOT_REALLY_BUTTON == null) NO_NOT_REALLY_BUTTON  =  new UiSelector().resourceId(getAppPackageId()+":id/view_dialog_love_neg_btn").makeUiObject();
        return NO_NOT_REALLY_BUTTON ;
    }

    private static UiObject maybeLaterButton(){
        if(MAYBE_LATER_BUTTON == null) MAYBE_LATER_BUTTON  =  new UiSelector().resourceId(getAppPackageId()+":id/view_dialog_rate_neg_btn").makeUiObject();
        return MAYBE_LATER_BUTTON ;
    }

    private static UiObject rateNowButton(){
        if(RATE_NOW_BUTTON == null) RATE_NOW_BUTTON  =  new UiSelector().resourceId(getAppPackageId()+":id/view_dialog_rate_pos_btn").makeUiObject();
        return RATE_NOW_BUTTON ;
    }

    private static UiObject visitHelpButton(){
        if(VISIT_HELP_BUTTON == null) VISIT_HELP_BUTTON  =  new UiSelector().resourceId(getAppPackageId()+":id/view_dialog_support_neg_btn").makeUiObject();
        return VISIT_HELP_BUTTON ;
    }

    private static UiObject contactSupportButton(){
        if(CONTACT_SUPPORT_BUTTON == null) CONTACT_SUPPORT_BUTTON  =  new UiSelector().resourceId(getAppPackageId()+":id/view_dialog_support_pos_btn").makeUiObject();
        return CONTACT_SUPPORT_BUTTON ;
    }

    private static boolean flagFeedbackDialogExpected = false;

    public static boolean isShown(){
        return mainTitle().exists();
    }

    public static void setFlagFeedbackDialogExpected(boolean value){
        flagFeedbackDialogExpected = value;
        QAManager.log.debug("Set Flag for expecting Feedback Dialog to: " + value);
    }

    public static void waitForFeedbackDialog(int seconds){
        if(flagFeedbackDialogExpected){
            QAManager.log.debug("Waiting for Feedback Dialog");
            while(seconds != 0 && !DialogFeedback.isShown()){
                seconds--;
            }
        }else{
            if(isShown()) new Android().pressBackButton();
        }
    }

    public DialogFeedback tapNotReally(){
        QAManager.log.debug("Tapping Help Button");
        noNotReallyButton().tap();
        return this;
    }

    public DialogFeedback tapYesThanks(){
        QAManager.log.debug("Tapping Help Button");
        yesThanksButton().tap();
        return this;
    }

    public ViewHelp tapHelp(){
        QAManager.log.debug("Tapping Help Button");
        visitHelpButton().tap();
        return new ViewHelp().waitForViewToLoad();
    }

    public GoogleChrome tapContactSupport(){
        QAManager.log.debug("Tapping Help Button");
        contactSupportButton().tap();
        return new GoogleChrome().waitForPageToLoad();
    }

    public GooglePlayApplicationPage tapRateNow(){
        QAManager.log.debug("Tapping Help Button");
        rateNowButton().tap();
        String appTitle = new GooglePlayApplicationPage().waitForViewToLoad().getAppTitle();
        Assert.assertTrue("Wrong Google Play page opened: "+appTitle, appTitle.contains("Hotspot Shield"));
        return new GooglePlayApplicationPage().waitForViewToLoad();
    }

    public ViewHome tapMaybeLater(){
        QAManager.log.debug("Tapping Help Button");
        maybeLaterButton().tap();
        return new ViewHome().waitForViewToLoad();
    }

    public DialogFeedback waitForDialogToLoad(){
        QAManager.log.debug("Tapping Help Button");
        mainTitle().waitForSelector(5);
        return this;
    }

    public ViewHome closeDialog(){
        QAManager.log.debug("Tapping Help Button");
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}