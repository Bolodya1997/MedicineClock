package ru.nsu.fit.popov.medicineclock.clock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;

import ru.nsu.fit.popov.medicineclock.R;
import ru.nsu.fit.popov.medicineclock.databinding.ActivityMedicineClockBinding;
import ru.nsu.fit.popov.medicineclock.model.MedicineParcelAdapter;
import ru.nsu.fit.popov.medicineclock.model.db.Medicine;
import ru.nsu.fit.popov.medicineclock.util.Localiser;

public class MedicineClockActivity extends Activity {

    private static boolean active = false;
    private static final Object ACTIVE_LOCK = new Object();

    static void startActivity(Context context, Intent receiverIntent) {
        final Intent intent = new Intent(context, MedicineClockActivity.class);

        intent.putExtras(receiverIntent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        synchronized (ACTIVE_LOCK) {
            try {
                if (active)
                    ACTIVE_LOCK.wait();
                active = true;

                context.startActivity(intent);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private AsyncPlayer player = new AsyncPlayer("player");

    private Medicine medicine;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Localiser.setLocale(this);

        final ActivityMedicineClockBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_medicine_clock);

        final Bundle extras = getIntent().getExtras();

        count = extras.getInt(ClockReceiver.COUNT);

        final MedicineParcelAdapter adapter =
                (MedicineParcelAdapter) extras.get(ClockReceiver.MEDICINE);
        if (adapter == null)
            throw new RuntimeException("no medicine");
        medicine = adapter.getMedicine();

        binding.setMedicine(medicine);

        wakeUp();
        startPlayer();
    }

    private void wakeUp() {
        final PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock lock =
                manager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.FULL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP), "wake up");
        lock.acquire();
    }

    private void startPlayer() {
        final Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm);
        player.play(this, uri, true, AudioManager.STREAM_MUSIC);
    }

    public void onUseNow(View view) {
        Clock.update(this, medicine, count);
        finish();
    }

    public void onRemindMeLater(View view) {
        Clock.delay(this, medicine, count);
        finish();
    }

    @Override
    public void finish() {
        player.stop();

        synchronized (ACTIVE_LOCK) {
            active = false;
            ACTIVE_LOCK.notifyAll();

            super.finish();
        }
    }
}
