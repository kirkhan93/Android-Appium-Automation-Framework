package api.hotspotshield.v400;

import api.android.Android;
import api.googleplay.GooglePlayApplicationPage;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/16/2015.
 */
public class ViewUpdate extends Android{

    private static UiObject

            UPDATE_NOW_BUTTON   = null,
            CLOSE_BUTTON        = null;

    private static UiObject updateNowButton(){
        if(UPDATE_NOW_BUTTON == null) UPDATE_NOW_BUTTON = new UiSelector().resourceId(getAppPackageId()+":id/view_update_button").makeUiObject();
        return UPDATE_NOW_BUTTON;
    }

    private static UiObject closeButton(){
        if(CLOSE_BUTTON == null) CLOSE_BUTTON = new UiSelector().resourceId(getAppPackageId()+":id/view_update_close_btn").makeUiObject();
        return CLOSE_BUTTON;
    }

    public ViewHome tapClose(){
        QAManager.log.debug("Tapping the Close Button");
        closeButton().tap();
        return new ViewHome();
    }

    public GooglePlayApplicationPage tapUpdateNowButton(){
        QAManager.log.debug("Tapping the Update Now Button");
        updateNowButton().tap();
        return new GooglePlayApplicationPage();
    }

    public ViewUpdate waitForViewToLoad(){
        QAManager.log.debug("Waiting for the Update View to Load");
        updateNowButton().waitForSelector(5);
        return this;
    }

    public ViewHome closeDialog(){
        QAManager.log.debug("Exiting the Update View");
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
