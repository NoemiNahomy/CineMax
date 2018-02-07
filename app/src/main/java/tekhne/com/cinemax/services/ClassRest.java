package tekhne.com.cinemax.services;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by desarrollo on 27-01-18.
 */

public abstract class ClassRest extends AsyncHttpResponseHandler {

    protected Context context;
    private boolean finalizo;
    private boolean exitoso;


    public ClassRest(Context context) {
        this.context = context;
    }

    public boolean isFinalizo() {
        return finalizo;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    protected  abstract   String getBaseUrl();
    protected  abstract String getMethod();

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        exitoso = true;

    }

    @Override
    public void onFinish() {
        finalizo = true;
        super.onFinish();
    }

    protected  void getMethodWithParamenters(AsyncHttpResponseHandler handler, RequestParams parametros){

        RestClient.Redirecs();
        RestClient.setTimeOut();
        RestClient.get(getBaseUrl(),getMethod(),parametros,handler);

    }

    protected  void  postMethodWithParameters(AsyncHttpResponseHandler handler, RequestParams parametros){
        RestClient.Redirecs();
        RestClient.setTimeOut();
        RestClient.post(context,getBaseUrl(),getMethod(),parametros,handler);

    }

}
