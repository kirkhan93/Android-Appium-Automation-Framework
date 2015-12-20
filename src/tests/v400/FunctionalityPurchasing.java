package tests.v400;

import api.android.Android;
import api.hotspotshield.v400.ViewAccount;
import managers.QAManager;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.TestInfo;
import utilities.constants.BuyButton;
import utilities.constants.Location;

/**
 * Created by Artur Spirin on 11/13/15.
 **/
public class FunctionalityPurchasing extends QAManager{

    private static TestInfo testInfo = new TestInfo();

    @BeforeClass
    public static void initialization(){
        testInfo.suite("Purchasing").execType("criticClient");
    }

    //@Test //TODO Finish when there is a debug menu item available for force fetching the add
    public void AC4_711(){
        testInfo.name("Buy via Ad Buy Button").expected("Able to make the purchase");
        testInfo.actual("Success");
    }

    @Test
    public void AC4_712(){
        testInfo.name("Buy via Up-Sell Monthly Elite Button").expected("Able to make the purchase");
        Android.openHotspotShield();
        new ViewAccount().signOut().openUpSell().tapBuyButton(BuyButton.MONTHLY_ELITE).closeDialog();
        testInfo.actual("Success");
    }

    //@Test //TODO Need custom scroll method for upsell view, appium's method dont work thus cannot scroll to yearly elite button
    public void AC4_713(){
        testInfo.name("Buy via Up-Sell Yearly Elite Button").expected("Able to make the purchase");
        Android.openHotspotShield();
        new ViewAccount().signOut().openUpSell().tapBuyButton(BuyButton.YEARLY_ELITE).closeDialog();
        testInfo.actual("Success");
    }

    @Test
    public void AC4_714(){
        testInfo.name("Buy via VL Up-Sell Monthly Elite Button").expected("Able to make the purchase");
        Android.openHotspotShield();
        new ViewAccount().signOut().openLocationSelector().selectLocationAsFreeUser(Location.JAPAN).tapBuyButton(BuyButton.MONTHLY_ELITE).closeDialog();
        testInfo.actual("Success");
    }

    //@Test //TODO Need custom scroll method for upsell view, appium's method dont work thus cannot scroll to yearly elite button
    public void AC4_715(){
        testInfo.name("Buy via VL Up-Sell Yearly Elite Button").expected("Able to make the purchase");
        Android.openHotspotShield();
        new ViewAccount().signOut().openLocationSelector().selectLocationAsFreeUser(Location.JAPAN).tapBuyButton(BuyButton.YEARLY_ELITE).closeDialog();
        testInfo.actual("Success");
    }

    //@Test //TODO Need custom scroll method for upsell view, appium's method dont work thus cannot scroll to yearly elite button
    public void AC4_716(){
        Assume.assumeTrue("Skipping test due to NAF in versions prior to Lollipop", Android.getDeviceOsVersion()>=500);
        testInfo.name("Buy via Bandwidth Google Play Yearly Elite Button").expected("Able to make the purchase");
        Android.openHotspotShield();
        new ViewAccount().signOut().openMenuDrawer().tapDebugTestBandwidthWall().tapGooglePlay().tapBuyButton(BuyButton.YEARLY_ELITE).closeDialog();
        testInfo.actual("Success");
    }

    @Test
    public void AC4_717(){
        Assume.assumeTrue("Skipping test due to NAF in versions prior to Lollipop", Android.getDeviceOsVersion()>=500);
        testInfo.name("Buy via Bandwidth Google Play Monthly Elite Button").expected("Able to make the purchase");
        Android.openHotspotShield().tapConnectButton();
        new ViewAccount().signOut().openMenuDrawer().tapDebugTestBandwidthWall().tapGooglePlay().tapBuyButton(BuyButton.MONTHLY_ELITE).closeDialog();
        testInfo.actual("Success");
    }

    @Test
    public void AC4_718(){
        Assume.assumeTrue("Skipping test due to NAF in versions prior to Lollipop", Android.getDeviceOsVersion()>=500);
        testInfo.name("Buy via Bandwidth Other Options Button").expected("Able to make the purchase");
        Android.openHotspotShield().tapConnectButton();
        testInfo.actual(new ViewAccount().signOut().openMenuDrawer().tapDebugTestBandwidthWall().tapOtherOptions().getCurrentUrl());
        Assert.assertTrue("Wrong page opened: " +TestInfo.actual(), TestInfo.actual().contains("hsselite.com"));
        testInfo.actual("Success");
    }
}
