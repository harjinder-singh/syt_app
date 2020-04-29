package com.example.syt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import static com.example.syt.utils.PreferencesUtility.*;

/**
 * Created by anuragdhunna on 13/2/18.
 */

public class SaveSharedPreference {

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Set the Login Status
     * @param context
     * @param loggedIn
     */
    public static void setLoggedIn(Context context, boolean loggedIn, String token) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.putString(LOGGED_IN_TOKEN, token);
        editor.apply();
    }

    /**
     * Get the Login Status
     * @param context
     * @return boolean: login status
     */
    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }

    public static String getTokenStatus(Context context) {
        return getPreferences(context).getString(LOGGED_IN_TOKEN, "");
    }
}