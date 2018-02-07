package tekhne.com.cinemax.notification;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by desarrollo on 03-02-18.
 */

public class NotificationListener extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Intent  notificacion =  new Intent(this,NotificacionIntent.class);
        startService(notificacion);
    }
}
