package ru.nsu.fit.popov.medicineclock.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ru.nsu.fit.popov.medicineclock.model.db.DaoMaster;
import ru.nsu.fit.popov.medicineclock.model.db.DaoMaster.DevOpenHelper;
import ru.nsu.fit.popov.medicineclock.model.db.DaoSession;
import ru.nsu.fit.popov.medicineclock.model.db.SysDate;
import ru.nsu.fit.popov.medicineclock.model.db.SysDateDao;

public class DBController {

    private static DBController INSTANCE;

    public static synchronized DBController getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new DBController(context);
        return INSTANCE;
    }

    private DaoSession session;

    private DBController(Context context) {
        final DevOpenHelper helper = new DevOpenHelper(context, "MedicineClock.db");
        final SQLiteDatabase db = helper.getWritableDatabase();
        session = new DaoMaster(db).newSession();

        onCreate();
    }

    private void onCreate() {
        final SysDateDao sysDateDao = session.getSysDateDao();
        if (sysDateDao.count() == 0)
            sysDateDao.insert(new SysDate(null, false));
    }

    public DaoSession getSession() {
        return session;
    }
}
