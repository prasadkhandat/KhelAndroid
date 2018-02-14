package org.hssus.khel.hsskhel.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by prasadkhandat on 2/11/18.
 */

public class SharedPreferenceManager {
    private static final String TAG = SharedPreferenceManager.class.getName();
    static final String SESSION = "khel_session";
    //private static final SharedPreferenceManager ourInstance = new SharedPreferenceManager();
    private SharedPreferences sessionPreferences;

    private static volatile SharedPreferenceManager sInstance;

    public static SharedPreferenceManager getInstance() {

        if (null == sInstance) {
            synchronized (SharedPreferenceManager.class) {
                if (sInstance == null) {
                    sInstance = new SharedPreferenceManager();
                }
            }
        }

        return sInstance;
    }

    private SharedPreferenceManager() {
    }

    public void init(Context context) {

        sessionPreferences = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE);
    }

    public void remove(String key) {
        (sessionPreferences.edit()).remove(key).apply();
    }

    public void putString(String key, String value) {
        if (sessionPreferences != null)
            (sessionPreferences.edit()).putString(key, value).apply();
    }

    public String getString(String key, String defaultVal) {
        if (sessionPreferences != null) {
            return sessionPreferences.getString(key, defaultVal);
        }
        return defaultVal;
    }
}
