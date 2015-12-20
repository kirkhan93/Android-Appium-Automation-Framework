package api.hotspotshield.v400;

import api.android.Android;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class ViewAppList extends Android{

    private static UiObject

            TITLE = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().text("App List").makeUiObject();
        return TITLE;
    }

    private static UiObject application(String application){
         return new UiSelector().text(application).makeUiObject();
    }

    public ViewAppList enableApplication(String applicationName){
        UiObject app = application(applicationName);
        if(!appEnabled(applicationName)) {
            if(app.exists())app.tap();
            else app.scrollTo().tap();
            QAManager.log.debug("Enabled: " + applicationName);
        }
        else QAManager.log.debug(applicationName + " Application is already enabled");
        return this;
    }

    public ViewAppList disableApplication(String applicationName){
        UiObject app = application(applicationName);
        if(appEnabled(applicationName)) {
            if(app.exists())app.tap();
            else app.scrollTo().tap();
            QAManager.log.debug("Disabled: " + applicationName);
        }
        else QAManager.log.debug(applicationName + " Application is already disabled");
        return this;
    }

    public ViewAppList waitForViewToLoad(){
        title().waitForSelector(5);
        return this;
    }

    public ViewHome exitView(){
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }

    private boolean appEnabled(String applicationName){
        return new UiSelector().exists("//*[@text='"+applicationName+"']/parent::*[1]/*[@class='android.widget.FrameLayout']/*[@resource-id='hotspotshield.android.vpn.debug:id/image_check']");
    }
}
