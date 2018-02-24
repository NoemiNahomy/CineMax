package tekhne.com.cinemax;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import tekhne.com.cinemax.db.Cine;
import tekhne.com.cinemax.db.CineDao;
import tekhne.com.cinemax.db.DaoSession;
import tekhne.com.cinemax.notification.Config;
import tekhne.com.cinemax.notification.NotificationUtils;
import tekhne.com.cinemax.services.CineService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private CineDao cineDao;
    private CineService servicioCine;
    private ListView listViewCines;
    private CineAdaptador adaptador;
    private List<Cine> listaCines;
    private AdView mAdView;


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        listViewCines = (ListView) findViewById(R.id.listview_cine);



        setupSesionCine();



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                  ///  txtMessage.setText(message);
                    Toast.makeText(getApplicationContext(), message.toString(),Toast.LENGTH_LONG).show();
                }
            }
        };



        MobileAds.initialize(this, "ca-app-pub-8002948765991814~9277717666");

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-8002948765991814/3642247604");


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("cinemax", "Firebase reg id: " + regId);


    }




    private void setupSesionCine(){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        cineDao = daoSession.getCineDao();
    }


    private void addCine() {

        Cine cine = new Cine();
        cine.setNombre("Cine Center");
        cine.setDireccion("2do anillo");
        cineDao.insert(cine);
        cineDao.loadAll();
        Log.d("cinemax", "Inserted new cine, ID: " + cine.getId());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());





      //  GetPeliculas peliculas = new GetPeliculas();
      //  peliculas.execute(new Cine());

        servicioCine = new CineService(this) {

            ProgressDialog progress  = new ProgressDialog(context);



            @Override
            public void onSucessCine(JSONObject cine) {
                Log.d("respuesta",cine.toString());
                Cine cinema = new Cine(cine);


            }

            @Override
            public void onSucessArrayCine(JSONArray listacines) {

                progress.setMessage("Actualizando, tomese un cafe..");
                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progress.setIndeterminate(true);
                progress.setProgress(0);



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progress.show();
                       // startActivity(new Intent(getBaseContext(), MainActivity.class));
                    }
                }, 10000);




                Log.d("respuesta-array",listacines.toString());
                try {
                    JSONObject object = listacines.getJSONObject(0);

                    for (int i = 0; i < listacines.length() ; i++) {

                        JSONObject objectC = listacines.getJSONObject(i);
                        Cine cine = new Cine(objectC);
                        //cineDao.deleteAll();
                        cineDao.insertOrReplace(cine);
                    }
                    Log.d("cines en la bd", ""+cineDao.count());
//                     if (object.has("Error")){
//                         Toast.makeText(getBaseContext(), "Error en el servidor", Toast.LENGTH_LONG).show();
//                     }else{
//
//
//                     }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Error",error.getMessage());
            }

            @Override
            public void onFinish() {
                super.onFinish();

                listaCines = cineDao.loadAll();
                progress.dismiss();

                adaptador = new CineAdaptador(getBaseContext(), listaCines);
                listViewCines.setAdapter(adaptador);
               listViewCines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       startActivity(new Intent(context, PeliculasActivity.class));
                   }
               });
                //Toast.makeText(getBaseContext(), "Finalizo Correctamente", Toast.LENGTH_LONG).show();
            }
        };
        servicioCine.getCines();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}
