package api.hotspotshield.v400;

import api.android.Android;
import api.googlechrome.GoogleChrome;
import managers.QAManager;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import utilities.Curl;
import utilities.TestInfo;
import utilities.Utilities;
import utilities.constants.AdProvider;
import utilities.constants.Website;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Artur Spirin on 11/2/15.
 **/
public class AdView extends Android{

    private static boolean flagAdExpected = false;
    private static String GOOGLE_AD_ID = null;

    public static boolean isExpected(){
        return flagAdExpected;
    }

    public static boolean isShown(){
        //QAManager.log.debug("Checking if an ad is shown");
        String focusedActivity = Android.getActivityInFocus();
        for(String adActivity : AdProvider.adActivities){
            if(focusedActivity.contains(adActivity)){
                QAManager.log.debug("Ad in foreground!");
                return true;
            }
        }
        return false;
    }

    public static void setFlagAdExpected(boolean value){
        flagAdExpected = value;
        QAManager.log.debug("Set Flag for expecting Ads to: " + value);
    }

    public static void waitForAd(){
        if(flagAdExpected){
            int seconds = 20;
            QAManager.log.debug("Expect ads was enabled so expecting to see an Ad for Manual Connect/Disconnect events");
            while(seconds != 0 && !isShown()){
                seconds--;
                Utilities.Wait(1000);
            }
        }else{
            if(isShown()){
                new Android().pressHomeButton();
                Android.openHotspotShield();
            }
        }
    }

    public static String getGoogleAdId(){
        if(GOOGLE_AD_ID == null){
            if(Curl.isAvailable()) GOOGLE_AD_ID = new Curl().get(Website.HTTP_X_AF_SESSION_STRING).getContentRssHeader();
            if(Utilities.isEmpty(TestInfo.actual())) GOOGLE_AD_ID = new GoogleChrome().open(Website.HTTP_X_AF_SESSION_STRING).getContent();
            GOOGLE_AD_ID = GOOGLE_AD_ID.substring(GOOGLE_AD_ID.indexOf("google_ad_id="), GOOGLE_AD_ID.indexOf("&isBlackIP")).replaceAll("google_ad_id=", "");
            QAManager.log.debug("Google Ad Id: " + GOOGLE_AD_ID);
        }
        return GOOGLE_AD_ID;
    }

    public static boolean didCallbackHappen(String adProvider, String googleAdId, int forLastNMinutes) throws IOException {
        String api = String.format("http://74.115.0.24:3128/getActiveEvent.php?google_ad_id=%s&provider=%s&exp=%d", googleAdId, adProvider, forLastNMinutes);
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(api);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            String result = IOUtils.toString(instream, "UTF-8");
            instream.close();
            return result.contains("\"found\":true");
        }else throw new AssertionError("Failed to Verify Callback");
    }

    //TODO remove once done testing
    public static boolean didCallbackHappen() throws IOException {
        String api = "http://74.115.0.24:3128/getActiveEvent.php?google_ad_id=2791cfb5-da1f-4d45-a03c-535e0496cb3e&provider=privacy&exp=30";
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(api);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            String result = IOUtils.toString(instream, "UTF-8");
            instream.close();
            return result.contains("\"found\":true");
        }else throw new AssertionError("Failed to Verify Callback");
    }
/*
    public static ArrayList<JSONObject> parseAdsJson() throws IOException, ParseException {
        JSONObject json = new Asset("ads.json").readFileAsJson();
        Object keys = json.keySet().toArray();
        System.out.println("Key Set: "+keys);
        ArrayList<JSONObject> list = new ArrayList<JSONObject>();

        for(Object key : keys){
            System.out.println("Key: " + key);
            String providerInfo = (String) json.get(key);
            System.out.println("Provider Info: " +providerInfo);
            int size = the_json_array.length();
            for (int i = 0; i < size; i++) {
                JSONObject another_json_object = the_json_array.getJSONObject(i);
                list.add(another_json_object);
            }
        }

        System.out.println(list);
        return list;
    }
    */
}
