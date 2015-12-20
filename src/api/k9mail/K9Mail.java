package api.k9mail;

import api.android.Android;
import api.android.AndroidDesktop;
import managers.QAManager;
import org.openqa.selenium.NoSuchElementException;
import utilities.UiObject;
import utilities.UiSelector;
import utilities.constants.Activity;
import utilities.constants.Package;

/**
 * Created by Artur Spirin on 11/12/15.
 **/
public class K9Mail extends Android{

    private static UiObject

            TITLE           = null,
            BACK_ARROW      = null,
            COMPOSE_BUTTON  = null,
            SEND_BUTTON     = null,
            TO              = null,
            SUBJECT         = null,
            MESSAGE         = null,
            P25             = null,
            P465            = null,
            P587            = null;

    private static UiObject title(){
        if(TITLE == null) TITLE  =  new UiSelector().resourceId("com.fsck.k9:id/actionbar_title_first").makeUiObject();
        return TITLE ;
    }

    private static UiObject backArrow(){
        if(BACK_ARROW == null) BACK_ARROW  =  new UiSelector().description("K-9 Mail, Navigate up").makeUiObject();
        return BACK_ARROW ;
    }

    private static UiObject composeButton(){
        if(COMPOSE_BUTTON == null) COMPOSE_BUTTON  =  new UiSelector().resourceId("com.fsck.k9:id/compose").makeUiObject();
        return COMPOSE_BUTTON ;
    }

    private static UiObject sendButton(){
        if(SEND_BUTTON== null) SEND_BUTTON =  new UiSelector().resourceId("com.fsck.k9:id/send").makeUiObject();
        return SEND_BUTTON;
    }

    private static UiObject to(){
        if(TO == null) TO  =  new UiSelector().resourceId("com.fsck.k9:id/to").makeUiObject();
        return TO ;
    }

    private static UiObject subject(){
        if(SUBJECT == null) SUBJECT  =  new UiSelector().resourceId("com.fsck.k9:id/subject").makeUiObject();
        return SUBJECT ;
    }

    private static UiObject message(){
        if(MESSAGE == null) MESSAGE  =  new UiSelector().resourceId("com.fsck.k9:id/message_content").makeUiObject();
        return MESSAGE ;
    }

    private static UiObject p25(){
        if(P25 == null) P25  =  new UiSelector().text("QA p25").makeUiObject();
        return P25 ;
    }

    private static UiObject p465(){
        if(P465 == null) P465  =  new UiSelector().text("QA p465").makeUiObject();
        return P465 ;
    }

    private static UiObject p587(){
        if(P587 == null) P587  =  new UiSelector().text("QA p587").makeUiObject();
        return P587 ;
    }

    public K9Mail enterTo(String to){
        QAManager.log.debug("Sending email to: " +to);
        try{
            to().clearText().typeText(to);
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't Send text to 'TO', Element absent");
        }
        pressBackButton();
        return this;
    }

    public K9Mail enterSubject(String subject){
        QAManager.log.debug("Email subject: " +subject);
        try{
            subject().clearText().typeText(subject);
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't Send text to 'Subject', Element absent");
        }
        pressBackButton();
        return this;
    }

    public K9Mail enterMessage(String message){
        QAManager.log.debug("Email Message: " +message);
        try{
            message().clearText().typeText(message);
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't Send text to 'Message', Element absent");
        }
        pressBackButton();
        return this;
    }

    public K9Mail tapSendButton(){
        QAManager.log.debug("Tapping Send Button");
        try{
            sendButton().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't tap Send Button, Element absent");
        }
        return this;
    }

    public K9Mail tapComposeButton(){
        QAManager.log.debug("Tapping Compose Button");
        try{
            composeButton().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't tap Compose Button, Element absent");
        }
        return this;
    }

    public K9Mail tapQaAccountPort25(){
        QAManager.log.debug("Tapping Account p25");
        try{
            p25().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't tap p25, Element absent");
        }
        return this;
    }

    public K9Mail tapQaAccountPort587(){
        QAManager.log.debug("Tapping Account p587");
        try{
            p587().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't tap p587, Element absent");
        }
        return this;
    }

    public K9Mail tapQaAccountPort465(){
        QAManager.log.debug("Tapping Account p465");
        try{
            p465().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't tap p465, Element absent");
        }
        return this;
    }

    public K9Mail open(){
        QAManager.log.debug("Opening K9Mail...");
        forceStopApp(Package.K9MAIL);
        openApp(Package.K9MAIL, Activity.K9MAIL);
        return waitForViewToLoad();
    }

    public K9Mail waitForViewToLoad(){
        QAManager.log.debug("Waiting for K9Mail Activity to load");
        try{
            title().waitForSelector(5);
        }catch (Exception e){
            QAManager.log.debug("Trying to reopen K9Mail");
            pressBackButton();
            open();
        }
        return this;
    }

    public AndroidDesktop closeK9Mail(){
        pressHomeButton();
        return new AndroidDesktop();
    }
}
