package api.hotspotshield.v400;

import api.android.Android;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin on 11/2/15.
 **/
public class ViewForgotPassword extends Android{

    private static UiObject

            TITLE       = null,
            SEND_BUTTON = null,
            EMAIL_FIELD = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().resourceId("android:id/action_bar_title").makeUiObject();
        return TITLE;
    }

    private static UiObject sendButton(){
        if(SEND_BUTTON == null) SEND_BUTTON = new UiSelector().resourceId(getAppPackageId() + ":id/view_forgot_forgot_btn").makeUiObject();
        return SEND_BUTTON;
    }

    private static UiObject emailField(){
        if(EMAIL_FIELD == null) EMAIL_FIELD = new UiSelector().resourceId(getAppPackageId() + ":id/view_login_forgot_email").makeUiObject();
        return EMAIL_FIELD;
    }

    public ViewForgotPassword enterEmailAddress(String emailAddress){
        emailField().clearText().typeText(emailAddress);
        return this;
    }

    public void tapSendButton(){
        sendButton().tap();
    }

    public ViewForgotPassword waitForViewToLoad(){
        title().waitForSelector(5);
        return this;
    }

    public ViewAccount exitView(){
        pressBackButton();
        return new ViewAccount().waitForViewToLoad();
    }
}
