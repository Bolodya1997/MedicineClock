package ru.nsu.fit.popov.medicineclock.util;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;

public final class RecursiveEnabledAttrAdapter {

    @BindingAdapter("recursiveEnabled")
    public static void setRecursiveEnabled(ViewGroup viewGroup, boolean enabled) {
        final int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup)
                setRecursiveEnabled((ViewGroup) view, enabled);
            else
                view.setEnabled(enabled);
        }
    }
}
