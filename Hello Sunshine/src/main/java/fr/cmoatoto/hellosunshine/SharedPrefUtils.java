package fr.cmoatoto.hellosunshine;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.Set;

public class SharedPrefUtils {

    /**
     * Special separator to create String from List
     */
    private static final String SEPARATOR = "&sprt;";

    /**
     * The preferred sound volume.
     */
    private static String SOUND_VOLUME = "sound_volume";

    /**
     * The start on boot preference
     */
    private static String START_ON_BOOT = "start_on_boot";

    /**
     * Stock String preferences
     */
    private static void setPreference(Context c, String name, String value) {
        PreferenceManager.getDefaultSharedPreferences(c).edit().putString(name, value).apply();
    }

    /**
     * Stock Boolean preferences
     */
    private static void setPreference(Context c, String name, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(c).edit().putBoolean(name, value).apply();
    }

    /**
     * Stock Integer preferences
     */
    private static void setPreference(Context c, String name, int value) {
        PreferenceManager.getDefaultSharedPreferences(c).edit().putInt(name, value).apply();
    }

    /**
     * Stock Float preferences
     */
    private static void setPreference(Context c, String name, float value) {
        PreferenceManager.getDefaultSharedPreferences(c).edit().putFloat(name, value).apply();
    }

//    /**
//     * Stock Serializable preferences
//     */
//    private static void setPreference(Context c, String name, Serializable value, Type type) {
//        PreferenceManager.getDefaultSharedPreferences(c).edit().putString(name, new Gson().toJson(value, type)).commit();
//    }

    /**
     * Stock Set preferences
     */
    private static void setStringSetPreference(Context c, String name, Set<String> values) {
        PreferenceManager.getDefaultSharedPreferences(c).edit().putStringSet(name, values).apply();
    }

    /**
     * Return pref value as string, if not found, will return default
     */
    private static String getStringPreference(Context c, String name, String defaut) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(name, defaut);
    }

    /**
     * Return pref value as boolean, if not found, will return default
     */
    private static boolean getBooleanPreference(Context c, String name, boolean defaut) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(name, defaut);
    }

    /**
     * Return pref value as int, if not found, will return default
     */
    private static int getIntPreference(Context c, String name, int defaut) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(c).getInt(name, defaut);
        } catch (Throwable t) {
            return defaut;
        }
    }

    /**
     * Return pref value as float, if not found, will return default
     */
    private static float getFloatPreference(Context c, String name, float defaut) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(c).getFloat(name, defaut);
        } catch (Throwable t) {
            return defaut;
        }
    }

    /**
     * Return pref value as set of string, if not found, will return default
     */
    private static Set<String> getSetPreference(Context c, String name, Set<String> defaut) {
        return PreferenceManager.getDefaultSharedPreferences(c).getStringSet(name, defaut);
    }

//    /**
//     * Return pref value as Serializable object, if not found, will return default
//     */
//    private static Serializable getSerializablePreference(Context c, String name, Serializable defaut, Type type) {
//        String value = PreferenceManager.getDefaultSharedPreferences(c).getString(name, null);
//        if (value != null) {
//            return (Serializable) new Gson().fromJson(value, type);
//        } else {
//            return defaut;
//        }
//    }

    /**
     * Clear prefs for the specified key
     */
    private static void removePreference(Context c, String key) {
        PreferenceManager.getDefaultSharedPreferences(c).edit().remove(key).apply();
    }

    // And now the public getters and setters

    // The sound volume
    public static int getSoundVolume(Context c) {
        return getIntPreference(c, SOUND_VOLUME, -1);
    }

    public static void setSoundVolume(Context c, int value) {
        setPreference(c, SOUND_VOLUME, value);
    }

    // Is start on boot
    public static boolean isStartOnBoot(Context c) {
        return getBooleanPreference(c, START_ON_BOOT, false);
    }

    public static void setStartOnBoot(Context c, boolean value) {
        setPreference(c, START_ON_BOOT, value);
    }
}
