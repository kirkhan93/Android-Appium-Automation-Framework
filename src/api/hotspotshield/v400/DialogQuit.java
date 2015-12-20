package api.hotspotshield.v400;

import api.android.Android;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/4/2015.
 */
public class DialogQuit extends Android{

    private static UiObject

            TITLE           = null,
            OK_BUTTON       = null,
            CANCEL_BUTTON   = null;

    private static UiObject title(){
        if(TITLE == null) TITLE  =  new UiSelector().text("Quit").makeUiObject();
        return TITLE;
    }

    private static UiObject okButton(){
        if(OK_BUTTON == null) OK_BUTTON  =  new UiSelector().text("OK").makeUiObject();
        return OK_BUTTON;
    }

    private static UiObject cancelButton(){
        if(CANCEL_BUTTON == null) CANCEL_BUTTON  =  new UiSelector().text("Cancel").makeUiObject();
        return CANCEL_BUTTON;
    }

    public void tapOK(){
        okButton().tap();
    }

    public ViewHome tapCancel(){
        cancelButton().tap();
        return new ViewHome().waitForViewToLoad();
    }

    public DialogQuit waitForDialogToLoad(){
        title().waitForSelector(5);
        return this;
    }

    public ViewHome closeDialog(){
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
