package ru.nsu.fit.popov.medicineclock.util;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.view.View;
import android.widget.TimePicker;

public final class TimeAttrAdapter {

    @BindingAdapter("timeChanged")
    public static void setTimeListener(TimePicker view, final InverseBindingListener timeChanged) {
        if (timeChanged == null) {
            view.setOnTimeChangedListener(null);
        } else {
            view.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                    timeChanged.onChange();
                }
            });
        }
    }

    @BindingAdapter({"time"})
    public static void setTime(TimePicker view, int time) {
        view.setCurrentHour(time / 60);
        view.setCurrentMinute(time % 60);
    }

    @InverseBindingAdapter(attribute = "time", event = "timeChanged")
    public static int getTime(TimePicker view) {
        return view.getCurrentHour() * 60 + view.getCurrentMinute();
    }
}
