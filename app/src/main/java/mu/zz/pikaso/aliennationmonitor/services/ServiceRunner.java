package mu.zz.pikaso.aliennationmonitor.services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

import mu.zz.pikaso.aliennationmonitor.settings.Settings;


public class ServiceRunner {
    private PendingIntent pendingIntent;
    private Activity activity;

    public ServiceRunner(Activity activity){
        this.activity = activity;
        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(activity, StatusServiceReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, 0);
    }

    public void execute(){
        AlarmManager manager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

        //Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.HOUR_OF_DAY, 10);
        //calendar.set(Calendar.MINUTE, 30);

        /* Repeating on every 1 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                Settings.interval, pendingIntent);

        Toast.makeText(activity, "NotificationService is ON!", Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        if(pendingIntent != null)
            pendingIntent.cancel();

        Toast.makeText(activity, "NotificationService is OFF!", Toast.LENGTH_SHORT).show();
    }

}
