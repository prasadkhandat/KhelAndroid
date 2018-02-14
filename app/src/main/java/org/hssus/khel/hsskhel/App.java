package org.hssus.khel.hsskhel;

import android.app.Application;

import org.hssus.khel.hsskhel.util.SharedPreferenceManager;

/**
 * Created by prasadkhandat on 2/11/18.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferenceManager.getInstance().init(this);
    }

}