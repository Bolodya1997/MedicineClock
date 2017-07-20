package ru.nsu.fit.popov.medicineclock.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import java.util.Calendar;

import ru.nsu.fit.popov.medicineclock.model.db.Medicine;

final class Clock { //  TODO: add logging

    private static final int MINUTE_OFFSET = 10;

    private static void start(Context context, Medicine medicine, int count, Calendar time) {
        int id = (int) (long) medicine.getId();
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id,
                ClockReceiver.getIntent(context, medicine, count), 0);

        final AlarmManager manager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
    }

    static void start(Context context, Medicine medicine) {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, medicine.getStartTime() / 60);
        time.set(Calendar.MINUTE, medicine.getStartTime() % 60);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);

        if (time.getTimeInMillis() < System.currentTimeMillis())
            time.add(Calendar.DATE, 1);

        start(context, medicine, 1, time);
    }

    static void delay(Context context, Medicine medicine, int count) {
        Calendar time = Calendar.getInstance();
        time.add(Calendar.MINUTE, MINUTE_OFFSET);

        start(context, medicine, count, time);
    }

    static void update(Context context, Medicine medicine, int count) {
        if (count >= medicine.getCount()) {
            start(context, medicine);
            return;
        }

        Calendar time = Calendar.getInstance();
        time.add(Calendar.MINUTE, medicine.getDelay());

        start(context, medicine, count + 1, time);
    }

    static void stop(Context context, Medicine medicine) {
        int id = (int) (long) medicine.getId();
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id,
                ClockReceiver.getIntent(context, medicine, 0), 0);

        final AlarmManager manager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }
}
