package api.hotspotshield.v400;

import api.android.Android;
import managers.DeviceManager;
import managers.QAManager;
import org.openqa.selenium.NoSuchElementException;
import utilities.UiObject;
import utilities.UiSelector;

import java.util.concurrent.TimeUnit;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 **/
public class DialogLocationSelector extends Android{

    private static UiObject

            TITLE       = null,
            CHECK_MARK  = null;

    public static String CURRENT_COLO;

    private static UiObject title(){
        if(TITLE == null) TITLE  =  new UiSelector().text("Select a virtual location").makeUiObject();
        return TITLE;
    }

    private static UiObject currentLocationCheckmark(){
        if(CHECK_MARK == null) CHECK_MARK  =  new UiSelector().resourceId(getAppPackageId() + ":id/country_checkbox").makeUiObject();
        return CHECK_MARK;
    }

    public ViewHome selectLocationAsEliteUser(String location){
        if(!getCurrentColo().equals(location)){
            try{
                DeviceManager.getDriver().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                new UiSelector().text(location).tap();
            }catch (NoSuchElementException e){
                new UiSelector().text(location).scrollTo().tap();
            }finally {
                DeviceManager.getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            }
            QAManager.log.debug("Tapped on the location: " + location);
            CURRENT_COLO = location;
            QAManager.log.debug("Selected: " + CURRENT_COLO);
            QAManager.log.debug("Client should start reconnecting...");
            new ViewHome().connectionAlgorithm();
        }else{
            QAManager.log.debug("Already connected to: " + CURRENT_COLO);
            pressBackButton();
        }
        return new ViewHome().waitForViewToLoad();
    }

    public ViewUpSell selectLocationAsFreeUser(String location){
        try{
            DeviceManager.getDriver().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            new UiSelector().text(location).tap();
        }catch (NoSuchElementException e){
            new UiSelector().text(location).scrollTo().tap();
        }finally {
            DeviceManager.getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        }
        QAManager.log.debug("Tapped on the location: " + location);
        QAManager.log.debug("Selected: " + location);
        return new ViewUpSell().waitForViewToLoad();
    }

    public DialogLocationSelector selectSameLocation(){
        String location = getCurrentColo();
        currentLocationCheckmark().tap();
        QAManager.log.debug("Selected the same virtual location that we were connected to previously: " + location);
        CURRENT_COLO = location;
        return this;
    }

    public DialogLocationSelector waitForDialogToLoad(){
        title().waitForSelector(5);
        getCurrentColo();
        return this;
    }

    public ViewHome closeDialog(){
        pressBackButton();
        return new ViewHome().waitForViewToLoad();
    }

    private String getCurrentColo(){
        CURRENT_COLO =
        DeviceManager.getDriver().findElementByXPath(
                "//*[@resource-id=\""+getAppPackageId()+":id/country_checkbox\"]/preceding-sibling::*[1]"
        ).getText();
        return CURRENT_COLO;
    }
}
