package utilities;

import managers.DeviceManager;
import managers.QAManager;
import org.openqa.selenium.NoSuchElementException;

import java.util.concurrent.TimeUnit;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 12/10/2015.
 **/
public class UiObject {

    private static final int defaultTimeOut = 5;
    private String uiSelector;

    public UiObject(String uiSelector){
        this.uiSelector = uiSelector;
    }

    public boolean exists(){
        try{
            String tag = "Exists: " +uiSelector;
            DeviceManager.setElementLocatorTimeOut(1, tag);
            DeviceManager.getDriver().findElementByAndroidUIAutomator(uiSelector);
            return true;
        }catch (NoSuchElementException e){
            return false;
        }
    }

    public boolean exists(String xPath){
        try{
            String tag = "Exists XPath: " +xPath;
            DeviceManager.setElementLocatorTimeOut(1, tag);
            DeviceManager.getDriver().findElementByXPath(xPath);
            return true;
        }catch (NoSuchElementException e){
            return false;
        }
    }

    public void doubleTap(){
        String tag = "doubleTap: " +uiSelector;
        DeviceManager.setElementLocatorTimeOut(defaultTimeOut, tag);
        DeviceManager.getDriver().findElementByAndroidUIAutomator(uiSelector).click();
        if(new UiObject(uiSelector).exists()) DeviceManager.getDriver().findElementByAndroidUIAutomator(uiSelector).click();
    }

    public void tap(){
        String tag = "tap: " +uiSelector;
        DeviceManager.setElementLocatorTimeOut(defaultTimeOut, tag);
        DeviceManager.getDriver().findElementByAndroidUIAutomator(uiSelector).click();
    }

    public UiObject waitForSelector(int seconds){
        while(seconds != 0 && !new UiObject(uiSelector).exists()){
            QAManager.log.debug("Waiting for uiSelector: "+uiSelector+" to appear for: "+seconds+" seconds");
            seconds--;
            if(seconds <= 0 && !new UiObject(uiSelector).exists()) throw new AssertionError("uiSelector: "+uiSelector+" not found!");
            Utilities.Wait(700);
        }
        return this;
    }

    public void waitForSelectorToVanish(int seconds){
        while(seconds != 0 && new UiObject(uiSelector).exists()){
            QAManager.log.debug("Waiting for uiSelector: " + uiSelector + " to vanish for: "+seconds+" seconds");
            seconds--;
            if(seconds <= 0) throw new AssertionError("Element: " + uiSelector + " did not vanish in " + seconds + " Seconds!");
            Utilities.Wait(700);
        }
    }

    public UiObject typeText(String value){
        if(!new UiObject(uiSelector).text().equals(value)) {
            DeviceManager.getDriver().findElementByAndroidUIAutomator(uiSelector).sendKeys(value);
            QAManager.log.debug("Typed: " + value);
        }else QAManager.log.debug("Edit Field already has the exact same text <"+value+"> typed inside of it");
        return this;
    }

    public UiObject clearText(){
        if(!Utilities.isEmpty(new UiObject(uiSelector).text())) {
            DeviceManager.getDriver().findElementByAndroidUIAutomator(uiSelector).clear();
            QAManager.log.debug("Cleared text field");
        }else QAManager.log.debug("Field is already clear");
        return this;
    }

    public boolean isEnabled(){
        return DeviceManager.getDriver().findElementByAndroidUIAutomator(uiSelector).isEnabled();
    }

    public boolean isSelected(){
        return DeviceManager.getDriver().findElementByAndroidUIAutomator(uiSelector).isSelected();
    }

    public boolean isChecked(String xPath){
        //Example: *[@text='Hotspot Shield']/following::*[1]/*[@class='android.widget.Switch'][@checked='false']
        try{
            DeviceManager.getDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            return DeviceManager.getDriver().findElementByXPath(xPath).getAttribute("checked").equals("true");
        }catch (NoSuchElementException e){
            QAManager.log.debug("Error in isChecked: "+e.getMessage());
        }
        return false;
    }

    public String text(){
        return DeviceManager.getDriver().findElementByAndroidUIAutomator(uiSelector).getText();
    }

    public UiObject scrollTo(){
        String selector = uiSelector.substring(uiSelector.indexOf(".text(\""), uiSelector.indexOf("\")")).replace(".text(\"","");
        QAManager.log.debug("Scrolling, looking for: " + selector);
        DeviceManager.getDriver().scrollTo(selector);
        return this;
    }

    public String getAttribute(String attribute){
        return DeviceManager.getDriver().findElementByAndroidUIAutomator(uiSelector).getAttribute(attribute);
    }
}
