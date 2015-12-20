package managers;

import api.android.Android;
import api.hotspotshield.v400.DialogLocationSelector;
import api.hotspotshield.v400.ViewGeneralSettings;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import utilities.TestInfo;
import utilities.Utilities;
import utilities.constants.Credentials;
import utilities.constants.Endpoint;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Artur Spirin on 7/10/2015.
 **/

public class DatabaseManager {

    public static Connection connection;
    private static PreparedStatement statement;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String USERNAME = Credentials.databaseUsername;
    private static final String PASSWORD = Credentials.databasePassword;
    private static final String DB_HOST = Credentials.databaseHost;
    public static boolean
            runDebugRequest = false,
            takeMasterLogcat = false;
    //Statuses
    public static final String

            OPEN_STATUS = "OPEN",
            IN_PROGRESS_STATUS = "In Progress",
            COMPLETED_STATUS = "Completed",
            DISCONNECTED_STATUS = "Disconnected",
            INCORRECT_HOST_STATUS = "Incorrect Host";

    //All Info of the open request, some variables are preset for debugging
    public static String

            REQUEST_ID = "",
            STATUS = "",
            HOST = "",
            BRANCH = "",
            JIRA_TICKET = "",
            GROUP_ID = "",
            ATTEMPT = "",
            INSTALL = "",
            SVN = "",
            REQUEST_TYPE = "",
            TEST_SET = "",
            NOTES = "",
            PROTOCOL = "",
            IP_IN = "";

