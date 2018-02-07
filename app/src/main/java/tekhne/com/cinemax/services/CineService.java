package tekhne.com.cinemax.services;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by desarrollo on 27-01-18.
 */

public abstract class CineService extends ClassRest {




    public CineService(Context context) {
        super(context);
    }

    @Override
    protected String getBaseUrl() {
        return "http://185.101.98.174:5000/";
    }

    @Override
    protected String getMethod() {
        return "listacines";
    }

    public  void getCines(){
        getMethodWithParamenters(this,null);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        super.onSuccess(statusCode, headers, responseBody);
        try {

            String  respuesta = new String(responseBody);


           // JSONObject jsonObject  = new JSONObject(respuesta);
            JSONArray arraycines = new JSONArray(respuesta);


            //onSucessCine(jsonObject);

            onSucessArrayCine(arraycines);
        }catch (Exception e){
            Log.d("cinemax-service-cine", e.getMessage());
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }


     public  abstract  void onSucessCine(JSONObject cine);
     public  abstract  void onSucessArrayCine(JSONArray listacines);




}
