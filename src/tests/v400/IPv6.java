package tests.v400;

import api.android.Android;
import api.googlechrome.GoogleChrome;
import api.hotspotshield.v400.ViewAccount;
import managers.QAManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.Curl;
import utilities.TestInfo;
import utilities.Utilities;
import utilities.constants.Credentials;
import utilities.constants.Website;

/**
 * Created by Artur Spirin on 11/6/15.
 **/
public class IPv6 extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){
        testInfo.suite("IPv6").execType("criticServer");
    }

    @Test
    public void HSSS_652() {

        testInfo.name("HTTP traffic via Clean IPv6 for Elite users - VPN").expected("IPv6 Address").jira("AUTO-1753");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword());
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.IPV6_FINDIPINFO).getIpAddress(IPv6));
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.IPV6_FINDIPINFO).getIpAddress(IPv6));
        //testInfo.actual(new GoogleChrome().open(Website.IPV6_FINDIPINFO).getIpAddress(IPv6));
        Assert.assertTrue("Not an IPv6 address", TestInfo.actual().split(":").length == 8);
    }

    @Test
    public void HSSS_653() {

        testInfo.name("HTTPS traffic via Clean IPv6 for Elite users - VPN").expected("IPv6 Address").jira("AUTO-1753");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword());
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTPS_IPV6_FINDIPINFO).getIpAddress(IPv6));
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTPS_IPV6_FINDIPINFO).getIpAddress(IPv6));
        Assert.assertTrue("Not an IPv6 address", TestInfo.actual().split(":").length == 8);
    }

    @Test
    public void HSSS_650() {

        testInfo.name("HTTP traffic via Clean IPv6 for Free users - VPN").expected("IPv6 Address").jira("AUTO-1753");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signOut();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.IPV6_FINDIPINFO).getIpAddress(IPv6));
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.IPV6_FINDIPINFO).getIpAddress(IPv6));
        //testInfo.actual(new GoogleChrome().open(Website.IPV6_FINDIPINFO).getIpAddress(IPv6));
        Assert.assertTrue("Not an IPv6 address", TestInfo.actual().split(":").length == 8);
    }

    @Test
    public void HSSS_651() {

        testInfo.name("HTTPS traffic via Clean IPv6 for Free users - VPN").expected("IPv6 Address").jira("AUTO-1753");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signOut();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTPS_IPV6_FINDIPINFO).getIpAddress(IPv6));
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTPS_IPV6_FINDIPINFO).getIpAddress(IPv6));
        Assert.assertTrue("Not an IPv6 address", TestInfo.actual().split(":").length == 8);
    }
}
