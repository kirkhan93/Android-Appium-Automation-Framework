package tests.v400;

import api.android.Android;
import api.googlechrome.GoogleChrome;
import api.hotspotshield.v400.ViewAccount;
import api.hotspotshield.v400.ViewHome;
import managers.DatabaseManager;
import managers.QAManager;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.Curl;
import utilities.FallbackLogic;
import utilities.TestInfo;
import utilities.Utilities;
import utilities.constants.Credentials;
import utilities.constants.Website;

import java.sql.SQLException;

/**
 * Created by Artur Spirin on 11/10/15.
 **/
public class Fallback extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){
        testInfo.suite("Fallback").execType("criticServer");
    }

    @AfterClass
    public static void tearDown(){
        if(isFunctionalRequest()) DatabaseManager.HOST = "";
        if(isFunctionalRequest()) DatabaseManager.IP_IN = "";
    }

    @Test
    public void HSSS_213() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        testInfo.name("HTTP traffic via Custom IP - VPN").expected(FallbackLogic.EXPECTED_CUSTOM_FREE_FALLBACK).jira("DROID-1354");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signOut();
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        String IP1=null;
        if(Curl.isAvailable()) IP1 = new Curl().get(Website.ELECTION_GUIDE).getIpAddress(IPv4);
        if(Utilities.isEmpty(IP1)) IP1 = new GoogleChrome().open(Website.ELECTION_GUIDE).getIpAddress(IPv4);
        testInfo.actual("ElectionGuide: " + IP1 + " ElectionAdvisor: Not checked yet");
        String[] results = FallbackLogic.checkCustomFreeFallback(IP1, DatabaseManager.HOST);
        Assert.assertTrue("Error in return method", Boolean.parseBoolean(results[1]));
        Android.openHotspotShield();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword());
        String IP2=null;
        if(Curl.isAvailable()) IP2 = new Curl().get(Website.ELECTION_ADVISOR).getIpAddress(IPv4);
        if(Utilities.isEmpty(IP2)) IP2 = new GoogleChrome().open(Website.ELECTION_ADVISOR).getIpAddress(IPv4);
        testInfo.actual(TestInfo.actual().replaceAll("Not checked yet", IP2));
        results = FallbackLogic.checkCustomEliteFallback(IP2, DatabaseManager.HOST);
        Assert.assertTrue("Error in return method", Boolean.parseBoolean(results[1]));
    }

    @Test
    public void HSSS_362() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        testInfo.name("HTTPS traffic via Elite IP for Elite users - VPN").expected(FallbackLogic.EXPECTED_CUSTOM_ELITE_FALLBACK+" and both IPs are the same.").jira("DROID-1354");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword());
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        String IP1=null;
        if(Curl.isAvailable()) IP1 = new Curl().get(Website.HTTPS_FINDIPINFO).getIpAddress(IPv4);
        if(Utilities.isEmpty(IP1)) IP1 = new GoogleChrome().open(Website.HTTPS_FINDIPINFO).getIpAddress(IPv4);
        testInfo.actual("httpS:FindIpInfo: " + IP1 + " http:FindIpInfo: Not checked yet");
        String[] results = FallbackLogic.checkEliteFallback(IP1, DatabaseManager.HOST);
        Assert.assertTrue("Error in return method", Boolean.parseBoolean(results[1]));
        String IP2 = null;
        if(Curl.isAvailable()) IP2 = new Curl().get(Website.FINDIPINFO).getIpAddress(IPv4);
        if(Utilities.isEmpty(IP2)) IP2 = new GoogleChrome().open(Website.FINDIPINFO).getIpAddress(IPv4);
        testInfo.actual(TestInfo.actual().replaceAll("Not checked yet", IP2));
        results = FallbackLogic.checkEliteFallback(IP2, DatabaseManager.HOST);
        Assert.assertTrue("Error in return method", Boolean.parseBoolean(results[1]));
        Assert.assertTrue("httpS: "+IP1+" and http: "+IP2+" IPs from FindIpInfo are not equal", IP1.equals(IP2));
    }

    @Test
    public void HSSS_111() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        testInfo.name("HTTPS traffic via Clean IP for Free users - VPN").expected(FallbackLogic.EXPECTED_ALWAYS_FREE_FALLBACK).jira("DROID-1354");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword());
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        String IP1=null;
        if(Curl.isAvailable()) IP1 = new Curl().get(Website.HTTPS_SHOWNETWORKIP).getIpAddress(IPv4);
        if(Utilities.isEmpty(IP1)) IP1 = new GoogleChrome().open(Website.HTTPS_SHOWNETWORKIP).getIpAddress(IPv4);
        testInfo.actual("httpS:ShowNetworkIp: " + IP1 + " http:FindIpInfo: Not checked yet");
        String[] results = FallbackLogic.checkEliteFallback(IP1, DatabaseManager.HOST);
        Assert.assertTrue("Error in return method", Boolean.parseBoolean(results[1]));
        String IP2 = null;
        if(Curl.isAvailable()) IP2 = new Curl().get(Website.FINDIPINFO).getIpAddress(IPv4);
        if(Utilities.isEmpty(IP2)) IP2 = new GoogleChrome().open(Website.FINDIPINFO).getIpAddress(IPv4);
        testInfo.actual(TestInfo.actual().replaceAll("Not checked yet", IP2));
        results = FallbackLogic.checkEliteFallback(IP2, DatabaseManager.HOST);
        Assert.assertTrue("Error in return method", Boolean.parseBoolean(results[1]));
        Assert.assertTrue("httpS: "+IP1+" and http: "+IP2+" IPs are not equal", IP1.equals(IP2));
    }

    @Test
    public void HSSS_224() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        testInfo.name("HTTP traffic via Custom IP Elite - VPN").expected(FallbackLogic.EXPECTED_CUSTOM_ELITE_FALLBACK).jira("DROID-1354");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword());
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        String IP=null;
        if(Curl.isAvailable()) IP = new Curl().get(Website.ELECTION_ADVISOR).getIpAddress(IPv4);
        if(Utilities.isEmpty(IP)) IP = new GoogleChrome().open(Website.ELECTION_ADVISOR).getIpAddress(IPv4);
        testInfo.actual("ElectionAdvisor: " + IP);
        String[] results = FallbackLogic.checkCustomEliteFallback(IP, DatabaseManager.HOST);
        Assert.assertTrue("Error in return method", Boolean.parseBoolean(results[1]));
    }

    @Test
    public void HSSS_223() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        testInfo.name("HTTPS traffic via Custom IP Elite - VPN").expected(FallbackLogic.EXPECTED_CUSTOM_ELITE_FALLBACK).jira("DROID-1354");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword());
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        String IP=null;
        if(Curl.isAvailable()) IP = new Curl().get(Website.HTTPS_ELECTION_ADVISOR).getIpAddress(IPv4);
        if(Utilities.isEmpty(IP)) IP = new GoogleChrome().open(Website.HTTPS_ELECTION_ADVISOR).getIpAddress(IPv4);
        testInfo.actual("httpS:ElectionAdvisor: " + IP);
        String[] results = FallbackLogic.checkCustomEliteFallback(IP, DatabaseManager.HOST);
        Assert.assertTrue("Error in return method", Boolean.parseBoolean(results[1]));
    }

    @Test
    public void HSSS_109() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        testInfo.name("HTTPS traffic via Elite IP for Elite users - VPN").expected(FallbackLogic.EXPECTED_CUSTOM_ELITE_FALLBACK).jira("DROID-1354");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword());
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        String IP=null;
        if(Curl.isAvailable()) IP = new Curl().get(Website.FINDIPINFO).getIpAddress(IPv4);
        if(Utilities.isEmpty(IP)) IP = new GoogleChrome().open(Website.FINDIPINFO).getIpAddress(IPv4);
        testInfo.actual("FindIpInfo: " + IP);
        String[] results = FallbackLogic.checkEliteFallback(IP, DatabaseManager.HOST);
        Assert.assertTrue("Error in return method", Boolean.parseBoolean(results[1]));
    }

    @Test
    public void HSSS_110() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        testInfo.name("HTTPS traffic via Custom IP Free - VPN").expected(FallbackLogic.EXPECTED_CUSTOM_FREE_FALLBACK).jira("DROID-1354");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signOut();
        if(isFunctionalRequest()) new ViewHome().getDebugHost();
        String IP=null;
        if(Curl.isAvailable()) IP = new Curl().get(Website.HTTPS_ELECTION_GUIDE).getIpAddress(IPv4);
        if(Utilities.isEmpty(IP)) IP = new GoogleChrome().open(Website.HTTPS_ELECTION_GUIDE).getIpAddress(IPv4);
        testInfo.actual("httpS:ElectionGuide: " + IP);
        String[] results = FallbackLogic.checkCustomFreeFallback(IP, DatabaseManager.HOST);
        Assert.assertTrue("Error in return method", Boolean.parseBoolean(results[1]));
    }
}
