package api.hotspotshield.v400;

import api.android.Android;
import managers.QAManager;
import managers.ReportingManager;
import org.junit.Assert;
import utilities.UiObject;
import utilities.UiSelector;
import utilities.constants.Credentials;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class ViewAccount extends Android{

    public ViewAccount(){
        connected = isVpnConnected();
    }

    private static boolean connected;
    private static final ViewHome viewHome = new ViewHome();
    private static final ErrorDialog error = new ErrorDialog();

    private static UiObject

            TITLE                   = null,
            FORGOT_PASSWORD_LINK    = null,
            USERNAME                = null,
            PASSWORD                = null,
            SIGN_OUT_BUTTON         = null,
            UPGRADE_BUTTON          = null,
            SIGN_IN_BUTTON          = null,
            PROGRESS_BAR            = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().resourceId("android:id/action_bar_title").makeUiObject();
        return TITLE;
    }

    private static UiObject forgotPasswordLink(){
        if(FORGOT_PASSWORD_LINK == null) FORGOT_PASSWORD_LINK = new UiSelector().resourceId(getAppPackageId() + ":id/view_login_forgot_btn").makeUiObject();
        return FORGOT_PASSWORD_LINK;
    }

    private static UiObject username(){
        if(USERNAME == null) USERNAME = new UiSelector().resourceId(getAppPackageId() + ":id/view_login_signin_username").makeUiObject();
        return USERNAME;
    }

    private static UiObject password(){
        if(PASSWORD == null) PASSWORD = new UiSelector().resourceId(getAppPackageId()+":id/view_login_signin_password").makeUiObject();
        return PASSWORD;
    }

    private static UiObject signOutButton(){
        if(SIGN_OUT_BUTTON == null) SIGN_OUT_BUTTON = new UiSelector().resourceId(getAppPackageId()+":id/view_account_signout_button").makeUiObject();
        return SIGN_OUT_BUTTON;
    }

    private static UiObject upgradeButton(){
        if(UPGRADE_BUTTON == null) UPGRADE_BUTTON = new UiSelector().resourceId(getAppPackageId()+":id/view_account_upgrade_button").makeUiObject();
        return UPGRADE_BUTTON;
    }

    private static UiObject signInButton(){
        if(SIGN_IN_BUTTON == null) SIGN_IN_BUTTON = new UiSelector().resourceId(getAppPackageId()+":id/view_login_signin_btn").makeUiObject();
        return SIGN_IN_BUTTON;
    }

    private static UiObject progressBar(){
        if(PROGRESS_BAR == null) PROGRESS_BAR = new UiSelector().resourceId("android.widget.ProgressBar").makeUiObject();
        return PROGRESS_BAR;
    }

    public ViewAccount tapSignInTab(){
        QAManager.log.debug("Tapping on the Sign In Tab");
        new UiSelector().text("SIGN IN").tap();
        return this;
    }

    public ViewForgotPassword tapForgotPassword(){
        QAManager.log.debug("Tapping on the Forgot Password link");
        forgotPasswordLink().tap();
        return new ViewForgotPassword().waitForViewToLoad();
    }

    public void tapSignInButton(){
        QAManager.log.debug("Tapping on the Sign In button");
        signInButton().tap();
    }

    public ViewSignUp tapSignUpTab(){
        QAManager.log.debug("Tapping on the Sign Up Tab");
        new UiSelector().text("SIGN UP").tap();
        return new ViewSignUp().waitForViewToLoad();
    }

    public ViewAccount enterUsername(String username){
        QAManager.log.debug("Entering Username");
        username().clearText().typeText(username);
        return this;
    }

    public ViewAccount enterPassword(String password){
        QAManager.log.debug("Entering Password");
        password().clearText().typeText(password);
        return this;
    }

    public void tapSignOutButton(){
        QAManager.log.debug("Tapping on the Sign Out button");
        signOutButton().tap();
    }

    public ViewUpSell tapUpgradeButton(){
        QAManager.log.debug("Tapping on the Upgrade button");
        upgradeButton().tap();
        return new ViewUpSell().waitForViewToLoad();
    }

    public ViewAccount waitForViewToLoad(){
        QAManager.log.debug("Waiting for Account view to load");
        title().waitForSelector(25);
        return this;
    }

    public ViewHome exitView(){
        QAManager.log.debug("Exiting Account Activity to Home View");
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }

    private boolean signInButtonExists(){
        return signInButton().exists();
    }

    public ViewHome signInAsElite(String username, String password){
        QAManager.log.debug("[TOP LEVEL] Sign In as Elite called");
        ViewHome viewHome = new ViewHome();
        boolean signedInAsElite = viewHome.openMenuDrawer().isElite();
        if(!signedInAsElite){
            new MenuDrawer().openAccount();
            if(signInButtonExists()){
                QAManager.log.debug("Signing in as Elite");
                enterUsername(username);
                enterPassword(password);
                tapSignInButton();
                accountAlgorithm();
                if(new ViewHome().waitForViewToLoad(25).upSellButtonPresent()){
                    throw new AssertionError("Signed In as Elite but Up-Sell Button is still present");
                }
                QAManager.log.debug("Sign with Elite account: "+username+" is successful!");
            }else{
                QAManager.log.debug("Signed in as Free, need to sign out before I can sign in as Elite");
                exitView();
                signOut();
                signInAsElite(username, password);
            }
        }else {
            QAManager.log.debug("Already Signed In as Elite");
            new MenuDrawer().closeDrawer();
        }
        QAManager.log.debug("[TOP LEVEL] Sign In as Elite done");
        return new ViewHome().waitForViewToLoad();
    }

    public ViewHome signInAsFree(String username, String password){
        QAManager.log.debug("[TOP LEVEL] Sign In as Free called");
        ViewHome viewHome = new ViewHome();
        boolean signedInAsElite = viewHome.openMenuDrawer().isElite();
        boolean signedOut = viewHome.openMenuDrawer().isSignedOut();
        if(signedInAsElite || signedOut){
            new MenuDrawer().openAccount();
            if(signInButtonExists()){
                QAManager.log.debug("Signing in as Free");
                enterUsername(username);
                enterPassword(password);
                tapSignInButton();
                accountAlgorithm();
                if(!new ViewHome().waitForViewToLoad(25).upSellButtonPresent()){
                    ReportingManager.addLogInIssue();
                    throw new AssertionError("Signed In as Free but Up-Sell Button is missing");
                }
                QAManager.log.debug("Sign with Free account: "+username+" is successful!");
            }else{
                QAManager.log.debug("Signed in as Elite, need to sign out before I can sign in as Free");
                exitView();
                signOut();
                signInAsFree(username, password);
            }
        }else {
            QAManager.log.debug("Already Signed In as Free");
            new MenuDrawer().closeDrawer();
        }
        QAManager.log.debug("[TOP LEVEL] Sign In as Free done");
        return new ViewHome().waitForViewToLoad();
    }

    public ErrorDialog signInToAccountWithExhaustedDeviceLimit(){
        signOut().openMenuDrawer().openAccount();
        enterUsername(Credentials.limitUsername());
        enterPassword(Credentials.limitPassword());
        tapSignInButton();
        QAManager.log.debug("Tried to signed in with Elite Account that has exhausted its 5/5 device limit");
        progressBar().waitForSelectorToVanish(25);
        Assert.assertTrue("Limit of devices error dialog did not show", new ErrorDialog().waitForDialogToLoad().isLimitOfDevices());
        return new ErrorDialog().waitForDialogToLoad();
    }

    public ErrorDialog signUpWithExistingUsername(){
        signOut().openMenuDrawer().openAccount().tapSignUpTab()
                .enterEmailAddress("a.spirin.anchorfree@gmail.com")
                .enterPassword("shield")
                .tapSignUpButton();
        return new ErrorDialog().waitForDialogToLoad();
    }

    public ViewHome signOut(){
        QAManager.log.debug("[TOP LEVEL] Sign Out called");
        MenuDrawer menuDrawer = new ViewHome().openMenuDrawer();
        boolean signedOut = menuDrawer.isSignedOut();
        if(!signedOut){
            menuDrawer.openAccount().tapSignOutButton();
            accountAlgorithm();
            if(!new ViewHome().waitForViewToLoad().upSellButtonPresent()){
                ReportingManager.addLogOutIssue();
                throw new AssertionError("Signed Out but the Up-Sell Button is missing");
            }
            QAManager.log.debug("Signed out!");
        }else{
            QAManager.log.debug("Already Signed Out");
            menuDrawer.closeDrawer();
        }
        QAManager.log.debug("[TOP LEVEL] Sign out done");
        return new ViewHome().waitForViewToLoad();
    }

    private boolean progressBarShown(){
        return progressBar().exists();
    }

    private void accountAlgorithm(){
        if(connected){
            int ls = 15;
            while(ls != 0 && !ViewHome.isShown()){
                QAManager.log.debug("Checking activity state: "+ls*2+" seconds left");
                if(ErrorDialog.isShown()){
                    QAManager.log.error("Error was thrown during sign in/out/up!");
                    throw new AssertionError("Error Thrown during sign in/out/up: " + new ErrorDialog().getErrorDescription());
                    /*if(error.isCantConnectToServers()){
                        throw new AssertionError("Error 1013 was thrown");
                    }*/
                }
                ls--;
                if(ls == 0 && !ViewHome.isShown()) throw new AssertionError("Account Activity did not update upon clicking Sign In/Out/Up");
            }
            viewHome.connectionAlgorithm();
        }else QAManager.log.debug("VPN is not connected. Client should not reconnect upon Sign Out/In!");
    }

    public void negativeSignIn(String username, String password){
        signOut().openMenuDrawer().openAccount()
                .enterUsername(username)
                .enterPassword(password)
                .tapSignInButton();
    }

    public void negativeSignUp(String username, String password){
        signOut().openMenuDrawer().openAccount()
                .enterUsername(username)
                .enterPassword(password)
                .tapSignInButton();
    }

    public ViewHome signUp(String username, String password){
        signOut().openMenuDrawer().openAccount().tapSignUpTab()
                .enterEmailAddress(username)
                .enterPassword(password)
                .tapSignUpButton();
        return new ViewHome().waitForViewToLoad(25);
    }
}