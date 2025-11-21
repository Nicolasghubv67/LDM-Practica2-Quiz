package com.example.practica2;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private static final String PREFS_NAME = "quiz_prefs";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_MUSIC_ENABLED = "music_enabled";

    public static boolean isSoundEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_SOUND_ENABLED, true);
    }

    public static void setSoundEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply();
    }

    public static boolean isMusicEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_MUSIC_ENABLED, true);
    }

    public static void setMusicEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_MUSIC_ENABLED, enabled).apply();
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}
