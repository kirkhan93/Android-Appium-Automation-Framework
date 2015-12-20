package api.hotspotshield.v400;

import api.android.Android;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class DialogDebugMenu extends Android{

    private static UiObject TITLE = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().text("DEBUG MENU").makeUiObject();
        return TITLE;
    }

    public void tapDebugOption(String debugOptionText){

    }

    public DialogDebugMenu waitForDialogToLoad(){
        title().waitForSelector(5);
        return this;
    }

    public ViewHome closeDialog(){
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
