package ru.nsu.fit.popov.medicineclock.model;

import android.app.Activity;

import java.util.List;

import ru.nsu.fit.popov.medicineclock.model.db.DaoSession;
import ru.nsu.fit.popov.medicineclock.model.db.Medicine;
import ru.nsu.fit.popov.medicineclock.model.db.MedicineDao;
import ru.nsu.fit.popov.medicineclock.model.db.SysDate;
import ru.nsu.fit.popov.medicineclock.model.db.SysDateDao;

public class DataLoader {

    public interface Callback {

        void update(List<Medicine> medicines, SysDate sysDate);
    }

    private Activity activity;
    private boolean runOnUIThread;
    private Callback callback;

    public DataLoader(Activity activity, boolean runOnUIThread, Callback callback) {
        this.activity = activity;
        this.runOnUIThread = runOnUIThread;
        this.callback = callback;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }).start();
    }

    private void loadData() {
        final DaoSession session = DBController.getInstance(activity).getSession();
        final MedicineDao medicineDao = session.getMedicineDao();
        final SysDateDao sysDateDao = session.getSysDateDao();

        final List<Medicine> entries = medicineDao.loadAll();
        final SysDate sysDate = sysDateDao.loadAll().get(0);

//        ------   end of loading   ------
        if (runOnUIThread) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callback.update(entries, sysDate);
                }
            });
        } else {
            callback.update(entries, sysDate);
        }
    }
}
