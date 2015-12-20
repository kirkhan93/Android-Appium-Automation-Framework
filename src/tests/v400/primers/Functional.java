package tests.v400.primers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.TestSuiteInstall;
import tests.v400.*;

/**
 * Created by Artur Spirin on 11/18/15.
 **/
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestSuiteInstall.class,
        SmokeCheck.class,
        Browsing.class,
        Speedtest.class,
        GooglePlayInstalls.class,
        FunctionalityEmail.class,
        FunctionalityAccount.class,
        FunctionalityInviteSDK.class,
        FunctionalityPurchasing.class,
        FunctionalityNetworkStatusBar.class,
        AutoOnAppsMobileNetwork.class,
        AutoOnAppsSecuredNetwork.class,
        AutoOnAppsUnsecuredNetwork.class,
        AutoOnNetworks.class,
        NavigationViewHome.class,
        NavigationMenuDrawer.class,
        NavigationViewAbout.class,
        NavigationDialogFeedback.class,
        Headers.class,
        IPv4.class,
        //IPv6.class,
        Fallback.class
})
public class Functional {}
