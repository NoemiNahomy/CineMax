package tekhne.com.cinemax.notification;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;


import java.io.IOException;

import tekhne.com.cinemax.R;

/**
 * Created by desarrollo on 03-02-18.
 */

public class NotificacionIntent  extends IntentService{


    private Context context;
    InstanceID id;
    public NotificacionIntent() {
        super("NotificacionIntent");
//         this.context = context;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

       // String id = (String)context.getResources().getString(R.string.idgcm);
         id  = InstanceID.getInstance(this);

        try {
            String token = id.getToken(getString(R.string.idgcm),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.d("NotificacionIntent", token);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //

         //getToken();
    }

    public String getToken() {
        String registro = "";
         try {
              registro = id.getToken(getString(R.string.idgcm),
                      GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
         }catch (Exception e){
             Log.d("error ",e.getMessage().toString());
         }
         return  registro;
    }
}
