package tekhne.com.cinemax;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by desarrollo on 23-01-18.
 */

public class GetPeliculas  extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        HttpURLConnection urlConnection = null;
        String website = "http://185.101.98.174:5000/";
        InputStream input  = null;
        JSONObject jsonresult = null;
        try {
            URL  urlserver  = new URL(website);
            Uri.Builder b   = new  Uri.Builder()
                    .appendQueryParameter("tag", "listapeliculas");
            HttpURLConnection conexcion = (HttpURLConnection) urlserver.openConnection();
            conexcion.setRequestMethod("GET");
            conexcion.setRequestProperty("Content-Type", "text/html");
            conexcion.setRequestProperty("Charset", "utf-8");
            conexcion.setUseCaches(false);
            DataOutputStream output  = new DataOutputStream(conexcion.getOutputStream());
            //output.write
            input = conexcion.getInputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("Urlexpextion", e.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
        return String.valueOf(input); // parserContenido(input);

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        Log.d("finish", "ok");
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    private String parserContenido(InputStream  input)  {

        StringBuilder builder  = new StringBuilder();

        try {
            BufferedReader bytes = new BufferedReader(
                    new InputStreamReader(input, "UTF-8"));
            String inicio ;
            while( (inicio = bytes.readLine())!=null){
                    builder.append(inicio);
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        return builder.toString();
    }

}
