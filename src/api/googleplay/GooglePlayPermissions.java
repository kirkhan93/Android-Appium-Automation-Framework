package api.googleplay;

import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/8/2015.
 */
public class GooglePlayPermissions {

    private static UiObject ACCEPT_BUTTON = null;

    private static UiObject acceptButton(){
        if(ACCEPT_BUTTON == null) ACCEPT_BUTTON = new UiSelector().resourceId("com.android.vending:id/continue_button").makeUiObject();
        return ACCEPT_BUTTON;
    }

    public GooglePlayApplicationPage acceptPermissions(){
        if(acceptButton().exists()) acceptButton().tap();
        else QAManager.log.debug("Permissions were not requested for the app that you are trying to install");
        return new GooglePlayApplicationPage();
    }
}
