package api.hotspotshield.v400;

import api.android.Android;
import api.gmail.GMailComposeActivity;
import api.googlechrome.GoogleChrome;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class ViewHelp extends Android{

    private static UiObject

            TITLE           = null,
            PROGRESS_BAR    = null;

    private static UiObject title(){
        if(TITLE == null) TITLE = new UiSelector().resourceId("android:id/action_bar_title").makeUiObject();
        return TITLE;
    }

    private static UiObject progressBar(){
        if(PROGRESS_BAR == null) PROGRESS_BAR = new UiSelector().resourceId("android.widget.ProgressBar").makeUiObject();
        return PROGRESS_BAR;
    }

    public GMailComposeActivity tapContactSupport(){
        //TODO Finish API
        return new GMailComposeActivity().waitForViewToLoad();
    }

    public GoogleChrome tapVisitHelpCenter(){
        //TODO Finish API
        return new GoogleChrome().waitForPageToLoad();
    }

    public ViewHelp waitForViewToLoad(){
        title().waitForSelector(15);
        return this;
    }

    public ViewHome exitView(){
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }
}
