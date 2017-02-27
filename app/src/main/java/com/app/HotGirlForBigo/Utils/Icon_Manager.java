package com.app.hotgirlforbigo.Utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

public class Icon_Manager {
    private static Hashtable<String, Typeface> cached_icon = new Hashtable<>();

    public static Typeface get_icons(String path, Context context) {
        Typeface icons = cached_icon.get(path);
        if (icons == null) {
            icons = Typeface.createFromAsset(context.getAssets(), path);
            cached_icon.put(path, icons);
        }
        return icons;
    }
}
