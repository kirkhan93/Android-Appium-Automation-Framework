package api.hotspotshield.v400;

import api.android.Android;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class ViewNetworks extends Android {

    private static UiObject TITLE = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().resourceId("android:id/action_bar_title").makeUiObject();
        return TITLE;
    }

    public ViewNetworks enableNetwork(String networkName){

        if(!new UiSelector().isChecked("//*[@text=\"" + networkName + "\"]/following-sibling::*[1]")){
            new UiSelector().text(networkName).tap();
            QAManager.log.debug("Enabled network settings for: " + networkName);
        }else QAManager.log.debug("Network settings already enabled for: " + networkName);
        return this;
    }

    public ViewNetworks disableNetwork(String networkName){

        if(new UiSelector().isChecked("//*[@text=\"" + networkName + "\"]/following-sibling::*[1]")){
            new UiSelector().text(networkName).tap();
            QAManager.log.debug("Disabled network settings for: " + networkName);
        }else QAManager.log.debug("Network settings already disabled for: " + networkName);
        return this;
    }

    public ViewNetworks waitForViewToLoad(){
        title().waitForSelector(5);
        return this;
    }

    public ViewHome exitView(){
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
