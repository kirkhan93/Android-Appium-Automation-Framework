package api.googleplay;

import api.android.Android;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin on 11/2/15.
 **/
public class GooglePlayVendingDialog extends Android {

    private static UiObject

            FRAME               = null,
            OK_CONTINUE_BUTTON  = null,
            TITLE               = null,
            CONTENT             = null,
            LOGO                = null;

    private static UiObject frame(){
        if(FRAME == null) FRAME = new UiSelector().resourceId("com.android.vending:id/content_frame").makeUiObject();
        return FRAME;
    }

    private static UiObject okContinueButton(){
        if(OK_CONTINUE_BUTTON == null) OK_CONTINUE_BUTTON = new UiSelector().resourceId("com.android.vending:id/continue_button").makeUiObject();
        return OK_CONTINUE_BUTTON;
    }

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().resourceId("com.android.vending:id/title").makeUiObject();
        return TITLE;
    }

    private static UiObject content(){
        if(CONTENT == null) CONTENT = new UiSelector().resourceId("com.android.vending:id/message").makeUiObject();
        return CONTENT;
    }

    private static UiObject uninstallButton(){
        if(LOGO == null) LOGO = new UiSelector().resourceId("com.android.vending:id/logo").makeUiObject();
        return LOGO;
    }

    public String getTitle(){
        return title().text();
    }

    public String getMessage(){
        return content().text();
    }

    public GooglePlayVendingDialog waitForDialogToLoad(){
        frame().waitForSelector(5);
        return this;
    }

    public void closeDialog(){
        pressBackButton();
    }
}
