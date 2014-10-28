package cc.kenai.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import cc.kenai.weather.event.FetchWeatherIfNeed;
import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;

public class UpdateReceiver extends BroadcastReceiver {

    @Override
    @DebugLog
    public void onReceive(final Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(MainBroadcastEvent.BROADCAST_UPDATE)) {
            EventBus.getDefault().post(new FetchWeatherIfNeed());
        } else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            EventBus.getDefault().post(new FetchWeatherIfNeed());
        }
    }

    @DebugLog
    public static void sendUpdateBroadcast(Context context) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainBroadcastEvent.BROADCAST_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
    }

    @DebugLog
    public static void cancelUpdateBroadcast(Context context) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainBroadcastEvent.BROADCAST_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pendingIntent);
    }
}
