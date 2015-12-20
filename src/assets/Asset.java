package assets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

/**
 * Created by Artur Spirin on 12/1/15.
 **/
public class Asset {

    public Asset(String assetID){
        assetName = "src/assets/"+assetID;
    }

    private String assetName;

    public boolean firstLineContains(String value) throws IOException {
        File file = new File(assetName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        if((line = br.readLine()) != null) br.close();
        return line != null && line.contains(value);
    }

    public void setValue(String value) throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File(assetName);
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        writer.println(value);
        writer.close();
    }

    public JSONObject readFileAsJson() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(assetName));
        return (JSONObject) obj;
    }
}
