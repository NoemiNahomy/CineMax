package tekhne.com.cinemax.notification;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by desarrollo on 03-02-18.
 */

public class NotificacitionServListener  extends GcmListenerService {

    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        Log.d("Notificacion", s);

    }


}
