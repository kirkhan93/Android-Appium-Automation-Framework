package api.ookla.speedtest;

import api.android.Android;
import api.android.AndroidDesktop;
import managers.QAManager;
import org.openqa.selenium.NoSuchElementException;
import utilities.UiObject;
import utilities.UiSelector;
import utilities.constants.Activity;
import utilities.constants.Package;

/**
 * Created by Artur Spirin on 12/7/15.
 **/
public class ViewBeginSpeedtest extends BaseSpeedtestView{

    private static UiObject

            BEGIN_TEST = null;

    private static UiObject beginTest(){
        if(BEGIN_TEST == null) BEGIN_TEST  =  new UiSelector().text("Begin Test").makeUiObject();
        return BEGIN_TEST ;
    }

    public ViewBeginSpeedtest open(){
        QAManager.log.debug("Opening OOKLA's Speedtest App");
        Android.clearData(Package.SPEEDTEST);
        Android.openApp(Package.SPEEDTEST, Activity.SPEEDTEST_HOME_VIEW);
        return waitForViewToLoad();
    }

    private void closeDialog(){
        if(button1().exists()){
            try{
                QAManager.log.debug("Closing Welcome Dialog in Speedtest App");
                button1().tap();
            }catch (NoSuchElementException e){
                throw new AssertionError("Can't Close Dialog, Element absent");
            }
        }
    }

    public ViewSpeedtestResults tapBeginTest(){
        QAManager.log.debug("Tapping Begin Test");
        try{
            beginTest().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't tap Begin Test, Element absent");
        }
        return new ViewSpeedtestResults().waitForSpeedtestResults();
    }

    public ViewBeginSpeedtest waitForViewToLoad(){
        QAManager.log.debug("Waiting for Speedtest Activity to fully load");
        int wait = 20;
        for(int ls=wait; ls!=0; ls--){
            closeDialog();
            if(beginTest().exists()) break;
            if(ls==0) throw new AssertionError("Speedtest Activity did not load Begin Test button in: "+wait+" seconds");
        }
        return this;
    }

    public AndroidDesktop close(){
        pressHomeButton();
        return new AndroidDesktop();
    }
}
