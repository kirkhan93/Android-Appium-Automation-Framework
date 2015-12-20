package tests.v400;

import api.android.Android;
import api.googlechrome.GoogleChrome;
import api.hotspotshield.v400.ViewAccount;
import managers.DatabaseManager;
import managers.QAManager;
import managers.SystemManager;
import net.lingala.zip4j.exception.ZipException;
import org.junit.Assert;
import org.junit.Assume;
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
public class Headers extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void localPreset() throws ZipException {
        testInfo.suite("Headers"); testInfo.execType("criticServer");
    }

    @Test
    public void ACH_203(){
        Assume.assumeTrue(Curl.isAvailable());
        testInfo.name("Insertion").expected("Insertion Exists");
        Android.openHotspotShield().tapConnectButton();
        testInfo.actual(SystemManager.executeCommand("adb shell /data/local/tmp/curl -A \"Chrome/32.0.1700.99 Mobile Safari/537.36\" http://stackoverflow.com | grep HSS"));
        Assert.assertTrue("Insertion not found: " +TestInfo.actual(), TestInfo.actual().contains("HSSHIELD"));
        testInfo.actual("Insertion Exists");
    }

    @Test
    public void HSSS_320() {
        testInfo.name("REMOTE_ADDR").expected("IP Starts with 127. or 10.");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.REMOTE_ADDR).getIpAddress(IPv4));
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.REMOTE_ADDR).getIpAddress(IPv4));
        Assert.assertTrue(TestInfo.actual().startsWith("127.") || TestInfo.actual().startsWith("10."));
    }

    @Test
    public void HSSS_321() {
        runOnlyForThoseTypes("ANDROID_SRV");
        testInfo.name("HTTP_X_AF_HOST").expected(DatabaseManager.HOST);
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_HOST).getContentRssHeader());
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_HOST).getContent());
        Assert.assertTrue("Different Host: " + TestInfo.actual(), TestInfo.actual().equals(TestInfo.expected()));
    }

    @Test
    public void HSSS_322() {
        testInfo.name("HTTP_X_HSS_TAG").expected("HSSCNL0IPSEC or HSSCNL000001");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_TAG).getContentRssHeader());
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_TAG).getContent());
        Assert.assertTrue("Incorrect Tag: " + TestInfo.actual(), TestInfo.expected().contains(TestInfo.actual()));
    }

    @Test
    public void HSSS_323() {
        testInfo.name("HTTP_X_AF_SERIAL").expected("HSSHIELD00US");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_SERIAL).getContentRssHeader());
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_SERIAL).getContent());
        Assert.assertTrue("Incorrect Serial: "+TestInfo.actual(), TestInfo.actual().equals(TestInfo.expected()));
    }

    @Test
    public void HSSS_324() {
        testInfo.name("HTTP_X_AF_TAG").expected("HSSCNL0IPSEC or HSSCNL000001");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_TAG).getContentRssHeader());
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_TAG).getContent());
        Assert.assertTrue("Incorrect Tag: " + TestInfo.actual(), TestInfo.expected().contains(TestInfo.actual()));
    }

    @Test
    public void HSSS_326() {
        testInfo.name("HTTP_X_AF_HASH").expected("Hash");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_HASH).getContentRssHeader());
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_HASH).getContent());
        Assert.assertTrue("Did no get Hash: "+TestInfo.actual() , TestInfo.actual().contains("0000000") && TestInfo.actual().split("").length == 32);
    }

    @Test
    public void HSSS_328() {
        testInfo.name("HTTP_X_AF_VER").expected(String.valueOf(Android.getAppVersion()));
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_VER).getContentRssHeader());
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_VER).getContent());
        Assert.assertTrue("Client versions did not match!", TestInfo.actual().equals(TestInfo.expected()));
    }

    @Test
    public void HSSS_330() {
        testInfo.name("HTTP_X_AF_C_COUNTRY").expected("US");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_C_COUNTRY).getContentRssHeader());
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_C_COUNTRY).getContent());
        Assert.assertTrue("Incorrect Country: " +TestInfo.actual(), TestInfo.actual().equals(TestInfo.expected()));
    }

    @Test
    public void HSSS_331() {
        testInfo.name("HTTP_X_AF_C_REGION").expected("California");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_C_REGION).getContentRssHeader());
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_C_REGION).getContent());
        Assert.assertTrue("Incorrect State: " + TestInfo.actual(), TestInfo.actual().equals(TestInfo.expected()));
    }

    //@Test Disabled at Viacheslav N. request Email Subject: HSSS-332 because city often changes
;    public void HSSS_332() {
        testInfo.name("HTTP_X_AF_C_CITY").expected("San Francisco");
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_C_CITY).getContentRssHeader());
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_C_CITY).getContent());
        Assert.assertTrue(TestInfo.actual().equals(TestInfo.expected()));
    }

    @Test
    public void HSSS_337() {
        testInfo.name("HTTP_X_AF_TUNNEL");
        Android.openHotspotShield().tapConnectButton();
        if(Utilities.isEmpty(PROTOCOL)) testInfo.expected("hydra");
        else testInfo.expected(PROTOCOL);
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_TUNNEL).getContentRssHeader());
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_TUNNEL).getContent());
        Assert.assertTrue("Protocols do not match", TestInfo.expected().equals(TestInfo.actual()));
    }

    @Test
    public void HSSS_338() {
        testInfo.name("HTTP_X_AF_BRIDGE").expected("Returns null or 0");
        Android.openHotspotShield().tapConnectButton();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_BRIDGE).getContentRssHeader());
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_BRIDGE).getContent());
        Assert.assertTrue("Incorrect value: " + TestInfo.actual(), TestInfo.actual().equals("") || TestInfo.actual().equals("0"));
    }

    @Test
    public void AC4_208(){
        testInfo.name("HTTP_X_AF_ELITE Header / Elite Sign In").expected("1").jira("DROID-1127");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword());
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_ELITE).getContentRssHeader());
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_ELITE).getContent());
        Assert.assertTrue("Not an Elite Flag: "+TestInfo.actual() , TestInfo.actual().equals("1"));
    }

    @Test
    public void AC4_209(){
        testInfo.name("HTTP_X_AF_ELITE Header / Free Sign In").expected("0").jira("DROID-1127");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signOut();
        if(Curl.isAvailable()) testInfo.actual(new Curl().get(Website.HTTP_X_AF_ELITE).getContentRssHeader());
        if(Utilities.isEmpty(TestInfo.actual())) testInfo.actual(new GoogleChrome().open(Website.HTTP_X_AF_ELITE).getContent());
        Assert.assertTrue("Not a Free User Flag: "+TestInfo.actual() , TestInfo.actual().equals("0"));
    }
}