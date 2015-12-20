package api.hotspotshield.v400;

import api.android.Android;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class ViewPrivacyPolicy extends Android{

    private static UiObject TITLE = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().resourceId("android:id/action_bar_title").makeUiObject();
        return TITLE;
    }

    public ViewPrivacyPolicy waitForViewToLoad(){
        title().waitForSelector(5);
        return this;
    }

    public ViewAbout exitView(){
        pressBackButton();
        return new ViewAbout().waitForViewToLoad();
    }
}
