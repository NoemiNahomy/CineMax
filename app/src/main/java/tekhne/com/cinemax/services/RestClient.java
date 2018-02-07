package tekhne.com.cinemax.services;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.client.params.ClientPNames;

/**
 * Created by desarrollo on 27-01-18.
 */

public class RestClient {

private static AsyncHttpClient client = new  AsyncHttpClient();


public   static void setTimeOut(){
    client.setTimeout(10000);
}

public   static void  Redirecs(){
    client.getHttpClient().getParams()
            .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

}

public static void get(String BASE_URL, String metodo,
                        RequestParams parametros, AsyncHttpResponseHandler handler){


    client.get(BASE_URL+metodo, parametros, handler);
}

public   static void post(Context context, String BASE_URL,String metodo,
                   RequestParams parametros, AsyncHttpResponseHandler handler ){
    client.post(context,BASE_URL+metodo, parametros,handler);
}


}


