package com.zheng.liuju.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageUtils {
    public static void setdefaultLanguage(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        config.locale = Locale.ENGLISH;
        context.getResources().updateConfiguration(config, metrics);

    }

}
