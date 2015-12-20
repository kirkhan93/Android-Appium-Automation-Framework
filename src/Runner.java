import managers.*;
import org.apache.log4j.Level;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Artur Spirin on 11/1/2015.
 **/
public class Runner {

   public static void main(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {

       QAManager.log.setLevel(Level.DEBUG);
       SystemManager.addShutdownHook();
       QAManager.parseRequestType(args);
       MonitoringManager.startAllMonitoringServices();
       DeviceManager.getDriver();
       if(!QAManager.isServerSmokeRequest() && !QAManager.isFunctionalSmokeRequest()) DatabaseManager.getRequest();
       else {
           if(QAManager.isServerSmokeRequest()) DatabaseManager.setContinuesServerSmokeParams();
           else if (QAManager.isFunctionalSmokeRequest()) DatabaseManager.setContinuesFunctionalSmokeParams();
       }
       QAManager.initiateTestCycle();
       System.exit(0);
   }
}