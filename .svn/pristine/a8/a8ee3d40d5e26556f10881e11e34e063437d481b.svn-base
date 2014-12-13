package com.ksu.team.temperatureTrackingApp;

import android.util.Log;

/**
 * Created by Marcel on 24-Nov-14.
 */
public class RefreshData implements Runnable {
    TemperatureActivity activity = null;

    public RefreshData(TemperatureActivity activity) {
        this.activity = activity;

    }

    public void run() {

        try {
            Thread.sleep(15000);
            Log.i("ThreadTag", "refreshing data ");

            JsonTask task=new JsonTask(activity,activity.username,activity.password);
            task.execute(TemperatureActivity.path);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
