package api.gmail;

import api.android.Android;
import api.android.AndroidDesktop;
import managers.QAManager;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class GMailComposeActivity extends Android{

    private static UiObject

            BODY                = null,
            SEND_BUTTON         = null,
            ATTACHMENT_BUTTON   = null;

    private static UiObject body(){
        if(BODY == null) BODY = new UiSelector().resourceId("android:id/action_bar_title").makeUiObject();
        return BODY;
    }

    private static UiObject sendButton(){
        if(SEND_BUTTON == null) SEND_BUTTON = new UiSelector().resourceId("com.google.android.gm:id/send").makeUiObject();
        return SEND_BUTTON;
    }

    private static UiObject attachmentButton(){
        if(ATTACHMENT_BUTTON == null) ATTACHMENT_BUTTON = new UiSelector().resourceId("com.google.android.gm:id/add_attachment").makeUiObject();
        return ATTACHMENT_BUTTON;
    }

    public String getBody(){
        QAManager.log.debug("Retrieving body of the email");
        return body().text();
    }

    public void tapSendButton(){
        QAManager.log.debug("Tapping send button");
        sendButton().tap();
    }

    public GMailComposeActivity waitForViewToLoad(){
        QAManager.log.debug("Waiting for the GMail Activity to load");
        new UiSelector().text("Compose").waitForSelector(5);
        return this;
    }

    public AndroidDesktop closeGMail(){
        QAManager.log.debug("Exiting from GMail to AndroidDesktop");
        pressHomeButton();
        return new AndroidDesktop();
    }
}
