package api.hotspotshield.v400;

import api.android.Android;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin on 12/8/15.
 **/
public class DialogOpenXDebug extends Android{

    private static UiObject

            EDIT_FIELD  = null,
            OK_BUTTON   = null;

    private static UiObject editField(){
        if(EDIT_FIELD == null) EDIT_FIELD  =  new UiSelector().resourceId(getAppPackageId() + ":id/custom_dlg_edit").makeUiObject();
        return EDIT_FIELD;
    }

    private static UiObject okButton(){
        if(OK_BUTTON == null) OK_BUTTON  =  new UiSelector().resourceId(getAppPackageId()+":id/custom_dlg_positiveButton").makeUiObject();
        return OK_BUTTON;
    }

    public DialogOpenXDebug enterUrl(String url){
        QAManager.log.debug("Entering URL");
        editField().typeText(url);
        return this;
    }

    public void tapOK(){
        QAManager.log.debug("Tapping the OK Button");
        okButton().tap();
    }

    public DialogOpenXDebug waitForDialogToLoad(){
        QAManager.log.debug("Waiting for the OpenX Debug Dialog to load");
        new UiSelector().text("OX URL checking tool").waitForSelector(5);
        return this;
    }

    public ViewHome closeDialog(){
        QAManager.log.debug("Closing the OpenX Debug Dialog");
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
