package ru.nsu.fit.popov.medicineclock.util;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public final class CountAttrAdapter {

    @BindingAdapter("countChanged")
    public static void setCountListener(EditText view, final InverseBindingListener countChanged) {
        if (countChanged == null) {
            view.addTextChangedListener(null);
        } else {
            view.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    countChanged.onChange();
                }
            });
        }
    }

    @BindingAdapter({"count"})
    public static void setCount(EditText view, int count) {
        view.setText(String.valueOf(count));
    }

    @InverseBindingAdapter(attribute = "count", event = "countChanged")
    public static int getCount(EditText view) {
        String text = view.getText().toString();
        if (text.isEmpty())
            return 0;

        return Integer.decode(text);
    }
}
