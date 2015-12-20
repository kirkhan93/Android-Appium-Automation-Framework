package managers;

import api.android.Android;
import api.android.AndroidSystemUpdate;
import api.hotspotshield.v400.AdView;
import api.hotspotshield.v400.ViewHome;
import assets.Asset;
import utilities.Utilities;

import java.io.IOException;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 11/1/2015.
 */
public class MonitoringManager {

    public static Boolean MONITOR = true;
    public static Boolean ENVIRONMENT_MONITORING_STOPPED = false;
    public static Boolean UPDATE_MONITORING_STOPPED = false;
    private static String monitoringID = "";
    public static boolean timeUp = true;
    private static int timeLimit;

    public static void start(Thread thread){

        thread.start();
        QAManager.log.debug("Monitoring Started: " + monitoringID);
    }

    public static void startAllMonitoringServices(){

        start(updateMonitoring());
        //start(shutdownEventMonitoring());
    }

    public Thread timer(int timeLimit){

        timeUp = false;
        monitoringID = "Timer: "+timeLimit+" second limit";
        QAManager.log.debug("TimeUp: " + timeUp);
        MonitoringManager.timeLimit = timeLimit;
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while(MonitoringManager.timeLimit != 0){
                    Utilities.Wait(1000);
                    MonitoringManager.timeLimit--;
                }
                timeUp = true;
                QAManager.log.debug("TimeUp: " + timeUp);
            }
        });
    }


    public static Thread connectionDeadTimer(){

        monitoringID = "Connect Button Block";
        return new Thread(new Runnable() {
            @Override
            public void run() {
                ViewHome.DEAD_TIME = 4;
                while(ViewHome.DEAD_TIME != 0){
                    Utilities.Wait(1000);
                    ViewHome.DEAD_TIME--;
                }
            }
        });
    }

    public static Thread shutdownEventMonitoring(){

        monitoringID = "Shutdown Event Monitoring";
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while(MONITOR){
                    Utilities.Wait(10000);
                    try {
                        if(!DatabaseManager.REQUEST_TYPE.equals("ANDROID_SRV_SMOKE")) return;
                        if(new Asset("kill.txt").firstLineContains("1")) {
                            QAManager.log.debug("Detected request to shutdown the Test Cycle.");
                            System.exit(0);
                        }
                    } catch (IOException e) {e.printStackTrace();}
                }
            }
        });
    }

    public static Thread updateMonitoring(){

        monitoringID = "System Update Monitoring, Ad Monitoring, Lock Monitoring";
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while(MONITOR){
                    Utilities.Wait(2500);
                    if(AndroidSystemUpdate.isShown()){
                        QAManager.log.debug("System Update detected, closing it!");
                        new Android().pressBackButton();
                        /*
                        QAManager.log.debug("System Update detected. Resetting update time");
                        try{
                            new AndroidSystemUpdate().tapInstallLater().selectTime().tapDone();
                        }catch (Exception e){
                            QAManager.log.error("Failed to reset System Update: "+e.getMessage());
                        }
                        QAManager.log.debug("Reset System Update time by 24 hours");
                        */
                    }
                    if(DeviceManager.isDriverExist()) Android.unlockDevice();
                    if(!AdView.isExpected()){
                        if(AdView.isShown()){
                            try{
                                new Android().pressHomeButton();
                                Android.openHotspotShield();
                            }catch (Exception e){
                                QAManager.log.error("Failed to deal with the unxpected Ad: "+e.getMessage());
                            }
                        }
                    }
                }
                QAManager.log.debug("System Update Monitoring Stopped");
            }
        });
    }
}
