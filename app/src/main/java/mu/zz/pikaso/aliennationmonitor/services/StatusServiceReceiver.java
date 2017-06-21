package mu.zz.pikaso.aliennationmonitor.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.Date;


public class StatusServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Make job
        if(isNetworkAvailable(context)) {
            NotificationScenario scenario = new NotificationScenario();
            scenario.scenario(context);
        }
        //Log timing
        Log.d("StatusServiceReceiver", "Scenario execution time: " + new Date().toString());
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
