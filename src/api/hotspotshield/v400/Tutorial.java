package api.hotspotshield.v400;

import api.android.Android;
import managers.QAManager;
import managers.ReportingManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/7/2015.
 */
public class Tutorial extends Android{

    private static UiObject

            FRAME = null,
            GET_STARTED = null,
            GOT_IT = null,
            DONE = null;

    private static UiObject frame(){
        if(FRAME == null) FRAME = new UiSelector().resourceId(getAppPackageId() + ":id/view_tutorial_bg").makeUiObject();
        return FRAME;
    }

    private static UiObject getStartedButton(){
        if(GET_STARTED == null) GET_STARTED = new UiSelector().text("Get started").makeUiObject();
        return GET_STARTED;
    }

    private static UiObject gotItButton(){
        if(GOT_IT == null) GOT_IT = new UiSelector().text("Got it").makeUiObject();
        return GOT_IT;
    }

    private static UiObject doneButton(){
        if(DONE == null) DONE = new UiSelector().text("Done").makeUiObject();
        return DONE;
    }

    public boolean isShown(){
        return frame().exists();
    }

    public Tutorial tapGetStartedButon(){
        QAManager.log.debug("Tapping Get Started Button");
        getStartedButton().tap();
        return this;
    }

    public Tutorial tapGotItButton(){
        QAManager.log.debug("Tapping Got It Button");
        gotItButton().tap();
        return this;
    }

    public ViewHome tapDoneButton(){
        QAManager.log.debug("Tapping Done Button");
        doneButton().tap();
        return new ViewHome();
    }

    public Tutorial enableApp(String appName){
        new ViewAppList().enableApplication(appName);
        return this;
    }

    public Tutorial disableApp(String appName){
        new ViewAppList().disableApplication(appName);
        return this;
    }

    public Tutorial waitForViewToLoad(){
        QAManager.log.debug("Waiting for the Tutorial to pop");
        int ls = 10;
        while(!isShown()){
            new ViewHome().grantVpnPermissions();
            ls--;
            if(ls == 0){
                ReportingManager.addTutorialNotShown();
                throw new AssertionError("Tutorial was not displayed");
            }
        }
        return this;
    }

    public void exitView(){
        QAManager.log.debug("Exiting Tutorial view");
        pressBackButton();
    }
}