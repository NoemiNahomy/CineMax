package tekhne.com.cinemax;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import tekhne.com.cinemax.db.DaoMaster;
import tekhne.com.cinemax.db.DaoSession;

/**
 * Created by bzgroup on 1/23/18.
 */

public class App extends Application {

    public static final boolean ENCRYPTED = true;
    DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "cinemax.db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
