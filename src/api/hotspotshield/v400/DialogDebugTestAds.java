package api.hotspotshield.v400;

import api.android.Android;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class DialogDebugTestAds extends Android{

    private static UiObject

            TITLE   = null,
            OPEN_X  = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().text("Choose provider").makeUiObject();
        return TITLE;
    }

    private static UiObject openXDebugOption(){
        if(OPEN_X == null) OPEN_X = new UiSelector().text("OX by URL").makeUiObject();
        return OPEN_X;
    }

    public DialogOpenXDebug tapOpenXDebugUtil(){
        QAManager.log.debug("Opening OpenX Debug Dialog");
        openXDebugOption().tap();
        return new DialogOpenXDebug().waitForDialogToLoad();
    }

    public AdView tapAdProvider(String adProviderName){

        return new AdView();
    }

    public DialogDebugTestAds waitForDialogToLoad(){
        title().waitForSelector(5);
        return this;
    }

    public ViewHome closeDialog(){
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
