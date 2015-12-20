package api.hotspotshield.v400;

import api.android.Android;
import api.googleplay.GooglePlayApplicationPage;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/16/2015.
 */
public class DialogPrivacyWizard extends Android{

    private static UiObject

            INSTALL_BUTTON  = null,
            BODY            = null;

    private static UiObject installButton(){
        if(INSTALL_BUTTON == null) INSTALL_BUTTON  =  new UiSelector().resourceId(getAppPackageId()+":id/view_dialog_privacy_wizard_pos_btn").makeUiObject();
        return INSTALL_BUTTON;
    }

    private static UiObject body(){
        if(BODY == null) BODY  =  new UiSelector().resourceId(getAppPackageId()+":id/view_dialog_privacy_wizard_text").makeUiObject();
        return BODY;
    }

    public GooglePlayApplicationPage tapInstall(){
        QAManager.log.debug("Tapping Install Button");
        installButton().tap();
        return new GooglePlayApplicationPage();
    }

    public DialogPrivacyWizard waitForDialogToLoad(){
        QAManager.log.debug("Waiting for the Privacy Wizard Dialog to Load");
        body().waitForSelector(5);
        return this;
    }

    public ViewHome closeDialog(){
        QAManager.log.debug("Exiting the Update View");
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
