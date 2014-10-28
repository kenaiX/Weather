package cc.kenai.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.kenai.function.message.XLog;

import cc.kenai.weather.event.UpdateWeatherIfNeed;
import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;

/**
 * Created by kenai on 13-12-9.
 */
public class UpdateReceiver extends BroadcastReceiver {
    private final String Tag = "WeatherStatebar";

    @Override
    @DebugLog
    public void onReceive(final Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(BROADCAST_UPDATE)) {
                EventBus.getDefault().post(new UpdateWeatherIfNeed());
            }
        }

        if (intent != null && intent.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            EventBus.getDefault().post(new UpdateWeatherIfNeed());
        }
    }


    public final static String BROADCAST_UPDATE = "cc.kenai.weather.MainXService.update";

    public static void sendUpdateBroadcast(Context context) {
        XLog.xLog("send to start update weather");
        AlarmManager xAlarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(BROADCAST_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        xAlarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
    }

    public static void cancelUpdateBroadcast(Context context) {
        XLog.xLog("send to cancel update weather");
        AlarmManager xAlarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(BROADCAST_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        xAlarmManager.cancel(pendingIntent);
    }
}
