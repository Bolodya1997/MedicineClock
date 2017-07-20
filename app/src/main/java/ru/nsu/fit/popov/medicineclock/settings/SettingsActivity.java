package ru.nsu.fit.popov.medicineclock.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import android.widget.ViewAnimator;

import java.util.List;

import ru.nsu.fit.popov.medicineclock.R;
import ru.nsu.fit.popov.medicineclock.clock.ClockController;
import ru.nsu.fit.popov.medicineclock.databinding.ActivitySettingsBinding;
import ru.nsu.fit.popov.medicineclock.model.DBController;
import ru.nsu.fit.popov.medicineclock.model.DataLoader;
import ru.nsu.fit.popov.medicineclock.model.db.Medicine;
import ru.nsu.fit.popov.medicineclock.model.db.SysDate;
import ru.nsu.fit.popov.medicineclock.util.Localiser;
import ru.nsu.fit.popov.medicineclock.model.MedicineParcelAdapter;

public class SettingsActivity extends Activity {

    private ObservableBoolean loaded = new ObservableBoolean(false);
    private ObservableBoolean active = new ObservableBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Localiser.setLocale(this);

        final ActivitySettingsBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_settings);
        binding.setLoaded(loaded);
        binding.setActive(active);

        onWaiting();
        loadData();
    }

    private void onWaiting() {
        final ViewAnimator animator = findViewById(R.id.settings_animator);
        animator.setDisplayedChild(0);

        loaded.set(false);
        active.set(false);
    }

    private void loadData() {
        final DataLoader loader = new DataLoader(this, true,
                new DataLoader.Callback() {
                    @Override
                    public void update(List<Medicine> medicines, SysDate system) {
                        updateList(medicines);
                        updateView(system);
                    }
                });
        loader.start();
    }

    private void updateList(List<Medicine> medicines) {
        final LinearLayout list = findViewById(R.id.settings_list);
        list.removeAllViews();
        for (Medicine medicine : medicines) {
            list.addView(new ListEntry(this, medicine, new ListEntry.EntryListener() {
                @Override
                public void onRemove(ListEntry entry, Medicine medicine) {
                    removeMedicine(entry, medicine);
                }

                @Override
                public void onSettings(Medicine medicine) {
                    startMedicineActivity(medicine);
                }
            }));
        }
    }

    private void updateView(SysDate sysDate) {
        loaded.set(true);
        active.set(sysDate.getActive());

        final ViewAnimator animator = findViewById(R.id.settings_animator);
        animator.showNext();
    }

    private void startMedicineActivity(Medicine medicine) {
        final Intent intent = new Intent(this, MedicineActivity.class);
        intent.putExtra(MedicineActivity.MEDICINE, new MedicineParcelAdapter(medicine));

        startActivity(intent);
    }

    private void removeMedicine(final ListEntry entry, final Medicine medicine) {
        Localiser.setLocale(this);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(getString(R.string.delete) + " " + medicine.getName() + "?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((LinearLayout) findViewById(R.id.settings_list)).removeView(entry);
                        DBController.getInstance(SettingsActivity.this).getSession()
                                .delete(medicine);
                    }
                });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.no),
                (DialogInterface.OnClickListener) null);

        alertDialog.show();
    }

    public void addMedicine(View view) {
        startMedicineActivity(new Medicine());
    }

    public void startStopAction(View view) {
        onWaiting();

        final ClockController.Callback callback = new ClockController.Callback() {
            @Override
            public void apply(SysDate system) {
                updateView(system);
            }
        };

        final ToggleButton button = (ToggleButton) view;
        if (button.isChecked()) {   //  start
            ClockController.startClock(this, callback);
        } else {    // stop
            ClockController.stopClock(this, callback);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        onWaiting();
        loadData();
    }
}
