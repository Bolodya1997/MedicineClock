package ru.nsu.fit.popov.medicineclock.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.nsu.fit.popov.medicineclock.model.MedicineParcelAdapter;
import ru.nsu.fit.popov.medicineclock.model.db.Medicine;

public class ClockReceiver extends BroadcastReceiver {

    static final String MEDICINE = "medicine";
    static final String COUNT = "count";

    static Intent getIntent(Context context, Medicine medicine, int count) {
        final Intent intent = new Intent(context, ClockReceiver.class);
        intent.putExtra(MEDICINE, new MedicineParcelAdapter(medicine));
        intent.putExtra(COUNT, count);

        return intent;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MedicineClockActivity.startActivity(context, intent);
            }
        }).start();
    }
}
