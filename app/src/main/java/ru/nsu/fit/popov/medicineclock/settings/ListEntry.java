package ru.nsu.fit.popov.medicineclock.settings;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import ru.nsu.fit.popov.medicineclock.R;
import ru.nsu.fit.popov.medicineclock.model.DBController;
import ru.nsu.fit.popov.medicineclock.model.db.Medicine;

class ListEntry extends RelativeLayout {

    interface EntryListener {

        void onRemove(ListEntry entry, Medicine medicine);
        void onSettings(Medicine medicine);
    }

    ListEntry(final Context context, final Medicine medicine,
              final EntryListener entryListener) {
        super(context);

//        ------   checked   ------
        final CheckBox checkBox = new CheckBox(context);
        checkBox.setText(medicine.getName());
        checkBox.setChecked(medicine.getActive());
        checkBox.setTextSize(30f);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                medicine.setActive(b);
                DBController.getInstance(context).getSession().update(medicine);
            }
        });

        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(CENTER_VERTICAL);
        params.addRule(ALIGN_PARENT_LEFT);

        addView(checkBox, params);

//        ------   remove   ------
        final ImageButton removeButton = new ImageButton(context);
        removeButton.setId(1 + 0);
        removeButton.setImageResource(R.drawable.list_remove);
        removeButton.setBackgroundColor(getSolidColor());
        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                entryListener.onRemove(ListEntry.this, medicine);
            }
        });

        params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(CENTER_VERTICAL);
        params.addRule(ALIGN_PARENT_RIGHT);

        addView(removeButton, params);

//        ------   settings   ------
        final ImageButton settingsButton = new ImageButton(context);
        settingsButton.setImageResource(R.drawable.list_settings);
        settingsButton.setBackgroundColor(getSolidColor());
        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                entryListener.onSettings(medicine);
            }
        });

        params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(CENTER_VERTICAL);
        params.addRule(LEFT_OF, removeButton.getId());

        addView(settingsButton, params);
    }
}
