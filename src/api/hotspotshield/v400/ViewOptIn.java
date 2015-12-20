package api.hotspotshield.v400;

import api.android.Android;
import api.googleplay.GooglePlayVendingDialog;
import managers.QAManager;
import managers.ReportingManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin on 12/1/15.
 **/
public class ViewOptIn extends Android {

    private static UiObject LOGO = null;

    private static UiObject logo(){
        if(LOGO == null) LOGO = new UiSelector().resourceId(getAppPackageId()+":id/view_typed_upsell_optin_ll").makeUiObject();
        return LOGO;
    }

    private static UiObject buyButton(String value){
        return new UiSelector().text(value).makeUiObject();
    }

    public static boolean isShown(){
        return logo().exists();
    }

    public GooglePlayVendingDialog tapBuyButton(String buyButton){
        UiObject button = buyButton(buyButton);
        if(button.exists()){
            QAManager.log.debug("Tapping Buy Button: " + buyButton);
            button.tap();
        }else button.scrollTo().tap();
        return new GooglePlayVendingDialog().waitForDialogToLoad();
    }

    public ViewOptIn waitForViewToLoad(){
        QAManager.log.debug("Waiting for the Opt-In Activity to pop");
        int ls = 25;
        while(!isShown()){
            new ViewHome().grantVpnPermissions();
            ls--;
            if(ls == 0){
                ReportingManager.addOptInNotShown();
                throw new AssertionError("Opt-In activity was not displayed");
            }
        }
        return this;
    }

    public Tutorial exitView(){
        pressBackButton();
        return new Tutorial().waitForViewToLoad();
    }
}