package utilities;

import api.android.Android;
import managers.DatabaseManager;
import managers.DeviceManager;
import managers.QAManager;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import utilities.constants.DistributionList;

/**
 * Created by Kirtesh & Artur Spirin on 7/10/2015.
 */
public class Retry implements TestRule {
    private int retryCount;
    public static int discardedTests = 0;

    public Retry(int retryCount) {
        this.retryCount = retryCount;
    }

    public Statement apply(Statement base, Description description) {
        return statement(base, description);
    }
    private Statement statement(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable caughtThrowable = null;
                // implement retry logic here
                for (int i = 0; i < retryCount; i++) {
                    try {
                        base.evaluate();
                        return;
                    } catch (Throwable t) {
                        caughtThrowable = t;
                        //  System.out.println(": run " + (i+1) + " failed");
                        if(caughtThrowable.getMessage().contains("got: <false>, expected: is <true>")){/**Ignore this exception in this loop, it will be handled later*/}
                        else{
                            System.err.println(description.getDisplayName() + ": run " + (i + 1) + " failed");
                            caughtThrowable.printStackTrace();
                        }
                    }
                }
                String errorMessage = caughtThrowable.getMessage();
                System.err.println(description.getDisplayName() + ": giving up after " + retryCount + " failures");
                if(errorMessage.contains("got: <false>, expected: is <true>")){
                    QAManager.log.warn("Assumption violated, test will be discarded! (" + discardedTests + ")");
                    discardedTests++;
                    throw caughtThrowable;
                }else if(errorMessage.contains("Command duration or timeout") && !errorMessage.contains("An element could not be located")){
                    QAManager.log.error("Appium Driver appears to shutdown due to timeout");
                    /*
                    QAManager.log.error("Timeout received, assuming server shutdown due to timeout. Stopping test cycle and sending a notification.");
                    QAManager.sendEmail(DistributionList.DEBUG, "Server Stopped due to Timeout",
                            "Request Type: "+ DatabaseManager.REQUEST_TYPE+"\nRequest ID: "+DatabaseManager.REQUEST_ID+"\nAgent: " + Android.getDeviceModel());
                    System.exit(0);
                    */
                    DeviceManager.resetDriver();
                } else if (errorMessage.contains("Error communicating with the remote browser. It may have died")){
                    QAManager.log.error("Server appears to be dead. Stopping test cycle and sending a notification.");
                    QAManager.sendEmail(DistributionList.DEBUG, "Server Died",
                            "Server appears to be dead. Possible causes: Appium server was manually shut down, or Node.js ran out of memory and Appium server had to shut down automatically.\nRequest Type: "
                                    + DatabaseManager.REQUEST_TYPE+"\nRequest ID: "+DatabaseManager.REQUEST_ID+"\nAgent: " + Android.getDeviceModel());
                    System.exit(0);
                }
                throw caughtThrowable;
            }
        };
    }
}