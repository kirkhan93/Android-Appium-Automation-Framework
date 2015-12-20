package api.hotspotshield.v400;

import api.android.Android;
import api.gmail.GMailComposeActivity;
import managers.QAManager;
import managers.ReportingManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin on 11/5/15.
 **/
public class ErrorDialog extends Android{

    private static UiObject

            CONTACT_SUPPORT_BUTTON  = null,
            OK_BUTTON               = null,
            ERROR_TITLE             = null,
            ERROR_DESCRIPTION       = null,
            ERROR_CODE              = null;

    private static UiObject contactSupportButton(){
        if(CONTACT_SUPPORT_BUTTON == null) CONTACT_SUPPORT_BUTTON  =  new UiSelector().resourceId(getAppPackageId() + ":id/view_dialog_error_neg_btn").makeUiObject();
        return CONTACT_SUPPORT_BUTTON;
    }

    private static UiObject okButton(){
        if(OK_BUTTON == null) OK_BUTTON  =  new UiSelector().resourceId(getAppPackageId() + ":id/view_dialog_error_pos_btn").makeUiObject();
        return OK_BUTTON;
    }

    private static UiObject errorTitle(){
        if(ERROR_TITLE == null) ERROR_TITLE  =  new UiSelector().resourceId(getAppPackageId() + ":id/view_dialog_error_header").makeUiObject();
        return ERROR_TITLE;
    }

    private static UiObject errorDescription(){
        if(ERROR_DESCRIPTION == null) ERROR_DESCRIPTION  =  new UiSelector().resourceId(getAppPackageId()+":id/view_dialog_error_message").makeUiObject();
        return ERROR_DESCRIPTION;
    }

    private static UiObject errorCode(){
        if(ERROR_TITLE == null) ERROR_TITLE  =  new UiSelector().resourceId(getAppPackageId()+":id/view_dialog_error_code").makeUiObject();
        return ERROR_TITLE;
    }

    public static boolean isShown(){
        QAManager.log.debug("Checking if error dialog is shown");
        if(getActivityInFocus().contains("com.anchorfree.ui.ViewDialogActivity")) return errorTitle().exists();
        else return false;
    }

    public boolean isCantConnectToServers(){
        QAManager.log.debug("Checking if Error is Can't Connect to Server (1013)");
        String errorID = "Hotspot Shield can't connect to it's servers.";
        String description = getErrorDescription();
        if(description.contains(errorID)) ReportingManager.addCantConnectToServers();
        return getErrorDescription().contains(errorID);
    }

    public boolean isLimitOfDevices(){
        QAManager.log.debug("Checking if Error is Limit of Devices");
        return getErrorDescription().contains("You have used more than 5 devices");
    }

    public boolean isUsernameAlreadyInUse(){
        QAManager.log.debug("Checking if Error is Username is Already in Use");
        return getErrorDescription().contains("This username is already in use");
    }

    public boolean isIncorrectUsernamePassword(){
        QAManager.log.debug("Checking if Error is Incorrect Username or Password");
        return getErrorDescription().contains("The username and/or password is incorrect");
    }

    public boolean isIncorrectPassword(){
        QAManager.log.debug("Checking if Error is Incorrect Password");
        return getErrorDescription().contains("Password must be");
    }

    public String getErrorCode(){
        QAManager.log.debug("Getting Error Code");
        return errorCode().text();
    }

    public String getErrorTitle(){
        QAManager.log.debug("Getting Error Title");
        return errorTitle().text();
    }

    public String getErrorDescription(){
        QAManager.log.debug("Getting Error Description");
        return errorDescription().text();
    }

    public void tapOK(){
        QAManager.log.debug("Tapping OK");
        okButton().tap();
    }

    public GMailComposeActivity tapContactSupport(){
        QAManager.log.debug("Tapping contact Support");
        contactSupportButton().tap();
        return new GMailComposeActivity().waitForViewToLoad();
    }

    public ErrorDialog waitForDialogToLoad(){
        QAManager.log.debug("Waiting for error dialog");
        errorTitle().waitForSelector(25);
        return this;
    }

    public void closeDialog(){
        QAManager.log.debug("Closing Error Dialog");
        pressBackButton();
    }
}
