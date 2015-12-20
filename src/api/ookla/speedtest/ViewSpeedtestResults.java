package api.ookla.speedtest;

import api.android.AndroidDesktop;
import managers.MonitoringManager;
import managers.QAManager;
import managers.ReportingManager;
import org.openqa.selenium.NoSuchElementException;
import utilities.UiObject;
import utilities.UiSelector;

/**
 * Created by Artur Spirin on 12/7/15.
 **/
public class ViewSpeedtestResults extends BaseSpeedtestView {

    private static double acceptableLoss = 49.99;

    private static UiObject

            TEST_AGAIN  = null,
            PING        = null,
            DOWNLOAD    = null,
            UPLOAD      = null;

    private static UiObject ping(){
        if(PING == null) PING  =  new UiSelector().resourceId("org.zwanoo.android.speedtest:id/pingSpeed").makeUiObject();
        return PING ;
    }

    private static UiObject download(){
        if(DOWNLOAD == null) DOWNLOAD  =  new UiSelector().resourceId("org.zwanoo.android.speedtest:id/downloadSpeed").makeUiObject();
        return DOWNLOAD ;
    }

    private static UiObject upload(){
        if(UPLOAD == null) UPLOAD  =  new UiSelector().resourceId("org.zwanoo.android.speedtest:id/uploadSpeed").makeUiObject();
        return UPLOAD ;
    }

    private static UiObject testAgain(){
        if(TEST_AGAIN == null) TEST_AGAIN = new UiSelector().text("Test Again").makeUiObject();
        return TEST_AGAIN;
    }

    public String getRawPing(){
        return ping().text();
    }

    public String getRawDownload(){
        return download().text();
    }

    public String getRawUpload(){
        return upload().text();
    }

    public int getPing(){
        QAManager.log.debug("Getting Ping");
        String sValue = getRawPing().replaceAll("ms", "").trim();
        Double dValue = Double.parseDouble(sValue);
        return dValue.intValue();
    }

    public int getDownload(){
        QAManager.log.debug("Getting Download");
        String sValue = getRawDownload().replaceAll("Mbps", "").trim();
        Double dValue = Double.parseDouble(sValue);
        return dValue.intValue();
    }

    public int getUpload(){
        QAManager.log.debug("Getting Upload");
        String sValue = getRawUpload().replaceAll("Mbps", "").trim();
        Double dValue = Double.parseDouble(sValue);
        return dValue.intValue();
    }

    public ViewSpeedtestResults tapTestAgian(){
        QAManager.log.debug("Tapping Test Again");
        try {
            testAgain().tap();
        }catch (NoSuchElementException e){
            throw new AssertionError("Failed to tap Test Again");
        }
        return this;
    }

    public static void assertDownloadResults(int before, int after){
        Double percentReduction = ((double) before - (double) after) /  before * 100;
        int roundedPercent = percentReduction.intValue();
        if((before / 2) < after) QAManager.log.debug("Speed Test Download result is good! Speed was reduced only by: " + roundedPercent+"%");
        else {
            ReportingManager.addSpeedIssue();
            throw new AssertionError("Download Speed was reduced by: " + roundedPercent+"%");
        }
    }

    public ViewSpeedtestResults waitForSpeedtestResults(){
        QAManager.log.debug("Waiting for Speedtest results");
        int wait = 120;
        Thread timer = new MonitoringManager().timer(wait);
        MonitoringManager.start(timer);
        try {
            while (!MonitoringManager.timeUp) {
                if (testAgain().exists()) break;
                else{
                    if (MonitoringManager.timeUp && !testAgain().exists()) {
                        throw new AssertionError("Speedtest failed to finish in: " + wait + " seconds");
                    }
                }
            }
        }finally {
            if(!timer.isInterrupted()) timer.interrupt();
        }
        return this;
    }

    public ViewSpeedtestResults waitForSpeedtestResults(int seconds){
        QAManager.log.debug("Waiting for Speedtest results");
        try{
            testAgain().waitForSelector(seconds);
        }catch (Exception e){
            throw new AssertionError("Speedtest failed to finish in: " + seconds+" seconds");
        }
        return this;
    }

    public AndroidDesktop close(){
        pressHomeButton();
        return new AndroidDesktop();
    }
}