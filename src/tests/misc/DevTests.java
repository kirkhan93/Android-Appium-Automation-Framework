package tests.misc;

import api.hotspotshield.v400.ViewHome;
import assets.Asset;
import managers.DatabaseManager;
import managers.DeviceManager;
import managers.QAManager;
import org.apache.log4j.Level;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Artur Spirin on 11/10/15.
 **/
//@RunWith(Parameterized.class)
public class DevTests {

    private static final boolean getDriverFirst = false;

    @Parameterized.Parameter
    public JSONObject[] json;

    @AfterClass
    public static void tearDown(){
        if(getDriverFirst) DeviceManager.getDriver().quit();
    }

    @BeforeClass
    public static void setUp(){
        QAManager.log.setLevel(Level.ALL);
        if(getDriverFirst) DeviceManager.getDriver();
    }

    @Parameterized.Parameters
    public static Collection data() throws IOException, ParseException {
        QAManager.log.setLevel(Level.ALL);
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

    @Test
    public void testAds() throws IOException, ParseException, InterruptedException {
        QAManager.log.setLevel(Level.ALL);
        long timestamp = new Date().getSeconds();
        new ViewHome().broski(timestamp, "shit happen", 10);
    }

    @Test
    public void resolveHostToIp() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String HOST = "hss255";
        DatabaseManager.resolveHostToIP(HOST);
    }

    @Test
    public void resolveIpToHost() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String IP = "173.245.66.79";
        DatabaseManager.resolveIPToHost(IP);
    }
}