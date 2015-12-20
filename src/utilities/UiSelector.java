package utilities;

import managers.DeviceManager;
import managers.QAManager;
import org.openqa.selenium.NoSuchElementException;

import java.util.concurrent.TimeUnit;

/**
 * Created by Artur Spirin on 11/2/15.
 **/
public class UiSelector{

    public UiSelector(){
        locator = "new UiSelector()";
        text = null;
    }

    private static String
            locator = null,
            text = null;

    public UiSelector resourceId(String value){
         locator = locator + ".resourceId(\""+value+"\")";
        return this;
    }

    public UiSelector className(String value){
        locator = locator + ".className(\""+value+"\")";
        return this;
    }

    public UiSelector classNameMatches(String regex){
        locator = locator + ".classNameMatches(\""+regex+"\")";
        return this;
    }

    public UiSelector text(String value){
        text = value;
        locator = locator + ".text(\""+value+"\")";
        return this;
    }

    public UiSelector index(int value){
        locator = locator + ".index("+value+")";
        return this;
    }

    public UiSelector clickable(boolean value){
        locator = locator + ".clickable("+value+")";
        return this;
    }

    public UiSelector checked(String value){
        locator = locator + ".checked(\""+value+"\")";
        return this;
    }

    public UiSelector description(String value){
        locator = locator + ".description(\""+value+"\")";
        return this;
    }

    public UiSelector descriptionContains(String value){
        locator = locator + ".descriptionContains(\""+value+"\")";
        return this;
    }

    public UiSelector descriptionMatches(String regex){
        locator = locator + ".descriptionMatches(\""+regex+"\")";
        return this;
    }

    public UiSelector enabled(boolean value){
        locator = locator + ".enabled("+value+")";
        return this;
    }

    public UiSelector textContains(String value){
        locator = locator + ".textContains(\""+value+"\")";
        return this;
    }

    public UiSelector textMatches(String regex){
        locator = locator + ".textMatches(\""+regex+"\")";
        return this;
    }

    public UiSelector uiSelector(String UiSelector){
        locator = UiSelector;
        return this;
    }

    public String compile(){
        return locator;
    }

    public UiObject makeUiObject(){
        return new UiObject(locator);
    }

    public boolean exists(){
        try{
            DeviceManager.getDriver().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            return DeviceManager.getDriver().findElementByAndroidUIAutomator(locator).isDisplayed();
        }catch (NoSuchElementException e){
            //e.printStackTrace();
            /**Ignore**/
        }
        DeviceManager.getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        return false;
    }

    public boolean exists(String xPath){
        try{
            DeviceManager.getDriver().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            DeviceManager.getDriver().findElementByXPath(xPath);
            return true;
        }catch (NoSuchElementException e){
            //e.printStackTrace();
            return false;/**Ignore**/
        }finally {
            DeviceManager.getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        }
    }

    public void doubleTap(){
        DeviceManager.getDriver().findElementByAndroidUIAutomator(locator).click();
        if(uiSelector(locator).exists()) DeviceManager.getDriver().findElementByAndroidUIAutomator(locator).click();
    }

    public void tap(){
        DeviceManager.getDriver().findElementByAndroidUIAutomator(locator).click();
    }

    public UiSelector waitForSelector(int seconds){
        while(seconds != 0){
            try{
                QAManager.log.debug("Looking for locator: " + locator);
                DeviceManager.getDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
                DeviceManager.getDriver().findElementByAndroidUIAutomator(locator);
                break;
            }catch (NoSuchElementException e){
                /**ignored, exception will be thrown in the bellow if statement*/
            }finally {
                DeviceManager.getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            }
            seconds--;
            if(seconds <= 0){
                throw new AssertionError("Element: " + locator + " was not found!");
            }
            Utilities.Wait(1000);
        }
        return this;
    }

    public void waitForSelectorToVanish(int seconds){
        boolean vanished = false;
        while(!vanished && seconds != 0){
            try{
                QAManager.log.debug("Waiting for "+locator+" to vanish for "+seconds+" Seconds");
                DeviceManager.getDriver().manage().timeouts().implicitlyWait(1500, TimeUnit.MILLISECONDS);
                DeviceManager.getDriver().findElementByAndroidUIAutomator(locator);
                vanished = false;
            }catch (NoSuchElementException e){
                vanished = true;
            }finally {
                DeviceManager.getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            }
            seconds--;
            if(seconds <= 0){
                throw new AssertionError("Element: " + locator + " did not vanish in "+seconds+" Seconds!");
            }
        }
    }

    public UiSelector typeText(String text){
        DeviceManager.getDriver().findElementByAndroidUIAutomator(locator).sendKeys(text);
        //executeCommand("adb shell input text " + text);
        QAManager.log.debug("Typed: " + text);
        return this;
    }

    public UiSelector clearText(){
        if(!Utilities.isEmpty(DeviceManager.getDriver().findElementByAndroidUIAutomator(locator).getText())) {
            DeviceManager.getDriver().findElementByAndroidUIAutomator(locator).clear();
            QAManager.log.debug("Cleared text field");
        }else QAManager.log.debug("Field is already clear");
        return this;
    }

    public boolean isEnabled(){
        return DeviceManager.getDriver().findElementByAndroidUIAutomator(locator).isEnabled();
    }

    public boolean isSelected(){
        return DeviceManager.getDriver().findElementByAndroidUIAutomator(locator).isSelected();
    }

    public boolean isChecked(String xPath){
        /**
            Example of Xpath to check whether a switch is enabled
            //*[@text='Hotspot Shield']/following::*[1]/*[@class='android.widget.Switch'][@checked='false']
         */
        //QAManager.log.debug("Checking if element is Checked...");
        try{
            DeviceManager.getDriver().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            //QAManager.log.debug("Checked: " + DeviceManager.getDriver().findElementByXPath(xPath).getAttribute("checked"));
            return DeviceManager.getDriver().findElementByXPath(xPath).getAttribute("checked").equals("true");
        }catch (NoSuchElementException e){
            QAManager.log.error("Error in isChecked");
            e.printStackTrace();
            /**Ignore**/
        }
        DeviceManager.getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        return false;
    }

    public String text(){
        return DeviceManager.getDriver().findElementByAndroidUIAutomator(locator).getText();
    }

    public UiSelector scrollTo(){
        QAManager.log.debug("Scrolling, looking for: " + text);
        DeviceManager.getDriver().scrollTo(text);
        return this;
    }
}
