package tests.v400;

import assets.Asset;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utilities.TestInfo;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Artur Spirin on 12/7/15.
 **/
@RunWith(Parameterized.class)
public class AdsDynamic {

    @Parameterized.Parameter
    public JSONObject[] json;

    private static TestInfo testInfo = new TestInfo();

    @Parameterized.Parameters
    public static Collection data() throws IOException, ParseException {
        List<Object[]> data = new LinkedList<Object[]>();
        JSONObject json = new Asset("ads.json").readFileAsJson();
        Object[] keys = json.keySet().toArray();
        for(Object key:keys){
            JSONObject params = new JSONObject((Map) json.get(key));
            params.put("provider",key);
            data.add(new Object[]{new JSONObject[]{ params}});
        }
        return data;
    }

    @BeforeClass
    public static void initialization(){
        testInfo.suite("Ads").execType("criticClient");
    }

    @Test
    public void AC4_000() {
        testInfo.name("Auto-On OFF: Mobile Network - Chrome Launch").expected("Google Play opens and App installs").jira("DROID-1790");
        System.out.println("\nProvider: "+json[0].get("provider"));
        System.out.println("URL: "+json[0].get("url"));
        System.out.println("Expected Result: " + json[0].get("expected"));
        testInfo.actual("Remained disconnected");
    }

}
