package ru.nsu.fit.popov.medicineclock.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public final class Localiser {

    public static void setLocale(Activity activity) {
        final Resources resources = activity.getResources();
        final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        final Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale("ru");
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
