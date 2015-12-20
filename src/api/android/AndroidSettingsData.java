package api.android;

import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 **/
public class AndroidSettingsData extends Android{

    private static UiObject

            TITLE           = null,
            ON              = null,
            OFF             = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().text("Data usage").makeUiObject();
        return TITLE;
    }

    private static UiObject on(){
        if(ON == null) ON = new UiSelector().text("ON").makeUiObject();
        return ON;
    }

    private static UiObject off(){
        if(OFF == null) OFF = new UiSelector().text("OFF").makeUiObject();
        return OFF;
    }

    public AndroidSettingsData enableData(){
        QAManager.log.debug("Toggling the switch to ON position");
        if(!isCellDataOn()) off().tap();
        return this;
    }

    public AndroidSettingsData disableData(){
        QAManager.log.debug("Toggling the switch to OFF position");
        if(isCellDataOn()){
            on().tap();
            button1().tap();
            if(button2().exists()) button2().tap();
        }
        return this;
    }

    public AndroidSettingsData waitForViewToLoad(){
        QAManager.log.debug("Waiting for the Data Settings Activity to load");
        title().waitForSelector(5);
        return this;
    }

    public AndroidDesktop closeSettings(){
        QAManager.log.debug("Exiting Data Settings Activity to AndroidDesktop");
        pressHomeButton();
        return new AndroidDesktop();
    }
}
