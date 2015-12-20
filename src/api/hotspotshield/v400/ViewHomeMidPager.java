package api.hotspotshield.v400;

import io.appium.java_client.android.AndroidDriver;
import managers.DeviceManager;
import managers.QAManager;
import org.openqa.selenium.Dimension;
import utilities.UiObject;
import utilities.UiSelector;
import utilities.Utilities;

/**
 * Created by Artur Spirin on 11/13/15.
 **/
public class ViewHomeMidPager extends ViewHome {

    private AndroidDriver driver = DeviceManager.getDriver();

    private static UiObject RIGHT_ARROW = null;

    private static UiObject rightArrow(){
        if(RIGHT_ARROW == null) RIGHT_ARROW = new UiSelector().resourceId(getAppPackageId()+":id/banner_right_arrow").makeUiObject();
        return RIGHT_ARROW;
    }

    public ViewHome swipeToInfoState(){
        QAManager.log.debug("Resetting Mid Pager to the Original State");
        int ls = 3;
        while(isShowingAds() && ls!=0){
            ls--;
            swipeRight();
            Utilities.Wait(500);
        }
        return new ViewHome();
    }

    public static boolean isShowingAds(){
        QAManager.log.debug("Checking if Mid Pager showing ads");
        return rightArrow().exists();
    }

    public ViewHomeMidPager swipeLeft(){
        QAManager.log.debug("Swiping Status Bar Left");
        driver.context("NATIVE_APP");
        Dimension size = driver.manage().window().getSize();
        int startx = (int) (size.width * 0.8);
        int endx = (int) (size.width * 0.20);
        int starty = size.height / 2;
        driver.swipe(startx, starty, endx, starty, 1000);
        return this;
    }
    public ViewHomeMidPager swipeRight(){
        QAManager.log.debug("Swiping Status Bar Right");
        driver.context("NATIVE_APP");
        Dimension size = driver.manage().window().getSize();
        int endx = (int) (size.width * 0.8);
        int startx = (int) (size.width * 0.20);
        int starty = size.height / 2;
        driver.swipe(startx, starty, endx, starty, 1000);
        return this;
    }
}
