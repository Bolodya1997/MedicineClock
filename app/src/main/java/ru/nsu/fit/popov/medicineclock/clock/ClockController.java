package ru.nsu.fit.popov.medicineclock.clock;

import android.app.Activity;

import java.util.List;

import ru.nsu.fit.popov.medicineclock.model.DBController;
import ru.nsu.fit.popov.medicineclock.model.DataLoader;
import ru.nsu.fit.popov.medicineclock.model.db.Medicine;
import ru.nsu.fit.popov.medicineclock.model.db.SysDate;

public final class ClockController {

    public interface Callback {

        void apply(SysDate sysDate);
    }

    public static void startClock(final Activity activity, final Callback callback) {
        new DataLoader(activity, false, new DataLoader.Callback() {
            @Override
            public void update(List<Medicine> medicines, final SysDate sysDate) {
                for (Medicine medicine : medicines) {
                    if (!medicine.getActive())
                        continue;
                    Clock.start(activity, medicine);
                }

                sysDate.setActive(true);
                DBController.getInstance(activity).getSession().update(sysDate);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.apply(sysDate);
                    }
                });
            }
        }).start();
    }

    public static void stopClock(final Activity activity, final Callback callback) {
        new DataLoader(activity, false, new DataLoader.Callback() {
            @Override
            public void update(List<Medicine> medicines, final SysDate sysDate) {
                for (Medicine medicine : medicines) {
                    if (!medicine.getActive())
                        continue;
                    Clock.stop(activity, medicine);
                }

                sysDate.setActive(false);
                DBController.getInstance(activity).getSession().update(sysDate);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.apply(sysDate);
                    }
                });
            }
        }).start();
    }
}
