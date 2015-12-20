package api.hotspotshield.v400;

import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin on 11/5/15.
 **/
public class ViewSignUp extends ViewAccount{

    private static UiObject

            TITLE           = null,
            PASSWORD        = null,
            EMAIL_ADDRESS   = null,
            SIGN_UP_BUTTON  = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().resourceId("android:id/action_bar_title").makeUiObject();
        return TITLE;
    }

    private static UiObject password(){
        if(PASSWORD == null) PASSWORD = new UiSelector().resourceId(getAppPackageId()+":id/view_login_signup_password").makeUiObject();
        return PASSWORD;
    }

    private static UiObject emailAddress(){
        if(EMAIL_ADDRESS == null) EMAIL_ADDRESS = new UiSelector().resourceId(getAppPackageId()+":id/view_login_signup_email").makeUiObject();
        return EMAIL_ADDRESS;
    }

    private static UiObject signUpButton(){
        if(SIGN_UP_BUTTON == null) SIGN_UP_BUTTON = new UiSelector().resourceId(getAppPackageId()+":id/view_login_signup_btn").makeUiObject();
        return SIGN_UP_BUTTON;
    }

    public ViewSignUp enterEmailAddress(String email){
        emailAddress().clearText().typeText(email);
        return this;
    }

    public ViewSignUp enterPassword(String password){
        password().clearText().typeText(password);
        return this;
    }

    public void tapSignUpButton(){
        signUpButton().tap();
    }

    public ViewSignUp waitForViewToLoad(){
        title().waitForSelector(5);
        return this;
    }

    public ViewHome exitView(){
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
