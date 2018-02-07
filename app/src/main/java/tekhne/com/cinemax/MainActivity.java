package tekhne.com.cinemax;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import tekhne.com.cinemax.db.Cine;
import tekhne.com.cinemax.db.CineDao;
import tekhne.com.cinemax.db.DaoSession;
import tekhne.com.cinemax.notification.NotificacionIntent;
import tekhne.com.cinemax.services.CineService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CineDao cineDao;
    private CineService servicioCine;
    private ListView listViewCines;
    private CineAdaptador adaptador;
    private List<Cine> listaCines;
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


        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        cineDao = daoSession.getCineDao();
       // addCine();

        Intent i = new Intent(this, NotificacionIntent.class);
        startService(i);
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
      //  GetPeliculas peliculas = new GetPeliculas();
      //  peliculas.execute(new Cine());

        servicioCine = new CineService(this) {
            @Override
            public void onSucessCine(JSONObject cine) {
                Log.d("respuesta",cine.toString());


                Cine cinema = new Cine(cine);

            }

            @Override
            public void onSucessArrayCine(JSONArray listacines) {
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
                adaptador = new CineAdaptador(getBaseContext(), listaCines);
                listViewCines.setAdapter(adaptador);

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
}
