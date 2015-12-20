package tests.v400;

import api.android.Android;
import api.hotspotshield.v400.ErrorDialog;
import api.hotspotshield.v400.ViewAccount;
import managers.QAManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import utilities.TestInfo;
import utilities.Utilities;
import utilities.constants.Credentials;

/**
 * Created by Artur Spirin on 11/13/15.
 **/
public class FunctionalityAccount extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){
        testInfo.suite("Account").execType("criticClient");
    }

    @Test
    public void AC4_111() {

        testInfo.name("Create New Account").expected("Account Created");
        String username = "anchorfree.artur"+ Utilities.generateRandomNumber(999999, 10000)+".test@anchorfree.com";
        String password = "Gtr#%123";
        Android.openHotspotShield();
        try{new ViewAccount().signUp(username, password);
        }catch(NoSuchElementException e){
            if(new ErrorDialog().isShown()){
                testInfo.actual(new ErrorDialog().getErrorDescription());
                throw new AssertionError("Got error dialog: " +TestInfo.actual());
            }else throw e;
        }
    }

    @Test
    public void AC4_582(){

        testInfo.name("Sign In with Incorrect Password").expected("Error Dialog Shown");
        Android.openHotspotShield();
        new ViewAccount().negativeSignIn(Credentials.eliteUsername(), Credentials.wrongPassword());
        ErrorDialog error = new ErrorDialog().waitForDialogToLoad();
        testInfo.actual(error.getErrorDescription());
        Assert.assertTrue("Wrong Error shown", error.isIncorrectUsernamePassword());
    }

    @Test
    public void AC4_583(){

        testInfo.name("Sign In with Incorrect Username").expected("Error Dialog Shown");
        Android.openHotspotShield();
        new ViewAccount().negativeSignIn(Credentials.wrongUsername(), Credentials.elitePassword());
        ErrorDialog error = new ErrorDialog().waitForDialogToLoad();
        testInfo.actual(error.getErrorDescription());
        Assert.assertTrue("Wrong Error shown", error.isIncorrectUsernamePassword());
    }

    @Test
    public void AC4_584(){

        testInfo.name("Sign In with Incorrect Username and Password").expected("Error Dialog Shown");
        Android.openHotspotShield();
        new ViewAccount().negativeSignIn(Credentials.wrongUsername(), Credentials.wrongPassword());
        ErrorDialog error = new ErrorDialog().waitForDialogToLoad();
        testInfo.actual(error.getErrorDescription());
        Assert.assertTrue("Wrong Error shown", error.isIncorrectUsernamePassword());
    }

    @Test
    public void AC4_586(){

        testInfo.name("Create New Account w/o Password").expected("Empty Field Underlined");
        Android.openHotspotShield();
        new ViewAccount().negativeSignIn(Credentials.eliteUsername(), "");
        Assert.assertFalse("Error was shown", new ErrorDialog().isShown());
    }

    @Test
    public void AC4_587(){

        testInfo.name("Create New Account w/o Username").expected("Empty Field Underlined");
        Android.openHotspotShield();
        new ViewAccount().negativeSignIn("", Credentials.elitePassword());
        Assert.assertFalse("Error was shown", new ErrorDialog().isShown());
    }
}
