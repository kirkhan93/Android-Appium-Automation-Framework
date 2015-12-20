package api.hotspotshield.v400;

import api.android.Android;
import managers.QAManager;
import org.junit.Assert;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 12/11/2015.
 **/
public class ViewAppLock extends Android{

    private static UiObject

            TITLE           = null,
            PASSCODE        = null,
            PATTERN_LOCK    = null,
            ENTER_PASSCODE  = null,
            DOTS            = null,
            PATTERN_VIEW    = null,
            BACKSPACE       = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().text("Set up App Lock").makeUiObject();
        return TITLE;
    }

    private static UiObject passcode(){
        if(PASSCODE == null) PASSCODE = new UiSelector().text("PASSCODE").makeUiObject();
        return PASSCODE;
    }

    private static UiObject patternLock(){
        if(PATTERN_LOCK == null) PATTERN_LOCK = new UiSelector().text("PATTERN LOCK").makeUiObject();
        return PATTERN_LOCK;
    }

    private static UiObject enterPasscode(){
        if(ENTER_PASSCODE == null) ENTER_PASSCODE = new UiSelector().text("Upgrade to Elite").makeUiObject();
        return ENTER_PASSCODE;
    }

    private static UiObject dots(){
        if(DOTS == null) DOTS = new UiSelector().text("App Lock").makeUiObject();
        return DOTS;
    }

    private static UiObject patternView(){
        if(PATTERN_VIEW == null) PATTERN_VIEW = new UiSelector().resourceId(getAppPackageId() + ":id/pattern_view").makeUiObject();
        return PATTERN_VIEW;
    }

    private static UiObject backSpace(){
        if(BACKSPACE == null) BACKSPACE = new UiSelector().resourceId(getAppPackageId() + ":id/backspace").makeUiObject();
        return BACKSPACE;
    }

    private static UiObject digit(int singleDigit){
        return new UiSelector().resourceId(getAppPackageId()+":id/num_"+singleDigit).makeUiObject();
    }

    public ViewAppLock tapPasscodeTab(){
        QAManager.log.debug("Tapping on the Passcode Tab");
        passcode().tap();
        Assert.assertTrue("Passcode View did not open", dots().exists());
        return this;
    }

    public ViewAppLock tapPatternLockTab(){
        QAManager.log.debug("Tapping on the Pattern Lock Tab");
        patternLock().tap();
        Assert.assertTrue("Pattern View did not open", patternView().exists());
        return this;
    }

    public ViewAppLock tapBackspace(){
        QAManager.log.debug("Tapping on the Backspace");
        backSpace().tap();
        return this;
    }

    public ViewAppLock tapDigit(int value){
        QAManager.log.debug("Tapping on Digit: " + value);
        digit(value).tap();
        return this;
    }

    public ViewAppLock waitForViewToLoad(){
        QAManager.log.debug("Waiting for App Lock Activity to open");
        title().waitForSelector(5);
        return this;
    }

    public ViewHome exitView(){
        QAManager.log.debug("Closing App Lock View");
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
