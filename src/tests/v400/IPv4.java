package tests.v400;

import api.android.Android;
import api.googlechrome.GoogleChrome;
import managers.QAManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.Curl;
import utilities.TestInfo;
import utilities.Utilities;
import utilities.constants.Website;

/**
 * Created by Artur Spirin on 11/6/15.
 **/
public class IPv4 extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){
        testInfo.suite("IPv4").execType("criticServer");
    }
    
    @Test
    public void HSSS_325() {
        testInfo.name("HTTP_X_AF_SRC_ADDR").expected("IPv4 Address");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_SRC_ADDR).getIpAddress(IPv4));
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_SRC_ADDR).getIpAddress(IPv4));
        Assert.assertTrue("Not an IPv4 Address: " + TestInfo.actual(), TestInfo.actual().split("\\.").length == 4);
    }

    @Test
    public void HSSS_327() {
        testInfo.name("HTTP_X_AF_SESSION_IP").expected("IPv4 Address");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_SESSION_IP).getIpAddress(IPv4));
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_SESSION_IP).getIpAddress(IPv4));
        Assert.assertTrue("Not an IPv4 Address: " +TestInfo.actual(), TestInfo.actual().split("\\.").length == 4);
    }

    @Test
    public void HSSS_333() {
        testInfo.name("HTTP_X_AF_FBW_OUT_IP").expected("IPv4 Address");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_FBW_OUT_IP).getIpAddress(IPv4));
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_FBW_OUT_IP).getIpAddress(IPv4));
        Assert.assertTrue("Not an IPv4 Address: "+TestInfo.actual(), TestInfo.actual().split("\\.").length == 4);
    }

    @Test
    public void HSSS_334() {
        testInfo.name("HTTP_X_AF_FBW_ELITE_OUT_IP").expected("IPv4 Address");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_FBW_ELITE_OUT_IP).getIpAddress(IPv4));
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_FBW_ELITE_OUT_IP).getIpAddress(IPv4));
        Assert.assertTrue("Not an IPv4 Address: " + TestInfo.actual(), TestInfo.actual().split("\\.").length == 4);
    }

    @Test
    public void HSSS_336() {
        testInfo.name("HTTP_X_AF_NON_HTTP_OUT_IP").expected("IPv4 Address");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_NON_HTTP_OUT_IP).getIpAddress(IPv4));
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_NON_HTTP_OUT_IP).getIpAddress(IPv4));
        Assert.assertTrue("Not an IPv4 Address: " + TestInfo.actual(), TestInfo.actual().split("\\.").length == 4);
    }

    @Test
    public void HSSS_339() {
        testInfo.name("HTTP_X_AF_CUSTOM_IP").expected("IPv4 Address");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_CUSTOM_IP).getIpAddress(IPv4));
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_CUSTOM_IP).getIpAddress(IPv4));
        Assert.assertTrue("Not an IPv4 Address: " + TestInfo.actual(), TestInfo.actual().split("\\.").length == 4);
    }

    @Test
    public void HSSS_340() {
        testInfo.name("HTTP_X_AF_CUSTOM_ELITE_IP").expected("IPv4 address");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_CUSTOM_ELITE_IP).getIpAddress(IPv4));
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_CUSTOM_ELITE_IP).getIpAddress(IPv4));
        Assert.assertTrue("Not an IPv4 Address: " + TestInfo.actual(), TestInfo.actual().split("\\.").length == 4);
    }
}