package tests.v400;

import api.android.Android;
import api.googlechrome.GoogleChrome;
import api.googleplay.GooglePlay;
import api.googleplay.GooglePlayApplicationPage;
import api.hotspotshield.v400.ViewAccount;
import api.youtube.YouTube;
import managers.QAManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;
import utilities.constants.Credentials;
import utilities.constants.Location;
import utilities.constants.Network;
import utilities.constants.Website;

/**
 * Created by Artur Spirin on 12/18/15.
 **/
public class Browsing  extends QAManager{

    private static TestInfo testInfo = new TestInfo();
    private static final String googlePlaySearchTerm = "Hotspot Shield VPN Privacy";

    @BeforeClass
    public static void localPreset(){
        testInfo.suite("Browsing").execType("criticClient");
        //if(DatabaseManager.TEST_SET.toLowerCase().contains("critic")) testInfo.execType("smokeServer");
    }

    @Test
    public void AC4_594_1(){
        runOnlyForThoseTypes("ANDROID_FUN");
        testInfo.name("UK - YouTube Streaming").expected("Able to watch YouTube");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword()).openLocationSelector().selectLocationAsEliteUser(Location.UNITED_KINGDOM).verifyLocationChange(Location.UNITED_KINGDOM);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout);
        YouTube video = new YouTube().open().clickSearchIcon().searchFor("Funny Videos").tapVideo(1);
        Assert.assertTrue("Video did not play", video.isVideoLoaded());
        testInfo.actual("Video Played");
    }

    @Test
    public void AC4_594_2(){
        runOnlyForThoseTypes("ANDROID_FUN");
        testInfo.name("UK - Google Play Browsing (HTTPS)").expected("Able to browse HTTPS");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword()).openLocationSelector().selectLocationAsEliteUser(Location.UNITED_KINGDOM).verifyLocationChange(Location.UNITED_KINGDOM);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout);
        GooglePlayApplicationPage appPage = new GooglePlay().open().tapSearchField().searchFor(googlePlaySearchTerm).tapOn(googlePlaySearchTerm);
        Assert.assertTrue("Google Play App Page did not load", appPage.isLoaded());
        testInfo.actual("Browsed fine");
    }

    @Test
    public void AC4_594_3(){
        runOnlyForThoseTypes("ANDROID_FUN");
        testInfo.name("UK - FindIpInfo (HTTP)").expected("Able to browse HTTP");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword()).openLocationSelector().selectLocationAsEliteUser(Location.UNITED_KINGDOM).verifyLocationChange(Location.UNITED_KINGDOM);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout);
        try{new GoogleChrome().open(Website.FINDIPINFO).getIpAddress(IPv4);}
        catch (AssertionError e){Assert.assertTrue("FindIpInfo Page did not load", false);}
    }

    @Test
    public void AC4_591_1(){
        runOnlyForThoseTypes("ANDROID_FUN");
        testInfo.name("Japan - YouTube Streaming").expected("Able to watch YouTube");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword()).openLocationSelector().selectLocationAsEliteUser(Location.JAPAN).verifyLocationChange(Location.JAPAN);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout);
        YouTube video = new YouTube().open().clickSearchIcon().searchFor("Funny Videos").tapVideo(1);
        Assert.assertTrue("Video did not play", video.isVideoLoaded());
        testInfo.actual("Video Played");
    }

    @Test
    public void AC4_591_2(){
        runOnlyForThoseTypes("ANDROID_FUN");
        testInfo.name("Japan - Google Play Browsing (HTTPS)").expected("Able to browse HTTPS");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword()).openLocationSelector().selectLocationAsEliteUser(Location.JAPAN).verifyLocationChange(Location.JAPAN);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout);
        GooglePlayApplicationPage appPage = new GooglePlay().open().tapSearchField().searchFor(googlePlaySearchTerm).tapOn(googlePlaySearchTerm);
        Assert.assertTrue("Google Play App Page did not load", appPage.isLoaded());
        testInfo.actual("Browsed fine");
    }

    @Test
    public void AC4_591_3(){
        runOnlyForThoseTypes("ANDROID_FUN");
        testInfo.name("Japan - FindIpInfo (HTTP)").expected("Able to browse HTTP");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword()).openLocationSelector().selectLocationAsEliteUser(Location.JAPAN).verifyLocationChange(Location.JAPAN);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout);
        try{testInfo.actual(new GoogleChrome().open(Website.FINDIPINFO).getIpAddress(IPv4));}
        catch (AssertionError e){Assert.assertTrue("FindIpInfo Page did not load", false);}
    }

    @Test
    public void AC4_595_1(){
        runOnlyForThoseTypes("ANDROID_FUN");
        testInfo.name("Australia - YouTube Streaming").expected("Able to watch YouTube");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword()).openLocationSelector().selectLocationAsEliteUser(Location.AUSTRALIA).verifyLocationChange(Location.AUSTRALIA);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout);
        YouTube video = new YouTube().open().clickSearchIcon().searchFor("Funny Videos").tapVideo(1);
        Assert.assertTrue("Video did not play", video.isVideoLoaded());
        testInfo.actual("Video Played");
    }

    @Test
    public void AC4_595_2(){
        runOnlyForThoseTypes("ANDROID_FUN");
        testInfo.name("Australia - Google Play Browsing (HTTPS)").expected("Able to browse HTTPS");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword()).openLocationSelector().selectLocationAsEliteUser(Location.AUSTRALIA).verifyLocationChange(Location.AUSTRALIA);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout);
        GooglePlayApplicationPage appPage = new GooglePlay().open().tapSearchField().searchFor(googlePlaySearchTerm).tapOn(googlePlaySearchTerm);
        Assert.assertTrue("Google Play App Page did not load", appPage.isLoaded());
        testInfo.actual("Browsed fine");
    }

    @Test
    public void AC4_595_3(){
        runOnlyForThoseTypes("ANDROID_FUN");
        testInfo.name("Australia - FindIpInfo (HTTP)").expected("Able to browse HTTP");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signInAsElite(Credentials.eliteUsername(), Credentials.elitePassword()).openLocationSelector().selectLocationAsEliteUser(Location.AUSTRALIA).verifyLocationChange(Location.AUSTRALIA);
        Android.openSettingsWifi().connectTo(Network.AnchorFree_5GHz_Private_optout);
        try{testInfo.actual(new GoogleChrome().open(Website.FINDIPINFO).getIpAddress(IPv4));}
        catch (AssertionError e){Assert.assertTrue("FindIpInfo Page did not load", false);}
    }
}
