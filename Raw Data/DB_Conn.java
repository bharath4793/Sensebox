package project.graduational.sensebox;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static android.app.PendingIntent.getActivity;

/**
 * Created by Manos on 8/7/2014.
 */
public class DB_Conn{

    static InputStream is = null;
    static String json = "";
    static JSONObject jObj = null;


    public DB_Conn() throws IOException {
        URL url = new URL("http://192.168.1.5/conf.php");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(new HttpPost("http://192.168.1.5/request.php"));
        HttpEntity httpEntity = response.getEntity();
        is = httpEntity.getContent();
        System.out.println("-----> " + response.toString());
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            System.out.println("--> " + sb.toString());
            json = sb.toString();

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }


        // return JSON String
       // return jObj;


    }
}
