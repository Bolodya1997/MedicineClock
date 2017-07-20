package ru.nsu.fit.popov.medicineclock.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import ru.nsu.fit.popov.medicineclock.R;
import ru.nsu.fit.popov.medicineclock.databinding.ActivityMedicineBinding;
import ru.nsu.fit.popov.medicineclock.model.DBController;
import ru.nsu.fit.popov.medicineclock.model.db.DaoSession;
import ru.nsu.fit.popov.medicineclock.model.db.Medicine;
import ru.nsu.fit.popov.medicineclock.util.Localiser;
import ru.nsu.fit.popov.medicineclock.model.MedicineParcelAdapter;

public class MedicineActivity extends Activity {

    static final String MEDICINE = "medicine";

    private Medicine medicine;

    //  TODO: add saving time on focus changing (lost data on 'keyboard input in TimePicker -> OK button')

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Localiser.setLocale(this);

        final ActivityMedicineBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_medicine);

        final Bundle extras = getIntent().getExtras();
        final MedicineParcelAdapter adapter = (MedicineParcelAdapter) extras.get(MEDICINE);
        if (adapter == null)
            throw new RuntimeException("no medicine");

        medicine = adapter.getMedicine();
        binding.setMedicine(medicine);

        TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        timePicker = findViewById(R.id.timePicker2);
        timePicker.setIs24HourView(true);
    }

    public void onCancel(View view) {
        onBackPressed();
    }

    public void onOK(View view) {
        view.requestFocus();

        medicine.setActive(true);

        Localiser.setLocale(this);

        final DaoSession session = DBController.getInstance(this).getSession();
        try {
            check();
            if (medicine.getId() == null)
                session.insert(medicine);
            else
                session.update(medicine);
        } catch (Exception e) { //  TODO: edit notification on DB error
            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setCustomTitle(null);
            dialog.setMessage(e.getMessage());
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                    (DialogInterface.OnClickListener) null);
            dialog.show();

            return;
        }

        onBackPressed();
    }

    private void check() {
        if (medicine.getName() == null || medicine.getName().isEmpty())
            throw new RuntimeException(getString(R.string.empty_name));

        if (medicine.getDelay() == 0)
            throw new RuntimeException(getString(R.string.zero_delay));

        if (medicine.getCount() <= 0)
            throw new RuntimeException(getString(R.string.zero_count));
    }
}
