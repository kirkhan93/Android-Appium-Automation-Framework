package tests.v400;

import api.android.Android;
import api.gmail.GMailReciever;
import api.k9mail.K9Mail;
import managers.QAManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;
import utilities.Utilities;
import utilities.constants.Credentials;

import javax.mail.MessagingException;

/**
 * Created by Artur Spirin on 11/12/15.
 **/
public class FunctionalityEmail extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){
        testInfo.suite("Email").execType("criticClient");
    }

    @Test
    public void AC4_699() throws MessagingException {

        testInfo.name("Connected - Send Email via Port 25").expected("Email Delivered and IP Hidden").jira("DROID-1370");
        Android.openHotspotShield().tapConnectButton();
        String subject = "Test-" + Utilities.generateRandomNumber(999999, 10000);
        new K9Mail().open().tapQaAccountPort25().tapComposeButton().enterTo(Credentials.gmailUsername).enterSubject(subject).tapSendButton();
        GMailReciever.waitForEmail(subject, 25);
        testInfo.actual(GMailReciever.getEmailHeaderIp(subject));
        Assert.assertTrue("IP Was not hidden: " + TestInfo.actual(), Utilities.isVpnIp(TestInfo.actual()));
    }

    @Test
    public void AC4_700() throws MessagingException {

        testInfo.name("Connected - Send Email via Port 587").expected("Email Delivered and IP Hidden").jira("DROID-1370");
        Android.openHotspotShield().tapConnectButton();
        String subject = "Test-" + Utilities.generateRandomNumber(999999, 10000);
        new K9Mail().open().tapQaAccountPort587().tapComposeButton().enterTo(Credentials.gmailUsername).enterSubject(subject).tapSendButton();
        GMailReciever.waitForEmail(subject, 25);
        testInfo.actual(GMailReciever.getEmailHeaderIp(subject));
        Assert.assertTrue("IP Was not hidden: " + TestInfo.actual(), Utilities.isVpnIp(TestInfo.actual()));
    }

    @Test
    public void AC4_701() throws MessagingException {

        testInfo.name("Connected - Send Email via Port 465").expected("Email Delivered and IP Hidden").jira("DROID-1370");
        Android.openHotspotShield().tapConnectButton();
        String subject = "Test-" + Utilities.generateRandomNumber(999999, 10000);
        new K9Mail().open().tapQaAccountPort465().tapComposeButton().enterTo(Credentials.gmailUsername).enterSubject(subject).tapSendButton();
        GMailReciever.waitForEmail(subject, 25);
        testInfo.actual(GMailReciever.getEmailHeaderIp(subject));
        Assert.assertTrue("IP Was not hidden: " + TestInfo.actual(), Utilities.isVpnIp(TestInfo.actual()));
    }

    @Test
    public void AC4_702() throws MessagingException {
        runOnlyForThoseTypes("ANDROID_FUN");
        testInfo.name("Disconnected - Send Email via Port 25").expected("Email Delivered and IP not Hidden").jira("DROID-1370");
        Android.openHotspotShield().tapDisconnectButton();
        String subject = "Test-" + Utilities.generateRandomNumber(999999, 10000);
        new K9Mail().open().tapQaAccountPort25().tapComposeButton().enterTo(Credentials.gmailUsername).enterSubject(subject).tapSendButton();
        GMailReciever.waitForEmail(subject, 25);
        testInfo.actual(GMailReciever.getEmailHeaderIp(subject));
        Assert.assertFalse("IP still hidden: " + TestInfo.actual(), Utilities.isVpnIp(TestInfo.actual()));
    }

    @Test
    public void AC4_703() throws MessagingException {
        runOnlyForThoseTypes("ANDROID_FUN");
        testInfo.name("Disconnected - Send Email via Port 587").expected("Email Delivered and IP not Hidden").jira("DROID-1370");
        Android.openHotspotShield().tapDisconnectButton();
        String subject = "Test-" + Utilities.generateRandomNumber(999999, 10000);
        new K9Mail().open().tapQaAccountPort587().tapComposeButton().enterTo(Credentials.gmailUsername).enterSubject(subject).tapSendButton();
        GMailReciever.waitForEmail(subject, 25);
        testInfo.actual(GMailReciever.getEmailHeaderIp(subject));
        Assert.assertFalse("IP still hidden: " + TestInfo.actual(), Utilities.isVpnIp(TestInfo.actual()));
    }

    @Test
    public void AC4_704() throws MessagingException {
        runOnlyForThoseTypes("ANDROID_FUN");
        testInfo.name("Disconnected - Send Email via Port 465").expected("Email Delivered and IP not Hidden").jira("DROID-1370");
        Android.openHotspotShield().tapDisconnectButton();
        String subject = "Test-" + Utilities.generateRandomNumber(999999, 10000);
        new K9Mail().open().tapQaAccountPort465().tapComposeButton().enterTo(Credentials.gmailUsername).enterSubject(subject).tapSendButton();
        GMailReciever.waitForEmail(subject, 25);
        testInfo.actual(GMailReciever.getEmailHeaderIp(subject));
        Assert.assertFalse("IP still hidden: " + TestInfo.actual(), Utilities.isVpnIp(TestInfo.actual()));
    }
}
