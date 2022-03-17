package com.nexsoft.showcaseviewlib.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionGuide {
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    public static void beginInitialization(Context context) {
        pref = context.getSharedPreferences("MySessionGuide", 0); // 0 - for private mode
        editor = pref.edit();
    }

    public static void setSessionGlobal(String key, String val) {
        editor.putString(key, val);
        editor.commit();
    }

    public static void setSessionGlobalBoolean(String key, boolean val) {
        editor.putBoolean(key, val);
        editor.commit();
    }

    public static void setSessionNotLogoutBoolean(String key, boolean val) {
        editor.putBoolean(key, val);
        editor.commit();
    }

    public static boolean getSessionNotLogoutBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    public static String getSessionGlobal(String key) {
        return pref.getString(key, null);
    }

    public static boolean getSessionGlobalBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    public static void clearSessionGlobal(String key) {
        editor.remove(key);
        editor.commit();
    }

    public static void sessionClearEverything() {
        editor.clear();
        editor.commit();
    }
}
