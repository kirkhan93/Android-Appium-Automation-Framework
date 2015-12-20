package managers;

/**
 * Created by Artur Spirin(a.spirin@anchorfree.com) on 12/11/2015.
 **/
public class ReportingManager {

    private static int

            uiServiceDisconnects = 0,
            hssCantConnectToServers = 0,
            couldntConnect = 0,
            couldntDisconnect = 0,
            issuesWithPlayInstalls = 0,
            issuesWithSpeed = 0,
            adsNotShown = 0,
            optInNotShown = 0,
            tutorialNotShown = 0,
            pwInterstitialNotShown = 0,
            animationIssues = 0,
            logInIssues = 0,
            logOutIssues = 0,
            vlFailedToChange = 0;

    public static void addUiServiceDisconnect(){
        uiServiceDisconnects++;
    }

    public static void addCouldntConnect(){
        couldntConnect++;
    }

    public static void addCouldntDisconnect(){
        couldntDisconnect++;
    }

    public static void addPlayInstallIssue(){
        issuesWithPlayInstalls++;
    }

    public static void addSpeedIssue(){
        issuesWithSpeed++;
    }

    public static void addAdsNotShown(){
        adsNotShown++;
    }

    public static void addOptInNotShown(){
        optInNotShown++;
    }

    public static void addTutorialNotShown(){
        tutorialNotShown++;
    }

    public static void addPWInterstitialNotShown(){
        pwInterstitialNotShown++;
    }

    public static void addAnimationIssue(){
        animationIssues++;
    }

    public static void addLogInIssue(){
        logInIssues++;
    }

    public static void addLogOutIssue(){
        logOutIssues++;
    }

    public static void addCantConnectToServers(){
        hssCantConnectToServers++;
    }

    public static void addVlFailedToChange(){
        vlFailedToChange++;
    }

    public static String getReport(){
        String report = "----------------Report (Beta)------------------";
        if(uiServiceDisconnects>0)      report = report+"\nUi/Service Disconnects: "+uiServiceDisconnects;
        if(couldntConnect>0)            report = report+"\nCould NOT Connect: "+couldntConnect;
        if(couldntDisconnect>0)         report = report+"\nCould NOT Disconnect: "+couldntDisconnect;
        if(issuesWithPlayInstalls>0)    report = report+"\nIssues with App installs from Google Play: "+issuesWithPlayInstalls;
        if(issuesWithSpeed>0)           report = report+"\nIssues with Speed: "+issuesWithSpeed;
        if(adsNotShown>0)               report = report+"\nAds NOT shown when they were expected: "+adsNotShown;
        if(optInNotShown>0)             report = report+"\nOpt-In NOT shown: "+optInNotShown;
        if(tutorialNotShown>0)          report = report+"\nTutorial NOT shown: "+tutorialNotShown;
        if(pwInterstitialNotShown>0)    report = report+"\nAppLock Interstitial Not Shown: "+pwInterstitialNotShown;
        if(animationIssues>0)           report = report+"\nAnimation Issues: "+animationIssues;
        if(logInIssues>0)               report = report+"\nLog In Issues: "+logInIssues;
        if(logOutIssues>0)              report = report+"\nLog Out Issues: "+logOutIssues;
        if(vlFailedToChange>0)          report = report+"\nVirtual Location Failed to Change: "+vlFailedToChange;
        if(hssCantConnectToServers>0)   report = report+"\nHotspot Shield Couldn't Connect to it's Servers (Legacy 1013): "+hssCantConnectToServers;
        return report;
    }
}
