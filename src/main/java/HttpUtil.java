import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

/**
 * @author TiltdBastion
 */
public class HttpUtil {

    public static String get(String addr){
        try {
            URL url = new URL(addr);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("GET");
            if(httpCon.getResponseCode() == 200) {
                return new BufferedReader(new InputStreamReader(httpCon.getInputStream())).lines().collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void put(String addr, String data){
        try {
            URL url = new URL(addr);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(data);
            out.close();
            httpCon.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String post(String addr, String data){
        try {

            URL url = new URL(addr);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(data);
            out.close();
            if(httpCon.getResponseCode() == 200) {
                return new BufferedReader(new InputStreamReader(httpCon.getInputStream())).lines().collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