    public Connection Connect() throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(DB_HOST, USERNAME, PASSWORD);
            if(!connection.isClosed()){
                QAManager.log.debug("Connection to the Database established successfully!");
            }
            return connection;
        }
        catch (SQLException e){
            QAManager.log.error("Was NOT able to establish Connection with the Database!");
            e.printStackTrace();
            return null;
        }
    }

    public static void Disconnect() throws SQLException {

        if(!connection.isClosed()){
            connection.close();
            if(connection.isClosed()){
                QAManager.log.debug("Connection to Database closed successfully!");
            }
        }
    }

    protected static void setProgressNotification(int value) throws FileNotFoundException, UnsupportedEncodingException {
        if(value==1) QAManager.log.debug("Informing other builds that I'm checking sst requests");
        else if(value==0) QAManager.log.debug("Informing other builds that I'm done checking sst requests");
        File outputFile = new File("src/assets/requestController.txt");
        PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
        writer.println(value);
        writer.close();
    }

    protected static boolean inProgress() throws IOException {
        File file = new File("src/assets/requestController.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        if((line = br.readLine()) != null) br.close();
        return line != null && line.contains("1");
    }

    protected static void holdWhileInProgress() throws IOException {
        int ls = 30;
        while(ls != 0 && inProgress()){
            if(ls==30) QAManager.log.warn("Holding checking for sst requests, another build is in the process of checking sst requests");
            if(ls==10) QAManager.log.warn("Seems like another build is still checking sst requests, will hold for "+ls+" seconds longer");
            if(ls==5) QAManager.log.warn("Seems like another build is still checking sst requests, will hold for "+ls+" seconds longer");
            if(ls==1) QAManager.log.warn("Controller status did not update but I'm not going to wait any longer, proceeding on to checking sst requests");
            ls--;
            Utilities.Wait(1500);
        }
    }

    public static String[] getRequest() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {

        try {holdWhileInProgress();} catch (IOException e) {e.printStackTrace();}
        try {setProgressNotification(1);} catch (FileNotFoundException | UnsupportedEncodingException e) {e.printStackTrace();}
        QAManager.log.debug("Looking for open request...");
        String[] request = new String[12];
        if(!runDebugRequest){
            connection = new DatabaseManager().Connect();
            try {
                String sql = "SELECT id, status, try, par1, par2, groupid, notes, install, host, protocol, svn, _set " +
                        "FROM sst_request WHERE id IN (SELECT MIN(id) FROM sst_request WHERE TYPE= ? AND UCASE(status)= ?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, REQUEST_TYPE);
                statement.setString(2, "OPEN");
                ResultSet results = statement.executeQuery();

                boolean val = results.next();
                if (val) {
                    while (val) {
                        QAManager.log.debug("--------------Found Open Request-------------");
                        request[0] = REQUEST_ID  = Integer.toString(results.getInt("id"));  QAManager.log.debug("Request ID: " + REQUEST_ID);
                        request[1] = STATUS = results.getString("status");                  QAManager.log.debug("Request Status: " + STATUS);
                        request[6] = ATTEMPT = Integer.toString(results.getInt("try"));     QAManager.log.debug("Attempt: " + ATTEMPT);
                        request[2] = BRANCH = results.getString("par1");                    QAManager.log.debug("Request branch: " + BRANCH);
                        request[3] = JIRA_TICKET = results.getString("par2");               QAManager.log.debug("Jira Ticket: " + JIRA_TICKET);
                        request[4] = GROUP_ID = results.getString("groupid");               QAManager.log.debug("Group ID: " + GROUP_ID);
                        request[5] = NOTES = results.getString("notes");                    QAManager.log.debug("Notes: " + NOTES);
                        request[7] = INSTALL = results.getString("install");                QAManager.log.debug("Install: " + INSTALL);
                        request[8] = HOST = results.getString("host");                      QAManager.log.debug("Host: " + HOST);
                        request[9] = PROTOCOL = results.getString("protocol");              QAManager.log.debug("Protocol: " + PROTOCOL);
                        request[10] = SVN = results.getString("svn");                       QAManager.log.debug("SVN: " + SVN);
                        request[11] = TEST_SET = results.getString("_set");                 QAManager.log.debug("Test Set: " + TEST_SET);
                        QAManager.log.debug("---------------------------------------------");
                        val = results.next();
                    }
                    results.close();
                    Disconnect();
                    while(Utilities.isEmpty(IP_IN) && REQUEST_TYPE.equals("ANDROID_SRV")){
                        try{
                            IP_IN = resolveHostToIP(HOST);
                        }catch (NullPointerException e){
                            QAManager.log.error("Incorrect host passed in as a debug param: " + e.getMessage());
                            updateRequestStatus(REQUEST_ID, INCORRECT_HOST_STATUS);
                            QAManager.log.warn("Closed this request, will look for another one...");
                            try {setProgressNotification(0);} catch (FileNotFoundException | UnsupportedEncodingException er) {e.printStackTrace();}
                            getRequest();
                        }
                    }
                    try {setProgressNotification(0);} catch (FileNotFoundException | UnsupportedEncodingException e) {e.printStackTrace();}
                    return request;
                }
                else{
                    QAManager.log.debug("No Open Request found in the Database!");
                    try {setProgressNotification(0);} catch (FileNotFoundException | UnsupportedEncodingException e) {e.printStackTrace();}
                    System.exit(0);
                }
                statement.close();
            } catch (SQLException sqlExcept) {
                sqlExcept.printStackTrace();
            } finally {
                Disconnect();
                return request;
            }
        }else{
            setDebugRequestParams();
            return request;
        }
    }

    public static void updateRequestStatus(String id, String status) throws IOException {

        QAManager.log.debug("[POST] Updating Status of the Request: " + id + " to: " + status);
        String time = dateFormat.format(new java.util.Date());
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(Endpoint.SST_REQUEST_UPDATE);
        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("status", status));
        switch (status) {
            case IN_PROGRESS_STATUS: params.add(new BasicNameValuePair("test_started", time)); break;
            case DISCONNECTED_STATUS: params.add(new BasicNameValuePair("test_completed", time)); break;
            case COMPLETED_STATUS:
                params.add(new BasicNameValuePair("test_completed", time));
                DatabaseManager.callFinishRequestApi(REQUEST_ID);
                break;
        }
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            instream.close();
        }
        QAManager.log.debug("Done!");
    }

    public static void callFinishRequestApi(String id) throws IOException {

        QAManager.log.debug("[POST] Calling Finish Request API on Request ID: " + id);
        String time = dateFormat.format(new java.util.Date());
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(Endpoint.API);
        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("cmd", "finishRequest"));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            String result = IOUtils.toString(instream, "UTF-8");
            System.out.println(result);
            instream.close();
        }
        QAManager.log.debug("Done!");
    }

    public static String resolveHostToIP(String host) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        connection = new DatabaseManager().Connect();
        String query = String.format("SELECT INET_NTOA(ip) FROM automation.ithosts WHERE in_sd > 0 and host=\"%s\" ORDER BY RAND() LIMIT 1", host);
        statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        if(results.next()){
            IP_IN = results.getString("INET_NTOA(ip)");
            if(IP_IN.split("\\.").length == 4){
                QAManager.log.debug("Resolved Host: "+host+" to IP: " + IP_IN);
            }else{

                throw new NullPointerException("Host: "+host+" does not exist or does not have an IP available!");
            }
        }else{
            throw new NullPointerException("Was not able to resolve Host: "+host+" to an IP!");
        }
        Disconnect();
        return IP_IN;
    }

    public static String resolveIPToHost(String IP) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        connection = new DatabaseManager().Connect();
        String query = String.format("SELECT host FROM automation.ithosts WHERE IP = INET_ATON(\"%s\")", IP);
        statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        if(results.next()){
            HOST = results.getString("host");
            if(!Utilities.isEmpty(HOST)){
                QAManager.log.debug("Resolved IP: "+IP+" to Host: " + HOST);
            }else{
                throw new NullPointerException("IP: "+IP+" does not exist in DB or does not have an Host assigned!");
            }
        }else{
            throw new NullPointerException("IP: "+IP+" does not exist in DB or does not have an Host assigned!");
        }
        Disconnect();
        return HOST;
    }

    public static String getRandomHost() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        connection = new DatabaseManager().Connect();
        String query = "SELECT host FROM automation.ithosts WHERE in_sd > 0 ORDER BY RAND() LIMIT 1";
        statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        if(results.next()){
            HOST = results.getString("host");
            if(!Utilities.isEmpty(HOST)){
                QAManager.log.debug("Got random Host: " + HOST);
            }else{
                HOST="hss100";
                QAManager.log.debug("Was not able to get a host from DB. Will use default: "+HOST);
            }
        }else{
            HOST="hss100";
            QAManager.log.debug("Was not able to get a host from DB. Will use default: "+HOST);
        }
        Disconnect();
        return HOST;
    }

    public static String getRandomProtocol(){
        if(new Random().nextBoolean()) PROTOCOL = "HYDRA";
        else PROTOCOL = "OVPN";
        QAManager.log.debug("Got Random Protocol: " + PROTOCOL);
        return PROTOCOL;
    }

    public String getIPType(String IP) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {

        Connect();
        String type = null;
        String query = String.format("SELECT out_ip FROM automation.ithosts WHERE ip = INET_ATON('%s')", IP);
        statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        if(results.next()){
            int IPtype = results.getInt("out_ip");
            switch (IPtype) {
                case 7: type = "always + dns";  break;
                case 6: type = "dns";           break;
                case 5: type = "custom";        break;
                case 4: type = "non-http";      break;
                case 3: type = "non-elite";     break;
                case 2: type = "elite";         break;
                case 1: type = "never";         break;
                case 0: type = "always";        break;
                default:
                    if (isDirtyIP(IP)) {
                        type = "dirty";
                    } else type = null;
                    break;
            }
        }
        QAManager.log.debug("IP TYPE IS: " + type);
        Disconnect();
        return type;
    }

    public Boolean isDirtyIP(String IP) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {

        Connect();
        String query = String.format("SELECT id FROM automation.itservers WHERE ip = INET_ATON('%s')",IP);
        statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        String serverID = results.getString("id");
        Disconnect();
        return (serverID!=null);
    }

    public String[] checkAvailableIPTypes(String host) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        Connect();
        int serverID;
        String[] array = new String[9];
        int ls = 8;

        while(ls != 0) {
            ls--;
            String query = String.format("SELECT out_ip FROM automation.ithosts WHERE host='%s' and out_ip = '%d'", host, ls);
            statement = connection.prepareStatement(query);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                serverID = results.getInt("out_ip");
                if (serverID == ls) {
                    array[ls] = "true";
                } else {
                    array[ls] = "false";
                }
            }else{
                array[ls] = "false";
            }
        }
        QAManager.log.debug("-----------AVAILABLE TYPES DEBUG OUTPUT------------");
        QAManager.log.debug("ALWAYS & DNS AVAILABLE: " + array[7]);
        QAManager.log.debug("DNS AVAILABLE: " + array[6]);
        QAManager.log.debug("CUSTOM AVAILABLE: " + array[5]);
        QAManager.log.debug("NON-HTTP AVAILABLE: " + array[4]);
        QAManager.log.debug("NON-ELITE AVAILABLE: " + array[3]);
        QAManager.log.debug("ELITE AVAILABLE: " + array[2]);
        QAManager.log.debug("NEVER AVAILABLE: " + array[1]);
        QAManager.log.debug("ALWAYS AVAILABLE: " + array[0]);
        Disconnect();
        return array;
    }

    public static void requestEmailNotification(String id) throws MalformedURLException {

        try {
            URL php = new URL("http://10.0.0.148/send_req_status.php?id=" + id);
            HttpURLConnection conn = (HttpURLConnection) php.openConnection();
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream is = conn.getInputStream();
                QAManager.log.debug("Successfully requested E-Mail status update!");
            }
        }catch (IOException e){
            QAManager.log.warn("Failed to request E-Mail status update!");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void addTestResult() {

        QAManager.log.debug("[POST] Adding Test Result to Request ID: "+REQUEST_ID+"...");
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(Endpoint.INSERT_TEST_RESULT);
            post.setHeader(HTTP.CONTENT_TYPE, "application/json");
            JSONObject json = new JSONObject();
                json.put("request_id", REQUEST_ID);
                json.put("exec_type", TestInfo.execType());
                json.put("test_name", TestInfo.name());
                json.put("platform", "Android");
                json.put("result", TestInfo.status());
                json.put("notes", TestInfo.notes());
                json.put("colo", DialogLocationSelector.CURRENT_COLO);
                json.put("test_link_id", TestInfo.id());
                json.put("test_suite", TestInfo.suite());
                json.put("agent", Android.getDeviceModel()+" ("+Android.getDeviceModelID()+")");
                json.put("host", HOST);
                json.put("svn", SVN);
                json.put("ip_in", IP_IN);
                json.put("protocol", ViewGeneralSettings.LAST_VPN_MODE);
                json.put("jira", TestInfo.jira());
                json.put("tray_version", Android.getAppVersion());
                json.put("info", TestInfo.info());
                json.put("start_time", TestInfo.startTime());
                json.put("end_time", Utilities.DATE_FORMAT.format(new java.util.Date()));
                json.put("expected_result", TestInfo.expected());
                json.put("actual_result", TestInfo.actual());
            StringEntity entity = new StringEntity(json.toString()); QAManager.log.debug("JSON Entity: " + EntityUtils.toString(entity));
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            String status = EntityUtils.toString(response.getEntity());
            QAManager.log.error("Response: " + status);
        }
        catch(IOException e) {e.printStackTrace();}

        QAManager.log.debug("Done!");
    }

    private static void setDebugRequestParams(){
        REQUEST_ID = "17055";
        STATUS = "OPEN";
        HOST = "";
        BRANCH = "";
        JIRA_TICKET = "";
        GROUP_ID = "";
        ATTEMPT = "";
        INSTALL = "";
        SVN = "";
        REQUEST_TYPE = "ANDROID_FUN";
        TEST_SET = "";
        NOTES = "";
        PROTOCOL = "";
        IP_IN = "";
    }

    public static void setContinuesServerSmokeParams() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        QAManager.log.debug("Setting Params for Continues Server Smoke Check");
        REQUEST_ID = "1";
        STATUS = "OPEN";
        HOST = DatabaseManager.getRandomHost();
        BRANCH = "";
        JIRA_TICKET = "DROID-1787";
        GROUP_ID = "";
        ATTEMPT = "";
        INSTALL = "";
        SVN = Utilities.DATE_FORMAT.format(new java.util.Date());
        REQUEST_TYPE = "ANDROID_SRV_SMOKE";
        TEST_SET = "serverCritic";
        NOTES = "Continues Server Smoke";
        PROTOCOL = DatabaseManager.getRandomProtocol();
        IP_IN = DatabaseManager.resolveHostToIP(DatabaseManager.getRandomHost());
        QAManager.log.debug("Done");
    }

    public static void setContinuesFunctionalSmokeParams() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        QAManager.log.debug("Setting Params for Continues Functional Smoke Check");
        REQUEST_ID = "2";
        STATUS = "OPEN";
        HOST = DatabaseManager.getRandomHost();
        BRANCH = "";
        JIRA_TICKET = "DROID-1919";
        GROUP_ID = "";
        ATTEMPT = "";
        INSTALL = "";
        SVN = Utilities.DATE_FORMAT.format(new java.util.Date());
        REQUEST_TYPE = "ANDROID_FUN_SMOKE";
        TEST_SET = "clientCritic";
        NOTES = "Continues Server Smoke";
        PROTOCOL = DatabaseManager.getRandomProtocol();
        IP_IN = DatabaseManager.resolveHostToIP(DatabaseManager.getRandomHost());
        QAManager.log.debug("Done");
    }
}